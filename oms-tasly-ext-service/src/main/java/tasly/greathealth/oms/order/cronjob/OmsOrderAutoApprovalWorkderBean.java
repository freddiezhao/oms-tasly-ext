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
package tasly.greathealth.oms.order.cronjob;

import com.hybris.kernel.api.JobWorkerBean;

import java.io.Serializable;

import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.facades.impl.DefaultTaslyOrderApprovalFacade;
import tasly.greathealth.thirdparty.order.OrderConstants;


/**
 * Cronjob for automatically approval the OMS order which meets the following condition
 *
 * @author zijie.ai
 */
public class OmsOrderAutoApprovalWorkderBean implements JobWorkerBean
{
	private static final long serialVersionUID = 1L;
	private String beanName;
	private DefaultTaslyOrderApprovalFacade taslyOrderApprovalFacade;
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();

	@Override
	public void execute(final Serializable arg0)
	{

		LOG.info("auto approval is running ...");
		taslyOrderApprovalFacade.approveOrderStatus(OrderConstants.ORDER_APPROVE_STATUS, OrderConstants.ORDER_UNAPPROVE_STATUS,
				OrderConstants.SHIPPING_LOCK_STATUS);
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
	 * @param taslyOrderApprovalFacade the taslyOrderApprovalFacade to set
	 */
	public void setTaslyOrderApprovalFacade(final DefaultTaslyOrderApprovalFacade taslyOrderApprovalFacade)
	{
		this.taslyOrderApprovalFacade = taslyOrderApprovalFacade;
	}

}
