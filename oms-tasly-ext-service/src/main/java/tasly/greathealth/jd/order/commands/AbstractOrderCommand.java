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
package tasly.greathealth.jd.order.commands;

import org.slf4j.Logger;

import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderConstants;


/**
 * 通用命令抽象类
 *
 * @author libin
 *
 */
public abstract class AbstractOrderCommand implements OrderCommand, OrderConstants
{
	private static final Logger LOG = OmsLoggerFactory.getJdorderlog();


	protected InnerSource innerSource;

	public AbstractOrderCommand(final InnerSource innerSource)
	{
		// 增加液态设置
		this.innerSource = innerSource;
	}


	@Override
	public void run()
	{
		LOG.info("execute command" + this);
		this.execute();
		try
		{
			Thread.sleep(100);
		}
		catch (final InterruptedException e)
		{
			LOG.warn(e.getMessage(), e);
		}

	}

	@Override
	public InnerSource getInnerSource()
	{
		return innerSource;
	}

	public void setInnerSource(final InnerSource innerSource)
	{
		this.innerSource = innerSource;
	}
}
