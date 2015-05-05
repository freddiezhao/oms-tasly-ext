
package tasly.greathealth.oms.soapclient.createorder;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZSTR_SD_OMS_SALE_ORDER_HEAD complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_SALE_ORDER_HEAD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OPERATION_TYPE_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_ORDER_TYPE_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
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
 *         &lt;element name="ORIGINAL_ORDER_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SAP_ORDER_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SALES_ORG" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_DISTRIBUTIO_CHANNEL" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_DIVISION" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PAYMENT_TERM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORDER_DATE" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="PAYMENT_DATE" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="MEMBERSHIP_POINT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="15"/>
 *               &lt;fractionDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="HEADER_EXTENSION1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION19" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HEADER_EXTENSION20" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSTR_SD_OMS_SALE_ORDER_HEAD", propOrder = {
    "operationtypeid",
    "eccordertypeid",
    "omsorderid",
    "originalorderid",
    "saporderid",
    "eccsalesorg",
    "eccdistributiochannel",
    "eccdivision",
    "paymentterm",
    "orderdate",
    "paymentdate",
    "membershippoint",
    "headerextension1",
    "headerextension2",
    "headerextension3",
    "headerextension4",
    "headerextension5",
    "headerextension6",
    "headerextension7",
    "headerextension8",
    "headerextension9",
    "headerextension10",
    "headerextension11",
    "headerextension12",
    "headerextension13",
    "headerextension14",
    "headerextension15",
    "headerextension16",
    "headerextension17",
    "headerextension18",
    "headerextension19",
    "headerextension20"
})
public class ZSTRSDOMSSALEORDERHEAD {

    @XmlElement(name = "OPERATION_TYPE_ID")
    protected String operationtypeid;
    @XmlElement(name = "ECC_ORDER_TYPE_ID")
    protected String eccordertypeid;
    @XmlElement(name = "OMS_ORDER_ID")
    protected String omsorderid;
    @XmlElement(name = "ORIGINAL_ORDER_ID")
    protected String originalorderid;
    @XmlElement(name = "SAP_ORDER_ID")
    protected String saporderid;
    @XmlElement(name = "ECC_SALES_ORG")
    protected String eccsalesorg;
    @XmlElement(name = "ECC_DISTRIBUTIO_CHANNEL")
    protected String eccdistributiochannel;
    @XmlElement(name = "ECC_DIVISION")
    protected String eccdivision;
    @XmlElement(name = "PAYMENT_TERM")
    protected String paymentterm;
    @XmlElement(name = "ORDER_DATE")
    protected String orderdate;
    @XmlElement(name = "PAYMENT_DATE")
    protected String paymentdate;
    @XmlElement(name = "MEMBERSHIP_POINT")
    protected BigDecimal membershippoint;
    @XmlElement(name = "HEADER_EXTENSION1")
    protected String headerextension1;
    @XmlElement(name = "HEADER_EXTENSION2")
    protected String headerextension2;
    @XmlElement(name = "HEADER_EXTENSION3")
    protected String headerextension3;
    @XmlElement(name = "HEADER_EXTENSION4")
    protected String headerextension4;
    @XmlElement(name = "HEADER_EXTENSION5")
    protected String headerextension5;
    @XmlElement(name = "HEADER_EXTENSION6")
    protected String headerextension6;
    @XmlElement(name = "HEADER_EXTENSION7")
    protected String headerextension7;
    @XmlElement(name = "HEADER_EXTENSION8")
    protected String headerextension8;
    @XmlElement(name = "HEADER_EXTENSION9")
    protected String headerextension9;
    @XmlElement(name = "HEADER_EXTENSION10")
    protected String headerextension10;
    @XmlElement(name = "HEADER_EXTENSION11")
    protected String headerextension11;
    @XmlElement(name = "HEADER_EXTENSION12")
    protected String headerextension12;
    @XmlElement(name = "HEADER_EXTENSION13")
    protected String headerextension13;
    @XmlElement(name = "HEADER_EXTENSION14")
    protected String headerextension14;
    @XmlElement(name = "HEADER_EXTENSION15")
    protected String headerextension15;
    @XmlElement(name = "HEADER_EXTENSION16")
    protected String headerextension16;
    @XmlElement(name = "HEADER_EXTENSION17")
    protected String headerextension17;
    @XmlElement(name = "HEADER_EXTENSION18")
    protected String headerextension18;
    @XmlElement(name = "HEADER_EXTENSION19")
    protected String headerextension19;
    @XmlElement(name = "HEADER_EXTENSION20")
    protected String headerextension20;

