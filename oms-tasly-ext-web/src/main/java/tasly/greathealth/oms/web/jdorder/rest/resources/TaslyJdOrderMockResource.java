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
package tasly.greathealth.oms.web.jdorder.rest.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
import tasly.greathealth.thirdparty.order.common.ProduceOrderService;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.order.OrderResult;
import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.jd.open.api.sdk.request.order.OrderSearchRequest;
import com.jd.open.api.sdk.response.order.OrderSearchResponse;


/**
 *
 * @author libin
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/jdorder")
public class TaslyJdOrderMockResource
{
	private static final Logger LOG = LoggerFactory.getLogger(TaslyJdOrderMockResource.class);

	private static OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieverService;
	private static DefaultJdClient jdJscClient;
	private ProduceOrderService jdJscProduceOrderService;

	/**
	 * @param jdJscProduceOrderService the jdJscProduceOrderService to set
	 */
	public void setJdJscProduceOrderService(final ProduceOrderService jdJscProduceOrderService)
	{
		this.jdJscProduceOrderService = jdJscProduceOrderService;
	}

	// private static String jd_jsc_url;
	// private static String jd_jsc_access_token;
	// private static String jd_jsc_app_key;
	// private static String jd_jsc_app_secret;
	/*
	 * 多订单状态可以用英文逗号隔开
	 * 1）WAIT_SELLER_STOCK_OUT 等待出库
	 * 2）SEND_TO_DISTRIBUTION_CENER 发往配送中心（只适用于LBP，SOPL商家）
	 * 3）DISTRIBUTION_CENTER_RECEIVED 配送中心已收货（只适用于LBP，SOPL商家）
	 * 4）WAIT_GOODS_RECEIVE_CONFIRM 等待确认收货
	 * 5）RECEIPTS_CONFIRM 收款确认（服务完成）（只适用于LBP，SOPL商家）
	 * 6）WAIT_SELLER_DELIVERY等待发货（只适用于海外购商家，等待境内发货 标签下的订单）
	 * 7）FINISHED_L 完成
	 * 8）TRADE_CANCELED 取消
	 * 9）LOCKED 已锁定
	 */
	private static String JD_JSC_ORDER_STATE = "WAIT_SELLER_STOCK_OUT,WAIT_GOODS_RECEIVE_CONFIRM";
	// 查询的页数
	private static String JD_JSC_PAGE = "1";
	// 每页的条数（最大page_size 100条）
	private static String JD_JSC_PAGE_SIZE = "2";
	// 需返回的字段列表。可选值：orderInfo结构体中的所有字段；字段之间用,分隔
	private static String JD_JSC_OPTIONAL_FIELDS = "order_id,consignee_info,payment_confirm_time,order_start_time,"
			+ "order_total_price,seller_discount,order_remark,coupon_detail_list,vender_remark,item_info_list,"
			+ "freight_price,order_state,delivery_type,pay_type";
	// 排序方式，默认升序,1是降序,其它数字都是升序
	private static String JD_JSC_SORTTYPE = "2";
	// 查询时间类型，默认按修改时间查询。 1为按订单创建时间查询；其它数字为按订单（订单状态、修改运单号）修改时间
	private static String JD_JSC_DATETYPE = "1";

	@PUT
	@Path("/triggerJd/orderCreate")
	public Response triggerJd()
	{
		LOG.info("Rest 开始同步JD订单到OMS: ");
		final long beginTime = System.currentTimeMillis();
		try
		{
			final List<String> jdOrderIds = jdJscProduceOrderService.produceOrders();
			LOG.info("京东JD订单同步工作完成！订单成功放到队列中的数量:" + jdOrderIds.size() + ",耗时:" + (System.currentTimeMillis() - beginTime) / 1000f
					+ " 秒 ");
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return Response.ok("OK").build();
	}

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
			final OrderSearchRequest request = new OrderSearchRequest();
			// request.setStartDate("2015-03-10 12:02:43");
			// request.setEndDate("2015-03-15 12:02:43");
			request.setStartDate(startDate);
			request.setEndDate(endDate);
			request.setOrderState(JD_JSC_ORDER_STATE);
			request.setPage(JD_JSC_PAGE);
			request.setPageSize(JD_JSC_PAGE_SIZE);
			request.setOptionalFields(JD_JSC_OPTIONAL_FIELDS);
			request.setSortType(JD_JSC_SORTTYPE);
			// request.setDateType(JD_JSC_DATETYPE);
			OrderSearchResponse response;
			response = jdJscClient.execute(request);

			final int orderTotal = response.getOrderInfoResult().getOrderTotal();
			LOG.info("总记录条数：" + orderTotal);

			int totalPage = (orderTotal / 2) + 1;
			LOG.info("总页数 : " + totalPage);

			while (totalPage > 0)
			{
				dealSinglePage(totalPage, startDate, endDate);
				totalPage--;
			}
		}
		catch (final JdException e)
		{
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		return Response.ok("OK").build();
	}

	private static void dealSinglePage(final int pageNo, final String startDate, final String endDate)
	{
		LOG.info("处理第 " + pageNo + "页");
		final OrderSearchRequest request = new OrderSearchRequest();
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		request.setOrderState(JD_JSC_ORDER_STATE);
		request.setPage(String.valueOf(pageNo));
		request.setPageSize(JD_JSC_PAGE_SIZE);
		request.setOptionalFields(JD_JSC_OPTIONAL_FIELDS);
		request.setSortType(JD_JSC_SORTTYPE);
		// request.setDateType(JD_JSC_DATETYPE);

		try
		{
			OrderSearchResponse response;
			response = jdJscClient.execute(request);

			final OrderResult orderResult = response.getOrderInfoResult();
			final List<OrderSearchInfo> orderSearchInfoList = orderResult.getOrderInfoList();
			if (orderSearchInfoList == null)
			{
				return;
			}
			for (int i = 0; i < orderSearchInfoList.size(); i++)
			{
				final OrderSearchInfo orderInfo = orderSearchInfoList.get(i);
				final OrderCommand command = OrderCommandFactory.createJdOrderCommand(omsOrderRetrieverService,
						EventType.ORDERCREATE, orderInfo, InnerSource.JSC);
				if (command != null)
				{
					OrderCommandsStorage.getInstance().addOrderCommand(command.getChannelSource(), command.getEventType(), command);
				}
			}
		}
		catch (final JdException e)
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

	public static void main(final String[] args) throws JdException
	{
		// final JdClient client = new DefaultJdClient("http://gw.api.360buy.com/routerjson",
		// "dbd0c4a4-484a-4c1a-b518-ab413b53cee6",
		// "7A8F5D511E42DFF7B56F3985E3AF27CD", "2aa57ec42c5c49ebbb6ab1cdeb46a876");
		// final OrderSearchRequest request = new OrderSearchRequest();
		// request.setStartDate("2015-03-03 12:02:43");
		// request.setEndDate("2015-03-04 12:02:43");
		// request.setOrderState("WAIT_SELLER_STOCK_OUT,WAIT_GOODS_RECEIVE_CONFIRM");
		// request.setPage("1");
		// request.setPageSize("100");
		// // request.setOptionalFields("jingdong");
		// // request.setSortType("jingdong");
		// // request.setDateType("jingdong");
		// final OrderSearchResponse response = client.execute(request);
		//
		// System.out.print(response.getOrderInfoResult().getOrderTotal());

		// final OrderResult orderResult = response.getOrderInfoResult();
		// final List<OrderSearchInfo> orderSearchInfoList = orderResult.getOrderInfoList();


		// 测试分页
		// try
		// {
		// final JdClient jdJscClient = new DefaultJdClient("http://gw.api.360buy.com/routerjson",
		// "dbd0c4a4-484a-4c1a-b518-ab413b53cee6", "7A8F5D511E42DFF7B56F3985E3AF27CD",
		// "2aa57ec42c5c49ebbb6ab1cdeb46a876");
		// final OrderSearchRequest request = new OrderSearchRequest();
		// request.setStartDate("2015-03-07 12:02:43");
		// request.setEndDate("2015-03-08 12:02:43");
		// request.setOrderState(JD_JSC_ORDER_STATE);
		// request.setPage("1");
		// request.setPageSize("2");
		// // request.setOptionalFields(JD_JSC_OPTIONAL_FIELDS);
		// request.setSortType(JD_JSC_SORTTYPE);
		// // request.setDateType(JD_JSC_DATETYPE);
		// OrderSearchResponse response;
		// response = jdJscClient.execute(request);
		//
		// final int orderTotal = response.getOrderInfoResult().getOrderTotal();
		// System.out.println("总记录条数：" + orderTotal);
		//
		// int totalPage = (orderTotal / 2) + 1;
		// final int lastPageSize = orderTotal % 2;
		// System.out.println("总页数 : " + totalPage);
		//
		// while (totalPage > 0)
		// {
		// dealSinglePageMain(totalPage, lastPageSize, jdJscClient);
		// totalPage--;
		// }
		// }
		// catch (final JdException e)
		// {
		// System.out.println(e.getMessage());
		// throw new RuntimeException(e);
		// }

		// 测试日期类型是否正确
		// System.out.print(isValidDate("2015-03-08 12:02:41"));

	}

	private static void dealSinglePageMain(final int pageNo, final int lastPageSize, final JdClient jdJscClient)
	{
		System.out.println("处理第 " + pageNo + "页");
		final OrderSearchRequest request = new OrderSearchRequest();
		request.setStartDate("2015-03-06 12:02:43");
		request.setEndDate("2015-03-07 12:02:43");
		request.setOrderState(JD_JSC_ORDER_STATE);
		request.setPage(String.valueOf(pageNo));
		request.setPageSize("2");
		// request.setOptionalFields(JD_JSC_OPTIONAL_FIELDS);
		request.setSortType(JD_JSC_SORTTYPE);
		// request.setDateType(JD_JSC_DATETYPE);

		try
		{
			OrderSearchResponse response;
			response = jdJscClient.execute(request);

			final OrderResult orderResult = response.getOrderInfoResult();
			final List<OrderSearchInfo> orderSearchInfoList = orderResult.getOrderInfoList();
			if (orderSearchInfoList == null)
			{
				return;
			}
			for (int i = 0; i < orderSearchInfoList.size(); i++)
			{
				final OrderSearchInfo orderInfo = orderSearchInfoList.get(i);
				System.out.println("OrderSearchInfo+" + i + ":" + orderInfo.getOrderId());
			}
		}
		catch (final JdException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}

	/**
	 * @param jdJscClient the jdJscClient to set
	 */
	public void setJdJscClient(final DefaultJdClient jdJscClient)
	{
		this.jdJscClient = jdJscClient;
	}


}
