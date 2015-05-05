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
package tasly.greathealth.thirdparty.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.domain.order.ChannelSource;
import tasly.greathealth.oms.domain.order.InnerSource;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tamll.api.SkuToProductFacade;
import tasly.greathealth.tmall.order.dao.OmsQueryDao;
import tasly.greathealth.tmall.order.exception.ProductNotFoundException;
import tasly.greathealth.tmall.order.exception.ProductUnitNotFoundException;
import tasly.greathealth.tmall.order.exception.SplitGiftException;
import tasly.greathealth.tmall.order.services.impl.OrderContext;
import tasly.greathealth.tmall.order.services.impl.TmallUtil;


/**
 *
 */
public abstract class AbstractOrderConverService<T, O> implements OrderConvertService<T, O>, OrderConstants
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();
	protected SkuToProductFacade skuToProductFacade;
	protected OmsQueryDao omsQueryDao;
	protected T trade;
	protected OrderContext orderContext;

	protected abstract List<O> getOrderLines();

	protected abstract Long getTid();// 第三方订单，订单级别ID

	protected abstract Long getOid(O o);// 第三方订单，订单行级别ID

	protected abstract String getOuterid(O o);

	protected abstract String getOuterSkuid(O o);

	protected abstract String getPrice(O o);

	protected abstract Long getNum(O o);

	protected abstract String getDiscountFee(O o);

	protected abstract String getPayment(O o);

	protected abstract String getTotalFee(O o);

	protected abstract String getAdjustFee(O o);

	protected abstract String getValidRentsRegex();

	protected abstract String getPostFee(T t);

	protected abstract String getPartMjzDiscount(O o);

	protected abstract void setOuterSkuId(O o, String skuid);

	protected abstract void setOuterIid(O o, String outerIid);

	protected abstract void setPrice(O o, String price);

	protected abstract void setDiscountFee(O o, String discountFee);

	protected abstract void setNum(O o, Long num);

	protected abstract void setPayment(O o, String payment);

	protected abstract void setTotalFee(O o, String totalFee);

	protected abstract void setAdjustFee(O o, String adjustFee);

	protected abstract void setOrderLines(T t, List<O> lines);

	protected abstract List<O> splitGift() throws SplitGiftException;

	protected abstract List<O> splitMemo() throws Exception;

	protected abstract void setRealPayment(T t);


	@Override
	public void setOriginalOrder(final T t)
	{
		this.trade = t;
	}

	/**
	 * @param orderContext the orderContext to set
	 */
	@Override
	public void setOrderContext(final OrderContext orderContext)
	{
		this.orderContext = orderContext;
	}

	@Override
	public T convertTrade() throws Exception
	{
		// ------save original trade information from tmall begin add by handong 2015-02-05------
		final Map<Long, Map<String, String>> origOrdersMap = new HashMap<Long, Map<String, String>>();
		// ------save original trade information from tmall end add by handong 2015-02-05------
		final Set<String> tradeRunFlag = new HashSet<String>();
		// final List<Order> orgOrders = orgTrade.getOrders();
		final List<O> orgOrders = getOrderLines();
		final List<O> conOrders = new ArrayList<>();
		final int orgOrdersNum = orgOrders.size();
		final String channelSource = String.valueOf(orderContext.getProperty(DEFAULT_CHANNEL_SOURCE_KEY));
		final String innerSource = String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY));
		LOG.info(".......交易tId = " + getTid() + "开始准备拆分订单" + "|order数量 = " + orgOrdersNum + "|channel = "
				+ String.valueOf(orderContext.getProperty(DEFAULT_CHANNEL_SOURCE_KEY)) + "|innerSource = "
				+ String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY)) + ".......");
		LOG.info("                  ");
		LOG.info(".......开始循环拆分订单.......");

		// ------set real payment begin add by handong 2015-04-07------
		setRealPayment(trade);
		// ------set real payment end add by handong 2015-04-07------

		for (int i = 0; i < orgOrdersNum; i++)
		{
			// final Order orgOrder = orgOrders.get(i);
			final O orgOrder = orgOrders.get(i);
			// final Long oid = orgOrder.getOid();
			final Long oid = getOid(orgOrder);
			final String outerIid = getOuterid(orgOrder);
			final String outerSkuIid = getOuterSkuid(orgOrder);
			final String flag = judgeFlag(outerIid, outerSkuIid);

			// ------save original trade information from tmall begin add by handong 2015-02-05------
			saveOrigTmallInfo(origOrdersMap, oid, outerIid, outerSkuIid, flag);
			// ------save original trade information from tmall end add by handong 2015-02-05------
			// 外租商品不转换
			if (judgeRents(orgOrder, flag))
			{
				LOG.info(".......交易tId = " + getTid() + "|outerIid = " + outerIid + "|outerSkuIid = " + outerSkuIid
						+ "进入外租商品处理流程.......");
				tradeRunFlag.add(RENT_RUN);
				this.convertRentOrder(orgOrder, conOrders, flag);
			}
			else
			{
				LOG.info(".......交易tId = " + getTid() + "|outerIid = " + outerIid + "|outerSkuIid = " + outerSkuIid
						+ "进入自营商品处理流程.......");
				tradeRunFlag.add(SELF_RUN);
				this.convertTcOrder(orgOrder, conOrders, flag);
			}
		}

		addTradeRunFlag(getTid(), tradeRunFlag);

		LOG.info(".......结束循环拆分订单.......");

		// 商品单位转换
		LOG.info("                  ");
		// 赠品处理
		LOG.info(".......开始处理赠品.......");

		final List<O> giftOrders = this.splitGift();
		final int giftOrderNum = giftOrders.size();
		if (giftOrderNum != 0)
		{
			// for (int j = 0; j < giftOrderNum; j++)
			// {
			// conOrders.add(giftOrders.get(j));
			// }
			conOrders.addAll(giftOrders);
		}

		LOG.info(".......结束处理赠品.......");
		LOG.info("                  ");
		if (channelSource.equals(ChannelSource.TMALL.toString()) && innerSource.equals(InnerSource.JSC.toString()))
		{
			LOG.info(".......开始处理备注拆单.......");
			LOG.info("                  ");

			final List<O> memoOrders = this.splitMemo();
			final int memoOrderNum = memoOrders.size();
			if (memoOrderNum != 0)
			{
				// for (int j = 0; j < giftOrderNum; j++)
				// {
				// conOrders.add(giftOrders.get(j));
				// }
				conOrders.addAll(memoOrders);
			}

			LOG.info(".......结束处理备注拆单.......");
			LOG.info("                  ");
		}

		LOG.info(".......开始商品单位查询.......");
		this.addCommoditUnit(conOrders);
		LOG.info(".......结束商品单位查询.......");
		LOG.info("                  ");
		// orgTrade.setOrders(conOrders);
		setOrderLines(this.trade, conOrders);

		LOG.info(".......交易tId = " + getTid() + "交易拆分订单结束 共拆分 = " + conOrders.size() + "条订单.......");
		// ------save original trade information from tmall begin add by handong 2015-02-05------
		orderContext.addProperty(TMALL_ORIG_ORDER_INFO, origOrdersMap);
		// ------save original trade information from tmall end add by handong 2015-02-05------
		return this.trade;
	}

	private void addCommoditUnit(final List<O> conOrders) throws ProductUnitNotFoundException
	{
		final String innerSource = String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY));
		final String channel = String.valueOf(orderContext.getProperty(DEFAULT_CHANNEL_SOURCE_KEY));

		final int num = conOrders.size();
		for (int i = 0; i < num; i++)
		{
			final String flag = judgeFlag(getOuterid(conOrders.get(i)), getOuterSkuid(conOrders.get(i)));
			final O orgOrder = conOrders.get(i);

			String id = null;
			if (DEFAULT_OUTERSKUID.equalsIgnoreCase(flag))
			{
				id = getOuterSkuid(orgOrder);
			}
			else if (DEFAULT_OUTERID.equalsIgnoreCase(flag))
			{
				id = getOuterid(orgOrder);
			}

			if (judgeRents(orgOrder, flag))
			{
				final String unit = omsQueryDao.getProductUnit(id, channel, innerSource);
				// 以下代码用于替换omsQueryDao
				// final String unit = getItemInfoBySku(id).getBaseUnitCode();
				orderContext.addProperty(id, unit);
				LOG.info(".......查询外租商品单位的outerId = " + getOuterid(orgOrder) + "|outerSkuId = " + getOuterSkuid(orgOrder)
						+ "|flag = " + flag + "|channel = " + channel + "|innerSource = " + innerSource + "|unit = " + unit + ".......");
			}
			else
			{
				final String unit = omsQueryDao.getProductUnit(id, channel, innerSource);
				// 以下代码用于替换omsQueryDao
				// final String unit = getItemInfoBySku(id).getBaseUnitCode();
				orderContext.addProperty(id, unit);
				LOG.info(".......查询自营商品单位的outerId = " + getOuterid(orgOrder) + "|outerSkuId = " + getOuterSkuid(orgOrder)
						+ "|flag = " + flag + "|unit = " + unit + ".......");
			}
		}
	}

	private void addTradeRunFlag(final Long tId, final Set<String> tradeRunFlag)
	{
		if (tradeRunFlag.contains(RENT_RUN) & tradeRunFlag.contains(SELF_RUN))
		{
			orderContext.addProperty(String.valueOf(tId), MIX_RUN);
		}
		else if (tradeRunFlag.contains(RENT_RUN) & !tradeRunFlag.contains(SELF_RUN))
		{
			orderContext.addProperty(String.valueOf(tId), RENT_RUN);
		}
		else if (!tradeRunFlag.contains(RENT_RUN) & tradeRunFlag.contains(SELF_RUN))
		{
			orderContext.addProperty(String.valueOf(tId), SELF_RUN);
		}
	}

	@SuppressWarnings("unchecked")
	private void splitOrders(final O orgOrder, final List<O> conOrders, final List<SkuToProduct> tcOrders, final String flag)
	{
		final double totalDiscountFee = Double.valueOf(getDiscountFee(orgOrder));
		double subedDiscountFee = 0.00;
		final double totalPayment = Double.valueOf(getPayment(orgOrder));
		double subedPayment = 0.00;
		final double totalPrice = Double.valueOf(getPrice(orgOrder));// 拆分后单行的价格
		double subedPrice = 0.00;
		final double totalTotalFee = Double.valueOf(getTotalFee(orgOrder));
		double subedTotalFee = 0.00;
		final double totalAdjustFee = Double.valueOf(getAdjustFee(orgOrder));
		double subedAdjustFee = 0.00;
		final long num = getNum(orgOrder);
		final int skutoproNum = tcOrders.size();
		LOG.info(".......套餐对照表中的拆分行数 = " + skutoproNum + ".......");
		LOG.info(".......[拆分前数据] 交易outerId = " + getOuterid(orgOrder) + "|outerSkuId = " + getOuterSkuid(orgOrder)
				+ "|totalDiscountFee = " + totalDiscountFee + "|totalPayment = " + totalPayment + "|totalPrice = "
				+ TmallUtil.getPirceByNum(totalPrice, num) + "|totalTotalFee = " + totalTotalFee + "|totalAdjustFee = "
				+ totalAdjustFee + "......");
		LOG.info(".......[拆分前数据] 交易outerId = " + getOuterid(orgOrder) + "|outerSkuId = " + getOuterSkuid(orgOrder)
				+ "|subedDiscountFee = " + subedDiscountFee + "|subedPayment = " + subedPayment + "|subedPrice = " + subedPrice
				+ "|subedTotalFee = " + subedTotalFee + "|subedAdjustFee = " + subedAdjustFee + "......");
		LOG.info(".......订单查分循环开始.......");
		for (int i = 0; i < skutoproNum; i++)
		{
			// skutoproducts.get(key)
			if (i != (skutoproNum - 1))
			{
				final O copyOrder = (O) TmallUtil.copyOrder(orgOrder);
				final SkuToProduct skuToProduct = tcOrders.get(i);
				final double ratio = skuToProduct.getRatio() / 100;
				// **************获取参数****************
				final double currDiscountFee = TmallUtil.getPirceByRate(totalDiscountFee, ratio, 2);
				final double currPayment = TmallUtil.getPirceByRate(totalPayment, ratio, 2);
				final double currPrice = TmallUtil.getPirceByRate(TmallUtil.getPirceByNum(totalPrice, num), ratio, 2);// 拆分后单行的价格
				final double currTotalFee = TmallUtil.getPirceByRate(totalTotalFee, ratio, 2);
				final double currAdjustFee = TmallUtil.getPirceByRate(totalAdjustFee, ratio, 2);
				// ***************转换SKU ***************
				if (DEFAULT_OUTERID.equalsIgnoreCase(flag))
				{
					setOuterIid(copyOrder, skuToProduct.getItemId());
				}
				else if (DEFAULT_OUTERSKUID.equalsIgnoreCase(flag))
				{
					this.setOuterSkuId(copyOrder, skuToProduct.getItemId());
				}

				// ***************转换数量 ***************
				this.setNum(copyOrder, num * (skuToProduct.getQuantity()));
				// ***************拆分金额 ***************
				setDiscountFee(copyOrder, String.valueOf(currDiscountFee));
				setPayment(copyOrder, String.valueOf(currPayment));
				setPrice(copyOrder, String.valueOf(currPrice));// 拆分后单行的价格
				setTotalFee(copyOrder, String.valueOf(currTotalFee));
				setAdjustFee(copyOrder, String.valueOf(currAdjustFee));
				// ***************递增***************
				subedDiscountFee += currDiscountFee;
				subedPayment += currPayment;
				subedPrice += currPrice;
				subedTotalFee += currTotalFee;
				subedAdjustFee += currAdjustFee;
				// **************************************
				LOG.info(".......[拆分中数据] 交易flag = " + flag + "|outerId = " + getOuterid(copyOrder) + "|outerSkuId = "
						+ getOuterSkuid(copyOrder) + "|currDiscountFee = " + currDiscountFee + "|currPayment = " + currPayment
						+ "|currPrice = " + currPrice + "|currTotalFee = " + currTotalFee + "|currAdjustFee = " + currAdjustFee
						+ "......");
				LOG.info(".......[拆分中数据] 交易flag = " + flag + "|outerId = " + getOuterid(copyOrder) + "|outerSkuId = "
						+ getOuterSkuid(copyOrder) + "|subedDiscountFee = " + subedDiscountFee + "|subedPayment = " + subedPayment
						+ "|subedPrice = " + subedPrice + "|subedTotalFee = " + subedTotalFee + "|subedAdjustFee = " + subedAdjustFee
						+ "......");
				// **************************************
				conOrders.add(copyOrder);
			}
			else
			{
				LOG.info(".......拆分最后一笔订单转换.......");
				final O copyOrder = (O) TmallUtil.copyOrder(orgOrder);
				final SkuToProduct skuToProduct = tcOrders.get(i);
				// **************获取参数****************
				final double currDiscountFee = TmallUtil.getPriceBySub(totalDiscountFee, subedDiscountFee);
				final double currPayment = TmallUtil.getPriceBySub(totalPayment, subedPayment);
				final double currPrice = TmallUtil.getPriceBySub(TmallUtil.getPirceByNum(totalPrice, num), subedPrice);// 拆分后单行的价格
				final double currTotalFee = TmallUtil.getPriceBySub(totalTotalFee, subedTotalFee);
				final double currAdjustFee = TmallUtil.getPriceBySub(totalAdjustFee, subedAdjustFee);

				// ***************转换SKU ***************
				if (DEFAULT_OUTERID.equalsIgnoreCase(flag))
				{
					setOuterIid(copyOrder, skuToProduct.getItemId());
				}
				else if (DEFAULT_OUTERSKUID.equalsIgnoreCase(flag))
				{
					setOuterSkuId(copyOrder, skuToProduct.getItemId());
				}
				// ***************转换数量 ***************
				setNum(copyOrder, num * (skuToProduct.getQuantity()));
				// ***************拆分金额 ***************
				setDiscountFee(copyOrder, String.valueOf(currDiscountFee));
				setPayment(copyOrder, String.valueOf(currPayment));
				setPrice(copyOrder, String.valueOf(currPrice));// 拆分后单行的价格
				setTotalFee(copyOrder, String.valueOf(currTotalFee));
				setAdjustFee(copyOrder, String.valueOf(currAdjustFee));
				// **************************************
				LOG.info(".......[拆分最后一笔] 交易flag = " + flag + "|outerId = " + getOuterid(copyOrder) + "|outerSkuId = "
						+ getOuterSkuid(copyOrder) + "|subedDiscountFee = " + subedDiscountFee + "|subedPayment = " + subedPayment
						+ "|subedPrice = " + subedPrice + "|subedTotalFee = " + subedTotalFee + "|subedAdjustFee = " + subedAdjustFee
						+ "......");

				LOG.info(".......[拆分最后一笔] 交易flag = " + flag + "|outerId = " + getOuterid(copyOrder) + "|outerSkuId = "
						+ getOuterSkuid(copyOrder) + "|currDiscountFee = " + currDiscountFee + "|currPayment = " + currPayment
						+ "|currPrice = " + currPrice + "|currTotalFee = " + currTotalFee + "|currAdjustFee = " + currAdjustFee
						+ "......");
				// **************************************
				conOrders.add(copyOrder);
			}
		}
		LOG.info(".......订单查分循环结束.......");
	}

	private List<SkuToProduct> getTcOrdersByCode(final String outerId, final String channel, final String innerSource)
	{
		// 先判断本地是否包含，如果返回为空怎通过webserices查询最新套餐数据
		List<SkuToProduct> localList = null;
		if (!StringUtils.isEmpty(outerId))
		{
			localList = (List<SkuToProduct>) skuToProductFacade.querySkuToProducts(outerId, channel, innerSource);
		}

		return localList;
	}

	private void convertTcOrder(final O orgOrder, final List<O> conOrders, final String flag)
	{
		final String innerSource = String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY));
		final String channel = String.valueOf(orderContext.getProperty(DEFAULT_CHANNEL_SOURCE_KEY));

		List<SkuToProduct> tcOrders = null;

		if (DEFAULT_OUTERSKUID.equalsIgnoreCase(flag))
		{
			final String outerSkuId = getOuterSkuid(orgOrder);
			tcOrders = this.getTcOrdersByCode(outerSkuId, channel, innerSource);
		}
		else if (DEFAULT_OUTERID.equalsIgnoreCase(flag))
		{
			final String outerId = getOuterid(orgOrder);
			tcOrders = this.getTcOrdersByCode(outerId, channel, innerSource);
		}

		if (tcOrders == null || tcOrders.isEmpty())
		{
			LOG.info(".......交易outerId = " + getOuterid(orgOrder) + "|outerSkuId = " + getOuterSkuid(orgOrder) + "为自营单一商品......");
			final double price = TmallUtil.getPirceByNum(Double.valueOf(getPrice(orgOrder)), this.getNum(orgOrder), 2);
			this.setPrice(orgOrder, String.valueOf(price));
			conOrders.add(orgOrder);// 自营单一商品
		}
		else
		{
			LOG.info(".......交易outerId = " + getOuterid(orgOrder) + "|outerSkuId = " + getOuterSkuid(orgOrder) + "为自营套餐商品，待拆分......");
			this.splitOrders(orgOrder, conOrders, tcOrders, flag);// 自营套餐类产品
		}

		orderContext.addProperty("giftOid", this.getOid(orgOrder));
	}

	public String judgeFlag(final String outerIid, final String outerSkuIid)
	{
		if (!StringUtils.isEmpty(outerSkuIid))
		{
			return DEFAULT_OUTERSKUID;
		}
		if (!StringUtils.isEmpty(outerIid))
		{
			return DEFAULT_OUTERID;
		}
		return null;
	}

	private void saveOrigTmallInfo(final Map<Long, Map<String, String>> origOrdersMap, final Long oid, final String outerIid,
			final String outerSkuIid, final String flag)
	{
		final Map<String, String> origOrderMap = new HashMap<String, String>();
		if (DEFAULT_OUTERID.equalsIgnoreCase(flag))
		{
			origOrderMap.put(TMALL_OUTERID_OUTERSKUID, outerIid);
		}
		else if (DEFAULT_OUTERSKUID.equalsIgnoreCase(flag))
		{
			origOrderMap.put(TMALL_OUTERID_OUTERSKUID, outerSkuIid);
		}
		origOrdersMap.put(oid, origOrderMap);
	}

	private boolean judgeRents(final O orgOrder, final String flag)
	{
		final String regex = getValidRentsRegex();
		if (DEFAULT_OUTERSKUID.equalsIgnoreCase(flag))
		{
			final String outerSkuIid = this.getOuterSkuid(orgOrder);
			final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(outerSkuIid);
			LOG.info(".......外租商品判断返回值：" + matcher.matches() + ".......");
			return matcher.matches();
		}

		if (DEFAULT_OUTERID.equalsIgnoreCase(flag))
		{
			final String outerIid = this.getOuterid(orgOrder);
			final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(outerIid);
			LOG.info(".......外租商品判断返回值：" + matcher.matches() + ".......");
			return matcher.matches();
		}
		LOG.info(".......外租商品判断返回值false.......");
		return false;
	}

	private void convertRentOrder(final O rentOrder, final List<O> conOrders, final String flag) throws ProductNotFoundException
	{
		final String outerId = this.getOuterid(rentOrder);
		final String outerSkuId = this.getOuterSkuid(rentOrder);
		final String innerSource = String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY));
		final String channel = String.valueOf(orderContext.getProperty(DEFAULT_CHANNEL_SOURCE_KEY));

		if (DEFAULT_OUTERSKUID.equalsIgnoreCase(flag))
		{
			final Map map = omsQueryDao.getRentProduct(outerSkuId, channel, innerSource);
			// rentOrder.setOuterSkuId((String) map.get("sku"));
			setOuterSkuId(rentOrder, (String) map.get("sku"));
			// 以下代码用于替换omsQueryDao
			// try
			// {
			// final ItemInfo itemIfno = itemInfoFacade.getByParams(outerSkuId, channel, innerSource);
			// rentOrder.setOuterSkuId(itemIfno.getSku());
			// }
			// catch (final EntityNotFoundException e)
			// {
			// throw new ProductNotFoundException("No related product information");
			// }
		}
		else if (DEFAULT_OUTERID.equalsIgnoreCase(flag))
		{
			final Map map = omsQueryDao.getRentProduct(outerId, channel, innerSource);
			// rentOrder.setOuterIid((String) map.get("sku"));
			setOuterIid(rentOrder, (String) map.get("sku"));
		}
		final double price = TmallUtil.getPirceByNum(Double.valueOf(getPrice(rentOrder)), getNum(rentOrder), 2);
		// rentOrder.setPrice(String.valueOf(price));
		setPrice(rentOrder, String.valueOf(price));
		conOrders.add(rentOrder);
	}

	/**
	 * @param skuToProductFacade the skuToProductFacade to set
	 */
	public void setSkuToProductFacade(final SkuToProductFacade skuToProductFacade)
	{
		this.skuToProductFacade = skuToProductFacade;
	}

	/**
	 * @param omsQueryDao the omsQueryDao to set
	 */
	public void setOmsQueryDao(final OmsQueryDao omsQueryDao)
	{
		this.omsQueryDao = omsQueryDao;
	}

}
