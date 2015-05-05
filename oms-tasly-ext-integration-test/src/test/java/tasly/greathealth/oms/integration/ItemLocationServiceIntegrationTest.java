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
package tasly.greathealth.oms.integration;

import static org.junit.Assert.assertTrue;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.Location;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tasly.greathealth.oms.api.inventory.ItemInfoFacade;
import tasly.greathealth.oms.api.inventory.TaslyItemLocationFacade;
import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.api.inventory.dto.TaslyItemLocation;


/**
 * Item Location Service Integration Test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-tasly-ext-integration-test-spring.xml"})
public class ItemLocationServiceIntegrationTest
{
	@Autowired
	private TaslyItemLocationFacade taslyItemLocationFacade;
	@Autowired
	private InventoryFacade inventoryFacade;
	@Autowired
	private ItemInfoFacade itemInfoFacade;

	private final List<TaslyItemLocation> taslyItemLocations = new ArrayList<TaslyItemLocation>();

	@Before
	public void init()
	{
		// Before initialize the testing data, please find a existing TaslyItemLocation type code record in your DB, then
		// set the related value in following parameters.
		final TaslyItemLocation taslyItemLocation = new TaslyItemLocation();
		final Location location = new Location();
		// Set the existing stockroomLocations - locationid
		location.setLocationId("jumei");
		taslyItemLocation.setLocation(location);
		// Set the existing itemlocaitons - itemid
		taslyItemLocation.setItemId("107701");
		taslyItemLocation.setAllocationPercent(80);

		taslyItemLocations.add(taslyItemLocation);
	}

	// @Test
	public void get()
	{
		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject();
		queryObject.setValue("skuId", "107701");
		final Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
		final List<ItemLocation> results = itemLocations.getResults();
		System.out.println(results.size());
		assertTrue(results.size() == 2);
	}

	// @Test
	public void batchUpdateTaslyItemLocations()
	{
		taslyItemLocationFacade.batchUpdate(taslyItemLocations);
	}

	@Test
	public void batchUpdateItemInfos() throws Exception
	{
		final List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
		final ItemInfo itemInfo1 = new ItemInfo();
		itemInfo1.setSku("654321");
		itemInfo1.setQuantity(400);

		final ItemInfo itemInfo2 = new ItemInfo();
		itemInfo2.setSku("123456");
		itemInfo2.setQuantity(400);

		itemInfos.add(itemInfo1);
		itemInfos.add(itemInfo2);
		itemInfoFacade.batchUpdate(itemInfos);
	}
}
