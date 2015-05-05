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
package tasly.greathealth.oms.producer.impl;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;

import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.producer.OrderProducer;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandFactory;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;
import tasly.greathealth.tmall.order.domain.SendTmallMessageRelatedConfig;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.top.link.LinkException;


/**
 *
 */
public class TmallOrderProducer2 implements OrderProducer
{

	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	private OmsOrderRetrieveService<Trade> omsOrderRetrieverService;

	private TmcClient tmcClient;

	private InnerSource innerSource;

	private SendTmallMessageRelatedConfig sendTmallMessageRelatedConfig;

	@Override
	public void produceOrder()
	{
		LOG.info("Start to " + innerSource + " connect to TMALL.");
		// try
		// {
		// omsOrderRetrieverService.loadPendingOrders();
		// }
		// catch (final Exception e1)
		// {
		// LOG.error(e1.getMessage(), e1);
		// throw new RuntimeException(e1);
		// }

		tmcClient.setMessageHandler(new MessageHandler()
		{
			@Override
			public void onMessage(final Message message, final MessageStatus status)
			{
				try
				{
					final ExecutorService executor = sendTmallMessageRelatedConfig.getExecutor();
					if (sendTmallMessageRelatedConfig.isSendCommands2testEnv())
					{
						final Runnable send2testEnvTask = new SendTmallMQ2TestEnvThread(sendTmallMessageRelatedConfig, message, LOG);
						LOG.debug("Before execute the thread pool is " + executor.toString());
						executor.execute(send2testEnvTask);
						LOG.debug("After execute the thread pool is " + executor.toString());
					}
					// else
					// {
					// if (!executor.isTerminated())
					// {
					// executor.shutdown();
					// }
					// }

				}
				catch (final Exception e)
				{
					LOG.error("<TmallOrderProducer2Fail> to send message to test env ", e);
				}


				final String topic = message.getTopic();
				LOG.info(Thread.currentThread().getName() + "---------start " + innerSource + " notification-------topic[" + topic
						+ "]");
				final OrderCommand command = OrderCommandFactory.createTmallOrderCommand(omsOrderRetrieverService, message,
						innerSource);
				if (command != null)
				{
					OrderCommandsStorage.getInstance().addOrderCommand(command.getChannelSource(), command.getEventType(), command);
				}

			}
		});
		try
		{
			tmcClient.connect();
		}
		catch (final LinkException e)
		{
			LOG.error("Error happened when connecting to Tmall " + innerSource + " message service." + e.getMessage());
		}
		LOG.info("End to connect to " + innerSource + " TMALL.");
	}

	@Override
	public void stopProduceOrder() throws Exception
	{
		LOG.info("Start to disconnect to " + innerSource + " TMALL.");
		if (tmcClient != null)
		{
			if (tmcClient.isOnline())
			{
				tmcClient.close();
			}
		}
		LOG.info("End to disconnect to " + innerSource + " TMALL.");
	}

	@Override
	public boolean isOnline()
	{
		if (tmcClient != null)
		{
			return tmcClient.isOnline();
		}
		return false;
	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService<Trade> omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}

	/**
	 * @param tmcClient the tmcClient to set
	 */
	public void setTmcClient(final TmcClient tmcClient)
	{
		this.tmcClient = tmcClient;
	}

	/**
	 * @param sendTmallMessageRelatedConfig the sendTmallMessageRelatedConfig to set
	 */
	public void setSendTmallMessageRelatedConfig(final SendTmallMessageRelatedConfig sendTmallMessageRelatedConfig)
	{
		this.sendTmallMessageRelatedConfig = sendTmallMessageRelatedConfig;
	}

	public void setInnerSource(final InnerSource innerSource)
	{
		this.innerSource = innerSource;
	}

}
