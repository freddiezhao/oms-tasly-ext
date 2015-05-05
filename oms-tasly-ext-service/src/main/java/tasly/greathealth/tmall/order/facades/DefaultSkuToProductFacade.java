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
package tasly.greathealth.tmall.order.facades;

import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.exception.EntityValidationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.order.SkuToProductQueryObject;
import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.api.order.dto.UISkuToProduct;
import tasly.greathealth.oms.domain.order.SkuToProductData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tamll.api.SkuToProductFacade;
import tasly.greathealth.tamll.api.order.dto.SkuToProductList;
import tasly.greathealth.tamll.api.order.dto.SkuToProductsList;
import tasly.greathealth.thirdparty.packagedto.ItemModel;
import tasly.greathealth.thirdparty.packagedto.PackageOuterIds;
import tasly.greathealth.thirdparty.packagedto.PackageRelationModel;
import tasly.greathealth.thirdparty.packagedto.Result;
import tasly.greathealth.tmall.order.conversion.SkuToProductDataToModelConverter;
import tasly.greathealth.tmall.order.conversion.SkuToProductModelToDataConverter;
import tasly.greathealth.tmall.order.services.SkuToProductService;


/**
 * Default implementation of {@link SkuToProductFacade}.
 */
public class DefaultSkuToProductFacade implements SkuToProductFacade
{
	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();
	private SkuToProductService skuToProductService;
	private SkuToProductModelToDataConverter skuToProductModelToDataConverter;
	private SkuToProductDataToModelConverter skuToProductDataToModelConverter;
	private Converters converters;

	@Override
	@Transactional
	public SkuToProduct updateSkuToProduct(final SkuToProduct skuToProduct)
	{
		if (null != skuToProduct)
		{
			final SkuToProductData newSkuProductData = this.skuToProductService.updateSkuToProduct(skuToProduct);
			return skuToProductDataToModelConverter.convert(newSkuProductData);
		}
		return skuToProduct;
	}

	@Override
	@Transactional
	public Collection<SkuToProduct> querySkuToProducts(final String outerId, final String channel, final String innerSource)
	{
		final List<SkuToProductData> skuToProductDatas = skuToProductService.querySkuToProducts(outerId, channel, innerSource);
		return converters.convertAll(skuToProductDatas, skuToProductDataToModelConverter);
	}

	@Override
	@Transactional
	public Collection<SkuToProduct> querySkuToProductByitemId(final String itemId, final String channel, final String innerSource)
	{
		final List<SkuToProductData> skuToProductDatas = skuToProductService
				.querySkuToProductByItemId(itemId, channel, innerSource);
		return converters.convertAll(skuToProductDatas, skuToProductDataToModelConverter);
	}

	private List<SkuToProductData> results = null;
	private Queue<String> outerIds = null;
	private Queue<String> itemIds = null;
	private List<String> handledOuterIds = null;
	private List<String> handledItemIds = null;
	private List<String> handledKeys = null;

	@Override
	@Transactional
	public Collection<SkuToProduct> queryAllRelatedSkuToProductByItemId(final String itemId, final String channel,
			final String innerSource)
	{
		results = new LinkedList<SkuToProductData>();

		outerIds = new LinkedList<String>();
		itemIds = new LinkedList<String>();

		handledOuterIds = new LinkedList<String>();
		handledItemIds = new LinkedList<String>();

		handledKeys = new LinkedList<String>();

		final List<SkuToProductData> skuToProductDatas = skuToProductService
				.querySkuToProductByItemId(itemId, channel, innerSource);
		for (final SkuToProductData skuToProductData : skuToProductDatas)
		{
			outerIds.offer(skuToProductData.getOuterId());
			itemIds.offer(skuToProductData.getItemId());
		}
		while (outerIds.size() > 0 || itemIds.size() > 0)
		{
			query(channel, innerSource);
		}
		return converters.convertAll(results, skuToProductDataToModelConverter);
	}

