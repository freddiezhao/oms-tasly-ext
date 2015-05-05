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

import java.util.List;

import tasly.greathealth.oms.domain.order.TaslyOrderData;


/**
 * @author zijie.ai
 */
public interface OmsOrderAutoApprovalService
{
	/**
	 * @param approveStatus
	 */
	public void approveOrderStatus(String approveStatus, List<TaslyOrderData> tOrderData);

	/**
	 *
	 * @param approvalStatus
	 * @param shippingLockStatus
	 * @return orders
	 */
	public List<TaslyOrderData> getOrdersByOrderQuerys(final String approvalStatus, final String shippingLockStatus);

}
