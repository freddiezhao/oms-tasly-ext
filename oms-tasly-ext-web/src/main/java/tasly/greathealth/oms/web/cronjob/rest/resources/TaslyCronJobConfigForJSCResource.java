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
package tasly.greathealth.oms.web.cronjob.rest.resources;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandFactory;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;

import flexjson.JSONSerializer;


@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/jobconfig/jsc")
public class TaslyCronJobConfigForJSCResource extends TaslyCronJobConfigResource
{

	@Override
	@GET
	@Path("/produceOrder/mock/{topic}/{orderid}/{lineid}/{refundFee}")
	public Response mockProcess(@PathParam("orderid") final String orderid, @PathParam("topic") final String topic,
			@PathParam("lineid") final String lineid, @PathParam("refundFee") final String refundFee)
	{
		LOG.info("Tmall JSC --- Mock Event Topic[" + topic + "] OrderId[" + orderid + "] LineId[" + lineid + "] RefundFee["
				+ refundFee + "]");
		final TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
		req.setFields("payment,created,tid,status,buyer_nick,receiver_name,receiver_address,receiver_mobile,receiver_phone,discount_fee,post_fee,has_yfx,yfx_fee,has_post_fee,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,real_point_fee,received_payment,pay_time,orders");
		req.setTid(Long.parseLong(orderid));
		TradeFullinfoGetResponse response = null;
		try
		{
			response = taobaoclient.execute(req, defaultTmallStoreSessionkey);
			final Trade trade = response.getTrade();
			final Message message = new Message();
			final Map<String, Object> rawMap = new HashMap<String, Object>();
			final Map<String, Object> contentMap = new HashMap<String, Object>();
			contentMap.put("tid", trade.getTid());
			contentMap.put("oid", "*".equals(lineid) ? String.valueOf(trade.getOrders().get(0).getOid()) : lineid);
			contentMap.put("type", trade.getType());
			contentMap.put("payment", trade.getPayment());
			contentMap.put("buyer_nick", trade.getBuyerNick());
			if ("*".equals(refundFee) == false)
			{
				contentMap.put("refund_fee", refundFee);
			}

			final String contentMapJson = new JSONSerializer().deepSerialize(contentMap);
			rawMap.put("content", contentMapJson);
			rawMap.put("time", String.valueOf(new Date()));
			setRawMsg(message, rawMap);

			LOG.info("---Tmall JSC---> " + message.getRaw());
			message.setUserId(911757567L);
			message.setTopic(topic);

			final OrderCommand command = OrderCommandFactory.createTmallOrderCommand(omsOrderRetrieverService, message,
					InnerSource.JSC);
			if (command != null)
			{
				OrderCommandsStorage.getInstance().addOrderCommand(command.getChannelSource(), command.getEventType(), command);
			}

		}
		catch (final ApiException e)
		{
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		return Response.ok("OK").build();
	}

}
