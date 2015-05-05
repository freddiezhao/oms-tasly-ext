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
package tasly.greathealth.jd.order.facades;

import com.hybris.oms.domain.job.JobTimerShaftData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.jd.api.order.TaslyJobTimerShaftFacade;
import tasly.greathealth.jd.order.conversion.JobTimerShaftDataToModelConverter;
import tasly.greathealth.jd.order.conversion.JobTimerShaftModelToDataConverter;
import tasly.greathealth.jd.order.service.TaslyJobTimerShaftService;
import tasly.greathealth.oms.api.job.dto.JobTimerShaft;


/**
 * Default implementation of {@link TaslyJobTimerShaftFacade}.
 * 
 * @author libin
 */
public class DefaultTaslyJobTimerShaftFacade implements TaslyJobTimerShaftFacade
{
	private TaslyJobTimerShaftService taslyJobTimerShaftService;
	private JobTimerShaftModelToDataConverter jobTimerShaftModelToDataConverter;
	private JobTimerShaftDataToModelConverter jobTimerShaftDataToModelConverter;


	/**
	 * @param jobTimerShaftDataToModelConverter the jobTimerShaftDataToModelConverter to set
	 */
	public void setJobTimerShaftDataToModelConverter(final JobTimerShaftDataToModelConverter jobTimerShaftDataToModelConverter)
	{
		this.jobTimerShaftDataToModelConverter = jobTimerShaftDataToModelConverter;
	}

	/**
	 * @param taslyJobTimerShaftService the taslyJobTimerShaftService to set
	 */
	public void setTaslyJobTimerShaftService(final TaslyJobTimerShaftService taslyJobTimerShaftService)
	{
		this.taslyJobTimerShaftService = taslyJobTimerShaftService;
	}

	/**
	 * @param jobTimerShaftModelToDataConverter the jobTimerShaftModelToDataConverter to set
	 */
	public void setJobTimerShaftModelToDataConverter(final JobTimerShaftModelToDataConverter jobTimerShaftModelToDataConverter)
	{
		this.jobTimerShaftModelToDataConverter = jobTimerShaftModelToDataConverter;
	}

	@Override
	@Transactional
	public void createJobTimerShaft(final JobTimerShaft jobTimerShaft)
	{
		jobTimerShaftModelToDataConverter.convert(jobTimerShaft);
	}

	@Override
	@Transactional
	public List<JobTimerShaft> getAll()
	{
		final List<JobTimerShaft> jobTimerShafts = new ArrayList<JobTimerShaft>();
		final List<JobTimerShaftData> jobTimerShaftDatas = taslyJobTimerShaftService.getAllJobTimerShaftConfig();
		for (final JobTimerShaftData jobTimerShaftData : jobTimerShaftDatas)
		{
			final JobTimerShaft jobTimerShaft = jobTimerShaftDataToModelConverter.convert(jobTimerShaftData);
			jobTimerShafts.add(jobTimerShaft);
		}
		return jobTimerShafts;
	}

	@Override
	@Transactional
	public JobTimerShaft getJobTimerShaftConfig(final String channelSource, final String innerSource, final String event)
	{
		final JobTimerShaftData jobTimerShaftData = taslyJobTimerShaftService.getJobTimerShaftConfig(channelSource, innerSource,
				event);
		return jobTimerShaftDataToModelConverter.convert(jobTimerShaftData);
	}
}
