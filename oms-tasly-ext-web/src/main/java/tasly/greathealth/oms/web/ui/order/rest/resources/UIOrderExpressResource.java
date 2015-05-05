package tasly.greathealth.oms.web.ui.order.rest.resources;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.ui.api.order.UiExpressFacade;


@Component
@Consumes({"application/xml", "application/json"})
@Produces({"application/xml", "application/json"})
@Path("/uiexpress")
public class UIOrderExpressResource
{
	private UiExpressFacade uiExpressFacade;

	@GET
	public Collection<Express> getUIAllExpress()
	{
		return this.uiExpressFacade.getUIAllExpress();
	}

	/**
	 * @return the uiExpressFacade
	 */
	public UiExpressFacade getUiExpressFacade()
	{
		return uiExpressFacade;
	}

	/**
	 * @param uiExpressFacade the uiExpressFacade to set
	 */
	public void setUiExpressFacade(UiExpressFacade uiExpressFacade)
	{
		this.uiExpressFacade = uiExpressFacade;
	}
}
