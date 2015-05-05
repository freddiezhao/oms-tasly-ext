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
package tasly.greathealth.oms.inventory.services.impl;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.service.service.AbstractQueryFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import tasly.greathealth.oms.api.inventory.ItemInfoQueryObject;
import tasly.greathealth.oms.api.inventory.ItemInfoQuerySupport;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;


/**
 * @author Henter Liu
 */
public class ItemInfoQueryFactory extends AbstractQueryFactory
{
	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<String, String>();

	static
	{
		QUERY_SUPPORT_MAPPING.put(ItemInfoQuerySupport.DEFAULT.name(), "sku");
		QUERY_SUPPORT_MAPPING.put(ItemInfoQuerySupport.SKU.name(), "sku");
		QUERY_SUPPORT_MAPPING.put(ItemInfoQuerySupport.DESCRIPTION.name(), "description");
	}

	/**
	 * @param queryObject
	 * @return CriteriaQuery
	 */
	public CriteriaQuery<ItemInfoData> findItemInfosByQueryObject(final ItemInfoQueryObject queryObject)
	{
		final CriteriaQuery<ItemInfoData> criteriaQuery = query(ItemInfoData.class);
		final List<Restriction> restrictions = new ArrayList<Restriction>();

		final String sku = queryObject.getSku();
		if (StringUtils.isNotEmpty(sku))
		{
			restrictions.add(Restrictions.like(ItemInfoData.SKU, sku));
		}

		final boolean hasInnerSources = CollectionUtils.isNotEmpty(queryObject.getInnerSources());
		if (hasInnerSources)
		{
			restrictions.add(RawRestrictions.in(ItemInfoData.INNERSOURCE.name(), queryObject.getInnerSources().toArray()));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(ItemInfoData.INNERSOURCE.name()));
		}

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		criteriaQuery.order(QUERY_SUPPORT_MAPPING.get(queryObject.getSorting().getAttribute()),
				SortDirection.ASC.equals(queryObject.getSorting().getDirection()));

		return criteriaQuery;
	}

	protected CriteriaQuery<ItemInfoData> applyingCollectedRestrictionsToCriteriaQuery(final CriteriaQuery<ItemInfoData> origQuery,
			final List<Restriction> restrictions)
	{
		CriteriaQuery<ItemInfoData> criteriaQuery = origQuery;
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
