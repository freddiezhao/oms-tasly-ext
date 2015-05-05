package tasly.greathealth.oms.web.inventory.soap.resources;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import tasly.greathealth.erp.api.inventory.dto.Baseinfo;
import tasly.greathealth.erp.api.inventory.dto.Message;



/**
 * Update Inventory Service(TS-193)
 * libin 2014-12-30
 *
 */

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface UpdateInventoryService
{
	/**
	 * Update Inventory Interface(TS-193)
	 *
	 * Return nothing
	 *
	 * @param baseinfo
	 * @param message
	 *           libin539 2015-01-06
	 *
	 */
	@WebMethod
	@Oneway
	public void updateInventory(@WebParam(name = "baseinfo") Baseinfo baseinfo, @WebParam(name = "message") Message message);
}
