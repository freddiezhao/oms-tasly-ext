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
package tasly.greathealth.jd.inventory.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.inventory.beans.StoreSku;
import tasly.greathealth.thirdparty.inventory.dao.impl.AbstractStoreItemDao;

import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.request.ware.SkuCustomGetRequest;
import com.jd.open.api.sdk.request.ware.WareInfoByInfoRequest;
import com.jd.open.api.sdk.request.ware.WareListRequest;
import com.jd.open.api.sdk.request.ware.WareListingGetRequest;
import com.jd.open.api.sdk.request.ware.WareSkuStockUpdateRequest;
import com.jd.open.api.sdk.response.ware.SkuCustomGetResponse;
import com.jd.open.api.sdk.response.ware.WareInfoByInfoSearchResponse;
import com.jd.open.api.sdk.response.ware.WareListResponse;
import com.jd.open.api.sdk.response.ware.WareListingGetResponse;
import com.jd.open.api.sdk.response.ware.WareSkuStockUpdateResponse;


/**
 *
 */
public class JdItemDaoImpl extends AbstractStoreItemDao
{


	protected static final Logger Log = OmsLoggerFactory.getJdinventorylog();
	private JdClient client;


	/**
	 * 获取分页数量
	 *
	 * @return
	 */
	@Override
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

	public List<StoreSku> getSkusFromItems(final List<Ware> infos)
	{
		final List<Sku> skus = new ArrayList<Sku>();
		final List<StoreSku> finalskus = new ArrayList<StoreSku>();
		final List<Ware> finalWares = new ArrayList<Ware>();
		if (infos == null || infos.size() == 0)
		{
			return finalskus;
		}

		final int size = (infos.size()) / 10;

		for (int i = 0; i < size - 1; i++)
		{
			String wareids = "";
			for (int j = 0; j < 9; j++)
			{

				f2: if (i * 10 + j - 1 > infos.size())
				{
					break f2;
				}

			final Ware ware = infos.get(i * 10 + j);
			wareids += ware.getWareId() + ",";
			}
			finalWares.addAll(getWareInfosByIds(wareids, 0));

		}
		for (final Ware ware : finalWares)
		{
			skus.addAll(ware.getSkus());
		}

		this.populateSkus(skus, finalskus);
		return finalskus;
	}

	@Override
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

	@Override
	public List<Ware> getWareInfosByIds(final String wareIds, int trytimes)
	{
		List<Ware> totalItems = new ArrayList<Ware>();

		final WareListRequest request = new WareListRequest();

		request.setWareIds(wareIds);

		WareListResponse response = null;
		try
		{
			response = client.execute(request);
			trytimes++;

		}
		catch (final JdException e)
		{
			Log.error("获取商品数据异常,重新尝试发送");
			// if (trytimes < countlimit)
			// {
			// totalItems = this.getWareInfosByIds(wareIds, trytimes);
			// }
			// else
			// {
			// Log.error("获取商品数据异常,重新尝试发送");
			// }
		}
		totalItems = response.getWareList();
		Log.info("获取商品数据正常");
		return totalItems;
	}



	/**
	 * get inStock items from tm
	 */
	@Override
	public List<StoreSku> getInStockItemsFromStore()
	{
		final List<Ware> totalItems = new ArrayList<Ware>();
		final WareListingGetRequest request = new WareListingGetRequest();
		WareListingGetResponse response = null;
		final int pageAmount = getPageAmount();
		int trytimes = 0;
		for (int i = 0; i < pageAmount; i++)
		{

			request.setPage(i + 1 + "");
			request.setPageSize(getPageSize() + "");
			try
			{
				response = client.execute(request);
				trytimes++;

			}
			catch (final JdException e)
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
			if (!response.getCode().equals("0"))
			{
				Log.error("获取库存中的产品异常，异常代码" + response.getCode());
				instockCount = 0;
			}
			else
			{
				trytimes = 0;
				totalItems.addAll(response.getWareInfos());

			}
		}
		Log.info("获取库存中的产品正常，获取产品数量为" + instockCount);
		return getSkusFromItems(totalItems);
	}

	/**
	 * get OnSale items from tm
	 */

	// get total ItemNums of inStock items from tmall
	@Override
	public int getTotalItemsOfStore(int times)
	{

		int total = 0;
		final WareInfoByInfoRequest request = new WareInfoByInfoRequest();
		request.setPage("10");
		request.setPageSize("100");
		WareInfoByInfoSearchResponse response = null;

		try
		{
			response = client.execute(request);
			times++;
		}
		catch (final JdException e)
		{
			Log.error("获取库存中的产品总数量异常,重新尝试发送");
			// if (times < countlimit)
			// {
			// total = this.getTotalItemsOfStore(times);
			// }
			// else
			// {
			// total = 0;
			// Log.error("获取库存中的产品总数量异常，超过处理次数，无法处理");
			// }
		}
		total = response.getTotal();
		Log.error("获取库存中的产品总数量正常" + response.getCode());
		return total;
	}

	@Override
	public boolean updateStockInfoBySku(final StoreSku sku)
	{
		WareSkuStockUpdateResponse response = null;
		final WareSkuStockUpdateRequest request = new WareSkuStockUpdateRequest();

		// request.setSkuId("1152582145");
		// request.setOuterId("10000379");
		// request.setQuantity("60");

		System.out.println(sku.getSkuid() + "-----" + sku.getOuterid() + "---" + sku.getStockQuntity());
		if (sku.getSkuid() != null)
		{
			request.setSkuId(sku.getSkuid());
		}
		if (sku.getOuterid() != null)
		{
			request.setOuterId(sku.getOuterid());
		}
		request.setQuantity(sku.getStockQuntity() + "");
		try
		{
			response = client.execute(request);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return false;
		}

		if (null == response.getCode())
		{
			exceptionHandle.handleException(response.getCode(), sku);
			return false;
		}
		else
		{
			if (response.getCode().equals("0"))
			{
				Log.info("库存同步:更新成功的数据:num_iid:" + response.getSkuId() + "outerId:" + response.getOuterId() + "|库存："
						+ sku.getStockQuntity());
			}
			else
			{
				Log.info("库存同步:数据:num_iid:" + response.getSkuId() + "结果异常");
				return false;
			}
		}
		return true;
	}


	@Override
	public StoreSku getStoreSkuByOuterId(final String outerid)
	{
		final StoreSku sku = new StoreSku();
		final SkuCustomGetRequest req = new SkuCustomGetRequest();
		req.setOuterId(outerid);
		// req.setFields("sku_id,num_iid,quantity");
		SkuCustomGetResponse response;
		try
		{
			response = client.execute(req);
		}
		catch (final JdException e)
		{
			Log.error("get item by outerid form tmall error...");
			return sku;
		}
		if (null != response.getSku())
		{
			Log.info("SKU:" + outerid + "根据Sku外部编码获取商品信息成功");
			// return response.getSkus().get(0);
			return sku;
		}
		else
		{
			Log.info("SKU:" + outerid + "根据Sku外部编码获取商品信息失败，尝试根据Item外部商家编码获取");
		}

		this.populateSku(response.getSku(), sku);
		return null;
	}

	private void populateSku(final Sku jdsku, final StoreSku storesku)
	{
		storesku.setSkuid(jdsku.getSkuId() + "");
		storesku.setOuterid(jdsku.getOuterId());
		storesku.setProductid(jdsku.getWareId());
	}


	/**
	 * Batch update skus
	 *
	 * @param skus
	 * @return
	 */
	@Override
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

	public JdClient getClient()
	{
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(final JdClient client)
	{
		this.client = client;
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






}
