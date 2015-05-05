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
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.HybrisSystemException;

import java.util.List;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationFacade;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocationList;


public class TaslyItemLocationRestClient implements TaslyItemLocationFacade
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
	public void create(final TaslyItemLocation taslyItemLocation)
	{
		try
		{
			getClient().call("/itemlocation").post(taslyItemLocation).getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public boolean batchUpdate(final List<TaslyItemLocation> taslyItemLocations)
	{
		try
		{
			final TaslyItemLocationList itemLocationList = new TaslyItemLocationList();
			itemLocationList.initializeItemLocations(taslyItemLocations);
			return getClient().call("/itemlocation").put(TaslyItemLocationList.class, itemLocationList).getResult() != null;
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

	@Override
	public List<TaslyItemLocation> getByItemID(final String itemID) throws EntityNotFoundException
	{
		try
		{
			getClient().call("/itemlocation/itemid/" + itemID).post(TaslyItemLocation.class).getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
		return null;
	}

	@Override
	public boolean addChannelInit(final String channels) throws Exception
	{
		// YTODO Auto-generated method stub
		return false;
	}
}
