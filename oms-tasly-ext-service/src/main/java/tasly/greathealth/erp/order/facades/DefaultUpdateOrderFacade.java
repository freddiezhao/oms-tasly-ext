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
package tasly.greathealth.erp.order.facades;

import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.slf4j.Logger;

import tasly.greathealth.erp.api.order.UpdateOrderFacade;
import tasly.greathealth.erp.api.order.updateorder.dto.Message;
import tasly.greathealth.erp.order.services.OrderUpdateService;
import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.soapclient.updateorder.SIECCOMSSALESORDERCHANGEOUTAsyn;
import tasly.greathealth.oms.soapclient.updateorder.ZFMSDOMSSALEORDERCOMMONIN;
import tasly.greathealth.oms.soapclient.updateorder.ZSDOMSSALESORDERCHANGE;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSSALEORDERSCOMMIN;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRUPIBASEINFO2;


/**
 * Default implementation of {@link UpdateOrderFacade}.
 * TS-689
 * libin539 20150107
 */
public class DefaultUpdateOrderFacade implements UpdateOrderFacade, TaslyERPConstants
{
	private static final Logger erpOrderLog = OmsLoggerFactory.getErporderlog();
	private OrderUpdateService orderUpdateService;
	private SIECCOMSSALESORDERCHANGEOUTAsyn eccOrderUpdateService;
	private static String PIUSERNAME;
	private static String PIPASSWORD;

	/**
	 * @param pIUSERNAME the pIUSERNAME to set
	 */
	public static void setPIUSERNAME(final String pIUSERNAME)
	{
		PIUSERNAME = pIUSERNAME;
	}

	/**
	 * @param pIPASSWORD the pIPASSWORD to set
	 */
	public static void setPIPASSWORD(final String pIPASSWORD)
	{
		PIPASSWORD = pIPASSWORD;
	}

	/**
	 * @param eccOrderUpdateService the eccOrderUpdateService to set
	 */
	public void setEccOrderUpdateService(final SIECCOMSSALESORDERCHANGEOUTAsyn eccOrderUpdateService)
	{
		this.eccOrderUpdateService = eccOrderUpdateService;
	}

	/**
	 * @param orderUpdateService the orderUpdateService to set
	 */
	public void setOrderUpdateService(final OrderUpdateService orderUpdateService)
	{
		this.orderUpdateService = orderUpdateService;
	}

	@Override
	/**
	 * 返回值：0：通信成功，1：通信失败，2：传过来的信息不完整 3、不可修改该订单
	 */
	public int updateOrders(final String orderId, final Message ordersMessage)
	{
		erpOrderLog.info(UO_LogHead + "Start: ");
		try
		{
			// 1、验证该订单是否可以被修改
			// 根据订单ID查出整个订单对象，然后获取ECC同步订单状态字段，判断该状态是否为S或W.
			if (!orderUpdateService.checkOrderStatus(orderId))
			{
				erpOrderLog.warn(UO_LogHead + "OMS订单ID: " + orderId + "因订单ID为空或者在ECC还未新建成功，所以不可以进行修改!");
				return UO_RESULTUNCHANGEABLE;
			}
			// 2、根据OMSMessage组织ECC所需订单信息ZSDOMSSALESORDERCHANGE
			ZSDOMSSALESORDERCHANGE salesorderchange = null;
			// name = "BASEINFO" name = "MESSAGE"
			ZFMSDOMSSALEORDERCOMMONIN zfsaleordercommonin = null;
			// Create BASEINFO
			final ZSTRUPIBASEINFO2 baseinfo2 = orderUpdateService.createEccBaseInfo();
			// Create MESSAGE
			final ZSTRSDOMSSALEORDERSCOMMIN zstrsdomssaleorderscommin = orderUpdateService.omsMessage2Ecc(ordersMessage);

			if (zstrsdomssaleorderscommin == null)
			{
				erpOrderLog.error(UO_LogHead + "OMS订单ID: " + orderId + ",因Message信息不完整或者不合法,所以Message信息转化错误,无法将修改订单投递到ERP.");
				return UO_RESULTPARAMETERFAILURE;
			}
			else if (baseinfo2 == null)
			{
				erpOrderLog.error(UO_LogHead + "OMS订单ID: " + orderId + ",因BASEINFO信息为空，无法将修改订单投递到ERP.");
			}
			else
			{
				// start put WSDL user name and password into Authenticator
				Authenticator.setDefault(new Authenticator()
				{
					// @Override
					@Override
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(PIUSERNAME, PIPASSWORD.toCharArray());
					}
				});

				salesorderchange = new ZSDOMSSALESORDERCHANGE();
				zfsaleordercommonin = new ZFMSDOMSSALEORDERCOMMONIN();
				zfsaleordercommonin.setBASEINFO(baseinfo2);
				zfsaleordercommonin.setMESSAGE(zstrsdomssaleorderscommin);
				salesorderchange.setIREQUEST(zfsaleordercommonin);
				// 3、调用PI客户端接口
				eccOrderUpdateService.siECCOMSSALESORDERCHANGEOUTAsyn(salesorderchange);
				erpOrderLog.info(UO_LogHead + "OMS订单ID: " + orderId + " 成功投递到ERP端.");
			}
		}
		catch (final EntityNotFoundException ne)
		{
			erpOrderLog.error(UO_LogHead + "OMS订单ID: " + orderId + " 在数据库订单表中不存在,无法将修改订单投递到ERP.");
			return UO_RESULTPARAMETERFAILURE;
		}
		catch (final Exception e)
		{
			erpOrderLog.error(UO_LogHead + "OMS订单ID: " + orderId + " 修改出现异常,该修改订单投递失败,具体异常信息如下：");
			erpOrderLog.error(e.getMessage());
			return UO_RESULTFAILURE;
		}
		finally
		{
			erpOrderLog.info(UO_LogHead + "END.");
		}
		return UO_RESULTSUCCEED;
	}
}
