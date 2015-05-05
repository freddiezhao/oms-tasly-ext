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
package tasly.greathealth.oms.principal.conversion;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.oms.api.principal.dto.DataPrincipal;
import tasly.greathealth.oms.domain.principal.DataPrincipalData;


/**
 * @author Henter Liu
 */
public class DataPrincipalDataToModelPopulator extends AbstractPopulator<DataPrincipalData, DataPrincipal>
{
	@Override
	public void populate(final DataPrincipalData source, final DataPrincipal target) throws ConversionException
	{
		target.setUniqueid(source.getUniqueid());
		target.setIsGroup(source.isIsGroup());
		target.setGroup(source.getGroup());
		target.setPrincipal(source.getPrincipal());
		target.setActive(source.isActive());
	}
}
