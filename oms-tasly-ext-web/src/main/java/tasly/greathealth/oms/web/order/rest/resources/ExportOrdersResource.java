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
package tasly.greathealth.oms.web.order.rest.resources;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import tasly.greathealth.oms.api.order.TaslyOrderQueryObject;
import tasly.greathealth.oms.export.api.order.ExportOrderFacade;
import tasly.greathealth.oms.export.api.order.dto.ExportOrder;


/**
 * @author Henter Liu
 */
@Component
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
@Path("/exportorders")
public class ExportOrdersResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportOrdersResource.class);

	@Autowired
	private ExportOrderFacade exportOrderFacade;

	@Required
	public void setExportOrderFacade(final ExportOrderFacade exportOrderFacade)
	{
		this.exportOrderFacade = exportOrderFacade;
	}

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	@GET
	@TypeHint(ExportOrder.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response findOrdersByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findOrdersByQuery");

		final TaslyOrderQueryObject queryObject = new TaslyOrderQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<ExportOrder> pagedOrders = this.exportOrderFacade.findOrdersByQuery(queryObject);
		final GenericEntity<List<ExportOrder>> entity = new GenericEntity<List<ExportOrder>>(pagedOrders.getResults())
		{
			// DONOTHING
		};
		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedOrders.getNextPage().intValue(),
				pagedOrders.getPreviousPage().intValue(), pagedOrders.getTotalPages().intValue(), pagedOrders.getTotalRecords()
						.longValue());

		return responseBuilder.entity(entity).build();
	}
}
