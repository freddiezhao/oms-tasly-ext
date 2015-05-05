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

import tasly.greathealth.oms.api.order.dto.TaslyPaymentInfo;
import tasly.greathealth.oms.domain.order.TaslyPaymentInfoData;


/**
 *
 */
public class TaslyPaymentInfoReversePopulator extends AbstractPopulator<TaslyPaymentInfo, TaslyPaymentInfoData>
{
	@Override
	public void populate(final TaslyPaymentInfo source, final TaslyPaymentInfoData target) throws ConversionException,
	IllegalArgumentException
	{
		target.setIssuedate(source.getIssueDate());
	}

}
