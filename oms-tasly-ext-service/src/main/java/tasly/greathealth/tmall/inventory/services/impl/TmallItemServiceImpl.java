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
package tasly.greathealth.tmall.inventory.services.impl;

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.impl.DefaultAtsService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.services.ItemInfoService;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.tmall.inventory.ItemConstants;
import tasly.greathealth.tmall.inventory.dao.TmallItemDao;
import tasly.greathealth.tmall.inventory.domain.ProductOms;
import tasly.greathealth.tmall.inventory.domain.ProductTm;
import tasly.greathealth.tmall.inventory.exception.ExceptionHandle;
import tasly.greathealth.tmall.inventory.exchange.BuildProductTmConvertor;
import tasly.greathealth.tmall.inventory.file.WriteToFile;
import tasly.greathealth.tmall.inventory.services.TmallItemService;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;
import com.taobao.api.response.ItemQuantityUpdateResponse;
import com.taobao.api.response.SkusQuantityUpdateResponse;


/**
 *
 */
public class TmallItemServiceImpl extends AbstractHybrisService implements TmallItemService
{
	private static final Logger Log = OmsLoggerFactory.getTmallinventorylog();

	private static List<ProductTm> updateFailedList = new ArrayList<ProductTm>();

	private TmallItemDao tmallItemDao;
	private BuildProductTmConvertor buildProductTmConvertor;
	private ExceptionHandle exceptionHandle;
	private DefaultAtsService defaultAtsService;
	private ItemInfoService itemService;
	private InventoryService inventoryService;
	private WriteToFile writeToFile;
	private String FILENAME;
	private String FILEPATH;



	private String tmLocation = ItemConstants.TMALL_LOCATION;
	// tmall otc client and session key
	private TaobaoClient taobaoOtcclient;
	private String OTC_TMALL_STORE_SESSIONKEY;

	// tmall jsc client and session key
	private TaobaoClient taobaoJscclient;
	private String JSC_TMALL_STORE_SESSIONKEY;

	private static int updateFailedDataCount = 0;
	private final int updateFailedLimit = ItemConstants.UPDATEFAILEDLIMIT;
	// tmall shop name
	// private final String otc = "otc";
	// private final String jsc = "jsc";

	private String TMALL_SHOP_OTC;
	private String TMALL_SHOP_JSC;


	@Override
	@Transactional
	public void syncProductsToTm()
	{
		// get invnetory from oms
		final Map<String, List<ProductOms>> posMap = this.getProductsInventoryFromOms();
		// update inventory to tmall
		this.updateInventoryToTmall(this.prepareData(posMap));
		// record failed data
		this.writeFailedDataToFile();
	}

	@Transactional
	public Map<String, List<ProductOms>> getProductsInventoryFromOms()
	{
		Log.info("库存同步:从oms获取商品库存信息...");
		/**
		 * 获取能够同步的商品信息
		 * 只同步自己的商品,不同步第三方或其他
		 * 这里只获取otc
		 */
		// final List<ItemInfoData> iteminfodata = new ArrayList<ItemInfoData>();
		final Map<String, List<ItemInfoData>> iteminfodataMap = new HashMap<String, List<ItemInfoData>>();
		final Map<String, List<ProductOms>> posMap = new HashMap<String, List<ProductOms>>();


		List<ItemInfoData> iteminfoOtc = new ArrayList<ItemInfoData>();
		iteminfoOtc = itemService.getItemInfoByStockManageFlag(ItemConstants.ITEM_STOCKROOM_FLAG, ItemConstants.INNERSOURCEOTC);
		iteminfodataMap.put(ItemConstants.INNERSOURCEOTC, iteminfoOtc);
		List<ItemInfoData> iteminfoJsc = new ArrayList<ItemInfoData>();
		iteminfoJsc = itemService.getItemInfoByStockManageFlag(ItemConstants.ITEM_STOCKROOM_FLAG, ItemConstants.INNERSOURCEJSC);
		iteminfodataMap.put(ItemConstants.INNERSOURCEJSC, iteminfoJsc);

		if (!iteminfodataMap.isEmpty())
		{
			for (final String innerSource : iteminfodataMap.keySet())
			{
				Log.info("渠道:" + innerSource);
				posMap.put(innerSource, this.convertToProductOms(iteminfodataMap.get(innerSource)));
			}
		}
		Log.info("获取oms所有商品库存信息完成");
		return posMap;
	}

