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
package tasly.greathealth.erp.order.services.impl;

import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.erp.order.services.OrderDeliveryStatusUpdateService;
import tasly.greathealth.oms.domain.erp.ErpCodeMappingData;
import tasly.greathealth.oms.domain.order.ExpressData;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.domain.order.PackingType;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tmall.order.services.impl.TmallUtil;
import tasly.greathealth.tmall.order.services.impl.TmallUtilForJSC;


/**
 * created by vincent for TS-688
 * ERP发货状态同步更新Hybris OMS订单
 * TS-689:Hybris OMS订单信息更新同步到SAP ERP
 */
public class DefaultOrderDeliveryStatusUpdateService extends AbstractHybrisService implements OrderDeliveryStatusUpdateService
{
	private TaslyOrderQueryFactory orderQueries;

	private static final Logger LOG = OmsLoggerFactory.getErporderlog();

	@Required
	public void setOrderQueries(final TaslyOrderQueryFactory orderQueries)
	{
		this.orderQueries = orderQueries;
	}

	private String pageSize;



	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(final String pageSize)
	{
		this.pageSize = pageSize;
	}

	@Transactional
	@Override
	public TaslyOrderData getTaslyOrderDataByOrderID(final String orderId)
	{
		try
		{

			final TaslyOrderData taslyOrderData = (TaslyOrderData) this.findOneSingle(this.orderQueries.getOrderByID(orderId));
			// @SuppressWarnings("deprecation")
			// final List<TaslyOrderData> taslyDatas = super.getPersistenceManager().search(
			// super.getPersistenceManager().createCriteriaQuery(TaslyOrderData.class)
			// .where(Restrictions.eq(TaslyOrderData.ORDERID, orderId)));

			//
			System.out.println("TaslyOrderData is :" + taslyOrderData);

			// final List<OrderData> datas = super.getPersistenceManager().search(
			// super.getPersistenceManager().createCriteriaQuery(OrderData.class)
			// .where(Restrictions.eq(OrderData.ORDERID, orderId)));

			// System.out.println("OrderData is :" + datas);

			return taslyOrderData;
		}
		catch (final Throwable e)
		{
			e.printStackTrace();
			throw new EntityNotFoundException("Order Id is not correct, " + orderId, e);
		}
	}

	// update Order packing status according to orderID
	@Override
	@Transactional
	public void updateTaslyOrderDataPackingByOrderID(final String orderId)
	{
		try
		{

			final TaslyOrderData taslyOrderData = (TaslyOrderData) this.findOneSingle(this.orderQueries.getOrderByID(orderId));

			taslyOrderData.setPacking(PackingType.PACKED);

			LOG.info("向天猫同步Packing信息");

			if (taslyOrderData.getInner_source() == InnerSource.OTC)
			{
				TmallUtil.synMemo2Tmall(Long.valueOf(taslyOrderData.getOriginal_order_id()), taslyOrderData.getSeller_message(), 5L);
				LOG.info("向天猫OTC店铺同步Packing信息,请检查旗子颜色");
			}
			else
			{
				TmallUtilForJSC.synMemo2Tmall(Long.valueOf(taslyOrderData.getOriginal_order_id()),
						taslyOrderData.getSeller_message(), 5L);
				LOG.info("向天猫酒水茶店铺同步Packing信息完毕,请检查旗子颜色");
			}
			super.getPersistenceManager().flush();

			LOG.info("TaslyOrderData is :" + taslyOrderData);
		}
		catch (final Throwable e)
		{
			e.printStackTrace();
			throw new EntityNotFoundException("Order Id is not correct, " + orderId, e);
		}

	}

	/**
	 * TS-689:Hybris OMS订单信息更新同步到SAP ERP
	 */

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<TaslyOrderData> getOmsApprovedOrders(final String approve_status, final String[] replication_status,
			final int replication_time)
			{

		@SuppressWarnings("rawtypes")
		final Page orderPage = findPaged(this.orderQueries.getOmsOrders(approve_status, replication_status, replication_time), 0,
				pageSize == null ? 50 : Integer.valueOf(pageSize).intValue());

		return orderPage.getContent();

		// return this.findAll(this.orderQueries.getOmsOrders(approve_status, replication_status, replication_time));


	}

	/**
	 * @author vincent.yin
	 *         TS-396 :hybris/OMS订单下行同步至ERP接口
	 * @param replication_status
	 */
	@Override
	@Transactional
	public void updateOmsOrderReplicationStatus(final String orderId, final String replication_status)
	{

		try
		{

			@SuppressWarnings("deprecation")
			final List<TaslyOrderData> taslyDatas = super.getPersistenceManager().search(
					super.getPersistenceManager().createCriteriaQuery(TaslyOrderData.class)
							.where(Restrictions.eq(TaslyOrderData.ORDERID, orderId)));

			for (final TaslyOrderData taslyOrderData : taslyDatas)
			{
				taslyOrderData.setReplication_status(replication_status);
			}
		}
		catch (final Throwable e)
		{
			LOG.error(e.toString());
			throw new EntityNotFoundException("Order Id is not correct, " + orderId, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ErpCodeMappingData getErpCodeMappingData(final String type, final String sourceCode)
	{
		try
		{
			return (ErpCodeMappingData) this.findOneSingle(this.orderQueries.getErpCodeMapping(type, sourceCode));

		}
		catch (final Throwable e)
		{
			LOG.info("ErpCodeMappingData is not exist!");
			return null;
		}

	}

	@Override
	@Transactional
	public ErpCodeMappingData createOrUpdateErpCodeMapping(final ErpCodeMappingData erpCodeMappingData)
	{

		LOG.info("invoke createOrUpdateErpCodeMapping() method!");
		if (null != erpCodeMappingData && erpCodeMappingData.getType() != null && erpCodeMappingData.getSouceCode() != null)
		{
			final String targetCode = erpCodeMappingData.getTargetCode();
			final ErpCodeMappingData newErpCodeMappingData = this.getErpCodeMappingData(erpCodeMappingData.getType(),
					erpCodeMappingData.getSouceCode());
			if (null != newErpCodeMappingData)
			{

				newErpCodeMappingData.setTargetCode(targetCode);

				return newErpCodeMappingData;
			}
			else
			{
				return this.getPersistenceManager().createOrUpdate(erpCodeMappingData);
			}
		}

		return erpCodeMappingData;
	}

	@Override
	@Transactional
	public ErpCodeMappingData createErpCodeMapping()
	{
		return this.getPersistenceManager().create(ErpCodeMappingData.class);
	}

	/**
	 * get all ERPCodeMapping data
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	public List<ErpCodeMappingData> getAllErpCodeMapping()
	{
		return this.getPersistenceManager().createCriteriaQuery(ErpCodeMappingData.class).resultList();
	}

	/**
	 * get All Express data
	 *
	 */
	@Transactional
	@Override
	public List<ExpressData> getAllExpressData()
	{
		return super.getPersistenceManager().createCriteriaQuery(ExpressData.class).resultList();
	}


}
