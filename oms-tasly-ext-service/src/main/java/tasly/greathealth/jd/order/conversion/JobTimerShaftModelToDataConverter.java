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

import com.hybris.commons.conversion.impl.AbstractPopulatingConverter;
import com.hybris.oms.domain.job.JobTimerShaftData;

import tasly.greathealth.jd.order.service.TaslyJobTimerShaftService;
import tasly.greathealth.oms.api.job.dto.JobTimerShaft;


/**
 *
 * @author libin
 */
public class JobTimerShaftModelToDataConverter extends AbstractPopulatingConverter<JobTimerShaft, JobTimerShaftData>
{
	private TaslyJobTimerShaftService taslyJobTimerShaftService;

	/**
	 * @param taslyJobTimerShaftService the taslyJobTimerShaftService to set
	 */
	public void setTaslyJobTimerShaftService(final TaslyJobTimerShaftService taslyJobTimerShaftService)
	{
		this.taslyJobTimerShaftService = taslyJobTimerShaftService;
	}

	@Override
	protected JobTimerShaftData createTarget()
	{
		return taslyJobTimerShaftService.createJobTimerShaft();
	}
}
