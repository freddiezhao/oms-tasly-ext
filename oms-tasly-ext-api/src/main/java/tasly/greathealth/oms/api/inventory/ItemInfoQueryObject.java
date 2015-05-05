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
package tasly.greathealth.oms.api.inventory;

import com.hybris.oms.domain.SortDirection;

import tasly.greathealth.oms.api.TaslyQueryObject;


/**
 * @author Henter Liu
 */
public class ItemInfoQueryObject extends TaslyQueryObject<ItemInfoQuerySupport>
{
	public ItemInfoQueryObject()
	{
		setSorting(ItemInfoQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public void setSku(final String sku)
	{
		superSetValue("sku", sku);
	}

	public String getSku()
	{
		return super.getValue("sku");
	}

	public void setDescription(final String description)
	{
		superSetValue("description", description);
	}

	public String getDescription()
	{
		return super.getValue("description");
	}
}
