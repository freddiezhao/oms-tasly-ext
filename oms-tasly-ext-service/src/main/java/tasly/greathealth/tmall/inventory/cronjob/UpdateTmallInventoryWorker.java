/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.tmall.inventory.cronjob;

import com.hybris.kernel.api.JobWorkerBean;

import java.io.Serializable;

import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.inventory.services.TmallItemService;


/**
 *
 */
public class UpdateTmallInventoryWorker implements JobWorkerBean
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger Log = OmsLoggerFactory.getTmallinventorylog();
	private String beanName;
	private TmallItemService tmallItemService;

	@Override
	public void execute(final Serializable arg0)
	{
		Log.info("库存同步:同步oms库存到天猫  开始 ...");
		tmallItemService.syncProductsToTm();
	}

	@Override
	public void setBeanName(final String name)
	{
		beanName = name;
	}

	@Override
	public String getBeanName()
	{
		return beanName;
	}


	/**
	 * @return the tmallItemService
	 */
	public TmallItemService getTmallItemService()
	{
		return tmallItemService;
	}


	/**
	 * @param tmallItemService the tmallItemService to set
	 */
	public void setTmallItemService(final TmallItemService tmallItemService)
	{
		this.tmallItemService = tmallItemService;
	}

}
