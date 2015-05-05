/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.tmall.order.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.order.dao.OmsQueryDao;
import tasly.greathealth.tmall.order.exception.ProductNotFoundException;
import tasly.greathealth.tmall.order.exception.ProductUnitNotFoundException;


/**
 * An implementation of {@link OmsQueryDao} for Tmall
 *
 * @author han dong
 */
public class OmsQueryImpl implements OmsQueryDao
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();

	private JdbcTemplate jdbcTemplate;

	@Override
	public String getExpressBySku(final String sku, final String channel, final String innerSource)
	{
		final String sql = "select b.`express_code` from  expressitems b  where b.channel_source='" + channel + "' and b.skuid='"
				+ sku + "' and b.inner_source='" + innerSource + "' and b.status ='ACTIVE'";
		final List list = jdbcTemplate.queryForList(sql);
		if (null != list && 0 != list.size())
		{
			final Map result = (Map) list.get(0);
			LOG.info(".......所属渠道=" + channel + ",业态=" + innerSource + ",SKU=" + sku + ",物流公司编码=" + result.get("express_code")
					+ ".......");
			return (String) result.get("express_code");
		}
		else
		{
			LOG.info(".......所属渠道=" + channel + ",业态=" + innerSource + ",SKU=" + sku + ",物流公司编码=null.......");
			return null;
		}
	}

	@Override
	public String getExpressByLocation(final String province, final String channel, final String innerSource)
	{
		final String sql = "select b.`express_code` from  expresslocations b  where b.channel_source='" + channel
				+ "' and b.province='" + province + "' and b.inner_source='" + innerSource + "'";
		final List list = jdbcTemplate.queryForList(sql);
		if (null != list && 0 != list.size())
		{
			final Map result = (Map) list.get(0);
			LOG.info(".......所属渠道=" + channel + ",业态=" + innerSource + ",送货地区=" + province + ",物流公司编码=" + result.get("express_code")
					+ ".......");
			return (String) result.get("express_code");
		}
		else
		{
			LOG.info(".......送货地区=" + province + ",物流公司编码=null.......");
			return null;
		}
	}

	@Override
	public String getProductUnit(final String sku, final String channel, final String innerSource)
			throws ProductUnitNotFoundException
	{

		final String sql = "select a.baseunitcode from itemInfos a where a.sku='" + sku + "' and a.innersource= '" + innerSource
				+ "'";
		final List list = jdbcTemplate.queryForList(sql);
		if (null != list && 0 != list.size())
		{
			final Map result = (Map) list.get(0);
			LOG.info(".......商品SKU=" + sku + "的商品单位=" + result.get("baseunitcode") + ".......");
			return (String) result.get("baseunitcode");
		}
		else
		{
			LOG.info(".......商品SKU=" + sku + "的商品单位没有相关数据.......");
			throw new ProductUnitNotFoundException("In iteminfos table, there is no products about sku is " + sku);
		}

	}

	@Override
	public Map getRentProduct(final String sku, final String channel, final String innerSource) throws ProductNotFoundException
	{
		// YTODO Auto-generated method stub
		final String sql = "select a.baseunitcode,a.sku from itemInfos a where a.oldmaterialnumber='" + sku
				+ "' and a.innersource= '" + innerSource + "'";
		final List list = jdbcTemplate.queryForList(sql);
		if (null != list && 0 != list.size())
		{
			final Map result = (Map) list.get(0);
			LOG.info(".......外租商品oldmaterialnumber=" + sku + "的商品单位=" + result.get("baseunitcode") + "|sku = " + result.get("sku")
					+ ".......");
			return result;
		}
		else
		{
			LOG.info(".......外租商品oldmaterialnumber=" + sku + "的商品没有相关数据.......");
			throw new ProductNotFoundException("No rent related product information about sku is " + sku);
		}

	}

	@Override
	public String getSkuDescription(final String sku, final String innerSource)
	{

		final String sql = "select a.description from itemInfos a where a.sku='" + sku + "' and a.innersource= '" + innerSource
				+ "'";
		final List list = jdbcTemplate.queryForList(sql);
		if (null != list && 0 != list.size())
		{
			final Map result = (Map) list.get(0);
			LOG.info(".......商品SKU=" + sku + "的商品描述=" + result.get("description") + ".......");
			return (String) result.get("description");
		}
		else
		{
			LOG.info(".......商品SKU=" + sku + "的商品单位没有相关数据.......");
			return "";
		}

	}

	@Required
	public void setJdbcTemplate(final JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
	}

}
