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
package tasly.greathealth.jd.order.service;

import com.hybris.oms.domain.job.JobTimerShaftData;

import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;


/**
 *
 * @author libin
 */
public interface TaslyJobTimerShaftService
{
	public JobTimerShaftData createJobTimerShaft();

	public JobTimerShaftData createOrUpdateJobTimerShaftConfig(final JobTimerShaftData cornjobtimershaftData)
			throws SchedulerException;

	public JobTimerShaftData getJobTimerShaftConfig(final String channelSource, final String innerSource, final String event);

	public List<JobTimerShaftData> getAllJobTimerShaftConfig();

	boolean update(final String channelSource, final String innerSource, final String event, final Date executtime)
			throws Exception;
}
