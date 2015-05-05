/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.api.inventory.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "StockRoomLocations")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaslyStockroomLocationList implements Serializable
{
	private static final long serialVersionUID = -1111567552402499472L;

	@XmlElement(name = "location")
	private List<StockroomLocation> locations = new ArrayList<StockroomLocation>();

	public void addLocation(final StockroomLocation location)
	{
		if (this.locations == null)
		{
			this.locations = new ArrayList<StockroomLocation>();
		}
		this.locations.add(location);
	}

	public List<StockroomLocation> getLocations()
	{
		return this.locations;
	}

	public int getNumberOfLocations()
	{
		return this.locations.size();
	}

	public void initializeLocations(final List<StockroomLocation> newLocations)
	{
		assert(this.locations.isEmpty());
		for (final StockroomLocation location : newLocations)
		{
			addLocation(location);
		}
	}

	public void removeLocation(final StockroomLocation location)
	{
		this.locations.remove(location);
	}

	public void setLocationForJaxb(final List<StockroomLocation> location)
	{
		this.locations = location;
	}
}
