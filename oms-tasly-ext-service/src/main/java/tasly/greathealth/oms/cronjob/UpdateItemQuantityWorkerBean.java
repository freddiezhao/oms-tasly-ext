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
package tasly.greathealth.oms.cronjob;

import com.hybris.kernel.api.JobWorkerBean;

import java.io.Serializable;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import tasly.greathealth.oms.api.inventory.ItemQuantityFacade;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * auto update item quantity by daily
 * qiuxingjie create for TS-194
 */
@Component
public class UpdateItemQuantityWorkerBean implements JobWorkerBean
{
	private static final long serialVersionUID = 7545011477694426262L;

	private ItemQuantityFacade itemQuantityFacade;

	/**
	 * @return the itemQuantityFacade
	 */
	public ItemQuantityFacade getItemQuantityFacade()
	{
		return itemQuantityFacade;
	}

	/**
	 * @param itemQuantityFacade the itemQuantityFacade to set
	 */
	public void setItemQuantityFacade(final ItemQuantityFacade itemQuantityFacade)
	{
		this.itemQuantityFacade = itemQuantityFacade;
	}

	private static final Logger omsLOG = OmsLoggerFactory.getOmsinventorylog();

	private String beanName;

	@Override
	public void execute(final Serializable arg0)
	{
		omsLOG.info("Update quantity start:");
		try
		{
			omsLOG.info("Update quantity running now...");
			itemQuantityFacade.updateItemQuantity();
			omsLOG.info("Update quantity finished!");
		}
		catch (final Exception e)
		{
			omsLOG.error("Update quantity failed! Error id " + e);
		}

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
