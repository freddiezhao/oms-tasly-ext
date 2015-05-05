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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 */
@XmlRootElement(name = "packageModelList")
@XmlAccessorType(XmlAccessType.FIELD)
public class PackageModelList
{
	@XmlElement(name = "packageModel")
	private List<PackageModel> packageModel;

	/**
	 * @return the packageModel
	 */
	public List<PackageModel> getPackageModel()
	{
		return packageModel;
	}

	/**
	 * @param packageModel the packageModel to set
	 */
	public void setPackageModel(final List<PackageModel> packageModel)
	{
		this.packageModel = packageModel;
	}


}
