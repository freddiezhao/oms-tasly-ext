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
import com.hybris.oms.domain.inventory.ItemLocationQuerySupport;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.service.inventory.impl.InventoryQueryFactory;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationQueryObject;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;


/**
 * Tasly Inventory Query Factory.
 */
public class TaslyInventoryQueryFactory extends InventoryQueryFactory
{
	private static final Map<String, String> ITEM_LOCATION_SUPPORT_MAPPING = new HashMap<String, String>();

	private static final String LOC = "loc";
	private static final String LOC2 = "loc.";
	private static final String BIN = "bin";
	private static final String BIN_DOT = "bin.";
	private static final String OWNER = "owner.";

	static
	{
		ITEM_LOCATION_SUPPORT_MAPPING.put(ItemLocationQuerySupport.DEFAULT.name(), "loc.description");
		ITEM_LOCATION_SUPPORT_MAPPING.put(ItemLocationQuerySupport.LOCATION_DESCRIPTION.name(), "loc.description");
		ITEM_LOCATION_SUPPORT_MAPPING.put(ItemLocationQuerySupport.LOCATION_STORENAME.name(), "loc.storeName");
		ITEM_LOCATION_SUPPORT_MAPPING.put(ItemLocationQuerySupport.SKU_ID.name(), "itemId");
	}

	@Override
	@Transactional
	public CriteriaQuery<ItemLocationData> findItemLocationByQuery(final ItemLocationsQueryObject queryObject)
	{
		final List<String> skuIds = queryObject.getSkuIds();
		final List<String> locationIds = queryObject.getLocationIds();

		CriteriaQuery criteriaQuery = query(TaslyItemLocationData.class);

		final List<Restriction> restrictions = new ArrayList<Restriction>();

		criteriaQuery = (CriteriaQuery) ((CriteriaQuery) ((CriteriaQuery) ((CriteriaQuery) criteriaQuery.join(
				StockroomLocationData.class, "loc")).on(ItemLocationData.STOCKROOMLOCATION)).join(BinData.class, "bin"))
				.on(ItemLocationData.BIN);

		restrictions.add(RawRestrictions.eq("bin." + BinData.BINCODE.name(), "default_bin"));

		if (CollectionUtils.isNotEmpty(locationIds))
		{
			restrictions.add(RawRestrictions.in("loc.locationId", locationIds.toArray()));
		}

		if (CollectionUtils.isNotEmpty(skuIds))
		{
			restrictions.add(RawRestrictions.in(ItemLocationData.ITEMID.name(), skuIds.toArray()));
		}

		if (queryObject instanceof TaslyItemLocationQueryObject)
		{
			criteriaQuery = (CriteriaQuery) ((CriteriaQuery) criteriaQuery.join(ItemInfoData.class, "iteminfo"))
					.on(TaslyItemLocationData.ITEMINFO);
			final TaslyItemLocationQueryObject query = (TaslyItemLocationQueryObject) queryObject;
			final boolean hasInnerSources = CollectionUtils.isNotEmpty(query.getInnerSources());
			if (hasInnerSources)
			{
				restrictions
						.add(RawRestrictions.in("iteminfo." + ItemInfoData.INNERSOURCE.name(), query.getInnerSources().toArray()));
			}
			else
			{
				restrictions.add(RawRestrictions.isNull("iteminfo." + ItemInfoData.INNERSOURCE.name()));
			}
		}

		if (!restrictions.isEmpty())
		{
			criteriaQuery = (CriteriaQuery) criteriaQuery.where(restrictions.remove(0));

			for (final Restriction restriction : restrictions)
			{
				criteriaQuery = (CriteriaQuery) criteriaQuery.and(restriction);
			}
		}

		criteriaQuery = (CriteriaQuery) criteriaQuery.order(
				ITEM_LOCATION_SUPPORT_MAPPING.get(queryObject.getSorting().getAttribute()),
				SortDirection.ASC.equals(queryObject.getSorting().getDirection()));

		return criteriaQuery;
	}

