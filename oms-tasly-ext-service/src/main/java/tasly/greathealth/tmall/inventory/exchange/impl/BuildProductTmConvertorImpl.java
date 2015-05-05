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
package tasly.greathealth.tmall.inventory.exchange.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tamll.api.SkuToProductFacade;
import tasly.greathealth.tmall.inventory.domain.ProductOms;
import tasly.greathealth.tmall.inventory.domain.ProductTm;
import tasly.greathealth.tmall.inventory.exchange.BuildProductTmConvertor;

import com.taobao.api.domain.Item;


/**
 *
 */
public class BuildProductTmConvertorImpl implements BuildProductTmConvertor
{
	private static final Logger Log = OmsLoggerFactory.getTmallinventorylog();

	// // tmall otc client and session key
	// private TaobaoClient taobaoOtcclient;
	// private String OTC_TMALL_STORE_SESSIONKEY;
	//
	// // tmall jsc client and session key
	// private TaobaoClient taobaoJscclient;
	// private String JSC_TMALL_STORE_SESSIONKEY;

	private String TMALL_CHANNEL_SYMBOL;
	private String TMALL_SHOP_OTC;
	private String TMALL_SHOP_JSC;

	private SkuToProductFacade skuToProductFacade;

	public static List<ProductTm> productTmData = new ArrayList<ProductTm>();
	public static List<ProductOms> matchFailedProducts = new ArrayList<ProductOms>();

	@Override
	public List<ProductTm> convertData(final Map<String, List<Item>> itemsMap, final List<ProductOms> omsProducts)
	{
		final Map<String, Item> map = new HashMap<String, Item>();

		for (final Map.Entry<String, List<Item>> entry : itemsMap.entrySet())
		{
			for (final Item item : entry.getValue())
			{
				if (null != item.getOuterId() && !"".equals(item.getOuterId()))
				{
					map.put(item.getOuterId(), item);
				}
			}
			Log.info("渠道:" + entry.getKey() + "商品列表商家编码过滤后(不为空或者null)的数量:" + map.size());

			this.dataMapping(entry.getKey(), map, omsProducts);
			this.dataMappingMatchFailed(entry.getKey(), map, matchFailedProducts);
			Log.info("匹配失败的数量:" + matchFailedProducts.size());
		}
		return productTmData;
	}

	public void dataMapping(final String shopName, final Map<String, Item> itemMap, final List<ProductOms> omsProducts)
	{

		for (final ProductOms productOms : omsProducts)
		{
			final Item item = itemMap.get(productOms.getSkuId());
			if (null != item)
			{
				// num_iid match oms_sku_id
				final ProductTm p = this.convertFromOmsAndItemToTm(productOms, item, shopName);
				productTmData.add(p);
				Log.info("库存同步:InnerSource:" + shopName + "匹配成功的outer_id:" + productOms.getSkuId() + "|待更新的库存数量:"
						+ p.getTmFreeQuntity());
			}
			else
			{
				// record match failed data and print into a log file
				this.recordMatchFailedData(productOms);
			}
		}
	}

	/**
	 * reMatch the failed data
	 *
	 * @param shopName
	 * @param itemMap
	 * @param omsProducts
	 */
	public void dataMappingMatchFailed(final String shopName, final Map<String, Item> itemMap, final List<ProductOms> omsProducts)
	{
		final List<ProductOms> tempfailed = new ArrayList<ProductOms>();
		tempfailed.addAll(omsProducts);
		matchFailedProducts.clear();
		if (null != omsProducts)
		{
			for (final ProductOms pos : tempfailed)
			{
				this.getDataFromSkuToProduct(shopName, itemMap, pos);
			}
		}
	}


