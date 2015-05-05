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
package tasly.greathealth.thirdparty.inventory.convertor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.tamll.api.SkuToProductFacade;
import tasly.greathealth.thirdparty.inventory.beans.StoreSku;
import tasly.greathealth.thirdparty.inventory.convertor.StoreSkuConvertor;
import tasly.greathealth.tmall.inventory.domain.ProductOms;
import tasly.greathealth.tmall.inventory.domain.ProductTm;

import com.taobao.api.domain.Item;


/**
 *
 */
public class AbstractStoreSkuConvertor implements StoreSkuConvertor
{
	protected String TMALL_CHANNEL_SYMBOL;
	protected String channel;
	protected String innerSource;

	/**
	 * @return the channel
	 */
	public String getChannel()
	{
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(final String channel)
	{
		this.channel = channel;
	}

	/**
	 * @return the innerSource
	 */
	public String getInnerSource()
	{
		return innerSource;
	}

	/**
	 * @param innerSource the innerSource to set
	 */
	public void setInnerSource(final String innerSource)
	{
		this.innerSource = innerSource;
	}

	protected SkuToProductFacade skuToProductFacade;



	/**
	 * @return the skuToProductFacade
	 */
	public SkuToProductFacade getSkuToProductFacade()
	{
		return skuToProductFacade;
	}

	/**
	 * @param skuToProductFacade the skuToProductFacade to set
	 */
	public void setSkuToProductFacade(final SkuToProductFacade skuToProductFacade)
	{
		this.skuToProductFacade = skuToProductFacade;
	}

	public Map<String, StoreSku> itemMap = new HashMap<String, StoreSku>();
	public static List<StoreSku> storeSkus = new ArrayList<StoreSku>();
	public static List<StoreSku> matchFailedSkus = new ArrayList<StoreSku>();

	@Override
	public List<StoreSku> convertData(final List<StoreSku> skus, final List<ProductOms> omsProducts)
	{

		// 做数据匹配，在这里以网站的所有商品为准，将本地的数据形成map，然后去查map里存不存在

		// 转成map
		final Map<String, ProductOms> omsItems = new HashMap<String, ProductOms>();
		for (final ProductOms p : omsProducts)
		{
			final String outerid = p.getSkuId();
			if (outerid != null && outerid.length() > 0)
			{
				omsItems.put(outerid, p);
			}
		}
		// Log.info("库存同步:最终筛选后的oms单品数量为:" + omsItems.keySet().size());
		// 首先匹配取出来的单品的数据，如果匹配不成功的放到一个新的map里面
		for (final StoreSku sku : skus)
		{
			final String outerid = sku.getOuterid();
			if (outerid == null || outerid.length() == 0)
			{
				continue;
			}
			if (omsItems.containsKey(outerid))
			{
				// 存在相同的,放到最终的集合里
				final ProductOms oms = omsItems.get(outerid);
				sku.setStockQuntity(oms.getNum());
				storeSkus.add(sku);
			}
			else
			{
				// 如果不存在,则将这个放到匹配不成功的集合里，下一步配对
				this.recordMatchFailedData(sku);
			}

		}
		// 其次匹配套餐的，如果还匹配不上则为最终不可用的
		// getDataFromSkuToProduct(omsItems);
		return storeSkus;
	}

	public void getDataFromSkuToProduct(final Map<String, ProductOms> itemMap)
	{
		final List<SkuToProduct> middles = skuToProductFacade.querySkuToProduct(channel, innerSource);
		if (middles == null || middles.size() == 0)
		{
			return;
		}
		for (int i = matchFailedSkus.size() - 1; i > -1; i--)
		{
			final StoreSku sku = matchFailedSkus.get(i);
			final String outerid1 = sku.getOuterid();
			for (final SkuToProduct mid : middles)
			{
				final String outerid2 = mid.getOuterId();
				if (outerid1 != null && outerid2 != null && outerid1.equals(outerid2))
				{
					final int amout = mid.getQuantity();
					final String skuid = mid.getItemId();
					final ProductOms oms = itemMap.get(skuid);

					if (amout != 0 && oms != null)
					{
						final long d = oms.getNum();
						final Long stockQuantity = d / amout;
						sku.setStockQuntity(stockQuantity);
						storeSkus.add(sku);
						matchFailedSkus.remove(i);
					}

				}

			}
		}
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

	public SkuToProduct validateIfExistInSkuRelation(final String shopname, final String itemId)
	{
		// List<SkuToProduct> skuToProduct = new ArrayList<SkuToProduct>();
		// if (shopname.equals(TMALL_SHOP_JSC))
		// {
		// skuToProduct = (List<SkuToProduct>) skuToProductFacade.querySkuToProductByitemId(itemId, TMALL_CHANNEL_SYMBOL,
		// TMALL_SHOP_JSC);
		// if (null != skuToProduct && skuToProduct.size() == 1)
		// {
		// return skuToProduct.get(0);
		// }
		// }
		// if (shopname.equals(TMALL_SHOP_OTC))
		// {
		// skuToProduct = (List<SkuToProduct>) skuToProductFacade.querySkuToProductByitemId(itemId, TMALL_CHANNEL_SYMBOL,
		// TMALL_SHOP_OTC);
		// if (null != skuToProduct && skuToProduct.size() == 1)
		// {
		// return skuToProduct.get(0);
		// }
		// }
		return null;
	}

	/**
	 * @return the itemMap
	 */
	public Map<String, StoreSku> getItemMap()
	{
		return itemMap;
	}

	/**
	 * @param itemMap the itemMap to set
	 */
	public void setItemMap(final Map<String, StoreSku> itemMap)
	{
		this.itemMap = itemMap;
	}

	/**
	 * @return the storeSkus
	 */
	public static List<StoreSku> getStoreSkus()
	{
		return storeSkus;
	}

	/**
	 * @param storeSkus the storeSkus to set
	 */
	public static void setStoreSkus(final List<StoreSku> storeSkus)
	{
		AbstractStoreSkuConvertor.storeSkus = storeSkus;
	}

	/**
	 * @return the matchFailedSkus
	 */
	public static List<StoreSku> getMatchFailedSkus()
	{
		return matchFailedSkus;
	}

	/**
	 * @param matchFailedSkus the matchFailedSkus to set
	 */
	public static void setMatchFailedSkus(final List<StoreSku> matchFailedSkus)
	{
		AbstractStoreSkuConvertor.matchFailedSkus = matchFailedSkus;
	}

	@Override
	public List<StoreSku> getListFromInternalMemory()
	{
		return storeSkus;
	}

	@Override
	public List<StoreSku> getMatchFailedData()
	{
		return matchFailedSkus;
	}

	/**
	 * record match faild data
	 *
	 * @param sku
	 */
	public void recordMatchFailedData(final StoreSku sku)
	{
		matchFailedSkus.add(sku);
	}


}
