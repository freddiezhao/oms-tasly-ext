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
package tasly.greathealth.thirdparty.inventory.services;

import java.util.List;

import tasly.greathealth.thirdparty.inventory.beans.StoreSku;
import tasly.greathealth.tmall.inventory.domain.ProductOms;
import tasly.greathealth.tmall.inventory.domain.ProductTm;


/**
 *
 */
public interface UpdateItemService
{
	public List<StoreSku> getItemsFromStore();

	public List<ProductTm> updateProductTmQuntityInMemory(List<ProductTm> pts, List<ProductOms> pos);

	public List<StoreSku> getUpdateFailedList();

	public List<StoreSku> prepareData(List<ProductOms> pos);

	public void updateInventoryToStore(List<StoreSku> skus);

	public boolean updateInventoryToStoreWithSkus(final ProductTm tms);

	public boolean updateItemQuantity(ProductTm pt);

	public void syncInventoryToStore();

	public StoreSku getStoreSkuFromStore(final String outerid);

	public boolean updateComboInventoryToStore(ProductTm tm);

	public boolean updateStockInfoBySku(StoreSku tm);

}
