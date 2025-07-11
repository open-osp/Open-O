package org.oscarehr.integration.ebs.client.ng;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.apache.xml.security.exceptions.AlgorithmAlreadyRegisteredException;
import org.apache.xml.security.utils.resolver.ResourceResolver;

import ca.ontario.health.ebs.idp.IdpHeader;

/**
 * Client that encapsulates MCEDT service access.
 * 
 * <h3>Implementation Details</h3>
 * 
 * MCEDT service operates under IPD (Identity Provider Model) authentication
 * model. This means that the sender must use public key technology to sign the
 * SOAP headers and body. The signing certificate can be any available
 * certificate and can be self-signed. If any response data is specified to be
 * encrypted, at least AES128-CBC symmetric encryption algorithm with the public
 * key belonging to the signer of the initial SOAP request must be used.
 * 
 * <p/>
 * 
 * The SOAP message must contain the EBS and IDP headers in the SOAP message
 * header with the user name and password (for the Go-Secure IDP in a
 * WS-Security username token). The SOAP headers and body are then digitally
 * signed to guarantee message integrity and source.
 * 
 * <p/>
 * 
 * If any request data is specified to be encrypted, by the specific web
 * service, it will use the public key of the EBS system. SOAP must be signed
 * with a Timestamp element for each message TTL for the SOAP message
 * will be 10 minutes. Each message must also include the Username token.
 * 
 * <p/>
 * 
 * EBS SOAP header must contains the software conformance key and the service
 * requester
 * audit ID. Security header must be specified with "must understand" flag set
 * to 1
 * 
 * <p/>
 *
 * The service requester must sign all headers and the body using a certificate
 * issued by an issuer approved by MHLTC. The signature must meet the following requirements:
 * Identifier Type
 * - Key Identifier Type: Binary Security Token, Direct Reference Signature
 * - Signature Canonicalization: Inclusive Canonicalization
 */
public class EdtClientBuilder {

	private static final String DEFAULT_CLIENT_KEYSTORE = "clientKeystore.properties";
	private static final String TAG_NAME_AUDIT_ID = "AuditId";
	private static final String TAG_NAME_SOFTWARE_CONFORMANCE_KEY = "SoftwareConformanceKey";
	private static final String TAG_NAME_EBS = "EBS";
	private static final String TAG_NAME_IDP = "IDP";
	private static final String TAG_NAME_SERVICE_USER_MUID = "ServiceUserMUID";
	private static final String NS_EBS = "http://ebs.health.ontario.ca/";
	private static final String NS_IDP = "http://idp.ebs.health.ontario.ca/";

	private static final QName QNAME_IDP = new QName(NS_IDP, TAG_NAME_IDP, "idp");
	private static final QName QNAME_EBS = new QName(NS_EBS, TAG_NAME_EBS, "ebs");

	private static AtomicBoolean isInitialized = new AtomicBoolean(false);

	private static String clientKeystore = DEFAULT_CLIENT_KEYSTORE;
	/**
	 * CXF directive for determining which message elements needs to be signed as
	 * declared in the
	 * MCEDT spec
	 */
	private static final String SIGNED_MESSAGE_ELEMENTS = "{Element}{"
			+ NS_EBS
			+ "}EBS;"
			+ "{Element}{"
			+ NS_IDP
			+ "}IDP;"
			+ "{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp;"
			+ "{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body;";

	protected EdtClientBuilderConfig config;

	private static void registerAttachmentResolver() {
		if (isInitialized.compareAndSet(false, true)) {
			ResourceResolver.register(AttachmentResolverSpi.class, true);
			try {
				org.apache.xml.security.transforms.Transform.register(
						TransformAttachmentCiphertext.TRANSFORM_ATTACHMENT_CIPHERTEXT,
						TransformAttachmentCiphertext.class);
			} catch (AlgorithmAlreadyRegisteredException e) {
				// swallow
			}
		}
	}

	/**
	 * Initializes the EdtClientBuilder and registers the attachment resolver.
	 */
	public EdtClientBuilder() {
		registerAttachmentResolver();
	}

	/**
	 * Initializes the EdtClientBuilder with the specified configuration.
	 *
	 * @param config the configuration to be used by this builder
	 */
	public EdtClientBuilder(EdtClientBuilderConfig config) {
		this();
		setConfig(config);
	}

	/**
	 * Retrieves the current configuration for this client builder.
	 *
	 * @return the configuration being used by this client builder
	 */
	public EdtClientBuilderConfig getConfig() {
		return config;
	}

	/**
	 * Sets the configuration for this client builder.
	 *
	 * @param config the configuration to set
	 */
	public void setConfig(EdtClientBuilderConfig config) {
		this.config = config;
	}

