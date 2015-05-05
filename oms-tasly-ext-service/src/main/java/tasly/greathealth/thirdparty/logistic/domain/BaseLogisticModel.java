package tasly.greathealth.thirdparty.logistic.domain;

import tasly.greathealth.oms.domain.erp.DNLogData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;

import java.io.Serializable;

/**
 * Created by houyi on 2015/4/10.
 */
public interface BaseLogisticModel extends Serializable {
    public TaslyOrderData getTaslyOrderData();

    public void setTaslyOrderData(TaslyOrderData taslyOrderData);

    public void setDnLogData(DNLogData dnLogData);

    public DNLogData getDnLogData();
}
