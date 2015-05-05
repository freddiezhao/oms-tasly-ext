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
package tasly.greathealth.erp.order.conversion;

import com.hybris.commons.conversion.impl.AbstractPopulator;

import tasly.greathealth.erp.api.codeMapping.dto.ErpCodeMapping;
import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.domain.erp.ErpCodeMappingData;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;


/**
 * Implementation of {@link AbstractPopulator} to populate a {@link ItemInfo} from {@link ItemInfoData}.
 */
public class ErpCodeMappingDataToModelPopulator extends AbstractPopulator<ErpCodeMappingData, ErpCodeMapping>
{
	@Override
	public void populate(final ErpCodeMappingData source, final ErpCodeMapping target)
	{
		target.setType(source.getType());
		target.setSourceCode(source.getSouceCode());
		target.setTargetCode(source.getTargetCode());
		target.setComments(source.getComments());

	}
}
