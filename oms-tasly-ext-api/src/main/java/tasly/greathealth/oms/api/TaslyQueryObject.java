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
package tasly.greathealth.oms.api;

import com.hybris.oms.domain.QueryCriteria;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.QuerySorting;
import com.hybris.oms.domain.SortDirection;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * @author Henter Liu
 */
public class TaslyQueryObject<S extends Enum<S>> extends QueryObject<S>
{
	public TaslyQueryObject()
	{
		super();
	}

	public TaslyQueryObject(final List<QueryCriteria> criteriaList, final Integer pageNumber, final Integer pageSize,
			final QuerySorting sorting) throws IllegalStateException
	{
		super(criteriaList, pageNumber, pageSize, sorting);
	}

	public void setSorting(final String sorting, final SortDirection direction)
	{
		setSorting(new QuerySorting(sorting, direction));
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

	protected void superSetValue(final String attributeName, final Date attributeValue)
	{
		if (attributeValue == null)
		{
			return;
		}
		super.setValue(attributeName, attributeValue);
	}

	protected void superSetValue(final String attributeName, final String attributeValue)
	{
		if (attributeValue == null)
		{
			return;
		}
		super.setValue(attributeName, attributeValue);
	}

	protected void superSetValues(final String attributeName, final List<String> attributeValue)
	{
		if (!(CollectionUtils.isNotEmpty(attributeValue)))
		{
			return;
		}
		super.setValues(attributeName, attributeValue);
	}
}
