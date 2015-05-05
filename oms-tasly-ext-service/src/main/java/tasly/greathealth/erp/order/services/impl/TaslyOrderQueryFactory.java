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

import com.hybris.kernel.api.CriteriaExpression;
import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.service.order.impl.OrderQueryFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.oms.domain.erp.ErpCodeMappingData;
import tasly.greathealth.oms.domain.order.ApproveStatus;
import tasly.greathealth.oms.domain.order.ShippingLockStatus;
import tasly.greathealth.oms.domain.order.TaslyOrderData;


/**
 * created by vincent for TS-688
 * ERP发货状态同步更新Hybris OMS订单
 */
public class TaslyOrderQueryFactory extends OrderQueryFactory
{

	@SuppressWarnings("rawtypes")
	@Transactional
	public CriteriaQuery getOrdersByEmail(final String email)
	{
		final CriteriaQuery<TaslyOrderData> criteriaQuery = this.query(TaslyOrderData.class);

		return criteriaQuery.where(Restrictions.eq(TaslyOrderData.EMAILID, email));

	}

	// fetch TaslyOrder by order ID
	@SuppressWarnings("rawtypes")
	@Transactional
	public CriteriaQuery getOrderByID(final String orderID)
	{
		final CriteriaQuery<TaslyOrderData> criteriaQuery = this.query(TaslyOrderData.class);
		return criteriaQuery.where(Restrictions.eq(TaslyOrderData.ORDERID, orderID));
	}


	/**
	 * fetch TaslyOrder according to below conditions:
	 * 1. 审核标记：order.approve_status=1
	 * 2. 同步标记：order.replication_status="N,E"
	 * 3. 重发次数：order.replication_times<3
	 * 4. 没有锁（订单默认状态)才进行下传，shipping_lock_status='NON_LOCK'
	 * 5. SOURCING状态：orders.`state`='SOURCING_SUCCESS',暂时注释掉
	 * 6. orders.merchant_tag <>'RENTRUN'
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Transactional
	public CriteriaQuery getOmsOrders(final String approve_status, final String[] replication_status, final int replication_time)
	{
		final CriteriaQuery<TaslyOrderData> criteriaQuery = this.query(TaslyOrderData.class);

		final List restrictions = new ArrayList();
		// approve_status
		restrictions.add(Restrictions.eq(TaslyOrderData.APPROVE_STATUS, ApproveStatus.valueOf(approve_status)));
		// replication_status
		restrictions.add(Restrictions.in(TaslyOrderData.REPLICATION_STATUS, replication_status));
		// replication_times
		restrictions.add(RawRestrictions.le(TaslyOrderData.REPLICATION_TIMES.name(), replication_time));
		// TS-685: Order lockStatus()
		// 如果是0，才可以传输订单;0: 没有锁（订单默认状态)
		// restrictions.add(RawRestrictions.in(TaslyOrderData.SHIPPING_LOCK_STATUS.name().toString(), lockStatus));
		final ShippingLockStatus[] shipping_lock_status = new ShippingLockStatus[]{ShippingLockStatus.NON_LOCK,
				ShippingLockStatus.LOCK_FAILED};
		restrictions.add(Restrictions.in(TaslyOrderData.SHIPPING_LOCK_STATUS, shipping_lock_status));

		// add by libin 20150304 增加新建订单条件：orders.`state`这个字段为SOURCING_SUCCESS时才进行往ERP同步新建订单
		// restrictions.add(Restrictions.eq(TaslyOrderData.STATE, TaslyERPConstants.ORDER_STATE_S));
		// TS-953 add by libin 20150309增加新建订单条件：OMS中仅包含外租商品的订单不进入ERP，orders.merchant_tag字段为RENTRUN的订单不进入ERP
		restrictions.add(Restrictions.neq(TaslyOrderData.MERCHANT_TAG, TaslyERPConstants.ORDER_MERCHANT_TAG_FZ));

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);

		// TS-881 :add order by issueDate asc
		criteriaQuery.order(TaslyOrderData.ISSUEDATE.name(), true);

		return criteriaQuery;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	protected CriteriaQuery applyingCollectedRestrictionsToCriteriaQuery(final CriteriaQuery origQuery, final List restrictions)
	{
		CriteriaQuery criteriaQuery = origQuery;
		if (!restrictions.isEmpty())
		{
			criteriaQuery = (CriteriaQuery) criteriaQuery.where((CriteriaExpression) restrictions.remove(0));
			for (final Iterator i$ = restrictions.iterator(); i$.hasNext();)
			{
				final Restriction restriction = (Restriction) i$.next();
				criteriaQuery = (CriteriaQuery) criteriaQuery.and(restriction);
			}

		}
		return criteriaQuery;
	}

	/**
	 * used for ErpcodeMapping table query
	 *
	 * @author vincent.yin
	 * @param type sourceCode
	 */

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Transactional
	public CriteriaQuery getErpCodeMapping(final String type, final String sourceCode)
	{
		final CriteriaQuery<ErpCodeMappingData> criteriaQuery = this.query(ErpCodeMappingData.class);

		final List restrictions = new ArrayList();
		// type
		restrictions.add(RawRestrictions.eq(ErpCodeMappingData.TYPE.name(), type));
		// sourceCode
		restrictions.add(RawRestrictions.in(ErpCodeMappingData.SOUCECODE.name(), sourceCode));

		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		return criteriaQuery;
	}
}
