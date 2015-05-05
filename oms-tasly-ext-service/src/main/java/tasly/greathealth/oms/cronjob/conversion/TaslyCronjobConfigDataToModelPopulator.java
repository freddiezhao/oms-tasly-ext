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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasly.greathealth.oms.api.job.dto.TaslyCronJobConfig;


/**
 *
 */
public class TaslyCronjobConfigDataToModelPopulator extends AbstractPopulator<TaslyJobConfigData, TaslyCronJobConfig>
{
	private static final Logger LOG = LoggerFactory.getLogger(TaslyCronjobConfigDataToModelPopulator.class);

	@Override
	public void populate(final TaslyJobConfigData source, final TaslyCronJobConfig target) throws ConversionException,
			IllegalArgumentException
	{
		LOG.info("TaslyCronjobConfigDataToModelPopulator::::");
		target.setJobName(source.getJobName());
		target.setCronExpression(source.getCronExpression());
		target.setDescription(source.getDescription());
		target.setEnable(source.isEnable());
		target.setJobParams(source.getJobParams());
		target.setTenantId(source.getTenantId());
		target.setSuccess(source.isSuccess());
	}
}
