package tasly.greathealth.oms.soapclient.updateorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * BASEINFO
 *
 * <p>
 * Java class for ZSTRU_PI_BASEINFO2 complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ZSTRU_PI_BASEINFO2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MSGID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PMSGID" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SENDTIME" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="S_SYSTEM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SERVICENAME" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="T_SYSTEM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RETRY" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
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
@XmlType(name = "ZSTRU_PI_BASEINFO2", propOrder = {"msgid", "pmsgid", "sendtime", "ssystem", "servicename", "tsystem", "retry"})
public class ZSTRUPIBASEINFO2
{

	@XmlElement(name = "MSGID")
	protected String msgid;
	@XmlElement(name = "PMSGID")
	protected String pmsgid;
	@XmlElement(name = "SENDTIME")
	protected String sendtime;
	@XmlElement(name = "S_SYSTEM")
	protected String ssystem;
	@XmlElement(name = "SERVICENAME")
	protected String servicename;
	@XmlElement(name = "T_SYSTEM")
	protected String tsystem;
	@XmlElement(name = "RETRY")
	protected String retry;

	/**
	 * Gets the value of the msgid property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getMSGID()
	{
		return msgid;
	}

	/**
	 * Sets the value of the msgid property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setMSGID(final String value)
	{
		this.msgid = value;
	}

	/**
	 * Gets the value of the pmsgid property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getPMSGID()
	{
		return pmsgid;
	}

	/**
	 * Sets the value of the pmsgid property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setPMSGID(final String value)
	{
		this.pmsgid = value;
	}

	/**
	 * Gets the value of the sendtime property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getSENDTIME()
	{
		return sendtime;
	}

	/**
	 * Sets the value of the sendtime property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setSENDTIME(final String value)
	{
		this.sendtime = value;
	}

	/**
	 * Gets the value of the ssystem property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getSSYSTEM()
	{
		return ssystem;
	}

	/**
	 * Sets the value of the ssystem property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setSSYSTEM(final String value)
	{
		this.ssystem = value;
	}

	/**
	 * Gets the value of the servicename property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getSERVICENAME()
	{
		return servicename;
	}

	/**
	 * Sets the value of the servicename property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setSERVICENAME(final String value)
	{
		this.servicename = value;
	}

	/**
	 * Gets the value of the tsystem property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getTSYSTEM()
	{
		return tsystem;
	}

	/**
	 * Sets the value of the tsystem property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setTSYSTEM(final String value)
	{
		this.tsystem = value;
	}

	/**
	 * Gets the value of the retry property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getRETRY()
	{
		return retry;
	}

	/**
	 * Sets the value of the retry property.
	 * 
	 * @param value
	 *           allowed object is {@link String }
	 * 
	 */
	public void setRETRY(final String value)
	{
		this.retry = value;
	}

}
