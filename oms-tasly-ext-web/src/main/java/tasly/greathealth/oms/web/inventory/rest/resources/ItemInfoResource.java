/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.web.inventory.rest.resources;


import com.hybris.oms.api.Pageable;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.RequestBody;

import tasly.greathealth.oms.api.inventory.ItemInfoFacade;
import tasly.greathealth.oms.api.inventory.ItemInfoQueryObject;
import tasly.greathealth.oms.api.inventory.dto.ItemInfo;
import tasly.greathealth.oms.api.inventory.dto.ItemInfoList;
import tasly.greathealth.oms.api.inventory.dto.SkuList;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;
import tasly.greathealth.oms.inventory.services.ItemQuantityService;
import tasly.greathealth.oms.inventory.services.ItemQuantityService.HandleReturn;
import tasly.greathealth.oms.inventory.services.TaslyItemLocationService;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 * REST resource to handle request relevant to ItemInfo object.
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/iteminfo")
public class ItemInfoResource
{
	private ItemInfoFacade itemInfoFacade;
	private ItemQuantityService itemQuantityService;
	private TaslyItemLocationService taslyItemLocationService;

	private static final Logger omsLOG = OmsLoggerFactory.getOmsinventorylog();

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	/**
	 * @param taslyItemLocationService the taslyItemLocationService to set
	 */
	public void setTaslyItemLocationService(final TaslyItemLocationService taslyItemLocationService)
	{
		this.taslyItemLocationService = taslyItemLocationService;
	}

	/**
	 * @param itemQuantityService the itemQuantityService to set
	 */
	public void setItemQuantityService(final ItemQuantityService itemQuantityService)
	{
		this.itemQuantityService = itemQuantityService;
	}

	@Required
	public void setItemInfoFacade(final ItemInfoFacade facade)
	{
		this.itemInfoFacade = facade;
	}

	@POST
	public Response create(@RequestBody final ItemInfo itemInfo)
	{
		itemInfoFacade.create(itemInfo);
		return Response.status(Response.Status.CREATED).build();
	}

	@GET
	@TypeHint(ItemInfo.class)
	public Response findItemInfoByQuery(@Context final UriInfo uriInfo)
	{
		final ItemInfoQueryObject queryObject = new ItemInfoQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<ItemInfo> pagedItemInfos = this.itemInfoFacade.findItemInfosByQuery(queryObject);
		final GenericEntity<List<ItemInfo>> entity = new GenericEntity<List<ItemInfo>>(pagedItemInfos.getResults())
		{
			//
		};
		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(
				pagedItemInfos.getNextPage().intValue(), pagedItemInfos.getPreviousPage().intValue(),
				pagedItemInfos.getTotalPages().intValue(), pagedItemInfos.getTotalRecords().longValue());
		return responseBuilder.entity(entity).build();
	}

	@GET
	@Path("/all")
	public Collection<ItemInfo> getAllItemInfos()
	{
		return itemInfoFacade.getAll();
	}

	@SuppressWarnings("null")
	@GET
	@Path("/flag0update")
	public Response updateQuantity()
	{
		omsLOG.info("Begin to update quantity where flag=0.");
		final Collection<ItemInfo> itemInfos = itemInfoFacade.getAll();
		int flag;
		String sku;
		EnumMap<HandleReturn, Object> handleReturn = new EnumMap<HandleReturn, Object>(HandleReturn.class);
		boolean updateStatus = false;
		if (itemInfos.size() == 0 || itemInfos == null)
		{
			omsLOG.error("Get all itemInfos failed!");
		}
		else
		{
			for (final ItemInfo itemInfo : itemInfos)
			{
				flag = itemInfo.getStockManageFlag();
				sku = itemInfo.getSku();
				if (flag == 0)
				{
					final List<TaslyItemLocationData> checkTaslyItemLocationDatas = taslyItemLocationService.getByItemID(sku);
					if (checkTaslyItemLocationDatas == null || checkTaslyItemLocationDatas.size() == 0)
					{
						omsLOG.error("sku:" + sku + ",no ItemLocation data!");
						continue;
					}
					else
					{
						try
						{
							handleReturn = itemQuantityService.handleUpdateMethod(checkTaslyItemLocationDatas, sku, flag, 0);
						}
						catch (final Exception e)
						{
							omsLOG.error("handle sku:" + sku + " failed and error is " + e);
							handleReturn.put(HandleReturn.handleStatus, false);
						}
						if ((boolean) handleReturn.get(HandleReturn.handleStatus))
						{
							try
							{
								updateStatus = itemQuantityService.updateMethod(sku, flag, 0);
								if (updateStatus)
								{
									omsLOG.debug("sku:" + sku + ",flag=0 allocated ok!");
								}
							}
							catch (final Exception e)
							{
								omsLOG.error("update sku:" + sku + " failed and error is " + e);
							}
						}
					}
				}
			}
		}
		omsLOG.info("Update quantity where flag=0 finished.");
		return Response.status(Response.Status.OK).build();
	}

	@SuppressWarnings("null")
	@PUT
	@Path("/flag0skus")
	public Response updateFlagSku(final SkuList skuList) throws Exception
	{
		int flag;
		EnumMap<HandleReturn, Object> handleReturn = new EnumMap<HandleReturn, Object>(HandleReturn.class);
		boolean updateStatus = false;
		final List<String> skus = skuList.getSkus();
		if (skus.size() > 0 || skus != null)
		{
			for (final String sku : skus)
			{
				final ItemInfo itemInfo = itemInfoFacade.getBySku(sku);
				flag = itemInfo.getStockManageFlag();
				if (flag == 0)
				{
					final List<TaslyItemLocationData> checkTaslyItemLocationDatas = taslyItemLocationService.getByItemID(sku);
					if (checkTaslyItemLocationDatas == null || checkTaslyItemLocationDatas.size() == 0)
					{
						omsLOG.error("sku:" + sku + ",no ItemLocation data!");
						continue;
					}
					else
					{
						try
						{

							handleReturn = itemQuantityService.handleUpdateMethod(checkTaslyItemLocationDatas, sku, flag, 0);
						}
						catch (final Exception e)
						{
							omsLOG.error("handle sku:" + sku + " failed and error is " + e);
							handleReturn.put(HandleReturn.handleStatus, false);
						}
						if ((boolean) handleReturn.get(HandleReturn.handleStatus))
						{
							try
							{
								updateStatus = itemQuantityService.updateMethod(sku, flag, 0);
								if (updateStatus)
								{
									omsLOG.debug("sku:" + sku + ",flag=0 allocated ok!");
								}
							}
							catch (final Exception e)
							{
								omsLOG.error("update sku:" + sku + " failed and error is " + e);
							}
						}
					}
				}
			}
			omsLOG.info("Update quantity where flag=0 finished.");
		}
		return Response.status(Response.Status.OK).build();
	}

	@PUT
	public Response batchUpdate(final ItemInfoList itemInfoList) throws Exception
	{
		final List<ItemInfo> list = itemInfoList.getItemInfos();
		itemInfoFacade.batchUpdate(list);
		return Response.status(Response.Status.OK).build();
	}
}
