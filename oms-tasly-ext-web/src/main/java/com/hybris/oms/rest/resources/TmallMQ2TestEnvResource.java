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
package com.hybris.oms.rest.resources;


import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.security.access.annotation.Secured;

import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandFactory;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;
import tasly.greathealth.tmall.order.domain.SendTmallMessageRelatedConfig;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.sun.jersey.api.client.ClientResponse;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;

import flexjson.JSONSerializer;


/**
 * REST resource for sending product env's Tmall Message to test env.
 */
@Path("/tmallmq2testenv")
public class TmallMQ2TestEnvResource
{

	protected OmsOrderRetrieveService<Trade> omsOrderRetrieverService;

	protected SendTmallMessageRelatedConfig sendTmallMessageRelatedConfig;

	protected static DateFormat format = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy");

	protected static final Logger logger = OmsLoggerFactory.getTmallorderlog();

	@GET
	@Path("/opentransfer")
	@Secured({"ROLE_admin"})
	@Produces("text/plain")
	public String openTransfer()
	{
		logger.info("Open transfer flag is " + sendTmallMessageRelatedConfig.isSendCommands2testEnv());
		// String url = OrderCommandsStorage.getInstance().getTestEnvUrl();

		// OrderCommandsStorage.getInstance().setSendCommands2testEnv(true);
		sendTmallMessageRelatedConfig.setSendCommands2testEnv(true);

		// Also test server will start receiving
		// Client client = sendTmallMessageRelatedConfig.getClient();
		// WebResource webResource = client.resource(url + "/oms-tasly-ext-web/webresources/tmallmq2testenv/openreceive");
		final ClientResponse response = sendTmallMessageRelatedConfig.getOpenreceiveWR().accept("text/plain")
				.header("X-tenantid", "single").header("X-role", "admin").get(ClientResponse.class);
		final String output = response.getEntity(String.class);

		logger.info("Output from test Server .... \n");
		logger.info(output);

		logger.info("Open transfer flag is " + sendTmallMessageRelatedConfig.isSendCommands2testEnv());
		return "Tmall message is ready to send? " + String.valueOf(sendTmallMessageRelatedConfig.isSendCommands2testEnv());
	}

	@GET
	@Path("/stoptransfer")
	@Secured({"ROLE_admin"})
	@Produces("text/plain")
	public String stopTransfer()
	{
		logger.info("Open transfer flag is " + sendTmallMessageRelatedConfig.isSendCommands2testEnv());

		// OrderCommandsStorage.getInstance().setSendCommands2testEnv(false);
		sendTmallMessageRelatedConfig.setSendCommands2testEnv(false);

		// Also test server will stop receiving
		// Client client = sendTmallMessageRelatedConfig.getClient();
		// WebResource webResource = client.resource(OrderCommandsStorage.getInstance().getTestEnvUrl()
		// + "/oms-tasly-ext-web/webresources/tmallmq2testenv/stopreceive");
		final ClientResponse response = sendTmallMessageRelatedConfig.getStopreceiveWR().accept("text/plain")
				.header("X-tenantid", "single").get(ClientResponse.class);
		final String output = response.getEntity(String.class);

		logger.info("Output from test Server .... \n");
		logger.info(output);
		logger.info("Open transfer flag is " + sendTmallMessageRelatedConfig.isSendCommands2testEnv());

		sendTmallMessageRelatedConfig.getTmallMessageList().clear();

		return "Tmall message stops sending? " + String.valueOf(!sendTmallMessageRelatedConfig.isSendCommands2testEnv());
	}

	@GET
	@Path("/openreceive")
	@Secured({"ROLE_admin"})
	@Produces("text/plain")
	public String openreceive()
	{
		logger.info("Open receive flag is " + sendTmallMessageRelatedConfig.isReceiveCommandsFromProductEnv());

		// OrderCommandsStorage.getInstance().setreceiveCommandsFromProductEnv(true);
		sendTmallMessageRelatedConfig.setReceiveCommandsFromProductEnv(true);

		logger.info("Now Open receive flag is " + sendTmallMessageRelatedConfig.isReceiveCommandsFromProductEnv());

		return "Test server starts receiving? " + String.valueOf(sendTmallMessageRelatedConfig.isReceiveCommandsFromProductEnv());
	}

	@GET
	@Path("/stopreceive")
	@Secured({"ROLE_admin"})
	@Produces("text/plain")
	public String stopreceive()
	{
		logger.info("Open receive flag is " + sendTmallMessageRelatedConfig.isReceiveCommandsFromProductEnv());

		sendTmallMessageRelatedConfig.setReceiveCommandsFromProductEnv(false);

		logger.info("Now Open receive flag is " + sendTmallMessageRelatedConfig.isReceiveCommandsFromProductEnv());

		return "Test server stops receiving? " + String.valueOf(!sendTmallMessageRelatedConfig.isReceiveCommandsFromProductEnv());

	}

