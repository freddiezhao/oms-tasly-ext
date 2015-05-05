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
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.export.api.order.dto.ExportOrderLine;
import tasly.greathealth.oms.export.api.order.dto.ExportOrderLineQuantity;


/**
 * @author Henter Liu
 */
public class ExportOrderLinePopulator extends AbstractPopulator<TaslyOrderLineData, ExportOrderLine>
{
	private Converters converters;
	private Converter<OrderLineQuantityData, ExportOrderLineQuantity> exportOrderLineQuantityConverter;

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setExportOrderLineQuantityConverter(
			final Converter<OrderLineQuantityData, ExportOrderLineQuantity> exportOrderLineQuantityConverter)
	{
		this.exportOrderLineQuantityConverter = exportOrderLineQuantityConverter;
	}

	@Override
	public void populate(final TaslyOrderLineData source, final ExportOrderLine target)
			throws ConversionException, IllegalArgumentException
	{
		target.setOrderLineId(source.getOrderLineId());
		target.setExportOrderLineQuantities(
				this.converters.convertAll(source.getOrderLineQuantities(), this.exportOrderLineQuantityConverter));
		target.setNote(source.getNote());
		target.setQuantityValue(source.getQuantityValue());
		target.setUnitPriceValue(source.getUnitPriceValue());
		target.setThirdPartyOrderlineId(source.getThird_party_orderline_id());
		target.setUnitDiscountFee(source.getUnit_discount_fee());
		target.setGiftItemFlag(source.getGift_item_flag());
		target.setRefundStatus(source.getRefundstatus());
	}
}
