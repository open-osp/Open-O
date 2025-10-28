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

@WebServiceClient(name = "ProgramWsService", wsdlLocation = "file:ProgramService.wsdl", targetNamespace = "http://ws.caisi_integrator.oscarehr.org/")
public class ProgramWsService extends Service
{
    public static final URL WSDL_LOCATION;
    public static final QName SERVICE;
    public static final QName ProgramWsPort;
    
    public ProgramWsService(final URL url) {
        super(url, ProgramWsService.SERVICE);
    }
    
    public ProgramWsService(final URL url, final QName qName) {
        super(url, qName);
    }
    
    public ProgramWsService() {
        super(ProgramWsService.WSDL_LOCATION, ProgramWsService.SERVICE);
    }
    
    public ProgramWsService(final WebServiceFeature... array) {
        super(ProgramWsService.WSDL_LOCATION, ProgramWsService.SERVICE, array);
    }
    
    public ProgramWsService(final URL url, final WebServiceFeature... array) {
        super(url, ProgramWsService.SERVICE, array);
    }
    
    public ProgramWsService(final URL url, final QName qName, final WebServiceFeature... array) {
        super(url, qName, array);
    }
    
    @WebEndpoint(name = "ProgramWsPort")
    public ProgramWs getProgramWsPort() {
        return (ProgramWs)super.getPort(ProgramWsService.ProgramWsPort, (Class)ProgramWs.class);
    }
    
    @WebEndpoint(name = "ProgramWsPort")
    public ProgramWs getProgramWsPort(final WebServiceFeature... array) {
        return (ProgramWs)super.getPort(ProgramWsService.ProgramWsPort, (Class)ProgramWs.class, array);
    }
    
    static {
        SERVICE = new QName("http://ws.caisi_integrator.oscarehr.org/", "ProgramWsService");
        ProgramWsPort = new QName("http://ws.caisi_integrator.oscarehr.org/", "ProgramWsPort");
        URL wsdl_LOCATION = null;
        try {
            wsdl_LOCATION = new URL("file:ProgramService.wsdl");
        }
        catch (final MalformedURLException ex) {
            Logger.getLogger(ProgramWsService.class.getName()).log(Level.INFO, "Can not initialize the default wsdl from {0}", "file:ProgramService.wsdl");
        }
        WSDL_LOCATION = wsdl_LOCATION;
    }
}
