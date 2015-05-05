/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package tasly.greathealth.oms.service.mixin;

import com.hybris.kernel.typesystem.TypeSystem;
import com.hybris.kernel.typesystem.builder.ClassPathTypeSystemReader;
import com.hybris.kernel.typesystem.model.Attribute;
import com.hybris.kernel.utils.DomainExtension;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Test to make sure that mixins work in the extension project.
 */
public class OrderMixinTest
{
	private ClassPathTypeSystemReader typeSystemReader;

	@Before
	public void setUp()
	{
		@SuppressWarnings("resource")
		final ApplicationContext ctx = new ClassPathXmlApplicationContext("/META-INF/typesystem-spring.xml");
		typeSystemReader = ctx.getBean(ClassPathTypeSystemReader.class);
	}

	@Test
	public void shouldCreateMixinTypeSystem()
	{
		final List<DomainExtension> extensions = new ArrayList<>();
		extensions.add(new DomainExtension("basestore-domain", "/META-INF/basestore-domain.xml"));
		extensions.add(new DomainExtension("inventory-domain", "/META-INF/inventory-domain.xml"));
		extensions.add(new DomainExtension("tenantPreference-domain", "/META-INF/tenantPreference-domain.xml"));
		extensions.add(new DomainExtension("rule-domain", "/META-INF/rule-domain.xml"));
		extensions.add(new DomainExtension("order-domain", "/META-INF/order-domain.xml"));
		extensions.add(new DomainExtension("shipment-domain", "/META-INF/shipment-domain.xml"));
		extensions.add(new DomainExtension("return-domain", "/META-INF/return-domain.xml"));
		extensions.add(new DomainExtension("i18n-domain", "/META-INF/i18n-domain.xml"));
		extensions.add(new DomainExtension("valuetypes-domain", "/META-INF/valuetypes-domain.xml"));
		extensions.add(new DomainExtension("order-mixin-domain", "/META-INF/order-mixin-domain.xml"));

		final TypeSystem typeSystem = typeSystemReader.readTypeSystem(extensions);
		final Attribute baseAttribute = typeSystem.getManagedObjectType("OrderData").getAttribute("orderId");
		final Attribute mixinAttribute = typeSystem.getManagedObjectType("OrderData").getAttribute("dummy");

		Assert.assertNotNull(baseAttribute);
		Assert.assertNotNull(mixinAttribute);
	}
}
