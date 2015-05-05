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
package tasly.greathealth.oms.order.facades.impl;

import tasly.greathealth.oms.api.order.TaslyOrderApprovalFacade;
import tasly.greathealth.oms.order.services.OmsOrderAutoApprovalService;


/**
 *
 */
public class DefaultTaslyOrderApprovalFacade implements TaslyOrderApprovalFacade
{

	private OmsOrderAutoApprovalService omsOrderAutoApprovalService;

	@Override
	public void approveOrderStatus(final String approvalStatus, final String unApprovalStatus, final String shippingLockStatus)
	{
		omsOrderAutoApprovalService.approveOrderStatus(approvalStatus,
				omsOrderAutoApprovalService.getOrdersByOrderQuerys(unApprovalStatus, shippingLockStatus));
	}

	/**
	 * @param omsOrderAutoApprovalService the omsOrderAutoApprovalService to set
	 */
	public void setOmsOrderAutoApprovalService(final OmsOrderAutoApprovalService omsOrderAutoApprovalService)
	{
		this.omsOrderAutoApprovalService = omsOrderAutoApprovalService;
	}

}
