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

import tasly.greathealth.oms.api.order.PendingOrderQueryObject;
import tasly.greathealth.oms.domain.order.PendingOrderData;


/**
 * @author Henter Liu
 */
public interface PendingOrderService
{
	public abstract Page<PendingOrderData> findPagedPendingOrdersByQuery(PendingOrderQueryObject paramPendingOrderQueryObject);
}
