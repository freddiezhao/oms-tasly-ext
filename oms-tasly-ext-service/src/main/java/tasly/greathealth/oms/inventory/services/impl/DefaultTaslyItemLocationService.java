/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.inventory.services.impl;

import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.services.TaslyItemLocationService;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * Default implementation of {@link TaslyItemLocationService}.
 */
public class DefaultTaslyItemLocationService extends AbstractHybrisService implements TaslyItemLocationService
{
	private static final Logger omsInventoryLog = OmsLoggerFactory.getOmsinventorylog();

	private InventoryService inventoryService;
	TaslyInventoryQueryFactory taslyInventoryQueryFactory;

	@Override
	public TaslyItemLocationData createTaslyItemLocation()
	{
		omsInventoryLog.info("createTaslyItemLocation");
		return super.getPersistenceManager().create(TaslyItemLocationData.class);
	}

	@Override
	public boolean batchUpdateTaslyItemLocation(final List<TaslyItemLocation> taslyItemLocations)
	{
		omsInventoryLog.info("batchUpdateTaslyItemLocation");
		omsInventoryLog.info(taslyItemLocations.size() + " TaslyItemLocations need to be updated");
		for (final TaslyItemLocation taslyItemLocation : taslyItemLocations)
		{
			final StockroomLocationData stockRoolLocation = inventoryService.getLocationByLocationId(taslyItemLocation.getLocation()
					.getLocationId());
			final TaslyItemLocationData taslyItemLocationData = this.findOneSingle(taslyInventoryQueryFactory
					.findItemLocationBySkuAndLocation(taslyItemLocation.getItemId(), stockRoolLocation));
			taslyItemLocationData.setAllocationPercent(taslyItemLocation.getAllocationPercent());
			taslyItemLocationData.setSafetyStock(taslyItemLocation.getSafetyStock());
		}

		return true;
	}

	/**
	 * @return the inventoryService
	 */
	public InventoryService getInventoryService()
	{
		return inventoryService;
	}

	/**
	 * @param inventoryService the inventoryService to set
	 */
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	/**
	 * @return the taslyInventoryQueryFactory
	 */
	public TaslyInventoryQueryFactory getTaslyInventoryQueryFactory()
	{
		return taslyInventoryQueryFactory;
	}

	/**
	 * @param taslyInventoryQueryFactory the taslyInventoryQueryFactory to set
	 */
	public void setTaslyInventoryQueryFactory(final TaslyInventoryQueryFactory taslyInventoryQueryFactory)
	{
		this.taslyInventoryQueryFactory = taslyInventoryQueryFactory;
	}

	@Override
	@Transactional
	public List<TaslyItemLocationData> getByItemID(final String itemID) throws EntityNotFoundException
	{
		try
		{
			return findAll(super.getPersistenceManager().createCriteriaQuery(TaslyItemLocationData.class)
					.where(Restrictions.eq(TaslyItemLocationData.ITEMID, itemID)));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			omsInventoryLog.error("TaslyItemLocationData not found. Or error is:" + e);
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	public List<TaslyItemLocationData> getAll()
	{
		omsInventoryLog.info("getAll ItemLocationData");
		return super.getPersistenceManager().createCriteriaQuery(TaslyItemLocationData.class).resultList();
	}
}