	@POST
	@Path("/postmq2testenv")
	@Secured({"ROLE_admin"})
	public String postMQ2TestEnv(final String messageJson)
	{
		final ClientResponse response = sendTmallMessageRelatedConfig.getReceivemqWR().type(MediaType.APPLICATION_JSON)
				.header("X-tenantid", "single").post(ClientResponse.class, messageJson);

		if (response.getStatus() != 200)
		{
			logger.debug("Failed : HTTP error code : " + response.getStatus());
		}

		logger.info("Output from test env .... \n");
		final String output = response.getEntity(String.class);
		logger.info("Receive " + output + " messages.");

		return output + " messages are sent to test env.";

	}


	@SuppressWarnings("unchecked")
	@GET
	@Path("/sendmq2testenv")
	@Secured({"ROLE_admin"})
	public String sendMQ2TestEnv()
	{
		final List<Message> messageList = sendTmallMessageRelatedConfig.getTmallMessageList();

		// Mock Message

		Map<String, Object> rawMap = null;
		Map<String, String> contentMap = null;
		Message message = null;

		Class c;
		try
		{
			c = Class.forName("com.taobao.api.internal.tmc.Message");
			final Method methodSetRaw = c.getDeclaredMethod("setRaw", Map.class);
			methodSetRaw.setAccessible(true);
			final Object tmallMessageObj = c.newInstance();

			rawMap = new HashMap<String, Object>();
			contentMap = new HashMap<String, String>();
			contentMap.put("buyer_nick", "sandbox_cilai_c");
			contentMap.put("payment", "44.00");
			contentMap.put("tid", "192559684481084");
			contentMap.put("oid", "192559684481084");
			contentMap.put("seller_nick", "sandbox_c_1");
			contentMap.put("type", "guarantee_trade");

			rawMap.put("content", contentMap);
			rawMap.put("topic", "taobao_trade_TradeBuyerPay");
			rawMap.put("time", Calendar.getInstance().getTime());
			rawMap.put("id", "2130700002172846269");
			rawMap.put("nick", "sandbox_c_1");
			rawMap.put("userid", "2074082786");
			rawMap.put("dataid", "192559684481084");
			rawMap.put("publisher", "4272");
			rawMap.put("outtime", Calendar.getInstance().getTime());

			methodSetRaw.invoke(tmallMessageObj, rawMap);
			message = (Message) tmallMessageObj;

			message.setId(4160600490938325004L);
			message.setTopic("taobao_trade_TradeBuyerPay");
			message.setPubTime(Calendar.getInstance().getTime());
			message.setOutgoingTime(Calendar.getInstance().getTime());
			message.setUserId(911757567L);
			message.setContentMap(contentMap);

			messageList.add(message);
		}
		catch (final Exception e1)
		{
			logger.error("message reflec error ", e1);
		}

		try
		{
			// messageNum = messageList.size();

			// ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			// String json = "";
			// json = ow.writeValueAsString(messageList.get(0).getRaw());

			final JSONSerializer jsonSerializer = new JSONSerializer();
			final String rawJson = jsonSerializer.deepSerialize(messageList.get(0).getRaw());


			// ClientResponse response = sendTmallMessageRelatedConfig.getPostmq2testenvWR().header("X-tenantid", "single")
			// .post(ClientResponse.class, rawJson);


			final ClientResponse response = sendTmallMessageRelatedConfig.getReceivemqWR().type(MediaType.APPLICATION_JSON)
					.header("X-tenantid", "single").post(ClientResponse.class, rawJson);

			if (response.getStatus() != 200)
			{
				logger.debug("Failed : HTTP error code : " + response.getStatus());
			}
			else
			{
				messageList.clear();
			}

			logger.info("Output from test env .... \n");
			final String output = response.getEntity(String.class);
			logger.info("Receive " + output + " messages.");

		}
		catch (final Exception e)
		{
			logger.error("Fail to send to test env ", e);
		}

		// return messageNum + " messages are sent to test env.";
		return "1 messages are sent to test env.";

	}

