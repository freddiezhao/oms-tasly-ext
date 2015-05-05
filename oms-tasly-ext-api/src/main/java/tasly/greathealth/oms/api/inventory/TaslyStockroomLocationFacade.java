/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.api.inventory;

import com.hybris.oms.api.Pageable;

import tasly.greathealth.oms.api.inventory.dto.StockroomLocation;


/**
 * The interface TaslyItemLocationService API.
 */
public interface TaslyStockroomLocationFacade
{
	/**
	 * Creates item location object.
	 *
	 * @category OMS-Extension
	 *
	 * @param product
	 *           the item location to create
	 */
	Pageable<StockroomLocation> findTaslyStockRoomLocationsByQuery(TaslyStockroomLocationQueryObject queryObject);

	StockroomLocation updateStockroomLocation(StockroomLocation stockroomLocation);
}
