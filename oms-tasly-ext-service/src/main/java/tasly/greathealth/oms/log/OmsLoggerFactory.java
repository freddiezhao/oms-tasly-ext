package tasly.greathealth.oms.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OmsLoggerFactory
{
	private static final Logger tmallOrderLog = LoggerFactory.getLogger("tmall-order");
	private static final Logger tmallInventoryLog = LoggerFactory.getLogger("tmall-inventory");
	private static final Logger tmallProductLog = LoggerFactory.getLogger("tmall-product");
	private static final Logger tmallLogisticLog = LoggerFactory.getLogger("tmall-logistic");

	private static final Logger erpOrderLog = LoggerFactory.getLogger("erp-order");
	private static final Logger erpInventoryLog = LoggerFactory.getLogger("erp-inventory");
	private static final Logger erpProductLog = LoggerFactory.getLogger("erp-product");
	// TS-879:记录没有通过OMS/ERP新建订单接口的订单信息
	private static final Logger erpOrderErrorLog = LoggerFactory.getLogger("erp-order-error");


	private static final Logger omsOrderLog = LoggerFactory.getLogger("oms-order");
	private static final Logger omsInventoryLog = LoggerFactory.getLogger("oms-inventory");
	private static final Logger omsErrorSkuLog = LoggerFactory.getLogger("oms-errorsku");
	private static final Logger omsProductLog = LoggerFactory.getLogger("oms-product");
	// 京东Log
	private static final Logger jdOrderLog = LoggerFactory.getLogger("jd-order");
	private static final Logger jdInventoryLog = LoggerFactory.getLogger("jd-inventory");
	private static final Logger jdProductLog = LoggerFactory.getLogger("jd-product");
	private static final Logger jdLogisticLog = LoggerFactory.getLogger("jd-logistic");
	// 一号店Log
	private static final Logger yhdOrderLog = LoggerFactory.getLogger("yhd-order");
	private static final Logger yhdInventoryLog = LoggerFactory.getLogger("yhd-inventory");
	private static final Logger yhdProductLog = LoggerFactory.getLogger("yhd-product");
	private static final Logger yhdLogisticLog = LoggerFactory.getLogger("yhd-logistic");

	/**
	 * @return the yhdorderlog
	 */
	public static Logger getYhdorderlog()
	{
		return yhdOrderLog;
	}

	/**
	 * @return the yhdinventorylog
	 */
	public static Logger getYhdinventorylog()
	{
		return yhdInventoryLog;
	}

	/**
	 * @return the yhdproductlog
	 */
	public static Logger getYhdproductlog()
	{
		return yhdProductLog;
	}

	/**
	 * @return the yhdlogisticlog
	 */
	public static Logger getYhdlogisticlog()
	{
		return yhdLogisticLog;
	}

	/**
	 * @return the jdinventorylog
	 */
	public static Logger getJdinventorylog()
	{
		return jdInventoryLog;
	}

	/**
	 * @return the jdproductlog
	 */
	public static Logger getJdproductlog()
	{
		return jdProductLog;
	}

	/**
	 * @return the jdlogisticlog
	 */
	public static Logger getJdlogisticlog()
	{
		return jdLogisticLog;
	}

	/**
	 * @return the jdorderlog
	 */
	public static Logger getJdorderlog()
	{
		return jdOrderLog;
	}

	public static Logger getTmallorderlog()
	{
		return tmallOrderLog;
	}

	public static Logger getTmallinventorylog()
	{
		return tmallInventoryLog;
	}

	public static Logger getTmallproductlog()
	{
		return tmallProductLog;
	}

	public static Logger getErporderlog()
	{
		return erpOrderLog;
	}

	public static Logger getErpinventorylog()
	{
		return erpInventoryLog;
	}

	public static Logger getErpproductlog()
	{
		return erpProductLog;
	}

	public static Logger getOmsorderlog()
	{
		return omsOrderLog;
	}

	public static Logger getOmsinventorylog()
	{
		return omsInventoryLog;
	}

	public static Logger getOmsproductlog()
	{
		return omsProductLog;
	}

	public static Logger getTmalllogisticlog()
	{
		return tmallLogisticLog;
	}

	/**
	 * @return the erpordererrorlog
	 */
	public static Logger getErpordererrorlog()
	{
		return erpOrderErrorLog;
	}

	/**
	 * @return the omserrorskulog
	 */
	public static Logger getOmserrorskulog()
	{
		return omsErrorSkuLog;
	}

}
