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
package tasly.greathealth.erp.order.facades;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.order.OrderLine;

import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.erp.api.codeMapping.dto.ErpCodeMapping;
import tasly.greathealth.erp.api.order.UpdateOrderDeliveryStatusFacade;
import tasly.greathealth.erp.api.order.updatedelivery.dto.Message;
import tasly.greathealth.erp.order.services.OrderDeliveryStatusUpdateService;
import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.erp.util.TaslyUtils;
import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLineQuantity;
import tasly.greathealth.oms.api.order.dto.TaslyPaymentInfo;
import tasly.greathealth.oms.api.order.dto.TaslyShippingAndHandling;
import tasly.greathealth.oms.domain.erp.ErpCodeMappingData;
import tasly.greathealth.oms.domain.order.ExpressData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.services.TaslyOrderLineQuantityService;
import tasly.greathealth.oms.soapclient.createorder.SIECCOMSSALESORDERCREATEOUTAsyn;
import tasly.greathealth.oms.soapclient.createorder.ZFMSDOMSSALEORDERREQUEST;
import tasly.greathealth.oms.soapclient.createorder.ZSDOMSSALESORDERCREATE;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSBILLTO;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSDELIVERY;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSHEADCOND;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSHEADCONDTAB;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSITEMCOND;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSITEMCONDTAB;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSPAYER;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSSALEORDER;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSSALEORDERHEAD;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSSALEORDERITEM;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSSALEORDERS;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSSALEORDERTAB;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSSHIPTO;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSSOITEMTAB;
import tasly.greathealth.oms.soapclient.createorder.ZSTRSDOMSSOLDTO;
import tasly.greathealth.oms.soapclient.createorder.ZSTRUPIBASEINFO2;


public class DefaultOrderDeliveryStatusUpdateFacade implements UpdateOrderDeliveryStatusFacade, TaslyERPConstants
{

	private static final Logger LOG = OmsLoggerFactory.getErporderlog();
	private static final Logger LOGERROR = OmsLoggerFactory.getErpordererrorlog();

	private Converter<ErpCodeMapping, ErpCodeMappingData> erpCodeMappingModelToDataConverter;

	private Converter<ErpCodeMappingData, ErpCodeMapping> erpCodeMappingDataToModelConverter;

	private Converter<TaslyOrderData, TaslyOrder> orderConverter;

	private Converters converters;

	HashMap<String, String> salesOrgMap;
	HashMap<String, String> customerOrgMap;
	HashMap<String, String> expressOrgMap;

	private static String PIUSERNAME;

	private static String PIPASSWORD;

	public OrderDeliveryStatusUpdateService orderService;

	// DNLogService dNlogService;

	TaslyOrderLineQuantityService taslyOrderLineQuantityService;

	SIECCOMSSALESORDERCREATEOUTAsyn createOrderSoapService;
	// blank flag
	boolean nullFlag = false;
	List<String> nullList = new ArrayList<String>();
	List<String> processedOmsOrderIDs = new ArrayList<String>();

	/**
	 * Used by TS-396
	 * Update OMS packing status according to received ECC packing status
	 *
	 * @param omsOrderIds
	 */
	@Override
	public List<String> updateOrderStatus4Packing(final List<String> omsOrderIds)
	{
		LOG.info("开始调用 updateOrderStatus4Packing 方法:");

		final List<String> packingFailedOrders = new ArrayList<String>();

		boolean status = true;

		if (null != omsOrderIds && omsOrderIds.size() > 0)
		{
			for (final String orderID : omsOrderIds)
			{
				status = updateOrdertPacking(orderID);
				if (!status)
				{
					packingFailedOrders.add(orderID);
				}
			}
		}
		return packingFailedOrders;
	}

	// Update OMS order packing status from PACKING to PACKED;
	private Boolean updateOrdertPacking(final String orderID)
	{
		Boolean status = true;
		LOG.debug("处理订单号 : " + orderID);
		if (null != orderID)
		{
			try
			{
				orderService.updateTaslyOrderDataPackingByOrderID(orderID);
			}
			catch (final EntityNotFoundException entityException)
			{
				LOG.error("订单未发现: " + entityException.getLocalizedMessage());
				status = false;
			}
		}
		return status;
	}

