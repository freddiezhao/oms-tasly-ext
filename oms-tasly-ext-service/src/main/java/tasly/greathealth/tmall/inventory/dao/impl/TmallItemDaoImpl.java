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
package tasly.greathealth.tmall.inventory.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.inventory.ItemConstants;
import tasly.greathealth.tmall.inventory.dao.TmallItemDao;
import tasly.greathealth.tmall.inventory.domain.ProductTm;
import tasly.greathealth.tmall.inventory.exception.ExceptionHandle;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.request.ItemQuantityUpdateRequest;
import com.taobao.api.request.ItemsCustomGetRequest;
import com.taobao.api.request.ItemsInventoryGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.SkusCustomGetRequest;
import com.taobao.api.request.SkusQuantityUpdateRequest;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.api.response.ItemQuantityUpdateResponse;
import com.taobao.api.response.ItemsCustomGetResponse;
import com.taobao.api.response.ItemsInventoryGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.SkusCustomGetResponse;
import com.taobao.api.response.SkusQuantityUpdateResponse;


/**
 *
 */
public class TmallItemDaoImpl implements TmallItemDao
{

	private static final Logger Log = OmsLoggerFactory.getTmallinventorylog();
	// we get the instock and onsale inventory from tmall,in order to avoid the situation of cycle getting data error,we
	// save the data we get from tamll currently

	// private static List<Item> currentItemsSave = new ArrayList<Item>();
	private ExceptionHandle exceptionHandle;

	private static int instockCount = 0;
	private static int onsaleCount = 0;
	private int countlimit = ItemConstants.COUNTLIMIT;

	/**
	 * get inStock items from tm
	 */
	@Override
	public List<Item> getInStockInfoFromTmall(final TaobaoClient client, final String sessionKey, final Long page_no,
			final Long page_size, String banner)
	{
		final Long pageSize = page_size;// pageSize
		Long pageNumber = page_no;// pageNo
		final Long totalResult = this.getTotalItemsOfInstockProduct(client, sessionKey, banner);
		List<Item> totalItems = new ArrayList<Item>();
		// final TaobaoClient client = TmallUtils.getConnection();
		final ItemsInventoryGetRequest req = new ItemsInventoryGetRequest();
		req.setFields("num_iid,num,sku,outer_id,props");
		req.setBanner(banner);
		if (totalResult % pageSize != 0)
		{
			pageNumber = totalResult / pageSize + 1;
		}
		else
		{
			pageNumber = totalResult / pageSize;
		}

		for (Long i = page_no; i <= pageNumber; i++)
		{
			req.setPageNo(i);
			req.setPageSize(pageSize);
			ItemsInventoryGetResponse resp = null;
			try
			{
				resp = client.execute(req, sessionKey);
				instockCount = 0;
				totalItems.addAll(resp.getItems());// save the current items in memory
			}
			catch (final ApiException e)
			{
				Log.error("获取库存中的产品异常,重新尝试发送");
				if (instockCount < countlimit)
				{
					instockCount++;
					totalItems = this.getInStockInfoFromTmall(client, sessionKey, page_no, page_size, banner);
					break;
				}
				else
				{
					instockCount = 0;
					Log.error("获取库存中的产品异常，超过处理次数，无法处理");
				}
			}

		}
		return totalItems;
	}

	/**
	 * get OnSale items from tm
	 */
	@Override
	public List<Item> getOnSaleInfoFromTmall(final TaobaoClient taboclient, final String sessionKey, final Long page_no,
			final Long page_size)
	{
		final Long pageSize = page_size;// pageSize
		Long pageNumber = page_no;// pageNo
		final Long totalResult = this.getTotalItemsOfOnsaleProduct(taboclient, sessionKey);// total items number

		List<Item> totalItems = new ArrayList<Item>();
		final ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
		req.setFields("num_iid,num,sku,outer_id,props");

		if (totalResult % pageSize != 0)
		{
			pageNumber = totalResult / pageSize + 1;
		}
		else
		{
			pageNumber = totalResult / pageSize;
		}
		for (Long i = page_no; i <= pageNumber; i++)
		{
			req.setPageNo(i);
			req.setPageSize(pageSize);
			ItemsOnsaleGetResponse res = null;
			try
			{
				// final TaobaoClient client = TmallUtils.getConnection();
				res = taboclient.execute(req, sessionKey);
				onsaleCount = 0;
				totalItems.addAll(res.getItems());
			}
			catch (final ApiException e)
			{
				Log.error("获取出售中的产品异常");
				if (onsaleCount < countlimit)
				{
					onsaleCount++;
					totalItems = this.getOnSaleInfoFromTmall(taboclient, sessionKey, i, page_size);
				}
				else
				{
					onsaleCount = 0;
					Log.error("获取出售中的产品异常，超过处理次数，无法处理");
				}
			}
		}
		return totalItems;
	}


