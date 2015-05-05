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
package tasly.greathealth.thirdparty.inventory.cronjob;

import com.hybris.kernel.api.JobWorkerBean;

import java.io.Serializable;

import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.inventory.services.UpdateItemService;


/**
 *
 */
public class AbstractUpdateInventoryWorker implements JobWorkerBean
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger Log = OmsLoggerFactory.getOmsinventorylog();
	protected String beanName;
	protected String storeName;

	/**
	 * @return the storeName
	 */
	public String getStoreName()
	{
		return storeName;
	}

	/**
	 * @param storeName the storeName to set
	 */
	public void setStoreName(final String storeName)
	{
		this.storeName = storeName;
	}






	protected UpdateItemService updateItemService;

	@Override
	public void execute(final Serializable arg0)
	{
		Log.info("库存同步:同步oms库存到" + getStoreName() + "开始 ...");
		updateItemService.syncInventoryToStore();
	}

	/**
	 * @return the updateItemService
	 */
	public UpdateItemService getUpdateItemService()
	{
		return updateItemService;
	}

	/**
	 * @param updateItemService the updateItemService to set
	 */
	public void setUpdateItemService(final UpdateItemService updateItemService)
	{
		this.updateItemService = updateItemService;
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



}
