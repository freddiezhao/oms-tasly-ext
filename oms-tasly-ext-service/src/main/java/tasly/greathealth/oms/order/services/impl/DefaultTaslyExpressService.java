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
package tasly.greathealth.oms.order.services.impl;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.order.ExpressItemQueryObject;
import tasly.greathealth.oms.api.order.ExpressLocationQueryObject;
import tasly.greathealth.oms.api.order.ExpressQueryObject;
import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.api.order.dto.ExpressItem;
import tasly.greathealth.oms.api.order.dto.Expresslocation;
import tasly.greathealth.oms.domain.order.ExpressData;
import tasly.greathealth.oms.domain.order.ExpressItemsData;
import tasly.greathealth.oms.domain.order.ExpresslocationsData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.services.TaslyExpressService;


/**
 *
 */
public class DefaultTaslyExpressService extends AbstractHybrisService implements TaslyExpressService
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	private ExpressQueryFactory expressQueries;
	private ExpressLocationQueryFactory expressLocationQueries;
	private ExpressItemQueryFactory expressItemQueries;

	/**
	 * @return the expressLocationQueries
	 */
	public ExpressLocationQueryFactory getExpressLocationQueries()
	{
		return expressLocationQueries;
	}

	/**
	 * @param expressLocationQueries the expressLocationQueries to set
	 */
	@Required
	public void setExpressLocationQueries(final ExpressLocationQueryFactory expressLocationQueries)
	{
		this.expressLocationQueries = expressLocationQueries;
	}

	/**
	 * @return the expressQueries
	 */
	public ExpressQueryFactory getExpressQueries()
	{
		return expressQueries;
	}

	/**
	 * @param expressQueries the expressQueries to set
	 */
	@Required
	public void setExpressQueries(final ExpressQueryFactory expressQueries)
	{
		this.expressQueries = expressQueries;
	}

	/**
	 * @return the expressItemQueries
	 */
	public ExpressItemQueryFactory getExpressItemQueries()
	{
		return expressItemQueries;
	}

	/**
	 * @param expressItemQueries the expressItemQueries to set
	 */
	public void setExpressItemQueries(final ExpressItemQueryFactory expressItemQueries)
	{
		this.expressItemQueries = expressItemQueries;
	}

	@Override
	@Transactional
	public ExpressItemsData createOrUpdateExpressItem(final ExpressItem expressItem)
	{
		// YTODO Auto-generated method stub
		ExpressItemsData expressItemsData = this.expressItemIsExist(expressItem.getSkuid(), expressItem.getInnerSource(),
				expressItem.getChannelSource());

		if (expressItemsData == null)
		{
			expressItemsData = getPersistenceManager().create(ExpressItemsData.class);
			expressItemsData.setSkuid(expressItem.getSkuid());
			expressItemsData.setChannel_source(expressItem.getChannelSource());
			expressItemsData.setInner_source(expressItem.getInnerSource());
		}
		try
		{
			expressItemsData.setExpress_code(expressItem.getExpressCode());
			expressItemsData.setStatus(expressItem.getStatus());
		}
		catch (final Exception e)
		{
			LOG.info(e.getMessage());
		}
		return expressItemsData;
	}

	@Override
	@Transactional
	public ExpressItemsData expressItemIsExist(final String skuId, final String innerSource, final String channelSource)
	{
		// YTODO Auto-generated method stub
		ExpressItemsData expressItemData = null;
		try
		{
			expressItemData = getPersistenceManager().createCriteriaQuery(ExpressItemsData.class)
					.where(Restrictions.eq(ExpressItemsData.SKUID, skuId))
					.and(Restrictions.eq(ExpressItemsData.INNER_SOURCE, innerSource))
					.and(Restrictions.eq(ExpressItemsData.CHANNEL_SOURCE, channelSource)).uniqueResult();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			LOG.error("ExpressItem Not Found:" + e);
		}
		finally
		{
			return expressItemData;
		}

	}

	@Override
	@Transactional
	public ExpresslocationsData expressLocationIsExist(final String province, final String channelSource, final String innerSource)
	{
		// YTODO Auto-generated method stub
		ExpresslocationsData expressLocationData = null;
		try
		{
			expressLocationData = getPersistenceManager().createCriteriaQuery(ExpresslocationsData.class)
					.where(Restrictions.eq(ExpresslocationsData.PROVINCE, province))
					.and(Restrictions.eq(ExpresslocationsData.CHANNEL_SOURCE, channelSource))
					.and(Restrictions.eq(ExpresslocationsData.INNER_SOURCE, innerSource)).uniqueResult();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			LOG.error("ExpressLocationData Not Found:" + e);
		}
		finally
		{
			return expressLocationData;
		}
	}

	@Override
	@Transactional
	public ExpresslocationsData createOrUpdateExpressLocation(final Expresslocation expresslocation)
	{
		// YTODO Auto-generated method stub
		ExpresslocationsData expresslocationsData = this.expressLocationIsExist(expresslocation.getProvince(),
				expresslocation.getChannelSource(), expresslocation.getInnerSource());

		if (expresslocationsData == null)
		{
			expresslocationsData = getPersistenceManager().create(ExpresslocationsData.class);
			expresslocationsData.setProvince(expresslocation.getProvince());
			expresslocationsData.setChannel_source(expresslocation.getChannelSource());
			expresslocationsData.setInner_source(expresslocation.getInnerSource());
		}
		try
		{
			expresslocationsData.setExpress_code(expresslocation.getExpressCode());
		}
		catch (final Exception e)
		{
			LOG.info(e.getMessage());
		}


		return expresslocationsData;
	}

	@Override
	@Transactional
	public ExpressData expressIsExist(final String code)
	{
		// YTODO Auto-generated method stub
		ExpressData expressData = null;
		try
		{
			expressData = getPersistenceManager().createCriteriaQuery(ExpressData.class)
					.where(Restrictions.eq(ExpressData.CODE, code)).uniqueResult();

		}
		catch (final ManagedObjectNotFoundException e)
		{
			LOG.error("ExpressData Not Found:" + e);
		}
		finally
		{
			return expressData;
		}
	}

	@Override
	@Transactional
	public ExpressData createOrUpdateExpress(final Express express)
	{
		// YTODO Auto-generated method stub
		ExpressData expressData = this.expressIsExist(express.getCode());
		if (expressData == null)
		{
			expressData = getPersistenceManager().create(ExpressData.class);
			expressData.setCode(express.getCode());
		}

		expressData.setName(express.getName());
		return expressData;
	}

	@Override
	public Page<ExpressData> findExpressByQuery(final ExpressQueryObject queryObject)
	{
		LOG.trace("service->findPagedExpressByQuery");
		final int[] pageNumberAndSize = getPageNumberAndSize(queryObject, 0, getQueryPageSizeDefault());
		return findPaged(this.expressQueries.findExpressByQuery(queryObject), pageNumberAndSize[0], pageNumberAndSize[1]);
	}

	@Override
	@Transactional
	public void deleteExpress(final String code)
	{
		// YTODO Auto-generated method stub
		final ExpressData expressData = expressIsExist(code);
		this.getPersistenceManager().remove(expressData);
	}

	@Override
	public Page<ExpresslocationsData> findExpressLocationByQuery(final ExpressLocationQueryObject queryObject)
	{
		LOG.trace("service->findExpressLocationByQuery");
		final int[] pageNumberAndSize = getPageNumberAndSize(queryObject, 0, getQueryPageSizeDefault());
		return findPaged(this.expressLocationQueries.findExpressByQuery(queryObject), pageNumberAndSize[0], pageNumberAndSize[1]);
	}

	@Override
	@Transactional
	public void deleteExpressLocation(final String province, final String channelSource, final String innerSource)
	{
		// YTODO Auto-generated method stub
		final ExpresslocationsData expressLocationData = expressLocationIsExist(province, channelSource, innerSource);
		this.getPersistenceManager().remove(expressLocationData);
	}

	@Override
	public Page<ExpressItemsData> findExpressItemByQuery(final ExpressItemQueryObject queryObject)
	{
		LOG.trace("service->findExpressItemByQuery");
		final int[] pageNumberAndSize = getPageNumberAndSize(queryObject, 0, getQueryPageSizeDefault());
		final CriteriaQuery<ExpressItemsData> criteriaQuery = this.expressItemQueries.findExpressByQuery(queryObject);
		return findPaged(criteriaQuery, pageNumberAndSize[0], pageNumberAndSize[1]);
	}

	@Override
	@Transactional
	public void deleteExpressItem(final String skuid, final String innerSource, final String channelSource)
	{
		// YTODO Auto-generated method stub
		final ExpressItemsData expressItemData = expressItemIsExist(skuid, innerSource, channelSource);
		this.getPersistenceManager().remove(expressItemData);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public List<ExpressData> getUIAllExpress()
	{
		return super.getPersistenceManager().createCriteriaQuery(ExpressData.class).resultList();
	}

}
