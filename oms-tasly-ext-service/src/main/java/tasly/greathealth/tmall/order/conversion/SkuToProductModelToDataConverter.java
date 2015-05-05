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
import tasly.greathealth.tmall.order.services.SkuToProductService;


/**
 * Implementation of {@link AbstractPopulatingConverter} to convert {@link SkuToProduct} to {@link SkuToProductData}.
 */
public class SkuToProductModelToDataConverter extends AbstractPopulatingConverter<SkuToProduct, SkuToProductData>
{

	private SkuToProductService skuToProductService;

	@Override
	protected SkuToProductData createTarget()
	{
		return skuToProductService.createSkuToProduct();
	}

	public void setSkuToProductService(final SkuToProductService skuToProductService)
	{
		this.skuToProductService = skuToProductService;
	}

}
