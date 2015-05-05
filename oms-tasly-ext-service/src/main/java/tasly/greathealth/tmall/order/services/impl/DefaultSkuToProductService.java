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
package tasly.greathealth.tmall.order.services.impl;

import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.order.SkuToProductQueryObject;
import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.domain.order.SkuToProductData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.order.services.SkuToProductService;


/**
 * Default implementation of {@link SkuToProductService}.
 */
public class DefaultSkuToProductService extends AbstractHybrisService implements SkuToProductService
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	private SkuToProductQueryFactory skuToProductQueries;


	/**
	 * @return the skuToProductQueries
	 */
	public SkuToProductQueryFactory getSkuToProductQueries()
	{
		return skuToProductQueries;
	}

	/**
	 * @param skuToProductQueries the skuToProductQueries to set
	 */
	@Required
	public void setSkuToProductQueries(final SkuToProductQueryFactory skuToProductQueries)
	{
		this.skuToProductQueries = skuToProductQueries;
	}

	@Override
	public List<SkuToProductData> querySkuToProducts(final String outerId, final String channel, final String innerSource)
			throws EntityNotFoundException
	{
		try
		{
			return this.findAll(super.getPersistenceManager().createCriteriaQuery(SkuToProductData.class)
					.where(Restrictions.eq(SkuToProductData.OUTERID, outerId)).and(Restrictions.eq(SkuToProductData.CHANNEL, channel))
					.and(Restrictions.eq(SkuToProductData.INNERSOURCE, innerSource)));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException(e.getMessage(), e);
		}

	}

	@Override
	public List<SkuToProductData> querySkuToProductByItemId(final String itemId, final String channel, final String innerSource)
			throws EntityNotFoundException
	{
		try
		{
			return this.findAll(super.getPersistenceManager().createCriteriaQuery(SkuToProductData.class)
					.where(Restrictions.eq(SkuToProductData.ITEMID, itemId)).and(Restrictions.eq(SkuToProductData.CHANNEL, channel))
					.and(Restrictions.eq(SkuToProductData.INNERSOURCE, innerSource)));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	public SkuToProductData createSkuToProduct()
	{
		// YTODO Auto-generated method stub
		return super.getPersistenceManager().create(SkuToProductData.class);
	}

	@Override
	public void deleteSkuToProducts(final String outerId, final String channel, final String innerSource)
	{

		final List<SkuToProductData> skuToProductDatas = this.querySkuToProducts(outerId, channel, innerSource);

		if (null != skuToProductDatas && 0 != skuToProductDatas.size())
		{
			final int num = skuToProductDatas.size();
			for (int i = 0; i < num; i++)
			{
				super.getPersistenceManager().remove(skuToProductDatas.get(i));
			}
			super.getPersistenceManager().flush();
		}
	}

	@Override
	public Page<SkuToProductData> findSkuToProductByQuery(final SkuToProductQueryObject queryObject)
	{
		LOG.trace("service->findSkuToProductByQuery");
		// final int[] pageNumberAndSize = getPageNumberAndSize(queryObject, 0, getQueryPageSizeDefault());
		return findPaged(this.skuToProductQueries.findSkuToProductByQuery(queryObject), 0, 1000);
	}

	@Override
	public SkuToProductData updateSkuToProduct(final SkuToProduct skuToProduct)
	{
		SkuToProductData skuToProductData = null;
		try
		{
			skuToProductData = getPersistenceManager().createCriteriaQuery(SkuToProductData.class)
					.where(Restrictions.eq(SkuToProductData.OUTERID, skuToProduct.getOuterId()))
					.and(Restrictions.eq(SkuToProductData.INNERSOURCE, skuToProduct.getInnerSource()))
					.and(Restrictions.eq(SkuToProductData.CHANNEL, skuToProduct.getChannel()))
					.and(Restrictions.eq(SkuToProductData.ITEMID, skuToProduct.getItemId())).uniqueResult();
			// skuToProductData.setItemId(skuToProduct.getItemId());
			skuToProductData.setItemName(skuToProduct.getItemName());
			skuToProductData.setQuantity(skuToProduct.getQuantity());
			skuToProductData.setRatio(skuToProduct.getRatio());
			skuToProductData.setLockStatus(skuToProduct.getLockStatus());
			skuToProductData.setPercent(skuToProduct.getPercent());
		}
		catch (final Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			return skuToProductData;
		}
	}

	@Override
	public List<SkuToProductData> querySkuToProductByItemId(final String channel, final String innerSource)
	{
		try
		{
			return this.findAll(super.getPersistenceManager().createCriteriaQuery(SkuToProductData.class)
					.where(Restrictions.eq(SkuToProductData.CHANNEL, channel))
					.and(Restrictions.eq(SkuToProductData.INNERSOURCE, innerSource)));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}

}
