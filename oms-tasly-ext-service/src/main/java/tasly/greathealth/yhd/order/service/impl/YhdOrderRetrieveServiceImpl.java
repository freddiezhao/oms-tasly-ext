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

import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import tasly.greathealth.thirdparty.order.common.TaslyThirdUtils;
import tasly.greathealth.tmall.order.domain.OmsSaveOrderParameter;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;
import tasly.greathealth.tmall.order.services.impl.OrderContext;

import com.yhd.YhdClient;
import com.yhd.object.trade.Trade;
import com.yhd.request.trade.TradeMemoUpdateRequest;


/**
 *
 * @author libin
 */
public class YhdOrderRetrieveServiceImpl extends AbstractHybrisService implements OmsOrderRetrieveService<Trade>, OrderConstants
{
	private OrderTransformer<Trade> orderTransformer;
	private OrderFacade orderFacade;
	private OrderConvertService<Trade, com.yhd.object.order.Order> yhdOrderConvertService;
	private YhdClient client;
	private String sessionKey;

	private final Logger LOG = OmsLoggerFactory.getYhdorderlog();


	/**
	 * @param client the client to set
	 */
	public void setClient(final YhdClient client)
	{
		this.client = client;
	}

	/**
	 * @param sessionKey the sessionKey to set
	 */
	public void setSessionKey(final String sessionKey)
	{
		this.sessionKey = sessionKey;
	}

	/**
	 * @param orderTransformer the orderTransformer to set
	 */
	public void setOrderTransformer(final OrderTransformer<Trade> orderTransformer)
	{
		this.orderTransformer = orderTransformer;
	}

	/**
	 * @param yhdOrderConvertService the yhdOrderConvertService to set
	 */
	public void setYhdOrderConvertService(final OrderConvertService<Trade, com.yhd.object.order.Order> yhdOrderConvertService)
	{
		this.yhdOrderConvertService = yhdOrderConvertService;
	}

	/**
	 * @param orderFacade the orderFacade to set
	 */
	public void setOrderFacade(final OrderFacade orderFacade)
	{
		this.orderFacade = orderFacade;
	}

	@Override
	@Transactional
	public Order transformThirdPartyOrder2OmsOrder(final Trade thirdPartyOrder, final OrderContext orderContext) throws Exception
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
	public Trade splitOrder(final Trade trade, final OrderContext orderContext) throws Exception
	{
		this.yhdOrderConvertService.setOriginalOrder(trade);
		this.yhdOrderConvertService.setOrderContext(orderContext);
		return this.yhdOrderConvertService.convertTrade();
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
	public void updateSellerMemo(final Long tid, final String originalMemo, final String newMemo, final boolean success)
	{
		final TradeMemoUpdateRequest req = new TradeMemoUpdateRequest();

		final String formattedDate = TaslyThirdUtils.formatTime(new Date());
		req.setTid(tid);
		if (success)
		{
			req.setFlag(3L);// 成功绿色
		}
		else
		{
			req.setFlag(2L);// 不成功黄色

		}
		req.setMemo(StringUtils.defaultString(originalMemo) + System.getProperty("line.separator") + formattedDate + "---"
				+ StringUtils.defaultString(newMemo));
		req.setReset(false);
		try
		{
			client.excute(req, sessionKey);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			throw new RuntimeException("Error happened when write memo back to TMALL[" + e.getMessage() + "]");
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
			pendingOrderData.setRefundfee(TaslyThirdUtils.safe2Double(refundFee));
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
	public void filterOutUnpayedLine(final Trade trade)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void updateLogisiticsAddress(final String tid) throws Exception
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public Trade retrieveOrderDetail(final String orderID) throws Exception
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSellerMemo(final String tid, final String originalMemo, final String newMemo, final boolean success)
	{
		// YTODO Auto-generated method stub

	}
}
