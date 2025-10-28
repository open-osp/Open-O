package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlBoolean;
import ca.openosp.openo.ar2005.CustomLab;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.PrenatalGeneticScreeningType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class PrenatalGeneticScreeningTypeImpl extends XmlComplexContentImpl implements PrenatalGeneticScreeningType
{
    private static final long serialVersionUID = 1L;
    private static final QName MSSIPSFTS$0;
    private static final QName EDBCVS$2;
    private static final QName MSAFP$4;
    private static final QName CUSTOMLAB1$6;
    private static final QName DECLINED$8;
    
    public PrenatalGeneticScreeningTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String getMSSIPSFTS() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.MSSIPSFTS$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetMSSIPSFTS() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.MSSIPSFTS$0, 0);
            return target;
        }
    }
    
    public void setMSSIPSFTS(final String mssipsfts) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.MSSIPSFTS$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.MSSIPSFTS$0);
            }
            target.setStringValue(mssipsfts);
        }
    }
    
    public void xsetMSSIPSFTS(final XmlString mssipsfts) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.MSSIPSFTS$0, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.MSSIPSFTS$0);
            }
            target.set((XmlObject)mssipsfts);
        }
    }
    
    public String getEDBCVS() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.EDBCVS$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetEDBCVS() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.EDBCVS$2, 0);
            return target;
        }
    }
    
    public void setEDBCVS(final String edbcvs) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.EDBCVS$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.EDBCVS$2);
            }
            target.setStringValue(edbcvs);
        }
    }
    
    public void xsetEDBCVS(final XmlString edbcvs) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.EDBCVS$2, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.EDBCVS$2);
            }
            target.set((XmlObject)edbcvs);
        }
    }
    
    public String getMSAFP() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.MSAFP$4, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetMSAFP() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.MSAFP$4, 0);
            return target;
        }
    }
    
    public void setMSAFP(final String msafp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.MSAFP$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.MSAFP$4);
            }
            target.setStringValue(msafp);
        }
    }
    
    public void xsetMSAFP(final XmlString msafp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.MSAFP$4, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.MSAFP$4);
            }
            target.set((XmlObject)msafp);
        }
    }
    
    public CustomLab getCustomLab1() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.CUSTOMLAB1$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCustomLab1(final CustomLab customLab1) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.CUSTOMLAB1$6, 0);
            if (target == null) {
                target = (CustomLab)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.CUSTOMLAB1$6);
            }
            target.set((XmlObject)customLab1);
        }
    }
    
    public CustomLab addNewCustomLab1() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.CUSTOMLAB1$6);
            return target;
        }
    }
    
    public boolean getDeclined() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.DECLINED$8, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetDeclined() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.DECLINED$8, 0);
            return target;
        }
    }
    
    public void setDeclined(final boolean declined) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.DECLINED$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.DECLINED$8);
            }
            target.setBooleanValue(declined);
        }
    }
    
    public void xsetDeclined(final XmlBoolean declined) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(PrenatalGeneticScreeningTypeImpl.DECLINED$8, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(PrenatalGeneticScreeningTypeImpl.DECLINED$8);
            }
            target.set((XmlObject)declined);
        }
    }
    
    static {
        MSSIPSFTS$0 = new QName("http://www.oscarmcmaster.org/AR2005", "MSS_IPS_FTS");
        EDBCVS$2 = new QName("http://www.oscarmcmaster.org/AR2005", "EDB_CVS");
        MSAFP$4 = new QName("http://www.oscarmcmaster.org/AR2005", "MSAFP");
        CUSTOMLAB1$6 = new QName("http://www.oscarmcmaster.org/AR2005", "customLab1");
        DECLINED$8 = new QName("http://www.oscarmcmaster.org/AR2005", "declined");
    }
}
