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
package tasly.greathealth.tmall.logistic.service.impl;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.erp.dnlog.service.impl.DefaultDNLogService;
import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.oms.api.order.dto.ChannelSource;
import tasly.greathealth.oms.domain.erp.DNLogData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.tmall.logistic.dao.TmallLogisticOfflineDao;
import tasly.greathealth.tmall.logistic.domain.LogisticTmall;
import tasly.greathealth.tmall.logistic.service.TmallLogisticService;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;


/**
 *
 */


public class TmallLogisticServiceImpl implements TmallLogisticService
{
	private static final Logger Log = OmsLoggerFactory.getTmalllogisticlog();

	private TmallLogisticOfflineDao tmallLogisticDao;
	private OmsOrderRetrieveService omsOrderRetrieverService;
	private DefaultDNLogService defaultDnLogService;
	// private DefaultOrderService defaultOrderService;

	private final String dnLog_create = TaslyERPConstants.CREATE;
	private final String dnLog_uncreate = TaslyERPConstants.UNCREATE;
	private final String dnLog_error = TaslyERPConstants.ERRORDNLOG;
	private final String dnLog_noExpress = TaslyERPConstants.NOEXPRESSCODE;

	@Transactional
	@Override
	public void createLogistic()
	{
		Log.info("获取订单和物流信息 ...");
		final List<LogisticTmall> logisticTmallList = this.getOrderAndLogisticInfo();

		Log.info("合并相同的数据信息 ...");
		// key 天猫Id(先按照天猫tid为主键组装map)
		final Map<String, List<LogisticTmall>> logisticMap = this.mergeSameLogisticCode(logisticTmallList);
		Log.info("订单数：" + logisticMap.size());
		// 循环map的key，为每一各不同天猫Tid的订单重组子订单
		final Iterator<String> it = logisticMap.keySet().iterator();
		while (it.hasNext())
		{
			// key运单号 map
			Map<String, List<LogisticTmall>> lMap = new HashMap<String, List<LogisticTmall>>();
			final String key = it.next().toString();
			final List<LogisticTmall> logisticTmalls = logisticMap.get(key);
			Log.info("订单号：" + key + "|子订单数:" + logisticTmalls.size());
			lMap = this.mergeSameLogisticCode_outSid(logisticTmalls);
			Log.info("子订单发货批次数：" + lMap.size());
			this.handleProcess(lMap);
			Log.info("－－－－－运单上传流程结束－－－－－");
		}
		Log.info("所有运单上传流程结束");
	}

	/**
	 * key 运单号
	 *
	 * @param logisticMap
	 */
	public void handleProcess(final Map<String, List<LogisticTmall>> logisticMap)
	{

		final Iterator<String> i = logisticMap.keySet().iterator();
		while (i.hasNext())
		{
			Log.info("－－－－－同步运单流程开始－－－－－");
			// 天猫ID
			final String key = i.next().toString();
			Log.info("运单号：" + key);
			final List<LogisticTmall> logisticTmalls = logisticMap.get(key);

			final Set<String> sub_tids = new HashSet<String>();
			final List<String> currentHandleOrderLineIds = new ArrayList<String>();
			final List<DNLogData> dnlogs = new ArrayList<DNLogData>();
			for (final LogisticTmall logisticTmall : logisticTmalls)
			{
				sub_tids.add(logisticTmall.getSub_tids());
				currentHandleOrderLineIds.add(logisticTmall.getOrderLineId());
				dnlogs.add(logisticTmall.getDnlog());
			}

			final LogisticTmall lt = logisticTmalls.get(0);
			lt.setDnlogs(dnlogs);
			final List<String> sub_tids_List = new ArrayList<String>();
			for (final String s : sub_tids)
			{
				sub_tids_List.add(s);
			}
			lt.setSubTidList(sub_tids_List);
			lt.setSub_tids(StringUtils.join(sub_tids, ","));
			Log.info("子订单号:" + StringUtils.join(sub_tids, ","));
			// create logistic to tmall
			final boolean isSuccess = this.createLogisticInTmall(lt);

			final TaslyOrderData tOrder = lt.getTaslyOrderData();
			if (isSuccess)
			{
				this.updateDnLogFlag(currentHandleOrderLineIds, dnLog_create);// update the dnlog flag
				if (this.filterUnCreateLogistic(lt.getTaslyOrderData()))
				{
					this.delDNLogs(tOrder);
				}
			}
			else
			{
				Log.info("订单" + lt.getTradeId() + "更新到天猫失败，请查看上面提示的错误信息");
			}
		}
	}

