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
package tasly.greathealth.erp.util;

/**
 * @author vincent.yin
 *         used for constants definition in facade layer for erp model
 */
public interface TaslyERPConstants
{
	/**
	 * Common Constants. add by libin
	 * abbreviation：Com
	 */
	// ECC同步订单状态
	public static String REPLICATIONSTATUS_E = "E";
	public static String REPLICATIONSTATUS_N = "N";
	public static String REPLICATIONSTATUS_S = "S";
	public static String REPLICATIONSTATUS_W = "W";
	public static String REPLICATIONSTATUS_Y = "Y";

	// SOURCING状态
	public static String ORDER_STATE_S = "SOURCING_SUCCESS";
	public static String ORDER_STATE_F = "SOURCING_PARTIAL";

	// TS-953
	// 非自营、纯外租
	public static String ORDER_MERCHANT_TAG_FZ = "RENTRUN";
	// 自营
	public static String ORDER_MERCHANT_TAG_SELF = "SELFRUN";
	// 混营
	public static String ORDER_MERCHANT_TAG_MIX = "MIXRUN";


	/**
	 * Update Order Constants. add by libin
	 * abbreviation：UO(Update Order)
	 */
	// for soap client order model:base info
	public static String UO_SSYSTEM = "HY_OMS";
	public static String UO_SERVICENAME = "updateOrder";
	public static String UO_TSYSTEM = "GERP";
	public static String UO_RETRY = "1";
	// 锁定
	public static final String UO_LOCKSTATUS = "02";
	// 修改备注、快递信息
	public static final String UO_ALTERTATUS = "03";
	// 解锁
	public static final String UO_DEBLOCKSTATUS = "04";
	// 修改备注
	// public static final String UO_ALTERNOTES = "05";
	// 修改快递
	// public static final String UO_ALTERSHIPTO = "06";

	// 接口返回值
	// 通信成功
	public static final int UO_RESULTSUCCEED = 0;
	// 通信失败
	public static final int UO_RESULTFAILURE = 1;
	// 参数信息不完整或者为空
	public static final int UO_RESULTPARAMETERFAILURE = 2;
	// 订单不可修改
	public static final int UO_RESULTUNCHANGEABLE = 3;
	// 更新订单日志头
	public static String UO_LogHead = "更新订单接口:";
	// 是否核查字段长度超过最大门限
	public static boolean CHECKLENGTH = true;

	/**
	 * Create Order Constants.
	 * abbreviation：CO(Create Order)
	 */

	// for soap client order model:base info
	public static String CO_SSYSTEM = "HY_OMS";
	public static String CO_SERVICENAME = "createOrder";
	public static String CO_TSYSTEM = "GERP";
	public static String CO_RETRY = "3";

	// header
	public static String CO_OPERATIONTYPE = "01";
	public static String CO_ORDERTYPE = "ZOR4";
	// headCondition
	public static String CO_ZDYF = "Z102";
	public static String CO_ZDZKJE = "Z100";
	public static String CO_CURRENCY = "CNY";
	public static String CO_JGZKJE = "Z003";
	// fix PR TS-833
	public static String CO_ZPR = "ZPR3";
	// Mapping relationship
	public static final String CO_SALESORGTYPE = "sales_org";
	public static final String CO_ECCCUSTOMERTYPE = "ecc_customer";
	// OMS order query
	public static String CO_APPROVESTATUS = "APPROVED";
	public static int CO_REPLICATIONTIME = 3;
	// fix PR TS-879
	public static String SHIPCITY = "未知";
	// soap client invoke
	// public static final String webServiceConTimeout = "6000";
	// public static final String webServiceRevTimeout = "6000";

	// 更新订单日志头
	public static String CO_LogHead = "新建订单接口:";

	public static final String CREATE = "create";
	public static final String UNCREATE = "unCreate";
	public static final String MANNUALDELIVERY = "mannualDelivery";
	public static final String NOEXPRESSCODE = "noExpressCode";
	public static final String ERRORDNLOG = "errorDnLog";
}
