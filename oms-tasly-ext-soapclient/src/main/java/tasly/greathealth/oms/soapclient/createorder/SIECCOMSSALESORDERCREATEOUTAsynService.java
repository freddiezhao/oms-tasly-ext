package tasly.greathealth.oms.soapclient.createorder;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 *
 */
@WebServiceClient(name = "SI_ECC_OMS_SALESORDER_CREATE_OUT_AsynService", targetNamespace = "urn:tasly:gerp:jt:ecc:proxy", wsdlLocation = "http://gerppidev.tsldomain.com:50000/dir/wsdl?p=sa/aa22c5861c9b38388541def4ebd9d540")
public class SIECCOMSSALESORDERCREATEOUTAsynService extends Service
{

	private final static URL SIECCOMSSALESORDERCREATEOUTASYNSERVICE_WSDL_LOCATION;
	private final static WebServiceException SIECCOMSSALESORDERCREATEOUTASYNSERVICE_EXCEPTION;
	private final static QName SIECCOMSSALESORDERCREATEOUTASYNSERVICE_QNAME = new QName("urn:tasly:gerp:jt:ecc:proxy",
			"SI_ECC_OMS_SALESORDER_CREATE_OUT_AsynService");

	static
	{
		URL url = null;
		WebServiceException e = null;
		try
		{
			url = new URL("http://gerppidev.tsldomain.com:50000/dir/wsdl?p=sa/aa22c5861c9b38388541def4ebd9d540");
		}
		catch (final MalformedURLException ex)
		{
			e = new WebServiceException(ex);
		}
		SIECCOMSSALESORDERCREATEOUTASYNSERVICE_WSDL_LOCATION = url;
		SIECCOMSSALESORDERCREATEOUTASYNSERVICE_EXCEPTION = e;
	}

	public SIECCOMSSALESORDERCREATEOUTAsynService()
	{
		super(__getWsdlLocation(), SIECCOMSSALESORDERCREATEOUTASYNSERVICE_QNAME);
	}

	public SIECCOMSSALESORDERCREATEOUTAsynService(final WebServiceFeature... features)
	{
		super(__getWsdlLocation(), SIECCOMSSALESORDERCREATEOUTASYNSERVICE_QNAME, features);
	}

	public SIECCOMSSALESORDERCREATEOUTAsynService(final URL wsdlLocation)
	{
		super(wsdlLocation, SIECCOMSSALESORDERCREATEOUTASYNSERVICE_QNAME);
	}

	public SIECCOMSSALESORDERCREATEOUTAsynService(final URL wsdlLocation, final WebServiceFeature... features)
	{
		super(wsdlLocation, SIECCOMSSALESORDERCREATEOUTASYNSERVICE_QNAME, features);
	}

	public SIECCOMSSALESORDERCREATEOUTAsynService(final URL wsdlLocation, final QName serviceName)
	{
		super(wsdlLocation, serviceName);
	}

	public SIECCOMSSALESORDERCREATEOUTAsynService(final URL wsdlLocation, final QName serviceName,
			final WebServiceFeature... features)
	{
		super(wsdlLocation, serviceName, features);
	}

	/**
	 * 
	 * @return
	 *         returns SIECCOMSSALESORDERCREATEOUTAsyn
	 */
	@WebEndpoint(name = "HTTP_Port")
	public SIECCOMSSALESORDERCREATEOUTAsyn getHTTPPort()
	{
		return super.getPort(new QName("urn:tasly:gerp:jt:ecc:proxy", "HTTP_Port"), SIECCOMSSALESORDERCREATEOUTAsyn.class);
	}

	/**
	 * 
	 * @param features
	 *           A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in
	 *           the <code>features</code> parameter will have their default values.
	 * @return
	 *         returns SIECCOMSSALESORDERCREATEOUTAsyn
	 */
	@WebEndpoint(name = "HTTP_Port")
	public SIECCOMSSALESORDERCREATEOUTAsyn getHTTPPort(final WebServiceFeature... features)
	{
		return super
				.getPort(new QName("urn:tasly:gerp:jt:ecc:proxy", "HTTP_Port"), SIECCOMSSALESORDERCREATEOUTAsyn.class, features);
	}

	/**
	 * 
	 * @return
	 *         returns SIECCOMSSALESORDERCREATEOUTAsyn
	 */
	@WebEndpoint(name = "HTTPS_Port")
	public SIECCOMSSALESORDERCREATEOUTAsyn getHTTPSPort()
	{
		return super.getPort(new QName("urn:tasly:gerp:jt:ecc:proxy", "HTTPS_Port"), SIECCOMSSALESORDERCREATEOUTAsyn.class);
	}

	/**
	 * 
	 * @param features
	 *           A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in
	 *           the <code>features</code> parameter will have their default values.
	 * @return
	 *         returns SIECCOMSSALESORDERCREATEOUTAsyn
	 */
	@WebEndpoint(name = "HTTPS_Port")
	public SIECCOMSSALESORDERCREATEOUTAsyn getHTTPSPort(final WebServiceFeature... features)
	{
		return super.getPort(new QName("urn:tasly:gerp:jt:ecc:proxy", "HTTPS_Port"), SIECCOMSSALESORDERCREATEOUTAsyn.class,
				features);
	}

	private static URL __getWsdlLocation()
	{
		if (SIECCOMSSALESORDERCREATEOUTASYNSERVICE_EXCEPTION != null)
		{
			throw SIECCOMSSALESORDERCREATEOUTASYNSERVICE_EXCEPTION;
		}
		return SIECCOMSSALESORDERCREATEOUTASYNSERVICE_WSDL_LOCATION;
	}

}
