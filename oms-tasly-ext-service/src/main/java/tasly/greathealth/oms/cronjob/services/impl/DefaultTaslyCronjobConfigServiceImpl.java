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
package tasly.greathealth.oms.cronjob.services.impl;

import com.hybris.kernel.api.JobDetails;
import com.hybris.kernel.api.JobSchedulerService;
import com.hybris.oms.domain.job.TaslyJobConfigData;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import tasly.greathealth.oms.cronjob.services.TaslyCronjobConfigService;



/**
 *
 */
public class DefaultTaslyCronjobConfigServiceImpl extends AbstractHybrisService implements TaslyCronjobConfigService,
ApplicationContextAware
{

	private static final Logger LOG = LoggerFactory.getLogger(DefaultTaslyCronjobConfigServiceImpl.class);

	private JobSchedulerService jobSchedulerService;
	private Scheduler scheduler;
	private ApplicationContext applicationContext;


	@Override
	public TaslyJobConfigData createOrUpdateJobConfig(final TaslyJobConfigData configData) throws SchedulerException
	{
		LOG.info("invoke serivce createOrUpdateJobConfig");
		configData.setSuccess(false);
		JobDetails jobDetails = jobSchedulerService.get(configData.getJobName());
		if (jobDetails == null)
		{
			jobDetails = (JobDetails) applicationContext.getBean(configData.getJobName());
			if (jobDetails == null)
			{
				configData.setDescription("Update the cronjob failed, there is no :" + configData.getJobName() + " job !");
				return this.getPersistenceManager().createOrUpdate(configData);
			}
			jobSchedulerService.scheduleJob(jobDetails.getWorker(), configData.getJobName(), configData.getTenantId(),
					configData.getCronExpression(), configData.getJobParams());
			jobSchedulerService.resume(jobDetails);
		}
		final String tenant = jobDetails.getTenant().isEmpty() ? configData.getTenantId() : jobDetails.getTenant();
		final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobDetails.getJobName(), tenant));

		@SuppressWarnings("unchecked")
		final List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobDetail.getKey());
		final Trigger orginalTrigger = triggers.get(0);

		try
		{
			final CronExpression cronExpression = new CronExpression(configData.getCronExpression());
			if (CronExpression.isValidExpression(configData.getCronExpression()) && isValidExpression(cronExpression))
			{
				final TriggerKey triggerKey = orginalTrigger.getKey();
				final CronTriggerImpl trigger = new CronTriggerImpl();
				trigger.setCronExpression(cronExpression);
				trigger.setJobName(jobDetail.getKey().getName());
				trigger.setJobKey(orginalTrigger.getJobKey());
				trigger.setKey(triggerKey);
				try
				{
					scheduler.addJob(jobDetail, true);
					if (scheduler.checkExists(triggerKey))
					{
						scheduler.rescheduleJob(triggerKey, trigger);
					}

					if (configData.isEnable())
					{
						scheduler.resumeJob(jobDetail.getKey());
					}
					else
					{
						scheduler.pauseJob(jobDetail.getKey());
					}
					configData.setSuccess(true);
				}
				catch (final SchedulerException e)
				{
					LOG.error(e.getMessage(), e);
					throw new IllegalArgumentException(e);
				}
			}
			else
			{
				throw new IllegalArgumentException("The cron expression is wrong format.");
			}
		}
		catch (final ParseException e)
		{
			LOG.error(e.getMessage(), e);
			throw new IllegalArgumentException(e);
		}
		return this.getPersistenceManager().createOrUpdate(configData);
	}

	private boolean isValidExpression(final CronExpression cronExpression)
	{
		final CronTriggerImpl trigger = new CronTriggerImpl();
		trigger.setCronExpression(cronExpression);
		final Date date = trigger.computeFirstFireTime(null);
		return date != null && date.after(new Date());
	}

	@Override
	public TaslyJobConfigData getJobConfig(final String jobName)
	{
		return this.getPersistenceManager().getByIndex(TaslyJobConfigData.UX_TASLYJOBCONFIGDATA_JOBNAME, jobName);
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<TaslyJobConfigData> getAllJobConfig()
	{
		return this.getPersistenceManager().createCriteriaQuery(TaslyJobConfigData.class).resultList();
	}

	/**
	 * @param jobSchedulerService the jobSchedulerService to set
	 */
	public void setJobSchedulerService(final JobSchedulerService jobSchedulerService)
	{
		this.jobSchedulerService = jobSchedulerService;
	}

	/**
	 * @param scheduler the scheduler to set
	 */
	public void setScheduler(final Scheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}
}
