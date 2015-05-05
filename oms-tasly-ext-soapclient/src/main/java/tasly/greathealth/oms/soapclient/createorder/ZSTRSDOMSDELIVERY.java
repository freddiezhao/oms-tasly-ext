
package tasly.greathealth.oms.soapclient.createorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZSTR_SD_OMS_DELIVERY complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_DELIVERY">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ECC_EXPRESS_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_EXPRESS_NAME" minOccurs="0">
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
@XmlType(name = "ZSTR_SD_OMS_DELIVERY", propOrder = {
    "eccexpressid",
    "eccexpressname"
})
public class ZSTRSDOMSDELIVERY {

    @XmlElement(name = "ECC_EXPRESS_ID")
    protected String eccexpressid;
    @XmlElement(name = "ECC_EXPRESS_NAME")
    protected String eccexpressname;

    /**
     * Gets the value of the eccexpressid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getECCEXPRESSID() {
        return eccexpressid;
    }

    /**
     * Sets the value of the eccexpressid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setECCEXPRESSID(String value) {
        this.eccexpressid = value;
    }

    /**
     * Gets the value of the eccexpressname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getECCEXPRESSNAME() {
        return eccexpressname;
    }

    /**
     * Sets the value of the eccexpressname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setECCEXPRESSNAME(String value) {
        this.eccexpressname = value;
    }

}
