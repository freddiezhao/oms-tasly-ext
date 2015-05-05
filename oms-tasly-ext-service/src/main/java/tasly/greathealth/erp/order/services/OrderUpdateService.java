/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.erp.order.services;

import com.hybris.kernel.engine.jdbc.query.Select.OrderByAttribute.Order;

import tasly.greathealth.erp.api.order.updateorder.dto.Message;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSSALEORDERSCOMMIN;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRUPIBASEINFO2;


/**
 * TS-689:Hybris OMS订单信息更新同步到SAP ERP
 *
 * @author libin539
 *
 *         Service providing methods to manipulate {@link Order} object.
 */
public interface OrderUpdateService
{
	/**
	 * TS-689:核查该order是否可以做修改操作
	 *
	 * @param orderId
	 * @return
	 */
	boolean checkOrderStatus(final String orderId) throws Exception;

	/**
	 * TS-689:通过orderId获取TaslyOrderData
	 *
	 * @param orderId
	 * @return
	 */
	TaslyOrderData getTaslyOrderDataByOrderID(String orderId) throws Exception;

	/**
	 * TS-689:create Ecc BaseInfo
	 *
	 * @return
	 */
	ZSTRUPIBASEINFO2 createEccBaseInfo() throws Exception;

	/**
	 * 组装发送ORDERS对象（现在支持支单个ORDER的情况）
	 *
	 * @param Message
	 * @return ZSTRSDOMSSALEORDERSCOMMIN
	 */
	ZSTRSDOMSSALEORDERSCOMMIN omsMessage2Ecc(final Message ordersMessage) throws Exception;

}
