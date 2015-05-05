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
package tasly.greathealth.jd.order.service.impl;

import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.job.JobTimerShaftData;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.jd.order.service.TaslyJobTimerShaftService;


/**
 *
 * @author libin
 */
public class TaslyJobTimerShaftServiceImpl extends AbstractHybrisService implements TaslyJobTimerShaftService
{
	private TaslyJDQueryFactory jdQueryFactory;

	/**
	 * @param jdQueryFactory the jdQueryFactory to set
	 */
	public void setJdQueryFactory(final TaslyJDQueryFactory jdQueryFactory)
	{
		this.jdQueryFactory = jdQueryFactory;
	}

	@Override
	@Transactional
	public JobTimerShaftData createJobTimerShaft()
	{
		return super.getPersistenceManager().create(JobTimerShaftData.class);
	}

	@Override
	public JobTimerShaftData createOrUpdateJobTimerShaftConfig(final JobTimerShaftData cornjobtimershaftData)
			throws SchedulerException
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public JobTimerShaftData getJobTimerShaftConfig(final String channelSource, final String innerSource, final String event)
	{
		try
		{
			return this.findOneSingle(this.jdQueryFactory.findJobTimerShaftByQuery(channelSource, innerSource, event));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public List<JobTimerShaftData> getAllJobTimerShaftConfig()
	{
		try
		{
			return findAll(super.getPersistenceManager().createCriteriaQuery(JobTimerShaftData.class));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	public boolean update(final String channelSource, final String innerSource, final String event, final Date executtime)
			throws Exception
	{
		final JobTimerShaftData jobTimerShaftData = this.getJobTimerShaftConfig(channelSource, innerSource, event);
		jobTimerShaftData.setExecuttime(executtime);

		return false;
	}
}
