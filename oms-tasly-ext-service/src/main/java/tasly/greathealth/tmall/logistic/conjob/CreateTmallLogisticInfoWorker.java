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
package tasly.greathealth.tmall.logistic.conjob;

import com.hybris.kernel.api.JobWorkerBean;

import java.io.Serializable;

import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.logistic.service.TmallLogisticService;


/**
 *
 */
public class CreateTmallLogisticInfoWorker implements JobWorkerBean
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = OmsLoggerFactory.getTmalllogisticlog();
	private String beanName;

	private TmallLogisticService defaultTmallLogisticService;

	@Override
	public void execute(final Serializable arg0)
	{
		LOG.info("同步运单到天猫 开始...");
		defaultTmallLogisticService.createLogistic();
	}

	/**
	 * @return the beanName
	 */
	@Override
	public String getBeanName()
	{
		return beanName;
	}

	/**
	 * @param beanName the beanName to set
	 */
	@Override
	public void setBeanName(final String beanName)
	{
		this.beanName = beanName;
	}

	/**
	 * @return the defaultTmallLogisticService
	 */
	public TmallLogisticService getDefaultTmallLogisticService()
	{
		return defaultTmallLogisticService;
	}

	/**
	 * @param defaultTmallLogisticService the defaultTmallLogisticService to set
	 */
	public void setDefaultTmallLogisticService(final TmallLogisticService defaultTmallLogisticService)
	{
		this.defaultTmallLogisticService = defaultTmallLogisticService;
	}

}
