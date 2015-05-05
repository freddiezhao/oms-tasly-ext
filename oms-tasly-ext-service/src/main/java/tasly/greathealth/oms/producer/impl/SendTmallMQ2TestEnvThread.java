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
package tasly.greathealth.oms.producer.impl;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import tasly.greathealth.tmall.order.domain.SendTmallMessageRelatedConfig;

import com.sun.jersey.api.client.ClientResponse;
import com.taobao.api.internal.tmc.Message;

import flexjson.JSONSerializer;


/**
 *
 */
public class SendTmallMQ2TestEnvThread implements Runnable
{
	private SendTmallMessageRelatedConfig sendTmallMessageRelatedConfig = null;
	private Message message = null;
	private Logger LOG = null;

	/**
	 *
	 */
	public SendTmallMQ2TestEnvThread(final SendTmallMessageRelatedConfig sendTmallMessageRelatedConfig, final Message message,
			final Logger LOG)
	{
		this.sendTmallMessageRelatedConfig = sendTmallMessageRelatedConfig;
		this.message = message;
		this.LOG = LOG;
	}

	@Override
	public void run()
	{
		try
		{
			final JSONSerializer jsonSerializer = new JSONSerializer();
			final String rawJson = jsonSerializer.deepSerialize(message.getRaw());
			LOG.debug("raw json is " + rawJson);
			LOG.info(Thread.currentThread().getName() + " Send message to test env start \n");
			final ClientResponse response = sendTmallMessageRelatedConfig.getReceivemqWR().type(MediaType.APPLICATION_JSON)
					.header("X-tenantid", "single").post(ClientResponse.class, rawJson);
			final String output = response.getEntity(String.class);
			LOG.info(output + " messages have been received by test env.");
			LOG.info(Thread.currentThread().getName() + "Send message to test env end \n");
		}
		catch (final Exception e)
		{
			LOG.error(Thread.currentThread().getName() + " SendTmallMQ2TestEnvThread failed! ", e);
		}
	}
}
