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
package tasly.greathealth.oms.inventory.services.impl;

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.impl.DefaultAtsService;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.inventory.dto.ItemQuantityElement;
import tasly.greathealth.oms.api.inventory.dto.NumberDto;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.services.ItemQuantityService;
import tasly.greathealth.oms.inventory.services.TaslyItemLocationService;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * Default implementation of {@link ItemQuantityService}.
 */
public class DefaultItemQuantityService extends AbstractHybrisService implements ItemQuantityService
{
	private DefaultAtsService defaultAtsService;
	private static final String atsId = "WEB";
	private static final Logger omsLOG = OmsLoggerFactory.getOmsinventorylog();
	private TaslyItemLocationService taslyItemLocationService;

	/**
	 * @param defaultAtsService the defaultAtsService to set
	 */
	@Required
	public void setDefaultAtsService(final DefaultAtsService defaultAtsService)
	{
		this.defaultAtsService = defaultAtsService;
	}

	/**
	 * @param taslyItemLocationService the taslyItemLocationService to set
	 */
	@Required
	public void setTaslyItemLocationService(final TaslyItemLocationService taslyItemLocationService)
	{
		this.taslyItemLocationService = taslyItemLocationService;
	}

	@Override
	@Transactional
	public EnumMap<HandleReturn, Object> handleUpdateMethod(final List<TaslyItemLocationData> taslyItemLocationDatas,
			final String sku, final int flag, final int totalNumber)
	{
		final EnumMap<HandleReturn, Object> retMap = new EnumMap<HandleReturn, Object>(HandleReturn.class);
		boolean status = true;
		boolean flagStatus = false;// insert new itemQuantity flagStatus.
		int totalOccupy = 0;
		try
		{
			for (int i = 0; i < taslyItemLocationDatas.size(); i++)
			{
				final List<ItemQuantityData> itemQuantityDatas = taslyItemLocationDatas.get(i).getItemQuantities();
				if (itemQuantityDatas == null || itemQuantityDatas.size() == 0)// no itemQuantityDatas, create!
				{
					final CurrentItemQuantityData currentItemQuantityData = getPersistenceManager().create(
							CurrentItemQuantityData.class);
					currentItemQuantityData.setQuantityValue(0);
					currentItemQuantityData.setStatusCode("ON_HAND");
					currentItemQuantityData.setOwner(taslyItemLocationDatas.get(i));
					flagStatus = true;
				}
				else if (itemQuantityDatas.size() > 0 && flag == 0)// flag=0 itemQuantityData exist,no need update
				{
					status = false;
				}
				else
				// itemQuantityDatas exist, check number
				{
					final ItemQuantityData iqd = checkItemQuantitySatus(itemQuantityDatas);
					if (iqd == null)
					{
						omsLOG.info("There is no on_hand data!");
						status = false;
						break;
					}
					final StockroomLocationData sld = taslyItemLocationDatas.get(i).getStockroomLocation();
					final int occupy = iqd.getQuantityValue() - calculateAts(sld, sku);
					totalOccupy = totalOccupy + occupy;

					// omsLOG.info("Sku:" + sku + ",LocationId:" + locationId + ",Assign value:"
					// + (int) Math.round(totalNumber * taslyItemLocationDatas.get(i).getAllocationPercent() / 100.0)
					// + ",Get value:" + iqd.getQuantityValue() + ",Ats value:" + atsResult.getResult(sku, atsId,
					// locationId));
					// if ((int) Math.round(totalNumber * taslyItemLocationDatas.get(i).getAllocationPercent() / 100.0) <
					// occupy)
					// {
					// omsLOG.error("SKU:" + sku + ",LocationId:" + locationId +
					// ",occupy number bigger than pre assigned value!");
					// // status = false;
					// }// this sku all ItemLocations skipped!
				}
			}
		}
		catch (final Exception e)
		{
			omsLOG.error("error is " + e);
		}
		if (flag == 0 && flagStatus)// flag = 0 and insert new itemQuantity need to be assign.
		{
			retMap.put(HandleReturn.handleStatus, true);
			retMap.put(HandleReturn.availableNumber, 0);
			retMap.put(HandleReturn.errorStatus, false);
			return retMap;
		}
		else
		{
			retMap.put(HandleReturn.handleStatus, status);
			retMap.put(HandleReturn.availableNumber, totalNumber - totalOccupy);
			if (totalNumber < totalOccupy)
			{
				omsLOG.error("SKU:" + sku + ",totalNumber:" + totalNumber + ",<,totalOccupy:" + totalOccupy);
				retMap.put(HandleReturn.errorStatus, true);
			}
			else
			{
				retMap.put(HandleReturn.errorStatus, false);
			}
			return retMap;
		}
	}

