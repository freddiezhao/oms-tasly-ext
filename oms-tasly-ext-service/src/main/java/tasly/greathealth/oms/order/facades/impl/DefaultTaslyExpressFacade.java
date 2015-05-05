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
package tasly.greathealth.oms.order.facades.impl;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.order.ExpressItemQueryObject;
import tasly.greathealth.oms.api.order.ExpressLocationQueryObject;
import tasly.greathealth.oms.api.order.ExpressQueryObject;
import tasly.greathealth.oms.api.order.TaslyExpressFacade;
import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.api.order.dto.ExpressItem;
import tasly.greathealth.oms.api.order.dto.Expresslocation;
import tasly.greathealth.oms.domain.order.ExpressData;
import tasly.greathealth.oms.domain.order.ExpressItemsData;
import tasly.greathealth.oms.domain.order.ExpresslocationsData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.services.TaslyExpressService;
import tasly.greathealth.oms.ui.order.conversion.ExpressInfoDataToModelConverter;


/**
 *
 */
public class DefaultTaslyExpressFacade implements TaslyExpressFacade
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	private TaslyExpressService taslyExpressService;
	private Converter<ExpressData, Express> expressConverter;
	private Converter<ExpresslocationsData, Expresslocation> expressLocationConverter;
	private Converter<ExpressItemsData, ExpressItem> expressItemConverter;
	private ExpressInfoDataToModelConverter expressInfoDataToModelConverter;
	private Converters converters;


	/**
	 * @return the expressLocationConverter
	 */
	public Converter<ExpresslocationsData, Expresslocation> getExpressLocationConverter()
	{
		return expressLocationConverter;
	}

	/**
	 * @param expressLocationConverter the expressLocationConverter to set
	 */
	@Required
	public void setExpressLocationConverter(final Converter<ExpresslocationsData, Expresslocation> expressLocationConverter)
	{
		this.expressLocationConverter = expressLocationConverter;
	}

	/**
	 * @return the expressLocationConverter
	 */
	public Converter<ExpressItemsData, ExpressItem> getExpressItemConverter()
	{
		return expressItemConverter;
	}

	/**
	 * @param expressItemConverter the expressItemConverter to set
	 */
	@Required
	public void setExpressItemConverter(final Converter<ExpressItemsData, ExpressItem> expressItemConverter)
	{
		this.expressItemConverter = expressItemConverter;
	}

	/**
	 * @return the expressInfoDataToModelConverter
	 */
	public ExpressInfoDataToModelConverter getExpressInfoDataToModelConverter()
	{
		return expressInfoDataToModelConverter;
	}

	/**
	 * @param expressInfoDataToModelConverter the expressInfoDataToModelConverter to set
	 */
	@Required
	public void setExpressInfoDataToModelConverter(final ExpressInfoDataToModelConverter expressInfoDataToModelConverter)
	{
		this.expressInfoDataToModelConverter = expressInfoDataToModelConverter;
	}

	/**
	 * @return the expressConverter
	 */
	public Converter<ExpressData, Express> getExpressConverter()
	{
		return expressConverter;
	}

	/**
	 * @param expressConverter the expressConverter to set
	 */
	@Required
	public void setExpressConverter(final Converter<ExpressData, Express> expressConverter)
	{
		this.expressConverter = expressConverter;
	}

	/**
	 * @return the converters
	 */
	public Converters getConverters()
	{
		return converters;
	}

	/**
	 * @param converters the converters to set
	 */
	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Override
	public ExpressItem createOrUpdataExpressItem(ExpressItem expressItem)
	{
		// YTODO Auto-generated method stub
		if (null != expressItem)
		{
			final ExpressItemsData expressItemData = this.taslyExpressService.createOrUpdateExpressItem(expressItem);
			expressItem = expressItemConverter.convert(expressItemData);
		}
		return expressItem;
	}

	@Override
	public Expresslocation createOrUpdataExpresslocation(Expresslocation expresslocation)
	{
		// YTODO Auto-generated method stub
		if (null != expresslocation)
		{
			final ExpresslocationsData newLocationData = this.taslyExpressService.createOrUpdateExpressLocation(expresslocation);
			expresslocation = expressLocationConverter.convert(newLocationData);
		}
		return expresslocation;
	}

	/**
	 * @return the taslyExpressService
	 */
	public TaslyExpressService getTaslyExpressService()
	{
		return taslyExpressService;
	}

	/**
	 * @param taslyExpressService the taslyExpressService to set
	 */
	public void setTaslyExpressService(final TaslyExpressService taslyExpressService)
	{
		this.taslyExpressService = taslyExpressService;
	}

	@Override
	public Express createOrUpdataExpress(Express express)
	{
		// YTODO Auto-generated method stub
		if (null != express)
		{
			final ExpressData newExpressData = this.taslyExpressService.createOrUpdateExpress(express);
			express = expressConverter.convert(newExpressData);
		}
		return express;
	}

	@Override
	@Transactional
	public Pageable<Express> findExpressByQuery(final ExpressQueryObject queryObject) throws EntityValidationException
	{
		LOG.trace("facade->findExpressByQuery");

		final Page<ExpressData> expressDatas = this.taslyExpressService.findExpressByQuery(queryObject);
		final List<Express> expressList = this.converters.convertAll(expressDatas.getContent(), this.expressConverter);
		final PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNumber(expressDatas.getNumber());
		pageInfo.setTotalPages(expressDatas.getTotalPages());
		pageInfo.setTotalResults(expressDatas.getTotalElements());
		final PagedResults<Express> results = new PagedResults<Express>(expressList, pageInfo);
		return results;
	}

	@Override
	public void deleteExpress(final String code)
	{
		// YTODO Auto-generated method stub
		this.taslyExpressService.deleteExpress(code);
	}

	@Override
	@Transactional
	public Pageable<Expresslocation> findExpressLocationByQuery(final ExpressLocationQueryObject queryObject)
			throws EntityValidationException
	{
		LOG.trace("facade->findExpressByQuery");
		final Page<ExpresslocationsData> expressLocationDatas = this.taslyExpressService.findExpressLocationByQuery(queryObject);
		final List<Expresslocation> expressList = this.converters.convertAll(expressLocationDatas.getContent(),
				this.expressLocationConverter);
		final PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNumber(expressLocationDatas.getNumber());
		pageInfo.setTotalPages(expressLocationDatas.getTotalPages());
		pageInfo.setTotalResults(expressLocationDatas.getTotalElements());
		final PagedResults<Expresslocation> results = new PagedResults<Expresslocation>(expressList, pageInfo);
		return results;
	}

	@Override
	public void deleteExpressLocation(final String province, final String channelSource, final String innerSource)
	{
		// YTODO Auto-generated method stub
		this.taslyExpressService.deleteExpressLocation(province, channelSource, innerSource);
	}

	@Override
	@Transactional
	public Pageable<ExpressItem> findExpressItemByQuery(final ExpressItemQueryObject queryObject) throws EntityValidationException
	{
		LOG.trace("facade->findExpressByQuery");
		final Page<ExpressItemsData> expressItemDatas = this.taslyExpressService.findExpressItemByQuery(queryObject);
		final List<ExpressItem> expressList = this.converters.convertAll(expressItemDatas.getContent(), this.expressItemConverter);
		final PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNumber(expressItemDatas.getNumber());
		pageInfo.setTotalPages(expressItemDatas.getTotalPages());
		pageInfo.setTotalResults(expressItemDatas.getTotalElements());
		final PagedResults<ExpressItem> results = new PagedResults<ExpressItem>(expressList, pageInfo);
		return results;
	}

	@Override
	public void deleteExpressItem(final String skuid, final String innerSource, final String channelSource)
	{
		// YTODO Auto-generated method stub
		this.taslyExpressService.deleteExpressItem(skuid, innerSource, channelSource);
	}

	@Override
	public List<Express> getUIAllExpress() throws EntityNotFoundException
	{
		final List<ExpressData> expressDataList = this.taslyExpressService.getUIAllExpress();
		return converters.convertAll(expressDataList, expressInfoDataToModelConverter);
	}
}
