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
package tasly.greathealth.oms.order.conversion;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.domain.order.TaslyOrderData;


/**
 *
 */
public class TaslyOrderPopulator extends AbstractPopulator<TaslyOrderData, TaslyOrder>
{

	@Override
	public void populate(final TaslyOrderData source, final TaslyOrder target) throws ConversionException,
	IllegalArgumentException
	{
		target.setExpressMemo(source.getExpress_memo());
		if (source.getPacking() != null)
		{
			target.setPacking(tasly.greathealth.oms.api.order.dto.PackingType.valueOf(source.getPacking().toString()));
		}
		target.setNickName(source.getNick_name());
		target.setInvoiceName(source.getInvoice_name());
		target.setInvoiceType(source.getInvoice_type());
		target.setInvoiceContent(source.getInvoice_content());
		if (source.getDelivery_service() != null)
		{
			target.setDeliveryService(tasly.greathealth.oms.api.order.dto.DeliveryServiceType.valueOf(source.getDelivery_service()
					.toString()));
		}
		target.setDiscountFee(source.getDiscount_fee());
		target.setTotalPrice(source.getTotal_price());
		target.setPaymentPointAmount(source.getPayment_point_amount());

		target.setBuyerMessage(source.getBuyer_message());
		target.setSellerMessage(source.getSeller_message());
		target.setSpecialMemo(source.getSpecial_memo());
		target.setSpecial_memo_reason(source.getSpecial_memo_reason());
		target.setConfirmReceivedTime(source.getConfirm_received_time());
		if (source.getChannel_source() != null)
		{
			target.setChannelSource(tasly.greathealth.oms.api.order.dto.ChannelSource.valueOf(source.getChannel_source().toString()));
		}
		if (source.getInner_source() != null)
		{
			target.setInnerSource(tasly.greathealth.oms.api.order.dto.InnerSource.valueOf(source.getInner_source().toString()));
		}
		if (source.getShipping_lock_status() != null)
		{
			target.setShippingLockStatus(tasly.greathealth.oms.api.order.dto.ShippingLockStatus.valueOf(source
					.getShipping_lock_status().toString()));
		}
		target.setShadCity(source.getShad_city());
		target.setShadCitydistrict(source.getShad_citydistrict());
		if (source.getShad_info_update_status() != null)
		{
			target.setShadInfoUpdateStatus(tasly.greathealth.oms.api.order.dto.ShadInfoUpdateStatus.valueOf(source
					.getShad_info_update_status().toString()));
		}
		if (source.getSpecial_memo_update_status() != null)
		{
			target.setSpecialMemoUpdateStatus(tasly.greathealth.oms.api.order.dto.SpecialMemoUpdateStatus.valueOf(source
					.getSpecial_memo_update_status().toString()));
		}
		target.setOriginalOrderId(source.getOriginal_order_id());
		if (source.getApprove_status() != null)
		{
			target.setApproveStatus(tasly.greathealth.oms.api.order.dto.ApproveStatus.valueOf(source.getApprove_status().toString()));
		}
		target.setPaymentNo(source.getPayment_no());
		target.setShadMobile(source.getShad_mobile());
		target.setReplicationStatus(source.getReplication_status());
		target.setReplicationTimes(source.getReplication_times());
		target.setEccTaxpayerNumber(source.getEcc_taxpayer_number());
		target.setEccBankName(source.getEcc_bank_name());
		target.setEccOrderId(source.getEcc_order_id());
		target.setEccBankNumber(source.getEcc_bank_number());
		target.setEccCustomerAddress(source.getEcc_customer_address());
		target.setEccCustomerPhone(source.getEcc_customer_phone());
		target.setMerchantTag(source.getMerchant_tag());
		target.setOrderCreatedTime(source.getOrder_created_time());
		target.setPayment(source.getPayment());
		// for creation status of order in ECC
		// target.setOrderId(source.getOrderId());
	}
}
