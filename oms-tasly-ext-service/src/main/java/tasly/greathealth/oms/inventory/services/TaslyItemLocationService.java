/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.inventory.services;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.service.Flushable;

import java.util.List;

import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;


/**
 * Service providing methods to manipulate {@link TaslyItemLocationData} object.
 */
public interface TaslyItemLocationService extends Flushable
{
	/**
	 * Creates an item location.
	 */
	TaslyItemLocationData createTaslyItemLocation();

	/***
	 * Batch updates an item location
	 *
	 * @param taslyItemLocations
	 * @return
	 */
	boolean batchUpdateTaslyItemLocation(List<TaslyItemLocation> taslyItemLocations);

	/**
	 * qiuxingjie add for TS-194
	 * Get the item location by the itemID passed in parameter.
	 *
	 * @return the requested item location which match the itemID in parameter
	 * @throws EntityNotFoundException
	 *            the Product not found exception
	 */
	List<TaslyItemLocationData> getByItemID(String itemID) throws EntityNotFoundException;

	/**
	 * add by libin
	 *
	 * Get all the item Location.
	 *
	 * @return a list of all the existing ItemLocation info
	 */
	List<TaslyItemLocationData> getAll();
}
