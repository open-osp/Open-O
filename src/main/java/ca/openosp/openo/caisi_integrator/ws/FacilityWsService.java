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

@WebServiceClient(name = "FacilityWsService", wsdlLocation = "file:FacilityService.wsdl", targetNamespace = "http://ws.caisi_integrator.oscarehr.org/")
public class FacilityWsService extends Service
{
    public static final URL WSDL_LOCATION;
    public static final QName SERVICE;
    public static final QName FacilityWsPort;
    
    public FacilityWsService(final URL url) {
        super(url, FacilityWsService.SERVICE);
    }
    
    public FacilityWsService(final URL url, final QName qName) {
        super(url, qName);
    }
    
    public FacilityWsService() {
        super(FacilityWsService.WSDL_LOCATION, FacilityWsService.SERVICE);
    }
    
    public FacilityWsService(final WebServiceFeature... array) {
        super(FacilityWsService.WSDL_LOCATION, FacilityWsService.SERVICE, array);
    }
    
    public FacilityWsService(final URL url, final WebServiceFeature... array) {
        super(url, FacilityWsService.SERVICE, array);
    }
    
    public FacilityWsService(final URL url, final QName qName, final WebServiceFeature... array) {
        super(url, qName, array);
    }
    
    @WebEndpoint(name = "FacilityWsPort")
    public FacilityWs getFacilityWsPort() {
        return (FacilityWs)super.getPort(FacilityWsService.FacilityWsPort, (Class)FacilityWs.class);
    }
    
    @WebEndpoint(name = "FacilityWsPort")
    public FacilityWs getFacilityWsPort(final WebServiceFeature... array) {
        return (FacilityWs)super.getPort(FacilityWsService.FacilityWsPort, (Class)FacilityWs.class, array);
    }
    
    static {
        SERVICE = new QName("http://ws.caisi_integrator.oscarehr.org/", "FacilityWsService");
        FacilityWsPort = new QName("http://ws.caisi_integrator.oscarehr.org/", "FacilityWsPort");
        URL wsdl_LOCATION = null;
        try {
            wsdl_LOCATION = new URL("file:FacilityService.wsdl");
        }
        catch (final MalformedURLException ex) {
            Logger.getLogger(FacilityWsService.class.getName()).log(Level.INFO, "Can not initialize the default wsdl from {0}", "file:FacilityService.wsdl");
        }
        WSDL_LOCATION = wsdl_LOCATION;
    }
}
