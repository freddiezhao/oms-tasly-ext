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
package tasly.greathealth.oms.web.delivery.soap.resources.impl;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.erp.api.inventory.dto.Baseinfo;
import tasly.greathealth.erp.api.order.UpdateOrderDeliveryStatusFacade;
import tasly.greathealth.erp.api.order.updatedelivery.dto.Message;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.web.delivery.soap.resources.TaslyOmsDeliveryStatusUpdateService;


/**
 * ERP发货状态同步更新Hybris OMS订单(TS-688)
 * vincent 2015-01-06
 */
@WebService(endpointInterface = "tasly.greathealth.oms.web.delivery.soap.resources.TaslyOmsDeliveryStatusUpdateService")
public class TaslyOmsDeliveryStatusUpdateServiceImpl implements TaslyOmsDeliveryStatusUpdateService
{

	private static final Logger LOG = OmsLoggerFactory.getErporderlog();

	private UpdateOrderDeliveryStatusFacade orderDeliveryStatusUpdateFacade;

	/**
	 * ECC order delivery number and delivery company updated into OMS
	 */
	@Override
	@WebMethod
	@Oneway
	public void updateOrderStatus4Delivery(final Baseinfo baseinfo, final Message eccOrderDelivery)
	{
		LOG.info("开始处理ECC订单物流号...");
		final long beginTime = System.currentTimeMillis();

		if (null != eccOrderDelivery && null != eccOrderDelivery.getEccOrderDeliveries()
				&& eccOrderDelivery.getEccOrderDeliveries().size() > 0)
		{
			LOG.info("处理的记录数量为：" + eccOrderDelivery.getEccOrderDeliveries().size());

			orderDeliveryStatusUpdateFacade.updateOrderStatus4Delivery(eccOrderDelivery);

			LOG.info("处理ECC订单物流号结束，耗时:" + (System.currentTimeMillis() - beginTime) / 1000f + " 秒 ");

		}

	}

	@Required
	public void setOrderDeliveryStatusUpdateFacade(final UpdateOrderDeliveryStatusFacade orderDeliveryStatusUpdateFacade)
	{
		this.orderDeliveryStatusUpdateFacade = orderDeliveryStatusUpdateFacade;
	}


}
