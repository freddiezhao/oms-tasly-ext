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
package tasly.greathealth.oms.web.order.rest.resources;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import tasly.greathealth.oms.api.financial.TaslyFinancialReportFacade;
import tasly.greathealth.oms.api.financial.TaslyFinancialReportQueryObject;
import tasly.greathealth.oms.api.financial.dto.TaslyFinancialReport;


/**
 * @author Henter Liu
 */
@Component
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
@Path("/taslyfinancialreport")
public class TaslyFinancialReportResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TaslyFinancialReportResource.class);

	@Autowired
	private TaslyFinancialReportFacade taslyFinancialReportFacade;

	/**
	 * @return the taslyFinancialReportFacade
	 */
	public TaslyFinancialReportFacade getTaslyFinancialReportFacade()
	{
		return taslyFinancialReportFacade;
	}

	/**
	 * @param taslyFinancialReportFacade the taslyFinancialReportFacade to set
	 */
	@Required
	public void setTaslyFinancialReportFacade(final TaslyFinancialReportFacade taslyFinancialReportFacade)
	{
		this.taslyFinancialReportFacade = taslyFinancialReportFacade;
	}

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	@GET
	@TypeHint(TaslyFinancialReport.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response findFinancialReportByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findOrdersByQuery");

		final TaslyFinancialReportQueryObject queryObject = new TaslyFinancialReportQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<TaslyFinancialReport> pagedReports = this.taslyFinancialReportFacade.findFinancialReportByQuery(queryObject);
		final GenericEntity<List<TaslyFinancialReport>> entity = new GenericEntity<List<TaslyFinancialReport>>(
				pagedReports.getResults())
		{
			// DONOTHING
		};
		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedReports.getNextPage().intValue(),
				pagedReports.getPreviousPage().intValue(), pagedReports.getTotalPages().intValue(), pagedReports.getTotalRecords()
						.longValue());

		return responseBuilder.entity(entity).build();
	}
}