	@Override
	@Transactional
	public boolean updateMethod(final String sku, final int flag, final int totalNumber)
	{
		int assignNumber = 0;
		int availableNumber = 0;
		int occupyNumber = 0;
		int tempNumber = 0;
		boolean status = true;
		final List<NumberDto> numberList = new ArrayList<NumberDto>();// output all stockroomLocation numbers
		try
		{
			final List<TaslyItemLocationData> taslyItemLocationDatas = taslyItemLocationService.getByItemID(sku);
			if (flag == 0)
			{
				for (int i = 0; i < taslyItemLocationDatas.size(); i++)
				{
					final List<ItemQuantityData> itemQuantityDatas1 = taslyItemLocationDatas.get(i).getItemQuantities();
					final ItemQuantityData iqd1 = checkItemQuantitySatus(itemQuantityDatas1);
					if (iqd1 == null)
					{
						omsLOG.info("There is no on_hand data!");
						status = false;
						break;
					}
					else
					{
						try
						{
							iqd1.setQuantityValue(99999999);
							taslyItemLocationDatas.get(i).setItemQuantities(itemQuantityDatas1);
						}
						catch (final Exception e)
						{
							omsLOG.info("error:" + e);
							status = false;
							break;
						}
					}
				}
			}
			else if (flag == 1)
			{
				for (int j = 0; j < taslyItemLocationDatas.size() - 1; j++)
				{
					final StockroomLocationData stockroomLocationData2 = taslyItemLocationDatas.get(j).getStockroomLocation();
					final List<ItemQuantityData> itemQuantityDatas2 = taslyItemLocationDatas.get(j).getItemQuantities();
					final ItemQuantityData iqd2 = checkItemQuantitySatus(itemQuantityDatas2);
					if (iqd2 == null)
					{
						omsLOG.info("There is no on_hand data in for each:" + j);
						status = false;
						break;
					}
					else
					{
						try
						{
							availableNumber = (int) Math.round(totalNumber * taslyItemLocationDatas.get(j).getAllocationPercent()
									/ 100.0);
							occupyNumber = iqd2.getQuantityValue() - calculateAts(stockroomLocationData2, sku);
							assignNumber = availableNumber + occupyNumber;
							iqd2.setQuantityValue(assignNumber);
							tempNumber = tempNumber + availableNumber;
							taslyItemLocationDatas.get(j).setItemQuantities(itemQuantityDatas2);
							numberList.add(new NumberDto(availableNumber, occupyNumber, stockroomLocationData2.getLocationId()));
						}
						catch (final Exception e)
						{
							omsLOG.info(" error:" + e);
							status = false;
							break;
						}
					}
				}
				final StockroomLocationData stockroomLocationData3 = taslyItemLocationDatas.get(taslyItemLocationDatas.size() - 1)
						.getStockroomLocation();
				final List<ItemQuantityData> itemQuantityDatas3 = taslyItemLocationDatas.get(taslyItemLocationDatas.size() - 1)
						.getItemQuantities();
				final ItemQuantityData iqd3 = checkItemQuantitySatus(itemQuantityDatas3);
				if (iqd3 == null)
				{
					omsLOG.info("There is no on_hand data at last!");
					status = false;
				}
				else
				{
					try
					{
						availableNumber = totalNumber - tempNumber;
						occupyNumber = iqd3.getQuantityValue() - calculateAts(stockroomLocationData3, sku);
						assignNumber = availableNumber + occupyNumber;
						iqd3.setQuantityValue(assignNumber);
						taslyItemLocationDatas.get(taslyItemLocationDatas.size() - 1).setItemQuantities(itemQuantityDatas3);
						numberList.add(new NumberDto(availableNumber, occupyNumber, stockroomLocationData3.getLocationId()));
					}
					catch (final Exception e)
					{
						omsLOG.info("error:" + e);
						status = false;
					}
				}
			}
		}
		catch (final Exception e)
		{
			omsLOG.info("error:" + e);
			status = false;
		}
		if (status && flag == 1)
		{
			omsLOG.info("SKU:" + sku + ",totalAvailableNumber:" + totalNumber + "******************");
			for (int k = 0; k < numberList.size(); k++)
			{
				omsLOG.info(numberList.get(k).getStockroomLocation() + ",availableNumber:" + numberList.get(k).getAvailableNumber()
						+ ",occupyNumber:" + numberList.get(k).getOccupyNumber());
			}
		}
		return status;
	}

