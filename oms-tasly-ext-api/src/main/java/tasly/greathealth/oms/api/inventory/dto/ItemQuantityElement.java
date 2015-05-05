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
package tasly.greathealth.oms.api.inventory.dto;

import javax.xml.bind.annotation.XmlElement;


/**
 * @author Henter Liu
 */
public class ItemQuantityElement
{
	@XmlElement
	public String channel;

	@XmlElement
	public Integer quantity;

	@SuppressWarnings("unused")
	private ItemQuantityElement()
	{
	} // Required by JAXB

	public ItemQuantityElement(final String channel, final Integer quantity)
	{
		this.channel = channel;
		this.quantity = quantity;
	}
}
