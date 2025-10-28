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

@WebServiceClient(name = "HnrWsService", wsdlLocation = "file:HnrService.wsdl", targetNamespace = "http://ws.caisi_integrator.oscarehr.org/")
public class HnrWsService extends Service
{
    public static final URL WSDL_LOCATION;
    public static final QName SERVICE;
    public static final QName HnrWsPort;
    
    public HnrWsService(final URL url) {
        super(url, HnrWsService.SERVICE);
    }
    
    public HnrWsService(final URL url, final QName qName) {
        super(url, qName);
    }
    
    public HnrWsService() {
        super(HnrWsService.WSDL_LOCATION, HnrWsService.SERVICE);
    }
    
    public HnrWsService(final WebServiceFeature... array) {
        super(HnrWsService.WSDL_LOCATION, HnrWsService.SERVICE, array);
    }
    
    public HnrWsService(final URL url, final WebServiceFeature... array) {
        super(url, HnrWsService.SERVICE, array);
    }
    
    public HnrWsService(final URL url, final QName qName, final WebServiceFeature... array) {
        super(url, qName, array);
    }
    
    @WebEndpoint(name = "HnrWsPort")
    public HnrWs getHnrWsPort() {
        return (HnrWs)super.getPort(HnrWsService.HnrWsPort, (Class)HnrWs.class);
    }
    
    @WebEndpoint(name = "HnrWsPort")
    public HnrWs getHnrWsPort(final WebServiceFeature... array) {
        return (HnrWs)super.getPort(HnrWsService.HnrWsPort, (Class)HnrWs.class, array);
    }
    
    static {
        SERVICE = new QName("http://ws.caisi_integrator.oscarehr.org/", "HnrWsService");
        HnrWsPort = new QName("http://ws.caisi_integrator.oscarehr.org/", "HnrWsPort");
        URL wsdl_LOCATION = null;
        try {
            wsdl_LOCATION = new URL("file:HnrService.wsdl");
        }
        catch (final MalformedURLException ex) {
            Logger.getLogger(HnrWsService.class.getName()).log(Level.INFO, "Can not initialize the default wsdl from {0}", "file:HnrService.wsdl");
        }
        WSDL_LOCATION = wsdl_LOCATION;
    }
}