	@Transactional
	public CriteriaQuery<TaslyItemLocationData> findTaslyItemLocationByQuery(final TaslyItemLocationQueryObject queryObject)
	{
		final List<String> skuIds = removeNvlByList(queryObject.getSkuIds());
		final List<String> locationIds = removeNvlByList(queryObject.getChannels());
		final List<String> innerSourcesList = removeNvlByList(queryObject.getInnerSources());

		CriteriaQuery<TaslyItemLocationData> criteriaQuery = query(TaslyItemLocationData.class);

		final List<Restriction> restrictions = new ArrayList<Restriction>();

		criteriaQuery = (CriteriaQuery) ((CriteriaQuery) ((CriteriaQuery) ((CriteriaQuery) ((CriteriaQuery) ((CriteriaQuery) criteriaQuery
				.join(StockroomLocationData.class, "loc")).on(TaslyItemLocationData.STOCKROOMLOCATION)).join(BinData.class, "bin"))
				.on(TaslyItemLocationData.BIN)).join(ItemInfoData.class, "iteminfo")).on(TaslyItemLocationData.ITEMINFO);

		restrictions.add(RawRestrictions.eq("bin." + BinData.BINCODE.name(), "default_bin"));

		if (CollectionUtils.isNotEmpty(skuIds) && skuIds.size() > 0)
		{
			restrictions.add(RawRestrictions.in(TaslyItemLocationData.ITEMID.name(), skuIds.toArray()));
		}

		if (CollectionUtils.isNotEmpty(locationIds) && locationIds.size() > 0)
		{
			restrictions.add(RawRestrictions.in("loc.locationId", locationIds.toArray()));
		}

		if (CollectionUtils.isNotEmpty(innerSourcesList) && innerSourcesList.size() > 0)
		{
			restrictions.add(RawRestrictions.in("iteminfo.innerSource", innerSourcesList.toArray()));
		}
		else
		{
			restrictions.add(RawRestrictions.isNull("iteminfo." + ItemInfoData.INNERSOURCE.name()));
		}

		restrictions.add(RawRestrictions.eq("iteminfo." + ItemInfoData.STOCKMANAGEFLAG.name(), 1));
		restrictions.add(RawRestrictions.isNotNull(TaslyItemLocationData.SAFETYSTOCK.name()));
		restrictions.add(RawRestrictions.gt(TaslyItemLocationData.SAFETYSTOCK.name(), 0));

		// if (queryObject instanceof TaslyItemLocationQueryObject)
		// {
		// criteriaQuery = (CriteriaQuery) ((CriteriaQuery) criteriaQuery.join(ItemInfoData.class, "iteminfo"))
		// .on(TaslyItemLocationData.ITEMINFO);
		// final TaslyItemLocationQueryObject query = queryObject;
		// final boolean hasInnerSources = CollectionUtils.isNotEmpty(query.getInnerSources());
		// if (hasInnerSources)
		// {
		// restrictions
		// .add(RawRestrictions.in("iteminfo." + ItemInfoData.INNERSOURCE.name(), query.getInnerSources().toArray()));
		// }
		// else
		// {
		// restrictions.add(RawRestrictions.isNull("iteminfo." + ItemInfoData.INNERSOURCE.name()));
		// }
		// }

		if (!restrictions.isEmpty())
		{
			criteriaQuery = criteriaQuery.where(restrictions.remove(0));

			for (final Restriction restriction : restrictions)
			{
				criteriaQuery = criteriaQuery.and(restriction);
			}
		}

		criteriaQuery = criteriaQuery.order(ITEM_LOCATION_SUPPORT_MAPPING.get(queryObject.getSorting().getAttribute()),
				SortDirection.ASC.equals(queryObject.getSorting().getDirection()));

		return criteriaQuery;
	}

	@Transactional
	public CriteriaQuery<ItemInfoData> findItemInfoByQuery(final ItemLocationsQueryObject queryObject)
	{
		final List<String> skus = removeNvlByList(queryObject.getSkuIds());

		CriteriaQuery<ItemInfoData> criteriaQuery = query(ItemInfoData.class);

		final List<Restriction> restrictions = new ArrayList<Restriction>();

		if (CollectionUtils.isNotEmpty(skus) && skus.size() > 0)
		{
			restrictions.add(RawRestrictions.in(ItemInfoData.SKU.name(), skus.toArray()));
		}

		if (!restrictions.isEmpty())
		{
			criteriaQuery = criteriaQuery.where(restrictions.remove(0));

			for (final Restriction restriction : restrictions)
			{
				criteriaQuery = criteriaQuery.and(restriction);
			}
		}

		return criteriaQuery;
	}

