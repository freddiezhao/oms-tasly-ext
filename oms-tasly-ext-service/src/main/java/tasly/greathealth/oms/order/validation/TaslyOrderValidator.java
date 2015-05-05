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
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.impl.order.OrderValidator;


/**
 *
 */
public class TaslyOrderValidator extends OrderValidator
{
	private Validator<Order> orderRepeatValidator;

	@Override
	public void validateInternal(final ValidationContext context, final Order order)
	{
		orderRepeatValidator.validate("Order.original_order_id", context, order);
		super.validateInternal(context, order);
	}



	/**
	 * @param orderRepeatValidator the orderRepeatValidator to set
	 */
	public void setOrderRepeatValidator(final Validator<Order> orderRepeatValidator)
	{
		this.orderRepeatValidator = orderRepeatValidator;
	}

}
