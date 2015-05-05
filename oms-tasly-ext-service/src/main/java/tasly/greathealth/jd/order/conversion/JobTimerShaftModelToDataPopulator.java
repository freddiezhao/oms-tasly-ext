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
public class JobTimerShaftModelToDataPopulator extends AbstractPopulator<JobTimerShaft, JobTimerShaftData>
{
	@Override
	public void populate(final JobTimerShaft source, final JobTimerShaftData target)
	{
		target.setChannel_source(source.getChannelSource());
		target.setInner_source(source.getInnerSource());
		target.setEvent(source.getEvent());
		target.setExecuttime(source.getExecutTime());
		target.setIntervaltime(source.getIntervalTime());
	}
}
