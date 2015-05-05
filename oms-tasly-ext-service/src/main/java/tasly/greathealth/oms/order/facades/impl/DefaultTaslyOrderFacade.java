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
package tasly.greathealth.oms.order.facades.impl;

import com.hybris.commons.conversion.Converter;
import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.erp.api.order.UpdateOrderFacade;
import tasly.greathealth.erp.api.order.updateorder.dto.EccBillto;
import tasly.greathealth.erp.api.order.updateorder.dto.EccDelivery;
import tasly.greathealth.erp.api.order.updateorder.dto.Item;
import tasly.greathealth.erp.api.order.updateorder.dto.Message;
import tasly.greathealth.erp.api.order.updateorder.dto.OmsOrder;
import tasly.greathealth.erp.api.order.updateorder.dto.OmsOrders;
import tasly.greathealth.erp.api.order.updateorder.dto.Refund;
import tasly.greathealth.erp.api.order.updateorder.dto.Shipto;
import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.oms.api.order.TaslyOrderFacade;
import tasly.greathealth.oms.api.order.dto.ApproveStatus;
import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLineQuantity;
import tasly.greathealth.oms.domain.order.ShippingLockStatus;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.services.TaslyOrderService;
import tasly.greathealth.thirdparty.order.OrderConstants;


// import com.hybris.oms.service.order.OrderService;


/**
 *
 */
