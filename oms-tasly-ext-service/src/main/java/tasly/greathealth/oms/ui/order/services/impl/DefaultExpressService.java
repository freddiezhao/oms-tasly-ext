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
package tasly.greathealth.oms.ui.order.services.impl;

import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.domain.order.ExpressData;
import tasly.greathealth.oms.ui.order.services.ExpressService;


/**
 * Default implementation of {@link DefaultExpressService}.
 */
public class DefaultExpressService extends AbstractHybrisService implements ExpressService
{
	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public List<ExpressData> getUIAllExpress()
	{
		return super.getPersistenceManager().createCriteriaQuery(ExpressData.class).resultList();
	}
}
