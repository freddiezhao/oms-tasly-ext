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
package tasly.greathealth.yhd.order.commands;

import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.order.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.jd.order.commands.AbstractOrderCommand;
import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.EventType;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.OrderState;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.common.TaslyThirdUtils;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;
import tasly.greathealth.tmall.order.services.impl.OrderContext;

import com.yhd.object.trade.Trade;

import flexjson.JSONDeserializer;


/**
 *
 */
public class CreateYhdOrderCommand extends AbstractOrderCommand
{
	private static final Logger LOG = OmsLoggerFactory.getYhdorderlog();

	private final Trade yhdOrder;
	private final OmsOrderRetrieveService<Trade> omsOrderRetrieveService;

	public CreateYhdOrderCommand(final OmsOrderRetrieveService<Trade> omsOrderRetrieveService, final Trade yhdOrder,
			final InnerSource innerSource)
	{
		super(innerSource);
		this.omsOrderRetrieveService = omsOrderRetrieveService;
		this.yhdOrder = yhdOrder;
	}

	/**
	 * @return the yhdOrder
	 */
	public Trade getYhdOrder()
	{
		return yhdOrder;
	}

	/**
	 * @return the omsOrderRetrieveService
	 */
	public OmsOrderRetrieveService<Trade> getOmsOrderRetrieveService()
	{
		return omsOrderRetrieveService;
	}


	@Override
	public String getTid()
	{
		return yhdOrder.getTid().toString();
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
	@Transactional
	public void execute()
	{
		LOG.info("execute YHD order create.");
		final String tid = this.getTid();
		// 交易创建时间
		final Date eventTime = TaslyThirdUtils.getDatebyStr(yhdOrder.getCreated());

		final OrderContext orderContext = new OrderContext();
		// TODU:需要增加YHD渠道
		orderContext.addProperty(DEFAULT_CHANNEL_SOURCE_KEY, ChannelSource.YHD);
		orderContext.addProperty(DEFAULT_INNER_SOURCE_KEY, innerSource.name());
		try
		{
			final Trade splittedOrder = omsOrderRetrieveService.splitOrder(yhdOrder, orderContext);
			final Order omsOrder = omsOrderRetrieveService.transformThirdPartyOrder2OmsOrder(splittedOrder, orderContext);
			LOG.info("订单ID：" + omsOrder.getOrderId());
			omsOrderRetrieveService.saveOrder(omsOrder);

			// 更新yhd订单小旗-成功
			// omsOrderRetrieveService.updateSellerMemo(tid, yhdOrder.getSeller_memo(), "订单受理成功", true);
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
			handleException(yhdOrder, tid, tid, eventTime, e);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			handleException(yhdOrder, tid, tid, eventTime, e);
		}
	}

	private void handleException(final Trade trade, final String tid, final String oid, final Date eventTime, final Exception e)
	{
		// 更新yhd订单小旗-失败
		// omsOrderRetrieveService
		// .updateSellerMemo(Long.parseLong(tid), trade == null ? null : trade.getSeller_memo(), "订单受理中", false);
		omsOrderRetrieveService.saveFailedOrder(tid, oid, null, EventType.ORDERCREATE, e.getMessage(), OrderState.FAIL, null,
				eventTime, this.getChannelSource(), this.getInnerSource());
		throw new RuntimeException("Error happened when save order. " + this.getInnerSource() + " TID[" + tid + "] ERROR["
				+ e.getMessage() + "]");
	}

	@Override
	public ChannelSource getChannelSource()
	{
		return ChannelSource.YHD;
	}

	@Override
	public tasly.greathealth.thirdparty.order.EventType getEventType()
	{
		return tasly.greathealth.thirdparty.order.EventType.ORDERCREATE;
	}

}
