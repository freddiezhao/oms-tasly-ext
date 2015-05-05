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
package tasly.greathealth.oms.shipment.services.impl;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.shipment.impl.ShipmentQueryFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.ui.api.shipment.TaslyShipmentQueryObject;
import tasly.greathealth.oms.ui.api.shipment.TaslyShipmentQuerySupport;


/**
 * @author Henter Liu
 */
public class TaslyShipmentQueryFactory extends ShipmentQueryFactory
{
	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<String, String>();

	@Override
	public CriteriaQuery<ShipmentData> findShipmentsByQuery(final ShipmentQueryObject queryObject)
	{
		final CriteriaQuery<ShipmentData> criteriaQuery = query(ShipmentData.class);

		final List<Restriction> restrictions = new ArrayList<Restriction>();

		final boolean customerFirstNameSort = TaslyShipmentQuerySupport.CUSTOMER_FIRSTNAME.name().equals(
				queryObject.getSorting().getAttribute());

		final boolean customerLastNameSort = TaslyShipmentQuerySupport.CUSTOMER_LASTNAME.name().equals(
				queryObject.getSorting().getAttribute());

		final boolean orderIssueDateSort = TaslyShipmentQuerySupport.ISSUE_DATE.name().equals(
				queryObject.getSorting().getAttribute());
		final boolean orderScheduledShippingDateSort = TaslyShipmentQuerySupport.SCHEDULED_SHIPPING_DATE.name().equals(
				queryObject.getSorting().getAttribute());

		final boolean originalOrderIdSort = TaslyShipmentQuerySupport.ORIGINAL_ORDER_ID.name().equals(
				queryObject.getSorting().getAttribute());
		final boolean orderIdSort = TaslyShipmentQuerySupport.ORDER_ID.name().equals(queryObject.getSorting().getAttribute());
		final boolean locationDescriptionSort = TaslyShipmentQuerySupport.LOCATION.name().equals(
				queryObject.getSorting().getAttribute());

		final boolean locationStoreNameSort = TaslyShipmentQuerySupport.LOCATION_STORENAME.name().equals(
				queryObject.getSorting().getAttribute());

		final Date startDate = queryObject.getStartDate();
		final Date endDate = queryObject.getEndDate();

		final Date startScheduledDate = queryObject.getStartScheduledDate();
		final Date endScheduledDate = queryObject.getEndScheduledDate();

		boolean originalOrderIdsFilter = false;
		if (queryObject instanceof TaslyShipmentQueryObject)
		{
			originalOrderIdsFilter = (((TaslyShipmentQueryObject) queryObject).getOriginalOrderIds() != null)
					&& (!(((TaslyShipmentQueryObject) queryObject).getOriginalOrderIds().isEmpty()));
		}
		final boolean orderIdsFilter = (queryObject.getOrderIds() != null) && (!(queryObject.getOrderIds().isEmpty()));
		final boolean orderLastNameFilter = queryObject.getCustomerLastName() != null;
		final boolean preOrderFilter = queryObject.getPreOrder() != null;

		final boolean olqStatusFilter = (queryObject.getShipmentStatusIds() != null)
				&& (!(queryObject.getShipmentStatusIds().isEmpty()));

		final boolean locationIdsFilter = (queryObject.getLocationIds() != null) && (!(queryObject.getLocationIds().isEmpty()));
		final boolean orderDateFilter = (startDate != null) || (endDate != null);
		final boolean scheduledShippingDateFilter = (startScheduledDate != null) || (endScheduledDate != null);
		final boolean pickupFilter = queryObject.getPickupInStore() != null;

		final boolean customerJoins = (customerFirstNameSort) || (customerLastNameSort);
		final boolean orderIdJoins = (orderIdSort) || (orderIdsFilter) || (originalOrderIdSort);
		final boolean otherFieldJoins = (orderIssueDateSort) || (orderScheduledShippingDateSort) || (orderLastNameFilter)
				|| (orderDateFilter) || (scheduledShippingDateFilter) || (originalOrderIdsFilter);

		final boolean orderIdJoin = (customerJoins) || (orderIdJoins) || (otherFieldJoins);
		final boolean locationJoin = locationDescriptionSort;

		final FillQueryParameter parameterObject = new FillQueryParameter();

		parameterObject.setQueryObject(queryObject);
		parameterObject.setCriteriaQuery(criteriaQuery);
		parameterObject.setRestrictions(restrictions);
		parameterObject.setLocationStoreNameSort(locationStoreNameSort);
		parameterObject.setStartDate(startDate);
		parameterObject.setEndDate(endDate);
		parameterObject.setStartScheduledDate(startScheduledDate);
		parameterObject.setEndScheduledDate(endScheduledDate);
		parameterObject.setOriginalOrderIdsFilter(originalOrderIdsFilter);
		parameterObject.setOrderIdsFilter(orderIdsFilter);
		parameterObject.setOrderLastNameFilter(orderLastNameFilter);
		parameterObject.setOlqStatusFilter(olqStatusFilter);
		parameterObject.setLocationIdsFilter(locationIdsFilter);
		parameterObject.setPickupFilter(pickupFilter);
		parameterObject.setOrderIdJoin(orderIdJoin);
		parameterObject.setLocationJoin(locationJoin);
		parameterObject.setPreOrderFilter(preOrderFilter);

		return fillQuery(parameterObject);
	}

