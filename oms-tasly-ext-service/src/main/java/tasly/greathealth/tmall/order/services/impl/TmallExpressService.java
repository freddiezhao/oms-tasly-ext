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
package tasly.greathealth.tmall.order.services.impl;

import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.tmall.order.dao.OmsQueryDao;
import tasly.greathealth.tmall.order.services.ExpressService;


/**
 *
 */
public class TmallExpressService implements ExpressService
{
	private OmsQueryDao omsQueryDao;

	@Override
	public TaslyOrderData modifyLogisticsCompany(final TaslyOrderData orderData)
	{
		final String innerSource = orderData.getInner_source().name();
		final String channel = orderData.getChannel_source().name();
		final String receiverState = orderData.getShippingAddress().getCountrySubentity();

		// *************** 拆分邮寄方式 ***************
		String realLogisticsCompany = null;
		final List<OrderLineData> orderLineDatas = orderData.getOrderLines();

		final int num = orderLineDatas.size();
		String expressCode = null;
		// 根据sku判断商品物流
		for (int i = 0; i < num; i++)
		{
			final List<OrderLineQuantityData> orderLineQuantityDatas = orderLineDatas.get(i).getOrderLineQuantities();

			for (final OrderLineQuantityData orderLineQuantityData : orderLineQuantityDatas)
			{
				if (orderLineQuantityData.getClass().isInstance(TaslyOrderLineQuantityData.class))
				{
					final TaslyOrderLineQuantityData taslyOrderLineQuantityData = (TaslyOrderLineQuantityData) orderLineQuantityData;
					expressCode = taslyOrderLineQuantityData.getExpress_code();
				}
			}

			if (StringUtils.isEmpty(expressCode))
			{
				String logisticsCompanyForLocal = null;
				final String skuId = orderLineDatas.get(i).getSkuId();
				logisticsCompanyForLocal = omsQueryDao.getExpressBySku(skuId, channel, innerSource);

				if (!StringUtils.isEmpty(logisticsCompanyForLocal))
				{
					realLogisticsCompany = logisticsCompanyForLocal;
					break;
				}
			}
		}
		// 根据送货地点判断商品物流
		if (realLogisticsCompany == null)
		{
			realLogisticsCompany = omsQueryDao.getExpressByLocation(receiverState, channel, innerSource);
		}

		// 所有order更改物流公司
		for (int j = 0; j < num; j++)
		{
			final List<OrderLineQuantityData> orderLineQuantityDatas = orderLineDatas.get(j).getOrderLineQuantities();

			for (final OrderLineQuantityData orderLineQuantityData : orderLineQuantityDatas)
			{
				if (orderLineQuantityData instanceof TaslyOrderLineQuantityData)
				{
					final TaslyOrderLineQuantityData taslyOrderLineQuantityData = (TaslyOrderLineQuantityData) orderLineQuantityData;
					taslyOrderLineQuantityData.setExpress_code(realLogisticsCompany);
				}
			}
		}

		return orderData;
	}

	@Required
	public void setOmsQueryDao(final OmsQueryDao omsQueryDao)
	{
		this.omsQueryDao = omsQueryDao;
	}
}
