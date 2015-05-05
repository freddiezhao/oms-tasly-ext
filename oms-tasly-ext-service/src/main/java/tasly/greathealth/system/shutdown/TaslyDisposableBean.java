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
package tasly.greathealth.system.shutdown;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.OrderState;
import tasly.greathealth.thirdparty.order.EventType;
import tasly.greathealth.thirdparty.order.OrderCommand;
import tasly.greathealth.thirdparty.order.OrderCommandsStorage;
import tasly.greathealth.tmall.order.domain.OmsSaveOrderParameter;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.taobao.api.domain.Trade;


/**
 *
 */
public class TaslyDisposableBean implements DisposableBean
{

	private static final Logger LOG = LoggerFactory.getLogger(TaslyDisposableBean.class);
	private OmsOrderRetrieveService<Trade> omsOrderRetrieverService;

	@Override
	public void destroy() throws Exception
	{
		LOG.info("tomcat shut down begin handing...");
		final List<OmsSaveOrderParameter> omsSaveOrderParameterList = new ArrayList<OmsSaveOrderParameter>();
		final Map<ChannelSource, Map<EventType, Queue<OrderCommand>>> command = OrderCommandsStorage.getInstance().getAllCommands();
		final Map<EventType, Queue<OrderCommand>> orderCommand = command.get(ChannelSource.TMALL);
		for (final EventType type : orderCommand.keySet())
		{
			if (orderCommand.containsKey(type))
			{
				final Queue<OrderCommand> cmds = orderCommand.get(type);
				final Iterator<OrderCommand> it = cmds.iterator();
				while (it.hasNext())
				{
					final OrderCommand oc = it.next();
					omsSaveOrderParameterList.add(this.packgingOmsSaveOrderParameter(type, oc));
				}
			}
		}
		try
		{
			omsOrderRetrieverService.batchSaveFailOrPendingOrder(omsSaveOrderParameterList);
			LOG.info("save into the database success");
		}
		catch (final Exception e)
		{
			LOG.info("save into the database failed");
			e.printStackTrace();
		}
	}

	public OmsSaveOrderParameter packgingOmsSaveOrderParameter(final EventType type, final OrderCommand source)
	{
		final OmsSaveOrderParameter target = new OmsSaveOrderParameter();
		// Event Type
		target.setEventType(tasly.greathealth.oms.domain.order.EventType.valueOf(type.toString()));
		// final Message message = source.getMessage();
		// tmall Trade Id
		target.setTid(source.getTid());
		target.setOid(source.getOid());
		// raw message
		target.setRawMsg(source.getContent());
		// status
		target.setState(OrderState.PENDING);
		target.setChannelSource(source.getChannelSource());
		target.setInnerSource(source.getInnerSource());
		return target;
	}

	/**
	 * @return the omsOrderRetrieverService
	 */
	public OmsOrderRetrieveService<Trade> getOmsOrderRetrieverService()
	{
		return omsOrderRetrieverService;
	}

	/**
	 * @param omsOrderRetrieverService the omsOrderRetrieverService to set
	 */
	public void setOmsOrderRetrieverService(final OmsOrderRetrieveService<Trade> omsOrderRetrieverService)
	{
		this.omsOrderRetrieverService = omsOrderRetrieverService;
	}
}
