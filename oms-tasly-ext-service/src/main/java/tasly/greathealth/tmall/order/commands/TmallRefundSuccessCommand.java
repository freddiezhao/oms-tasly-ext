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
public class TmallRefundSuccessCommand extends AbstractTmallOrderCommand
{

	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	public TmallRefundSuccessCommand(final OmsOrderRetrieveService<Trade> omsOrderRetrieveService, final Message message,
			final InnerSource innerSource)
	{
		super(omsOrderRetrieveService, message, innerSource);
	}

	@Override
	public void execute()
	{
		LOG.info("TmallRefundSuccessCommand#####" + this.getInnerSource());
		final String tmallTid = this.getTmallTidFromMsg();
		final String tmallOid = this.getTmallOidFromMsg();
		final String refundFee = this.getTmallRefundFeeFromMsg();
		final Date eventTime = getTmallEventtimeFromMsg();
		Trade trade;
		try
		{
			trade = omsOrderRetrieveService.retrieveOrderDetail(tmallTid);
			final String currentState = getLineLevelState(trade, tmallOid);
			LOG.info("InnerSource: " + this.getInnerSource() + "TID : " + tmallTid + " Current State : " + currentState
					+ " Refund Fee : " + refundFee);
			if (WAIT_SELLER_SEND_GOODS.equalsIgnoreCase(currentState) || TRADE_CLOSED.equalsIgnoreCase(currentState))
			{
				// 未发货退款
				// 需要释放该行项目占用库存，该行项目置“退货退款成功”，并且保存退款金额。
				omsOrderRetrieveService.successCompleteRefund(tmallTid, tmallOid, refundFee, false);
			}
			else if (WAIT_BUYER_CONFIRM_GOODS.equalsIgnoreCase(currentState) || TRADE_FINISHED.equalsIgnoreCase(currentState))
			{
				// 已发货退款
				// 更新该行项目状态置为“退款退货完成”，为相应行项目同步退款金额
				omsOrderRetrieveService.successCompleteRefund(tmallTid, tmallOid, refundFee, true);
			}
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			omsOrderRetrieveService.saveFailedOrder(tmallTid, tmallOid, refundFee, EventType.REFUNDSUCCESS, e.getMessage(),
					OrderState.FAIL, null, eventTime, this.getChannelSource(), this.getInnerSource());
			throw new RuntimeException("Error happened when handle refund success event. InnerSource:" + this.getInnerSource()
					+ " TID[" + tmallTid + "] ERROR[" + e.getMessage() + "]");
		}
	}

	@Override
	public tasly.greathealth.thirdparty.order.EventType getEventType()
	{
		return tasly.greathealth.thirdparty.order.EventType.REFUNDSUCCESS;
	}
}
