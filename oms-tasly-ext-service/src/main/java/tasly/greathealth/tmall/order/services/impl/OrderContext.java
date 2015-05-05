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
package tasly.greathealth.tmall.order.services.impl;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class OrderContext
{

	private final Map<String, Object> context = new HashMap<String, Object>();

	private final Map<String, String> unitQuantityCodeMap = new HashMap<String, String>();

	public Map<String, String> getAllBaseUnitQuantityCode()
	{
		return unitQuantityCodeMap;
	}

	public void addBaseUnitQuantityCode(final Map<String, String> baseUnitQuantity)
	{
		if (baseUnitQuantity != null)
		{
			unitQuantityCodeMap.putAll(baseUnitQuantity);
		}
	}

	public void addProperty(final String key, final Object value)
	{
		context.put(key, value);
	}

	public Object getProperty(final String key)
	{
		if (key != null)
		{
			return context.get(key);
		}
		return null;
	}

	public String getStringProperty(final String key)
	{
		if (key != null)
		{
			return context.get(key) == null ? "" : String.valueOf(context.get(key));
		}
		return null;
	}
}
