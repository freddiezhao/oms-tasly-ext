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
package tasly.greathealth.oms.web.orderproducer.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.producer.OrderProducer;
import tasly.greathealth.thirdparty.order.EventType;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;
import flexjson.JSONSerializer;


@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/")
public class TaslyOrderProducerConfigResource
{

	protected static final Logger LOG = LoggerFactory.getLogger(TaslyOrderProducerConfigResource.class);

	protected OrderProducer producer;

	protected InnerSource innerSource;

	@GET
	@Path("orderproducer/status")
	public Response getOrderProducerStatus()
	{
		LOG.info("Look up " + innerSource + "order producer status.");
		return Response.ok(String.valueOf(producer.isOnline())).build();
	}

	@GET
	@Path("orderconsumer/status/{eventType}")
	public @ResponseBody String getOrderConsumerStatus(@PathParam("eventType") final String eventType)
	{
		LOG.info("Look up order consumer status." + eventType);
		if ("all".equalsIgnoreCase(eventType))
		{
			return new JSONSerializer().deepSerialize(OrderCommandsStorage.getInstance().getAllConsumerStatus());
		}
		else if (EnumUtils.isValidEnum(EventType.class, eventType) == false)
		{
			return "Not support event type " + eventType;
		}
		else
		{
			return new JSONSerializer().deepSerialize(OrderCommandsStorage.getInstance().getSingleConsumerStatus(eventType));
		}
	}

	@GET
	@Path("orderproducer/start")
	public Response startOrderProducder()
	{
		LOG.info("Start " + innerSource + "order producer.");
		try
		{
			producer.produceOrder();
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			return Response.ok(innerSource + "start failed. " + e.getMessage()).build();
		}
		return Response.ok("OK").build();
	}

	@GET
	@Path("orderconsumer/start/{sourceType}/{eventType}")
	public @ResponseBody String startOrderConsumer(@PathParam("sourceType") final String st,
			@PathParam("eventType") final String eventType)
	{
		LOG.info("Start order consumer." + eventType);
		if ("all".equalsIgnoreCase(eventType))
		{
			OrderCommandsStorage.getInstance().startAllConsumer(st);
			return new JSONSerializer().deepSerialize(OrderCommandsStorage.getInstance().getAllConsumerStatus());
		}
		else if (EnumUtils.isValidEnum(EventType.class, eventType) == false)
		{
			return "Not support event type " + eventType;
		}
		else
		{
			OrderCommandsStorage.getInstance().startSingleConsumer(st, eventType);
			return new JSONSerializer().deepSerialize(OrderCommandsStorage.getInstance().getAllConsumerStatus());
		}
	}

	@GET
	@Path("orderproducer/stop")
	public Response stopOrderProducder()
	{
		LOG.info("Stop " + innerSource + "order producer.");
		try
		{
			producer.stopProduceOrder();
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			return Response.ok(innerSource + "stop failed. " + e.getMessage()).build();
		}
		return Response.ok("OK").build();
	}

	@GET
	@Path("orderconsumer/stop/{eventType}")
	public @ResponseBody String stopOrderConsumer(@PathParam("eventType") final String eventType)
	{
		LOG.info("Stop order consumer." + eventType);
		if ("all".equalsIgnoreCase(eventType))
		{
			OrderCommandsStorage.getInstance().stopAllConsumer();
			return new JSONSerializer().deepSerialize(OrderCommandsStorage.getInstance().getAllConsumerStatus());
		}
		else if (EnumUtils.isValidEnum(EventType.class, eventType) == false)
		{
			return "Not support event type " + eventType;
		}
		else
		{
			OrderCommandsStorage.getInstance().stopSingleConsumer(eventType);
			return new JSONSerializer().deepSerialize(OrderCommandsStorage.getInstance().getAllConsumerStatus());
		}
	}

	/**
	 * @param producer the producer to set
	 */
	public void setProducer(final OrderProducer producer)
	{
		this.producer = producer;
	}

	public void setInnerSource(final InnerSource innerSource)
	{
		this.innerSource = innerSource;
	}

}
