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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;

import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * All kinds of tools for Third
 *
 * @author libin
 */
public class TaslyThirdUtils
{
	private static final Logger LOG = OmsLoggerFactory.getJdorderlog();

	// 验证日期类型是否正确
	public static boolean isValidDate(final String date)
	{
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(date);
		}
		catch (final ParseException e)
		{
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		catch (final NullPointerException e)
		{
			convertSuccess = false;
		}
		return convertSuccess;
	}

	public static String addTime(final Date startDate, final int intervaltime)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final Calendar cd = Calendar.getInstance();
		try
		{
			cd.setTime(sdf.parse(sdf.format(startDate)));
		}
		catch (final ParseException e)
		{
			e.printStackTrace();
		}
		cd.add(Calendar.MINUTE, intervaltime);// 增加分钟
		final Date date = cd.getTime();
		return sdf.format(date);
	}

	public static String formatTime(final Date date)
	{
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	public static Date getDatebyStr(final String dateSrt)
	{
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try
		{
			date = sdf.parse(dateSrt);
		}
		catch (final ParseException e)
		{
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return date;
	}

	public static Integer safeStr2Integer(final String str)
	{
		if (str != null)
		{
			return Integer.valueOf(str);
		}
		else
		{
			return 0;
		}
	}

	public static Long safeStr2Long(final String str)
	{
		if (str != null)
		{
			return Long.valueOf(str);
		}
		else
		{
			return 0L;
		}
	}

	public static Double safe2Double(final String str)
	{
		return StringUtils.isNotBlank(str) && NumberUtils.isNumber(str) ? Double.parseDouble(str) : 0d;
	}

	// public static Double safeStr2Double(final String str)
	// {
	// if (str != null)
	// {
	// return Double.valueOf(str);
	// }
	// else
	// {
	// return 0.00;
	// }
	// }

	public static String safe2String(final Object object)
	{
		if (object == null)
		{
			return "";
		}
		return object.toString();
	}

	public static String CreateUUID()
	{
		final UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static Long CreateUID()
	{
		return UID.next();
	}

	// 计算总和
	public static double getTotal(final double num1, final double num2)
	{
		return BigDecimal.valueOf(num1).add(BigDecimal.valueOf(num2)).doubleValue();
	}

	// 计算差
	public static double negate(final double priceStr, final double num)
	{
		return BigDecimal.valueOf(priceStr).add(BigDecimal.valueOf(num).negate()).doubleValue();
	}

	// 计算剩余金额
	public static double getPriceBySub(final double totlePrice, final double subedprice)
	{
		return BigDecimal.valueOf(totlePrice).subtract(BigDecimal.valueOf(subedprice)).doubleValue();
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

	public static double getPirceByNum(final double priceStr, final double num)
	{
		return BigDecimal.valueOf(priceStr).multiply(BigDecimal.valueOf(num)).doubleValue();
	}

	public static double getRate(final double priceStr, final double num, final int scale)
	{
		// return BigDecimal.valueOf(priceStr).divide(BigDecimal.valueOf(num)).setScale(scale,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
		return BigDecimal.valueOf(priceStr).divide(BigDecimal.valueOf(num), scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
