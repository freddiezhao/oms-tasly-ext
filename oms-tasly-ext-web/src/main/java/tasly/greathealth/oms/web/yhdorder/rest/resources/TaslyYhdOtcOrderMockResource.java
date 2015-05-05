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
package tasly.greathealth.oms.web.yhdorder.rest.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.thirdparty.order.EventType;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandFactory;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.yhd.YhdClient;
import com.yhd.object.trade.Trade;
import com.yhd.object.trade.TradeList;
import com.yhd.request.trade.TradesSoldGetRequest;
import com.yhd.response.trade.TradesSoldGetResponse;


/**
 *
 * @author libin
 */

@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/yhdOtcOrder")
public class TaslyYhdOtcOrderMockResource
{
	private static final Logger LOG = LoggerFactory.getLogger(TaslyYhdOtcOrderMockResource.class);

	private static OmsOrderRetrieveService<Trade> omsOrderRetrieverService;
	private static YhdClient client;
	private String sessionKey;

	private static String YHD_OTC_ORDER_STATE = "ORDER_PAYED,ORDER_WAIT_SEND";
	private static Long YHD_OTC_PAGE = 1L;
	private static Long YHD_OTC_PAGE_SIZE = 30L;
	private static String YHD_OTC_START_TIME = "2015-04-16 00:00:00";
	private static String YHD_OTC_END_TIME = "2015-04-20 00:00:00";

	private static String url = "http://openapi.yhd.com/app/api/rest/router";
	private static String appKey = "10220015022700003013";
	private static String secretKey = "d56f41ba03025dda080b80be44fb0d3c";
	private static String sessionKeyM = "b80ded0e4941732df401d686c3012817";

	@GET
	@Path("/produceOrder/mock/{startDate}/{endDate}")
	public Response mockProcess(@PathParam("startDate") final String startDate, @PathParam("endDate") final String endDate)
	{
		if (isValidDate(startDate) || isValidDate(endDate))
		{
			LOG.info("Mock Event startDate[" + startDate + "] endDate[" + endDate + "] 格式正确.");
		}
		else
		{
			LOG.error("Mock Event startDate[" + startDate + "] endDate[" + endDate + "] 格式不正确，此次同步任务失败.");
			return Response.status(400).build();
		}
		try
		{
			final TradesSoldGetRequest request = new TradesSoldGetRequest();
			request.setStartCreated(startDate);
			request.setEndCreated(endDate);
			request.setStatus(YHD_OTC_ORDER_STATE);
			request.setPageNo(YHD_OTC_PAGE);
			request.setPageSize(YHD_OTC_PAGE_SIZE);
			final TradesSoldGetResponse response = client.excute(request, sessionKey);

			final Long orderTotal = response.getTotal_results();
			if (orderTotal != null)
			{
				LOG.info("总记录条数：" + orderTotal);

				Long totalPage = (orderTotal / YHD_OTC_PAGE_SIZE) + 1;
				LOG.info("总页数 : " + totalPage);

				while (totalPage > 0)
				{
					dealSinglePage(totalPage, startDate, endDate);
					totalPage--;
				}
			}
			else
			{
				LOG.info("无交易订单." + response.getSub_msg());
			}

		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return Response.ok("OK").build();
	}

	private void dealSinglePage(final Long pageNo, final String startDate, final String endDate)
	{
		LOG.info("处理第 " + pageNo + "页");

		final TradesSoldGetRequest request = new TradesSoldGetRequest();
		request.setStartCreated(startDate);
		request.setEndCreated(endDate);
		request.setStatus(YHD_OTC_ORDER_STATE);
		request.setPageNo(YHD_OTC_PAGE);
		request.setPageSize(YHD_OTC_PAGE_SIZE);

		try
		{
			final TradesSoldGetResponse response = client.excute(request, sessionKey);

			final TradeList tradeList = response.getTrades();
			final List<Trade> trades = tradeList.getTrade();
			if (trades == null)
			{
				return;
			}
			for (int i = 0; i < trades.size(); i++)
			{
				final Trade trade = trades.get(i);
				final OrderCommand command = OrderCommandFactory.createYhdOrderCommand(omsOrderRetrieverService,
						EventType.ORDERCREATE, trade, InnerSource.OTC);
				if (command != null)
				{
					OrderCommandsStorage.getInstance().addOrderCommand(command.getChannelSource(), command.getEventType(), command);
				}
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	// 验证日期类型是否正确
	public boolean isValidDate(final String date)
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

	public static void main(final String[] args)
	{
		// 订单API
		// final YhdClient yhd = new YhdClient(url, appKey, secretKey);
		// final OrdersGetRequest request = new OrdersGetRequest();
		// request.setStartTime(YHD_OTC_START_TIME);
		// request.setEndTime(YHD_OTC_END_TIME);
		// request.setOrderStatusList(YHD_OTC_ORDER_STATE);
		// request.setCurPage(YHD_OTC_PAGE);
		// request.setPageRows(YHD_OTC_PAGE_SIZE);
		// request.setDateType(YHD_OTC_DATETYPE);
		// final OrdersGetResponse response = yhd.excute(request, sessionKey);

		// 交易API,订单下单时间
		// final YhdClient yhd = new YhdClient(url, appKey, secretKey);
		// final TradesSoldGetRequest request = new TradesSoldGetRequest();
		// request.setEndCreated("2015-04-20 23:00:00");
		// request.setStartCreated("2015-04-20 00:00:00");
		// request.setStatus(YHD_OTC_ORDER_STATE);
		// request.setPageNo(1L);
		// request.setPageSize(40L);
		// final TradesSoldGetResponse response = yhd.excute(request, sessionKeyM);
		//
		// System.out.println(response.getTotal_results());
		// System.out.println(response.getSub_msg());

	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public static void setOmsOrderRetrieverService(final OmsOrderRetrieveService<Trade> omsOrderRetrieverService)
	{
		TaslyYhdOtcOrderMockResource.omsOrderRetrieverService = omsOrderRetrieverService;
	}

	/**
	 * @param client the client to set
	 */
	public static void setClient(final YhdClient client)
	{
		TaslyYhdOtcOrderMockResource.client = client;
	}

	/**
	 * @param sessionKey the sessionKey to set
	 */
	public void setSessionKey(final String sessionKey)
	{
		this.sessionKey = sessionKey;
	}

}
