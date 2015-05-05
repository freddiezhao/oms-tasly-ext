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
package tasly.greathealth.oms.web.express.rest.resources;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import tasly.greathealth.oms.api.order.ExpressItemQueryObject;
import tasly.greathealth.oms.api.order.ExpressLocationQueryObject;
import tasly.greathealth.oms.api.order.ExpressQueryObject;
import tasly.greathealth.oms.api.order.TaslyExpressFacade;
import tasly.greathealth.oms.api.order.dto.Express;
import tasly.greathealth.oms.api.order.dto.ExpressItem;
import tasly.greathealth.oms.api.order.dto.Expresslocation;
import tasly.greathealth.oms.log.OmsLoggerFactory;


/**
 *
 */
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
@Path("/taslyExpress")
public class TaslyExpressResource
{
	private static final Logger LOGGER = OmsLoggerFactory.getOmsorderlog();

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	private TaslyExpressFacade taslyExpressFacade;

	/**
	 * @return the taslyExpressFacade
	 */
	public TaslyExpressFacade getTaslyExpressFacade()
	{
		return taslyExpressFacade;
	}

	/**
	 * @param taslyExpressFacade the taslyExpressFacade to set
	 */
	@Required
	public void setTaslyExpressFacade(final TaslyExpressFacade taslyExpressFacade)
	{
		this.taslyExpressFacade = taslyExpressFacade;
	}

	@Path("/createOrUpdate/expressItem")
	@POST
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response createOrUpdateExpressItem(final ExpressItem expressItem)
	{
		taslyExpressFacade.createOrUpdataExpressItem(expressItem);
		return Response.status(Response.Status.OK).build();
	}

	@Path("/createOrUpdate/expressLocation")
	@POST
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response createOrUpdateExpressLocation(@RequestBody final Expresslocation expressLocation)
	{
		Expresslocation result = null;
		try
		{
			result = taslyExpressFacade.createOrUpdataExpresslocation(expressLocation);
		}
		catch (final RuntimeException r)
		{
			Response.ok(r.getMessage()).build();
		}
		return Response.ok(result).build();
	}

	@Path("/createOrUpdate/express")
	@POST
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response createOrUpdateExpress(@RequestBody final Express express)
	{
		Express result = null;
		try
		{
			result = taslyExpressFacade.createOrUpdataExpress(express);
		}
		catch (final RuntimeException r)
		{
			Response.ok(r.getMessage()).build();
		}
		return Response.ok(result).build();
	}

