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
 *
 */
public class ProductNotFoundException extends Exception
{

	private static final long serialVersionUID = 7876391139719884406L;

	public ProductNotFoundException()
	{
		super();
	}

	public ProductNotFoundException(final String message)
	{
		super(message);
	}

	public ProductNotFoundException(final String message, final Throwable cause)
	{
		super(message, cause);
	}


}
