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
package tasly.greathealth.tmall.order.services.impl;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.taobao.api.domain.Trade;

import flexjson.JSONDeserializer;



/**
 * @author Gaoxin
 */
public class TmallRestClient
{

	private String tmallRESTUrl;

	public void setTmallRESTUrl(final String tmallRESTUrl)
	{
		this.tmallRESTUrl = tmallRESTUrl;
	}

	public String getTmallRESTUrl()
	{
		return tmallRESTUrl;
	}

	public Trade getTradeFromTmallJSC(final String orderId) throws Exception
	{
		final Client client = Client.create();
		final WebResource webResource = client.resource(tmallRESTUrl + "/tmall/rest/api/getOrderDetail/" + orderId);
		final ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		if (response.getStatus() != 200)
		{
			throw new Exception("Tmall REST server has exception when get order, order id=" + orderId);
		}
		final String textEntity = response.getEntity(String.class);
		final Trade trade = new JSONDeserializer<Trade>().deserialize(textEntity, Trade.class);

		return trade;
	}

}
