package ca.openosp.openo.ckd.impl;

import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.XmlObject;
import java.util.List;
import java.util.ArrayList;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ckd.DxCodes;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class DxCodesImpl extends XmlComplexContentImpl implements DxCodes
{
    private static final long serialVersionUID = 1L;
    private static final QName CODE$0;
    
    public DxCodesImpl(final SchemaType sType) {
        super(sType);
    }
    
    public Code[] getCodeArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(DxCodesImpl.CODE$0, targetList);
            final Code[] result = new Code[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public Code getCodeArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Code target = null;
            target = (Code)this.get_store().find_element_user(DxCodesImpl.CODE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfCodeArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(DxCodesImpl.CODE$0);
        }
    }
    
    public void setCodeArray(final Code[] codeArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])codeArray, DxCodesImpl.CODE$0);
        }
    }
    
    public void setCodeArray(final int i, final Code code) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Code target = null;
            target = (Code)this.get_store().find_element_user(DxCodesImpl.CODE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)code);
        }
    }
    
    public Code insertNewCode(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Code target = null;
            target = (Code)this.get_store().insert_element_user(DxCodesImpl.CODE$0, i);
            return target;
        }
    }
    
    public Code addNewCode() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Code target = null;
            target = (Code)this.get_store().add_element_user(DxCodesImpl.CODE$0);
            return target;
        }
    }
    
    public void removeCode(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(DxCodesImpl.CODE$0, i);
        }
    }
    
    static {
        CODE$0 = new QName("http://www.oscarmcmaster.org/ckd", "code");
    }
    
    public static class CodeImpl extends JavaStringHolderEx implements Code
    {
        private static final long serialVersionUID = 1L;
        private static final QName TYPE$0;
        private static final QName NAME$2;
        
        public CodeImpl(final SchemaType sType) {
            super(sType, true);
        }
        
        protected CodeImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
        
        public Type.Enum getType() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_attribute_user(CodeImpl.TYPE$0);
                if (target == null) {
                    return null;
                }
                return (Type.Enum)target.getEnumValue();
            }
        }
        
        public Type xgetType() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Type target = null;
                target = (Type)this.get_store().find_attribute_user(CodeImpl.TYPE$0);
                return target;
            }
        }
        
        public boolean isSetType() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                return this.get_store().find_attribute_user(CodeImpl.TYPE$0) != null;
            }
        }
        
        public void setType(final Type.Enum type) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_attribute_user(CodeImpl.TYPE$0);
                if (target == null) {
                    target = (SimpleValue)this.get_store().add_attribute_user(CodeImpl.TYPE$0);
                }
                target.setEnumValue((StringEnumAbstractBase)type);
            }
        }
        
        public void xsetType(final Type type) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Type target = null;
                target = (Type)this.get_store().find_attribute_user(CodeImpl.TYPE$0);
                if (target == null) {
                    target = (Type)this.get_store().add_attribute_user(CodeImpl.TYPE$0);
                }
                target.set((XmlObject)type);
            }
        }
        
        public void unsetType() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.get_store().remove_attribute(CodeImpl.TYPE$0);
            }
        }
        
        public String getName() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_attribute_user(CodeImpl.NAME$2);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        public XmlString xgetName() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                XmlString target = null;
                target = (XmlString)this.get_store().find_attribute_user(CodeImpl.NAME$2);
                return target;
            }
        }
        
        public boolean isSetName() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                return this.get_store().find_attribute_user(CodeImpl.NAME$2) != null;
            }
        }
        
        public void setName(final String name) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_attribute_user(CodeImpl.NAME$2);
                if (target == null) {
                    target = (SimpleValue)this.get_store().add_attribute_user(CodeImpl.NAME$2);
                }
                target.setStringValue(name);
            }
        }
        
        public void xsetName(final XmlString name) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                XmlString target = null;
                target = (XmlString)this.get_store().find_attribute_user(CodeImpl.NAME$2);
                if (target == null) {
                    target = (XmlString)this.get_store().add_attribute_user(CodeImpl.NAME$2);
                }
                target.set((XmlObject)name);
            }
        }
        
        public void unsetName() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.get_store().remove_attribute(CodeImpl.NAME$2);
            }
        }
        
        static {
            TYPE$0 = new QName("", "type");
            NAME$2 = new QName("", "name");
        }
        
        public static class TypeImpl extends JavaStringEnumerationHolderEx implements Type
        {
            private static final long serialVersionUID = 1L;
            
            public TypeImpl(final SchemaType sType) {
                super(sType, false);
            }
            
            protected TypeImpl(final SchemaType sType, final boolean b) {
                super(sType, b);
            }
        }
    }
}
