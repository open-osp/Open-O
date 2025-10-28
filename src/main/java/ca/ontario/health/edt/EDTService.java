package ca.ontario.health.edt;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.WebEndpoint;
import javax.xml.namespace.QName;
import java.net.URL;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.Service;

@WebServiceClient(name = "EDTService", wsdlLocation = "file:/home/oscara/mcedt/edt-stubs/src/main/resources/from_ohip_web_site/EDTService.wsdl", targetNamespace = "http://edt.health.ontario.ca/")
public class EDTService extends Service
{
    public static final URL WSDL_LOCATION;
    public static final QName SERVICE;
    public static final QName EDTPort;
    
    public EDTService(final URL wsdlLocation) {
        super(wsdlLocation, EDTService.SERVICE);
    }
    
    public EDTService(final URL wsdlLocation, final QName serviceName) {
        super(wsdlLocation, serviceName);
    }
    
    public EDTService() {
        super(EDTService.WSDL_LOCATION, EDTService.SERVICE);
    }
    
    @WebEndpoint(name = "EDTPort")
    public EDTDelegate getEDTPort() {
        return (EDTDelegate)super.getPort(EDTService.EDTPort, (Class)EDTDelegate.class);
    }
    
    @WebEndpoint(name = "EDTPort")
    public EDTDelegate getEDTPort(final WebServiceFeature... features) {
        return (EDTDelegate)super.getPort(EDTService.EDTPort, (Class)EDTDelegate.class, features);
    }
    
    static {
        SERVICE = new QName("http://edt.health.ontario.ca/", "EDTService");
        EDTPort = new QName("http://edt.health.ontario.ca/", "EDTPort");
        URL url = null;
        try {
            url = new URL("file:/home/oscara/mcedt/edt-stubs/src/main/resources/from_ohip_web_site/EDTService.wsdl");
        }
        catch (final MalformedURLException e) {
            Logger.getLogger(EDTService.class.getName()).log(Level.INFO, "Can not initialize the default wsdl from {0}", "file:/home/oscara/mcedt/edt-stubs/src/main/resources/from_ohip_web_site/EDTService.wsdl");
        }
        WSDL_LOCATION = url;
    }
}