	/**
	 * remove create success dnlog data
	 *
	 * @param taslyOrderData
	 */
	public void delDNLogs(final TaslyOrderData taslyOrderData)
	{
		try
		{
			final String omsOrderId = taslyOrderData.getOrderId();
			this.delOneOrderAndLogisticInfo(defaultDnLogService.getDNLogDataList(omsOrderId));
			Log.info("删除dnlog数据成功");
		}
		catch (final Exception e)
		{
			Log.info("删除dnlog数据失败");
		}
	}


	// update dnlog item's replication_flag
	public void updateDnLogFlag(final List<String> orderLineIds, final String replication_flag)
	{
		for (final String orderLineId : orderLineIds)
		{
			DNLogData dnlog = null;

			try
			{
				dnlog = defaultDnLogService.getSingleDNLogByOrderLineID(orderLineId);
			}
			catch (final Exception e)
			{
				Log.error(e.getMessage(), e);
			}
			try
			{
				if (null != dnlog)
				{
					defaultDnLogService.updateSingleDNLog(dnlog, replication_flag);
					Log.info("Logistic create :oms_orderLine_id|" + dnlog.getOms_order_line_id()
							+ " update dnlog item status create success");
				}
				else
				{
					Log.error("dnlog is not exist...");
				}
			}
			catch (final EntityNotFoundException e)
			{
				Log.info("Logistic create : oms_orderLine_id|" + dnlog.getOms_order_line_id()
						+ "update dnlog item status create failed");
			}
		}
	}

	public boolean createLogisticInTmall(final LogisticTmall logisticTmall)
	{
		boolean isSuccess = false;
		// split
		// 判断赠品，订单只有一种产品，但是有赠品，系统存在两行的orderline，但是赠品没有orderlinequantity。
		int orderLineSize = logisticTmall.getTaslyOrderData().getOrderLines().size();
		Log.info("本单上传初始订单行项目数量:" + orderLineSize);
		final Map<String, String> map = new HashMap();
		for (final OrderLineData orderLine : logisticTmall.getTaslyOrderData().getOrderLines())
		{
			if (orderLine instanceof TaslyOrderLineData)
			{
				final TaslyOrderLineData taslyOrderLineData = (TaslyOrderLineData) orderLine;
				if (null != taslyOrderLineData.getGift_item_flag()
						&& taslyOrderLineData.getGift_item_flag().equalsIgnoreCase(OrderConstants.ORDER_LINE_GIFT_FLAG))
				{
					Log.info("订单有赠品行项目");
					orderLineSize--;
				}
				else
				{
					if (null == map.get(taslyOrderLineData.getThird_party_orderline_id()))
					{
						map.put(taslyOrderLineData.getThird_party_orderline_id(), "");
					}
					else
					{
						Log.info("订单有套餐(A+B)行项目");
						orderLineSize--;
					}
				}
			}
		}
		Log.info("本单上传初始最终行项目数量:" + orderLineSize);
		if (orderLineSize == logisticTmall.getSubTidList().size())
		{
			logisticTmall.setIsSplit(0L);
			Log.info("此订单不拆单发货");
		}
		else
		{
			logisticTmall.setIsSplit(1L);
			Log.info("此订单拆单发货");
		}
		isSuccess = tmallLogisticDao.createOfflineLogistic(logisticTmall);
		return isSuccess;
	}

