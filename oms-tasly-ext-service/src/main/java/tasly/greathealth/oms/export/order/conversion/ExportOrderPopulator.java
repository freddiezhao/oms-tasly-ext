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
package tasly.greathealth.oms.export.order.conversion;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.order.dto.ApproveStatus;
import tasly.greathealth.oms.api.order.dto.ChannelSource;
import tasly.greathealth.oms.api.order.dto.DeliveryServiceType;
import tasly.greathealth.oms.api.order.dto.InnerSource;
import tasly.greathealth.oms.api.order.dto.ShippingLockStatus;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyPaymentInfoData;
import tasly.greathealth.oms.domain.order.TaslyShippingAndHandlingData;
import tasly.greathealth.oms.export.api.order.dto.ExportOrder;
import tasly.greathealth.oms.export.api.order.dto.ExportOrderLine;


/**
 * @author Henter Liu
 */
public class ExportOrderPopulator extends AbstractPopulator<TaslyOrderData, ExportOrder>
{
	private Converters converters;
	private Converter<OrderLineData, ExportOrderLine> exportOrderLineConverter;

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setExportOrderLineConverter(final Converter<OrderLineData, ExportOrderLine> exportOrderLineConverter)
	{
		this.exportOrderLineConverter = exportOrderLineConverter;
	}

	@Override
	public void populate(final TaslyOrderData source, final ExportOrder target)
			throws ConversionException, IllegalArgumentException
	{
		target.setOrderId(source.getOrderId());
		target.setBuyerMessage(source.getBuyer_message());
		target.setChannelSource(ChannelSource.valueOf(source.getChannel_source().toString()));
		target.setConfirmReceivedTime(source.getConfirm_received_time());
		target.setDeliveryService(DeliveryServiceType.valueOf(source.getDelivery_service().toString()));
		target.setDiscountFee(source.getDiscount_fee());
		target.setEccBankName(source.getEcc_bank_name());
		target.setEccBankNumber(source.getEcc_bank_number());
		target.setEccCustomerAddress(source.getEcc_customer_address());
		target.setEccCustomerPhone(source.getEcc_customer_phone());
		target.setEccTaxpayerNumber(source.getEcc_taxpayer_number());
		target.setInnerSource(InnerSource.valueOf(source.getInner_source().toString()));
		target.setInvoiceContent(source.getInvoice_content());
		target.setInvoiceName(source.getInvoice_name());
		target.setInvoiceType(source.getInvoice_type());
		target.setIssueDate(source.getIssueDate());
		target.setNickName(source.getNick_name());
		target.setOriginalOrderId(source.getOriginal_order_id());
		target.setPaymentNo(source.getPayment_no());
		target.setPaymentPointAmount(source.getPayment_point_amount());
		final List<PaymentInfoData> paymentInfos = source.getPaymentInfos();
		final StringBuffer sb1 = new StringBuffer();
		final StringBuffer sb2 = new StringBuffer();
		for (final PaymentInfoData paymentInfoData : paymentInfos)
		{
			if (paymentInfoData instanceof TaslyPaymentInfoData)
			{
				sb1.append(paymentInfoData.getPaymentInfoType());
				sb1.append(';');
				sb2.append(((TaslyPaymentInfoData) paymentInfoData).getIssuedate());
				sb2.append(';');
			}
		}
		target.setPaymentInfoTypes(sb1.toString());
		target.setPaymentIssueDates(sb2.toString());
		target.setSellerMessage(source.getSeller_message());
		target.setShadCity(source.getShad_city());
		target.setShadCitydistrict(source.getShad_citydistrict());
		target.setShadCountrySubentity(source.getShippingAddress().getCountrySubentity());
		target.setShadMobile(source.getShad_mobile());
		target.setShadPhoneNumber(source.getShippingAddress().getPhoneNumber());
		target.setShadPostalZone(source.getShippingAddress().getPostalZone());
		target.setShippingFirstName(source.getShippingFirstName());
		target.setShprInsurance(((TaslyShippingAndHandlingData) source.getShippingAndHandling()).getShpr_insurance());
		target.setShprSubTotalValue(source.getShippingAndHandling().getShippingPrice().getSubTotalValue());
		target.setTotalPrice(source.getTotal_price());
		target.setApproveStatus(ApproveStatus.valueOf(source.getApprove_status().toString()));
		target.setShippingLockStatus(ShippingLockStatus.valueOf(source.getShipping_lock_status().toString()));
		target.setShadAddressLine1(source.getShippingAddress().getAddressLine1());

		populateOrderLines(source, target);
	}

	protected void populateOrderLines(final OrderData source, final ExportOrder target)
	{
		target.setExportOrderLines(this.converters.convertAll(source.getOrderLines(), this.exportOrderLineConverter));
	}
}
