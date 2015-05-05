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
package tasly.greathealth.thirdparty.order.common;

import com.hybris.oms.domain.job.JobTimerShaftData;

import tasly.greathealth.jd.order.service.TaslyJobTimerShaftService;


/**
 *
 * @author libin
 */
public abstract class AbstractProduceOrderService implements ProduceOrderService
{
	private TaslyJobTimerShaftService taslyJobTimerShaftService;

	/**
	 * @param taslyJobTimerShaftService the taslyJobTimerShaftService to set
	 */
	public void setTaslyJobTimerShaftService(final TaslyJobTimerShaftService taslyJobTimerShaftService)
	{
		this.taslyJobTimerShaftService = taslyJobTimerShaftService;
	}

	/**
	 * 获取cronjob时间轴对象
	 *
	 * @param channel
	 * @param innersource
	 * @param event
	 * @return
	 */
	protected JobTimerShaftData getJobTimerShaftConfig(final String channel, final String innersource, final String event)
	{
		final JobTimerShaftData jobTimerShaftData = taslyJobTimerShaftService.getJobTimerShaftConfig(channel, innersource, event);
		return jobTimerShaftData;
	}

}
