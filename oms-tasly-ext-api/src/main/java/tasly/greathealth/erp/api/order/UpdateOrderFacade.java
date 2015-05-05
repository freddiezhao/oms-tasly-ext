/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.erp.api.order;

import tasly.greathealth.erp.api.order.updateorder.dto.Message;



/**
 * The interface UpdateOrderFacade API.
 * ERP is SOAP Server，OMS is Client
 */
public interface UpdateOrderFacade
{
	/**
	 * Update Order.
	 *
	 * @param messageUpdateOrders 待更新订单对象
	 * @return 0(通信成功) or 1(通信失败) or 2(参数信息不完整或者为空) or 3(订单不可修改).
	 */

	int updateOrders(String orderId, Message ordersMessage);
}
