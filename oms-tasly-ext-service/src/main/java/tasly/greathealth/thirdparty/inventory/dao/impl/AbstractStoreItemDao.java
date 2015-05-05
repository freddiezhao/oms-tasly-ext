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
package tasly.greathealth.thirdparty.inventory.dao.impl;

import java.util.List;

import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.inventory.beans.StoreSku;
import tasly.greathealth.thirdparty.inventory.dao.StoreItemDao;
import tasly.greathealth.thirdparty.inventory.exception.StoreExceptionHandle;
import tasly.greathealth.tmall.inventory.ItemConstants;

import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.domain.ware.Ware;


/**
 *
 */
public class AbstractStoreItemDao implements StoreItemDao
{

	protected static final Logger Log = OmsLoggerFactory.getTmallinventorylog();
	// we get the instock and onsale inventory from tmall,in order to avoid the situation of cycle getting data error,we
	// save the data we get from tamll currently

	// private static List<Item> currentItemsSave = new ArrayList<Item>();
	protected StoreExceptionHandle exceptionHandle;

	public int pageSize;
	public int pageAmount;

	/**
	 * @param pageAmount the pageAmount to set
	 */
	public void setPageAmount(final int pageAmount)
	{
		this.pageAmount = pageAmount;
	}


	/**
	 * @return the pageSize
	 */
	public int getPageSize()
	{
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}

	protected int instockCount = 0;
	protected final int onsaleCount = 0;
	protected final int countlimit = ItemConstants.COUNTLIMIT;


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
	 * @return the countlimit
	 */
	public int getCountlimit()
	{
		return countlimit;
	}


	/**
	 * @param instockCount the instockCount to set
	 */
	public void setInstockCount(final int instockCount)
	{
		this.instockCount = instockCount;
	}

	@Override
	public List<StoreSku> getDownStockItemsFromStore()
	{
		return null;
	}

	/**
	 * 获取分页数量
	 *
	 * @return
	 */
	public int getPageAmount()
	{
		if (pageAmount == 0)
		{
			// 获取京东上的所有的商品数量，来决定总共分多少页
			final int totalResult = this.getTotalItemsOfStore(0);
			if (totalResult % pageSize != 0)
			{
				pageAmount = totalResult / pageSize + 1;
			}
			else
			{
				pageAmount = totalResult / pageSize;
			}
		}
		return pageAmount;
	}

	public void populateSkus(final List<Sku> sources, final List<StoreSku> targets)
	{
		for (final Sku s : sources)
		{
			final StoreSku t = new StoreSku();
			t.setSkuid(s.getSkuId() + "");
			t.setOuterid(s.getOuterId());
			t.setShopName(s.getShopId() + "");
			targets.add(t);
		}

	}

	public List<Ware> getWareInfosByIds(final String wareIds, final int trytimes)
	{
		return null;
	}



	/**
	 * get OnSale items from tm
	 */
	@Override
	public List<StoreSku> getOnSaleItemsFromStore()
	{

		return null;
	}


	@Override
	public boolean updateStockInfoBySku(final StoreSku sku)
	{
		return true;
	}


	@Override
	public StoreSku getStoreSkuByOuterId(final String outerid)
	{
		return null;
	}


	/**
	 * Batch update skus
	 *
	 * @param skus
	 * @return
	 */
	public String convertFromListToStringOfSkus(final List<Sku> skus)
	{
		final StringBuilder sb = new StringBuilder();
		// for (final Sku sku : skus)
		// {
		// // Log.info("更新套餐编码:" + sku.getOuterId() + "库存:" + sku.getQuantity());
		// // sb.append(";" + sku.getSkuId() + ":" + sku.getQuantity());
		// }
		return sb.substring(1);
	}


	@Override
	public int getOnsaleCount()
	{
		// YTODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getInstockCount()
	{
		// YTODO Auto-generated method stub
		return 0;
	}


	@Override
	public List<StoreSku> getInStockItemsFromStore()
	{
		// YTODO Auto-generated method stub
		return null;
	}


	@Override
	public int getTotalItemsOfStore(final int times)
	{
		// YTODO Auto-generated method stub
		return 0;
	}






}
