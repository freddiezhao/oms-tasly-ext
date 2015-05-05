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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author Henter Liu
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemQuantityList implements Serializable
{
	private static final long serialVersionUID = 3823572988798096640L;

	@XmlElement(name = "itemQuantity")
	private List<ItemQuantityElement> itemQuantities;

	public ItemQuantityList()
	{
		this.itemQuantities = new ArrayList<ItemQuantityElement>();
	}

	public void addItemQuantity(final ItemQuantityElement itemQuantityElement)
	{
		if (this.itemQuantities == null)
		{
			this.itemQuantities = new ArrayList<ItemQuantityElement>();
		}
		this.itemQuantities.add(itemQuantityElement);
	}

	public int getNumberOfItemQuantities()
	{
		return this.itemQuantities.size();
	}

	public List<ItemQuantityElement> getItemQuantities()
	{
		return Collections.unmodifiableList(itemQuantities);
	}

	public void initializeItemQuantities(final List<ItemQuantityElement> newItemQuantities)
	{
		assert (this.itemQuantities.isEmpty());
		for (final ItemQuantityElement itemQuantity : newItemQuantities)
		{
			addItemQuantity(itemQuantity);
		}
	}

	public void removeItemQuantity(final ItemQuantityElement itemQuantity)
	{
		this.itemQuantities.remove(itemQuantity);
	}
}
