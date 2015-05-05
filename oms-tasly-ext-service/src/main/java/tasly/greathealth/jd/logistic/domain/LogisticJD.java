package tasly.greathealth.jd.logistic.domain;

import com.jd.open.api.sdk.request.order.OrderSopOutstorageRequest;
import tasly.greathealth.oms.domain.erp.DNLogData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.thirdparty.logistic.domain.BaseLogisticModel;

/**
 * Created by houyi on 2015/4/10.
 */
public class LogisticJD extends OrderSopOutstorageRequest implements BaseLogisticModel {
    private TaslyOrderData taslyOrderData;
    private DNLogData dnLogData;
    private String expressCode;
    private String expressOrderId;

    /**
     * 物流公司ID
     *
     * @return
     */
    public String getExpressCode() {
        return expressCode;
    }

    /**
     * 物流公司ID
     *
     * @param expressCode
     */
    public void setExpressCode(String expressCode) {
        this.expressCode = expressCode;
    }

    /**
     * 运单号
     *
     * @return
     */
    public String getExpressOrderId() {
        return expressOrderId;
    }

    /**
     * 运单号
     *
     * @param expressOrderId
     */
    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    @Override
    public DNLogData getDnLogData() {
        return dnLogData;
    }

    @Override
    public void setDnLogData(DNLogData dnLogData) {
        this.dnLogData = dnLogData;
    }

    @Override
    public TaslyOrderData getTaslyOrderData() {
        return taslyOrderData;
    }

    @Override
    public void setTaslyOrderData(TaslyOrderData taslyOrderData) {
        this.taslyOrderData = taslyOrderData;
    }
}
