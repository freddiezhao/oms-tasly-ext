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
 *
 *
 */
package tasly.greathealth.oms.order.services.dataaccess;
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


import com.hybris.kernel.api.AggregateOperator;
import com.hybris.kernel.api.annotations.Aggregate;
import com.hybris.kernel.api.annotations.AggregationPojo;
import com.hybris.kernel.api.annotations.Grouped;
import com.hybris.oms.service.ats.AtsQuantityAggregate;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;

@AggregationPojo(name = "AggTaslyOrderLineQuantityBySkuStatus", typeCode = TaslyOrderLineQuantityData._TYPECODE, live = true, sendEvent = true, cronExpression = "0 0 0 * * ?")
public class AggTaslyOrderLineQuantityBySkuStatus implements AtsQuantityAggregate
{
	@Grouped(priority = 1, attribute = "orderLine.skuId")
	public String sku; // NOPMD aggregation fields have to be public

	@Grouped(priority = 2, attribute = "status.statusCode")
	public String statusCode; // NOPMD aggregation fields have to be public

	@Aggregate(attribute = "quantityValue", operator = AggregateOperator.SUM)
	public int quantity; // NOPMD aggregation fields have to be public

	@Override
	public String getSku()
	{
		return this.sku;
	}

	@Override
	public String getStatusCode()
	{
		return this.statusCode;
	}

	@Override
	public int getQuantity()
	{
		return this.quantity;
	}
}
