
package tasly.greathealth.oms.soapclient.createorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZSTR_SD_OMS_SALE_ORDER complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_SALE_ORDER">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HEADER" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SALE_ORDER_HEAD" minOccurs="0"/>
 *         &lt;element name="ECC_SOLD_TO" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SOLD_TO" minOccurs="0"/>
 *         &lt;element name="ECC_BILL_TO" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_BILL_TO" minOccurs="0"/>
 *         &lt;element name="ECC_PAYER" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_PAYER" minOccurs="0"/>
 *         &lt;element name="ECC_SHIP_TO" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SHIP_TO" minOccurs="0"/>
 *         &lt;element name="ECC_DELIVERY" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_DELIVERY" minOccurs="0"/>
 *         &lt;element name="OMS_CUSTOMER_NOTES" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OMS_AGENT_NOTES" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="HEADER_CONDITIONS" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_HEAD_COND_TAB" minOccurs="0"/>
 *         &lt;element name="ITEMS" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SO_ITEM_TAB" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSTR_SD_OMS_SALE_ORDER", propOrder = {
    "header",
    "eccsoldto",
    "eccbillto",
    "eccpayer",
    "eccshipto",
    "eccdelivery",
    "omscustomernotes",
    "omsagentnotes",
    "headerconditions",
    "items"
})
public class ZSTRSDOMSSALEORDER {

    @XmlElement(name = "HEADER")
    protected ZSTRSDOMSSALEORDERHEAD header;
    @XmlElement(name = "ECC_SOLD_TO")
    protected ZSTRSDOMSSOLDTO eccsoldto;
    @XmlElement(name = "ECC_BILL_TO")
    protected ZSTRSDOMSBILLTO eccbillto;
    @XmlElement(name = "ECC_PAYER")
    protected ZSTRSDOMSPAYER eccpayer;
    @XmlElement(name = "ECC_SHIP_TO")
    protected ZSTRSDOMSSHIPTO eccshipto;
    @XmlElement(name = "ECC_DELIVERY")
    protected ZSTRSDOMSDELIVERY eccdelivery;
    @XmlElement(name = "OMS_CUSTOMER_NOTES")
    protected String omscustomernotes;
    @XmlElement(name = "OMS_AGENT_NOTES")
    protected String omsagentnotes;
    @XmlElement(name = "HEADER_CONDITIONS")
    protected ZSTRSDOMSHEADCONDTAB headerconditions;
    @XmlElement(name = "ITEMS")
    protected ZSTRSDOMSSOITEMTAB items;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSSALEORDERHEAD }
     *     
     */
    public ZSTRSDOMSSALEORDERHEAD getHEADER() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSSALEORDERHEAD }
     *     
     */
    public void setHEADER(ZSTRSDOMSSALEORDERHEAD value) {
        this.header = value;
    }

    /**
     * Gets the value of the eccsoldto property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSSOLDTO }
     *     
     */
    public ZSTRSDOMSSOLDTO getECCSOLDTO() {
        return eccsoldto;
    }

    /**
     * Sets the value of the eccsoldto property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSSOLDTO }
     *     
     */
    public void setECCSOLDTO(ZSTRSDOMSSOLDTO value) {
        this.eccsoldto = value;
    }

    /**
     * Gets the value of the eccbillto property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSBILLTO }
     *     
     */
    public ZSTRSDOMSBILLTO getECCBILLTO() {
        return eccbillto;
    }

    /**
     * Sets the value of the eccbillto property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSBILLTO }
     *     
     */
    public void setECCBILLTO(ZSTRSDOMSBILLTO value) {
        this.eccbillto = value;
    }

    /**
     * Gets the value of the eccpayer property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSPAYER }
     *     
     */
    public ZSTRSDOMSPAYER getECCPAYER() {
        return eccpayer;
    }

    /**
     * Sets the value of the eccpayer property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSPAYER }
     *     
     */
    public void setECCPAYER(ZSTRSDOMSPAYER value) {
        this.eccpayer = value;
    }

    /**
     * Gets the value of the eccshipto property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSSHIPTO }
     *     
     */
    public ZSTRSDOMSSHIPTO getECCSHIPTO() {
        return eccshipto;
    }

    /**
     * Sets the value of the eccshipto property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSSHIPTO }
     *     
     */
    public void setECCSHIPTO(ZSTRSDOMSSHIPTO value) {
        this.eccshipto = value;
    }

    /**
     * Gets the value of the eccdelivery property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSDELIVERY }
     *     
     */
    public ZSTRSDOMSDELIVERY getECCDELIVERY() {
        return eccdelivery;
    }

    /**
     * Sets the value of the eccdelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSDELIVERY }
     *     
     */
    public void setECCDELIVERY(ZSTRSDOMSDELIVERY value) {
        this.eccdelivery = value;
    }

    /**
     * Gets the value of the omscustomernotes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOMSCUSTOMERNOTES() {
        return omscustomernotes;
    }

    /**
     * Sets the value of the omscustomernotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOMSCUSTOMERNOTES(String value) {
        this.omscustomernotes = value;
    }

    /**
     * Gets the value of the omsagentnotes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOMSAGENTNOTES() {
        return omsagentnotes;
    }

    /**
     * Sets the value of the omsagentnotes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOMSAGENTNOTES(String value) {
        this.omsagentnotes = value;
    }

    /**
     * Gets the value of the headerconditions property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSHEADCONDTAB }
     *     
     */
    public ZSTRSDOMSHEADCONDTAB getHEADERCONDITIONS() {
        return headerconditions;
    }

    /**
     * Sets the value of the headerconditions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSHEADCONDTAB }
     *     
     */
    public void setHEADERCONDITIONS(ZSTRSDOMSHEADCONDTAB value) {
        this.headerconditions = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSSOITEMTAB }
     *     
     */
    public ZSTRSDOMSSOITEMTAB getITEMS() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSSOITEMTAB }
     *     
     */
    public void setITEMS(ZSTRSDOMSSOITEMTAB value) {
        this.items = value;
    }

}
