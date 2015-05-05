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

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.domain.order.EventType;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.OrderState;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;


/**
 *
 */
public class TmallMemoModifiedCommand extends AbstractTmallOrderCommand
{

	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	private static final String REGEX = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}---订单受理\\D{1,2}";

	public TmallMemoModifiedCommand(final OmsOrderRetrieveService<Trade> omsOrderRetrieveService, final Message message,
			final InnerSource innerSource)
	{
		super(omsOrderRetrieveService, message, innerSource);
	}

	@Override
	public void execute()
	{
		LOG.info("######TmallMemoModifiedCommand###" + this.getInnerSource());

		final String tid = getTmallTidFromMsg();
		final String oid = getTmallOidFromMsg();
		final Date eventTime = getTmallEventtimeFromMsg();
		Trade trade = null;
		try
		{
			trade = omsOrderRetrieveService.retrieveOrderDetail(tid);
			if (trade != null)
			{
				if (WAIT_BUYER_PAY.equalsIgnoreCase(trade.getStatus()) || TRADE_CLOSED_BY_TAOBAO.equalsIgnoreCase(trade.getStatus()))
				{
					LOG.info("该订单为未付款订单或在未付款之前已被关闭[" + tid + "]。");
					return;
				}
				final String sellerMemo = trade.getSellerMemo();
				LOG.info("原始的客服备注为: " + sellerMemo);
				if (sellerMemo == null)
				{
					LOG.info("客服备注为空。[" + tid + "]。");
					return;
				}
				final String filteredMemo = sellerMemo.replaceAll(REGEX, "").replaceAll("\r", "").replaceAll("\n", "");
				LOG.info("过滤后的客服备注为: " + filteredMemo);
				if (StringUtils.isBlank(filteredMemo))
				{
					LOG.info("过滤OMS上传备注内容后，客服备注为空。[" + tid + "]。");
					return;
				}
				omsOrderRetrieveService.updateSellerMemo(tid, filteredMemo);
			}
			else
			{
				LOG.info("天猫API没有搜索到该订单[" + tid + "]。" + this.getInnerSource());
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			omsOrderRetrieveService.saveFailedOrder(tid, oid, null, EventType.UPDATEORDERMEMO, e.getMessage(), OrderState.FAIL,
					null, eventTime, this.getChannelSource(), this.getInnerSource());
			throw new RuntimeException(e);
		}
	}

	@Override
	public tasly.greathealth.thirdparty.order.EventType getEventType()
	{
		return tasly.greathealth.thirdparty.order.EventType.UPDATEORDERMEMO;
	}
}
