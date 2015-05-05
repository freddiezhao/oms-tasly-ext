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
package tasly.greathealth.jd.order.service.impl;

import com.hybris.oms.domain.job.JobTimerShaftData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.EventType;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandFactory;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.thirdparty.order.common.AbstractProduceOrderService;
import tasly.greathealth.thirdparty.order.common.TaslyThirdUtils;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.order.OrderResult;
import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.jd.open.api.sdk.request.order.OrderSearchRequest;
import com.jd.open.api.sdk.response.order.OrderSearchResponse;


/**
 * JD 新建订单生产者
 *
 * @author libin
 *
 */
public class JdCreateOrderServiceImpl extends AbstractProduceOrderService implements OrderConstants
{
	private static final Logger LOG = OmsLoggerFactory.getJdorderlog();
	private OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieverService;
	private DefaultJdClient client;

	private String channel;
	private String innersource;
	private String event;

	/**
	 * @param client the client to set
	 */
	public void setClient(final DefaultJdClient client)
	{
		this.client = client;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(final String channel)
	{
		this.channel = channel;
	}

	/**
	 * @param innersource the innersource to set
	 */
	public void setInnersource(final String innersource)
	{
		this.innersource = innersource;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(final String event)
	{
		this.event = event;
	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService<OrderSearchInfo> omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}

	List<String> ordersId = null;

	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public List<String> produceOrders() throws Exception
	{
		// 1、获取上次成功执行时间
		final JobTimerShaftData jobTimerShaftData = this.getJobTimerShaftConfig(channel, innersource, event);
		// 2、根据获取的时间轴对象，获取订单并保存到OMS
		if (jobTimerShaftData != null)
		{
			// yyyy-MM-dd HH:mm:ss
			final Date Executtime = jobTimerShaftData.getExecuttime();
			// 时间间隔，单位毫秒
			final int intervaltime = jobTimerShaftData.getIntervaltime();
			if (Executtime != null && intervaltime > 0)
			{
				final String startExecuttime = TaslyThirdUtils.formatTime(Executtime);
				final String endExecuttime = TaslyThirdUtils.addTime(Executtime, intervaltime);
				ordersId = new ArrayList<String>();
				// 获取该时间段内记录总条数
				final OrderSearchRequest request = new OrderSearchRequest();
				request.setStartDate(startExecuttime);
				request.setEndDate(endExecuttime);
				request.setOrderState(JD_JSC_ORDER_STATE);
				request.setPage(JD_JSC_PAGE);
				request.setPageSize(JD_JSC_PAGE_SIZE);
				request.setOptionalFields(JD_JSC_OPTIONAL_FIELDS);
				request.setSortType(JD_JSC_SORTTYPE);
				request.setDateType(JD_JSC_DATETYPE);
				OrderSearchResponse response;
				response = client.execute(request);

				final int orderTotal = response.getOrderInfoResult().getOrderTotal();
				LOG.info("总记录条数：" + orderTotal);

				int totalPage = (orderTotal / 2) + 1;
				LOG.info("总页数 : " + totalPage);

				while (totalPage > 0)
				{
					dealSinglePage(totalPage, startExecuttime, endExecuttime);
					totalPage--;
				}

				// 3、更新本次成功执行时间点
				jobTimerShaftData.setExecuttime(TaslyThirdUtils.getDatebyStr(endExecuttime));
			}
			else
			{
				LOG.error("渠道:" + channel + ",业态：" + innersource + ",事件：" + event + ",获取到corjob时间轴对象的执行时间为空或者时间间隔为0,请核查数据!");
			}
		}
		else
		{
			LOG.error("渠道:" + channel + ",业态：" + innersource + ",事件：" + event + ",未能查到corjob时间轴对象,请核查数据!");
		}

		return ordersId;
	}

	private void dealSinglePage(final int pageNo, final String startDate, final String endDate) throws JdException
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
		request.setDateType(JD_JSC_DATETYPE);
		OrderSearchResponse response;
		response = client.execute(request);

		final OrderResult orderResult = response.getOrderInfoResult();
		final List<OrderSearchInfo> orderSearchInfoList = orderResult.getOrderInfoList();
		if (orderSearchInfoList == null)
		{
			return;
		}
		for (int i = 0; i < orderSearchInfoList.size(); i++)
		{
			final OrderSearchInfo orderInfo = orderSearchInfoList.get(i);
			ordersId.add(orderInfo.getOrderId());
			final OrderCommand command = OrderCommandFactory.createJdOrderCommand(omsOrderRetrieverService, EventType.ORDERCREATE,
					orderInfo, InnerSource.JSC);
			if (command != null)
			{
				// OrderCommandsStorage.getInstance().addOrderCommand(command.getSourceType(), command.getEventType(),
				// command);
				// 基于高欣修改做了相应改动
				OrderCommandsStorage.getInstance().addOrderCommand(command.getChannelSource(), command.getEventType(), command);
			}
		}
	}

	public static void main(final String[] args) throws JdException
	{
		final Date d = new Date();
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println("今天的日期：" + df.format(d));
		System.out.println("两天前的日期：" + df.format(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000)));
		System.out.println("三天后的日期：" + df.format(new Date(d.getTime() + 3 * 24 * 60 * 60 * 1000)));

		final String startExecuttime = TaslyThirdUtils.formatTime(d);
		final String endExecuttime = TaslyThirdUtils.addTime(d, 5);
		System.out.println("startExecuttime:" + startExecuttime + ",endExecuttime:" + endExecuttime);

	}
}