	protected CriteriaQuery<ShipmentData> fillQuery(final FillQueryParameter parameterObject)
	{
		CriteriaQuery<ShipmentData> queryToFill = parameterObject.getCriteriaQuery();

		filteringOnOriginalOrderId(parameterObject.getQueryObject(), parameterObject.getRestrictions(),
				parameterObject.isOriginalOrderIdsFilter());
		filteringOnOrderId(parameterObject.getQueryObject(), parameterObject.getRestrictions(), parameterObject.isOrderIdsFilter());
		filteringOnStatusIds(parameterObject.getQueryObject(), parameterObject.getRestrictions(),
				parameterObject.isOlqStatusFilter());

		filteringOnLocIds(parameterObject.getQueryObject(), parameterObject.getRestrictions(),
				parameterObject.isLocationIdsFilter());

		addPreOrderFilter(parameterObject.getQueryObject(), parameterObject.getRestrictions(), parameterObject.isPreOrderFilter());
		addPickupFilter(parameterObject.getQueryObject(), parameterObject.getRestrictions(), parameterObject.isPickupFilter());
		queryToFill = joiningToOrdersTable(parameterObject.getQueryObject(), queryToFill, parameterObject.getRestrictions(),
				parameterObject.isOrderLastNameFilter(), parameterObject.isOrderIdJoin());

		queryToFill = joiningToLocationTables(queryToFill, parameterObject.getRestrictions(),
				parameterObject.isLocationStoreNameSort(), parameterObject.isLocationJoin());

		filteringOnStartDateAndEndDate(parameterObject.getRestrictions(), parameterObject.getStartDate(),
				parameterObject.getEndDate());

		filteringOnScheduledShippingStartAndEndDate(parameterObject.getRestrictions(), parameterObject.getStartScheduledDate(),
				parameterObject.getEndScheduledDate());

		queryToFill = applyingCollectedRestrictionsToTheCriteriaQueryObject(queryToFill, parameterObject.getRestrictions());

		queryToFill = queryToFill.order(QUERY_SUPPORT_MAPPING.get(parameterObject.getQueryObject().getSorting().getAttribute()),
				SortDirection.ASC.equals(parameterObject.getQueryObject().getSorting().getDirection()));

		return queryToFill;
	}

	protected void filteringOnOriginalOrderId(final ShipmentQueryObject queryObject, final List<Restriction> restrictions,
			final boolean originalOrderIdFilter)
	{
		if (!(originalOrderIdFilter))
		{
			return;
		}
		if (queryObject instanceof TaslyShipmentQueryObject)
		{
			restrictions.add(RawRestrictions.eq("ordat.original_order_id",
					((TaslyShipmentQueryObject) queryObject).getOriginalOrderIds()));
		}
	}

	@Override
	protected CriteriaQuery<ShipmentData> joiningToOrdersTable(final ShipmentQueryObject queryObject,
			final CriteriaQuery<ShipmentData> origCriteria, final List<Restriction> restrictions, final boolean orderLastNameFilter,
			final boolean orderIdJoin)
	{
		CriteriaQuery<ShipmentData> criteriaQuery = origCriteria;
		if (orderIdJoin)
		{
			criteriaQuery = (criteriaQuery.join(TaslyOrderData.class, "ordat")).on(ShipmentData.ORDERFK);
			if (orderLastNameFilter)
			{
				restrictions.add(RawRestrictions.like("ordat.lastName", queryObject.getCustomerLastName()));
			}
		}
		return criteriaQuery;
	}

