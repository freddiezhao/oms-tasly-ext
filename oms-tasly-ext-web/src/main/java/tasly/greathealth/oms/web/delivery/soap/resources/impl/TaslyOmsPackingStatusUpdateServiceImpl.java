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

import java.util.ArrayList;
import java.util.List;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.erp.api.inventory.dto.Baseinfo;
import tasly.greathealth.erp.api.order.UpdateOrderDeliveryStatusFacade;
import tasly.greathealth.erp.api.order.updatepacking.dto.Message;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.web.delivery.soap.resources.TaslyOmsPackingStatusUpdateService;


/**
 * ERP发货状态同步更新Hybris OMS订单(TS-688)
 * vincent 2015-01-06
 */
@WebService(endpointInterface = "tasly.greathealth.oms.web.delivery.soap.resources.TaslyOmsPackingStatusUpdateService")
public class TaslyOmsPackingStatusUpdateServiceImpl implements TaslyOmsPackingStatusUpdateService
{
	private static final Logger LOG = OmsLoggerFactory.getErporderlog();

	private UpdateOrderDeliveryStatusFacade orderDeliveryStatusUpdateFacade;

	/**
	 * ECC order packing status updated into OMS
	 *
	 * Packing status
	 * Return nothing
	 *
	 * @param baseinfo
	 * @param packingMessage
	 *
	 */

	@Override
	@WebMethod
	@Oneway
	public void updateOrderStatus4Packing(final Baseinfo baseinfo, final Message packingMessage)
	{
		LOG.info("开始处理ECC订单波次状态更新...");
		final long beginTime = System.currentTimeMillis();
		final List<String> omsOrderIds = new ArrayList<String>();
		List<String> packingFailedOrders = new ArrayList<String>();
		// put all of these packing order id into a list;
		if (null != packingMessage && null != packingMessage.getOmsOrderIds())
		{
			for (final String omsOrderId : packingMessage.getOmsOrderIds())
			{
				omsOrderIds.add(omsOrderId);
			}
		}
		LOG.info("需处理订单数量为： " + omsOrderIds.size());

		// begin process all of these packing order status
		if (omsOrderIds.size() > 0)
		{
			try
			{
				packingFailedOrders = orderDeliveryStatusUpdateFacade.updateOrderStatus4Packing(omsOrderIds);

				LOG.info("处理订单波次结束，耗时:" + (System.currentTimeMillis() - beginTime) / 1000f + " 秒 ");
				if (null != packingFailedOrders && packingFailedOrders.size() > 0)
				{
					LOG.error("未处理成功数量：" + packingFailedOrders.size() + "，订单号为：");
					for (final String id : packingFailedOrders)
					{
						LOG.error(id);
					}
				}
			}
			catch (final Exception e)
			{
				LOG.error("处理订单波次失败!");
				LOG.error(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	@Required
	public void setOrderDeliveryStatusUpdateFacade(final UpdateOrderDeliveryStatusFacade orderDeliveryStatusUpdateFacade)
	{
		this.orderDeliveryStatusUpdateFacade = orderDeliveryStatusUpdateFacade;
	}

}
