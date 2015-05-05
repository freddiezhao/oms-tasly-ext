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
package tasly.greathealth.thirdparty.inventory.services.impl;

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
import tasly.greathealth.thirdparty.inventory.beans.StoreSku;
import tasly.greathealth.thirdparty.inventory.convertor.StoreSkuConvertor;
import tasly.greathealth.thirdparty.inventory.dao.StoreItemDao;
import tasly.greathealth.thirdparty.inventory.exception.StoreExceptionHandle;
import tasly.greathealth.thirdparty.inventory.services.UpdateItemService;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.tmall.inventory.ItemConstants;
import tasly.greathealth.tmall.inventory.domain.ProductOms;
import tasly.greathealth.tmall.inventory.domain.ProductTm;
import tasly.greathealth.tmall.inventory.file.WriteToFile;


/**
 *
 */
public class AbstractStoreItemService extends AbstractHybrisService implements UpdateItemService
{
	protected static final Logger Log = OmsLoggerFactory.getJdinventorylog();

	protected String innerSource;

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

	/**
	 * @return the storeItemDao
	 */
	public StoreItemDao getStoreItemDao()
	{
		return storeItemDao;
	}

	/**
	 * @param storeItemDao the storeItemDao to set
	 */
	public void setStoreItemDao(final StoreItemDao storeItemDao)
	{
		this.storeItemDao = storeItemDao;
	}

	/**
	 * @return the storeItemService
	 */
	public UpdateItemService getStoreItemService()
	{
		return storeItemService;
	}

	/**
	 * @param storeItemService the storeItemService to set
	 */
	public void setStoreItemService(final UpdateItemService storeItemService)
	{
		this.storeItemService = storeItemService;
	}

	protected static List<StoreSku> updateFailedList = new ArrayList<StoreSku>();
	protected StoreItemDao storeItemDao;
	protected StoreSkuConvertor storeSkuConvertor;
	protected StoreExceptionHandle exceptionHandle;
	protected DefaultAtsService defaultAtsService;
	protected ItemInfoService itemService;
	protected InventoryService inventoryService;
	protected WriteToFile writeToFile;
	protected String FILENAME;

	/**
	 * @param updateFailedList the updateFailedList to set
	 */
	public static void setUpdateFailedList(final List<StoreSku> updateFailedList)
	{
		AbstractStoreItemService.updateFailedList = updateFailedList;
	}

	protected String FILEPATH;


	protected static int updateFailedDataCount = 0;
	protected final int updateFailedLimit = ItemConstants.UPDATEFAILEDLIMIT;
	protected UpdateItemService storeItemService;

	protected String innerSource_otc;
	protected String innerSource_jsc;
	protected String channel;

	/**
	 * @param innerSource_jsc the innerSource_jsc to set
	 */
	public void setInnerSource_jsc(final String innerSource_jsc)
	{
		this.innerSource_jsc = innerSource_jsc;
	}

	/**
	 * @return the innerSource_jsc
	 */
	public String getInnerSource_jsc()
	{
		return innerSource_jsc;
	}

	/**
	 * @param innerSource_otc the innerSource_otc to set
	 */
	public void setInnerSource_otc(final String innerSource_otc)
	{
		this.innerSource_otc = innerSource_otc;
	}

	/**
	 * @return the innerSource_otc
	 */
	public String getInnerSource_otc()
	{
		return innerSource_otc;
	}

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

	@Override
	@Transactional
	public void syncInventoryToStore()
	{
		// get invnetory from oms
		List<ProductOms> pos = new ArrayList<ProductOms>();
		pos = this.getProductsInventoryFromOms();
		// update inventory to store
		this.updateInventoryToStore(this.prepareData(pos));
		// record failed data
		this.writeFailedDataToFile();
	}

