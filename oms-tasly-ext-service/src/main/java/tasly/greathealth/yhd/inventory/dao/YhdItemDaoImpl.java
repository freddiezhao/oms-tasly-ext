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
package tasly.greathealth.yhd.inventory.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.inventory.beans.StoreSku;
import tasly.greathealth.thirdparty.inventory.dao.impl.AbstractStoreItemDao;

import com.yhd.YhdClient;
import com.yhd.object.product.Product;
import com.yhd.request.product.GeneralProductsSearchRequest;
import com.yhd.response.product.GeneralProductsSearchResponse;


/**
 *
 */
public class YhdItemDaoImpl extends AbstractStoreItemDao
{
	protected static final Logger Log = OmsLoggerFactory.getYhdinventorylog();
	private YhdClient client;
	private String sessionKey;



	@Override
	public List<StoreSku> getInStockItemsFromStore()
	{
		final GeneralProductsSearchRequest request = new GeneralProductsSearchRequest();
		GeneralProductsSearchResponse response = null;
		final List<Product> totalItems = new ArrayList<Product>();
		final int pageAmount = getPageAmount();
		int trytimes = 0;
		for (int i = 0; i < pageAmount; i++)
		{

			request.setCurPage(i + 1);
			request.setPageRows(getPageSize());
			try
			{
				response = client.excute(request, sessionKey);
				trytimes++;
				totalItems.addAll(response.getProductList().getProduct());

			}
			catch (final Exception e)
			{
				Log.error("获取库存中的产品异常,重新尝试发送");
				if (trytimes < countlimit)
				{
					i--;
				}
				else
				{
					instockCount = 0;
					Log.error("获取库存中的产品异常，超过处理次数，无法处理");
				}
			}
		}
		Log.info("获取库存中的产品正常，获取产品数量为" + totalItems.size());
		return populatorSkus(totalItems);
	}

	@Override
	public int getTotalItemsOfStore(int t)
	{
		int total = 0;
		final GeneralProductsSearchRequest request = new GeneralProductsSearchRequest();
		GeneralProductsSearchResponse response = null;
		request.setCurPage(10);
		request.setPageRows(50);
		try
		{
			response = client.excute(request, sessionKey);
			t++;
		}
		catch (final Exception e)
		{
			Log.error("获取库存中的产品异常,重新尝试发送");
			// if (t < countlimit)
			// {
			// total = getTotalItemsOfStore(t);
			// }
			// else
			// {
			// total = 0;
			// Log.error("获取库存中的产品异常，超过处理次数，无法处理");
			// }
		}
		if (response.getErrorCount() > 0)
		{
			Log.error("获取库存中的产品异常,异常原因：" + response.getErrInfoList().getErrDetailInfo());
		}
		else
		{
			total = response.getTotalCount();
			Log.info("获取库存中的产品正常，获取产品数量为" + total);
		}

		return total;
	}

	public List<StoreSku> populatorSkus(final List<Product> products)
	{
		final List<StoreSku> skus = new ArrayList<StoreSku>();
		for (final Product product : products)
		{
			final StoreSku sku = new StoreSku();
			sku.setOuterid(product.getOuterId());
			sku.setProductid(product.getProductId());
			skus.add(sku);
		}
		return skus;
	}

	@Override
	public boolean updateStockInfoBySku(final StoreSku sku)
	{
		// final ProductStockUpdateRequest request = new ProductStockUpdateRequest();
		// ProductStockUpdateResponse response = null;
		// final String outerid = sku.getOuterid();
		// final long quantity = sku.getStockQuntity();
		// request.setOuterId(outerid);
		// request.setVirtualStockNum((int) quantity);
		// try
		// {
		// response = getClient(sku.getInnerSource()).excute(request, getSessionKey(sku.getInnerSource()));
		// }
		// catch (final Exception e)
		// {
		// Log.error("获取库存中的产品异常，超过处理次数，无法处理");
		// return false;
		// }
		// if (response.getErrorCount() > 0)
		// {
		// Log.error("获取库存中的产品异常,异常原因：" + response.getErrInfoList().getErrDetailInfo());
		// exceptionHandle.handleException(response.getErrInfoList().toString(), sku);
		// return false;
		// }
		// else
		// {
		// Log.info("库存同步:更新成功的数据:num_iid:" + sku.getProductid() + "outerId:" + sku.getOuterid() + "|库存：" +
		// sku.getStockQuntity());
		// }

		return true;
	}

	/**
	 * @return the client
	 */
	public YhdClient getClient()
	{
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(final YhdClient client)
	{
		this.client = client;
	}

	/**
	 * @return the sessionKey
	 */
	public String getSessionKey()
	{
		return sessionKey;
	}

	/**
	 * @param sessionKey the sessionKey to set
	 */
	public void setSessionKey(final String sessionKey)
	{
		this.sessionKey = sessionKey;
	}

}
