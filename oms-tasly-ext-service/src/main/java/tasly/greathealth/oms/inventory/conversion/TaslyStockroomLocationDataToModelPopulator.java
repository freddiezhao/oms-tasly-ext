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

import tasly.greathealth.oms.api.inventory.dto.StockroomLocation;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.domain.TaslyStockroomLocationData;


/**
 * Implementation of {@link AbstractPopulator} to populate a {@link TaslyItemLocation} from
 * {@link TaslyItemLocationData}.
 */
public class TaslyStockroomLocationDataToModelPopulator extends AbstractPopulator<TaslyStockroomLocationData, StockroomLocation>
{
	@Override
	public void populate(final TaslyStockroomLocationData source, final StockroomLocation target)
	{
		target.setDefaultAllocatePercent(source.getDefaultAllocatePercent());
	}
}
