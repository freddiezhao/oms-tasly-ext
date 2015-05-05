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
package tasly.greathealth.system.init;

import com.hybris.kernel.api.InitAware;

import javax.ws.rs.ext.RuntimeDelegate;


/**
 *
 */
public class TaslyInitializer implements InitAware
{

	public TaslyInitializer()
	{
		RuntimeDelegate.setInstance(new com.sun.ws.rs.ext.RuntimeDelegateImpl());
	}
}
