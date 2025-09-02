package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.impl.values.JavaFloatHolderEx;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import ca.openosp.openo.ar2005.CustomLab;
import ca.openosp.openo.ar2005.PrenatalGeneticScreeningType;
import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.InitialLaboratoryInvestigations;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class InitialLaboratoryInvestigationsImpl extends XmlComplexContentImpl implements InitialLaboratoryInvestigations
{
    private static final long serialVersionUID = 1L;
    private static final QName HBRESULT$0;
    private static final QName HIVRESULT$2;
    private static final QName HIVCOUNSEL$4;
    private static final QName LASTPAPDATE$6;
    private static final QName PAPRESULT$8;
    private static final QName MCVRESULT$10;
    private static final QName ABORESULT$12;
    private static final QName RHRESULT$14;
    private static final QName ANTIBODYRESULT$16;
    private static final QName GCRESULTGONORRHEA$18;
    private static final QName GCRESULTCHLAMYDIA$20;
    private static final QName RUBELLARESULT$22;
    private static final QName URINERESULT$24;
    private static final QName HBSAGRESULT$26;
    private static final QName VDRLRESULT$28;
    private static final QName SICKLECELLRESULT$30;
    private static final QName PRENATALGENERICSCREENING$32;
    private static final QName CUSTOMLAB1$34;
    private static final QName CUSTOMLAB2$36;
    
    public InitialLaboratoryInvestigationsImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String getHbResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HBRESULT$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetHbResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HBRESULT$0, 0);
            return target;
        }
    }
    
    public void setHbResult(final String hbResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HBRESULT$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.HBRESULT$0);
            }
            target.setStringValue(hbResult);
        }
    }
    
    public void xsetHbResult(final XmlString hbResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HBRESULT$0, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.HBRESULT$0);
            }
            target.set((XmlObject)hbResult);
        }
    }
    
    public HivResult.Enum getHivResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HIVRESULT$2, 0);
            if (target == null) {
                return null;
            }
            return (HivResult.Enum)target.getEnumValue();
        }
    }
    
    public HivResult xgetHivResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            HivResult target = null;
            target = (HivResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HIVRESULT$2, 0);
            return target;
        }
    }
    
    public void setHivResult(final HivResult.Enum hivResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HIVRESULT$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.HIVRESULT$2);
            }
            target.setEnumValue((StringEnumAbstractBase)hivResult);
        }
    }
    
    public void xsetHivResult(final HivResult hivResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            HivResult target = null;
            target = (HivResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HIVRESULT$2, 0);
            if (target == null) {
                target = (HivResult)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.HIVRESULT$2);
            }
            target.set((XmlObject)hivResult);
        }
    }
    
    public boolean getHivCounsel() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HIVCOUNSEL$4, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetHivCounsel() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HIVCOUNSEL$4, 0);
            return target;
        }
    }
    
    public void setHivCounsel(final boolean hivCounsel) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HIVCOUNSEL$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.HIVCOUNSEL$4);
            }
            target.setBooleanValue(hivCounsel);
        }
    }
    
    public void xsetHivCounsel(final XmlBoolean hivCounsel) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HIVCOUNSEL$4, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.HIVCOUNSEL$4);
            }
            target.set((XmlObject)hivCounsel);
        }
    }
    
    public Calendar getLastPapDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.LASTPAPDATE$6, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetLastPapDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.LASTPAPDATE$6, 0);
            return target;
        }
    }
    
    public boolean isNilLastPapDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.LASTPAPDATE$6, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setLastPapDate(final Calendar lastPapDate) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.LASTPAPDATE$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.LASTPAPDATE$6);
            }
            target.setCalendarValue(lastPapDate);
        }
    }
    
    public void xsetLastPapDate(final XmlDate lastPapDate) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.LASTPAPDATE$6, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.LASTPAPDATE$6);
            }
            target.set((XmlObject)lastPapDate);
        }
    }
    
    public void setNilLastPapDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.LASTPAPDATE$6, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.LASTPAPDATE$6);
            }
            target.setNil();
        }
    }
    
    public String getPapResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.PAPRESULT$8, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetPapResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.PAPRESULT$8, 0);
            return target;
        }
    }
    
    public void setPapResult(final String papResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.PAPRESULT$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.PAPRESULT$8);
            }
            target.setStringValue(papResult);
        }
    }
    
    public void xsetPapResult(final XmlString papResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.PAPRESULT$8, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.PAPRESULT$8);
            }
            target.set((XmlObject)papResult);
        }
    }
    
    public float getMcvResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.MCVRESULT$10, 0);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }
    
    public McvResult xgetMcvResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            McvResult target = null;
            target = (McvResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.MCVRESULT$10, 0);
            return target;
        }
    }
    
    public boolean isNilMcvResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            McvResult target = null;
            target = (McvResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.MCVRESULT$10, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setMcvResult(final float mcvResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.MCVRESULT$10, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.MCVRESULT$10);
            }
            target.setFloatValue(mcvResult);
        }
    }
    
    public void xsetMcvResult(final McvResult mcvResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            McvResult target = null;
            target = (McvResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.MCVRESULT$10, 0);
            if (target == null) {
                target = (McvResult)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.MCVRESULT$10);
            }
            target.set((XmlObject)mcvResult);
        }
    }
    
    public void setNilMcvResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            McvResult target = null;
            target = (McvResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.MCVRESULT$10, 0);
            if (target == null) {
                target = (McvResult)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.MCVRESULT$10);
            }
            target.setNil();
        }
    }
    
    public AboResult.Enum getAboResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.ABORESULT$12, 0);
            if (target == null) {
                return null;
            }
            return (AboResult.Enum)target.getEnumValue();
        }
    }
    
    public AboResult xgetAboResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AboResult target = null;
            target = (AboResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.ABORESULT$12, 0);
            return target;
        }
    }
    
    public void setAboResult(final AboResult.Enum aboResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.ABORESULT$12, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.ABORESULT$12);
            }
            target.setEnumValue((StringEnumAbstractBase)aboResult);
        }
    }
    
    public void xsetAboResult(final AboResult aboResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AboResult target = null;
            target = (AboResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.ABORESULT$12, 0);
            if (target == null) {
                target = (AboResult)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.ABORESULT$12);
            }
            target.set((XmlObject)aboResult);
        }
    }
    
    public RhResult.Enum getRhResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.RHRESULT$14, 0);
            if (target == null) {
                return null;
            }
            return (RhResult.Enum)target.getEnumValue();
        }
    }
    
    public RhResult xgetRhResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            RhResult target = null;
            target = (RhResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.RHRESULT$14, 0);
            return target;
        }
    }
    
    public void setRhResult(final RhResult.Enum rhResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.RHRESULT$14, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.RHRESULT$14);
            }
            target.setEnumValue((StringEnumAbstractBase)rhResult);
        }
    }
    
    public void xsetRhResult(final RhResult rhResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            RhResult target = null;
            target = (RhResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.RHRESULT$14, 0);
            if (target == null) {
                target = (RhResult)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.RHRESULT$14);
            }
            target.set((XmlObject)rhResult);
        }
    }
    
    public String getAntibodyResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.ANTIBODYRESULT$16, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetAntibodyResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.ANTIBODYRESULT$16, 0);
            return target;
        }
    }
    
    public void setAntibodyResult(final String antibodyResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.ANTIBODYRESULT$16, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.ANTIBODYRESULT$16);
            }
            target.setStringValue(antibodyResult);
        }
    }
    
    public void xsetAntibodyResult(final XmlString antibodyResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.ANTIBODYRESULT$16, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.ANTIBODYRESULT$16);
            }
            target.set((XmlObject)antibodyResult);
        }
    }
    
    public GcResultGonorrhea.Enum getGcResultGonorrhea() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTGONORRHEA$18, 0);
            if (target == null) {
                return null;
            }
            return (GcResultGonorrhea.Enum)target.getEnumValue();
        }
    }
    
    public GcResultGonorrhea xgetGcResultGonorrhea() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            GcResultGonorrhea target = null;
            target = (GcResultGonorrhea)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTGONORRHEA$18, 0);
            return target;
        }
    }
    
    public void setGcResultGonorrhea(final GcResultGonorrhea.Enum gcResultGonorrhea) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTGONORRHEA$18, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTGONORRHEA$18);
            }
            target.setEnumValue((StringEnumAbstractBase)gcResultGonorrhea);
        }
    }
    
    public void xsetGcResultGonorrhea(final GcResultGonorrhea gcResultGonorrhea) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            GcResultGonorrhea target = null;
            target = (GcResultGonorrhea)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTGONORRHEA$18, 0);
            if (target == null) {
                target = (GcResultGonorrhea)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTGONORRHEA$18);
            }
            target.set((XmlObject)gcResultGonorrhea);
        }
    }
    
    public GcResultChlamydia.Enum getGcResultChlamydia() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTCHLAMYDIA$20, 0);
            if (target == null) {
                return null;
            }
            return (GcResultChlamydia.Enum)target.getEnumValue();
        }
    }
    
    public GcResultChlamydia xgetGcResultChlamydia() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            GcResultChlamydia target = null;
            target = (GcResultChlamydia)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTCHLAMYDIA$20, 0);
            return target;
        }
    }
    
    public void setGcResultChlamydia(final GcResultChlamydia.Enum gcResultChlamydia) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTCHLAMYDIA$20, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTCHLAMYDIA$20);
            }
            target.setEnumValue((StringEnumAbstractBase)gcResultChlamydia);
        }
    }
    
    public void xsetGcResultChlamydia(final GcResultChlamydia gcResultChlamydia) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            GcResultChlamydia target = null;
            target = (GcResultChlamydia)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTCHLAMYDIA$20, 0);
            if (target == null) {
                target = (GcResultChlamydia)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.GCRESULTCHLAMYDIA$20);
            }
            target.set((XmlObject)gcResultChlamydia);
        }
    }
    
    public String getRubellaResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.RUBELLARESULT$22, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetRubellaResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.RUBELLARESULT$22, 0);
            return target;
        }
    }
    
    public void setRubellaResult(final String rubellaResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.RUBELLARESULT$22, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.RUBELLARESULT$22);
            }
            target.setStringValue(rubellaResult);
        }
    }
    
    public void xsetRubellaResult(final XmlString rubellaResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.RUBELLARESULT$22, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.RUBELLARESULT$22);
            }
            target.set((XmlObject)rubellaResult);
        }
    }
    
    public String getUrineResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.URINERESULT$24, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetUrineResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.URINERESULT$24, 0);
            return target;
        }
    }
    
    public void setUrineResult(final String urineResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.URINERESULT$24, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.URINERESULT$24);
            }
            target.setStringValue(urineResult);
        }
    }
    
    public void xsetUrineResult(final XmlString urineResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.URINERESULT$24, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.URINERESULT$24);
            }
            target.set((XmlObject)urineResult);
        }
    }
    
    public HbsAgResult.Enum getHbsAgResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HBSAGRESULT$26, 0);
            if (target == null) {
                return null;
            }
            return (HbsAgResult.Enum)target.getEnumValue();
        }
    }
    
    public HbsAgResult xgetHbsAgResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            HbsAgResult target = null;
            target = (HbsAgResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HBSAGRESULT$26, 0);
            return target;
        }
    }
    
    public void setHbsAgResult(final HbsAgResult.Enum hbsAgResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HBSAGRESULT$26, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.HBSAGRESULT$26);
            }
            target.setEnumValue((StringEnumAbstractBase)hbsAgResult);
        }
    }
    
    public void xsetHbsAgResult(final HbsAgResult hbsAgResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            HbsAgResult target = null;
            target = (HbsAgResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.HBSAGRESULT$26, 0);
            if (target == null) {
                target = (HbsAgResult)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.HBSAGRESULT$26);
            }
            target.set((XmlObject)hbsAgResult);
        }
    }
    
    public VdrlResult.Enum getVdrlResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.VDRLRESULT$28, 0);
            if (target == null) {
                return null;
            }
            return (VdrlResult.Enum)target.getEnumValue();
        }
    }
    
    public VdrlResult xgetVdrlResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            VdrlResult target = null;
            target = (VdrlResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.VDRLRESULT$28, 0);
            return target;
        }
    }
    
    public void setVdrlResult(final VdrlResult.Enum vdrlResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.VDRLRESULT$28, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.VDRLRESULT$28);
            }
            target.setEnumValue((StringEnumAbstractBase)vdrlResult);
        }
    }
    
    public void xsetVdrlResult(final VdrlResult vdrlResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            VdrlResult target = null;
            target = (VdrlResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.VDRLRESULT$28, 0);
            if (target == null) {
                target = (VdrlResult)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.VDRLRESULT$28);
            }
            target.set((XmlObject)vdrlResult);
        }
    }
    
    public SickleCellResult.Enum getSickleCellResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.SICKLECELLRESULT$30, 0);
            if (target == null) {
                return null;
            }
            return (SickleCellResult.Enum)target.getEnumValue();
        }
    }
    
    public SickleCellResult xgetSickleCellResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SickleCellResult target = null;
            target = (SickleCellResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.SICKLECELLRESULT$30, 0);
            return target;
        }
    }
    
    public void setSickleCellResult(final SickleCellResult.Enum sickleCellResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.SICKLECELLRESULT$30, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.SICKLECELLRESULT$30);
            }
            target.setEnumValue((StringEnumAbstractBase)sickleCellResult);
        }
    }
    
    public void xsetSickleCellResult(final SickleCellResult sickleCellResult) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SickleCellResult target = null;
            target = (SickleCellResult)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.SICKLECELLRESULT$30, 0);
            if (target == null) {
                target = (SickleCellResult)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.SICKLECELLRESULT$30);
            }
            target.set((XmlObject)sickleCellResult);
        }
    }
    
    public PrenatalGeneticScreeningType getPrenatalGenericScreening() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PrenatalGeneticScreeningType target = null;
            target = (PrenatalGeneticScreeningType)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.PRENATALGENERICSCREENING$32, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setPrenatalGenericScreening(final PrenatalGeneticScreeningType prenatalGenericScreening) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PrenatalGeneticScreeningType target = null;
            target = (PrenatalGeneticScreeningType)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.PRENATALGENERICSCREENING$32, 0);
            if (target == null) {
                target = (PrenatalGeneticScreeningType)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.PRENATALGENERICSCREENING$32);
            }
            target.set((XmlObject)prenatalGenericScreening);
        }
    }
    
    public PrenatalGeneticScreeningType addNewPrenatalGenericScreening() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PrenatalGeneticScreeningType target = null;
            target = (PrenatalGeneticScreeningType)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.PRENATALGENERICSCREENING$32);
            return target;
        }
    }
    
    public CustomLab getCustomLab1() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.CUSTOMLAB1$34, 0);
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
            target = (CustomLab)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.CUSTOMLAB1$34, 0);
            if (target == null) {
                target = (CustomLab)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.CUSTOMLAB1$34);
            }
            target.set((XmlObject)customLab1);
        }
    }
    
    public CustomLab addNewCustomLab1() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.CUSTOMLAB1$34);
            return target;
        }
    }
    
    public CustomLab getCustomLab2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.CUSTOMLAB2$36, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCustomLab2(final CustomLab customLab2) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(InitialLaboratoryInvestigationsImpl.CUSTOMLAB2$36, 0);
            if (target == null) {
                target = (CustomLab)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.CUSTOMLAB2$36);
            }
            target.set((XmlObject)customLab2);
        }
    }
    
    public CustomLab addNewCustomLab2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().add_element_user(InitialLaboratoryInvestigationsImpl.CUSTOMLAB2$36);
            return target;
        }
    }
    
    static {
        HBRESULT$0 = new QName("http://www.oscarmcmaster.org/AR2005", "hbResult");
        HIVRESULT$2 = new QName("http://www.oscarmcmaster.org/AR2005", "hivResult");
        HIVCOUNSEL$4 = new QName("http://www.oscarmcmaster.org/AR2005", "hivCounsel");
        LASTPAPDATE$6 = new QName("http://www.oscarmcmaster.org/AR2005", "lastPapDate");
        PAPRESULT$8 = new QName("http://www.oscarmcmaster.org/AR2005", "papResult");
        MCVRESULT$10 = new QName("http://www.oscarmcmaster.org/AR2005", "mcvResult");
        ABORESULT$12 = new QName("http://www.oscarmcmaster.org/AR2005", "aboResult");
        RHRESULT$14 = new QName("http://www.oscarmcmaster.org/AR2005", "rhResult");
        ANTIBODYRESULT$16 = new QName("http://www.oscarmcmaster.org/AR2005", "antibodyResult");
        GCRESULTGONORRHEA$18 = new QName("http://www.oscarmcmaster.org/AR2005", "gcResultGonorrhea");
        GCRESULTCHLAMYDIA$20 = new QName("http://www.oscarmcmaster.org/AR2005", "gcResultChlamydia");
        RUBELLARESULT$22 = new QName("http://www.oscarmcmaster.org/AR2005", "rubellaResult");
        URINERESULT$24 = new QName("http://www.oscarmcmaster.org/AR2005", "urineResult");
        HBSAGRESULT$26 = new QName("http://www.oscarmcmaster.org/AR2005", "hbsAgResult");
        VDRLRESULT$28 = new QName("http://www.oscarmcmaster.org/AR2005", "vdrlResult");
        SICKLECELLRESULT$30 = new QName("http://www.oscarmcmaster.org/AR2005", "sickleCellResult");
        PRENATALGENERICSCREENING$32 = new QName("http://www.oscarmcmaster.org/AR2005", "prenatalGenericScreening");
        CUSTOMLAB1$34 = new QName("http://www.oscarmcmaster.org/AR2005", "customLab1");
        CUSTOMLAB2$36 = new QName("http://www.oscarmcmaster.org/AR2005", "customLab2");
    }
    
    public static class HivResultImpl extends JavaStringEnumerationHolderEx implements HivResult
    {
        private static final long serialVersionUID = 1L;
        
        public HivResultImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected HivResultImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class McvResultImpl extends JavaFloatHolderEx implements McvResult
    {
        private static final long serialVersionUID = 1L;
        
        public McvResultImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected McvResultImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class AboResultImpl extends JavaStringEnumerationHolderEx implements AboResult
    {
        private static final long serialVersionUID = 1L;
        
        public AboResultImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected AboResultImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class RhResultImpl extends JavaStringEnumerationHolderEx implements RhResult
    {
        private static final long serialVersionUID = 1L;
        
        public RhResultImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected RhResultImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class GcResultGonorrheaImpl extends JavaStringEnumerationHolderEx implements GcResultGonorrhea
    {
        private static final long serialVersionUID = 1L;
        
        public GcResultGonorrheaImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected GcResultGonorrheaImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class GcResultChlamydiaImpl extends JavaStringEnumerationHolderEx implements GcResultChlamydia
    {
        private static final long serialVersionUID = 1L;
        
        public GcResultChlamydiaImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected GcResultChlamydiaImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class HbsAgResultImpl extends JavaStringEnumerationHolderEx implements HbsAgResult
    {
        private static final long serialVersionUID = 1L;
        
        public HbsAgResultImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected HbsAgResultImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class VdrlResultImpl extends JavaStringEnumerationHolderEx implements VdrlResult
    {
        private static final long serialVersionUID = 1L;
        
        public VdrlResultImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected VdrlResultImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class SickleCellResultImpl extends JavaStringEnumerationHolderEx implements SickleCellResult
    {
        private static final long serialVersionUID = 1L;
        
        public SickleCellResultImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected SickleCellResultImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
}
