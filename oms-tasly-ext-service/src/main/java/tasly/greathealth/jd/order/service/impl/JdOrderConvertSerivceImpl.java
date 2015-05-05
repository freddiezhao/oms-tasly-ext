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
package tasly.greathealth.jd.order.service.impl;

import java.util.ArrayList;
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

import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.order.CouponDetail;
import com.jd.open.api.sdk.domain.order.ItemInfo;
import com.jd.open.api.sdk.domain.order.OrderPrintData;
import com.jd.open.api.sdk.domain.order.OrderSearchInfo;
import com.jd.open.api.sdk.request.order.OrderPrintDataGetRequest;
import com.jd.open.api.sdk.response.order.OrderPrintDataGetResponse;


/**
 * JD拆单
 *
 * @author libin
 *
 */
public class JdOrderConvertSerivceImpl extends AbstractOrderConverService<OrderSearchInfo, ItemInfo> implements OrderConstants
{
	private final Logger LOG = this.getLogObj();
	private static String DEFAULT_RENTS;
	private static JdClient jdJscClient;


	@Override
	protected Logger getLogObj()
	{
		return OmsLoggerFactory.getJdorderlog();
	}

	/**
	 * @param jdJscClient the jdJscClient to set
	 */
	public static void setJdJscClient(final JdClient jdJscClient)
	{
		JdOrderConvertSerivceImpl.jdJscClient = jdJscClient;
	}

	/**
	 * @param dEFAULT_RENTS the dEFAULT_RENTS to set
	 */
	public static void setDEFAULT_RENTS(final String dEFAULT_RENTS)
	{
		DEFAULT_RENTS = dEFAULT_RENTS;
	}

	@Override
	protected List<ItemInfo> getOrderLines()
	{
		return this.originalOrder.getItemInfoList();
	}

	@Override
	protected String getOriginalOrderid()
	{
		return this.originalOrder.getOrderId();
	}

	@Override
	protected String getOriginalOrderLineid(final ItemInfo o)
	{
		return o.getGiftPoint();
	}

	@Override
	// 将订单行级别id放在gift_point字段上
	protected void setOriginalOrderLineid(final ItemInfo o)
	{
		// final String orderId = getOriginalOrderid();
		// final String skuid = getOuterSkuid(o);
		// final SimpleDateFormat sf = new SimpleDateFormat("YYMMddHHmmss");
		// final String temp = sf.format(new Date());
		// final String originalOrderLineid = TaslyThirdUtils.CreateUUID();
		final String originalOrderLineid = TaslyThirdUtils.CreateUID().toString();
		o.setGiftPoint(originalOrderLineid);
	}

	// TM 因为某种原因，SKU存储在Outerid和OuterSkuid两个字段之一，优先查OuterSkuid
	// JD行级别没有Outerid，所以返回null
	@Override
	protected String getOuterid(final ItemInfo o)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOuterSkuid(final ItemInfo o)
	{
		return o.getOuterSkuId();
	}

	@Override
	protected String getPrice(final ItemInfo o)
	{
		return o.getJdPrice();
	}

	@Override
	protected String getTotalPrice(final ItemInfo o)
	{
		return String.valueOf(TmallUtil.getPirceByNum(TaslyThirdUtils.safe2Double(getPrice(o)), Long.valueOf(getItemTotal(o))));
	}

	@Override
	protected String getItemTotal(final ItemInfo o)
	{
		return o.getItemTotal();
	}

	// 将行级别优惠金额放在sku_name字段上
	@Override
	protected String getDiscountFee(final ItemInfo o)
	{
		return o.getSkuName();
	}

	@Override
	protected void setDiscountFee(final ItemInfo o, final String discountFee)
	{
		o.setSkuName(discountFee);

	}

