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
package tasly.greathealth.oms.transformer.impl;

import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.api.order.dto.ApproveStatus;
import tasly.greathealth.oms.api.order.dto.DeliveryServiceType;
import tasly.greathealth.oms.api.order.dto.PackingType;
import tasly.greathealth.oms.api.order.dto.ShadInfoUpdateStatus;
import tasly.greathealth.oms.api.order.dto.ShippingLockStatus;
import tasly.greathealth.oms.api.order.dto.SpecialMemoUpdateStatus;
import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;
import tasly.greathealth.oms.api.order.dto.TaslyPaymentInfo;
import tasly.greathealth.oms.api.order.dto.TaslyShippingAndHandling;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.transformer.OrderTransformer;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.tmall.order.services.impl.OrderContext;

import com.taobao.api.domain.Trade;


public class OrderTransformerTmallImpl implements OrderTransformer<Trade>, OrderConstants
{
	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();
	private static final String REGEX = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}---订单受理\\D{1,2}";

	@Override
	public Order transformOrder(final Trade thirdPartyOrder, final OrderContext orderContext)
	{
		// 判断天猫订单的卖家备注是否为规则REGEX，如果是替换成空，主要目的是为了自动审批订单
		String filteredMemo = thirdPartyOrder.getSellerMemo() == null ? null : thirdPartyOrder.getSellerMemo()
				.replaceAll(REGEX, "").replaceAll("\r", "").replaceAll("\n", "").trim();
		if (StringUtils.isBlank(filteredMemo))
		{
			filteredMemo=null;
		}
		thirdPartyOrder.setSellerMemo(filteredMemo);

		final TaslyOrder taslyOrder = new TaslyOrder();
		final String innerSource = orderContext.getStringProperty(DEFAULT_INNER_SOURCE_KEY);
		final String channelSource = orderContext.getStringProperty(DEFAULT_CHANNEL_SOURCE_KEY);
		final String generatedOrderId = orderContext.getStringProperty(DEFAULT_ORDER_ID_KEY);
		final String orderType = orderContext.getStringProperty(String.valueOf(thirdPartyOrder.getTid()));

		final List<String> locationIds = new ArrayList<>();
		locationIds.add("TMALL");// TODO::::hard code here, will comment after the main data imported

		taslyOrder.setMerchantTag(orderType);
		taslyOrder.setLocationIds(locationIds);
		taslyOrder.setOrderId(generatedOrderId);
		taslyOrder.setInnerSource(tasly.greathealth.oms.api.order.dto.InnerSource.valueOf(innerSource));
		taslyOrder.setChannelSource(tasly.greathealth.oms.api.order.dto.ChannelSource.valueOf(channelSource));
		taslyOrder.setUsername(thirdPartyOrder.getBuyerNick());// 客户姓名
		taslyOrder.setIssueDate(thirdPartyOrder.getPayTime() == null ? new Date() : thirdPartyOrder.getPayTime());// 外部订单生成时间
		taslyOrder.setCurrencyCode(DEFAULT_CURRENCY_CODE);// Mandatory field in OMS
		taslyOrder.setFirstName(thirdPartyOrder.getBuyerNick());// Mandatory field in OMS
		taslyOrder.setLastName(thirdPartyOrder.getBuyerNick());// Mandatory field in OMS
		taslyOrder.setOriginalOrderId(String.valueOf(thirdPartyOrder.getTid()));// Tmall订单号

		// taslyOrder.setInvoiceName(thirdPartyOrder.getInvoiceName());// 发票抬头
		// taslyOrder.setInvoiceType("00");//
		// taslyOrder.setInvoiceContent(thirdPartyOrder.getInvoiceType());// 发票内容
		taslyOrder.setShippingFirstName(thirdPartyOrder.getReceiverName());// 收货人姓名
		taslyOrder.setShadMobile(thirdPartyOrder.getReceiverMobile());// 电话

		final Address shippingAddress = new Address();// 收货地址
		shippingAddress.setAddressLine1(thirdPartyOrder.getReceiverAddress());
		shippingAddress.setCityName(thirdPartyOrder.getReceiverCity());// Mandatory field in OMS
		shippingAddress.setCountryIso3166Alpha2Code(DEFAULT_COUNTRY_ISO3166ALPHA2CODE);// Mandatory field in OMS
		shippingAddress.setPostalZone(thirdPartyOrder.getReceiverZip());// Mandatory field in OMS
		shippingAddress.setCountrySubentity(thirdPartyOrder.getReceiverState());// 收货人的所在省份
		shippingAddress.setPhoneNumber(thirdPartyOrder.getReceiverPhone());// 收货人电话
		taslyOrder.setShippingAddress(shippingAddress);

		taslyOrder
		.setDeliveryService(DEFAULT_EMS_NAME.equalsIgnoreCase(thirdPartyOrder.getShippingType()) ? DeliveryServiceType.EMS
				: DeliveryServiceType.EXPRESS);// 配送方式

		final String tmallDiscountFee = thirdPartyOrder.getDiscountFee();
		taslyOrder.setDiscountFee(safe2Double(tmallDiscountFee));// 促销金额
		taslyOrder.setExpressMemo(thirdPartyOrder.getSellerMemo());// 发货备注信息

		taslyOrder.setPacking(PackingType.PACKING);// packing // 默认值为0未发货，1已发货
		taslyOrder.setNickName(thirdPartyOrder.getBuyerNick());// 买家昵称
		taslyOrder.setTotalPrice(safe2Double(thirdPartyOrder.getTotalFee()));// 订单总价
		taslyOrder.setPaymentPointAmount(thirdPartyOrder.getPointFee() == null ? null : (thirdPartyOrder.getPointFee()
				.doubleValue() / 100));// 积分支付额度,单位：元
		taslyOrder.setBuyerMessage(thirdPartyOrder.getBuyerMessage());// 买家说明信息
		taslyOrder.setSellerMessage(thirdPartyOrder.getSellerMemo());// 卖家说明信息
		taslyOrder.setShippingLockStatus(ShippingLockStatus.NON_LOCK);// 发货锁定标记 默认为0
		taslyOrder.setShadCity(thirdPartyOrder.getReceiverCity());// 收货人地址：市
		taslyOrder.setShadCitydistrict(thirdPartyOrder.getReceiverDistrict());// 收货人地址：区／县
		taslyOrder.setShadInfoUpdateStatus(ShadInfoUpdateStatus.DEFAULT);// 订单收货人信息修改状态
		taslyOrder.setSpecialMemoUpdateStatus(SpecialMemoUpdateStatus.DEFAULT);// 特殊备注信息修改状态
		taslyOrder.setApproveStatus(ApproveStatus.DEFAULT);// 审核标示
		taslyOrder.setPaymentNo(thirdPartyOrder.getAlipayNo());// 支付交易号
		taslyOrder.setShadMobile(thirdPartyOrder.getReceiverMobile());// 收货人手机号
		taslyOrder.setReplicationStatus(DEFAULT_REPLICATION_STATUS);// ECC同步订单状态
		taslyOrder.setReplicationTimes(DEFAULT_REPLICATION_TIMES);// ECC同步订单次数

		taslyOrder.setShippingFirstName(thirdPartyOrder.getReceiverName());// Mandatory field in OMS
		taslyOrder.setShippingLastName(thirdPartyOrder.getReceiverName());// Mandatory field in OMS
		taslyOrder.setShippingMethod("03");// Mandatory field in OMS
		taslyOrder.setShippingTaxCategory("FR000000");// Mandatory field in OMS

		taslyOrder.setOrderCreatedTime(thirdPartyOrder.getCreated());// 2015.3.9 add by handong 订单在第三方平台创建时间
		taslyOrder.setPayment(safe2Double(thirdPartyOrder.getPayment()));// 2015.3.26 add by handong 订单级别实付金额

		final List<com.taobao.api.domain.Order> tmallLines = thirdPartyOrder.getOrders();
		final int cntLines = tmallLines.size();
		for (int i = 0; i < cntLines; i++)
		{
			final com.taobao.api.domain.Order tmallLine = tmallLines.get(i);
			final Long tmallOid = tmallLine.getOid();
			final String tmallSkuId = tmallLine.getOuterSkuId();
			final String skuId = StringUtils.isEmpty(tmallSkuId) ? tmallLine.getOuterIid() : tmallSkuId;
			final TaslyOrderLine omsLine = new TaslyOrderLine();
			final String unitCode = orderContext.getStringProperty(skuId);
			if (unitCode == null)
			{
				LOG.warn("Can not find unit code for SKUID[" + skuId + "]");
			}
			omsLine.setBaseQuantityUnitCode(unitCode);
			omsLine.setThirdPartyOrderlineId(String.valueOf(tmallLine.getOid()));// 淘宝订单行项目ID
			omsLine.setOrderLineId(generatedOrderId + "_" + i);
			omsLine.setUnitDiscountFee(safe2Double(tmallLine.getDiscountFee()) - safe2Double(tmallLine.getAdjustFee()));
			omsLine.setOrderLineStatus(DEFAULT_BUYER_PAY_HINT);

			final Set<LocationRole> locationRoles = new HashSet<LocationRole>();
			locationRoles.add(LocationRole.SHIPPING);
			omsLine.setLocationRoles(locationRoles);
			omsLine.setSkuId(skuId);// Mandatory field in OMS
			omsLine.setTaxCategory("P0000000");// Mandatory field in OMS
			omsLine.setOrderlinePayment(safe2Double(tmallLine.getPayment()));// 行级别实付金额
			if (orderContext.getProperty(TMALL_ORIG_ORDER_INFO) instanceof Map)
			{
				@SuppressWarnings("rawtypes")
				final Map originalOrderInfoMap = (Map) orderContext.getProperty(TMALL_ORIG_ORDER_INFO);
				if (originalOrderInfoMap.containsKey(tmallOid))
				{
					@SuppressWarnings("rawtypes")
					final Map m = (Map) originalOrderInfoMap.get(tmallOid);
					omsLine.setNote(safe2String(m.get(TMALL_OUTERID_OUTERSKUID)));
				}
			}

			final Quantity quantity = new Quantity();// quantity value
			quantity.setUnitCode(unitCode);// Mandatory field in OMS
			quantity.setValue(tmallLine.getNum().intValue());
			omsLine.setQuantity(quantity);

			final Quantity quantityUnassigned = new Quantity();// quantity unassigned value
			quantityUnassigned.setUnitCode(unitCode);// Mandatory field in OMS
			quantityUnassigned.setValue(tmallLine.getNum().intValue());// Mandatory field in OMS
			omsLine.setQuantityUnassigned(quantityUnassigned);

			final Amount unitTax = new Amount();// Mandatory field in OMS
			unitTax.setCurrencyCode(DEFAULT_CURRENCY_CODE);
			unitTax.setValue(0d);
			omsLine.setUnitTax(unitTax);

			final Double price = safe2Double(tmallLine.getPrice()) == null ? 0d : Double.parseDouble(tmallLine.getPrice());
			final Amount amount = new Amount();// Mandatory field in OMS
			amount.setCurrencyCode(DEFAULT_CURRENCY_CODE);
			amount.setValue(price);// 商品单价
			omsLine.setUnitPrice(amount);

			if (price == null || price == 0d)
			{
				omsLine.setGiftItemFlag("X");// 赠品标识
			}


			// 增加产品描述
			final OrderLineAttribute orderLineAttribute = new OrderLineAttribute();
			orderLineAttribute.setId(generatedOrderId + "_" + i + "_" + i);
			orderLineAttribute.setDescription(StringUtils.defaultString(tmallLine.getTitle()) + "|"
					+ StringUtils.defaultString(tmallLine.getSkuPropertiesName()));
			omsLine.addOrderLineAttribute(orderLineAttribute);

			// omsLine.addOrderLineQuantity(taslyOrderLineQuantity);
			taslyOrder.addOrderLine(omsLine);
		}

		final TaslyPaymentInfo taslyPaymentInfo = new TaslyPaymentInfo();
		final Address billingAddress = new Address();// Billing address is the same with delivery
		billingAddress.setAddressLine1(thirdPartyOrder.getReceiverAddress());
		billingAddress.setCityName(thirdPartyOrder.getReceiverCity());
		billingAddress.setCountryIso3166Alpha2Code(DEFAULT_COUNTRY_ISO3166ALPHA2CODE);// Mandatory field in OMS TODO:::
		billingAddress.setPostalZone(thirdPartyOrder.getReceiverZip());
		taslyPaymentInfo.setBillingAddress(billingAddress);

		taslyPaymentInfo.setAuthUrl(DEFAULT_CIS_AUTH);// Mandatory field in OMS
		taslyPaymentInfo.setIssueDate(thirdPartyOrder.getPayTime());
		taslyPaymentInfo.setPaymentInfoType(DEFAULT_TMALL_PAYMENT_INFO_TYPE);// 付款方式（支付宝）
		taslyPaymentInfo.setIssueDate(new Date());
		taslyOrder.addPaymentInfo(taslyPaymentInfo);

		final TaslyShippingAndHandling shippingHandling = new TaslyShippingAndHandling();
		shippingHandling.setOrderId(generatedOrderId);
		final Price postFee = new Price();

		final Amount postAmount = new Amount();
		postAmount.setCurrencyCode(DEFAULT_CURRENCY_CODE);
		postAmount.setValue(Double.parseDouble(thirdPartyOrder.getPostFee()));
		postFee.setSubTotal(postAmount);

		final Amount taxAmount = new Amount();
		taxAmount.setCurrencyCode(DEFAULT_CURRENCY_CODE);
		taxAmount.setValue(0d);
		postFee.setTax(taxAmount);

		final Amount taxCommittedAmount = new Amount();
		taxCommittedAmount.setCurrencyCode(DEFAULT_CURRENCY_CODE);
		taxCommittedAmount.setValue(0d);
		postFee.setTaxCommitted(taxCommittedAmount);

		shippingHandling.setShippingPrice(postFee);// 运费

		if (BooleanUtils.toBoolean(thirdPartyOrder.getHasYfx()))
		{
			shippingHandling.setShpr_insurance(Double.parseDouble(thirdPartyOrder.getYfxFee()));// 运费险
		}
		taslyOrder.setShippingAndHandling(shippingHandling);
		return taslyOrder;
	}

	private Double safe2Double(final String str)
	{
		return StringUtils.isNotBlank(str) && NumberUtils.isNumber(str) ? Double.parseDouble(str) : 0d;
	}

	private String safe2String(final Object object)
	{
		if (object == null)
		{
			return "";
		}
		return object.toString();
	}
}
