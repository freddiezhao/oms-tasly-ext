package tasly.greathealth.oms.soapclient.updateorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="I_REQUEST" type="{urn:sap-com:document:sap:rfc:functions}ZFM_SD_OMS_SALEORDER_COMMON_IN"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "ZSD_OMS_SALESORDER_CHANGE")
public class ZSDOMSSALESORDERCHANGE
{

	@XmlElement(name = "I_REQUEST", required = true)
	protected ZFMSDOMSSALEORDERCOMMONIN irequest;

	/**
	 * Gets the value of the irequest property.
	 * 
	 * @return
	 *         possible object is {@link ZFMSDOMSSALEORDERCOMMONIN }
	 * 
	 */
	public ZFMSDOMSSALEORDERCOMMONIN getIREQUEST()
	{
		return irequest;
	}

	/**
	 * Sets the value of the irequest property.
	 * 
	 * @param value
	 *           allowed object is {@link ZFMSDOMSSALEORDERCOMMONIN }
	 * 
	 */
	public void setIREQUEST(final ZFMSDOMSSALEORDERCOMMONIN value)
	{
		this.irequest = value;
	}

}