	@Override
	@Transactional
	public List<LogisticTmall> getOrderAndLogisticInfo()
	{
		TaslyOrderData taslyOrderData = null;
		final List<LogisticTmall> logisticTmalls = new ArrayList<LogisticTmall>();
		// get the unCreate logistic info items
		final List<DNLogData> dnLogs = defaultDnLogService.getDNLogByFlag(dnLog_uncreate, ChannelSource.TMALL.name());

		Log.info("状态为unCreate的dnlog数量:" + dnLogs.size());

		final List<DNLogData> removeRepeatList = new ArrayList<DNLogData>();
		final List<DNLogData> noRepeatList = new ArrayList<DNLogData>();
		final Map<String, DNLogData> map = new HashMap<String, DNLogData>();
		// 除去重复的数据
		for (final DNLogData d : dnLogs)
		{
			if (null == map.get(d.getOms_order_line_id()))
			{
				map.put(d.getOms_order_line_id(), d);
				noRepeatList.add(d);
			}
			else
			{
				removeRepeatList.add(d);
			}
		}
		for (final DNLogData d : removeRepeatList)
		{
			Log.info("删除dnLog重复数据");
			Log.info("omsOrderlIneId:" + d.getOms_order_line_id());
		}
		this.delOneOrderAndLogisticInfo(removeRepeatList);

		for (final DNLogData dnlog : noRepeatList)
		{
			// get tasly order
			taslyOrderData = omsOrderRetrieverService.getTaslyOrderDataDetail(dnlog.getOms_order_id());
			if (null != taslyOrderData)
			{
				// judge the two oms_line_id ->one tmall orderLineId situation
				// if (this.filterOneToManyOrderLineIds(taslyOrderData, dnlog))
				// {
				// get orderLines
				final List<OrderLineData> taslyOrderLines = taslyOrderData.getOrderLines();

				for (final OrderLineData orderLineData : taslyOrderLines)
				{
					if (orderLineData.getOrderLineId().equals(dnlog.getOms_order_line_id()))
					{
						final LogisticTmall lt = new LogisticTmall();
						// 判断当前的orderline是否为赠品
						if (!this.validateIsGift(orderLineData))
						{
							if (orderLineData instanceof TaslyOrderLineData)
							{
								final TaslyOrderLineData taslyOrderLine = (TaslyOrderLineData) orderLineData;
								final List<OrderLineQuantityData> taslyOrderQuantityList = taslyOrderLine.getOrderLineQuantities();
								if (null != taslyOrderQuantityList && !taslyOrderQuantityList.isEmpty())// sourcing失败会出现
								{
									// get orderLine Quantity data for the express info
									// 循环判断orderLineQuantity有expresscode的情况（sourcing异常，会出现同一条orderLine对应多条orderlinequantity）

									final TaslyOrderLineQuantityData taslyOrderQuantity = this
											.getTaslyOrderLineQuantity(taslyOrderQuantityList);

									lt.setCompany_code(taslyOrderQuantity.getExpress_code());
									lt.setOut_sid(taslyOrderQuantity.getExpress_order_id());
									lt.setTradeId(taslyOrderData.getOriginal_order_id());
									lt.setSub_tids(taslyOrderLine.getThird_party_orderline_id());
									lt.setInner_source(taslyOrderData.getInner_source().toString());// enum
									lt.setOrderLineId(orderLineData.getOrderLineId());
									lt.setTaslyOrderData(taslyOrderData);
									lt.setDnlog(dnlog);
									logisticTmalls.add(lt);
								}
								else
								{
									Log.error("订单:" + dnlog.getOms_order_id() + "orderLinequantity 不存在");
									defaultDnLogService.updateSingleDNLog(dnlog, dnLog_noExpress);
								}
							}
						}
						else
						{
							defaultDnLogService.updateSingleDNLog(dnlog, dnLog_create);
						}
					}
				}
				// }
			}
			else
			{
				Log.error("订单:" + dnlog.getOms_order_id() + "不存在");
				defaultDnLogService.updateSingleDNLog(dnlog, dnLog_error);
			}
		}
		Log.info("dnlog获取数据成功");
		return logisticTmalls;
	}

	public TaslyOrderLineQuantityData getTaslyOrderLineQuantity(final List<OrderLineQuantityData> taslyOrderQuantityList)
	{
		for (final OrderLineQuantityData orderLineQuantity : taslyOrderQuantityList)
		{
			final TaslyOrderLineQuantityData taslyOrderLineQuantity = (TaslyOrderLineQuantityData) orderLineQuantity;
			if (null != taslyOrderLineQuantity.getExpress_code())
			{
				return taslyOrderLineQuantity;
			}
		}
		return (TaslyOrderLineQuantityData) taslyOrderQuantityList.get(0);
	}

