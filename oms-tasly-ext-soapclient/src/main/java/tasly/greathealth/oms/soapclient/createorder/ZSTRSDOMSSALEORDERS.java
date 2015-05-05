
package tasly.greathealth.oms.soapclient.createorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Sales Orders
 * 
 * <p>Java class for ZSTR_SD_OMS_SALE_ORDERS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_SALE_ORDERS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ORDERS" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SALE_ORDER_TAB" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSTR_SD_OMS_SALE_ORDERS", propOrder = {
    "orders"
})
public class ZSTRSDOMSSALEORDERS {

    @XmlElement(name = "ORDERS")
    protected ZSTRSDOMSSALEORDERTAB orders;

    /**
     * Gets the value of the orders property.
     * 
     * @return
     *     possible object is
     *     {@link ZSTRSDOMSSALEORDERTAB }
     *     
     */
    public ZSTRSDOMSSALEORDERTAB getORDERS() {
        return orders;
    }

    /**
     * Sets the value of the orders property.
     * 
     * @param value
     *     allowed object is
     *     {@link ZSTRSDOMSSALEORDERTAB }
     *     
     */
    public void setORDERS(ZSTRSDOMSSALEORDERTAB value) {
        this.orders = value;
    }

}
