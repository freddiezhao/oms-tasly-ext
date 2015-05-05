package com.hybris.oms.rest.web.inventory;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationList;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.ClientResponse;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationQueryObject;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocationList;


@Component
@Path("/inventory")
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
public class InventoryResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryResource.class);

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	@Autowired
	private InventoryFacade inventoryServiceApi;

	@DELETE
	@Secured({"ROLE_admin"})
	public void deleteInventory(final OmsInventory inventory)
	{
		this.inventoryServiceApi.deleteInventory(inventory);
	}

	@GET
	@Secured({"ROLE_admin", "ROLE_accelerator", "ROLE_fulfillmentmanager", "ROLE_inventorymanager"})
	public Response findItemLocationsByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findItemLocationsByQuery");
		final ItemLocationList result = new ItemLocationList();
		final TaslyItemLocationList cloneresult = new TaslyItemLocationList();
		final List<TaslyItemLocation> cloneItemLocations = new ArrayList<TaslyItemLocation>();

		final TaslyItemLocationQueryObject queryObject = new TaslyItemLocationQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<ItemLocation> pagedItemLocations = this.inventoryServiceApi.findItemLocationsByQuery(queryObject);
		if (pagedItemLocations.getResults() != null)
		{
			result.initializeItemLocations(pagedItemLocations.getResults());
		}

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(
				pagedItemLocations.getNextPage().intValue(), pagedItemLocations.getPreviousPage().intValue(),
				pagedItemLocations.getTotalPages().intValue(), pagedItemLocations.getTotalRecords().longValue());

		final List<ItemLocation> itemLocations = result.getItemLocations();
		for (final ItemLocation itemLocation : itemLocations)
		{
			if (itemLocation instanceof TaslyItemLocation)
			{
				final TaslyItemLocation taslyItemLocation = (TaslyItemLocation) itemLocation;
				cloneItemLocations.add(taslyItemLocation);
			}
		}

		cloneresult.initializeItemLocations(cloneItemLocations);
		final GenericEntity<TaslyItemLocationList> entity = new ItemLocationListEntity(cloneresult);

		return responseBuilder.entity(entity).build();
	}

	@PUT
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_inventorymanager"})
	public Response updateInventory(final OmsInventory inventory, @QueryParam("incremental") final Boolean isIncremental)
	{
		OmsInventory updatedDto = null;
		if (isIncremental.booleanValue())
		{
			updatedDto = this.inventoryServiceApi.updateIncrementalInventory(inventory);
		}
		else
		{
			updatedDto = this.inventoryServiceApi.updateInventory(inventory);
		}
		return Response.status(ClientResponse.Status.OK).entity(updatedDto).build();
	}

	private static final class ItemLocationListEntity extends GenericEntity<TaslyItemLocationList>
	{
		private ItemLocationListEntity(final TaslyItemLocationList entity)
		{
			super(entity);
		}
	}
}
