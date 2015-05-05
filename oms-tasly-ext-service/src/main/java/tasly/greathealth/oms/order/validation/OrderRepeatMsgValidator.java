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
package tasly.greathealth.oms.order.validation;

import com.hybris.oms.domain.order.Order;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.slf4j.Logger;

import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.TaslyOrderValidateService;


/**
 *
 */
public class OrderRepeatMsgValidator extends AbstractValidator<Order>
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	private TaslyOrderValidateService taslyOrderValidateService;

	public OrderRepeatMsgValidator()
	{
	}

	@Override
	public void validateInternal(final ValidationContext context, final Order order)
	{
		LOG.debug("Validating order's third_part_id no repeat.");
		if (order instanceof TaslyOrder)
		{
			final TaslyOrder taslyOrder = (TaslyOrder) order;
			final String orginal_order_id = taslyOrder.getOriginalOrderId();
			if (taslyOrderValidateService.validateOrderOriginalOrderIdRepeat(orginal_order_id))
			{
				LOG.error("original_order_id is existing in the order table");
				context.reportFailure(getClass().getName(), new Failure(FieldValidationType.INVALID, "Order.original_order_id",
						taslyOrder.getOriginalOrderId(), "original order id exists!"));
			}
		}
	}

	/**
	 * @param taslyOrderValidateService the taslyOrderValidateService to set
	 */
	public void setTaslyOrderValidateService(final TaslyOrderValidateService taslyOrderValidateService)
	{
		this.taslyOrderValidateService = taslyOrderValidateService;
	}

}
