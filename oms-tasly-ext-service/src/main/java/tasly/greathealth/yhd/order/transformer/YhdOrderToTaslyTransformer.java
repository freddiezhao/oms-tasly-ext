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
package tasly.greathealth.yhd.order.transformer;

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
import tasly.greathealth.thirdparty.order.common.TaslyThirdUtils;
import tasly.greathealth.tmall.order.services.impl.OrderContext;

import com.yhd.object.trade.Trade;


/**
 *
 * @author libin
 */
public class YhdOrderToTaslyTransformer implements OrderTransformer<Trade>, OrderConstants
{
	private static final Logger LOG = OmsLoggerFactory.getYhdorderlog();

	@Override
	public Order transformOrder(final Trade thirdPartyOrder, final OrderContext orderContext) throws ParseException
	{
		final TaslyOrder taslyOrder = new TaslyOrder();
		final String innerSource = orderContext.getStringProperty(DEFAULT_INNER_SOURCE_KEY);
		final String channelSource = orderContext.getStringProperty(DEFAULT_CHANNEL_SOURCE_KEY);
		final String generatedOrderId = orderContext.getStringProperty(DEFAULT_ORDER_ID_KEY);
		final String orderType = orderContext.getStringProperty(String.valueOf(thirdPartyOrder.getTid()));

		final List<String> locationIds = new ArrayList<>();
		locationIds.add("YHD");

		taslyOrder.setMerchantTag(orderType);
		taslyOrder.setLocationIds(locationIds);
		taslyOrder.setOrderId(generatedOrderId);
		taslyOrder.setInnerSource(tasly.greathealth.oms.api.order.dto.InnerSource.valueOf(innerSource));
		taslyOrder.setChannelSource(tasly.greathealth.oms.api.order.dto.ChannelSource.valueOf(channelSource));
		taslyOrder.setUsername(thirdPartyOrder.getBuyer_nick());// 客户姓名
		taslyOrder.setIssueDate(thirdPartyOrder.getPay_time() == null ? new Date() : TaslyThirdUtils.getDatebyStr(thirdPartyOrder
				.getPay_time()));// 外部订单生成时间
		taslyOrder.setCurrencyCode(DEFAULT_CURRENCY_CODE);// Mandatory field in OMS
		taslyOrder.setFirstName(thirdPartyOrder.getBuyer_nick());// Mandatory field in OMS
		taslyOrder.setLastName(thirdPartyOrder.getBuyer_nick());// Mandatory field in OMS
		taslyOrder.setOriginalOrderId(String.valueOf(thirdPartyOrder.getTid()));// Tmall订单号

		// taslyOrder.setInvoiceName(thirdPartyOrder.getInvoiceName());// 发票抬头
		// taslyOrder.setInvoiceType("00");//
		// taslyOrder.setInvoiceContent(thirdPartyOrder.getInvoiceType());// 发票内容
		taslyOrder.setShippingFirstName(thirdPartyOrder.getReceiver_name());// 收货人姓名
		taslyOrder.setShadMobile(thirdPartyOrder.getReceiver_mobile());// 电话

		final Address shippingAddress = new Address();// 收货地址
		shippingAddress.setAddressLine1(thirdPartyOrder.getReceiver_address());
		shippingAddress.setCityName(thirdPartyOrder.getReceiver_city());// Mandatory field in OMS
		shippingAddress.setCountryIso3166Alpha2Code(DEFAULT_COUNTRY_ISO3166ALPHA2CODE);// Mandatory field in OMS
		shippingAddress.setPostalZone(thirdPartyOrder.getReceiver_zip());// Mandatory field in OMS
		shippingAddress.setCountrySubentity(thirdPartyOrder.getReceiver_state());// 收货人的所在省份
		shippingAddress.setPhoneNumber(thirdPartyOrder.getReceiver_phone());// 收货人电话
		taslyOrder.setShippingAddress(shippingAddress);

		// YHD暂不提供
		taslyOrder.setDeliveryService(DeliveryServiceType.EXPRESS);// 配送方式

		final String tmallDiscountFee = thirdPartyOrder.getDiscount_fee();
		taslyOrder.setDiscountFee(TaslyThirdUtils.safe2Double(tmallDiscountFee));// 促销金额
		taslyOrder.setExpressMemo(thirdPartyOrder.getSeller_memo());// 发货备注信息

		taslyOrder.setPacking(PackingType.PACKING);// packing // 默认值为0未发货，1已发货
		// todo:YHD昵称超过了OMS定义的35个，暂时采取截串的方法
		taslyOrder.setNickName(thirdPartyOrder.getBuyer_nick().substring(0, 35));// 买家昵称
		taslyOrder.setTotalPrice(TaslyThirdUtils.safe2Double(thirdPartyOrder.getTotal_fee()));// 订单总价
		// YHD暂不提供
		taslyOrder.setPaymentPointAmount(0d);// 积分支付额度,单位：元
		taslyOrder.setBuyerMessage(thirdPartyOrder.getBuyer_message());// 买家说明信息
		taslyOrder.setSellerMessage(thirdPartyOrder.getSeller_memo());// 卖家说明信息
		taslyOrder.setShippingLockStatus(ShippingLockStatus.NON_LOCK);// 发货锁定标记 默认为0
		taslyOrder.setShadCity(thirdPartyOrder.getReceiver_city());// 收货人地址：市
		taslyOrder.setShadCitydistrict(thirdPartyOrder.getReceiver_district());// 收货人地址：区／县
		taslyOrder.setShadInfoUpdateStatus(ShadInfoUpdateStatus.DEFAULT);// 订单收货人信息修改状态
		taslyOrder.setSpecialMemoUpdateStatus(SpecialMemoUpdateStatus.DEFAULT);// 特殊备注信息修改状态
		taslyOrder.setApproveStatus(ApproveStatus.DEFAULT);// 审核标示
		// YHD暂不提供
		taslyOrder.setPaymentNo(thirdPartyOrder.getTid().toString());// 支付交易号
		taslyOrder.setShadMobile(thirdPartyOrder.getReceiver_mobile());// 收货人手机号
		taslyOrder.setReplicationStatus(DEFAULT_REPLICATION_STATUS);// ECC同步订单状态
		taslyOrder.setReplicationTimes(DEFAULT_REPLICATION_TIMES);// ECC同步订单次数

		taslyOrder.setShippingFirstName(thirdPartyOrder.getReceiver_name());// Mandatory field in OMS
		taslyOrder.setShippingLastName(thirdPartyOrder.getReceiver_name());// Mandatory field in OMS
		taslyOrder.setShippingMethod("03");// Mandatory field in OMS
		taslyOrder.setShippingTaxCategory("FR000000");// Mandatory field in OMS

		taslyOrder.setOrderCreatedTime(TaslyThirdUtils.getDatebyStr(thirdPartyOrder.getCreated()));// 订单在第三方平台创建时间
		taslyOrder.setPayment(TaslyThirdUtils.safe2Double(thirdPartyOrder.getPayment()));// 订单级别实付金额

		final List<com.yhd.object.trade.Order> tmallLines = thirdPartyOrder.getOrders().getOrder();
		final int cntLines = tmallLines.size();
		for (int i = 0; i < cntLines; i++)
		{
			final com.yhd.object.trade.Order tmallLine = tmallLines.get(i);
			final Long tmallOid = tmallLine.getOid();
			final String tmallSkuId = tmallLine.getOuter_sku_id();
			final String skuId = StringUtils.isEmpty(tmallSkuId) ? tmallLine.getOuter_iid() : tmallSkuId;
			final TaslyOrderLine omsLine = new TaslyOrderLine();
			final String unitCode = orderContext.getStringProperty(skuId);
			if (unitCode == null)
			{
				LOG.warn("Can not find unit code for SKUID[" + skuId + "]");
			}
			omsLine.setBaseQuantityUnitCode(unitCode);
			omsLine.setThirdPartyOrderlineId(String.valueOf(tmallLine.getOid()));// 淘宝订单行项目ID
			omsLine.setOrderLineId(generatedOrderId + "_" + i);
			omsLine.setUnitDiscountFee(TaslyThirdUtils.safe2Double(tmallLine.getDiscount_fee()));
			omsLine.setOrderLineStatus(DEFAULT_BUYER_PAY_HINT);

			final Set<LocationRole> locationRoles = new HashSet<LocationRole>();
			locationRoles.add(LocationRole.SHIPPING);
			omsLine.setLocationRoles(locationRoles);
			omsLine.setSkuId(skuId);// Mandatory field in OMS
			omsLine.setTaxCategory("P0000000");// Mandatory field in OMS
			omsLine.setOrderlinePayment(TaslyThirdUtils.safe2Double(tmallLine.getPayment()));// 行级别实付金额

			if (orderContext.getProperty(JUDGE_FLAG) instanceof Map)
			{
				@SuppressWarnings("rawtypes")
				final Map originalOrderInfoMap = (Map) orderContext.getProperty(JUDGE_FLAG);
				if (originalOrderInfoMap.containsKey(tmallOid))
				{
					omsLine.setNote(TaslyThirdUtils.safe2String(originalOrderInfoMap.get(tmallOid)));
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

			final Double price = TaslyThirdUtils.safe2Double(tmallLine.getPrice()) == null ? 0d : TaslyThirdUtils
					.safe2Double(tmallLine.getPrice());
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
					+ StringUtils.defaultString(tmallLine.getSku_properties_name()));
			omsLine.addOrderLineAttribute(orderLineAttribute);

			// omsLine.addOrderLineQuantity(taslyOrderLineQuantity);
			taslyOrder.addOrderLine(omsLine);
		}

		final TaslyPaymentInfo taslyPaymentInfo = new TaslyPaymentInfo();
		final Address billingAddress = new Address();// Billing address is the same with delivery
		billingAddress.setAddressLine1(thirdPartyOrder.getReceiver_address());
		billingAddress.setCityName(thirdPartyOrder.getReceiver_city());
		billingAddress.setCountryIso3166Alpha2Code(DEFAULT_COUNTRY_ISO3166ALPHA2CODE);// Mandatory field in OMS TODO:::
		billingAddress.setPostalZone(thirdPartyOrder.getReceiver_zip());
		taslyPaymentInfo.setBillingAddress(billingAddress);

		taslyPaymentInfo.setAuthUrl(DEFAULT_CIS_AUTH);// Mandatory field in OMS
		taslyPaymentInfo.setIssueDate(TaslyThirdUtils.getDatebyStr(thirdPartyOrder.getPay_time()));
		taslyPaymentInfo.setPaymentInfoType(DEFAULT_TMALL_PAYMENT_INFO_TYPE);// 付款方式（支付宝）
		taslyPaymentInfo.setIssueDate(new Date());
		taslyOrder.addPaymentInfo(taslyPaymentInfo);

		final TaslyShippingAndHandling shippingHandling = new TaslyShippingAndHandling();
		shippingHandling.setOrderId(generatedOrderId);
		final Price postFee = new Price();

		final Amount postAmount = new Amount();
		postAmount.setCurrencyCode(DEFAULT_CURRENCY_CODE);
		postAmount.setValue(TaslyThirdUtils.safe2Double(thirdPartyOrder.getPost_fee()));
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

		if (BooleanUtils.toBoolean(thirdPartyOrder.getHas_yfx()))
		{
			shippingHandling.setShpr_insurance(TaslyThirdUtils.safe2Double(thirdPartyOrder.getYfx_fee()));// 运费险
		}
		taslyOrder.setShippingAndHandling(shippingHandling);
		return taslyOrder;
	}
}
