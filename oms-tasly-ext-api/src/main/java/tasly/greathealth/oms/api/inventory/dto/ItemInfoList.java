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
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "itemInfos")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemInfoList implements Serializable
{
	private static final long serialVersionUID = 647083152399188137L;

	@XmlElement(name = "itemInfo")
	private List<ItemInfo> itemInfos;

	public ItemInfoList()
	{
		this.itemInfos = new ArrayList<ItemInfo>();
	}

	public void addItemInfo(final ItemInfo itemInfo)
	{
		if (this.itemInfos == null)
		{
			this.itemInfos = new ArrayList<ItemInfo>();
		}
		this.itemInfos.add(itemInfo);
	}

	public int getNumberOfItemInfos()
	{
		return this.itemInfos.size();
	}

	public List<ItemInfo> getItemInfos()
	{
		return Collections.unmodifiableList(itemInfos);
	}

	public void initializeItemInfos(final List<ItemInfo> newItemInfos)
	{
		assert (this.itemInfos.isEmpty());
		for (final ItemInfo itemInfo : newItemInfos)
		{
			addItemInfo(itemInfo);
		}
	}

	public void removeItemInfo(final ItemInfo itemInfo)
	{
		this.itemInfos.remove(itemInfo);
	}
}
