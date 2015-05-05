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
package tasly.greathealth.tmall.inventory.domain;

import java.util.List;

import com.taobao.api.domain.Sku;


/**
 *
 */
public class ProductTm
{
	private Long tmProductId;
	private String outerId;
	private Long tmFreeQuntity;
	private Long tmorderQuntity;
	private List<Sku> skus;
	private String shopName;
	boolean isCombo;

	/**
	 * @return the tmProductId
	 */
	public Long getTmProductId()
	{
		return tmProductId;
	}

	/**
	 * @param tmProductId the tmProductId to set
	 */
	public void setTmProductId(final Long tmProductId)
	{
		this.tmProductId = tmProductId;
	}

	/**
	 * @return the outerId
	 */
	public String getOuterId()
	{
		return outerId;
	}

	/**
	 * @param outerId the outerId to set
	 */
	public void setOuterId(final String outerId)
	{
		this.outerId = outerId;
	}



	/**
	 * @return the tmFreeQuntity
	 */
	public Long getTmFreeQuntity()
	{
		return tmFreeQuntity;
	}

	/**
	 * @param tmFreeQuntity the tmFreeQuntity to set
	 */
	public void setTmFreeQuntity(final Long tmFreeQuntity)
	{
		this.tmFreeQuntity = tmFreeQuntity;
	}

	/**
	 * @return the tmorderQuntity
	 */
	public Long getTmorderQuntity()
	{
		return tmorderQuntity;
	}

	/**
	 * @param tmorderQuntity the tmorderQuntity to set
	 */
	public void setTmorderQuntity(final Long tmorderQuntity)
	{
		this.tmorderQuntity = tmorderQuntity;
	}

	/**
	 * @return the skus
	 */
	public List<Sku> getSkus()
	{
		return skus;
	}

	/**
	 * @param skus the skus to set
	 */
	public void setSkus(final List<Sku> skus)
	{
		this.skus = skus;
	}

	/**
	 * @return the shopName
	 */
	public String getShopName()
	{
		return shopName;
	}

	/**
	 * @param shopName the shopName to set
	 */
	public void setShopName(final String shopName)
	{
		this.shopName = shopName;
	}

	/**
	 * @return the isCombo
	 */
	public boolean isCombo()
	{
		return isCombo;
	}

	/**
	 * @param isCombo the isCombo to set
	 */
	public void setCombo(boolean isCombo)
	{
		this.isCombo = isCombo;
	}
}
