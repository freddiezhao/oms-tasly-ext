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

import java.util.List;

import tasly.greathealth.oms.api.inventory.dto.ItemQuantityElement;


/**
 * The interface ItemQuantityService API.
 */
public interface ItemQuantityFacade
{
	/***
	 * Update item quantity every 4.AM.
	 *
	 * @category OMS-Extension
	 *
	 * @return
	 */
	boolean updateItemQuantity();

	boolean batchUpdate(final String sku, final List<ItemQuantityElement> list);
}
