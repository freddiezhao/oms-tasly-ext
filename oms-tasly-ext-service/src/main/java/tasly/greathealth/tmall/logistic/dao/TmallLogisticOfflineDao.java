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
package tasly.greathealth.tmall.logistic.dao;

import tasly.greathealth.tmall.logistic.domain.LogisticTmall;



/**
 *
 */
public interface TmallLogisticOfflineDao
{
	// create offline logistic in tmall
	public boolean createOfflineLogistic(LogisticTmall logisticTmall);
}
