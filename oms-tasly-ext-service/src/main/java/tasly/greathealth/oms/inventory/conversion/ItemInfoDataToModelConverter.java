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


/**
 * Implementation of {@link AbstractPopulatingConverter} to convert {@link ItemInfoData} to {@link ItemInfo}.
 */
public class ItemInfoDataToModelConverter extends AbstractPopulatingConverter<ItemInfoData, ItemInfo>
{
	@Override
	public ItemInfo createTarget()
	{
		return new ItemInfo();
	}
}
