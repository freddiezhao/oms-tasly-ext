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
package tasly.greathealth.oms.ui.api.order;

import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.Collection;

import tasly.greathealth.oms.api.order.dto.Express;


/**
 * UI express facade.
 */
public abstract interface UiExpressFacade
{

	/**
	 * Get UI all express info.
	 *
	 * @return
	 * @throws EntityNotFoundException
	 */
	public abstract Collection<Express> getUIAllExpress() throws EntityNotFoundException;
}