public class DefaultTaslyOrderFacade implements TaslyOrderFacade, TaslyERPConstants
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	private Converter<TaslyOrder, TaslyOrderData> taslyOrderReverseConverter;
	private Converter<TaslyOrderData, TaslyOrder> taslyOrderConverter;
	private UpdateOrderFacade updateOrderFacade;
	private TaslyOrderService taslyOrderService;

	@Override
	public void create(final TaslyOrder taslyOrder)
	{
		// final TaslyOrderData orderData = populatingConverter.convert(taslyOrder);
	}

	@Override
	@Transactional
	public void updateTaslyOrder(final TaslyOrder taslyOrder)
	{
		taslyOrderService.updateTaslyOrder(taslyOrder);
		sendUpdateEccOrder(taslyOrder);
	}

	private void sendUpdateEccOrder(final TaslyOrder taslyOrder)
	{
		final Message message = new Message();
		final OmsOrders omsOrders = new OmsOrders();
		final List<OmsOrder> omsOrder = new ArrayList<OmsOrder>();
		final OmsOrder order = new OmsOrder();
		order.setOperation(UO_ALTERTATUS);
		order.setEccOrderId(taslyOrder.getEccOrderId());
		order.setOmsOrderId(taslyOrder.getOrderId());
		order.setUserNotes(taslyOrder.getBuyerMessage());
		order.setCsNotes(taslyOrder.getSellerMessage());
		order.setEccBillto(getEccOrderInvoice(taslyOrder));
		order.setEccDelivery(getEccDelivery(taslyOrder));
		order.setShipto(getEccShipto(taslyOrder));
		omsOrder.add(order);
		omsOrders.setOmsOrder(omsOrder);
		message.setOmsOrders(omsOrders);

		final int result = updateOrderFacade.updateOrders(taslyOrder.getOrderId(), message);
		LOG.debug("update ecc for the order [%s], the result is " + result, taslyOrder.getOrderId());
	}

	private EccDelivery getEccDelivery(final TaslyOrder taslyOrder)
	{
		final EccDelivery eccDelivery = new EccDelivery();
		final TaslyOrderLineQuantity taslyOrderLineQuantity = (TaslyOrderLineQuantity) taslyOrder.getOrderLines().get(0)
				.getOrderLineQuantities().get(0);
		eccDelivery.setEccExpressId(taslyOrderLineQuantity.getExpressCode());
		eccDelivery.setEccExpressName(taslyOrderLineQuantity.getExpressName());
		return eccDelivery;
	}

	private Shipto getEccShipto(final TaslyOrder taslyOrder)
	{
		final TaslyOrderData taslyOrderData = taslyOrderService.getTaslyOrderDataByOrderId(taslyOrder.getOrderId());
		final Shipto shipto = new Shipto();
		shipto.setEccShiptoId(taslyOrderData.getChannel_source().toString()); // modified by Amen 2015.01.20
		shipto.setEccShiptoName(taslyOrder.getShippingFirstName());
		shipto.setEccShiptoRegion(taslyOrder.getShippingAddress().getCountrySubentity());
		shipto.setEccShiptoCity(taslyOrder.getShippingAddress().getCityName());
		shipto.setEccShiptoDistrict(taslyOrder.getShadCitydistrict());
		shipto.setEccShiptoAddress(taslyOrder.getShippingAddress().getAddressLine1());
		shipto.setEccShiptoZipcode(taslyOrder.getShippingAddress().getPostalZone());
		shipto.setEccShiptoMob(taslyOrder.getShadMobile());
		shipto.setEccShiptoTel(taslyOrder.getShadMobile());
		return shipto;
	}

	private EccBillto getEccOrderInvoice(final TaslyOrder taslyOrder)
	{
		final EccBillto eccBillto = new EccBillto();
		eccBillto.setEccInvoiceType(taslyOrder.getInvoiceType());
		eccBillto.setEccInvoiceTitle(taslyOrder.getInvoiceName());
		eccBillto.setEccInvoiceContent(taslyOrder.getInvoiceContent());
		eccBillto.setEccTaxpayerNumber(taslyOrder.getEccTaxpayerNumber());
		eccBillto.setEccBankName(taslyOrder.getEccBankName());
		eccBillto.setEccBankNumber(taslyOrder.getEccBankNumber());
		eccBillto.setEccCustomerAddress(taslyOrder.getEccCustomerAddress());
		eccBillto.setEccCustomerPhone(taslyOrder.getEccCustomerPhone());
		return eccBillto;
	}

	/**
	 * @param taslyOrderService the taslyOrderService to set
	 */
	public void setTaslyOrderService(final TaslyOrderService taslyOrderService)
	{
		this.taslyOrderService = taslyOrderService;
	}

	/**
	 * @return the taslyOrderReverseConverter
	 */
	protected Converter<TaslyOrder, TaslyOrderData> getTaslyOrderReverseConverter()
	{
		return taslyOrderReverseConverter;
	}

	/**
	 * @param taslyOrderReverseConverter the taslyOrderReverseConverter to set
	 */
	@Required
	public void setTaslyOrderReverseConverter(final Converter<TaslyOrder, TaslyOrderData> taslyOrderReverseConverter)
	{
		this.taslyOrderReverseConverter = taslyOrderReverseConverter;
	}

	/**
	 * @param updateOrderFacade the updateOrderFacade to set
	 */
	@Required
	public void setUpdateOrderFacade(final UpdateOrderFacade updateOrderFacade)
	{
		this.updateOrderFacade = updateOrderFacade;
	}

	/**
	 * @return the taslyOrderConverter
	 */
	protected Converter<TaslyOrderData, TaslyOrder> getTaslyOrderConverter()
	{
		return taslyOrderConverter;
	}

	@Override
	@Transactional
	public String updateTaslyOrderLockStatus(final String orderId, final String lockStatus)
	{
		LOG.info("TaslyOrderFacade updateTaslyOrderLockStatus:orderId :{} ,lockStatus :{}", orderId, lockStatus);
		taslyOrderService.updateTaslyOrderLockStats(orderId, lockStatus);
		final TaslyOrderData orderData = this.taslyOrderService.getTaslyOrderDataByOrderId(orderId);
		LOG.info("TaslyOrderFacade updateTaslyOrderLockStatus:orderData lock:{}", orderData.getShipping_lock_status());
		final String eccStatus = orderData.getReplication_status();
		if (eccStatus != null)
		{
			if (REPLICATIONSTATUS_N.equals(eccStatus) || REPLICATIONSTATUS_E.equals(eccStatus))
			{
				if (lockStatus.equals(ShippingLockStatus.PENDING_LOCK.toString()))
				{
					orderData.setShipping_lock_status(ShippingLockStatus.LOCK_SUCCESS);
				}
				else if (lockStatus.equals(ShippingLockStatus.PENDING_UNLOCK.toString()))
				{
					orderData.setShipping_lock_status(ShippingLockStatus.NON_LOCK);
				}
			}
			else if (REPLICATIONSTATUS_S.equals(eccStatus) || REPLICATIONSTATUS_W.equals(eccStatus))
			{
				sendLockStatusToECC(orderData);
			}
		}
		else
		{
			LOG.error("ECC Replication Status is nvl");
		}
		return orderData.getShipping_lock_status().toString();
	}

	private void sendLockStatusToECC(final TaslyOrderData order)
	{
		LOG.info("sendLockStatusToECC start and OmsOrderId :{},eccOrderId :{},lockStatus :{}", order.getOrderId(),
				order.getEcc_order_id(), order.getShipping_lock_status());

		final List<Item> itemList = new ArrayList<Item>();
		final Refund refund = new Refund();
		final OmsOrder omsOrder = new OmsOrder();
		final List<TaslyOrderLineData> orderLineDataList = taslyOrderService.getTaslyOrderLinesByOrderId(order);

		if (null != orderLineDataList && !orderLineDataList.isEmpty())
		{
			final Iterator<TaslyOrderLineData> iter = orderLineDataList.iterator();
			while (iter.hasNext())
			{
				final Item item = new Item();
				final TaslyOrderLineData orderLineData = iter.next();
				if (OrderConstants.REFUND_FLAG_AGREE.equalsIgnoreCase(orderLineData.getRefundflag()))
				{
					item.setRefundAmount(orderLineData.getRefundamount() == null ? 0 : orderLineData.getRefundamount());
					item.setRefundOmsItemId(orderLineData.getOrderLineId());
					// 天猫行退款只支持整行项目数量
					item.setRefundQuantity(String.valueOf(orderLineData.getQuantityValue()));
					// 暂无退款类型
					item.setRefundType("");
					itemList.add(item);
				}

			}
			refund.setItem(itemList);
		}

		omsOrder.setRefund(refund);
		omsOrder.setEccOrderId(order.getEcc_order_id());
		omsOrder.setOmsOrderId(order.getOrderId());
		if (order.getShipping_lock_status().equals(tasly.greathealth.oms.domain.order.ShippingLockStatus.PENDING_LOCK))
		{
			omsOrder.setOperation(UO_LOCKSTATUS);
		}
		else if (order.getShipping_lock_status().equals(tasly.greathealth.oms.domain.order.ShippingLockStatus.PENDING_UNLOCK))
		{
			omsOrder.setOperation(UO_DEBLOCKSTATUS);
		}

		final List<OmsOrder> orderList = new LinkedList<OmsOrder>();
		orderList.add(omsOrder);

		final OmsOrders omsOrders = new OmsOrders();
		omsOrders.setOmsOrder(orderList);

		final Message message = new Message();
		message.setOmsOrders(omsOrders);

		LOG.info("EccMessage:" + message.toString());

		final int status = updateOrderFacade.updateOrders(order.getOrderId(), message);
		LOG.info("updateOrderFacade status:" + status);
		if (status != 0)
		{
			LOG.error("更新ECC锁定状态失败");
		}
		else
		{
			LOG.info("更新ECC锁定状态成功");
		}
	}

	@Override
	@Transactional
	public void approveOrder(final String orderId)
	{
		// System.out.println("####DefaultTaslyOrderFacadeImpl" + 1);
		// boolean status = true;
		LOG.info("input orderID is : " + orderId);
		try
		{
			if (orderId != null)
			{
				// System.out.println("####DefaultTaslyOrderFacadeImpl" + 2);
				taslyOrderService.updateOrderApproveStatus(orderId, ApproveStatus.APPROVED.toString());
				// System.out.println("####DefaultTaslyOrderFacadeImpl" + 3);
			}
		}
		catch (final EntityNotFoundException entityException)
		{
			LOG.error("EntityNotFoundException: " + entityException.getLocalizedMessage());
			// status = false;
		}
		// return status;
	}

	/**
	 * @param taslyOrderConverter the taslyOrderConverter to set
	 */
	@Required
	public void setTaslyOrderConverter(final Converter<TaslyOrderData, TaslyOrder> taslyOrderConverter)
	{
		this.taslyOrderConverter = taslyOrderConverter;
	}

	@Override
	@Transactional
	public TaslyOrder getTaslyOrderByOrderId(final String orderId)
	{
		final TaslyOrderData taslyOrderDara = taslyOrderService.getTaslyOrderDataByOrderId(orderId);
		final TaslyOrder taslyOrder = taslyOrderConverter.convert(taslyOrderDara);
		return taslyOrder;
	}

	@Override
	@Transactional
	public void mockOrder(final String orderId)
	{
		// Boolean status = true;
		LOG.info("input orderID is : " + orderId);
		try
		{
			if (orderId != null)
			{
				taslyOrderService.updateOrderApproveStatus(orderId, ApproveStatus.PROCESSING.toString());
			}
		}
		catch (final EntityNotFoundException entityException)
		{
			LOG.error("EntityNotFoundException: " + entityException.getLocalizedMessage());
			// status = false;
		}
		// return status;
	}


	@Override
	public void updateExpressCodeByOrderId(final String orderId, final String expressCode)
	{
		LOG.trace("updateExpressCodeByOrderLine");
		this.taslyOrderService.updateExpressCodeByOrderId(orderId, expressCode);
	}

	// @Override
	// public TaslyOrderLineQuantity getOrderLineQuantityByOlqId(final Long olqId)
	// {
	// final TaslyOrderLineQuantity taslyOlq = orderservice.getOrderLineQuantityByOlqId(olqId);
	// }

	@Override
	@Transactional
	public void updateTaslyECCOrder(final String orderId, final String operationType, final String status, final String eccOrderId)
	{
		this.taslyOrderService.updateErpStatus(orderId, operationType, status, eccOrderId);
	}

	@SuppressWarnings("finally")
	@Override
	@Transactional
	public String updateOrderLineRefundFlag(final String orderLineId, final String refundFlag)
	{
		String str = null;
		try
		{
			this.taslyOrderService.updateOrderLineRefundFlag(orderLineId, refundFlag);
			str = "OK";
		}
		catch (final Exception e)
		{
			str = e.toString();
		}
		finally
		{
			return str;
		}

	}

	@Override
	public TaslyOrderLine getTaslyOrderLineByOrderLineId(final String orderLineId)
	{
		return this.taslyOrderService.getTaslyOrderLineByOrderLineId(orderLineId);
	}

	// @Override
	// @Transactional
	// public List<TaslyOrderLineData> getTaslyOrderLinesByOrderId(final TaslyOrderData orderData)
	// {
	// return this.taslyOrderService.getTaslyOrderLinesByOrderId(orderData);
	// }
	//
	// @Override
	// @Transactional
	// public TaslyOrderData getTaslyOrderDataByOrderId(final String orderId)
	// {
	// return this.taslyOrderService.getTaslyOrderDataByOrderId(orderId);
	// }
}
