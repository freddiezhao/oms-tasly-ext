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
package tasly.greathealth.oms.client.inventory;

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
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationQueryObject;
import tasly.greathealth.oms.export.api.stock.ExportStockFacade;
import tasly.greathealth.oms.export.api.stock.dto.ExportStock;

import com.sun.jersey.api.client.GenericType;


/**
 *
 */
public class ExportStockRestClient extends DefaultRestClient implements ExportStockFacade
{
	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}

	@Override
	public Pageable<ExportStock> findPagedExportStocksByQuery(final TaslyItemLocationQueryObject queryObject)
			throws EntityValidationException
			{
		final RestCallBuilder call = getClient().call("/exportstocks", new Object[0]);

		this.queryObjectRestCallPopulator.populate(queryObject, call);
		try
		{
			final RestResponse<List<ExportStock>> response = call.get(new GenericType<List<ExportStock>>()
					{
				// Nothing
					});
			final List<ExportStock> stockList = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<ExportStock>(stockList, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
			}

	@Override
	public List<ExportStock> findListExportStocksByQuery(final TaslyItemLocationQueryObject queryObject)
	{
		final RestCallBuilder call = getClient().call("/exportstocks/all", new Object[0]);
		this.queryObjectRestCallPopulator.populate(queryObject, call);
		try
		{
			final RestResponse<List<ExportStock>> response = call.get(new GenericType<List<ExportStock>>()
					{
				// Nothing
					});
			return response.result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

}
