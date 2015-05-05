package tasly.greathealth.oms.client.order;


import com.hybris.commons.client.GenericRestClient;
import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import tasly.greathealth.oms.api.order.ExpressItemQueryObject;
import tasly.greathealth.oms.api.order.ExpressLocationQueryObject;
import tasly.greathealth.oms.api.order.ExpressQueryObject;
import tasly.greathealth.oms.api.order.TaslyExpressFacade;
import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.api.order.dto.ExpressItem;
import tasly.greathealth.oms.api.order.dto.Expresslocation;

import com.sun.jersey.api.client.GenericType;


public class TaslyExpressRestClient implements TaslyExpressFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TaslyExpressRestClient.class);

	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}

	private static final GenericType<Collection<Express>> EXPRESS = new GenericType<Collection<Express>>()
	{
		// DO NOTHING
	};

	private GenericRestClient client;

	public void setClient(final GenericRestClient genericRestClient)
	{
		this.client = genericRestClient;
	}

	public GenericRestClient getClient()
	{
		return client;
	}


	@Override
	public Collection<Express> getUIAllExpress()
	{
		LOGGER.debug("getUIAllExpress");
		try
		{
			return getClient().call("/uiexpress").get(EXPRESS).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Express createOrUpdataExpress(final Express express)
	{
		// LOGGER.debug("updateOrderMemo:order={}", order);
		try
		{
			return getClient().call("taslyExpress/createOrUpdate/express").post(Express.class, express).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
		catch (final Exception e)
		{
			System.out.println("createOrUpdataExpress: " + e.getMessage());
		}
		return express;
	}

	@Override
	public Pageable<Express> findExpressByQuery(final ExpressQueryObject queryObject)
	{
		final RestCallBuilder call = getClient().call("/taslyExpress/findExpressByQuery", new Object[0]);

		this.queryObjectRestCallPopulator.populate(queryObject, call);
		try
		{
			final RestResponse<List<Express>> response = call.get(new GenericType<List<Express>>()
			{
				// Nothing
			});
			final List<Express> ordersList = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<Express>(ordersList, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void deleteExpress(final String code)
	{
		try
		{
			getClient().call("taslyExpress/deleteExpress/%s", new Object[]{code}).delete().result();
		}
		catch (final RestResponseException e)
		{
			// YTODO Auto-generated catch block
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Expresslocation createOrUpdataExpresslocation(final Expresslocation expressLocation)
	{
		// LOGGER.debug("updateOrderMemo:order={}", order);
		try
		{
			return getClient().call("taslyExpress/createOrUpdate/expressLocation").post(Expresslocation.class, expressLocation)
					.result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
		catch (final Exception e)
		{
			System.out.println("createOrUpdataExpresslocation: " + e.getMessage());
		}
		return expressLocation;
	}

	@Override
	public Pageable<Expresslocation> findExpressLocationByQuery(final ExpressLocationQueryObject queryObject)
	{
		final RestCallBuilder call = getClient().call("/taslyExpress/findExpressLocationByQuery", new Object[0]);

		this.queryObjectRestCallPopulator.populate(queryObject, call);
		try
		{
			final RestResponse<List<Expresslocation>> response = call.get(new GenericType<List<Expresslocation>>()
			{
				// Nothing
			});
			final List<Expresslocation> ordersList = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<Expresslocation>(ordersList, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void deleteExpressLocation(final String province, final String channelSource, final String innerSource)
	{
		try
		{
			getClient().call("taslyExpress/deleteExpressLocation/%s/%s/%s", new Object[]{province, channelSource, innerSource})
					.delete().result();
		}
		catch (final RestResponseException e)
		{
			// YTODO Auto-generated catch block
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public ExpressItem createOrUpdataExpressItem(final ExpressItem expressItem)
	{
		// YTODO Auto-generated method stub
		// LOGGER.debug("updateOrderMemo:order={}", order);
		try
		{
			getClient().call("taslyExpress/createOrUpdate/expressItem").post(ExpressItem.class, expressItem).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
		catch (final Exception e)
		{
			System.out.println("updateOrderMemo: " + e.getMessage());
		}
		return expressItem;
	}

	@Override
	public Pageable<ExpressItem> findExpressItemByQuery(final ExpressItemQueryObject queryObject)
	{
		final RestCallBuilder call = getClient().call("/taslyExpress/findExpressItemByQuery", new Object[0]);

		this.queryObjectRestCallPopulator.populate(queryObject, call);
		try
		{
			final RestResponse<List<ExpressItem>> response = call.get(new GenericType<List<ExpressItem>>()
			{
				// Nothing
			});
			final List<ExpressItem> ordersList = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<ExpressItem>(ordersList, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
		// throws EntityValidationException
	}

	@Override
	public void deleteExpressItem(final String skuid, final String innerSource, final String channelSource)
	{
		try
		{
			getClient().call("taslyExpress/deleteExpressItem/%s/%s/%s", new Object[]{skuid, innerSource, channelSource}).delete()
					.result();
		}
		catch (final RestResponseException e)
		{
			// YTODO Auto-generated catch block
			e.unwrap(HybrisSystemException.class);
		}
	}
}
