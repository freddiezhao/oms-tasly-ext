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
package tasly.greathealth.jd.order.conversion;

import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.job.JobTimerShaftData;

import tasly.greathealth.oms.api.job.dto.JobTimerShaft;


/**
 *
 * @author libin
 */
public class JobTimerShaftDataToModelPopulator extends AbstractPopulator<JobTimerShaftData, JobTimerShaft>
{
	@Override
	public void populate(final JobTimerShaftData source, final JobTimerShaft target)
	{
		target.setChannelSource(source.getChannel_source());
		target.setInnerSource(source.getInner_source());
		target.setEvent(source.getEvent());
		target.setExecutTime(source.getExecuttime());
		target.setIntervalTime(source.getIntervaltime());
	}
}
