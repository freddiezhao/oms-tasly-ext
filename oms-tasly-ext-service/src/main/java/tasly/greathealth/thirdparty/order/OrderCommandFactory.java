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
package tasly.greathealth.thirdparty.order;

import org.slf4j.Logger;

import tasly.greathealth.jd.order.commands.CreateJdOrderCommand;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.order.commands.CreateTmallOrderCommand;
import tasly.greathealth.tmall.order.commands.CreateTmallRefundCommand;
import tasly.greathealth.tmall.order.commands.CreateTmallSellerShippingCommand;
import tasly.greathealth.tmall.order.commands.TmallLogisticsAddressChangedCommand;
import tasly.greathealth.tmall.order.commands.TmallMemoModifiedCommand;
import tasly.greathealth.tmall.order.commands.TmallRefundCloseCommand;
import tasly.greathealth.tmall.order.commands.TmallRefundSuccessCommand;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;
import tasly.greathealth.yhd.order.commands.CreateYhdOrderCommand;

import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;


/**
 *
 */
public class OrderCommandFactory
{
	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	public static OrderCommand createTmallOrderCommand(final EventType eventType, final OmsOrderRetrieveService<Trade> service,
			final Message message, final InnerSource innerSource)
	{
		OrderCommand result = null;
		switch (eventType)
		{
			case ORDERCREATE:
				result = new CreateTmallOrderCommand(service, message, innerSource);
				break;
			case REFUNDCREATE:
				result = new CreateTmallRefundCommand(service, message, innerSource);
				break;
			case REFUNDSUCCESS:
				result = new TmallRefundSuccessCommand(service, message, innerSource);
				break;
			case REFUNDCLOSE:
				result = new TmallRefundCloseCommand(service, message, innerSource);
				break;
			case SELLERSHIP:
				result = new CreateTmallSellerShippingCommand(service, message, innerSource);
				break;
			default:
				LOG.info("unknow event type for create commands.");
				break;
		}
		return result;
	}

	public static OrderCommand createTmallOrderCommand(final OmsOrderRetrieveService<Trade> omsOrderRetrieverService,
			final Message message, final InnerSource innerSource)
	{
		OrderCommand result = null;
		final String topic = message.getTopic();
		if ("taobao_trade_TradeBuyerPay".equalsIgnoreCase(topic))
		{
			result = new CreateTmallOrderCommand(omsOrderRetrieverService, message, innerSource);
		}
		else if ("taobao_refund_RefundCreated".equalsIgnoreCase(topic))
		{
			result = new CreateTmallRefundCommand(omsOrderRetrieverService, message, innerSource);
		}
		else if ("taobao_refund_RefundSuccess".equalsIgnoreCase(topic))
		{
			result = new TmallRefundSuccessCommand(omsOrderRetrieverService, message, innerSource);
		}
		else if ("taobao_refund_RefundClosed".equalsIgnoreCase(topic))
		{
			result = new TmallRefundCloseCommand(omsOrderRetrieverService, message, innerSource);
		}
		else if ("taobao_trade_TradeSellerShip".equalsIgnoreCase(topic))
		{
			result = new CreateTmallSellerShippingCommand(omsOrderRetrieverService, message, innerSource);
		}
		else if ("taobao_trade_TradeMemoModified".equalsIgnoreCase(topic))
		{
			result = new TmallMemoModifiedCommand(omsOrderRetrieverService, message, innerSource);
		}
		else if ("taobao_trade_TradeLogisticsAddressChanged".equalsIgnoreCase(topic))
		{
			result = new TmallLogisticsAddressChangedCommand(omsOrderRetrieverService, message, innerSource);
		}
		else
		{
			LOG.info("unknow event type. Raw message [" + message.getRaw().toString() + "]");
		}
		return result;
	}

	/**
	 * JD
	 *
	 * @param omsOrderRetrieverService
	 * @param eventType
	 * @param orderInfo
	 * @param innerSource
	 * @return
	 */

	public static OrderCommand createJdOrderCommand(final OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieverService,
			final EventType eventType, final OrderSearchInfo orderInfo, final InnerSource innerSource)
	{
		OrderCommand result = null;
		switch (eventType)
		{
			case ORDERCREATE:
				result = new CreateJdOrderCommand(omsOrderRetrieverService, orderInfo, innerSource);
				break;
		}
		return result;
	}

	/**
	 * YHD
	 *
	 * @param omsOrderRetrieverService
	 * @param eventType
	 * @param yhdOrder
	 * @param innerSource
	 * @return
	 */
	public static OrderCommand createYhdOrderCommand(
			final OmsOrderRetrieveService<com.yhd.object.trade.Trade> omsOrderRetrieverService, final EventType eventType,
			final com.yhd.object.trade.Trade yhdOrder, final InnerSource innerSource)
	{
		OrderCommand result = null;
		switch (eventType)
		{
			case ORDERCREATE:
				result = new CreateYhdOrderCommand(omsOrderRetrieverService, yhdOrder, innerSource);
				break;
		}
		return result;
	}
}