	private void query(final String channel, final String innerSource)
	{
		if (outerIds.size() > 0)
		{
			final String outerId = outerIds.poll();
			final List<SkuToProductData> skuToProductDatas = skuToProductService.querySkuToProducts(outerId, channel, innerSource);
			for (final SkuToProductData skuToProductData : skuToProductDatas)
			{
				final String key = skuToProductData.getId().getKey();
				final String itemId = skuToProductData.getItemId();
				if (handledOuterIds.contains(outerId))
				{
					if (handledItemIds.contains(itemId))
					{
						continue;
					}
					else
					{
						if (handledKeys.contains(key))
						{
							continue;
						}
						handledKeys.add(key);
						results.add(skuToProductData);
						itemIds.add(itemId);
					}
				}
				else
				{
					handledOuterIds.add(outerId);
					if (handledKeys.contains(key))
					{
						continue;
					}
					handledKeys.add(key);
					results.add(skuToProductData);
					itemIds.add(itemId);
				}
			}
		}
		if (itemIds.size() > 0)
		{
			final String itemId = itemIds.poll();
			final List<SkuToProductData> skuToProductDatas = skuToProductService.querySkuToProductByItemId(itemId, channel,
					innerSource);
			for (final SkuToProductData skuToProductData : skuToProductDatas)
			{
				final String key = skuToProductData.getId().getKey();
				final String outerId = skuToProductData.getOuterId();
				if (handledItemIds.contains(itemId))
				{
					if (handledOuterIds.contains(outerId))
					{
						continue;
					}
					else
					{
						if (handledKeys.contains(key))
						{
							continue;
						}
						handledKeys.add(key);
						results.add(skuToProductData);
						outerIds.add(outerId);
					}
				}
				else
				{
					handledItemIds.add(itemId);
					if (handledKeys.contains(key))
					{
						continue;
					}
					handledKeys.add(key);
					results.add(skuToProductData);
					outerIds.add(outerId);
				}
			}
		}
	}

	@Override
	@Transactional
	public void createSkuToProduct(final SkuToProduct skuToProduct)
	{
		skuToProductModelToDataConverter.convert(skuToProduct);
	}

	@Override
	@Transactional
	public SkuToProductsList updateSkuToProducts(final List<SkuToProductList> skuToProductLists)
	{
		final SkuToProductsList paraSkuToProductsList = new SkuToProductsList();
		LOG.info(".......进入Rest服务updateSkuToProducts方法.......");
		final int num = skuToProductLists.size();
		for (int i = 0; i < num; i++)
		{
			final List<SkuToProduct> skuToProducts = skuToProductLists.get(i).getSkuToProducts();
			if (null != skuToProducts && 0 != skuToProducts.size())
			{
				final List<SkuToProduct> returnSkuToProductList = this
						.updateSkuToProduct(skuToProductLists.get(i).getSkuToProducts());
				final SkuToProductList paraSkuToProductList = new SkuToProductList();
				paraSkuToProductList.initializeSkuToProducts(returnSkuToProductList);
				paraSkuToProductsList.addSkuToProducts(paraSkuToProductList);
			}
		}
		LOG.info(".......离开Rest服务updateSkuToProducts方法.......");
		return paraSkuToProductsList;
	}

	@Override
	public PackageRelationModel updateSkuToProductInventoryToThirdPartProcess(final PackageRelationModel packageItemRelationModel)
	{
		return null;
	}

	private List<SkuToProduct> updateSkuToProduct(final List<SkuToProduct> skuToProducts)
	{
		final SkuToProduct skuToProduct = skuToProducts.get(0);
		final String channel = skuToProduct.getChannel();
		final String outerId = skuToProduct.getOuterId();
		final String innerSource = skuToProduct.getInnerSource();
		final List<SkuToProduct> localSkuToProducts = (List<SkuToProduct>) this.querySkuToProducts(outerId, channel, innerSource);

		if (null != localSkuToProducts && 0 != localSkuToProducts.size())
		{
			skuToProductService.deleteSkuToProducts(outerId, channel, innerSource);
			final int num = skuToProducts.size();
			for (int i = 0; i < num; i++)
			{
				skuToProductModelToDataConverter.convert(skuToProducts.get(i));
			}
		}
		else
		{
			final int num = skuToProducts.size();
			for (int i = 0; i < num; i++)
			{
				skuToProductModelToDataConverter.convert(skuToProducts.get(i));
			}
		}
		return skuToProducts;
	}

