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


/**
 *
 */
@XmlRootElement(name = "skus")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkuList implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 2956360712814695360L;
	@XmlElement(name = "sku")
	private List<String> skus;

	public SkuList()
	{
		this.skus = new ArrayList<String>();
	}

	public void addSku(final String sku)
	{
		if (this.skus == null)
		{
			this.skus = new ArrayList<String>();
		}
		this.skus.add(sku);
	}

	public int getNumberOfSkus()
	{
		return this.skus.size();
	}

	public List<String> getSkus()
	{
		return Collections.unmodifiableList(skus);
	}

	public void initializeSkus(final List<String> newSkus)
	{
		assert (this.skus.isEmpty());
		for (final String sku : newSkus)
		{
			addSku(sku);
		}
	}

	public void removeSku(final String sku)
	{
		this.skus.remove(sku);
	}
}
