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

import java.util.Date;

import org.slf4j.Logger;

import tasly.greathealth.oms.domain.order.EventType;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.OrderState;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import com.taobao.api.domain.Trade;
import com.taobao.api.internal.tmc.Message;


/**
 *
 */
public class TmallLogisticsAddressChangedCommand extends AbstractTmallOrderCommand
{

	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	public TmallLogisticsAddressChangedCommand(final OmsOrderRetrieveService<Trade> omsOrderRetrieveService,
			final Message message, final InnerSource innerSource)
	{
		super(omsOrderRetrieveService, message, innerSource);
	}

	@Override
	public void execute()
	{
		final String tid = this.getTmallTidFromMsg();
		final String oid = getTmallOidFromMsg();
		LOG.info("处理订单地址修改事件。TID[" + tid + "]");
		final Date eventTime = getTmallEventtimeFromMsg();
		try
		{
			this.omsOrderRetrieveService.updateLogisiticsAddress(tid);
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage(), e);
			omsOrderRetrieveService.saveFailedOrder(tid, oid, null, EventType.LOGISTICSCHANGED, e.getMessage(), OrderState.FAIL,
					null, eventTime, this.getChannelSource(), this.getInnerSource());
			throw new RuntimeException("Error happened when logisitics address. TID[" + oid + "] ERROR[" + e.getMessage() + "]");
		}
	}

	@Override
	public tasly.greathealth.thirdparty.order.EventType getEventType()
	{
		return tasly.greathealth.thirdparty.order.EventType.LOGISTICSCHANGED;
	}
}
