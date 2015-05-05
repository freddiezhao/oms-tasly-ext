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
package tasly.greathealth.tmall.inventory.dao;

import java.util.List;

import tasly.greathealth.tmall.inventory.domain.ProductTm;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;
import com.taobao.api.response.ItemQuantityUpdateResponse;
import com.taobao.api.response.SkusQuantityUpdateResponse;


/**
 *
 */
public interface TmallItemDao
{
	/**
	 *
	 * @param taboclient
	 * @param sessionKey
	 * @param page_no
	 * @param page_size
	 * @return
	 */
	public List<Item> getInStockInfoFromTmall(TaobaoClient taboclient, String sessionKey, Long page_no, Long page_size,
			String banner);

	/**
	 *
	 * @param taboclient
	 * @param sessinKey
	 * @param page_no
	 * @param page_size
	 * @return
	 */
	public List<Item> getOnSaleInfoFromTmall(TaobaoClient taboclient, String sessinKey, Long page_no, Long page_size);


	/**
	 * update products which don't have skus attribute to tmall
	 *
	 * @param taboclient
	 * @param sessionKey
	 * @param productTm
	 * @return
	 * @throws ApiException
	 */
	public ItemQuantityUpdateResponse updateStockInfoWithoutSkus(TaobaoClient taboclient, String sessionKey, ProductTm productTm)
			throws ApiException;


	/**
	 * update products which have skus attribute to tmall
	 * 
	 * @param taboclient
	 * @param sessionKey
	 * @return
	 * @throws ApiException
	 */
	public SkusQuantityUpdateResponse updateStockInfoWithSkus(TaobaoClient taboclient, String sessionKey, ProductTm productTm)
			throws ApiException;

	public SkusQuantityUpdateResponse updateStockInfoWithSingleSkus(TaobaoClient taboclient, String sessionKey, ProductTm productTm)
			throws ApiException;

	public Item getItem(TaobaoClient taboclient, String sessionKey, Long num_iid);

	public int getOnsaleCount();

	public int getInstockCount();

	public Item getItemByOuterId(TaobaoClient taboclient, String sessionKey, String outerid);

	public Sku getSkuByOuterId(TaobaoClient taboclient, String sessionKey, String outerid);
}
