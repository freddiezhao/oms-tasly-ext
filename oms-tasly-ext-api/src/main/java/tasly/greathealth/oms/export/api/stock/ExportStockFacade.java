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
package tasly.greathealth.oms.export.api.stock;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.exception.EntityValidationException;

import java.util.List;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationQueryObject;
import tasly.greathealth.oms.export.api.stock.dto.ExportStock;


/**
 *
 */
public interface ExportStockFacade
{
	Pageable<ExportStock> findPagedExportStocksByQuery(TaslyItemLocationQueryObject queryObject) throws EntityValidationException;

	List<ExportStock> findListExportStocksByQuery(final TaslyItemLocationQueryObject queryObject);
}
