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
package tasly.greathealth.oms.web.orderstatus.soap.resources.impl;

import java.util.List;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.slf4j.Logger;

import tasly.greathealth.oms.api.order.TaslyOrderFacade;
import tasly.greathealth.oms.api.orderstatus.dto.Baseinfo;
import tasly.greathealth.oms.api.orderstatus.dto.Message;
import tasly.greathealth.oms.api.orderstatus.dto.Oms_order;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.web.orderstatus.soap.resources.EccOmsOrderCallbackService;


/**
 * @description ECC通过PI向OMS同步订单创建和修改结果 TS-689[TS-752]
 * @author LIANG
 * @date 2015-01-10
 */
@WebService(endpointInterface = "tasly.greathealth.oms.web.orderstatus.soap.resources.EccOmsOrderCallbackService")
public class EccOmsOrderCallbackServiceImpl implements EccOmsOrderCallbackService
{

	TaslyOrderFacade taslyOrderFacade;

	public void setTaslyOrderFacade(final TaslyOrderFacade taslyOrderFacade)
	{
		this.taslyOrderFacade = taslyOrderFacade;
	}

	private static final Logger erpOrderLog = OmsLoggerFactory.getErporderlog();

	/**
	 * 01: 新建订单
	 * 02: 锁定
	 * 03: 修改
	 * 04: 解锁
	 */
	@Override
	@WebMethod
	@Oneway
	public void orderCallback(final Baseinfo baseinfo, final Message message)
	{
		final List<Oms_order> order_list = message.getOms_orders().get(0).getOms_order();

		final String log_baseinfo = "MSGID: " + baseinfo.getMSGID() + "\n" + "PMSGID: " + baseinfo.getPMSGID() + "\n"
				+ "SENDTIME: " + baseinfo.getSENDTIME() + "\n" + "SERVICENAME: " + baseinfo.getSERVICENAME() + "\n" + "S_SYSTEM: "
				+ baseinfo.getS_SYSTEM() + "\n" + "T_SYSTEM: " + baseinfo.getT_SYSTEM();


		if (null != message.getOms_orders() && order_list.size() > 0)
		{
			final String order_size = "共有" + message.getOms_orders().get(0).getOms_order().size() + "个订单。";

			erpOrderLog.debug(log_baseinfo + "\n" + order_size);

			for (final Oms_order oms_order : order_list)
			{
				final String oms_order_id = oms_order.getOMS_ORDER_ID();
				final String oms_order_status = oms_order.getRESULT_CODE();
				final String ecc_order_id = oms_order.getECC_ORDER_ID();
				final String operation = oms_order.getOPERATION();
				final String result_message = oms_order.getRESULT_MESSAGE();


				final String log_message = "操作类型: " + operation + "\n" + "OMS订单ID: " + oms_order_id + "\n" + "ECC订单ID: "
						+ ecc_order_id + "\n" + "结果代码: " + oms_order_status + "\n" + "结果消息: " + result_message;

				erpOrderLog.info(log_message);

				if (oms_order_status == null || oms_order_status.trim().equals("") || operation == null
						|| operation.trim().equals(""))
				{
					erpOrderLog.error("ERROR: Order status from ECC or operaiton code is null.");
				}
				// 新建
				else if (operation.equals("01"))
				{
					if (oms_order_status.equalsIgnoreCase("S") || oms_order_status.equalsIgnoreCase("W"))
					{
						if (ecc_order_id == null || ecc_order_id.trim().equals(""))
						{
							erpOrderLog.error("ERROR: Order ID from ECC is null.");
						}
						else
						{
							taslyOrderFacade.updateTaslyECCOrder(oms_order_id, operation, oms_order_status, ecc_order_id);
						}
					}
					// 正常情况下，订单创建失败，无ECC_ORDER_ID返回
					// 非正常情况下，同一条OMS_ORDER_ID的请求重复发送，ECC返回状态E以及之前已存在的ECC_ORDER_ID
					else if (oms_order_status.equalsIgnoreCase("E"))
					{
						taslyOrderFacade.updateTaslyECCOrder(oms_order_id, operation, oms_order_status, ecc_order_id);
					}
					else
					{
						erpOrderLog.error("ERROR: Order status is invalid or duplicate request for creation of order.");
					}
				}
				// 锁定
				else if (operation.equals("02"))
				{
					if (oms_order_status.equalsIgnoreCase("S") || oms_order_status.equalsIgnoreCase("E"))
					{
						taslyOrderFacade.updateTaslyECCOrder(oms_order_id, operation, oms_order_status, ecc_order_id);
					}
					else
					{
						erpOrderLog.error("ERROR: Order status is invalid.");
					}

				}
				// 修改
				else if (operation.equals("03"))
				{
					if (oms_order_status.equalsIgnoreCase("S") || oms_order_status.equalsIgnoreCase("E"))
					{
						taslyOrderFacade.updateTaslyECCOrder(oms_order_id, operation, oms_order_status, ecc_order_id);
					}
					else
					{
						erpOrderLog.error("ERROR: Order status is invalid.");
					}

				}
				// 解锁
				else if (operation.equals("04"))
				{
					if (oms_order_status.equalsIgnoreCase("S") || oms_order_status.equalsIgnoreCase("E"))
					{
						taslyOrderFacade.updateTaslyECCOrder(oms_order_id, operation, oms_order_status, ecc_order_id);
					}
					else
					{
						erpOrderLog.error("ERROR: Order status is invalid.");
					}

				}

			}

		}
		else
		{
			erpOrderLog.error("<OMS_ORDER> doesn't exist!");
		}

	}


}
