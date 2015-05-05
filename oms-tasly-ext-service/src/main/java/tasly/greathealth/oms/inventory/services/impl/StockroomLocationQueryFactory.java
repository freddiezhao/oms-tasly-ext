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

import com.hybris.kernel.api.CriteriaExpression;
import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.inventory.LocationQuerySortSupport;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.service.AbstractQueryFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.api.inventory.TaslyStockroomLocationQueryObject;
import tasly.greathealth.oms.inventory.domain.TaslyStockroomLocationData;
import tasly.greathealth.oms.log.OmsLoggerFactory;

import com.google.common.base.Preconditions;


/**
 *
 */
public class StockroomLocationQueryFactory extends AbstractQueryFactory
{
	private static final Logger LOG = OmsLoggerFactory.getOmsinventorylog();
	private long locationsQueryTtl;
	private static final Map<String, String> LOCATION_QUERY_SUPPORT_MAPPING = new HashMap();

	/**
	 * @param queryObject
	 * @return
	 */
	public CriteriaQuery<TaslyStockroomLocationData> findLocationsByQueryObject(
			final TaslyStockroomLocationQueryObject queryObject, final boolean searchByWildcardAllowed)
			{
		final CriteriaQuery<TaslyStockroomLocationData> criteriaQuery = query(TaslyStockroomLocationData.class);
		final List<Restriction> restrictions = new ArrayList<Restriction>();

		final String name = queryObject.getLocationName();
		if (StringUtils.isNotEmpty(name))
		{
			restrictions.add(Restrictions.like(StockroomLocationData.STORENAME, '%' + name + '%'));
		}

		final boolean hasChannels = CollectionUtils.isNotEmpty(queryObject.getChannels());
		if (hasChannels)
		{
			restrictions.add(RawRestrictions.in(StockroomLocationData.STORENAME.name(), queryObject.getChannels().toArray()));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull(StockroomLocationData.STORENAME.name()));
		}

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		criteriaQuery.order(LOCATION_QUERY_SUPPORT_MAPPING.get(queryObject.getSorting().getAttribute()),
				SortDirection.ASC.equals(queryObject.getSorting().getDirection()));

		return criteriaQuery;

		// final String locationName = queryObject.getLocationName();
		// final String priority = queryObject.getPriority();
		//
		// final List locationIds = queryObject.getLocationIds();
		// final List baseStores = queryObject.getBaseStores();
		// // final List countryCodes = queryObject.getCountries();
		// final CriteriaQuery criteriaQuery = query(TaslyStockroomLocationData.class);
		//
		// final List restrictions = new ArrayList();
		//
		// filterLocationIds(searchByWildcardAllowed, locationIds, restrictions);
		//
		// if (StringUtils.isNotEmpty(locationName))
		// {
		// restrictions.add(Restrictions.like(StockroomLocationData.STORENAME, '%' + locationName + '%'));
		// }
		//
		// if (StringUtils.isNotEmpty(priority))
		// {
		// restrictions.add(Restrictions.eq(StockroomLocationData.PRIORITY, Integer.valueOf(priority)));
		// }
		//
		// if (CollectionUtils.isNotEmpty(baseStores))
		// {
		// ((CriteriaQuery) criteriaQuery.join(BaseStoreData.class, "bs")).on(StockroomLocationData.BASESTORES);
		// restrictions.add(RawRestrictions.in("bs.name", baseStores.toArray()));
		// }
		//
		// // if (CollectionUtils.isNotEmpty(locationRoles))
		// // {
		// // restrictions.add(CollectionRestrictions.containsAll(TaslyStockroomLocationData.LOCATIONROLES,
		// // locationRoles.toArray(new String[locationRoles.size()])));
		// // }
		//
		// applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		// criteriaQuery.order(LOCATION_QUERY_SUPPORT_MAPPING.get(queryObject.getSorting().getAttribute()),
		// SortDirection.ASC.equals(queryObject.getSorting().getDirection()));
		//
		// if (!searchByWildcardAllowed)
		// {
		// applyLocationsQueryTtl(criteriaQuery);
		// }
		// return criteriaQuery;
			}

	protected void filterLocationIds(final boolean searchByWildcardAllowed, final List<String> locationIds,
			final List<CriteriaExpression<?, ?>> restrictions)
	{
		if (locationIds.size() == 1)
		{
			final String locationId = locationIds.get(0);
			if (searchByWildcardAllowed)
			{
				restrictions.add(Restrictions.like(TaslyStockroomLocationData.LOCATIONID, '%' + locationId + '%'));
			}
			else
			{
				restrictions.add(Restrictions.eq(TaslyStockroomLocationData.LOCATIONID, locationId));
			}

		}
		else if (locationIds.size() > 1)
		{
			restrictions.add(Restrictions.in(TaslyStockroomLocationData.LOCATIONID,
					locationIds.toArray(new String[locationIds.size()])));
		}
	}

	// protected boolean filterCountryCodes(final List<String> countryCodes, final List<CriteriaExpression<?, ?>>
	// restrictions)
	// {
	// boolean emptyResult = false;
	// if (CollectionUtils.isNotEmpty(countryCodes))
	// {
	// final List countryList = ((CriteriaQuery) query(CountryData.class).where(
	// Restrictions.in(CountryData.CODE, countryCodes.toArray(new String[countryCodes.size()])))).resultList();
	//
	// if (countryList.size() != countryCodes.size())
	// {
	// LOG.warn("Unknown country code provided: " + countryCodes);
	// }
	// if (countryList.isEmpty())
	// {
	// emptyResult = true;
	// }
	// else
	// {
	// // restrictions.add(CollectionRestrictions.containsAny(TaslyStockroomLocationData.SHIPTOCOUNTRIES,
	// // countryList.toArray(new CountryData[countryList.size()])));
	// }
	// }
	// }

	protected CriteriaQuery<TaslyStockroomLocationData> applyingCollectedRestrictionsToCriteriaQuery(
			final CriteriaQuery<TaslyStockroomLocationData> origQuery, final List<Restriction> restrictions)
			{
		CriteriaQuery<TaslyStockroomLocationData> criteriaQuery = origQuery;
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

	protected void applyLocationsQueryTtl(final CriteriaQuery<TaslyStockroomLocationData> criteriaQuery)
	{
		if ((this.locationsQueryTtl > 0L) && (criteriaQuery != null))
		{
			criteriaQuery.setTTL(this.locationsQueryTtl);
		}
	}

	protected long getLocationsQueryTtl()
	{
		return this.locationsQueryTtl;
	}

	public void setLocationsQueryTtl(final long locationsQueryTtl)
	{
		Preconditions.checkArgument(locationsQueryTtl >= 0L, "locationQueryTtl has to be >= 0");
		this.locationsQueryTtl = locationsQueryTtl;
	}

	static
	{
		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.DEFAULT.name(), "locationId");
		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.LOCATION_ID.name(), "locationId");
		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.LOCATION_DESCRIPTION.name(), "description");
		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.LOCATION_STORENAME.name(), "storeName");
		LOCATION_QUERY_SUPPORT_MAPPING.put(LocationQuerySortSupport.LOCATION_PRIORITY.name(), "priority");
	}
}
