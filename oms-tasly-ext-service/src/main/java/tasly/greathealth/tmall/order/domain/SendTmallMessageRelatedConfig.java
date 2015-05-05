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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.taobao.api.internal.tmc.Message;


/**
 *
 */
public class SendTmallMessageRelatedConfig
{
	protected Client client = null;

	protected String testEnvUrl = "";

	protected int message2sendOneTime = 1;

	protected boolean sendCommands2testEnv = false;

	protected boolean receiveCommandsFromProductEnv = false;

	protected final List<Message> tmallMessageList = new ArrayList<Message>();

	protected WebResource openreceiveWR = null;

	protected WebResource stopreceiveWR = null;

	protected WebResource receivemqWR = null;

	protected WebResource sendmq2testenvWR = null;

	protected WebResource postmq2testenvWR = null;

	protected ExecutorService executor = null;

	public Client getClient()
	{
		if (client == null)
		{
			client = Client.create();
		}

		return client;
	}


	/**
	 * @return the openreceiveWR
	 */
	public WebResource getOpenreceiveWR()
	{
		if (openreceiveWR == null)
		{
			openreceiveWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallmq2testenv/openreceive");
		}
		return openreceiveWR;
	}


	/**
	 * @return the stopreceiveWR
	 */
	public WebResource getStopreceiveWR()
	{
		if (stopreceiveWR == null)
		{
			stopreceiveWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallmq2testenv/stopreceive");
		}
		return stopreceiveWR;
	}


	/**
	 * @return the receivemqWR
	 */
	public WebResource getReceivemqWR()
	{
		if (receivemqWR == null)
		{
			receivemqWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallmq2testenv/receivemq");

		}
		return receivemqWR;
	}

	/**
	 * @return the testEnvUrl
	 */
	public String getTestEnvUrl()
	{
		return testEnvUrl;
	}


	/**
	 * @param testEnvUrl the testEnvUrl to set
	 */
	public void setTestEnvUrl(final String testEnvUrl)
	{
		if (!testEnvUrl.equals(this.testEnvUrl))
		{
			openreceiveWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallmq2testenv/openreceive");
			stopreceiveWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallmq2testenv/stopreceive");
			receivemqWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallmq2testenv/receivemq");
			sendmq2testenvWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallmq2testenv/sendmq2testenv");
			this.testEnvUrl = testEnvUrl;
		}
	}


	/**
	 * @return the message2sendOneTime
	 */
	public int getMessage2sendOneTime()
	{
		return message2sendOneTime;
	}


	/**
	 * @param message2sendOneTime the message2sendOneTime to set
	 */
	public void setMessage2sendOneTime(final int message2sendOneTime)
	{
		this.message2sendOneTime = message2sendOneTime;
	}


	/**
	 * @return the sendCommands2testEnv
	 */
	public boolean isSendCommands2testEnv()
	{
		return sendCommands2testEnv;
	}


	/**
	 * @param sendCommands2testEnv the sendCommands2testEnv to set
	 */
	public void setSendCommands2testEnv(final boolean sendCommands2testEnv)
	{
		this.sendCommands2testEnv = sendCommands2testEnv;
	}


	/**
	 * @return the receiveCommandsFromProductEnv
	 */
	public boolean isReceiveCommandsFromProductEnv()
	{
		return receiveCommandsFromProductEnv;
	}


	/**
	 * @param receiveCommandsFromProductEnv the receiveCommandsFromProductEnv to set
	 */
	public void setReceiveCommandsFromProductEnv(final boolean receiveCommandsFromProductEnv)
	{
		this.receiveCommandsFromProductEnv = receiveCommandsFromProductEnv;
	}


	/**
	 * @return the tmallMessageList
	 */
	public List<Message> getTmallMessageList()
	{
		return tmallMessageList;
	}


	/**
	 * @param message the tmallMessageList to add
	 */
	public void addToTmallMessageList(final Message message)
	{
		this.tmallMessageList.add(message);
	}


	/**
	 * @return the sendmq2testenvWR
	 */
	public WebResource getSendmq2testenvWR()
	{
		if (sendmq2testenvWR == null)
		{
			sendmq2testenvWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallmq2testenv/sendmq2testenv");
		}
		return sendmq2testenvWR;
	}


	/**
	 * @return the executor
	 */
	public ExecutorService getExecutor()
	{
		if (executor == null)
		{
			executor = Executors.newFixedThreadPool(10);
		}
		return executor;
	}


	/**
	 * @return the postmq2testenvWR
	 */
	public WebResource getPostmq2testenvWR()
	{
		if (postmq2testenvWR == null)
		{
			postmq2testenvWR = getClient().resource(testEnvUrl + "/oms-tasly-ext-web/webresources/tmallmq2testenv/postmq2testenv");
		}
		return postmq2testenvWR;
	}


	/**
	 * @param postmq2testenvWR the postmq2testenvWR to set
	 */
	public void setPostmq2testenvWR(final WebResource postmq2testenvWR)
	{
		this.postmq2testenvWR = postmq2testenvWR;
	}

}
