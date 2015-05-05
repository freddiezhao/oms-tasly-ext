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
package tasly.greathealth.oms.order.services.impl;

import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.impl.DefaultOrderService;
import com.hybris.oms.service.shipment.ShipmentService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.erp.api.order.updatedelivery.dto.EccDelivery;
import tasly.greathealth.erp.api.order.updatedelivery.dto.Message;
import tasly.greathealth.erp.dnlog.service.DNLogService;
import tasly.greathealth.oms.domain.erp.DNLogData;
import tasly.greathealth.oms.domain.order.TaslyOrderData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineData;
import tasly.greathealth.oms.domain.order.TaslyOrderLineQuantityData;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.oms.order.services.TaslyOrderLineQuantityService;


/**
 *
 */
public class DefaultTaslyOrderLineQuantityService extends DefaultOrderService implements TaslyOrderLineQuantityService
{
	private static final Logger LOG = OmsLoggerFactory.getOmsorderlog();

	private final String EXPRESS_SEPARATOR_INNER = "_"; // Example: "SF_1234"
	private final String EXPRESS_SEPARATOR_OUTER = ";"; // Example: "SF_1234;SF_5678"

	private ShipmentFacade shipmentFacade;
	private ShipmentService shipmentService;

	DNLogService dNlogService;
	private final int UPDATE_STATUS_SUCCESS = 0;
	private final int UPDATE_STATUS_FAILURE = 1;
	private final int UPDATE_STATUS_NO_UPDATE = 2;

