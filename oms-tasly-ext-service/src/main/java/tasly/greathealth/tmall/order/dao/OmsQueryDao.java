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
package tasly.greathealth.tmall.order.dao;

import java.util.Map;

import tasly.greathealth.tmall.order.exception.ProductNotFoundException;
import tasly.greathealth.tmall.order.exception.ProductUnitNotFoundException;



/**
 * DAO for retrieving order from third party platform to OMS
 *
 * @author han dong
 */
public interface OmsQueryDao
{

	String getExpressBySku(String sku, String channel, String innerSource);

	String getExpressByLocation(String province, String channel, String innerSource);

	String getProductUnit(String sku, String channel, String innerSource) throws ProductUnitNotFoundException;

	String getSkuDescription(String sku, String innerSource);

	Map getRentProduct(String sku, String channel, String innerSource) throws ProductNotFoundException;
}