	public void getDataFromSkuToProduct(final String shopName, final Map<String, Item> itemMap, final ProductOms productOms)
	{
		final SkuToProduct skuToProduct = this.validateIfExistInSkuRelation(shopName, productOms.getSkuId());
		if (null != skuToProduct)
		{
			// if we can get info from the relationship table
			final List<ProductOms> poms = new ArrayList<ProductOms>();
			// caculate the new quantity of this item
			final Long num = productOms.getNum() / skuToProduct.getQuantity();
			productOms.setNum(num);
			productOms.setSkuId(skuToProduct.getOuterId());
			poms.add(productOms);
			Log.info("套餐:" + productOms.getSkuId());
			this.dataMapping(shopName, itemMap, poms);
		}
		else
		{
			// record match failed data and print into a log file
			this.recordMatchFailedData(productOms);
		}
	}

	public SkuToProduct validateIfExistInSkuRelation(final String shopname, final String itemId)
	{
		List<SkuToProduct> skuToProduct = new ArrayList<SkuToProduct>();
		if (shopname.equals(TMALL_SHOP_JSC))
		{
			skuToProduct = (List<SkuToProduct>) skuToProductFacade.querySkuToProductByitemId(itemId, TMALL_CHANNEL_SYMBOL,
					TMALL_SHOP_JSC);
			if (null != skuToProduct && skuToProduct.size() == 1)
			{
				return skuToProduct.get(0);
			}
		}
		if (shopname.equals(TMALL_SHOP_OTC))
		{
			skuToProduct = (List<SkuToProduct>) skuToProductFacade.querySkuToProductByitemId(itemId, TMALL_CHANNEL_SYMBOL,
					TMALL_SHOP_OTC);
			if (null != skuToProduct && skuToProduct.size() == 1)
			{
				return skuToProduct.get(0);
			}
		}
		return null;
	}


	/*
	 * @Override
	 * public List<ProductTm> convertData_sku(final List<Item> items, final List<ProductOms> omsProducts)
	 * {
	 * if (!items.isEmpty())
	 * {
	 * for (final Item item : items)
	 * {
	 * if (item.getNumIid() == 2100529818935L)
	 * {
	 * item.getSkus();
	 * }
	 * Item it = null;
	 * try
	 * {
	 * it = this.getItem(item.getNumIid());
	 * }
	 * catch (final ApiException e)
	 * {
	 * }
	 * if (null != it.getSkus())
	 * {
	 * for (final Sku sku : it.getSkus())
	 * {
	 * sku.setNumIid(item.getNumIid());
	 * skuMap.put(sku.getOuterId(), sku);
	 * }
	 * }
	 * }
	 * 
	 * for (final ProductOms productOms : omsProducts)
	 * {
	 * final String productOmsId = productOms.getSkuId();
	 * final Sku sku = skuMap.get(productOmsId);
	 * if (null != sku)
	 * {
	 * for (final ProductTm productTm : productTmData)
	 * {
	 * ptmMap.put(productTm.getTmProductId(), productTm);
	 * }
	 * 
	 * if (null != ptmMap.get(productOms.getSkuId()))
	 * {
	 * sku.setQuantity(Long.valueOf(productOms.getNum()));
	 * ptmMap.get(sku.getNumIid()).getSkus().add(sku);// no checking - data repeat situation
	 * }
	 * else
	 * {
	 * productTmData.add(this.convertFromOmsToTm(productOms, sku));
	 * }
	 * }
	 * else
	 * {
	 * 
	 * recordMatchFailedData(productOms);
	 * }
	 * }
	 * }
	 * return productTmData;
	 * }
	 */

	@Override
	public List<ProductTm> getListFromInternalMemory()
	{
		return productTmData;
	}

	@Override
	public List<ProductOms> getMatchFailedData()
	{
		return matchFailedProducts;
	}


	public ProductTm convertFromOmsAndItemToTm(final ProductOms productOms, final Item item, final String shopName)
	{
		final ProductTm productTm = new ProductTm();
		productTm.setTmProductId(item.getNumIid());
		productTm.setTmFreeQuntity(productOms.getNum());
		productTm.setOuterId(productOms.getSkuId());
		productTm.setShopName(shopName);
		productTm.setCombo(false);
		return productTm;
	}

