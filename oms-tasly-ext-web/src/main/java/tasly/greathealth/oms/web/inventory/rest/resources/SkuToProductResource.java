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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;

import tasly.greathealth.oms.api.order.SkuToProductQueryObject;
import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.oms.api.order.dto.UISkuToProduct;
import tasly.greathealth.oms.log.OmsLoggerFactory;
import tasly.greathealth.tamll.api.SkuToProductFacade;
import tasly.greathealth.tamll.api.order.dto.SkuToProductList;
import tasly.greathealth.tamll.api.order.dto.SkuToProductsList;
import tasly.greathealth.thirdparty.inventory.beans.StoreSku;
import tasly.greathealth.thirdparty.inventory.services.UpdateItemService;
import tasly.greathealth.thirdparty.packagedto.ItemModel;
import tasly.greathealth.thirdparty.packagedto.PackageItemModelList;
import tasly.greathealth.thirdparty.packagedto.PackageItemRelationModel;
import tasly.greathealth.thirdparty.packagedto.PackageModel;
import tasly.greathealth.thirdparty.packagedto.PackageOuterId;
import tasly.greathealth.thirdparty.packagedto.PackageOuterIds;
import tasly.greathealth.thirdparty.packagedto.PackageRelationModel;
import tasly.greathealth.thirdparty.packagedto.Result;
import tasly.greathealth.tmall.inventory.ItemConstants;
import tasly.greathealth.tmall.inventory.domain.ProductTm;
import tasly.greathealth.tmall.inventory.services.TmallItemService;

import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;


