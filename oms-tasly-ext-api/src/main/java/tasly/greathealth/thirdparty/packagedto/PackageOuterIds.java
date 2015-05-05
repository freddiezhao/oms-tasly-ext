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
@XmlRootElement(name = "packageOuterIds")
@XmlAccessorType(XmlAccessType.FIELD)
public class PackageOuterIds
{
	private String channel;
	private String innerSource;
	@XmlElement(name = "packageOuterIdList")
	private PackageOuterIdList packageOuterIdList;

	/**
	 * @return the channel
	 */
	public String getChannel()
	{
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(final String channel)
	{
		this.channel = channel;
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

	/**
	 * @return the packageOuterIdList
	 */
	public PackageOuterIdList getPackageOuterIdList()
	{
		return packageOuterIdList;
	}

	/**
	 * @param packageOuterIdList the packageOuterIdList to set
	 */
	public void setPackageOuterIdList(final PackageOuterIdList packageOuterIdList)
	{
		this.packageOuterIdList = packageOuterIdList;
	}


}
