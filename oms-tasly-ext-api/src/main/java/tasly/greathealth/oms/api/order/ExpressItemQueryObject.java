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

import tasly.greathealth.oms.api.TaslyQueryObject;
import tasly.greathealth.oms.api.order.dto.ExpressItemQuerySupport;


/**
 *
 */
public class ExpressItemQueryObject extends TaslyQueryObject<ExpressItemQuerySupport>
{
	public ExpressItemQueryObject()
	{
		setSorting(ExpressItemQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public void setSkuid(final String skuid)
	{
		superSetValue("skuid", skuid);
	}

	public String getSkuid()
	{
		return super.getValue("skuid");
	}

	public void setCode(final String code)
	{
		superSetValue("code", code);
	}

	public String getCode()
	{
		return super.getValue("code");
	}

	public void setInner(final String inner)
	{
		superSetValue("inner", inner);
	}

	public String getInner()
	{
		return super.getValue("inner");
	}

	public void setChannel(final String channel)
	{
		superSetValue("channel", channel);
	}

	public String getChannel()
	{
		return super.getValue("channel");
	}

	public void setStatus(final String status)
	{
		superSetValue("status", status);
	}

	public String getStatus()
	{
		return super.getValue("status");
	}
}
