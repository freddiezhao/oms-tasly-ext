
package tasly.greathealth.oms.soapclient.createorder;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZSTR_SD_OMS_SALE_ORDER_ITEM complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_SALE_ORDER_ITEM">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OMS_ITEM_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SKU_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="18"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BASE_UNIT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="QUANTITY" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="13"/>
 *               &lt;fractionDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZFREE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="THIRD_PARTY_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="128"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="THIRD_PARTY_ITEM_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="THIRD_PARTY_PAYMENT_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ITEM_CONDITIONS" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_ITEM_COND_TAB" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION19" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEM_EXTENSION20" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSTR_SD_OMS_SALE_ORDER_ITEM", propOrder = {
    "omsitemid",
    "skuid",
    "baseunit",
    "quantity",
    "zfree",
    "thirdpartyid",
    "thirdpartyitemid",
    "thirdpartypaymentid",
    "itemconditions",
    "itemextension1",
    "itemextension2",
    "itemextension3",
    "itemextension4",
    "itemextension5",
    "itemextension6",
    "itemextension7",
    "itemextension8",
    "itemextension9",
    "itemextension10",
    "itemextension11",
    "itemextension12",
    "itemextension13",
    "itemextension14",
    "itemextension15",
    "itemextension16",
    "itemextension17",
    "itemextension18",
    "itemextension19",
    "itemextension20"
})
public class ZSTRSDOMSSALEORDERITEM {

    @XmlElement(name = "OMS_ITEM_ID")
    protected String omsitemid;
    @XmlElement(name = "SKU_ID")
    protected String skuid;
    @XmlElement(name = "BASE_UNIT")
    protected String baseunit;
    @XmlElement(name = "QUANTITY")
    protected BigDecimal quantity;
    @XmlElement(name = "ZFREE")
    protected String zfree;
    @XmlElement(name = "THIRD_PARTY_ID")
    protected String thirdpartyid;
    @XmlElement(name = "THIRD_PARTY_ITEM_ID")
    protected String thirdpartyitemid;
    @XmlElement(name = "THIRD_PARTY_PAYMENT_ID")
    protected String thirdpartypaymentid;
    @XmlElement(name = "ITEM_CONDITIONS")
    protected ZSTRSDOMSITEMCONDTAB itemconditions;
    @XmlElement(name = "ITEM_EXTENSION1")
    protected String itemextension1;
    @XmlElement(name = "ITEM_EXTENSION2")
    protected String itemextension2;
    @XmlElement(name = "ITEM_EXTENSION3")
    protected String itemextension3;
    @XmlElement(name = "ITEM_EXTENSION4")
    protected String itemextension4;
    @XmlElement(name = "ITEM_EXTENSION5")
    protected String itemextension5;
    @XmlElement(name = "ITEM_EXTENSION6")
    protected String itemextension6;
    @XmlElement(name = "ITEM_EXTENSION7")
    protected String itemextension7;
    @XmlElement(name = "ITEM_EXTENSION8")
    protected String itemextension8;
    @XmlElement(name = "ITEM_EXTENSION9")
    protected String itemextension9;
    @XmlElement(name = "ITEM_EXTENSION10")
    protected String itemextension10;
    @XmlElement(name = "ITEM_EXTENSION11")
    protected String itemextension11;
    @XmlElement(name = "ITEM_EXTENSION12")
    protected String itemextension12;
    @XmlElement(name = "ITEM_EXTENSION13")
    protected String itemextension13;
    @XmlElement(name = "ITEM_EXTENSION14")
    protected String itemextension14;
    @XmlElement(name = "ITEM_EXTENSION15")
    protected String itemextension15;
    @XmlElement(name = "ITEM_EXTENSION16")
    protected String itemextension16;
    @XmlElement(name = "ITEM_EXTENSION17")
    protected String itemextension17;
    @XmlElement(name = "ITEM_EXTENSION18")
    protected String itemextension18;
    @XmlElement(name = "ITEM_EXTENSION19")
    protected String itemextension19;
    @XmlElement(name = "ITEM_EXTENSION20")
    protected String itemextension20;

    /**
     * Gets the value of the omsitemid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOMSITEMID() {
        return omsitemid;
    }

    /**
     * Sets the value of the omsitemid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOMSITEMID(String value) {
        this.omsitemid = value;
    }

    /**
     * Gets the value of the skuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSKUID() {
        return skuid;
    }

    /**
     * Sets the value of the skuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSKUID(String value) {
        this.skuid = value;
    }

    /**
     * Gets the value of the baseunit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBASEUNIT() {
        return baseunit;
    }

    /**
     * Sets the value of the baseunit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBASEUNIT(String value) {
        this.baseunit = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQUANTITY() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQUANTITY(BigDecimal value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the zfree property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZFREE() {
        return zfree;
    }

    /**
     * Sets the value of the zfree property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZFREE(String value) {
        this.zfree = value;
    }

    /**
     * Gets the value of the thirdpartyid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTHIRDPARTYID() {
        return thirdpartyid;
    }

    /**
     * Sets the value of the thirdpartyid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTHIRDPARTYID(String value) {
        this.thirdpartyid = value;
    }

    /**
     * Gets the value of the thirdpartyitemid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTHIRDPARTYITEMID() {
        return thirdpartyitemid;
    }

    /**
     * Sets the value of the thirdpartyitemid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTHIRDPARTYITEMID(String value) {
        this.thirdpartyitemid = value;
    }

    /**
     * Gets the value of the thirdpartypaymentid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTHIRDPARTYPAYMENTID() {
        return thirdpartypaymentid;
    }

    /**
     * Sets the value of the thirdpartypaymentid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTHIRDPARTYPAYMENTID(String value) {
        this.thirdpartypaymentid = value;
    }

    /**
     * Gets the value of the itemconditions property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSITEMCONDTAB }
     *     
     */
    public ZSTRSDOMSITEMCONDTAB getITEMCONDITIONS() {
        return itemconditions;
    }

