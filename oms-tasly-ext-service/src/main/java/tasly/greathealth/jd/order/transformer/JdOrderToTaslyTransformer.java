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
package tasly.greathealth.jd.order.transformer;

import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
import tasly.greathealth.thirdparty.order.common.TaslyThirdUtils;
import tasly.greathealth.tmall.order.services.impl.OrderContext;

import com.jd.open.api.sdk.domain.order.ItemInfo;
import com.jd.open.api.sdk.domain.order.OrderSearchInfo;


/**
 * @author libin
 */
public class JdOrderToTaslyTransformer implements OrderTransformer<OrderSearchInfo>, OrderConstants
{
	private static final Logger LOG = OmsLoggerFactory.getJdorderlog();

	@Override
	public Order transformOrder(final OrderSearchInfo thirdPartyOrder, final OrderContext orderContext) throws ParseException
	{
		final TaslyOrder taslyOrder = new TaslyOrder();
		final String innerSource = orderContext.getStringProperty(DEFAULT_INNER_SOURCE_KEY);
		final String channelSource = orderContext.getStringProperty(DEFAULT_CHANNEL_SOURCE_KEY);
		final String generatedOrderId = orderContext.getStringProperty(DEFAULT_ORDER_ID_KEY);
		final String orderType = orderContext.getStringProperty(thirdPartyOrder.getOrderId());

		final List<String> locationIds = new ArrayList<>();
		locationIds.add("JD");

		taslyOrder.setMerchantTag(orderType);
		taslyOrder.setLocationIds(locationIds);
		taslyOrder.setOrderId(generatedOrderId);
		taslyOrder.setInnerSource(tasly.greathealth.oms.api.order.dto.InnerSource.valueOf(innerSource));
		taslyOrder.setChannelSource(tasly.greathealth.oms.api.order.dto.ChannelSource.valueOf(channelSource));
		taslyOrder.setUsername(thirdPartyOrder.getConsigneeInfo().getFullname());// 客户姓名
		taslyOrder.setIssueDate(thirdPartyOrder.getPaymentConfirmTime() == null ? new Date() : TaslyThirdUtils
				.getDatebyStr(thirdPartyOrder.getPaymentConfirmTime()));// 外部订单付款时间
		taslyOrder.setOrderCreatedTime(thirdPartyOrder.getOrderStartTime() == null ? new Date() : TaslyThirdUtils
				.getDatebyStr(thirdPartyOrder.getOrderStartTime())); // 外部订单生成时间
		taslyOrder.setCurrencyCode(DEFAULT_CURRENCY_CODE);// Mandatory field in OMS
		taslyOrder.setFirstName(thirdPartyOrder.getConsigneeInfo().getFullname());// Mandatory field in OMS
		taslyOrder.setLastName(thirdPartyOrder.getConsigneeInfo().getFullname());// Mandatory field in OMS
		taslyOrder.setOriginalOrderId(thirdPartyOrder.getOrderId());// Tmall订单号

		taslyOrder.setInvoiceName(orderContext.getStringProperty(JD_INVOICE_TITLE));// 发票抬头
		taslyOrder.setInvoiceType(orderContext.getStringProperty(JD_INVOICE_TYPE));//
		taslyOrder.setInvoiceContent(orderContext.getStringProperty(JD_INVOICE_CONTENT));// 发票内容

		taslyOrder.setShippingFirstName(thirdPartyOrder.getConsigneeInfo().getFullname());// 收货人姓名
		taslyOrder.setShadMobile(thirdPartyOrder.getConsigneeInfo().getMobile());// 电话

		final Address shippingAddress = new Address();// 收货地址
		shippingAddress.setAddressLine1(thirdPartyOrder.getConsigneeInfo().getFullAddress());
		shippingAddress.setCityName(thirdPartyOrder.getConsigneeInfo().getCity());// Mandatory field in OMS
		shippingAddress.setCountryIso3166Alpha2Code(DEFAULT_COUNTRY_ISO3166ALPHA2CODE);// Mandatory field in OMS
		shippingAddress.setPostalZone("jd3000");// Mandatory field in OMS
		shippingAddress.setCountrySubentity(thirdPartyOrder.getConsigneeInfo().getProvince());// 收货人的所在省份
		shippingAddress.setPhoneNumber(thirdPartyOrder.getConsigneeInfo().getMobile());// 收货人电话
		taslyOrder.setShippingAddress(shippingAddress);
		// JD 默认配送方式EXPRESS
		taslyOrder.setDeliveryService(DeliveryServiceType.EXPRESS);// 配送方式

		final String jdDiscountFee = thirdPartyOrder.getSellerDiscount();
		taslyOrder.setDiscountFee(safe2Double(jdDiscountFee));// 促销金额
		taslyOrder.setExpressMemo(thirdPartyOrder.getOrderRemark());// 发货备注信息

		taslyOrder.setPacking(PackingType.PACKING);// packing // 默认值为0未发货，1已发货
		taslyOrder.setNickName(thirdPartyOrder.getConsigneeInfo().getFullname());// 买家昵称
		taslyOrder.setTotalPrice(safe2Double(thirdPartyOrder.getOrderTotalPrice()));// 订单总价
		taslyOrder.setPaymentPointAmount(safe2Double(thirdPartyOrder.getCouponDetailList().get(0).getCouponPrice()));// 积分支付额度,单位：元
		taslyOrder.setBuyerMessage(thirdPartyOrder.getOrderRemark());// 买家说明信息
		taslyOrder.setSellerMessage(thirdPartyOrder.getVenderRemark());// 卖家说明信息
		taslyOrder.setShippingLockStatus(ShippingLockStatus.NON_LOCK);// 发货锁定标记 默认为0
		taslyOrder.setShadCity(thirdPartyOrder.getConsigneeInfo().getCity());// 收货人地址：市
		taslyOrder.setShadCitydistrict(thirdPartyOrder.getConsigneeInfo().getCounty());// 收货人地址：区／县
		taslyOrder.setShadInfoUpdateStatus(ShadInfoUpdateStatus.DEFAULT);// 订单收货人信息修改状态
		taslyOrder.setSpecialMemoUpdateStatus(SpecialMemoUpdateStatus.DEFAULT);// 特殊备注信息修改状态
		taslyOrder.setApproveStatus(ApproveStatus.DEFAULT);// 审核标示
		// JD支付交易号 无，现取订单id
		taslyOrder.setPaymentNo(thirdPartyOrder.getOrderId());
		taslyOrder.setShadMobile(thirdPartyOrder.getConsigneeInfo().getMobile());// 收货人手机号
		taslyOrder.setReplicationStatus(DEFAULT_REPLICATION_STATUS);// ECC同步订单状态
		taslyOrder.setReplicationTimes(DEFAULT_REPLICATION_TIMES);// ECC同步订单次数

		taslyOrder.setShippingFirstName(thirdPartyOrder.getConsigneeInfo().getFullname());// Mandatory field in OMS
		taslyOrder.setShippingLastName(thirdPartyOrder.getConsigneeInfo().getFullname());// Mandatory field in OMS
		taslyOrder.setShippingMethod("03");// Mandatory field in OMS
		taslyOrder.setShippingTaxCategory("FR000000");// Mandatory field in OMS

		final List<ItemInfo> itemList = thirdPartyOrder.getItemInfoList();
		for (int i = 0; i < itemList.size(); i++)
		{
			final ItemInfo itemLine = itemList.get(i);
			// 京东无orderLineId，自造放在了GiftPoint上
			final String orderLineId = itemLine.getGiftPoint();
			// SKU外部ID,OMS内部SKUid
			final String outerSkuId = itemLine.getOuterSkuId();
			// 京东内部SKU的ID
			final String jdSkuId = itemLine.getSkuId();
			final String omsSkuId = StringUtils.isEmpty(outerSkuId) ? jdSkuId : outerSkuId;
			final TaslyOrderLine omsLine = new TaslyOrderLine();
			final String unitCode = orderContext.getStringProperty(omsSkuId);
			if (unitCode == null)
			{
				LOG.warn("Can not find unit code for SKUID[" + omsSkuId + "]");
			}
			omsLine.setBaseQuantityUnitCode(unitCode);
			omsLine.setThirdPartyOrderlineId(orderLineId);
			omsLine.setOrderLineId(generatedOrderId + "_" + i);
			// 京东行级别优惠金额
			omsLine.setUnitDiscountFee(safe2Double(itemLine.getSkuName()));
			// if (orderContext.getProperty(ORDER_LINE_DISCOUNTFEE) instanceof Map)
			// {
			// final Map orderLineDiscountfee = (Map) orderContext.getProperty(ORDER_LINE_DISCOUNTFEE);
			// if (orderLineDiscountfee.containsKey(omsSkuId))
			// {
			// final String couponPrice = (String) orderLineDiscountfee.get(omsSkuId);
			// omsLine.setUnitDiscountFee(safe2Double(couponPrice));
			// }
			// else
			// {
			// omsLine.setUnitDiscountFee(0D);
			// }
			// }

			omsLine.setOrderLineStatus(DEFAULT_BUYER_PAY_HINT);

			final Set<LocationRole> locationRoles = new HashSet<LocationRole>();
			locationRoles.add(LocationRole.SHIPPING);
			omsLine.setLocationRoles(locationRoles);
			omsLine.setSkuId(omsSkuId);// Mandatory field in OMS
			omsLine.setTaxCategory("P0000000");// Mandatory field in OMS
			// todo:实付金额未设置
			// omsLine.setOrderlinePayment(TaslyThirdUtils.safe2Double(tmallLine.getPayment()));// 行级别实付金额

			if (orderContext.getProperty(JUDGE_FLAG) instanceof Map)
			{
				@SuppressWarnings("rawtypes")
				final Map originalOrderInfoMap = (Map) orderContext.getProperty(JUDGE_FLAG);
				if (originalOrderInfoMap.containsKey(orderLineId))
				{
					omsLine.setNote(safe2String(originalOrderInfoMap.get(orderLineId)));
				}
			}

			final Quantity quantity = new Quantity();// quantity value
			quantity.setUnitCode(unitCode);// Mandatory field in OMS
			quantity.setValue(TaslyThirdUtils.safeStr2Integer(itemLine.getItemTotal()));
			omsLine.setQuantity(quantity);

			final Quantity quantityUnassigned = new Quantity();// quantity unassigned value
			quantityUnassigned.setUnitCode(unitCode);// Mandatory field in OMS
			quantityUnassigned.setValue(TaslyThirdUtils.safeStr2Integer(itemLine.getItemTotal()));// Mandatory field in OMS
			omsLine.setQuantityUnassigned(quantityUnassigned);

			final Amount unitTax = new Amount();// Mandatory field in OMS
			unitTax.setCurrencyCode(DEFAULT_CURRENCY_CODE);
			unitTax.setValue(0d);
			omsLine.setUnitTax(unitTax);

			final Double price = safe2Double(itemLine.getJdPrice()) == null ? 0d : safe2Double(itemLine.getJdPrice());
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
			orderLineAttribute.setDescription(StringUtils.defaultString(itemLine.getSkuName())); // + "|"
			// + StringUtils.defaultString(itemLine.));
			omsLine.addOrderLineAttribute(orderLineAttribute);

			// omsLine.addOrderLineQuantity(taslyOrderLineQuantity);
			taslyOrder.addOrderLine(omsLine);
		}

		final TaslyPaymentInfo taslyPaymentInfo = new TaslyPaymentInfo();
		final Address billingAddress = new Address();// Billing address is the same with delivery
		billingAddress.setAddressLine1(thirdPartyOrder.getConsigneeInfo().getFullAddress());
		billingAddress.setCityName(thirdPartyOrder.getConsigneeInfo().getCity());
		billingAddress.setCountryIso3166Alpha2Code(DEFAULT_COUNTRY_ISO3166ALPHA2CODE);
		billingAddress.setPostalZone("jd3000"); // 暂设默认
		taslyPaymentInfo.setBillingAddress(billingAddress);

		taslyPaymentInfo.setAuthUrl(DEFAULT_CIS_AUTH);// Mandatory field in OMS
		// taslyPaymentInfo.setIssueDate(thirdPartyOrder.getPayTime());
		taslyPaymentInfo.setPaymentInfoType(DEFAULT_JD_PAYMENT_INFO_TYPE);// 付款方式 JD
		taslyPaymentInfo.setIssueDate(thirdPartyOrder.getPaymentConfirmTime() == null ? new Date() : TaslyThirdUtils
				.getDatebyStr(thirdPartyOrder.getPaymentConfirmTime()));
		taslyOrder.addPaymentInfo(taslyPaymentInfo);

		final TaslyShippingAndHandling shippingHandling = new TaslyShippingAndHandling();
		shippingHandling.setOrderId(generatedOrderId);
		final Price postFee = new Price();

		final Amount postAmount = new Amount();
		postAmount.setCurrencyCode(DEFAULT_CURRENCY_CODE);
		postAmount.setValue(safe2Double(thirdPartyOrder.getFreightPrice())); // 暂时设0
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

		// if (BooleanUtils.toBoolean(thirdPartyOrder.getHasYfx()))
		// {
		// shippingHandling.setShpr_insurance(Double.parseDouble(thirdPartyOrder.getYfxFee()));// 运费险
		// }
		taslyOrder.setShippingAndHandling(shippingHandling);
		return taslyOrder;
	}

	private Double safe2Double(final String str)
	{
		return StringUtils.isNotBlank(str) && NumberUtils.isNumber(str) ? TaslyThirdUtils.safe2Double(str) : 0d;
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
