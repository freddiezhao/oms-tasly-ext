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
package tasly.greathealth.oms.export.stock.services.impl;

import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationQueryObject;
import tasly.greathealth.oms.export.stock.services.ExportStockService;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.services.ItemQuantityService;
import tasly.greathealth.oms.inventory.services.impl.TaslyInventoryQueryFactory;


/**
 *
 */
public class DefaultExportStockService extends AbstractHybrisService implements ExportStockService
{
	private TaslyInventoryQueryFactory taslyInventoryQueryFactory;
	private ItemQuantityService itemQuantityService;

	/**
	 * @param taslyInventoryQueryFactory the taslyInventoryQueryFactory to set
	 */
	@Required
	public void setTaslyInventoryQueryFactory(final TaslyInventoryQueryFactory taslyInventoryQueryFactory)
	{
		this.taslyInventoryQueryFactory = taslyInventoryQueryFactory;
	}

	/**
	 * @param itemQuantityService the itemQuantityService to set
	 */
	@Required
	public void setItemQuantityService(final ItemQuantityService itemQuantityService)
	{
		this.itemQuantityService = itemQuantityService;
	}

	@Override
	@Transactional
	public List<TaslyItemLocationData> findListItemLocationsByQuery(final TaslyItemLocationQueryObject queryObject)
	{
		return super.getPersistenceManager().search(this.taslyInventoryQueryFactory.findTaslyItemLocationByQuery(queryObject));
	}

	@Override
	@Transactional
	public List<ItemInfoData> findListItemInfosByQuery(final ItemLocationsQueryObject queryObject)
	{
		return super.getPersistenceManager().search(this.taslyInventoryQueryFactory.findItemInfoByQuery(queryObject));
	}

	@Override
	@Transactional
	public int getAtsByQuery(final String itemId, final String locationId)
	{
		return itemQuantityService.calculateAts(itemId, locationId);
	}
}
