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
package tasly.greathealth.oms.web.erpcodemapping.rest.resources;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;

import tasly.greathealth.erp.api.codeMapping.dto.ErpCodeMapping;
import tasly.greathealth.erp.api.order.UpdateOrderDeliveryStatusFacade;
import tasly.greathealth.oms.log.OmsLoggerFactory;


@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/erpCodeMapping")
public class TaslyErpCodeMappingResource
{

	private static final Logger LOG = OmsLoggerFactory.getErporderlog();

	private UpdateOrderDeliveryStatusFacade updateOrderDeliveryStatusFacade;

	@POST
	public Response createOrUpdateJobConfig(@RequestBody final ErpCodeMapping erpCodeMapping)
	{
		LOG.info("POST Erp code mapping." + erpCodeMapping);
		final ErpCodeMapping result = updateOrderDeliveryStatusFacade.createOrUpdateErpCodeMapping(erpCodeMapping);
		return Response.ok(result).build();
	}

	@GET
	public Collection<ErpCodeMapping> getAllErpCodeMapping()
	{
		LOG.info("Look up all erp code mapping.");
		return updateOrderDeliveryStatusFacade.getAllErpCodeMapping();
	}

	@GET
	@Path("/createOrder")
	public Response createEccOrder()
	{
		LOG.info("Look up all erp code mapping.");
		final List<String> result = updateOrderDeliveryStatusFacade.createEccOrders();
		if (null != result)
		{
			LOG.info("size is " + result.size());
		}
		return Response.ok("OK").build();
	}


	/**
	 * @param updateOrderDeliveryStatusFacade the updateOrderDeliveryStatusFacade to set
	 */
	public void setUpdateOrderDeliveryStatusFacade(final UpdateOrderDeliveryStatusFacade updateOrderDeliveryStatusFacade)
	{
		this.updateOrderDeliveryStatusFacade = updateOrderDeliveryStatusFacade;
	}

}
