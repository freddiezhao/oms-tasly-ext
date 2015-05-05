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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandFactory;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;
import tasly.greathealth.tmall.order.exception.ThirdPartyApiInvokeException;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;
import com.taobao.api.response.TradesSoldGetResponse;

import flexjson.JSONSerializer;


/**
 *
 * @author gaoxin
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/tmallorder/jsc")
public class TaslyTmallOrderMockResource
{
	protected static final Logger LOG = LoggerFactory.getLogger(TaslyTmallOrderMockResource.class);

	protected OmsOrderRetrieveService<Trade> omsOrderRetrieverService;
	protected TaobaoClient taobaoclient;
	protected String defaultTmallStoreSessionkey;

	/*
	 * 1）TRADE_NO_CREATE_PAY(没有创建支付宝交易)
	 * 2）WAIT_BUYER_PAY(等待买家付款)
	 * 3）WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)
	 * 4）SELLER_CONSIGNED_PART（卖家部分发货）
	 * 5）WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)
	 * 6）TRADE_BUYER_SIGNED(买家已签收,货到付款专用)
	 * 7）TRADE_FINISHED(交易成功)
	 * 8）TRADE_CLOSED(交易关闭)
	 * 9）TRADE_CLOSED_BY_TAOBAO(交易被淘宝关闭)
	 */
	protected final String STATUS = "WAIT_SELLER_SEND_GOODS";
	// 查询的页数
	protected long page_no = 1L;
	// 每页的条数（最大page_size 100条）
	protected final long PAGE_SIZE = 100L;
	// Trade中可以指定返回的fields
	protected final String FIELDS = "tid";
	// 天猫交易类型
	protected final String TYPE = "fixed";
	// 是否启用has_next的分页方式，如果指定true,则返回的结果中不包含总记录数，但是会新增一个是否存在下一页的的字段，通过此种方式获取增量交易，接口调用成功率在原有的基础上有所提升
	protected final String USE_HAS_NEXT = "true";

	@PUT
	@Path("/produceOrder/mock/{startDate}/{endDate}")
	public Response mockProcess(@PathParam("startDate") final String startDateTime, @PathParam("endDate") final String endDateTime)
			throws Exception
	{
		if (isValidDate(startDateTime) || isValidDate(endDateTime))
		{
			LOG.info("Mock Event startDate[" + startDateTime + "] endDate[" + endDateTime + "] 格式正确.");
		}
		else
		{
			LOG.error("Mock Event startDate[" + startDateTime + "] endDate[" + endDateTime + "] 格式不正确，此次同步任务失败.");
			return Response.status(400).build();
		}

		final List<Trade> trades = this.getTrades(startDateTime, endDateTime);

		dealTrades(trades);

		return Response.ok("OK").build();
	}

	protected void dealTrades(final List<Trade> trades)
	{
		LOG.info("get trades for Tmall JSC --- Mock Event Topic[taobao_trade_TradeBuyerPay], trades size=" + trades.size());

		for (final Trade t : trades)
		{
			try
			{
				final TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
				req.setFields("payment,tid,status,type,orders,buyer_nick");
				req.setTid(t.getTid());
				TradeFullinfoGetResponse response = null;
				response = taobaoclient.execute(req, defaultTmallStoreSessionkey);

				final Trade trade = response.getTrade();
				final Message message = new Message();
				final Map<String, Object> rawMap = new HashMap<String, Object>();
				final Map<String, Object> contentMap = new HashMap<String, Object>();
				contentMap.put("tid", trade.getTid());
				contentMap.put("oid", String.valueOf(trade.getOrders().get(0).getOid()));
				contentMap.put("type", trade.getType());
				contentMap.put("payment", trade.getPayment());
				contentMap.put("buyer_nick", trade.getBuyerNick());

				final String contentMapJson = new JSONSerializer().deepSerialize(contentMap);
				rawMap.put("content", contentMapJson);
				rawMap.put("time", String.valueOf(new Date()));
				setRawMsg(message, rawMap);

				LOG.info("---Tmall JSC---> " + message.getRaw());
				message.setTopic("taobao_trade_TradeBuyerPay");

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
		}

		LOG.info("get trades for Tmall JSC over--- Mock Event Topic[taobao_trade_TradeBuyerPay], trades size=" + trades.size());
	}

	// 验证日期类型是否正确
	protected boolean isValidDate(final String date)
	{
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(date);
		}
		catch (final ParseException e)
		{
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		catch (final NullPointerException e)
		{
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService<Trade> omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}

	protected List<Trade> getTrades(final String startDateTime, final String endDateTime) throws Exception
	{
		try
		{
			LOG.info("getTrades method: startDateTime=" + startDateTime + " endDateTime=" + endDateTime);

			final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setLenient(false);

			final TradesSoldGetRequest req = new TradesSoldGetRequest();
			req.setFields(FIELDS);
			req.setStartCreated(format.parse(startDateTime));
			req.setEndCreated(format.parse(endDateTime));
			req.setStatus(STATUS);
			req.setPageNo(page_no);
			req.setPageSize(PAGE_SIZE);
			req.setUseHasNext(true);
			TradesSoldGetResponse response = null;
			List<Trade> trades = null;

			LOG.info("get trades from tmall.");

			response = taobaoclient.execute(req, defaultTmallStoreSessionkey);

			trades = response.getTrades();

			while (response.getHasNext())
			{
				++page_no;
				req.setPageNo(page_no);
				response = taobaoclient.execute(req, defaultTmallStoreSessionkey);

				if (trades != null)
				{
					trades.addAll(response.getTrades());
				}

			}

			return trades;
		}
		catch (final ApiException e)
		{
			throw new ThirdPartyApiInvokeException("Invoke taobao API [Find Detail] failed!", e);
		}
		catch (final ParseException e)
		{
			throw new ThirdPartyApiInvokeException("ParseException get trades failed!", e);
		}

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

	public void setTaobaoclient(final TaobaoClient taobaoclient)
	{
		this.taobaoclient = taobaoclient;
	}

	public void setDefaultTmallStoreSessionkey(final String defaultTmallStoreSessionkey)
	{
		this.defaultTmallStoreSessionkey = defaultTmallStoreSessionkey;
	}

}
