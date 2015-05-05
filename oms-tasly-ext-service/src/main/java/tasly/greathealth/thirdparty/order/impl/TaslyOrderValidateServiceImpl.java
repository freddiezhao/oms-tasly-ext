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
package tasly.greathealth.thirdparty.order.impl;

import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.thirdparty.order.TaslyOrderValidateService;


public class TaslyOrderValidateServiceImpl extends AbstractHybrisService implements TaslyOrderValidateService, OrderConstants
{

	@Override
	@Transactional
	public boolean validateOrderOriginalOrderIdRepeat(final String originalOrderId)
	{
		@SuppressWarnings("deprecation")
		final List<TaslyOrderData> taslyOrders = super.getPersistenceManager().createCriteriaQuery(TaslyOrderData.class)
				.where(Restrictions.eq(TaslyOrderData.ORIGINAL_ORDER_ID, originalOrderId)).resultList();
		return !CollectionUtils.isEmpty(taslyOrders);
	}
}
