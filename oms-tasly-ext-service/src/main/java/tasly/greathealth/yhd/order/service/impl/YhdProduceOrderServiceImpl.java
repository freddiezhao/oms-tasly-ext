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
package tasly.greathealth.yhd.order.service.impl;

import com.hybris.oms.domain.job.JobTimerShaftData;

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

import com.yhd.YhdClient;
import com.yhd.object.trade.Trade;
import com.yhd.object.trade.TradeList;
import com.yhd.request.trade.TradesSoldGetRequest;
import com.yhd.response.trade.TradesSoldGetResponse;


/**
 * YHD 新建订单生产者
 *
 * @author libin
 *
 */
public class YhdProduceOrderServiceImpl extends AbstractProduceOrderService implements OrderConstants
{
	private final Logger LOG = OmsLoggerFactory.getYhdorderlog();
	private OmsOrderRetrieveService<Trade> omsOrderRetrieverService;
	private YhdClient client;
	private String sessionKey;

	private String channel;
	private String innersource;
	private String event;

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService<Trade> omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(final YhdClient client)
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
	 * @param sessionKey the sessionKey to set
	 */
	public void setSessionKey(final String sessionKey)
	{
		this.sessionKey = sessionKey;
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
			// yyyy-MM-dd HH:MM:SS
			final Date Executtime = jobTimerShaftData.getExecuttime();
			// 时间间隔，单位毫秒
			final int intervaltime = jobTimerShaftData.getIntervaltime();
			if (Executtime != null && intervaltime > 0)
			{
				final String startExecuttime = TaslyThirdUtils.formatTime(Executtime);
				final String endExecuttime = TaslyThirdUtils.addTime(Executtime, intervaltime);
				ordersId = new ArrayList<String>();
				// 获取该时间段内记录总条数
				final TradesSoldGetRequest request = new TradesSoldGetRequest();
				request.setStartCreated(startExecuttime);
				request.setEndCreated(endExecuttime);
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
						dealSinglePage(totalPage, startExecuttime, endExecuttime);
						totalPage--;
					}
				}
				else
				{
					LOG.info("无交易订单." + response.getSub_msg());
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

	private void dealSinglePage(final long pageNo, final String startExecuttime, final String endExecuttime) throws Exception
	{
		LOG.info("处理第 " + pageNo + "页");

		final TradesSoldGetRequest request = new TradesSoldGetRequest();
		request.setStartCreated(startExecuttime);
		request.setEndCreated(endExecuttime);
		request.setStatus(YHD_OTC_ORDER_STATE);
		request.setPageNo(YHD_OTC_PAGE);
		request.setPageSize(YHD_OTC_PAGE_SIZE);

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
			OrderCommand command = null;
			if (innersource.equalsIgnoreCase("OTC"))
			{
				command = OrderCommandFactory.createYhdOrderCommand(omsOrderRetrieverService, EventType.ORDERCREATE, trade,
						InnerSource.OTC);
			}
			else if (innersource.equalsIgnoreCase("JSC"))
			{
				command = OrderCommandFactory.createYhdOrderCommand(omsOrderRetrieverService, EventType.ORDERCREATE, trade,
						InnerSource.JSC);
			}

			if (command != null)
			{
				OrderCommandsStorage.getInstance().addOrderCommand(command.getChannelSource(), command.getEventType(), command);
			}
		}
	}
}
