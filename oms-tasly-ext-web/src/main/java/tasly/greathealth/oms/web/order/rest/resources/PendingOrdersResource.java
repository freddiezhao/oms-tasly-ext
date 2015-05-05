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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

import tasly.greathealth.oms.api.order.PendingOrderFacade;
import tasly.greathealth.oms.api.order.PendingOrderQueryObject;
import tasly.greathealth.oms.api.order.dto.PendingOrder;


/**
 * @author Henter Liu
 */
@Component
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
@Path("/pendingorders")
public class PendingOrdersResource
{
	protected static final Logger LOGGER = LoggerFactory.getLogger(PendingOrdersResource.class);

	@Autowired
	protected PendingOrderFacade pendingOrderFacade;

	@Required
	public void setPendingOrderFacade(final PendingOrderFacade pendingOrderFacade)
	{
		this.pendingOrderFacade = pendingOrderFacade;
	}

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	@GET
	@TypeHint(PendingOrder.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response findOrdersByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findOrdersByQuery");

		final PendingOrderQueryObject queryObject = new PendingOrderQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<PendingOrder> pagedOrders = this.pendingOrderFacade.findPendingOrdersByQuery(queryObject);
		final GenericEntity<List<PendingOrder>> entity = new GenericEntity<List<PendingOrder>>(pagedOrders.getResults())
				{
			// DONOTHING
				};
				final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedOrders.getNextPage().intValue(),
						pagedOrders.getPreviousPage().intValue(), pagedOrders.getTotalPages().intValue(), pagedOrders.getTotalRecords()
						.longValue());

				return responseBuilder.entity(entity).build();
	}

	/**
	 * restore the failded orders in table pendingOrders
	 *
	 * @author vincent.yin
	 * @param orderid
	 * @return
	 */
	@POST
	@Path("/restore/{orderid}")
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response mockProcess(@PathParam("orderid") final String orderid)
	{
		try
		{
			pendingOrderFacade.restorePendingOrders(orderid);
		}
		catch (final Exception e)
		{
			LOGGER.error("恢复失败，订单编号[" + orderid + "]" + "Error: " + e.getMessage());
			return Response.ok("恢复失败,请查看日志。").build();
		}
		return Response.ok("恢复成功").build();
	}
}
