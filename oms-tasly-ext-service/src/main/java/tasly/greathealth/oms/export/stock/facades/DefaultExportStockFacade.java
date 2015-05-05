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
package tasly.greathealth.oms.export.stock.facades;

import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.exception.EntityValidationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import tasly.greathealth.oms.api.inventory.TaslyItemLocationQueryObject;
import tasly.greathealth.oms.export.api.stock.ExportStockFacade;
import tasly.greathealth.oms.export.api.stock.dto.ExportStock;
import tasly.greathealth.oms.export.stock.conversion.ExportStockPopulator;
import tasly.greathealth.oms.export.stock.services.ExportStockService;
import tasly.greathealth.oms.inventory.domain.ItemInfoData;
import tasly.greathealth.oms.inventory.domain.TaslyItemLocationData;


/**
 *
 */
@Transactional
public class DefaultExportStockFacade implements ExportStockFacade
{
	private ExportStockService exportStockService;
	private ExportStockPopulator exportStockPopulator;

	/**
	 * @return the exportStockService
	 */
	@Required
	public ExportStockService getExportStockService()
	{
		return exportStockService;
	}

	/**
	 * @param exportStockService the exportStockService to set
	 */
	@Required
	public void setExportStockService(final ExportStockService exportStockService)
	{
		this.exportStockService = exportStockService;
	}

	/**
	 * @return the exportStockPopulator
	 */
	public ExportStockPopulator getExportStockPopulator()
	{
		return exportStockPopulator;
	}

	/**
	 * @param exportStockPopulator the exportStockPopulator to set
	 */
	@Required
	public void setExportStockPopulator(final ExportStockPopulator exportStockPopulator)
	{
		this.exportStockPopulator = exportStockPopulator;
	}

	@Override
	public Pageable<ExportStock> findPagedExportStocksByQuery(final TaslyItemLocationQueryObject queryObject)
			throws EntityValidationException
	{
		// final Page<TaslyItemLocationData> pagedTaslyItemLocationDatas = exportStockService
		// .findPagedItemLocationsByQuery(queryObject);
		// final List<ExportStock> stocks = converters.convertAll(pagedTaslyItemLocationDatas.getContent(),
		// exportStockConverter);
		//
		// for (final ExportStock exportStock : stocks)
		// {
		// exportStock.setAts(exportStockService.getAtsByQuery(exportStock.getSku(), exportStock.getLocationId()));
		// }
		//
		// final PageInfo pageInfo = new PageInfo();
		// pageInfo.setTotalPages(Integer.valueOf(pagedTaslyItemLocationDatas.getTotalPages()));
		// pageInfo.setTotalResults(Long.valueOf(pagedTaslyItemLocationDatas.getTotalElements()));
		// pageInfo.setPageNumber(Integer.valueOf(pagedTaslyItemLocationDatas.getNumber()));
		//
		// return new PagedResults<ExportStock>(stocks, pageInfo);
		// }
		// }
		final List<ExportStock> stocks = findListExportStocksByQuery(queryObject);
		// begin to paging
		final List<ExportStock> viewStocks = new ArrayList<ExportStock>();
		// calculate totalPage
		int totalPage = 0;
		int totalResult = 0;
		final int currentPage = queryObject.getPageNumber();

		if (null != stocks && stocks.size() > 0)
		{
			final int pageSize = queryObject.getPageSize();
			if (stocks.size() % pageSize == 0)
			{
				totalPage = stocks.size() / pageSize;
			}
			else
			{
				totalPage = (stocks.size() / pageSize) + 1;
			}
			if (currentPage + 1 <= totalPage)
			{
				final int startIndex = currentPage == 0 ? 0 : currentPage * pageSize;
				final int endIndex = currentPage + 1 == totalPage ? stocks.size() : (currentPage + 1) * pageSize;

				for (int i = startIndex; i < endIndex; i++)
				{
					viewStocks.add(stocks.get(i));
				}
			}
			totalResult = stocks.size();
		}
		final PageInfo pageInfo = new PageInfo();
		pageInfo.setPageNumber(currentPage);
		pageInfo.setTotalPages(totalPage);
		pageInfo.setTotalResults((long) totalResult);
		return new PagedResults<ExportStock>(viewStocks, pageInfo);
	}

	@SuppressWarnings("null")
	@Override
	public List<ExportStock> findListExportStocksByQuery(final TaslyItemLocationQueryObject queryObject)
	{
		// final List<ItemInfoData> itemInfoDataList = exportStockService.findListItemInfosByQuery(queryObject);
		final List<TaslyItemLocationData> itemLocationDataList = exportStockService.findListItemLocationsByQuery(queryObject);
		final List<ExportStock> stocks = new ArrayList<ExportStock>();
		ExportStock exportStock;
		String sku;
		String locationId;
		int ats = 0;
		int safetyStock = 0;
		if (null != itemLocationDataList && itemLocationDataList.size() > 0)
		{
			for (final TaslyItemLocationData taslyItemLocationData : itemLocationDataList)
			{
				sku = taslyItemLocationData.getItemId();
				locationId = taslyItemLocationData.getStockroomLocation().getLocationId();
				ats = exportStockService.getAtsByQuery(sku, locationId);
				safetyStock = taslyItemLocationData.getSafetyStock();
				final ItemInfoData itemInfoData = taslyItemLocationData.getItemInfo();
				if (ats <= safetyStock && null != itemInfoData)
				{
					exportStock = new ExportStock();
					exportStockPopulator.populate(taslyItemLocationData, exportStock);
					exportStock.setAts(ats);
					stocks.add(exportStock);
				}
			}
			if (null != stocks && stocks.size() > 0)
			{
				Collections.sort(stocks, new Comparator<ExportStock>()
				{
					@Override
					public int compare(final ExportStock arg0, final ExportStock arg1)
					{
						return arg0.getSku().compareTo(arg1.getSku());

					}
				});
				return stocks;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
}