	@Override
	@Transactional
	public SkuToProductsList querySkuToProductsList(final SkuToProductsList skuToProductsList)
	{
		final List<SkuToProductList> paraList = skuToProductsList.getSkuToProductsList();
		final SkuToProductsList returnSkuToProductsList = new SkuToProductsList();
		if (null != paraList && 0 != paraList.size())
		{
			final int num = paraList.size();
			for (int i = 0; i < num; i++)
			{
				final SkuToProductList skuToProductList = paraList.get(i);
				final SkuToProduct skuToProduct = skuToProductList.getSkuToProducts().get(0);
				final List<SkuToProduct> localNewSkuToProducts = (List<SkuToProduct>) this.querySkuToProducts(
						skuToProduct.getOuterId(), skuToProduct.getChannel(), skuToProduct.getInnerSource());
				LOG.info("..........localNewSkuToProducts.........size = " + localNewSkuToProducts.size());
				LOG.info("outerId = " + skuToProduct.getOuterId() + " channel = " + skuToProduct.getChannel() + "innerSource = "
						+ skuToProduct.getInnerSource());
				if (0 != localNewSkuToProducts.size())
				{
					final SkuToProductList returnSkuToProductList = new SkuToProductList();
					returnSkuToProductList.initializeSkuToProducts(localNewSkuToProducts);
					returnSkuToProductsList.addSkuToProducts(returnSkuToProductList);
				}
			}
		}
		return returnSkuToProductsList;
	}

	@Override
	@Transactional
	public void deleteSkuToProducts(final SkuToProduct skuToProduct)
	{
		skuToProductService
		.deleteSkuToProducts(skuToProduct.getOuterId(), skuToProduct.getChannel(), skuToProduct.getInnerSource());
	}

	@Override
	@Transactional
	public void deleteSkuToProducts(final String outerId, final String channel, final String innerSource)
	{
		skuToProductService.deleteSkuToProducts(outerId, channel, innerSource);
	}

	@Required
	public void setSkuToProductService(final SkuToProductService skuToProductService)
	{
		this.skuToProductService = skuToProductService;
	}

	@Required
	public void setSkuToProductModelToDataConverter(final SkuToProductModelToDataConverter skuToProductModelToDataConverter)
	{
		this.skuToProductModelToDataConverter = skuToProductModelToDataConverter;
	}

	@Required
	public void setSkuToProductDataToModelConverter(final SkuToProductDataToModelConverter skuToProductDataToModelConverter)
	{
		this.skuToProductDataToModelConverter = skuToProductDataToModelConverter;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Override
	@Transactional
	public List<SkuToProduct> querySkuToProduct(final String channel, final String innerSource)
	{
		final List<SkuToProductData> skuToProductDatas = skuToProductService.querySkuToProductByItemId(channel, innerSource);
		return converters.convertAll(skuToProductDatas, skuToProductDataToModelConverter);
	}

	@Override
	public Result updateItemQuantity(final ItemModel itemModel)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public PackageOuterIds getPackageQuantity(final PackageOuterIds packageOuterIds)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Pageable<UISkuToProduct> findSkuToProductByQuery(final SkuToProductQueryObject queryObject)
			throws EntityValidationException
	{
		LOG.trace("facade->findSkuToProductByQuery");
		final Page<SkuToProductData> skuToProductDatas = this.skuToProductService.findSkuToProductByQuery(queryObject);
		final List<SkuToProduct> skuToProductList = this.converters.convertAll(skuToProductDatas.getContent(),
				this.skuToProductDataToModelConverter);

		final Set<UISkuToProduct> uiSkuSet = new HashSet<UISkuToProduct>();
		for (final SkuToProduct sku : skuToProductList)
		{
			final UISkuToProduct uiSku = new UISkuToProduct();
			uiSku.setOuterId(sku.getOuterId());
			uiSku.setChannel(sku.getChannel());
			uiSku.setInnerSource(sku.getInnerSource());
			uiSku.setLockStatus(sku.getLockStatus());

			uiSkuSet.add(uiSku);
		}

		final int startIndex = queryObject.getPageNumber() * queryObject.getPageSize();
		final int endIndex = uiSkuSet.size() < startIndex + queryObject.getPageSize() ? uiSkuSet.size() : startIndex
				+ queryObject.getPageSize();

		final List<UISkuToProduct> skuSetToList = new ArrayList<UISkuToProduct>(uiSkuSet);
		final List<UISkuToProduct> newSkuToList = new LinkedList<UISkuToProduct>();
		for (int i = startIndex; i < endIndex; i++)
		{
			newSkuToList.add(skuSetToList.get(i));
		}
		// (uiSkuSet.size() + queryObject.getPageNumber() - 1) / queryObject.getPageNumber()
		final PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNumber(queryObject.getPageNumber());
		pageInfo.setTotalPages((uiSkuSet.size() + queryObject.getPageSize() - 1) / queryObject.getPageSize());
		pageInfo.setTotalResults((long) uiSkuSet.size());
		return new PagedResults<UISkuToProduct>(newSkuToList, pageInfo);
	}
}
