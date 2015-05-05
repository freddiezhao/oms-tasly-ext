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
package tasly.greathealth.oms.inventory.services;

import com.hybris.oms.domain.inventory.ItemQuantity;

import java.util.EnumMap;
import java.util.List;

import tasly.greathealth.oms.api.inventory.dto.ItemQuantityElement;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;


/**
 * Service providing methods to manipulate {@link ItemQuantity} object.
 */
public interface ItemQuantityService
{
	enum HandleReturn
	{
		handleStatus, availableNumber, errorStatus
	}

	/***
	 * handle one item quantity
	 *
	 * @param taslyItemLocationDatas
	 * @param sku
	 * @param flag
	 * @param totalNumber
	 * @return EnumMap
	 */
	EnumMap<HandleReturn, Object> handleUpdateMethod(final List<TaslyItemLocationData> taslyItemLocationDatas, final String sku,
			final int flag, final int totalNumber);

	/***
	 * Update one item quantity
	 *
	 * @param sku
	 * @param flag
	 * @param totalNumber
	 */
	boolean updateMethod(final String sku, final int flag, final int totalNumber);

	int calculateAts(final String sku, final String locationId);

	boolean batchUpdate(String sku, List<ItemQuantityElement> list);
}
