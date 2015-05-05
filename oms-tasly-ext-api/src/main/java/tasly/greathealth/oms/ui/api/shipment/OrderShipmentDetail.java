package tasly.greathealth.oms.ui.api.shipment;

import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.inventory.Bin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLineQuantity;


public class OrderShipmentDetail implements Serializable
{
	private static final long serialVersionUID = 4137482838536056803L;
	private TaslyOrderLine orderLine;
	private TaslyOrderLineQuantity orderLineQuantity;
	private Map<AtsLocalQuantities, String> locationATS;
	private List<Bin> bins;

	public Map<AtsLocalQuantities, String> getLocationATS()
	{
		return this.locationATS;
	}

	public void setLocationATS(final Map<AtsLocalQuantities, String> locationATS)
	{
		this.locationATS = locationATS;
	}

	public List<Bin> getBins()
	{
		if (this.bins == null)
		{
			this.bins = new ArrayList<Bin>();
		}

		return this.bins;
	}

	public void setBins(final List<Bin> bins)
	{
		this.bins = bins;
	}

	/**
	 * @return the orderLine
	 */
	public TaslyOrderLine getOrderLine()
	{
		return orderLine;
	}

	/**
	 * @param orderLine the orderLine to set
	 */
	public void setOrderLine(final TaslyOrderLine orderLine)
	{
		this.orderLine = orderLine;
	}

	/**
	 * @return the orderLineQuantity
	 */
	public TaslyOrderLineQuantity getOrderLineQuantity()
	{
		return orderLineQuantity;
	}

	/**
	 * @param orderLineQuantity the orderLineQuantity to set
	 */
	public void setOrderLineQuantity(final TaslyOrderLineQuantity orderLineQuantity)
	{
		this.orderLineQuantity = orderLineQuantity;
	}
}
