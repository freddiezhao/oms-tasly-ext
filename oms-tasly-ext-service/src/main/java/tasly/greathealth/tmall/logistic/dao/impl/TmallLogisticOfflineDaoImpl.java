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
package tasly.greathealth.tmall.logistic.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import tasly.greathealth.erp.dnlog.service.DNLogService;
import tasly.greathealth.erp.dnlog.service.impl.DefaultDNLogService;
import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.tmall.logistic.dao.TmallLogisticOfflineDao;
import tasly.greathealth.tmall.logistic.domain.LogisticTmall;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.LogisticsOfflineSendRequest;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.response.LogisticsOfflineSendResponse;
import com.taobao.api.response.TradeFullinfoGetResponse;



/**
 *
 */
public class TmallLogisticOfflineDaoImpl implements TmallLogisticOfflineDao
{
	// private TaobaoClient client;
	// private String OTC_TMALL_STORE_SESSIONKEY;


	// tmall otc client and session key
	private TaobaoClient taobaoOtcclient;
	private String OTC_TMALL_STORE_SESSIONKEY;

	// tmall jsc client and session key
	private TaobaoClient taobaoJscclient;
	private String JSC_TMALL_STORE_SESSIONKEY;
	private final String dnLog_mannualDelivery = TaslyERPConstants.MANNUALDELIVERY;

	private DNLogService defaultDnLogService;

	private static final Logger Log = OmsLoggerFactory.getTmalllogisticlog();


	@Override
	public boolean createOfflineLogistic(final LogisticTmall logisticTmall)
	{
		boolean isSuccess = false;
		// final TaobaoClient client = TmallUtils.getConnection();
		final LogisticsOfflineSendRequest req = new LogisticsOfflineSendRequest();
		req.setTid(Long.valueOf(logisticTmall.getTradeId()));
		req.setOutSid(logisticTmall.getOut_sid());
		req.setCompanyCode(logisticTmall.getCompany_code());
		req.setIsSplit(logisticTmall.getIsSplit());
		if (logisticTmall.getIsSplit() == 1L)
		{
			req.setSubTid(logisticTmall.getSub_tids());
		}
		LogisticsOfflineSendResponse response = null;
		try
		{
			if (logisticTmall.getInner_source().equals(OrderConstants.TMALL_INNER_JSC))
			{
				if (this.validatePermissionToSend(taobaoJscclient, JSC_TMALL_STORE_SESSIONKEY, logisticTmall))
				{
					response = taobaoJscclient.execute(req, JSC_TMALL_STORE_SESSIONKEY);
				}
			}
			if (logisticTmall.getInner_source().equals(OrderConstants.TMALL_INNER_OTC))
			{
				if (this.validatePermissionToSend(taobaoOtcclient, OTC_TMALL_STORE_SESSIONKEY, logisticTmall))
				{
					response = taobaoOtcclient.execute(req, OTC_TMALL_STORE_SESSIONKEY);
				}
			}
		}
		catch (final ApiException e)
		{
			Log.error("tmall connection exception...");
		}
		if (null != response)
		{
			if (null != response.getShipping())
			{
				isSuccess = response.getShipping().getIsSuccess();
			}
			if (null != response.getErrorCode())
			{
				Log.error("create logistic to tmall :::" + response.getSubMsg() + "｜订单异常");
			}
			Log.info("运单更新到天猫:::" + isSuccess);
		}
		return isSuccess;
	}

	/**
	 * 判断订单的状态，只有交易存在、属于自己、并且是等待卖家发货的才能发
	 *
	 * @param taobaoClient
	 * @param sessionkey
	 * @return
	 */
	public boolean validatePermissionToSend(final TaobaoClient taobaoClient, final String sessionkey,
			final LogisticTmall logisticTmall)
	{
		boolean flag = true;
		final TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
		req.setFields("status,tid,orders.status,orders.oid");
		req.setTid(Long.valueOf(logisticTmall.getTradeId()).longValue());
		TradeFullinfoGetResponse res = new TradeFullinfoGetResponse();
		try
		{
			res = taobaoClient.execute(req, sessionkey);
			if (null != res.getTrade())
			{
				flag = this.validateTradeStatus(res.getTrade(), logisticTmall);
			}
			else
			{
				flag = false;
			}
		}
		catch (final Exception e)
		{
			flag = false;
			Log.error("tmall connection exception...");
			// this.validatePermissionToSend(taobaoClient, sessionkey, logisticTmall);
		}
		return flag;
	}

