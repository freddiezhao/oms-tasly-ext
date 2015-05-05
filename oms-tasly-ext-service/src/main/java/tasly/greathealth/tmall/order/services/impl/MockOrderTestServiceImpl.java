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
package tasly.greathealth.tmall.order.services.impl;

import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.service.service.AbstractHybrisService;

import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.tmall.order.services.MockOrderTestService;


/**
 *
 */
public class MockOrderTestServiceImpl extends AbstractHybrisService implements MockOrderTestService
{

	@Transactional
	@Override
	public void updateOrderPackStatus(final String tid, final String packStatus)
	{
		@SuppressWarnings("deprecation")
		final TaslyOrderData taslyOrderData = super.getPersistenceManager().createCriteriaQuery(TaslyOrderData.class)
		.where(Restrictions.eq(TaslyOrderData.ORIGINAL_ORDER_ID, tid)).uniqueResult();
		taslyOrderData.setPacking(tasly.greathealth.oms.domain.order.PackingType.valueOf(packStatus));
	}
}
