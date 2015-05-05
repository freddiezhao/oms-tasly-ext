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
package tasly.greathealth.thirdparty.inventory.convertor;

import java.util.List;

import tasly.greathealth.thirdparty.inventory.beans.StoreSku;
import tasly.greathealth.tmall.inventory.domain.ProductOms;


/**
 *
 */
public interface StoreSkuConvertor
{
	// convert productTms from Item and productOms
	public List<StoreSku> convertData(final List<StoreSku> itemMap, final List<ProductOms> omsProducts);

	// public List<ProductTm> convertData_sku(final List<Item> items, final List<ProductOms> omsProducts);

	public List<StoreSku> getListFromInternalMemory();

	public List<StoreSku> getMatchFailedData();
}
