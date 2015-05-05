package tasly.greathealth.jd.logistic.service.impl;

import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import org.slf4j.Logger;
import tasly.greathealth.jd.logistic.domain.LogisticJD;
import tasly.greathealth.oms.domain.erp.DNLogData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.logistic.domain.BaseLogisticModel;
import tasly.greathealth.thirdparty.logistic.service.BaseLogisticService;
import tasly.greathealth.thirdparty.logistic.service.impl.AbstractBaseLogisticServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houyi on 2015/4/15.
 */
public class JDSopOutstorageServiceImpl extends AbstractBaseLogisticServiceImpl implements BaseLogisticService {
    private final String JD_MULTI_SEPERATER = "|";

    @Override
    protected void handleBussness(DNLogData dnLogData, TaslyOrderData taslyOrderData, TaslyOrderLineData taslyOrderLine, List<BaseLogisticModel> logisticModels) {
        handleDnLogData(dnLogData, taslyOrderData, taslyOrderLine, logisticModels);
        /** 天猫不需要处理赠品
         if (!validateIsGift(orderLineData)) {
         handleDnLogData(dnLogData, taslyOrderData, orderLineData, logisticModels);
         } else {
         defaultDnLogService.updateSingleDNLog(dnLogData, TaslyERPConstants.CREATE);
         }
         */
    }

    /**
     * 创建京东物流信息接口对象
     *
     * @param taslyOrderData
     * @param taslyOrderLine
     * @return
     */
    @Override
    protected BaseLogisticModel createLogisticModel(TaslyOrderData taslyOrderData, TaslyOrderLineData taslyOrderLine) {
        List<OrderLineQuantityData> taslyOrderQuantityList = taslyOrderLine.getOrderLineQuantities();
        TaslyOrderLineQuantityData taslyOrderQuantity = getTaslyOrderLineQuantity(taslyOrderQuantityList);
        LogisticJD logisticJD = new LogisticJD();
        //logisticJD.setLogisticsId(taslyOrderQuantity.getExpress_code()); // 京东统一使用韵达快递
        //logisticJD.setWaybill(taslyOrderQuantity.getExpress_order_id());
        logisticJD.setExpressCode(taslyOrderQuantity.getExpress_code()); // 物流公司ID
        logisticJD.setExpressOrderId(taslyOrderQuantity.getExpress_order_id()); //运单号
        logisticJD.setOrderId(taslyOrderData.getOriginal_order_id());
        logisticJD.setTaslyOrderData(taslyOrderData);
        return logisticJD;
    }

    /**
     * 京东物流信息合并
     *
     * @param logisticModels
     * @return
     */
    @Override
    protected List<BaseLogisticModel> mergeLogicticModel(List<BaseLogisticModel> logisticModels) {
        List<BaseLogisticModel> logisticModelList = new ArrayList<BaseLogisticModel>();
        // 订单标识做为发送的条件
        Map<String, List<LogisticJD>> logisticModelMap = new HashMap<String, List<LogisticJD>>();
        for (int i = 0; i < logisticModels.size(); i++) {
            LogisticJD logisticJD = (LogisticJD) logisticModels.get(i);
            if (logisticModelMap.get(logisticJD.getOrderId()) == null) {
                List<LogisticJD> list = new ArrayList<LogisticJD>();
                list.add(logisticJD);
                logisticModelMap.put(logisticJD.getOrderId(), list);
            } else {
                logisticModelMap.get(logisticJD.getOrderId()).add(logisticJD);
            }
        }
        // 合并
        for (String key : logisticModelMap.keySet()) {
            LogisticJD logisticJD = new LogisticJD();
            List<LogisticJD> list = logisticModelMap.get(key);
            for (int i = 0 ; i < list.size(); i ++) {
                LogisticJD jd = list.get(i);
                if(i == 0){
                    logisticJD.setOrderId(jd.getOrderId());
                }
                if(logisticJD.getLogisticsId() == null || logisticJD.getLogisticsId().isEmpty()){
                    logisticJD.setLogisticsId(jd.getExpressCode());
                }else {
                    if(logisticJD.getLogisticsId().indexOf(jd.getExpressCode()) == -1){
                        logisticJD.setLogisticsId(logisticJD.getLogisticsId().concat(JD_MULTI_SEPERATER).concat(jd.getExpressCode()));
                    }
                }
                if(logisticJD.getWaybill() == null || logisticJD.getWaybill().isEmpty()){
                    logisticJD.setWaybill(jd.getExpressOrderId());
                }else {
                    if(logisticJD.getWaybill().indexOf(jd.getExpressOrderId()) == -1){
                        logisticJD.setWaybill(logisticJD.getWaybill().concat(JD_MULTI_SEPERATER).concat(jd.getExpressCode()));
                    }
                }
            }
            logisticModelList.add(logisticJD);
        }
        return logisticModelList;
    }

    /**
     * 京东物流信息日志
     *
     * @return
     */
    @Override
    protected Logger getLogger() {
        if (logger == null) {
            logger = OmsLoggerFactory.getJdlogisticlog();
        }
        return logger;
    }

}
