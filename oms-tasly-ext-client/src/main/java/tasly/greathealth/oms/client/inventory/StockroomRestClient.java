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
package tasly.greathealth.oms.client.inventory;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.TaslyStockroomLocationFacade;
import tasly.greathealth.oms.api.inventory.TaslyStockroomLocationQueryObject;
import tasly.greathealth.oms.api.inventory.dto.StockroomLocation;
import tasly.greathealth.oms.api.inventory.dto.TaslyStockroomLocationList;

import com.sun.jersey.api.client.GenericType;


/**
 *
 */
public class StockroomRestClient extends DefaultRestClient implements TaslyStockroomLocationFacade
{
	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;
	private static final Logger LOGGER = LoggerFactory.getLogger(StockroomRestClient.class);

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}

	@Override
	public Pageable<StockroomLocation> findTaslyStockRoomLocationsByQuery(final TaslyStockroomLocationQueryObject queryObject)
	{
		final RestCallBuilder call = getClient().call("/taslyStockrooms/findAllStockRoomLocationByQuery", new Object[0]);

		this.queryObjectRestCallPopulator.populate(queryObject, call);
		try
		{
			final RestResponse<TaslyStockroomLocationList> response = call.get(new GenericType<TaslyStockroomLocationList>()
					{
				// Nothing
					});
			final TaslyStockroomLocationList stockRoomLocationList = response.result();
			final List<StockroomLocation> locations = stockRoomLocationList.getLocations();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<StockroomLocation>(locations, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public StockroomLocation updateStockroomLocation(final StockroomLocation stockroomLocation)
	{
		LOGGER.trace("updateStockRoomLocation");

		// Call the web service and return the updated location.
		try
		{
			return getClient().call("/taslyStockrooms/updateStockroomLocation").put(StockroomLocation.class, stockroomLocation)
					.result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}



}
