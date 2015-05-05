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
package tasly.greathealth.oms.export.stock.services;

import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;

import java.util.List;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationQueryObject;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;


/**
 *
 */
public interface ExportStockService
{

	List<ItemInfoData> findListItemInfosByQuery(ItemLocationsQueryObject queryObject);

	List<TaslyItemLocationData> findListItemLocationsByQuery(TaslyItemLocationQueryObject queryObject);

	int getAtsByQuery(String itemId, String locationId);
}
