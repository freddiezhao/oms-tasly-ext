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
package tasly.greathealth.tmall.order.commands;

import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.order.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.api.order.dto.ChannelSource;
import tasly.greathealth.oms.domain.order.EventType;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.OrderState;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;
import tasly.greathealth.tmall.order.services.impl.OrderContext;

import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;

import flexjson.JSONDeserializer;


/**
 *
 */
public class CreateTmallOrderCommand extends AbstractTmallOrderCommand
{
	public CreateTmallOrderCommand(final OmsOrderRetrieveService<Trade> omsOrderRetrieveService, final Message message,
			final InnerSource innerSource)
	{
		super(omsOrderRetrieveService, message, innerSource);
	}

	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	@Override
	public void execute()
	{
		final String tid = getTmallTidFromMsg();
		final String oid = getTmallOidFromMsg();
		final Date eventTime = getTmallEventtimeFromMsg();
		Trade trade = null;
		try
		{
			trade = omsOrderRetrieveService.retrieveOrderDetail(tid);
			omsOrderRetrieveService.filterOutUnpayedLine(trade);
			final OrderContext orderContext = new OrderContext();
			orderContext.addProperty(DEFAULT_INNER_SOURCE_KEY, this.getInnerSource());
			orderContext.addProperty(DEFAULT_CHANNEL_SOURCE_KEY, ChannelSource.TMALL);
			final Trade splittedTrade = omsOrderRetrieveService.splitOrder(trade, orderContext);
			final Order order = omsOrderRetrieveService.transformThirdPartyOrder2OmsOrder(splittedTrade, orderContext);
			omsOrderRetrieveService.saveOrder(order);
			if (omsOrderRetrieveService.isWriteBack2Tamll())
			{
				omsOrderRetrieveService.updateSellerMemo(Long.valueOf(tid), trade.getSellerMemo(), "订单受理成功", true);
			}
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
			handleException(trade, tid, oid, eventTime, e);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			handleException(trade, tid, oid, eventTime, e);
		}
	}


	private void handleException(final Trade trade, final String tid, final String oid, final Date eventTime, final Exception e)
	{
		if (omsOrderRetrieveService.isWriteBack2Tamll())
		{
			omsOrderRetrieveService.updateSellerMemo(Long.parseLong(tid), trade == null ? null : trade.getSellerMemo(), "订单受理中",
					false);
		}
		omsOrderRetrieveService.saveFailedOrder(tid, oid, null, EventType.ORDERCREATE, e.getMessage(), OrderState.FAIL, null,
				eventTime, this.getChannelSource(), this.getInnerSource());
		throw new RuntimeException("Error happened when save order. " + this.getInnerSource() + " TID[" + tid + "] ERROR["
				+ e.getMessage() + "]");
	}


	@Override
	public tasly.greathealth.thirdparty.order.EventType getEventType()
	{
		return tasly.greathealth.thirdparty.order.EventType.ORDERCREATE;
	}

}
