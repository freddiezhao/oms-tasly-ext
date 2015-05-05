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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "packageModel")
public class PackageModel
{
	String outerId;
	Long quantity;

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


}
