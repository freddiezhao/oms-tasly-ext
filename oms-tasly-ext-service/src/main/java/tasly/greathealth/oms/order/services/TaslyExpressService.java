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

import com.hybris.kernel.api.Page;

import java.util.List;

import tasly.greathealth.oms.api.order.ExpressItemQueryObject;
import tasly.greathealth.oms.api.order.ExpressLocationQueryObject;
import tasly.greathealth.oms.api.order.ExpressQueryObject;
import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.api.order.dto.ExpressItem;
import tasly.greathealth.oms.api.order.dto.Expresslocation;
import tasly.greathealth.oms.domain.order.ExpressData;
import tasly.greathealth.oms.domain.order.ExpressItemsData;
import tasly.greathealth.oms.domain.order.ExpresslocationsData;


/**
 *
 */
public interface TaslyExpressService
{
	/**
	 *
	 * @param expressItem
	 * @return
	 */
	ExpressItemsData createOrUpdateExpressItem(final ExpressItem expressItem);

	/**
	 * @param skuId
	 * @param innerSource
	 * @param channelSource
	 * @return
	 */
	ExpressItemsData expressItemIsExist(final String skuId, final String innerSource, final String channelSource);

	/**
	 * @param code
	 * @return
	 */
	ExpressData expressIsExist(String code);

	/**
	 * @param queryObject
	 * @return
	 */
	Page<ExpressData> findExpressByQuery(ExpressQueryObject queryObject);

	/**
	 * @param code
	 */
	void deleteExpress(String code);

	/**
	 * @param queryObject
	 * @return
	 */
	Page<ExpresslocationsData> findExpressLocationByQuery(ExpressLocationQueryObject queryObject);

	/**
	 * @param province
	 */
	void deleteExpressLocation(String province, String channelSource, String innerSource);

	/**
	 *
	 * @param queryObject
	 * @return
	 */
	Page<ExpressItemsData> findExpressItemByQuery(ExpressItemQueryObject queryObject);

	/**
	 *
	 * @param province
	 * @param innerSource
	 * @param channelSource
	 */
	void deleteExpressItem(String skuid, String innerSource, String channelSource);

	/**
	 * @return
	 */
	List<ExpressData> getUIAllExpress();

	/**
	 * @param expresslocation
	 * @return
	 */
	ExpresslocationsData createOrUpdateExpressLocation(Expresslocation expresslocation);

	/**
	 * @param express
	 * @return
	 */
	ExpressData createOrUpdateExpress(Express express);

	/**
	 * @param province
	 * @param channelSource
	 * @param innerSource
	 * @return
	 */
	ExpresslocationsData expressLocationIsExist(String province, String channelSource, String innerSource);
}
