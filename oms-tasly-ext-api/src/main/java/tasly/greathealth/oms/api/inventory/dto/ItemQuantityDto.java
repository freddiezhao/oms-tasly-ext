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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "itemQuantities")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemQuantityDto implements Serializable
{
	private static final long serialVersionUID = 416915313989714420L;

	@XmlElement
	private String sku;

	@XmlElement(name = "itemQuantityList")
	private ItemQuantityList list;

	public ItemQuantityDto()
	{
		this.list = new ItemQuantityList();
	}

	/**
	 * @return the sku
	 */
	public String getSku()
	{
		return sku;
	}

	/**
	 * @param sku the sku to set
	 */
	public void setSku(final String sku)
	{
		this.sku = sku;
	}

	/**
	 * @return the list
	 */
	public ItemQuantityList getList()
	{
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(final ItemQuantityList list)
	{
		if (list == null)
		{
			this.list = new ItemQuantityList();
		}
		this.list = list;
	}
}
