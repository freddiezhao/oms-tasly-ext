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
package tasly.greathealth.thirdparty.order.common;

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
import tasly.greathealth.tamll.api.SkuToProductFacade;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.thirdparty.order.OrderConvertService;
import tasly.greathealth.tmall.order.dao.OmsQueryDao;
import tasly.greathealth.tmall.order.exception.ProductNotFoundException;
import tasly.greathealth.tmall.order.exception.ProductUnitNotFoundException;
import tasly.greathealth.tmall.order.exception.SplitGiftException;
import tasly.greathealth.tmall.order.services.impl.OrderContext;
import tasly.greathealth.tmall.order.services.impl.TmallUtil;

import com.jd.open.api.sdk.JdException;


/**
 * 通用拆单抽象类
 *
 * @author libin
 */
public abstract class AbstractOrderConverService<T, O> implements OrderConvertService<T, O>, OrderConstants
{
	// private static final Logger LOG = OmsLoggerFactory.getJdorderlog();
	private final Logger LOG = this.getLogObj();
	protected SkuToProductFacade skuToProductFacade;
	protected OmsQueryDao omsQueryDao;


	protected abstract Logger getLogObj();

	/**
	 * 第三方平台的订单对象
	 */
	protected T originalOrder;

	/**
	 * 附件信息对象
	 */
	protected OrderContext orderContext;

	/**
	 * 注：必须实现
	 * 获取订单行项目对象
	 *
	 * @return
	 */
	protected abstract List<O> getOrderLines();

	/**
	 * 注：必须实现
	 * 第三方订单，订单级别ID
	 *
	 * @return
	 */
	protected abstract String getOriginalOrderid();

	/**
	 * 注：必须实现
	 * 第三方订单，行级别ID
	 *
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getOriginalOrderLineid(O o);

	/**
	 * 注：选择性实现
	 * 对于像JD，没有行级别ID的渠道，需要设置行级别ID
	 *
	 *
	 * @param o
	 */
	protected abstract void setOriginalOrderLineid(O o);

	/**
	 * 注：可选实现
	 *
	 * 商家外部编码
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getOuterid(O o);

	/**
	 * 注：必须实现
	 * 外部网店自己定义的Sku编号
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getOuterSkuid(O o);

	/**
	 * 注：必须实现
	 * 行级别商品单价
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getPrice(O o);

	/**
	 * 注：必须实现
	 * 行级别商品总价
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getTotalPrice(O o);

	/**
	 * 注：必须实现
	 * 购买数量
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getItemTotal(O o);

	/**
	 * 行级别打折
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getDiscountFee(O o);

	/**
	 * 行级别实付金额
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getPayment(O o);

	/**
	 * 应付金额
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getTotalFee(O o);

	/**
	 * 行级别手工调整金额
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getAdjustFee(O o);

	/**
	 * 获得外租商品的正则
	 *
	 * @return
	 */
	protected abstract String getValidRentsRegex();


	protected abstract void setOuterSkuId(O o, String skuid);

	protected abstract void setOuterIid(O o, String outerIid);

	protected abstract void setPrice(O o, String price);

	protected abstract void setDiscountFee(O o, String discountFee);

	protected abstract void setItemTotal(O o, Long num);

	protected abstract void setPayment(O o, String payment);

	protected abstract void setTotalFee(O o, String totalFee);

	protected abstract void setAdjustFee(O o, String adjustFee);

	/**
	 * 如果有套餐产品必须实现
	 *
	 * @param t
	 * @param lines
	 */
	protected abstract void setOrderLines(T t, List<O> lines);

	/**
	 * 拆赠品，TM有自己的promotion结构，JD无，需要自己组装
	 * 根据具体情况选择性实现
	 *
	 * @return
	 * @throws SplitGiftException
	 */
	protected abstract List<O> splitGift() throws SplitGiftException;

	/**
	 * 订单级别附加信息，如京东的发票信息
	 * 根据具体情况选择性实现
	 *
	 * @param orderContext
	 */
	protected abstract void setAdditionInfo(OrderContext orderContext) throws JdException;

