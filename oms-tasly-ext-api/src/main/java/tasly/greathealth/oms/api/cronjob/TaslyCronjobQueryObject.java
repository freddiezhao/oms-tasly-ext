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
package tasly.greathealth.oms.api.cronjob;

import com.hybris.oms.domain.SortDirection;

import tasly.greathealth.oms.api.TaslyQueryObject;


/**
 * @author Henter Liu
 */
public class TaslyCronjobQueryObject extends TaslyQueryObject<TaslyCronjobQuerySupport>
{
	public TaslyCronjobQueryObject()
	{
		setSorting(TaslyCronjobQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public void setName(final String name)
	{
		superSetValue("name", name);
	}

	public String getName()
	{
		return super.getValue("name");
	}
}
