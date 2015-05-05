package tasly.greathealth.oms.soapclient.updateorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for ZSTR_SD_OMS_SALE_ORDER_COMMIN complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_SALE_ORDER_COMMIN">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OPERATION" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_ORDER_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OMS_ORDER_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="USER_NOTES" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CS_NOTES" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SHIPTO" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SHIP_TO" minOccurs="0"/>
 *         &lt;element name="ECC_BILL_TO" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_BILL_TO" minOccurs="0"/>
 *         &lt;element name="ECC_DELIVERY" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_DELIVERY" minOccurs="0"/>
 *         &lt;element name="REFUND" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SO_ITEM_REFUND" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSTR_SD_OMS_SALE_ORDER_COMMIN", propOrder = {"operation", "eccorderid", "omsorderid", "usernotes", "csnotes",
		"shipto", "eccbillto", "eccdelivery", "refund"})
public class ZSTRSDOMSSALEORDERCOMMIN
{

	@XmlElement(name = "OPERATION")
	protected String operation;
	@XmlElement(name = "ECC_ORDER_ID")
	protected String eccorderid;
	@XmlElement(name = "OMS_ORDER_ID")
	protected String omsorderid;
	@XmlElement(name = "USER_NOTES")
	protected String usernotes;
	@XmlElement(name = "CS_NOTES")
	protected String csnotes;
	@XmlElement(name = "SHIPTO")
	protected ZSTRSDOMSSHIPTO shipto;
	@XmlElement(name = "ECC_BILL_TO")
	protected ZSTRSDOMSBILLTO eccbillto;
	@XmlElement(name = "ECC_DELIVERY")
	protected ZSTRSDOMSDELIVERY eccdelivery;
	@XmlElement(name = "REFUND")
	protected ZSTRSDOMSSOITEMREFUND refund;

	/**
	 * Gets the value of the operation property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getOPERATION()
	{
		return operation;
	}

	/**
	 * Sets the value of the operation property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setOPERATION(final String value)
	{
		this.operation = value;
	}

	/**
	 * Gets the value of the eccorderid property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCORDERID()
	{
		return eccorderid;
	}

	/**
	 * Sets the value of the eccorderid property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCORDERID(final String value)
	{
		this.eccorderid = value;
	}

	/**
	 * Gets the value of the omsorderid property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getOMSORDERID()
	{
		return omsorderid;
	}

	/**
	 * Sets the value of the omsorderid property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setOMSORDERID(final String value)
	{
		this.omsorderid = value;
	}

	/**
	 * Gets the value of the usernotes property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getUSERNOTES()
	{
		return usernotes;
	}

	/**
	 * Sets the value of the usernotes property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setUSERNOTES(final String value)
	{
		this.usernotes = value;
	}

	/**
	 * Gets the value of the csnotes property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getCSNOTES()
	{
		return csnotes;
	}

	/**
	 * Sets the value of the csnotes property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setCSNOTES(final String value)
	{
		this.csnotes = value;
	}

	/**
	 * Gets the value of the shipto property.
	 * 
	 * @return
	 *         possible object is {@link ZSTRSDOMSSHIPTO }
	 * 
	 */
	public ZSTRSDOMSSHIPTO getSHIPTO()
	{
		return shipto;
	}

	/**
	 * Sets the value of the shipto property.
	 * 
	 * @param value
	 *           allowed object is {@link ZSTRSDOMSSHIPTO }
	 * 
	 */
	public void setSHIPTO(final ZSTRSDOMSSHIPTO value)
	{
		this.shipto = value;
	}

	/**
	 * Gets the value of the eccbillto property.
	 * 
	 * @return
	 *         possible object is {@link ZSTRSDOMSBILLTO }
	 * 
	 */
	public ZSTRSDOMSBILLTO getECCBILLTO()
	{
		return eccbillto;
	}

	/**
	 * Sets the value of the eccbillto property.
	 * 
	 * @param value
	 *           allowed object is {@link ZSTRSDOMSBILLTO }
	 * 
	 */
	public void setECCBILLTO(final ZSTRSDOMSBILLTO value)
	{
		this.eccbillto = value;
	}

	/**
	 * Gets the value of the eccdelivery property.
	 * 
	 * @return
	 *         possible object is {@link ZSTRSDOMSDELIVERY }
	 * 
	 */
	public ZSTRSDOMSDELIVERY getECCDELIVERY()
	{
		return eccdelivery;
	}

	/**
	 * Sets the value of the eccdelivery property.
	 * 
	 * @param value
	 *           allowed object is {@link ZSTRSDOMSDELIVERY }
	 * 
	 */
	public void setECCDELIVERY(final ZSTRSDOMSDELIVERY value)
	{
		this.eccdelivery = value;
	}

	/**
	 * Gets the value of the refund property.
	 * 
	 * @return
	 *         possible object is {@link ZSTRSDOMSSOITEMREFUND }
	 * 
	 */
	public ZSTRSDOMSSOITEMREFUND getREFUND()
	{
		return refund;
	}

	/**
	 * Sets the value of the refund property.
	 * 
	 * @param value
	 *           allowed object is {@link ZSTRSDOMSSOITEMREFUND }
	 * 
	 */
	public void setREFUND(final ZSTRSDOMSSOITEMREFUND value)
	{
		this.refund = value;
	}

}
