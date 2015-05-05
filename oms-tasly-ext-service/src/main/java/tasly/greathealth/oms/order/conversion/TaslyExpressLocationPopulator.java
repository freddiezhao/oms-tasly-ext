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

import org.slf4j.Logger;

import tasly.greathealth.oms.api.order.dto.Expresslocation;
import tasly.greathealth.oms.domain.order.ExpresslocationsData;
import tasly.greathealth.oms.log.OmsLoggerFactory;


public class TaslyExpressLocationPopulator extends AbstractPopulator<ExpresslocationsData, Expresslocation>
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();

	@Override
	public void populate(final ExpresslocationsData source, final Expresslocation target) throws ConversionException
	{
		LOG.debug("ExpressLocationPopulator Start");

		target.setChannelSource(source.getChannel_source());
		target.setExpressCode(source.getExpress_code());
		target.setInnerSource(source.getInner_source());
		target.setProvince(source.getProvince());
	}
}