	/**
	 * Initializes service client for carrying out the request operation.
	 *
	 * @param clientClass the class of the service client to be built
     * @return an instance of the service client
     * @param <T> the type of the service client
	 */
	public <T> T build(Class<T> clientClass) {
		T result = newDelegate(clientClass);

		Client client = ClientProxy.getClient(result);
		configureSsl((HTTPConduit) client.getConduit());
		configureOutInterceptor(client);
		configureInInterceptor(client);

		BindingProvider bindingProvider = ((BindingProvider) result);
		SOAPBinding sp = (SOAPBinding) bindingProvider.getBinding();
		if (getConfig().isMtomEnabled()) {
			sp.setMTOMEnabled(true);
		}

		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put("com.sun.xml.internal.ws.request.timeout", 1000 * 240); // Timeout in millis

		HTTPConduit conduit = (HTTPConduit) client.getConduit();

		// HTTPClientPolicy - Properties used to configure a client-side HTTP port
		HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy(); // Line #1
		httpClientPolicy.setConnectionTimeout(100000); // Line #2
		httpClientPolicy.setReceiveTimeout(240000); // Line #3

		conduit.setClient(httpClientPolicy);

		bindingProvider.getRequestContext().put("signaturePropFile", clientKeystore);

		try {
			configureHeaderList(bindingProvider);
		} catch (Exception e) {
			throw new RuntimeException("Unable to configure header list", e);
		}
		return result;
	}

