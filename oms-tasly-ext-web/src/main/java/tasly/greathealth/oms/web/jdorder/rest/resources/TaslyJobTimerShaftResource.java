/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.web.jdorder.rest.resources;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import tasly.greathealth.jd.api.order.TaslyJobTimerShaftFacade;
import tasly.greathealth.oms.api.job.dto.JobTimerShaft;


/**
 *
 * @author libin
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/jobtimershaft")
public class TaslyJobTimerShaftResource
{
	private static final Logger LOG = LoggerFactory.getLogger(TaslyJobTimerShaftResource.class);

	private TaslyJobTimerShaftFacade taslyJobTimerShaftFacade;


	/**
	 * @param taslyJobTimerShaftFacade the taslyJobTimerShaftFacade to set
	 */
	public void setTaslyJobTimerShaftFacade(final TaslyJobTimerShaftFacade taslyJobTimerShaftFacade)
	{
		this.taslyJobTimerShaftFacade = taslyJobTimerShaftFacade;
	}


	@POST
	public Response create(@RequestBody final JobTimerShaft jobTimerShaft)
	{
		taslyJobTimerShaftFacade.createJobTimerShaft(jobTimerShaft);
		return Response.status(Response.Status.CREATED).build();
	}

	@GET
	public Collection<JobTimerShaft> getAll()
	{
		return taslyJobTimerShaftFacade.getAll();
	}

	@GET
	@Path("/getJobTimerShaft/{channelSource}/{innerSource}/{event}")
	public JobTimerShaft getJobTimerShaft(@PathParam("channelSource") final String channelSource,
			@PathParam("innerSource") final String innerSource, @PathParam("event") final String event)
	{
		return taslyJobTimerShaftFacade.getJobTimerShaftConfig(channelSource, innerSource, event);
	}
}
