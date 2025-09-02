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

@WebServiceClient(name = "ReferralWsService", wsdlLocation = "file:ReferralService.wsdl", targetNamespace = "http://ws.caisi_integrator.oscarehr.org/")
public class ReferralWsService extends Service
{
    public static final URL WSDL_LOCATION;
    public static final QName SERVICE;
    public static final QName ReferralWsPort;
    
    public ReferralWsService(final URL url) {
        super(url, ReferralWsService.SERVICE);
    }
    
    public ReferralWsService(final URL url, final QName qName) {
        super(url, qName);
    }
    
    public ReferralWsService() {
        super(ReferralWsService.WSDL_LOCATION, ReferralWsService.SERVICE);
    }
    
    public ReferralWsService(final WebServiceFeature... array) {
        super(ReferralWsService.WSDL_LOCATION, ReferralWsService.SERVICE, array);
    }
    
    public ReferralWsService(final URL url, final WebServiceFeature... array) {
        super(url, ReferralWsService.SERVICE, array);
    }
    
    public ReferralWsService(final URL url, final QName qName, final WebServiceFeature... array) {
        super(url, qName, array);
    }
    
    @WebEndpoint(name = "ReferralWsPort")
    public ReferralWs getReferralWsPort() {
        return (ReferralWs)super.getPort(ReferralWsService.ReferralWsPort, (Class)ReferralWs.class);
    }
    
    @WebEndpoint(name = "ReferralWsPort")
    public ReferralWs getReferralWsPort(final WebServiceFeature... array) {
        return (ReferralWs)super.getPort(ReferralWsService.ReferralWsPort, (Class)ReferralWs.class, array);
    }
    
    static {
        SERVICE = new QName("http://ws.caisi_integrator.oscarehr.org/", "ReferralWsService");
        ReferralWsPort = new QName("http://ws.caisi_integrator.oscarehr.org/", "ReferralWsPort");
        URL wsdl_LOCATION = null;
        try {
            wsdl_LOCATION = new URL("file:ReferralService.wsdl");
        }
        catch (final MalformedURLException ex) {
            Logger.getLogger(ReferralWsService.class.getName()).log(Level.INFO, "Can not initialize the default wsdl from {0}", "file:ReferralService.wsdl");
        }
        WSDL_LOCATION = wsdl_LOCATION;
    }
}
