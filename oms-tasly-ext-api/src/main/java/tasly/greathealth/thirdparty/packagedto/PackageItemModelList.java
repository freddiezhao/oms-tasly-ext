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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 */
@XmlRootElement(name = "packageItemModelList")
@XmlAccessorType(XmlAccessType.FIELD)
public class PackageItemModelList
{
	@XmlElement(name = "packageModelList")
	private PackageModelList packageModelList;

	String itemId;


	/**
	 * @return the packageModelList
	 */
	public PackageModelList getPackageModelList()
	{
		return packageModelList;
	}

	/**
	 * @param packageModelList the packageModelList to set
	 */
	public void setPackageModelList(final PackageModelList packageModelList)
	{
		this.packageModelList = packageModelList;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId()
	{
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(final String itemId)
	{
		this.itemId = itemId;
	}

}