	/**
	 * Used by TS-396
	 * Update OMS delivery Number and company name according to received ECC order message
	 *
	 * @param deliveryMessage
	 */
	@Override
	@Transactional
	public List<String> updateOrderStatus4Delivery(final Message deliveryMessage)
	{
		final List<String> deliveryFailedOrders = new ArrayList<String>();

		LOG.info("开始调用 UpdateOrderStatus4Delivery ...");

		/*
		 * move DNLog item creation to TaslyOrderLineQuantityService
		 *
		 * for (final EccDelivery eccDelivery : deliveryMessage.getEccOrderDeliveries())
		 * {
		 * LOG.debug("物流单号 :" + eccDelivery.getDeliveryNumber() + ",订单号:" + eccDelivery.getOmsOrderId() + ",订单行项目号: "
		 * + eccDelivery.getOmsLineId() + ",SKU :" + eccDelivery.getSkuId());
		 * // invoke DNLogService to store data
		 * if (null != eccDelivery.getOmsOrderId() && null != eccDelivery.getOmsLineId())
		 * {
		 * try
		 * {
		 * dNlogService.createDNLog(eccDelivery.getOmsOrderId(), eccDelivery.getOmsLineId());
		 * }
		 * catch (final Exception e)
		 * {
		 * LOG.error("更改dNlog失败!");
		 * LOG.error(e.getMessage(), e);
		 * }
		 * }
		 */

		taslyOrderLineQuantityService.updateOrderExpressCodeNumberMemo(deliveryMessage);
		return deliveryFailedOrders;
	}

