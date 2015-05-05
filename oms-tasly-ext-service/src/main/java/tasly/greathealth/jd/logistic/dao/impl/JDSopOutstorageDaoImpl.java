package tasly.greathealth.jd.logistic.dao.impl;

import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.order.OrderDetailInfo;
import com.jd.open.api.sdk.request.order.OrderGetRequest;
import com.jd.open.api.sdk.response.order.OrderGetResponse;
import com.jd.open.api.sdk.response.order.OrderSopOutstorageResponse;
import org.slf4j.Logger;
import tasly.greathealth.erp.dnlog.service.impl.DefaultDNLogService;
import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.jd.logistic.domain.LogisticJD;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.thirdparty.logistic.dao.BaseLogisticDao;
import tasly.greathealth.thirdparty.logistic.domain.BaseLogisticModel;
import tasly.greathealth.thirdparty.order.OrderConstants;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by houyi on 2015/4/10.
 */
public class JDSopOutstorageDaoImpl implements BaseLogisticDao {
    private static final Logger logger = OmsLoggerFactory.getJdlogisticlog();
    private DefaultDNLogService defaultDnLogService;
    private JdClient jdClient;

    public static Map<String, String> orderStateMap = new HashMap<String, String>();

    static {
        orderStateMap.put(OrderConstants.JD_ORDER_STATE_WAIT_SELLER_STOCK_OUT, "等待出库");
        orderStateMap.put(OrderConstants.JD_ORDER_STATE_SEND_TO_DISTRIBUTION_CENER, "发往配送中心");
        orderStateMap.put(OrderConstants.JD_ORDER_STATE_DISTRIBUTION_CENTER_RECEIVED, "配送中心已收货");
        orderStateMap.put(OrderConstants.JD_ORDER_STATE_WAIT_GOODS_RECEIVE_CONFIRM, "等待确认收货");
        orderStateMap.put(OrderConstants.JD_ORDER_STATE_RECEIPTS_CONFIRM, "收款确认（服务完成）");
        orderStateMap.put(OrderConstants.JD_ORDER_STATE_WAIT_SELLER_DELIVERY, "等待发货");
        orderStateMap.put(OrderConstants.JD_ORDER_STATE_FINISHED_L, "完成");
        orderStateMap.put(OrderConstants.JD_ORDER_STATE_LOCKED, "已锁定");
    }

    /**
     * 发送京东物流信息
     *
     * @param baseLogisticModel
     * @return
     */
    @Override
    public boolean sendOfflineLogistic(BaseLogisticModel baseLogisticModel) {
        LogisticJD logisticJD = (LogisticJD) baseLogisticModel;
        try {
            if (validateOrderState(logisticJD.getOrderId())) {
                OrderSopOutstorageResponse response = jdClient.execute(logisticJD);
                if (JD_RESPONSE_SUCCESS.equals(response.getCode())) {
                    logger.info("同步京东物流信息[订单标识：" + logisticJD.getOrderId() + "]成功。");
                    return true;
                } else {
                    logger.error("同步京东物流信息[订单标识：" + logisticJD.getOrderId() + "]失败。");
                    return false;
                }
            } else {
                logger.info("同步京东物流信息[订单标识：" + logisticJD.getOrderId() + "]的状态不为等待出库。");
                defaultDnLogService.updateSingleDNLog(logisticJD.getDnLogData(), TaslyERPConstants.MANNUALDELIVERY);
                return false;
            }
        } catch (final JdException e) {
            e.printStackTrace();
            logger.error("同步京东物流信息[订单标识：" + logisticJD.getOrderId() + "]失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 根据订单状态判断是否允许发货
     * API : 360buy.order.get
     *
     * @param orderId
     * @return
     */
    private boolean validateOrderState(String orderId) throws JdException {
        OrderGetRequest request = new OrderGetRequest();
        request.setOrderId(orderId);
        request.setOptionalFields("order_state");
        OrderGetResponse response = jdClient.execute(request);
        OrderDetailInfo orderDetailInfo = response.getOrderDetailInfo();
        if (OrderConstants.JD_ORDER_STATE_WAIT_SELLER_STOCK_OUT.equalsIgnoreCase(orderDetailInfo.getOrderInfo().getOrderState())) {
            logger.info("同步京东订单[订单标识：" + orderId + "]的状态为：" + orderStateMap.get(orderDetailInfo.getOrderInfo().getOrderState()));
            return true;
        } else {
            logger.info("同步京东订单[订单标识：" + orderId + "]的状态为：" + orderStateMap.get(orderDetailInfo.getOrderInfo().getOrderState()));
            return false;
        }
    }

    public void setDefaultDnLogService(final DefaultDNLogService defaultDnLogService) {
        this.defaultDnLogService = defaultDnLogService;
    }

    public void setJdClient(final JdClient jdClient) {
        this.jdClient = jdClient;
    }
}
