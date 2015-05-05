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
package tasly.greathealth.tmall.order.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.order.AbstractOrderConverService;
import tasly.greathealth.tmall.order.exception.SplitGiftException;
import tasly.greathealth.tmall.order.services.impl.TmallUtil;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.PromotionDetail;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.response.ItemGetResponse;


/**
 *
 */
public class TmallOrderConvertSerivceImpl extends AbstractOrderConverService<Trade, Order>
{

	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	private static final String DEFAULT_TMALL_FIELDS = "payment,detail_url,num_iid,title,nick,type,cid,seller_cids,props,input_pids,input_str,desc,pic_url,num,valid_thru,list_time,delist_time,stuff_status,location,price,post_fee,express_fee,ems_fee,has_discount,freight_payer,has_invoice,has_warranty,has_showcase,modified,increment,approve_status,postage_id,product_id,auction_point,property_alias,item_img,prop_img,sku,video,outer_id,is_virtual";

	private static String DEFAULT_RENTS;
	private static String DEFAULT_TMALL_STORE_SESSIONKEY;
	private TaobaoClient client;
	private final String REGEX = "z(\\d+)s(\\d+)";

	/**
	 * @param dEFAULT_RENTS the dEFAULT_RENTS to set
	 */
	public static void setDEFAULT_RENTS(final String dEFAULT_RENTS)
	{
		DEFAULT_RENTS = dEFAULT_RENTS;
	}

	/**
	 * @param trade the trade to set
	 */
	public void setTrade(final Trade trade)
	{
		this.trade = trade;
	}


	/**
	 * @param dEFAULT_TMALL_STORE_SESSIONKEY the dEFAULT_TMALL_STORE_SESSIONKEY to set
	 */
	public static void setDEFAULT_TMALL_STORE_SESSIONKEY(final String dEFAULT_TMALL_STORE_SESSIONKEY)
	{
		DEFAULT_TMALL_STORE_SESSIONKEY = dEFAULT_TMALL_STORE_SESSIONKEY;
	}

	@Override
	protected List<Order> getOrderLines()
	{
		return this.trade.getOrders();
	}

	@Override
	protected Long getTid()
	{
		return this.trade.getTid();
	}

	@Override
	protected Long getOid(final Order o)
	{
		return o.getOid();
	}

	@Override
	protected String getOuterid(final Order o)
	{
		return o.getOuterIid();
	}

	@Override
	protected String getOuterSkuid(final Order o)
	{
		return o.getOuterSkuId();
	}

	@Override
	protected String getPrice(final Order o)
	{
		return o.getPrice();
	}

	@Override
	protected Long getNum(final Order o)
	{
		return o.getNum();
	}

	@Override
	protected String getDiscountFee(final Order o)
	{
		return o.getDiscountFee();
	}

	@Override
	protected String getPayment(final Order o)
	{
		return o.getPayment();
	}

	@Override
	protected String getTotalFee(final Order o)
	{
		return o.getTotalFee();
	}

	@Override
	protected String getAdjustFee(final Order o)
	{
		return o.getAdjustFee();
	}

	@Override
	protected String getValidRentsRegex()
	{
		return DEFAULT_RENTS;
	}

	@Override
	protected String getPostFee(final Trade t)
	{
		return t.getPostFee();
	}

	@Override
	protected String getPartMjzDiscount(final Order o)
	{
		return o.getPartMjzDiscount();
	}

	@Override
	protected void setOuterSkuId(final Order o, final String skuid)
	{
		o.setOuterSkuId(skuid);

	}

	@Override
	protected void setOuterIid(final Order o, final String outerIid)
	{
		o.setOuterIid(outerIid);
	}

	@Override
	protected void setPrice(final Order o, final String price)
	{
		o.setPrice(price);
	}

	@Override
	protected void setDiscountFee(final Order o, final String discountFee)
	{
		o.setDiscountFee(discountFee);
	}

