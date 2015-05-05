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
public class ErpCodeMappingModelToDataPopulator extends AbstractPopulator<ErpCodeMapping, ErpCodeMappingData>
{
	@Override
	public void populate(final ErpCodeMapping source, final ErpCodeMappingData target)
	{
		target.setType(source.getType());
		target.setSouceCode(source.getSourceCode());
		target.setTargetCode(source.getTargetCode());
		target.setComments(source.getComments());
	}
}
