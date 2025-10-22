package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlInt;
import ca.openosp.openo.ar2005.DatingMethods;
import org.apache.xmlbeans.XmlString;
import ca.openosp.openo.ar2005.YesNoNullType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.SimpleValue;
import java.util.Calendar;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.PregnancyHistory;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class PregnancyHistoryImpl extends XmlComplexContentImpl implements PregnancyHistory
{
    private static final long serialVersionUID = 1L;
    private static final QName LMP$0;
    private static final QName LMPCERTAIN$2;
    private static final QName MENCYCLE$4;
    private static final QName MENCYCLEREGULAR$6;
    private static final QName CONTRACEPTIVETYPE$8;
    private static final QName CONTRACEPTIVELASTUSED$10;
    private static final QName MENSTRUALEDB$12;
    private static final QName FINALEDB$14;
    private static final QName DATINGMETHODS$16;
    private static final QName GRAVIDA$18;
    private static final QName TERM$20;
    private static final QName PREMATURE$22;
    private static final QName ABORTUSES$24;
    private static final QName LIVING$26;
    
    public PregnancyHistoryImpl(final SchemaType sType) {
        super(sType);
    }
    
    public Calendar getLMP() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.LMP$0, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetLMP() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.LMP$0, 0);
            return target;
        }
    }
    
    public boolean isNilLMP() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.LMP$0, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setLMP(final Calendar lmp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.LMP$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.LMP$0);
            }
            target.setCalendarValue(lmp);
        }
    }
    
    public void xsetLMP(final XmlDate lmp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.LMP$0, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(PregnancyHistoryImpl.LMP$0);
            }
            target.set((XmlObject)lmp);
        }
    }
    
    public void setNilLMP() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.LMP$0, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(PregnancyHistoryImpl.LMP$0);
            }
            target.setNil();
        }
    }
    
    public YesNoNullType getLMPCertain() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PregnancyHistoryImpl.LMPCERTAIN$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setLMPCertain(final YesNoNullType lmpCertain) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PregnancyHistoryImpl.LMPCERTAIN$2, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(PregnancyHistoryImpl.LMPCERTAIN$2);
            }
            target.set((XmlObject)lmpCertain);
        }
    }
    
    public YesNoNullType addNewLMPCertain() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(PregnancyHistoryImpl.LMPCERTAIN$2);
            return target;
        }
    }
    
    public String getMenCycle() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.MENCYCLE$4, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetMenCycle() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PregnancyHistoryImpl.MENCYCLE$4, 0);
            return target;
        }
    }
    
    public void setMenCycle(final String menCycle) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.MENCYCLE$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.MENCYCLE$4);
            }
            target.setStringValue(menCycle);
        }
    }
    
    public void xsetMenCycle(final XmlString menCycle) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PregnancyHistoryImpl.MENCYCLE$4, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PregnancyHistoryImpl.MENCYCLE$4);
            }
            target.set((XmlObject)menCycle);
        }
    }
    
    public YesNoNullType getMenCycleRegular() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PregnancyHistoryImpl.MENCYCLEREGULAR$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setMenCycleRegular(final YesNoNullType menCycleRegular) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PregnancyHistoryImpl.MENCYCLEREGULAR$6, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(PregnancyHistoryImpl.MENCYCLEREGULAR$6);
            }
            target.set((XmlObject)menCycleRegular);
        }
    }
    
    public YesNoNullType addNewMenCycleRegular() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(PregnancyHistoryImpl.MENCYCLEREGULAR$6);
            return target;
        }
    }
    
    public String getContraceptiveType() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVETYPE$8, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetContraceptiveType() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVETYPE$8, 0);
            return target;
        }
    }
    
    public boolean isSetContraceptiveType() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(PregnancyHistoryImpl.CONTRACEPTIVETYPE$8) != 0;
        }
    }
    
    public void setContraceptiveType(final String contraceptiveType) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVETYPE$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.CONTRACEPTIVETYPE$8);
            }
            target.setStringValue(contraceptiveType);
        }
    }
    
    public void xsetContraceptiveType(final XmlString contraceptiveType) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVETYPE$8, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PregnancyHistoryImpl.CONTRACEPTIVETYPE$8);
            }
            target.set((XmlObject)contraceptiveType);
        }
    }
    
    public void unsetContraceptiveType() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(PregnancyHistoryImpl.CONTRACEPTIVETYPE$8, 0);
        }
    }
    
    public Calendar getContraceptiveLastUsed() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVELASTUSED$10, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetContraceptiveLastUsed() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVELASTUSED$10, 0);
            return target;
        }
    }
    
    public boolean isNilContraceptiveLastUsed() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVELASTUSED$10, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setContraceptiveLastUsed(final Calendar contraceptiveLastUsed) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVELASTUSED$10, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.CONTRACEPTIVELASTUSED$10);
            }
            target.setCalendarValue(contraceptiveLastUsed);
        }
    }
    
    public void xsetContraceptiveLastUsed(final XmlDate contraceptiveLastUsed) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVELASTUSED$10, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(PregnancyHistoryImpl.CONTRACEPTIVELASTUSED$10);
            }
            target.set((XmlObject)contraceptiveLastUsed);
        }
    }
    
    public void setNilContraceptiveLastUsed() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.CONTRACEPTIVELASTUSED$10, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(PregnancyHistoryImpl.CONTRACEPTIVELASTUSED$10);
            }
            target.setNil();
        }
    }
    
    public Calendar getMenstrualEDB() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.MENSTRUALEDB$12, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetMenstrualEDB() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.MENSTRUALEDB$12, 0);
            return target;
        }
    }
    
    public boolean isNilMenstrualEDB() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.MENSTRUALEDB$12, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setMenstrualEDB(final Calendar menstrualEDB) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.MENSTRUALEDB$12, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.MENSTRUALEDB$12);
            }
            target.setCalendarValue(menstrualEDB);
        }
    }
    
    public void xsetMenstrualEDB(final XmlDate menstrualEDB) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.MENSTRUALEDB$12, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(PregnancyHistoryImpl.MENSTRUALEDB$12);
            }
            target.set((XmlObject)menstrualEDB);
        }
    }
    
    public void setNilMenstrualEDB() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.MENSTRUALEDB$12, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(PregnancyHistoryImpl.MENSTRUALEDB$12);
            }
            target.setNil();
        }
    }
    
    public Calendar getFinalEDB() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.FINALEDB$14, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetFinalEDB() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.FINALEDB$14, 0);
            return target;
        }
    }
    
    public void setFinalEDB(final Calendar finalEDB) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.FINALEDB$14, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.FINALEDB$14);
            }
            target.setCalendarValue(finalEDB);
        }
    }
    
    public void xsetFinalEDB(final XmlDate finalEDB) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PregnancyHistoryImpl.FINALEDB$14, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(PregnancyHistoryImpl.FINALEDB$14);
            }
            target.set((XmlObject)finalEDB);
        }
    }
    
    public DatingMethods getDatingMethods() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            DatingMethods target = null;
            target = (DatingMethods)this.get_store().find_element_user(PregnancyHistoryImpl.DATINGMETHODS$16, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setDatingMethods(final DatingMethods datingMethods) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            DatingMethods target = null;
            target = (DatingMethods)this.get_store().find_element_user(PregnancyHistoryImpl.DATINGMETHODS$16, 0);
            if (target == null) {
                target = (DatingMethods)this.get_store().add_element_user(PregnancyHistoryImpl.DATINGMETHODS$16);
            }
            target.set((XmlObject)datingMethods);
        }
    }
    
    public DatingMethods addNewDatingMethods() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            DatingMethods target = null;
            target = (DatingMethods)this.get_store().add_element_user(PregnancyHistoryImpl.DATINGMETHODS$16);
            return target;
        }
    }
    
    public int getGravida() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.GRAVIDA$18, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetGravida() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.GRAVIDA$18, 0);
            return target;
        }
    }
    
    public void setGravida(final int gravida) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.GRAVIDA$18, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.GRAVIDA$18);
            }
            target.setIntValue(gravida);
        }
    }
    
    public void xsetGravida(final XmlInt gravida) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.GRAVIDA$18, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(PregnancyHistoryImpl.GRAVIDA$18);
            }
            target.set((XmlObject)gravida);
        }
    }
    
    public int getTerm() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.TERM$20, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetTerm() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.TERM$20, 0);
            return target;
        }
    }
    
    public void setTerm(final int term) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.TERM$20, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.TERM$20);
            }
            target.setIntValue(term);
        }
    }
    
    public void xsetTerm(final XmlInt term) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.TERM$20, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(PregnancyHistoryImpl.TERM$20);
            }
            target.set((XmlObject)term);
        }
    }
    
    public int getPremature() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.PREMATURE$22, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetPremature() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.PREMATURE$22, 0);
            return target;
        }
    }
    
    public void setPremature(final int premature) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.PREMATURE$22, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.PREMATURE$22);
            }
            target.setIntValue(premature);
        }
    }
    
    public void xsetPremature(final XmlInt premature) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.PREMATURE$22, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(PregnancyHistoryImpl.PREMATURE$22);
            }
            target.set((XmlObject)premature);
        }
    }
    
    public int getAbortuses() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.ABORTUSES$24, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetAbortuses() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.ABORTUSES$24, 0);
            return target;
        }
    }
    
    public void setAbortuses(final int abortuses) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.ABORTUSES$24, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.ABORTUSES$24);
            }
            target.setIntValue(abortuses);
        }
    }
    
    public void xsetAbortuses(final XmlInt abortuses) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.ABORTUSES$24, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(PregnancyHistoryImpl.ABORTUSES$24);
            }
            target.set((XmlObject)abortuses);
        }
    }
    
    public int getLiving() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.LIVING$26, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetLiving() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.LIVING$26, 0);
            return target;
        }
    }
    
    public void setLiving(final int living) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PregnancyHistoryImpl.LIVING$26, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PregnancyHistoryImpl.LIVING$26);
            }
            target.setIntValue(living);
        }
    }
    
    public void xsetLiving(final XmlInt living) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(PregnancyHistoryImpl.LIVING$26, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(PregnancyHistoryImpl.LIVING$26);
            }
            target.set((XmlObject)living);
        }
    }
    
    static {
        LMP$0 = new QName("http://www.oscarmcmaster.org/AR2005", "LMP");
        LMPCERTAIN$2 = new QName("http://www.oscarmcmaster.org/AR2005", "LMPCertain");
        MENCYCLE$4 = new QName("http://www.oscarmcmaster.org/AR2005", "menCycle");
        MENCYCLEREGULAR$6 = new QName("http://www.oscarmcmaster.org/AR2005", "menCycleRegular");
        CONTRACEPTIVETYPE$8 = new QName("http://www.oscarmcmaster.org/AR2005", "contraceptiveType");
        CONTRACEPTIVELASTUSED$10 = new QName("http://www.oscarmcmaster.org/AR2005", "contraceptiveLastUsed");
        MENSTRUALEDB$12 = new QName("http://www.oscarmcmaster.org/AR2005", "menstrualEDB");
        FINALEDB$14 = new QName("http://www.oscarmcmaster.org/AR2005", "finalEDB");
        DATINGMETHODS$16 = new QName("http://www.oscarmcmaster.org/AR2005", "datingMethods");
        GRAVIDA$18 = new QName("http://www.oscarmcmaster.org/AR2005", "gravida");
        TERM$20 = new QName("http://www.oscarmcmaster.org/AR2005", "term");
        PREMATURE$22 = new QName("http://www.oscarmcmaster.org/AR2005", "premature");
        ABORTUSES$24 = new QName("http://www.oscarmcmaster.org/AR2005", "abortuses");
        LIVING$26 = new QName("http://www.oscarmcmaster.org/AR2005", "living");
    }
}
