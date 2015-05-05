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
package tasly.greathealth.oms.api.inventory.dto;

import com.hybris.oms.domain.inventory.ItemLocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "itemLocations")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaslyItemLocationList implements Serializable
{
	private static final long serialVersionUID = 5446980805661537057L;

	@XmlElement(name = "itemLocation")
	private List<TaslyItemLocation> itemLocations;

	public TaslyItemLocationList()
	{
		this.itemLocations = new ArrayList<TaslyItemLocation>();
	}

	public void addItemLocation(final TaslyItemLocation itemLocation)
	{
		if (this.itemLocations == null)
		{
			this.itemLocations = new ArrayList<TaslyItemLocation>();
		}
		this.itemLocations.add(itemLocation);
	}

	public int getNumberOfItemLocations()
	{
		return this.itemLocations.size();
	}

	public List<TaslyItemLocation> getItemLocations()
	{
		final List<TaslyItemLocation> list = new ArrayList<TaslyItemLocation>();
		for (final ItemLocation itemLocation : itemLocations)
		{
			if (itemLocation instanceof TaslyItemLocation)
			{
				list.add((TaslyItemLocation) itemLocation);
			}
		}
		return Collections.unmodifiableList(list);
	}

	public void initializeItemLocations(final List<TaslyItemLocation> newItemLocations)
	{
		assert(this.itemLocations.isEmpty());
		for (final TaslyItemLocation itemLocation : newItemLocations)
		{
			addItemLocation(itemLocation);
		}
	}

	public void removeItemLocation(final TaslyItemLocation itemLocation)
	{
		this.itemLocations.remove(itemLocation);
	}
}