    /**
     * Sets the value of the itemconditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSITEMCONDTAB }
     *     
     */
    public void setITEMCONDITIONS(ZSTRSDOMSITEMCONDTAB value) {
        this.itemconditions = value;
    }

    /**
     * Gets the value of the itemextension1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION1() {
        return itemextension1;
    }

    /**
     * Sets the value of the itemextension1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION1(String value) {
        this.itemextension1 = value;
    }

    /**
     * Gets the value of the itemextension2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION2() {
        return itemextension2;
    }

    /**
     * Sets the value of the itemextension2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION2(String value) {
        this.itemextension2 = value;
    }

    /**
     * Gets the value of the itemextension3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION3() {
        return itemextension3;
    }

    /**
     * Sets the value of the itemextension3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION3(String value) {
        this.itemextension3 = value;
    }

    /**
     * Gets the value of the itemextension4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION4() {
        return itemextension4;
    }

    /**
     * Sets the value of the itemextension4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION4(String value) {
        this.itemextension4 = value;
    }

    /**
     * Gets the value of the itemextension5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION5() {
        return itemextension5;
    }

    /**
     * Sets the value of the itemextension5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION5(String value) {
        this.itemextension5 = value;
    }

    /**
     * Gets the value of the itemextension6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION6() {
        return itemextension6;
    }

    /**
     * Sets the value of the itemextension6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION6(String value) {
        this.itemextension6 = value;
    }

    /**
     * Gets the value of the itemextension7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION7() {
        return itemextension7;
    }

    /**
     * Sets the value of the itemextension7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION7(String value) {
        this.itemextension7 = value;
    }

    /**
     * Gets the value of the itemextension8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION8() {
        return itemextension8;
    }

    /**
     * Sets the value of the itemextension8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION8(String value) {
        this.itemextension8 = value;
    }

    /**
     * Gets the value of the itemextension9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION9() {
        return itemextension9;
    }

    /**
     * Sets the value of the itemextension9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION9(String value) {
        this.itemextension9 = value;
    }

    /**
     * Gets the value of the itemextension10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION10() {
        return itemextension10;
    }

    /**
     * Sets the value of the itemextension10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION10(String value) {
        this.itemextension10 = value;
    }

    /**
     * Gets the value of the itemextension11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION11() {
        return itemextension11;
    }

    /**
     * Sets the value of the itemextension11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION11(String value) {
        this.itemextension11 = value;
    }

    /**
     * Gets the value of the itemextension12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION12() {
        return itemextension12;
    }

    /**
     * Sets the value of the itemextension12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION12(String value) {
        this.itemextension12 = value;
    }

    /**
     * Gets the value of the itemextension13 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION13() {
        return itemextension13;
    }

    /**
     * Sets the value of the itemextension13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION13(String value) {
        this.itemextension13 = value;
    }

    /**
     * Gets the value of the itemextension14 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION14() {
        return itemextension14;
    }

    /**
     * Sets the value of the itemextension14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION14(String value) {
        this.itemextension14 = value;
    }

    /**
     * Gets the value of the itemextension15 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION15() {
        return itemextension15;
    }

    /**
     * Sets the value of the itemextension15 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION15(String value) {
        this.itemextension15 = value;
    }

    /**
     * Gets the value of the itemextension16 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION16() {
        return itemextension16;
    }

    /**
     * Sets the value of the itemextension16 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION16(String value) {
        this.itemextension16 = value;
    }

    /**
     * Gets the value of the itemextension17 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION17() {
        return itemextension17;
    }

    /**
     * Sets the value of the itemextension17 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION17(String value) {
        this.itemextension17 = value;
    }

    /**
     * Gets the value of the itemextension18 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION18() {
        return itemextension18;
    }

    /**
     * Sets the value of the itemextension18 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION18(String value) {
        this.itemextension18 = value;
    }

    /**
     * Gets the value of the itemextension19 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION19() {
        return itemextension19;
    }

    /**
     * Sets the value of the itemextension19 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION19(String value) {
        this.itemextension19 = value;
    }

    /**
     * Gets the value of the itemextension20 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITEMEXTENSION20() {
        return itemextension20;
    }

    /**
     * Sets the value of the itemextension20 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITEMEXTENSION20(String value) {
        this.itemextension20 = value;
    }

}
