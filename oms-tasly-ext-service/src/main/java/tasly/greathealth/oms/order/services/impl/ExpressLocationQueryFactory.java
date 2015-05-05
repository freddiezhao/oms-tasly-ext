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

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.service.service.AbstractQueryFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import tasly.greathealth.oms.api.order.ExpressLocationQueryObject;
import tasly.greathealth.oms.api.order.dto.ExpressLocationQuerySupport;
import tasly.greathealth.oms.domain.order.ExpresslocationsData;


/**
 * @author Henter Liu
 */
public class ExpressLocationQueryFactory extends AbstractQueryFactory
{
	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<String, String>();

	static
	{
		QUERY_SUPPORT_MAPPING.put(ExpressLocationQuerySupport.DEFAULT.name(), "province");
		QUERY_SUPPORT_MAPPING.put(ExpressLocationQuerySupport.PROVINCE.name(), "province");
		QUERY_SUPPORT_MAPPING.put(ExpressLocationQuerySupport.CODE.name(), "express_code");
	}

	public CriteriaQuery<ExpresslocationsData> findExpressByQuery(final ExpressLocationQueryObject query)
	{
		final CriteriaQuery<ExpresslocationsData> criteriaQuery = query(ExpresslocationsData.class);
		final List<Restriction> restrictions = new ArrayList<Restriction>();

		final String province = query.getProvince();
		if (StringUtils.isNotEmpty(province))
		{
			restrictions.add(RawRestrictions.like(ExpresslocationsData.PROVINCE.name(), province));
		}
		final String code = query.getCode();
		if (StringUtils.isNotEmpty(code))
		{
			restrictions.add(RawRestrictions.like(ExpresslocationsData.EXPRESS_CODE.name(), code));
		}

		final boolean hasChannels = CollectionUtils.isNotEmpty(query.getChannels());
		if (hasChannels)
		{
			restrictions.add(RawRestrictions.in(ExpresslocationsData.CHANNEL_SOURCE.name(), query.getChannels().toArray()));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(ExpresslocationsData.CHANNEL_SOURCE.name()));
		}
		final boolean hasInnerSources = CollectionUtils.isNotEmpty(query.getInnerSources());
		if (hasInnerSources)
		{
			restrictions.add(RawRestrictions.in(ExpresslocationsData.INNER_SOURCE.name(), query.getInnerSources().toArray()));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(ExpresslocationsData.INNER_SOURCE.name()));
		}

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		criteriaQuery.order(QUERY_SUPPORT_MAPPING.get(query.getSorting().getAttribute()),
				SortDirection.ASC.equals(query.getSorting().getDirection()));

		return criteriaQuery;
	}

	protected CriteriaQuery<ExpresslocationsData> applyingCollectedRestrictionsToCriteriaQuery(
			final CriteriaQuery<ExpresslocationsData> origQuery, final List<Restriction> restrictions)
	{
		CriteriaQuery<ExpresslocationsData> criteriaQuery = origQuery;
		if (!(restrictions.isEmpty()))
		{
			criteriaQuery = criteriaQuery.where(restrictions.remove(0));

			for (final Restriction restriction : restrictions)
			{
				criteriaQuery = criteriaQuery.and(restriction);
			}
		}

		return criteriaQuery;
	}
}
