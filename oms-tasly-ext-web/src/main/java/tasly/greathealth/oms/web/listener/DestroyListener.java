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
package tasly.greathealth.oms.web.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;


/**
 *
 */
public class DestroyListener implements ApplicationListener<ContextClosedEvent>
{

	private static final Logger LOG = LoggerFactory.getLogger(DestroyListener.class);

	@Override
	public void onApplicationEvent(final ContextClosedEvent paramE)
	{
		LOG.info("===================Destroy!!!!!!!!!!================================");

	}

}
