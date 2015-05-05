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
package tasly.greathealth.oms.cronjob.services;

import com.hybris.oms.domain.job.TaslyJobConfigData;

import java.util.List;

import org.quartz.SchedulerException;


/**
 *
 */
public interface TaslyCronjobConfigService
{
	public TaslyJobConfigData createOrUpdateJobConfig(final TaslyJobConfigData config) throws SchedulerException;

	public TaslyJobConfigData getJobConfig(final String jobName);

	public List<TaslyJobConfigData> getAllJobConfig();
}
