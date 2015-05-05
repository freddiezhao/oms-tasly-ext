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
package tasly.greathealth.oms.export.stock.conversion;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.oms.export.api.stock.dto.ExportStock;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;


/**
 *
 */
public class ExportStockPopulator extends AbstractPopulator<TaslyItemLocationData, ExportStock>
{

	@Override
	public void populate(final TaslyItemLocationData source, final ExportStock target) throws ConversionException,
			IllegalArgumentException
	{
		target.setSku(source.getItemInfo().getSku());
		target.setDescription(source.getItemInfo().getDescription());
		target.setOldMaterialNumber(source.getItemInfo().getOldMaterialNumber());
		target.setLocationId(source.getStockroomLocation().getLocationId());
		target.setSafetyStock(source.getSafetyStock());
		target.setInnerSource(source.getItemInfo().getInnerSource().name());
	}

}
