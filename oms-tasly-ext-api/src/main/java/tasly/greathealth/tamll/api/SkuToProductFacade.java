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
package tasly.greathealth.tamll.api;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.exception.EntityValidationException;

import java.util.Collection;
import java.util.List;

import tasly.greathealth.oms.api.order.SkuToProductQueryObject;
import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.api.order.dto.UISkuToProduct;
import tasly.greathealth.tamll.api.order.dto.SkuToProductList;
import tasly.greathealth.tamll.api.order.dto.SkuToProductsList;
import tasly.greathealth.thirdparty.packagedto.ItemModel;
import tasly.greathealth.thirdparty.packagedto.PackageOuterIds;
import tasly.greathealth.thirdparty.packagedto.PackageRelationModel;
import tasly.greathealth.thirdparty.packagedto.Result;


/**
 *
 */
public interface SkuToProductFacade
{
	Collection<SkuToProduct> querySkuToProducts(String outerId, String channel, String innerSource);

	Collection<SkuToProduct> querySkuToProductByitemId(String itemId, String channel, String innerSource);

	void createSkuToProduct(SkuToProduct skuToProduct);

	SkuToProductsList updateSkuToProducts(List<SkuToProductList> skuToProductsList);

	SkuToProductsList querySkuToProductsList(SkuToProductsList skuToProductsList);

	void deleteSkuToProducts(SkuToProduct skuToProduct);

	/**
	 * @param queryObject
	 * @return
	 * @throws EntityValidationException
	 */
	Pageable<UISkuToProduct> findSkuToProductByQuery(SkuToProductQueryObject queryObject) throws EntityValidationException;

	/**
	 * @param skuToProduct
	 * @return
	 */
	SkuToProduct updateSkuToProduct(SkuToProduct skuToProduct);

	/**
	 * @param outerId
	 * @param channel
	 * @param innerSource
	 */
	void deleteSkuToProducts(String outerId, String channel, String innerSource);


	Collection<SkuToProduct> queryAllRelatedSkuToProductByItemId(String itemId, String channel, String innerSource);

	/**
	 * @param channel
	 * @param innerSource
	 * @return
	 */
	List<SkuToProduct> querySkuToProduct(String channel, String innerSource);

	PackageRelationModel updateSkuToProductInventoryToThirdPartProcess(PackageRelationModel packageItemRelationModel);

	Result updateItemQuantity(ItemModel itemModel);

	PackageOuterIds getPackageQuantity(PackageOuterIds packageOuterIds);
}
