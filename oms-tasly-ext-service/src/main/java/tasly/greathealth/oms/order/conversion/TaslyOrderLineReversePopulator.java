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
public class TaslyOrderLineReversePopulator extends AbstractPopulator<TaslyOrderLine, TaslyOrderLineData>
{

	@Override
	public void populate(final TaslyOrderLine source, final TaslyOrderLineData target) throws ConversionException,
			IllegalArgumentException
	{
		target.setThird_party_orderline_id(source.getThirdPartyOrderlineId());
		target.setBasequantityunitcode(source.getBaseQuantityUnitCode());
		target.setUnit_discount_fee(source.getUnitDiscountFee());
		target.setGift_item_flag(source.getGiftItemFlag());
		target.setRefundstatus(source.getRefundStatus());
		target.setOrderlinePayment(source.getOrderlinePayment());
		target.setRefundamount(source.getRefundAmount());
		target.setRefundflag(source.getRefundFlag());
	}
}