	/**
	 * 初始化快递公司数据
	 *
	 * @return
	 */
	@Path("/initializeExpress")
	@POST
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response initializeExpress()
	{
		final Express express1 = new Express();
		express1.setCode("YUNDA");
		express1.setName("韵达快递");

		final Express express2 = new Express();
		express2.setCode("ZJS");
		express2.setName("宅急送");

		final Express express3 = new Express();
		express3.setCode("SF");
		express3.setName("顺丰速运");

		final Express express4 = new Express();
		express4.setCode("EMS");
		express4.setName("EMS");

		final Express express5 = new Express();
		express5.setCode("YTO");
		express5.setName("圆通速递");

		final Express express6 = new Express();
		express6.setCode("STO");
		express6.setName("申通快递");

		final Express express7 = new Express();
		express7.setCode("ZTO");
		express7.setName("中通快递");

		final Express express8 = new Express();
		express8.setCode("HTKY");
		express8.setName("百世汇通");

		// final Express express4 = new Express();
		// express4.setCode("CYEXP");
		// express4.setName("长宇");
		//
		// final Express express5 = new Express();
		// express5.setCode("DTW");
		// express5.setName("大田");
		//
		// final Express express6 = new Express();
		// express6.setCode("YUD");
		// express6.setName("长发");
		//
		// final Express express7 = new Express();
		// express7.setCode("DFH");
		// express7.setName("东方汇");
		//
		// final Express express8 = new Express();
		// express8.setCode("SY");
		// express8.setName("首业");

		// final Express express9 = new Express();
		// express9.setCode("YC");
		// express9.setName("远长");
		//
		// final Express express10 = new Express();
		// express10.setCode("UNIPS");
		// express10.setName("发网");
		//
		// final Express express11 = new Express();
		// express11.setCode("GZLT");
		// express11.setName("飞远配送");
		//
		// final Express express12 = new Express();
		// express12.setCode("MGSD");
		// express12.setName("美国速递");
		//
		// final Express express13 = new Express();
		// express13.setCode("PKGJWL");
		// express13.setName("派易国际物流77");
		//
		// final Express express14 = new Express();
		// express14.setCode("BHWL");
		// express14.setName("保宏物流");
		//
		// final Express express15 = new Express();
		// express15.setCode("EMS");
		// express15.setName("EMS");
		//
		// final Express express16 = new Express();
		// express16.setCode("YTO");
		// express16.setName("圆通速递");
		//
		// final Express express17 = new Express();
		// express17.setCode("POST");
		// express17.setName("中国邮政");
		//
		// final Express express18 = new Express();
		// express18.setCode("AIR");
		// express18.setName("亚风");
		//
		// final Express express19 = new Express();
		// express19.setCode("UC");
		// express19.setName("优速快递");
		//
		// final Express express20 = new Express();
		// express20.setCode("YANWENINTE");
		// express20.setName("燕文国际");
		//
		// final Express express21 = new Express();
		// express21.setCode("YANWENSH");
		// express21.setName("燕文上海");
		//
		// final Express express22 = new Express();
		// express22.setCode("YANWENSZ");
		// express22.setName("燕文深圳");
		//
		// final Express express23 = new Express();
		// express23.setCode("YANWENBJ");
		// express23.setName("燕文北京");
		//
		// final Express express24 = new Express();
		// express24.setCode("YANWENYW");
		// express24.setName("燕文义乌");
		//
		// final Express express25 = new Express();
		// express25.setCode("YANWENGZ");
		// express25.setName("燕文广州");
		//
		// final Express express26 = new Express();
		// express26.setCode("ZTOGZ");
		// express26.setName("ZTOGZ");
		//
		// final Express express27 = new Express();
		// express27.setCode("RUSTON");
		// express27.setName("RUSTON");
		//
		// final Express express28 = new Express();
		// express28.setCode("ZTOSH");
		// express28.setName("ZTOSH");
		//
		// final Express express29 = new Express();
		// express29.setCode("GZFY");
		// express29.setName("凡宇速递");
		//
		// final Express express30 = new Express();
		// express30.setCode("LTS");
		// express30.setName("联昊通");
		//
		// final Express express31 = new Express();
		// express31.setCode("QFKD");
		// express31.setName("全峰快递");
		//
		// final Express express32 = new Express();
		// express32.setCode("UAPEX");
		// express32.setName("全一快递");
		//
		// final Express express33 = new Express();
		// express33.setCode("BJCS");
		// express33.setName("城市100");
		//
		// final Express express34 = new Express();
		// express34.setCode("GDEMS");
		// express34.setName("广东EMS");
		//
		// final Express express35 = new Express();
		// express35.setCode("SURE");
		// express35.setName("速尔");
		//
		// final Express express36 = new Express();
		// express36.setCode("EYB");
		// express36.setName("EMS经济快递");
		//
		// final Express express37 = new Express();
		// express37.setCode("ZTO");
		// express37.setName("中通快递");
		//
		// final Express express38 = new Express();
		// express38.setCode("HZABC");
		// express38.setName("飞远(爱彼西)配送");
		//
		// final Express express40 = new Express();
		// express40.setCode("TTKDEX");
		// express40.setName("天天快递");
		//
		// final Express express41 = new Express();
		// express41.setCode("CNEX");
		// express41.setName("佳吉快递");
		//
		// final Express express42 = new Express();
		// express42.setCode("BEST");
		// express42.setName("百世物流");
		//
		// final Express express43 = new Express();
		// express43.setCode("FEDEX");
		// express43.setName("联邦快递");
		//
		// final Express express44 = new Express();
		// express44.setCode("SHQ");
		// express44.setName("华强物流");
		//
		// final Express express45 = new Express();
		// express45.setCode("HTKY");
		// express45.setName("百世汇通");
		//
		// final Express express46 = new Express();
		// express46.setCode("CRE");
		// express46.setName("中铁快运");
		//
		// final Express express47 = new Express();
		// express47.setCode("XFWL");
		// express47.setName("信丰物流");
		//
		// final Express express48 = new Express();
		// express48.setCode("STO");
		// express48.setName("申通快递");
		//
		// final Express express49 = new Express();
		// express49.setCode("LB");
		// express49.setName("龙邦速递");
		//
		// final Express express50 = new Express();
		// express50.setCode("HOAU");
		// express50.setName("天地华宇");
		//
		// final Express express51 = new Express();
		// express51.setCode("FAST");
		// express51.setName("快捷快递");
		//
		// final Express express52 = new Express();
		// express52.setCode("POSTB");
		// express52.setName("邮政国内小包");
		//
		// final Express express53 = new Express();
		// express53.setCode("YCT");
		// express53.setName("黑猫宅急便");
		//
		// final Express express54 = new Express();
		// express54.setCode("XB");
		// express54.setName("新邦物流");
		//
		// final Express express55 = new Express();
		// express55.setCode("QRT");
		// express55.setName("增益速递");
		//
		// final Express express56 = new Express();
		// express56.setCode("GTO");
		// express56.setName("国通快递");
		//
		// final Express express57 = new Express();
		// express57.setCode("DBL");
		// express57.setName("德邦物流");
		//
		// final Express express58 = new Express();
		// express58.setCode("ESB");
		// express58.setName("E速宝");
		//
		// final Express express59 = new Express();
		// express59.setCode("JWY");
		// express59.setName("居无忧");
		//
		// final Express express60 = new Express();
		// express60.setCode("BOYOL");
		// express60.setName("贝业新兄弟");
		//
		// final Express express61 = new Express();
		// express61.setCode("YS");
		// express61.setName("合众阳晟");
		//
		// final Express express62 = new Express();
		// express62.setCode("OTHER");
		// express62.setName("其他");

		this.taslyExpressFacade.createOrUpdataExpress(express1);
		this.taslyExpressFacade.createOrUpdataExpress(express2);
		this.taslyExpressFacade.createOrUpdataExpress(express3);
		this.taslyExpressFacade.createOrUpdataExpress(express4);
		this.taslyExpressFacade.createOrUpdataExpress(express5);
		this.taslyExpressFacade.createOrUpdataExpress(express6);
		this.taslyExpressFacade.createOrUpdataExpress(express7);
		this.taslyExpressFacade.createOrUpdataExpress(express8);

		return Response.status(Response.Status.OK).build();
	}