	/**
	 * TS-689:Hybris OMS订单信息更新同步到SAP ERP
	 * Create ECC orders according to OMS orders:
	 * 1. 审核标记：order.approve_status=1
	 * 2. 同步标记：order.replication_status="N,E"
	 * 3. 重发次数：order.replication_times<3
	 *
	 * @return processed order ID list
	 * @author vincent.yin
	 */
	@Override
	@Transactional
	public List<String> createEccOrders()
	{
		List<TaslyOrderData> approverdOmsOrderDatas = new ArrayList<TaslyOrderData>();
		List<TaslyOrder> approverdOmsOrders = new ArrayList<TaslyOrder>();
		final String[] replication_status = {REPLICATIONSTATUS_N, REPLICATIONSTATUS_E};

		processedOmsOrderIDs = new ArrayList<String>();
		// below used for create ecc order soap client
		// baseinfo
		ZSTRUPIBASEINFO2 baseInfor = new ZSTRUPIBASEINFO2();
		// orders
		final ZSTRSDOMSSALEORDERTAB orders = new ZSTRSDOMSSALEORDERTAB();
		ZSTRSDOMSSALEORDER order = new ZSTRSDOMSSALEORDER();
		// message
		final ZSTRSDOMSSALEORDERS message = new ZSTRSDOMSSALEORDERS();
		// parameter
		final ZSDOMSSALESORDERCREATE parameters = new ZSDOMSSALESORDERCREATE();
		// request
		final ZFMSDOMSSALEORDERREQUEST orderRequest = new ZFMSDOMSSALEORDERREQUEST();

		// fetch the ERP code mapping data
		this.setErpCodeMappingMap();
		// fetch express data
		this.setExpressMap();

		// 1.get approved order list from OMS
		approverdOmsOrderDatas = orderService.getOmsApprovedOrders(CO_APPROVESTATUS, replication_status, CO_REPLICATIONTIME);
		// convert orderdata to order dto
		approverdOmsOrders = converters.convertAll(approverdOmsOrderDatas, orderConverter);

		LOG.info(CO_LogHead + "OMS 订单数量: " + approverdOmsOrders.size());

		// 2.process message baseInfor
		// set baseInfor
		baseInfor = createEccBaseInfo();

		// 3.process message orderList
		// process each oms order
		for (final TaslyOrder taslyOrder : approverdOmsOrders)
		{
			// used to process sales and customer match
			String salsOrg = null;
			String customerOrg = null;
			LOG.info(CO_LogHead + "--------------------------------------------------");
			LOG.info(CO_LogHead + "开始处理订单 :" + taslyOrder.getOrderId());

			if (null != taslyOrder.getInnerSource() || null != taslyOrder.getChannelSource())
			{
				salsOrg = this.getSalesOrgMap().get(taslyOrder.getInnerSource().toString());
				customerOrg = this.getCustomerOrgMap().get(taslyOrder.getChannelSource().toString());

				LOG.info(CO_LogHead + "channelSource is: " + taslyOrder.getChannelSource().toString() + ",innserSource is: "
						+ taslyOrder.getInnerSource().toString() + ",salsOrg is:" + salsOrg + ",customerOrg is: " + customerOrg);
			}
			if (null == salsOrg || null == customerOrg)
			{
				nullFlag = true;
				nullList.add(CO_LogHead + "[orders].inner_source or ||[orders].channel_source");

			}

			order = new ZSTRSDOMSSALEORDER();
			// this list was used to update order.replication_status later
			if (!nullFlag)
			{
				// create each ECC order
				order = createEccOrder(taslyOrder, salsOrg, customerOrg);
				// This order is OK,
				if (!nullFlag)
				{
					orders.getItem().add(order);
					processedOmsOrderIDs.add(taslyOrder.getOrderId());
				}
				else
				{
					LOG.error(CO_LogHead + "订单 " + taslyOrder.getOrderId() + " 创建失败,下列字段为空:");
					LOGERROR.error(CO_LogHead + "订单 " + taslyOrder.getOrderId() + " 创建失败,下列字段为空:");
					for (final String field : nullList)
					{
						LOG.error(field);
						// TS-891
						LOGERROR.error(field);
					}
					// to process the next order
					nullFlag = false;
					nullList = new ArrayList<String>();
				}
			}
		}
		// set baseInfo attribute
		orderRequest.setBASEINFO(baseInfor);
		// set message orders attribute
		message.setORDERS(orders);
		// set message attribute
		orderRequest.setMESSAGE(message);
		// set Ecc parameters
		parameters.setIREQUEST(orderRequest);

		// SIECCOMSSALESORDERCREATEOUTAsyn serviceInterface = null;
		// 4. invoke eccSoapClient to creat new order
		if (processedOmsOrderIDs.size() > 0)
		{
			// // start put WSDL user name and password into Authenticator
			Authenticator.setDefault(new Authenticator()
			{
				// @Override
				@Override
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(PIUSERNAME, PIPASSWORD.toCharArray());
				}
			});
			//
			try
			{
				// invoke the soap service
				LOG.info(CO_LogHead + "开始发送订单到PI接口。。。");

				createOrderSoapService.siECCOMSSALESORDERCREATEOUTAsyn(parameters);
				// 5. updated all of these omsorders replication_status from 'N' to 'Y'
				updateOrderReplicationStatus(processedOmsOrderIDs);

				LOG.info(CO_LogHead + "更新订单同步状态完毕！");

			}
			catch (final Exception e)
			{
				LOG.error(CO_LogHead + "连接PI接口超时，请检查当前网络状态!");
			}
		}
		return processedOmsOrderIDs;
	}

	// 1.create ecc order base info method

	public ZSTRUPIBASEINFO2 createEccBaseInfo()
	{
		final ZSTRUPIBASEINFO2 baseInfo = new ZSTRUPIBASEINFO2();
		String msgID = "OMS_VENDOR_";
		msgID = msgID.concat(TaslyUtils.convertTimeToString()).concat("_").concat(TaslyUtils.generateRandomCode());

		LOG.info(CO_LogHead + "开始处理 BaseInfo,msgID is:" + msgID);

		baseInfo.setMSGID(msgID);
		// baseInfo.setPMSGID(value);
		baseInfo.setSENDTIME(TaslyUtils.convertTimeToString());
		baseInfo.setSSYSTEM(CO_SSYSTEM);
		baseInfo.setSERVICENAME(CO_SERVICENAME);
		baseInfo.setTSYSTEM(CO_TSYSTEM);
		baseInfo.setRETRY(CO_RETRY);
		return baseInfo;
	}

	// 2.create ecc order

	public ZSTRSDOMSSALEORDER createEccOrder(final TaslyOrder taslyOrder, final String salesOrg, final String customerOrg)
	{
		final ZSTRSDOMSSALEORDER order = new ZSTRSDOMSSALEORDER();

		ZSTRSDOMSSALEORDERHEAD header = new ZSTRSDOMSSALEORDERHEAD();

		ZSTRSDOMSSOLDTO soldTo = new ZSTRSDOMSSOLDTO();

		ZSTRSDOMSBILLTO billTo = new ZSTRSDOMSBILLTO();

		ZSTRSDOMSPAYER payer = new ZSTRSDOMSPAYER();

		ZSTRSDOMSSHIPTO shipTo = new ZSTRSDOMSSHIPTO();

		ZSTRSDOMSDELIVERY delivery = new ZSTRSDOMSDELIVERY();

		ZSTRSDOMSHEADCONDTAB headerConditions = new ZSTRSDOMSHEADCONDTAB();

		ZSTRSDOMSSOITEMTAB itemTab = new ZSTRSDOMSSOITEMTAB();
		// set header info
		header = createEccHeader(taslyOrder, salesOrg);
		// set soldTO
		soldTo = createEccSoldTo(taslyOrder, customerOrg);
		// set billTo
		billTo = createEccBillTo(taslyOrder, customerOrg);
		// set payer
		payer = createPayer(taslyOrder);
		// set shipTo
		shipTo = createShipTo(taslyOrder, customerOrg);
		// set delivery
		delivery = createDelivery(taslyOrder);
		// set headConditions
		headerConditions = createHeadCondtions(taslyOrder);
		// set items
		itemTab = createItemTab(taslyOrder);
		// set to Order
		order.setHEADER(header);
		order.setECCSOLDTO(soldTo);
		order.setECCBILLTO(billTo);
		order.setECCPAYER(payer);
		order.setECCSHIPTO(shipTo);
		order.setECCDELIVERY(delivery);
		// ensure maxlength is 132
		//
		if (null != taslyOrder.getBuyerMessage() && taslyOrder.getBuyerMessage().length() > 132)
		{
			order.setOMSCUSTOMERNOTES(taslyOrder.getBuyerMessage().substring(0, 132));
		}
		else
		{
			order.setOMSCUSTOMERNOTES(taslyOrder.getBuyerMessage());
		}
		// seller comments
		if (null != taslyOrder.getSellerMessage() && taslyOrder.getSellerMessage().length() > 132)
		{
			order.setOMSAGENTNOTES(taslyOrder.getSellerMessage().substring(0, 132));
		}
		else
		{
			order.setOMSAGENTNOTES(taslyOrder.getSellerMessage());
		}

		order.setHEADERCONDITIONS(headerConditions);

		LOG.debug(CO_LogHead + "买家备注：" + order.getOMSCUSTOMERNOTES() + ",卖家备注：" + order.getOMSAGENTNOTES());
		order.setITEMS(itemTab);

		return order;
	}

	// 2.1 create header

	public ZSTRSDOMSSALEORDERHEAD createEccHeader(final TaslyOrder order, final String salesOrg)
	{
		final ZSTRSDOMSSALEORDERHEAD header = new ZSTRSDOMSSALEORDERHEAD();

		header.setOPERATIONTYPEID(CO_OPERATIONTYPE);
		header.setECCORDERTYPEID(CO_ORDERTYPE);
		header.setOMSORDERID(order.getOrderId());
		header.setORIGINALORDERID(order.getOriginalOrderId());
		// 通过OMS订单inner_source字段，
		// sales_org OTC 1901
		// sales_org JSC 1990
		header.setECCSALESORG(salesOrg);
		// format :20131010153020
		if (null != order.getOrderCreatedTime())
		{
			// TS-956 订单的创建日期和时间,格式2015-03-02 23:43:39 modify by libin 20150311
			// header.setORDERDATE(TaslyUtils.convertDateToString(order.getIssueDate()));
			header.setORDERDATE(TaslyUtils.convertDateToStringT(order.getOrderCreatedTime()));
		}
		else
		{
			nullFlag = true;
			nullList.add("[orders].issuedate");
		}
		if (null != order.getIssueDate())
		{
			// TS-956 订单的付款日期和时间，格式2015-03-02 23:43:39 modify by libin 20150311
			// final TaslyPaymentInfo paymentInfo = (TaslyPaymentInfo) order.getPaymentInfos().get(0);
			// header.setPAYMENTDATE(TaslyUtils.convertDateToString(paymentInfo.getIssueDate()));
			header.setPAYMENTDATE(TaslyUtils.convertDateToStringT(order.getIssueDate()));
		}
		if (null != order.getPaymentPointAmount())
		{
			header.setMEMBERSHIPPOINT(BigDecimal.valueOf(order.getPaymentPointAmount().doubleValue()));
		}
		else
		{
			header.setMEMBERSHIPPOINT(BigDecimal.valueOf(0));
		}

		LOG.debug(CO_LogHead + "处理 OrderHeader 完毕,salesOrg is :" + salesOrg + ",paymentDate is: " + header.getPAYMENTDATE()
				+ ",OriginalOrderId is ：" + header.getORIGINALORDERID());
		return header;
	}

	// 2.2 create soldTo

	public ZSTRSDOMSSOLDTO createEccSoldTo(final TaslyOrder order, final String customerOrg)
	{
		final ZSTRSDOMSSOLDTO soldTo = new ZSTRSDOMSSOLDTO();
		// 通过OMS订单上channel_source
		// ecc_customer TMALL 800000
		// ecc_customer JD 800001
		soldTo.setECCSOLDTOID(customerOrg);
		if (null != order.getNickName())
		{
			soldTo.setECCSOLDTONICKNAME(order.getNickName());
		}
		else
		{
			nullFlag = true;
			nullList.add("[orders].nick_name");
		}
		LOG.debug(CO_LogHead + "处理 SoldTo 完毕,nikeName is :" + soldTo.getECCSOLDTONICKNAME());
		return soldTo;
	}

	// 2.3 create billTo

	public ZSTRSDOMSBILLTO createEccBillTo(final TaslyOrder order, final String customerOrg)
	{
		final ZSTRSDOMSBILLTO billTo = new ZSTRSDOMSBILLTO();
		// 通过OMS订单上channel_source
		// ecc_customer TMALL 800000
		// ecc_customer JD 800001
		billTo.setECCBILLTOID(customerOrg);
		billTo.setECCINVOICETYPE(order.getInvoiceType());
		billTo.setECCINVOICETITLE(order.getInvoiceName());
		billTo.setECCTAXPAYERNUMBER(order.getEccTaxpayerNumber());
		billTo.setECCBANKNAME(order.getEccBankName());
		billTo.setECCBANKNUMBER(order.getEccBankNumber());
		billTo.setECCCUSTOMERADDRESS(order.getEccCustomerAddress());
		billTo.setECCCUSTOMERPHONE(order.getEccCustomerPhone());
		billTo.setECCINVOICECONTENT(order.getInvoiceContent());

		LOG.debug(CO_LogHead + "处理 EccBill完毕, INVOICETYPE is :" + billTo.getECCINVOICETYPE());

		return billTo;
	}

	// 2.4 create payer

	public ZSTRSDOMSPAYER createPayer(final TaslyOrder order)
	{
		final ZSTRSDOMSPAYER payer = new ZSTRSDOMSPAYER();
		if (null != order.getPaymentInfos() && order.getPaymentInfos().size() > 0)
		{
			final TaslyPaymentInfo paymentInfo = (TaslyPaymentInfo) order.getPaymentInfos().get(0);
			payer.setECCPAYERID(paymentInfo.getPaymentInfoType());
		}

		LOG.debug(CO_LogHead + "处理 Payer完毕,PAYERID is :" + payer.getECCPAYERID());

		return payer;
	}

	// 2.5 create shipTo
	public ZSTRSDOMSSHIPTO createShipTo(final TaslyOrder order, final String customerOrg)
	{
		final ZSTRSDOMSSHIPTO shipTo = new ZSTRSDOMSSHIPTO();
		// 通过OMS订单上channel_source
		// ecc_customer TMALL 800000
		// ecc_customer JD 800001
		shipTo.setECCSHIPTOID(customerOrg);
		if (null != order.getShippingFirstName())
		{
			if (order.getShippingFirstName().length() > 64)
			{
				shipTo.setECCSHIPTONAME(order.getShippingFirstName().substring(0, 64));
			}
			else
			{
				shipTo.setECCSHIPTONAME(order.getShippingFirstName());
			}
		}
		else
		{
			nullFlag = true;
			nullList.add("[orders].shippingfirstname");
		}
		if (null != order.getShippingAddress() && null != order.getShippingAddress().getCountrySubentity())
		{
			if (order.getShippingAddress().getCountrySubentity().length() > 64)
			{
				shipTo.setECCSHIPTOREGION(order.getShippingAddress().getCountrySubentity().substring(0, 64));
			}
			else
			{
				shipTo.setECCSHIPTOREGION(order.getShippingAddress().getCountrySubentity());
			}
		}
		else
		{
			nullFlag = true;
			nullList.add("[orders].shad_countrySubentity");
		}
		if (null != order.getShippingAddress() && null != order.getShippingAddress().getCityName())
		{
			shipTo.setECCSHIPTOCITY(order.getShippingAddress().getCityName());
		}
		else
		{
			nullFlag = true;
			nullList.add("[orders].shad_cityName");
		}
		if (null != order.getShadCitydistrict())
		{
			shipTo.setECCSHIPTODISTRICT(order.getShadCitydistrict());
		}
		else
		{
			shipTo.setECCSHIPTODISTRICT(SHIPCITY);
		}
		if (null != order.getShippingAddress() && null != order.getShippingAddress().getAddressLine1())
		{
			shipTo.setECCSHIPTOADDRESS(order.getShippingAddress().getAddressLine1());
		}
		else
		{
			nullFlag = true;
			nullList.add("[orders].shad_addressLine1");
		}
		if (null != order.getShippingAddress() && null != order.getShippingAddress().getPostalZone())
		{
			if (order.getShippingAddress().getPostalZone().length() > 12)
			{
				shipTo.setECCSHIPTOZIPCODE(order.getShippingAddress().getPostalZone().substring(0, 12));
			}
			else
			{
				shipTo.setECCSHIPTOZIPCODE(order.getShippingAddress().getPostalZone());
			}
		}
		shipTo.setECCSHIPTOMOB(order.getShadMobile());

		if (null != order.getShippingAddress() && null != order.getShippingAddress().getPhoneNumber())
		{
			if (order.getShippingAddress().getPhoneNumber().length() > 64)
			{
				shipTo.setECCSHIPTOTEL(order.getShippingAddress().getPhoneNumber().substring(0, 64));
			}
			else
			{
				shipTo.setECCSHIPTOTEL(order.getShippingAddress().getPhoneNumber());
			}
		}

		LOG.debug(CO_LogHead + "处理 ShipTo 完毕,SHIPTOREGION is :" + shipTo.getECCSHIPTOREGION() + ",shipCity is ："
				+ shipTo.getECCSHIPTOCITY() + ",shipName is ：" + shipTo.getECCSHIPTONAME() + ",shipDistrict is ："
				+ shipTo.getECCSHIPTODISTRICT() + ",shipAddress is ：" + shipTo.getECCSHIPTOADDRESS());
		return shipTo;
	}

	// 2.6 create delivery
	public ZSTRSDOMSDELIVERY createDelivery(final TaslyOrder order)
	{
		final ZSTRSDOMSDELIVERY delivery = new ZSTRSDOMSDELIVERY();
		if (null != order.getOrderLineQuantities() && order.getOrderLineQuantities().size() > 0)
		{
			final TaslyOrderLineQuantity orderLineQuantity = (TaslyOrderLineQuantity) order.getOrderLineQuantities().get(0);
			if (null != orderLineQuantity.getExpressCode())
			{
				delivery.setECCEXPRESSID(orderLineQuantity.getExpressCode());
				if (null != orderLineQuantity.getExpressCode() && null != expressOrgMap)
				{
					delivery.setECCEXPRESSNAME(expressOrgMap.get(orderLineQuantity.getExpressCode()));
					LOG.debug(CO_LogHead + "处理 Delivery 完毕,ExpressID is " + delivery.getECCEXPRESSID() + ",ExpressName is :"
							+ delivery.getECCEXPRESSNAME());
				}
			}
			else
			{
				nullFlag = true;
				nullList.add("[orderLineQuantities].express_code");
			}
		}
		return delivery;
	}

	// 2.7 create headConditon
	public ZSTRSDOMSHEADCONDTAB createHeadCondtions(final TaslyOrder order)
	{

		ZSTRSDOMSHEADCOND headCondition = new ZSTRSDOMSHEADCOND();
		final ZSTRSDOMSHEADCONDTAB headerConditionTab = new ZSTRSDOMSHEADCONDTAB();
		TaslyShippingAndHandling shipingANDHandling = new TaslyShippingAndHandling();
		// 整单运费
		if (null != order.getShippingAndHandling().getShippingPrice()
				&& null != order.getShippingAndHandling().getShippingPrice().getSubTotal()
				&& order.getShippingAndHandling().getShippingPrice().getSubTotal().getValue().doubleValue() > 0)
		{
			headCondition = new ZSTRSDOMSHEADCOND();
			headCondition.setCONDITIONTYPE(CO_ZDYF);
			shipingANDHandling = (TaslyShippingAndHandling) order.getShippingAndHandling();
			headCondition.setPRICE(BigDecimal.valueOf(shipingANDHandling.getShippingPrice().getSubTotal().getValue().doubleValue()));
			headCondition.setCURRENCY(CO_CURRENCY);
			headerConditionTab.getItem().add(headCondition);
		}
		// 整单折扣

		if (null != order.getDiscountFee() && order.getDiscountFee().doubleValue() > 0)
		{
			headCondition = new ZSTRSDOMSHEADCOND();
			headCondition.setCONDITIONTYPE(CO_ZDZKJE);
			headCondition.setPRICE(BigDecimal.valueOf(order.getDiscountFee().doubleValue()));
			headCondition.setCURRENCY(CO_CURRENCY);
			headerConditionTab.getItem().add(headCondition);
			LOG.debug(CO_LogHead + "处理 HeadCondtions 完毕,Price is :" + headCondition.getPRICE().toString());
		}
		return headerConditionTab;
	}

	// 2.8 create itemTab

	public ZSTRSDOMSSOITEMTAB createItemTab(final TaslyOrder order)
	{
		final ZSTRSDOMSSOITEMTAB itemTab = new ZSTRSDOMSSOITEMTAB();
		ZSTRSDOMSITEMCONDTAB itemPriceConditionTab = new ZSTRSDOMSITEMCONDTAB();
		ZSTRSDOMSSALEORDERITEM item;
		TaslyOrderLine taslyOrderLine = new TaslyOrderLine();
		for (final OrderLine orderLine : order.getOrderLines())
		{
			taslyOrderLine = (TaslyOrderLine) orderLine;
			item = new ZSTRSDOMSSALEORDERITEM();
			item.setOMSITEMID(orderLine.getOrderLineId());
			if (null != orderLine.getSkuId())
			{
				item.setSKUID(orderLine.getSkuId());
			}
			else
			{
				nullFlag = true;
				nullList.add("[orderLines].skuid");
			}
			item.setBASEUNIT(taslyOrderLine.getBaseQuantityUnitCode());
			if (null != taslyOrderLine.getQuantity())
			{
				item.setQUANTITY(BigDecimal.valueOf(taslyOrderLine.getQuantity().getValue()));
			}
			else
			{
				nullFlag = true;
				nullList.add("[orderLines].quantityvalue");
			}
			itemPriceConditionTab = createItemPriceConditionTab(taslyOrderLine);
			item.setTHIRDPARTYPAYMENTID(order.getPaymentNo());
			if (null != taslyOrderLine.getThirdPartyOrderlineId())
			{
				item.setTHIRDPARTYITEMID(taslyOrderLine.getThirdPartyOrderlineId());
			}
			else
			{
				nullFlag = true;
				nullList.add("[orderLines].third_party_orderline_id");
			}
			item.setITEMCONDITIONS(itemPriceConditionTab);
			item.setZFREE(taslyOrderLine.getGiftItemFlag());
			// item.setTHIRDPARTYID(value);
			if (null != taslyOrderLine.getUnitDiscountFee())
			{
				item.setITEMEXTENSION3(String.valueOf(taslyOrderLine.getUnitDiscountFee().doubleValue()));
			}

			LOG.debug(CO_LogHead + "处理 item完毕,QUANTITY is :" + item.getQUANTITY() + ",thirdPartyItemId is： "
					+ item.getTHIRDPARTYITEMID() + ",SKU is ：" + item.getSKUID() + ",BaseUnit is： " + item.getBASEUNIT());

			itemTab.getItem().add(item);
		}

		return itemTab;
	}

	// 2.9 create orderLine price conditon
	public ZSTRSDOMSITEMCONDTAB createItemPriceConditionTab(final TaslyOrderLine taslyOrderLine)
	{

		final ZSTRSDOMSITEMCONDTAB itemPriceConditionTab = new ZSTRSDOMSITEMCONDTAB();
		ZSTRSDOMSITEMCOND itemCondition;
		// 备用item pricing condition
		if (null != taslyOrderLine.getUnitDiscountFee() && taslyOrderLine.getUnitDiscountFee().doubleValue() > 0)
		{
			itemCondition = new ZSTRSDOMSITEMCOND();
			itemCondition.setCURRENCY(CO_CURRENCY);
			itemCondition.setPRICE(BigDecimal.valueOf(taslyOrderLine.getUnitDiscountFee().doubleValue()));
			itemCondition.setCONDITIONTYPE(CO_JGZKJE);

			LOG.debug(CO_LogHead + "处理 PriceConditionTab完毕,DiscountFee is :" + itemCondition.getPRICE());

			itemPriceConditionTab.getItem().add(itemCondition);
		}
		// 单价 Condition Type： ZPR1
		if (null != taslyOrderLine.getUnitPrice() && taslyOrderLine.getUnitPrice().getValue().doubleValue() > 0)
		{
			itemCondition = new ZSTRSDOMSITEMCOND();
			itemCondition.setCURRENCY(CO_CURRENCY);
			itemCondition.setPRICE(BigDecimal.valueOf(taslyOrderLine.getUnitPrice().getValue().doubleValue()));
			itemCondition.setCONDITIONTYPE(CO_ZPR);
			itemPriceConditionTab.getItem().add(itemCondition);

			LOG.debug(CO_LogHead + "处理 PriceConditionTab完毕,UnitPrice is :" + itemCondition.getPRICE());
		}

		return itemPriceConditionTab;
	}

	/**
	 * update OMS replication_status from 'N' to 'Y' after order synicinized from OMS to ECC
	 *
	 * @author vincent.yin
	 * @param omsOrderIds
	 * @return updated failed orderId list
	 */
	public List<String> updateOrderReplicationStatus(final List<String> omsOrderIds)
	{
		LOG.info(CO_LogHead + "开始更新订单同步状态。。。");

		final List<String> updateFailedOrders = new ArrayList<String>();

		boolean status = true;

		if (null != omsOrderIds && omsOrderIds.size() > 0)
		{

			for (final String orderID : omsOrderIds)
			{
				status = updateOrdertReplicationStatus(orderID);
				if (!status)
				{
					updateFailedOrders.add(orderID);
				}
			}
		}

		return updateFailedOrders;
	}

	// update OMS order replication status from 'N' to 'Y';
	private Boolean updateOrdertReplicationStatus(final String orderId)
	{
		// 1. first find the order by order id;
		Boolean status = true;
		LOG.info("input orderID is : " + orderId);
		if (null != orderId)
		{
			try
			{
				orderService.updateOmsOrderReplicationStatus(orderId, REPLICATIONSTATUS_Y);
			}
			catch (final EntityNotFoundException entityException)
			{
				LOG.error("EntityNotFoundException: " + entityException.getLocalizedMessage());
				status = false;
			}
		}
		return status;
	}

	@Required
	public void setOrderService(final OrderDeliveryStatusUpdateService orderService)
	{
		this.orderService = orderService;
	}

	/**
	 * @param taslyOrderLineQuantityService the taslyOrderLineQuantityService to set
	 */
	public void setTaslyOrderLineQuantityService(final TaslyOrderLineQuantityService taslyOrderLineQuantityService)
	{
		this.taslyOrderLineQuantityService = taslyOrderLineQuantityService;
	}

	/**
	 * @param createOrderSoapService the serviceInterface to set
	 */
	public void setCreateOrderSoapService(final SIECCOMSSALESORDERCREATEOUTAsyn createOrderSoapService)
	{
		this.createOrderSoapService = createOrderSoapService;
	}

	@Required
	public void setOrderConverter(final Converter<TaslyOrderData, TaslyOrder> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	/**
	 * @param erpCodeMappingModelToDataConverter the erpCodeMappingModelToDataConverter to set
	 */
	public void setErpCodeMappingModelToDataConverter(
			final Converter<ErpCodeMapping, ErpCodeMappingData> erpCodeMappingModelToDataConverter)
	{
		this.erpCodeMappingModelToDataConverter = erpCodeMappingModelToDataConverter;
	}

	/**
	 * @param erpCodeMappingDataToModelConverter the erpCodeMappingDataToModelConverter to set
	 */
	public void setErpCodeMappingDataToModelConverter(
			final Converter<ErpCodeMappingData, ErpCodeMapping> erpCodeMappingDataToModelConverter)
	{
		this.erpCodeMappingDataToModelConverter = erpCodeMappingDataToModelConverter;
	}


	/**
	 * @param pIUSERNAME the pIUSERNAME to set
	 */
	public static void setPIUSERNAME(final String pIUSERNAME)
	{
		PIUSERNAME = pIUSERNAME;
	}

	/**
	 * @param pIPASSWORD the pIPASSWORD to set
	 */
	public static void setPIPASSWORD(final String pIPASSWORD)
	{
		PIPASSWORD = pIPASSWORD;
	}


	/**
	 * @return the salesOrgMap
	 */
	public HashMap<String, String> getSalesOrgMap()
	{
		return salesOrgMap;
	}

	/**
	 * @param salesOrgMap the salesOrgMap to set
	 */
	public void setSalesOrgMap(final HashMap<String, String> salesOrgMap)
	{
		this.salesOrgMap = salesOrgMap;
	}

	/**
	 * @return the customerOrgMap
	 */
	public HashMap<String, String> getCustomerOrgMap()
	{
		return customerOrgMap;
	}

	/**
	 * @param customerOrgMap the customerOrgMap to set
	 */
	public void setCustomerOrgMap(final HashMap<String, String> customerOrgMap)
	{
		this.customerOrgMap = customerOrgMap;
	}

	@Override
	@Transactional
	public ErpCodeMapping getErpCodeMappingData(final String type, final String sourceCode)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public ErpCodeMapping createOrUpdateErpCodeMapping(final ErpCodeMapping erpCodeMapping)
	{
		// YTODO Auto-generated method stub
		final ErpCodeMappingData erpCodeMappingData = erpCodeMappingModelToDataConverter.convert(erpCodeMapping);

		orderService.createOrUpdateErpCodeMapping(erpCodeMappingData);

		return erpCodeMapping;
	}

	/**
	 * get all the ErpCodeMapping data
	 */

	@Override
	public List<ErpCodeMapping> getAllErpCodeMapping()
	{
		final List<ErpCodeMappingData> erpCodeMappingDataList = orderService.getAllErpCodeMapping();

		LOG.info("erpCodeMapping 记录数量： " + String.valueOf(erpCodeMappingDataList == null ? 0 : erpCodeMappingDataList.size()));

		return converters.convertAll(erpCodeMappingDataList, erpCodeMappingDataToModelConverter);
	}

	/**
	 * fetch all of the data into Map
	 */

	public void setErpCodeMappingMap()
	{

		final List<ErpCodeMapping> erpCodeMappingList = getAllErpCodeMapping();

		if (null != erpCodeMappingList)
		{
			salesOrgMap = new HashMap<String, String>();
			customerOrgMap = new HashMap<String, String>();
			for (final ErpCodeMapping erpCOdeMapping : erpCodeMappingList)
			{

				if (CO_SALESORGTYPE.equals(erpCOdeMapping.getType()))
				{
					salesOrgMap.put(erpCOdeMapping.getSourceCode(), erpCOdeMapping.getTargetCode());
				}

				else if (CO_ECCCUSTOMERTYPE.equals(erpCOdeMapping.getType()))
				{
					customerOrgMap.put(erpCOdeMapping.getSourceCode(), erpCOdeMapping.getTargetCode());
				}
			}
		}
		this.setSalesOrgMap(salesOrgMap);
		this.setCustomerOrgMap(customerOrgMap);
	}

	/**
	 * put all of the express code and name into hashMap
	 */

	public void setExpressMap()
	{
		final List<ExpressData> expressDataList = orderService.getAllExpressData();

		if (null != expressDataList)
		{
			expressOrgMap = new HashMap<String, String>();
			for (final ExpressData expressData : expressDataList)
			{
				expressOrgMap.put(expressData.getCode(), expressData.getName());
			}
		}

	}
}
