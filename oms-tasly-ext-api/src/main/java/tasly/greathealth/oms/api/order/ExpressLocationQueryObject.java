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
import tasly.greathealth.oms.api.order.dto.ExpressLocationQuerySupport;


/**
 * @author Henter Liu
 */
public class ExpressLocationQueryObject extends TaslyQueryObject<ExpressLocationQuerySupport>
{
	public ExpressLocationQueryObject()
	{
		setSorting(ExpressLocationQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public void setProvince(final String province)
	{
		superSetValue("province", province);
	}

	public String getProvince()
	{
		return super.getValue("province");
	}

	public void setCode(final String code)
	{
		superSetValue("code", code);
	}

	public String getCode()
	{
		return super.getValue("code");
	}
}
