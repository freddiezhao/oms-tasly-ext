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
package tasly.greathealth.oms.api.financial;

import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.exception.EntityValidationException;

import tasly.greathealth.oms.api.financial.dto.TaslyFinancialReport;



/**
 *
 */
public interface TaslyFinancialReportFacade
{
	PagedResults<TaslyFinancialReport> findFinancialReportByQuery(TaslyFinancialReportQueryObject queryObject)
			throws EntityValidationException;
}
