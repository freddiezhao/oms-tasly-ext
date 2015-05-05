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
package tasly.greathealth.tmall.order.event;

import com.hybris.kernel.api.KernelEventAware;


/**
 *
 */
public class ToTmallSynchronizor implements KernelEventAware
{
	// private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();

	// @KernelEventHandler(events = {KernelEventType.UPDATE}, typeCodes = {"TaslyOrderData"})
	// public void onTaslyOrderSellerMemoUpdate(final KernelEvent event)
	// {
	// final Map<String, Object> currentValue = event.getCurrentValues();
	// LOG.info(String.valueOf(currentValue));
	// // final String tid = (String) currentValue.get("original_order_id");
	// final String currentSellerMemo = (String) currentValue.get("seller_message");
	//
	// final Map<String, Object> previousValue = event.getPreviousValues();
	// final String preSellerMemo = (String) previousValue.get("seller_message");
	//
	// if (currentSellerMemo != null && currentSellerMemo.equalsIgnoreCase(preSellerMemo) == false)
	// {// 若OMS客服更新SellerMemo，同步到天猫
	// // LOG.info("-----向天猫同步消息！-------");
	// // TmallUtil.synMemo2Tmall(Long.valueOf(tid), currentSellerMemo, null);
	// }
	//
	// // final String currentPackStatus = (String) currentValue.get("packing");
	// // // final String prePackStatus = (String) previousValue.get("packing");
	// //
	// // LOG.info("currentPackStatus : " + currentPackStatus);
	// //
	// // if ("PACKED".equalsIgnoreCase(currentPackStatus))
	// // {// ERP 成功更新PACKED，需要同步天猫旗帜
	// // LOG.info("向天猫同步Packing信息");
	// // TmallUtil.synMemo2Tmall(Long.valueOf(tid), currentSellerMemo, 5L);
	// // }
	// }
}
