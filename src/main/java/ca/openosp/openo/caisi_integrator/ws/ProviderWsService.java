package ca.openosp.openo.caisi_integrator.ws;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceFeature;
import javax.xml.namespace.QName;
import java.net.URL;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.Service;

@WebServiceClient(name = "ProviderWsService", wsdlLocation = "file:ProviderService.wsdl", targetNamespace = "http://ws.caisi_integrator.oscarehr.org/")
public class ProviderWsService extends Service
{
    public static final URL WSDL_LOCATION;
    public static final QName SERVICE;
    public static final QName ProviderWsPort;
    
    public ProviderWsService(final URL url) {
        super(url, ProviderWsService.SERVICE);
    }
    
    public ProviderWsService(final URL url, final QName qName) {
        super(url, qName);
    }
    
    public ProviderWsService() {
        super(ProviderWsService.WSDL_LOCATION, ProviderWsService.SERVICE);
    }
    
    public ProviderWsService(final WebServiceFeature... array) {
        super(ProviderWsService.WSDL_LOCATION, ProviderWsService.SERVICE, array);
    }
    
    public ProviderWsService(final URL url, final WebServiceFeature... array) {
        super(url, ProviderWsService.SERVICE, array);
    }
    
    public ProviderWsService(final URL url, final QName qName, final WebServiceFeature... array) {
        super(url, qName, array);
    }
    
    @WebEndpoint(name = "ProviderWsPort")
    public ProviderWs getProviderWsPort() {
        return (ProviderWs)super.getPort(ProviderWsService.ProviderWsPort, (Class)ProviderWs.class);
    }
    
    @WebEndpoint(name = "ProviderWsPort")
    public ProviderWs getProviderWsPort(final WebServiceFeature... array) {
        return (ProviderWs)super.getPort(ProviderWsService.ProviderWsPort, (Class)ProviderWs.class, array);
    }
    
    static {
        SERVICE = new QName("http://ws.caisi_integrator.oscarehr.org/", "ProviderWsService");
        ProviderWsPort = new QName("http://ws.caisi_integrator.oscarehr.org/", "ProviderWsPort");
        URL wsdl_LOCATION = null;
        try {
            wsdl_LOCATION = new URL("file:ProviderService.wsdl");
        }
        catch (final MalformedURLException ex) {
            Logger.getLogger(ProviderWsService.class.getName()).log(Level.INFO, "Can not initialize the default wsdl from {0}", "file:ProviderService.wsdl");
        }
        WSDL_LOCATION = wsdl_LOCATION;
    }
}
