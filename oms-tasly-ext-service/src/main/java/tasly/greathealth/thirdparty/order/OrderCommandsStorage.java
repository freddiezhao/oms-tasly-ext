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
package tasly.greathealth.thirdparty.order;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 *
 */
public class OrderCommandsStorage extends Observable
{

	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	private static OrderCommandsStorage instance;

	private static Map<ChannelSource, Map<EventType, Queue<OrderCommand>>> commands = new ConcurrentHashMap<ChannelSource, Map<EventType, Queue<OrderCommand>>>();

	private static ExecutorService orderCreateExecutor = Executors.newSingleThreadExecutor();
	private static ExecutorService sellerShipExecutor = Executors.newSingleThreadExecutor();
	private static ExecutorService refundCreateExecutor = Executors.newSingleThreadExecutor();
	private static ExecutorService refundSuccessExecutor = Executors.newSingleThreadExecutor();
	private static ExecutorService refundCloseExecutor = Executors.newSingleThreadExecutor();
	private static ExecutorService memoChangeExecutor = Executors.newSingleThreadExecutor();
	private static ExecutorService logisticsAddressChangedExecutor = Executors.newSingleThreadExecutor();

	private final Map<String, Boolean> statusMap = new HashMap<String, Boolean>();

	private OrderCommandsStorage()
	{
		statusMap.put(String.valueOf(EventType.ORDERCREATE), Boolean.FALSE);
		statusMap.put(String.valueOf(EventType.REFUNDCREATE), Boolean.FALSE);
		statusMap.put(String.valueOf(EventType.REFUNDSUCCESS), Boolean.FALSE);
		statusMap.put(String.valueOf(EventType.REFUNDCLOSE), Boolean.FALSE);
		statusMap.put(String.valueOf(EventType.SELLERSHIP), Boolean.FALSE);
		statusMap.put(String.valueOf(EventType.UPDATEORDERMEMO), Boolean.FALSE);
		statusMap.put(String.valueOf(EventType.LOGISTICSCHANGED), Boolean.FALSE);

		this.addObserver(new CommandExecutorObserver());
		this.addObserver(new CommandsStatisticObserver());

	}

	public synchronized static OrderCommandsStorage getInstance()
	{
		if (instance == null)
		{
			instance = new OrderCommandsStorage();
		}
		return instance;
	}

	public synchronized Map<String, Boolean> getAllConsumerStatus()
	{
		return statusMap;
	}

	public Boolean getSingleConsumerStatus(final String eventKey)
	{
		return statusMap.get(eventKey);
	}

	public synchronized void startAllConsumer(final String sourceType)
	{
		for (final String key : statusMap.keySet())
		{
			final Boolean status = statusMap.get(key);
			if (status == false)
			{
				statusMap.put(key, Boolean.TRUE);
			}
			this.setChanged();
			final StoragePara para = new StoragePara();
			para.eventType = EventType.valueOf(key);
			para.channelSource = ChannelSource.valueOf(sourceType);
			this.notifyObservers(para);
		}
	}

	public synchronized void startSingleConsumer(final String sourceType, final String eventKey)
	{
		final Boolean status = statusMap.get(eventKey);
		if (status != null && status == false)
		{
			statusMap.put(eventKey, Boolean.TRUE);
		}
		this.setChanged();
		final StoragePara para = new StoragePara();
		para.eventType = EventType.valueOf(eventKey);
		para.channelSource = ChannelSource.valueOf(sourceType);
		this.notifyObservers(para);
	}

	public synchronized void stopAllConsumer()
	{
		for (final String key : statusMap.keySet())
		{
			final Boolean status = statusMap.get(key);
			if (status == true)
			{
				statusMap.put(key, Boolean.FALSE);
			}
		}
	}

	public synchronized void stopSingleConsumer(final String eventKey)
	{
		final Boolean status = statusMap.get(eventKey);
		if (status != null && status == true)
		{
			statusMap.put(eventKey, Boolean.FALSE);
		}
	}

