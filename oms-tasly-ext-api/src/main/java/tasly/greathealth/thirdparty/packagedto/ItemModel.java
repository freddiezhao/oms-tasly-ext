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
package tasly.greathealth.thirdparty.packagedto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 */
@XmlRootElement(name = "itemModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemModel
{
	private String innerSource;
	private String channle;
	private Long quantity;
	private String outerId;

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

	/**
	 * @return the channle
	 */
	public String getChannle()
	{
		return channle;
	}

	/**
	 * @param channle the channle to set
	 */
	public void setChannle(final String channle)
	{
		this.channle = channle;
	}

	/**
	 * @return the quantity
	 */
	public Long getQuantity()
	{
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
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



}
