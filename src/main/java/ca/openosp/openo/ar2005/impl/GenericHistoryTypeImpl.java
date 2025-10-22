package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.YesNoNullType;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.GenericHistoryType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class GenericHistoryTypeImpl extends XmlComplexContentImpl implements GenericHistoryType
{
    private static final long serialVersionUID = 1L;
    private static final QName ATRISK$0;
    private static final QName DEVELOPMENTALDELAY$2;
    private static final QName CONGENITALANOMOLIES$4;
    private static final QName CHROMOSOMALDISORDERS$6;
    private static final QName GENETICDISORDERS$8;
    
    public GenericHistoryTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public YesNoNullType getAtRisk() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.ATRISK$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setAtRisk(final YesNoNullType atRisk) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.ATRISK$0, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.ATRISK$0);
            }
            target.set((XmlObject)atRisk);
        }
    }
    
    public YesNoNullType addNewAtRisk() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.ATRISK$0);
            return target;
        }
    }
    
    public YesNoNullType getDevelopmentalDelay() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.DEVELOPMENTALDELAY$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setDevelopmentalDelay(final YesNoNullType developmentalDelay) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.DEVELOPMENTALDELAY$2, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.DEVELOPMENTALDELAY$2);
            }
            target.set((XmlObject)developmentalDelay);
        }
    }
    
    public YesNoNullType addNewDevelopmentalDelay() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.DEVELOPMENTALDELAY$2);
            return target;
        }
    }
    
    public YesNoNullType getCongenitalAnomolies() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.CONGENITALANOMOLIES$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCongenitalAnomolies(final YesNoNullType congenitalAnomolies) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.CONGENITALANOMOLIES$4, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.CONGENITALANOMOLIES$4);
            }
            target.set((XmlObject)congenitalAnomolies);
        }
    }
    
    public YesNoNullType addNewCongenitalAnomolies() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.CONGENITALANOMOLIES$4);
            return target;
        }
    }
    
    public YesNoNullType getChromosomalDisorders() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.CHROMOSOMALDISORDERS$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setChromosomalDisorders(final YesNoNullType chromosomalDisorders) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.CHROMOSOMALDISORDERS$6, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.CHROMOSOMALDISORDERS$6);
            }
            target.set((XmlObject)chromosomalDisorders);
        }
    }
    
    public YesNoNullType addNewChromosomalDisorders() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.CHROMOSOMALDISORDERS$6);
            return target;
        }
    }
    
    public YesNoNullType getGeneticDisorders() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.GENETICDISORDERS$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setGeneticDisorders(final YesNoNullType geneticDisorders) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(GenericHistoryTypeImpl.GENETICDISORDERS$8, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.GENETICDISORDERS$8);
            }
            target.set((XmlObject)geneticDisorders);
        }
    }
    
    public YesNoNullType addNewGeneticDisorders() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(GenericHistoryTypeImpl.GENETICDISORDERS$8);
            return target;
        }
    }
    
    static {
        ATRISK$0 = new QName("http://www.oscarmcmaster.org/AR2005", "atRisk");
        DEVELOPMENTALDELAY$2 = new QName("http://www.oscarmcmaster.org/AR2005", "developmentalDelay");
        CONGENITALANOMOLIES$4 = new QName("http://www.oscarmcmaster.org/AR2005", "congenitalAnomolies");
        CHROMOSOMALDISORDERS$6 = new QName("http://www.oscarmcmaster.org/AR2005", "chromosomalDisorders");
        GENETICDISORDERS$8 = new QName("http://www.oscarmcmaster.org/AR2005", "geneticDisorders");
    }
}
