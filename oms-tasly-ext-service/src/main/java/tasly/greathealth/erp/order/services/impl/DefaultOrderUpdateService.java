/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.erp.order.services.impl;

import com.hybris.oms.service.service.AbstractHybrisService;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.erp.api.order.updateorder.dto.EccBillto;
import tasly.greathealth.erp.api.order.updateorder.dto.EccDelivery;
import tasly.greathealth.erp.api.order.updateorder.dto.Item;
import tasly.greathealth.erp.api.order.updateorder.dto.Message;
import tasly.greathealth.erp.api.order.updateorder.dto.OmsOrder;
import tasly.greathealth.erp.api.order.updateorder.dto.OmsOrders;
import tasly.greathealth.erp.api.order.updateorder.dto.Refund;
import tasly.greathealth.erp.api.order.updateorder.dto.Shipto;
import tasly.greathealth.erp.order.services.OrderUpdateService;
import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.erp.util.TaslyUtils;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSBILLTO;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSDELIVERY;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSORDERITEMREFUND;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSSALEORDERCOMMIN;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSSALEORDERCOMMTAB;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSSALEORDERSCOMMIN;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSSHIPTO;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRSDOMSSOITEMREFUND;
import tasly.greathealth.oms.soapclient.updateorder.ZSTRUPIBASEINFO2;


/**
 * created by libin539 for TS-689
 * OMS订单信息更新同步到SAP ERP
 */
public class DefaultOrderUpdateService extends AbstractHybrisService implements OrderUpdateService, TaslyERPConstants
{
	private static final Logger erpOrderLog = OmsLoggerFactory.getErporderlog();
	private TaslyOrderQueryFactory orderQueries;

	@Required
	public void setOrderQueries(final TaslyOrderQueryFactory orderQueries)
	{
		this.orderQueries = orderQueries;
	}

	/**
	 * TS-689:Hybris OMS订单信息更新同步到SAP ERP
	 */
	@Transactional
	@Override
	public TaslyOrderData getTaslyOrderDataByOrderID(final String orderId) throws Exception
	{
		TaslyOrderData taslyOrderData = null;
		taslyOrderData = (TaslyOrderData) this.findOneSingle(this.orderQueries.getOrderByID(orderId));
		return taslyOrderData;
	}

	@Override
	public boolean checkOrderStatus(final String orderId) throws Exception
	{

		String orderStatus = REPLICATIONSTATUS_N;
		boolean flag = false;
		if (orderId != null && orderId.length() > 0)
		{
			final TaslyOrderData taslyOrderData = getTaslyOrderDataByOrderID(orderId);
			if (taslyOrderData != null)
			{
				orderStatus = taslyOrderData.getReplication_status();
			}
			if (orderStatus.equalsIgnoreCase(REPLICATIONSTATUS_S) || orderStatus.equalsIgnoreCase(REPLICATIONSTATUS_W))
			{
				flag = true;
			}
		}
		else
		{
			erpOrderLog.error(UO_LogHead + "OMS订单ID为空！");
		}
		return flag;
	}

	@Override
	public ZSTRUPIBASEINFO2 createEccBaseInfo() throws Exception
	{
		final ZSTRUPIBASEINFO2 baseInfo = new ZSTRUPIBASEINFO2();
		final String sendTime = TaslyUtils.convertTimeToString();
		// generated ramdom code
		String msgID = "OMS_VENDOR_";
		msgID = msgID.concat(sendTime).concat("_").concat(TaslyUtils.generateRandomCode());
		baseInfo.setMSGID(msgID);
		// baseInfo.setPMSGID(value);
		baseInfo.setSENDTIME(sendTime);
		baseInfo.setSSYSTEM(UO_SSYSTEM);
		baseInfo.setSERVICENAME(UO_SERVICENAME);
		baseInfo.setTSYSTEM(UO_TSYSTEM);
		baseInfo.setRETRY(UO_RETRY);
		return baseInfo;
	}

