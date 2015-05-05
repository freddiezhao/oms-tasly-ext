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
package tasly.greathealth.oms.cronjob.conversion;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.job.TaslyJobConfigData;

import tasly.greathealth.oms.api.job.dto.TaslyCronJobConfig;


/**
 *
 */
public class TaslyCronjobConfigModelToDataPopulator extends AbstractPopulator<TaslyCronJobConfig, TaslyJobConfigData>
{
	@Override
	public void populate(final TaslyCronJobConfig source, final TaslyJobConfigData target) throws ConversionException,
	IllegalArgumentException
	{
		target.setJobName(source.getJobName());
		target.setCronExpression(source.getCronExpression());
		target.setDescription(source.getDescription());
		target.setEnable(source.getEnable());
		target.setJobParams(source.getJobParams());
		target.setTenantId(source.getTenantId());
	}
}
