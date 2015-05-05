package tasly.greathealth.oms.web.jdlogistic.rest.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tasly.greathealth.thirdparty.logistic.service.BaseLogisticService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by houyi on 2015/4/17.
 */

@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/jd")
public class TaslyJdLogisticMockResource {
    private static final Logger LOG = LoggerFactory.getLogger(TaslyJdLogisticMockResource.class);
    private BaseLogisticService baseLogisticService;

    @PUT
    @Path("/logistic/upload/{orderId}")
    public Response mockProcess(@PathParam("orderId") final String orderId) {
        baseLogisticService.createLogistic();
        return Response.ok("OK").build();
    }

    public void setBaseLogisticService(BaseLogisticService baseLogisticService) {
        this.baseLogisticService = baseLogisticService;
    }
}
