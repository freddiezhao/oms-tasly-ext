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
package tasly.greathealth.oms.client.inventory;

import com.hybris.commons.client.GenericRestClient;
import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.ItemInfoFacade;
import tasly.greathealth.oms.api.inventory.ItemInfoQueryObject;
import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.api.inventory.dto.ItemInfoList;

import com.sun.jersey.api.client.GenericType;


/**
 * @author Henter Liu
 */
public class ItemInfoRestClient implements ItemInfoFacade
{
	private static final GenericType<Collection<ItemInfo>> ITEM_INFOS = new GenericType<Collection<ItemInfo>>()
	{
		// DO NOTHING
	};
	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}

	private GenericRestClient client;

	public void setClient(final GenericRestClient genericRestClient)
	{
		this.client = genericRestClient;
	}

	public GenericRestClient getClient()
	{
		return client;
	}

	@Override
	public void create(final ItemInfo itemInfo)
	{
		try
		{
			getClient().call("/iteminfo").post(itemInfo).getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public ItemInfo getBySku(final String sku)
	{
		try
		{
			return getClient().call("/iteminfo/sku/" + sku).get(ItemInfo.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Collection<ItemInfo> getAll()
	{
		try
		{
			return getClient().call("/iteminfo/all").get(ITEM_INFOS).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	// SOAP批量更新库存使用
	@Override
	public List<?> batchUpdateInventory(final List<ItemInfo> itemInfos)
	{
		return null;
	}

	@Override
	public boolean batchUpdate(final List<ItemInfo> itemInfos) throws Exception
	{
		try
		{
			final ItemInfoList itemInfoList = new ItemInfoList();
			itemInfoList.initializeItemInfos(itemInfos);
			return getClient().call("/iteminfo").put(ItemInfoList.class, itemInfoList).getResult() != null;
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
		catch (final Exception e)
		{
			if (e instanceof javax.ws.rs.WebApplicationException)
			{
				if (e.getCause().toString().startsWith("javax.xml.bind.UnmarshalException"))
				{
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public Pageable<ItemInfo> findItemInfosByQuery(final ItemInfoQueryObject query) throws EntityNotFoundException
	{
		final RestCallBuilder call = getClient().call("/iteminfo", new Object[0]);

		this.queryObjectRestCallPopulator.populate(query, call);
		try
		{
			final RestResponse<List<ItemInfo>> response = call.get(new GenericType<List<ItemInfo>>()
			{
				// Nothing
			});
			final List<ItemInfo> itemInfoList = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<ItemInfo>(itemInfoList, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}
}
