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
package tasly.greathealth.oms.ui.facade.order;

import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.domain.order.ExpressData;
import tasly.greathealth.oms.ui.api.order.UiExpressFacade;
import tasly.greathealth.oms.ui.order.conversion.ExpressInfoDataToModelConverter;
import tasly.greathealth.oms.ui.order.services.ExpressService;


/**
 * Default Ui Express Facade.
 */
@Transactional
public class DefaultUiExpressFacade implements UiExpressFacade
{
	private ExpressService expressService;

	private ExpressInfoDataToModelConverter expressInfoDataToModelConverter;

	private Converters converters;

	@Override
	public List<Express> getUIAllExpress() throws EntityNotFoundException
	{
		final List<ExpressData> expressDataList = this.expressService.getUIAllExpress();
		return converters.convertAll(expressDataList, expressInfoDataToModelConverter);
	}

	/**
	 * @return the expressService
	 */
	public ExpressService getExpressService()
	{
		return expressService;
	}

	/**
	 * @param expressService the expressService to set
	 */
	public void setExpressService(final ExpressService expressService)
	{
		this.expressService = expressService;
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
	public void setExpressInfoDataToModelConverter(final ExpressInfoDataToModelConverter expressInfoDataToModelConverter)
	{
		this.expressInfoDataToModelConverter = expressInfoDataToModelConverter;
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
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}
}
