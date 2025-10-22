package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.SchemaType;
import ca.openosp.openo.ar2005.EthnicValueType;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;

public class EthnicValueTypeImpl extends JavaStringEnumerationHolderEx implements EthnicValueType
{
    private static final long serialVersionUID = 1L;
    
    public EthnicValueTypeImpl(final SchemaType sType) {
        super(sType, false);
    }
    
    protected EthnicValueTypeImpl(final SchemaType sType, final boolean b) {
        super(sType, b);
    }
}
