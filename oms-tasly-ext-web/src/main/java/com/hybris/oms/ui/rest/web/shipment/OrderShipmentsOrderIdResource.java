package com.hybris.oms.ui.rest.web.shipment;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.ui.api.shipment.OrderShipmentDetail;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import tasly.greathealth.oms.log.OmsLoggerFactory;


@Component
@Path("/uiordershipments/order/{orderId}")
@Consumes({"application/xml"})
@Produces({"application/xml"})
public class OrderShipmentsOrderIdResource
{
	//private static final Logger LOGGER = LoggerFactory.getLogger(OrderShipmentsOrderIdResource.class);
	private static final Logger LOGGER = OmsLoggerFactory.getOmsorderlog();
	@Autowired
	private UiShipmentFacade uiShipmentFacade;

	@SuppressWarnings("null")
	@GET
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response getOLQListForOrder(@PathParam("orderId") final String orderId,
			@QueryParam("allLocationDisplay") final boolean allLocationDisplay) throws EntityNotFoundException, JAXBException
	{
		LOGGER.debug("getOLQListForOrder");
		final tasly.greathealth.oms.ui.api.shipment.OrderShipmentList resultList = new tasly.greathealth.oms.ui.api.shipment.OrderShipmentList();

		final List<OrderShipmentDetail> ordrShpmntDtails = this.uiShipmentFacade.findOrderShipmentDetailsByOrderId(orderId,
				allLocationDisplay);
		List<tasly.greathealth.oms.ui.api.shipment.OrderShipmentDetail> newOrdrShpmntDtails = new ArrayList<tasly.greathealth.oms.ui.api.shipment.OrderShipmentDetail>();

		for (final OrderShipmentDetail osDetail : ordrShpmntDtails)
		{
			newOrdrShpmntDtails.add(this.createNewOrderShipmentDetail(osDetail));
		}

		if (newOrdrShpmntDtails != null)
		{
			resultList.initializeOrders(newOrdrShpmntDtails);
		}

		final GenericEntity<?> entity = new OrderShipmentEntity(resultList);
		return Response.ok().entity(entity).build();
	}

	private static final class OrderShipmentEntity extends GenericEntity<tasly.greathealth.oms.ui.api.shipment.OrderShipmentList>
	{
		private OrderShipmentEntity(tasly.greathealth.oms.ui.api.shipment.OrderShipmentList newOrdrShpmntDtails)
		{
			super(newOrdrShpmntDtails);
		}
	}
	
	/**
	 * Create new Order Shipment Detail.
	 * @param orderShipmentDetail
	 * @param osDetail
	 */
	private tasly.greathealth.oms.ui.api.shipment.OrderShipmentDetail createNewOrderShipmentDetail(OrderShipmentDetail osDetail){
		tasly.greathealth.oms.ui.api.shipment.OrderShipmentDetail orderShipmentDetail = new tasly.greathealth.oms.ui.api.shipment.OrderShipmentDetail();
		try
		{
			org.apache.commons.beanutils.BeanUtils.copyProperties(orderShipmentDetail, osDetail);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
		return orderShipmentDetail;
	}
}
