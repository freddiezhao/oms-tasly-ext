package tasly.greathealth.oms.web.inventory.soap.resources;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import tasly.greathealth.erp.api.order.updateorder.dto.Message;




/**
 * Oms Status Update Service
 * libin539 2015-01-06
 *
 */

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface TaslySoapTestService
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
	// @WebMethod
	// @Oneway
	// public void updateInventory(@WebParam(name = "baseinfo") Baseinfo baseinfo, @WebParam(name = "message") Message
	// message);

	/**
	 * Update Inventory Interface(TS-193)
	 *
	 * Return nothing
	 *
	 * @param orderId
	 * @param ordersMessage
	 *           libin539 2015-01-06
	 *
	 */
	@WebMethod
	public boolean testUpdateInventory(@WebParam(name = "orderId") String orderId,
			@WebParam(name = "ordersMessage") Message ordersMessage);

	@WebMethod
	public boolean invoking();
}
