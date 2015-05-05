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
package tasly.greathealth.oms.web.inventory.rest.resources;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationQueryObject;
import tasly.greathealth.oms.export.api.stock.ExportStockFacade;
import tasly.greathealth.oms.export.api.stock.dto.ExportStock;


/**
 *
 */
@Component
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
@Path("/exportstocks")
public class ExportStockResource
{
	@Autowired
	private ExportStockFacade exportStockFacade;

	/**
	 * @param exportStockFacade the exportStockFacade to set
	 */
	@Required
	public void setExportStockFacade(final ExportStockFacade exportStockFacade)
	{
		this.exportStockFacade = exportStockFacade;
	}

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	@GET
	@TypeHint(ExportStock.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator"})
	public Response findPagedExportStocksByQuery(@Context final UriInfo uriInfo)
	{
		final TaslyItemLocationQueryObject queryObject = new TaslyItemLocationQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<ExportStock> pagedExportStocks = this.exportStockFacade.findPagedExportStocksByQuery(queryObject);
		final GenericEntity<List<ExportStock>> entity = new GenericEntity<List<ExportStock>>(pagedExportStocks.getResults())
		{
			//
		};
		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedExportStocks.getNextPage()
				.intValue(), pagedExportStocks.getPreviousPage().intValue(), pagedExportStocks.getTotalPages().intValue(),
				pagedExportStocks.getTotalRecords().longValue());
		return responseBuilder.entity(entity).build();

	}


	@GET
	@Path("/all")
	@TypeHint(ExportStock.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator"})
	public List<ExportStock> findListExportStocksByQuery(@Context final UriInfo uriInfo)
	{
		final TaslyItemLocationQueryObject queryObject = new TaslyItemLocationQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		return this.exportStockFacade.findListExportStocksByQuery(queryObject);

	}
}