    /**
     * Gets the value of the operationtypeid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOPERATIONTYPEID() {
        return operationtypeid;
    }

    /**
     * Sets the value of the operationtypeid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOPERATIONTYPEID(String value) {
        this.operationtypeid = value;
    }

    /**
     * Gets the value of the eccordertypeid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getECCORDERTYPEID() {
        return eccordertypeid;
    }

    /**
     * Sets the value of the eccordertypeid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setECCORDERTYPEID(String value) {
        this.eccordertypeid = value;
    }

    /**
     * Gets the value of the omsorderid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOMSORDERID() {
        return omsorderid;
    }

    /**
     * Sets the value of the omsorderid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOMSORDERID(String value) {
        this.omsorderid = value;
    }

    /**
     * Gets the value of the originalorderid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGINALORDERID() {
        return originalorderid;
    }

    /**
     * Sets the value of the originalorderid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGINALORDERID(String value) {
        this.originalorderid = value;
    }

    /**
     * Gets the value of the saporderid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSAPORDERID() {
        return saporderid;
    }

    /**
     * Sets the value of the saporderid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSAPORDERID(String value) {
        this.saporderid = value;
    }

    /**
     * Gets the value of the eccsalesorg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getECCSALESORG() {
        return eccsalesorg;
    }

    /**
     * Sets the value of the eccsalesorg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setECCSALESORG(String value) {
        this.eccsalesorg = value;
    }

    /**
     * Gets the value of the eccdistributiochannel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getECCDISTRIBUTIOCHANNEL() {
        return eccdistributiochannel;
    }

    /**
     * Sets the value of the eccdistributiochannel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setECCDISTRIBUTIOCHANNEL(String value) {
        this.eccdistributiochannel = value;
    }

    /**
     * Gets the value of the eccdivision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getECCDIVISION() {
        return eccdivision;
    }

    /**
     * Sets the value of the eccdivision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setECCDIVISION(String value) {
        this.eccdivision = value;
    }

    /**
     * Gets the value of the paymentterm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAYMENTTERM() {
        return paymentterm;
    }

    /**
     * Sets the value of the paymentterm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAYMENTTERM(String value) {
        this.paymentterm = value;
    }

    /**
     * Gets the value of the orderdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORDERDATE() {
        return orderdate;
    }

    /**
     * Sets the value of the orderdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORDERDATE(String value) {
        this.orderdate = value;
    }

    /**
     * Gets the value of the paymentdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAYMENTDATE() {
        return paymentdate;
    }

    /**
     * Sets the value of the paymentdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAYMENTDATE(String value) {
        this.paymentdate = value;
    }

    /**
     * Gets the value of the membershippoint property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMEMBERSHIPPOINT() {
        return membershippoint;
    }

    /**
     * Sets the value of the membershippoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMEMBERSHIPPOINT(BigDecimal value) {
        this.membershippoint = value;
    }

    /**
     * Gets the value of the headerextension1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION1() {
        return headerextension1;
    }

    /**
     * Sets the value of the headerextension1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION1(String value) {
        this.headerextension1 = value;
    }

    /**
     * Gets the value of the headerextension2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION2() {
        return headerextension2;
    }

    /**
     * Sets the value of the headerextension2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION2(String value) {
        this.headerextension2 = value;
    }

    /**
     * Gets the value of the headerextension3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION3() {
        return headerextension3;
    }

    /**
     * Sets the value of the headerextension3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION3(String value) {
        this.headerextension3 = value;
    }

    /**
     * Gets the value of the headerextension4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION4() {
        return headerextension4;
    }

    /**
     * Sets the value of the headerextension4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION4(String value) {
        this.headerextension4 = value;
    }

    /**
     * Gets the value of the headerextension5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION5() {
        return headerextension5;
    }

    /**
     * Sets the value of the headerextension5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION5(String value) {
        this.headerextension5 = value;
    }

    /**
     * Gets the value of the headerextension6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION6() {
        return headerextension6;
    }

    /**
     * Sets the value of the headerextension6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION6(String value) {
        this.headerextension6 = value;
    }

    /**
     * Gets the value of the headerextension7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION7() {
        return headerextension7;
    }

    /**
     * Sets the value of the headerextension7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION7(String value) {
        this.headerextension7 = value;
    }

    /**
     * Gets the value of the headerextension8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION8() {
        return headerextension8;
    }

    /**
     * Sets the value of the headerextension8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION8(String value) {
        this.headerextension8 = value;
    }

    /**
     * Gets the value of the headerextension9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION9() {
        return headerextension9;
    }

    /**
     * Sets the value of the headerextension9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION9(String value) {
        this.headerextension9 = value;
    }

    /**
     * Gets the value of the headerextension10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION10() {
        return headerextension10;
    }

    /**
     * Sets the value of the headerextension10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION10(String value) {
        this.headerextension10 = value;
    }

    /**
     * Gets the value of the headerextension11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION11() {
        return headerextension11;
    }

    /**
     * Sets the value of the headerextension11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION11(String value) {
        this.headerextension11 = value;
    }

    /**
     * Gets the value of the headerextension12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION12() {
        return headerextension12;
    }

    /**
     * Sets the value of the headerextension12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION12(String value) {
        this.headerextension12 = value;
    }

    /**
     * Gets the value of the headerextension13 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION13() {
        return headerextension13;
    }

    /**
     * Sets the value of the headerextension13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION13(String value) {
        this.headerextension13 = value;
    }

    /**
     * Gets the value of the headerextension14 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION14() {
        return headerextension14;
    }

    /**
     * Sets the value of the headerextension14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION14(String value) {
        this.headerextension14 = value;
    }

    /**
     * Gets the value of the headerextension15 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION15() {
        return headerextension15;
    }

    /**
     * Sets the value of the headerextension15 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION15(String value) {
        this.headerextension15 = value;
    }

    /**
     * Gets the value of the headerextension16 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION16() {
        return headerextension16;
    }

    /**
     * Sets the value of the headerextension16 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION16(String value) {
        this.headerextension16 = value;
    }

    /**
     * Gets the value of the headerextension17 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION17() {
        return headerextension17;
    }

    /**
     * Sets the value of the headerextension17 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION17(String value) {
        this.headerextension17 = value;
    }

    /**
     * Gets the value of the headerextension18 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION18() {
        return headerextension18;
    }

    /**
     * Sets the value of the headerextension18 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION18(String value) {
        this.headerextension18 = value;
    }

    /**
     * Gets the value of the headerextension19 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION19() {
        return headerextension19;
    }

    /**
     * Sets the value of the headerextension19 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION19(String value) {
        this.headerextension19 = value;
    }

    /**
     * Gets the value of the headerextension20 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHEADEREXTENSION20() {
        return headerextension20;
    }

    /**
     * Sets the value of the headerextension20 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHEADEREXTENSION20(String value) {
        this.headerextension20 = value;
    }

}
