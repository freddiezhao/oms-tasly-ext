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
package tasly.greathealth.tamll.api.order.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "skuToProductsList")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkuToProductsList implements Serializable
{

	private static final long serialVersionUID = 617660343887905067L;
	@XmlElement(name = "skuToProducts")
	private List<SkuToProductList> skuToProductsList;

	public SkuToProductsList()
	{
		this.skuToProductsList = new ArrayList<SkuToProductList>();
	}

	public void addSkuToProducts(final SkuToProductList skuToProductList)
	{
		if (this.skuToProductsList == null)
		{
			this.skuToProductsList = new ArrayList<SkuToProductList>();
		}
		this.skuToProductsList.add(skuToProductList);
	}

	public int getNumberOfSkuToProductsList()
	{
		return this.skuToProductsList.size();
	}

	public List<SkuToProductList> getSkuToProductsList()
	{
		return Collections.unmodifiableList(skuToProductsList);
	}

	public void initializeSkuToProducts(final List<SkuToProductList> newSkuToProductsList)
	{
		assert (this.skuToProductsList.isEmpty());
		for (final SkuToProductList skuToProductList : newSkuToProductsList)
		{
			this.addSkuToProducts(skuToProductList);
		}
	}

	public void removeSkuToProducts(final SkuToProductList skuToProductList)
	{
		this.skuToProductsList.remove(skuToProductList);
	}
}
