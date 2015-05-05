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
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.domain.order.OrderQuerySupport;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * @author Henter Liu
 */
public class TaslyOrderQueryObject extends OrderQueryObject
{
	public TaslyOrderQueryObject()
	{
		setSorting(OrderQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public String getOrderId()
	{
		return super.getValue("orderId");
	}

	public void setOrderId(final String orderId)
	{
		this.superSetValue("orderId", orderId);
	}

	public String getBuyerMessage()
	{
		return super.getValue("buyerMessage");
	}

	public void setBuyerMessage(final String buyerMessage)
	{
		this.superSetValue("buyerMessage", buyerMessage);
	}

	public String getSellerMessage()
	{
		return super.getValue("sellerMessage");
	}

	public void setSellerMessage(final String sellerMessage)
	{
		this.superSetValue("sellerMessage", sellerMessage);
	}

	public String getPacking()
	{
		return super.getValue("packing");
	}

	public void setPacking(final String packing)
	{
		this.superSetValue("packing", packing);
	}

	public String getOriginalOrderId()
	{
		return super.getValue("originalOrderId");
	}

	public void setOriginalOrderId(final String originalOrderId)
	{
		this.superSetValue("originalOrderId", originalOrderId);
	}

	public String getApproveStatus()
	{
		return super.getValue("approveStatus");
	}

	public void setApproveStatus(final String approveStatus)
	{
		superSetValue("approveStatus", approveStatus);
	}

	public List<String> getApproveStatusIds()
	{
		return super.getValues("approveStatusId");
	}

	public void setApproveStatusIds(final List<String> approveStatusIds)
	{
		superSetValues("approveStatusId", approveStatusIds);
	}

	public String getSkuId()
	{
		return super.getValue("skuId");
	}

	public void setSkuId(final String skuId)
	{
		this.superSetValue("skuId", skuId);
	}

	public List<String> getRemarkStatusIds()
	{
		return super.getValues("remarkStatusId");
	}

	public void setRemarkStatusIds(final List<String> remarkStatusId)
	{
		superSetValues("remarkStatusId", remarkStatusId);
	}

	public List<String> getLockStatusIds()
	{
		return super.getValues("lockStatusId");
	}

	public void setLockStatusIds(final List<String> lockStatusId)
	{
		superSetValues("lockStatusId", lockStatusId);
	}

	public List<String> getInnerSourceStatusIds()
	{
		return super.getValues("innerSourceStatusId");
	}

	public void setInnerSourceStatusIds(final List<String> innerSourceStatusId)
	{
		superSetValues("innerSourceStatusId", innerSourceStatusId);
	}

	public void setChannels(final List<String> channels)
	{
		superSetValues("channel", channels);
	}

	public List<String> getChannels()
	{
		return super.getValues("channel");
	}

	public void setInnerSources(final List<String> innerSources)
	{
		superSetValues("innerSource", innerSources);
	}

	public List<String> getInnerSources()
	{
		return super.getValues("innerSource");
	}

	private void superSetValue(final String attributeName, final String attributeValue)
	{
		if (attributeValue == null)
		{
			return;
		}
		super.setValue(attributeName, attributeValue);
	}

	private void superSetValues(final String attributeName, final List<String> attributeValue)
	{
		if (!(CollectionUtils.isNotEmpty(attributeValue)))
		{
			return;
		}
		super.setValues(attributeName, attributeValue);
	}
}
