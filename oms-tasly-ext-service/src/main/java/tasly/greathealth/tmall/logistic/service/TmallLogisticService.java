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
package tasly.greathealth.tmall.logistic.service;

import java.util.List;

import tasly.greathealth.oms.domain.erp.DNLogData;
import tasly.greathealth.tmall.logistic.domain.LogisticTmall;


/**
 *
 */
public interface TmallLogisticService
{
	// generate logistic record t tmall
	public void createLogistic();

	// get order and logistic information from oms
	public List<LogisticTmall> getOrderAndLogisticInfo();

	// delete one row
	public void delOneOrderAndLogisticInfo(final List<DNLogData> dnlogs);

}