	public boolean validateIsGift(final OrderLineData orderLine)
	{
		if (orderLine instanceof TaslyOrderLineData)
		{
			final TaslyOrderLineData taslyOrderLine = (TaslyOrderLineData) orderLine;
			if (null != taslyOrderLine.getGift_item_flag()
					&& taslyOrderLine.getGift_item_flag().equalsIgnoreCase(OrderConstants.ORDER_LINE_GIFT_FLAG))
			{
				Log.info("订单行项目号：" + orderLine.getOrderLineId() + "---是赠品,直接更新为create状态");
				if (this.filterUnCreateLogistic((TaslyOrderData) orderLine.getMyOrder()))
				{
					this.delDNLogs((TaslyOrderData) orderLine.getMyOrder());
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * 合并相同运单号的订单
	 * 订单号对应的子订单拥有相同的运单号，可以合并
	 * 如果不同订单号对应相同的运单号，不可以合并
	 * key 天猫id
	 *
	 * @param logisticTmalls
	 * @return
	 */

	public Map<String, List<LogisticTmall>> mergeSameLogisticCode(final List<LogisticTmall> logisticTmalls)
	{
		final Map<String, List<LogisticTmall>> logisticMap = new HashMap<String, List<LogisticTmall>>();
		for (final LogisticTmall logisticTmall : logisticTmalls)
		{
			if (null != logisticMap.get(logisticTmall.getTradeId()) && logisticMap.get(logisticTmall.getTradeId()).size() > 0)
			{
				logisticMap.get(logisticTmall.getTradeId()).add(logisticTmall);
			}
			else
			{
				final List<LogisticTmall> tempDataList = new ArrayList<LogisticTmall>();
				tempDataList.add(logisticTmall);
				logisticMap.put(logisticTmall.getTradeId(), tempDataList);
			}
		}
		Log.info("合并所有key为天猫Tid的数据完成");
		return logisticMap;
	}


	/**
	 * 合并相同运单号的子订单
	 *
	 * key 天猫运单id
	 *
	 * @param logisticTmalls
	 * @return
	 */

	public Map<String, List<LogisticTmall>> mergeSameLogisticCode_outSid(final List<LogisticTmall> logisticTmalls)
	{
		final Map<String, List<LogisticTmall>> logisticMap = new HashMap<String, List<LogisticTmall>>();
		for (final LogisticTmall logisticTmall : logisticTmalls)
		{
			if (null != logisticMap.get(logisticTmall.getOut_sid()) && logisticMap.get(logisticTmall.getOut_sid()).size() > 0)
			{
				logisticMap.get(logisticTmall.getOut_sid()).add(logisticTmall);
			}
			else
			{
				final List<LogisticTmall> tempDataList = new ArrayList<LogisticTmall>();
				tempDataList.add(logisticTmall);
				if (null != logisticTmall.getOut_sid())
				{
					logisticMap.put(logisticTmall.getOut_sid(), tempDataList);
				}
				else
				{
					Log.info("订单号:" + logisticTmall.getTradeId() + "物流单号为空，请检查");
					defaultDnLogService.updateSingleDNLog(logisticTmall.getDnlog(), dnLog_noExpress);
				}
			}
		}
		return logisticMap;
	}

	public boolean filterUnCreateLogistic(final TaslyOrderData taslyOrderData)
	{
		boolean isHaveUnCreateOrderLine = false;
		final List<OrderLineData> orderLinesData = taslyOrderData.getOrderLines();
		for (final OrderLineData orderLine : orderLinesData)
		{
			DNLogData dNLogData;
			try
			{
				dNLogData = defaultDnLogService.getSingleDNLogByOrderLineID(orderLine.getOrderLineId());
			}
			catch (final Exception e)
			{
				throw new RuntimeException(e);
			}
			if (null != dNLogData && dnLog_create.equals(dNLogData.getReplication_flag()))
			{
				isHaveUnCreateOrderLine = true;
			}
			else
			{
				isHaveUnCreateOrderLine = false;
				break;
			}
		}
		Log.debug("Logistic create : filter UnCreate Logistic :::" + isHaveUnCreateOrderLine);
		return isHaveUnCreateOrderLine;
	}

	@Override
	public void delOneOrderAndLogisticInfo(final List<DNLogData> dnlogs)
	{
		defaultDnLogService.removeReplicatedDNLogs(dnlogs);
	}


	/**
	 * there is one kind of story
	 * one tmall_order_line_id -> two oms_order_line_id
	 */
	public boolean filterOneToManyOrderLineIds(final TaslyOrderData taslyOrderData, final DNLogData dNLogData)
	{
		Log.info("当前OrderId|orderlineId:" + dNLogData.getOms_order_id() + "|" + dNLogData.getOms_order_line_id());
		final String tmall_order_line_id = this.getTmallOId(taslyOrderData, dNLogData);// get tmall oid
		boolean flag = true;
		for (final OrderLineData orderLineData : taslyOrderData.getOrderLines())
		{
			Log.info("订单一对多(赠品)场景比对--订单行项目Id" + orderLineData.getOrderLineId());
			if (orderLineData instanceof TaslyOrderLineData)
			{
				final TaslyOrderLineData taslyOrderLine = (TaslyOrderLineData) orderLineData;
				// 天猫oid相同但是omsOrderLineId不同
				if (tmall_order_line_id.equals(taslyOrderLine.getThird_party_orderline_id())
						&& !dNLogData.getOms_order_line_id().equals(taslyOrderLine.getOrderLineId().toString()))
				{
					Log.info("匹配成功");
					final DNLogData logdata = defaultDnLogService.getSingleDNLogByOrderIDAndOrderLineID(taslyOrderData.getOrderId(),
							taslyOrderLine.getOrderLineId());
					if (null != logdata && logdata.getReplication_flag().equals(dnLog_create))
					{
						// 符合条件得数据直接更新状态
						defaultDnLogService.updateSingleDNLog(dNLogData, dnLog_create);
						if (this.filterUnCreateLogistic(taslyOrderData))
						{
							this.delDNLogs(taslyOrderData);
						}
						flag = false;
						break;
					}
				}
			}
		}
		return flag;
	}

	// get the tmall oid
	public String getTmallOId(final TaslyOrderData taslyOrderData, final DNLogData dNLogData)
	{
		String tmall_order_line_id = "";

		for (final OrderLineData orderLineData : taslyOrderData.getOrderLines())
		{
			if (orderLineData instanceof TaslyOrderLineData)
			{
				final TaslyOrderLineData taslyOrderLine = (TaslyOrderLineData) orderLineData;
				if (taslyOrderLine.getOrderLineId().equals(dNLogData.getOms_order_line_id()))
				{
					tmall_order_line_id = taslyOrderLine.getThird_party_orderline_id();
				}
			}
		}
		return tmall_order_line_id;
	}


	/**
	 * @return the tmallLogisticDao
	 */
	public TmallLogisticOfflineDao getTmallLogisticDao()
	{
		return tmallLogisticDao;
	}

	/**
	 * @param tmallLogisticDao the tmallLogisticDao to set
	 */
	public void setTmallLogisticDao(final TmallLogisticOfflineDao tmallLogisticDao)
	{
		this.tmallLogisticDao = tmallLogisticDao;
	}

	/**
	 * @return the omsOrderRetrieverService
	 */
	public OmsOrderRetrieveService getOmsOrderRetrieverService()
	{
		return omsOrderRetrieverService;
	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}


	/**
	 * @return the defaultDnLogService
	 */
	public DefaultDNLogService getDefaultDnLogService()
	{
		return defaultDnLogService;
	}

	/**
	 * @param defaultDnLogService the defaultDnLogService to set
	 */
	public void setDefaultDnLogService(final DefaultDNLogService defaultDnLogService)
	{
		this.defaultDnLogService = defaultDnLogService;
	}


	/**
	 * @param defaultOrderService the defaultOrderService to set
	 */
	// public void setDefaultOrderService(final DefaultOrderService defaultOrderService)
	// {
	// this.defaultOrderService = defaultOrderService;
	// }

}
