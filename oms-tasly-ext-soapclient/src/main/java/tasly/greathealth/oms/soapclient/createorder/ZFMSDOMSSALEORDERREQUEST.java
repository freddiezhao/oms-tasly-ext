
package tasly.greathealth.oms.soapclient.createorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ZFM_SD_OMS_SALE_ORDER_REQUEST complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZFM_SD_OMS_SALE_ORDER_REQUEST">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BASEINFO" type="{urn:sap-com:document:sap:rfc:functions}ZSTRU_PI_BASEINFO2" minOccurs="0"/>
 *         &lt;element name="MESSAGE" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SALE_ORDERS" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZFM_SD_OMS_SALE_ORDER_REQUEST", propOrder = {
    "baseinfo",
    "message"
})
public class ZFMSDOMSSALEORDERREQUEST {

    @XmlElement(name = "BASEINFO")
    protected ZSTRUPIBASEINFO2 baseinfo;
    @XmlElement(name = "MESSAGE")
    protected ZSTRSDOMSSALEORDERS message;

    /**
     * Gets the value of the baseinfo property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRUPIBASEINFO2 }
     *     
     */
    public ZSTRUPIBASEINFO2 getBASEINFO() {
        return baseinfo;
    }

    /**
     * Sets the value of the baseinfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRUPIBASEINFO2 }
     *     
     */
    public void setBASEINFO(ZSTRUPIBASEINFO2 value) {
        this.baseinfo = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSSALEORDERS }
     *     
     */
    public ZSTRSDOMSSALEORDERS getMESSAGE() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSSALEORDERS }
     *     
     */
    public void setMESSAGE(ZSTRSDOMSSALEORDERS value) {
        this.message = value;
    }

}
