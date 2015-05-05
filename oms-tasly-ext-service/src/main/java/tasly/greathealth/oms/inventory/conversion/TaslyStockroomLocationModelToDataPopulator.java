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
package tasly.greathealth.oms.inventory.conversion;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.oms.api.inventory.dto.StockroomLocation;
import tasly.greathealth.oms.inventory.domain.TaslyStockroomLocationData;


/**
 *
 */
public class TaslyStockroomLocationModelToDataPopulator extends AbstractPopulator<StockroomLocation, TaslyStockroomLocationData>
{

	@Override
	public void populate(final StockroomLocation source, final TaslyStockroomLocationData target) throws ConversionException,
	IllegalArgumentException
	{
		target.setDefaultAllocatePercent(source.getDefaultAllocatePercent());
	}

}
