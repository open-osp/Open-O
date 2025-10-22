package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import java.util.List;
import java.util.ArrayList;
import ca.openosp.openo.ar2005.ObstetricalHistoryItemList;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.ObstetricalHistory;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class ObstetricalHistoryImpl extends XmlComplexContentImpl implements ObstetricalHistory
{
    private static final long serialVersionUID = 1L;
    private static final QName OBSLIST$0;
    
    public ObstetricalHistoryImpl(final SchemaType sType) {
        super(sType);
    }
    
    public ObstetricalHistoryItemList[] getObsListArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(ObstetricalHistoryImpl.OBSLIST$0, targetList);
            final ObstetricalHistoryItemList[] result = new ObstetricalHistoryItemList[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public ObstetricalHistoryItemList getObsListArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ObstetricalHistoryItemList target = null;
            target = (ObstetricalHistoryItemList)this.get_store().find_element_user(ObstetricalHistoryImpl.OBSLIST$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfObsListArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(ObstetricalHistoryImpl.OBSLIST$0);
        }
    }
    
    public void setObsListArray(final ObstetricalHistoryItemList[] obsListArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])obsListArray, ObstetricalHistoryImpl.OBSLIST$0);
        }
    }
    
    public void setObsListArray(final int i, final ObstetricalHistoryItemList obsList) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ObstetricalHistoryItemList target = null;
            target = (ObstetricalHistoryItemList)this.get_store().find_element_user(ObstetricalHistoryImpl.OBSLIST$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)obsList);
        }
    }
    
    public ObstetricalHistoryItemList insertNewObsList(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ObstetricalHistoryItemList target = null;
            target = (ObstetricalHistoryItemList)this.get_store().insert_element_user(ObstetricalHistoryImpl.OBSLIST$0, i);
            return target;
        }
    }
    
    public ObstetricalHistoryItemList addNewObsList() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ObstetricalHistoryItemList target = null;
            target = (ObstetricalHistoryItemList)this.get_store().add_element_user(ObstetricalHistoryImpl.OBSLIST$0);
            return target;
        }
    }
    
    public void removeObsList(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(ObstetricalHistoryImpl.OBSLIST$0, i);
        }
    }
    
    static {
        OBSLIST$0 = new QName("http://www.oscarmcmaster.org/AR2005", "obsList");
    }
}
