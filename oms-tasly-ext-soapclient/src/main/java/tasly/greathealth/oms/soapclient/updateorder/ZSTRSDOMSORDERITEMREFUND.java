package tasly.greathealth.oms.soapclient.updateorder;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for ZSTR_SD_OMS_ORDER_ITEM_REFUND complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_ORDER_ITEM_REFUND">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="REFUND_OMS_ITEM_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="REFUND_TYPE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="REFUND_QUANTITY" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="REFUND_AMOUNT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="15"/>
 *               &lt;fractionDigits value="3"/>
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
@XmlType(name = "ZSTR_SD_OMS_ORDER_ITEM_REFUND", propOrder = {"refundomsitemid", "refundtype", "refundquantity", "refundamount"})
public class ZSTRSDOMSORDERITEMREFUND
{

	@XmlElement(name = "REFUND_OMS_ITEM_ID")
	protected String refundomsitemid;
	@XmlElement(name = "REFUND_TYPE")
	protected String refundtype;
	@XmlElement(name = "REFUND_QUANTITY")
	protected String refundquantity;
	@XmlElement(name = "REFUND_AMOUNT")
	protected BigDecimal refundamount;

	/**
	 * Gets the value of the refundomsitemid property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getREFUNDOMSITEMID()
	{
		return refundomsitemid;
	}

	/**
	 * Sets the value of the refundomsitemid property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setREFUNDOMSITEMID(final String value)
	{
		this.refundomsitemid = value;
	}

	/**
	 * Gets the value of the refundtype property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getREFUNDTYPE()
	{
		return refundtype;
	}

	/**
	 * Sets the value of the refundtype property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setREFUNDTYPE(final String value)
	{
		this.refundtype = value;
	}

	/**
	 * Gets the value of the refundquantity property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getREFUNDQUANTITY()
	{
		return refundquantity;
	}

	/**
	 * Sets the value of the refundquantity property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setREFUNDQUANTITY(final String value)
	{
		this.refundquantity = value;
	}

	/**
	 * Gets the value of the refundamount property.
	 * 
	 * @return
	 *         possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getREFUNDAMOUNT()
	{
		return refundamount;
	}

	/**
	 * Sets the value of the refundamount property.
	 * 
	 * @param value
	 *           allowed object is {@link BigDecimal }
	 * 
	 */
	public void setREFUNDAMOUNT(final BigDecimal value)
	{
		this.refundamount = value;
	}

}
