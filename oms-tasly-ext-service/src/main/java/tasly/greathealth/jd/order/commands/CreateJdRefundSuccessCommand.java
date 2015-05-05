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

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.thirdparty.order.EventType;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.jd.open.api.sdk.domain.order.OrderSearchInfo;


/**
 *
 * @author libin
 */
public class CreateJdRefundSuccessCommand extends AbstractOrderCommand
{

	/**
	 *
	 */
	private final OrderSearchInfo orderInfo;
	private final OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieveService;

	public CreateJdRefundSuccessCommand(final OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieveService,
			final OrderSearchInfo orderInfo, final InnerSource innerSource)
	{
		super(innerSource);
		this.omsOrderRetrieveService = omsOrderRetrieveService;
		this.orderInfo = orderInfo;

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
	public void execute()
	{
		final String jingdongTid = orderInfo.getOrderId();
		final String jingdongOid = "test";// orderInfo.getItemInfoList();
		final String currentState = orderInfo.getOrderState();
		// TODO: get refund fee from QueryMap

		final String refundFee = orderInfo.getReturnOrder();
		if (WAIT_SELLER_SEND_GOODS.equalsIgnoreCase(currentState) || TRADE_CLOSED.equalsIgnoreCase(currentState))
		{
			// 未发货退款
			// 需要释放该行项目占用库存，该行项目置“退货退款成功”，并且保存退款金额。
			omsOrderRetrieveService.successCompleteRefund(jingdongTid, jingdongOid, refundFee, false);
		}
		else if (WAIT_BUYER_CONFIRM_GOODS.equalsIgnoreCase(currentState) || TRADE_FINISHED.equalsIgnoreCase(currentState))
		{
			// 已发货退款
			// 更新该行项目状态置为“退款退货完成”，为相应行项目同步退款金额
			omsOrderRetrieveService.successCompleteRefund(jingdongTid, jingdongOid, refundFee, true);
		}
	}

	@Override
	public EventType getEventType()
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public String getTid()
	{
		// YTODO Auto-generated method stub
		return null;
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

}
