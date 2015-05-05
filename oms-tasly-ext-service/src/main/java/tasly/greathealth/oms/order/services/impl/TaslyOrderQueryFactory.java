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

import com.hybris.kernel.api.CriteriaExpression;
import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.Operator;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.impl.OrderQueryFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import tasly.greathealth.oms.api.order.TaslyOrderQueryObject;
import tasly.greathealth.oms.api.order.dto.TaslyOrderQuerySupport;
import tasly.greathealth.oms.domain.order.ApproveStatus;
import tasly.greathealth.oms.domain.order.ShippingLockStatus;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;


/**
 * Henter Liu
 */
public class TaslyOrderQueryFactory extends OrderQueryFactory
{
	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<String, String>();

	static
	{
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.DEFAULT.name(), "orderId");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.ORDER_ID.name(), "orderId");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.LAST_NAME.name(), "lastName");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.FIRST_NAME.name(), "firstName");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.ORDER_DATE.name(), "issueDate");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.SCHEDULED_SHIPPING_DATE.name(), "scheduledShippingDate");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.BUYER_MESSAGE.name(), "buyer_message");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.SELLER_MESSAGE.name(), "seller_message");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.PACKING.name(), "packing");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.ORIGINAL_ORDER_ID.name(), "original_order_id");
		QUERY_SUPPORT_MAPPING.put(TaslyOrderQuerySupport.APPROVE_STATUS.name(), "approve_status");
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public CriteriaQuery<OrderData> getOrdersByIdQuery(final String orderId)
	{
		return ((CriteriaQuery) query(TaslyOrderData.class).where(Restrictions.eq(TaslyOrderData.ORDERID, orderId)));
	}

	public CriteriaQuery<TaslyOrderLineQuantityData> getOrderLineQuantityByOlqIds(final List<Long> olqIds)
	{
		return query(TaslyOrderLineQuantityData.class).where(
				Restrictions.in(OrderLineQuantityData.OLQID, olqIds.toArray(new Long[olqIds.size()])));
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public CriteriaQuery<TaslyOrderData> findTaslyOrdersByQuery(final String approvalStatus, final String shippingLockStatus,
			final Date startDate)
			{
		final CriteriaQuery criteriaQuery = query(TaslyOrderData.class);
		final List restrictions = new ArrayList();
		restrictions.add(Restrictions.eq(TaslyOrderData.APPROVE_STATUS, ApproveStatus.valueOf(approvalStatus)));
		restrictions.add(Restrictions.eq(TaslyOrderData.SHIPPING_LOCK_STATUS, ShippingLockStatus.valueOf(shippingLockStatus)));
		restrictions.add(Restrictions.isNull(TaslyOrderData.BUYER_MESSAGE));
		restrictions.add(Restrictions.isNull(TaslyOrderData.SELLER_MESSAGE));
		restrictions.add(Restrictions.lt(TaslyOrderData.MODIFIEDTIME, startDate));

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		return criteriaQuery;
			}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public CriteriaQuery<TaslyOrderData> findTaslyOrdersByIssuedDate(final Date startDate, final Date endDate)
	{
		final CriteriaQuery criteriaQuery = query(TaslyOrderData.class);
		final List restrictions = new ArrayList();

		if (startDate != null)
		{
			restrictions.add(Restrictions.gt(TaslyOrderData.ISSUEDATE, startDate));
		}

		if (endDate != null)
		{
			restrictions.add(Restrictions.lt(TaslyOrderData.ISSUEDATE, endDate));
		}

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		return criteriaQuery;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public CriteriaQuery<OrderData> findOrdersByQuery(final OrderQueryObject query)
	{
		final CriteriaQuery<TaslyOrderData> criteriaQuery = query(TaslyOrderData.class);
		final List<Restriction> restrictions = new ArrayList<Restriction>();
		final List<CriteriaExpression> coupandExpression = new ArrayList<CriteriaExpression>();
		filteringOnNames(query.getUserName(), query.getFirstName(), query.getLastName(), restrictions);

		final boolean hasLocationIds = CollectionUtils.isNotEmpty(query.getLocationIds());
		final boolean hasShipmentStatusIds = CollectionUtils.isNotEmpty(query.getShipmentStatusIds());
		boolean hasApproveStatusIds = false;
		boolean hasRemarkStatusIds = false;
		boolean hasLockStatusIds = false;
		boolean hasInnerSourceStatusIds = false;
		if (query instanceof TaslyOrderQueryObject)
		{
			hasApproveStatusIds = CollectionUtils.isNotEmpty(((TaslyOrderQueryObject) query).getApproveStatusIds());
			if (hasApproveStatusIds)
			{
				restrictions.add(RawRestrictions.in(TaslyOrderData.APPROVE_STATUS.name(), ((TaslyOrderQueryObject) query)
						.getApproveStatusIds().toArray()));
			}

			final String skuId = ((TaslyOrderQueryObject) query).getSkuId();
			if (skuId != null)
			{
				criteriaQuery.join(TaslyOrderLineData.class, "tol");
				restrictions.add(RawRestrictions.attrEq(OrderData.ID.name(), "tol." + TaslyOrderLineData.MYORDER.name()));
				restrictions.add(RawRestrictions.eq("tol." + TaslyOrderLineData.SKUID.name(), skuId));
			}


			hasRemarkStatusIds = CollectionUtils.isNotEmpty(((TaslyOrderQueryObject) query).getRemarkStatusIds());

			if (hasRemarkStatusIds)
			{
				final List<String> remarkStatusIds = ((TaslyOrderQueryObject) query).getRemarkStatusIds();

				if (remarkStatusIds.size() == 2)
				{
					final Restriction buyerMessageIsNotNull = RawRestrictions.isNotNull(TaslyOrderData.BUYER_MESSAGE.name());
					final Restriction sellerMessageIsNotNull = RawRestrictions.isNotNull(TaslyOrderData.SELLER_MESSAGE.name());
					final CriteriaExpression expression = RawRestrictions.createCompoundExpression(buyerMessageIsNotNull,
							sellerMessageIsNotNull, Operator.OR);
					coupandExpression.add(expression);
				}
				else if (remarkStatusIds.size() == 1)
				{
					final String remarkStatus = ((TaslyOrderQueryObject) query).getRemarkStatusIds().get(0);
					Restriction restriction = null;
					if (remarkStatus.toLowerCase().contains("buyermessage"))
					{
						restriction = RawRestrictions.isNotNull(TaslyOrderData.BUYER_MESSAGE.name());
					}
					else if (remarkStatus.toLowerCase().contains("sellermessage"))
					{
						restriction = RawRestrictions.isNotNull(TaslyOrderData.SELLER_MESSAGE.name());
					}
					restrictions.add(restriction);
				}
			}

			hasLockStatusIds = CollectionUtils.isNotEmpty(((TaslyOrderQueryObject) query).getLockStatusIds());
			if (hasLockStatusIds)
			{
				restrictions.add(RawRestrictions.in(TaslyOrderData.SHIPPING_LOCK_STATUS.name(), ((TaslyOrderQueryObject) query)
						.getLockStatusIds().toArray()));
			}

			hasInnerSourceStatusIds = CollectionUtils.isNotEmpty(((TaslyOrderQueryObject) query).getInnerSourceStatusIds());
			if (hasInnerSourceStatusIds)
			{
				restrictions.add(RawRestrictions.in(TaslyOrderData.INNER_SOURCE.name(), ((TaslyOrderQueryObject) query)
						.getInnerSourceStatusIds().toArray()));
			}

			final boolean hasChannels = CollectionUtils.isNotEmpty(((TaslyOrderQueryObject) query).getChannels());
			if (hasChannels)
			{
				restrictions.add(RawRestrictions.in(TaslyOrderData.CHANNEL_SOURCE.name(), ((TaslyOrderQueryObject) query)
						.getChannels().toArray()));
			}
			else
			{
				restrictions.add(RawRestrictions.isNull(TaslyOrderData.CHANNEL_SOURCE.name()));
			}
			final boolean hasInnerSources = CollectionUtils.isNotEmpty(((TaslyOrderQueryObject) query).getInnerSources());
			if (hasInnerSources)
			{
				restrictions.add(RawRestrictions.in(TaslyOrderData.INNER_SOURCE.name(), ((TaslyOrderQueryObject) query)
						.getInnerSources().toArray()));
			}
			else
			{
				restrictions.add(RawRestrictions.isNull(TaslyOrderData.INNER_SOURCE.name()));
			}

		}

		if ((hasLocationIds) || (hasShipmentStatusIds))
		{
			criteriaQuery.join(ShipmentData.class, "s");
			restrictions.add(RawRestrictions.attrEq(OrderData.ID.name(), "s." + ShipmentData.ORDERFK.name()));
			if (hasLocationIds)
			{
				restrictions
						.add(RawRestrictions.in("s." + ShipmentData.STOCKROOMLOCATIONID.name(), query.getLocationIds().toArray()));
			}

			if (hasShipmentStatusIds)
			{
				restrictions.add(RawRestrictions.in("s." + ShipmentData.OLQSSTATUS.name(), query.getShipmentStatusIds().toArray()));
			}
		}

		filteringOnDates(query.getStartDate(), query.getEndDate(), restrictions);
		filteringOnScheduledShippingDates(query.getStartScheduledDate(), query.getEndScheduledDate(), restrictions);
		filteringOnTaslyAttributes((TaslyOrderQueryObject) query, restrictions);
		applyingCollectedRestrictionsToCriteriaQuery1(criteriaQuery, restrictions, coupandExpression);
		if (query instanceof TaslyOrderQueryObject)
		{
			criteriaQuery.order(QUERY_SUPPORT_MAPPING.get(((TaslyOrderQueryObject) query).getSorting().getAttribute()),
					SortDirection.ASC.equals(query.getSorting().getDirection()));
		}

		return (CriteriaQuery) criteriaQuery;
	}

	/**
	 * @param query
	 * @param restrictions
	 */
	private void filteringOnTaslyAttributes(final TaslyOrderQueryObject query, final List<Restriction> restrictions)
	{
		final String orderId = query.getOrderId();
		if (StringUtils.isNotEmpty(orderId))
		{
			restrictions.add(RawRestrictions.eq(TaslyOrderData.ORDERID.name(), orderId));
		}
		final String originalOrderId = query.getOriginalOrderId();
		if (StringUtils.isNotEmpty(originalOrderId))
		{
			restrictions.add(RawRestrictions.eq(TaslyOrderData.ORIGINAL_ORDER_ID.name(), originalOrderId));
		}
		final String approveStatus = query.getApproveStatus();
		if (StringUtils.isNotEmpty(approveStatus))
		{
			restrictions.add(RawRestrictions.eq(TaslyOrderData.APPROVE_STATUS.name(), approveStatus));
		}
		final String buyerMessage = query.getBuyerMessage();
		if (StringUtils.isNotEmpty(buyerMessage))
		{
			restrictions.add(RawRestrictions.contains(TaslyOrderData.BUYER_MESSAGE.name(), buyerMessage));
		}
	}

	protected CriteriaQuery<TaslyOrderData> applyingCollectedRestrictionsToCriteriaQuery1(
			final CriteriaQuery<TaslyOrderData> origQuery, final List<Restriction> restrictions,
			final List<CriteriaExpression> coumpandExpressions)
			{
		CriteriaQuery<TaslyOrderData> criteriaQuery = origQuery.where(RawRestrictions.isNotNull(TaslyOrderData.ID.name()));
		if (!(restrictions.isEmpty()) || !(coumpandExpressions.isEmpty()))
		{
			if (!(restrictions.isEmpty()))
			{
				for (final Restriction restriction : restrictions)
				{
					criteriaQuery = criteriaQuery.and(restriction);
				}
			}

			if (!(coumpandExpressions.isEmpty()))
			{
				for (final CriteriaExpression coumpandExpression : coumpandExpressions)
				{
					criteriaQuery = criteriaQuery.and(coumpandExpression);
				}
			}
		}

		return criteriaQuery;
			}
}
