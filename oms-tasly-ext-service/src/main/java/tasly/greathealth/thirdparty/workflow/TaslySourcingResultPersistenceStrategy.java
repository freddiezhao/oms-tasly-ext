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
package tasly.greathealth.thirdparty.workflow;

import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.SourcingResult;
import com.hybris.oms.service.sourcing.strategy.impl.DefaultSourcingResultPersistenceStrategy;
import com.hybris.oms.service.workflow.WorkflowConstants;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.tmall.order.services.ExpressService;

import com.google.common.base.Preconditions;


/**
 *
 */
public class TaslySourcingResultPersistenceStrategy extends DefaultSourcingResultPersistenceStrategy implements OrderConstants
{

	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	private ExpressService expressService;

	@Override
	public void persistSourcingResult(final OrderData order, final SourcingResult result)
	{
		TaslyOrderData taslyOrderData = null;
		if (order instanceof TaslyOrderData)
		{
			taslyOrderData = (TaslyOrderData) order;
			// final String innerSource = String.valueOf(taslyOrderData.getInner_source());
			// final String channelSource = String.valueOf(taslyOrderData.getChannel_source());
			// final String receiverState = taslyOrderData.getShippingAddress().getCountrySubentity();
			//
			// if (result instanceof DefaultSourcingResult)
			// {
			// final DefaultSourcingResult defaultSourcingResult = (DefaultSourcingResult) result;
			// final Map<String, Object> oldProperties = defaultSourcingResult.getProperties();
			// final Map<String, Object> newProperties = new HashMap<>();
			// newProperties.putAll(oldProperties);
			// newProperties.put(DEFAULT_INNER_SOURCE_KEY, innerSource);
			// newProperties.put(DEFAULT_CHANNEL_SOURCE_KEY, channelSource);
			// newProperties.put(DEFAULT_RECIEVE_STATE_KEY, receiverState);
			// defaultSourcingResult.setProperties(newProperties);
			// }
		}

		Preconditions.checkArgument(order != null, "order cannot be null");
		Preconditions.checkArgument(result != null, "sourcing result cannot be null");
		if (!result.isEmpty())
		{
			final OrderLineQuantityStatusData olqStatus = this.getOrderService().getOrderLineQuantityStatusByTenantPreferenceKey(
					TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SOURCED);

			final List<OrderLineData> orderLines = order.getOrderLines();
			final Set<String> orderLineIds = new HashSet<>(orderLines.size());
			for (final OrderLineData orderLine : orderLines)
			{
				orderLineIds.add(orderLine.getOrderLineId());
				this.processOLQs(result, olqStatus, orderLine);
			}
			validateOrderLineIds(result, order.getOrderId(), orderLineIds);
			expressService.modifyLogisticsCompany(taslyOrderData);
			order.setState(WorkflowConstants.STATE_SOURCING_PREFIX + result.getStatus());
			this.getPersistenceManager().flush();
			this.getPersistenceManager().refresh(order);
		}
	}




	/**
	 * @param expressService the expressService to set
	 */
	public void setExpressService(final ExpressService expressService)
	{
		this.expressService = expressService;
	}




	@Override
	protected void processOLQs(final SourcingResult result, final OrderLineQuantityStatusData olqStatus,
			final OrderLineData orderLine)
	{
		LOG.info("####Invoke Tasly processOLQs####");
		// final List<SourcingOLQ> newQLQlist = new ArrayList<>();
		// for (final SourcingOLQ olq : result.getSourcingOlqs())
		// {
		// final Map<String, Object> olqProperties = olq.getProperties();
		// final Map<String, Object> newOlqProperties = new HashMap<>();
		// newOlqProperties.putAll(result.getProperties());
		// newOlqProperties.putAll(olqProperties);
		// final SourcingOLQ newOlq = new SourcingOLQ(olq.getSku(), olq.getQuantity(), olq.getLocationId(),
		// olq.getLineId(),
		// olq.getClass(), newOlqProperties);
		//
		// newQLQlist.add(newOlq);
		// }
		// if (result instanceof DefaultSourcingResult)
		// {
		// final DefaultSourcingResult defaultSourcingResult = (DefaultSourcingResult) result;
		// defaultSourcingResult.setSourcingOlqs(newQLQlist);
		// }
		super.processOLQs(result, olqStatus, orderLine);
	}

	@Override
	protected void createOLQ(final OrderLineQuantityStatusData olqStatus, final OrderLineData orderLine, final SourcingOLQ olq)
	{
		LOG.info("####Invoke Tasly Sourcing Result Persistence Strategy####");
		// LOG.info("####omsQueryImpl####" + omsQueryImpl);
		// final String sku = orderLine.getSkuId();
		// final String channel = (String) olq.getProperties().get(DEFAULT_CHANNEL_SOURCE_KEY);
		// final String innerSource = (String) olq.getProperties().get(DEFAULT_INNER_SOURCE_KEY);
		// final String receiverState = (String) olq.getProperties().get(DEFAULT_RECIEVE_STATE_KEY);

		// String express = omsQueryImpl.getExpressBySku(sku, channel, innerSource);
		// LOG.info("express:::" + express);
		// if (express == null)
		// {
		// express = omsQueryImpl.getExpressByLocation(receiverState);
		// }

		final TaslyOrderLineQuantityData olqData = super.getPersistenceManager().create(TaslyOrderLineQuantityData.class);
		olqData.setStockroomLocationId(olq.getLocationId());
		olqData.setOrderLine(orderLine);
		olqData.setQuantityValue(olq.getQuantity());
		olqData.setStatus(olqStatus);
		// olqData.setExpress_code(express);
		if (MapUtils.isNotEmpty(olq.getProperties()))
		{
			for (final Map.Entry<String, Object> entry : olq.getProperties().entrySet())
			{
				olqData.setProperty(entry.getKey(), entry.getValue());
			}
		}
		orderLine.getOrderLineQuantities().add(olqData);
	}
}
