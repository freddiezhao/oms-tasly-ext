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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import tasly.greathealth.oms.api.cronjob.TaslyCronjobFacade;
import tasly.greathealth.oms.api.job.dto.TaslyCronJobConfig;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandFactory;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;
import tasly.greathealth.tmall.order.services.MockOrderTestService;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;

import flexjson.JSONSerializer;


@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/jobconfig")
public class TaslyCronJobConfigResource
{
	protected static final Logger LOG = LoggerFactory.getLogger(TaslyCronJobConfigResource.class);

	protected TaslyCronjobFacade taslyCronjobFacade;
	protected TaobaoClient taobaoclient;
	protected OmsOrderRetrieveService<Trade> omsOrderRetrieverService;
	protected MockOrderTestService testService;
	protected String defaultTmallStoreSessionkey;

	public void setDefaultTmallStoreSessionkey(final String defaultTmallStoreSessionkey)
	{
		this.defaultTmallStoreSessionkey = defaultTmallStoreSessionkey;
	}

	@POST
	public Response createOrUpdateJobConfig(@RequestBody final TaslyCronJobConfig config)
	{
		LOG.info("POST Cron job config." + config);
		TaslyCronJobConfig result = null;
		try
		{
			result = taslyCronjobFacade.createOrUpdateJobConfig(config);
		}
		catch (final RuntimeException r)
		{
			Response.ok(r.getMessage()).build();
		}
		return Response.ok(result).build();
	}

	@GET
	@Path("/loadCache")
	public Response loadCache()
	{
		LOG.info("Load cache...");
		try
		{
			omsOrderRetrieverService.loadPendingOrders();
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			Response.ok(e.getMessage()).build();
		}
		return Response.ok("OK").build();
	}

	@GET
	@Path("/{jobName}")
	public Response getJobConfig(@PathParam("jobName") final String jobName)
	{
		LOG.info("Look up job config with name : " + jobName);
		final TaslyCronJobConfig job = taslyCronjobFacade.getCronjobConfig(jobName);
		return Response.ok(job).build();
	}

	@DELETE
	@Path("/deleteOrder/{orderid}")
	public Response deleteOrder(@PathParam("orderid") final String orderid)
	{
		LOG.info("Delete order! order id[" + orderid + "]");
		omsOrderRetrieverService.deleteTaslyOrder(orderid);
		return Response.ok().build();
	}

	@GET
	@Path("/updateOrderMemo/{tid}/{memo}")
	public Response updateOrderMemo(@PathParam("tid") final String tid, @PathParam("memo") final String memo)
	{
		LOG.info("Update order memo. TID[" + tid + "] MEMO [" + memo + "]");
		omsOrderRetrieverService.updateSellerMemo(tid, memo);
		return Response.ok().build();
	}

	@GET
	@Path("/updateOrderPack/{tid}/{pack}")
	public Response updateOrderPackstatus(@PathParam("tid") final String tid, @PathParam("pack") final String pack)
	{
		LOG.info("Update order memo. TID[" + tid + "] PACK [" + pack + "]");
		testService.updateOrderPackStatus(tid, pack);
		return Response.ok().build();
	}

	protected void setRawMsg(final Message msg, final Map<String, Object> para)
	{
		final Method[] method = Message.class.getDeclaredMethods();
		for (int i = method.length - 1; i >= 0; i--)
		{
			if ("setRaw".equalsIgnoreCase(method[i].getName()))
			{
				method[i].setAccessible(true);
				try
				{
					method[i].invoke(msg, para);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					LOG.error(e.getMessage(), e);
					throw new RuntimeException(e);
				}
			}
		}
	}

	@GET
	@Path("/produceOrder/mock/{topic}/{orderid}/{lineid}/{refundFee}")
	public Response mockProcess(@PathParam("orderid") final String orderid, @PathParam("topic") final String topic,
			@PathParam("lineid") final String lineid, @PathParam("refundFee") final String refundFee)
	{
		LOG.info("Mock Event Topic[" + topic + "] OrderId[" + orderid + "] LineId[" + lineid + "] RefundFee[" + refundFee + "]");
		final TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
		req.setFields("created,tid,status,buyer_nick,receiver_name,receiver_address,receiver_mobile,receiver_phone,discount_fee,post_fee,has_yfx,yfx_fee,has_post_fee,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,real_point_fee,received_payment,pay_time,orders");
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

			LOG.info("------> " + message.getRaw());
			message.setUserId(911757567L);
			message.setTopic(topic);

			final OrderCommand command = OrderCommandFactory.createTmallOrderCommand(omsOrderRetrieverService, message,
					InnerSource.OTC);
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

	/**
	 * @param taobaoclient the taobaoclient to set
	 */
	public void setTaobaoclient(final TaobaoClient taobaoclient)
	{
		this.taobaoclient = taobaoclient;
	}

	@GET
	public Collection<TaslyCronJobConfig> getAllJobConfig()
	{
		LOG.info("Look up all job config.");
		return taslyCronjobFacade.getAllJobConfig();
	}

	/**
	 * @param taslyCronjobFacade the taslyCronjobFacade to set
	 */
	public void setTaslyCronjobFacade(final TaslyCronjobFacade taslyCronjobFacade)
	{
		this.taslyCronjobFacade = taslyCronjobFacade;
	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService<Trade> omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}

	/**
	 * @return the testService
	 */
	public MockOrderTestService getTestService()
	{
		return testService;
	}

	/**
	 * @param testService the testService to set
	 */
	public void setTestService(final MockOrderTestService testService)
	{
		this.testService = testService;
	}


}
