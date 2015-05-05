
package tasly.greathealth.oms.soapclient.createorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZSTR_SD_OMS_PAYER complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_PAYER">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ECC_PAYER_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
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
@XmlType(name = "ZSTR_SD_OMS_PAYER", propOrder = {
    "eccpayerid"
})
public class ZSTRSDOMSPAYER {

    @XmlElement(name = "ECC_PAYER_ID")
    protected String eccpayerid;

    /**
     * Gets the value of the eccpayerid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getECCPAYERID() {
        return eccpayerid;
    }

    /**
     * Sets the value of the eccpayerid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setECCPAYERID(String value) {
        this.eccpayerid = value;
    }

}
