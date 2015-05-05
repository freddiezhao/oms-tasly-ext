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
package tasly.greathealth.oms.client.principal;

import com.hybris.commons.client.GenericRestClient;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.domain.exception.HybrisSystemException;

import java.util.List;

import javax.ws.rs.core.GenericEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.sun.jersey.api.client.GenericType;

import tasly.greathealth.oms.api.principal.DataPrincipalFacade;
import tasly.greathealth.oms.api.principal.dto.DataPrincipal;


/**
 * @author Henter Liu
 */
public class DataPrincipalRestClient implements DataPrincipalFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DataPrincipalRestClient.class);

	private GenericRestClient client;

	public GenericRestClient getClient()
	{
		return this.client;
	}

	@Required
	public void setClient(final GenericRestClient genericRestClient)
	{
		this.client = genericRestClient;
	}

	@Override
	public List<DataPrincipal> batchUpdate(final List<DataPrincipal> dataPrincipals)
	{
		try
		{
			final GenericEntity<List<DataPrincipal>> genericEntity = new GenericEntity<List<DataPrincipal>>(dataPrincipals)
			{
				// empty block
			};
			return getClient().call("principal").put(new GenericType<List<DataPrincipal>>()
			{
				// empty block
			}, genericEntity).result();
		}
		catch (final RestResponseException e)
		{
			LOGGER.error(e.unwrap(HybrisSystemException.class).toString());
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public List<DataPrincipal> getAllUsers()
	{
		try
		{
			return getClient().call("principal/users").get(new GenericType<List<DataPrincipal>>()
			{
				// empty block
			}).result();
		}
		catch (final RestResponseException e)
		{
			LOGGER.error(e.unwrap(HybrisSystemException.class).toString());
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public List<DataPrincipal> getAllUserGroups()
	{
		try
		{
			return getClient().call("principal/groups").get(new GenericType<List<DataPrincipal>>()
			{
				// empty block
			}).result();
		}
		catch (final RestResponseException e)
		{
			LOGGER.error(e.unwrap(HybrisSystemException.class).toString());
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public DataPrincipal getPrincipalByUid(final String uid)
	{
		try
		{
			return getClient().call("principal/%s", new Object[]{uid}).get(DataPrincipal.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public DataPrincipal updatePrincipal(final DataPrincipal dataPrincipal)
	{
		try
		{
			return getClient().call("principal").post(DataPrincipal.class, dataPrincipal).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}
}
