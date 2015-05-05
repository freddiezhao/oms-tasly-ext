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
package tasly.greathealth.oms.order.services;

import tasly.greathealth.erp.api.order.updatedelivery.dto.Message;


/**
 *
 */
public interface TaslyOrderLineQuantityService
{
	/**
	 * added by panlin for TS-707/TS-339: update order express code and number, and express memo
	 * last modified on 2015-01-15
	 *
	 * @param message
	 */
	void updateOrderExpressCodeNumberMemo(final Message message);

}
