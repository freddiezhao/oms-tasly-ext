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
package tasly.greathealth.oms.inventory.services.impl;

import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.inventory.ItemInfoQueryObject;
import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.inventory.domain.InventoryDomainInnerSource;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.domain.TaslyStockroomLocationData;
import tasly.greathealth.oms.inventory.services.ItemInfoService;
import tasly.greathealth.oms.inventory.services.StockroomLocationService;
import tasly.greathealth.oms.inventory.services.TaslyItemLocationService;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * Default implementation of {@link ItemInfoService}.
 */
public class DefaultItemInfoService extends AbstractHybrisService implements ItemInfoService
{
	// private static final Logger erpInventoryLog = OmsLoggerFactory.getErpinventorylog();
	private static final Logger omsInventoryLog = OmsLoggerFactory.getOmsinventorylog();

	private final static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssZ");

	private StockroomLocationService stockroomLocationService;

	private TaslyItemLocationService taslyItemLocationService;

	private ItemInfoQueryFactory itemInfoQueries;

	/**
	 * @param taslyItemLocationService the taslyItemLocationService to set
	 */
	public void setTaslyItemLocationService(final TaslyItemLocationService taslyItemLocationService)
	{
		this.taslyItemLocationService = taslyItemLocationService;
	}

	public void setStockroomLocationService(final StockroomLocationService stockroomLocationService)
	{
		this.stockroomLocationService = stockroomLocationService;
	}

	public void setItemInfoQueries(final ItemInfoQueryFactory itemInfoQueries)
	{
		this.itemInfoQueries = itemInfoQueries;
	}

	private String channels;
	private String percents;

	public String getChannels()
	{
		return channels;
	}

	public void setChannels(final String channels)
	{
		this.channels = channels;
	}

	public String getPercents()
	{
		return percents;
	}

	public void setPercents(final String percents)
	{
		this.percents = percents;
	}

	private InventoryService inventoryService;

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

	@Override
	public ItemInfoData createItemInfo()
	{
		omsInventoryLog.info("createItemInfo");
		return super.getPersistenceManager().create(ItemInfoData.class);
	}

