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

package tasly.greathealth.oms.api.inventory.dto;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import com.hybris.oms.domain.inventory.ItemLocationCurrent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Tasly Extended ItemLocation
 * Generated automatically
 *
 * @author: dto-generator, [y] hybris Platform
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TaslyItemLocation extends ItemLocationCurrent
{
	public final static long serialVersionUID = 1521800303L;

	private int allocationPercent;

	private int safetyStock;

	private ItemInfo itemInfo;

	/**
	 * @return the safetyStock
	 */
	public int getSafetyStock()
	{
		return safetyStock;
	}

	/**
	 * @param safetyStock the safetyStock to set
	 */
	public void setSafetyStock(final int safetyStock)
	{
		this.safetyStock = safetyStock;
	}

	/**
	 * @return the itemInfo
	 */
	public ItemInfo getItemInfo()
	{
		return itemInfo;
	}

	/**
	 * @param itemInfo the itemInfo to set
	 */
	public void setItemInfo(final ItemInfo itemInfo)
	{
		this.itemInfo = itemInfo;
	}

	public TaslyItemLocation()
	{
	}

	protected TaslyItemLocation(final Builder<?> builder)
	{
		setAllocationPercent(builder.getAllocationPercent());
		setSafetyStock(builder.getSafetyStock());
		setItemInfo(builder.getItemInfo());
	}


	/**
	 * gets
	 *
	 * @returns int
	 */
	public int getAllocationPercent()
	{
		return allocationPercent;
	}

	/**
	 * sets
	 */
	public void setAllocationPercent(final int allocationPercent)
	{
		this.allocationPercent = allocationPercent;
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

	public abstract static class Builder<T extends Builder<T>> extends ItemLocationCurrent.Builder<T>
	{
		private int allocationPercent;

		private int safetyStock;

		private ItemInfo itemInfo;

		/**
		 * sets
		 */
		public T setAllocationPercent(final int allocationPercent)
		{
			this.allocationPercent = allocationPercent;
			return self();
		}

		private int getAllocationPercent()
		{
			return allocationPercent;
		}

		public T setSafetyStock(final int safetyStock)
		{
			this.safetyStock = safetyStock;
			return self();
		}

		private int getSafetyStock()
		{
			return safetyStock;
		}

		public T setItemInfo(final ItemInfo itemInfo)
		{
			this.itemInfo = itemInfo;
			return self();
		}

		private ItemInfo getItemInfo()
		{
			return itemInfo;
		}

		@Override
		protected abstract T self();

		public TaslyItemLocation build()
		{
			return new TaslyItemLocation(this);
		}
	}
}
