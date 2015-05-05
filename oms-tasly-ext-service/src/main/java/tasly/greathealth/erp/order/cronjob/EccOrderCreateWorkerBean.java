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
package tasly.greathealth.erp.order.cronjob;

import com.hybris.kernel.api.JobWorkerBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import tasly.greathealth.erp.api.order.UpdateOrderDeliveryStatusFacade;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * @author vincent.yin
 *         Push OMS status="approved" orders to ECC
 */
@Component
public class EccOrderCreateWorkerBean implements JobWorkerBean
{
	private static final long serialVersionUID = 357819854242895588L;

	private static final Logger LOG = OmsLoggerFactory.getErporderlog();

	private String beanName;

	UpdateOrderDeliveryStatusFacade updateOrderDeliveryStatusFacade;

	List<String> orderIdList = new ArrayList<String>();

	@Override
	public void execute(final Serializable arg0)
	{
		LOG.info("开始同步OMS订单到ECC: " + arg0);
		final long beginTime = System.currentTimeMillis();
		try
		{
			orderIdList = updateOrderDeliveryStatusFacade.createEccOrders();
			LOG.info("同步工作完成！同步订单成功数量:" + String.valueOf(null == orderIdList ? 0 : orderIdList.size()) + ",耗时:"
					+ (System.currentTimeMillis() - beginTime) / 1000f + " 秒 ");
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void setBeanName(final String name)
	{
		beanName = name;

	}

	@Override
	public String getBeanName()
	{

		return beanName;
	}

	/**
	 * @return the updateOrderDeliveryStatusFacade
	 */
	public UpdateOrderDeliveryStatusFacade getUpdateOrderDeliveryStatusFacade()
	{
		return updateOrderDeliveryStatusFacade;
	}

	/**
	 * @param updateOrderDeliveryStatusFacade the updateOrderDeliveryStatusFacade to set
	 */
	public void setUpdateOrderDeliveryStatusFacade(final UpdateOrderDeliveryStatusFacade updateOrderDeliveryStatusFacade)
	{
		this.updateOrderDeliveryStatusFacade = updateOrderDeliveryStatusFacade;
	}

}
