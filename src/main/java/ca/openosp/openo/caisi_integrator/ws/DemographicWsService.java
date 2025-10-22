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

@WebServiceClient(name = "DemographicWsService", wsdlLocation = "file:DemographicService.wsdl", targetNamespace = "http://ws.caisi_integrator.oscarehr.org/")
public class DemographicWsService extends Service
{
    public static final URL WSDL_LOCATION;
    public static final QName SERVICE;
    public static final QName DemographicWsPort;
    
    public DemographicWsService(final URL url) {
        super(url, DemographicWsService.SERVICE);
    }
    
    public DemographicWsService(final URL url, final QName qName) {
        super(url, qName);
    }
    
    public DemographicWsService() {
        super(DemographicWsService.WSDL_LOCATION, DemographicWsService.SERVICE);
    }
    
    public DemographicWsService(final WebServiceFeature... array) {
        super(DemographicWsService.WSDL_LOCATION, DemographicWsService.SERVICE, array);
    }
    
    public DemographicWsService(final URL url, final WebServiceFeature... array) {
        super(url, DemographicWsService.SERVICE, array);
    }
    
    public DemographicWsService(final URL url, final QName qName, final WebServiceFeature... array) {
        super(url, qName, array);
    }
    
    @WebEndpoint(name = "DemographicWsPort")
    public DemographicWs getDemographicWsPort() {
        return (DemographicWs)super.getPort(DemographicWsService.DemographicWsPort, (Class)DemographicWs.class);
    }
    
    @WebEndpoint(name = "DemographicWsPort")
    public DemographicWs getDemographicWsPort(final WebServiceFeature... array) {
        return (DemographicWs)super.getPort(DemographicWsService.DemographicWsPort, (Class)DemographicWs.class, array);
    }
    
    static {
        SERVICE = new QName("http://ws.caisi_integrator.oscarehr.org/", "DemographicWsService");
        DemographicWsPort = new QName("http://ws.caisi_integrator.oscarehr.org/", "DemographicWsPort");
        URL wsdl_LOCATION = null;
        try {
            wsdl_LOCATION = new URL("file:DemographicService.wsdl");
        }
        catch (final MalformedURLException ex) {
            Logger.getLogger(DemographicWsService.class.getName()).log(Level.INFO, "Can not initialize the default wsdl from {0}", "file:DemographicService.wsdl");
        }
        WSDL_LOCATION = wsdl_LOCATION;
    }
}
