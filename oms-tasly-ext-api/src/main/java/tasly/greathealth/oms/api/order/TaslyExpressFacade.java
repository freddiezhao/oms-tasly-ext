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
package tasly.greathealth.oms.api.order;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;

import java.util.Collection;

import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.api.order.dto.ExpressItem;
import tasly.greathealth.oms.api.order.dto.Expresslocation;
import tasly.greathealth.oms.api.order.dto.PendingOrder;


/**
 *
 */
public interface TaslyExpressFacade
{
	/**
	 *
	 * @param expressItem
	 */
	ExpressItem createOrUpdataExpressItem(ExpressItem expressItem);

	/**
	 *
	 * @param expresslocation
	 * @return
	 */
	Expresslocation createOrUpdataExpresslocation(Expresslocation expresslocation);

	/**
	 *
	 * @param express
	 */
	Express createOrUpdataExpress(Express express);

	/**
	 * Search pending orders regarding how the query (a PendingOrderQueryObject) is populated. See
	 * PendingOrderQueryObject to find out available features.
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
	 * @return A pageable results of {@link PendingOrder}.
	 * @throws EntityValidationException if preconditions are not met.
	 */
	Pageable<Express> findExpressByQuery(ExpressQueryObject queryObject) throws EntityValidationException;

	/**
	 * @param code
	 */
	void deleteExpress(String code);

	/**
	 * @param province
	 */
	void deleteExpressLocation(String province, String channelSource, String innerSource);

	/**
	 * @param queryObject
	 * @return
	 * @throws EntityValidationException
	 */
	Pageable<Expresslocation> findExpressLocationByQuery(ExpressLocationQueryObject queryObject) throws EntityValidationException;

	/**
	 *
	 * @param queryObject
	 * @return
	 */
	Pageable<ExpressItem> findExpressItemByQuery(ExpressItemQueryObject queryObject) throws EntityValidationException;

	/**
	 *
	 * @param province
	 * @param innerSource
	 * @param channelSource
	 */
	void deleteExpressItem(String skuid, String innerSource, String channelSource);

	/**
	 * Get UI all express info.
	 *
	 * @return
	 * @throws EntityNotFoundException
	 */
	public abstract Collection<Express> getUIAllExpress() throws EntityNotFoundException;

}
