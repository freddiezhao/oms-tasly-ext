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

import tasly.greathealth.oms.api.order.ExpressItemQueryObject;
import tasly.greathealth.oms.api.order.dto.ExpressItemQuerySupport;
import tasly.greathealth.oms.domain.order.ExpressItemsData;


/**
 * @author jhm
 */
public class ExpressItemQueryFactory extends AbstractQueryFactory
{
	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<String, String>();

	static
	{
		QUERY_SUPPORT_MAPPING.put(ExpressItemQuerySupport.DEFAULT.name(), "skuid");
		QUERY_SUPPORT_MAPPING.put(ExpressItemQuerySupport.SKUID.name(), "skuid");
		QUERY_SUPPORT_MAPPING.put(ExpressItemQuerySupport.CODE.name(), "express_code");
		QUERY_SUPPORT_MAPPING.put(ExpressItemQuerySupport.INNER.name(), "inner_source");
		QUERY_SUPPORT_MAPPING.put(ExpressItemQuerySupport.CHANNEL.name(), "channel_source");
		QUERY_SUPPORT_MAPPING.put(ExpressItemQuerySupport.STATUS.name(), "status");

	}

	public CriteriaQuery<ExpressItemsData> findExpressByQuery(final ExpressItemQueryObject query)
	{
		final CriteriaQuery<ExpressItemsData> criteriaQuery = query(ExpressItemsData.class);
		final List<Restriction> restrictions = new ArrayList<Restriction>();

		final String skuid = query.getSkuid();
		if (StringUtils.isNotEmpty(skuid))
		{
			restrictions.add(RawRestrictions.like(ExpressItemsData.SKUID.name(), skuid));
		}
		final String code = query.getCode();
		if (StringUtils.isNotEmpty(code))
		{
			restrictions.add(RawRestrictions.like(ExpressItemsData.EXPRESS_CODE.name(), code));
		}
		// final String inner = query.getInner();
		// if (StringUtils.isNotEmpty(inner))
		// {
		// restrictions.add(RawRestrictions.like(ExpressItemsData.INNER_SOURCE.name(), inner));
		// }
		// final String channel = query.getChannel();
		// if (StringUtils.isNotEmpty(channel))
		// {
		// restrictions.add(RawRestrictions.like(ExpressItemsData.CHANNEL_SOURCE.name(), channel));
		// }
		final String status = query.getStatus();
		if (StringUtils.isNotEmpty(status))
		{
			restrictions.add(RawRestrictions.like(ExpressItemsData.STATUS.name(), status));
		}

		final boolean hasInnerSourceStatusIds = CollectionUtils.isNotEmpty(query.getInnerSources());
		if (hasInnerSourceStatusIds)
		{
			restrictions.add(RawRestrictions.in(ExpressItemsData.INNER_SOURCE.name(), (query.getInnerSources().toArray())));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(ExpressItemsData.INNER_SOURCE.name()));
		}

		final boolean hasChannels = CollectionUtils.isNotEmpty(query.getChannels());
		if (hasChannels)
		{
			restrictions.add(RawRestrictions.in(ExpressItemsData.CHANNEL_SOURCE.name(), query.getChannels().toArray()));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(ExpressItemsData.CHANNEL_SOURCE.name()));
		}
		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		criteriaQuery.order(QUERY_SUPPORT_MAPPING.get(query.getSorting().getAttribute()),
				SortDirection.ASC.equals(query.getSorting().getDirection()));

		return criteriaQuery;
	}

	protected CriteriaQuery<ExpressItemsData> applyingCollectedRestrictionsToCriteriaQuery(
			final CriteriaQuery<ExpressItemsData> origQuery, final List<Restriction> restrictions)
			{
		CriteriaQuery<ExpressItemsData> criteriaQuery = origQuery;
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
