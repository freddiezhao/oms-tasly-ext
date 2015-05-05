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
package tasly.greathealth.thirdparty.order;

import com.hybris.kernel.api.JobWorkerBean;

import java.io.Serializable;

import org.slf4j.Logger;

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.log.OmsLoggerFactory;



/**
 *
 */
public class OrderCronjobConsumer implements JobWorkerBean
{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();
	private String beanName;
	private ChannelSource channelSource;
	private EventType eventType;

	@Override
	public void execute(final Serializable arg0)
	{
		LOG.info(Thread.currentThread().getName() + " Consume Order. Channel Source[" + channelSource + "] EventType[" + eventType
				+ "]");
		final OrderCommand command = OrderCommandsStorage.getInstance().getOrderCommand(channelSource, eventType);
		if (command != null)
		{
			command.execute();
		}
		LOG.info(Thread.currentThread().getName() + " Consume Order Done!");
	}

	@Override
	public void setBeanName(final String beanName)
	{
		this.beanName = beanName;

	}

	@Override
	public String getBeanName()
	{
		return beanName;
	}

	/**
	 * @param channelSource the channelSource to set
	 */
	public void setChannelSource(final ChannelSource channelSource)
	{
		this.channelSource = channelSource;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(final EventType eventType)
	{
		this.eventType = eventType;
	}
}
