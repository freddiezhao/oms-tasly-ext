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
package tasly.greathealth.tmall.inventory.dao.impl;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.service.order.impl.OrderQueryFactory;

import java.util.ArrayList;
import java.util.List;

import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;


/**
 *
 */
public class TaslyOrderLineQueryFactory extends OrderQueryFactory
{
	@SuppressWarnings("unchecked")
	public CriteriaQuery<Object> getOrderLineBySkuIdQuery(final String skuId)
	{
		@SuppressWarnings("rawtypes")
		final CriteriaQuery criteriaQuery = this.query(TaslyOrderLineData.class);
		final List restrictions = new ArrayList();
		final Object[] status = {"single|OrderLineQuantityStatusData|1", "single|OrderLineQuantityStatusData|2",
				"single|OrderLineQuantityStatusData|3", "single|OrderLineQuantityStatusData|4",
				"single|OrderLineQuantityStatusData|5"};


		criteriaQuery.join(TaslyOrderLineQuantityData.class, "q");
		restrictions.add(RawRestrictions.eq(TaslyOrderLineData.ID.name(),
				(new StringBuilder()).append("q.").append(TaslyOrderLineQuantityData.ID.name()).toString()));
		restrictions.add(RawRestrictions.in((new StringBuilder()).append("q.").append(TaslyOrderLineQuantityData.STATUS.name())
				.toString(), status));
		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		criteriaQuery.and(Restrictions.eq(TaslyOrderLineData.SKUID, skuId));
		return criteriaQuery;
	}
}
