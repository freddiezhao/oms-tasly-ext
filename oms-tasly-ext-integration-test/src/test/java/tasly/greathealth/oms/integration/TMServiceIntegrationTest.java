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
package tasly.greathealth.oms.integration;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tasly.greathealth.oms.api.order.dto.SkuToProduct;
import tasly.greathealth.tamll.api.SkuToProductFacade;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-tasly-ext-integration-test-spring.xml"})
// @Ignore("OMS-458")
public class TMServiceIntegrationTest
{

	@Autowired
	private SkuToProductFacade skuToProductFacade;

	private SkuToProduct skuToProduct;
	private List<SkuToProduct> skuToProducts;

	@Before
	public void init()
	{
		// skuToProduct = new SkuToProduct();
		// skuToProduct.setChannel("");
		// skuToProduct.setCommodityCode("tm12345");
		// skuToProduct.setPriceRate(0.8);
		// skuToProduct.setProductAmount(3);
		// skuToProduct.setProductCode("p45434");
		// skuToProduct.setProductName("testProduct");
		skuToProduct = new SkuToProduct();
		skuToProduct.setChannel("0");
		skuToProduct.setInnerSource("1");
		skuToProduct.setRatio(0.3d);
		skuToProduct.setItemName("ITEM NAME 0");
		skuToProduct.setOuterId("tm12345");
		skuToProduct.setQuantity(6);
		skuToProduct.setItemId("p45436");
	}


	@Test
	public void createSkuToProduct()
	{
		skuToProductFacade.createSkuToProduct(skuToProduct);
	}

	// @Test
	// public void getSkuToProduct()
	// {
	//
	// // System.out.println(skuToProducts.getClass().getName().equals(java.util.ArrayList.class.getName()));
	// skuToProducts = (List<SkuToProduct>) skuToProductFacade.querySkuToProducts("tc12345", "tc");
	// if (skuToProducts.isEmpty())
	// {
	// System.out.println(" 空。。。。。。");
	// }
	// else
	// {
	// final int num = skuToProducts.size();
	// for (int i = 0; i < num; i++)
	// {
	// skuToProduct = skuToProducts.get(i);
	// System.out.println(skuToProduct.getProductName());
	// }
	// }
	//
	// }
}
