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
import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.Collection;
import java.util.List;

import tasly.greathealth.oms.api.inventory.dto.ItemInfo;


/**
 * The interface ProductDescriptionService API.
 */
public interface ItemInfoFacade
{
	/**
	 * Creates item object.
	 *
	 * @category OMS-Extension
	 *
	 * @param product
	 *           the item info to create
	 */
	void create(ItemInfo itemInfo);

	/***
	 * add by libin539 TS-193
	 * Batch Update Inventory
	 *
	 * @param itemInfos
	 *           return 无法入库产品SKU list
	 */
	List<?> batchUpdateInventory(List<ItemInfo> itemInfos) throws Exception;

	/***
	 * add by libin539
	 * Batch Update For
	 *
	 * @param itemInfos
	 */
	boolean batchUpdate(List<ItemInfo> itemInfos) throws Exception;

	/**
	 * Search a item by its sku.
	 *
	 * @category OMS-Extension
	 *
	 * @param sku
	 *           the sku you are looking for
	 * @return the item corresponding to your search
	 * @throws EntityNotFoundException the ItemInfo not found exception
	 */
	ItemInfo getBySku(String sku) throws EntityNotFoundException;

	/**
	 * Search items by query object.
	 *
	 * @category OMS-Extension
	 *
	 * @param query
	 *           the query you are looking for
	 * @return the item corresponding to your search
	 * @throws EntityNotFoundException the ItemInfo not found exception
	 */
	Pageable<ItemInfo> findItemInfosByQuery(ItemInfoQueryObject query) throws EntityNotFoundException;

	/**
	 * Returns all item infos.
	 *
	 * @category OMS-Extension
	 *
	 * @return full list of item infos
	 */
	Collection<ItemInfo> getAll();
}
