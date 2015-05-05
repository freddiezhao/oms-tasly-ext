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
package com.hybris.oms.rest.client.inventory;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.exception.ModifiedSinceLastReadException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.BinList;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.ItemStatusList;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.domain.inventory.StockRoomLocationList;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocationList;

import com.sun.jersey.api.client.GenericType;


/**
 * The Class InventoryRestClient.
 */
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.TooManyPublicMethods"})
public class InventoryRestClient extends DefaultRestClient implements InventoryFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryRestClient.class);

	private static final String INVENTORY2 = "/inventory";
	private static final String STOCKROOMS_S = "stockrooms/%s";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String BINS = "/bins";
	private static final String BIN = "/stockrooms/%s/bins";
	private static final String BIN_WITH_BIN_CODE = "/stockrooms/%s/bins/%s";

	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Override
	public OmsInventory createInventory(final OmsInventory inventory) throws EntityValidationException
	{
		LOGGER.trace("createInventory");

		try
		{
			return getClient().call(INVENTORY2).post(OmsInventory.class, inventory).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(EntityValidationException.class, DuplicateEntityException.class);
		}
	}

	/**
	 * Creates the item status.
	 *
	 * @param itemStatus the item status
	 * @return the item status
	 */
	@Override
	public ItemStatus createItemStatus(final ItemStatus itemStatus)
	{
		try
		{
			return getClient().call("statuses/items").post(ItemStatus.class, itemStatus).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public Location createStockRoomLocation(final Location location) throws EntityValidationException
	{
		LOGGER.trace("createStockRoomLocation");

		try
		{
			return getClient().call("stockrooms").post(Location.class, location).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(EntityValidationException.class, DuplicateEntityException.class);
		}
	}

	@Override
	public OmsInventory createUpdateInventory(final OmsInventory inventory) throws EntityValidationException
	{
		LOGGER.trace("createUpdateInventory");

		try
		{
			return getClient().call("/inventory/createUpdate").put(OmsInventory.class, inventory).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(EntityValidationException.class);
		}
	}

	@Override
	public Location createUpdateStockRoomLocation(final Location location) throws EntityValidationException
	{
		LOGGER.trace("createUpdateStockRoomLocation");

		// Call the web service and return the updated location.
		try
		{
			return getClient().call("/stockrooms").put(Location.class, location).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(EntityNotFoundException.class, EntityValidationException.class, ModifiedSinceLastReadException.class);
		}
	}

	@Override
	public void deleteInventory(final OmsInventory inventory) throws EntityValidationException, EntityNotFoundException
	{
		LOGGER.trace("deleteInventory");

		try
		{
			getClient().call(INVENTORY2).delete(inventory).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(EntityValidationException.class, EntityNotFoundException.class);
		}
	}

	@Override
	public List<ItemStatus> findAllItemStatuses()
	{
		try
		{
			return getClient().call("statuses/items").get(ItemStatusList.class).result().getItemStatuses();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Pageable<ItemLocation> findItemLocationsByQuery(final ItemLocationsQueryObject queryObject)
	{
		final RestCallBuilder call = getClient().call("inventory");

		this.queryObjectRestCallPopulator.populate(queryObject, call);

		try
		{
			final RestResponse<TaslyItemLocationList> response = call.get(TaslyItemLocationList.class);
			final TaslyItemLocationList itemLocationList = response.result();
			final List<TaslyItemLocation> itemLocations = itemLocationList.getItemLocations();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			final List<ItemLocation> results = new ArrayList<ItemLocation>();
			for (final TaslyItemLocation taslyItemLocation : itemLocations)
			{
				results.add(taslyItemLocation);
			}
			return new PagedResults<ItemLocation>(results, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Pageable<Location> findStockRoomLocationsByQuery(final LocationQueryObject queryObject)
	{
		final RestCallBuilder call = getClient().call("stockrooms");

		this.queryObjectRestCallPopulator.populate(queryObject, call);

		try
		{
			final RestResponse<?> response = call.get(StockRoomLocationList.class);
			final StockRoomLocationList stockRoomLocationList = (StockRoomLocationList) response.result();
			final List<Location> locations = stockRoomLocationList.getLocations();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<Location>(locations, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	/**
	 * Gets the item status by status code.
	 *
	 * @param statusCode the status code
	 * @return the item status by status code
	 */
	@Override
	public ItemStatus getItemStatusByStatusCode(final String statusCode)
	{
		try
		{
			final ItemStatus itemStatus = getClient().call("statuses/items/%s", statusCode).get(ItemStatus.class).result();
			LOGGER.debug("Server returned itemStatus with status code  " + itemStatus.getStatusCode());
			return itemStatus;
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Pageable<Bin> findBinsByQuery(final BinQueryObject queryObject)
	{
		LOGGER.debug("[FindBinsByQuery]");

		final RestCallBuilder call = getClient().call(BINS);

		this.queryObjectRestCallPopulator.populate(queryObject, call);

		try
		{
			final RestResponse<?> response = call.get(BinList.class);
			final BinList binList = (BinList) response.result();
			final List<Bin> bins = binList.getBins();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<Bin>(bins, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Location getStockRoomLocationByLocationId(final String locationId)
	{
		LOGGER.debug("[GetLocationByLocationId]");
		try
		{
			return getClient().call(STOCKROOMS_S, locationId).get(Location.class).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public List<OmsInventory> updateFutureInventoryDate(final List<OmsInventory> inventories, final Date newDeliveryDate)
			throws EntityValidationException, EntityNotFoundException
			{
		LOGGER.trace("updateFutureCurrentInventory");
		try
		{
			final GenericEntity<List<OmsInventory>> genericEntity = new GenericEntity<List<OmsInventory>>(inventories)
					{
				// empty block
					};
					final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
					final String newDate = dateFormat.format(newDeliveryDate);
					return getClient().call("inventory/date/%s", newDate).put(new GenericType<List<OmsInventory>>()
							{
						// empty block
							}, genericEntity).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
			}

	@Override
	public OmsInventory updateIncrementalInventory(final OmsInventory inventory) throws EntityValidationException,
	EntityNotFoundException
	{
		LOGGER.trace("updateIncrementalInventory");

		try
		{
			return getClient().call(INVENTORY2).queryParam("incremental", "true").put(OmsInventory.class, inventory).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public OmsInventory updateInventory(final OmsInventory inventory) throws EntityValidationException, EntityNotFoundException
	{

		LOGGER.trace("updateInventory");

		try
		{
			return getClient().call(INVENTORY2).queryParam("incremental", "false").put(OmsInventory.class, inventory).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}


	@Override
	public Location updateStockRoomLocation(final Location location) throws EntityNotFoundException, EntityValidationException
	{
		LOGGER.trace("updateStockRoomLocation");

		// Call the web service and return the updated location.
		try
		{
			return getClient().call(STOCKROOMS_S, location.getLocationId()).put(Location.class, location).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Bin createBin(final Bin bin) throws EntityValidationException
	{
		LOGGER.trace("createBin");
		try
		{
			return getClient().call(BIN, bin.getLocationId()).post(Bin.class, bin).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Bin updateBin(final Bin bin) throws EntityValidationException, EntityNotFoundException
	{
		LOGGER.trace("updateBin");
		try
		{
			return getClient().call(BIN_WITH_BIN_CODE, bin.getLocationId(), bin.getBinCode()).put(Bin.class, bin).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void deleteBinByBinCodeLocationId(final String binCode, final String locationId) throws EntityNotFoundException
	{
		LOGGER.trace("deleteBinByBinCodeLocationId");
		try
		{
			getClient().call(BIN_WITH_BIN_CODE, locationId, binCode).delete().result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Bin getBinByBinCodeLocationId(final String binCode, final String locationId) throws EntityNotFoundException
	{
		LOGGER.trace("getBinByQuery");
		try
		{
			return getClient().call(BIN_WITH_BIN_CODE, locationId, binCode).get(Bin.class).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}
}
