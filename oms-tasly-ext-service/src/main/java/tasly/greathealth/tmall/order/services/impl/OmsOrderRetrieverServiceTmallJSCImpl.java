/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.tmall.order.services.impl;

import com.taobao.api.domain.Trade;



/**
 * An implementation for retrieving order information from Tmall JSC
 *
 * @author Gao Xin
 */
public class OmsOrderRetrieverServiceTmallJSCImpl extends OmsOrderRetrieverServiceTmallImpl
{

	private TmallRestClient restClient;


	public TmallRestClient getRestClient()
	{
		return restClient;
	}

	public void setRestClient(final TmallRestClient restClient)
	{
		this.restClient = restClient;
	}


	@Override
	public Trade retrieveOrderDetail(final String orderId) throws Exception
	{

		final Trade trade = restClient.getTradeFromTmallJSC(orderId);

		return trade;
	}
}
