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
package tasly.greathealth.oms.inventory.services;

import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.List;

import tasly.greathealth.oms.api.inventory.ItemInfoQueryObject;
import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;


/**
 * Service providing methods to manipulate {@link ItemInfo} object.
 */
public interface ItemInfoService
{
	/**
	 * Creates a item info.
	 *
	 * @param item info
	 *           the item info to create
	 */
	ItemInfoData createItemInfo();

	/***
	 * add by libin539 TS-193
	 * Batch Update Inventory
	 *
	 * @param itemInfos
	 * @return
	 */
	List<?> batchUpdateInventory(List<ItemInfo> itemInfos) throws Exception;

	/***
	 * add by libin539
	 * Batch Update Iteminfos
	 *
	 * @param itemInfos
	 * @return
	 */
	boolean batchUpdate(List<ItemInfo> itemInfos) throws Exception;

	/***
	 * add by libin539
	 * Batch Update Itemlocations,仅供增加渠道使用
	 *
	 * @param itemInfos
	 * @return
	 */
	boolean batchUpdateItemlocation(String channels) throws Exception;

	/**
	 * Get the item info by the sku passed in parameter.
	 *
	 * @return the requested item info which match the sku in parameter
	 * @throws EntityNotFoundException
	 *            the Product not found exception
	 */
	ItemInfoData getBySku(String sku) throws EntityNotFoundException;

	/**
	 * Get the item info by the description passed in parameter.
	 *
	 * @return the requested item info which match the sku in parameter
	 * @throws EntityNotFoundException
	 *            the Product not found exception
	 */
	Page<ItemInfoData> findItemInfosByQuery(ItemInfoQueryObject query) throws EntityNotFoundException;

	/**
	 * Get all the item infos by flag.
	 *
	 * @return a list of all the existing item info
	 */
	List<ItemInfoData> getAll();

	/**
	 *
	 * @return a list of all the Item info Data
	 */
	List<ItemInfoData> getItemInfoByStockManageFlag(int flag, String innerSource);

	/**
	 * Get all the items by location
	 *
	 * @return
	 */
	public List<TaslyItemLocationData> getByLocation(StockroomLocationData location, String[] skus);
}
