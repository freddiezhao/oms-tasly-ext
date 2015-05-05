package tasly.greathealth.thirdparty.logistic.dao;

import tasly.greathealth.thirdparty.logistic.domain.BaseLogisticModel;

/**
 * Created by houyi on 2015/4/13.
 */
public interface BaseLogisticDao {
    public static final String JD_RESPONSE_SUCCESS = "0";

    /**
     * 上行物流信息
     *
     * @param baseLogisticModel
     * @return
     */
    public <T extends BaseLogisticModel> boolean sendOfflineLogistic(T baseLogisticModel);

}

