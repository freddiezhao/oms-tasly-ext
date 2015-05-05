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
package tasly.greathealth.oms.inventory.facades;

import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.inventory.ItemInfoFacade;
import tasly.greathealth.oms.api.inventory.ItemInfoQueryObject;
import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.inventory.conversion.ItemInfoDataToModelConverter;
import tasly.greathealth.oms.inventory.conversion.ItemInfoModelToDataConverter;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.services.ItemInfoService;


/**
 * Default implementation of {@link ItemInfoFacade}.
 */
public class DefaultItemInfoFacade implements ItemInfoFacade
{
	private ItemInfoService itemInfoService;
	private ItemInfoModelToDataConverter itemInfoModelToDataConverter;
	private ItemInfoDataToModelConverter itemInfoDataToModelConverter;
	private Converters converters;

	/**
	 * @return the itemInfoModelToDataConverter
	 */
	public ItemInfoModelToDataConverter getItemInfoModelToDataConverter()
	{
		return itemInfoModelToDataConverter;
	}

	/**
	 * @return the itemInfoDataToModelConverter
	 */
	public ItemInfoDataToModelConverter getItemInfoDataToModelConverter()
	{
		return itemInfoDataToModelConverter;
	}

	/**
	 * @return the converters
	 */
	public Converters getConverters()
	{
		return converters;
	}

	/**
	 * @return the itemInfoService
	 */
	public ItemInfoService getItemInfoService()
	{
		return itemInfoService;
	}

	@Required
	public void setItemInfoService(final ItemInfoService itemInfoService)
	{
		this.itemInfoService = itemInfoService;
	}

	@Required
	public void setItemInfoModelToDataConverter(final ItemInfoModelToDataConverter itemInfoModelToDataConverter)
	{
		this.itemInfoModelToDataConverter = itemInfoModelToDataConverter;
	}

	@Required
	public void setItemInfoDataToModelConverter(final ItemInfoDataToModelConverter itemInfoDataToModelConverter)
	{
		this.itemInfoDataToModelConverter = itemInfoDataToModelConverter;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Override
	@Transactional
	public void create(final ItemInfo itemInfo)
	{
		itemInfoModelToDataConverter.convert(itemInfo);
	}

	/**
	 * add by libin539 批量更新库存信息
	 */
	@Override
	@Transactional
	public List<?> batchUpdateInventory(final List<ItemInfo> itemInfos) throws Exception
	{
		return itemInfoService.batchUpdateInventory(itemInfos);
	}

	@Override
	@Transactional
	public boolean batchUpdate(final List<ItemInfo> itemInfos) throws Exception
	{
		return itemInfoService.batchUpdate(itemInfos);
	}

	@Override
	@Transactional
	public ItemInfo getBySku(final String sku)
	{
		final ItemInfoData itemInfoData = itemInfoService.getBySku(sku);
		return itemInfoDataToModelConverter.convert(itemInfoData);
	}

	@Override
	public Collection<ItemInfo> getAll()
	{
		final List<ItemInfoData> itemInfos = itemInfoService.getAll();
		return converters.convertAll(itemInfos, itemInfoDataToModelConverter);
	}

	@Override
	public Pageable<ItemInfo> findItemInfosByQuery(final ItemInfoQueryObject query) throws EntityNotFoundException
	{
		final Page<ItemInfoData> itemInfoDatas = itemInfoService.findItemInfosByQuery(query);
		final List<ItemInfo> itemInfos = converters.convertAll(itemInfoDatas.getContent(), itemInfoDataToModelConverter);
		final PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNumber(itemInfoDatas.getNumber());
		pageInfo.setTotalPages(itemInfoDatas.getTotalPages());
		pageInfo.setTotalResults(itemInfoDatas.getTotalElements());
		final PagedResults<ItemInfo> results = new PagedResults<ItemInfo>(itemInfos, pageInfo);
		return results;
	}
}
