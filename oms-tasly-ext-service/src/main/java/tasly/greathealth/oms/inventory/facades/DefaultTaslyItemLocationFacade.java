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
package tasly.greathealth.oms.inventory.facades;

import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationFacade;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.inventory.conversion.TaslyItemLocationDataToModelConverter;
import tasly.greathealth.oms.inventory.conversion.TaslyItemLocationModelToDataConverter;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.services.ItemInfoService;
import tasly.greathealth.oms.inventory.services.TaslyItemLocationService;


/**
 * Default implementation of {@link TaslyItemLocationFacade}.
 */
public class DefaultTaslyItemLocationFacade implements TaslyItemLocationFacade
{
	private TaslyItemLocationService taslyItemLocationService;
	private TaslyItemLocationModelToDataConverter taslyItemLocationModelToDataConverter;
	private TaslyItemLocationDataToModelConverter taslyItemLocationDataToModelConverter;
	private Converters converters;

	// add by libin
	private ItemInfoService itemInfoService;

	/**
	 * @param itemInfoService the itemInfoService to set
	 */
	public void setItemInfoService(final ItemInfoService itemInfoService)
	{
		this.itemInfoService = itemInfoService;
	}

	/**
	 * @return the taslyItemLocationModelToDataConverter
	 */
	public TaslyItemLocationModelToDataConverter getTaslyItemLocationModelToDataConverter()
	{
		return taslyItemLocationModelToDataConverter;
	}

	/**
	 * @return the taslyItemLocationDataToModelConverter
	 */
	public TaslyItemLocationDataToModelConverter getTaslyItemLocationDataToModelConverter()
	{
		return taslyItemLocationDataToModelConverter;
	}

	/**
	 * @return the converters
	 */
	public Converters getConverters()
	{
		return converters;
	}

	/**
	 * @return the taslyItemLocationService
	 */
	public TaslyItemLocationService getTaslyItemLocationService()
	{
		return taslyItemLocationService;
	}

	@Required
	public void setTaslyItemLocationService(final TaslyItemLocationService taslyItemLocationService)
	{
		this.taslyItemLocationService = taslyItemLocationService;
	}

	@Required
	public void setTaslyItemLocationModelToDataConverter(
			final TaslyItemLocationModelToDataConverter taslyItemLocationModelToDataConverter)
	{
		this.taslyItemLocationModelToDataConverter = taslyItemLocationModelToDataConverter;
	}

	@Required
	public void setTaslyItemLocationDataToModelConverter(
			final TaslyItemLocationDataToModelConverter taslyItemLocationDataToModelConverter)
	{
		this.taslyItemLocationDataToModelConverter = taslyItemLocationDataToModelConverter;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Override
	@Transactional
	public void create(final TaslyItemLocation taslyItemLocation)
	{
		taslyItemLocationModelToDataConverter.convert(taslyItemLocation);
		taslyItemLocationService.flush();
	}

	@Override
	@Transactional
	public boolean batchUpdate(final List<TaslyItemLocation> taslyItemLocations)
	{
		// final List<TaslyItemLocationData> taslyItemLocationDatas = new ArrayList<TaslyItemLocationData>();
		// for (final TaslyItemLocation taslyItemLocation : taslyItemLocations)
		// {
		// taslyItemLocationDatas.add(taslyItemLocationModelToDataConverter.convert(taslyItemLocation));
		// }
		return taslyItemLocationService.batchUpdateTaslyItemLocation(taslyItemLocations);
	}

	@Override
	@Transactional
	public List<TaslyItemLocation> getByItemID(final String itemID) throws EntityNotFoundException
	{
		final List<TaslyItemLocationData> taslyItemLocationDatas = taslyItemLocationService.getByItemID(itemID);
		final List<TaslyItemLocation> taslyItemLocations = new ArrayList<TaslyItemLocation>();
		for (final TaslyItemLocationData taslyItemLocationData : taslyItemLocationDatas)
		{
			taslyItemLocations.add(taslyItemLocationDataToModelConverter.convert(taslyItemLocationData));
		}
		return taslyItemLocations;
	}

	@Override
	@Transactional
	public boolean addChannelInit(final String channels) throws Exception
	{
		return itemInfoService.batchUpdateItemlocation(channels);
	}
}