	static
	{
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.DEFAULT.name(), "shipmentId");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.CUSTOMER_FIRSTNAME.name(), "ordat.firstName");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.CUSTOMER_LASTNAME.name(), "ordat.lastName");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.ISSUE_DATE.name(), "ordat.issueDate");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.SCHEDULED_SHIPPING_DATE.name(), "ordat.scheduledShippingDate");

		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.LOCATION.name(), "loc.description");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.LOCATION_STORENAME.name(), "loc.storeName");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.OLQSSTATUS.name(), "olqsStatus");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.ORIGINAL_ORDER_ID.name(), "ordat.original_order_id");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.ORDER_ID.name(), "ordat.orderId");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.SHIPMENT_ID.name(), "shipmentId");
		QUERY_SUPPORT_MAPPING.put(TaslyShipmentQuerySupport.SHIPPING_METHOD.name(), "shippingMethod");
	}

	public static class FillQueryParameter
	{
		private ShipmentQueryObject queryObject;
		private CriteriaQuery<ShipmentData> criteriaQuery;
		private List<Restriction> restrictions;
		private boolean locationStoreNameSort;
		private Date startDate;
		private Date endDate;
		private Date startScheduledDate;
		private Date endScheduledDate;
		private boolean originalOrderIdsFilter;
		private boolean orderIdsFilter;
		private boolean orderLastNameFilter;
		private boolean olqStatusFilter;
		private boolean locationIdsFilter;
		private boolean pickupFilter;
		private boolean orderIdJoin;
		private boolean olqStatusJoin;
		private boolean locationJoin;
		private boolean preOrderFilter;

		public ShipmentQueryObject getQueryObject()
		{
			return this.queryObject;
		}

		public void setQueryObject(final ShipmentQueryObject queryObject)
		{
			this.queryObject = queryObject;
		}

		public CriteriaQuery<ShipmentData> getCriteriaQuery()
		{
			return this.criteriaQuery;
		}

		public void setCriteriaQuery(final CriteriaQuery<ShipmentData> criteriaQuery)
		{
			this.criteriaQuery = criteriaQuery;
		}

		public List<Restriction> getRestrictions()
		{
			return this.restrictions;
		}

		public void setRestrictions(final List<Restriction> restrictions)
		{
			this.restrictions = restrictions;
		}

		public boolean isLocationStoreNameSort()
		{
			return this.locationStoreNameSort;
		}

		public void setLocationStoreNameSort(final boolean locationStoreNameSort)
		{
			this.locationStoreNameSort = locationStoreNameSort;
		}

		public Date getStartDate()
		{
			return this.startDate;
		}

		public void setStartDate(final Date startDate)
		{
			this.startDate = startDate;
		}

		public Date getEndDate()
		{
			return this.endDate;
		}

		public void setEndDate(final Date endDate)
		{
			this.endDate = endDate;
		}

		public boolean isOriginalOrderIdsFilter()
		{
			return originalOrderIdsFilter;
		}

		public void setOriginalOrderIdsFilter(final boolean originalOrderIdsFilter)
		{
			this.originalOrderIdsFilter = originalOrderIdsFilter;
		}

		public boolean isOrderIdsFilter()
		{
			return this.orderIdsFilter;
		}

		public void setOrderIdsFilter(final boolean orderIdsFilter)
		{
			this.orderIdsFilter = orderIdsFilter;
		}

		public boolean isOrderLastNameFilter()
		{
			return this.orderLastNameFilter;
		}

		public void setOrderLastNameFilter(final boolean orderLastNameFilter)
		{
			this.orderLastNameFilter = orderLastNameFilter;
		}

		public boolean isOlqStatusFilter()
		{
			return this.olqStatusFilter;
		}

		public void setOlqStatusFilter(final boolean olqStatusFilter)
		{
			this.olqStatusFilter = olqStatusFilter;
		}

		public boolean isLocationIdsFilter()
		{
			return this.locationIdsFilter;
		}

		public void setLocationIdsFilter(final boolean locationIdsFilter)
		{
			this.locationIdsFilter = locationIdsFilter;
		}

		public boolean isPickupFilter()
		{
			return this.pickupFilter;
		}

		public void setPickupFilter(final boolean pickupFilter)
		{
			this.pickupFilter = pickupFilter;
		}

		public boolean isOrderIdJoin()
		{
			return this.orderIdJoin;
		}

		public void setOrderIdJoin(final boolean orderIdJoin)
		{
			this.orderIdJoin = orderIdJoin;
		}

		public boolean isOlqStatusJoin()
		{
			return this.olqStatusJoin;
		}

		public void setOlqStatusJoin(final boolean olqStatusJoin)
		{
			this.olqStatusJoin = olqStatusJoin;
		}

		public boolean isLocationJoin()
		{
			return this.locationJoin;
		}

		public void setLocationJoin(final boolean locationJoin)
		{
			this.locationJoin = locationJoin;
		}

		public void setPreOrderFilter(final boolean preOrderFilter)
		{
			this.preOrderFilter = preOrderFilter;
		}

		public boolean isPreOrderFilter()
		{
			return this.preOrderFilter;
		}

		public Date getStartScheduledDate()
		{
			return this.startScheduledDate;
		}

		public void setStartScheduledDate(final Date startScheduledDate)
		{
			this.startScheduledDate = startScheduledDate;
		}

		public Date getEndScheduledDate()
		{
			return this.endScheduledDate;
		}

		public void setEndScheduledDate(final Date endScheduledDate)
		{
			this.endScheduledDate = endScheduledDate;
		}
	}
}
