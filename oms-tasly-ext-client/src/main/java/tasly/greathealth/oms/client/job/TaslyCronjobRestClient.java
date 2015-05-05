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
package tasly.greathealth.oms.client.job;

import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.util.Collection;

import com.sun.jersey.api.client.GenericType;

import tasly.greathealth.oms.api.cronjob.TaslyCronjobFacade;
import tasly.greathealth.oms.api.job.dto.TaslyCronJobConfig;


/**
 * @author Henter Liu
 */
public class TaslyCronjobRestClient extends DefaultRestClient implements TaslyCronjobFacade
{
	private static final GenericType<Collection<TaslyCronJobConfig>> TASLY_JOBS = new GenericType<Collection<TaslyCronJobConfig>>()
	{
		// DO NOTHING
	};

	@Override
	public TaslyCronJobConfig getCronjobConfig(final String jobName)
	{
		try
		{
			return getClient().call("/jobconfig/" + jobName).get(TaslyCronJobConfig.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public TaslyCronJobConfig createOrUpdateJobConfig(final TaslyCronJobConfig config)
	{
		try
		{
			return getClient().call("/jobconfig").post(TaslyCronJobConfig.class, config).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Collection<TaslyCronJobConfig> getAllJobConfig()
	{
		try
		{
			return getClient().call("/jobconfig").get(TASLY_JOBS).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}
}
