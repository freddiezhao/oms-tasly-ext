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
package tasly.greathealth.oms.order.services;

import com.hybris.kernel.api.Page;

import java.util.Date;
import java.util.List;

import tasly.greathealth.oms.api.financial.TaslyFinancialReportQueryObject;
import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;


/**
 *
 */
public interface TaslyOrderService
{
	/**
	 * 创建工单
	 */
	void createTaslyOrderData();

	/**
	 * 更新订单信息
	 *
	 * @param taslyOrder
	 */
	void updateTaslyOrder(TaslyOrder taslyOrder);

	/**
	 * 更新ERP返回状态
	 *
	 * @param orderId
	 * @param type
	 * @param status
	 * @param eccOrderId
	 */
	void updateErpStatus(final String orderId, final String type, final String status, final String eccOrderId);

	/**
	 * 审批、刷单
	 *
	 * @param orderId
	 * @param approveMockStatus
	 */
	void updateOrderApproveStatus(final String orderId, final String approveMockStatus);

	/**
	 * @param orderId
	 * @return
	 */
	TaslyOrderData getTaslyOrderDataByOrderId(String orderId);

    /**
     * 更新orderline下的快递公司
     * @param orderId
     * @param expressCode
     */
	void updateExpressCodeByOrderId(final String orderId, final String expressCode);

	/**
	 * 更新工单锁定状态
	 *
	 * @param orderId
	 * @param lockStatus
	 */
	void updateTaslyOrderLockStats(final String orderId, final String lockStatus);

	/**
	 * 得到快递公司名称
	 *
	 * @param code
	 * @return
	 */
	String getExpressDataName(final String code);

	Page<TaslyOrderData> getTaslyOrderDataByIssuedDate(Date startDate, Date endDate, TaslyFinancialReportQueryObject queryObject);

	/**
	 * @param order
	 * @return
	 */
	List<TaslyOrderLineData> getTaslyOrderLinesByOrderId(TaslyOrderData order);

	/***
	 * get TaslyOrderLine By OrderLineId
	 */
	TaslyOrderLine getTaslyOrderLineByOrderLineId(String orderLineId);

	/***
	 * 更新退款标志
	 */
	void updateOrderLineRefundFlag(final String orderLineId, final String refundFlag);
}
