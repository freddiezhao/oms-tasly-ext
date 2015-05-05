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
package tasly.greathealth.yhd.order.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.thirdparty.order.common.AbstractOrderConverService;
import tasly.greathealth.thirdparty.order.common.TaslyThirdUtils;
import tasly.greathealth.tmall.order.exception.SplitGiftException;
import tasly.greathealth.tmall.order.services.impl.OrderContext;
import tasly.greathealth.tmall.order.services.impl.TmallUtil;

import com.jd.open.api.sdk.JdException;
import com.yhd.YhdClient;
import com.yhd.object.trade.Order;
import com.yhd.object.trade.Trade;


/**
 * YHD拆单
 *
 * @author libin
 *
 */
public class YhdOrderConvertSerivceImpl extends AbstractOrderConverService<Trade, Order> implements OrderConstants
{
	private final Logger LOG = this.getLogObj();
	private YhdClient client;
	private String sessionKey;
	private static String DEFAULT_RENTS;

	/**
	 * @param sessionKey the sessionKey to set
	 */
	public void setSessionKey(final String sessionKey)
	{
		this.sessionKey = sessionKey;
	}

	/**
	 * @param dEFAULT_RENTS the dEFAULT_RENTS to set
	 */
	public static void setDEFAULT_RENTS(final String dEFAULT_RENTS)
	{
		DEFAULT_RENTS = dEFAULT_RENTS;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(final YhdClient client)
	{
		this.client = client;
	}

	@Override
	protected Logger getLogObj()
	{
		return OmsLoggerFactory.getYhdorderlog();
	}


	@Override
	protected String getOriginalOrderid()
	{
		// YTODO Auto-generated method stub
		return originalOrder.getTid().toString();
	}

	@Override
	protected List<Order> getOrderLines()
	{
		// YTODO Auto-generated method stub
		return originalOrder.getOrders().getOrder();
	}

	@Override
	protected String getOriginalOrderLineid(final Order o)
	{
		return o.getOid().toString();
	}

	@Override
	protected void setOriginalOrderLineid(final Order o)
	{
		final long originalOrderLineid = TaslyThirdUtils.CreateUID();
		o.setOid(originalOrderLineid);
	}

	@Override
	protected String getOuterid(final Order o)
	{
		return o.getOuter_iid();
	}

	@Override
	protected String getOuterSkuid(final Order o)
	{
		return o.getOuter_sku_id();
	}

	@Override
	protected String getPrice(final Order o)
	{
		return o.getPrice();
	}

	@Override
	protected String getTotalPrice(final Order o)
	{
		return String.valueOf(TmallUtil.getPirceByNum(TaslyThirdUtils.safe2Double(getPrice(o)), Long.valueOf(getItemTotal(o))));
	}

	@Override
	protected String getItemTotal(final Order o)
	{
		return o.getNum().toString();
	}

	@Override
	protected String getDiscountFee(final Order o)
	{
		return o.getDiscount_fee();
	}

	@Override
	protected String getPayment(final Order o)
	{
		return o.getPayment();
	}

	@Override
	protected String getTotalFee(final Order o)
	{
		return o.getTotal_fee();
	}

	@Override
	protected String getAdjustFee(final Order o)
	{
		return o.getAdjust_fee();
	}

	@Override
	protected String getValidRentsRegex()
	{
		return DEFAULT_RENTS;
	}

	@Override
	protected void setOuterSkuId(final Order o, final String skuid)
	{
		o.setOuter_sku_id(skuid);
	}

	@Override
	protected void setOuterIid(final Order o, final String outerIid)
	{
		o.setOuter_iid(outerIid);

	}

	@Override
	protected void setPrice(final Order o, final String price)
	{
		o.setPrice(price);

	}

	@Override
	protected void setDiscountFee(final Order o, final String discountFee)
	{
		o.setDiscount_fee(discountFee);

	}

	@Override
	protected void setItemTotal(final Order o, final Long num)
	{
		o.setNum(num);

	}

	@Override
	protected void setPayment(final Order o, final String payment)
	{
		o.setPayment(payment);
	}

	@Override
	protected void setTotalFee(final Order o, final String totalFee)
	{
		o.setTotal_fee(totalFee);

	}

	@Override
	protected void setAdjustFee(final Order o, final String adjustFee)
	{
		o.setAdjust_fee(adjustFee);

	}

	@Override
	protected void setOrderLines(final Trade t, final List<Order> lines)
	{
		t.getOrders().setOrder(lines);
	}

	@Override
	protected List<Order> splitGift() throws SplitGiftException
	{

		// todo:YHD PromotionDetail暂不提供
		// final List<PromotionDetail> list = this.originalOrder.getPromotion_details().getPromotion_detail();
		return null;
	}

	@Override
	protected void setAdditionInfo(final OrderContext orderContext) throws JdException
	{
		// YTODO Auto-generated method stub

	}

	@Override
	protected void setLineAdditionInfo(final OrderContext orderContext, final Order o) throws JdException
	{
		// YTODO Auto-generated method stub

	}

	@Override
	protected String getSkuPos(final Order o)
	{
		if (!StringUtils.isEmpty(this.getOuterSkuid(o)))
		{
			return DEFAULT_OUTERSKUID;
		}
		else if (!StringUtils.isEmpty(this.getOuterid(o)))
		{
			return DEFAULT_OUTERID;
		}
		else
		{
			LOG.error("订单" + this.originalOrder.getTid() + ",行项目ID" + o.getOid() + "，因为维护SKU，所以无法进行查单");
		}
		return null;
	}

	@Override
	protected String getSkuByPos(final Order o)
	{
		if (DEFAULT_OUTERSKUID.equals(getSkuPos(o)))
		{
			return this.getOuterSkuid(o);
		}
		else if (DEFAULT_OUTERID.equals(getSkuPos(o)))
		{
			return this.getOuterid(o);
		}
		return null;
	}

	@Override
	protected void setSkuByPos(final Order copyOrder, final SkuToProduct skuToProduct)
	{

		if (DEFAULT_OUTERSKUID.equals(getSkuPos(copyOrder)))
		{
			this.setOuterSkuId(copyOrder, skuToProduct.getItemId());
		}
		else if (DEFAULT_OUTERID.equals(getSkuPos(copyOrder)))
		{
			this.setOuterIid(copyOrder, skuToProduct.getItemId());
		}
	}

	// 行级别应付金额-分摊金额
	@Override
	protected void setRealPayment()
	{
		final List<Order> lines = this.originalOrder.getOrders().getOrder();
		for (final Order order : lines)
		{

			final double payment = TmallUtil.getPriceBySub(Double.valueOf(order.getPayment()),
					Double.valueOf(order.getPart_mjz_discount()));

			order.setPayment(String.valueOf(payment));
		}

	}

	@Override
	protected void setOrderLineDiscountfee()
	{
		// YTODO Auto-generated method stub

	}
}
