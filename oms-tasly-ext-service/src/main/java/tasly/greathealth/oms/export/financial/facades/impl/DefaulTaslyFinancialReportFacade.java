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
package tasly.greathealth.oms.export.financial.facades.impl;

import com.hybris.kernel.api.Page;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.financial.TaslyFinancialReportFacade;
import tasly.greathealth.oms.api.financial.TaslyFinancialReportQueryObject;
import tasly.greathealth.oms.api.financial.dto.TaslyFinancialReport;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.services.ItemInfoService;
import tasly.greathealth.oms.order.services.TaslyOrderService;


/**
 * @author GXX
 */
@Transactional
public class DefaulTaslyFinancialReportFacade implements TaslyFinancialReportFacade
{
	private TaslyOrderService taslyOrderService;

	private ItemInfoService itemInfoService;

	private static String DEFAULT_RENTS;

	/**
	 * @return the itemInfoService
	 */
	public ItemInfoService getItemInfoService()
	{
		return itemInfoService;
	}

	/**
	 * @param itemInfoService the itemInfoService to set
	 */
	@Required
	public void setItemInfoService(final ItemInfoService itemInfoService)
	{
		this.itemInfoService = itemInfoService;
	}


	/**
	 * @return the taslyOrderService
	 */
	public TaslyOrderService getTaslyOrderService()
	{
		return taslyOrderService;
	}

	/**
	 * @param taslyOrderService the taslyOrderService to set
	 */
	@Required
	public void setTaslyOrderService(final TaslyOrderService taslyOrderService)
	{
		this.taslyOrderService = taslyOrderService;
	}


	/**
	 * @return the dEFAULT_RENTS
	 */
	public static String getDEFAULT_RENTS()
	{
		return DEFAULT_RENTS;
	}

	/**
	 * @param dEFAULT_RENTS the dEFAULT_RENTS to set
	 */
	@Required
	public static void setDEFAULT_RENTS(final String dEFAULT_RENTS)
	{
		DEFAULT_RENTS = dEFAULT_RENTS;
	}

	@Override
	public PagedResults<TaslyFinancialReport> findFinancialReportByQuery(final TaslyFinancialReportQueryObject queryObject)
			throws EntityValidationException
			{
		// 根据前台的时间范围获得订单中的issuedDate内的订单集合
		final Page<TaslyOrderData> taslyOrderDatas = this.taslyOrderService.getTaslyOrderDataByIssuedDate(
				queryObject.getStartDate(), queryObject.getEndDate(), queryObject);
		final List<TaslyOrderData> taslyOrderList = taslyOrderDatas.getContent();
		final List<TaslyFinancialReport> taslyFinancialReportList = new LinkedList<TaslyFinancialReport>();
		if (null != taslyOrderList && taslyOrderList.size() > 0)
		{
			for (final TaslyOrderData order : taslyOrderList)
			{
				final List<TaslyOrderLineData> orderLines = this.taslyOrderService.getTaslyOrderLinesByOrderId(order);

				final TaslyFinancialReport taslyFinancialReport = new TaslyFinancialReport();

				// 初始化FinancialReport类，将运费及订单号传入
				initFinancialReport(taslyFinancialReport, order);

				for (int i = 0; i < orderLines.size(); i++)
				{
					final TaslyOrderLineData orderLine = orderLines.get(i);
					// 根据orderLine下的sku查询对应itemInfo信息
					final ItemInfoData itemInfo = itemInfoService.getBySku(orderLine.getSkuId());

					// 获取订单行下的实付金额
					double price = orderLine.getOrderlinePayment() == null ? 0 : orderLine.getOrderlinePayment();

					// 如果退款状态是退款退货完成，实付金额减去退款金额
					final String status = orderLine.getRefundstatus();
					if (StringUtils.isNotEmpty(status) && status.equals("退款退货完成"))
					{
						final double refundAmount = orderLine.getRefundamount() == null ? 0 : orderLine.getRefundamount();
						price = price - refundAmount;
					}

					if (null != itemInfo)
					{
						final String vendor = itemInfo.getVendor();

						// 根据vendor进行外租商铺类型判断
						final String priceType = getTaslyFinancialReportPriceType(vendor, taslyFinancialReport);

						// 迭代Map，进行价格赋值和最后一个价格判断
						final Iterator<String> iter = taslyFinancialReport.getPriceMap().keySet().iterator();

						while (iter.hasNext())
						{
							final String key = iter.next();
							final Double value = taslyFinancialReport.getPriceMap().get(key);

							if (key.equals(priceType))
							{
								if (price != 0)
								{
									final double sum = value + price;
									taslyFinancialReport.getPriceMap().put(key, sum);
								}

								if (i == orderLines.size() - 1)
								{
									taslyFinancialReport.setEndPrice(key);
								}
								break;
							}
						}
					}
				}
				taslyFinancialReportList.add(taslyFinancialReport);
			}
		}

		return reorganizeFreight(taslyFinancialReportList, taslyOrderDatas);
			}

