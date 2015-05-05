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
package tasly.greathealth.oms.client.order;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.order.PendingOrderFacade;
import tasly.greathealth.oms.api.order.PendingOrderQueryObject;
import tasly.greathealth.oms.api.order.dto.PendingOrder;

import com.sun.jersey.api.client.GenericType;


/**
 * @author Henter Liu
 */
public class PendingOrderRestClient extends DefaultRestClient implements PendingOrderFacade
{
	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}

	@Override
	public Pageable<PendingOrder> findPendingOrdersByQuery(final PendingOrderQueryObject queryObject)
	{
		final RestCallBuilder call = getClient().call("pendingorders", new Object[0]);

		this.queryObjectRestCallPopulator.populate(queryObject, call);
		try
		{
			final RestResponse<List<PendingOrder>> response = call.get(new GenericType<List<PendingOrder>>()
			{
				// Nothing
			});
			final List<PendingOrder> ordersList = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<PendingOrder>(ordersList, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void restorePendingOrders(final String tid)
	{
		try
		{
			getClient().call("/pendingorders/restore/" + tid).post().getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void restorePendingOrdersForTmallJSC(final String tid)
	{
		try
		{
			getClient().call("/pendingorders/tmall/jsc/restore/" + tid).post().getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}
}
