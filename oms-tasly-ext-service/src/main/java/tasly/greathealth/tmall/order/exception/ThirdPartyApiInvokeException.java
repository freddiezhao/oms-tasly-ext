/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.tmall.order.exception;

/**
 * Exception thrown when the third party API failed
 */
public class ThirdPartyApiInvokeException extends Exception
{

	private static final long serialVersionUID = 8575836999704464952L;

	public ThirdPartyApiInvokeException()
	{
		super();
	}

	public ThirdPartyApiInvokeException(final String message)
	{
		super(message);
	}

	public ThirdPartyApiInvokeException(final String message, final Throwable cause)
	{
		super(message, cause);
	}


}
