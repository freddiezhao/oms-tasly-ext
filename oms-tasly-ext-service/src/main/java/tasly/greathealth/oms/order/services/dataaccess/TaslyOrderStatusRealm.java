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
 *
 *
 */
package tasly.greathealth.oms.order.services.dataaccess;

import com.hybris.oms.service.ats.StatusRealm;

public final class TaslyOrderStatusRealm implements StatusRealm
{
    public static final StatusRealm INSTANCE = new TaslyOrderStatusRealm();
 
    private static final String PREFIX = "O";
 
    @Override
    public String getPrefix()
    {
        return PREFIX;
    }
 
    private TaslyOrderStatusRealm()
    {
        // avoid instantiation
    }
}