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
package tasly.greathealth.thirdparty.inventory.beans;

import java.util.List;

import com.taobao.api.domain.Sku;


/**
 *
 */
public class StoreSku
{
	/**
	 * @return the productid
	 */
	public Long getProductid()
	{
		return productid;
	}

	/**
	 * @param productid the productid to set
	 */
	public void setProductid(final Long productid)
	{
		this.productid = productid;
	}

	/**
	 * @return the outerid
	 */
	public String getOuterid()
	{
		return outerid;
	}

	/**
	 * @param outerid the outerid to set
	 */
	public void setOuterid(final String outerid)
	{
		this.outerid = outerid;
	}


	/**
	 * @return the freeQuntity
	 */
	public Long getFreeQuntity()
	{
		return freeQuntity;
	}

	/**
	 * @param freeQuntity the freeQuntity to set
	 */
	public void setFreeQuntity(final Long freeQuntity)
	{
		this.freeQuntity = freeQuntity;
	}

	/**
	 * @return the orderQuntity
	 */
	public Long getOrderQuntity()
	{
		return orderQuntity;
	}

	/**
	 * @param orderQuntity the orderQuntity to set
	 */
	public void setOrderQuntity(final Long orderQuntity)
	{
		this.orderQuntity = orderQuntity;
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
	 * @return the skuid
	 */
	public String getSkuid()
	{
		return skuid;
	}

	/**
	 * @param skuid the skuid to set
	 */
	public void setSkuid(final String skuid)
	{
		this.skuid = skuid;
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
	public void setCombo(final boolean isCombo)
	{
		this.isCombo = isCombo;
	}

	/**
	 * @return the stockQuntity
	 */
	public Long getStockQuntity()
	{
		return stockQuntity;
	}

	/**
	 * @param stockQuntity the stockQuntity to set
	 */
	public void setStockQuntity(final Long stockQuntity)
	{
		this.stockQuntity = stockQuntity;
	}


	private Long productid;
	private String outerid;
	private Long freeQuntity;
	private Long orderQuntity;
	private List<Sku> skus;
	private String skuid;
	private String shopName;
	boolean isCombo;
	private Long stockQuntity;



}
