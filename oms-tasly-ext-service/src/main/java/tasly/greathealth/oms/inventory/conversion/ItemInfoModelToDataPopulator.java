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

import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.inventory.domain.InventoryDomainInnerSource;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;


/**
 * Implementation of {@link AbstractPopulator} to populate a {@link ItemInfo} from {@link ItemInfoData}.
 */
public class ItemInfoModelToDataPopulator extends AbstractPopulator<ItemInfo, ItemInfoData>
{
	@Override
	public void populate(final ItemInfo source, final ItemInfoData target)
	{
		target.setSku(source.getSku());
		target.setQuantity(source.getQuantity());
		target.setDescription(source.getDescription());
		target.setBaseUnitCode(source.getBaseUnitCode());
		target.setStockManageFlag(source.getStockManageFlag());
		if (source.getInnerSource() != null)
		{
			target.setInnerSource(InventoryDomainInnerSource.valueOf(source.getInnerSource().toString()));
		}
		target.setExt1(source.getExt1());
		target.setExt2(source.getExt2());
		target.setExt3(source.getExt3());
		target.setExt4(source.getExt4());
		target.setExt5(source.getExt5());
		target.setVendor(source.getVendor());
	}
}
