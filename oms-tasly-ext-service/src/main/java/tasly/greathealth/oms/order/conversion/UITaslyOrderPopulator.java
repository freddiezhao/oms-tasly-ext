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
import com.hybris.oms.ui.api.order.UIOrder;

import org.slf4j.Logger;

import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.log.OmsLoggerFactory;


public class UITaslyOrderPopulator extends AbstractPopulator<TaslyOrderData, UIOrder>
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();

	@Override
	public void populate(final TaslyOrderData source, final UIOrder target) throws ConversionException
	{

		target.setExpressMemo(source.getExpress_memo());
		if (source.getPacking() != null)
		{
			target.setPacking(source.getPacking().toString());
		}
		target.setNickName(source.getNick_name());
		target.setInvoiceName(source.getInvoice_name());
		target.setInvoiceType(source.getInvoice_type());
		target.setInvoiceContent(source.getInvoice_content());
		if (source.getDelivery_service() != null)
		{
			target.setDeliveryService(source.getDelivery_service().toString());
		}
		target.setDiscountFee(source.getDiscount_fee());
		target.setTotalPrice(source.getTotal_price());
		target.setPaymentPointAmount(source.getPayment_point_amount());
		target.setBuyerMessage(source.getBuyer_message());
		target.setSellerMessage(source.getSeller_message());
		target.setSpecialMemo(source.getSpecial_memo());
		target.setSpecialMemoReason(source.getSpecial_memo_reason());

		target.setConfirmReceivedTime(source.getConfirm_received_time());

		if (source.getChannel_source() != null)
		{
			target.setChannelSource(source.getChannel_source().toString());
		}

		if (source.getInner_source() != null)
		{
			target.setInnerSource(source.getInner_source().toString());
		}

		if (source.getShipping_lock_status() != null)
		{
			target.setShippingLockStatus(source.getShipping_lock_status().toString());
		}

		target.setShadCity(source.getShad_city());
		target.setShadCitydistrict(source.getShad_citydistrict());

		if (source.getShad_info_update_status() != null)
		{
			target.setShadInfoUpdateStatus(source.getShad_info_update_status().toString());
		}

		if (source.getSpecial_memo_update_status() != null)
		{
			target.setSpecialMemoUpdateStatus(source.getSpecial_memo_update_status().toString());
		}

		target.setOriginalOrderId(source.getOriginal_order_id());

		if (source.getApprove_status() != null)
		{
			target.setApproveStatus(source.getApprove_status().toString());
		}

		target.setPaymentNo(source.getPayment_no());
		target.setShadMobile(source.getShad_mobile());
		target.setReplicationStatus(source.getReplication_status());
		target.setReplicationTimes(source.getReplication_times());
		target.setEccTaxpayerNumber(source.getEcc_bank_name());
		target.setEccBankNumber(source.getEcc_bank_number());
		target.setEccCustomerAddress(source.getEcc_customer_address());
		target.setEccCustomerPhone(source.getEcc_customer_phone());
		target.setEccOrderId(source.getEcc_order_id());
		target.setEccModificationStatus(source.getEcc_modification_status());
	}

}
