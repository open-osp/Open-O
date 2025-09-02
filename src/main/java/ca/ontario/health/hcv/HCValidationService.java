package ca.ontario.health.hcv;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.WebEndpoint;
import javax.xml.namespace.QName;
import java.net.URL;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.Service;

@WebServiceClient(name = "HCValidationService", wsdlLocation = "file:/home/oscara/mcedt/hcv-stubs/src/main/resources/from_ohip_web_site/HCValidationService.wsdl", targetNamespace = "http://hcv.health.ontario.ca/")
public class HCValidationService extends Service
{
    public static final URL WSDL_LOCATION;
    public static final QName SERVICE;
    public static final QName HCValidationPort1;
    public static final QName HCValidationPort0;
    
    public HCValidationService(final URL wsdlLocation) {
        super(wsdlLocation, HCValidationService.SERVICE);
    }
    
    public HCValidationService(final URL wsdlLocation, final QName serviceName) {
        super(wsdlLocation, serviceName);
    }
    
    public HCValidationService() {
        super(HCValidationService.WSDL_LOCATION, HCValidationService.SERVICE);
    }
    
    @WebEndpoint(name = "HCValidationPort.1")
    public HCValidation getHCValidationPort1() {
        return (HCValidation)super.getPort(HCValidationService.HCValidationPort1, (Class)HCValidation.class);
    }
    
    @WebEndpoint(name = "HCValidationPort.1")
    public HCValidation getHCValidationPort1(final WebServiceFeature... features) {
        return (HCValidation)super.getPort(HCValidationService.HCValidationPort1, (Class)HCValidation.class, features);
    }
    
    @WebEndpoint(name = "HCValidationPort.0")
    public HCValidation getHCValidationPort0() {
        return (HCValidation)super.getPort(HCValidationService.HCValidationPort0, (Class)HCValidation.class);
    }
    
    @WebEndpoint(name = "HCValidationPort.0")
    public HCValidation getHCValidationPort0(final WebServiceFeature... features) {
        return (HCValidation)super.getPort(HCValidationService.HCValidationPort0, (Class)HCValidation.class, features);
    }
    
    static {
        SERVICE = new QName("http://hcv.health.ontario.ca/", "HCValidationService");
        HCValidationPort1 = new QName("http://hcv.health.ontario.ca/", "HCValidationPort.1");
        HCValidationPort0 = new QName("http://hcv.health.ontario.ca/", "HCValidationPort.0");
        URL url = null;
        try {
            url = new URL("file:/home/oscara/mcedt/hcv-stubs/src/main/resources/from_ohip_web_site/HCValidationService.wsdl");
        }
        catch (final MalformedURLException e) {
            Logger.getLogger(HCValidationService.class.getName()).log(Level.INFO, "Can not initialize the default wsdl from {0}", "file:/home/oscara/mcedt/hcv-stubs/src/main/resources/from_ohip_web_site/HCValidationService.wsdl");
        }
        WSDL_LOCATION = url;
    }
}
