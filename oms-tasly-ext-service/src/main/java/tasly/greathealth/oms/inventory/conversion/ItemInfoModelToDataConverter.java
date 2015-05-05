/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.inventory.conversion;

import com.hybris.commons.conversion.impl.AbstractPopulatingConverter;

import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.services.ItemInfoService;


/**
 * Implementation of {@link AbstractPopulatingConverter} to convert {@link ItemInfo} to {@link ItemInfoData}.
 */
public class ItemInfoModelToDataConverter extends AbstractPopulatingConverter<ItemInfo, ItemInfoData>
{
	private ItemInfoService itemInfoService;

	@Override
	protected ItemInfoData createTarget()
	{
		return itemInfoService.createItemInfo();
	}

	public void setItemInfoService(final ItemInfoService itemInfoService)
	{
		this.itemInfoService = itemInfoService;
	}
}