	// JD 无
	@Override
	protected String getPayment(final ItemInfo o)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	// JD 无
	@Override
	protected String getTotalFee(final ItemInfo o)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	// JD 无
	@Override
	protected String getAdjustFee(final ItemInfo o)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getValidRentsRegex()
	{
		return DEFAULT_RENTS;
	}

	@Override
	protected void setOuterSkuId(final ItemInfo o, final String skuid)
	{
		o.setOuterSkuId(skuid);

	}

	@Override
	protected void setOuterIid(final ItemInfo o, final String outerIid)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	protected void setPrice(final ItemInfo o, final String price)
	{
		o.setJdPrice(price);

	}

	@Override
	protected void setItemTotal(final ItemInfo o, final Long num)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	protected void setPayment(final ItemInfo o, final String payment)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	protected void setTotalFee(final ItemInfo o, final String totalFee)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	protected void setAdjustFee(final ItemInfo o, final String adjustFee)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	protected void setOrderLines(final OrderSearchInfo t, final List<ItemInfo> lines)
	{
		t.setItemInfoList(lines);
	}

	/*
	 * 京东判断是不是赠品，根据jd_price是否为0判断
	 * 京东的赠品和正常商品一样，是独立的一行
	 */
	@Override
	protected List<ItemInfo> splitGift() throws SplitGiftException
	{
		// YTODO Auto-generated method stub
		return null;

	}

	@Override
	public String getSkuPos(final ItemInfo o)
	{
		if (!StringUtils.isEmpty(this.getOuterSkuid(o)))
		{
			return DEFAULT_OUTERSKUID;
		}
		else
		{
			LOG.error("订单" + this.originalOrder.getOrderId() + ",行项目ID" + o.getGiftPoint() + "，因为维护SKU，所以无法进行查单");
		}
		return null;
	}

	@Override
	public String getSkuByPos(final ItemInfo o)
	{
		if (DEFAULT_OUTERSKUID.equals(getSkuPos(o)))
		{
			return this.getOuterSkuid(o);
		}
		return null;
	}

	@Override
	protected void setSkuByPos(final ItemInfo copyOrder, final SkuToProduct skuToProduct)
	{
		if (DEFAULT_OUTERSKUID.equals(getSkuPos(copyOrder)))
		{
			this.setOuterSkuId(copyOrder, skuToProduct.getItemId());
		}
	}

	// @Override
	// protected boolean judgeRents(final ItemInfo o)
	// {
	// final String regex = getValidRentsRegex();
	//
	// if (DEFAULT_OUTERSKUID.equals(getSkuPos(o)))
	// {
	// final String outerSkuIid = this.getOuterSkuid(o);
	// final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	// final Matcher matcher = pattern.matcher(outerSkuIid);
	// LOG.info(".......外租商品判断返回值：" + matcher.matches() + ".......");
	// return matcher.matches();
	// }
	// LOG.info(".......外租商品判断返回值false.......");
	// return false;
	// }

	// @Override
	// protected void setOrderLineSkuMap(final Map<String, String> origOrdersMap, final ItemInfo o)
	// {
	// if (DEFAULT_OUTERSKUID.equals(getSkuPos(o)))
	// {
	// origOrdersMap.put(this.getOriginalOrderLineid(o), getOuterSkuid(o));
	// }
	// }

	@Override
	protected void setAdditionInfo(final OrderContext orderContext) throws JdException
	{
		final OrderPrintDataGetRequest request = new OrderPrintDataGetRequest();
		request.setOrderId(getOriginalOrderid());
		final OrderPrintDataGetResponse response = jdJscClient.execute(request);
		final OrderPrintData orderPrintData = response.getApiOrderPrintData();
		orderContext.addProperty(JD_INVOICE_TYPE, orderPrintData.getInvoiceType());
		orderContext.addProperty(JD_INVOICE_TITLE, orderPrintData.getInvoiceTitle());
		orderContext.addProperty(JD_INVOICE_CONTENT, orderPrintData.getInvoiceContent());
		// orderContext.addProperty(ORDER_LINE_DISCOUNTFEE, getOrderLineDiscountfee());
	}

