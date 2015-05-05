package tasly.greathealth.thirdparty.logistic.service.impl;

import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import tasly.greathealth.erp.dnlog.service.impl.DefaultDNLogService;
import tasly.greathealth.erp.util.TaslyERPConstants;
import tasly.greathealth.oms.domain.erp.DNLogData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.thirdparty.logistic.dao.BaseLogisticDao;
import tasly.greathealth.thirdparty.logistic.domain.BaseLogisticModel;
import tasly.greathealth.thirdparty.logistic.service.BaseLogisticService;
import tasly.greathealth.thirdparty.order.OrderConstants;
import tasly.greathealth.tmall.order.services.OmsOrderRetrieveService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by houyi on 2015/4/14.
 */
public abstract class AbstractBaseLogisticServiceImpl implements BaseLogisticService {
    protected Logger logger = null;
    protected DefaultDNLogService defaultDnLogService;
    private OmsOrderRetrieveService omsOrderRetrieverService;
    private BaseLogisticDao baseLogisticDao;

    /**
     * 业态信息
     */
    private String innerSource;

    /**
     * 渠道信息
     */
    private String channelSource;

    @Override
    @Transactional
    public void createLogistic() {
        getLogger().info("开始处理DNLOG信息 ...");
        getLogger().info("获取订单和物流信息 ...");
        List<BaseLogisticModel> originalLogisticModelList = getLogisticModel();
        getLogger().info("合并相同的数据信息 ...");
        List<BaseLogisticModel> mergeLogisticModelList = mergeLogicticModel(originalLogisticModelList);
        getLogger().info("发送物流数据信息 ...");
        handleProcess(mergeLogisticModelList);
        getLogger().info("处理DNLOG信息完毕 ...");

    }

    /**
     * 根据dnlog信息组装发送数据
     *
     * @return
     */
    private List<BaseLogisticModel> getLogisticModel() {
        List<BaseLogisticModel> logisticModels = new ArrayList<BaseLogisticModel>();
        List<DNLogData> dnLogs = getNoRepeatDNLogList();
        TaslyOrderData taslyOrderData = null;
        for (DNLogData dnLogData : dnLogs) {
            taslyOrderData = omsOrderRetrieverService.getTaslyOrderDataDetail(dnLogData.getOms_order_id());
            if (taslyOrderData != null) {
                List<OrderLineData> taslyOrderLines = taslyOrderData.getOrderLines();
                for (OrderLineData orderLineData : taslyOrderLines) {
                    TaslyOrderLineData taslyOrderLine = (TaslyOrderLineData) orderLineData;
                    if (orderLineData.getOrderLineId().equals(dnLogData.getOms_order_line_id())) {
                        handleBussness(dnLogData, taslyOrderData, taslyOrderLine, logisticModels);
                    } else {
                        getLogger().info("订单行项目信息标识[DNLOG.OMS_ORDER_LINE_ID:" + dnLogData.getOms_order_line_id() + "]不相符。");
                        defaultDnLogService.updateSingleDNLog(dnLogData, TaslyERPConstants.ERRORDNLOG);
                    }
                }
            } else {
                getLogger().info("没有找到相应的订单信息[订单标识：" + dnLogData.getOms_order_id() + "]。");
                defaultDnLogService.updateSingleDNLog(dnLogData, TaslyERPConstants.ERRORDNLOG);
            }
        }
        return logisticModels;
    }

    /**
     * 根据DnLog生成接口对象
     *
     * @param dnLogData
     * @param taslyOrderData
     * @param taslyOrderLine
     * @param logisticModels
     */
    protected void handleDnLogData(DNLogData dnLogData, TaslyOrderData taslyOrderData, TaslyOrderLineData taslyOrderLine, List<BaseLogisticModel> logisticModels) {
        List<OrderLineQuantityData> taslyOrderQuantityList = taslyOrderLine.getOrderLineQuantities();
        if (taslyOrderQuantityList != null && !taslyOrderQuantityList.isEmpty()) {
            BaseLogisticModel model = createLogisticModel(taslyOrderData, taslyOrderLine);
            model.setDnLogData(dnLogData);
            logisticModels.add(model);
        } else {
            getLogger().info("没有找到相应物流信息[订单行项目标识：" + taslyOrderLine.getOrderLineId() + "]。");
            defaultDnLogService.updateSingleDNLog(dnLogData, TaslyERPConstants.NOEXPRESSCODE);
        }
    }

    /**
     * 同步物流信息
     *
     * @param logisticModels
     */
    private void handleProcess(List<BaseLogisticModel> logisticModels) {
        for (BaseLogisticModel logisticModel : logisticModels) {
            if (baseLogisticDao.sendOfflineLogistic(logisticModel)) {
                if (filterUnCreateLogistic(logisticModel)) {
                    deleteDnLogs(logisticModel.getTaslyOrderData().getOrderId());
                }
            }
        }
    }