	public List<ProductOms> convertToProductOms(final List<ItemInfoData> iteminfodata)
	{
		final String atsId = ItemConstants.ATSID;
		final Set<String> filterSkus = new HashSet<String>();
		final List<ProductOms> oms = new ArrayList<ProductOms>();
		final String skuIds[] = new String[iteminfodata.size()];
		for (int i = 0, j = iteminfodata.size(); i < j; i++)
		{
			skuIds[i] = iteminfodata.get(i).getSku();
		}
		final StockroomLocationData stockRoolLocation = inventoryService.getLocationByLocationId(tmLocation);
		final List<TaslyItemLocationData> itemInfos = itemService.getByLocation(stockRoolLocation, skuIds);
		if (null != itemInfos && !itemInfos.isEmpty())
		{
			Log.info("库存同步:oms商品数量" + itemInfos.size());
			for (final TaslyItemLocationData itemLocationInfoData : itemInfos)
			{
				filterSkus.add(itemLocationInfoData.getItemId());
			}
			// add location addr
			final Set<String> filterLocations = new HashSet<String>();
			filterLocations.add(tmLocation);

			// add available to sell id
			final Set<String> atsIds = new HashSet<String>();
			atsIds.add(atsId);

			final AtsResult atsResult = defaultAtsService.getLocalAts(filterSkus, filterLocations, atsIds);
			for (final String sku : filterSkus)
			{
				final int inventory = atsResult.getResult(sku, atsId, stockRoolLocation.getLocationId().toString());
				final ProductOms po = new ProductOms();
				po.setSkuId(sku);
				po.setNum(Long.valueOf(String.valueOf(inventory)));
				oms.add(po);
			}
			for (final ProductOms pom : oms)
			{
				if (pom.getNum() < 0)
				{
					Log.info("库存小于等于0的商品:" + pom.getSkuId() + "|原库存数量:" + pom.getNum());
					pom.setNum(0L);
					Log.info("库存小于等于0的商品:" + pom.getSkuId() + "|现库存数量:" + pom.getNum());
				}
			}
			if (oms.size() > 0)
			{
				Log.info("库存同步:该渠道封装后商品库存数量" + oms.size() + "|获取该渠道商品库存完成 ...");
			}
		}
		return oms;
	}

	/**
	 * according to the different shop, include otc and jsc
	 * here get the different products info
	 *
	 * @param shopName
	 * @return
	 */
	@Override
	public List<Item> mergeAllProductsFromTmall(final String shopName)
	{
		final List<Item> items = new ArrayList<Item>();

		if (shopName.equals(TMALL_SHOP_OTC))
		{
			items.addAll(this.mergeSingleInnerSourceProductsFromTmall(TMALL_SHOP_OTC, taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY));
		}

		if (shopName.equals(TMALL_SHOP_JSC))
		{
			items.addAll(this.mergeSingleInnerSourceProductsFromTmall(TMALL_SHOP_JSC, taobaoJscclient, JSC_TMALL_STORE_SESSIONKEY));
		}
		return items;
	}

	public List<Item> mergeSingleInnerSourceProductsFromTmall(final String innersource, final TaobaoClient client,
			final String sessionKey)
	{

		final List<Item> items = new ArrayList<Item>();
		List<Item> inStockItem_for_shelved = new ArrayList<Item>();
		List<Item> inStockItem_sold_out = new ArrayList<Item>();
		List<Item> onSaleItem = new ArrayList<Item>();


		inStockItem_for_shelved = tmallItemDao.getInStockInfoFromTmall(client, sessionKey, ItemConstants.DEFAULT_PAGE_NO,
				ItemConstants.DEFAULT_PAGE_SIZE, ItemConstants.FOR_SHELVED);
		Log.info("库存同步:获取未上架的天猫" + innersource + "商品列表-数量:" + inStockItem_for_shelved.size());

		inStockItem_sold_out = tmallItemDao.getInStockInfoFromTmall(client, sessionKey, ItemConstants.DEFAULT_PAGE_NO,
				ItemConstants.DEFAULT_PAGE_SIZE, ItemConstants.SOLD_OUT);
		Log.info("库存同步:获取已售罄的天猫" + innersource + "商品列表-数量:" + inStockItem_sold_out.size());

		onSaleItem = tmallItemDao.getOnSaleInfoFromTmall(client, sessionKey, ItemConstants.DEFAULT_PAGE_NO,
				ItemConstants.DEFAULT_PAGE_SIZE);
		Log.info("库存同步:获取已上架的天猫" + innersource + "商品列表-数量:" + onSaleItem.size());

		if (null != inStockItem_for_shelved)
		{
			items.addAll(inStockItem_for_shelved);
		}
		if (null != inStockItem_sold_out)
		{
			items.addAll(inStockItem_sold_out);
		}
		if (null != onSaleItem)
		{
			items.addAll(onSaleItem);
		}
		return items;

	}