	/**
	 * 行级别附加信息，如京东的发票信息
	 * 根据具体情况选择性实现
	 *
	 * @param orderContext
	 */
	protected abstract void setLineAdditionInfo(OrderContext orderContext, O o) throws JdException;

	/**
	 * 如果商城SKU字段维护不统一，需要按照个性实现此方法
	 * 获取OMS sku 所在字段值，必须实现
	 *
	 * @return
	 */
	protected abstract String getSkuPos(O o);

	/**
	 * 根据getSkuPos获取的位置获取sku值
	 * 必须实现
	 *
	 * @param o
	 * @return
	 */
	protected abstract String getSkuByPos(O o);

	/**
	 * 如果商城SKU字段维护不统一，需要实现方法getSkuPos;
	 * 该方法用于当如果商城SKU字段维护不统一，拆单时需要设置的字段值（根据getSkuPos方法的返回设置相应字段值）
	 * 必须实现
	 *
	 * @param rentOrder
	 * @param conOrders
	 * @throws ProductNotFoundException
	 */
	protected abstract void setSkuByPos(O copyOrder, SkuToProduct skuToProduct);

	/**
	 * 根据outerSkuIid或者outerIid判断是否为外租商品
	 *
	 * 如果有外租商品必须实现
	 *
	 * @param orgOrder
	 * @param flag
	 * @return
	 */
	// protected abstract boolean judgeRents(O o);

	/**
	 * 设置行级别oid、sku对应关系
	 * 必须实现
	 *
	 * @param origOrdersMap
	 * @param o
	 */
	// protected abstract void setOrderLineSkuMap(Map<String, String> origOrdersMap, O o);

	/**
	 * 商品转换
	 *
	 */
	// protected abstract void convertOrder(O o, List<O> conOrders, Set<String> selfOrRunFlag) throws
	// ProductNotFoundException;

	/**
	 * 设置行级别优惠金额
	 * 选择实现 JD没有行项目优惠金额需要实现，TM有就不必实现
	 */
	protected abstract void setOrderLineDiscountfee();

	/**
	 * 设置行级别应付金额
	 * 必须实现，用于出对账报表
	 *
	 * @param t
	 */
	protected abstract void setRealPayment();


