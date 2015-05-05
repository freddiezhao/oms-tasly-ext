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
package tasly.greathealth.oms.web.inventory.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationFacade;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocationList;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * REST resource to handle request relevant to TaslyItemLocation object.
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/itemlocation")
public class TaslyItemLocationResource
{
	private TaslyItemLocationFacade taslyItemLocationFacade;
	private static final Logger omsLOG = OmsLoggerFactory.getOmsinventorylog();

	@Required
	public void setTaslyItemLocationFacade(final TaslyItemLocationFacade facade)
	{
		this.taslyItemLocationFacade = facade;
	}

	@POST
	public Response create(final TaslyItemLocation taslyItemLocation)
	{
		taslyItemLocationFacade.create(taslyItemLocation);
		return Response.status(Response.Status.CREATED).build();
	}

	@PUT
	public Response batchUpdate(final TaslyItemLocationList itemLocationList)
	{
		final List<TaslyItemLocation> list = itemLocationList.getItemLocations();
		taslyItemLocationFacade.batchUpdate(list);
		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/itemid/{itemid}")
	public List<TaslyItemLocation> getByItemID(@PathParam("itemid") final String itemid)
	{
		final List<TaslyItemLocation> taslyItemLocations = taslyItemLocationFacade.getByItemID(itemid);
		return taslyItemLocations;
	}

	@POST
	@Path("/addchannel/init/{channels}")
	public Response addChannelInit(@PathParam("channels") final String channels)
	{
		try
		{
			taslyItemLocationFacade.addChannelInit(channels);
		}
		catch (final Exception e)
		{
			omsLOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return Response.status(Response.Status.OK).build();
	}
}
