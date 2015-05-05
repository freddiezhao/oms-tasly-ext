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
package tasly.greathealth.tmall.order.conversion;

import com.hybris.commons.conversion.impl.AbstractPopulatingConverter;

import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.domain.order.SkuToProductData;


/**
 * Implementation of {@link AbstractPopulatingConverter} to convert {@link SkuToProductData} to {@link SkuToProduct}.
 */
public class SkuToProductDataToModelConverter extends AbstractPopulatingConverter<SkuToProductData, SkuToProduct>
{

	@Override
	public SkuToProduct createTarget()
	{
		return new SkuToProduct();
	}

}
