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
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result
{
	boolean result;

	public Result()
	{

	}

	public Result(final boolean result)
	{
		this.result = result;
	}

	/**
	 * @return the result
	 */
	public boolean isResult()
	{
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(final boolean result)
	{
		this.result = result;
	}

}
