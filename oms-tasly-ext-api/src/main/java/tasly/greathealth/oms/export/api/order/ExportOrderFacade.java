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
package tasly.greathealth.oms.export.api.order;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.exception.EntityValidationException;

import tasly.greathealth.oms.api.order.TaslyOrderQueryObject;
import tasly.greathealth.oms.export.api.order.dto.ExportOrder;


/**
 * @author Henter Liu
 */
public interface ExportOrderFacade
{
	/**
	 * Search orders regarding how the query (a TaslyOrderQueryObject) is populated. See TaslyOrderQueryObject to find
	 * out available features.
	 *
	 * @category OMS-Export
	 *
	 * @param queryObject The {@link com.hybris.oms.domain.QueryObject} that determines how
	 *           the search should be performed.
	 *           <p/>
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           queryObject.pageNumber must not be null.
	 *           <dd>
	 *           queryObject.pageNumber must not be less than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be null.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than max allowed page size.
	 * @return A pageable results of {@link ExportOrder}.
	 * @throws EntityValidationException if preconditions are not met.
	 */
	Pageable<ExportOrder> findOrdersByQuery(TaslyOrderQueryObject queryObject) throws EntityValidationException;
}