	/**
	 * 初始化快递Location数据
	 *
	 * @return
	 */
	@Path("/initializeExpressLocation")
	@POST
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response initializeExpressLocation()
	{
		final Expresslocation expresslocation1 = new Expresslocation();
		expresslocation1.setChannelSource("TMALL");
		expresslocation1.setInnerSource("OTC");
		expresslocation1.setProvince("天津");
		expresslocation1.setExpressCode("YUNDA");
		final Expresslocation expresslocation2 = new Expresslocation();
		expresslocation2.setChannelSource("TMALL");
		expresslocation2.setInnerSource("OTC");
		expresslocation2.setProvince("北京");
		expresslocation2.setExpressCode("ZJS");
		final Expresslocation expresslocation3 = new Expresslocation();
		expresslocation3.setChannelSource("TMALL");
		expresslocation3.setInnerSource("OTC");
		expresslocation3.setProvince("河北省");
		expresslocation3.setExpressCode("ZJS");
		final Expresslocation expresslocation4 = new Expresslocation();
		expresslocation4.setChannelSource("TMALL");
		expresslocation4.setInnerSource("OTC");
		expresslocation4.setProvince("山东省");
		expresslocation4.setExpressCode("YUNDA");
		final Expresslocation expresslocation5 = new Expresslocation();
		expresslocation5.setChannelSource("TMALL");
		expresslocation5.setInnerSource("OTC");
		expresslocation5.setProvince("山西省");
		expresslocation5.setExpressCode("YUNDA");
		final Expresslocation expresslocation6 = new Expresslocation();
		expresslocation6.setChannelSource("TMALL");
		expresslocation6.setInnerSource("OTC");
		expresslocation6.setProvince("上海");
		expresslocation6.setExpressCode("YUNDA");
		final Expresslocation expresslocation7 = new Expresslocation();
		expresslocation7.setChannelSource("TMALL");
		expresslocation7.setInnerSource("OTC");
		expresslocation7.setProvince("江苏省");
		expresslocation7.setExpressCode("YUNDA");
		final Expresslocation expresslocation8 = new Expresslocation();
		expresslocation8.setChannelSource("TMALL");
		expresslocation8.setInnerSource("OTC");
		expresslocation8.setProvince("浙江省");
		expresslocation8.setExpressCode("YUNDA");
		final Expresslocation expresslocation9 = new Expresslocation();
		expresslocation9.setChannelSource("TMALL");
		expresslocation9.setInnerSource("OTC");
		expresslocation9.setProvince("福建省");
		expresslocation9.setExpressCode("YUNDA");
		final Expresslocation expresslocation10 = new Expresslocation();
		expresslocation10.setChannelSource("TMALL");
		expresslocation10.setInnerSource("OTC");
		expresslocation10.setProvince("广东省");
		expresslocation10.setExpressCode("YUNDA");
		final Expresslocation expresslocation11 = new Expresslocation();
		expresslocation11.setChannelSource("TMALL");
		expresslocation11.setInnerSource("OTC");
		expresslocation11.setProvince("安徽省");
		expresslocation11.setExpressCode("YUNDA");
		final Expresslocation expresslocation12 = new Expresslocation();
		expresslocation12.setChannelSource("TMALL");
		expresslocation12.setInnerSource("OTC");
		expresslocation12.setProvince("河南省");
		expresslocation12.setExpressCode("YUNDA");
		final Expresslocation expresslocation13 = new Expresslocation();
		expresslocation13.setChannelSource("TMALL");
		expresslocation13.setInnerSource("OTC");
		expresslocation13.setProvince("黑龙江省");
		expresslocation13.setExpressCode("YUNDA");
		final Expresslocation expresslocation14 = new Expresslocation();
		expresslocation14.setChannelSource("TMALL");
		expresslocation14.setInnerSource("OTC");
		expresslocation14.setProvince("辽宁省");
		expresslocation14.setExpressCode("YUNDA");
		final Expresslocation expresslocation15 = new Expresslocation();
		expresslocation15.setChannelSource("TMALL");
		expresslocation15.setInnerSource("OTC");
		expresslocation15.setProvince("吉林省");
		expresslocation15.setExpressCode("YUNDA");
		final Expresslocation expresslocation16 = new Expresslocation();
		expresslocation16.setChannelSource("TMALL");
		expresslocation16.setInnerSource("OTC");
		expresslocation16.setProvince("内蒙古自治区");
		expresslocation16.setExpressCode("YUNDA");
		final Expresslocation expresslocation17 = new Expresslocation();
		expresslocation17.setChannelSource("TMALL");
		expresslocation17.setInnerSource("OTC");
		expresslocation17.setProvince("江西省");
		expresslocation17.setExpressCode("YUNDA");
		final Expresslocation expresslocation18 = new Expresslocation();
		expresslocation18.setChannelSource("TMALL");
		expresslocation18.setInnerSource("OTC");
		expresslocation18.setProvince("湖北省");
		expresslocation18.setExpressCode("YUNDA");
		final Expresslocation expresslocation19 = new Expresslocation();
		expresslocation19.setChannelSource("TMALL");
		expresslocation19.setInnerSource("OTC");
		expresslocation19.setProvince("湖南省");
		expresslocation19.setExpressCode("YUNDA");
		final Expresslocation expresslocation20 = new Expresslocation();
		expresslocation20.setChannelSource("TMALL");
		expresslocation20.setInnerSource("OTC");
		expresslocation20.setProvince("陕西省");
		expresslocation20.setExpressCode("YUNDA");
		final Expresslocation expresslocation21 = new Expresslocation();
		expresslocation21.setChannelSource("TMALL");
		expresslocation21.setInnerSource("OTC");
		expresslocation21.setProvince("四川省");
		expresslocation21.setExpressCode("YUNDA");
		final Expresslocation expresslocation22 = new Expresslocation();
		expresslocation22.setChannelSource("TMALL");
		expresslocation22.setInnerSource("OTC");
		expresslocation22.setProvince("重庆");
		expresslocation22.setExpressCode("YUNDA");
		final Expresslocation expresslocation23 = new Expresslocation();
		expresslocation23.setChannelSource("TMALL");
		expresslocation23.setInnerSource("OTC");
		expresslocation23.setProvince("宁夏回族自治区");
		expresslocation23.setExpressCode("YUNDA");
		final Expresslocation expresslocation24 = new Expresslocation();
		expresslocation24.setChannelSource("TMALL");
		expresslocation24.setInnerSource("OTC");
		expresslocation24.setProvince("广西壮族自治区");
		expresslocation24.setExpressCode("YUNDA");
		final Expresslocation expresslocation25 = new Expresslocation();
		expresslocation25.setChannelSource("TMALL");
		expresslocation25.setInnerSource("OTC");
		expresslocation25.setProvince("贵州省");
		expresslocation25.setExpressCode("YUNDA");
		final Expresslocation expresslocation26 = new Expresslocation();
		expresslocation26.setChannelSource("TMALL");
		expresslocation26.setInnerSource("OTC");
		expresslocation26.setProvince("云南省");
		expresslocation26.setExpressCode("YUNDA");
		final Expresslocation expresslocation27 = new Expresslocation();
		expresslocation27.setChannelSource("TMALL");
		expresslocation27.setInnerSource("OTC");
		expresslocation27.setProvince("海南省");
		expresslocation27.setExpressCode("YUNDA");
		final Expresslocation expresslocation28 = new Expresslocation();
		expresslocation28.setChannelSource("TMALL");
		expresslocation28.setInnerSource("OTC");
		expresslocation28.setProvince("青海省");
		expresslocation28.setExpressCode("YUNDA");
		final Expresslocation expresslocation29 = new Expresslocation();
		expresslocation29.setChannelSource("TMALL");
		expresslocation29.setInnerSource("OTC");
		expresslocation29.setProvince("甘肃省");
		expresslocation29.setExpressCode("YUNDA");
		final Expresslocation expresslocation30 = new Expresslocation();
		expresslocation30.setChannelSource("TMALL");
		expresslocation30.setInnerSource("OTC");
		expresslocation30.setProvince("新疆维吾尔自治区");
		expresslocation30.setExpressCode("YUNDA");
		final Expresslocation expresslocation31 = new Expresslocation();
		expresslocation31.setChannelSource("TMALL");
		expresslocation31.setInnerSource("OTC");
		expresslocation31.setProvince("西藏自治区");
		expresslocation31.setExpressCode("YUNDA");

		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation1);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation2);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation3);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation4);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation5);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation6);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation7);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation8);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation9);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation10);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation11);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation12);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation13);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation14);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation15);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation16);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation17);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation18);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation19);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation20);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation21);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation22);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation23);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation24);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation25);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation26);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation27);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation28);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation29);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation30);
		this.taslyExpressFacade.createOrUpdataExpresslocation(expresslocation31);

		return Response.status(Response.Status.OK).build();
	}

	/**
	 * 初始化快递公司Item数据
	 *
	 * @return
	 */
	@Path("/initializeExpressItem")
	@POST
	@Secured({"ROLE_admin", "ROLE_shippinguser", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_instorepickup",
			"ROLE_instorepickupandshipping"})
	public Response initializeExpressItem()
	{
		final ExpressItem expressItem1 = new ExpressItem();
		expressItem1.setChannelSource("TMALL");
		expressItem1.setInnerSource("OTC");
		expressItem1.setExpressCode("SF");
		expressItem1.setSkuid("60001537");

		final ExpressItem expressItem2 = new ExpressItem();
		expressItem2.setChannelSource("TMALL");
		expressItem2.setInnerSource("OTC");
		expressItem2.setExpressCode("SF");
		expressItem2.setSkuid("60001971");

		final ExpressItem expressItem3 = new ExpressItem();
		expressItem3.setChannelSource("TMALL");
		expressItem3.setInnerSource("OTC");
		expressItem3.setExpressCode("SF");
		expressItem3.setSkuid("60001970");

		final ExpressItem expressItem4 = new ExpressItem();
		expressItem4.setChannelSource("TMALL");
		expressItem4.setInnerSource("OTC");
		expressItem4.setExpressCode("SF");
		expressItem4.setSkuid("60002025");

		final ExpressItem expressItem5 = new ExpressItem();
		expressItem5.setChannelSource("TMALL");
		expressItem5.setInnerSource("OTC");
		expressItem5.setExpressCode("SF");
		expressItem5.setSkuid("60002007");

		final ExpressItem expressItem6 = new ExpressItem();
		expressItem6.setChannelSource("TMALL");
		expressItem6.setInnerSource("OTC");
		expressItem6.setExpressCode("SF");
		expressItem6.setSkuid("60002008");

		final ExpressItem expressItem7 = new ExpressItem();
		expressItem7.setChannelSource("TMALL");
		expressItem7.setInnerSource("OTC");
		expressItem7.setExpressCode("SF");
		expressItem7.setSkuid("60001624");

		final ExpressItem expressItem8 = new ExpressItem();
		expressItem8.setChannelSource("TMALL");
		expressItem8.setInnerSource("OTC");
		expressItem8.setExpressCode("SF");
		expressItem8.setSkuid("60001906");

		taslyExpressFacade.createOrUpdataExpressItem(expressItem1);
		taslyExpressFacade.createOrUpdataExpressItem(expressItem2);
		taslyExpressFacade.createOrUpdataExpressItem(expressItem3);
		taslyExpressFacade.createOrUpdataExpressItem(expressItem4);
		taslyExpressFacade.createOrUpdataExpressItem(expressItem5);
		taslyExpressFacade.createOrUpdataExpressItem(expressItem6);
		taslyExpressFacade.createOrUpdataExpressItem(expressItem7);
		taslyExpressFacade.createOrUpdataExpressItem(expressItem8);

		return Response.status(Response.Status.OK).build();
	}

	@GET
	@Path("/findExpressByQuery")
	@TypeHint(Express.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response findExpressByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findExpressByQuery");

		final ExpressQueryObject queryObject = new ExpressQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<Express> expressList = this.taslyExpressFacade.findExpressByQuery(queryObject);
		final GenericEntity<List<Express>> entity = new GenericEntity<List<Express>>(expressList.getResults())
				{
			// DONOTHING
				};
				final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(expressList.getNextPage().intValue(),
						expressList.getPreviousPage().intValue(), expressList.getTotalPages().intValue(), expressList.getTotalRecords()
						.longValue());

				return responseBuilder.entity(entity).build();
	}

	@GET
	@Path("/findExpressLocationByQuery")
	@TypeHint(Express.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response findExpressLocationByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findExpressLocationByQuery");

		final ExpressLocationQueryObject queryObject = new ExpressLocationQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<Expresslocation> expressLocationList = this.taslyExpressFacade.findExpressLocationByQuery(queryObject);
		final GenericEntity<List<Expresslocation>> entity = new GenericEntity<List<Expresslocation>>(
				expressLocationList.getResults())
				{
			// DONOTHING
				};
				final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(expressLocationList.getNextPage()
						.intValue(), expressLocationList.getPreviousPage().intValue(), expressLocationList.getTotalPages().intValue(),
						expressLocationList.getTotalRecords().longValue());

				return responseBuilder.entity(entity).build();
	}

	@GET
	@Path("/findExpressItemByQuery")
	@TypeHint(Express.class)
	@Secured({"ROLE_admin", "ROLE_fulfillmentmanager", "ROLE_accelerator", "ROLE_shippinguser", "ROLE_instorepickup",
	"ROLE_instorepickupandshipping"})
	public Response findExpressItemByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findExpressItemByQuery");

		final ExpressItemQueryObject queryObject = new ExpressItemQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<ExpressItem> expressItemList = this.taslyExpressFacade.findExpressItemByQuery(queryObject);
		final GenericEntity<List<ExpressItem>> entity = new GenericEntity<List<ExpressItem>>(expressItemList.getResults())
				{
			// DONOTHING
				};
				final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(expressItemList.getNextPage()
						.intValue(), expressItemList.getPreviousPage().intValue(), expressItemList.getTotalPages().intValue(),
						expressItemList.getTotalRecords().longValue());

				return responseBuilder.entity(entity).build();
	}

	@DELETE
	@Path("/deleteExpress/{expressCode}")
	public Response deleteExpress(@PathParam("expressCode") final String code)
	{
		LOGGER.info("Delete Express! express Code[" + code + "]");
		taslyExpressFacade.deleteExpress(code);
		return Response.ok().build();
	}

	@DELETE
	@Path("/deleteExpressLocation/{province}/{channelSource}/{innerSource}")
	public Response deleteExpressLocation(@PathParam("province") final String province,
			@PathParam("channelSource") final String channelSource, @PathParam("innerSource") final String innerSource)
	{
		LOGGER.info("Delete ExpressLocation! express province[" + province + "]");
		taslyExpressFacade.deleteExpressLocation(province, channelSource, innerSource);
		return Response.ok().build();
	}

	@DELETE
	@Path("/deleteExpressItem/{skuid}/{innerSource}/{channelSource}")
	public Response deleteExpressItem(@PathParam("skuid") final String skuid, @PathParam("innerSource") final String innerSource,
			@PathParam("channelSource") final String channelSource)
	{
		LOGGER.info("Delete ExpressItem! express skuid[" + skuid + "],innerSource[" + innerSource + "],channelSource["
				+ channelSource + "]");
		taslyExpressFacade.deleteExpressItem(skuid, innerSource, channelSource);
		return Response.ok().build();
	}
}