    /**
     * 判断订单是否已经正常处理，如果所有的行信息都处理成功才认为该订单是成功处理的。
     *
     * @param baseLogisticModel
     * @return
     */
    private boolean filterUnCreateLogistic(BaseLogisticModel baseLogisticModel) {
        boolean isHaveCreateOrderLine = true;
        TaslyOrderData taslyOrderData = baseLogisticModel.getTaslyOrderData();
        List<OrderLineData> orderLinesData = taslyOrderData.getOrderLines();
        for (final OrderLineData orderLine : orderLinesData) {
            DNLogData dNLogData = null;
            try {
                dNLogData = defaultDnLogService.getSingleDNLogByOrderLineID(orderLine.getOrderLineId());
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().error("没有找到DNLOG信息[订单行信息标识 : " + orderLine.getOrderLineId() + "]");
            }
            if (dNLogData != null && TaslyERPConstants.CREATE.equals(dNLogData.getReplication_flag())) {
                isHaveCreateOrderLine = true;
            } else {
                isHaveCreateOrderLine = false;
                break;
            }
        }
        getLogger().info("订单[orderId : " + taslyOrderData.getOrderId() + "]是否正常处理的结果为：" + isHaveCreateOrderLine);
        return isHaveCreateOrderLine;
    }

    /**
     * 删除DNLOG信息
     *
     * @param orderId
     */
    private void deleteDnLogs(String orderId) {
        try {
            defaultDnLogService.removeReplicatedDNLogs(defaultDnLogService.getDNLogDataList(orderId));
            getLogger().info("删除NDLOG信息[订单号：" + orderId + "]成功！");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error("删除NDLOG信息[订单号：" + orderId + "]失败！");
        }
    }

    /**
     * 获取订单物流信息
     * 目前订单每笔行信息对应唯一的物流信息
     *
     * @param taslyOrderQuantityList
     * @return
     */
    protected TaslyOrderLineQuantityData getTaslyOrderLineQuantity(List<OrderLineQuantityData> taslyOrderQuantityList) {
        for (OrderLineQuantityData orderLineQuantity : taslyOrderQuantityList) {
            TaslyOrderLineQuantityData taslyOrderLineQuantity = (TaslyOrderLineQuantityData) orderLineQuantity;
            if (taslyOrderLineQuantity.getExpress_code() != null) {
                return taslyOrderLineQuantity;
            }
        }
        return (TaslyOrderLineQuantityData) taslyOrderQuantityList.get(0);
    }

    /**
     * 判断商品是否为赠品
     *
     * @param orderLine
     * @return
     */
    protected boolean validateIsGift(final OrderLineData orderLine) {
        if (orderLine instanceof TaslyOrderLineData) {
            TaslyOrderLineData taslyOrderLine = (TaslyOrderLineData) orderLine;
            if (taslyOrderLine.getGift_item_flag() != null
                    && taslyOrderLine.getGift_item_flag().equalsIgnoreCase(OrderConstants.ORDER_LINE_GIFT_FLAG)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 根据订单行信息ID，删除DNLOG所有重复数据
     *
     * @return
     */
    @Transactional
    private List<DNLogData> getNoRepeatDNLogList() {
        List<DNLogData> dataList = null;
        List<DNLogData> noRepeatList = null;
        try {
            dataList = defaultDnLogService.getDNLogByFlag(TaslyERPConstants.UNCREATE, innerSource, channelSource);
            List<DNLogData> removeRepeatList = new ArrayList<DNLogData>();
            noRepeatList = new ArrayList<DNLogData>();
            Map<String, DNLogData> map = new HashMap<String, DNLogData>();
            if (dataList != null && dataList.size() != 0) {
                for (DNLogData dnLogData : dataList) {
                    if (map.get(dnLogData.getOms_order_line_id()) == null) {
                        map.put(dnLogData.getOms_order_line_id(), dnLogData);
                        noRepeatList.add(dnLogData);
                    } else {
                        removeRepeatList.add(dnLogData);
                        getLogger().info("删除dnLog重复数据[oms_order_line_id : " + dnLogData.getOms_order_line_id() + "]");
                    }
                }
            }
            if (removeRepeatList != null && !removeRepeatList.isEmpty()) {
                defaultDnLogService.removeReplicatedDNLogs(removeRepeatList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().info("没有找到dnlog[innerSource:" + innerSource + ",channelSource:" + channelSource + "]的信息");
        }
        return noRepeatList;
    }

    /**
     * 每个电商的处理方式不尽相同
     *
     * @param dnLogData
     * @param taslyOrderData
     * @param taslyOrderLine
     * @param logisticModels
     */
    protected abstract void handleBussness(DNLogData dnLogData, TaslyOrderData taslyOrderData, TaslyOrderLineData taslyOrderLine, List<BaseLogisticModel> logisticModels);

    /**
     * 根据DNLOG创建接口发送对象
     *
     * @param taslyOrderData
     * @param taslyOrderLine
     * @return
     */
    protected abstract BaseLogisticModel createLogisticModel(TaslyOrderData taslyOrderData, TaslyOrderLineData taslyOrderLine);

    /**
     * 合并接口发送对象
     *
     * @param logisticModels
     * @return
     */
    protected abstract List<BaseLogisticModel> mergeLogicticModel(List<BaseLogisticModel> logisticModels);

    /**
     * 日志信息
     *
     * @return
     */
    protected abstract Logger getLogger();


    public void setInnerSource(String innerSource) {
        this.innerSource = innerSource;
    }

    public void setChannelSource(String channelSource) {
        this.channelSource = channelSource;
    }

    public void setDefaultDnLogService(DefaultDNLogService defaultDnLogService) {
        this.defaultDnLogService = defaultDnLogService;
    }

    public void setOmsOrderRetrieverService(OmsOrderRetrieveService omsOrderRetrieverService) {
        this.omsOrderRetrieverService = omsOrderRetrieverService;
    }

    public void setBaseLogisticDao(BaseLogisticDao baseLogisticDao) {
        this.baseLogisticDao = baseLogisticDao;
    }
}