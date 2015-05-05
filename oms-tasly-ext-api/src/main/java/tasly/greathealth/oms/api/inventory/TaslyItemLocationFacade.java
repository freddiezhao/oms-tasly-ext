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
package tasly.greathealth.oms.api.inventory;

import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.List;

import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;


/**
 * The interface TaslyItemLocationService API.
 */
public interface TaslyItemLocationFacade
{
	/**
	 * Creates item location object.
	 *
	 * @category OMS-Extension
	 *
	 * @param product
	 *           the item location to create
	 */
	void create(TaslyItemLocation taslyItemLocation);

	/***
	 * Batch updates item location objects.
	 *
	 * @param taslyItemLocations
	 */
	boolean batchUpdate(List<TaslyItemLocation> taslyItemLocations);

	/**
	 * Get the item location by the itemID passed in parameter.
	 *
	 * @return the requested item location which match the itemID in parameter
	 * @throws EntityNotFoundException
	 *            the Product not found exception
	 */
	List<TaslyItemLocation> getByItemID(String itemID) throws EntityNotFoundException;

	/**
	 *
	 * add by libin.
	 *
	 * 用于增加新渠道时，更新ItemLocations数据，以保证新渠道的商品百分比可以正常分配
	 * 
	 * @param channels
	 * @return
	 * @throws Exception
	 */
	boolean addChannelInit(String channels) throws Exception;
}