	/**
	 * Find Item Location By Sku And Location.
	 *
	 * @param sku
	 * @param stockroomLocationData
	 * @return
	 */
	@Transactional
	public CriteriaQuery<TaslyItemLocationData> findItemLocationBySkuAndLocation(final String sku,
			final StockroomLocationData stockroomLocationData)
			{
		CriteriaQuery<TaslyItemLocationData> criteriaQuery = this.query(TaslyItemLocationData.class);
		criteriaQuery = criteriaQuery.where(Restrictions.eq(TaslyItemLocationData.ITEMID, sku)).and(
				Restrictions.eq(TaslyItemLocationData.STOCKROOMLOCATION, stockroomLocationData));
		return criteriaQuery;
			}

	@Override
	public CriteriaQuery<CurrentItemQuantityData> getCurrentItemQuantityBySkuLocationBinCodeStatusQuery(final String skuId,
			final String locationId, final String binCode, final String statusCode)
			{
		return this
				.query(CurrentItemQuantityData.class)
				.join(TaslyItemLocationData.class, "owner")
				.on(CurrentItemQuantityData.OWNER)
				.join(StockroomLocationData.class, LOC)
				.join(BinData.class, BIN)
				.where(Restrictions.eq(CurrentItemQuantityData.STATUSCODE, statusCode))
				.and(RawRestrictions.eq(OWNER + TaslyItemLocationData.ITEMID.name(), skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId))
				.and(RawRestrictions.attrEq(LOC2 + StockroomLocationData.ID.name(),
						OWNER + TaslyItemLocationData.STOCKROOMLOCATION.name()))
						.and(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), binCode))
						.and(RawRestrictions.attrEq(BIN_DOT + BinData.ID.name(), OWNER + TaslyItemLocationData.BIN.name()))
						.and(RawRestrictions.eq(OWNER + TaslyItemLocationData.FUTURE.name(), Boolean.FALSE));
			}

	@Override
	public CriteriaQuery<FutureItemQuantityData> getFutureItemQuantityBySkuLocationBinCodeStatusDateQuery(final String skuId,
			final String locationId, final String binCode, final String statusCode, final Date expectedDate)
			{
		return this
				.query(FutureItemQuantityData.class)
				.join(TaslyItemLocationData.class, "owner")
				.on(FutureItemQuantityData.OWNER)
				.join(StockroomLocationData.class, LOC)
				.join(BinData.class, BIN)
				.where(Restrictions.eq(FutureItemQuantityData.STATUSCODE, statusCode))
				.and(Restrictions.eq(FutureItemQuantityData.EXPECTEDDELIVERYDATE, DateUtils.truncate(expectedDate, Calendar.DATE)))
				.and(RawRestrictions.eq(OWNER + TaslyItemLocationData.ITEMID.name(), skuId))
				.and(RawRestrictions.eq(LOC2 + StockroomLocationData.LOCATIONID.name(), locationId))
				.and(RawRestrictions.attrEq(LOC2 + StockroomLocationData.ID.name(),
						OWNER + TaslyItemLocationData.STOCKROOMLOCATION.name()))
						.and(RawRestrictions.eq(BIN_DOT + BinData.BINCODE.name(), binCode))
						.and(RawRestrictions.attrEq(BIN_DOT + BinData.ID.name(), OWNER + TaslyItemLocationData.BIN.name()))
						.and(RawRestrictions.eq(OWNER + TaslyItemLocationData.FUTURE.name(), Boolean.TRUE));
			}

	private List<String> removeNvlByList(final List<String> oldList)
	{
		final List<String> newList = new LinkedList<String>();

		if (null != oldList && oldList.size() > 0)
		{
			for (final String str : oldList)
			{
				if (StringUtils.isNotEmpty(str))
				{
					newList.add(str);
				}
			}
		}

		return newList;
	}
}