	private ItemQuantityData checkItemQuantitySatus(final List<ItemQuantityData> itemQuantityDatas)
	{
		if (itemQuantityDatas == null || itemQuantityDatas.size() == 0)
		{
			omsLOG.info("ItemLocationData has no itemQuantityDatas!");
		}
		else
		{
			for (final ItemQuantityData itemQuantityData : itemQuantityDatas)
			{
				if (itemQuantityData.getStatusCode() != null && "ON_HAND".equals(itemQuantityData.getStatusCode()))
				{
					return itemQuantityData;
				}
			}
		}
		return null;
	}

	private int calculateAts(final StockroomLocationData sld, final String sku)
	{
		final String locationId = sld.getLocationId();
		final Set<String> filterSkus = new HashSet<String>();
		filterSkus.add(sku);
		final Set<String> filterLocations = new HashSet<String>();
		filterLocations.add(locationId);
		final Set<String> atsIds = new HashSet<String>();
		atsIds.add(atsId);
		final AtsResult atsResult = defaultAtsService.getLocalAts(filterSkus, filterLocations, atsIds);
		return atsResult.getResult(sku, atsId, locationId);
	}

	@Override
	public int calculateAts(final String sku, final String locationId)
	{
		final Set<String> filterSkus = new HashSet<String>();
		filterSkus.add(sku);
		final Set<String> filterLocations = new HashSet<String>();
		filterLocations.add(locationId);
		final Set<String> atsIds = new HashSet<String>();
		atsIds.add(atsId);
		final AtsResult atsResult = defaultAtsService.getLocalAts(filterSkus, filterLocations, atsIds);
		return atsResult.getResult(sku, atsId, locationId);
	}

	@Override
	@Transactional
	public boolean batchUpdate(final String sku, final List<ItemQuantityElement> list)
	{
		try
		{
			final Map<String, Integer> map = new HashMap<String, Integer>();
			for (final ItemQuantityElement itemQuantityElement : list)
			{
				map.put(itemQuantityElement.channel, itemQuantityElement.quantity);
			}
			final List<TaslyItemLocationData> taslyItemLocationDatas = taslyItemLocationService.getByItemID(sku);
			for (final TaslyItemLocationData taslyItemLocationData : taslyItemLocationDatas)
			{
				final List<ItemQuantityData> itemQuantityDatas = taslyItemLocationData.getItemQuantities();
				final String location = taslyItemLocationData.getStockroomLocation().getLocationId();
				for (final ItemQuantityData itemQuantityData : itemQuantityDatas)
				{
					final String statusCode = itemQuantityData.getStatusCode();
					if ("ON_HAND".equals(statusCode))
					{
						itemQuantityData.setQuantityValue(map.get(location).intValue());
					}
					else
					{
						continue;
					}
				}
				taslyItemLocationData.setItemQuantities(itemQuantityDatas);
			}
			getPersistenceManager().flush();
		}
		catch (final Exception e)
		{
			return false;
		}
		return true;
	}
}
