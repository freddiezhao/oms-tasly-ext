package tasly.greathealth.oms.soapclient.updateorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for ZSTR_SD_OMS_SHIP_TO complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_SHIP_TO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ECC_SHIPTO_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SHIPTO_NAME" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SHIPTO_REGION" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SHIPTO_CITY" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SHIPTO_DISTRICT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SHIPTO_ADDRESS" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SHIPTO_ZIPCODE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SHIPTO_MOB" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SHIPTO_TEL" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSTR_SD_OMS_SHIP_TO", propOrder = {"eccshiptoid", "eccshiptoname", "eccshiptoregion", "eccshiptocity",
		"eccshiptodistrict", "eccshiptoaddress", "eccshiptozipcode", "eccshiptomob", "eccshiptotel"})
public class ZSTRSDOMSSHIPTO
{

	@XmlElement(name = "ECC_SHIPTO_ID")
	protected String eccshiptoid;
	@XmlElement(name = "ECC_SHIPTO_NAME")
	protected String eccshiptoname;
	@XmlElement(name = "ECC_SHIPTO_REGION")
	protected String eccshiptoregion;
	@XmlElement(name = "ECC_SHIPTO_CITY")
	protected String eccshiptocity;
	@XmlElement(name = "ECC_SHIPTO_DISTRICT")
	protected String eccshiptodistrict;
	@XmlElement(name = "ECC_SHIPTO_ADDRESS")
	protected String eccshiptoaddress;
	@XmlElement(name = "ECC_SHIPTO_ZIPCODE")
	protected String eccshiptozipcode;
	@XmlElement(name = "ECC_SHIPTO_MOB")
	protected String eccshiptomob;
	@XmlElement(name = "ECC_SHIPTO_TEL")
	protected String eccshiptotel;

	/**
	 * Gets the value of the eccshiptoid property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCSHIPTOID()
	{
		return eccshiptoid;
	}

	/**
	 * Sets the value of the eccshiptoid property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCSHIPTOID(final String value)
	{
		this.eccshiptoid = value;
	}

	/**
	 * Gets the value of the eccshiptoname property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCSHIPTONAME()
	{
		return eccshiptoname;
	}

	/**
	 * Sets the value of the eccshiptoname property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCSHIPTONAME(final String value)
	{
		this.eccshiptoname = value;
	}

	/**
	 * Gets the value of the eccshiptoregion property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCSHIPTOREGION()
	{
		return eccshiptoregion;
	}

	/**
	 * Sets the value of the eccshiptoregion property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCSHIPTOREGION(final String value)
	{
		this.eccshiptoregion = value;
	}

	/**
	 * Gets the value of the eccshiptocity property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCSHIPTOCITY()
	{
		return eccshiptocity;
	}

	/**
	 * Sets the value of the eccshiptocity property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCSHIPTOCITY(final String value)
	{
		this.eccshiptocity = value;
	}

	/**
	 * Gets the value of the eccshiptodistrict property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCSHIPTODISTRICT()
	{
		return eccshiptodistrict;
	}

	/**
	 * Sets the value of the eccshiptodistrict property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCSHIPTODISTRICT(final String value)
	{
		this.eccshiptodistrict = value;
	}

	/**
	 * Gets the value of the eccshiptoaddress property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCSHIPTOADDRESS()
	{
		return eccshiptoaddress;
	}

	/**
	 * Sets the value of the eccshiptoaddress property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCSHIPTOADDRESS(final String value)
	{
		this.eccshiptoaddress = value;
	}

	/**
	 * Gets the value of the eccshiptozipcode property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCSHIPTOZIPCODE()
	{
		return eccshiptozipcode;
	}

	/**
	 * Sets the value of the eccshiptozipcode property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCSHIPTOZIPCODE(final String value)
	{
		this.eccshiptozipcode = value;
	}

	/**
	 * Gets the value of the eccshiptomob property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCSHIPTOMOB()
	{
		return eccshiptomob;
	}

	/**
	 * Sets the value of the eccshiptomob property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCSHIPTOMOB(final String value)
	{
		this.eccshiptomob = value;
	}

	/**
	 * Gets the value of the eccshiptotel property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCSHIPTOTEL()
	{
		return eccshiptotel;
	}

	/**
	 * Sets the value of the eccshiptotel property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCSHIPTOTEL(final String value)
	{
		this.eccshiptotel = value;
	}

}
