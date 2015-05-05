package tasly.greathealth.oms.client.order;


import com.hybris.commons.client.GenericRestClient;
// import com.hybris.commons.client.RestCallDecoratorRegistry;
import com.hybris.commons.client.RestResponseException;
// import com.hybris.commons.client.StaticHeaderValueRestCallDecorator;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.exception.RemoteRequestException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tasly.greathealth.oms.api.order.dto.ChannelSource;
import tasly.greathealth.oms.api.order.dto.InnerSource;
import tasly.greathealth.oms.api.order.dto.TaslyOrder;
import tasly.greathealth.oms.api.order.dto.TaslyOrderLine;
import tasly.greathealth.oms.ui.api.shipment.OrderShipmentDetail;
import tasly.greathealth.oms.ui.api.shipment.OrderShipmentList;


public class TaslyOrderRestClient
{
	private GenericRestClient client;

	public void setClient(final GenericRestClient genericRestClient)
	{
		this.client = genericRestClient;
	}

	public GenericRestClient getClient()
	{
		return client;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(TaslyOrderRestClient.class);

	// static final Logger LOGGER = OmsLoggerFactory.getOmsorderlog();

	public void updateTaslyOrderLockStatus(final String orderId, final String userId, final String lockStatus)
	{
		LOGGER.debug("updateTaslyOrderLockStatus:order={}", orderId);
		try
		{
			getClient().call("taslyorder/%s/%s/LockStatus", new Object[]{orderId, userId}).put(String.class, lockStatus).result();
		}
		catch (final RestResponseException e)
		{
			e.printStackTrace();
		}
	}

	public String createOrderByDateTime(final String startDateTime, final String endDateTime, final String channelSource,
			final String innerSource)
	{
		LOGGER.debug("createOrderByDateTime:startDateTime={}, endDateTime={}, channelSource={}, innerSource={}.", startDateTime,
				endDateTime, channelSource, innerSource);
		String result = "";
		try
		{
			if (channelSource.equals(ChannelSource.TMALL.toString()) && innerSource.equals(InnerSource.JSC.toString()))
			{

				result = getClient().call("tmallorder/jsc/produceOrder/mock/%s/%s", new Object[]{startDateTime, endDateTime})
						.put(String.class).getResult();
			}
			else
			{
				result = "先只支持渠道为天猫，业态为酒水茶的手工下单，其他业务暂未提供";
			}
		}
		catch (final RestResponseException e)
		{
			result = "后台错误";
			e.unwrap(HybrisSystemException.class);
		}
		finally
		{
			return result;
		}

	}

	public void updateTaslyOrder(final TaslyOrder order)
	{
		LOGGER.debug("updateOrderMemo:order={}", order);
		try
		{
			getClient().call("taslyorder/updateTaslyOrder").post(TaslyOrder.class, order).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
		catch (final Exception e)
		{
			System.out.println("updateOrderMemo: " + e.getMessage());
		}
	}

	public TaslyOrder getTaslyOrderByOrderID(final String orderId)
	{
		LOGGER.debug("getTaslyOrderByOrderID:orderId={}", orderId);
		try
		{
			return getClient().call("taslyorder/%s", new Object[]{orderId}).get(TaslyOrder.class).result();
		}
		catch (final RestResponseException e)
		{
			return (TaslyOrder) e.unwrap(HybrisSystemException.class);
		}
	}

	public void approveOrder(final String orderId)
	{
		LOGGER.debug("approveOrder:orderId={}", orderId);
		try
		{
			// StaticHeaderValueRestCallDecorator restCallDecorator = new StaticHeaderValueRestCallDecorator();
			//
			// final RestCallDecoratorRegistry restCallDecoratorRegistry = new RestCallDecoratorRegistry();
			//
			// restCallDecoratorRegistry.registerDecorator(restCallDecorator);
			// client.setRestCallDecoratorRegistry(restCallDecoratorRegistry);
			//
			// restCallDecorator = new StaticHeaderValueRestCallDecorator();
			//
			// restCallDecorator.setHeaderName("X-XXrole");
			// restCallDecorator.setStaticValue("aXXdmin");
			//
			// restCallDecoratorRegistry.registerDecorator(restCallDecorator);
			//
			// restCallDecorator = new StaticHeaderValueRestCallDecorator();
			// restCallDecorator.setHeaderName("X-tenantId");
			// restCallDecorator.setStaticValue("single");
			//
			// restCallDecoratorRegistry.registerDecorator(restCallDecorator);
			//
			// restCallDecorator = new StaticHeaderValueRestCallDecorator();
			// restCallDecorator.setHeaderName("X-role");
			// restCallDecorator.setStaticValue("admin");
			//
			// restCallDecoratorRegistry.registerDecorator(restCallDecorator);
			//
			// client.setRestCallDecoratorRegistry(restCallDecoratorRegistry);

			getClient().call("taslyorder/" + orderId + "/approve").post().result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	public void mockOrder(final String orderId)
	{
		LOGGER.debug("mockOrder:orderId={}", orderId);
		try
		{
			getClient().call("taslyorder/%s/mock", new Object[]{orderId}).post().result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	public void updateExpressCodeByOrderId(final String orderId, final String expressCode)
	{
		try
		{
			getClient().call("taslyorder/%s/express/update", new Object[]{orderId}).put(String.class, expressCode).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	public List<OrderShipmentDetail> getOrderShipmentDetailsByOrderId(final String orderId, final boolean allLocationDisplay)
			throws EntityNotFoundException, RemoteRequestException
			{
		try
		{
			return getClient().call("/uiordershipments/order/%s", new Object[]{orderId})
					.queryParam("allLocationDisplay", Boolean.toString(allLocationDisplay)).get(OrderShipmentList.class).result()
					.getList();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
		return null;
			}

	@SuppressWarnings("finally")
	public String updateOrderLineRefundFlag(final String orderLineId, final String refundFlag) throws EntityNotFoundException,
			RemoteRequestException
	{
		String result = "";
		try
		{
			result = getClient().call("taslyorder/taslyorderline/%s", new Object[]{orderLineId,}).post(String.class, refundFlag)
					.result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			result = e.toString();
		}
		finally
		{
			return result;
		}
	}

	public TaslyOrderLine getTaslyOrderLineByOrderLineId(final String orderLineId) throws EntityNotFoundException,
			RemoteRequestException
	{
		try
		{
			return getClient().call("taslyorder/taslyorderline/%s", new Object[]{orderLineId}).get(TaslyOrderLine.class).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
		return null;
	}
}
