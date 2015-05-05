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
package tasly.greathealth.jd.api.order;

import java.util.List;

import tasly.greathealth.oms.api.job.dto.JobTimerShaft;


/**
 * @author libin
 *
 */
public interface TaslyJobTimerShaftFacade
{
	void createJobTimerShaft(JobTimerShaft jobTimerShaft);

	public List<JobTimerShaft> getAll();

	public JobTimerShaft getJobTimerShaftConfig(final String channelSource, final String innerSource, final String event);
}
