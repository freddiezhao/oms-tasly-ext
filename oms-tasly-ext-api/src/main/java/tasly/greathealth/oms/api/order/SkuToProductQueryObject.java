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
package tasly.greathealth.oms.api.order;

import com.hybris.oms.domain.SortDirection;

import tasly.greathealth.oms.api.TaslyQueryObject;
import tasly.greathealth.oms.api.order.dto.SkuToProductQuerySupport;


/**
 * @author Henter Liu
 */
public class SkuToProductQueryObject extends TaslyQueryObject<SkuToProductQuerySupport>
{
	// DEFAULT, ITEMID, RATIO, ITEMNAME, OUTERID, INNERSOURCE, QUANTITY, CHANNEL
	public SkuToProductQueryObject()
	{
		setSorting(SkuToProductQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public void setItemId(final String itemId)
	{
		superSetValue("itemid", itemId);
	}

	public String getItemId()
	{
		return super.getValue("itemid");
	}

	public void setRatio(final String ratio)
	{
		superSetValue("ratio", ratio);
	}

	public String getRatio()
	{
		return super.getValue("ratio");
	}

	public void setItemName(final String itemName)
	{
		superSetValue("itemname", itemName);
	}

	public String getItemName()
	{
		return super.getValue("itemname");
	}

	public void setOuterId(final String outerId)
	{
		superSetValue("outerid", outerId);
	}

	public String getOuterId()
	{
		return super.getValue("outerid");
	}

	public void setQuantity(final String quantity)
	{
		superSetValue("quantity", quantity);
	}

	public String getQuantity()
	{
		return super.getValue("quantity");
	}
}