	/**
	 * 获取产品库中，相关的所有产品数据
	 *
	 * @return
	 */
	@Transactional
	public List<ProductOms> getProductsInventoryFromOms()
	{
		Log.info("库存同步:从oms获取商品库存信息...");
		final String atsId = ItemConstants.ATSID;
		final Set<String> filterSkus = new HashSet<String>();
		final List<ProductOms> oms = new ArrayList<ProductOms>();
		/**
		 * 获取能够同步的商品信息
		 * 只同步自己的商品,不同步第三方或其他
		 * 这里只获取otc
		 */
		final List<ItemInfoData> iteminfodata = itemService.getItemInfoByStockManageFlag(ItemConstants.ITEM_STOCKROOM_FLAG,
				innerSource);
		if (!iteminfodata.isEmpty())
		{
			final String skuIds[] = new String[iteminfodata.size()];
			for (int i = 0, j = iteminfodata.size(); i < j; i++)
			{
				skuIds[i] = iteminfodata.get(i).getSku();
			}

			final StockroomLocationData stockRoolLocation = inventoryService.getLocationByLocationId(channel);
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
				filterLocations.add(getChannel());

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
					Log.info("库存同步:封装后商品库存数量" + oms.size() + "|获取所有的商品库存完成 ...");
				}
			}
		}
		return oms;
	}

	/**
	 * according to the different shop, include otc and jsc
	 * here get the different products info
	 *
	 * @return
	 */
	@Override
	public List<StoreSku> getItemsFromStore()
	{
		final List<StoreSku> items = new ArrayList<StoreSku>();
		List<StoreSku> inStockItem_for_shelved = new ArrayList<StoreSku>();
		final List<StoreSku> inStockItem_sold_out = new ArrayList<StoreSku>();
		final List<StoreSku> onSaleItem = new ArrayList<StoreSku>();
		// if (shopName.equals(""))// @Todo
		// {
		Log.info("库存同步:获取未上架的商品列表");
		inStockItem_for_shelved = storeItemDao.getInStockItemsFromStore();
		// Log.info("库存同步:获取已售罄的商品列表");
		// inStockItem_sold_out = storeItemDao.getDownStockItemsFromStore();
		// Log.info("库存同步:获取已上架的商品列表");
		// onSaleItem = storeItemDao.getOnSaleItemsFromStore();
		// }

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

	@Override
	public List<StoreSku> prepareData(final List<ProductOms> pos)
	{
		// get the productTm List from memory
		final List<StoreSku> tms = storeSkuConvertor.getListFromInternalMemory();
		tms.clear();
		List<StoreSku> skus = new ArrayList<StoreSku>();
		/**
		 * if the memory has data, update the quantity directly
		 * else convert the data
		 */
		if (tms.isEmpty())
		{
			// get all the otc products information from tmall
			Log.info("库存同步:获取商品列表...");
			final List<StoreSku> otcItems = this.getItemsFromStore();

			if (!otcItems.isEmpty())
			{
				Log.info("库存同步:otc商品数量" + otcItems.size());
				// final Map<String, List<Sku>> itemsMap = new HashMap<String, List<Sku>>();
				// itemsMap.put(storeName, otcItems);

				// comvert the data to prodcutTm list
				Log.info("库存同步: 开始转换数据格式...");
				skus = storeSkuConvertor.convertData(otcItems, pos);
			}
		}
		return skus;
	}

	@Override
	public void updateInventoryToStore(final List<StoreSku> skus)
	{
		Log.debug("库存同步:清空更新失败的内存列表");
		this.getUpdateFailedList().clear();// clear the data which update to store failed last time
		Log.debug("库存同步:开始更新库存");
		for (final StoreSku sku : skus)
		{
			try
			{
				storeItemDao.updateStockInfoBySku(sku);
			}
			catch (final Exception e)
			{
				// handle the update failed data
				exceptionHandle.handleConnectionTimeOutException(sku);
			}
		}
		// update the failed data
		if (null != this.getUpdateFailedList() && !this.getUpdateFailedList().isEmpty())
		{
			if (updateFailedDataCount < updateFailedLimit)
			{
				this.updateFailedDataToStore();
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
		boolean result = true;
		try
		{
			// result = storeItemDao.updateStockInfoWithoutSkus(pt);
		}
		catch (final Exception e)
		{
			Log.error(e.getMessage());
			result = false;
		}
		return result;
	}


	/**
	 * update again for the data which update to tmall failed last time
	 * the failed data saved in the memory
	 */
	public void updateFailedDataToStore()
	{
		updateFailedDataCount++;
		// get the failed data from memory
		final List<StoreSku> failedData = new ArrayList<StoreSku>();
		failedData.addAll(this.getUpdateFailedList());
		this.getUpdateFailedList().clear();
		if (failedData.size() > 0)
		{
			Log.info("update failed data size:" + failedData.size());

			final List<StoreSku> tempFailedPts = new ArrayList<StoreSku>();
			tempFailedPts.addAll(failedData);
			this.updateInventoryToStore(tempFailedPts);
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
	public List<StoreSku> getUpdateFailedList()
	{
		return updateFailedList;
	}


	public void writeFailedDataToFile()
	{
		final List<StoreSku> poms = storeSkuConvertor.getMatchFailedData();
		if (poms == null)
		{
			Log.info("没有需要更新的失败数据");
			return;
		}
		final List<StoreSku> failedSkus = this.getUpdateFailedList();
		final StringBuilder sb = new StringBuilder();
		sb.append("--------------------------match failed list----------------------");
		sb.append(System.getProperty("line.separator"));
		for (final StoreSku p : poms)
		{
			sb.append("oms-sku:" + p.getSkuid() + "|oms-sku-num:" + p.getOuterid());
			sb.append(System.getProperty("line.separator"));
		}
		sb.append("--------------------------update failed list----------------------");
		sb.append(System.getProperty("line.separator"));
		for (final StoreSku p : failedSkus)
		{
			sb.append("oms-sku:" + p.getOuterid() + "|tmall num_iid" + p.getShopName() + "|oms-sku-num:" + p.getStockQuntity());
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
	public boolean updateInventoryToStoreWithSkus(final ProductTm tms)
	{
		final boolean flag = true;
		if (tms.getShopName().equals(OrderConstants.TMALL_INNER_OTC))
		{
			// try
			// {
			// final SkusQuantityUpdateResponse response = storeItemDao.updateStockInfoWithSkus(taobaoOtcclient,
			// OTC_TMALL_STORE_SESSIONKEY, tms);
			// if (null != response.getSubCode())
			// {
			// if ("isv.item-quantity-item-update-service-error-tmall".equals(response.getSubCode()))
			// {
			// Log.info("更新带有sku属性的标准套餐失败");
			// }
			// flag = false;
			// }
			// }
			// catch (final ApiException e)
			// {
			// exceptionHandle.handleConnectionTimeOutException(tms);
			// }
		}
		return flag;
	}

	@Override
	public boolean updateComboInventoryToStore(final ProductTm tm)
	{
		final boolean flag = true;
		if (tm.getShopName().equals(OrderConstants.TMALL_INNER_OTC))
		{
			// try
			// {
			// if (tm.isCombo())
			// {
			// Log.info("更新方式:sku更新");
			// final SkusQuantityUpdateResponse skusQuantityUpdateResponse = storeItemDao.updateStockInfoWithSingleSkus(
			// taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY, tm);
			// if (null != skusQuantityUpdateResponse.getSubCode())
			// {
			// flag = false;
			// }
			// }
			// else
			// {
			// Log.info("更新方式:item更新");
			// final ItemQuantityUpdateResponse itemQuantityUpdateResponse = storeItemDao.updateStockInfoWithoutSkus(
			// taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY, tm);
			// if (null != itemQuantityUpdateResponse.getSubCode())
			// {
			// flag = false;
			// }
			// }
			// }
			// catch (final ApiException e)
			// {
			// exceptionHandle.handleConnectionTimeOutException(tm);
			// }
		}
		return flag;
	}

	@Override
	public boolean updateStockInfoBySku(final StoreSku sku)
	{
		return storeItemDao.updateStockInfoBySku(sku);
	}

	@Override
	public List<ProductTm> updateProductTmQuntityInMemory(final List<ProductTm> pts, final List<ProductOms> pos)
	{
		// clear the failed msg generated last sync time
		storeSkuConvertor.getMatchFailedData().clear();

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
					// for (final Sku sku : pt.getSkus())
					// {
					// if (sku.getIid() == po.getSkuId())
					// {
					// sku.setQuantity(Long.valueOf(po.getNum()));
					// }
					// else
					// {
					// storeSkuConvertor.getMatchFailedData().add(po);
					// }
					// }
				}
			}
			else
			{
				// storeSkuConvertor.getMatchFailedData().add(po);
			}
		}

		if (null != storeSkuConvertor.getMatchFailedData() && storeSkuConvertor.getMatchFailedData().size() > 0)
		{
			exceptionHandle.handleDataMappingException();
		}

		return pts;
	}

	/**************************************************
	 * ----------------get set method---------------- *
	 *************************************************/

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
	 * @return the exceptionHandle
	 */
	public StoreExceptionHandle getExceptionHandle()
	{
		return exceptionHandle;
	}

	/**
	 * @param exceptionHandle the exceptionHandle to set
	 */
	public void setExceptionHandle(final StoreExceptionHandle exceptionHandle)
	{
		this.exceptionHandle = exceptionHandle;
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
	 * @return the storeItemDao
	 */
	public StoreItemDao getstoreItemDao()
	{
		return storeItemDao;
	}

	/**
	 * @param storeItemDao the storeItemDao to set
	 */
	public void setstoreItemDao(final StoreItemDao storeItemDao)
	{
		this.storeItemDao = storeItemDao;
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
	public StoreSku getStoreSkuFromStore(final String outerid)
	{
		return storeItemDao.getStoreSkuByOuterId(outerid);
	}

	/**
	 * @return the storeSkuConvertor
	 */
	public StoreSkuConvertor getStoreSkuConvertor()
	{
		return storeSkuConvertor;
	}

	/**
	 * @param storeSkuConvertor the storeSkuConvertor to set
	 */
	public void setStoreSkuConvertor(final StoreSkuConvertor storeSkuConvertor)
	{
		this.storeSkuConvertor = storeSkuConvertor;
	}
}
