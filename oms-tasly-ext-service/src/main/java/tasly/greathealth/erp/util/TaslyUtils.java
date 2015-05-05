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
package tasly.greathealth.erp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * All kinds of tools for Tasly project
 *
 * @author vincent.yin
 */
public class TaslyUtils
{
	// convert currentTime into "20131010153020";
	public static String convertTimeToString()
	{
		final Calendar c = Calendar.getInstance();
		final String year = String.valueOf(c.get(Calendar.YEAR));

		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
		{
			month = "0" + month;
		}
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1)
		{
			day = "0" + day;
		}

		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1)
		{
			hour = "0" + hour;
		}

		String minute = String.valueOf(c.get(Calendar.MINUTE));
		if (minute.length() == 1)
		{
			minute = "0" + minute;
		}

		String second = String.valueOf(c.get(Calendar.SECOND));
		if (second.length() == 1)
		{
			second = "0" + second;
		}
		return year.concat(month).concat(day).concat(hour).concat(minute).concat(second);
	}

	// convert date into String,Format 2015-01-10
	public static String convertDateToString(final Date date)
	{
		String dateString = null;
		dateString = date == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(date);
		return dateString;
	}

	// TS-956 convert date into String,Format 2015-03-17 16:14:09
	public static String convertDateToStringT(final Date date)
	{
		String dateString = null;
		dateString = date == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return dateString;
	}

	// generate random code such as ("ABCDE");
	public static String generateRandomCode()
	{
		final StringBuffer randomString = new StringBuffer();

		for (int i = 0; i < 5; i++)
		{
			char c = 'A';
			c = (char) (c + (int) (Math.random() * 26));
			randomString.append(c);
		}
		return randomString.toString();
	}

	// main methods
	public static void main(final String[] args)
	{
		// final DefaultOrderDeliveryStatusUpdateFacade orderTest = new DefaultOrderDeliveryStatusUpdateFacade();

		// final String time = orderTest.convertTimeToString();

		// System.out.println(time);

		// final String randomString = orderTest.generateRandomCode();

		// System.out.println(randomString);

		String msgID = "OMS_VENDOR_";
		msgID = msgID.concat(TaslyUtils.convertTimeToString()).concat("_").concat(TaslyUtils.generateRandomCode());

		System.out.println(msgID);

		// test date to String function

		final String dateString = TaslyUtils.convertDateToStringT(Calendar.getInstance().getTime());

		System.out.println(dateString);

	}
}
