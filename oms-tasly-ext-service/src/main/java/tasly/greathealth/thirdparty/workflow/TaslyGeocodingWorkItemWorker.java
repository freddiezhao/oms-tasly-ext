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
package tasly.greathealth.thirdparty.workflow;

import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;


/**
 *
 */
public class TaslyGeocodingWorkItemWorker extends AbstractWorkItemWorker
{


	@Override
	protected void executeInternal(final Map<String, Object> paramMap)
	{
		paramMap.put(WorkflowConstants.KEY_OUTCOME_NAME, true);
	}

}
