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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 */
@XmlRootElement(name = "packageOuterIdList")
@XmlAccessorType(XmlAccessType.FIELD)
public class PackageOuterIdList
{
	@XmlElement(name = "packageOuterId")
	List<PackageOuterId> outerIds = new ArrayList<PackageOuterId>();

	/**
	 * @return the outerIds
	 */
	public List<PackageOuterId> getOuterIds()
	{
		return outerIds;
	}

	/**
	 * @param outerIds the outerIds to set
	 */
	public void setOuterIds(final List<PackageOuterId> outerIds)
	{
		this.outerIds = outerIds;
	}

}