	public void addOrderCommand(final ChannelSource channelSource, final EventType eventType, final OrderCommand orderCommand)
	{
		Map<EventType, Queue<OrderCommand>> sourceCmds = commands.get(channelSource);
		if (sourceCmds == null)
		{
			sourceCmds = new ConcurrentHashMap<EventType, Queue<OrderCommand>>();
			commands.put(channelSource, sourceCmds);
		}

		Queue<OrderCommand> eventCmds = sourceCmds.get(eventType);
		if (eventCmds == null)
		{
			eventCmds = new LinkedList<OrderCommand>();
			sourceCmds.put(eventType, eventCmds);
		}

		final Queue<OrderCommand> cmds = sourceCmds.get(eventType);
		if (cmds != null)
		{
			synchronized (cmds)
			{
				cmds.add(orderCommand);
				final StoragePara para = new StoragePara();
				para.eventType = eventType;
				para.channelSource = channelSource;
				this.setChanged();
				this.notifyObservers(para);
			}
		}
	}

	public synchronized Collection<OrderCommand> getOrderCommandsByType(final ChannelSource channelSource,
			final EventType eventType)
	{
		final Map<EventType, Queue<OrderCommand>> sourceCmds = commands.get(channelSource);
		if (sourceCmds == null)
		{
			return Collections.emptyList();
		}

		final Queue<OrderCommand> cmds = sourceCmds.get(eventType);
		if (cmds != null)
		{
			return cmds;
		}
		return Collections.emptyList();
	}

	public synchronized OrderCommand getOrderCommand(final ChannelSource channelSource, final EventType eventType)
	{
		OrderCommand result = null;
		final Map<EventType, Queue<OrderCommand>> sourceCmds = commands.get(channelSource);
		if (sourceCmds == null)
		{
			return null;
		}

		final Queue<OrderCommand> cmds = sourceCmds.get(eventType);
		if (cmds != null)
		{
			result = cmds.poll();
			final StoragePara para = new StoragePara();
			para.eventType = eventType;
			para.channelSource = channelSource;
		}
		return result;
	}

	public synchronized Map<ChannelSource, Map<EventType, Queue<OrderCommand>>> getAllCommands()
	{
		return commands;
	}


	private static class StoragePara
	{
		private ChannelSource channelSource;
		private EventType eventType;
	}

	private static class CommandsStatisticObserver implements Observer
	{

		@Override
		public void update(final Observable o, final Object arg)
		{
			final Map<ChannelSource, Map<EventType, Queue<OrderCommand>>> orderCommands = OrderCommandsStorage.getInstance()
					.getAllCommands();
			for (final Map.Entry<ChannelSource, Map<EventType, Queue<OrderCommand>>> outerEntry : orderCommands.entrySet())
			{
				final Map<EventType, Queue<OrderCommand>> singleCommands = outerEntry.getValue();
				if (MapUtils.isNotEmpty(singleCommands))
				{
					for (final Map.Entry<EventType, Queue<OrderCommand>> innerCommands : singleCommands.entrySet())
					{

						LOG.info("event type[" + innerCommands.getKey() + "] current size["
								+ (CollectionUtils.isEmpty(innerCommands.getValue()) ? 0 : innerCommands.getValue().size()) + "]");
					}
				}
			}
		}
	}

	private static class CommandExecutorObserver implements Observer
	{

		@Override
		public void update(final Observable o, final Object arg)
		{
			if (arg instanceof StoragePara)
			{
				final StoragePara para = (StoragePara) arg;
				final EventType eventType = para.eventType;
				final ChannelSource sourceType = para.channelSource;

				if (OrderCommandsStorage.getInstance().getSingleConsumerStatus(String.valueOf(eventType)))
				{
					OrderCommand orderCommand = OrderCommandsStorage.getInstance().getOrderCommand(sourceType, eventType);
					while (orderCommand != null)
					{
						switch (eventType)
						{
							case ORDERCREATE:
								orderCreateExecutor.execute(orderCommand);
								break;
							case REFUNDCREATE:
								refundCreateExecutor.execute(orderCommand);
								break;
							case REFUNDSUCCESS:
								refundSuccessExecutor.execute(orderCommand);
								break;
							case REFUNDCLOSE:
								refundCloseExecutor.execute(orderCommand);
								break;
							case SELLERSHIP:
								sellerShipExecutor.execute(orderCommand);
								break;
							case UPDATEORDERMEMO:
								memoChangeExecutor.execute(orderCommand);
								break;
							case LOGISTICSCHANGED:
								logisticsAddressChangedExecutor.execute(orderCommand);
								break;
							default:
								LOG.info("Unknown command " + eventType);
								break;
						}
						orderCommand = OrderCommandsStorage.getInstance().getOrderCommand(sourceType, eventType);
					}
				}
			}
		}
	}

}
