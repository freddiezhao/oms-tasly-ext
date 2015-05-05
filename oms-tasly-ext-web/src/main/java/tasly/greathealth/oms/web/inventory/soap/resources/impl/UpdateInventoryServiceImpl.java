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
package tasly.greathealth.oms.web.inventory.soap.resources.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.slf4j.Logger;

import tasly.greathealth.erp.api.inventory.dto.Baseinfo;
import tasly.greathealth.erp.api.inventory.dto.Inventory;
import tasly.greathealth.erp.api.inventory.dto.Inventorys;
import tasly.greathealth.erp.api.inventory.dto.Message;
import tasly.greathealth.oms.api.inventory.ItemInfoFacade;
import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.web.inventory.soap.resources.UpdateInventoryService;


/**
 * Update Inventory Service(TS-193)
 *
 * @author libin539
 *         2014-12-30
 *
 */
@WebService(endpointInterface = "tasly.greathealth.oms.web.inventory.soap.resources.UpdateInventoryService")
public class UpdateInventoryServiceImpl implements UpdateInventoryService
{
	// private static final Logger LOG = LoggerFactory.getLogger(UpdateInventoryServiceImpl.class);
	private static final Logger erpInventoryLog = OmsLoggerFactory.getErpinventorylog();
	ItemInfoFacade itemInfoFacade;

	/**
	 * @return the itemInfoFacade
	 */
	public ItemInfoFacade getItemInfoFacade()
	{
		return itemInfoFacade;
	}

	/**
	 * @param itemInfoFacade the itemInfoFacade to set
	 */
	public void setItemInfoFacade(final ItemInfoFacade itemInfoFacade)
	{
		this.itemInfoFacade = itemInfoFacade;
	}

	@Override
	@WebMethod
	@Oneway
	public void updateInventory(final Baseinfo baseinfo, final Message message)
	{
		// 库存更新开始时间
		final Calendar startTime = Calendar.getInstance();
		// 库存更新结束时间
		Calendar endTime = startTime;
		// 库存更新时间长度
		long between = 0;
		erpInventoryLog.info("********** Start:");
		erpInventoryLog.info("********** 本次接收ERP请求时间：" + convertDateToString(startTime.getTime()) + ",开始更新库存信息：");
		try
		{
			List resultList = null;
			List<String> error = null;
			List<String> correct = null;
			// 1.将Message转为ItemInfo list对象
			final List<ItemInfo> itemInfoList = messageToItemInfo(message);
			// 2.调用库存批量导入Facade
			if (itemInfoList != null)
			{
				erpInventoryLog.info("********** 本次待更新库存信息产品总数是" + itemInfoList.size() + "个.");
				resultList = itemInfoFacade.batchUpdateInventory(itemInfoList);
				endTime = Calendar.getInstance();
				between = endTime.getTimeInMillis() - startTime.getTimeInMillis();
				error = (List<String>) resultList.get(0);
				correct = (List<String>) resultList.get(1);

				if (error != null && error.size() > 0)
				{
					erpInventoryLog.warn("********** 共有" + error.size() + "条产品,因数据库表itemInfos没有该产品，无法进行更新！具体产品SKU信息："
							+ error.toString());
				}
				if (correct != null)
				{
					erpInventoryLog.info("********** 本次成功更新：" + correct.size() + "条产品库存信息.结束时间："
							+ convertDateToString(endTime.getTime()));
				}
				else
				{
					erpInventoryLog.warn("********** 本次成功更新：0条产品库存信息.结束时间：");
				}
				erpInventoryLog.info("********** 本次库存信息更新共耗时：" + between + "毫秒.**********");
			}
			else
			{
				erpInventoryLog.warn("********** 本次库存信息更新Failure.原因：通过Ecc所提供的Message无法获得List<ItemInfo>." + "**********");
			}
			erpInventoryLog.info("********** End.");
		}
		catch (final Exception e)
		{
			erpInventoryLog.error("********** 本次库存信息更新Failure.出现异常，具体异常信息如下：**********");
			erpInventoryLog.error(e.getMessage());
		}
	}

	private List<ItemInfo> messageToItemInfo(final Message message)
	{
		List<ItemInfo> itemInfoList = null;
		int num = 0;

		if (message == null)
		{
			erpInventoryLog.warn("********** 本次ECC所传Message对象是空.**********");
		}
		else
		{
			final Inventorys inventorys = message.getInventorys();
			final List<Inventory> inventoryList = inventorys.getInventory();
			if (inventoryList == null)
			{
				erpInventoryLog.warn("********** 本次ECC所传Message对象中Inventory列表是空.**********");
			}
			else
			{
				itemInfoList = new ArrayList();
				for (int i = 0; i < inventoryList.size(); i++)
				{
					final Inventory inventory = inventoryList.get(i);
					if (inventory != null && inventory.getSkuId() != null && inventory.getSkuId().length() > 0)
					{
						final ItemInfo itemInfo = new ItemInfo();
						itemInfo.setSku(inventory.getSkuId());
						itemInfo.setQuantity(inventory.getQuantity());
						itemInfoList.add(itemInfo);
					}
					else
					{
						erpInventoryLog.debug("********** 本次ECC所传Message对象Inventory列表中第 " + i
								+ "个Inventory单元信息中SKUID为空,将跳过该单元信息继续处理.**********");
						num++;
						continue;
					}
				}
				if (num > 0)
				{
					erpInventoryLog.warn("********** 请注意：本次更新：有" + num + "条商品库存数据因信息不完整，无法更新.**********");
				}
			}
		}
		return itemInfoList;
	}

	// 2015-01-18 13:19:18
	public static String convertDateToString(final Date date)
	{
		String dateString = null;
		dateString = date == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return dateString;
	}

}