	@SuppressWarnings("rawtypes")
	@POST
	@Path("/testenvurl")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	// public Response receiveMQfromProductEnv(@RequestBody Map<SourceType, Map<EventType, Queue<OrderCommand>>>
	// commands){
	public String setTestEnvUrl(final String url)
	{
		sendTmallMessageRelatedConfig.setTestEnvUrl(url);
		logger.info("Now the test env URL is " + sendTmallMessageRelatedConfig.getTestEnvUrl());
		return "Now the test env URL is " + sendTmallMessageRelatedConfig.getTestEnvUrl();
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@POST
	@Path("/receivemq")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	// public Response receiveMQfromProductEnv(@RequestBody Map<SourceType, Map<EventType, Queue<OrderCommand>>>
	// commands){
	public String receiveMQfromProductEnv(final String json)
	{
		try
		{
			logger.debug("Receive json is " + json.toString());

			final ObjectMapper om = new ObjectMapper();
			// List readValue = om.readValue(json, List.class);
			final Map raw = om.readValue(json, new TypeReference<HashMap<String, Object>>()
			{});

			// String topic = "";
			Message message = null;

			// Map messageMap = null;
			// Map<String, Map> rawMap = null;
			// Map contentMap = null;

			final Class c = Class.forName("com.taobao.api.internal.tmc.Message");
			final Method methodSetRaw = c.getDeclaredMethod("setRaw", Map.class);
			methodSetRaw.setAccessible(true);
			final Object tmallMessageObj = c.newInstance();
			// rawMap = new HashMap<String, Map>();
			// contentMap = (Map) messageMap.get("raw");
			// rawMap.put("content", (Map) contentMap.get("content"));
			raw.put("time", new Date(Long.valueOf(nullToZero(String.valueOf(raw.get("time"))))));
			methodSetRaw.invoke(tmallMessageObj, raw);

			message = (Message) tmallMessageObj;

			message.setContent(String.valueOf(raw.get("content")));
			message.setTopic(String.valueOf(raw.get("topic")));
			message.setId(Long.valueOf(String.valueOf(raw.get("id"))));
			message.setPubAppKey(String.valueOf(raw.get("publisher")));
			message.setOutgoingTime(new Date(Long.valueOf(nullToZero(String.valueOf(raw.get("outtime"))))));
			message.setUserId(Long.valueOf(nullToZero(String.valueOf(raw.get("userid")))));
			message.setUserNick(String.valueOf(raw.get("nick")));
			message.setContent(String.valueOf(raw.get("content")));

			// topic = message.getTopic();

			logger.debug("outgoing time is " + message.getOutgoingTime());
			logger.debug("time in raw is " + message.getRaw().get("time"));
			// for test only
			// final OrderCommand createOrderCommand = new CreateTmallOrderCommand(omsOrderRetrieverService, message);
			// OrderCommandsStorage.getInstance().addOrderCommand(SourceType.TMALL, EventType.ORDERCREATE,
			// createOrderCommand);


			// Add to singleton commands
			final OrderCommand command = OrderCommandFactory.createTmallOrderCommand(omsOrderRetrieverService, message,
					InnerSource.OTC);
			OrderCommandsStorage.getInstance().addOrderCommand(command.getChannelSource(), command.getEventType(), command);
			// if ("taobao_trade_TradeBuyerPay".equalsIgnoreCase(topic))
			// {
			// final OrderCommand createOrderCommand = new CreateTmallOrderCommand(omsOrderRetrieverService, message);
			// OrderCommandsStorage.getInstance().addOrderCommand(SourceType.TMALL, EventType.ORDERCREATE,
			// createOrderCommand);
			// }
			// else if ("taobao_refund_RefundCreated".equalsIgnoreCase(topic))
			// {
			// final OrderCommand createRefundcommand = new CreateTmallRefundCommand(omsOrderRetrieverService, message);
			// OrderCommandsStorage.getInstance().addOrderCommand(SourceType.TMALL, EventType.REFUNDCREATE,
			// createRefundcommand);
			// }
			// else if ("taobao_refund_RefundSuccess".equalsIgnoreCase(topic))
			// {
			// final OrderCommand refundSuccessCommand = new TmallRefundSuccessCommand(omsOrderRetrieverService, message);
			// OrderCommandsStorage.getInstance().addOrderCommand(SourceType.TMALL, EventType.REFUNDSUCCESS,
			// refundSuccessCommand);
			// }
			// else if ("taobao_refund_RefundClosed".equalsIgnoreCase(topic))
			// {
			// final OrderCommand refundCloseCommand = new TmallRefundCloseCommand(omsOrderRetrieverService, message);
			// OrderCommandsStorage.getInstance().addOrderCommand(SourceType.TMALL, EventType.REFUNDCLOSE,
			// refundCloseCommand);
			// }
			// else if ("taobao_trade_TradeSellerShip".equalsIgnoreCase(topic))
			// {
			// final OrderCommand createSellerShipcommand = new CreateTmallSellerShippingCommand(omsOrderRetrieverService,
			// message);
			// OrderCommandsStorage.getInstance().addOrderCommand(SourceType.TMALL, EventType.SELLERSHIP,
			// createSellerShipcommand);
			// }

		}
		catch (final Exception e)
		{
			logger.error("Failed to recieve Tmall message from Product Env. ", e);
		}
		return String.valueOf(OrderCommandsStorage.getInstance().getAllCommands().size());
	}

	public String nullToZero(final String input)
	{
		if (input == null || "null".equals(input))
		{
			return "0";
		}
		else
		{
			return input;
		}
	}

	/**
	 * @return the omsOrderRetrieverService
	 */
	public OmsOrderRetrieveService<Trade> getOmsOrderRetrieverService()
	{
		return omsOrderRetrieverService;
	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService<Trade> omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}

	/**
	 * @param sendTmallMessageRelatedConfig the sendTmallMessageRelatedConfig to set
	 */
	public void setSendTmallMessageRelatedConfig(final SendTmallMessageRelatedConfig sendTmallMessageRelatedConfig)
	{
		this.sendTmallMessageRelatedConfig = sendTmallMessageRelatedConfig;
	}

}
