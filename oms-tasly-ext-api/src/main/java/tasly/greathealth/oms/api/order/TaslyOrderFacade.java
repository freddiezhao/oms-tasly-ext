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
package tasly.greathealth.oms.api.order;

import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;


// import tasly.greathealth.oms.api.order.dto.TaslyOrderLineQuantity;


/**
 *
 */
public interface TaslyOrderFacade
{
	/**
	 * @param taslyOrder
	 */
	void create(TaslyOrder taslyOrder);

	/**
	 * @param taslyOrder
	 */
	void updateTaslyOrder(TaslyOrder taslyOrder);

	/**
	 * @param orderId
	 */
	public void approveOrder(final String orderId);

	/**
	 * @param orderId
	 */
	public void mockOrder(final String orderId);

	public TaslyOrder getTaslyOrderByOrderId(final String orderId);

	/**
	 * Update Express Code By Order.
	 *
	 * @param orderId
	 * @param expressCode
	 */
	void updateExpressCodeByOrderId(String orderId, String expressCode);

	/**
	 * @param orderId
	 * @param lockStatus
	 * @return
	 */
	String updateTaslyOrderLockStatus(String orderId, String lockStatus);

	/**
	 * @param olqId
	 */
	// TaslyOrderLineQuantity getOrderLineQuantityByOlqId(final Long olqId);

	/**
	 * update status of ECC order
	 *
	 * @param orderId
	 * @param operationType
	 * @param status
	 * @param eccOrderId
	 */
	void updateTaslyECCOrder(String orderId, String operationType, String status, String eccOrderId);

	/***
	 * get TaslyOrderLine By OrderLineId
	 */
	TaslyOrderLine getTaslyOrderLineByOrderLineId(String orderLineId);

	/***
	 * update OrderLine RefundFlag
	 */
	String updateOrderLineRefundFlag(final String orderLineId, final String refundFlag);
}
