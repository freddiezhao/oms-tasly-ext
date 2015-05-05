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

import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.services.OmsOrderAutoApprovalService;
import tasly.greathealth.oms.order.services.TaslyOrderService;


/**
 *
 */
public class OmsOrderAutoApprovalServiceImpl extends AbstractHybrisService implements OmsOrderAutoApprovalService
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();

	private TaslyOrderQueryFactory orderQueryFactory;
	private TaslyOrderService taslyOrderService;
	private int timeInterval;

	@Override
	@Transactional
	public void approveOrderStatus(final String approveStatus, final List<TaslyOrderData> tOrderData)
	{
		for (final TaslyOrderData taslyOrder : tOrderData)
		{
			boolean flag = false;
			labelA: for (final OrderLineData orderLine : taslyOrder.getOrderLines())
			{
				for (final OrderLineQuantityData orderLineQuantity : orderLine.getOrderLineQuantities())
				{
					flag = this.validateOrderLineQuantityExpress(orderLineQuantity, flag);
					if (!flag)
					{
						break labelA;
					}
				}
			}
			if (flag)
			{
				taslyOrderService.updateOrderApproveStatus(taslyOrder.getOrderId().toString(), approveStatus);
				LOG.info("approve order:" + taslyOrder.getId() + " success ");
			}
		}
	}

	public boolean validateOrderLineQuantityExpress(final OrderLineQuantityData orderLineQuantity, boolean flag)
	{
		if (orderLineQuantity instanceof TaslyOrderLineQuantityData)
		{
			final TaslyOrderLineQuantityData taslyOrderLineQuantityData = (TaslyOrderLineQuantityData) orderLineQuantity;
			if (null != taslyOrderLineQuantityData.getExpress_code() && !"".equals(taslyOrderLineQuantityData.getExpress_code()))
			{
				flag = true;
			}
			else
			{
				flag = false;
			}
		}
		return flag;
	}





	@Override
	@Transactional
	public List<TaslyOrderData> getOrdersByOrderQuerys(final String approvalStatus, final String shippingLockStatus)
	{
		// get the order list by approvalStatus and shippingLockStatus
		Date d = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDate = df.format(new Date(d.getTime() - timeInterval * 60 * 60 * 1000));
		List<TaslyOrderData> taslyOrderDataList = new ArrayList<TaslyOrderData>();
		try
		{
			taslyOrderDataList = this.findAll(orderQueryFactory.findTaslyOrdersByQuery(approvalStatus, shippingLockStatus,
					df.parse(startDate)));
		}
		catch (ParseException e)
		{
			LOG.error("日期转换异常" + e.getMessage(), e);
		}
		return taslyOrderDataList;
	}

	/**
	 * @param orderQueryFactory the orderQueryFactory to set
	 */
	public void setOrderQueryFactory(final TaslyOrderQueryFactory orderQueryFactory)
	{
		this.orderQueryFactory = orderQueryFactory;
	}

	/**
	 * @param taslyOrderService the taslyOrderService to set
	 */
	public void setTaslyOrderService(final TaslyOrderService taslyOrderService)
	{
		this.taslyOrderService = taslyOrderService;
	}

	/**
	 * @param timeInterval the timeInterval to set
	 */
	public void setTimeInterval(int timeInterval)
	{
		this.timeInterval = timeInterval;
	}

}
