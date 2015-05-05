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
package tasly.greathealth.oms.web.principal.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.principal.DataPrincipalFacade;
import tasly.greathealth.oms.api.principal.dto.DataPrincipal;


/**
 * REST resource to handle request relevant to DataPrincipal object.
 *
 * @author Henter Liu
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/principal")
public class DataPrincipalResource
{
	private DataPrincipalFacade dataPrincipalFacade;

	/**
	 * @param dataPrincipalFacade the dataPrincipalFacade to set
	 */
	@Required
	public void setDataPrincipalFacade(final DataPrincipalFacade dataPrincipalFacade)
	{
		this.dataPrincipalFacade = dataPrincipalFacade;
	}

	@PUT
	public Response batchUpdate(final List<DataPrincipal> dataPrincipals)
	{
		try
		{
			final List<DataPrincipal> list = dataPrincipalFacade.batchUpdate(dataPrincipals);
			final GenericEntity<List<DataPrincipal>> entity = new GenericEntity<List<DataPrincipal>>(list)
			{
				// Empty block
			};
			return Response.ok(entity).build();
		}
		catch (final Exception e)
		{
			return Response.ok(e.getMessage()).build();
		}
	}

	@GET
	@Path("/users")
	public Response getAllUsers()
	{
		try
		{
			final List<DataPrincipal> list = dataPrincipalFacade.getAllUsers();
			final GenericEntity<List<DataPrincipal>> entity = new GenericEntity<List<DataPrincipal>>(list)
			{
				// Empty block
			};
			return Response.ok(entity).build();
		}
		catch (final Exception e)
		{
			return Response.ok(e.getMessage()).build();
		}
	}

	@GET
	@Path("/groups")
	public Response getAllUserGroups()
	{
		try
		{
			final List<DataPrincipal> list = dataPrincipalFacade.getAllUserGroups();
			final GenericEntity<List<DataPrincipal>> entity = new GenericEntity<List<DataPrincipal>>(list)
			{
				// Empty block
			};
			return Response.ok(entity).build();
		}
		catch (final Exception e)
		{
			return Response.ok(e.getMessage()).build();
		}
	}

	@GET
	@Path("/{uid}")
	public Response getPrincipalByUid(@PathParam("uid") final String uid)
	{
		try
		{
			final DataPrincipal principal = dataPrincipalFacade.getPrincipalByUid(uid);
			return Response.ok(principal).build();
		}
		catch (final Exception e)
		{
			return Response.ok(e.getMessage()).build();
		}
	}

	@POST
	public Response updatePrincipal(final DataPrincipal dataPrincipal)
	{
		try
		{
			final DataPrincipal principal = dataPrincipalFacade.updatePrincipal(dataPrincipal);
			return Response.ok(principal).build();
		}
		catch (final Exception e)
		{
			return Response.ok(e.getMessage()).build();
		}
	}
}
