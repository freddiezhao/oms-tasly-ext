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

import tasly.greathealth.tmall.order.services.impl.OrderContext;


/**
 *
 */
public interface OrderConvertService<T, O>
{
	public T convertTrade() throws Exception;

	public void setOriginalOrder(T t);

	public void setOrderContext(OrderContext orderContext);
}
