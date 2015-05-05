/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.tmall.order.services.impl;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.AmbiguousIdentifierException;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.service.AbstractHybrisService;
import com.hybris.oms.service.shipment.ShipmentService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.order.TaslyOrderFacade;
import tasly.greathealth.oms.api.order.dto.PackingType;
import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.domain.order.ApproveStatus;
import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.EventType;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.OrderIdGeneratorData;
import tasly.greathealth.oms.domain.order.OrderState;
import tasly.greathealth.oms.domain.order.PendingOrderData;
import tasly.greathealth.oms.domain.order.ShippingLockStatus;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.services.TaslyOrderService;
import tasly.greathealth.oms.transformer.OrderTransformer;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.thirdparty.order.OrderConvertService;
import tasly.greathealth.thirdparty.order.TaslyOrderDataHandler;
import tasly.greathealth.thirdparty.order.TaslyOrderLineDataHandler;
import tasly.greathealth.thirdparty.order.TaslyOrderLineQuantityDataHandler;
import tasly.greathealth.tmall.order.domain.OmsSaveOrderParameter;
import tasly.greathealth.tmall.order.exception.ThirdPartyApiInvokeException;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.request.TradeMemoUpdateRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;


/**
 * An implementation for retrieving order information from Tmall
 *
 * @author Jason Bao
 */