	@Override
	public ZSTRSDOMSSALEORDERSCOMMIN omsMessage2Ecc(final Message ordersMessage) throws Exception
	{
		// 判断参数是否为空
		if (ordersMessage == null)
		{
			// erpOrderLog.warn("UpdateOrder接口:Message is null,please check!");
			return null;
		}
		/**
		 * omsorders:订单组对象
		 * omsorderList:订单对象list
		 */
		OmsOrders omsorders = null;
		List<OmsOrder> omsorderList = null;
		/**
		 * eccOrderId:Ecc订单ID
		 * operation:02 – 锁定|03 – 修改 (修改需要将快递地址和用户备注同时传递给PI)|04 – 解锁|05 – 修改备注|06 – 修改快递
		 * omsOrderId:OMS 订单ID
		 * userNotes:用户备注
		 * csNotes:客服备注
		 */
		String eccOrderId = null;
		String operation = null;
		String omsOrderId = null;
		String userNotes = null;
		String csNotes = null;
		/**
		 * 快递对象
		 *
		 * ECC预设9个临时客户之一
		 * 客户名字
		 * 收货人的所在省份（文本）
		 * 收货人的所在城市（文本）
		 * 收货人的所在地区（文本）
		 * 收货人的详细地址（文本）
		 * 收货人的邮编
		 * 收货人的手机号码
		 * 收货人的电话号码
		 */
		Shipto shipto = null;
		String eccShiptoId = null;
		String eccShiptoName = null;
		String eccShiptoRegion = null;
		String eccShiptoCity = null;
		String eccShiptoDistrict = null;
		String eccShiptoAddress = null;
		String eccShiptoZipcode = null;
		String eccShiptoMob = null;
		String eccShiptoTel = null;
		/**
		 * eccBillto:EccBillto对象
		 *
		 * eccInvoiceType:发票类型
		 * eccInvoiceTitle:发票抬头
		 * eccInvoiceContent:发票内容
		 * eccTaxpayerNumber:专票纳税人登记号
		 * eccBankName:专票银行名称
		 * eccBankNumber:专票银行账号
		 * eccCustomerAddress:专票客户地址
		 * eccCustomerPhone:专票客户电话
		 */
		EccBillto eccBillto = null;
		String eccInvoiceType = null;
		String eccInvoiceTitle = null;
		String eccInvoiceContent = null;
		String eccTaxpayerNumber = null;
		String eccBankName = null;
		String eccBankNumber = null;
		String eccCustomerAddress = null;
		String eccCustomerPhone = null;
		/**
		 * eccDelivery:ECC_DELIVERY对象
		 * eccExpressId:快递公司code
		 * eccExpressName:快递公司名称
		 */
		EccDelivery eccDelivery = null;
		String eccExpressId;
		String eccExpressName;
		/**
		 * refund:所有退款行项目
		 * itemList:退款行项目list
		 * refundOmsItemId:OMS中退款行项目ID
		 * refundType:退款类型
		 * refundQuantity:退款项数量
		 * refundAmount:退款金额
		 */
		Refund refund = null;
		List<Item> itemList = null;
		String refundOmsItemId = null;
		String refundType = null;
		String refundQuantity = null;
		double refundAmount;


		/**
		 * WSDL BEAN
		 *
		 * zstrsdomssaleorderscommin:ECC对象,name = "ORDERS"
		 * saleordercommtab/zssaleordercommin:OMS单个ORDER对象
		 * zstrsdomsshipto:SHIPTO对象
		 */
		ZSTRSDOMSSALEORDERSCOMMIN zstrsdomssaleorderscommin = null;
		ZSTRSDOMSSALEORDERCOMMTAB saleordercommtab = null;
		ZSTRSDOMSSALEORDERCOMMIN zssaleordercommin = null;
		ZSTRSDOMSSHIPTO zstrsdomsshipto = null;

		// 获取参数值
		omsorders = ordersMessage.getOmsOrders();
		omsorderList = omsorders.getOmsOrder();
		for (final OmsOrder omsorder : omsorderList)
		{
			operation = omsorder.getOperation();
			eccOrderId = omsorder.getEccOrderId();
			omsOrderId = omsorder.getOmsOrderId();
			erpOrderLog.info(UO_LogHead + "Message对象中operation,eccOrderId,omsOrderId三个字段分别为：" + operation + "," + eccOrderId + ","
					+ omsOrderId + ".");
			// 判断操作类型必须是02、03、04
			if (operation.equals(UO_LOCKSTATUS) || operation.equals(UO_ALTERTATUS) || operation.equals(UO_DEBLOCKSTATUS))
			{
				if (checkOrderId(eccOrderId, 35, CHECKLENGTH) && checkOrderId(omsOrderId, 64, CHECKLENGTH))
				{
					zstrsdomssaleorderscommin = new ZSTRSDOMSSALEORDERSCOMMIN();
					saleordercommtab = new ZSTRSDOMSSALEORDERCOMMTAB();
					zssaleordercommin = new ZSTRSDOMSSALEORDERCOMMIN();
					// 赋值用户、客服备注长度
					userNotes = omsorder.getUserNotes();
					csNotes = omsorder.getCsNotes();
					// 验证用户、客服备注长度
					if (CHECKLENGTH)
					{
						userNotes = controlFileLength("userNotes", userNotes, 255);
						csNotes = controlFileLength("csNotes", csNotes, 255);
					}
					shipto = omsorder.getShipto();
					eccBillto = omsorder.getEccBillto();
					eccDelivery = omsorder.getEccDelivery();

					if (shipto != null)
					{
						// 赋值并验证Shipto相关字段
						eccShiptoId = shipto.getEccShiptoId();
						eccShiptoName = shipto.getEccShiptoName();
						eccShiptoRegion = shipto.getEccShiptoRegion();
						eccShiptoCity = shipto.getEccShiptoCity();
						eccShiptoDistrict = shipto.getEccShiptoDistrict();
						eccShiptoAddress = shipto.getEccShiptoAddress();
						eccShiptoZipcode = shipto.getEccShiptoZipcode();
						eccShiptoMob = shipto.getEccShiptoMob();
						eccShiptoTel = shipto.getEccShiptoTel();
						if (CHECKLENGTH)
						{
							eccShiptoId = controlFileLength("eccShiptoId", eccShiptoId, 10);
							eccShiptoName = controlFileLength("eccShiptoName", eccShiptoName, 64);
							eccShiptoRegion = controlFileLength("eccShiptoRegion", eccShiptoRegion, 64);
							eccShiptoCity = controlFileLength("eccShiptoCity", eccShiptoCity, 64);
							eccShiptoDistrict = controlFileLength("eccShiptoDistrict", eccShiptoDistrict, 64);
							eccShiptoAddress = controlFileLength("eccShiptoAddress", eccShiptoAddress, 255);
							eccShiptoZipcode = controlFileLength("eccShiptoZipcode", eccShiptoZipcode, 12);
							eccShiptoMob = controlFileLength("eccShiptoMob", eccShiptoMob, 64);
							eccShiptoTel = controlFileLength("eccShiptoTel", eccShiptoTel, 64);
						}
						zstrsdomsshipto = new ZSTRSDOMSSHIPTO();
						zstrsdomsshipto.setECCSHIPTOID(eccShiptoId);
						zstrsdomsshipto.setECCSHIPTONAME(eccShiptoName);
						zstrsdomsshipto.setECCSHIPTOREGION(eccShiptoRegion);
						zstrsdomsshipto.setECCSHIPTOCITY(eccShiptoCity);
						zstrsdomsshipto.setECCSHIPTODISTRICT(eccShiptoDistrict);
						zstrsdomsshipto.setECCSHIPTOADDRESS(eccShiptoAddress);
						zstrsdomsshipto.setECCSHIPTOZIPCODE(eccShiptoZipcode);
						zstrsdomsshipto.setECCSHIPTOMOB(eccShiptoMob);
						zstrsdomsshipto.setECCSHIPTOTEL(eccShiptoTel);
					}
					else
					{
						erpOrderLog.warn(UO_LogHead + "Message对象Shipto节点为空.");
					}
					// converter
					ZSTRSDOMSBILLTO zstrsdomsbillto = null;
					if (eccBillto != null)
					{
						// 赋值并验证BILLTO相关字段
						eccInvoiceType = eccBillto.getEccInvoiceType();
						eccInvoiceTitle = eccBillto.getEccInvoiceTitle();
						eccInvoiceContent = eccBillto.getEccInvoiceContent();
						eccTaxpayerNumber = eccBillto.getEccTaxpayerNumber();
						eccBankName = eccBillto.getEccBankName();
						eccBankNumber = eccBillto.getEccBankNumber();
						eccCustomerAddress = eccBillto.getEccCustomerAddress();
						eccCustomerPhone = eccBillto.getEccCustomerPhone();
						if (CHECKLENGTH)
						{
							eccInvoiceType = controlFileLength("eccInvoiceType", eccInvoiceType, 8);
							eccInvoiceTitle = controlFileLength("eccInvoiceTitle", eccInvoiceTitle, 80);
							eccInvoiceContent = controlFileLength("eccInvoiceContent", eccInvoiceContent, 128);
							eccTaxpayerNumber = controlFileLength("eccTaxpayerNumber", eccTaxpayerNumber, 30);
							eccBankName = controlFileLength("eccBankName", eccBankName, 80);
							eccBankNumber = controlFileLength("eccBankNumber", eccBankNumber, 30);
							eccCustomerAddress = controlFileLength("eccCustomerAddress", eccCustomerAddress, 80);
							eccCustomerPhone = controlFileLength("eccCustomerPhone", eccCustomerPhone, 18);
						}
						zstrsdomsbillto = new ZSTRSDOMSBILLTO();
						zstrsdomsbillto.setECCINVOICETYPE(eccInvoiceType);
						zstrsdomsbillto.setECCINVOICETITLE(eccInvoiceTitle);
						zstrsdomsbillto.setECCINVOICECONTENT(eccInvoiceContent);
						zstrsdomsbillto.setECCTAXPAYERNUMBER(eccTaxpayerNumber);
						zstrsdomsbillto.setECCBANKNAME(eccBankName);
						zstrsdomsbillto.setECCBANKNUMBER(eccBankNumber);
						zstrsdomsbillto.setECCCUSTOMERADDRESS(eccCustomerAddress);
						zstrsdomsbillto.setECCCUSTOMERPHONE(eccCustomerPhone);
					}
					else
					{
						erpOrderLog.warn(UO_LogHead + "Message对象EccBillto节点为空.");

					}
					ZSTRSDOMSDELIVERY zstrsdomsdelivery = null;
					if (eccDelivery != null)
					{
						// 赋值并验证EccDelivery相关字段
						eccExpressId = eccDelivery.getEccExpressId();
						eccExpressName = eccDelivery.getEccExpressName();
						if (CHECKLENGTH)
						{
							eccExpressId = controlFileLength("eccExpressId", eccExpressId, 10);
							eccExpressName = controlFileLength("eccExpressName", eccExpressName, 64);
						}

						zstrsdomsdelivery = new ZSTRSDOMSDELIVERY();
						zstrsdomsdelivery.setECCEXPRESSID(eccExpressId);
						zstrsdomsdelivery.setECCEXPRESSNAME(eccExpressName);
					}
					else
					{
						erpOrderLog.warn(UO_LogHead + "Message对象EccDelivery节点为空.");
					}

					// TS-168 added by Liang Weng
					// eccItemNode:item
					// eccRefundNode:refund

					final ZSTRSDOMSSOITEMREFUND eccRefundNode = new ZSTRSDOMSSOITEMREFUND();
					final List<ZSTRSDOMSORDERITEMREFUND> eccItemList = eccRefundNode.getItem();
					refund = omsorder.getRefund();
					if (null != refund)
					{
						itemList = refund.getItem();
					}
					else
					{
						erpOrderLog.warn(UO_LogHead + "Message对象Refund节点为空.");
					}
					BigDecimal refundAmountBD = null;
					// 解锁退款操作
					if (operation.equals(UO_DEBLOCKSTATUS))
					{
						if (null != itemList && !itemList.isEmpty())
						{
							final Iterator<Item> itemIter = itemList.iterator();
							while (itemIter.hasNext())
							{
								final ZSTRSDOMSORDERITEMREFUND eccItemNode = new ZSTRSDOMSORDERITEMREFUND();
								final Item item = itemIter.next();
								refundOmsItemId = item.getRefundOmsItemId();
								refundType = item.getRefundType();
								refundQuantity = item.getRefundQuantity();
								refundAmount = item.getRefundAmount();
								if (CHECKLENGTH)
								{
									refundOmsItemId = controlFileLength("refundOmsItemId", refundOmsItemId, 35);
									refundType = controlFileLength("refundType", refundType, 1);
									refundQuantity = controlFileLength("refundQuantity", refundQuantity, 8);
									refundAmountBD = double2BigDecimal(refundAmount);

								}
								eccItemNode.setREFUNDOMSITEMID(refundOmsItemId);
								eccItemNode.setREFUNDTYPE(refundType);
								eccItemNode.setREFUNDQUANTITY(refundQuantity);
								eccItemNode.setREFUNDAMOUNT(refundAmountBD);
								eccItemList.add(eccItemNode);
							}
						}
						else
						{
							erpOrderLog.warn(UO_LogHead + "Refund对象Item节点为空.");
						}
					}

					// SET
					zssaleordercommin.setOPERATION(operation);
					zssaleordercommin.setECCORDERID(eccOrderId);
					zssaleordercommin.setOMSORDERID(omsOrderId);
					zssaleordercommin.setCSNOTES(csNotes);
					zssaleordercommin.setUSERNOTES(userNotes);
					zssaleordercommin.setSHIPTO(zstrsdomsshipto);
					zssaleordercommin.setECCBILLTO(zstrsdomsbillto);
					zssaleordercommin.setECCDELIVERY(zstrsdomsdelivery);
					zssaleordercommin.setREFUND(eccRefundNode);

					saleordercommtab.getItem().add(zssaleordercommin);
					zstrsdomssaleorderscommin.setORDERS(saleordercommtab);
				}
				else
				{
					erpOrderLog.error(UO_LogHead + "Message对象eccOrderId或者msOrderId为空,无法进行后续修改操作.");
				}
			}
			else
			{
				erpOrderLog.error(UO_LogHead + "Message对象中operation字段不合法,无法进行后续修改操作.");
			}
		}
		return zstrsdomssaleorderscommin;
	}