	/**
	 * added by panlin for TS-707/TS-339: update order express code and number, and express memo
	 * changed: move DNLog item creation from UpdateOrderDeliveryStatusFacade
	 * last modified on 2015-04-17
	 */
	@Override
	@Transactional
	public void updateOrderExpressCodeNumberMemo(final Message message)
	{
		LOG.info("更新快递信息开始...");

		try
		{
			final List<EccDelivery> eccDeliveries = message.getEccOrderDeliveries();

			for (final EccDelivery eccDelivery : eccDeliveries)
			{
				try
				{
					final String orderId = eccDelivery.getOmsOrderId();
					final String orderLineId = eccDelivery.getOmsLineId();
					final String expressCode = eccDelivery.getForwardId();
					final String expressNumber = eccDelivery.getDeliveryNumber();

					LOG.info("待更新快递信息: 订单号 = " + orderId + ", 订单行号 = " + orderLineId + ", 快递公司代码 = " + expressCode + ", 快递单号 = "
							+ expressNumber);

					boolean messageHasEmptyInfo = false;
					if (StringUtils.isEmpty(orderId))
					{
						messageHasEmptyInfo = true;
						LOG.error("订单号为空");
					}
					if (StringUtils.isEmpty(orderLineId))
					{
						messageHasEmptyInfo = true;
						LOG.error("订单行号为空");
					}
					if (StringUtils.isEmpty(expressCode))
					{
						messageHasEmptyInfo = true;
						LOG.error("快递公司代码为空");
					}
					if (StringUtils.isEmpty(expressNumber))
					{
						messageHasEmptyInfo = true;
						LOG.error("快递单号为空");
					}
					if (messageHasEmptyInfo)
					{
						LOG.error("更新快递信息异常: ");
						LOG.error("快递信息含有空数据, 无法更新");
						continue;
					}

					final String singleExpressMemo = expressCode + EXPRESS_SEPARATOR_INNER + expressNumber; // Example:
					// "SF_1234"
					int updateExpressCodeNumberStatus = UPDATE_STATUS_FAILURE;
					int updateExpressMemoStatus = UPDATE_STATUS_FAILURE;

					final TaslyOrderData taslyOrderData = (TaslyOrderData) super.getOrderByOrderId(orderId);
					if (null != taslyOrderData)
					{
						final List<OrderLineData> orderLineDatas = taslyOrderData.getOrderLines();
						if (null != orderLineDatas && !orderLineDatas.isEmpty())
						{
							boolean isOrderLineIdFound = false;
							for (final OrderLineData orderLineData : orderLineDatas)
							{
								final TaslyOrderLineData taslyOrderLineData = (TaslyOrderLineData) orderLineData;

								if (orderLineId.equals(taslyOrderLineData.getOrderLineId()))
								{
									final List<OrderLineQuantityData> orderLineQuantityDatas = taslyOrderLineData.getOrderLineQuantities();
									if (null != orderLineQuantityDatas && !orderLineQuantityDatas.isEmpty())
									{
										for (final OrderLineQuantityData orderLineQuantityData : orderLineQuantityDatas)
										{
											final TaslyOrderLineQuantityData taslyOrderLineQuantityData = (TaslyOrderLineQuantityData) orderLineQuantityData;

											LOG.info("行快递信息更新前: 快递公司代码 = " + taslyOrderLineQuantityData.getExpress_code() + ", 快递单号 = "
													+ taslyOrderLineQuantityData.getExpress_order_id());
											// update orderLineQuantityExpress code and number
											updateExpressCodeNumberStatus = updateOrderLineQuantityExpressCodeNumber(
													taslyOrderLineQuantityData, expressCode, expressNumber);
											if (UPDATE_STATUS_SUCCESS == updateExpressCodeNumberStatus)
											{
												LOG.info("行快递信息更新后: 快递公司代码 = " + taslyOrderLineQuantityData.getExpress_code() + ", 快递单号 = "
														+ taslyOrderLineQuantityData.getExpress_order_id());

												try
												{
													// invoke DNLogService to create DNLog item using orderId and orderLineId
													final DNLogData dNLogData = dNlogService.createDNLog(orderId, orderLineId);
													if (null != dNLogData)
													{
														LOG.debug("DNLog记录创建成功: DNLog记录订单号 = " + dNLogData.getOms_order_id() + ", 订单行号 = "
																+ dNLogData.getOms_order_line_id());
													}
													else
													{
														LOG.error("DNLog记录创建失败");
													}
												}
												catch (final Exception e)
												{
													LOG.error("DNLog记录创建异常: ");
													LOG.error(e.getMessage(), e);
												}
											}
											else if (UPDATE_STATUS_NO_UPDATE == updateExpressCodeNumberStatus)
											{
												LOG.warn("行快递信息不更新: 快递公司代码 = " + taslyOrderLineQuantityData.getExpress_code() + ", 快递单号 = "
														+ taslyOrderLineQuantityData.getExpress_order_id());
												LOG.warn("行快递信息不更新, DNLog记录不创建");
											}
											else
											{
												LOG.error("行快递信息更新失败: 快递公司代码 = " + taslyOrderLineQuantityData.getExpress_code() + ", 快递单号 = "
														+ taslyOrderLineQuantityData.getExpress_order_id());
												LOG.error("行快递信息更新失败, DNLog记录不创建");
											}
										}
									}
									else
									{
										LOG.error("订单号" + orderId + ", 订单行号" + orderLineId + "下没有订单行数量信息, 无法更新");
									}

									isOrderLineIdFound = true;
									break;
								}
							}
							if (!isOrderLineIdFound)
							{
								LOG.error("订单号" + orderId + "下不存在订单行号" + orderLineId + "的信息, 无法更新");
							}

							LOG.info("物流备注更新前: 物流备注 = " + taslyOrderData.getExpress_memo());
							// update order express memo
							updateExpressMemoStatus = updateOrderExpressMemo(taslyOrderData, singleExpressMemo);
							if (UPDATE_STATUS_SUCCESS == updateExpressMemoStatus)
							{
								LOG.info("物流备注更新后: 物流备注 = " + taslyOrderData.getExpress_memo());
							}
							else if (UPDATE_STATUS_NO_UPDATE == updateExpressMemoStatus)
							{
								LOG.info("物流备注不更新: 物流备注 = " + taslyOrderData.getExpress_memo());
							}
							else
							{
								LOG.error("物流备注更新失败: 物流备注 = " + taslyOrderData.getExpress_memo());
							}

							if (UPDATE_STATUS_SUCCESS == updateExpressCodeNumberStatus)
							{
								// confirm order shipments
								confirmOrderShipments(orderId);
							}
							else
							{
								LOG.error("订单行快递信息未更新成功, 不处理orderId = " + orderId + "的确认订单发货");
							}
						}
						else
						{
							LOG.error("订单号" + orderId + "下没有订单行信息, 无法更新");
						}
					}
					else
					{
						LOG.error("订单号" + orderId + "不存在, 无法更新");
					}
				}
				catch (final Exception e)
				{
					LOG.error("更新快递信息异常: ");
					LOG.error(e.getMessage());
				}
			}

			LOG.info("...更新快递信息结束");
		}
		catch (final Exception e)
		{
			LOG.error("更新快递信息异常, 中止: ");
			LOG.error(e.getMessage());
		}
	}

