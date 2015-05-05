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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.springframework.security.access.annotation.Secured;

import com.sun.jersey.api.client.ClientResponse;
import com.taobao.api.internal.tmc.Message;

import flexjson.JSONSerializer;


/**
 * REST resource for sending product env's Tmall JSC Message to test env.
 */
@Path("/tmallJSCmq2testenv")
public class TmallJSCMQ2TestEnvResource extends TmallMQ2TestEnvResource
{

	@SuppressWarnings("unchecked")
	@POST
	@Path("/sendmq2testenv")
	@Secured({"ROLE_admin"})
	public String sendMQ2TestEnv(final String oid)
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
			contentMap.put("tid", oid);
			contentMap.put("oid", oid);
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

}