	// 判断字段是否为NULL及长度是否大于0小于字段长度
	private boolean checkOrderId(final String parameter, final int length, final boolean checkLength)
	{
		boolean falg = false;
		if (parameter != null && checkLength)
		{
			if (parameter.length() > 0 && parameter.length() <= length)
			{
				falg = true;
			}
		}
		else
		{
			if (parameter != null && parameter.length() > 0)
			{
				falg = true;
			}
		}
		return falg;
	}

	// 增加字段长度控制
	private String controlFileLength(final String fileName, final String fileValue, final int length)
	{
		String result = null;
		if (fileValue != null && fileValue.length() > length)
		{
			result = fileValue.substring(0, length);
			erpOrderLog.warn(UO_LogHead + "Message对象字段:" + fileName + "值为:" + fileValue + ",长度是：" + fileValue.length() + ",接口规范长度："
					+ length + ".");
			erpOrderLog.warn(UO_LogHead + "Message对象字段:" + fileName + "超长,需要截串已保证可以顺利传送到ECC.");
		}
		else
		{
			result = fileValue;
		}
		return result;
	}

	// 用于退款金额精度与长度控制(15,3)
	private BigDecimal double2BigDecimal(final double refundAmount)
	{
		final BigDecimal decimal = new BigDecimal(Double.toString(refundAmount));
		decimal.setScale(3, BigDecimal.ROUND_HALF_UP);
		if (decimal.precision() > 18)
		{
			erpOrderLog.warn(UO_LogHead + "Message对象字段: " + "refundAmount" + "值为: " + refundAmount + ",长度是：" + decimal.precision()
					+ ",接口规范长度：" + "(15,3)" + ".");
			erpOrderLog.warn(UO_LogHead + "Message对象字段:" + "refundAmount" + "超长,需要截串已保证可以顺利传送到ECC.");
		}
		return decimal;
	}
}
