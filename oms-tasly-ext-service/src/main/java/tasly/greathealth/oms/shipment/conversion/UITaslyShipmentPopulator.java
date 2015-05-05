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
package tasly.greathealth.oms.shipment.conversion;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.ui.api.shipment.UIShipment;

import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.order.services.TaslyOrderService;


/**
 * @author Henter Liu
 */
public class UITaslyShipmentPopulator extends AbstractPopulator<ShipmentData, UIShipment>
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
	public void populate(final ShipmentData source, final UIShipment target) throws ConversionException, IllegalArgumentException
	{
		final OrderData orderData = source.getOrderFk();
		final String orderId = orderData.getOrderId();
		final TaslyOrderData tod = taslyOrderService.getTaslyOrderDataByOrderId(orderId);
		target.setOriginalOrderId(tod.getOriginal_order_id());
	}
}
