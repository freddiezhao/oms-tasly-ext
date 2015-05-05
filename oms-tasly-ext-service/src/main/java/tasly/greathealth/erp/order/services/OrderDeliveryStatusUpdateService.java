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

import java.util.List;

import tasly.greathealth.oms.domain.erp.ErpCodeMappingData;
import tasly.greathealth.oms.domain.order.ExpressData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;


/**
 * Service providing methods to manipulate {@link Order} object.
 */
public interface OrderDeliveryStatusUpdateService
{
	/**
	 * TS-688:ERP发货状态同步更新Hybris OMS订单
	 *
	 * @param orderId
	 * @return TaslyOrderData
	 */
	public TaslyOrderData getTaslyOrderDataByOrderID(String orderId);

	/**
	 * TS-688:ERP发货状态同步更新Hybris OMS订单
	 *
	 * @param orderId
	 */
	// update TaslyOrderData packing status
	public void updateTaslyOrderDataPackingByOrderID(String orderId);

	/**
	 * TS-689:Hybris OMS订单信息更新同步到SAP ERP
	 *
	 * @author vincent.yin
	 * @param approve_status replication_status, replication_time
	 */
	public List<TaslyOrderData> getOmsApprovedOrders(final String approve_status, final String[] replication_status,
			final int replication_time);

	/**
	 * @author vincent.yin
	 *         TS-396 :hybris/OMS订单下行同步至ERP接口
	 * @param orderId replication_status
	 *
	 */
	public void updateOmsOrderReplicationStatus(String orderId, String replication_status);

	/**
	 * get ERP code according to type and oms source code
	 *
	 * @author vincent.yin
	 *         TS-396: hybris/OMS订单下行同步至ERP接口
	 */
	public ErpCodeMappingData getErpCodeMappingData(String type, String sourceCode);

	/**
	 * create or update table erpCodeMapping
	 *
	 * @author vincent.yin
	 * @param erpCodeMappingData
	 */
	public ErpCodeMappingData createOrUpdateErpCodeMapping(ErpCodeMappingData erpCodeMappingData);

	/**
	 * create erpCodeMappingData
	 */
	public ErpCodeMappingData createErpCodeMapping();


	/**
	 * get All erpCodeMapping data
	 */
	public List<ErpCodeMappingData> getAllErpCodeMapping();

	/**
	 * get All Express data
	 *
	 */
	public List<ExpressData> getAllExpressData();

}
