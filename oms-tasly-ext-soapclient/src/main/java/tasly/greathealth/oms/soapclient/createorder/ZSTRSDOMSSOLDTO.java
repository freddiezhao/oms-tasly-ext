
package tasly.greathealth.oms.soapclient.createorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Sold-to of Sales Order
 * 
 * <p>Java class for ZSTR_SD_OMS_SOLD_TO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_SOLD_TO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ECC_SOLDTO_ID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ECC_SOLDTO_NICKNAME" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="35"/>
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
@XmlType(name = "ZSTR_SD_OMS_SOLD_TO", propOrder = {
    "eccsoldtoid",
    "eccsoldtonickname"
})
public class ZSTRSDOMSSOLDTO {

    @XmlElement(name = "ECC_SOLDTO_ID")
    protected String eccsoldtoid;
    @XmlElement(name = "ECC_SOLDTO_NICKNAME")
    protected String eccsoldtonickname;

    /**
     * Gets the value of the eccsoldtoid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getECCSOLDTOID() {
        return eccsoldtoid;
    }

    /**
     * Sets the value of the eccsoldtoid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setECCSOLDTOID(String value) {
        this.eccsoldtoid = value;
    }

    /**
     * Gets the value of the eccsoldtonickname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getECCSOLDTONICKNAME() {
        return eccsoldtonickname;
    }

    /**
     * Sets the value of the eccsoldtonickname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setECCSOLDTONICKNAME(String value) {
        this.eccsoldtonickname = value;
    }

}
