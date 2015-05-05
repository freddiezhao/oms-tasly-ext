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
package tasly.greathealth.oms.inventory.services;

import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;

import java.util.List;

import tasly.greathealth.oms.api.inventory.TaslyStockroomLocationQueryObject;
import tasly.greathealth.oms.inventory.domain.TaslyStockroomLocationData;


/**
 *
 */
public interface StockroomLocationService
{

	/**
	 * @param queryObject
	 * @return
	 */
	Page<TaslyStockroomLocationData> findPagedLocations(TaslyStockroomLocationQueryObject queryObject);

	public abstract TaslyStockroomLocationData getLocationByLocationId(String paramString) throws EntityNotFoundException;

	public abstract TaslyStockroomLocationData flushStockroomLocation(TaslyStockroomLocationData taslyStockroomLocationData)
			throws EntityValidationException;

	public abstract List<TaslyStockroomLocationData> getAll();
}