/**
 * REST resource to handle request relevant to Person object.
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/skuToProduct")
public class SkuToProductResource
{
	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	private SkuToProductFacade skuToProductFacade;
	private TmallItemService tmallItemService;
	private UpdateItemService jdItemService;


	private static final Logger LOG = OmsLoggerFactory.getTmallorderlog();
	String channel_tm = ItemConstants.TMALL_LOCATION;
	String channel_jd = ItemConstants.JD_LOCATION;

	/**
	 * @return the jdItemService
	 */
	public UpdateItemService getJdItemService()
	{
		return jdItemService;
	}

	/**
	 * @param jdItemService the jdItemService to set
	 */
	public void setJdItemService(final UpdateItemService jdItemService)
	{
		this.jdItemService = jdItemService;
	}

	@Required
	public void setSkuToProductFacade(final SkuToProductFacade facade)
	{
		this.skuToProductFacade = facade;
	}

	@Required
	public void setTmallItemService(final TmallItemService tmallItemService)
	{
		this.tmallItemService = tmallItemService;
	}


	@POST
	public Response create(@RequestBody final SkuToProduct skuToProduct)
	{
		skuToProductFacade.createSkuToProduct(skuToProduct);
		return Response.status(Response.Status.CREATED).build();
	}

	@PUT
	@Path("/batchUpdate")
	public Response batchUpdate(final SkuToProductsList skuToProductsList)
	{
		final List<SkuToProductList> skuToProductList = skuToProductsList.getSkuToProductsList();
		final SkuToProductsList paraSkuToProductsList = skuToProductFacade.updateSkuToProducts(skuToProductList);
		final SkuToProductsList returnSkuToProductsList = skuToProductFacade.querySkuToProductsList(paraSkuToProductsList);
		final Response.ResponseBuilder response = Response.ok();
		return response.entity(returnSkuToProductsList).build();
	}

	@POST
	@Path("/update")
	public Response update(@RequestBody final SkuToProduct skuToProduct)
	{
		SkuToProduct result = null;
		try
		{
			result = skuToProductFacade.updateSkuToProduct(skuToProduct);
		}
		catch (final RuntimeException r)
		{
			Response.ok(r.getMessage()).build();
		}
		return Response.ok(result).build();

	}

	@PUT
	@Path("/updatePackageInventoryToThirdParty")
	public Response updateSkuToProductInventoryToThirdPartProcess(final PackageRelationModel packageItemRelationModel)
	{
		LOG.info("套餐商品手动修改库存比例向网站开始同步...");
		boolean result = true;
		// 处理天猫
		if (packageItemRelationModel.getChannel().equalsIgnoreCase(channel_tm))
		{
			final List<ProductTm> ptms = this.rebuildTmallDto(packageItemRelationModel);
			for (final ProductTm ptm : ptms)
			{
				final boolean flag = tmallItemService.updateComboInventoryToTmall(ptm);
				if (!flag)
				{
					result = false;
				}
			}
			// 处理京东
		}
		else if (packageItemRelationModel.getChannel().equalsIgnoreCase(channel_jd))
		{
			final List<StoreSku> skus = this.rebuildStoreDto(packageItemRelationModel);
			jdItemService.updateInventoryToStore(skus);
			for (final StoreSku sku : skus)
			{
				final boolean flag = jdItemService.updateStockInfoBySku(sku);
				if (!flag)
				{
					result = false;
				}
			}
		}
		packageItemRelationModel.setUpdateResult(result);
		final Response.ResponseBuilder response = Response.ok();
		return response.entity(packageItemRelationModel).build();
	}

	/**
	 * 构造天猫的dto类型
	 *
	 * @return
	 */
	public List<ProductTm> rebuildTmallDto(final PackageRelationModel packageRelationModel)
	{
		final List<ProductTm> ptms = new ArrayList<ProductTm>();
		final PackageItemRelationModel packageItemRelationModel = packageRelationModel.getPackageItemRelationModel();
		final List<PackageItemModelList> packageItemModelList = packageItemRelationModel.getPackageItemModelList();
		final List<String> tempOuterIds = new ArrayList<String>();

		for (final PackageItemModelList pim : packageItemModelList)
		{
			final String itemId = pim.getItemId();
			LOG.info("待更新的skuId:" + itemId);
			final List<PackageModel> packageModels = pim.getPackageModelList().getPackageModel();
			for (final PackageModel p : packageModels)
			{
				boolean dataException = false;
				final String outerId = p.getOuterId();
				final Long quantity = p.getQuantity();
				if (!tempOuterIds.contains(outerId))
				{
					tempOuterIds.add(outerId);
					// 根据套餐outerId获取信息
					final ProductTm pt = new ProductTm();
					pt.setShopName(packageRelationModel.getInnersource());
					pt.setTmFreeQuntity(quantity);
					pt.setOuterId(outerId);
					final Sku sku = tmallItemService.getSkuFromTmByOuterId(packageRelationModel.getInnersource(), outerId.toString());
					if (null == sku)
					{
						final Item item = tmallItemService.getItemFromTmByOuterId(packageRelationModel.getInnersource(),
								outerId.toString());
						if (null != item)
						{
							pt.setCombo(false);
							pt.setTmProductId(item.getNumIid());
							LOG.info("相关待更新的套餐编码:库存|" + outerId + ":" + quantity + "SKU:" + item.getNumIid() + "|tmall库存"
									+ item.getNum());
						}
						else
						{
							dataException = true;
							LOG.info("严重错误：获取失败,请检查数据");
						}

					}
					else
					{
						pt.setCombo(true);
						pt.setTmProductId(sku.getNumIid());
						LOG.info("相关待更新的套餐编码:库存|" + outerId + ":" + quantity + "SKU:" + sku.getNumIid() + "|tmall库存"
								+ sku.getQuantity());
					}
					if (!dataException)
					{
						ptms.add(pt);
					}
				}
			}
		}
		LOG.info("待更新总数:" + ptms.size());
		return ptms;
	}

	/**
	 * 构造天猫的dto类型
	 *
	 * @return
	 */
	public List<StoreSku> rebuildStoreDto(final PackageRelationModel packageRelationModel)
	{
		final List<StoreSku> ptms = new ArrayList<StoreSku>();
		final PackageItemRelationModel packageItemRelationModel = packageRelationModel.getPackageItemRelationModel();
		final List<PackageItemModelList> packageItemModelList = packageItemRelationModel.getPackageItemModelList();
		final List<String> tempOuterIds = new ArrayList<String>();

		for (final PackageItemModelList pim : packageItemModelList)
		{
			final String itemId = pim.getItemId();
			LOG.info("待更新的skuId:" + itemId);
			final List<PackageModel> packageModels = pim.getPackageModelList().getPackageModel();
			for (final PackageModel p : packageModels)
			{
				// final boolean dataException = false;
				final String outerId = p.getOuterId();
				final Long quantity = p.getQuantity();
				if (!tempOuterIds.contains(outerId))
				{
					tempOuterIds.add(outerId);
					// 根据套餐outerId获取信息
					// final ProductTm pt = new ProductTm();
					// pt.setShopName(packageRelationModel.getInnersource());
					// pt.setTmFreeQuntity(quantity);
					// pt.setOuterId(outerId);
					final StoreSku sku = jdItemService.getStoreSkuFromStore(outerId.toString());
					// pt.setCombo(true);
					// pt.setTmProductId(sku.getNumIid());
					ptms.add(sku);
					LOG.info("相关待更新的套餐编码:库存|" + outerId + ":" + quantity + "SKU:" + sku.getSkuid() + "|tmall库存"
							+ sku.getStockQuntity());
				}
			}
		}
		LOG.info("待更新总数:" + ptms.size());
		return ptms;
	}

	@DELETE
	@Path("/deleteSkuToProduct/{outerId}/{channel}/{innerSource}")
	public Response deleteSkuToProduct(@PathParam("outerId") final String outerId, @PathParam("channel") final String channel,
			@PathParam("innerSource") final String innerSource)
	{
		skuToProductFacade.deleteSkuToProducts(outerId, channel, innerSource);
		return Response.ok().build();
	}

	@DELETE
	@Path("/deleteSkuToProduct")
	public Response deleteSkuToProduct(final SkuToProduct skuToProduct)
	{
		skuToProductFacade.deleteSkuToProducts(skuToProduct);
		return Response.ok().build();
	}

	@GET
	@Path("/{outerId}/{channelId}/{innerSource}")
	public Collection<SkuToProduct> getSkuToProduct(@PathParam("outerId") final String outerId,
			@PathParam("channelId") final String channelId, @PathParam("innerSource") final String innerSource)
	{
		LOG.info("productId : " + outerId);
		LOG.info("channelId : " + channelId);
		final Collection<SkuToProduct> skuToProducts = skuToProductFacade.querySkuToProducts(outerId, channelId, innerSource);
		LOG.info("skuToProducts : " + skuToProducts.size());
		return skuToProducts;
	}

	@PUT
	@Path("/updateItemQuantity")
	public Result getItemQuantity(final ItemModel itemModel)
	{
		boolean result = false;
		if (null != itemModel && itemModel.getChannle().equalsIgnoreCase(channel_tm))
		{
			final Item item = tmallItemService.getItemFromTmByOuterId(itemModel.getInnerSource(), itemModel.getOuterId());
			if (null != item)
			{
				final ProductTm pt = new ProductTm();
				pt.setCombo(false);
				pt.setOuterId(itemModel.getOuterId());
				pt.setShopName(itemModel.getInnerSource());
				pt.setTmFreeQuntity(itemModel.getQuantity());
				pt.setTmProductId(item.getNumIid());
				result = tmallItemService.updateItemQuantity(pt);
			}
		}
		LOG.info("更新单品" + itemModel.getOuterId() + "的库存:" + result);
		return new Result(result);
	}

	@GET
	@Path("/findSkuToProductByQuery")
	@TypeHint(Express.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response findSkuToProductByQuery(@Context final UriInfo uriInfo)
	{
		LOG.trace("findSkuToProductByQuery");

		final SkuToProductQueryObject queryObject = new SkuToProductQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<UISkuToProduct> skuToProductList = this.skuToProductFacade.findSkuToProductByQuery(queryObject);
		final GenericEntity<List<UISkuToProduct>> entity = new GenericEntity<List<UISkuToProduct>>(skuToProductList.getResults())
		{
			// DONOTHING
		};
		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(skuToProductList.getNextPage()
				.intValue(), skuToProductList.getPreviousPage().intValue(), skuToProductList.getTotalPages().intValue(),
				skuToProductList.getTotalRecords().longValue());
		return responseBuilder.entity(entity).build();
	}

	@PUT
	@Path("/getPackageQuantity")
	public PackageOuterIds getPackageQuantity(final PackageOuterIds packageOuterIds)
	{
		final List<PackageOuterId> poi = new ArrayList<PackageOuterId>();
		final List<PackageOuterId> packageIds = packageOuterIds.getPackageOuterIdList().getOuterIds();
		for (final PackageOuterId packageOuterId : packageIds)
		{
			final String outerId = packageOuterId.getOuterId();
			if (packageOuterIds.getChannel().equalsIgnoreCase(channel_tm))
			{
				poi.add(this.getPackageQuantity(outerId, packageOuterIds.getInnerSource()));
			}
		}
		packageOuterIds.getPackageOuterIdList().setOuterIds(poi);
		return packageOuterIds;
	}

	public PackageOuterId getPackageQuantity(final String outerId, final String innerSource)
	{
		Long quantity = -1L;
		final Item item = tmallItemService.getItemFromTmByOuterId(innerSource, outerId);
		if (null == item)
		{
			final Sku sku = tmallItemService.getSkuFromTmByOuterId(innerSource, outerId);
			if (null != sku)
			{
				quantity = sku.getQuantity();
			}
		}
		else
		{
			quantity = item.getNum();
		}
		return new PackageOuterId(outerId, quantity);
	}


	@GET
	@Path("related/{itemId}/{channelId}/{innerSource}")
	public Collection<SkuToProduct> queryAllRelatedSkuToProductByItemId(@PathParam("itemId") final String itemId,
			@PathParam("channelId") final String channelId, @PathParam("innerSource") final String innerSource)
	{
		LOG.info("productId : " + itemId);
		LOG.info("channelId : " + channelId);
		final Collection<SkuToProduct> skuToProducts = skuToProductFacade.queryAllRelatedSkuToProductByItemId(itemId, channelId,
				innerSource);
		LOG.info("skuToProducts : " + skuToProducts.size());
		return skuToProducts;
	}

	@GET
	@Path("/{channelId}/{innerSource}")
	public Collection<SkuToProduct> querySkuToProduct(@PathParam("channelId") final String channelId,
			@PathParam("innerSource") final String innerSource)
	{
		LOG.info("channelId : " + channelId);
		final Collection<SkuToProduct> skuToProducts = skuToProductFacade.querySkuToProduct(channelId, innerSource);
		LOG.info("skuToProducts : " + skuToProducts.size());
		return skuToProducts;
	}
}
