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
package tasly.greathealth.oms.export.api.order.dto;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import com.hybris.commons.dto.impl.PropertyAwareEntityDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import tasly.greathealth.oms.api.order.dto.ApproveStatus;
import tasly.greathealth.oms.api.order.dto.ChannelSource;
import tasly.greathealth.oms.api.order.dto.DeliveryServiceType;
import tasly.greathealth.oms.api.order.dto.InnerSource;
import tasly.greathealth.oms.api.order.dto.ShippingLockStatus;


/**
 * @author Henter Liu
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportOrder extends PropertyAwareEntityDto
{
	private static final long serialVersionUID = 2847246393456298843L;

	@XmlID
	private String orderId;

	@XmlElement
	private List<ExportOrderLine> exportOrderLines = new ArrayList<ExportOrderLine>();

	private String buyerMessage;
	private ChannelSource channelSource;
	private Date confirmReceivedTime;
	private DeliveryServiceType deliveryService;
	private Double discountFee;
	private String eccBankName;
	private String eccBankNumber;
	private String eccCustomerAddress;
	private String eccCustomerPhone;
	private String eccTaxpayerNumber;
	private InnerSource innerSource;
	private String invoiceContent;
	private String invoiceName;
	private String invoiceType;
	private Date issueDate;
	private String nickName;
	private String originalOrderId;
	private String paymentNo;
	private Double paymentPointAmount;

	// combined multiple paymentInfoType
	private String paymentInfoTypes;
	private String paymentIssueDates;

	private String sellerMessage;
	private String shadCity;
	private String shadCitydistrict;
	private String shadCountrySubentity;
	private String shadMobile;
	private String shadPhoneNumber;
	private String shadPostalZone;
	private String shippingFirstName;
	private Double shprInsurance;
	private Double shprSubTotalValue;
	private Double totalPrice;

	private ApproveStatus approveStatus;
	private ShippingLockStatus shippingLockStatus;
	private String shadAddressLine1;

	@Override
	public String getId()
	{
		return orderId;
	}

	public ExportOrder()
	{
	}

	protected ExportOrder(final Builder<?> builder)
	{
		super(builder);
		setOrderId(builder.getOrderId());
		setBuyerMessage(builder.getBuyerMessage());
		setChannelSource(builder.getChannelSource());
		setConfirmReceivedTime(builder.getConfirmReceivedTime());
		setDeliveryService(builder.getDeliveryService());
		setDiscountFee(builder.getDiscountFee());
		setEccBankName(builder.getEccBankName());
		setEccBankNumber(builder.getEccBankNumber());
		setEccCustomerAddress(builder.getEccCustomerAddress());
		setEccCustomerPhone(builder.getEccCustomerPhone());
		setEccTaxpayerNumber(builder.getEccTaxpayerNumber());
		setInnerSource(builder.getInnerSource());
		setInvoiceContent(builder.getInvoiceContent());
		setInvoiceName(builder.getInvoiceName());
		setInvoiceType(builder.getInvoiceType());
		setIssueDate(builder.getIssueDate());
		setNickName(builder.getNickName());
		setOriginalOrderId(builder.getOriginalOrderId());
		setPaymentNo(builder.getPaymentNo());
		setPaymentPointAmount(builder.getPaymentPointAmount());
		setPaymentInfoTypes(builder.getPaymentInfoTypes());
		setSellerMessage(builder.getSellerMessage());
		setShadCity(builder.getShadCity());
		setShadCitydistrict(builder.getShadCitydistrict());
		setShadCountrySubentity(builder.getShadCountrySubentity());
		setShadMobile(builder.getShadMobile());
		setShadPhoneNumber(builder.getShadPhoneNumber());
		setShadPostalZone(builder.getShadPostalZone());
		setShippingFirstName(builder.getShippingFirstName());
		setShprInsurance(builder.getShprInsurance());
		setShprSubTotalValue(builder.getShprSubTotalValue());
		setApproveStatus(builder.getApproveStatus());
		setShippingLockStatus(builder.getShippingLockStatus());
		setShadAddressLine1(builder.getShadAddressLine1());
	}

	public void addExportOrderLine(final ExportOrderLine line)
	{
		if (this.exportOrderLines == null)
		{
			this.exportOrderLines = new ArrayList<ExportOrderLine>();
		}
		this.exportOrderLines.add(line);
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId()
	{
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	/**
	 * @return the exportOrderLines
	 */
	public List<ExportOrderLine> getExportOrderLines()
	{
		return exportOrderLines;
	}

	/**
	 * @param exportOrderLines the exportOrderLines to set
	 */
	public void setExportOrderLines(final List<ExportOrderLine> exportOrderLines)
	{
		this.exportOrderLines = exportOrderLines;
	}

	/**
	 * @return the buyerMessage
	 */
	public String getBuyerMessage()
	{
		return buyerMessage;
	}

	/**
	 * @param buyerMessage the buyerMessage to set
	 */
	public void setBuyerMessage(final String buyerMessage)
	{
		this.buyerMessage = buyerMessage;
	}

	/**
	 * @return the channelSource
	 */
	public ChannelSource getChannelSource()
	{
		return channelSource;
	}

	/**
	 * @param channelSource the channelSource to set
	 */
	public void setChannelSource(final ChannelSource channelSource)
	{
		this.channelSource = channelSource;
	}

	/**
	 * @return the confirmReceivedTime
	 */
	public Date getConfirmReceivedTime()
	{
		return confirmReceivedTime;
	}

	/**
	 * @param confirmReceivedTime the confirmReceivedTime to set
	 */
	public void setConfirmReceivedTime(final Date confirmReceivedTime)
	{
		this.confirmReceivedTime = confirmReceivedTime;
	}

	/**
	 * @return the deliveryService
	 */
	public DeliveryServiceType getDeliveryService()
	{
		return deliveryService;
	}

	/**
	 * @param deliveryService the deliveryService to set
	 */
	public void setDeliveryService(final DeliveryServiceType deliveryService)
	{
		this.deliveryService = deliveryService;
	}

	/**
	 * @return the discountFee
	 */
	public Double getDiscountFee()
	{
		return discountFee;
	}

	/**
	 * @param discountFee the discountFee to set
	 */
	public void setDiscountFee(final Double discountFee)
	{
		this.discountFee = discountFee;
	}

	/**
	 * @return the eccBankName
	 */
	public String getEccBankName()
	{
		return eccBankName;
	}

	/**
	 * @param eccBankName the eccBankName to set
	 */
	public void setEccBankName(final String eccBankName)
	{
		this.eccBankName = eccBankName;
	}

	/**
	 * @return the eccBankNumber
	 */
	public String getEccBankNumber()
	{
		return eccBankNumber;
	}

	/**
	 * @param eccBankNumber the eccBankNumber to set
	 */
	public void setEccBankNumber(final String eccBankNumber)
	{
		this.eccBankNumber = eccBankNumber;
	}

	/**
	 * @return the eccCustomerAddress
	 */
	public String getEccCustomerAddress()
	{
		return eccCustomerAddress;
	}

	/**
	 * @param eccCustomerAddress the eccCustomerAddress to set
	 */
	public void setEccCustomerAddress(final String eccCustomerAddress)
	{
		this.eccCustomerAddress = eccCustomerAddress;
	}

	/**
	 * @return the eccCustomerPhone
	 */
	public String getEccCustomerPhone()
	{
		return eccCustomerPhone;
	}

	/**
	 * @param eccCustomerPhone the eccCustomerPhone to set
	 */
	public void setEccCustomerPhone(final String eccCustomerPhone)
	{
		this.eccCustomerPhone = eccCustomerPhone;
	}

	/**
	 * @return the eccTaxpayerNumber
	 */
	public String getEccTaxpayerNumber()
	{
		return eccTaxpayerNumber;
	}

	/**
	 * @param eccTaxpayerNumber the eccTaxpayerNumber to set
	 */
	public void setEccTaxpayerNumber(final String eccTaxpayerNumber)
	{
		this.eccTaxpayerNumber = eccTaxpayerNumber;
	}

	/**
	 * @return the innerSource
	 */
	public InnerSource getInnerSource()
	{
		return innerSource;
	}

	/**
	 * @param innerSource the innerSource to set
	 */
	public void setInnerSource(final InnerSource innerSource)
	{
		this.innerSource = innerSource;
	}

	/**
	 * @return the invoiceContent
	 */
	public String getInvoiceContent()
	{
		return invoiceContent;
	}

	/**
	 * @param invoiceContent the invoiceContent to set
	 */
	public void setInvoiceContent(final String invoiceContent)
	{
		this.invoiceContent = invoiceContent;
	}

	/**
	 * @return the invoiceName
	 */
	public String getInvoiceName()
	{
		return invoiceName;
	}

	/**
	 * @param invoiceName the invoiceName to set
	 */
	public void setInvoiceName(final String invoiceName)
	{
		this.invoiceName = invoiceName;
	}

	/**
	 * @return the invoiceType
	 */
	public String getInvoiceType()
	{
		return invoiceType;
	}

	/**
	 * @param invoiceType the invoiceType to set
	 */
	public void setInvoiceType(final String invoiceType)
	{
		this.invoiceType = invoiceType;
	}

	/**
	 * @return the issueDate
	 */
	public Date getIssueDate()
	{
		return issueDate;
	}

	/**
	 * @param issueDate the issueDate to set
	 */
	public void setIssueDate(final Date issueDate)
	{
		this.issueDate = issueDate;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName()
	{
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(final String nickName)
	{
		this.nickName = nickName;
	}

	/**
	 * @return the originalOrderId
	 */
	public String getOriginalOrderId()
	{
		return originalOrderId;
	}

	/**
	 * @param originalOrderId the originalOrderId to set
	 */
	public void setOriginalOrderId(final String originalOrderId)
	{
		this.originalOrderId = originalOrderId;
	}

	/**
	 * @return the paymentNo
	 */
	public String getPaymentNo()
	{
		return paymentNo;
	}

	/**
	 * @param paymentNo the paymentNo to set
	 */
	public void setPaymentNo(final String paymentNo)
	{
		this.paymentNo = paymentNo;
	}

	/**
	 * @return the paymentPointAmount
	 */
	public Double getPaymentPointAmount()
	{
		return paymentPointAmount;
	}

	/**
	 * @param paymentPointAmount the paymentPointAmount to set
	 */
	public void setPaymentPointAmount(final Double paymentPointAmount)
	{
		this.paymentPointAmount = paymentPointAmount;
	}

	/**
	 * @return the paymentInfoTypes
	 */
	public String getPaymentInfoTypes()
	{
		return paymentInfoTypes;
	}

	/**
	 * @param paymentInfoTypes the paymentInfoTypes to set
	 */
	public void setPaymentInfoTypes(final String paymentInfoTypes)
	{
		this.paymentInfoTypes = paymentInfoTypes;
	}

	/**
	 * @return the paymentIssueDates
	 */
	public String getPaymentIssueDates()
	{
		return paymentIssueDates;
	}

	/**
	 * @param paymentIssueDates the paymentIssueDates to set
	 */
	public void setPaymentIssueDates(final String paymentIssueDates)
	{
		this.paymentIssueDates = paymentIssueDates;
	}

	/**
	 * @return the sellerMessage
	 */
	public String getSellerMessage()
	{
		return sellerMessage;
	}

	/**
	 * @param sellerMessage the sellerMessage to set
	 */
	public void setSellerMessage(final String sellerMessage)
	{
		this.sellerMessage = sellerMessage;
	}

	/**
	 * @return the shadCity
	 */
	public String getShadCity()
	{
		return shadCity;
	}

	/**
	 * @param shadCity the shadCity to set
	 */
	public void setShadCity(final String shadCity)
	{
		this.shadCity = shadCity;
	}

	/**
	 * @return the shadCitydistrict
	 */
	public String getShadCitydistrict()
	{
		return shadCitydistrict;
	}

	/**
	 * @param shadCitydistrict the shadCitydistrict to set
	 */
	public void setShadCitydistrict(final String shadCitydistrict)
	{
		this.shadCitydistrict = shadCitydistrict;
	}

	/**
	 * @return the shadCountrySubentity
	 */
	public String getShadCountrySubentity()
	{
		return shadCountrySubentity;
	}

	/**
	 * @param shadCountrySubentity the shadCountrySubentity to set
	 */
	public void setShadCountrySubentity(final String shadCountrySubentity)
	{
		this.shadCountrySubentity = shadCountrySubentity;
	}

	/**
	 * @return the shadMobile
	 */
	public String getShadMobile()
	{
		return shadMobile;
	}

	/**
	 * @param shadMobile the shadMobile to set
	 */
	public void setShadMobile(final String shadMobile)
	{
		this.shadMobile = shadMobile;
	}

	/**
	 * @return the shadPhoneNumber
	 */
	public String getShadPhoneNumber()
	{
		return shadPhoneNumber;
	}

	/**
	 * @param shadPhoneNumber the shadPhoneNumber to set
	 */
	public void setShadPhoneNumber(final String shadPhoneNumber)
	{
		this.shadPhoneNumber = shadPhoneNumber;
	}

	/**
	 * @return the shadPostalZone
	 */
	public String getShadPostalZone()
	{
		return shadPostalZone;
	}

	/**
	 * @param shadPostalZone the shadPostalZone to set
	 */
	public void setShadPostalZone(final String shadPostalZone)
	{
		this.shadPostalZone = shadPostalZone;
	}

	/**
	 * @return the shippingFirstName
	 */
	public String getShippingFirstName()
	{
		return shippingFirstName;
	}

	/**
	 * @param shippingFirstName the shippingFirstName to set
	 */
	public void setShippingFirstName(final String shippingFirstName)
	{
		this.shippingFirstName = shippingFirstName;
	}

	/**
	 * @return the shprInsurance
	 */
	public Double getShprInsurance()
	{
		return shprInsurance;
	}

	/**
	 * @param shprInsurance the shprInsurance to set
	 */
	public void setShprInsurance(final Double shprInsurance)
	{
		this.shprInsurance = shprInsurance;
	}

	/**
	 * @return the shprSubTotalValue
	 */
	public Double getShprSubTotalValue()
	{
		return shprSubTotalValue;
	}

	/**
	 * @param shprSubTotalValue the shprSubTotalValue to set
	 */
	public void setShprSubTotalValue(final Double shprSubTotalValue)
	{
		this.shprSubTotalValue = shprSubTotalValue;
	}

	/**
	 * @return the totalPrice
	 */
	public Double getTotalPrice()
	{
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(final Double totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	/**
	 * @return the approveStatus
	 */
	public ApproveStatus getApproveStatus()
	{
		return approveStatus;
	}

	/**
	 * @param approveStatus the approveStatus to set
	 */
	public void setApproveStatus(final ApproveStatus approveStatus)
	{
		this.approveStatus = approveStatus;
	}

	/**
	 * @return the shippingLockStatus
	 */
	public ShippingLockStatus getShippingLockStatus()
	{
		return shippingLockStatus;
	}

	/**
	 * @param shippingLockStatus the shippingLockStatus to set
	 */
	public void setShippingLockStatus(final ShippingLockStatus shippingLockStatus)
	{
		this.shippingLockStatus = shippingLockStatus;
	}

	/**
	 * @return the shadAddressLine1
	 */
	public String getShadAddressLine1()
	{
		return shadAddressLine1;
	}

	/**
	 * @param shadAddressLine1 the shadAddressLine1 to set
	 */
	public void setShadAddressLine1(final String shadAddressLine1)
	{
		this.shadAddressLine1 = shadAddressLine1;
	}

	@Override
	public String toString()
	{
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

	private static class Builder2 extends Builder<Builder2>
	{
		@Override
		protected Builder2 self()
		{
			return this;
		}
	}

	public static Builder<?> builder()
	{
		return new Builder2();
	}

	public abstract static class Builder<T extends Builder<T>> extends PropertyAwareEntityDto.Builder<T>
	{
		private String orderId;
		private List<ExportOrderLine> exportOrderLines = new ArrayList<ExportOrderLine>();
		private String buyerMessage;
		private ChannelSource channelSource;
		private Date confirmReceivedTime;
		private DeliveryServiceType deliveryService;
		private Double discountFee;
		private String eccBankName;
		private String eccBankNumber;
		private String eccCustomerAddress;
		private String eccCustomerPhone;
		private String eccTaxpayerNumber;
		private InnerSource innerSource;
		private String invoiceContent;
		private String invoiceName;
		private String invoiceType;
		private Date issueDate;
		private String nickName;
		private String originalOrderId;
		private String paymentNo;
		private Double paymentPointAmount;

		// combined multiple paymentInfoType
		private String paymentInfoTypes;
		private String paymentIssueDates;

		private String sellerMessage;
		private String shadCity;
		private String shadCitydistrict;
		private String shadCountrySubentity;
		private String shadMobile;
		private String shadPhoneNumber;
		private String shadPostalZone;
		private String shippingFirstName;
		private Double shprInsurance;
		private Double shprSubTotalValue;
		private Double totalPrice;

		private ApproveStatus approveStatus;
		private ShippingLockStatus shippingLockStatus;
		private String shadAddressLine1;

		/**
		 * @return the orderId
		 */
		public String getOrderId()
		{
			return orderId;
		}

		/**
		 * @param orderId the orderId to set
		 */
		public T setOrderId(final String orderId)
		{
			this.orderId = orderId;
			return self();
		}

		/**
		 * @return the exportOrderLines
		 */
		public List<ExportOrderLine> getExportOrderLines()
		{
			return exportOrderLines;
		}

		/**
		 * @param exportOrderLines the exportOrderLines to set
		 */
		public T setExportOrderLines(final List<ExportOrderLine> exportOrderLines)
		{
			this.exportOrderLines = exportOrderLines;
			return self();
		}

		/**
		 * @return the buyerMessage
		 */
		public String getBuyerMessage()
		{
			return buyerMessage;
		}

		/**
		 * @param buyerMessage the buyerMessage to set
		 */
		public T setBuyerMessage(final String buyerMessage)
		{
			this.buyerMessage = buyerMessage;
			return self();
		}

		/**
		 * @return the channelSource
		 */
		public ChannelSource getChannelSource()
		{
			return channelSource;
		}

		/**
		 * @param channelSource the channelSource to set
		 */
		public T setChannelSource(final ChannelSource channelSource)
		{
			this.channelSource = channelSource;
			return self();
		}

		/**
		 * @return the confirmReceivedTime
		 */
		public Date getConfirmReceivedTime()
		{
			return confirmReceivedTime;
		}

		/**
		 * @param confirmReceivedTime the confirmReceivedTime to set
		 */
		public T setConfirmReceivedTime(final Date confirmReceivedTime)
		{
			this.confirmReceivedTime = confirmReceivedTime;
			return self();
		}

		/**
		 * @return the deliveryService
		 */
		public DeliveryServiceType getDeliveryService()
		{
			return deliveryService;
		}

		/**
		 * @param deliveryService the deliveryService to set
		 */
		public T setDeliveryService(final DeliveryServiceType deliveryService)
		{
			this.deliveryService = deliveryService;
			return self();
		}

		/**
		 * @return the discountFee
		 */
		public Double getDiscountFee()
		{
			return discountFee;
		}

		/**
		 * @param discountFee the discountFee to set
		 */
		public T setDiscountFee(final Double discountFee)
		{
			this.discountFee = discountFee;
			return self();
		}

		/**
		 * @return the eccBankName
		 */
		public String getEccBankName()
		{
			return eccBankName;
		}

		/**
		 * @param eccBankName the eccBankName to set
		 */
		public T setEccBankName(final String eccBankName)
		{
			this.eccBankName = eccBankName;
			return self();
		}

		/**
		 * @return the eccBankNumber
		 */
		public String getEccBankNumber()
		{
			return eccBankNumber;
		}

		/**
		 * @param eccBankNumber the eccBankNumber to set
		 */
		public T setEccBankNumber(final String eccBankNumber)
		{
			this.eccBankNumber = eccBankNumber;
			return self();
		}

		/**
		 * @return the eccCustomerAddress
		 */
		public String getEccCustomerAddress()
		{
			return eccCustomerAddress;
		}

		/**
		 * @param eccCustomerAddress the eccCustomerAddress to set
		 */
		public T setEccCustomerAddress(final String eccCustomerAddress)
		{
			this.eccCustomerAddress = eccCustomerAddress;
			return self();
		}

		/**
		 * @return the eccCustomerPhone
		 */
		public String getEccCustomerPhone()
		{
			return eccCustomerPhone;
		}

		/**
		 * @param eccCustomerPhone the eccCustomerPhone to set
		 */
		public T setEccCustomerPhone(final String eccCustomerPhone)
		{
			this.eccCustomerPhone = eccCustomerPhone;
			return self();
		}

		/**
		 * @return the eccTaxpayerNumber
		 */
		public String getEccTaxpayerNumber()
		{
			return eccTaxpayerNumber;
		}

		/**
		 * @param eccTaxpayerNumber the eccTaxpayerNumber to set
		 */
		public T setEccTaxpayerNumber(final String eccTaxpayerNumber)
		{
			this.eccTaxpayerNumber = eccTaxpayerNumber;
			return self();
		}

		/**
		 * @return the innerSource
		 */
		public InnerSource getInnerSource()
		{
			return innerSource;
		}

		/**
		 * @param innerSource the innerSource to set
		 */
		public T setInnerSource(final InnerSource innerSource)
		{
			this.innerSource = innerSource;
			return self();
		}

		/**
		 * @return the invoiceContent
		 */
		public String getInvoiceContent()
		{
			return invoiceContent;
		}

		/**
		 * @param invoiceContent the invoiceContent to set
		 */
		public T setInvoiceContent(final String invoiceContent)
		{
			this.invoiceContent = invoiceContent;
			return self();
		}

		/**
		 * @return the invoiceName
		 */
		public String getInvoiceName()
		{
			return invoiceName;
		}

		/**
		 * @param invoiceName the invoiceName to set
		 */
		public T setInvoiceName(final String invoiceName)
		{
			this.invoiceName = invoiceName;
			return self();
		}

		/**
		 * @return the invoiceType
		 */
		public String getInvoiceType()
		{
			return invoiceType;
		}

		/**
		 * @param invoiceType the invoiceType to set
		 */
		public T setInvoiceType(final String invoiceType)
		{
			this.invoiceType = invoiceType;
			return self();
		}

		/**
		 * @return the issueDate
		 */
		public Date getIssueDate()
		{
			return issueDate;
		}

		/**
		 * @param issueDate the issueDate to set
		 */
		public T setIssueDate(final Date issueDate)
		{
			this.issueDate = issueDate;
			return self();
		}

		/**
		 * @return the nickName
		 */
		public String getNickName()
		{
			return nickName;
		}

		/**
		 * @param nickName the nickName to set
		 */
		public T setNickName(final String nickName)
		{
			this.nickName = nickName;
			return self();
		}

		/**
		 * @return the originalOrderId
		 */
		public String getOriginalOrderId()
		{
			return originalOrderId;
		}

		/**
		 * @param originalOrderId the originalOrderId to set
		 */
		public T setOriginalOrderId(final String originalOrderId)
		{
			this.originalOrderId = originalOrderId;
			return self();
		}

		/**
		 * @return the paymentNo
		 */
		public String getPaymentNo()
		{
			return paymentNo;
		}

		/**
		 * @param paymentNo the paymentNo to set
		 */
		public T setPaymentNo(final String paymentNo)
		{
			this.paymentNo = paymentNo;
			return self();
		}

		/**
		 * @return the paymentPointAmount
		 */
		public Double getPaymentPointAmount()
		{
			return paymentPointAmount;
		}

		/**
		 * @param paymentPointAmount the paymentPointAmount to set
		 */
		public T setPaymentPointAmount(final Double paymentPointAmount)
		{
			this.paymentPointAmount = paymentPointAmount;
			return self();
		}

		/**
		 * @return the paymentInfoTypes
		 */
		public String getPaymentInfoTypes()
		{
			return paymentInfoTypes;
		}

		/**
		 * @param paymentInfoTypes the paymentInfoTypes to set
		 */
		public T setPaymentInfoTypes(final String paymentInfoTypes)
		{
			this.paymentInfoTypes = paymentInfoTypes;
			return self();
		}

		/**
		 * @return the paymentIssueDates
		 */
		public String getPaymentIssueDates()
		{
			return paymentIssueDates;
		}

		/**
		 * @param paymentIssueDates the paymentIssueDates to set
		 */
		public T setPaymentIssueDates(final String paymentIssueDates)
		{
			this.paymentIssueDates = paymentIssueDates;
			return self();
		}

		/**
		 * @return the sellerMessage
		 */
		public String getSellerMessage()
		{
			return sellerMessage;
		}

		/**
		 * @param sellerMessage the sellerMessage to set
		 */
		public T setSellerMessage(final String sellerMessage)
		{
			this.sellerMessage = sellerMessage;
			return self();
		}

		/**
		 * @return the shadCity
		 */
		public String getShadCity()
		{
			return shadCity;
		}

		/**
		 * @param shadCity the shadCity to set
		 */
		public T setShadCity(final String shadCity)
		{
			this.shadCity = shadCity;
			return self();
		}

		/**
		 * @return the shadCitydistrict
		 */
		public String getShadCitydistrict()
		{
			return shadCitydistrict;
		}

		/**
		 * @param shadCitydistrict the shadCitydistrict to set
		 */
		public T setShadCitydistrict(final String shadCitydistrict)
		{
			this.shadCitydistrict = shadCitydistrict;
			return self();
		}

		/**
		 * @return the shadCountrySubentity
		 */
		public String getShadCountrySubentity()
		{
			return shadCountrySubentity;
		}

		/**
		 * @param shadCountrySubentity the shadCountrySubentity to set
		 */
		public T setShadCountrySubentity(final String shadCountrySubentity)
		{
			this.shadCountrySubentity = shadCountrySubentity;
			return self();
		}

		/**
		 * @return the shadMobile
		 */
		public String getShadMobile()
		{
			return shadMobile;
		}

		/**
		 * @param shadMobile the shadMobile to set
		 */
		public T setShadMobile(final String shadMobile)
		{
			this.shadMobile = shadMobile;
			return self();
		}

		/**
		 * @return the shadPhoneNumber
		 */
		public String getShadPhoneNumber()
		{
			return shadPhoneNumber;
		}

		/**
		 * @param shadPhoneNumber the shadPhoneNumber to set
		 */
		public T setShadPhoneNumber(final String shadPhoneNumber)
		{
			this.shadPhoneNumber = shadPhoneNumber;
			return self();
		}

		/**
		 * @return the shadPostalZone
		 */
		public String getShadPostalZone()
		{
			return shadPostalZone;
		}

		/**
		 * @param shadPostalZone the shadPostalZone to set
		 */
		public T setShadPostalZone(final String shadPostalZone)
		{
			this.shadPostalZone = shadPostalZone;
			return self();
		}

		/**
		 * @return the shippingFirstName
		 */
		public String getShippingFirstName()
		{
			return shippingFirstName;
		}

		/**
		 * @param shippingFirstName the shippingFirstName to set
		 */
		public T setShippingFirstName(final String shippingFirstName)
		{
			this.shippingFirstName = shippingFirstName;
			return self();
		}

		/**
		 * @return the shprInsurance
		 */
		public Double getShprInsurance()
		{
			return shprInsurance;
		}

		/**
		 * @param shprInsurance the shprInsurance to set
		 */
		public T setShprInsurance(final Double shprInsurance)
		{
			this.shprInsurance = shprInsurance;
			return self();
		}

		/**
		 * @return the shprSubTotalValue
		 */
		public Double getShprSubTotalValue()
		{
			return shprSubTotalValue;
		}

		/**
		 * @param shprSubTotalValue the shprSubTotalValue to set
		 */
		public T setShprSubTotalValue(final Double shprSubTotalValue)
		{
			this.shprSubTotalValue = shprSubTotalValue;
			return self();
		}

		/**
		 * @return the totalPrice
		 */
		public Double getTotalPrice()
		{
			return totalPrice;
		}

		/**
		 * @param totalPrice the totalPrice to set
		 */
		public T setTotalPrice(final Double totalPrice)
		{
			this.totalPrice = totalPrice;
			return self();
		}

		/**
		 * @return the approveStatus
		 */
		public ApproveStatus getApproveStatus()
		{
			return approveStatus;
		}

		/**
		 * @param approveStatus the approveStatus to set
		 */
		public T setApproveStatus(final ApproveStatus approveStatus)
		{
			this.approveStatus = approveStatus;
			return self();
		}

		/**
		 * @return the shippingLockStatus
		 */
		public ShippingLockStatus getShippingLockStatus()
		{
			return shippingLockStatus;
		}

		/**
		 * @param shippingLockStatus the shippingLockStatus to set
		 */
		public T setShippingLockStatus(final ShippingLockStatus shippingLockStatus)
		{
			this.shippingLockStatus = shippingLockStatus;
			return self();
		}

		/**
		 * @return the shadAddressLine1
		 */
		public String getShadAddressLine1()
		{
			return shadAddressLine1;
		}

		/**
		 * @param shadAddressLine1 the shadAddressLine1 to set
		 */
		public T setShadAddressLine1(final String shadAddressLine1)
		{
			this.shadAddressLine1 = shadAddressLine1;
			return self();
		}

		@Override
		protected abstract T self();

		public ExportOrder build()
		{
			return new ExportOrder(this);
		}
	}
}
