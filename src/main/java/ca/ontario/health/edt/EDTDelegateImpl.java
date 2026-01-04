package ca.ontario.health.edt;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;
import javax.jws.WebService;

@WebService(serviceName = "EDTService", portName = "EDTPort", targetNamespace = "http://edt.health.ontario.ca/", wsdlLocation = "file:/home/oscara/mcedt/edt-stubs/src/main/resources/from_ohip_web_site/EDTService.wsdl", endpointInterface = "ca.ontario.health.edt.EDTDelegate")
public class EDTDelegateImpl implements EDTDelegate
{
    private static final Logger LOG;
    
    @Override
    public ResourceResult submit(final List<BigInteger> resourceIDs) throws Faultexception {
        EDTDelegateImpl.LOG.info("Executing operation submit");
        System.out.println(resourceIDs);
        try {
            final ResourceResult _return = null;
            return _return;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public ResourceResult upload(final List<UploadData> upload) throws Faultexception {
        EDTDelegateImpl.LOG.info("Executing operation upload");
        System.out.println(upload);
        try {
            final ResourceResult _return = null;
            return _return;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public Detail info(final List<BigInteger> resourceIDs) throws Faultexception {
        EDTDelegateImpl.LOG.info("Executing operation info");
        System.out.println(resourceIDs);
        try {
            final Detail _return = null;
            return _return;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public ResourceResult delete(final List<BigInteger> resourceIDs) throws Faultexception {
        EDTDelegateImpl.LOG.info("Executing operation delete");
        System.out.println(resourceIDs);
        try {
            final ResourceResult _return = null;
            return _return;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public DownloadResult download(final List<BigInteger> resourceIDs) throws Faultexception {
        EDTDelegateImpl.LOG.info("Executing operation download");
        System.out.println(resourceIDs);
        try {
            final DownloadResult _return = null;
            return _return;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public ResourceResult update(final List<UpdateRequest> updates) throws Faultexception {
        EDTDelegateImpl.LOG.info("Executing operation update");
        System.out.println(updates);
        try {
            final ResourceResult _return = null;
            return _return;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public TypeListResult getTypeList() throws Faultexception {
        EDTDelegateImpl.LOG.info("Executing operation getTypeList");
        try {
            final TypeListResult _return = null;
            return _return;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public Detail list(final String resourceType, final ResourceStatus status, final BigInteger pageNo) throws Faultexception {
        EDTDelegateImpl.LOG.info("Executing operation list");
        System.out.println(resourceType);
        System.out.println(status);
        System.out.println(pageNo);
        try {
            final Detail _return = null;
            return _return;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
    static {
        LOG = Logger.getLogger(EDTDelegateImpl.class.getName());
    }
}
