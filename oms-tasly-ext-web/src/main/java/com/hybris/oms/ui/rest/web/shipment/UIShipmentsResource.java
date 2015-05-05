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
package com.hybris.oms.ui.rest.web.shipment;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;
import com.hybris.oms.ui.api.shipment.UIShipment;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import tasly.greathealth.oms.ui.api.shipment.TaslyShipmentQueryObject;


@Component
@Path("/uishipments")
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
public class UIShipmentsResource
{
	@Autowired
	private UiShipmentFacade uiShipmentFacade;

	@Autowired
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	@GET
	@TypeHint(UIShipment.class)
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response findUIShipmentsByQuery(@Context final UriInfo uriInfo)
	{
		final TaslyShipmentQueryObject queryObject = new TaslyShipmentQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);
		final Pageable<UIShipment> pagedOrderShipments = this.uiShipmentFacade.findUIShipmentsByQuery(queryObject);
		final GenericEntity<List<UIShipment>> entity = new GenericEntity<List<UIShipment>>(pagedOrderShipments.getResults())
		{
			// NOTHING
		};
		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedOrderShipments.getNextPage()
				.intValue(), pagedOrderShipments.getPreviousPage().intValue(), pagedOrderShipments.getTotalPages().intValue(),
				pagedOrderShipments.getTotalRecords().longValue());

		return responseBuilder.entity(entity).build();
	}
}
