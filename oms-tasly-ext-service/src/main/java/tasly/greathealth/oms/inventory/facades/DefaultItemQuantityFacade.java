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
package tasly.greathealth.oms.inventory.facades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.ItemQuantityFacade;
import tasly.greathealth.oms.api.inventory.dto.ItemQuantityElement;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.services.ItemInfoService;
import tasly.greathealth.oms.inventory.services.ItemQuantityService;
import tasly.greathealth.oms.inventory.services.ItemQuantityService.HandleReturn;
import tasly.greathealth.oms.inventory.services.TaslyItemLocationService;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * Default implementation of {@link ItemQuantityFacade}
 */
public class DefaultItemQuantityFacade implements ItemQuantityFacade
{
	private ItemInfoService itemInfoService;
	private TaslyItemLocationService taslyItemLocationService;
	private ItemQuantityService itemQuantityService;

	/**
	 * @param itemInfoService the itemInfoService to set
	 */
	@Required
	public void setItemInfoService(final ItemInfoService itemInfoService)
	{
		this.itemInfoService = itemInfoService;
	}

	/**
	 * @param taslyItemLocationService the taslyItemLocationService to set
	 */
	@Required
	public void setTaslyItemLocationService(final TaslyItemLocationService taslyItemLocationService)
	{
		this.taslyItemLocationService = taslyItemLocationService;
	}

	/**
	 * @param itemQuantityService the itemQuantityService to set
	 */
	@Required
	public void setItemQuantityService(final ItemQuantityService itemQuantityService)
	{
		this.itemQuantityService = itemQuantityService;
	}

	private static final Logger omsLOG = OmsLoggerFactory.getOmsinventorylog();
	private static final Logger omsSkuLog = OmsLoggerFactory.getOmserrorskulog();

	@Override
	public boolean updateItemQuantity()
	{
		EnumMap<HandleReturn, Object> handleReturn = new EnumMap<HandleReturn, Object>(HandleReturn.class);
		boolean updateStatus = true;
		final List<String> errorSkus = new ArrayList<>();
		final List<ItemInfoData> itemInfoDatas = itemInfoService.getAll();
		if (itemInfoDatas != null && itemInfoDatas.size() > 0)
		{
			for (int i = 0; i < itemInfoDatas.size(); i++)
			{
				final String sku = itemInfoDatas.get(i).getSku();
				final int totalNumber = itemInfoDatas.get(i).getQuantity();
				final int flag = itemInfoDatas.get(i).getStockManageFlag();

				final Date nowDay = new Date();
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
				final String nowDate = sdf.format(nowDay);
				final String nowDateString = nowDate.substring(0, 8);
				final String modifyDay = itemInfoDatas.get(i).getExt1();
				String modifyDayString = null;
				if (StringUtils.isEmpty(modifyDay))
				{
					modifyDayString = "";
				}
				else
				{
					modifyDayString = modifyDay.substring(0, 8);
				}
				if ("".equals(modifyDayString) || nowDateString.equals(modifyDayString) || flag == 0)
				{
					final List<TaslyItemLocationData> checkTaslyItemLocationDatas = taslyItemLocationService.getByItemID(sku);
					if (checkTaslyItemLocationDatas == null || checkTaslyItemLocationDatas.size() == 0)
					{
						omsLOG.error("sku:" + sku + ",no ItemLocation data!");
						continue;
					}
					else
					{
						try
						{
							handleReturn = itemQuantityService.handleUpdateMethod(checkTaslyItemLocationDatas, sku, flag, totalNumber);
						}
						catch (final Exception e)
						{
							omsLOG.error("handle sku:" + sku + " failed and error is " + e);
							handleReturn.put(HandleReturn.handleStatus, false);
						}
						if ((boolean) handleReturn.get(HandleReturn.handleStatus))
						{
							if ((boolean) handleReturn.get(HandleReturn.errorStatus))
							{
								errorSkus.add(sku);
							}
							try
							{
								updateStatus = itemQuantityService.updateMethod(sku, flag,
										(int) handleReturn.get(HandleReturn.availableNumber));
								if (flag == 0 && updateStatus)
								{
									omsLOG.debug("sku:" + sku + ",flag=0 allocated ok!");
								}
								else if (flag == 1 && updateStatus)
								{
									omsLOG.debug("sku:" + sku + ",flag=1 allocated ok!");
								}
							}
							catch (final Exception e)
							{
								omsLOG.error("update sku:" + sku + " failed and error is " + e);
							}
						}
						else
						{
							continue;
						}
					}
				}
				else if (!nowDateString.equals(modifyDayString) && flag == 1)
				{
					omsLOG.error("sku:" + sku + ",modifyTime:" + modifyDayString + ",nowday:" + nowDateString
							+ ".The modifyTime not equal currentdate,current sku skip allocated");
					continue;
				}
			}
			if (errorSkus.size() > 0)
			{
				final StringBuffer skulist = new StringBuffer();
				for (final String errorSku : errorSkus)
				{
					skulist.append(errorSku + ",");
				}
				omsSkuLog.error("商品库存不足，需要补货的SKU有：" + skulist.toString());
			}
			return true;
		}
		else
		{
			omsLOG.error("get iteminfos failed!");
			return false;
		}
	}

	@Override
	public boolean batchUpdate(final String sku, final List<ItemQuantityElement> list)
	{
		return this.itemQuantityService.batchUpdate(sku, list);
	}
}
