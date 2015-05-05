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
package tasly.greathealth.oms.ui.order.conversion;

import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.domain.order.ExpressData;


/**
 * Express Info Data To Model Populator.
 */
public class ExpressInfoDataToModelPopulator extends AbstractPopulator<ExpressData, Express>
{
	@Override
	public void populate(final ExpressData source, final Express target)
	{
		target.setCode(source.getCode());
		target.setName(source.getName());
	}
}
