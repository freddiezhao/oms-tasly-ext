package tasly.greathealth.oms.soapclient.updateorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Bill-to of Sales Order
 *
 * <p>
 * Java class for ZSTR_SD_OMS_BILL_TO complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_BILL_TO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ECC_BILLTO_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_INVOICE_TYPE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_INVOICE_TITLE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_TAXPAYER_NUMBER" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_BANK_NAME" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_BANK_NUMBER" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_CUSTOMER_ADDRESS" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_CUSTOMER_PHONE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_INVOICE_CONTENT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="128"/>
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
@XmlType(name = "ZSTR_SD_OMS_BILL_TO", propOrder = {"eccbilltoid", "eccinvoicetype", "eccinvoicetitle", "ecctaxpayernumber",
		"eccbankname", "eccbanknumber", "ecccustomeraddress", "ecccustomerphone", "eccinvoicecontent"})
public class ZSTRSDOMSBILLTO
{

	@XmlElement(name = "ECC_BILLTO_ID")
	protected String eccbilltoid;
	@XmlElement(name = "ECC_INVOICE_TYPE")
	protected String eccinvoicetype;
	@XmlElement(name = "ECC_INVOICE_TITLE")
	protected String eccinvoicetitle;
	@XmlElement(name = "ECC_TAXPAYER_NUMBER")
	protected String ecctaxpayernumber;
	@XmlElement(name = "ECC_BANK_NAME")
	protected String eccbankname;
	@XmlElement(name = "ECC_BANK_NUMBER")
	protected String eccbanknumber;
	@XmlElement(name = "ECC_CUSTOMER_ADDRESS")
	protected String ecccustomeraddress;
	@XmlElement(name = "ECC_CUSTOMER_PHONE")
	protected String ecccustomerphone;
	@XmlElement(name = "ECC_INVOICE_CONTENT")
	protected String eccinvoicecontent;

	/**
	 * Gets the value of the eccbilltoid property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCBILLTOID()
	{
		return eccbilltoid;
	}

	/**
	 * Sets the value of the eccbilltoid property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCBILLTOID(final String value)
	{
		this.eccbilltoid = value;
	}

	/**
	 * Gets the value of the eccinvoicetype property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCINVOICETYPE()
	{
		return eccinvoicetype;
	}

	/**
	 * Sets the value of the eccinvoicetype property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCINVOICETYPE(final String value)
	{
		this.eccinvoicetype = value;
	}

	/**
	 * Gets the value of the eccinvoicetitle property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCINVOICETITLE()
	{
		return eccinvoicetitle;
	}

	/**
	 * Sets the value of the eccinvoicetitle property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCINVOICETITLE(final String value)
	{
		this.eccinvoicetitle = value;
	}

	/**
	 * Gets the value of the ecctaxpayernumber property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCTAXPAYERNUMBER()
	{
		return ecctaxpayernumber;
	}

	/**
	 * Sets the value of the ecctaxpayernumber property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCTAXPAYERNUMBER(final String value)
	{
		this.ecctaxpayernumber = value;
	}

	/**
	 * Gets the value of the eccbankname property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCBANKNAME()
	{
		return eccbankname;
	}

	/**
	 * Sets the value of the eccbankname property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCBANKNAME(final String value)
	{
		this.eccbankname = value;
	}

	/**
	 * Gets the value of the eccbanknumber property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCBANKNUMBER()
	{
		return eccbanknumber;
	}

	/**
	 * Sets the value of the eccbanknumber property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCBANKNUMBER(final String value)
	{
		this.eccbanknumber = value;
	}

	/**
	 * Gets the value of the ecccustomeraddress property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCCUSTOMERADDRESS()
	{
		return ecccustomeraddress;
	}

	/**
	 * Sets the value of the ecccustomeraddress property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCCUSTOMERADDRESS(final String value)
	{
		this.ecccustomeraddress = value;
	}

	/**
	 * Gets the value of the ecccustomerphone property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCCUSTOMERPHONE()
	{
		return ecccustomerphone;
	}

	/**
	 * Sets the value of the ecccustomerphone property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCCUSTOMERPHONE(final String value)
	{
		this.ecccustomerphone = value;
	}

	/**
	 * Gets the value of the eccinvoicecontent property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getECCINVOICECONTENT()
	{
		return eccinvoicecontent;
	}

	/**
	 * Sets the value of the eccinvoicecontent property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setECCINVOICECONTENT(final String value)
	{
		this.eccinvoicecontent = value;
	}

}
