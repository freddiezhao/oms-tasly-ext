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
package tasly.greathealth.tmall.order.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;
import tasly.greathealth.tmall.order.services.impl.TmallUtil;

import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


/**
 *
 */
abstract class AbstractTmallOrderCommand implements OrderCommand, OrderConstants
{
	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	protected OmsOrderRetrieveService<Trade> omsOrderRetrieveService;

	protected Message message;

	protected InnerSource innerSource;

	/**
	 *
	 */
	public AbstractTmallOrderCommand(final OmsOrderRetrieveService<Trade> omsOrderRetrieveService, final Message message,
			final InnerSource innerSource)
	{
		super();
		this.omsOrderRetrieveService = omsOrderRetrieveService;
		this.message = message;
		this.innerSource = innerSource;
	}

	protected String getTmallTidFromMsg()
	{
		return safe2String(parseMessageJson2Map().get(DEFAULT_TMALL_TID_KEY));
	}

	protected String getTmallOidFromMsg()
	{
		return safe2String(parseMessageJson2Map().get(DEFAULT_TMALL_OID_KEY));
	}

	protected String getTmallRefundFeeFromMsg()
	{
		return safe2String(parseMessageJson2Map().get(DEFAULT_TMALL_REFUND_FEE_KEY));
	}

	protected Date getTmallEventtimeFromMsg()
	{
		final SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
		Date result = null;
		try
		{
			result = formatter.parse(safe2String(message.getRaw().get("time")));
		}
		catch (final ParseException e)
		{
			LOG.error(e.getMessage(), e);
		}
		return result;
	}

	protected String getLineLevelState(final Trade trade, final String oid)
	{
		if (StringUtils.isEmpty(oid))
		{
			return null;
		}
		final List<Order> orders = trade.getOrders();
		for (int i = orders.size() - 1; i >= 0; i--)
		{
			if (oid.equalsIgnoreCase(String.valueOf(orders.get(i).getOid())))
			{
				return orders.get(i).getStatus();
			}
		}
		return null;
	}

	private Map<String, Object> parseMessageJson2Map()
	{
		if (message == null)
		{
			return Collections.emptyMap();
		}
		final String contentJson = safe2String(message.getRaw().get(DEFAULT_TMALL_RAW_MSG_KEY));
		return new JSONDeserializer<Map<String, Object>>().deserialize(contentJson);
	}

	private String safe2String(final Object object)
	{
		if (object == null)
		{
			return "";
		}
		return object.toString();
	}

	@Override
	public void run()
	{
		LOG.info("execute command" + this);
		this.execute();
		try
		{
			Thread.sleep(100);
		}
		catch (final InterruptedException e)
		{
			LOG.warn(e.getMessage(), e);
		}
	}

	/**
	 * @param omsOrderRetrieveService the omsOrderRetrieveService to set
	 */
	public void setOmsOrderRetrieveService(final OmsOrderRetrieveService<Trade> omsOrderRetrieveService)
	{
		this.omsOrderRetrieveService = omsOrderRetrieveService;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(final Message message)
	{
		this.message = message;
	}

	/**
	 * @return the message
	 */
	// @Override
	public Message getMessage()
	{
		return message;
	}

	@Override
	public String getTid()
	{
		return (String) TmallUtil.parseMessageJson2Map(message).get("tid");
	}

	@Override
	public String getOid()
	{
		return (String) TmallUtil.parseMessageJson2Map(message).get("oid");
	}

	@Override
	public String getContent()
	{
		if (message != null)
		{
			return new JSONSerializer().deepSerialize(message.getRaw());
		}
		return null;
	}

	@Override
	public ChannelSource getChannelSource()
	{
		return ChannelSource.TMALL;
	}

	@Override
	public InnerSource getInnerSource()
	{
		return innerSource;
	}

	public void setInnerSource(final InnerSource innerSource)
	{
		this.innerSource = innerSource;
	}

}
