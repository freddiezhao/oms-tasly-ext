/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.order.facades.impl;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.exception.EntityValidationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.order.PendingOrderFacade;
import tasly.greathealth.oms.api.order.PendingOrderQueryObject;
import tasly.greathealth.oms.api.order.dto.PendingOrder;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.PendingOrderData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.services.PendingOrderService;
import tasly.greathealth.thirdparty.order.EventType;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandFactory;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;

import flexjson.JSONSerializer;


/**
 * @author Henter Liu
 */
public class DefaultPendingOrderFacade implements PendingOrderFacade, OrderConstants
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	private Converter<PendingOrderData, PendingOrder> pendingOrderConverter;
	private Converters converters;
	private PendingOrderService pendingOrderService;
	private OmsOrderRetrieveService<Trade> omsOrderRetrieverService;

	/**
	 * @param pendingOrderService the pendingOrderService to set
	 */
	public void setPendingOrderService(final PendingOrderService pendingOrderService)
	{
		this.pendingOrderService = pendingOrderService;
	}

	/**
	 * @return the taslyOrderConverter
	 */
	protected Converter<PendingOrderData, PendingOrder> getPendingOrderConverter()
	{
		return pendingOrderConverter;
	}

	/**
	 * @param pendingOrderConverter the pendingOrderConverter to set
	 */
	@Required
	public void setPendingOrderConverter(final Converter<PendingOrderData, PendingOrder> pendingOrderConverter)
	{
		this.pendingOrderConverter = pendingOrderConverter;
	}

	protected Converters getConverters()
	{
		return this.converters;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Override
	@Transactional
	public Pageable<PendingOrder> findPendingOrdersByQuery(final PendingOrderQueryObject queryObject)
			throws EntityValidationException
			{
		LOG.trace("findPendingOrdersByQuery");
		final Page<PendingOrderData> pendingOrderDatas = this.pendingOrderService.findPagedPendingOrdersByQuery(queryObject);
		final List<PendingOrder> pendingOrders = this.converters.convertAll(pendingOrderDatas.getContent(),
				this.pendingOrderConverter);
		final PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNumber(pendingOrderDatas.getNumber());
		pageInfo.setTotalPages(pendingOrderDatas.getTotalPages());
		pageInfo.setTotalResults(pendingOrderDatas.getTotalElements());
		final PagedResults<PendingOrder> results = new PagedResults<PendingOrder>(pendingOrders, pageInfo);
		return results;
			}

	/**
	 * recreate failed orders for eventtype in EventType
	 *
	 * @param tid 天猫订单号，oid 天猫行项目号 ，eventType EventType
	 * @author vincent.yin
	 */

	@Override
	public void restorePendingOrders(final String tid)
	{
		LOG.info("开始恢复pendingOrders, tid= " + tid);
		final List<PendingOrderData> pendingOrderDataList = omsOrderRetrieverService.loadPendingOrdersByTID(tid);

		final List<PendingOrder> pendingOrders = this.converters.convertAll(pendingOrderDataList, this.pendingOrderConverter);

		final List<PendingOrder> pendingOrderOrderCreateList = new ArrayList<PendingOrder>();

		final List<PendingOrder> pendingOrderOtherList = new ArrayList<PendingOrder>();

		// EventType.ORDERCREATE should be first processed.

		for (final PendingOrder pendingOrder : pendingOrders)
		{

			if (pendingOrder.getEventType().toString().equals(EventType.ORDERCREATE.toString()))
			{
				pendingOrderOrderCreateList.add(pendingOrder);
			}
			// now only process eventype= REFUNDCREATE
			else if (pendingOrder.getEventType().toString().equals(EventType.REFUNDCREATE.toString()))
			{
				pendingOrderOtherList.add(pendingOrder);
			}

		}


		// fist process EventType.ORDERCREATE
		for (final PendingOrder pendingOrder : pendingOrderOrderCreateList)
		{
			LOG.info("开始处理 EventType.ORDERCREATE 的 PendingOrders, oid = " + pendingOrder.getOid() + " ,eventType is ,"
					+ pendingOrder.getEventType().toString() + ",refund_fee is " + pendingOrder.getRefundFee());

			// first rest the stat from fail to success.

			omsOrderRetrieverService.saveSuccessPendingOrder(tid, pendingOrder.getOid(), pendingOrder.getEventType().toString());

			restorePendingOrders(pendingOrder);
		}

		// wait a little minutes

		try
		{
			Thread.sleep(5000);
		}
		catch (final InterruptedException e)
		{
			LOG.warn(e.getMessage(), e);
		}

		// then others

		for (final PendingOrder pendingOrder : pendingOrderOtherList)
		{
			LOG.info("开始处理其他的PendingOrders, oid = " + pendingOrder.getOid() + " ,eventType is ,"
					+ pendingOrder.getEventType().toString() + ",refund_fee is " + pendingOrder.getRefundFee());

			// first rest the stat from fail to success.

			omsOrderRetrieverService.saveSuccessPendingOrder(tid, pendingOrder.getOid(), pendingOrder.getEventType().toString());

			restorePendingOrders(pendingOrder);
		}
	}

	/**
	 * put this order into queue according to different event type
	 *
	 * @param pendingOrder
	 * @author vincent.yin
	 */
	public void restorePendingOrders(final PendingOrder pendingOrder)
	{
		final Message message = new Message();
		final String eventType = pendingOrder.getEventType().toString();
		if (EnumUtils.isValidEnum(EventType.class, eventType) == false)
		{
			LOG.error("Not a valid event type for pending order." + eventType);
			return;
		}
		final Map<String, Object> rawMap = new HashMap<String, Object>();
		final Map<String, Object> contentMap = new HashMap<String, Object>();
		contentMap.put("tid", pendingOrder.getTid());
		contentMap.put("oid", pendingOrder.getOid());
		contentMap.put("type", eventType);



		if (EventType.REFUNDCREATE.equals(pendingOrder.getEventType())
				|| EventType.REFUNDSUCCESS.toString().equalsIgnoreCase(eventType)
				|| EventType.REFUNDCLOSE.toString().equalsIgnoreCase(eventType))
		{
			contentMap.put("refund_fee", pendingOrder.getRefundFee());
		}

		final String contentMapJson = new JSONSerializer().deepSerialize(contentMap);
		rawMap.put("content", contentMapJson);
		rawMap.put("time", String.valueOf(new Date()));
		setRawMsg(message, rawMap);

		LOG.info("------> " + message.getRaw());
		message.setUserId(911757567L);
		message.setTopic(eventType);

		final OrderCommand command;

		if (pendingOrder.getInnerSource().toString().equals(TMALL_INNER_OTC))
		{

			command = OrderCommandFactory.createTmallOrderCommand(EventType.valueOf(eventType), omsOrderRetrieverService, message,
					InnerSource.OTC);
		}
		else
		{
			command = OrderCommandFactory.createTmallOrderCommand(EventType.valueOf(eventType), omsOrderRetrieverService, message,
					InnerSource.JSC);

		}
		command.execute();
	}

	/**
	 * set para into msg.
	 *
	 * @param msg
	 * @param para
	 */
	private void setRawMsg(final Message msg, final Map<String, Object> para)
	{
		final Method[] method = Message.class.getDeclaredMethods();
		for (int i = method.length - 1; i >= 0; i--)
		{
			if ("setRaw".equalsIgnoreCase(method[i].getName()))
			{
				method[i].setAccessible(true);
				try
				{
					method[i].invoke(msg, para);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					LOG.error(e.getMessage(), e);
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService<Trade> omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}

	@Override
	public void restorePendingOrdersForTmallJSC(final String tid)
	{
		this.restorePendingOrders(tid);
	}
}
