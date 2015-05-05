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

import tasly.greathealth.oms.api.order.dto.PendingOrder;
import tasly.greathealth.oms.domain.order.PendingOrderData;
import tasly.greathealth.oms.log.OmsLoggerFactory;


public class PendingOrderPopulator extends AbstractPopulator<PendingOrderData, PendingOrder>
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();

	@Override
	public void populate(final PendingOrderData source, final PendingOrder target) throws ConversionException
	{
		LOG.debug("PendingOrderPopulator Start");

		target.setTid(source.getTid());
		target.setOid(source.getOid());
		target.setEventType(tasly.greathealth.oms.api.order.dto.EventType.valueOf(source.getEventtype().toString()));
		target.setErrorMsg(source.getErrormsg());
		target.setRawMsg(source.getRawmsg());
		target.setRefundFee(source.getRefundfee());
		target.setState(tasly.greathealth.oms.api.order.dto.OrderState.valueOf(source.getState().toString()));
		target.setChannelSource(tasly.greathealth.oms.api.order.dto.ChannelSource.valueOf(source.getChannelsource().toString()));
		target.setInnerSource(tasly.greathealth.oms.api.order.dto.InnerSource.valueOf(source.getInnersource().toString()));
	}
}
