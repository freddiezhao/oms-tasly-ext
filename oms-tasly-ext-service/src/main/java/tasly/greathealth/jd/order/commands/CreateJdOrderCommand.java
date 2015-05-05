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
package tasly.greathealth.jd.order.commands;

import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.order.Order;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.EventType;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.OrderState;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.common.TaslyThirdUtils;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;
import tasly.greathealth.tmall.order.services.impl.OrderContext;

import com.jd.open.api.sdk.domain.order.OrderSearchInfo;

import flexjson.JSONDeserializer;


/**
 *
 * @author libin
 */

public class CreateJdOrderCommand extends AbstractOrderCommand
{
	private static final Logger LOG = OmsLoggerFactory.getJdorderlog();

	private final OrderSearchInfo orderInfo;
	private final OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieveService;

	public CreateJdOrderCommand(final OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieveService,
			final OrderSearchInfo orderInfo, final InnerSource innerSource)
	{
		super(innerSource);
		this.omsOrderRetrieveService = omsOrderRetrieveService;
		this.orderInfo = orderInfo;
	}

	@Override
	public String getTid()
	{
		// YTODO Auto-generated method stub
		return orderInfo.getOrderId();
	}

	@Override
	public String getOid()
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public String getContent()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public ChannelSource getChannelSource()
	{
		return ChannelSource.JD;
	}

	/**
	 * @return the orderInfo
	 */
	public OrderSearchInfo getOrderInfo()
	{
		return orderInfo;
	}

	/**
	 * @return the omsOrderRetrieveService
	 */
	public OmsOrderRetrieveService<OrderSearchInfo> getOmsOrderRetrieveService()
	{
		return omsOrderRetrieveService;
	}

	@Override
	@Transactional
	public void execute()
	{
		LOG.info("execute JD order create.");
		final OrderContext orderContext = new OrderContext();
		// TODU:需要增加JD渠道
		orderContext.addProperty(DEFAULT_CHANNEL_SOURCE_KEY, ChannelSource.JD);
		orderContext.addProperty(DEFAULT_INNER_SOURCE_KEY, InnerSource.JSC);
		try
		{
			final OrderSearchInfo splittedOrder = omsOrderRetrieveService.splitOrder(orderInfo, orderContext);
			final Order omsOrder = omsOrderRetrieveService.transformThirdPartyOrder2OmsOrder(splittedOrder, orderContext);
			LOG.info("订单ID：" + omsOrder.getOrderId());
			omsOrderRetrieveService.saveOrder(omsOrder);

			// 更新京东订单小旗-成功
			// omsOrderRetrieveService.updateSellerMemo(orderInfo.getOrderId(), orderInfo == null ? null :
			// orderInfo.getVenderRemark(),
			// "订单受理成功", true);
		}
		catch (final EntityValidationException e)
		{
			final String detailMsg = e.getMessage();
			final List<Map<String, String>> exceptionList = new JSONDeserializer<List<Map<String, String>>>().deserialize(detailMsg);
			if (CollectionUtils.isNotEmpty(exceptionList) && exceptionList.get(0) != null)
			{
				final String exceptionMsg = exceptionList.get(0).get("compare");
				if (exceptionMsg != null && "original order id exists!".equalsIgnoreCase(StringUtils.trim(exceptionMsg)))
				{
					throw e;
				}
			}

			handleException(orderInfo, e);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			handleException(orderInfo, e);
		}
	}

	private void handleException(final OrderSearchInfo orderInfo, final Exception e)
	{
		final String tid = orderInfo.getOrderId();
		// 下单时间
		final String order_start_time = orderInfo.getOrderStartTime();
		// 更新京东订单小旗-失败
		// omsOrderRetrieveService.updateSellerMemo(tid, orderInfo == null ? null : orderInfo.getVenderRemark(), "订单受理中",
		// false);

		// 建立pendingorder
		omsOrderRetrieveService.saveFailedOrder(tid, tid, null, EventType.ORDERCREATE, e.getMessage(), OrderState.FAIL, null,
				TaslyThirdUtils.getDatebyStr(order_start_time), this.getChannelSource(), this.getInnerSource());
		throw new RuntimeException("Error happened when save order. " + this.getInnerSource() + " TID[" + tid + "] ERROR["
				+ e.getMessage() + "]");
	}

	@Override
	public tasly.greathealth.thirdparty.order.EventType getEventType()
	{
		return tasly.greathealth.thirdparty.order.EventType.ORDERCREATE;
	}

}
