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
package tasly.greathealth.erp.dnlog.service;

import java.util.List;

import tasly.greathealth.oms.domain.erp.DNLogData;


/**
 * Interface for DNLog
 * Created by Yu Weirong 1/10/2015
 */
public interface DNLogService
{

	public DNLogData createDNLog(String OMSOrderID, String OMSLineID) throws Exception;

	/*
	 * Search single DNLog by combination of OMS Order ID and OMS Order Line ID
	 * Params: OMSOrderID, OMSLineID
	 * Return: DNLogData
	 */
	public DNLogData getSingleDNLogByOrderIDAndOrderLineID(String OMSOrderID, String OMSLineID) throws Exception;



	public DNLogData getSingleDNLogByOrderLineID(String OMSLineID) throws Exception;


	public List<DNLogData> getDNLogDataList(String omsOrderId) throws Exception;

	/*
	 * Search the DNLog by replication status
	 * Params: RPFlag
	 * Return: <DNLogData>
	 */
	public List<DNLogData> getDNLogByFlag(String RPFlag) throws Exception;

    /**
     * Search the DNLog by replication status and channel source
     * @param RPFlag
     * @param channelSource
     * @return
     * @throws Exception
     */
    public List<DNLogData> getDNLogByFlag(String RPFlag, String channelSource) throws Exception;

    /**
     * Search the DNLog by replication status, inner source and channel source
     * @param RPFlag
     * @param innerSource
     * @param channelSource
     * @return
     * @throws Exception
     */
    public List<DNLogData> getDNLogByFlag(String RPFlag, String innerSource, String channelSource) throws Exception;

	/*
	 * Update the replication flag of DNLog
	 * Params: DNLog
	 * Return: Boolean
	 */
	public DNLogData updateSingleDNLog(DNLogData DNLog, String RPFlag) throws Exception;

	/*
	 * Update the replication flags of DNLogs
	 * Params: List<DNLogData>
	 * Return: Boolean
	 */
	public List<DNLogData> updateBatchDNLogs(List<DNLogData> DNLogs, String RPFlag) throws Exception;

	/*
	 * remove the replicated records in DNLogs
	 * Params: List<DNLogData>
	 * Return:
	 */
	public void removeReplicatedDNLogs(List<DNLogData> DNLogs) throws Exception;




}