	public List<ProductTm> prepareData(final Map<String, List<ProductOms>> posMap)
	{
		// get the productTm List from memory
		List<ProductTm> tms = new ArrayList<ProductTm>();
		// get all the otc products information from tmall
		Log.info("库存同步:从天猫获取商品列表...");
		for (final String innerSource : posMap.keySet())
		{
			tms = this.convertDataByInnerSource(innerSource, posMap);
		}
		Log.info("转换后所有符合同步条件的商品数量:" + tms.size());
		return tms;
	}

	public List<ProductTm> convertDataByInnerSource(final String innerSource, final Map<String, List<ProductOms>> posMap)
	{
		List<ProductTm> ptms = new ArrayList<ProductTm>();
		final List<Item> items = this.mergeAllProductsFromTmall(innerSource);
		if (!items.isEmpty())
		{
			Log.info("库存同步:天猫" + innerSource + "商品数量" + items.size());
			final Map<String, List<Item>> itemsMap = new HashMap<String, List<Item>>();
			itemsMap.put(innerSource, items);
			// comvert the data to prodcutTm list
			if (null != itemsMap && !itemsMap.isEmpty())
			{
				Log.info("库存同步: 开始转换数据格式...");
				ptms = buildProductTmConvertor.convertData(itemsMap, posMap.get(innerSource));
			}
		}
		Log.info("渠道:" + innerSource + "转换后数量:" + ptms.size());
		return ptms;
	}

	@Override
	public void updateInventoryToTmall(final List<ProductTm> tms)
	{
		Log.debug("库存同步:清空更新失败的内存列表");
		this.getUpdateFailedList().clear();// clear the data which update to tmall failed last time

		for (final ProductTm pt : tms)
		{
			try
			{
				// invoke the method to update the item's quantity in tmall which does not have skus
				if (pt.getShopName().equals(OrderConstants.TMALL_INNER_OTC))
				{
					tmallItemDao.updateStockInfoWithoutSkus(taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY, pt);
				}
				if (pt.getShopName().equals(OrderConstants.TMALL_INNER_JSC))
				{
					tmallItemDao.updateStockInfoWithoutSkus(taobaoJscclient, JSC_TMALL_STORE_SESSIONKEY, pt);
				}

			}
			catch (final ApiException e)
			{
				// handle the update failed data
				exceptionHandle.handleConnectionTimeOutException(pt);
			}
		}
		// update the failed data
		if (null != this.getUpdateFailedList() && !this.getUpdateFailedList().isEmpty())
		{
			if (updateFailedDataCount < updateFailedLimit)
			{
				this.updateFailedDataToTmall();
			}
			else
			{
				updateFailedDataCount = 0;
				Log.info("更新成功. 失败商品数量:" + this.getUpdateFailedList().size());
			}
		}
		else
		{
			Log.info("所有商品更新成功");
		}
	}


	@Override
	public boolean updateItemQuantity(final ProductTm pt)
	{
		ItemQuantityUpdateResponse response = null;
		boolean result = true;
		try
		{
			if (pt.getShopName().equals(OrderConstants.TMALL_INNER_OTC))
			{
				response = tmallItemDao.updateStockInfoWithoutSkus(taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY, pt);
			}
			if (pt.getShopName().equals(OrderConstants.TMALL_INNER_JSC))
			{
				response = tmallItemDao.updateStockInfoWithoutSkus(taobaoJscclient, JSC_TMALL_STORE_SESSIONKEY, pt);
			}
		}
		catch (final ApiException e)
		{
			Log.error(e.getMessage());
			result = false;
		}
		if (null == response || null != response.getSubMsg())
		{
			result = false;
		}
		return result;
	}


