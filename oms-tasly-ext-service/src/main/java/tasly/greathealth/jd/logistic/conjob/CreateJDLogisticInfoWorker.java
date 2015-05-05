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
package tasly.greathealth.jd.logistic.conjob;

import com.hybris.kernel.api.JobWorkerBean;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.logistic.service.BaseLogisticService;

import java.io.Serializable;


/**
 * Created by houyi on 2015/4/10.
 */
public class CreateJDLogisticInfoWorker implements JobWorkerBean {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = OmsLoggerFactory.getJdlogisticlog();
    private String beanName;

    private BaseLogisticService baseLogisticService;

    @Override
    public void execute(final Serializable arg0) {
        logger.info("同步运单到京东，开始...");
        long beginTime = System.currentTimeMillis();
        baseLogisticService.createLogistic();
        logger.info("同步运单到京东，结束...耗时: " + (System.currentTimeMillis() - beginTime) / 1000f + " 秒");
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setBaseLogisticService(BaseLogisticService baseLogisticService) {
        this.baseLogisticService = baseLogisticService;
    }
}
