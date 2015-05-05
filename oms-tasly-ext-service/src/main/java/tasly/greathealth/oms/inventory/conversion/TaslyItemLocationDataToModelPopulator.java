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

import com.hybris.commons.conversion.impl.AbstractPopulator;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;


/**
 * Implementation of {@link AbstractPopulator} to populate a {@link TaslyItemLocation} from
 * {@link TaslyItemLocationData}.
 */
public class TaslyItemLocationDataToModelPopulator extends AbstractPopulator<TaslyItemLocationData, TaslyItemLocation>
{
	private ItemInfoDataToModelConverter itemInfoDataToModelConverter;


	/**
	 * @return the itemInfoDataToModelConverter
	 */
	@Required
	public ItemInfoDataToModelConverter getItemInfoDataToModelConverter()
	{
		return itemInfoDataToModelConverter;
	}


	/**
	 * @param itemInfoDataToModelConverter the itemInfoDataToModelConverter to set
	 */
	@Required
	public void setItemInfoDataToModelConverter(final ItemInfoDataToModelConverter itemInfoDataToModelConverter)
	{
		this.itemInfoDataToModelConverter = itemInfoDataToModelConverter;
	}


	@Override
	public void populate(final TaslyItemLocationData source, final TaslyItemLocation target)
	{
		target.setAllocationPercent(source.getAllocationPercent());
		target.setSafetyStock(source.getSafetyStock());
		target.setItemInfo(itemInfoDataToModelConverter.convert(source.getItemInfo()));
	}
}