public class OmsOrderRetrieverServiceTmallImpl extends AbstractHybrisService implements OmsOrderRetrieveService<Trade>,
OrderConstants
{
	protected static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	protected static final String DEFAULT_TMALL_FIELDS = "payment,created,buyer_message,promotion_details,seller_memo,total_fee,adjust_fee,tid,status,buyer_nick,receiver_name,receiver_address,receiver_mobile,receiver_phone,discount_fee,post_fee,has_yfx,yfx_fee,has_post_fee,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,point_fee,received_payment,pay_time,orders,alipay_no";

	protected String defaultTmallStoreSessionkey;

	protected OrderFacade orderFacade;

	protected ShipmentFacade shipmentFacade;

	protected ShipmentService shipmentService;

	protected OrderTransformer<Trade> orderTransformer;

	// private OrderConvertServer orderConvertServer;
	protected OrderConvertService<Trade, com.taobao.api.domain.Order> orderConvertService;

	protected TaobaoClient client;

	protected boolean writeBack2Tamll = false;

	protected Converter<OrderData, Order> converter;

	protected TaslyOrderFacade taslyOrderFacade;

	protected TaslyOrderService taslyOrderService;

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


	/**
	 * @param converter the converter to set
	 */
	public void setConverter(final Converter<OrderData, Order> converter)
	{
		this.converter = converter;
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
	public Trade splitOrder(final Trade trade, final OrderContext orderContext) throws Exception
	{
		// return orderConvertServer.convertTrade(trade, orderContext);
		this.orderConvertService.setOriginalOrder(trade);
		this.orderConvertService.setOrderContext(orderContext);
		return this.orderConvertService.convertTrade();
	}

	/**
	 * @param taslyOrderFacade the taslyOrderFacade to set
	 */
	public void setTaslyOrderFacade(final TaslyOrderFacade taslyOrderFacade)
	{
		this.taslyOrderFacade = taslyOrderFacade;
	}


	/**
	 * @param taslyOrderService the taslyOrderService to set
	 */
	public void setTaslyOrderService(final TaslyOrderService taslyOrderService)
	{
		this.taslyOrderService = taslyOrderService;
	}


	@Override
	public Trade retrieveOrderDetail(final String orderID) throws Exception
	{
		final TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
		req.setFields(DEFAULT_TMALL_FIELDS);
		req.setTid(Long.parseLong(orderID));
		TradeFullinfoGetResponse response = null;
		int count = 0;
		boolean success = false;
		try
		{
			do
			{
				count++;
				LOG.info("Retrieve order detail from tmall. try time [" + count + "]");
				response = client.execute(req, defaultTmallStoreSessionkey);
				if (response.getTrade() != null)
				{
					success = true;
					break;
				}
				else
				{
					Thread.sleep(1000);
				}
			}
			while (success == false && count <= 3);

			if (success == false)
			{
				throw new ThirdPartyApiInvokeException("Get error message from TMall. Message[ " + response.getErrorCode() + "]");
			}

		}
		catch (final ApiException e)
		{
			throw new ThirdPartyApiInvokeException("Invoke taobao API [Find Detail] failed!", e);
		}
		return response.getTrade();
	}

	@SuppressWarnings("deprecation")
	@Override
	public TaslyOrderLineData getTaslyOrerLineDetail(final String orderLineId)
	{
		return super.getPersistenceManager().createCriteriaQuery(TaslyOrderLineData.class)
				.where(Restrictions.eq(TaslyOrderLineData.ORDERLINEID, orderLineId)).uniqueResult();
	}

	@SuppressWarnings("deprecation")
	@Override
	public TaslyOrderData getTaslyOrderDataDetail(final String oms_orderId)
	{
		try
		{
			return super.getPersistenceManager().createCriteriaQuery(TaslyOrderData.class)
					.where(Restrictions.eq(TaslyOrderData.ORDERID, oms_orderId)).uniqueResult();
		}
		catch (final AmbiguousIdentifierException e)
		{
			LOG.info("get Order exception: more than one items");
			return null;
		}
		catch (final ManagedObjectNotFoundException e1)
		{
			LOG.info("get Order exception: Could not retrieve result for this");
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public TaslyOrderData getTaslyOrderDataDetailByTmallId(final String tmall_orderId)
	{
		return super.getPersistenceManager().createCriteriaQuery(TaslyOrderData.class)
				.where(Restrictions.eq(TaslyOrderData.ORIGINAL_ORDER_ID, tmall_orderId)).uniqueResult();
	}


	@SuppressWarnings("deprecation")
	@Override
	public TaslyOrderLineQuantityData getTaslyOrderLineQuntity(final OrderLineData orderLine)
	{
		return super.getPersistenceManager().createCriteriaQuery(TaslyOrderLineQuantityData.class)
				.where(Restrictions.eq(TaslyOrderLineQuantityData.ORDERLINE, orderLine)).uniqueResult();
	}

	@Override
	@Transactional
	public void createRefund(final String orderId, final String lineId, final String refundFee, final boolean sendGoods)
	{
		LOG.info("Create refund. tid[" + orderId + " oid[" + lineId + "]");
		checkParameter(orderId, lineId);
		final TaslyOrderData taslyOrderData = getTaslyOrderDataDetailByTmallId(orderId);
		updateOrderData(taslyOrderData, new TaslyRefundStatusHandler(DEFAULT_REFUND_CREATE_HINT, "YES", lineId));
		updateOrderData(taslyOrderData, new TaslyRefundFeeHandler(refundFee, lineId));

		// 获取该订单下所有的赠品行，进行退货退款的操作，锁定后业务需检查并将不需退款的赠品的标识修改
		updateOrderLineDataByGiftFlag(taslyOrderData, refundFee);

		if (hasRefundLine(taslyOrderData) == false)// 若所有行都没有处于退款中，则尝试锁定整张订单
		{
			taslyOrderFacade.updateTaslyOrderLockStatus(taslyOrderData.getOrderId(), ShippingLockStatus.PENDING_LOCK.name());
		}
	}

	private boolean hasRefundLine(final TaslyOrderData taslyOrderData)
	{
		final List<OrderLineData> orderLines = taslyOrderData.getOrderLines();
		for (int i = orderLines.size() - 1; i >= 0; i--)
		{
			final OrderLineData lineData = orderLines.get(i);
			if (lineData instanceof TaslyOrderLineData)
			{
				final TaslyOrderLineData taslyOrderLineData = (TaslyOrderLineData) lineData;
				if (DEFAULT_REFUND_CREATE_HINT.equalsIgnoreCase(taslyOrderLineData.getOrderLineStatus()))
				{
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public void successCompleteRefund(final String orderId, final String lineId, final String refundFee, final boolean sendGoods)
	{
		LOG.info("Refund successful. tid[" + orderId + " oid[" + lineId + "]");
		checkParameter(orderId, lineId);
		final TaslyOrderData taslyOrderData = getTaslyOrderDataDetailByTmallId(orderId);
		updateOrderData(taslyOrderData, new TaslyRefundStatusHandler(DEFAULT_REFUND_SUCCESS_HINT, null, lineId));
		// updateOrderData(taslyOrderData, new TaslyRefundFeeHandler(refundFee, lineId));
		if (sendGoods == false)
		{
			final List<ShipmentData> shipments = shipmentService.findShipmentsByOrder(taslyOrderData);
			if (CollectionUtils.isEmpty(shipments))
			{
				LOG.warn("There is no shipment for order. OrderId[" + taslyOrderData.getOrderId() + "]");
			}
			else if (shipments.size() > 1)
			{
				LOG.warn("There are more than 1 shipments for order. OrderId[" + taslyOrderData.getOrderId() + "]");
			}
			else
			{
				final ShipmentData shipmentData = shipments.get(0);
				if ("CANCELLED".equalsIgnoreCase(shipmentData.getOlqsStatus()) == false)// 如果未取消过，取消整张订单
				{
					orderFacade.cancelOrder(taslyOrderData.getOrderId());
				}
			}
		}
	}

	@Override
	@Transactional
	public void createSellerShipment(final String tid, final String oid)
	{
		LOG.info("Create Seller Shipment. tid[" + tid + "]");
		final TaslyOrderData taslyOrderData = this.getTaslyOrderDataDetailByTmallId(tid);
		updateOrderData(taslyOrderData, new TaslySellerShipStatusHandler(DEFAULT_SELLER_SHIPPING_HINT, oid));
		// 纯外租或者刷单商品会Confirm Shipment
		if (RENT_RUN.equalsIgnoreCase(taslyOrderData.getMerchant_tag())
				|| ApproveStatus.PROCESSING.equals(taslyOrderData.getApprove_status()))
		{
			LOG.info("外租订单或刷单订单,ConfirmShipment! orderId[" + tid + "] ");
			final List<ShipmentData> shipmentDatas = shipmentService.findShipmentsByOrder(taslyOrderData);
			final Set<String> shipmentIds = new HashSet<String>();
			for (int i = shipmentDatas.size() - 1; i >= 0; i--)
			{
				final ShipmentData shipmentData = shipmentDatas.get(i);
				shipmentIds.add(String.valueOf(shipmentData.getShipmentId()));
			}
			shipmentFacade.confirmShipments(shipmentIds);
		}
	}

	public void updateOrderData(final TaslyOrderData taslyOrderData, final TaslyOrderDataHandler taslyOrderDataHandler)
	{
		this.updateOrderData(taslyOrderData, taslyOrderDataHandler, null, null);
	}

	public void updateOrderData(final TaslyOrderData taslyOrderData, final TaslyOrderLineDataHandler taslyOrderLineDataHandler)
	{
		this.updateOrderData(taslyOrderData, null, taslyOrderLineDataHandler, null);
	}

	public void updateOrderData(final TaslyOrderData taslyOrderData,
			final TaslyOrderLineQuantityDataHandler taslyOrderLineQuantityDataHandler)
	{
		this.updateOrderData(taslyOrderData, null, null, taslyOrderLineQuantityDataHandler);
	}

	public void updateOrderData(final TaslyOrderData taslyOrderData, final TaslyOrderDataHandler taslyOrderDataHandler,
			final TaslyOrderLineDataHandler taslyOrderLineDataHandler)
	{
		this.updateOrderData(taslyOrderData, taslyOrderDataHandler, taslyOrderLineDataHandler, null);
	}

	public void updateOrderData(final TaslyOrderData taslyOrderData, final TaslyOrderLineDataHandler taslyOrderLineDataHandler,
			final TaslyOrderLineQuantityDataHandler taslyOrderLineQuantityDataHandler)
	{
		this.updateOrderData(taslyOrderData, null, taslyOrderLineDataHandler, taslyOrderLineQuantityDataHandler);
	}

	public void updateOrderData(final TaslyOrderData taslyOrderData, final TaslyOrderDataHandler taslyOrderDataHandler,
			final TaslyOrderLineDataHandler taslyOrderLineDataHandler,
			final TaslyOrderLineQuantityDataHandler taslyOrderLineQuantityDataHandler)
	{
		if (taslyOrderDataHandler != null)
		{
			taslyOrderDataHandler.handlerOrderData(taslyOrderData);
		}
		final List<OrderLineData> lineDatas = taslyOrderData.getOrderLines();
		for (int i = lineDatas.size() - 1; i >= 0; i--)
		{
			final OrderLineData lineData = lineDatas.get(i);
			if (lineData instanceof TaslyOrderLineData)
			{
				final TaslyOrderLineData taslyOrderLineData = (TaslyOrderLineData) lineData;
				if (taslyOrderLineDataHandler != null)
				{
					taslyOrderLineDataHandler.handlerTaslyOrderLineData(taslyOrderLineData);
				}
				final List<OrderLineQuantityData> orderLineQuantityDatas = taslyOrderLineData.getOrderLineQuantities();
				for (int j = orderLineQuantityDatas.size() - 1; j >= 0; j--)
				{
					final OrderLineQuantityData orderLineQuantityData = orderLineQuantityDatas.get(j);
					if (orderLineQuantityData instanceof TaslyOrderLineQuantityData)
					{
						final TaslyOrderLineQuantityData taslyOrderLineQuantityData = (TaslyOrderLineQuantityData) orderLineQuantityData;
						if (taslyOrderLineQuantityDataHandler != null)
						{
							taslyOrderLineQuantityDataHandler.handleOrderLineQuantityData(taslyOrderLineQuantityData);
						}
					}
				}
			}
		}
	}


	@Override
	@Transactional
	public void cancelShipment(final String orderId)
	{
		LOG.info("Cancel Shipment. tid[" + orderId + "]");
		final TaslyOrderData taslyOrderData = this.getTaslyOrderDataDetailByTmallId(orderId);
		final List<ShipmentData> shipmentDatas = shipmentService.findShipmentsByOrder(taslyOrderData);
		for (int i = shipmentDatas.size() - 1; i >= 0; i--)
		{
			final Long shipmentId = shipmentDatas.get(i).getShipmentId();
			shipmentFacade.cancelShipment(String.valueOf(shipmentId));
		}
	}


	/**
	 * @param orderFacade the orderFacade to set
	 */
	public void setOrderFacade(final OrderFacade orderFacade)
	{
		this.orderFacade = orderFacade;
	}


	/**
	 * @param orderConvertService the orderConvertService to set
	 */
	public void setOrderConvertService(final OrderConvertService<Trade, com.taobao.api.domain.Order> orderConvertService)
	{
		this.orderConvertService = orderConvertService;
	}


	/**
	 * @param orderTransformer the orderTransformer to set
	 */
	public void setOrderTransformer(final OrderTransformer<Trade> orderTransformer)
	{
		this.orderTransformer = orderTransformer;
	}

	@Override
	@Transactional
	public void closeRefund(final String orderId, final String lineId, final boolean sendGoods)
	{
		LOG.info("Refund close. tid[" + orderId + " oid[" + lineId + "]");
		checkParameter(orderId, lineId);
		final TaslyOrderData taslyOrderData = getTaslyOrderDataDetailByTmallId(orderId);
		updateOrderData(taslyOrderData, new TaslyRefundStatusHandler(DEFAULT_REFUND_SELLER_REFUSE_HINT, null, lineId));
	}

	private void checkParameter(final String orderId, final String lineId)
	{
		if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(lineId))
		{
			throw new IllegalArgumentException("OrderId[" + orderId + "]  LineId " + lineId + "]");
		}
	}

	/**
	 * @param shipmentFacade the shipmentFacade to set
	 */
	public void setShipmentFacade(final ShipmentFacade shipmentFacade)
	{
		this.shipmentFacade = shipmentFacade;
	}

	/**
	 * @param shipmentService the shipmentService to set
	 */
	public void setShipmentService(final ShipmentService shipmentService)
	{
		this.shipmentService = shipmentService;
	}

	private static class TaslySellerShipStatusHandler implements TaslyOrderLineDataHandler
	{
		private final String lineStatus;
		private final String expectedLineId;

		TaslySellerShipStatusHandler(final String lineStatus, final String expectedLineId)
		{
			this.expectedLineId = expectedLineId;
			this.lineStatus = lineStatus;
		}

		@Override
		public void handlerTaslyOrderLineData(final TaslyOrderLineData taslyOrderLineData, final Object... paras)
		{
			if (expectedLineId != null && expectedLineId.equalsIgnoreCase(taslyOrderLineData.getThird_party_orderline_id()))
			{
				taslyOrderLineData.setOrderLineStatus(lineStatus);
			}
		}
	}

	private static class TaslyRefundStatusHandler implements TaslyOrderLineDataHandler
	{
		private final String refundStatus;
		private final String refundFlag;
		private final String expectedLineId;

		TaslyRefundStatusHandler(final String refundStatus, final String refundFlag, final String expectedLineId)
		{
			this.expectedLineId = expectedLineId;
			this.refundStatus = refundStatus;
			this.refundFlag = refundFlag;
		}

		@Override
		public void handlerTaslyOrderLineData(final TaslyOrderLineData taslyOrderLineData, final Object... paras)
		{
			if (expectedLineId != null && expectedLineId.equalsIgnoreCase(taslyOrderLineData.getThird_party_orderline_id()))
			{
				taslyOrderLineData.setRefundstatus(refundStatus);
				if (refundFlag != null)
				{
					taslyOrderLineData.setRefundflag(refundFlag);
				}
			}
		}
	}

	private static class TaslyRefundFeeHandler implements TaslyOrderDataHandler
	{
		private final String refundFee;
		private final String expectedLineId;

		TaslyRefundFeeHandler(final String refundFee, final String expectedLineId)
		{
			this.expectedLineId = expectedLineId;
			this.refundFee = refundFee;
		}

		@Override
		public void handlerOrderData(final TaslyOrderData taslyOrderData, final Object... paras)
		{
			final List<OrderLineData> lines = taslyOrderData.getOrderLines();
			final int cnt = lines.size();
			for (int i = 0; i < cnt; i++)
			{
				// 当有同样天猫OID时，只更新其中一个
				final OrderLineData orderLineData = lines.get(i);
				if (orderLineData instanceof TaslyOrderLineData)
				{
					final TaslyOrderLineData taslyOrderLineData = (TaslyOrderLineData) orderLineData;
					if (expectedLineId != null && expectedLineId.equalsIgnoreCase(taslyOrderLineData.getThird_party_orderline_id()))
					{
						if (StringUtils.isNotBlank(refundFee) && NumberUtils.isNumber(refundFee))
						{
							taslyOrderLineData.setRefundamount(Double.parseDouble(refundFee));
						}
						break;
					}
				}
			}

		}
	}

	// private static class TaslyRefundFeeHandler implements TaslyOrderLineDataHandler
	// {
	//
	// private final String refundFee;
	// private final String expectedLineId;
	//
	// TaslyRefundFeeHandler(final String refundFee, final String expectedLineId)
	// {
	// this.expectedLineId = expectedLineId;
	// this.refundFee = refundFee;
	// }
	//
	// @Override
	// public void handlerTaslyOrderLineData(final TaslyOrderLineData orderLineData, final Object... paras)
	// {
	// final TaslyOrderLineData taslyOrderLineData = orderLineData;
	// if (expectedLineId.equalsIgnoreCase(taslyOrderLineData.getThird_party_orderline_id()))
	// {
	// if (StringUtils.isNotBlank(refundFee) && NumberUtils.isNumber(refundFee))
	// {
	// taslyOrderLineData.setRefundamount(Double.parseDouble(refundFee));
	// }
	// }
	//
	// }
	// }

	@Override
	@Transactional
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
			LOG.info("开始创建pendingOrders记录,tid is " + tid);
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
				LOG.info("开始更新pendingOrders记录状态为FAIL,tid is " + tid);
				pendingOrderData.setState(OrderState.FAIL);
			}
		}
	}

	private Double safe2Double(final String str)
	{
		return StringUtils.isNotBlank(str) && NumberUtils.isNumber(str) ? Double.parseDouble(str) : 0d;
	}


	@Override
	@Transactional
	public void batchSaveFailOrPendingOrder(final List<OmsSaveOrderParameter> saveOrderParameters) throws Exception
	{
		for (final OmsSaveOrderParameter osop : saveOrderParameters)
		{
			this.saveFailedOrder(osop.getTid(), osop.getOid(), osop.getRefundFee(), osop.getEventType(), osop.getErrorMessage(),
					osop.getState(), osop.getRawMsg(), osop.getEventtime(), osop.getChannelSource(), osop.getInnerSource());
			LOG.debug("save into database");
		}
	}


	@Override
	@Transactional
	public void deleteTaslyOrder(final String orderId)
	{
		final TaslyOrderData taslyOrderData = getTaslyOrderDataDetailByTmallId(orderId);
		this.getPersistenceManager().remove(taslyOrderData);
	}


	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public void loadPendingOrders() throws Exception
	{
		final List<PendingOrderData> pendingOrders = super.getPersistenceManager().search(
				super.getPersistenceManager().createCriteriaQuery(PendingOrderData.class)
				.where(Restrictions.eq(PendingOrderData.STATE, OrderState.PENDING)));
		for (int i = pendingOrders.size() - 1; i >= 0; i--)
		{
			pendingOrders.get(i).setState(OrderState.PROCESSED);
		}
	}


	@Override
	public void updateSellerMemo(final Long tid, final String originalMemo, final String newMemo, final boolean success)
	{
		final TradeMemoUpdateRequest req = new TradeMemoUpdateRequest();
		final SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_UPDATE_DATE_FORMAT);
		final String formattedDate = sf.format(new Date());
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
			client.execute(req, defaultTmallStoreSessionkey);
		}
		catch (final ApiException e)
		{
			LOG.error(e.getMessage(), e);
			throw new RuntimeException("Error happened when write memo back to TMALL[" + e.getMessage() + "]");
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
	 * @param defaultTmallStoreSessionkey the defaultTmallStoreSessionkey to set
	 */
	public void setDefaultTmallStoreSessionkey(final String defaultTmallStoreSessionkey)
	{
		this.defaultTmallStoreSessionkey = defaultTmallStoreSessionkey;
	}


	@Override
	public synchronized void setWriteBack2Tamll(final boolean writeBack2Tamll)
	{
		this.writeBack2Tamll = writeBack2Tamll;
	}


	/**
	 * @return the writeBack2Tamll
	 */
	@Override
	public synchronized boolean isWriteBack2Tamll()
	{
		return writeBack2Tamll;
	}


	/**
	 * fetch pendingOrderData list according to tid
	 *
	 * @param tid 天猫订单号
	 * @return List<PendingOrderData>
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public List<PendingOrderData> loadPendingOrdersByTID(final String tid)
	{
		return super.getPersistenceManager().search(
				super.getPersistenceManager().createCriteriaQuery(PendingOrderData.class)
				.where(Restrictions.eq(PendingOrderData.TID, tid))
				.and(Restrictions.eq(PendingOrderData.STATE, OrderState.FAIL)));

	}

	@Override
	@Transactional
	public void saveSuccessPendingOrder(final String tid, final String oid, final String eventType)
	{
		// check if record is old or new
		@SuppressWarnings("deprecation")
		final List<PendingOrderData> pendingOrderDataList = super.getPersistenceManager().search(
				super.getPersistenceManager().createCriteriaQuery(PendingOrderData.class)
				.where(Restrictions.eq(PendingOrderData.TID, tid)).and(Restrictions.eq(PendingOrderData.OID, oid))
				.and(Restrictions.eq(PendingOrderData.STATE, OrderState.FAIL))
				.and(Restrictions.eq(PendingOrderData.EVENTTYPE, EventType.valueOf(eventType))));


		for (final PendingOrderData pendingOrderData : pendingOrderDataList)
		{
			pendingOrderData.setState(OrderState.SUCCESS);
		}

	}


	@Override
	public void filterOutUnpayedLine(final Trade trade)
	{
		if (trade == null)
		{
			return;
		}
		final List<com.taobao.api.domain.Order> orderLines = trade.getOrders();
		final Iterator<com.taobao.api.domain.Order> lineIt = orderLines.iterator();
		while (lineIt.hasNext())
		{
			final com.taobao.api.domain.Order orderLine = lineIt.next();
			if (TRADE_CLOSED_BY_TAOBAO.equalsIgnoreCase(orderLine.getStatus()))
			{
				LOG.info("发现被关闭交易行：OID[" + orderLine.getOid() + "]");
				lineIt.remove();
			}
		}
	}


	@Override
	@Transactional
	public void updateSellerMemo(final String tid, final String memo)
	{
		LOG.info("备注信息修改: Tid[" + tid + "] Memo[" + memo + "]");
		final TaslyOrderData taslyOrderData = this.getTaslyOrderDataDetailByTmallId(tid);
		if (memo != null && memo.equalsIgnoreCase(taslyOrderData.getSeller_message()))
		{
			LOG.info("客服备注与当前天猫备注相同。");
			return;
		}
		if (PackingType.PACKED.name().equalsIgnoreCase(taslyOrderData.getPacking().name()))
		{
			LOG.info("该订单已经进入波次，备注更新失败。");
			return;
		}
		taslyOrderData.setSeller_message(memo);

		final String eccOrderId = taslyOrderData.getEcc_order_id();
		if (eccOrderId != null)
		{// 说明ERP端有此订单消息，发送更新请求
			final Order order = converter.convert(taslyOrderData);
			if (order instanceof TaslyOrder)
			{
				final TaslyOrder taslyOrder = (TaslyOrder) order;
				LOG.info("向ERP发送更新客服备注消息。客服备注[" + taslyOrder.getSellerMessage() + "]");
				taslyOrderFacade.updateTaslyOrder(taslyOrder);
			}
		}
	}


	@Override
	@Transactional
	public void updateLogisiticsAddress(final String tid) throws Exception
	{
		final Trade trade = this.retrieveOrderDetail(tid);

		// 地址修改覆盖的字段如下：省、市、区、街道、邮政编码、街道地址、收货人姓名、电话、手机。
		// 其中街道字段在交易API中会合并到Address字段中。
		final String tmallState = trade.getReceiverState();// 省
		final String tmallCity = trade.getReceiverCity();// 市
		final String tmallDistrict = trade.getReceiverDistrict();// 区
		final String tmallPostzip = trade.getReceiverZip();// 邮政编码
		final String tmallAddress = trade.getReceiverAddress();// 街道地址
		final String tmallReceiverName = trade.getReceiverName();// 收货人姓名
		final String tmallPhoneNumber = trade.getReceiverPhone();// 电话
		final String tmallMobile = trade.getReceiverMobile();// 手机

		final TaslyOrderData taslyOrderData = this.getTaslyOrderDataDetailByTmallId(tid);

		final AddressVT originalAddress = taslyOrderData.getShippingAddress();
		if (originalAddress == null)
		{
			LOG.warn("没有对应的Shipping地址信息，请检查OMS原订单。 TID[" + tid + "]");
			return;
		}
		final String newAddress = StringUtils.isBlank(tmallAddress) ? originalAddress.getAddressLine1() : tmallAddress; // 街道地址
		final String newCity = StringUtils.isBlank(tmallCity) ? originalAddress.getCityName() : tmallCity;// 市
		final String newState = StringUtils.isBlank(tmallState) ? originalAddress.getCountrySubentity() : tmallState;// 省
		final String newPostzip = StringUtils.isBlank(tmallPostzip) ? originalAddress.getPostalZone() : tmallPostzip;// 邮政编码
		final String newPhoneNumber = StringUtils.isBlank(tmallPhoneNumber) ? originalAddress.getPhoneNumber() : tmallPhoneNumber;// 电话

		final AddressVT addressVT = new AddressVT(newAddress, originalAddress.getAddressLine2(), newCity, newState, newPostzip,
				originalAddress.getLatitudeValue(), originalAddress.getLongitudeValue(),
				originalAddress.getCountryIso3166Alpha2Code(), originalAddress.getCountryName(), originalAddress.getName(),
				newPhoneNumber);

		taslyOrderData.setShippingAddress(addressVT);

		final String originalCity = taslyOrderData.getShad_city();
		// 市，在Order中也存在
		if (tmallCity != null && tmallCity.equalsIgnoreCase(originalCity) == false)
		{
			taslyOrderData.setShad_city(tmallCity);
		}
		// 区
		final String originalDistrict = taslyOrderData.getShad_city();
		if (tmallDistrict != null && tmallDistrict.equalsIgnoreCase(originalDistrict) == false)
		{
			taslyOrderData.setShad_citydistrict(tmallDistrict);
		}
		// 收货人姓名
		final String originalRecieverName = taslyOrderData.getShippingFirstName();
		if (tmallReceiverName != null && tmallReceiverName.equalsIgnoreCase(originalRecieverName) == false)
		{
			taslyOrderData.setShippingFirstName(tmallReceiverName);
			taslyOrderData.setShippingLastName(tmallReceiverName);// LastName也改了，保持统一
		}
		// 手机
		final String originalRecieverMobile = taslyOrderData.getShad_mobile();
		if (tmallMobile != null && tmallMobile.equalsIgnoreCase(originalRecieverMobile) == false)
		{
			taslyOrderData.setShad_mobile(originalRecieverMobile);
		}

		final String eccOrderId = taslyOrderData.getEcc_order_id();
		if (eccOrderId != null)
		{// 说明ERP端有此订单消息，发送更新请求
			final Order order = converter.convert(taslyOrderData);
			if (order instanceof TaslyOrder)
			{
				final TaslyOrder taslyOrder = (TaslyOrder) order;
				LOG.info("向ERP发送修改地址消息");
				taslyOrderFacade.updateTaslyOrder(taslyOrder);
			}
		}
	}

	public void updateOrderLineDataByGiftFlag(final TaslyOrderData orderData, final String refundFee)
	{
		final List<OrderLineData> taslyOrderLineDataList = orderData.getOrderLines();

		if (CollectionUtils.isNotEmpty(taslyOrderLineDataList))
		{
			for (final OrderLineData orderLine : taslyOrderLineDataList)
			{
				if (orderLine instanceof TaslyOrderLineData)
				{
					final TaslyOrderLineData taslyOrderLine = (TaslyOrderLineData) orderLine;
					if (null != taslyOrderLine.getGift_item_flag()
							&& taslyOrderLine.getGift_item_flag().equalsIgnoreCase(OrderConstants.ORDER_LINE_GIFT_FLAG))
					{
						LOG.info("订单行项目号：" + taslyOrderLine.getOrderLineId() + "---是赠品,直接更新为退货退款状态");
						updateOrderData(orderData,
								new TaslyRefundStatusHandler(DEFAULT_REFUND_CREATE_HINT, "YES", taslyOrderLine.getOrderLineId()));
						updateOrderData(orderData, new TaslyRefundFeeHandler(refundFee, taslyOrderLine.getOrderLineId()));
					}
				}

			}
		}

	}


	@Override
	public void updateSellerMemo(final String tid, final String originalMemo, final String newMemo, final boolean success)
	{
		// YTODO Auto-generated method stub

	}
}
