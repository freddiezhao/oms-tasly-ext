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

import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;


/**
 *
 */
public class TaslyOrderLinePopulator extends AbstractPopulator<TaslyOrderLineData, TaslyOrderLine>
{

	@Override
	public void populate(final TaslyOrderLineData source, final TaslyOrderLine target) throws ConversionException,
	IllegalArgumentException
	{
		target.setThirdPartyOrderlineId(source.getThird_party_orderline_id());
		target.setBaseQuantityUnitCode(source.getBasequantityunitcode());
		target.setUnitDiscountFee(source.getUnit_discount_fee());
		target.setGiftItemFlag(source.getGift_item_flag());
		target.setRefundStatus(source.getRefundstatus());
		target.setOrderlinePayment(source.getOrderlinePayment());
		target.setRefundAmount(source.getRefundamount());
		target.setRefundFlag(source.getRefundflag());
	}

}
