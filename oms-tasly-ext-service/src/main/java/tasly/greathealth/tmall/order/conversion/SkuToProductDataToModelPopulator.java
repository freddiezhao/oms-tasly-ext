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

import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.domain.order.SkuToProductData;


/**
 * Implementation of {@link AbstractPopulator} to populate a {@link SkuToProduct} from {@link SkuToProduct}.
 */
public class SkuToProductDataToModelPopulator extends AbstractPopulator<SkuToProductData, SkuToProduct>
{
	@Override
	public void populate(final SkuToProductData source, final SkuToProduct target)
	{
		target.setChannel(source.getChannel());
		target.setOuterId(source.getOuterId());
		target.setRatio(source.getRatio());
		target.setQuantity(source.getQuantity());
		target.setItemId(source.getItemId());
		target.setItemName(source.getItemName());
		target.setInnerSource(source.getInnerSource());
		target.setPercent(source.getPercent());
		target.setLockStatus(source.getLockStatus());
	}
}
