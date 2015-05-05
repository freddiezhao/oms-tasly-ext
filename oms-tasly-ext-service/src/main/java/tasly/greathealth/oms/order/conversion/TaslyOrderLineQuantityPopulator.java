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
package tasly.greathealth.oms.order.conversion;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.oms.api.order.dto.TaslyOrderLineQuantity;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.order.services.TaslyOrderService;


/**
 *
 */
public class TaslyOrderLineQuantityPopulator extends AbstractPopulator<TaslyOrderLineQuantityData, TaslyOrderLineQuantity>
{

	private TaslyOrderService taslyOrderService;

	/**
	 * @return the taslyOrderService
	 */
	public TaslyOrderService getTaslyOrderService()
	{
		return taslyOrderService;
	}

	/**
	 * @param taslyOrderService the taslyOrderService to set
	 */
	public void setTaslyOrderService(final TaslyOrderService taslyOrderService)
	{
		this.taslyOrderService = taslyOrderService;
	}

	@Override
	public void populate(final TaslyOrderLineQuantityData source, final TaslyOrderLineQuantity target) throws ConversionException,
	IllegalArgumentException
	{
		final String name = taslyOrderService.getExpressDataName(source.getExpress_code());
		target.setExpressName(name);
		target.setExpressCode(source.getExpress_code());
		target.setExpressOrderId(source.getExpress_order_id());
		target.setRefundStatus(source.getRefundstatus());
	}
}