	/**
	 * @param vendor
	 * @param taslyFinancialReport
	 */
	private String getTaslyFinancialReportPriceType(final String vendor, final TaslyFinancialReport taslyFinancialReport)
	{
		// YTODO Auto-generated method stub
		String type = "self";
		if (StringUtils.isNotEmpty(vendor))
		{
			switch (vendor)
			{
				case "50000076":
					type = "qpta";
					break;
				case "51000348":
					type = "sem";
					break;
				case "51000349":
					type = "qptb";
					break;
				case "51000350":
					type = "zjpc";
					break;
				case "51000351":
					type = "sajt";
					break;
				case "51000352":
					type = "seven";
					break;
				case "51000353":
					type = "kfzj";
					break;
				case "51000354":
					type = "xykj";
					break;
				case "50000085":
					type = "shjl";
					break;
			}
		}
		return type;
	}

	/**
	 * @param taslyFinancialReport
	 * @param order
	 */
	private void initFinancialReport(final TaslyFinancialReport taslyFinancialReport, final TaslyOrderData order)
	{
		// YTODO Auto-generated method stub
		taslyFinancialReport.setOriginalOrderId(order.getOriginal_order_id());

		// 设置运费
		final ShippingAndHandlingData shipping = order.getShippingAndHandling();
		if (null != shipping)
		{
			taslyFinancialReport.setFreight((shipping.getShippingPrice().getSubTotalValue()));
		}
		else
		{
			taslyFinancialReport.setFreight(0);
		}

		taslyFinancialReport.setTotalPrice(0);

		final String[] priceArray = DEFAULT_RENTS.split(",");
		if (ArrayUtils.isNotEmpty(priceArray))
		{
			final Map<String, Double> priceMap = new LinkedHashMap<String, Double>();
			for (final String priceType : priceArray)
			{
				priceMap.put(priceType, new Double(0));
			}
			taslyFinancialReport.setPriceMap(priceMap);
		}
	}

	/**
	 * @param taslyFinancialReportList
	 * @param taslyOrderDatas
	 * @return
	 */
	private PagedResults<TaslyFinancialReport> reorganizeFreight(final List<TaslyFinancialReport> taslyFinancialReportList,
			final Page<TaslyOrderData> taslyOrderDatas)
			{
		// YTODO Auto-generated method stub

		if (null != taslyFinancialReportList && taslyFinancialReportList.size() > 0)
		{

			for (final TaslyFinancialReport taslyFinancialReport : taslyFinancialReportList)
			{
				double sumDouble = 0;
				final double freight = taslyFinancialReport.getFreight();

				// 循环获得除了运费的价格综合
				final Iterator<String> iter = taslyFinancialReport.getPriceMap().keySet().iterator();
				while (iter.hasNext())
				{
					sumDouble += taslyFinancialReport.getPriceMap().get(iter.next());

				}

				if (sumDouble > 0)
				{
					// 循环price，根据单价/总价*运费的计算方式，重新给每个单价赋值
					while (iter.hasNext())
					{
						final String key = iter.next();
						final Double price = taslyFinancialReport.getPriceMap().get(key);
						final Double sum = sumDouble(price, this.getPriceByFreight(price, sumDouble, freight), 2,
								BigDecimal.ROUND_HALF_UP);
						taslyFinancialReport.getPriceMap().put(key, sum);
					}
				}

				taslyFinancialReport.setTotalPrice(new BigDecimal(sumDouble + freight).setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue());

				countLastPrice(taslyFinancialReport, 2, BigDecimal.ROUND_HALF_UP);

			}
		}
		final PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNumber(taslyOrderDatas.getNumber());
		pageInfo.setTotalPages(taslyOrderDatas.getTotalPages());
		pageInfo.setTotalResults(taslyOrderDatas.getTotalElements());

		return new PagedResults<TaslyFinancialReport>(taslyFinancialReportList, pageInfo);
			}

	/**
	 * @param taslyFinancialReport
	 * @param i
	 * @param roundHalfUp
	 */
	private void countLastPrice(final TaslyFinancialReport taslyFinancialReport, final int scale, final int type)
	{
		// YTODO Auto-generated method stub
		// 计算最后一分钱
		final Iterator<String> iter = taslyFinancialReport.getPriceMap().keySet().iterator();
		while (iter.hasNext())
		{
			final String key = iter.next();
			if (key.equals(taslyFinancialReport.getEndPrice()))
			{

				double price = taslyFinancialReport.getTotalPrice();

				while (iter.hasNext())
				{
					final String key1 = iter.next();
					if (!key1.equals(taslyFinancialReport.getEndPrice()))
					{
						price -= taslyFinancialReport.getPriceMap().get(key1);
					}
				}
				price = new BigDecimal(price).setScale(scale, type).doubleValue();
				taslyFinancialReport.getPriceMap().put(key, price);
				break;
			}
		}
	}

	private double getPriceByFreight(final double unitPrice, final double sumPrice, final double freight)
	{
		final BigDecimal unit = new BigDecimal(unitPrice);
		final BigDecimal sum = new BigDecimal(sumPrice);
		final BigDecimal fre = new BigDecimal(freight);

		return unit.divide(sum, 4, BigDecimal.ROUND_HALF_UP).multiply(fre).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	private double sumDouble(final double price1, final double price2, final int scale, final int type)
	{
		final double sum = price1 + price2;
		return new BigDecimal(sum).setScale(scale, type).doubleValue();
	}
}
