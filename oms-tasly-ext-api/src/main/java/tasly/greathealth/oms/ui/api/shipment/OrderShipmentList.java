package tasly.greathealth.oms.ui.api.shipment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "orderShipments")
public class OrderShipmentList implements Serializable
{
	private static final long serialVersionUID = -30376085578364593L;
	private List<OrderShipmentDetail> orderShipmentDetails;

	public OrderShipmentList()
	{
		this.orderShipmentDetails = new ArrayList<OrderShipmentDetail>();
	}

	public void addOrderShipment(final OrderShipmentDetail orderShipmentVO)
	{
		if (this.orderShipmentDetails == null)
		{
			this.orderShipmentDetails = new ArrayList<OrderShipmentDetail>();
		}
		this.orderShipmentDetails.add(orderShipmentVO);
	}

	public List<OrderShipmentDetail> getList()
	{
		return Collections.unmodifiableList(this.orderShipmentDetails);
	}

	@XmlElement(name = "orderShipments")
	public List<OrderShipmentDetail> getOrderShipmentDetailForJaxb()
	{
		return this.orderShipmentDetails;
	}

	public void initializeOrders(final List<OrderShipmentDetail> orderShipmentsVOs)
	{
		assert (this.orderShipmentDetails.isEmpty());
		for (final OrderShipmentDetail orderShipmentVO : orderShipmentsVOs)
		{
			addOrderShipment(orderShipmentVO);
		}
	}

	public void setOrderShipmentDetailForJaxb(final List<OrderShipmentDetail> orderShipmentDetail)
	{
		this.orderShipmentDetails = orderShipmentDetail;
	}
}
