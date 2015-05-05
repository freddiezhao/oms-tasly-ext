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
package tasly.greathealth.oms.api.financial;

import java.util.Date;

import tasly.greathealth.oms.api.TaslyQueryObject;


/**
 * @author gxx
 */
public class TaslyFinancialReportQueryObject extends TaslyQueryObject<TaslyFinancialReportQuerySupport>
{
	public TaslyFinancialReportQueryObject()
	{
		// setSorting(ExpressQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public Date getStartDate()
	{
		return getDateValue("startDate");
	}

	public void setStartDate(final Date startDate)
	{
		superSetValue("startDate", startDate);
	}

	public Date getEndDate()
	{
		return getDateValue("endDate");
	}

	public void setEndate(final Date endDate)
	{
		superSetValue("endDate", endDate);
	}
}
