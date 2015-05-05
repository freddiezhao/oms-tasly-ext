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
package tasly.greathealth.thirdparty.order.common;

import java.util.List;


/**
 * 通用生成订单接口（订单生产者）
 *
 * @author libin
 */
public interface ProduceOrderService
{
	/**
	 * 生成订单
	 *
	 * @return
	 */
	public List<String> produceOrders() throws Exception;

}
