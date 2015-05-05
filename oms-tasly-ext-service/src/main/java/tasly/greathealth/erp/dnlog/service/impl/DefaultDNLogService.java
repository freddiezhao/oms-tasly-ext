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
package tasly.greathealth.erp.dnlog.service.impl;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.service.AbstractHybrisService;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import tasly.greathealth.erp.dnlog.service.DNLogService;
import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.oms.domain.erp.DNLogData;
import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.log.OmsLoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * DefaultDNLogService implementing the DNLogService
 * Created by Yu Weirong 01/10/1015
 */
public class DefaultDNLogService extends AbstractHybrisService implements DNLogService
{
	private static final Logger Log = OmsLoggerFactory.getTmallorderlog();

	private final String dnlog_unCreate = TaslyERPConstants.UNCREATE;

	// @Autowired
	// PersistenceManager perManager = super.getPersistenceManager();

	@Override
	public DNLogData createDNLog(final String OMSOrderID, final String OMSLineID) throws Exception
	{
		// Create the new DNLog object via the PersistenceManager
		final DNLogData newDNLog = super.getPersistenceManager().create(DNLogData.class);

		// Fill in the attributes to the new DNLog object, the PersistenceManager is supposedly to update the database
		// automatically
		newDNLog.setOms_order_id(OMSOrderID);
		newDNLog.setOms_order_line_id(OMSLineID);
		newDNLog.setReplication_flag(dnlog_unCreate);
		return newDNLog;
	}


	@Override
	public DNLogData getSingleDNLogByOrderIDAndOrderLineID(final String OMSOrderID, final String OMSLineID)
			throws EntityNotFoundException
	{

		try
		{
			return super.getPersistenceManager().createCriteriaQuery(DNLogData.class)
					.where(Restrictions.eq(DNLogData.OMS_ORDER_ID, OMSOrderID))
					.and(Restrictions.eq(DNLogData.OMS_ORDER_LINE_ID, OMSLineID)).uniqueResult();

		}
		catch (final ManagedObjectNotFoundException e)
		{
			Log.error("Could not retrieve result for " + this);
			return null;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public List<DNLogData> getDNLogByFlag(final String RPFlag) throws EntityNotFoundException
	{
		try
		{
			return super.getPersistenceManager().createCriteriaQuery(DNLogData.class)
					.where(Restrictions.eq(DNLogData.REPLICATION_FLAG, RPFlag)).resultList();

		}
		catch (final ManagedObjectNotFoundException e)
		{
			Log.error("Could not retrieve result for " + this);
			return new ArrayList<DNLogData>();
		}
	}

	@Override
	public DNLogData updateSingleDNLog(final DNLogData DNLog, final String RPFlag) throws EntityNotFoundException
	{
		DNLog.setReplication_flag(RPFlag);
		return DNLog;
	}

	@Override
	public List<DNLogData> updateBatchDNLogs(final List<DNLogData> DNLogs, final String RPFlag) throws EntityNotFoundException
	{

		if (DNLogs.size() == 0 || DNLogs == null)
		{
			return DNLogs;
		}

		for (int i = 0; i < DNLogs.size(); i++)
		{
			DNLogs.get(i).setReplication_flag(RPFlag);
		}
		return DNLogs;
	}

	@SuppressWarnings({"unused", "null"})
	@Override
	public void removeReplicatedDNLogs(final List<DNLogData> DNLogs)
	{

		if (null == DNLogs || DNLogs.size() == 0)
		{
			return;
		}

		for (int i = 0; i < DNLogs.size(); i++)
		{
			super.getPersistenceManager().remove(DNLogs.get(i));
		}
		super.getPersistenceManager().flush();
	}

	@Override
	public DNLogData getSingleDNLogByOrderLineID(final String OMSLineID) throws Exception
	{
		try
		{
			return super.getPersistenceManager().createCriteriaQuery(DNLogData.class)
					.where(Restrictions.eq(DNLogData.OMS_ORDER_LINE_ID, OMSLineID)).uniqueResult();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			Log.error("can not retrieve one item...");
			return null;
		}
	}


	@Override
	public List<DNLogData> getDNLogDataList(final String omsOrderId) throws Exception
	{
		try
		{
			return super.getPersistenceManager().createCriteriaQuery(DNLogData.class)
					.where(Restrictions.eq(DNLogData.OMS_ORDER_ID, omsOrderId)).resultList();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	public List<DNLogData> getDNLogByFlag(String RPFlag, String channelSource) throws EntityNotFoundException {
		try {
			return getDNLogCriteriaQuery(RPFlag, channelSource).resultList();
		} catch (final ManagedObjectNotFoundException e) {
			e.printStackTrace();
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}

	@Override
	public List<DNLogData> getDNLogByFlag(String RPFlag, String innerSource, String channelSource) throws EntityNotFoundException {
		try {
			return getDNLogCriteriaQuery(RPFlag, channelSource)
					.and(RawRestrictions.eq("order." + TaslyOrderData.INNER_SOURCE.name(), innerSource)).resultList();
		} catch (ManagedObjectNotFoundException e) {
			e.printStackTrace();
			throw new EntityNotFoundException(e.getMessage(), e);
		}
	}

	private CriteriaQuery<DNLogData> getDNLogCriteriaQuery(String RPFlag, String channelSource) {
		return getPersistenceManager().createCriteriaQuery(DNLogData.class).join(TaslyOrderData.class, "order")
				.where(RawRestrictions.attrEq(DNLogData.OMS_ORDER_ID.name(), "order." + TaslyOrderData.ORDERID.name()))
				.and(RawRestrictions.eq(DNLogData.REPLICATION_FLAG.name(), RPFlag))
				.and(RawRestrictions.eq("order." + TaslyOrderData.CHANNEL_SOURCE.name(), channelSource));
	}
}
