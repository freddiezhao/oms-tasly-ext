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

/**
 * @Description: ECC通过PI向OMS同步订单创建和修改结果(TS-689[TS-752])
 * @author liangw
 * @date 2015-01-10
 */
package tasly.greathealth.oms.web.orderstatus.soap.resources;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import tasly.greathealth.oms.api.orderstatus.dto.Baseinfo;
import tasly.greathealth.oms.api.orderstatus.dto.Message;


/**
 * @description ECC通过PI向OMS同步订单创建和修改结果 TS-689[TS-752]
 * @author LIANG
 * @date 2015-01-10
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface EccOmsOrderCallbackService
{
	@WebMethod
	@Oneway
	public void orderCallback(@WebParam(name = "BASEINFO") Baseinfo baseinfo, @WebParam(name = "MESSAGE") Message message);
}
