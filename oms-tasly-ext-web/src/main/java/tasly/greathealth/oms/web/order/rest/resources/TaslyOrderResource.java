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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.annotation.Secured;

import tasly.greathealth.oms.api.order.TaslyOrderFacade;
import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 *
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/taslyorder")
public class TaslyOrderResource
{
	private static final Logger LOGGER = OmsLoggerFactory.getOmsorderlog();
	private static final String LOCK_TYPE = "updateOrderLockStatus";
	private TaslyOrderFacade taslyOrderFacade;

	public TaslyOrderFacade getTaslyOrderFacade()
	{
		return taslyOrderFacade;
	}

	/**
	 * @param taslyOrderFacade the taslyOrderFacade to set
	 */
	@Required
	public void setTaslyOrderFacade(final TaslyOrderFacade taslyOrderFacade)
	{
		this.taslyOrderFacade = taslyOrderFacade;
	}

	@Path("/updateTaslyOrder")
	@POST
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response updateTaslyOrder(final TaslyOrder taslyOrder)
	{
		taslyOrderFacade.updateTaslyOrder(taslyOrder);
		return Response.status(Response.Status.OK).build();
	}

	@Path("/{orderId}/{userId}/LockStatus")
	@PUT
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response updateOrderLockStatus(@PathParam("orderId") final String orderId, @PathParam("userId") final String userId,
			final String lockStatus)
	{
		// @Context final HttpHeaders headers,
		// final String role = headers.getRequestHeader("X-role").get(0);
		final TaslyOrder order = this.taslyOrderFacade.getTaslyOrderByOrderId(orderId);
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put("lockStatus", order.getShippingLockStatus().toString());
		map.put("userId", userId);

		printOrderLOGbyType(LOCK_TYPE, true, map);

		final String afterLockStatus = this.taslyOrderFacade.updateTaslyOrderLockStatus(orderId, lockStatus);
		map.put("lockStatus", afterLockStatus);

		printOrderLOGbyType(LOCK_TYPE, false, map);

		return Response.ok("OK").build();
	}

	@GET
	@Path("/{orderId}")
	@TypeHint(TaslyOrder.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response getOrderByOrderId(@PathParam("orderId") final String orderId)
	{
		final Response.ResponseBuilder response = Response.ok();
		final TaslyOrder order = this.taslyOrderFacade.getTaslyOrderByOrderId(orderId);
		response.entity(order);
		return response.build();
	}

	@POST
	@Path("/{orderId}/approve")
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response approve(@PathParam("orderId") final String orderId)
	{
		// System.out.println("####TaslyOrderResource" + 1);
		this.taslyOrderFacade.approveOrder(orderId);
		// System.out.println("####TaslyOrderResource" + 2);

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/{orderId}/mock")
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response mock(@PathParam("orderId") final String orderId)
	{
		this.taslyOrderFacade.mockOrder(orderId);
		return Response.status(Response.Status.OK).build();
	}

	@Path("/{orderId}/express/update")
	@PUT
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response updateExpressCodeByOrderLine(@PathParam("orderId") final String orderId, final String expressCode)
	{
		LOGGER.trace("updateExpressCodeByOrderLine");
		this.taslyOrderFacade.updateExpressCodeByOrderId(orderId, expressCode);
		return Response.status(Response.Status.OK).build();
	}

	private void printOrderLOGbyType(final String type, final boolean isBefore, final Map<String, Object> map)
	{
		final StringBuffer str = new StringBuffer();
		str.append("Operator time:" + new Date() + ",");
		if (isBefore)
		{
			str.append("Before ");
		}
		else
		{
			str.append("After ");
		}

		str.append(" TaslyOrderResource " + type + ":");

		if (map != null)
		{
			for (final Map.Entry entry : map.entrySet())
			{
				str.append(entry.getKey() + " is " + entry.getValue() + ";");
			}
		}
		LOGGER.info(str.toString());
	}

	@GET
	@Path("/taslyorderline/{orderlineid}")
	@TypeHint(TaslyOrderLine.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response getTaslyOrderLineByOrderLineId(@PathParam("orderlineid") final String orderLineId)
	{
		final Response.ResponseBuilder response = Response.ok();
		final TaslyOrderLine taslyOrderLine = taslyOrderFacade.getTaslyOrderLineByOrderLineId(orderLineId);
		return response.entity(taslyOrderLine).build();
	}

	@POST
	@Path("/taslyorderline/{orderlineid}")
	public String updateOrderLineRefundFlag(@PathParam("orderlineid") final String orderLineId, final String refundFlag)
	{
		final String str = taslyOrderFacade.updateOrderLineRefundFlag(orderLineId, refundFlag);
		return str;
	}

	// @GET
	// @Path("/olq/{olqId}")
	// public Response getTaslyOrderLineQuantityByOlqId(@PathParam("olqId") final Long olqId)
	// {
	//
	// final TaslyOrderLineQuantity taslyOlq = this.taslyOrderFacade.getOrderLineQuantityByOlqId(olqId);
	// return Response.ok().entity(taslyOlq).build();
	// }
}
