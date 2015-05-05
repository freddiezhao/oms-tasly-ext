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
package tasly.greathealth.oms.ui.api.shipment;

import com.hybris.oms.domain.QuerySorting;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.shipping.ShipmentQuerySupport;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.base.Strings;


/**
 * @author Henter Liu
 */
public class TaslyShipmentQueryObject extends ShipmentQueryObject
{
	public TaslyShipmentQueryObject()
	{
		setSorting(ShipmentQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public TaslyShipmentQueryObject(final List<String> originalOrderIds, final List<String> orderIds, final List<String> statusIds,
			final List<String> locationIds, final Integer pageNumber, final Integer pageSize, final String customerLastName,
			final Boolean preOrder)
	{
		if (CollectionUtils.isNotEmpty(originalOrderIds))
		{
			setOriginalOrderIds(originalOrderIds);
		}
		if (CollectionUtils.isNotEmpty(orderIds))
		{
			setOrderIds(orderIds);
		}
		if (CollectionUtils.isNotEmpty(statusIds))
		{
			setShipmentStatusIds(statusIds);
		}
		if (CollectionUtils.isNotEmpty(locationIds))
		{
			setLocationIds(locationIds);
		}
		if (customerLastName != null)
		{
			setCustomerLastName(customerLastName);
		}
		if (preOrder != null)
		{
			setPreOrder(preOrder);
		}
		buildPageNumberAndSize(pageNumber, pageSize);
	}

	public TaslyShipmentQueryObject(final List<String> originalOrderIds, final List<String> orderIds, final List<String> statusIds,
			final List<String> locationIds, final Integer pageNumber, final Integer pageSize, final String customerLastName,
			final String sortColumn, final String sortDirection, final String startDate, final String endDate,
			final String startScheduledDate, final String endSchedulingDate, final String pickupInStore, final Boolean preOrder)
	{
		this(originalOrderIds, orderIds, statusIds, locationIds, pageNumber, pageSize, customerLastName, preOrder);
		if (startDate != null)
		{
			final Date beginDate = new Date(Long.valueOf(startDate).longValue());
			setStartDate(beginDate);
		}
		if (endDate != null)
		{
			final Date endyDate = new Date(Long.valueOf(endDate).longValue());
			setEndDate(endyDate);
		}

		if (startScheduledDate != null)
		{
			final Date beginScheduledDate = new Date(Long.valueOf(startScheduledDate).longValue());
			setStartScheduledDate(beginScheduledDate);
		}
		if (endSchedulingDate != null)
		{
			final Date endScheduledDate = new Date(Long.valueOf(endSchedulingDate).longValue());
			setEndScheduledDate(endScheduledDate);
		}

		if (pickupInStore != null)
		{
			setPickupInStore(Boolean.valueOf(pickupInStore));
		}
		if (Strings.isNullOrEmpty(sortColumn))
		{
			return;
		}
		final ShipmentQuerySupport column = ShipmentQuerySupport.valueOf(sortColumn);
		final SortDirection direction = (Strings.isNullOrEmpty(sortDirection)) ? SortDirection.ASC
				: SortDirection.valueOf(sortDirection);

		final QuerySorting sorting = new QuerySorting(column.name(), direction);
		setSorting(sorting);
	}

	public List<String> getOriginalOrderIds()
	{
		return super.getValues("originalOrderId");
	}

	public final void setOriginalOrderIds(final List<String> originalOrderIds)
	{
		superSetValues("originalOrderId", originalOrderIds);
	}

	public void setChannels(final List<String> channels)
	{
		superSetValues("channel", channels);
	}

	public List<String> getChannels()
	{
		return super.getValues("channel");
	}

	public void setInnerSources(final List<String> innerSources)
	{
		superSetValues("innerSource", innerSources);
	}

	public List<String> getInnerSources()
	{
		return super.getValues("innerSource");
	}

	private void superSetValues(final String attributeName, final List<String> attributeValue)
	{
		if (!(CollectionUtils.isNotEmpty(attributeValue)))
		{
			return;
		}
		super.setValues(attributeName, attributeValue);
	}
}
