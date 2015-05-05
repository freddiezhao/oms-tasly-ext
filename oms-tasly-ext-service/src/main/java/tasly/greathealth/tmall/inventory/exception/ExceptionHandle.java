/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.tmall.inventory.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.inventory.domain.ProductOms;
import tasly.greathealth.tmall.inventory.domain.ProductTm;
import tasly.greathealth.tmall.inventory.exchange.BuildProductTmConvertor;
import tasly.greathealth.tmall.inventory.services.TmallItemService;

import com.taobao.api.domain.Item;


/**
 *
 */
public class ExceptionHandle
{
	private static final Logger Log = OmsLoggerFactory.getTmallinventorylog();

	private TmallItemService tmallItemService;
	private BuildProductTmConvertor buildProductTmConvertor;
	private String TMALL_SHOP_OTC;
	private String TMALL_SHOP_JSC;

	/**
	 * handle update failed exception
	 *
	 * @param subcode
	 * @param p
	 */
	public void handleException(final String subcode, final ProductTm p)
	{
		switch (subcode)
		{
			case "isv.invalid-parameter:quantity-tmall":
				Log.error("商品编号:" + p.getOuterId() + "库存数量不能为负数");
				break;
			case "isv.item-not-exist:invalid-numIid-or-iid-tmall":
				Log.error("商品编号:" + p.getOuterId() + "商品id对应的商品不存在");
				break;
			case "isv.item-is-delete:invalid-numIid-or-iid-tmall":
				Log.error("商品编号:" + p.getOuterId() + "商品id对应的商品已经被删除");
				break;
			case "isv.item-quantity-item-update-service-error-tmall":
				Log.error("商品编号:" + p.getOuterId() + "商品库存更新失败,可能含有销售属性");
				Log.info("库存同步:更新带有sku编码的单品商品");
				tmallItemService.updateSingleSkuInventoryToTmall(p);
				break;
			case "isv.error-inventory-invalid-item-quantity-tmall":
				Log.error("商品编号:" + p.getOuterId() + "宝贝数量 必须大于预扣库存,否则会导致拍卖下未付款的卖家,付款失败");
				break;
			default:
				Log.error("system error,please contact the developers");
				break;
		}
	}


	// public boolean syncItemWithSku(ProductTm p)
	// {
	// Log.info("获取itemsku信息，更新带有sku销售属性的套餐");
	// boolean flag = false;
	// Item item = tmallItemService.getItemFromTm(p.getShopName(), p.getTmProductId());
	// if (null != item && null != item.getSkus() && !item.getSkus().isEmpty())
	// {
	// List<Sku> skus = item.getSkus();
	// p.setSkus(skus);
	// for (Sku sku : skus)
	// {
	// if (sku.getOuterId().equals(p.getOuterId()))
	// {
	// Log.info("成功匹配套餐sku属性");
	// p.setCombo(true);
	// flag = tmallItemService.updateInventoryToTmallWithSkus(p);
	// if (flag)
	// {
	// Log.debug("sku套餐更新成功");
	// }
	// else
	// {
	// Log.info("skuId:" + sku.getOuterId() + "|sku套餐更新失败");
	// }
	// }
	// }
	// }
	// return flag;
	// }







	/**
	 * handle dataMappding exception
	 */
	public void handleDataMappingException()
	{
		final List<ProductOms> fpos = buildProductTmConvertor.getMatchFailedData();

		// get all the otc products information from tmall
		final List<Item> otcItems = tmallItemService.mergeAllProductsFromTmall(TMALL_SHOP_OTC);

		// get all the jsc products information from tmall
		// final List<Item> jscItems = tmallItemService.mergeAllProductsFromTmall(TMALL_SHOP_JSC);

		final Map<String, List<Item>> itemsMap = new HashMap<String, List<Item>>();
		itemsMap.put(TMALL_SHOP_OTC, otcItems);
		// itemsMap.put(TMALL_SHOP_JSC, jscItems);

		if (null != fpos && !fpos.isEmpty())
		{
			buildProductTmConvertor.convertData(itemsMap, fpos);
		}
	}

	/**
	 * 处理内存中的商品在天猫删除或不存在
	 */
	public void removeIsNotExistOrDelItemInfo(final String outerId)
	{
		final List<ProductTm> tms = buildProductTmConvertor.getListFromInternalMemory();
		final Map<String, ProductTm> ptmMap = new HashMap<String, ProductTm>();
		for (final ProductTm ptm : tms)
		{
			ptmMap.put(ptm.getOuterId(), ptm);
		}
		if (null != ptmMap.get(outerId))
		{
			tms.remove(ptmMap.get(outerId));
		}
	}

	/**
	 * time out exception
	 */
	public void handleConnectionTimeOutException(final ProductTm p)
	{
		tmallItemService.getUpdateFailedList().add(p);
	}


	/**
	 * print Log
	 *
	 * @param pos
	 */
	public void logMatchFailedData(final List<ProductOms> pos)
	{
		for (final ProductOms po : pos)
		{
			Log.info(po.getSkuId());
		}
	}

	/**
	 * @return the tmallItemService
	 */
	public TmallItemService getTmallItemService()
	{
		return tmallItemService;
	}

	/**
	 * @param tmallItemService the tmallItemService to set
	 */
	public void setTmallItemService(final TmallItemService tmallItemService)
	{
		this.tmallItemService = tmallItemService;
	}

	/**
	 * @return the buildProductTmConvertor
	 */
	public BuildProductTmConvertor getBuildProductTmConvertor()
	{
		return buildProductTmConvertor;
	}

	/**
	 * @param buildProductTmConvertor the buildProductTmConvertor to set
	 */
	public void setBuildProductTmConvertor(final BuildProductTmConvertor buildProductTmConvertor)
	{
		this.buildProductTmConvertor = buildProductTmConvertor;
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
