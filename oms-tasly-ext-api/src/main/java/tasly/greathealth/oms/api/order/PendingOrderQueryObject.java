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
package tasly.greathealth.oms.api.order;

import com.hybris.oms.domain.SortDirection;

import java.util.List;

import tasly.greathealth.oms.api.TaslyQueryObject;


/**
 * @author Henter Liu
 */
public class PendingOrderQueryObject extends TaslyQueryObject<PendingOrderQuerySupport>
{
	public PendingOrderQueryObject()
	{
		setSorting(PendingOrderQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public void setTid(final String tid)
	{
		superSetValue("tid", tid);
	}

	public String getTid()
	{
		return super.getValue("tid");
	}

	public void setOid(final String oid)
	{
		superSetValue("oid", oid);
	}

	public String getOid()
	{
		return super.getValue("oid");
	}

	public void setEventTypes(final List<String> eventTypes)
	{
		superSetValues("eventType", eventTypes);
	}

	public List<String> getEventTypes()
	{
		return super.getValues("eventType");
	}

	public void setState(final String state)
	{
		superSetValue("state", state);
	}

	public String getState()
	{
		return super.getValue("state");
	}
}
