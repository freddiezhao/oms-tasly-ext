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
package tasly.greathealth.oms.web.delivery.soap.resources;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import tasly.greathealth.erp.api.inventory.dto.Baseinfo;
import tasly.greathealth.erp.api.order.updatepacking.dto.Message;


/**
 * ERP发货状态同步更新Hybris OMS订单(TS-688)
 * vincent 2015-01-06
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface TaslyOmsPackingStatusUpdateService
{
	/**
	 *
	 * Packing status
	 * Return nothing
	 *
	 * @param baseinfo
	 * @param message
	 *
	 */
	@WebMethod
	@Oneway
	public void updateOrderStatus4Packing(@WebParam(name = "baseinfo") Baseinfo baseinfo,
			@WebParam(name = "message") Message message);

}