	@Override
	public T convertTrade() throws Exception
	{
		// Map<oid,skuiid>
		final Map<String, String> orderLineSkuMap = new HashMap<String, String>();
		// 自营、非自营标志
		final Set<String> selfOrRunFlag = new HashSet<String>();
		// 储存拆单后的订单行项目
		final List<O> conOrders = new ArrayList<>();
		// 1.设置订单级个性信息
		this.setAdditionInfo(orderContext);
		// 2.获取订单行级别信息,并进行订单转换
		final List<O> orgOrders = getOrderLines();
		LOG.info(".......交易Original Order Id = " + getOriginalOrderid() + "开始准备拆分订单" + "|order数量 = " + orgOrders.size()
				+ "|channel = " + String.valueOf(orderContext.getProperty(DEFAULT_CHANNEL_SOURCE_KEY)) + "|innerSource = "
				+ String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY)) + ".......");
		LOG.info("                  ");
		LOG.info(".......开始循环拆分订单.......");

		// 设置行级别优惠金额
		this.setOrderLineDiscountfee();
		// 设置行级别实付金额
		this.setRealPayment();
		for (int i = 0; i < orgOrders.size(); i++)
		{
			// 获取订单行对象
			final O orgOrder = orgOrders.get(i);
			// 设置行级别id
			setOriginalOrderLineid(orgOrder);
			// TODU:测试
			// setOuterSkuId(orgOrder, "60000391");
			// 设置saveOrigOrdersMap的值
			setOrderLineSkuMap(orderLineSkuMap, orgOrder);
			// 商品转换
			convertOrder(orgOrder, conOrders, selfOrRunFlag);
			// 设置行级个性信息
			this.setLineAdditionInfo(orderContext, orgOrder);
		}
		orderContext.addProperty(JUDGE_FLAG, orderLineSkuMap);
		// 判断该行项目是否为外租、自营、混搭
		this.setRentFlag(getOriginalOrderid(), selfOrRunFlag);
		LOG.info(".......结束循环拆分订单.......");
		LOG.info("                  ");
		// 赠品处理
		LOG.info(".......开始处理赠品.......");
		// 不同渠道各自写赠品信息
		final List<O> giftOrders = this.splitGift();
		if (giftOrders != null && giftOrders.size() > 0)
		{
			conOrders.addAll(giftOrders);
		}
		LOG.info(".......结束处理赠品.......");
		LOG.info("                  ");
		LOG.info(".......开始商品单位查询.......");
		this.setCommoditUnit(conOrders);
		LOG.info(".......结束商品单位查询.......");
		LOG.info("                  ");

		// 拆完单后，将拆后的行数据set
		setOrderLines(this.originalOrder, conOrders);
		LOG.info(".......交易tId = " + getOriginalOrderid() + "交易拆分订单结束 共拆分 = " + conOrders.size() + "条订单.......");
		return this.originalOrder;
	}

	/**
	 * 判断是否为外租还是自营
	 *
	 * @param tId
	 * @param tradeRunFlag
	 */
	private void setRentFlag(final String tId, final Set<String> tradeRunFlag)
	{
		if (tradeRunFlag.contains(RENT_RUN) & tradeRunFlag.contains(SELF_RUN))
		{
			orderContext.addProperty(tId, MIX_RUN);
		}
		else if (tradeRunFlag.contains(RENT_RUN) & !tradeRunFlag.contains(SELF_RUN))
		{
			orderContext.addProperty(tId, RENT_RUN);
		}
		else if (!tradeRunFlag.contains(RENT_RUN) & tradeRunFlag.contains(SELF_RUN))
		{
			orderContext.addProperty(tId, SELF_RUN);
		}
	}

	private void convertOrder(final O o, final List<O> conOrders, final Set<String> selfOrRunFlag) throws ProductNotFoundException
	{
		final String innerSource = String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY));
		final String channel = String.valueOf(orderContext.getProperty(DEFAULT_CHANNEL_SOURCE_KEY));
		// 外租商品不转换
		if (this.judgeRents(o))
		{
			LOG.info(".......交易tId = " + getOriginalOrderid() + "|outerIid = " + this.getOuterid(o) + "|outerSkuIid = "
					+ this.getOuterSkuid(o) + "进入外租商品处理流程.......");
			selfOrRunFlag.add(RENT_RUN);
			this.convertRentOrder(o, conOrders);
		}
		else
		{
			LOG.info(".......交易tId = " + getOriginalOrderid() + "|outerIid = " + this.getOuterid(o) + "|outerSkuIid = "
					+ this.getOuterSkuid(o) + "进入自营商品处理流程.......");
			selfOrRunFlag.add(SELF_RUN);
			// 判断是套餐还是单品
			final List<SkuToProduct> tcOrders = this.getTcOrdersByCode(getOuterSkuid(o), channel, innerSource);
			if (tcOrders != null && tcOrders.size() > 0)
			{
				this.convertTcOrder(o, conOrders, tcOrders);
			}
			else
			{
				this.convertSingleOrder(o, conOrders);
			}
			// orderContext.addProperty("giftOid", this.getOriginalOrderLineid(o));
		}
	}

	// 处理单品
	@SuppressWarnings("unchecked")
	private void convertSingleOrder(final O o, final List<O> conOrders)
	{
		LOG.info(".......交易outerId = " + getOuterid(o) + "|outerSkuId = " + getOuterSkuid(o) + "为自营单一商品......");
		final double price = TmallUtil.getPirceByNum(Double.valueOf(getPrice(o)), Long.valueOf(this.getItemTotal(o)), 2);
		this.setPrice(o, String.valueOf(price));
		conOrders.add(o);
	}

	private void convertTcOrder(final O o, final List<O> conOrders, final List<SkuToProduct> tcOrders)
			throws ProductNotFoundException
	{
		LOG.info(".......交易outerId = " + getOuterid(o) + "|outerSkuId = " + getOuterSkuid(o) + "为自营套餐商品，待拆分......");
		this.splitOrders(o, conOrders, tcOrders);// 自营套餐类产品
	}

	private void convertRentOrder(final O rentOrder, final List<O> conOrders) throws ProductNotFoundException
	{
		final String innerSource = String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY));
		final String channel = String.valueOf(orderContext.getProperty(DEFAULT_CHANNEL_SOURCE_KEY));
		// 外租商品需要转换SKU
		final Map map = omsQueryDao.getRentProduct(getSkuByPos(rentOrder), channel, innerSource);
		setOuterSkuId(rentOrder, (String) map.get("sku"));
		// 设置价格
		final double price = TmallUtil.getPirceByNum(Double.valueOf(getPrice(rentOrder)),
				Long.valueOf(this.getItemTotal(rentOrder)), 2);
		setPrice(rentOrder, String.valueOf(price));
		conOrders.add(rentOrder);
	}

	// 处理、拆套餐
	@SuppressWarnings("unchecked")
	private void splitOrders(final O orgOrder, final List<O> conOrders, final List<SkuToProduct> tcOrders)
	{
		// 行级别折扣金额
		final double totalDiscountFee = TaslyThirdUtils.safe2Double(getDiscountFee(orgOrder));
		double subedDiscountFee = 0.00;
		double totalPayment = 0.00;
		// 行级别应付金额
		totalPayment = TaslyThirdUtils.safe2Double(getPayment(orgOrder));
		double subedPayment = 0.00;
		// 行级别价格总和，单价*数量
		final double totalPrice = TaslyThirdUtils.safe2Double(getTotalPrice(orgOrder));
		double subedPrice = 0.00;
		// 行级别应付金额
		final double totalTotalFee = TaslyThirdUtils.safe2Double(getTotalFee(orgOrder));
		double subedTotalFee = 0.00;
		// 手动调整金额
		final double totalAdjustFee = TaslyThirdUtils.safe2Double(getAdjustFee(orgOrder));
		double subedAdjustFee = 0.00;

		final long num = TaslyThirdUtils.safeStr2Long(getItemTotal(orgOrder));
		final int skutoproNum = tcOrders.size();
		LOG.info(".......套餐对照表中的拆分行数 = " + skutoproNum + ".......");
		LOG.info(".......[拆分前数据] 交易outerId = " + getOuterid(orgOrder) + "|outerSkuId = " + getOuterSkuid(orgOrder)
				+ "|totalDiscountFee = " + totalDiscountFee + "|totalPayment = " + totalPayment + "|totalPrice = " + totalPrice
				+ "|totalTotalFee = " + totalTotalFee + "|totalAdjustFee = " + totalAdjustFee + "......");
		LOG.info(".......[拆分前数据] 交易outerId = " + getOuterid(orgOrder) + "|outerSkuId = " + getOuterSkuid(orgOrder)
				+ "|subedDiscountFee = " + subedDiscountFee + "|subedPayment = " + subedPayment + "|subedPrice = " + subedPrice
				+ "|subedTotalFee = " + subedTotalFee + "|subedAdjustFee = " + subedAdjustFee + "......");
		LOG.info(".......订单查分循环开始.......");
		for (int i = 0; i < skutoproNum; i++)
		{
			if (i != (skutoproNum - 1))
			{
				final O copyOrder = (O) TmallUtil.copyOrder(orgOrder);
				final SkuToProduct skuToProduct = tcOrders.get(i);
				final double ratio = skuToProduct.getRatio() / 100;
				// **************获取参数****************
				final double currDiscountFee = TmallUtil.getPirceByRate(totalDiscountFee, ratio, 2);
				final double currPayment = TmallUtil.getPirceByRate(totalPayment, ratio, 2);
				final double currPrice = TmallUtil.getPirceByRate(totalPrice, ratio, 2);// 拆分后单行的价格
				final double currTotalFee = TmallUtil.getPirceByRate(totalTotalFee, ratio, 2);
				final double currAdjustFee = TmallUtil.getPirceByRate(totalAdjustFee, ratio, 2);
				// ***************转换SKU ***************
				this.setSkuByPos(copyOrder, skuToProduct);

				// ***************转换数量 ***************
				this.setItemTotal(copyOrder, num * (skuToProduct.getQuantity()));
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
				LOG.info(".......[拆分中数据] 交易" + "|outerId = " + getOuterid(copyOrder) + "|outerSkuId = " + getOuterSkuid(copyOrder)
						+ "|currDiscountFee = " + currDiscountFee + "|currPayment = " + currPayment + "|currPrice = " + currPrice
						+ "|currTotalFee = " + currTotalFee + "|currAdjustFee = " + currAdjustFee + "......");
				LOG.info(".......[拆分中数据] 交易" + "|outerId = " + getOuterid(copyOrder) + "|outerSkuId = " + getOuterSkuid(copyOrder)
						+ "|subedDiscountFee = " + subedDiscountFee + "|subedPayment = " + subedPayment + "|subedPrice = " + subedPrice
						+ "|subedTotalFee = " + subedTotalFee + "|subedAdjustFee = " + subedAdjustFee + "......");
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
				final double currPrice = TmallUtil.getPriceBySub(totalPrice, subedPrice);// 拆分后单行的价格
				final double currTotalFee = TmallUtil.getPriceBySub(totalTotalFee, subedTotalFee);
				final double currAdjustFee = TmallUtil.getPriceBySub(totalAdjustFee, subedAdjustFee);

				// ***************转换SKU ***************
				this.setSkuByPos(copyOrder, skuToProduct);
				// ***************转换数量 ***************
				setItemTotal(copyOrder, num * (skuToProduct.getQuantity()));
				// ***************拆分金额 ***************
				setDiscountFee(copyOrder, String.valueOf(currDiscountFee));
				setPayment(copyOrder, String.valueOf(currPayment));
				setPrice(copyOrder, String.valueOf(currPrice));// 拆分后单行的价格
				setTotalFee(copyOrder, String.valueOf(currTotalFee));
				setAdjustFee(copyOrder, String.valueOf(currAdjustFee));
				// **************************************
				LOG.info(".......[拆分最后一笔] 交易" + "|outerId = " + getOuterid(copyOrder) + "|outerSkuId = " + getOuterSkuid(copyOrder)
						+ "|subedDiscountFee = " + subedDiscountFee + "|subedPayment = " + subedPayment + "|subedPrice = " + subedPrice
						+ "|subedTotalFee = " + subedTotalFee + "|subedAdjustFee = " + subedAdjustFee + "......");

				LOG.info(".......[拆分最后一笔] 交易" + "|outerId = " + getOuterid(copyOrder) + "|outerSkuId = " + getOuterSkuid(copyOrder)
						+ "|currDiscountFee = " + currDiscountFee + "|currPayment = " + currPayment + "|currPrice = " + currPrice
						+ "|currTotalFee = " + currTotalFee + "|currAdjustFee = " + currAdjustFee + "......");
				// **************************************
				conOrders.add(copyOrder);
			}
		}
		LOG.info(".......订单查分循环结束.......");
	}


	/**
	 * 获取商品单位
	 *
	 * @param conOrders
	 * @throws ProductUnitNotFoundException
	 */
	private void setCommoditUnit(final List<O> conOrders) throws ProductUnitNotFoundException
	{
		final String innerSource = String.valueOf(orderContext.getProperty(DEFAULT_INNER_SOURCE_KEY));
		final String channel = String.valueOf(orderContext.getProperty(DEFAULT_CHANNEL_SOURCE_KEY));
		for (int i = 0; i < conOrders.size(); i++)
		{
			final O orgOrder = conOrders.get(i);
			final String skuid = getSkuByPos(orgOrder);
			final String unit = omsQueryDao.getProductUnit(skuid, channel, innerSource);
			// 以下代码用于替换omsQueryDao
			// final String unit = getItemInfoBySku(id).getBaseUnitCode();
			orderContext.addProperty(skuid, unit);
			LOG.info(".......查询outerId = " + getOuterid(orgOrder) + "|outerSkuId = " + getOuterSkuid(orgOrder) + "|channel = "
					+ channel + "|innerSource = " + innerSource + "|unit = " + unit + "的商品单位.......");
		}
	}

	/**
	 * 根据outerSkuIid或者outerIid判断是否为外租商品
	 *
	 * @param o
	 * @return
	 */
	protected boolean judgeRents(final O o)
	{
		final String regex = getValidRentsRegex();
		final String sku = getSkuByPos(o);
		if (sku != null)
		{
			final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(sku);
			LOG.info(".......外租商品判断返回值：" + matcher.matches() + ".......");
			return matcher.matches();
		}
		else
		{
			LOG.info(".......外租商品判断返回值false.......");
		}
		return false;
	}

	/**
	 * 设置行级别oid、sku对应关系
	 *
	 * @param origOrdersMap
	 * @param o
	 */
	protected void setOrderLineSkuMap(final Map<String, String> origOrdersMap, final O o)
	{
		origOrdersMap.put(this.getOriginalOrderLineid(o), getSkuByPos(o));
	}

	/**
	 * 获取套餐数据
	 *
	 * @param outerSkuId
	 * @param channel
	 * @param innerSource
	 * @return
	 */
	protected List<SkuToProduct> getTcOrdersByCode(final String outerSkuId, final String channel, final String innerSource)
	{
		List<SkuToProduct> localList = null;
		if (!StringUtils.isEmpty(outerSkuId))
		{
			localList = (List<SkuToProduct>) skuToProductFacade.querySkuToProducts(outerSkuId, channel, innerSource);
		}

		return localList;
	}

	/**
	 * 分摊行项目优惠金额，规则，按照金额比例
	 *
	 * @param Sub_coupon_price
	 * @param itemInfoListSub
	 */
	protected void shareSellerDiscount(final Double Sub_coupon_price, final List<O> itemInfoListSub)
	{
		final List<O> lineList = getOrderLines();
		// 1、算百分比
		Double total_price = 0D;
		for (final O o : itemInfoListSub)
		{
			total_price = TaslyThirdUtils.getTotal(total_price, TaslyThirdUtils.safe2Double(this.getTotalPrice(o)));
		}
		// 2、根据百分比分摊
		Double rate = 0D;
		Double discountFee = 0D;
		Double totalDiscountFee = 0D;
		for (int i = 0; i < itemInfoListSub.size(); i++)
		{
			final O source = itemInfoListSub.get(i);
			for (final O target : lineList)
			{
				if (this.getOriginalOrderLineid(source).equals(this.getOriginalOrderLineid(target)))
				{
					// 最后一个不需要按照百分比计算
					if (i == (itemInfoListSub.size() - 1))
					{
						this.setDiscountFee(target, String.valueOf(TaslyThirdUtils.getPriceBySub(Sub_coupon_price, totalDiscountFee)));
					}
					else
					{
						rate = TaslyThirdUtils.getRate(TaslyThirdUtils.safe2Double(this.getTotalPrice(source)), total_price, 2);
						discountFee = TaslyThirdUtils.getPirceByRate(Sub_coupon_price, rate, 3);
						totalDiscountFee += discountFee;
						this.setDiscountFee(target, String.valueOf(discountFee));
					}
				}
			}
		}
	}

	@Override
	public void setOriginalOrder(final T t)
	{
		this.originalOrder = t;
	}

	/**
	 * @param orderContext the orderContext to set
	 */
	@Override
	public void setOrderContext(final OrderContext orderContext)
	{
		this.orderContext = orderContext;
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
