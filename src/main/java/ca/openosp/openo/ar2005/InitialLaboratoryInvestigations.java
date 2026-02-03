package ca.openosp.openo.ar2005;

import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.w3c.dom.Node;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;
import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.io.File;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface InitialLaboratoryInvestigations extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(InitialLaboratoryInvestigations.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("initiallaboratoryinvestigations708dtype");
    
    String getHbResult();
    
    XmlString xgetHbResult();
    
    void setHbResult(final String p0);
    
    void xsetHbResult(final XmlString p0);
    
    HivResult.Enum getHivResult();
    
    HivResult xgetHivResult();
    
    void setHivResult(final HivResult.Enum p0);
    
    void xsetHivResult(final HivResult p0);
    
    boolean getHivCounsel();
    
    XmlBoolean xgetHivCounsel();
    
    void setHivCounsel(final boolean p0);
    
    void xsetHivCounsel(final XmlBoolean p0);
    
    Calendar getLastPapDate();
    
    XmlDate xgetLastPapDate();
    
    boolean isNilLastPapDate();
    
    void setLastPapDate(final Calendar p0);
    
    void xsetLastPapDate(final XmlDate p0);
    
    void setNilLastPapDate();
    
    String getPapResult();
    
    XmlString xgetPapResult();
    
    void setPapResult(final String p0);
    
    void xsetPapResult(final XmlString p0);
    
    float getMcvResult();
    
    McvResult xgetMcvResult();
    
    boolean isNilMcvResult();
    
    void setMcvResult(final float p0);
    
    void xsetMcvResult(final McvResult p0);
    
    void setNilMcvResult();
    
    AboResult.Enum getAboResult();
    
    AboResult xgetAboResult();
    
    void setAboResult(final AboResult.Enum p0);
    
    void xsetAboResult(final AboResult p0);
    
    RhResult.Enum getRhResult();
    
    RhResult xgetRhResult();
    
    void setRhResult(final RhResult.Enum p0);
    
    void xsetRhResult(final RhResult p0);
    
    String getAntibodyResult();
    
    XmlString xgetAntibodyResult();
    
    void setAntibodyResult(final String p0);
    
    void xsetAntibodyResult(final XmlString p0);
    
    GcResultGonorrhea.Enum getGcResultGonorrhea();
    
    GcResultGonorrhea xgetGcResultGonorrhea();
    
    void setGcResultGonorrhea(final GcResultGonorrhea.Enum p0);
    
    void xsetGcResultGonorrhea(final GcResultGonorrhea p0);
    
    GcResultChlamydia.Enum getGcResultChlamydia();
    
    GcResultChlamydia xgetGcResultChlamydia();
    
    void setGcResultChlamydia(final GcResultChlamydia.Enum p0);
    
    void xsetGcResultChlamydia(final GcResultChlamydia p0);
    
    String getRubellaResult();
    
    XmlString xgetRubellaResult();
    
    void setRubellaResult(final String p0);
    
    void xsetRubellaResult(final XmlString p0);
    
    String getUrineResult();
    
    XmlString xgetUrineResult();
    
    void setUrineResult(final String p0);
    
    void xsetUrineResult(final XmlString p0);
    
    HbsAgResult.Enum getHbsAgResult();
    
    HbsAgResult xgetHbsAgResult();
    
    void setHbsAgResult(final HbsAgResult.Enum p0);
    
    void xsetHbsAgResult(final HbsAgResult p0);
    
    VdrlResult.Enum getVdrlResult();
    
    VdrlResult xgetVdrlResult();
    
    void setVdrlResult(final VdrlResult.Enum p0);
    
    void xsetVdrlResult(final VdrlResult p0);
    
    SickleCellResult.Enum getSickleCellResult();
    
    SickleCellResult xgetSickleCellResult();
    
    void setSickleCellResult(final SickleCellResult.Enum p0);
    
    void xsetSickleCellResult(final SickleCellResult p0);
    
    PrenatalGeneticScreeningType getPrenatalGenericScreening();
    
    void setPrenatalGenericScreening(final PrenatalGeneticScreeningType p0);
    
    PrenatalGeneticScreeningType addNewPrenatalGenericScreening();
    
    CustomLab getCustomLab1();
    
    void setCustomLab1(final CustomLab p0);
    
    CustomLab addNewCustomLab1();
    
    CustomLab getCustomLab2();
    
    void setCustomLab2(final CustomLab p0);
    
    CustomLab addNewCustomLab2();
    
    public interface HivResult extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(HivResult.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("hivresultd08belemtype");
        public static final Enum POS = Enum.forString("POS");
        public static final Enum NEG = Enum.forString("NEG");
        public static final Enum IND = Enum.forString("IND");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_POS = 1;
        public static final int INT_NEG = 2;
        public static final int INT_IND = 3;
        public static final int INT_NDONE = 4;
        public static final int INT_UNK = 5;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_POS = 1;
            static final int INT_NEG = 2;
            static final int INT_IND = 3;
            static final int INT_NDONE = 4;
            static final int INT_UNK = 5;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("POS", 1), new Enum("NEG", 2), new Enum("IND", 3), new Enum("NDONE", 4), new Enum("UNK", 5) });
            }
        }
        
        public static final class Factory
        {
            public static HivResult newValue(final Object obj) {
                return (HivResult)HivResult.type.newValue(obj);
            }
            
            public static HivResult newInstance() {
                return (HivResult)XmlBeans.getContextTypeLoader().newInstance(HivResult.type, (XmlOptions)null);
            }
            
            public static HivResult newInstance(final XmlOptions options) {
                return (HivResult)XmlBeans.getContextTypeLoader().newInstance(HivResult.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface McvResult extends XmlFloat
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(McvResult.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("mcvresult5216elemtype");
        
        public static final class Factory
        {
            public static McvResult newValue(final Object obj) {
                return (McvResult)McvResult.type.newValue(obj);
            }
            
            public static McvResult newInstance() {
                return (McvResult)XmlBeans.getContextTypeLoader().newInstance(McvResult.type, (XmlOptions)null);
            }
            
            public static McvResult newInstance(final XmlOptions options) {
                return (McvResult)XmlBeans.getContextTypeLoader().newInstance(McvResult.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface AboResult extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(AboResult.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("aboresultefe4elemtype");
        public static final Enum A = Enum.forString("A");
        public static final Enum B = Enum.forString("B");
        public static final Enum AB = Enum.forString("AB");
        public static final Enum O = Enum.forString("O");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_A = 1;
        public static final int INT_B = 2;
        public static final int INT_AB = 3;
        public static final int INT_O = 4;
        public static final int INT_NDONE = 5;
        public static final int INT_UNK = 6;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_A = 1;
            static final int INT_B = 2;
            static final int INT_AB = 3;
            static final int INT_O = 4;
            static final int INT_NDONE = 5;
            static final int INT_UNK = 6;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("A", 1), new Enum("B", 2), new Enum("AB", 3), new Enum("O", 4), new Enum("NDONE", 5), new Enum("UNK", 6) });
            }
        }
        
        public static final class Factory
        {
            public static AboResult newValue(final Object obj) {
                return (AboResult)AboResult.type.newValue(obj);
            }
            
            public static AboResult newInstance() {
                return (AboResult)XmlBeans.getContextTypeLoader().newInstance(AboResult.type, (XmlOptions)null);
            }
            
            public static AboResult newInstance(final XmlOptions options) {
                return (AboResult)XmlBeans.getContextTypeLoader().newInstance(AboResult.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface RhResult extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(RhResult.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("rhresultc3dcelemtype");
        public static final Enum POS = Enum.forString("POS");
        public static final Enum WPOS = Enum.forString("WPOS");
        public static final Enum NEG = Enum.forString("NEG");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_POS = 1;
        public static final int INT_WPOS = 2;
        public static final int INT_NEG = 3;
        public static final int INT_NDONE = 4;
        public static final int INT_UNK = 5;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_POS = 1;
            static final int INT_WPOS = 2;
            static final int INT_NEG = 3;
            static final int INT_NDONE = 4;
            static final int INT_UNK = 5;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("POS", 1), new Enum("WPOS", 2), new Enum("NEG", 3), new Enum("NDONE", 4), new Enum("UNK", 5) });
            }
        }
        
        public static final class Factory
        {
            public static RhResult newValue(final Object obj) {
                return (RhResult)RhResult.type.newValue(obj);
            }
            
            public static RhResult newInstance() {
                return (RhResult)XmlBeans.getContextTypeLoader().newInstance(RhResult.type, (XmlOptions)null);
            }
            
            public static RhResult newInstance(final XmlOptions options) {
                return (RhResult)XmlBeans.getContextTypeLoader().newInstance(RhResult.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface GcResultGonorrhea extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(GcResultGonorrhea.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("gcresultgonorrhead61belemtype");
        public static final Enum POS = Enum.forString("POS");
        public static final Enum NEG = Enum.forString("NEG");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_POS = 1;
        public static final int INT_NEG = 2;
        public static final int INT_NDONE = 3;
        public static final int INT_UNK = 4;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_POS = 1;
            static final int INT_NEG = 2;
            static final int INT_NDONE = 3;
            static final int INT_UNK = 4;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("POS", 1), new Enum("NEG", 2), new Enum("NDONE", 3), new Enum("UNK", 4) });
            }
        }
        
        public static final class Factory
        {
            public static GcResultGonorrhea newValue(final Object obj) {
                return (GcResultGonorrhea)GcResultGonorrhea.type.newValue(obj);
            }
            
            public static GcResultGonorrhea newInstance() {
                return (GcResultGonorrhea)XmlBeans.getContextTypeLoader().newInstance(GcResultGonorrhea.type, (XmlOptions)null);
            }
            
            public static GcResultGonorrhea newInstance(final XmlOptions options) {
                return (GcResultGonorrhea)XmlBeans.getContextTypeLoader().newInstance(GcResultGonorrhea.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface GcResultChlamydia extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(GcResultChlamydia.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("gcresultchlamydiacb16elemtype");
        public static final Enum POS = Enum.forString("POS");
        public static final Enum NEG = Enum.forString("NEG");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_POS = 1;
        public static final int INT_NEG = 2;
        public static final int INT_NDONE = 3;
        public static final int INT_UNK = 4;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_POS = 1;
            static final int INT_NEG = 2;
            static final int INT_NDONE = 3;
            static final int INT_UNK = 4;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("POS", 1), new Enum("NEG", 2), new Enum("NDONE", 3), new Enum("UNK", 4) });
            }
        }
        
        public static final class Factory
        {
            public static GcResultChlamydia newValue(final Object obj) {
                return (GcResultChlamydia)GcResultChlamydia.type.newValue(obj);
            }
            
            public static GcResultChlamydia newInstance() {
                return (GcResultChlamydia)XmlBeans.getContextTypeLoader().newInstance(GcResultChlamydia.type, (XmlOptions)null);
            }
            
            public static GcResultChlamydia newInstance(final XmlOptions options) {
                return (GcResultChlamydia)XmlBeans.getContextTypeLoader().newInstance(GcResultChlamydia.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface HbsAgResult extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(HbsAgResult.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("hbsagresultd4b5elemtype");
        public static final Enum POS = Enum.forString("POS");
        public static final Enum NEG = Enum.forString("NEG");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_POS = 1;
        public static final int INT_NEG = 2;
        public static final int INT_NDONE = 3;
        public static final int INT_UNK = 4;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_POS = 1;
            static final int INT_NEG = 2;
            static final int INT_NDONE = 3;
            static final int INT_UNK = 4;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("POS", 1), new Enum("NEG", 2), new Enum("NDONE", 3), new Enum("UNK", 4) });
            }
        }
        
        public static final class Factory
        {
            public static HbsAgResult newValue(final Object obj) {
                return (HbsAgResult)HbsAgResult.type.newValue(obj);
            }
            
            public static HbsAgResult newInstance() {
                return (HbsAgResult)XmlBeans.getContextTypeLoader().newInstance(HbsAgResult.type, (XmlOptions)null);
            }
            
            public static HbsAgResult newInstance(final XmlOptions options) {
                return (HbsAgResult)XmlBeans.getContextTypeLoader().newInstance(HbsAgResult.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface VdrlResult extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(VdrlResult.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("vdrlresultce0eelemtype");
        public static final Enum POS = Enum.forString("POS");
        public static final Enum NEG = Enum.forString("NEG");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_POS = 1;
        public static final int INT_NEG = 2;
        public static final int INT_NDONE = 3;
        public static final int INT_UNK = 4;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_POS = 1;
            static final int INT_NEG = 2;
            static final int INT_NDONE = 3;
            static final int INT_UNK = 4;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("POS", 1), new Enum("NEG", 2), new Enum("NDONE", 3), new Enum("UNK", 4) });
            }
        }
        
        public static final class Factory
        {
            public static VdrlResult newValue(final Object obj) {
                return (VdrlResult)VdrlResult.type.newValue(obj);
            }
            
            public static VdrlResult newInstance() {
                return (VdrlResult)XmlBeans.getContextTypeLoader().newInstance(VdrlResult.type, (XmlOptions)null);
            }
            
            public static VdrlResult newInstance(final XmlOptions options) {
                return (VdrlResult)XmlBeans.getContextTypeLoader().newInstance(VdrlResult.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface SickleCellResult extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(SickleCellResult.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("sicklecellresultb65felemtype");
        public static final Enum POS = Enum.forString("POS");
        public static final Enum NEG = Enum.forString("NEG");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_POS = 1;
        public static final int INT_NEG = 2;
        public static final int INT_NDONE = 3;
        public static final int INT_UNK = 4;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_POS = 1;
            static final int INT_NEG = 2;
            static final int INT_NDONE = 3;
            static final int INT_UNK = 4;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("POS", 1), new Enum("NEG", 2), new Enum("NDONE", 3), new Enum("UNK", 4) });
            }
        }
        
        public static final class Factory
        {
            public static SickleCellResult newValue(final Object obj) {
                return (SickleCellResult)SickleCellResult.type.newValue(obj);
            }
            
            public static SickleCellResult newInstance() {
                return (SickleCellResult)XmlBeans.getContextTypeLoader().newInstance(SickleCellResult.type, (XmlOptions)null);
            }
            
            public static SickleCellResult newInstance(final XmlOptions options) {
                return (SickleCellResult)XmlBeans.getContextTypeLoader().newInstance(SickleCellResult.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public static final class Factory
    {
        public static InitialLaboratoryInvestigations newInstance() {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().newInstance(InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        public static InitialLaboratoryInvestigations newInstance(final XmlOptions options) {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().newInstance(InitialLaboratoryInvestigations.type, options);
        }
        
        public static InitialLaboratoryInvestigations parse(final String xmlAsString) throws XmlException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(xmlAsString, InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        public static InitialLaboratoryInvestigations parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(xmlAsString, InitialLaboratoryInvestigations.type, options);
        }
        
        public static InitialLaboratoryInvestigations parse(final File file) throws XmlException, IOException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(file, InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        public static InitialLaboratoryInvestigations parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(file, InitialLaboratoryInvestigations.type, options);
        }
        
        public static InitialLaboratoryInvestigations parse(final URL u) throws XmlException, IOException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(u, InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        public static InitialLaboratoryInvestigations parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(u, InitialLaboratoryInvestigations.type, options);
        }
        
        public static InitialLaboratoryInvestigations parse(final InputStream is) throws XmlException, IOException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(is, InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        public static InitialLaboratoryInvestigations parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(is, InitialLaboratoryInvestigations.type, options);
        }
        
        public static InitialLaboratoryInvestigations parse(final Reader r) throws XmlException, IOException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(r, InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        public static InitialLaboratoryInvestigations parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(r, InitialLaboratoryInvestigations.type, options);
        }
        
        public static InitialLaboratoryInvestigations parse(final XMLStreamReader sr) throws XmlException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(sr, InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        public static InitialLaboratoryInvestigations parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(sr, InitialLaboratoryInvestigations.type, options);
        }
        
        public static InitialLaboratoryInvestigations parse(final Node node) throws XmlException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(node, InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        public static InitialLaboratoryInvestigations parse(final Node node, final XmlOptions options) throws XmlException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(node, InitialLaboratoryInvestigations.type, options);
        }
        
        @Deprecated
        public static InitialLaboratoryInvestigations parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(xis, InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static InitialLaboratoryInvestigations parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (InitialLaboratoryInvestigations)XmlBeans.getContextTypeLoader().parse(xis, InitialLaboratoryInvestigations.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, InitialLaboratoryInvestigations.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, InitialLaboratoryInvestigations.type, options);
        }
        
        private Factory() {
        }
    }
}
