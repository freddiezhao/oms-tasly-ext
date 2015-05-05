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
package tasly.greathealth.tmall.order.services;

import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.List;

import tasly.greathealth.oms.api.order.SkuToProductQueryObject;
import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.domain.order.SkuToProductData;


/**
 * Service providing methods to manipulate {@link SkuToProduct} object.
 */
public interface SkuToProductService
{

	List<SkuToProductData> querySkuToProducts(String outerId, String channel, String innerSource) throws EntityNotFoundException;

	List<SkuToProductData> querySkuToProductByItemId(String itemId, String channel, String innerSource)
			throws EntityNotFoundException;

	SkuToProductData createSkuToProduct();

	void deleteSkuToProducts(String outerId, String channel, String innerSource);

	/**
	 * @param queryObject
	 * @return
	 */
	Page<SkuToProductData> findSkuToProductByQuery(SkuToProductQueryObject queryObject);

	/**
	 * @param skuToProduct
	 * @return
	 */
	SkuToProductData updateSkuToProduct(SkuToProduct skuToProduct);

	/**
	 * @param channel
	 * @param innerSource
	 * @return
	 */
	List<SkuToProductData> querySkuToProductByItemId(String channel, String innerSource);

}
