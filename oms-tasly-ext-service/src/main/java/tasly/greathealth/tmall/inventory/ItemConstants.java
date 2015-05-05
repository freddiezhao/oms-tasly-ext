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
package tasly.greathealth.tmall.inventory;

/**
 *
 */
public interface ItemConstants
{
	public static final Long DEFAULT_PAGE_NO = 1L;
	public static final Long DEFAULT_PAGE_SIZE = 200L;
	public static final int ITEM_STOCKROOM_FLAG = 1;// TASLY 自营商品
	public static final String TMALL_LOCATION = "TMALL";
	public static final String JD_LOCATION = "JD";
	public static final String YHD_LOCATION = "YHD";
	public static final String ATSID = "WEB";
	public static final String INNERSOURCEOTC = "OTC";
	public static final String INNERSOURCEJSC = "JSC";
	public static final int COUNTLIMIT = 3;
	public static final int UPDATEFAILEDLIMIT = 10;

	public static final String FOR_SHELVED = "for_shelved";
	public static final String SOLD_OUT = "sold_out";

}
