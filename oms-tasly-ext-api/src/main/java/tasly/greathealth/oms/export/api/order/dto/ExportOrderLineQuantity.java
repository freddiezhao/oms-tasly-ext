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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Henter Liu
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportOrderLineQuantity extends PropertyAwareEntityDto
{
	private static final long serialVersionUID = 19999424203981798L;

	private Long olqId;

	private String status;
	private String expressOrderId;
	private String expressCode;

	@Override
	public String getId()
	{
		return String.valueOf(olqId);
	}

	public ExportOrderLineQuantity()
	{
	}

	protected ExportOrderLineQuantity(final Builder<?> builder)
	{
		super(builder);
		setOlqId(builder.getOlqId());
		setStatus(builder.getStatus());
		setExpressOrderId(builder.getExpressOrderId());
		setExpressCode(builder.getExpressCode());
	}

	/**
	 * @return the olqId
	 */
	public Long getOlqId()
	{
		return olqId;
	}

	/**
	 * @param olqId the olqId to set
	 */
	public void setOlqId(final Long olqId)
	{
		this.olqId = olqId;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the expressOrderId
	 */
	public String getExpressOrderId()
	{
		return expressOrderId;
	}

	/**
	 * @param expressOrderId the expressOrderId to set
	 */
	public void setExpressOrderId(final String expressOrderId)
	{
		this.expressOrderId = expressOrderId;
	}

	/**
	 * @return the expressCode
	 */
	public String getExpressCode()
	{
		return expressCode;
	}

	/**
	 * @param expressCode the expressCode to set
	 */
	public void setExpressCode(final String expressCode)
	{
		this.expressCode = expressCode;
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
		private Long olqId;
		private String status;
		private String expressOrderId;
		private String expressCode;

		/**
		 * @return the olqId
		 */
		private Long getOlqId()
		{
			return olqId;
		}

		/**
		 * @param olqId the olqId to set
		 */
		public T setOlqId(final Long olqId)
		{
			this.olqId = olqId;
			return self();
		}

		/**
		 * @return the status
		 */
		public String getStatus()
		{
			return status;
		}

		/**
		 * @param status the status to set
		 */
		public T setStatus(final String status)
		{
			this.status = status;
			return self();
		}

		/**
		 * @return the expressOrderId
		 */
		public String getExpressOrderId()
		{
			return expressOrderId;
		}

		/**
		 * @param expressOrderId the expressOrderId to set
		 */
		public T setExpressOrderId(final String expressOrderId)
		{
			this.expressOrderId = expressOrderId;
			return self();
		}

		/**
		 * @return the expressCode
		 */
		public String getExpressCode()
		{
			return expressCode;
		}

		/**
		 * @param expressCode the expressCode to set
		 */
		public T setExpressCode(final String expressCode)
		{
			this.expressCode = expressCode;
			return self();
		}

		@Override
		protected abstract T self();

		public ExportOrderLineQuantity build()
		{
			return new ExportOrderLineQuantity(this);
		}
	}
}