	@Override
	protected void setNum(final Order o, final Long num)
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
		o.setTotalFee(totalFee);
	}

	@Override
	protected void setAdjustFee(final Order o, final String adjustFee)
	{
		o.setAdjustFee(adjustFee);
	}

	@Override
	protected void setOrderLines(final Trade t, final List<Order> lines)
	{
		t.setOrders(lines);
	}

	@Override
	protected List<Order> splitGift() throws SplitGiftException
	{

		final List<PromotionDetail> list = this.trade.getPromotionDetails();
		final List<Order> giftOrders = new ArrayList<Order>();
		final Long oid = (Long) orderContext.getProperty("giftOid");
		if (null != list && 0 != list.size())
		{
			final int num = list.size();
			for (int i = 0; i < num; i++)
			{
				final String outerId = list.get(i).getGiftItemId();
				if (!StringUtils.isEmpty(outerId))
				{
					final ItemGetRequest req = new ItemGetRequest();
					req.setFields(DEFAULT_TMALL_FIELDS);
					req.setNumIid(Long.parseLong(outerId));
					ItemGetResponse response;
					Item item;
					try
					{
						response = client.execute(req, DEFAULT_TMALL_STORE_SESSIONKEY);
						item = response.getItem();
						// 创建giftOrder
						LOG.info(".......赠品GiftItemId = " + outerId + ".......");
						giftOrders.add(creatOrder(item, list.get(i), oid));
					}
					catch (final ApiException e)
					{
						LOG.error(".......拆分赠品异常.......");
						LOG.error(e.getMessage(), e);
						throw new SplitGiftException("拆分赠品异常");
					}
				}
			}
		}

		LOG.info(".......赠品拆分后order数量= " + giftOrders.size() + "......");

		return giftOrders;

	}



	private Order creatOrder(final Item item, final PromotionDetail promotionDetail, final Long oid)
	{
		LOG.info(".......创建赠品详细信息oid = " + oid + "|num = " + item.getNum() + "|price = 0" + "|outerIid = " + item.getOuterId()
				+ ".......");
		final Order order = new Order();
		order.setOid(oid);// 子订单号
		order.setNum(Long.valueOf(promotionDetail.getGiftItemNum()));// 数量
		order.setPrice("0");// 价位0
		order.setDiscountFee("0");
		order.setAdjustFee("0");
		order.setOuterIid(item.getOuterId());// sku
		return order;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(final TaobaoClient client)
	{
		this.client = client;
	}

	@Override
	protected void setRealPayment(final Trade trade)
	{
		final List<Order> lines = trade.getOrders();
		final int num = lines.size();
		if (num == 1)
		{
			for (final Order order : lines)
			{

				final double payment = TmallUtil
						.getPriceBySub(Double.valueOf(order.getPayment()), Double.valueOf(trade.getPostFee()));

				order.setPayment(String.valueOf(payment));
			}
		}
		else
		{
			for (final Order order : lines)
			{
				final String divideOrderFee = order.getDivideOrderFee();

				order.setPayment(String.valueOf(divideOrderFee));
			}
		}
	}

	@Override
	protected List<Order> splitMemo() throws Exception
	{

		final List<Order> orders = new ArrayList<Order>();
		final String sellerMemo = trade.getSellerMemo();

		if (sellerMemo == null)
		{
			LOG.info("过滤后的卖家备注为空");
			return orders;
		}


		final String filteredMemo = sellerMemo.replaceAll("\r", "").replaceAll("\n", "").toLowerCase();
		LOG.info("过滤后的卖家备注为: " + filteredMemo);

		final Pattern pattern = Pattern.compile(REGEX);
		final Matcher matcher = pattern.matcher(filteredMemo);
		// 找出符合正则表达式的字符串进行拆解 z60000833s10 表示赠品是sku是60000833数量是10
		while (matcher.find())
		{
			final String sku = matcher.group(1);
			final String innerSource = String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY));
			final String skuTitle = omsQueryDao.getSkuDescription(sku, innerSource);
			final String num = matcher.group(2);
			LOG.info(".......创建备注拆单详细信息sku = " + sku + "|num = " + num + "|price = 0|sku title = " + skuTitle + "|innerSource = "
					+ innerSource + ".......");
			final Order order = new Order();
			order.setOid(trade.getOrders().get(0).getOid());// 子订单号
			order.setNum(Long.valueOf(num));// 数量
			order.setPrice("0");// 价位0
			order.setDiscountFee("0");
			order.setAdjustFee("0");
			order.setOuterIid(sku);// sku
			order.setTitle(skuTitle + " (用于赠品)");
			orders.add(order);
		}

		return orders;
	}

}
