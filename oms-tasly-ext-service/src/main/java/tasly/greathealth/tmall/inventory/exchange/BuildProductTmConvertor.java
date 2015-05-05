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
package tasly.greathealth.tmall.inventory.exchange;

import java.util.List;
import java.util.Map;

import tasly.greathealth.tmall.inventory.domain.ProductOms;
import tasly.greathealth.tmall.inventory.domain.ProductTm;

import com.taobao.api.domain.Item;


/**
 *
 */
public interface BuildProductTmConvertor
{
	// convert productTms from Item and productOms
	public List<ProductTm> convertData(final Map<String, List<Item>> itemMap, final List<ProductOms> omsProducts);

	// public List<ProductTm> convertData_sku(final List<Item> items, final List<ProductOms> omsProducts);

	public List<ProductTm> getListFromInternalMemory();

	public List<ProductOms> getMatchFailedData();
}
