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
public class CreateJdRefundCloseCommand extends AbstractOrderCommand
{

	/**
	 *
	 */
	private final OrderSearchInfo orderInfo;
	private final OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieveService;

	public CreateJdRefundCloseCommand(final OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieveService,
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

		if (WAIT_SELLER_SEND_GOODS.equalsIgnoreCase(currentState))
		{
			// 未发货 未发货订单，取消订单锁定状态。所有行项目置"退款退货关闭"
			omsOrderRetrieveService.closeRefund(jingdongTid, jingdongOid, false);
		}
		else if (WAIT_BUYER_CONFIRM_GOODS.equalsIgnoreCase(currentState))
		{
			// 已发货 退款退货关闭
			omsOrderRetrieveService.closeRefund(jingdongTid, jingdongOid, true);
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
