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

/**
 *
 */
public class ProductOms
{
	private String skuId;
	private Long num;
	private String innerSource;

	/**
	 * @return the skuId
	 */
	public String getSkuId()
	{
		return skuId;
	}

	/**
	 * @param skuId the skuId to set
	 */
	public void setSkuId(final String skuId)
	{
		this.skuId = skuId;
	}

	/**
	 * @return the num
	 */
	public Long getNum()
	{
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(final Long num)
	{
		this.num = num;
	}

	/**
	 * @return the innerSource
	 */
	public String getInnerSource()
	{
		return innerSource;
	}

	/**
	 * @param innerSource the innerSource to set
	 */
	public void setInnerSource(final String innerSource)
	{
		this.innerSource = innerSource;
	}

}
