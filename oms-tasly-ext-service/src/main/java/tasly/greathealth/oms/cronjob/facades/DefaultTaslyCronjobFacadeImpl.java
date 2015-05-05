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
package tasly.greathealth.oms.cronjob.facades;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.job.TaslyJobConfigData;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.cronjob.TaslyCronjobFacade;
import tasly.greathealth.oms.api.job.dto.TaslyCronJobConfig;
import tasly.greathealth.oms.cronjob.services.TaslyCronjobConfigService;


/**
 *
 */
public class DefaultTaslyCronjobFacadeImpl implements TaslyCronjobFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTaslyCronjobFacadeImpl.class);

	private TaslyCronjobConfigService taslyCronjobConfigService;
	private Converter<TaslyJobConfigData, TaslyCronJobConfig> cronJobConfigConverter;
	private Converter<TaslyCronJobConfig, TaslyJobConfigData> cronJobConfigReverseConverter;
	private Converters converters;

	@Override
	@Transactional
	public TaslyCronJobConfig getCronjobConfig(final String jobName)
	{
		return cronJobConfigConverter.convert(taslyCronjobConfigService.getJobConfig(jobName));
	}

	@Override
	@Transactional
	public TaslyCronJobConfig createOrUpdateJobConfig(final TaslyCronJobConfig config)
	{
		TaslyJobConfigData cronjobData = null;
		try
		{
			cronjobData = taslyCronjobConfigService.getJobConfig(config.getJobName());
		}
		catch (final ManagedObjectNotFoundException re)
		{
			LOG.info("not find the job with name is " + config.getJobName() + ". Create one.");
		}

		if (cronjobData == null)
		{
			cronjobData = cronJobConfigReverseConverter.convert(config);
		}
		else
		{
			if (StringUtils.isNotEmpty(config.getCronExpression()))
			{
				cronjobData.setCronExpression(config.getCronExpression());
			}
			if (StringUtils.isNotEmpty(config.getTenantId()))
			{
				cronjobData.setTenantId(config.getTenantId());
			}
			cronjobData.setEnable(config.getEnable());
			cronjobData.setDescription(config.getDescription());
			cronjobData.setJobParams(config.getJobParams());
		}
		try
		{
			cronjobData = taslyCronjobConfigService.createOrUpdateJobConfig(cronjobData);
		}
		catch (final SchedulerException e)
		{
			LOG.error("Reschedule failed. " + e.getMessage());
			throw new RuntimeException("Reschedule failed." + e.getMessage());
		}
		return config;
	}

	@Override
	@Transactional
	public List<TaslyCronJobConfig> getAllJobConfig()
	{
		return converters.convertAll(taslyCronjobConfigService.getAllJobConfig(), cronJobConfigConverter);
	}

	/**
	 * @param taslyCronjobConfigService the taslyCronjobConfigService to set
	 */
	public void setTaslyCronjobConfigService(final TaslyCronjobConfigService taslyCronjobConfigService)
	{
		this.taslyCronjobConfigService = taslyCronjobConfigService;
	}

	/**
	 * @param cronJobConfigConverter the cronJobConfigConverter to set
	 */
	public void setCronJobConfigConverter(final Converter<TaslyJobConfigData, TaslyCronJobConfig> cronJobConfigConverter)
	{
		this.cronJobConfigConverter = cronJobConfigConverter;
	}

	/**
	 * @param cronJobConfigReverseConverter the cronJobConfigReverseConverter to set
	 */
	public void setCronJobConfigReverseConverter(
			final Converter<TaslyCronJobConfig, TaslyJobConfigData> cronJobConfigReverseConverter)
	{
		this.cronJobConfigReverseConverter = cronJobConfigReverseConverter;
	}

	/**
	 * @param converters the converters to set
	 */
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}
}
