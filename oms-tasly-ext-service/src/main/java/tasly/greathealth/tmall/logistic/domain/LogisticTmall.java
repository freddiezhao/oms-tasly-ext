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
package tasly.greathealth.tmall.logistic.domain;

import java.util.List;

import tasly.greathealth.oms.domain.erp.DNLogData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;


/**
 *
 */
public class LogisticTmall
{
	private String tradeId;
	private String sub_tids;
	private String out_sid;
	private String company_code;
	private Long isSplit;
	private String inner_source;
	private List<String> subTidList;
	private String orderLineId;
	private TaslyOrderData taslyOrderData;
	private DNLogData dnlog;
	private List<DNLogData> dnlogs;

	/**
	 * @return the tradeId
	 */
	public String getTradeId()
	{
		return tradeId;
	}

	/**
	 * @param tradeId the tradeId to set
	 */
	public void setTradeId(final String tradeId)
	{
		this.tradeId = tradeId;
	}

	/**
	 * @return the sub_tids
	 */
	public String getSub_tids()
	{
		return sub_tids;
	}

	/**
	 * @param sub_tids the sub_tids to set
	 */
	public void setSub_tids(final String sub_tids)
	{
		this.sub_tids = sub_tids;
	}

	/**
	 * @return the subTidList
	 */
	public List<String> getSubTidList()
	{
		return subTidList;
	}

	/**
	 * @param subTidList the subTidList to set
	 */
	public void setSubTidList(final List<String> subTidList)
	{
		this.subTidList = subTidList;
	}

	/**
	 * @return the out_sid
	 */
	public String getOut_sid()
	{
		return out_sid;
	}

	/**
	 * @param out_sid the out_sid to set
	 */
	public void setOut_sid(final String out_sid)
	{
		this.out_sid = out_sid;
	}

	/**
	 * @return the company_code
	 */
	public String getCompany_code()
	{
		return company_code;
	}

	/**
	 * @param company_code the company_code to set
	 */
	public void setCompany_code(final String company_code)
	{
		this.company_code = company_code;
	}

	/**
	 * @return the inner_source
	 */
	public String getInner_source()
	{
		return inner_source;
	}

	/**
	 * @param inner_source the inner_source to set
	 */
	public void setInner_source(final String inner_source)
	{
		this.inner_source = inner_source;
	}

	/**
	 * @return the orderLineId
	 */
	public String getOrderLineId()
	{
		return orderLineId;
	}

	/**
	 * @param orderLineId the orderLineId to set
	 */
	public void setOrderLineId(final String orderLineId)
	{
		this.orderLineId = orderLineId;
	}

	/**
	 * @return the taslyOrderData
	 */
	public TaslyOrderData getTaslyOrderData()
	{
		return taslyOrderData;
	}

	/**
	 * @param taslyOrderData the taslyOrderData to set
	 */
	public void setTaslyOrderData(final TaslyOrderData taslyOrderData)
	{
		this.taslyOrderData = taslyOrderData;
	}

	/**
	 * @return the isSplit
	 */
	public Long getIsSplit()
	{
		return isSplit;
	}

	/**
	 * @param isSplit the isSplit to set
	 */
	public void setIsSplit(final Long isSplit)
	{
		this.isSplit = isSplit;
	}


	/**
	 * @return the dnlogs
	 */
	public List<DNLogData> getDnlogs()
	{
		return dnlogs;
	}

	/**
	 * @param dnlogs the dnlogs to set
	 */
	public void setDnlogs(List<DNLogData> dnlogs)
	{
		this.dnlogs = dnlogs;
	}

	/**
	 * @param dnlog the dnlog to set
	 */
	public void setDnlog(DNLogData dnlog)
	{
		this.dnlog = dnlog;
	}

	/**
	 * @return the dnlog
	 */
	public DNLogData getDnlog()
	{
		return dnlog;
	}

}