	// public ProductTm convertFromOmsToTm(final ProductOms productOms, final Sku sku)
	// {
	// final List<Sku> skus = new ArrayList<Sku>();
	// sku.setQuantity(Long.valueOf(productOms.getNum()));// update the new quantity of a sku item.
	// skus.add(sku);
	// final ProductTm productTm = new ProductTm();
	// productTm.setTmProductId(sku.getNumIid());
	// productTm.setOuterId(productOms.getSkuId());
	// productTm.setSkus(skus);
	// productTm.setTmFreeQuntity(productOms.getNum());
	// return productTm;
	// }

	/**
	 * record match faild data
	 *
	 * @param productOms
	 */
	public void recordMatchFailedData(final ProductOms productOms)
	{
		matchFailedProducts.add(productOms);
	}

	/**
	 * get Item info from tmall
	 *
	 * @param num_iid
	 * @return
	 * @throws ApiException
	 */
	// public Item getItem(final Long num_iid) throws ApiException
	// {
	// // final TaobaoClient client = TmallUtils.getConnection();
	// final ItemGetRequest req = new ItemGetRequest();
	// req.setFields("num_iid,num,sku,outer_id");
	// req.setNumIid(num_iid);
	// final ItemGetResponse response = client.execute(req, DEFAULT_TMALL_STORE_SESSIONKEY);
	// if (null != response.getSubCode())
	// {
	// this.getItem(num_iid);
	// }
	// return response.getItem();
	// }




	// /**
	// * @param taobaoOtcclient the taobaoOtcclient to set
	// */
	// public void setTaobaoOtcclient(final TaobaoClient taobaoOtcclient)
	// {
	// this.taobaoOtcclient = taobaoOtcclient;
	// }
	//
	// /**
	// * @param oTC_TMALL_STORE_SESSIONKEY the oTC_TMALL_STORE_SESSIONKEY to set
	// */
	// public void setOTC_TMALL_STORE_SESSIONKEY(final String oTC_TMALL_STORE_SESSIONKEY)
	// {
	// OTC_TMALL_STORE_SESSIONKEY = oTC_TMALL_STORE_SESSIONKEY;
	// }
	//
	// /**
	// * @param taobaoJscclient the taobaoJscclient to set
	// */
	// public void setTaobaoJscclient(final TaobaoClient taobaoJscclient)
	// {
	// this.taobaoJscclient = taobaoJscclient;
	// }
	//
	// /**
	// * @param jSC_TMALL_STORE_SESSIONKEY the jSC_TMALL_STORE_SESSIONKEY to set
	// */
	// public void setJSC_TMALL_STORE_SESSIONKEY(final String jSC_TMALL_STORE_SESSIONKEY)
	// {
	// JSC_TMALL_STORE_SESSIONKEY = jSC_TMALL_STORE_SESSIONKEY;
	// }

	/**
	 * @param skuToProductFacade the skuToProductFacade to set
	 */
	public void setSkuToProductFacade(final SkuToProductFacade skuToProductFacade)
	{
		this.skuToProductFacade = skuToProductFacade;
	}

	/**
	 * @param tMALL_CHANNEL_SYMBOL the tMALL_CHANNEL_SYMBOL to set
	 */
	public void setTMALL_CHANNEL_SYMBOL(final String tMALL_CHANNEL_SYMBOL)
	{
		TMALL_CHANNEL_SYMBOL = tMALL_CHANNEL_SYMBOL;
	}

	/**
	 * @param tMALL_SHOP_OTC the tMALL_SHOP_OTC to set
	 */
	public void setTMALL_SHOP_OTC(final String tMALL_SHOP_OTC)
	{
		TMALL_SHOP_OTC = tMALL_SHOP_OTC;
	}

	/**
	 * @param tMALL_SHOP_JSC the tMALL_SHOP_JSC to set
	 */
	public void setTMALL_SHOP_JSC(final String tMALL_SHOP_JSC)
	{
		TMALL_SHOP_JSC = tMALL_SHOP_JSC;
	}

}
