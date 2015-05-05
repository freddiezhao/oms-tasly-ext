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
package tasly.greathealth.oms.web.mock.rest.resource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.thirdparty.order.EventType;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;
import tasly.greathealth.tmall.order.commands.CreateTmallOrderCommand;
import tasly.greathealth.tmall.order.commands.CreateTmallRefundCommand;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.response.TradesSoldIncrementGetResponse;

import flexjson.JSONSerializer;


@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/import")
public class TaslyOrderProducerMockTestResource
{

	protected static final Logger LOG = LoggerFactory.getLogger(TaslyOrderProducerMockTestResource.class);

	protected static String DEFAULT_MOCK_TMALL_FIELDS = "tid,orders,payment,buyer_nick";

	protected static String defaultTmallStoreSessionkey;

	protected TaobaoClient client;

	protected OmsOrderRetrieveService<Trade> omsOrderRetrieveService;

	@GET
	@Path("/writeback2tmall/{flag}")
	public @ResponseBody String updateWriteBackFlag(@PathParam("flag") final Boolean flag)
	{
		omsOrderRetrieveService.setWriteBack2Tamll(flag);
		return String.valueOf(omsOrderRetrieveService.isWriteBack2Tamll());
	}

	@GET
	@Path("/writeback2tmall")
	public @ResponseBody String getWriteBackFlag()
	{
		return String.valueOf(omsOrderRetrieveService.isWriteBack2Tamll());
	}

	@GET
	@Path("/{tid}/{memo}/{replace}")
	public @ResponseBody String updateSellerMemo(@PathParam("tid") final Long tid, @PathParam("memo") final String memo,
			@PathParam("replace") final Boolean replace)
	{
		Trade trade;
		try
		{
			trade = omsOrderRetrieveService.retrieveOrderDetail(String.valueOf(tid));
			omsOrderRetrieveService.updateSellerMemo(tid, trade.getSellerMemo(), memo, !replace);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			return e.getMessage();
		}
		return "OK";
	}


	@GET
	@Path("/{operation}/{status}/{startdate}/{enddate}/{pagesize}/{pageno}")
	public Response getOrderProducerStatus(@PathParam("operation") final String operation,
			@PathParam("status") final String status, @PathParam("startdate") final String startdate,
			@PathParam("enddate") final String enddate, @PathParam("pagesize") final String pagesize,
			@PathParam("pageno") final String pageno)
	{
		LOG.info("Mock produce orders ");
		final List<String> tids = new ArrayList<>();
		{
			final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			// final List<OrderCommand> orderCreateCommands = new ArrayList<>();
			final TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
			TradesSoldIncrementGetResponse response = null;
			try
			{
				req.setStatus(status);
				req.setStartModified(sf.parse(startdate));
				req.setEndModified(sf.parse(enddate));
				req.setFields(DEFAULT_MOCK_TMALL_FIELDS);
				req.setPageNo(Long.valueOf(pageno));
				req.setPageSize(Long.valueOf(pagesize));
				response = client.execute(req, defaultTmallStoreSessionkey);
				if (response.getTrades() != null)
				{
					final List<Trade> trades = response.getTrades();
					for (int i = trades.size() - 1; i >= 0; i--)
					{
						final Trade trade = trades.get(i);
						final String tid = String.valueOf(trade.getTid());
						tids.add(tid);
						final String buyerNick = trade.getBuyerNick();
						final List<Order> orders = trade.getOrders();
						final Order order = orders.get(0);
						final String oid = String.valueOf(order.getOid());
						final String payment = order.getPayment();

						final Message message = new Message();

						final Map<String, Object> rawMap = new HashMap<String, Object>();
						final Map<String, Object> contentMap = new HashMap<String, Object>();
						contentMap.put("tid", tid);
						contentMap.put("oid", oid);
						contentMap.put("payment", payment);
						contentMap.put("buyer_nick", buyerNick);

						rawMap.put("content", contentMap);
						rawMap.put("time", String.valueOf(new Date()));

						setRawMsg(message, rawMap);
						message.setId(123l);

						if ("createOrder".equalsIgnoreCase(operation))
						{
							message.setTopic("taobao_trade_TradeBuyerPay");
							final OrderCommand command = new CreateTmallOrderCommand(this.omsOrderRetrieveService, message,
									InnerSource.OTC);
							OrderCommandsStorage.getInstance().addOrderCommand(ChannelSource.TMALL, EventType.ORDERCREATE, command);
						}
						else if ("createRefund".equalsIgnoreCase(operation))
						{
							message.setTopic("taobao_refund_RefundCreated");
							final OrderCommand command = new CreateTmallRefundCommand(this.omsOrderRetrieveService, message,
									InnerSource.OTC);
							OrderCommandsStorage.getInstance().addOrderCommand(ChannelSource.TMALL, EventType.REFUNDCREATE, command);
						}
					}

					// OrderCommandsStorage.getInstance().addOrderCommands(SourceType.TMALL, EventType.ORDERCREATE,
					// orderCreateCommands);
				}
			}
			catch (final ParseException e)
			{
				LOG.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
			catch (final ApiException e)
			{
				LOG.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
			catch (final IllegalArgumentException e)
			{
				LOG.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
			catch (final IllegalAccessException e)
			{
				// YTODO Auto-generated catch block
				LOG.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
			catch (final InvocationTargetException e)
			{
				// YTODO Auto-generated catch block
				LOG.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
		return Response.ok(new JSONSerializer().serialize(tids)).build();
	}

	private void setRawMsg(final Message msg, final Map<String, Object> para) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		final Method[] method = Message.class.getDeclaredMethods();
		for (int i = method.length - 1; i >= 0; i--)
		{
			if ("setRaw".equalsIgnoreCase(method[i].getName()))
			{
				method[i].setAccessible(true);
				method[i].invoke(msg, para);
			}
		}
	}


	/**
	 * @param client the client to set
	 */
	public void setClient(final TaobaoClient client)
	{
		this.client = client;
	}



	/**
	 * @param defaultMockTmallFields the defaultMockTmallFields to set
	 */
	public static void setDefaultMockTmallFields(final String defaultMockTmallFields)
	{
		DEFAULT_MOCK_TMALL_FIELDS = defaultMockTmallFields;
	}


	public static void setDefaultTmallStoreSessionkey(final String defaultTmallStoreSessionkey)
	{
		TaslyOrderProducerMockTestResource.defaultTmallStoreSessionkey = defaultTmallStoreSessionkey;
	}

	/**
	 * @param omsOrderRetrieveService the omsOrderRetrieveService to set
	 */
	public void setOmsOrderRetrieveService(final OmsOrderRetrieveService<Trade> omsOrderRetrieveService)
	{
		this.omsOrderRetrieveService = omsOrderRetrieveService;
	}



}