	public boolean validateTradeStatus(final Trade trade, final LogisticTmall logisticTmall) throws Exception {
		boolean flag = true;
		final String tradeStatus = trade.getStatus();
		// 1.如果trade级别的状态为SELLER_CONSIGNED_PART 或者 WAIT_SELLER_SEND_GOODS 验证通过
		if (tradeStatus.equals(OrderConstants.SELLER_CONSIGNED_PART) || tradeStatus.equals(OrderConstants.WAIT_SELLER_SEND_GOODS))
		{
			// 2.如果trade级别的状态为SELLER_CONSIGNED_PART，为部分发货
			if (tradeStatus.equals(OrderConstants.SELLER_CONSIGNED_PART))
			{
				// 3.循环遍历当前订单的子订单，确认子订单的发货状态
				final Map<Long, String> orderMap = new HashMap<Long, String>();
				for (final Order order : trade.getOrders())
				{
					orderMap.put(order.getOid(), order.getStatus());
				}
				for (final String subOid : logisticTmall.getSubTidList())
				{
					// 4.如果匹配不上子订单的订单号，验证失败
					if (null != orderMap.get(Long.valueOf(subOid)))
					{
						// 5.如果子订单的发货状态为等待发货，验证通过，否则失败
						if (orderMap.get(Long.valueOf(subOid)).equals(OrderConstants.WAIT_SELLER_SEND_GOODS))
						{
							flag = true;
						}
						else
						{
							Log.error("天猫Oid:" + subOid + "已发货");
							flag = false;
							break;
						}
					}
					else
					{
						Log.error("天猫Oid:" + subOid + "匹配失败，请检查");
						flag = false;
						break;
					}
				}
			}
		}
		else
		{
			Log.error("天猫Tid:" + trade.getTid() + " 状态为已发货，请勿重新发货");
			flag = false;
			defaultDnLogService.updateBatchDNLogs(logisticTmall.getDnlogs(), dnLog_mannualDelivery);
		}
		return flag;
	}

	/*********** get set method ***************/



	/**
	 * @return the taobaoOtcclient
	 */
	public TaobaoClient getTaobaoOtcclient()
	{
		return taobaoOtcclient;
	}


	/**
	 * @param taobaoOtcclient the taobaoOtcclient to set
	 */
	public void setTaobaoOtcclient(final TaobaoClient taobaoOtcclient)
	{
		this.taobaoOtcclient = taobaoOtcclient;
	}





	/**
	 * @return the taobaoJscclient
	 */
	public TaobaoClient getTaobaoJscclient()
	{
		return taobaoJscclient;
	}





	/**
	 * @param taobaoJscclient the taobaoJscclient to set
	 */
	public void setTaobaoJscclient(final TaobaoClient taobaoJscclient)
	{
		this.taobaoJscclient = taobaoJscclient;
	}





	/**
	 * @return the jSC_TMALL_STORE_SESSIONKEY
	 */
	public String getJSC_TMALL_STORE_SESSIONKEY()
	{
		return JSC_TMALL_STORE_SESSIONKEY;
	}





	/**
	 * @param jSC_TMALL_STORE_SESSIONKEY the jSC_TMALL_STORE_SESSIONKEY to set
	 */
	public void setJSC_TMALL_STORE_SESSIONKEY(final String jSC_TMALL_STORE_SESSIONKEY)
	{
		JSC_TMALL_STORE_SESSIONKEY = jSC_TMALL_STORE_SESSIONKEY;
	}





	/**
	 * @return the oTC_TMALL_STORE_SESSIONKEY
	 */
	public String getOTC_TMALL_STORE_SESSIONKEY()
	{
		return OTC_TMALL_STORE_SESSIONKEY;
	}


	/**
	 * @param oTC_TMALL_STORE_SESSIONKEY the oTC_TMALL_STORE_SESSIONKEY to set
	 */
	public void setOTC_TMALL_STORE_SESSIONKEY(final String oTC_TMALL_STORE_SESSIONKEY)
	{
		OTC_TMALL_STORE_SESSIONKEY = oTC_TMALL_STORE_SESSIONKEY;
	}

	/**
	 * @return the defaultDnLogService
	 */
	public DNLogService getDefaultDnLogService()
	{
		return defaultDnLogService;
	}

	/**
	 * @param defaultDnLogService the defaultDnLogService to set
	 */
	public void setDefaultDnLogService(final DefaultDNLogService defaultDnLogService)
	{
		this.defaultDnLogService = defaultDnLogService;
	}



}
