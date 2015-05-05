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
package tasly.greathealth.tmall.inventory.services;

import java.util.List;

import tasly.greathealth.tmall.inventory.domain.ProductOms;
import tasly.greathealth.tmall.inventory.domain.ProductTm;

import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;


/**
 *
 */
public interface TmallItemService
{
	public List<Item> mergeAllProductsFromTmall(String shopName);

	public List<ProductTm> updateProductTmQuntityInMemory(List<ProductTm> pts, List<ProductOms> pos);

	public List<ProductTm> getUpdateFailedList();

	public void updateInventoryToTmall(final List<ProductTm> tms);

	public boolean updateInventoryToTmallWithSkus(final ProductTm tms);

	public boolean updateItemQuantity(ProductTm pt);

	public void syncProductsToTm();

	public Item getItemFromTm(String shopName, Long num_iid);

	public Item getItemFromTmByOuterId(String shopName, String outerId);

	public Sku getSkuFromTmByOuterId(String shopName, String outerId);

	public boolean updateComboInventoryToTmall(ProductTm tm);

	public boolean updateSingleSkuInventoryToTmall(ProductTm tm);

}
