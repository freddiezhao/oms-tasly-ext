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
package tasly.greathealth.oms.export.order.conversion;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.export.api.order.dto.ExportOrderLineQuantity;


/**
 * @author Henter Liu
 */
public class ExportOrderLineQuantityPopulator extends AbstractPopulator<TaslyOrderLineQuantityData, ExportOrderLineQuantity>
{
	@Override
	public void populate(final TaslyOrderLineQuantityData source, final ExportOrderLineQuantity target)
			throws ConversionException, IllegalArgumentException
	{
		target.setOlqId(source.getOlqId());
		target.setStatus(source.getStatus().getDescription());
		target.setExpressOrderId(source.getExpress_order_id());
		target.setExpressCode(source.getExpress_code());
	}
}
