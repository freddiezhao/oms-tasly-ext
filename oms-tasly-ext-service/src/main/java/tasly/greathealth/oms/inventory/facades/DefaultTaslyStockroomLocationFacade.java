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

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.Populator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.exceptions.ValidationException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.util.ValidationUtils;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationFacade;
import tasly.greathealth.oms.api.inventory.TaslyStockroomLocationFacade;
import tasly.greathealth.oms.api.inventory.TaslyStockroomLocationQueryObject;
import tasly.greathealth.oms.api.inventory.dto.StockroomLocation;
import tasly.greathealth.oms.inventory.domain.TaslyStockroomLocationData;
import tasly.greathealth.oms.inventory.services.StockroomLocationService;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * Default implementation of {@link TaslyItemLocationFacade}.
 */
public class DefaultTaslyStockroomLocationFacade implements TaslyStockroomLocationFacade
{
	private Validator<QueryObject<?>> queryObjectValidator;
	private FailureHandler entityValidationHandler;
	private Converter<TaslyStockroomLocationData, StockroomLocation> locationConverter;
	private Converters converters;
	private StockroomLocationService stockroomLocationService;

	private Populator<StockroomLocation, TaslyStockroomLocationData> locationReversePopulator;
	private Validator<StockroomLocation> locationValidator;

	public Validator<StockroomLocation> getLocationValidator()
	{
		return locationValidator;
	}

	public void setLocationValidator(final Validator<StockroomLocation> locationValidator)
	{
		this.locationValidator = locationValidator;
	}

	private static final Logger LOGGER = OmsLoggerFactory.getOmsinventorylog();

	public Populator<StockroomLocation, TaslyStockroomLocationData> getLocationReversePopulator()
	{
		return locationReversePopulator;
	}

	public void setLocationReversePopulator(final Populator<StockroomLocation, TaslyStockroomLocationData> locationReversePopulator)
	{
		this.locationReversePopulator = locationReversePopulator;
	}

	/**
	 * @return the queryObjectValidator
	 */
	public Validator<QueryObject<?>> getQueryObjectValidator()
	{
		return queryObjectValidator;
	}

	/**
	 * @param queryObjectValidator the queryObjectValidator to set
	 */
	@Required
	public void setQueryObjectValidator(final Validator<QueryObject<?>> queryObjectValidator)
	{
		this.queryObjectValidator = queryObjectValidator;
	}

	/**
	 * @return the entityValidationHandler
	 */
	public FailureHandler getEntityValidationHandler()
	{
		return entityValidationHandler;
	}

	/**
	 * @param entityValidationHandler the entityValidationHandler to set
	 */
	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

	/**
	 * @return the locationConverter
	 */
	public Converter<TaslyStockroomLocationData, StockroomLocation> getLocationConverter()
	{
		return locationConverter;
	}

	/**
	 * @param locationConverter the locationConverter to set
	 */
	@Required
	public void setLocationConverter(final Converter<TaslyStockroomLocationData, StockroomLocation> locationConverter)
	{
		this.locationConverter = locationConverter;
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

	/**
	 * @return the stockroomLocationService
	 */
	public StockroomLocationService getStockroomLocationService()
	{
		return stockroomLocationService;
	}

	/**
	 * @param stockroomLocationService the stockroomLocationService to set
	 */
	@Required
	public void setStockroomLocationService(final StockroomLocationService stockroomLocationService)
	{
		this.stockroomLocationService = stockroomLocationService;
	}

	@Override
	@Transactional
	public Pageable<StockroomLocation> findTaslyStockRoomLocationsByQuery(final TaslyStockroomLocationQueryObject queryObject)
	{
		this.queryObjectValidator.validate("LocationQueryObject", queryObject, this.entityValidationHandler);

		final Page<TaslyStockroomLocationData> pagedLocationDatas = this.stockroomLocationService.findPagedLocations(queryObject);
		final List<StockroomLocation> stockroomLocations = this.converters.convertAll(pagedLocationDatas.getContent(),
				this.locationConverter);

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(Integer.valueOf(pagedLocationDatas.getTotalPages()));
		pageInfo.setTotalResults(Long.valueOf(pagedLocationDatas.getTotalElements()));
		pageInfo.setPageNumber(Integer.valueOf(pagedLocationDatas.getNumber()));

		return new PagedResults(stockroomLocations, pageInfo);
	}

	@Override
	@Transactional
	public StockroomLocation updateStockroomLocation(final StockroomLocation stockroomLocation)
	{
		LOGGER.trace("updateStockRoomLocation");
		this.locationValidator.validate("StockroomLocation", stockroomLocation, this.entityValidationHandler);

		final TaslyStockroomLocationData taslyStockroomLocationData = this.stockroomLocationService
				.getLocationByLocationId(stockroomLocation.getLocationId());

		this.locationReversePopulator.populate(stockroomLocation, taslyStockroomLocationData);
		try
		{
			this.stockroomLocationService.flushStockroomLocation(taslyStockroomLocationData);
		}
		catch (final ValidationException e)
		{
			throw new EntityValidationException(ValidationUtils.getValidationMessage(e.getConstraintViolations()), e);
		}

		return (this.locationConverter.convert(taslyStockroomLocationData));

	}

}
