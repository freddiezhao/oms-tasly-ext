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
package tasly.greathealth.tmall.order.domain;

import java.util.Date;

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.EventType;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.OrderState;


/**
 *
 */
public class OmsSaveOrderParameter
{

	private String tid;
	private String oid;
	private String refundFee;
	private EventType eventType;
	private OrderState state;
	private String rawMsg;
	private String errorMessage;
	private Date eventtime;
	private ChannelSource channelSource;
	private InnerSource innerSource;

	/**
	 * @return the tid
	 */
	public String getTid()
	{
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(final String tid)
	{
		this.tid = tid;
	}

	/**
	 * @return the oid
	 */
	public String getOid()
	{
		return oid;
	}

	/**
	 * @param oid the oid to set
	 */
	public void setOid(final String oid)
	{
		this.oid = oid;
	}

	/**
	 * @return the refundFee
	 */
	public String getRefundFee()
	{
		return refundFee;
	}

	/**
	 * @param refundFee the refundFee to set
	 */
	public void setRefundFee(final String refundFee)
	{
		this.refundFee = refundFee;
	}

	/**
	 * @return the eventType
	 */
	public EventType getEventType()
	{
		return eventType;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(final EventType eventType)
	{
		this.eventType = eventType;
	}

	/**
	 * @return the state
	 */
	public OrderState getState()
	{
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(final OrderState state)
	{
		this.state = state;
	}

	/**
	 * @return the rawMsg
	 */
	public String getRawMsg()
	{
		return rawMsg;
	}

	/**
	 * @param rawMsg the rawMsg to set
	 */
	public void setRawMsg(final String rawMsg)
	{
		this.rawMsg = rawMsg;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(final String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the eventtime
	 */
	public Date getEventtime()
	{
		return eventtime;
	}

	/**
	 * @param eventtime the eventtime to set
	 */
	public void setEventtime(final Date eventtime)
	{
		this.eventtime = eventtime;
	}

	public ChannelSource getChannelSource()
	{
		return channelSource;
	}

	public void setChannelSource(final ChannelSource channelSource)
	{
		this.channelSource = channelSource;
	}

	public InnerSource getInnerSource()
	{
		return innerSource;
	}

	public void setInnerSource(final InnerSource innerSource)
	{
		this.innerSource = innerSource;
	}

	@Override
	public String toString()
	{
		return "OmsSaveOrderParameter [tid=" + tid + ", oid=" + oid + ", refundFee=" + refundFee + ", eventType=" + eventType
				+ ", state=" + state + ", rawMsg=" + rawMsg + ", errorMessage=" + errorMessage + ", eventtime=" + eventtime
				+ ", channelSource=" + channelSource + ", innerSource=" + innerSource + "]";
	}
}
