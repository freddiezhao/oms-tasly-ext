/*
 * [y] hybris Core+ Platform
 * 
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package tasly.greathealth.oms.export.api.stock.dto;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import com.hybris.commons.dto.impl.PropertyAwareEntityDto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * Generated automatically
 *
 * @author: dto-generator, [y] hybris Platform
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportStock extends PropertyAwareEntityDto
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1954916962295660343L;


	@XmlID
	private String sku;

	private String description;

	private String oldMaterialNumber;

	private String locationId;

	private String innerSource;

	private int ats;

	private int safetyStock;


	public ExportStock()
	{
	}

	protected ExportStock(final Builder<?> builder)
	{
		super(builder);
		setSku(builder.getSku());
		setDescription(builder.getDescription());
		setOldMaterialNumber(builder.getOldMaterialNumber());
		setLocationId(builder.getLocationId());
		setInnerSource(builder.getInnerSource());
		setAts(builder.getAts());
		setSafetyStock(builder.getSafetyStock());

	}


	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getSku()
	{
		return sku;
	}

	/**
	 * sets
	 *
	 */
	public void setSku(final String sku)
	{
		this.sku = sku;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * sets
	 *
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getOldMaterialNumber()
	{
		return oldMaterialNumber;
	}

	/**
	 * sets
	 *
	 */
	public void setOldMaterialNumber(final String oldMaterialNumber)
	{
		this.oldMaterialNumber = oldMaterialNumber;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getLocationId()
	{
		return locationId;
	}

	/**
	 * sets
	 *
	 */
	public void setLocationId(final String locationId)
	{
		this.locationId = locationId;
	}

	/**
	 * gets
	 *
	 * @returns String
	 */
	public String getInnerSource()
	{
		return innerSource;
	}

	/**
	 * sets
	 *
	 */
	public void setInnerSource(final String innerSource)
	{
		this.innerSource = innerSource;
	}

	/**
	 * gets
	 *
	 * @returns int
	 */
	public int getAts()
	{
		return ats;
	}

	/**
	 * sets
	 *
	 */
	public void setAts(final int ats)
	{
		this.ats = ats;
	}

	/**
	 * gets
	 *
	 * @returns int
	 */
	public int getSafetyStock()
	{
		return safetyStock;
	}

	/**
	 * sets
	 *
	 */
	public void setSafetyStock(final int safetyStock)
	{
		this.safetyStock = safetyStock;
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
		private String sku;
		private String description;
		private String oldMaterialNumber;
		private String locationId;
		private String innerSource;
		private int ats;
		private int safetyStock;


		/**
		 * sets
		 *
		 */
		public T setSku(final String sku)
		{
			this.sku = sku;
			return self();
		}

		private String getSku()
		{
			return sku;
		}

		/**
		 * sets
		 *
		 */
		public T setDescription(final String description)
		{
			this.description = description;
			return self();
		}

		private String getDescription()
		{
			return description;
		}

		/**
		 * sets
		 *
		 */
		public T setOldMaterialNumber(final String oldMaterialNumber)
		{
			this.oldMaterialNumber = oldMaterialNumber;
			return self();
		}

		private String getOldMaterialNumber()
		{
			return oldMaterialNumber;
		}

		/**
		 * sets
		 *
		 */
		public T setLocationId(final String locationId)
		{
			this.locationId = locationId;
			return self();
		}

		private String getLocationId()
		{
			return locationId;
		}

		/**
		 * sets
		 *
		 */
		public T setInnerSource(final String innerSource)
		{
			this.innerSource = innerSource;
			return self();
		}

		private String getInnerSource()
		{
			return innerSource;
		}

		/**
		 * sets
		 *
		 */
		public T setAts(final int ats)
		{
			this.ats = ats;
			return self();
		}

		private int getAts()
		{
			return ats;
		}

		/**
		 * sets
		 *
		 */
		public T setSafetyStock(final int safetyStock)
		{
			this.safetyStock = safetyStock;
			return self();
		}

		private int getSafetyStock()
		{
			return safetyStock;
		}

		@Override
		protected abstract T self();

		public ExportStock build()
		{
			return new ExportStock(this);
		}
	}

	@Override
	public String getId()
	{
		// YTODO Auto-generated method stub
		return null;
	}
}
