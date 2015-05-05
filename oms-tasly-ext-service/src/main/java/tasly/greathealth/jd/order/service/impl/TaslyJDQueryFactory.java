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
package tasly.greathealth.jd.order.service.impl;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.domain.job.JobTimerShaftData;
import com.hybris.oms.service.order.impl.OrderQueryFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * @author libin
 */
public class TaslyJDQueryFactory extends OrderQueryFactory
{
	@SuppressWarnings({"unchecked", "rawtypes"})
	public CriteriaQuery<JobTimerShaftData> findJobTimerShaftByQuery(final String channelSource, final String innerSource,
			final String event)
			{
		final CriteriaQuery criteriaQuery = query(JobTimerShaftData.class);
		final List restrictions = new ArrayList();
		restrictions.add(Restrictions.eq(JobTimerShaftData.CHANNEL_SOURCE, channelSource));
		restrictions.add(Restrictions.eq(JobTimerShaftData.INNER_SOURCE, innerSource));
		restrictions.add(Restrictions.eq(JobTimerShaftData.EVENT, event));

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		return criteriaQuery;
			}

	protected CriteriaQuery<JobTimerShaftData> applyingCollectedRestrictionsToCriteriaQuery1(
			final CriteriaQuery<JobTimerShaftData> origQuery, final List<Restriction> restrictions)
	{
		CriteriaQuery<JobTimerShaftData> criteriaQuery = origQuery;
		if (!(restrictions.isEmpty()))
		{
			criteriaQuery = criteriaQuery.where(restrictions.remove(0));

			for (final Restriction restriction : restrictions)
			{
				criteriaQuery = criteriaQuery.and(restriction);
			}
		}

		return criteriaQuery;
	}
}
