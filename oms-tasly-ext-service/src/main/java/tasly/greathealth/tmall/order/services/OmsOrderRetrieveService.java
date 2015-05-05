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
package tasly.greathealth.tmall.order.services;

import com.hybris.oms.domain.order.Order;
import com.hybris.oms.service.managedobjects.order.OrderLineData;

import java.util.Date;
import java.util.List;

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.EventType;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.OrderState;
import tasly.greathealth.oms.domain.order.PendingOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.tmall.order.domain.OmsSaveOrderParameter;
import tasly.greathealth.tmall.order.services.impl.OrderContext;


/**
 * Retrieve order information from Tmall
 *
 * @author Jason Bao
 */
public interface OmsOrderRetrieveService<T>
{
	T retrieveOrderDetail(String orderID) throws Exception;

	Order transformThirdPartyOrder2OmsOrder(T thirdPartyOrder, OrderContext orderContext) throws Exception;

	void saveOrder(Order order);

	void updateSellerMemo(Long tid, String originalMemo, String newMemo, boolean success);

	// JD 订单id为字符串
	void updateSellerMemo(String tid, String originalMemo, String newMemo, boolean success);

	void updateSellerMemo(String tid, String memo);

	void filterOutUnpayedLine(T trade);

	void saveFailedOrder(String tid, String oid, String refundFee, EventType eventType, String errorMsg, OrderState state,
			String rawMsg, Date eventTime, ChannelSource channelSource, InnerSource innerSource);

	void batchSaveFailOrPendingOrder(List<OmsSaveOrderParameter> saveOrderParameters) throws Exception;

	T splitOrder(T trade, OrderContext orderContext) throws Exception;

	void createRefund(String orderId, String lineId, String refundFee, boolean sendGoods);

	void successCompleteRefund(String orderId, String lineId, String refundFee, boolean sendGoods);

	void closeRefund(String orderId, String lineId, boolean sendGoods);

	void createSellerShipment(String tid, String oid);

	void cancelShipment(String orderId);

	void deleteTaslyOrder(String orderId);

	void loadPendingOrders() throws Exception;

	void updateLogisiticsAddress(String tid) throws Exception;

	/**
	 * get orderLineDetail
	 *
	 * @param orderLineId
	 * @return
	 */
	public TaslyOrderLineData getTaslyOrerLineDetail(String orderLineId);

	/**
	 * get orderDetail
	 *
	 * @param omsorderId
	 * @return
	 */
	public TaslyOrderData getTaslyOrderDataDetail(String omsorderId);


	/**
	 * get orderDetail
	 *
	 * @param tmallorderId
	 * @return
	 */
	public TaslyOrderData getTaslyOrderDataDetailByTmallId(String tmallorderId);

	/**
	 * get orderLine quantity detail
	 *
	 * @return
	 */
	public TaslyOrderLineQuantityData getTaslyOrderLineQuntity(OrderLineData orderLine);


	/**
	 * @param writeBack2Tamll the writeBack2Tamll to set
	 */
	public void setWriteBack2Tamll(final boolean writeBack2Tamll);


	/**
	 * @return the writeBack2Tamll
	 */
	public boolean isWriteBack2Tamll();

	/**
	 * fetch pendingOrderData list according to tid
	 *
	 * @param tid 天猫订单号
	 * @return List<PendingOrderData>
	 */

	public List<PendingOrderData> loadPendingOrdersByTID(String tid);

	/**
	 * fetch pendingOrderData list according to tid
	 *
	 * @param tid 天猫订单号 oid 天猫行项目 EventType eventType
	 */

	public void saveSuccessPendingOrder(final String tid, final String oid, final String eventType);


}
