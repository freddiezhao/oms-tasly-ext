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

/**
 *
 */
public interface OrderConstants
{
	public static final String DEFAULT_CURRENCY_CODE = "CNY";
	public static final String DEFAULT_TMALL_PAYMENT_INFO_TYPE = "ALIPAY";
	// 京东、支付方式
	public static final String DEFAULT_JD_PAYMENT_INFO_TYPE = "JD";
	public static final String DEFAULT_YHD_PAYMENT_INFO_TYPE = "YHD";

	public static final String DEFAULT_EMS_NAME = "EMS";
	public static final String DEFAULT_EMS_CODE = "1";
	public static final String DEFAULT_EXPRESS_CODE = "0";
	public static final String DEFAULT_PACKING = "PACKING";
	public static final String DEFAULT_SHIPPING_LOCK_STATUS = "0";
	public static final String DEFAULT_SHAD_INFO_UPDATE_STATUS = "0";
	public static final String DEFAULT_SPECIAL_MEMO_UPDATE_STATUS = "0";
	public static final String DEFAULT_APPROVE_STATUS = "0";
	public static final String DEFAULT_REPLICATION_STATUS = "N";
	public static final int DEFAULT_REPLICATION_TIMES = 0;
	public static final String DEFAULT_COUNTRY_ISO3166ALPHA2CODE = "CN";
	public static final String DEFAULT_CIS_AUTH = "http://localhost:8080/hybris-cis-cybersource-payment-web/psp/cisPaymentCybersource/authorizations/123/3418696812220176056470/";

	public static final String DEFAULT_INNER_SOURCE_KEY = "innerSource";
	public static final String DEFAULT_CHANNEL_SOURCE_KEY = "channelSource";
	public static final String DEFAULT_RECIEVE_STATE_KEY = "recieveState";

	public static final String OTC_INNNER_SOURCER_CODE = "1";
	public static final String TMALL_CHANNEL_SOURCER_CODE = "0";

	public static final String DEFAULT_TMALL_RAW_MSG_KEY = "content";
	public static final String DEFAULT_TMALL_TID_KEY = "tid";
	public static final String DEFAULT_TMALL_OID_KEY = "oid";
	public static final String DEFAULT_TMALL_REFUND_FEE_KEY = "refund_fee";
	public static final String DEFAULT_ORDER_ID_KEY = "orderID";
	public static final String DEFAULT_GENERATE_ID_DATE_FORMAT = "YYMMddHHmmss";
	public static final String DEFAULT_UPDATE_DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";

	public static final String DEFAULT_ORDER_ID_PLACEHOLDER = "0";
	public static final int DEFAULT_ORDER_ID_LEN = 8;

	public static final String DEFAULT_REFUND_CREATE_HINT = "退货退款中";
	public static final String DEFAULT_REFUND_SELLER_AGREE_HINT = "退货退款成功";
	public static final String DEFAULT_REFUND_SELLER_REFUSE_HINT = "退货退款关闭";
	public static final String DEFAULT_REFUND_SUCCESS_HINT = "退款退货完成";

	public static final String DEFAULT_BUYER_PAY_HINT = "已付款";
	public static final String DEFAULT_SELLER_SHIPPING_HINT = "已出货/待收货";

	public static final String REFUND_FLAG_AGREE = "YES";
	public static final String REFUND_FLAG_DISAGREE = "NO";
	public static final String REFUND_FLAG_DEFAULT = "NULL";

	public static final String ORDER_UNAPPROVE_STATUS = "DEFAULT";// order approve status
	public static final String ORDER_APPROVE_STATUS = "APPROVED";
	public static final String SHIPPING_LOCK_STATUS = "NON_LOCK";// shipping lock status

	public static final String SELLER_CONSIGNED_PART = "SELLER_CONSIGNED_PART";
	public static final String WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";
	public static final String WAIT_BUYER_CONFIRM_GOODS = "WAIT_BUYER_CONFIRM_GOODS";
	public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
	public static final String TRADE_CLOSED_BY_TAOBAO = "TRADE_CLOSED_BY_TAOBAO";
	public static final String TRADE_CLOSED = "TRADE_CLOSED";
	public static final String TRADE_FINISHED = "TRADE_FINISHED";

	public static final String TMALL_INNER_OTC = "OTC";
	public static final String TMALL_INNER_JSC = "JSC";

	public static final String TMALL_STATE_FAIL = "FAIL";
	public static final String TMALL_STATE_PENDING = "PENDING";

	public static final String ORDER_EVENT_TYPE_ORDERCREATE = "ORDERCREATE";
	public static final String ORDER_EVENT_TYPE_SELLERSHIP = "SELLERSHIP";
	public static final String ORDER_EVENT_TYPE_REFUNDCREATE = "REFUNDCREATE";
	public static final String ORDER_EVENT_TYPE_REFUNDSELLERAGREE = "REFUNDSELLERAGREE";
	public static final String ORDER_EVENT_TYPE_REFUNDSELLERREFUSE = "REFUNDSELLERREFUSE";
	public static final String ORDER_EVENT_TYPE_REFUNDSUCCESS = "REFUNDSUCCESS";
	public static final String ORDER_EVENT_TYPE_REFUNDCLOSE = "REFUNDCLOSE";

