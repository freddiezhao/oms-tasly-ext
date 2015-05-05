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
package tasly.greathealth.oms.order.services.impl;


import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.order.impl.DefaultOrderService;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.oms.api.financial.TaslyFinancialReportQueryObject;
import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLineQuantity;
import tasly.greathealth.oms.domain.order.ApproveStatus;
import tasly.greathealth.oms.domain.order.ExpressData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.conversion.TaslyOrderLinePopulator;
import tasly.greathealth.oms.order.services.TaslyOrderService;


/**
 *
 */
public class DefaultTaslyOrderService extends DefaultOrderService implements TaslyOrderService
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	public TaslyOrderLinePopulator taslyOrderLinePopulator;


	/**
	 * @return the taslyOrderLinePopulator
	 */
	public TaslyOrderLinePopulator getTaslyOrderLinePopulator()
	{
		return taslyOrderLinePopulator;
	}

	/**
	 * @param taslyOrderLinePopulator the taslyOrderLinePopulator to set
	 */
	@Required
	public void setTaslyOrderLinePopulator(final TaslyOrderLinePopulator taslyOrderLinePopulator)
	{
		this.taslyOrderLinePopulator = taslyOrderLinePopulator;
	}

	private TaslyOrderQueryFactory taslyOrderQueryFactory;

	/**
	 * @param taslyOrderQueryFactory the taslyOrderQueryFactory to set
	 */
	@Required
	public void setTaslyOrderQueryFactory(final TaslyOrderQueryFactory taslyOrderQueryFactory)
	{
		this.taslyOrderQueryFactory = taslyOrderQueryFactory;
	}

	@Override
	@Transactional
	public void updateTaslyOrder(final TaslyOrder taslyOrder)
	{
		final TaslyOrderData orderData = getTaslyOrderDataByOrderId(taslyOrder.getOrderId());
		updateTaslyOrderMemo(orderData, taslyOrder);
		updateTaslyOrderInvoice(orderData, taslyOrder);
		updateTaslyOrderAddress(orderData, taslyOrder);
		updateTaslyOrderLineQuantity(orderData, taslyOrder);
	}

	private void updateTaslyOrderMemo(final TaslyOrderData orderData, final TaslyOrder taslyOrder)
	{
		orderData.setShippingFirstName(taslyOrder.getShippingFirstName());
		orderData.setShippingLastName(taslyOrder.getShippingLastName());
		orderData.setSpecial_memo(taslyOrder.getSpecialMemo());
		orderData.setSpecial_memo_reason(taslyOrder.getSpecial_memo_reason());
		orderData.setSeller_message(taslyOrder.getSellerMessage());
		orderData.setShad_mobile(taslyOrder.getShadMobile());
		orderData.setShad_citydistrict(taslyOrder.getShadCitydistrict());
	}

	private void updateTaslyOrderInvoice(final TaslyOrderData orderData, final TaslyOrder taslyOrder)
	{
		orderData.setInvoice_name(taslyOrder.getInvoiceName());
		orderData.setInvoice_type(taslyOrder.getInvoiceType());
		orderData.setInvoice_content(taslyOrder.getInvoiceContent());
		orderData.setEcc_taxpayer_number(taslyOrder.getEccTaxpayerNumber());
		orderData.setEcc_bank_name(taslyOrder.getEccBankName());
		orderData.setEcc_bank_number(taslyOrder.getEccBankNumber());
		orderData.setEcc_customer_address(taslyOrder.getEccCustomerAddress());
		orderData.setEcc_customer_phone(taslyOrder.getEccCustomerPhone());
	}

	private void updateTaslyOrderAddress(final TaslyOrderData orderData, final TaslyOrder taslyOrder)
	{
		orderData.setFirstName(taslyOrder.getFirstName());
		orderData.setLastName(taslyOrder.getLastName());

		final Address address = taslyOrder.getShippingAddress();
		final AddressVT addressVT = new AddressVT(address.getAddressLine1(), address.getAddressLine2(), address.getCityName(),
				address.getCountrySubentity(), address.getPostalZone(), address.getLatitudeValue(), address.getLongitudeValue(),
				address.getCountryIso3166Alpha2Code(), address.getCountryName(), address.getName(), address.getPhoneNumber());

		orderData.setShippingAddress(addressVT);
	}

	private void updateTaslyOrderLineQuantity(final TaslyOrderData orderData, final TaslyOrder taslyOrder)
	{
		final List<OrderLine> taslyOrderLines = taslyOrder.getOrderLines();
		if (taslyOrderLines != null && taslyOrderLines.size() != 0)
		{
			final List<OrderLineQuantity> taslyOrderLineQuantities = taslyOrderLines.get(0).getOrderLineQuantities();
			if (taslyOrderLineQuantities != null && taslyOrderLineQuantities.size() != 0)
			{
				final TaslyOrderLineQuantity taslyOrderLineQuantity = (TaslyOrderLineQuantity) taslyOrderLineQuantities.get(0);

				final List<OrderLineData> orderLine = orderData.getOrderLines();
				for (final OrderLineData orderLineData : orderLine)
				{
					final List<OrderLineQuantityData> orderLineQuantity = orderLineData.getOrderLineQuantities();
					for (final OrderLineQuantityData quantityData : orderLineQuantity)
					{
						final TaslyOrderLineQuantityData lineQuantity = (TaslyOrderLineQuantityData) quantityData;
						lineQuantity.setExpress_code(taslyOrderLineQuantity.getExpressCode());
					}
				}
			}
			else
			{
				LOG.error("TaslyOrderLineQuantity is null, the order id is [%s].", taslyOrder.getOrderId());
			}
		}
		else
		{
			LOG.error("TaslyOrderLine is null, the order id is [%s].", taslyOrder.getOrderId());
		}
	}

	@Override
	@Transactional
	public void updateOrderLineRefundFlag(final String orderLineId, final String RefundFlag)
	{
		final TaslyOrderLineData told = (TaslyOrderLineData) super.getOrderLineById(orderLineId);
		if (!told.equals(null))
		{
			told.setRefundflag(RefundFlag);
		}
		else
		{
			LOG.error("orderLineId " + orderLineId + " not found!");
		}
	}

	@Override
	public void createTaslyOrderData()
	{
		super.getPersistenceManager().create(TaslyOrderData.class);
	}

	@Override
	public void updateErpStatus(final String orderId, final String type, final String status, final String eccOrderId)
	{
		final TaslyOrderData orderData = getTaslyOrderDataByOrderId(orderId);
		if (type.equals("02") || type.equals("04"))
		{
			eccToOmsOrderLock(orderData, type, status);
		}
		else if (type.equals("01") || type.equals("03"))
		{
			eccToOmsOrderStatus(orderData, type, status, eccOrderId);
		}
	}

	private void eccToOmsOrderLock(final TaslyOrderData orderData, final String type, final String status)
	{
		if (type.equals(TaslyERPConstants.UO_LOCKSTATUS))
		{
			if (status.equals(TaslyERPConstants.REPLICATIONSTATUS_S))
			{
				orderData.setShipping_lock_status(tasly.greathealth.oms.domain.order.ShippingLockStatus.LOCK_SUCCESS);
			}
			else if (status.equals(TaslyERPConstants.REPLICATIONSTATUS_E))
			{
				orderData.setShipping_lock_status(tasly.greathealth.oms.domain.order.ShippingLockStatus.LOCK_FAILED);
			}
		}
		else if (type.equals(TaslyERPConstants.UO_DEBLOCKSTATUS))
		{
			if (status.equals(TaslyERPConstants.REPLICATIONSTATUS_S))
			{
				orderData.setShipping_lock_status(tasly.greathealth.oms.domain.order.ShippingLockStatus.NON_LOCK);
			}
			else if (status.equals(TaslyERPConstants.REPLICATIONSTATUS_E))
			{
				orderData.setShipping_lock_status(tasly.greathealth.oms.domain.order.ShippingLockStatus.UNLOCK_FAILED);
			}
		}
	}

	private void eccToOmsOrderStatus(final TaslyOrderData orderData, final String type, final String status,
			final String eccOrderId)
	{
		if (type.equals("01"))
		{
			if (status.equals(TaslyERPConstants.REPLICATIONSTATUS_E))
			{
				final String orderStatus = orderData.getReplication_status();
				if (!orderStatus.equals(TaslyERPConstants.REPLICATIONSTATUS_S)
						&& !orderStatus.equals(TaslyERPConstants.REPLICATIONSTATUS_W))
				{
					orderData.setReplication_status(status);
				}
			}
			else
			{
				orderData.setReplication_status(status);
			}

			if (status.equals(TaslyERPConstants.REPLICATIONSTATUS_S) || status.equals(TaslyERPConstants.REPLICATIONSTATUS_W))
			{
				orderData.setEcc_order_id(eccOrderId);
			}
			else if (status.equals(TaslyERPConstants.REPLICATIONSTATUS_E))
			{
				// replicationTime初始值如果为null，则默认为0
				Integer replicationTime = orderData.getReplication_times();
				if (replicationTime == null)
				{
					replicationTime = 0;
				}
				orderData.setReplication_times(replicationTime + 1);
			}
		}
		else if (type.equals("03"))
		{
			orderData.setEcc_modification_status(status);
		}
	}

	@Override
	public void updateTaslyOrderLockStats(final String orderId, final String lockStatus)
	{
		LOG.info("TaslyOrderService updateTaslyOrderLockStatus:orderId :{} ,lockStatus :{}", orderId, lockStatus);
		final TaslyOrderData orderData = super.getPersistenceManager().getByIndex(TaslyOrderData.UX_ORDERS_ORDERID, orderId);

		final tasly.greathealth.oms.domain.order.ShippingLockStatus status = tasly.greathealth.oms.domain.order.ShippingLockStatus
				.valueOf(lockStatus);
		orderData.setShipping_lock_status(status);
	}

	@Override
	@Transactional
	public void updateOrderApproveStatus(final String orderId, final String approveMockStatus)
	{
		// System.out.println("####TaslyOrderServiceImpl" + 1);
		final TaslyOrderData orderData = (TaslyOrderData) super.getOrderByOrderId(orderId);
		// System.out.println("####TaslyOrderServiceImpl" + 1);
		orderData.setApprove_status(ApproveStatus.valueOf(approveMockStatus));
		// System.out.println("####TaslyOrderServiceImpl" + 1);
		super.getPersistenceManager().createOrUpdate(orderData);
	}

	@Override
	@Transactional
	public TaslyOrderData getTaslyOrderDataByOrderId(final String orderId)
	{
		try
		{
			return (TaslyOrderData) super.getOrderByOrderId(orderId);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Order Id is not correct, " + orderId, e);
		}
	}

	@Override
	@Transactional
	public void updateExpressCodeByOrderId(final String orderId, final String expressCode)
	{
		final OrderData orderData = super.getPersistenceManager().createCriteriaQuery(OrderData.class)
				.where(Restrictions.eq(OrderData.ORDERID, orderId)).uniqueResult();

		final List<TaslyOrderLineData> taslyOrderLineDataList = super.getPersistenceManager()
				.createCriteriaQuery(TaslyOrderLineData.class).where(Restrictions.eq(TaslyOrderLineData.MYORDER, orderData))
				.resultList();

		for (final TaslyOrderLineData taslyOrderLineData : taslyOrderLineDataList)
		{
			final List<TaslyOrderLineQuantityData> taslyOrderLineQuantityDataList = super.getPersistenceManager()
					.createCriteriaQuery(TaslyOrderLineQuantityData.class)
					.where(Restrictions.eq(TaslyOrderLineQuantityData.ORDERLINE, taslyOrderLineData)).resultList();

			for (final TaslyOrderLineQuantityData taslyOrderLineQuantityData : taslyOrderLineQuantityDataList)
			{
				taslyOrderLineQuantityData.setExpress_code(expressCode);
			}
		}
	}

	@Override
	@Transactional
	public String getExpressDataName(final String code)
	{
		LOG.debug("TaslyOrderService getExpressDataName:Express Code :{}", code);

		String expressName = "";
		if (code != null)
		{
			try
			{
				final ExpressData express = super.getPersistenceManager().getByIndex(ExpressData.UX_EXPRESS_CODE, code);
				if (express != null)
				{
					expressName = express.getName();
					if (expressName == null)
					{
						LOG.error("TaslyOrderService getExpressDataName:expressName is null");
					}
				}
			}
			catch (final ManagedObjectNotFoundException e)
			{
				LOG.error("TaslyOrderService getExpressDataName:expressName search is error {}", e.getMessage());
				return "";
			}
		}
		LOG.debug("TaslyOrderService getExpressDataName:getExpressDataName end and Express Name {}", expressName);
		return expressName;
	}

	@Override
	public Page<TaslyOrderData> getTaslyOrderDataByIssuedDate(final Date startDate, final Date endDate,
			final TaslyFinancialReportQueryObject queryObject)
			{
		try
		{
			final int[] pageNumberAndSize = getPageNumberAndSize(queryObject, 0, getQueryPageSizeDefault());
			return this.findPaged(taslyOrderQueryFactory.findTaslyOrdersByIssuedDate(startDate, endDate), pageNumberAndSize[0],
					pageNumberAndSize[1]);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("getTaslyOrderDataByIssuedDate is not correct, ", e);
		}
			}

	@Override
	public List<TaslyOrderLineData> getTaslyOrderLinesByOrderId(final TaslyOrderData order)
	{
		// YTODO Auto-generated method stub
		final List<TaslyOrderLineData> taslyOrderLineDataList = super.getPersistenceManager()
				.createCriteriaQuery(TaslyOrderLineData.class).where(Restrictions.eq(TaslyOrderLineData.MYORDER, order)).resultList();
		return taslyOrderLineDataList;
	}

	@Override
	@Transactional
	public TaslyOrderLine getTaslyOrderLineByOrderLineId(final String orderLineId)
	{
		final TaslyOrderLineData told = (TaslyOrderLineData) super.getOrderLineById(orderLineId);
		final TaslyOrderLine taslyOrderLine = new TaslyOrderLine();
		this.taslyOrderLinePopulator.populate(told, taslyOrderLine);
		return taslyOrderLine;
	}
}
