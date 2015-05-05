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
package tasly.greathealth.oms.order.services.impl;


import com.hybris.kernel.api.Page;
import com.hybris.oms.service.service.AbstractHybrisService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.order.PendingOrderQueryObject;
import tasly.greathealth.oms.domain.order.PendingOrderData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.services.PendingOrderService;


/**
 * @author Henter Liu
 */
public class DefaultPendingOrderService extends AbstractHybrisService implements PendingOrderService
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	private PendingOrderQueryFactory pendingOrderQueries;

	protected PendingOrderQueryFactory getPendingOrderQueries()
	{
		return this.pendingOrderQueries;
	}

	@Required
	public void setPendingOrderQueries(final PendingOrderQueryFactory pendingOrderQueries)
	{
		this.pendingOrderQueries = pendingOrderQueries;
	}

	@Override
	public Page<PendingOrderData> findPagedPendingOrdersByQuery(final PendingOrderQueryObject queryObject)
	{
		LOG.trace("findPagedPendingOrdersByQuery");
		final int[] pageNumberAndSize = getPageNumberAndSize(queryObject, 0, getQueryPageSizeDefault());
		return findPaged(this.pendingOrderQueries.findPendingOrdersByQuery(queryObject), pageNumberAndSize[0], pageNumberAndSize[1]);
	}
}
