package tasly.greathealth.oms.soapclient.updateorder;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for ZSTR_SD_OMS_SALEORDER_COMM_TAB complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ZSTR_SD_OMS_SALEORDER_COMM_TAB">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZSTR_SD_OMS_SALE_ORDER_COMMIN" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZSTR_SD_OMS_SALEORDER_COMM_TAB", propOrder = {"item"})
public class ZSTRSDOMSSALEORDERCOMMTAB
{

	protected List<ZSTRSDOMSSALEORDERCOMMIN> item;

	/**
	 * Gets the value of the item property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the item property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getItem().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link ZSTRSDOMSSALEORDERCOMMIN }
	 * 
	 * 
	 */
	public List<ZSTRSDOMSSALEORDERCOMMIN> getItem()
	{
		if (item == null)
		{
			item = new ArrayList<ZSTRSDOMSSALEORDERCOMMIN>();
		}
		return this.item;
	}

}
