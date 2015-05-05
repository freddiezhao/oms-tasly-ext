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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Henter Liu
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportOrderLine extends PropertyAwareEntityDto
{
	private static final long serialVersionUID = -6198435093681135824L;

	private String orderLineId;

	@XmlElement
	private List<ExportOrderLineQuantity> exportOrderLineQuantities = new ArrayList<ExportOrderLineQuantity>();

	private String note;
	private Integer quantityValue;
	private Double unitPriceValue;
	private String thirdPartyOrderlineId;
	private Double unitDiscountFee;
	private String giftItemFlag;
	private String refundStatus;

	@Override
	public String getId()
	{
		return orderLineId;
	}

	public ExportOrderLine()
	{
	}

	protected ExportOrderLine(final Builder<?> builder)
	{
		super(builder);
		setOrderLineId(builder.getOrderLineId());
		setExportOrderLineQuantities(builder.getExportOrderLineQuantities());
		setNote(builder.getNote());
		setQuantityValue(builder.getQuantityValue());
		setUnitPriceValue(builder.getUnitPriceValue());
		setThirdPartyOrderlineId(builder.getThirdPartyOrderlineId());
		setUnitDiscountFee(builder.getUnitDiscountFee());
		setGiftItemFlag(builder.getGiftItemFlag());
		setRefundStatus(builder.getRefundStatus());
	}

	public void addExportOrderLineQuantity(final ExportOrderLineQuantity exportOrderLineQuantity)
	{
		if (this.exportOrderLineQuantities == null)
		{
			this.exportOrderLineQuantities = new ArrayList<ExportOrderLineQuantity>();
		}
		this.exportOrderLineQuantities.add(exportOrderLineQuantity);
	}

	/**
	 * @return the orderLineId
	 */
	public String getOrderLineId()
	{
		return orderLineId;
	}

	/**
	 * @param orderLineId the orderLineId to set
	 */
	public void setOrderLineId(final String orderLineId)
	{
		this.orderLineId = orderLineId;
	}

	/**
	 * @return the note
	 */
	public String getNote()
	{
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(final String note)
	{
		this.note = note;
	}

	/**
	 * @return the quantityValue
	 */
	public Integer getQuantityValue()
	{
		return quantityValue;
	}

	/**
	 * @param quantityValue the quantityValue to set
	 */
	public void setQuantityValue(final Integer quantityValue)
	{
		this.quantityValue = quantityValue;
	}

	/**
	 * @return the unitPriceValue
	 */
	public Double getUnitPriceValue()
	{
		return unitPriceValue;
	}

	/**
	 * @param unitPriceValue the unitPriceValue to set
	 */
	public void setUnitPriceValue(final Double unitPriceValue)
	{
		this.unitPriceValue = unitPriceValue;
	}

	/**
	 * @return the thirdPartyOrderlineId
	 */
	public String getThirdPartyOrderlineId()
	{
		return thirdPartyOrderlineId;
	}

	/**
	 * @param thirdPartyOrderlineId the thirdPartyOrderlineId to set
	 */
	public void setThirdPartyOrderlineId(final String thirdPartyOrderlineId)
	{
		this.thirdPartyOrderlineId = thirdPartyOrderlineId;
	}

	/**
	 * @return the unitDiscountFee
	 */
	public Double getUnitDiscountFee()
	{
		return unitDiscountFee;
	}

	/**
	 * @param unitDiscountFee the unitDiscountFee to set
	 */
	public void setUnitDiscountFee(final Double unitDiscountFee)
	{
		this.unitDiscountFee = unitDiscountFee;
	}

	/**
	 * @return the giftItemFlag
	 */
	public String getGiftItemFlag()
	{
		return giftItemFlag;
	}

	/**
	 * @param giftItemFlag the giftItemFlag to set
	 */
	public void setGiftItemFlag(final String giftItemFlag)
	{
		this.giftItemFlag = giftItemFlag;
	}

	/**
	 * @return the refundStatus
	 */
	public String getRefundStatus()
	{
		return refundStatus;
	}

	/**
	 * @param refundStatus the refundStatus to set
	 */
	public void setRefundStatus(final String refundStatus)
	{
		this.refundStatus = refundStatus;
	}

	/**
	 * @return the orderLineQuantities
	 */
	public List<ExportOrderLineQuantity> getExportOrderLineQuantities()
	{
		return exportOrderLineQuantities;
	}

	/**
	 * @param exportOrderLineQuantities the exportOrderLineQuantities to set
	 */
	public void setExportOrderLineQuantities(final List<ExportOrderLineQuantity> exportOrderLineQuantities)
	{
		this.exportOrderLineQuantities = exportOrderLineQuantities;
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
		private String orderLineId;
		private List<ExportOrderLineQuantity> exportOrderLineQuantities = new ArrayList<ExportOrderLineQuantity>();
		private String note;
		private Integer quantityValue;
		private Double unitPriceValue;
		private String thirdPartyOrderlineId;
		private Double unitDiscountFee;
		private String giftItemFlag;
		private String refundStatus;

		/**
		 * @return the orderLineId
		 */
		private String getOrderLineId()
		{
			return orderLineId;
		}

		/**
		 * @param orderLineId the orderLineId to set
		 */
		public T setOrderLineId(final String orderLineId)
		{
			this.orderLineId = orderLineId;
			return self();
		}

		/**
		 * @return the note
		 */
		public String getNote()
		{
			return note;
		}

		/**
		 * @param note the note to set
		 */
		public T setNote(final String note)
		{
			this.note = note;
			return self();
		}

		/**
		 * @return the quantityValue
		 */
		public Integer getQuantityValue()
		{
			return quantityValue;
		}

		/**
		 * @param quantityValue the quantityValue to set
		 */
		public T setQuantityValue(final Integer quantityValue)
		{
			this.quantityValue = quantityValue;
			return self();
		}

		/**
		 * @return the unitPriceValue
		 */
		public Double getUnitPriceValue()
		{
			return unitPriceValue;
		}

		/**
		 * @param unitPriceValue the unitPriceValue to set
		 */
		public T setUnitPriceValue(final Double unitPriceValue)
		{
			this.unitPriceValue = unitPriceValue;
			return self();
		}

		/**
		 * @return the thirdPartyOrderlineId
		 */
		public String getThirdPartyOrderlineId()
		{
			return thirdPartyOrderlineId;
		}

		/**
		 * @param thirdPartyOrderlineId the thirdPartyOrderlineId to set
		 */
		public T setThirdPartyOrderlineId(final String thirdPartyOrderlineId)
		{
			this.thirdPartyOrderlineId = thirdPartyOrderlineId;
			return self();
		}

		/**
		 * @return the unitDiscountFee
		 */
		public Double getUnitDiscountFee()
		{
			return unitDiscountFee;
		}

		/**
		 * @param unitDiscountFee the unitDiscountFee to set
		 */
		public T setUnitDiscountFee(final Double unitDiscountFee)
		{
			this.unitDiscountFee = unitDiscountFee;
			return self();
		}

		/**
		 * @return the giftItemFlag
		 */
		public String getGiftItemFlag()
		{
			return giftItemFlag;
		}

		/**
		 * @param giftItemFlag the giftItemFlag to set
		 */
		public T setGiftItemFlag(final String giftItemFlag)
		{
			this.giftItemFlag = giftItemFlag;
			return self();
		}

		/**
		 * @return the refundStatus
		 */
		public String getRefundStatus()
		{
			return refundStatus;
		}

		/**
		 * @param refundStatus the refundStatus to set
		 */
		public T setRefundStatus(final String refundStatus)
		{
			this.refundStatus = refundStatus;
			return self();
		}

		/**
		 * @return the exportOrderLineQuantities
		 */
		public List<ExportOrderLineQuantity> getExportOrderLineQuantities()
		{
			return exportOrderLineQuantities;
		}

		/**
		 * @param exportOrderLineQuantities the exportOrderLineQuantities to set
		 */
		public T setExportOrderLineQuantities(final List<ExportOrderLineQuantity> exportOrderLineQuantities)
		{
			this.exportOrderLineQuantities = exportOrderLineQuantities;
			return self();
		}

		@Override
		protected abstract T self();

		public ExportOrderLine build()
		{
			return new ExportOrderLine(this);
		}
	}
}
