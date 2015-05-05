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
package tasly.greathealth.oms.principal.facades.impl;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.principal.DataPrincipalFacade;
import tasly.greathealth.oms.api.principal.dto.DataPrincipal;
import tasly.greathealth.oms.domain.principal.DataPrincipalData;
import tasly.greathealth.oms.principal.services.DataPrincipalService;


/**
 * @author Henter Liu
 */
public class DefaultDataPrincipalFacade implements DataPrincipalFacade
{
	private DataPrincipalService dataPrincipalService;
	private Converter<DataPrincipalData, DataPrincipal> dataPrincipalDataToModelConverter;
	private Converters converters;

	/**
	 * @param dataPrincipalService the dataPrincipalService to set
	 */
	@Required
	public void setDataPrincipalService(final DataPrincipalService dataPrincipalService)
	{
		this.dataPrincipalService = dataPrincipalService;
	}

	/**
	 * @param dataPrincipalDataToModelConverter the dataPrincipalDataToModelConverter to set
	 */
	@Required
	public void setDataPrincipalDataToModelConverter(
			final Converter<DataPrincipalData, DataPrincipal> dataPrincipalDataToModelConverter)
	{
		this.dataPrincipalDataToModelConverter = dataPrincipalDataToModelConverter;
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
	public List<DataPrincipal> batchUpdate(final List<DataPrincipal> dataPrincipals)
	{
		final List<DataPrincipalData> result = dataPrincipalService.batchUpdate(dataPrincipals);
		return converters.convertAll(result, dataPrincipalDataToModelConverter);
	}

	@Override
	public List<DataPrincipal> getAllUsers()
	{
		final List<DataPrincipalData> result = dataPrincipalService.getAllUsers();
		return converters.convertAll(result, dataPrincipalDataToModelConverter);
	}

	@Override
	public List<DataPrincipal> getAllUserGroups()
	{
		final List<DataPrincipalData> result = dataPrincipalService.getAllUserGroups();
		return converters.convertAll(result, dataPrincipalDataToModelConverter);
	}

	@Override
	public DataPrincipal getPrincipalByUid(final String uid)
	{
		final DataPrincipalData dataPrincipalData = dataPrincipalService.getPrincipalByUid(uid);
		final DataPrincipal dataPrincipal = dataPrincipalDataToModelConverter.convert(dataPrincipalData);
		return dataPrincipal;
	}

	@Override
	public DataPrincipal updatePrincipal(final DataPrincipal dataPrincipal)
	{
		return dataPrincipalDataToModelConverter.convert(dataPrincipalService.updatePrincipal(dataPrincipal));
	}
}
