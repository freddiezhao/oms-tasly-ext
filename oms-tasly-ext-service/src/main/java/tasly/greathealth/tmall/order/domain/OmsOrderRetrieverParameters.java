/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.tmall.order.domain;

import java.util.Date;


/**
 * A domain class for encapsulate retriever order parameters
 *
 * @author Jason Bao
 */
public class OmsOrderRetrieverParameters
{

	private Date startTime;
	private Date endTime;

	private int pageSize;
	private long total;
	private String orderID;

	/**
	 * @return the startTime
	 */
	public Date getStartTime()
	{
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(final Date startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime()
	{
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(final Date endTime)
	{
		this.endTime = endTime;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize()
	{
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}

	/**
	 * @return the total
	 */
	public long getTotal()
	{
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(final long total)
	{
		this.total = total;
	}

	/**
	 * @return the orderID
	 */
	public String getOrderID()
	{
		return orderID;
	}

	/**
	 * @param orderID the orderID to set
	 */
	public void setOrderID(final String orderID)
	{
		this.orderID = orderID;
	}

	@Override
	public String toString()
	{
		return "OmsOrderRetrieverParameters [startTime=" + startTime + ", endTime=" + endTime + ", pageSize=" + pageSize
				+ ", total=" + total + ", orderID=" + orderID + "]";
	}
}
