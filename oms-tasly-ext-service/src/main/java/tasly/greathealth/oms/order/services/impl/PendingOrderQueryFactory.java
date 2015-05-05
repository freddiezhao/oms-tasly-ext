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

import tasly.greathealth.oms.api.order.PendingOrderQueryObject;
import tasly.greathealth.oms.api.order.PendingOrderQuerySupport;
import tasly.greathealth.oms.domain.order.PendingOrderData;


/**
 * @author Henter Liu
 */
public class PendingOrderQueryFactory extends AbstractQueryFactory
{
	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<String, String>();

	static
	{
		QUERY_SUPPORT_MAPPING.put(PendingOrderQuerySupport.DEFAULT.name(), "tid");
		QUERY_SUPPORT_MAPPING.put(PendingOrderQuerySupport.TID.name(), "tid");
		QUERY_SUPPORT_MAPPING.put(PendingOrderQuerySupport.OID.name(), "oid");
		QUERY_SUPPORT_MAPPING.put(PendingOrderQuerySupport.EVENT_TYPE.name(), "eventtype");
		QUERY_SUPPORT_MAPPING.put(PendingOrderQuerySupport.STATE.name(), "state");
	}

	public CriteriaQuery<PendingOrderData> findPendingOrdersByQuery(final PendingOrderQueryObject query)
	{
		final CriteriaQuery<PendingOrderData> criteriaQuery = query(PendingOrderData.class);
		final List<Restriction> restrictions = new ArrayList<Restriction>();

		final String tid = query.getTid();
		if (StringUtils.isNotEmpty(tid))
		{
			restrictions.add(RawRestrictions.eq(PendingOrderData.TID.name(), tid));
		}
		final String oid = query.getOid();
		if (StringUtils.isNotEmpty(oid))
		{
			restrictions.add(RawRestrictions.eq(PendingOrderData.OID.name(), oid));
		}
		final List<String> eventTypes = query.getEventTypes();
		final boolean hasEventTypes = CollectionUtils.isNotEmpty(eventTypes);
		if (hasEventTypes)
		{
			restrictions.add(RawRestrictions.in(PendingOrderData.EVENTTYPE.name(), eventTypes.toArray()));
		}
		final String state = query.getState();
		if (StringUtils.isNotEmpty(state))
		{
			restrictions.add(RawRestrictions.eq(PendingOrderData.STATE.name(), state));
		}

		final boolean hasChannels = CollectionUtils.isNotEmpty(query.getChannels());
		if (hasChannels)
		{
			restrictions.add(RawRestrictions.in(PendingOrderData.CHANNELSOURCE.name(), query.getChannels().toArray()));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(PendingOrderData.CHANNELSOURCE.name()));
		}
		final boolean hasInnerSources = CollectionUtils.isNotEmpty(query.getInnerSources());
		if (hasInnerSources)
		{
			restrictions.add(RawRestrictions.in(PendingOrderData.INNERSOURCE.name(), query.getInnerSources().toArray()));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(PendingOrderData.INNERSOURCE.name()));
		}

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		criteriaQuery.order(QUERY_SUPPORT_MAPPING.get(query.getSorting().getAttribute()),
				SortDirection.ASC.equals(query.getSorting().getDirection()));

		return criteriaQuery;
	}

	protected CriteriaQuery<PendingOrderData> applyingCollectedRestrictionsToCriteriaQuery(
			final CriteriaQuery<PendingOrderData> origQuery, final List<Restriction> restrictions)
	{
		CriteriaQuery<PendingOrderData> criteriaQuery = origQuery;
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
