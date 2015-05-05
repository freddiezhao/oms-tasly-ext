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
package tasly.greathealth.oms.export.order.facades.impl;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.Page;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.order.TaslyOrderQueryObject;
import tasly.greathealth.oms.export.api.order.ExportOrderFacade;
import tasly.greathealth.oms.export.api.order.dto.ExportOrder;


/**
 * @author Henter Liu
 */
@Transactional
public class DefaultExportOrderFacade implements ExportOrderFacade
{
	private OrderService orderService;
	private Converter<OrderData, ExportOrder> exportOrderConverter;
	private Converters converters;
	private Validator<QueryObject<?>> queryObjectValidator;
	private FailureHandler entityValidationHandler;

	protected OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	@Required
	public void setQueryObjectValidator(final Validator<QueryObject<?>> queryObjectValidator)
	{
		this.queryObjectValidator = queryObjectValidator;
	}

	@Required
	public void setExportOrderConverter(final Converter<OrderData, ExportOrder> exportOrderConverter)
	{
		this.exportOrderConverter = exportOrderConverter;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

	@Override
	public Pageable<ExportOrder> findOrdersByQuery(final TaslyOrderQueryObject queryObject) throws EntityValidationException
	{
		queryObjectValidator.validate("TaslyOrderQueryObject", queryObject, entityValidationHandler);

		final Page<OrderData> pagedOrderDatas = orderService.findPagedOrdersByQuery(queryObject);
		final List<ExportOrder> orders = converters.convertAll(pagedOrderDatas.getContent(), exportOrderConverter);

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(Integer.valueOf(pagedOrderDatas.getTotalPages()));
		pageInfo.setTotalResults(Long.valueOf(pagedOrderDatas.getTotalElements()));
		pageInfo.setPageNumber(Integer.valueOf(pagedOrderDatas.getNumber()));

		return new PagedResults<ExportOrder>(orders, pageInfo);
	}
}
