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
import com.hybris.oms.domain.inventory.ItemLocationQuerySupport;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * @author Henter Liu
 */
public class TaslyItemLocationQueryObject extends ItemLocationsQueryObject
{
	public TaslyItemLocationQueryObject()
	{
		setSorting(ItemLocationQuerySupport.DEFAULT, SortDirection.ASC);
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
