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

import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.EventType;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.OrderIdGeneratorData;
import tasly.greathealth.oms.domain.order.OrderState;
import tasly.greathealth.oms.domain.order.PendingOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.transformer.OrderTransformer;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.thirdparty.order.OrderConvertService;
import tasly.greathealth.tmall.order.domain.OmsSaveOrderParameter;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;
import tasly.greathealth.tmall.order.services.impl.OrderContext;

import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.order.ItemInfo;
import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.jd.open.api.sdk.request.order.OrderVenderRemarkUpdateRequest;


/**
 *
 * @author libin
 */
public class JdOrderRetrieveServiceImpl extends AbstractHybrisService implements OmsOrderRetrieveService<OrderSearchInfo>,
		OrderConstants
{
	private OrderTransformer<OrderSearchInfo> orderTransformer;
	private OrderFacade orderFacade;
	private OrderConvertService<OrderSearchInfo, ItemInfo> jdOrderConvertService;

	private static JdClient jdJscClient;

	private final Logger LOG = OmsLoggerFactory.getJdorderlog();


	/**
	 * @param jdJscClient the jdJscClient to set
	 */
	public static void setJdJscClient(final JdClient jdJscClient)
	{
		JdOrderRetrieveServiceImpl.jdJscClient = jdJscClient;
	}



	@Override
	@Transactional
	public Order transformThirdPartyOrder2OmsOrder(final OrderSearchInfo thirdPartyOrder, final OrderContext orderContext)
			throws Exception
	{
		final OrderIdGeneratorData orderIdGenerator = this.getPersistenceManager().create(OrderIdGeneratorData.class);
		final SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_GENERATE_ID_DATE_FORMAT);
		final String temp = sf.format(new Date());
		orderContext
				.addProperty(
						DEFAULT_ORDER_ID_KEY,
						StringUtils.leftPad(String.valueOf(orderIdGenerator.getOrderId()), DEFAULT_ORDER_ID_LEN,
								DEFAULT_ORDER_ID_PLACEHOLDER) + temp);
		return orderTransformer.transformOrder(thirdPartyOrder, orderContext);
	}



	@Override
	public OrderSearchInfo splitOrder(final OrderSearchInfo trade, final OrderContext orderContext) throws Exception
	{
		this.jdOrderConvertService.setOriginalOrder(trade);
		this.jdOrderConvertService.setOrderContext(orderContext);
		return this.jdOrderConvertService.convertTrade();
	}


	@Override
	public OrderSearchInfo retrieveOrderDetail(final String orderID) throws Exception
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void saveOrder(final Order order)
	{
		synchronized (orderFacade)
		{
			orderFacade.createOrder(order);
		}
	}

	@Override
	public void updateSellerMemo(final String tid, final String originalMemo, final String newMemo, final boolean success)
	{

		final SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_UPDATE_DATE_FORMAT);
		final String formattedDate = sf.format(new Date());
		final OrderVenderRemarkUpdateRequest request = new OrderVenderRemarkUpdateRequest();
		// 订单号
		request.setOrderId("");
		// 订单备注（可为空备注信息）
		request.setRemark(StringUtils.defaultString(originalMemo) + System.getProperty("line.separator") + formattedDate + "---"
				+ StringUtils.defaultString(newMemo));
		// 流水号，不能重复的随机数
		// request.setTradeNo("");
		// 商家备注提示文字颜色值（默认为灰色） 0:灰色 1:红色 2:黄色 3:绿色 4:蓝色 5:紫色。
		if (success)
		{
			request.setFlag("3"); // 成功绿色
		}
		else
		{
			request.setFlag("2"); // 不成功黄色

		}


		try
		{
			// final OrderVenderRemarkUpdateResponse response = jdJscClient.execute(request);
			jdJscClient.execute(request);
		}
		catch (final JdException e)
		{
			LOG.error(e.getMessage(), e);
			throw new RuntimeException("Error happened when write memo back to JD[" + e.getMessage() + "]");
		}
	}

	@Override
	public void saveFailedOrder(final String tid, final String oid, final String refundFee, final EventType eventType,
			final String errorMsg, final OrderState state, final String rawMsg, final Date eventTime,
			final ChannelSource channelSource, final InnerSource innerSource)
	{

		// check if record is old or new
		@SuppressWarnings("deprecation")
		final List<PendingOrderData> pendingOrderDataList = super.getPersistenceManager().search(
				super.getPersistenceManager().createCriteriaQuery(PendingOrderData.class)
				.where(Restrictions.eq(PendingOrderData.TID, tid)).and(Restrictions.eq(PendingOrderData.OID, oid))
				.and(Restrictions.eq(PendingOrderData.EVENTTYPE, eventType)));
		if (null == pendingOrderDataList || pendingOrderDataList.size() == 0)
		{
			LOG.info("开始创建pendingOrders记录,京东订单id is " + tid);
			final PendingOrderData pendingOrderData = this.getPersistenceManager().create(PendingOrderData.class);
			pendingOrderData.setTid(tid);
			pendingOrderData.setOid(oid);
			pendingOrderData.setRefundfee(safe2Double(refundFee));
			pendingOrderData.setRawmsg(rawMsg);
			pendingOrderData.setErrormsg(errorMsg);
			pendingOrderData.setEventtype(eventType);
			pendingOrderData.setState(state);
			pendingOrderData.setEventtime(eventTime);
			pendingOrderData.setChannelsource(channelSource);
			pendingOrderData.setInnersource(innerSource);
		}
		else
		{
			for (final PendingOrderData pendingOrderData : pendingOrderDataList)
			{
				LOG.info("开始更新pendingOrders记录状态为FAIL,京东订单id is " + tid);
				pendingOrderData.setState(OrderState.FAIL);
			}
		}
	}

	private Double safe2Double(final String str)
	{
		return StringUtils.isNotBlank(str) && NumberUtils.isNumber(str) ? Double.parseDouble(str) : 0d;
	}

	@Override
	public void batchSaveFailOrPendingOrder(final List<OmsSaveOrderParameter> saveOrderParameters) throws Exception
	{
		// YTODO Auto-generated method stub

	}



	@Override
	public void createRefund(final String orderId, final String refundFee, final String lineId, final boolean sendGoods)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void successCompleteRefund(final String orderId, final String lineId, final String refundFee, final boolean sendGoods)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void closeRefund(final String orderId, final String lineId, final boolean sendGoods)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void createSellerShipment(final String tid, final String oid)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void cancelShipment(final String orderId)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void deleteTaslyOrder(final String orderId)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void loadPendingOrders() throws Exception
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public TaslyOrderLineData getTaslyOrerLineDetail(final String orderLineId)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public TaslyOrderData getTaslyOrderDataDetail(final String omsorderId)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public TaslyOrderData getTaslyOrderDataDetailByTmallId(final String tmallorderId)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public TaslyOrderLineQuantityData getTaslyOrderLineQuntity(final OrderLineData orderLine)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWriteBack2Tamll(final boolean writeBack2Tamll)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public boolean isWriteBack2Tamll()
	{
		// YTODO Auto-generated method stub
		return false;
	}

	@Override
	public List<PendingOrderData> loadPendingOrdersByTID(final String tid)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveSuccessPendingOrder(final String tid, final String oid, final String eventType)
	{
		// YTODO Auto-generated method stub

	}



	@Override
	public void updateSellerMemo(final String tid, final String memo)
	{
		// YTODO Auto-generated method stub

	}



	@Override
	public void filterOutUnpayedLine(final OrderSearchInfo trade)
	{
		// YTODO Auto-generated method stub

	}



	/**
	 * @param orderTransformer the orderTransformer to set
	 */
	public void setOrderTransformer(final OrderTransformer<OrderSearchInfo> orderTransformer)
	{
		this.orderTransformer = orderTransformer;
	}



	/**
	 * @param orderFacade the orderFacade to set
	 */
	public void setOrderFacade(final OrderFacade orderFacade)
	{
		this.orderFacade = orderFacade;
	}



	/**
	 * @param jdOrderConvertService the jdOrderConvertService to set
	 */
	public void setJdOrderConvertService(final OrderConvertService<OrderSearchInfo, ItemInfo> jdOrderConvertService)
	{
		this.jdOrderConvertService = jdOrderConvertService;
	}



	@Override
	public void updateLogisiticsAddress(final String tid) throws Exception
	{
		// YTODO Auto-generated method stub

	}



	@Override
	public void updateSellerMemo(final Long tid, final String originalMemo, final String newMemo, final boolean success)
	{
		// YTODO Auto-generated method stub

	}



}
