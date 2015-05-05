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

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.internal.tmc.Message;
import com.taobao.api.request.TradeMemoUpdateRequest;

import flexjson.JSONDeserializer;


/**
 *
 */
public class TmallUtil
{

	protected static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	protected static TaobaoClient client;

	protected static String defaultTmallOtcSessionKey;

	public static void synMemo2Tmall(final Long tid, final String memo, final Long memoFlag)
	{
		final TradeMemoUpdateRequest req = new TradeMemoUpdateRequest();
		boolean success = false;
		int count = 1;
		req.setTid(tid);
		req.setFlag(memoFlag);
		req.setMemo(memo);
		req.setReset(false);
		while (success == false && count <= 5)
		{
			try
			{
				client.execute(req, defaultTmallOtcSessionKey);
				success = true;
			}
			catch (final ApiException e)
			{
				LOG.warn("Error happened when write memo back to TMALL. Try time[" + count++ + "]");
				try
				{
					Thread.sleep(1000);
				}
				catch (final InterruptedException e1)
				{// Ignore this
				}
			}
		}

		if (success == false)
		{
			throw new RuntimeException("Error happened when write memo back to TMALL.");
		}
	}

	public static Object copyOrder(final Object thirdPartyOrder)
	{
		Object order = null;
		try
		{
			order = BeanUtils.cloneBean(thirdPartyOrder);
		}
		catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return order;
	}

	// 复制订单对象
	public static Order copyOrder(final Order order)
	{
		Order copyOrder = null;
		try
		{
			copyOrder = (Order) BeanUtils.cloneBean(order);
		}
		catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return copyOrder;
	}

	// 根据百分比计算金额(四舍五入)
	public static double getPirceByRate(final double priceStr, final double rateStr, final int scale)
	{
		return BigDecimal.valueOf(priceStr).multiply(BigDecimal.valueOf(rateStr)).setScale(scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	// 根据数量计算金额(四舍五入)
	public static double getPirceByNum(final double priceStr, final long num, final int scale)
	{
		return BigDecimal.valueOf(priceStr).multiply(BigDecimal.valueOf(num)).setScale(scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	public static double getPirceByNum(final double priceStr, final long num)
	{
		return BigDecimal.valueOf(priceStr).multiply(BigDecimal.valueOf(num)).doubleValue();
	}

	// 根据计算剩余金额
	public static double getPriceBySub(final double totlePrice, final double subedprice)
	{
		return BigDecimal.valueOf(totlePrice).subtract(BigDecimal.valueOf(subedprice)).doubleValue();
	}


	public static Map<String, Object> parseMessageJson2Map(final Message message)
	{
		if (message == null)
		{
			return Collections.emptyMap();
		}
		final String contentJson = safe2String(message.getRaw().get("content"));
		return new JSONDeserializer<Map<String, Object>>().deserialize(contentJson);
	}

	private static String safe2String(final Object object)
	{
		if (object == null)
		{
			return "";
		}
		return object.toString();
	}

	/**
	 * @return the client
	 */
	public static TaobaoClient getClient()
	{
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public static void setClient(final TaobaoClient client)
	{
		TmallUtil.client = client;
	}

	/**
	 * @return the defaultTmallOtcSessionKey
	 */
	public static String getDefaultTmallOtcSessionKey()
	{
		return defaultTmallOtcSessionKey;
	}

	/**
	 * @param defaultTmallOtcSessionKey the defaultTmallOtcSessionKey to set
	 */
	public static void setDefaultTmallOtcSessionKey(final String defaultTmallOtcSessionKey)
	{
		TmallUtil.defaultTmallOtcSessionKey = defaultTmallOtcSessionKey;
	}

}
