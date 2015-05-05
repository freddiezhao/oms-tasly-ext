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
@XmlRootElement(name = "packageRelationModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class PackageRelationModel
{
	@XmlElement(name = "packageItemRelationModel")
	private PackageItemRelationModel packageItemRelationModel;

	private String channel;
	private String innersource;
	private boolean updateResult;

	/**
	 * @return the packageItemRelationModel
	 */
	public PackageItemRelationModel getPackageItemRelationModel()
	{
		return packageItemRelationModel;
	}

	/**
	 * @param packageItemRelationModel the packageItemRelationModel to set
	 */
	public void setPackageItemRelationModel(final PackageItemRelationModel packageItemRelationModel)
	{
		this.packageItemRelationModel = packageItemRelationModel;
	}

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
	 * @return the innersource
	 */
	public String getInnersource()
	{
		return innersource;
	}

	/**
	 * @param innersource the innersource to set
	 */
	public void setInnersource(final String innersource)
	{
		this.innersource = innersource;
	}

	/**
	 * @return the updateResult
	 */
	public boolean isUpdateResult()
	{
		return updateResult;
	}

	/**
	 * @param updateResult the updateResult to set
	 */
	public void setUpdateResult(final boolean updateResult)
	{
		this.updateResult = updateResult;
	}

}