	// get total ItemNums of OnSale items from tmall
	public Long getTotalItemsOfOnsaleProduct(final TaobaoClient tabaoclient, final String sessionKey)
	{
		final ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
		req.setFields("num_iid,num,sku,outer_id,props");
		Long total = 0L;
		ItemsOnsaleGetResponse response = null;
		try
		{
			// final TaobaoClient client = TmallUtils.getConnection();
			response = tabaoclient.execute(req, sessionKey);
			onsaleCount = 0;
			total = response.getTotalResults();
		}
		catch (final ApiException e)
		{
			Log.error("获取出售中的产品总数量异常,重新尝试发送");
			if (onsaleCount < countlimit)
			{
				onsaleCount++;
				this.getTotalItemsOfOnsaleProduct(tabaoclient, sessionKey);
			}
			else
			{
				onsaleCount = 0;
				Log.error("获取出售中的产品总数量异常，超过处理次数，无法处理");
			}
		}
		return total;
	}

	// get total ItemNums of inStock items from tmall
	public Long getTotalItemsOfInstockProduct(final TaobaoClient tabaoclient, final String sessionKey, String banner)
	{
		final ItemsInventoryGetRequest req = new ItemsInventoryGetRequest();
		Long total = 0L;
		req.setFields("num_iid,outer_id,num,props");
		req.setBanner(banner);
		ItemsInventoryGetResponse response = null;
		try
		{
			// final TaobaoClient client = TmallUtils.getConnection();
			response = tabaoclient.execute(req, sessionKey);
			instockCount = 0;
			total = response.getTotalResults();
		}
		catch (final ApiException e)
		{
			Log.error("获取库存中的产品总数量异常,重新尝试发送");
			if (instockCount < countlimit)
			{
				instockCount++;
				this.getTotalItemsOfInstockProduct(tabaoclient, sessionKey, banner);
			}
			else
			{
				instockCount = 0;
				Log.error("获取库存中的产品总数量异常，超过处理次数，无法处理");
			}
		}
		return total;
	}


	@Override
	public ItemQuantityUpdateResponse updateStockInfoWithoutSkus(final TaobaoClient taboclient, final String sessionKey,
			final ProductTm productTm) throws ApiException
	{
		// final TaobaoClient client = TmallUtils.getConnection();
		final ItemQuantityUpdateRequest req = new ItemQuantityUpdateRequest();
		req.setNumIid(productTm.getTmProductId());
		req.setQuantity(Long.valueOf(productTm.getTmFreeQuntity()));
		req.setType(1L);
		ItemQuantityUpdateResponse response;
		response = taboclient.execute(req, sessionKey);
		if (null != response.getSubCode())
		{
			exceptionHandle.handleException(response.getSubCode(), productTm);
		}
		else
		{
			if (null != response.getItem())
			{
				Log.info("库存同步:更新成功的数据:num_iid:" + productTm.getTmProductId() + "outerId:" + response.getItem().getOuterId() + "|库存："
						+ response.getItem().getNum());
			}
			else
			{
				Log.info("库存同步:数据:num_iid:" + productTm.getTmProductId() + "结果异常");
			}
		}
		return response;
	}