	/**
	 * added by panlin for TS-707/TS-339: update orderLineQuantityExpress express code and number
	 * last modified on 2015-01-21
	 *
	 * @param taslyOrderLineQuantityData
	 * @param expressCode
	 * @param expressNumber
	 */
	private int updateOrderLineQuantityExpressCodeNumber(final TaslyOrderLineQuantityData taslyOrderLineQuantityData,
			final String expressCode, final String expressNumber)
	{
		LOG.info("行快递信息更新中: 处理olqId = " + taslyOrderLineQuantityData.getOlqId());

		try
		{
			if (StringUtils.isEmpty(taslyOrderLineQuantityData.getExpress_order_id())) // original express number is empty
			{
				taslyOrderLineQuantityData.setExpress_code(expressCode);
				taslyOrderLineQuantityData.setExpress_order_id(expressNumber);
				return UPDATE_STATUS_SUCCESS;
			}
			else
			{
				LOG.warn("订单行快递单号不为空, 快递信息不更新");
				return UPDATE_STATUS_NO_UPDATE;
			}
		}
		catch (final Exception e)
		{
			LOG.error("更新订单行快递公司和快递单号异常: ");
			LOG.error(e.getMessage());
			return UPDATE_STATUS_FAILURE;
		}
	}

	/**
	 * added by panlin for TS-707/TS-339: update order express memo
	 * last modified on 2015-01-21
	 *
	 * @param taslyOrderData
	 * @param singleExpressMemo
	 */
	private int updateOrderExpressMemo(final TaslyOrderData taslyOrderData, final String singleExpressMemo)
	{
		LOG.info("物流备注更新中: 处理orderId = " + taslyOrderData.getOrderId());

		try
		{
			String expressMemo = taslyOrderData.getExpress_memo();

			if (StringUtils.isEmpty(expressMemo)) // original express memo is empty
			{
				taslyOrderData.setExpress_memo(singleExpressMemo);
				return UPDATE_STATUS_SUCCESS;
			}
			else if (!expressMemo.contains(singleExpressMemo)) // single express memo is not contained in original express
			// memo
			{
				expressMemo += EXPRESS_SEPARATOR_OUTER + singleExpressMemo;
				taslyOrderData.setExpress_memo(expressMemo);
				return UPDATE_STATUS_SUCCESS;
			}
			else
			{
				LOG.warn("订单快递信息" + singleExpressMemo + "已包含在订单物流备注" + expressMemo + "中, 订单物流备注不更新");
				return UPDATE_STATUS_NO_UPDATE;
			}
		}
		catch (final Exception e)
		{
			LOG.error("更新订单物流备注异常: ");
			LOG.error(e.getMessage());
			return UPDATE_STATUS_FAILURE;
		}
	}

	/**
	 * added by panlin for TS-707/TS-339: confirm order shipments
	 * last modified on 2015-01-21
	 *
	 * @param orderId
	 */
	private void confirmOrderShipments(final String orderId)
	{
		LOG.info("确认订单发货: 处理orderId = " + orderId);

		try
		{
			final List<ShipmentData> shipmentDatas = this.shipmentService.findAllShipmentsByOrderId(orderId);

			final Set<String> shipmentIds = new HashSet<String>();

			for (final ShipmentData shipmentData : shipmentDatas)
			{
				LOG.debug("shipmentId = " + Long.toString(shipmentData.getShipmentId()));
				shipmentIds.add(Long.toString(shipmentData.getShipmentId()));
			}

			this.shipmentFacade.confirmShipments(shipmentIds);
		}
		catch (final EntityNotFoundException e)
		{
			LOG.error("确认订单发货异常: ");
			LOG.error(e.getMessage());
		}
		catch (final InvalidOperationException e)
		{
			LOG.error("确认订单发货异常: ");
			LOG.error(e.getMessage());
		}
	}

	/**
	 * @param shipmentFacade the shipmentFacade to set
	 */
	public void setShipmentFacade(final ShipmentFacade shipmentFacade)
	{
		this.shipmentFacade = shipmentFacade;
	}

	/**
	 * @param shipmentService the shipmentService to set
	 */
	@Override
	public void setShipmentService(final ShipmentService shipmentService)
	{
		this.shipmentService = shipmentService;
	}

	/**
	 * @param dNlogService the dNlogService to set
	 */
	public void setdNlogService(final DNLogService dNlogService)
	{
		this.dNlogService = dNlogService;
	}
}
