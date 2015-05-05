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
package tasly.greathealth.thirdparty.workflow;

import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.shipment.strategy.impl.DefaultOlqGroupingStrategy;

import tasly.greathealth.oms.domain.order.TaslyOrderLineData;


/**
 *
 */
public class TaslyOlqGroupingStrategy extends DefaultOlqGroupingStrategy
{

	@Override
	protected String makeKey(final OrderLineQuantityData olq)
	{
		final String locationId = olq.getStockroomLocationId();
		final String statusCode = olq.getStatus().getStatusCode();
		final OrderLineData orderLineData = olq.getOrderLine();
		if (orderLineData instanceof TaslyOrderLineData)
		{
			final TaslyOrderLineData taslyOrderLineData = (TaslyOrderLineData) orderLineData;
			final String orderLineId = taslyOrderLineData.getThird_party_orderline_id();
			return String.format("%s|%s|%s", locationId, statusCode, orderLineId);
		}
		return String.format("%s|%s", locationId, statusCode);
	}
}