	@Override
	public SkusQuantityUpdateResponse updateStockInfoWithSkus(final TaobaoClient taboclient, final String sessionKey,
			final ProductTm productTm) throws ApiException
	{
		final SkusQuantityUpdateRequest req = new SkusQuantityUpdateRequest();
		SkusQuantityUpdateResponse response = null;
		req.setNumIid(productTm.getTmProductId());
		req.setType(1L);

		final List<Sku> skus = productTm.getSkus();
		final int total = skus.size();// get skus size
		final int eachBatchSize = 20;// set pageSize
		int batch = 0;
		if (total <= eachBatchSize)
		{
			batch = 1;
		}
		else if (total % eachBatchSize == 0)
		{
			batch = total / eachBatchSize;
		}
		else
		{
			batch = total / eachBatchSize + 1;
		}

		final List<Sku> tempSkus = new ArrayList<Sku>();

		for (int i = 1; i <= batch; i++)
		{
			if (batch == 1)
			{
				for (int j = 0; j < total; j++)
				{
					if (null == skus.get(j))
					{
						break;
					}
					tempSkus.add(skus.get(j));
				}
			}
			else
			{
				for (int j = (i - 1) * eachBatchSize; j < i * eachBatchSize; j++)
				{
					if (null == skus.get(j))
					{
						break;
					}
					tempSkus.add(skus.get(j));
				}
			}

			req.setSkuidQuantities(this.convertFromListToStringOfSkus(tempSkus));
			response = taboclient.execute(req, sessionKey);
			if (null != response.getItem())
			{
				Log.info("第" + i + "批" + response.getItem().getNumIid() + "商品的skus(套餐)库存更新成功");
			}
			tempSkus.clear();
		}
		return response;
	}

	@Override
	public SkusQuantityUpdateResponse updateStockInfoWithSingleSkus(TaobaoClient taboclient, String sessionKey, ProductTm productTm)
			throws ApiException
	{
		final SkusQuantityUpdateRequest req = new SkusQuantityUpdateRequest();
		SkusQuantityUpdateResponse response = null;
		req.setNumIid(productTm.getTmProductId());
		req.setType(1L);
		if (null == productTm.getSkus())
		{
			req.setOuteridQuantities(productTm.getOuterId() + ":" + productTm.getTmFreeQuntity());
		}
		response = taboclient.execute(req, sessionKey);
		if (null != response.getItem())
		{
			Log.info("库存同步:" + response.getItem().getNumIid() + "|商品的skus(套餐)库存更新成功");
		}
		return response;
	}



	@Override
	public Item getItem(TaobaoClient taboclient, String sessionKey, Long num_iid)
	{
		ItemGetRequest req = new ItemGetRequest();
		req.setFields("detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual");
		req.setNumIid(Long.valueOf(num_iid));
		ItemGetResponse response = null;
		try
		{
			response = taboclient.execute(req, sessionKey);
		}
		catch (ApiException e)
		{
			Log.error("get item from tmall error...");
			return null;
		}
		if (null != response.getItem())
		{
			return response.getItem();
		}
		return null;
	}

	@Override
	public Item getItemByOuterId(TaobaoClient taboclient, String sessionKey, String outerid)
	{
		ItemsCustomGetRequest req = new ItemsCustomGetRequest();
		req.setOuterId(outerid);
		req.setFields("num_iid,sku,num");
		ItemsCustomGetResponse response;
		try
		{
			response = taboclient.execute(req, sessionKey);
		}
		catch (ApiException e)
		{
			Log.error("get item by outerid form tmall error...");
			return null;
		}
		if (null != response.getItems() && response.getItems().size() > 0)
		{
			Log.info("Item:" + outerid + "根据Item外部编码获取天猫商品信息成功");
			return response.getItems().get(0);
		}
		else
		{
			Log.info("Item:" + outerid + "根据Item外部编码获取天猫商品信息失败");
		}
		return null;
	}

	@Override
	public Sku getSkuByOuterId(TaobaoClient taboclient, String sessionKey, String outerid)
	{
		SkusCustomGetRequest req = new SkusCustomGetRequest();
		req.setOuterId(outerid);
		req.setFields("sku_id,num_iid,quantity");
		SkusCustomGetResponse response;
		try
		{
			response = taboclient.execute(req, sessionKey);
		}
		catch (ApiException e)
		{
			Log.error("get item by outerid form tmall error...");
			return null;
		}
		if (null != response.getSkus() && response.getSkus().size() > 0)
		{
			Log.info("SKU:" + outerid + "根据Sku外部编码获取天猫商品信息成功");
			return response.getSkus().get(0);
		}
		else
		{
			Log.info("SKU:" + outerid + "根据Sku外部编码获取天猫商品信息失败，尝试根据Item外部商家编码获取");
		}
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
		for (final Sku sku : skus)
		{
			Log.info("更新套餐编码:" + sku.getOuterId() + "库存:" + sku.getQuantity());
			sb.append(";" + sku.getSkuId() + ":" + sku.getQuantity());
		}
		return sb.substring(1);
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

	@Override
	public int getOnsaleCount()
	{
		return TmallItemDaoImpl.onsaleCount;
	}

	@Override
	public int getInstockCount()
	{
		return TmallItemDaoImpl.instockCount;
	}

}
