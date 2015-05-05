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

import tasly.greathealth.oms.api.order.SkuToProductQueryObject;
import tasly.greathealth.oms.api.order.dto.SkuToProductQuerySupport;
import tasly.greathealth.oms.domain.order.SkuToProductData;


/**
 * @author Henter Liu
 */
public class SkuToProductQueryFactory extends AbstractQueryFactory
{
	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<String, String>();

	static
	{
		QUERY_SUPPORT_MAPPING.put(SkuToProductQuerySupport.DEFAULT.name(), "itemId");
		QUERY_SUPPORT_MAPPING.put(SkuToProductQuerySupport.ITEMID.name(), "itemId");
	}

	public CriteriaQuery<SkuToProductData> findSkuToProductByQuery(final SkuToProductQueryObject query)
	{
		final CriteriaQuery<SkuToProductData> criteriaQuery = query(SkuToProductData.class);
		final List<Restriction> restrictions = new ArrayList<Restriction>();
		final String itemId = query.getItemId();
		if (StringUtils.isNotEmpty(itemId))
		{
			restrictions.add(RawRestrictions.eq(SkuToProductData.ITEMID.name(), itemId));
		}

		final String outerId = query.getOuterId();
		if (StringUtils.isNotEmpty(outerId))
		{
			restrictions.add(RawRestrictions.eq(SkuToProductData.OUTERID.name(), outerId));
		}

		final boolean hasInnerSourceStatusIds = CollectionUtils.isNotEmpty(query.getInnerSources());
		if (hasInnerSourceStatusIds)
		{
			restrictions.add(RawRestrictions.in(SkuToProductData.INNERSOURCE.name(), (query.getInnerSources().toArray())));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(SkuToProductData.INNERSOURCE.name()));
		}

		final boolean hasChannels = CollectionUtils.isNotEmpty(query.getChannels());
		if (hasChannels)
		{
			restrictions.add(RawRestrictions.in(SkuToProductData.CHANNEL.name(), query.getChannels().toArray()));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(SkuToProductData.CHANNEL.name()));
		}

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		criteriaQuery.order(QUERY_SUPPORT_MAPPING.get(query.getSorting().getAttribute()),
				SortDirection.ASC.equals(query.getSorting().getDirection()));

		return criteriaQuery;
	}

	protected CriteriaQuery<SkuToProductData> applyingCollectedRestrictionsToCriteriaQuery(
			final CriteriaQuery<SkuToProductData> origQuery, final List<Restriction> restrictions)
			{
		CriteriaQuery<SkuToProductData> criteriaQuery = origQuery;
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
