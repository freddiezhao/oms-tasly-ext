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

import com.hybris.commons.conversion.impl.AbstractPopulatingConverter;

import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.domain.order.ExpressData;


/**
 * Express Info Data To Model converter.
 */
public class ExpressInfoDataToModelConverter extends AbstractPopulatingConverter<ExpressData, Express>
{
	@Override
	public Express createTarget()
	{
		return new Express();
	}

}