	public static final String ORDER_LINE_GIFT_FLAG = "X";

	public static final String SELF_RUN = "SELFRUN";// 纯自营
	public static final String RENT_RUN = "RENTRUN";// 纯外租
	public static final String MIX_RUN = "MIXRUN";// 混合
	public static final String TMALL_ORIG_ORDER_INFO = "ORIGORDERINFO";// 原始TMLA订单信息
	public static final String DEFAULT_OUTERID = "OUTERID";
	public static final String DEFAULT_OUTERSKUID = "OUTERSKUID";
	public static final String TMALL_OUTERID_OUTERSKUID = "TMORDERNODEID";


	// ***************************************JD START********************************************
	/**
	 * 发票信息
	 */
	public static String JD_INVOICE_TYPE = "JD_INVOICE_TYPE";
	public static String JD_INVOICE_TITLE = "JD_INVOICE_TITLE";
	public static String JD_INVOICE_CONTENT = "JD_INVOICE_CONTENT";

	public static String JUDGE_FLAG = "JUDGE_FLAG";

	public static String ORDER_LINE_DISCOUNTFEE = "ORDER_LINE_DISCOUNTFEE";

	public static String JD_JSC_ORDER_STATE = "WAIT_SELLER_STOCK_OUT"; // WAIT_SELLER_STOCK_OUT 等待出库
	public static String JD_JSC_PAGE = "1";
	public static String JD_JSC_PAGE_SIZE = "30";
	public static String JD_JSC_OPTIONAL_FIELDS = "order_id,consignee_info,payment_confirm_time,order_start_time,order_total_price,seller_discount,order_remark,coupon_detail_list,vender_remark,item_info_list,freight_price,order_seller_price,order_payment,coupon_detail_list,order_state,delivery_type,pay_type,modified,order_type";
	public static String JD_JSC_SORTTYPE = "2";
	public static String JD_JSC_DATETYPE = "1"; // 1为按订单创建时间查询；其它数字为按订单（订单状态、修改运单号）修改时间

	// public static String JD_JSC_CHANNEL = "JD";
	// public static String JD_JSC_INNERSOURCE = "JSC";
	// public static String JD_JSC_EVENT = "create";

	public static String JD_DELIVERY_YUNDA_ID = "1327"; // 京东物流统一使用韵达快递

	/**
	 * 等待出库
	 */
	public static String JD_ORDER_STATE_WAIT_SELLER_STOCK_OUT = "WAIT_SELLER_STOCK_OUT";
	/**
	 * 发往配送中心（只适用于LBP，SOPL商家）
	 */
	public static String JD_ORDER_STATE_SEND_TO_DISTRIBUTION_CENER = "SEND_TO_DISTRIBUTION_CENER";
	/**
	 * 配送中心已收货（只适用于LBP，SOPL商家）
	 */
	public static String JD_ORDER_STATE_DISTRIBUTION_CENTER_RECEIVED = "DISTRIBUTION_CENTER_RECEIVED";
	/**
	 * 等待确认收货
	 */
	public static String JD_ORDER_STATE_WAIT_GOODS_RECEIVE_CONFIRM = "WAIT_GOODS_RECEIVE_CONFIRM";
	/**
	 * 收款确认（服务完成）
	 */
	public static String JD_ORDER_STATE_RECEIPTS_CONFIRM = "RECEIPTS_CONFIRM";
	/**
	 * 等待发货
	 */
	public static String JD_ORDER_STATE_WAIT_SELLER_DELIVERY = "WAIT_SELLER_DELIVERY";
	/**
	 * 完成
	 */
	public static String JD_ORDER_STATE_FINISHED_L = "FINISHED_L";
	/**
	 * 已锁定
	 */
	public static String JD_ORDER_STATE_LOCKED = "LOCKED";
	// ***************************************JD END**********************************************

	// ***************************************YHD START********************************************

	// public static String YHD_OTC_CHANNEL = "YHD";
	// public static String YHD_OTC_INNERSOURCE = "JSC";
	// public static String YHD_OTC_EVENT = "ORDERCREATE";
	//
	// public static String YHD_JSC_CHANNEL = "YHD";
	// public static String YHD_JSC_INNERSOURCE = "JSC";
	// public static String YHD_JSC_EVENT = "ORDERCREATE";
	public static String DEFAULT_YHD_FIELDS = "tid,status,payment,created,buyer_message,seller_memo,total_fee,buyer_nick,receiver_name,receiver_address,receiver_mobile,receiver_phone,receiver_state,receiver_city,receiver_district,receiver_zip,received_payment,discount_fee,post_fee,has_post_fee,pay_time,orders,outer_sku_id,outer_iid,num,price,divide_order_fee,part_mjz_discount,product_deposit";
	public static String YHD_OTC_ORDER_STATE = "";
	public static Long YHD_OTC_PAGE = 1L;
	public static Long YHD_OTC_PAGE_SIZE = 30L;
	public static String YHD_OTC_OPTIONAL_FIELDS = "";


	// ***************************************YHD END**********************************************
}
