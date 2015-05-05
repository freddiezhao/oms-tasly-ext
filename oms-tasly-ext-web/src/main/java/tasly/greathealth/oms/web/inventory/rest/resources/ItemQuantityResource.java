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

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.ItemQuantityFacade;
import tasly.greathealth.oms.api.inventory.dto.ItemQuantityDto;
import tasly.greathealth.oms.api.inventory.dto.ItemQuantityList;


/**
 * REST resource to handle request relevant to ItemQuantity object.
 *
 * @author Henter Liu
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/itemquantity")
public class ItemQuantityResource
{
	private ItemQuantityFacade itemQuantityFacade;

	@Required
	public void setItemQuantityFacade(final ItemQuantityFacade facade)
	{
		this.itemQuantityFacade = facade;
	}

	@PUT
	public Response batchUpdate(final ItemQuantityDto itemQuantityDto) throws Exception
	{
		final String sku = itemQuantityDto.getSku();
		final ItemQuantityList list = itemQuantityDto.getList();
		itemQuantityFacade.batchUpdate(sku, list.getItemQuantities());
		return Response.status(Response.Status.OK).build();
	}
}