	/**
	 * Creates a new delegate instance of the specified client class.
	 *
	 * @param clientClass the class of the client to create
	 * @return a new instance of the client
	 * @param <T> the type of the client class
	 */
	@SuppressWarnings("unchecked")
	protected <T> T newDelegate(Class<T> clientClass) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(clientClass);
		factory.setAddress(getConfig().getServiceUrl());
		return (T) factory.create();
	}

	/**
	 * Configures the header list to be included in the SOAP request.
	 *
	 * @param bindingProvider the binding provider to configure
	 * @throws Exception if an error occurs during configuration
	 */
	protected void configureHeaderList(BindingProvider bindingProvider) throws Exception {
		SOAPFactory sf = SOAPFactory.newInstance();
		// initializes custom IDP header
		SOAPElement ebsHeader = sf.createElement(QNAME_EBS);
		SOAPElement softwareConformanceKey = sf
				.createElement(TAG_NAME_SOFTWARE_CONFORMANCE_KEY);
		softwareConformanceKey.setTextContent(getConfig().getConformanceKey());
		ebsHeader.addChildElement(softwareConformanceKey);
		SOAPElement auditId = sf.createElement(TAG_NAME_AUDIT_ID);
		auditId.setTextContent(getConfig().getAuditId());
		ebsHeader.addChildElement(auditId);
		SOAPElement idpHeader = sf.createElement(QNAME_IDP);
		SOAPElement serviceUserMUID = sf
				.createElement(TAG_NAME_SERVICE_USER_MUID);
		serviceUserMUID.setTextContent(getConfig().getServiceId());
		idpHeader.addChildElement(serviceUserMUID);

		List<Header> headersList = new ArrayList<Header>();
		headersList.add(new Header(QNAME_EBS, ebsHeader));
		headersList.add(new Header(QNAME_IDP, idpHeader));

		bindingProvider.getRequestContext().put(Header.HEADER_LIST, headersList);
	}

	/**
	 * Configures the inbound interceptors for the client.
	 *
	 * @param client the client to configure
	 */
	protected void configureInInterceptor(Client client) {
		if (getConfig().isLoggingRequired()) {
			client.getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
		}
		client.getEndpoint().getInInterceptors().add(new AttachmentCachingInterceptor());
		Map<String, Object> inProps = newWSSInInterceptorConfiguration();
		WSS4JInInterceptor wssIn = new WSS4JInNonValidatingActionInterceptor(inProps);
		client.getEndpoint().getInInterceptors().add(wssIn);
		client.getEndpoint().getInInterceptors().add(new DownloadInInterceptor());
		client.getEndpoint().getInInterceptors().add(new AttachmentCleanupInterceptor());
	}

	/**
	 * Creates a new configuration for the inbound WSS4J interceptor.
	 *
	 * @return a map of properties for configuring the inbound interceptor
	 */
	protected Map<String, Object> newWSSInInterceptorConfiguration() {
		Map<String, Object> inProps = new HashMap<String, Object>();
		inProps.put(WSHandlerConstants.ACTION, getCxfOutHandlerDirectives());
		inProps.put(WSHandlerConstants.PW_CALLBACK_REF, newCallback());
		inProps.put(WSHandlerConstants.DEC_PROP_FILE, clientKeystore);
		inProps.put(WSHandlerConstants.ALLOW_RSA15_KEY_TRANSPORT_ALGORITHM, "true");
		inProps.put(WSHandlerConstants.STORE_BYTES_IN_ATTACHMENT, "false");
		inProps.put(WSHandlerConstants.EXPAND_XOP_INCLUDE, "false");
		return inProps;
	}

	/**
	 * Configures the outbound interceptors for the client.
	 *
	 * @param client the client to configure
	 */
	protected void configureOutInterceptor(Client client) {
		if (getConfig().isLoggingRequired()) {
			client.getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
		}

		Map<String, Object> outProps = newWSSOutInterceptorConfiguration();
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		wssOut.setAllowMTOM(true);
		client.getEndpoint().getOutInterceptors().add(wssOut);
	}

	/**
	 * Creates a new configuration for the outbound WSS4J interceptor.
	 *
	 * @return a map of properties for configuring the outbound interceptor
	 */
	protected Map<String, Object> newWSSOutInterceptorConfiguration() {
		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(WSHandlerConstants.MUST_UNDERSTAND, "1");
		outProps.put(WSHandlerConstants.ACTION, getCxfOutHandlerDriectives());
		outProps.put(WSHandlerConstants.USER, getConfig().getKeystoreUser());
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, newCallback());
		// configure parts of the message that needs to be signed
		outProps.put(WSHandlerConstants.SIGNATURE_PARTS, SIGNED_MESSAGE_ELEMENTS);
		// keystore file is loaded via classpath loader
		outProps.put(WSHandlerConstants.SIG_PROP_FILE, clientKeystore);
		outProps.put(WSHandlerConstants.SIG_KEY_ID, "DirectReference");
		outProps.put(WSHandlerConstants.STORE_BYTES_IN_ATTACHMENT, "0");
		outProps.put(WSHandlerConstants.EXPAND_XOP_INCLUDE, "0");
		return outProps;
	}

	/**
	 * Returns the CXF directives for the outbound handler configuration, including
	 * additional encryption directives.
	 *
	 * @return a string of directives for the outbound handler, including encryption
	 */
	protected String getCxfOutHandlerDirectives() {
		return getCxfOutHandlerDriectives() // same as for In handler
				+ " " + WSHandlerConstants.ENCRYPT; // plus decryption
	}

	/**
	 * Returns the CXF directives for the outbound handler configuration, specifying
	 * the required security actions such as username token, timestamp, and signature.
	 *
	 * @return a string of directives for the outbound handler
	 */
	protected String getCxfOutHandlerDriectives() {
		return WSHandlerConstants.USERNAME_TOKEN + " " + // Tells CXF to add user name token
				WSHandlerConstants.TIMESTAMP + " " + // and timestamp element
				WSHandlerConstants.SIGNATURE; // and finally sing the content
	}

	/**
	 * Creates a new callback for client password handling.
	 *
	 * @return a new ClientPasswordCallback instance
	 */
	protected ClientPasswordCallback newCallback() {
		return new ClientPasswordCallback(getConfig().getUserNameTokenUser(), getConfig().getUserNameTokenPassword(),
				getConfig().getKeystoreUser(), getConfig().getKeystorePassword());
	}

	/**
	 * Creates an IDP header with the specified user MUID.
	 *
	 * @param userMuid the user MUID to include in the header
	 * @return a new IdpHeader instance
	 */
	protected IdpHeader createIdpHeader(String userMuid) {
		IdpHeader idpHeader = new IdpHeader();
		idpHeader.setServiceUserMUID(userMuid);
		return idpHeader;
	}

	/**
	 * Configures SSL settings for the specified HTTP conduit.
	 *
	 * @param httpConduit the HTTP conduit to configure
	 */
	public static void configureSsl(HTTPConduit httpConduit) {
		TLSClientParameters tslClientParameters = httpConduit
				.getTlsClientParameters();
		if (tslClientParameters == null) {
			tslClientParameters = new TLSClientParameters();
		}
		tslClientParameters.setDisableCNCheck(true);

		TrustAllManager[] tam = { new TrustAllManager() };
		tslClientParameters.setTrustManagers(tam);
		tslClientParameters.setSecureSocketProtocol("TLS");
		httpConduit.setTlsClientParameters(tslClientParameters);
	}

	/**
	 * Sets the client keystore filename to the specified file in the classpath.
	 *
	 * @param fileInClassPath the filename of the keystore in the classpath
	 */
	public static void setClientKeystoreFilename(String fileInClassPath) {
		clientKeystore = fileInClassPath;
	}

	/**
	 * Trust manager that accepts all certificates.
	 */
	public static class TrustAllManager implements X509TrustManager {
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		@Override
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
			// allow local self made
		}

		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
			// allow local self made
		}
	}

}
