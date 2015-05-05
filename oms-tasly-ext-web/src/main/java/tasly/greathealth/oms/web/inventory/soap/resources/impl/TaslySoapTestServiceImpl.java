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
package tasly.greathealth.oms.web.inventory.soap.resources.impl;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasly.greathealth.erp.api.order.UpdateOrderFacade;
import tasly.greathealth.erp.api.order.updateorder.dto.Message;
import tasly.greathealth.oms.web.inventory.soap.resources.TaslySoapTestService;


/**
 * Oms Status Update Service
 * libin539 2015-01-06
 */

@WebService(endpointInterface = "tasly.greathealth.oms.web.inventory.soap.resources.TaslySoapTestService")
public class TaslySoapTestServiceImpl implements TaslySoapTestService
{
	private static final Logger LOG = LoggerFactory.getLogger(TaslySoapTestServiceImpl.class);

	UpdateOrderFacade updateOrderFacade;

	/**
	 * @param updateOrderFacade the updateOrderFacade to set
	 */
	public void setUpdateOrderFacade(final UpdateOrderFacade updateOrderFacade)
	{
		this.updateOrderFacade = updateOrderFacade;
	}

	// @Override
	// @WebMethod
	// @Oneway
	// public void updateInventory(final Baseinfo baseinfo, final Message message)
	// {
	// if (baseinfo == null || message == null)
	// {
	// LOG.error("updateInventory parameter is null,please check!");
	// }
	// }

	@Override
	public boolean testUpdateInventory(final String orderId, final Message ordersMessage)
	{
		boolean flag = false;
		try
		{
			final int result = updateOrderFacade.updateOrders(orderId, ordersMessage);
			LOG.info(result + "");
			if (result == 0)
			{
				flag = true;
			}

		}
		catch (final Exception e)
		{
			e.printStackTrace();
			LOG.info(e.getMessage());
		}

		return flag;
	}

	@Override
	public boolean invoking()
	{
		final boolean flag = false;
		// try
		// {
		// taslyDnlogFacade.invoking();
		// flag = true;
		// }
		// catch (final Exception e)
		// {
		// LOG.error(e.getMessage(), e);
		// throw new RuntimeException(e);
		// }
		return flag;
	}
}
