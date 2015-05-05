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
package tasly.greathealth.thirdparty.inventory.dao;

import java.util.List;

import tasly.greathealth.thirdparty.inventory.beans.StoreSku;


;


/**
 *
 */
public interface StoreItemDao
{
	/**
	 *
	 * @return
	 */
	public List<StoreSku> getInStockItemsFromStore();

	/**
	 *
	 * @return
	 */
	public List<StoreSku> getDownStockItemsFromStore();

	/**
	 *
	 * @return
	 */
	public List<StoreSku> getOnSaleItemsFromStore();

	/**
	 * update products which don't have skus attribute to store
	 *
	 * @return
	 */
	public boolean updateStockInfoBySku(StoreSku sku);




	public int getOnsaleCount();

	public int getInstockCount();

	public StoreSku getStoreSkuByOuterId(String outerid);

	public int getTotalItemsOfStore(int times);
}