	@Override
	public ItemInfoData getBySku(final String sku)
	{
		omsInventoryLog.info("getBySku: sku=" + sku);
		try
		{
			return super.getPersistenceManager().getByIndex(ItemInfoData.UX_ITEMINFOS_SKU, sku);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			omsInventoryLog.error("getBySku: ItemInfoData not found");
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public Page<ItemInfoData> findItemInfosByQuery(final ItemInfoQueryObject query)
	{
		final int[] pageNumberAndSize = getPageNumberAndSize(query, 0, getQueryPageSizeDefault());
		return findPaged(this.itemInfoQueries.findItemInfosByQueryObject(query), pageNumberAndSize[0], pageNumberAndSize[1]);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public List<ItemInfoData> getAll()
	{
		omsInventoryLog.info("getAll");
		return super.getPersistenceManager().createCriteriaQuery(ItemInfoData.class).resultList();
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public List<ItemInfoData> getItemInfoByStockManageFlag(final int flag, final String innerSource)
	{
		omsInventoryLog.info("getItemInfoByStockManageFlag: flag=" + flag);
		return super.getPersistenceManager().createCriteriaQuery(ItemInfoData.class)
				.where(Restrictions.eq(ItemInfoData.STOCKMANAGEFLAG, flag))
				.and(Restrictions.eq(ItemInfoData.INNERSOURCE, InventoryDomainInnerSource.valueOf(innerSource))).resultList();
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public List<TaslyItemLocationData> getByLocation(final StockroomLocationData location, final String[] skus)
	{
		omsInventoryLog.info("getByLocation: location=" + location.getLocationId() + ", skus=" + Arrays.toString(skus));
		try
		{
			return super.getPersistenceManager().createCriteriaQuery(TaslyItemLocationData.class)
					.where(Restrictions.eq(TaslyItemLocationData.STOCKROOMLOCATION, location))
					.and(Restrictions.in(TaslyItemLocationData.ITEMID, skus)).resultList();
		}
		catch (final Exception e)
		{
			omsInventoryLog.error("getByLocation: TaslyItemLocationData not found");
			return new ArrayList<TaslyItemLocationData>();
		}
	}

	@Override
	@Transactional
	public List<List<String>> batchUpdateInventory(final List<ItemInfo> itemInfoSL) throws Exception
	{
		final List<List<String>> result = new ArrayList<List<String>>();
		result.add(null);
		result.add(null);
		// 问题SKU列表
		final List<String> error = new ArrayList<String>();
		// 入库SKU列表
		final List<String> correct = new ArrayList<String>();
		// itemInfoSL 数据源头
		// itemInfoDataTL 目标数据 待储存
		final List<ItemInfoData> itemInfoDataTL = this.getAll();
		if (itemInfoDataTL != null)
		{
			final List<String> allSkus = new ArrayList<String>();
			for (final ItemInfoData itemInfoData : itemInfoDataTL)
			{
				final String sku = itemInfoData.getSku();
				allSkus.add(sku);
			}

			// separate list into two, one is for existing updates, the other one is for new created
			final List<ItemInfo> exists = new ArrayList<ItemInfo>();
			for (final ItemInfo itemInfo : itemInfoSL)
			{
				if (allSkus.contains(itemInfo.getSku()))
				{
					exists.add(itemInfo);
					correct.add(itemInfo.getSku());
				}
				else
				{
					error.add(itemInfo.getSku());
				}
			}
			// 增加有问题SKU列表
			result.set(0, error);

			// handle updated
			final String now = SDF.format(new Date());
			if (exists.size() > 0)
			{
				for (final ItemInfo itemInfoS : exists)
				{
					if (itemInfoS != null)
					{
						for (final ItemInfoData itemInfoDataT : itemInfoDataTL)
						{
							if (itemInfoDataT.getSku().equals(itemInfoS.getSku()))
							{
								omsInventoryLog.info("sku: " + itemInfoS.getSku() + ", original: " + itemInfoDataT.getQuantity()
										+ ", updated: " + itemInfoS.getQuantity());
								itemInfoDataT.setQuantity(itemInfoS.getQuantity());
								itemInfoDataT.setExt1(now);
								break;
							}
						}
					}
				}
			}
			// flush the changes to DB
			super.getPersistenceManager().flush();
			// 增加已经入库SKU列表
			result.set(1, correct);
		}
		return result;
	}

	@Override
	@Transactional
	public boolean batchUpdate(final List<ItemInfo> itemInfoSL) throws Exception
	{
		omsInventoryLog.info("batchUpdate (for rest client only)");
		boolean flag = false;
		// itemInfoSL 数据源头
		// itemInfoDataTL 目标数据 待储存
		final List<ItemInfoData> itemInfoDataTL = this.getAll();
		if (itemInfoDataTL != null)
		{
			final List<String> allSkus = new ArrayList<String>();
			for (final ItemInfoData itemInfoData : itemInfoDataTL)
			{
				final String sku = itemInfoData.getSku();
				allSkus.add(sku);
			}

			omsInventoryLog.info("separate ItemInfo list into two");
			// separate list into two, one is for existing updates, the other one is for new created
			final List<ItemInfo> exists = new ArrayList<ItemInfo>();
			final List<ItemInfo> create = new ArrayList<ItemInfo>();
			for (final ItemInfo itemInfo : itemInfoSL)
			{
				if (allSkus.contains(itemInfo.getSku()))
				{
					exists.add(itemInfo);
				}
				else
				{
					create.add(itemInfo);
				}
			}
			omsInventoryLog.info("existing ItemInfo list size=" + exists.size());
			omsInventoryLog.info("creating ItemInfo list size=" + create.size());

			omsInventoryLog.info("handle existing ItemInfo list update");
			for (final ItemInfoData itemInfoDataT : itemInfoDataTL)
			{
				if (itemInfoDataT != null)
				{
					for (int i = 0; i < exists.size(); i++)
					{
						final ItemInfo itemInfoS = exists.get(i);
						if (itemInfoS != null && itemInfoDataT.getSku().equals(itemInfoS.getSku()))
						{
							itemInfoDataT.setDescription(itemInfoS.getDescription());
							// itemInfoDataT.setQuantity(itemInfoS.getQuantity());
							itemInfoDataT.setBaseUnitCode(itemInfoS.getBaseUnitCode());
							itemInfoDataT.setStockManageFlag(itemInfoS.getStockManageFlag());
							if (itemInfoS.getInnerSource() != null)
							{
								itemInfoDataT.setInnerSource(InventoryDomainInnerSource.valueOf(itemInfoS.getInnerSource().toString()));
							}
							itemInfoDataT.setOldMaterialNumber(itemInfoS.getOldMaterialNumber());
							// itemInfoDataT.setExt1(itemInfoS.getExt1());
							itemInfoDataT.setExt2(itemInfoS.getExt2());
							itemInfoDataT.setExt3(itemInfoS.getExt3());
							itemInfoDataT.setExt4(itemInfoS.getExt4());
							itemInfoDataT.setExt5(itemInfoS.getExt5());
							itemInfoDataT.setVendor(itemInfoS.getVendor());
							exists.remove(i);
							break;
						}
					}
				}
			}

			omsInventoryLog.info("handle creating ItemInfo list create");
			if (create.size() > 0)
			{
				// TS-198
				final List<TaslyStockroomLocationData> tsld = stockroomLocationService.getAll();
				final String[] channelArray = new String[tsld.size()];
				final String[] percentArray = new String[tsld.size()];
				int idx = 0;
				for (final TaslyStockroomLocationData taslyStockroomLocationData : tsld)
				{
					channelArray[idx] = taslyStockroomLocationData.getLocationId();
					percentArray[idx++] = String.valueOf(taslyStockroomLocationData.getDefaultAllocatePercent());
				}

				for (final ItemInfo itemInfo : create)
				{
					final ItemInfoData newItemInfoData = getPersistenceManager().create(ItemInfoData.class);
					newItemInfoData.setSku(itemInfo.getSku());
					newItemInfoData.setDescription(itemInfo.getDescription());
					// newItemInfoData.setQuantity(itemInfo.getQuantity());
					newItemInfoData.setBaseUnitCode(itemInfo.getBaseUnitCode());
					newItemInfoData.setStockManageFlag(itemInfo.getStockManageFlag());
					if (itemInfo.getInnerSource() != null)
					{
						newItemInfoData.setInnerSource(InventoryDomainInnerSource.valueOf(itemInfo.getInnerSource().toString()));
					}
					newItemInfoData.setOldMaterialNumber(itemInfo.getOldMaterialNumber());
					// newItemInfoData.setExt1(itemInfo.getExt1());
					newItemInfoData.setExt2(itemInfo.getExt2());
					newItemInfoData.setExt3(itemInfo.getExt3());
					newItemInfoData.setExt4(itemInfo.getExt4());
					newItemInfoData.setExt5(itemInfo.getExt5());
					newItemInfoData.setVendor(itemInfo.getVendor());

					// initialize default allocation percent into itemlocation
					final String sku = itemInfo.getSku();
					for (int i = 0; i < channelArray.length; i++)
					{
						final String channel = channelArray[i];
						final String percent = percentArray[i];
						final BinData binData = inventoryService.getBinByBinCodeLocationId("default_bin", channel);
						final StockroomLocationData stockRoomLocationData = inventoryService.getLocationByLocationId(channel);
						final TaslyItemLocationData itemLocation = getPersistenceManager().create(TaslyItemLocationData.class);
						itemLocation.setItemId(sku);
						itemLocation.setStockroomLocation(stockRoomLocationData);
						itemLocation.setBin(binData);
						itemLocation.setAllocationPercent(Integer.valueOf(percent));
						itemLocation.setFuture(false);
						itemLocation.setItemInfo(newItemInfoData);
					}
				}
			}

			omsInventoryLog.info("flush ItemInfo list changes to DB");
			super.getPersistenceManager().flush();
			flag = true;
		}
		return flag;
	}

	@Override
	@Transactional
	public boolean batchUpdateItemlocation(final String newChannels) throws Exception
	{
		omsInventoryLog.info("batchUpdateItemlocation (for add channel only) Begin********.");
		boolean flag = false;
		// 获取Stockroom中的渠道、相应百分比
		final List<TaslyStockroomLocationData> tsld = stockroomLocationService.getAll();
		if (tsld != null && tsld.size() > 0)
		{
			final String[] channelArray = new String[tsld.size()];
			final String[] percentArray = new String[tsld.size()];
			int idx = 0;
			for (final TaslyStockroomLocationData taslyStockroomLocationData : tsld)
			{
				channelArray[idx] = taslyStockroomLocationData.getLocationId();
				percentArray[idx++] = String.valueOf(taslyStockroomLocationData.getDefaultAllocatePercent());
			}
			// String[] oldChannelArray = null;
			// String[] oldpercentArray = null;
			final Map<String, String> oldChannelMap = new HashMap<String, String>();
			String[] newChannelArray = null;
			// String[] newpercentArray = null;
			final Map<String, String> newChannelMap = new HashMap<String, String>();
			final StringBuffer oldChannelBuf = new StringBuffer();
			final StringBuffer newChannelBuf = new StringBuffer();
			// 如果没有传入渠道，则进行更新
			if (newChannels != null && newChannels.length() > 0)
			{
				// 新渠道、百分比
				newChannelArray = newChannels.split(",");
				// newpercentArray = new String[newChannelArray.length];
				// 新渠道、百分比
				// oldChannelArray = new String[channelArray.length - newChannelArray.length];
				// oldpercentArray = new String[percentArray.length - newpercentArray.length];

				for (int j = 0; j < channelArray.length; j++)
				{
					boolean newChannelFlag = false;
					final String channel = channelArray[j];
					for (final String newChannel : newChannelArray)
					{
						if (newChannel.equals(channel))
						{
							// newpercentArray[i] = percentArray[j];
							newChannelMap.put(newChannel, percentArray[j]);
							newChannelFlag = true;
							newChannelBuf.append(newChannel).append(",");
							break;
						}
					}
					if (!newChannelFlag)
					{
						// oldChannelArray[oldIdx] = channelArray[j];
						// oldpercentArray[oldIdx++] = percentArray[j];

						oldChannelMap.put(channelArray[j], percentArray[j]);

						oldChannelBuf.append(channelArray[j]).append(",");
					}
				}
			}
			else
			{
				for (int k = 0; k < channelArray.length; k++)
				{
					oldChannelMap.put(channelArray[k], percentArray[k]);
					oldChannelBuf.append(channelArray[k]).append(",");

				}
			}

			// 1、批量更新
			if (oldChannelMap.size() > 0)
			{
				omsInventoryLog.info("开始更新ItemLocation中数据：");
				omsInventoryLog.info("ItemLocation中待更新渠道包括：" + oldChannelBuf.substring(0, oldChannelBuf.length() - 1));
				batchUpdateItemLocationSub(oldChannelMap);
				omsInventoryLog.info("更新ItemLocation中数据结束.");
			}
			// 2、批量新建
			if (newChannelMap.size() > 0)
			{
				omsInventoryLog.info("开始新增ItemLocation中数据：");
				omsInventoryLog.info("ItemLocation中待新增渠道包括：" + newChannelBuf.substring(0, newChannelBuf.length() - 1));
				batchCreateItemLocationSub(newChannelArray, newChannelMap);
				omsInventoryLog.info("新增ItemLocation中数据结束.");
			}
			// 提交
			super.getPersistenceManager().flush();
			flag = true;
		}
		else
		{
			omsInventoryLog.equals("StockroomLocation中无任何渠道信息，请核查.");
		}
		omsInventoryLog.info("batchUpdateItemlocation (for add channel only) End********.");
		return flag;
	}

	private boolean batchUpdateItemLocationSub(final Map<String, String> oldChannelMap) throws Exception
	{
		// 获取全部ItemLocation数据，待更新百分比
		final List<TaslyItemLocationData> taslyItemLocationDatas = taslyItemLocationService.getAll();
		int num = 0;
		final StringBuffer failList = new StringBuffer();
		// 批量进行更新
		if (taslyItemLocationDatas != null && taslyItemLocationDatas.size() > 0)
		{
			final Set<String> oldKeySet = oldChannelMap.keySet();
			for (final TaslyItemLocationData taslyItemLocationData : taslyItemLocationDatas)
			{
				if (oldKeySet.contains(taslyItemLocationData.getStockroomLocation().getStoreName()))
				{
					final String percent = oldChannelMap.get(taslyItemLocationData.getStockroomLocation().getStoreName());
					taslyItemLocationData.setAllocationPercent(Integer.valueOf(percent));
					num++;
				}
				else
				{
					failList.append(taslyItemLocationData.getItemId()).append(",");
				}
			}
		}
		else
		{
			omsInventoryLog.info("taslyItemLocation没有数据可供更新.");
		}
		omsInventoryLog.info("更新ItemLocation记录总数：" + num + "个.");
		if ((taslyItemLocationDatas.size() - num) > 0)
		{
			omsInventoryLog.info("未更新ItemLocation记录总数：" + (taslyItemLocationDatas.size() - num) + "个.");
			omsInventoryLog.info("未更新ItemLocation的SKU包括：" + failList.substring(0, failList.length() - 1));
		}

		return true;
	}

	private boolean batchCreateItemLocationSub(final String[] newChannelArray, final Map<String, String> newChannelMap)
			throws Exception
	{
		// 获取全部ItemInfo数据，用于新建
		final List<ItemInfoData> itemInfoDataL = this.getAll();
		int addNum = 0;
		if (itemInfoDataL != null && itemInfoDataL.size() > 0)
		{
			for (final ItemInfoData itemInfoData : itemInfoDataL)
			{
				final String sku = itemInfoData.getSku();
				for (final String element : newChannelArray)
				{
					final String newchannel = element;
					final String newpercent = newChannelMap.get(newchannel);
					if (!addSingleItemLocation(itemInfoData, newchannel, newpercent))
					{
						omsInventoryLog.error("商品SKU：" + sku + "，新建渠道" + newchannel + "的ItemLocation数据失败.");
					}
					else
					{
						addNum++;
					}
				}
			}
			omsInventoryLog.info("新建ItemLocation记录总数：" + addNum + "个.");
		}
		else
		{
			omsInventoryLog.equals("itemInfo没有数据，请核查.");
		}
		return true;
	}

	private boolean addSingleItemLocation(final ItemInfoData itemInfoData, final String channel, final String percent)
			throws Exception
	{
		final BinData binData = inventoryService.getBinByBinCodeLocationId("default_bin", channel);
		final StockroomLocationData stockRoomLocationData = inventoryService.getLocationByLocationId(channel);
		final TaslyItemLocationData itemLocation = getPersistenceManager().create(TaslyItemLocationData.class);
		itemLocation.setItemId(itemInfoData.getSku());
		itemLocation.setStockroomLocation(stockRoomLocationData);
		itemLocation.setBin(binData);
		itemLocation.setAllocationPercent(Integer.valueOf(percent));
		itemLocation.setItemInfo(itemInfoData);
		itemLocation.setFuture(false);
		return true;
	}
}
