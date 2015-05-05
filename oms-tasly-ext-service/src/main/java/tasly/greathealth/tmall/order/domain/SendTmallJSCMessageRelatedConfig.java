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
package tasly.greathealth.tmall.order.domain;

import com.sun.jersey.api.client.WebResource;


/**
 *
 */
public class SendTmallJSCMessageRelatedConfig extends SendTmallMessageRelatedConfig
{

	/**
	 * @return the openreceiveWR
	 */
	@Override
	public WebResource getOpenreceiveWR()
	{
		if (openreceiveWR == null)
		{
			openreceiveWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallJSCmq2testenv/openreceive");
		}
		return openreceiveWR;
	}

	/**
	 * @return the stopreceiveWR
	 */
	@Override
	public WebResource getStopreceiveWR()
	{
		if (stopreceiveWR == null)
		{
			stopreceiveWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallJSCmq2testenv/stopreceive");
		}
		return stopreceiveWR;
	}

	/**
	 * @return the receivemqWR
	 */
	@Override
	public WebResource getReceivemqWR()
	{
		if (receivemqWR == null)
		{
			receivemqWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallJSCmq2testenv/receivemq");

		}
		return receivemqWR;
	}

	/**
	 * @param testEnvUrl the testEnvUrl to set
	 */
	@Override
	public void setTestEnvUrl(final String testEnvUrl)
	{
		if (!testEnvUrl.equals(this.testEnvUrl))
		{
			openreceiveWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallJSCmq2testenv/openreceive");
			stopreceiveWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallJSCmq2testenv/stopreceive");
			receivemqWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallJSCmq2testenv/receivemq");
			sendmq2testenvWR = getClient()
					.resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallJSCmq2testenv/sendmq2testenv");
			this.testEnvUrl = testEnvUrl;
		}
	}

	/**
	 * @return the sendmq2testenvWR
	 */
	@Override
	public WebResource getSendmq2testenvWR()
	{
		if (sendmq2testenvWR == null)
		{
			sendmq2testenvWR = getClient()
					.resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallJSCmq2testenv/sendmq2testenv");
		}
		return sendmq2testenvWR;
	}

	/**
	 * @return the postmq2testenvWR
	 */
	@Override
	public WebResource getPostmq2testenvWR()
	{
		if (postmq2testenvWR == null)
		{
			postmq2testenvWR = getClient()
					.resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallJSCmq2testenv/postmq2testenv");
		}
		return postmq2testenvWR;
	}

}
