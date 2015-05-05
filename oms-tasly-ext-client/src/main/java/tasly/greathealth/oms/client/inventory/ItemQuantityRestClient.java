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
package tasly.greathealth.oms.client.inventory;

import com.hybris.commons.client.GenericRestClient;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.domain.exception.HybrisSystemException;

import java.util.List;

import tasly.greathealth.oms.api.inventory.ItemQuantityFacade;
import tasly.greathealth.oms.api.inventory.dto.ItemQuantityDto;
import tasly.greathealth.oms.api.inventory.dto.ItemQuantityElement;
import tasly.greathealth.oms.api.inventory.dto.ItemQuantityList;


/**
 * @author Henter Liu
 */
public class ItemQuantityRestClient implements ItemQuantityFacade
{
	private GenericRestClient client;

	public void setClient(final GenericRestClient genericRestClient)
	{
		this.client = genericRestClient;
	}

	public GenericRestClient getClient()
	{
		return client;
	}

	@Override
	public boolean updateItemQuantity()
	{
		// not for rest client
		return false;
	}

	@Override
	public boolean batchUpdate(final String sku, final List<ItemQuantityElement> list)
	{
		try
		{
			final ItemQuantityDto itemQuantityDto = new ItemQuantityDto();
			itemQuantityDto.setSku(sku);
			final ItemQuantityList itemQuantityList = new ItemQuantityList();
			itemQuantityList.initializeItemQuantities(list);
			itemQuantityDto.setList(itemQuantityList);
			return getClient().call("/itemquantity").put(ItemQuantityDto.class, itemQuantityDto).getResult() != null;
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
		catch (final Exception e)
		{
			if (e instanceof javax.ws.rs.WebApplicationException)
			{
				if (e.getCause().toString().startsWith("javax.xml.bind.UnmarshalException"))
				{
					return true;
				}
			}
			return false;
		}
	}
}
