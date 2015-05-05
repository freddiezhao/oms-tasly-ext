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
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.order.SkuToProductQueryObject;
import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.api.order.dto.UISkuToProduct;
import tasly.greathealth.tamll.api.SkuToProductFacade;
import tasly.greathealth.tamll.api.order.dto.SkuToProductList;
import tasly.greathealth.tamll.api.order.dto.SkuToProductsList;
import tasly.greathealth.thirdparty.packagedto.ItemModel;
import tasly.greathealth.thirdparty.packagedto.PackageOuterIds;
import tasly.greathealth.thirdparty.packagedto.PackageRelationModel;
import tasly.greathealth.thirdparty.packagedto.Result;

import com.sun.jersey.api.client.GenericType;


public class SkuToProductRestClient implements SkuToProductFacade
{
	private GenericRestClient client;
	private static final Logger LOG = LoggerFactory.getLogger(SkuToProductRestClient.class);
	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}

	private static final GenericType<List<SkuToProduct>> SKU_PRODUCT = new GenericType<List<SkuToProduct>>()
	{
		// NOTHING
	};

	@Override
	public Collection<SkuToProduct> querySkuToProducts(final String outerId, final String channelId, final String innerSource)
	{
		try
		{
			return getClient().call("/skuToProduct/" + outerId + "/" + channelId + "/" + innerSource).get(SKU_PRODUCT).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void createSkuToProduct(final SkuToProduct skuToProduct)
	{
		try
		{
			getClient().call("/skuToProduct").post(skuToProduct).getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public PackageRelationModel updateSkuToProductInventoryToThirdPartProcess(final PackageRelationModel packageItemRelationModel)
	{
		PackageRelationModel packageRelationModel = new PackageRelationModel();
		try
		{
			packageRelationModel = getClient().call("/skuToProduct/updatePackageInventoryToThirdParty")
					.put(PackageRelationModel.class, packageItemRelationModel).getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
		return packageRelationModel;
	}

	@Override
	public SkuToProductsList updateSkuToProducts(final List<SkuToProductList> skuToProductsList)
	{
		try
		{
			final SkuToProductsList skuToProductsLists = new SkuToProductsList();
			skuToProductsLists.initializeSkuToProducts(skuToProductsList);
			return getClient().call("/skuToProduct/batchUpdate").put(SkuToProductsList.class, skuToProductsLists).getResult();
		}
		catch (final RestResponseException e)
		{
			LOG.error(e.unwrap(HybrisSystemException.class).toString());
			return null;
		}
		catch (final Exception e)
		{
			if (e instanceof javax.ws.rs.WebApplicationException)
			{
				if (e.getCause().toString().startsWith("javax.xml.bind.UnmarshalException"))
				{
					return null;
				}
			}
			return null;
		}
	}

	@Override
	public void deleteSkuToProducts(final SkuToProduct skuToProduct)
	{
		try
		{
			getClient().call("/skuToProduct/deleteSkuToProduct").delete(SkuToProduct.class, skuToProduct).getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Collection<SkuToProduct> querySkuToProductByitemId(final String itemId, final String channel, final String innerSource)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public SkuToProductsList querySkuToProductsList(final SkuToProductsList skuToProductsList)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable<UISkuToProduct> findSkuToProductByQuery(final SkuToProductQueryObject queryObject)
			throws EntityValidationException
	{
		final RestCallBuilder call = getClient().call("/skuToProduct/findSkuToProductByQuery", new Object[0]);

		this.queryObjectRestCallPopulator.populate(queryObject, call);
		try
		{
			final RestResponse<Set<UISkuToProduct>> response = call.get(new GenericType<Set<UISkuToProduct>>()
			{
				// Nothing
			});
			final Set<UISkuToProduct> skuToProductList = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<UISkuToProduct>(new ArrayList(skuToProductList), pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public SkuToProduct updateSkuToProduct(final SkuToProduct skuToProduct)
	{
		// YTODO Auto-generated method stub
		try
		{
			return getClient().call("/skuToProduct/update").post(SkuToProduct.class, skuToProduct).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
		catch (final Exception e)
		{
			LOG.error("updateSkuToProduct: " + e.getMessage());
		}
		return skuToProduct;
	}

	@Override
	public void deleteSkuToProducts(final String outerId, final String channel, final String innerSource)
	{
		// YTODO Auto-generated method stub
		try
		{
			getClient().call("/skuToProduct/deleteSkuToProduct/%s/%s/%s", new Object[]{outerId, channel, innerSource}).delete()
					.result();
		}
		catch (final RestResponseException e)
		{
			// YTODO Auto-generated catch block
			e.unwrap(HybrisSystemException.class);
		}
	}

	public void setClient(final GenericRestClient genericRestClient)
	{
		this.client = genericRestClient;
	}

	public GenericRestClient getClient()
	{
		return client;
	}

	@Override
	public Collection<SkuToProduct> queryAllRelatedSkuToProductByItemId(final String itemId, final String channel,
			final String innerSource)
	{
		try
		{
			return getClient().call("/skuToProduct/related/" + itemId + "/" + channel + "/" + innerSource).get(SKU_PRODUCT)
					.getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public List<SkuToProduct> querySkuToProduct(final String channel, final String innerSource)
	{
		try
		{
			return getClient().call("/skuToProduct/" + channel + "/" + innerSource).get(SKU_PRODUCT).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}


	@Override
	public Result updateItemQuantity(final ItemModel itemModel)
	{
		Result result = new Result();
		try
		{
			result = getClient().call("/skuToProduct/updateItemQuantity").put(Result.class, itemModel).getResult();

		}
		catch (final RestResponseException e)
		{
			LOG.error("请求出错");
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

	@Override
	public PackageOuterIds getPackageQuantity(final PackageOuterIds packageOuterIds)
	{
		PackageOuterIds packageOuterId = new PackageOuterIds();
		try
		{
			packageOuterId = getClient().call("/skuToProduct/getPackageQuantity").put(PackageOuterIds.class, packageOuterIds)
					.getResult();
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
		}
		return packageOuterId;
	}
}
