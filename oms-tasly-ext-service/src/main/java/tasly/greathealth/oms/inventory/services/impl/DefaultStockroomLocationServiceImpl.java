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
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.TaslyStockroomLocationQueryObject;
import tasly.greathealth.oms.inventory.domain.TaslyStockroomLocationData;
import tasly.greathealth.oms.inventory.services.StockroomLocationService;


/**
 *
 */
public class DefaultStockroomLocationServiceImpl extends AbstractHybrisService implements StockroomLocationService
{
	private StockroomLocationQueryFactory stockroomLocationQueries;

	/**
	 * @return the stockroomLocationQueries
	 */
	public StockroomLocationQueryFactory getStockroomLocationQueries()
	{
		return stockroomLocationQueries;
	}

	/**
	 * @param stockroomLocationQueries the stockroomLocationQueries to set
	 */
	@Required
	public void setStockroomLocationQueries(final StockroomLocationQueryFactory stockroomLocationQueries)
	{
		this.stockroomLocationQueries = stockroomLocationQueries;
	}

	@Override
	public Page<TaslyStockroomLocationData> findPagedLocations(final TaslyStockroomLocationQueryObject queryObject)
	{
		final CriteriaQuery<TaslyStockroomLocationData> criteriaQuery = this.stockroomLocationQueries.findLocationsByQueryObject(
				queryObject, true);

		if (criteriaQuery == null)
		{
			return new Page<TaslyStockroomLocationData>()
					{
				@Override
				public List<TaslyStockroomLocationData> getContent()
				{
					return Collections.emptyList();
				}

				@Override
				public int getNumber()
				{
					return 0;
				}

				@Override
				public int getNumberOfElements()
				{
					return 0;
				}

				@Override
				public int getSize()
				{
					return 0;
				}

				@Override
				public long getTotalElements()
				{
					return 0L;
				}

				@Override
				public int getTotalPages()
				{
					return 0;
				}
					};
		}

		final int[] pageNumberAndSize = getPageNumberAndSize(queryObject, 0, getQueryPageSizeDefault());

		return findPaged(this.stockroomLocationQueries.findLocationsByQueryObject(queryObject, true), pageNumberAndSize[0],
				pageNumberAndSize[1]);
	}

	@Override
	public TaslyStockroomLocationData getLocationByLocationId(final String locationId) throws EntityNotFoundException
	{
		try
		{
			return (getPersistenceManager().getByIndex(TaslyStockroomLocationData.UX_STOCKROOMLOCATIONS_LOCATIONID, locationId));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Location not found. Location Id: " + locationId, e);
		}
	}

	@Override
	public TaslyStockroomLocationData flushStockroomLocation(final TaslyStockroomLocationData taslyStockroomLocationData)
			throws EntityValidationException
	{
		flush();

		return taslyStockroomLocationData;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<TaslyStockroomLocationData> getAll()
	{
		return super.getPersistenceManager().createCriteriaQuery(TaslyStockroomLocationData.class).resultList();
	}
}
