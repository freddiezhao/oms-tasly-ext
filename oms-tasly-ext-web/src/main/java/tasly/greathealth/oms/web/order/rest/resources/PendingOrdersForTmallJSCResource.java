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

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;


/**
 * @author Gaoxin
 */
@Component
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
@Path("/pendingorders/tmall/jsc")
public class PendingOrdersForTmallJSCResource extends PendingOrdersResource
{

}
