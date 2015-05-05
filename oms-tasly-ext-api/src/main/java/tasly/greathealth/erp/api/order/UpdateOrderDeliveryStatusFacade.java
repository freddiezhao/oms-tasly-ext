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
package tasly.greathealth.erp.api.order;

import java.util.List;

import tasly.greathealth.erp.api.codeMapping.dto.ErpCodeMapping;
import tasly.greathealth.erp.api.order.updatedelivery.dto.Message;


/**
 * TS-396:update OMS order packing status
 * update OMS delivery number and company
 * TS-396: create ECC order according to OMS order
 *
 * @author vincent,yin
 */

public interface UpdateOrderDeliveryStatusFacade
{
	/**
	 * update ECC packing status into OMS order
	 *
	 * @param omsOrderIds
	 */
	public List<String> updateOrderStatus4Packing(List<String> omsOrderIds);

	/**
	 * update ECC Delivery status into OMS order
	 *
	 * @param deliveryMessage
	 */
	public List<String> updateOrderStatus4Delivery(Message deliveryMessage);

	/**
	 * create ECC order according to OMS order
	 *
	 */

	public List<String> createEccOrders();

	/**
	 * get ERP code according to type and oms source code
	 *
	 * @author vincent.yin
	 *         TS-396: hybris/OMS订单下行同步至ERP接口
	 */
	public ErpCodeMapping getErpCodeMappingData(String type, String sourceCode);

	/**
	 * create or update table erpCodeMapping
	 *
	 * @author vincent.yin
	 * @param erpCodeMapping
	 */
	public ErpCodeMapping createOrUpdateErpCodeMapping(ErpCodeMapping erpCodeMapping);


	/**
	 * get All erpCodeMapping data
	 */
	public List<ErpCodeMapping> getAllErpCodeMapping();
}
