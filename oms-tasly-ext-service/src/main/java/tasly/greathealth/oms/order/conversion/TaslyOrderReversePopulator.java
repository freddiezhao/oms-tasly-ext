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
public class TaslyOrderReversePopulator extends AbstractPopulator<TaslyOrder, TaslyOrderData>
{

	@Override
	public void populate(final TaslyOrder source, final TaslyOrderData target) throws ConversionException,
			IllegalArgumentException
	{
		target.setExpress_memo(source.getExpressMemo());
		if (source.getPacking() != null)
		{
			target.setPacking(tasly.greathealth.oms.domain.order.PackingType.valueOf(source.getPacking().toString()));
		}
		target.setNick_name(source.getNickName());
		target.setInvoice_name(source.getInvoiceName());
		target.setInvoice_type(source.getInvoiceType());
		target.setInvoice_content(source.getInvoiceContent());
		if (source.getDeliveryService() != null)
		{
			target.setDelivery_service(tasly.greathealth.oms.domain.order.DeliveryServiceType.valueOf(source.getDeliveryService()
					.toString()));
		}
		target.setDiscount_fee(source.getDiscountFee());
		target.setTotal_price(source.getTotalPrice());
		target.setPayment_point_amount(source.getPaymentPointAmount());

		target.setBuyer_message(source.getBuyerMessage());
		target.setSeller_message(source.getSellerMessage());
		target.setSpecial_memo(source.getSpecialMemo());
		target.setSpecial_memo_reason(source.getSpecial_memo_reason());
		target.setConfirm_received_time(source.getConfirmReceivedTime());
		if (source.getChannelSource() != null)
		{
			target.setChannel_source(tasly.greathealth.oms.domain.order.ChannelSource.valueOf(source.getChannelSource().toString()));
		}
		if (source.getInnerSource() != null)
		{
			target.setInner_source(tasly.greathealth.oms.domain.order.InnerSource.valueOf(source.getInnerSource().toString()));
		}
		if (source.getShippingLockStatus() != null)
		{
			target.setShipping_lock_status(tasly.greathealth.oms.domain.order.ShippingLockStatus.valueOf(source
					.getShippingLockStatus().toString()));
		}
		target.setShad_city(source.getShadCity());
		target.setShad_citydistrict(source.getShadCitydistrict());
		if (source.getShadInfoUpdateStatus() != null)
		{
			target.setShad_info_update_status(tasly.greathealth.oms.domain.order.ShadInfoUpdateStatus.valueOf(source
					.getShadInfoUpdateStatus().toString()));
		}
		if (source.getSpecialMemoUpdateStatus() != null)
		{
			target.setSpecial_memo_update_status(tasly.greathealth.oms.domain.order.SpecialMemoUpdateStatus.valueOf(source
					.getSpecialMemoUpdateStatus().toString()));
		}
		target.setOriginal_order_id(source.getOriginalOrderId());
		if (source.getApproveStatus() != null)
		{
			target.setApprove_status(tasly.greathealth.oms.domain.order.ApproveStatus.valueOf(source.getApproveStatus().toString()));
		}
		target.setPayment_no(source.getPaymentNo());
		target.setShad_mobile(source.getShadMobile());
		target.setReplication_status(source.getReplicationStatus());
		target.setReplication_times(source.getReplicationTimes());
		target.setEcc_taxpayer_number(source.getEccTaxpayerNumber());
		target.setEcc_bank_name(source.getEccBankName());
		target.setEcc_order_id(source.getEccOrderId());
		target.setEcc_bank_number(source.getEccBankNumber());
		target.setEcc_customer_address(source.getEccCustomerAddress());
		target.setEcc_customer_phone(source.getEccCustomerPhone());
		target.setMerchant_tag(source.getMerchantTag());
		target.setOrder_created_time(source.getOrderCreatedTime());
		target.setPayment(source.getPayment());
		// for creation status of order in ECC
		// target.setOrderId(source.getOrderId());
	}
}
