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
package tasly.greathealth.oms.inventory.conversion;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;


/**
 * Implementation of {@link AbstractPopulator} to populate a {@link TaslyItemLocation} from
 * {@link TaslyItemLocationData}.
 */
public class TaslyItemLocationModelToDataPopulator extends AbstractPopulator<TaslyItemLocation, TaslyItemLocationData>
{
	private Converter<Location, StockroomLocationData> locationReverseConverter;

	private ItemInfoModelToDataConverter itemInfoModelToDataConverter;

	/**
	 * @return the itemInfoModelToDataConverter
	 */
	@Required
	public ItemInfoModelToDataConverter getItemInfoModelToDataConverter()
	{
		return itemInfoModelToDataConverter;
	}

	/**
	 * @param itemInfoModelToDataConverter the itemInfoModelToDataConverter to set
	 */
	@Required
	public void setItemInfoModelToDataConverter(final ItemInfoModelToDataConverter itemInfoModelToDataConverter)
	{
		this.itemInfoModelToDataConverter = itemInfoModelToDataConverter;
	}

	@Override
	public void populate(final TaslyItemLocation source, final TaslyItemLocationData target)
	{
		target.setStockroomLocation(this.locationReverseConverter.convert(source.getLocation()));
		target.setItemId(source.getItemId());
		target.setAllocationPercent(source.getAllocationPercent());
		target.setSafetyStock(source.getSafetyStock());
		target.setItemInfo(itemInfoModelToDataConverter.convert(source.getItemInfo()));
	}

	/**
	 * @return the locationReverseConverter
	 */
	public Converter<Location, StockroomLocationData> getLocationReverseConverter()
	{
		return locationReverseConverter;
	}

	/**
	 * @param locationReverseConverter the locationReverseConverter to set
	 */
	public void setLocationReverseConverter(final Converter<Location, StockroomLocationData> locationReverseConverter)
	{
		this.locationReverseConverter = locationReverseConverter;
	}

}
