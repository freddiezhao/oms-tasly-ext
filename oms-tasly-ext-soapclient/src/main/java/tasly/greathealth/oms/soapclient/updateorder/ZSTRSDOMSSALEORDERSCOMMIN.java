package tasly.greathealth.oms.soapclient.updateorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for ZSTR_SD_OMS_SALE_ORDERS_COMMIN complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_SALE_ORDERS_COMMIN">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ORDERS" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SALEORDER_COMM_TAB" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSTR_SD_OMS_SALE_ORDERS_COMMIN", propOrder = {"orders"})
public class ZSTRSDOMSSALEORDERSCOMMIN
{

	@XmlElement(name = "ORDERS")
	protected ZSTRSDOMSSALEORDERCOMMTAB orders;

	/**
	 * Gets the value of the orders property.
	 * 
	 * @return
	 *         possible object is {@link ZSTRSDOMSSALEORDERCOMMTAB }
	 * 
	 */
	public ZSTRSDOMSSALEORDERCOMMTAB getORDERS()
	{
		return orders;
	}

	/**
	 * Sets the value of the orders property.
	 * 
	 * @param value
	 *           allowed object is {@link ZSTRSDOMSSALEORDERCOMMTAB }
	 * 
	 */
	public void setORDERS(final ZSTRSDOMSSALEORDERCOMMTAB value)
	{
		this.orders = value;
	}

}
