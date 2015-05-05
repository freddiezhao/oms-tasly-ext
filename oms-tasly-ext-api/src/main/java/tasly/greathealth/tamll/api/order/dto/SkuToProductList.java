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

import tasly.greathealth.oms.api.order.dto.SkuToProduct;


@XmlRootElement(name = "skuToProducts")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkuToProductList implements Serializable
{

	private static final long serialVersionUID = 5022779292414835462L;

	@XmlElement(name = "skuToProduct")
	private List<SkuToProduct> skuToProducts;

	public SkuToProductList()
	{
		this.skuToProducts = new ArrayList<SkuToProduct>();
	}

	public void addSkuToProduct(final SkuToProduct skuToProduct)
	{
		if (this.skuToProducts == null)
		{
			this.skuToProducts = new ArrayList<SkuToProduct>();
		}
		this.skuToProducts.add(skuToProduct);
	}

	public int getNumberOfSkuToProducts()
	{
		return this.skuToProducts.size();
	}

	public List<SkuToProduct> getSkuToProducts()
	{
		return Collections.unmodifiableList(skuToProducts);
	}

	public void initializeSkuToProducts(final List<SkuToProduct> newSkuToProducts)
	{
		assert (this.skuToProducts.isEmpty());
		for (final SkuToProduct skuToProduct : newSkuToProducts)
		{
			this.addSkuToProduct(skuToProduct);
		}
	}

	public void removeSkuToProduct(final SkuToProduct skuToProduct)
	{
		this.skuToProducts.remove(skuToProduct);
	}
}