	/**
	 * update again for the data which update to tmall failed last time
	 * the failed data saved in the memory
	 */
	public void updateFailedDataToTmall()
	{
		updateFailedDataCount++;
		// get the failed data from memory
		final List<ProductTm> failedData = new ArrayList<ProductTm>();
		failedData.addAll(this.getUpdateFailedList());
		this.getUpdateFailedList().clear();
		if (failedData.size() > 0)
		{
			Log.info("update failed data size:" + failedData.size());

			final List<ProductTm> tempFailedPts = new ArrayList<ProductTm>();
			tempFailedPts.addAll(failedData);
			this.updateInventoryToTmall(tempFailedPts);
		}
		else
		{
			Log.info("update all items success.");
		}
	}

	/**
	 * @return the updateFailedList
	 */
	@Override
	public List<ProductTm> getUpdateFailedList()
	{
		return updateFailedList;
	}


	public void writeFailedDataToFile()
	{
		final List<ProductOms> poms = buildProductTmConvertor.getMatchFailedData();
		final List<ProductTm> ptms = this.getUpdateFailedList();
		final StringBuilder sb = new StringBuilder();
		sb.append("--------------------------match failed list----------------------");
		sb.append(System.getProperty("line.separator"));
		for (final ProductOms p : poms)
		{
			sb.append("oms-sku:" + p.getSkuId() + "|oms-sku-num:" + p.getNum());
			sb.append(System.getProperty("line.separator"));
		}
		sb.append("--------------------------update failed list----------------------");
		sb.append(System.getProperty("line.separator"));
		for (final ProductTm p : ptms)
		{
			sb.append("oms-sku:" + p.getOuterId() + "|tmall num_iid" + p.getTmProductId() + "|oms-sku-num:" + p.getTmFreeQuntity());
			sb.append(System.getProperty("line.separator"));
		}
		try
		{
			writeToFile.createFile(FILEPATH, FILENAME, sb);
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	@Override
	public Item getItemFromTm(final String shopName, final Long num_iid)
	{
		Item item = new Item();
		if (shopName.equals(OrderConstants.TMALL_INNER_OTC))
		{
			item = tmallItemDao.getItem(taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY, num_iid);
		}
		return item;
	}

	@Override
	public boolean updateInventoryToTmallWithSkus(final ProductTm tms)
	{
		boolean flag = true;
		if (tms.getShopName().equals(OrderConstants.TMALL_INNER_OTC))
		{
			try
			{
				final SkusQuantityUpdateResponse response = tmallItemDao.updateStockInfoWithSkus(taobaoOtcclient,
						OTC_TMALL_STORE_SESSIONKEY, tms);
				if (null != response.getSubCode())
				{
					if ("isv.item-quantity-item-update-service-error-tmall".equals(response.getSubCode()))
					{
						Log.info("更新带有sku属性的标准套餐失败");
					}
					flag = false;
				}
			}
			catch (final ApiException e)
			{
				exceptionHandle.handleConnectionTimeOutException(tms);
			}
		}
		return flag;
	}

	@Override
	public boolean updateComboInventoryToTmall(final ProductTm tm)
	{
		boolean flag = true;
		if (tm.getShopName().equals(OrderConstants.TMALL_INNER_OTC))
		{
			flag = this.updateComboInventory(flag, tm, taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY);
		}

		if (tm.getShopName().equals(OrderConstants.TMALL_INNER_JSC))
		{
			flag = this.updateComboInventory(flag, tm, taobaoJscclient, JSC_TMALL_STORE_SESSIONKEY);
		}
		return flag;
	}

	public boolean updateComboInventory(boolean flag, final ProductTm tm, final TaobaoClient client, final String sessionKey)
	{
		try
		{
			if (tm.isCombo())
			{
				Log.info("更新方式:sku更新");
				final SkusQuantityUpdateResponse skusQuantityUpdateResponse = tmallItemDao.updateStockInfoWithSingleSkus(client,
						sessionKey, tm);
				if (null != skusQuantityUpdateResponse.getSubCode())
				{
					flag = false;
				}
			}
			else
			{
				Log.info("更新方式:item更新");
				final ItemQuantityUpdateResponse itemQuantityUpdateResponse = tmallItemDao.updateStockInfoWithoutSkus(client,
						sessionKey, tm);
				if (null != itemQuantityUpdateResponse.getSubCode())
				{
					flag = false;
				}
			}
		}
		catch (final ApiException e)
		{
			exceptionHandle.handleConnectionTimeOutException(tm);
		}
		return flag;
	}





	@Override
	public boolean updateSingleSkuInventoryToTmall(final ProductTm tm)
	{
		boolean flag = true;

		SkusQuantityUpdateResponse skusQuantityUpdateResponse = null;
		try
		{
			if (tm.getShopName().equals(OrderConstants.TMALL_INNER_OTC))
			{
				skusQuantityUpdateResponse = tmallItemDao.updateStockInfoWithSingleSkus(taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY,
						tm);
			}

			if (tm.getShopName().equals(OrderConstants.TMALL_INNER_JSC))
			{
				skusQuantityUpdateResponse = tmallItemDao.updateStockInfoWithSingleSkus(taobaoJscclient, JSC_TMALL_STORE_SESSIONKEY,
						tm);
			}
			if (null != skusQuantityUpdateResponse && null != skusQuantityUpdateResponse.getSubCode())
			{
				flag = false;
			}
		}
		catch (final ApiException e)
		{
			exceptionHandle.handleConnectionTimeOutException(tm);
		}
		return flag;
	}

	@Override
	public List<ProductTm> updateProductTmQuntityInMemory(final List<ProductTm> pts, final List<ProductOms> pos)
	{
		// clear the failed msg generated last sync time
		buildProductTmConvertor.getMatchFailedData().clear();

		final Map<String, ProductTm> ptmMap = new HashMap<String, ProductTm>();
		for (final ProductTm pt : pts)
		{
			ptmMap.put(pt.getOuterId(), pt);
		}

		for (final ProductOms po : pos)
		{
			final ProductTm pt = ptmMap.get(po.getSkuId());
			if (null != pt)
			{
				if (null != pt.getSkus())
				{
					for (final Sku sku : pt.getSkus())
					{
						if (sku.getIid() == po.getSkuId())
						{
							sku.setQuantity(Long.valueOf(po.getNum()));
						}
						else
						{
							buildProductTmConvertor.getMatchFailedData().add(po);
						}
					}
				}
			}
			else
			{
				buildProductTmConvertor.getMatchFailedData().add(po);
			}
		}

		if (null != buildProductTmConvertor.getMatchFailedData() && buildProductTmConvertor.getMatchFailedData().size() > 0)
		{
			exceptionHandle.handleDataMappingException();
		}

		return pts;
	}

	/**************************************************
	 * ----------------get set method---------------- *
	 *************************************************/
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
	 * @return the exceptionHandle
	 */
	public ExceptionHandle getExceptionHandle()
	{
		return exceptionHandle;
	}

	/**
	 * @param exceptionHandle the exceptionHandle to set
	 */
	public void setExceptionHandle(final ExceptionHandle exceptionHandle)
	{
		this.exceptionHandle = exceptionHandle;
	}

	/**
	 * @return the defaultAtsService
	 */
	public DefaultAtsService getDefaultAtsService()
	{
		return defaultAtsService;
	}

	/**
	 * @param defaultAtsService the defaultAtsService to set
	 */
	public void setDefaultAtsService(final DefaultAtsService defaultAtsService)
	{
		this.defaultAtsService = defaultAtsService;
	}



	/**
	 * @return the itemService
	 */
	public ItemInfoService getItemService()
	{
		return itemService;
	}



	/**
	 * @param itemService the itemService to set
	 */
	public void setItemService(final ItemInfoService itemService)
	{
		this.itemService = itemService;
	}



	/**
	 * @return the inventoryService
	 */
	public InventoryService getInventoryService()
	{
		return inventoryService;
	}



	/**
	 * @param inventoryService the inventoryService to set
	 */
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}


	/**
	 * @param tmLocation the tmLocation to set
	 */
	public void setTmLocation(final String tmLocation)
	{
		this.tmLocation = tmLocation;
	}

	/**
	 * @return the taobaoOtcclient
	 */
	public TaobaoClient getTaobaoOtcclient()
	{
		return taobaoOtcclient;
	}

	/**
	 * @param taobaoOtcclient the taobaoOtcclient to set
	 */
	public void setTaobaoOtcclient(final TaobaoClient taobaoOtcclient)
	{
		this.taobaoOtcclient = taobaoOtcclient;
	}

	/**
	 * @return the oTC_TMALL_STORE_SESSIONKEY
	 */
	public String getOTC_TMALL_STORE_SESSIONKEY()
	{
		return OTC_TMALL_STORE_SESSIONKEY;
	}

	/**
	 * @param oTC_TMALL_STORE_SESSIONKEY the oTC_TMALL_STORE_SESSIONKEY to set
	 */
	public void setOTC_TMALL_STORE_SESSIONKEY(final String oTC_TMALL_STORE_SESSIONKEY)
	{
		OTC_TMALL_STORE_SESSIONKEY = oTC_TMALL_STORE_SESSIONKEY;
	}

	/**
	 * @return the taobaoJscclient
	 */
	public TaobaoClient getTaobaoJscclient()
	{
		return taobaoJscclient;
	}

	/**
	 * @param taobaoJscclient the taobaoJscclient to set
	 */
	public void setTaobaoJscclient(final TaobaoClient taobaoJscclient)
	{
		this.taobaoJscclient = taobaoJscclient;
	}

	/**
	 * @return the jSC_TMALL_STORE_SESSIONKEY
	 */
	public String getJSC_TMALL_STORE_SESSIONKEY()
	{
		return JSC_TMALL_STORE_SESSIONKEY;
	}

	/**
	 * @param jSC_TMALL_STORE_SESSIONKEY the jSC_TMALL_STORE_SESSIONKEY to set
	 */
	public void setJSC_TMALL_STORE_SESSIONKEY(final String jSC_TMALL_STORE_SESSIONKEY)
	{
		JSC_TMALL_STORE_SESSIONKEY = jSC_TMALL_STORE_SESSIONKEY;
	}

	/**
	 * @return the tmallItemDao
	 */
	public TmallItemDao getTmallItemDao()
	{
		return tmallItemDao;
	}

	/**
	 * @param tmallItemDao the tmallItemDao to set
	 */
	public void setTmallItemDao(final TmallItemDao tmallItemDao)
	{
		this.tmallItemDao = tmallItemDao;
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

	/**
	 * @param writeToFile the writeToFile to set
	 */
	public void setWriteToFile(final WriteToFile writeToFile)
	{
		this.writeToFile = writeToFile;
	}

	/**
	 * @param fILENAME the fILENAME to set
	 */
	public void setFILENAME(final String fILENAME)
	{
		FILENAME = fILENAME;
	}

	/**
	 * @param fILEPATH the fILEPATH to set
	 */
	public void setFILEPATH(final String fILEPATH)
	{
		FILEPATH = fILEPATH;
	}

	@Override
	public Item getItemFromTmByOuterId(final String shopName, final String outerId)
	{
		Item item = new Item();
		if (shopName.equals(OrderConstants.TMALL_INNER_OTC))
		{
			item = tmallItemDao.getItemByOuterId(taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY, outerId);
		}
		if (shopName.equals(OrderConstants.TMALL_INNER_JSC))
		{
			item = tmallItemDao.getItemByOuterId(taobaoJscclient, JSC_TMALL_STORE_SESSIONKEY, outerId);
		}
		return item;
	}

	@Override
	public Sku getSkuFromTmByOuterId(final String shopName, final String outerId)
	{
		Sku sku = new Sku();
		if (shopName.equals(OrderConstants.TMALL_INNER_OTC))
		{
			sku = tmallItemDao.getSkuByOuterId(taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY, outerId);
		}
		if (shopName.equals(OrderConstants.TMALL_INNER_JSC))
		{
			sku = tmallItemDao.getSkuByOuterId(taobaoJscclient, JSC_TMALL_STORE_SESSIONKEY, outerId);
		}
		return sku;
	}

}
