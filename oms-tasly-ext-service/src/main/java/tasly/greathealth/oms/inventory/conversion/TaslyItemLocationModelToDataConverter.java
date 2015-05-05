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

import com.hybris.commons.conversion.impl.AbstractPopulatingConverter;

import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.services.TaslyItemLocationService;


/**
 * Implementation of {@link AbstractPopulatingConverter} to convert {@link TaslyItemLocation} to
 * {@link TaslyItemLocationData}.
 */
public class TaslyItemLocationModelToDataConverter extends AbstractPopulatingConverter<TaslyItemLocation, TaslyItemLocationData>
{
	private TaslyItemLocationService taslyItemLocationService;

	@Override
	protected TaslyItemLocationData createTarget()
	{
		return taslyItemLocationService.createTaslyItemLocation();
	}

	public void setTaslyItemLocationService(final TaslyItemLocationService taslyItemLocationService)
	{
		this.taslyItemLocationService = taslyItemLocationService;
	}
}
