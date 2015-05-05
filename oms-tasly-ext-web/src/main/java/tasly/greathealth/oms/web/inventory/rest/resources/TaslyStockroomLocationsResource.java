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
package tasly.greathealth.oms.web.inventory.rest.resources;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.annotation.Secured;

import tasly.greathealth.oms.api.inventory.TaslyStockroomLocationFacade;
import tasly.greathealth.oms.api.inventory.TaslyStockroomLocationQueryObject;
import tasly.greathealth.oms.api.inventory.dto.StockroomLocation;
import tasly.greathealth.oms.api.inventory.dto.TaslyStockroomLocationList;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 *
 */
@Consumes({"application/xml"})
@Produces({"application/xml"})
@Path("/taslyStockrooms")
public class TaslyStockroomLocationsResource
{
	private static final Logger LOGGER = OmsLoggerFactory.getOmsinventorylog();

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	private TaslyStockroomLocationFacade taslyStockroomLocationFacade;

	/**
	 * @return the taslyStockroomLocationFacade
	 */
	public TaslyStockroomLocationFacade getTaslyStockroomLocationFacade()
	{
		return taslyStockroomLocationFacade;
	}

	/**
	 * @param taslyStockroomLocationFacade the taslyStockroomLocationFacade to set
	 */
	@Required
	public void setTaslyStockroomLocationFacade(final TaslyStockroomLocationFacade taslyStockroomLocationFacade)
	{
		this.taslyStockroomLocationFacade = taslyStockroomLocationFacade;
	}


	@Path("/findAllStockRoomLocationByQuery")
	@GET
	@TypeHint(StockroomLocation.class)
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response findAllStockRoomLocations(@Context final UriInfo uriInfo)
	{
		// YTODO Auto-generated method stub
		LOGGER.trace("findStockRoomLocationsByQuery");
		final TaslyStockroomLocationQueryObject queryObject = new TaslyStockroomLocationQueryObject();
		final TaslyStockroomLocationList result = new TaslyStockroomLocationList();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<StockroomLocation> pagedLocations = this.taslyStockroomLocationFacade
				.findTaslyStockRoomLocationsByQuery(queryObject);
		if (pagedLocations.getResults() != null)
		{
			result.initializeLocations(pagedLocations.getResults());
		}
		final GenericEntity<TaslyStockroomLocationList> entity = new GenericEntity<TaslyStockroomLocationList>(result)
				{
			// DONOTHING
				};

				final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedLocations.getNextPage()
						.intValue(), pagedLocations.getPreviousPage().intValue(), pagedLocations.getTotalPages().intValue(), pagedLocations
						.getTotalRecords().longValue());

				return responseBuilder.entity(entity).build();
	}

	@Path("/updateStockroomLocation")
	@PUT
	@TypeHint(StockroomLocation.class)
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response updateStockroomLocation(final StockroomLocation stockroomLocation)
	{
		LOGGER.trace("updateStockroomLocation");
		final StockroomLocation locationSaved = taslyStockroomLocationFacade.updateStockroomLocation(stockroomLocation);
		return Response.ok().entity(locationSaved).build();

	}

}