	@Override
	protected void setLineAdditionInfo(final OrderContext orderContext, final ItemInfo o) throws JdException
	{
		// YTODO Auto-generated method stub

	}

	/**
	 * 获取行级别优惠信息
	 *
	 * @param t
	 */
	@Override
	protected void setOrderLineDiscountfee()
	{
		final List<ItemInfo> itemInfoList = getOrderLines();
		// 没有商家优惠，每个行项目设置为0
		if (TaslyThirdUtils.safe2Double(this.originalOrder.getSellerDiscount()) == 0D)
		{
			for (final ItemInfo itemInfo : itemInfoList)
			{
				this.setDiscountFee(itemInfo, "0.00");
			}
		}
		else
		{
			// 存放未有行项目优惠行对象
			final List<ItemInfo> itemInfoListSub = new ArrayList<ItemInfo>();
			// 商家优惠金额和
			Double total_coupon_price = 0D;
			// 卖家优惠与商家优惠金额和之差
			Double Sub_coupon_price = 0D;
			for (final ItemInfo itemInfo : itemInfoList)
			{
				// final String omsSku = this.getSkuPos(itemInfo);
				final String orderLineDiscountfee = getSingleOrderLineDiscountfee(itemInfo);
				if (orderLineDiscountfee != null)
				{
					// 将行项目优惠金额设置在sku_name（商品的名称+SKU规格）上
					itemInfo.setSkuName(orderLineDiscountfee);
					total_coupon_price = TaslyThirdUtils.getTotal(total_coupon_price,
							TaslyThirdUtils.safe2Double(orderLineDiscountfee));
				}
				else
				{
					itemInfoListSub.add(itemInfo);
				}
			}
			// 如果在CouponDetail获取的卖家优惠不等于订单级别的优惠
			Sub_coupon_price = TaslyThirdUtils.getPriceBySub(TaslyThirdUtils.safe2Double(this.originalOrder.getSellerDiscount()),
					total_coupon_price);
			if (Sub_coupon_price == 0D)
			{
				LOG.debug("订单：" + this.getOriginalOrderid() + ",卖家优惠金额（订单级别）与优惠对象CouponDetail优惠之和相等无需程序按照金额百分比进行分摊！");
			}
			else if (itemInfoListSub.size() > 0)
			{
				// 将差额分摊
				this.shareSellerDiscount(Sub_coupon_price, itemInfoListSub);
			}
			else
			{
				LOG.error("订单：" + this.getOriginalOrderid() + ",卖家优惠金额（订单级别）与优惠对象CouponDetail优惠之和不等，单已经没有可以分摊的行项目！");
			}
		}
	}

	/**
	 * 获取行级别优惠信息
	 *
	 * @param t
	 * @return
	 */
	protected String getSingleOrderLineDiscountfee(final ItemInfo o)
	{
		final List<CouponDetail> couponDetailList = this.originalOrder.getCouponDetailList();
		final String jdSku = o.getSkuId();
		final String omsSku = o.getOuterSkuId();
		final Double jdPrice = TaslyThirdUtils.safe2Double(o.getJdPrice());
		String orderLineDiscountfee = null;
		if (jdSku.trim().length() > 0)
		{
			if (!(omsSku.trim().length() > 0))
			{
				// todo:通过商品API查询，如果还查不到，就是异常
			}
			else
			{
				// 非赠品
				if (jdPrice > 0)
				{
					for (final CouponDetail couponDetail : couponDetailList)
					{
						if (couponDetail.getSkuId().equals(jdSku))
						{
							orderLineDiscountfee = couponDetail.getCouponPrice();
							break;
						}
					}
				}
			}
		}
		else
		{
			// todo:京东sku为空,异常情况
		}
		return orderLineDiscountfee;
	}

	@Override
	protected void setRealPayment()
	{

	}
}
