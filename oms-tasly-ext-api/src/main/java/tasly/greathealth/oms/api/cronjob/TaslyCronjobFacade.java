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
package tasly.greathealth.oms.api.cronjob;

import java.util.Collection;

import tasly.greathealth.oms.api.job.dto.TaslyCronJobConfig;


/**
 * @author Bao Gang
 */
public interface TaslyCronjobFacade
{
	public TaslyCronJobConfig getCronjobConfig(String jobName);

	public TaslyCronJobConfig createOrUpdateJobConfig(final TaslyCronJobConfig config);

	public Collection<TaslyCronJobConfig> getAllJobConfig();
}
