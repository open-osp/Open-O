package ca.openosp.openo.flowsheets.impl;

import java.util.ArrayList;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import ca.openosp.openo.flowsheets.FlowsheetDocument;

public class FlowsheetDocumentImpl extends XmlComplexContentImpl implements FlowsheetDocument {
   private static final long serialVersionUID = 1L;
   private static final QName FLOWSHEET$0 = new QName("flowsheets.oscarehr.org", "flowsheet");

   public FlowsheetDocumentImpl(SchemaType var1) {
      super(var1);
   }

   public FlowsheetDocument.Flowsheet getFlowsheet() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         Object var2 = null;
         FlowsheetDocument.Flowsheet var5 = (FlowsheetDocument.Flowsheet)this.get_store().find_element_user(FLOWSHEET$0, 0);
         return var5 == null ? null : var5;
      }
   }

   public void setFlowsheet(FlowsheetDocument.Flowsheet var1) {
      this.generatedSetterHelperImpl(var1, FLOWSHEET$0, 0, (short)1);
   }

   public FlowsheetDocument.Flowsheet addNewFlowsheet() {
      synchronized(this.monitor()) {
         this.check_orphaned();
         FlowsheetDocument.Flowsheet var2 = null;
         var2 = (FlowsheetDocument.Flowsheet)this.get_store().add_element_user(FLOWSHEET$0);
         return var2;
      }
   }

   public static class FlowsheetImpl extends XmlComplexContentImpl implements FlowsheetDocument.Flowsheet {
      private static final long serialVersionUID = 1L;
      private static final QName INDICATOR$0 = new QName("flowsheets.oscarehr.org", "indicator");
      private static final QName HEADER$2 = new QName("flowsheets.oscarehr.org", "header");
      private static final QName MEASUREMENT$4 = new QName("flowsheets.oscarehr.org", "measurement");
      private static final QName NAME$6 = new QName("", "name");
      private static final QName DSRULES$8 = new QName("", "ds_rules");
      private static final QName DXCODETRIGGERS$10 = new QName("", "dxcode_triggers");
      private static final QName DISPLAYNAME$12 = new QName("", "display_name");
      private static final QName WARNINGCOLOUR$14 = new QName("", "warning_colour");
      private static final QName RECOMMENDATIONCOLOUR$16 = new QName("", "recommendation_colour");
      private static final QName TOPHTML$18 = new QName("", "top_HTML");

      public FlowsheetImpl(SchemaType var1) {
         super(var1);
      }

      public FlowsheetDocument.Flowsheet.Indicator[] getIndicatorArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            ArrayList var2 = new ArrayList();
            this.get_store().find_all_element_users(INDICATOR$0, var2);
            FlowsheetDocument.Flowsheet.Indicator[] var3 = new FlowsheetDocument.Flowsheet.Indicator[var2.size()];
            var2.toArray(var3);
            return var3;
         }
      }

      public FlowsheetDocument.Flowsheet.Indicator getIndicatorArray(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            FlowsheetDocument.Flowsheet.Indicator var3 = null;
            var3 = (FlowsheetDocument.Flowsheet.Indicator)this.get_store().find_element_user(INDICATOR$0, var1);
            if (var3 == null) {
               throw new IndexOutOfBoundsException();
            } else {
               return var3;
            }
         }
      }

      public int sizeOfIndicatorArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(INDICATOR$0);
         }
      }

      public void setIndicatorArray(FlowsheetDocument.Flowsheet.Indicator[] var1) {
         this.check_orphaned();
         this.arraySetterHelper(var1, INDICATOR$0);
      }

      public void setIndicatorArray(int var1, FlowsheetDocument.Flowsheet.Indicator var2) {
         this.generatedSetterHelperImpl(var2, INDICATOR$0, var1, (short)2);
      }

      public FlowsheetDocument.Flowsheet.Indicator insertNewIndicator(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            FlowsheetDocument.Flowsheet.Indicator var3 = null;
            var3 = (FlowsheetDocument.Flowsheet.Indicator)this.get_store().insert_element_user(INDICATOR$0, var1);
            return var3;
         }
      }

      public FlowsheetDocument.Flowsheet.Indicator addNewIndicator() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            FlowsheetDocument.Flowsheet.Indicator var2 = null;
            var2 = (FlowsheetDocument.Flowsheet.Indicator)this.get_store().add_element_user(INDICATOR$0);
            return var2;
         }
      }

      public void removeIndicator(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(INDICATOR$0, var1);
         }
      }

      public FlowsheetDocument.Flowsheet.Header[] getHeaderArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            ArrayList var2 = new ArrayList();
            this.get_store().find_all_element_users(HEADER$2, var2);
            FlowsheetDocument.Flowsheet.Header[] var3 = new FlowsheetDocument.Flowsheet.Header[var2.size()];
            var2.toArray(var3);
            return var3;
         }
      }

      public FlowsheetDocument.Flowsheet.Header getHeaderArray(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            FlowsheetDocument.Flowsheet.Header var3 = null;
            var3 = (FlowsheetDocument.Flowsheet.Header)this.get_store().find_element_user(HEADER$2, var1);
            if (var3 == null) {
               throw new IndexOutOfBoundsException();
            } else {
               return var3;
            }
         }
      }

      public int sizeOfHeaderArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(HEADER$2);
         }
      }

      public void setHeaderArray(FlowsheetDocument.Flowsheet.Header[] var1) {
         this.check_orphaned();
         this.arraySetterHelper(var1, HEADER$2);
      }

      public void setHeaderArray(int var1, FlowsheetDocument.Flowsheet.Header var2) {
         this.generatedSetterHelperImpl(var2, HEADER$2, var1, (short)2);
      }

      public FlowsheetDocument.Flowsheet.Header insertNewHeader(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            FlowsheetDocument.Flowsheet.Header var3 = null;
            var3 = (FlowsheetDocument.Flowsheet.Header)this.get_store().insert_element_user(HEADER$2, var1);
            return var3;
         }
      }

      public FlowsheetDocument.Flowsheet.Header addNewHeader() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            FlowsheetDocument.Flowsheet.Header var2 = null;
            var2 = (FlowsheetDocument.Flowsheet.Header)this.get_store().add_element_user(HEADER$2);
            return var2;
         }
      }

      public void removeHeader(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(HEADER$2, var1);
         }
      }

      public FlowsheetDocument.Flowsheet.Measurement[] getMeasurementArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            ArrayList var2 = new ArrayList();
            this.get_store().find_all_element_users(MEASUREMENT$4, var2);
            FlowsheetDocument.Flowsheet.Measurement[] var3 = new FlowsheetDocument.Flowsheet.Measurement[var2.size()];
            var2.toArray(var3);
            return var3;
         }
      }

      public FlowsheetDocument.Flowsheet.Measurement getMeasurementArray(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            FlowsheetDocument.Flowsheet.Measurement var3 = null;
            var3 = (FlowsheetDocument.Flowsheet.Measurement)this.get_store().find_element_user(MEASUREMENT$4, var1);
            if (var3 == null) {
               throw new IndexOutOfBoundsException();
            } else {
               return var3;
            }
         }
      }

      public int sizeOfMeasurementArray() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(MEASUREMENT$4);
         }
      }

      public void setMeasurementArray(FlowsheetDocument.Flowsheet.Measurement[] var1) {
         this.check_orphaned();
         this.arraySetterHelper(var1, MEASUREMENT$4);
      }

      public void setMeasurementArray(int var1, FlowsheetDocument.Flowsheet.Measurement var2) {
         this.generatedSetterHelperImpl(var2, MEASUREMENT$4, var1, (short)2);
      }

      public FlowsheetDocument.Flowsheet.Measurement insertNewMeasurement(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            FlowsheetDocument.Flowsheet.Measurement var3 = null;
            var3 = (FlowsheetDocument.Flowsheet.Measurement)this.get_store().insert_element_user(MEASUREMENT$4, var1);
            return var3;
         }
      }

      public FlowsheetDocument.Flowsheet.Measurement addNewMeasurement() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            FlowsheetDocument.Flowsheet.Measurement var2 = null;
            var2 = (FlowsheetDocument.Flowsheet.Measurement)this.get_store().add_element_user(MEASUREMENT$4);
            return var2;
         }
      }

      public void removeMeasurement(int var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(MEASUREMENT$4, var1);
         }
      }

      public String getName() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(NAME$6);
            return var5 == null ? null : var5.getStringValue();
         }
      }

      public XmlString xgetName() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            XmlString var5 = (XmlString)this.get_store().find_attribute_user(NAME$6);
            return var5;
         }
      }

      public boolean isSetName() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(NAME$6) != null;
         }
      }

      public void setName(String var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(NAME$6);
            if (var6 == null) {
               var6 = (SimpleValue)this.get_store().add_attribute_user(NAME$6);
            }

            var6.setStringValue(var1);
         }
      }

      public void xsetName(XmlString var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            XmlString var6 = (XmlString)this.get_store().find_attribute_user(NAME$6);
            if (var6 == null) {
               var6 = (XmlString)this.get_store().add_attribute_user(NAME$6);
            }

            var6.set(var1);
         }
      }

      public void unsetName() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(NAME$6);
         }
      }

      public String getDsRules() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(DSRULES$8);
            return var5 == null ? null : var5.getStringValue();
         }
      }

      public XmlString xgetDsRules() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            XmlString var5 = (XmlString)this.get_store().find_attribute_user(DSRULES$8);
            return var5;
         }
      }

      public boolean isSetDsRules() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(DSRULES$8) != null;
         }
      }

      public void setDsRules(String var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(DSRULES$8);
            if (var6 == null) {
               var6 = (SimpleValue)this.get_store().add_attribute_user(DSRULES$8);
            }

            var6.setStringValue(var1);
         }
      }

      public void xsetDsRules(XmlString var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            XmlString var6 = (XmlString)this.get_store().find_attribute_user(DSRULES$8);
            if (var6 == null) {
               var6 = (XmlString)this.get_store().add_attribute_user(DSRULES$8);
            }

            var6.set(var1);
         }
      }

      public void unsetDsRules() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(DSRULES$8);
         }
      }

      public String getDxcodeTriggers() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(DXCODETRIGGERS$10);
            return var5 == null ? null : var5.getStringValue();
         }
      }

      public XmlString xgetDxcodeTriggers() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            XmlString var5 = (XmlString)this.get_store().find_attribute_user(DXCODETRIGGERS$10);
            return var5;
         }
      }

      public boolean isSetDxcodeTriggers() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(DXCODETRIGGERS$10) != null;
         }
      }

      public void setDxcodeTriggers(String var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(DXCODETRIGGERS$10);
            if (var6 == null) {
               var6 = (SimpleValue)this.get_store().add_attribute_user(DXCODETRIGGERS$10);
            }

            var6.setStringValue(var1);
         }
      }

      public void xsetDxcodeTriggers(XmlString var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            XmlString var6 = (XmlString)this.get_store().find_attribute_user(DXCODETRIGGERS$10);
            if (var6 == null) {
               var6 = (XmlString)this.get_store().add_attribute_user(DXCODETRIGGERS$10);
            }

            var6.set(var1);
         }
      }

      public void unsetDxcodeTriggers() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(DXCODETRIGGERS$10);
         }
      }

      public String getDisplayName() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(DISPLAYNAME$12);
            return var5 == null ? null : var5.getStringValue();
         }
      }

      public XmlString xgetDisplayName() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            XmlString var5 = (XmlString)this.get_store().find_attribute_user(DISPLAYNAME$12);
            return var5;
         }
      }

      public boolean isSetDisplayName() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(DISPLAYNAME$12) != null;
         }
      }

      public void setDisplayName(String var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(DISPLAYNAME$12);
            if (var6 == null) {
               var6 = (SimpleValue)this.get_store().add_attribute_user(DISPLAYNAME$12);
            }

            var6.setStringValue(var1);
         }
      }

      public void xsetDisplayName(XmlString var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            XmlString var6 = (XmlString)this.get_store().find_attribute_user(DISPLAYNAME$12);
            if (var6 == null) {
               var6 = (XmlString)this.get_store().add_attribute_user(DISPLAYNAME$12);
            }

            var6.set(var1);
         }
      }

      public void unsetDisplayName() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(DISPLAYNAME$12);
         }
      }

      public String getWarningColour() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(WARNINGCOLOUR$14);
            return var5 == null ? null : var5.getStringValue();
         }
      }

      public XmlString xgetWarningColour() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            XmlString var5 = (XmlString)this.get_store().find_attribute_user(WARNINGCOLOUR$14);
            return var5;
         }
      }

      public boolean isSetWarningColour() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(WARNINGCOLOUR$14) != null;
         }
      }

      public void setWarningColour(String var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(WARNINGCOLOUR$14);
            if (var6 == null) {
               var6 = (SimpleValue)this.get_store().add_attribute_user(WARNINGCOLOUR$14);
            }

            var6.setStringValue(var1);
         }
      }

      public void xsetWarningColour(XmlString var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            XmlString var6 = (XmlString)this.get_store().find_attribute_user(WARNINGCOLOUR$14);
            if (var6 == null) {
               var6 = (XmlString)this.get_store().add_attribute_user(WARNINGCOLOUR$14);
            }

            var6.set(var1);
         }
      }

      public void unsetWarningColour() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(WARNINGCOLOUR$14);
         }
      }

      public String getRecommendationColour() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(RECOMMENDATIONCOLOUR$16);
            return var5 == null ? null : var5.getStringValue();
         }
      }

      public XmlString xgetRecommendationColour() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            XmlString var5 = (XmlString)this.get_store().find_attribute_user(RECOMMENDATIONCOLOUR$16);
            return var5;
         }
      }

      public boolean isSetRecommendationColour() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(RECOMMENDATIONCOLOUR$16) != null;
         }
      }

      public void setRecommendationColour(String var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(RECOMMENDATIONCOLOUR$16);
            if (var6 == null) {
               var6 = (SimpleValue)this.get_store().add_attribute_user(RECOMMENDATIONCOLOUR$16);
            }

            var6.setStringValue(var1);
         }
      }

      public void xsetRecommendationColour(XmlString var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            XmlString var6 = (XmlString)this.get_store().find_attribute_user(RECOMMENDATIONCOLOUR$16);
            if (var6 == null) {
               var6 = (XmlString)this.get_store().add_attribute_user(RECOMMENDATIONCOLOUR$16);
            }

            var6.set(var1);
         }
      }

      public void unsetRecommendationColour() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(RECOMMENDATIONCOLOUR$16);
         }
      }

      public String getTopHTML() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(TOPHTML$18);
            return var5 == null ? null : var5.getStringValue();
         }
      }

      public XmlString xgetTopHTML() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var2 = null;
            XmlString var5 = (XmlString)this.get_store().find_attribute_user(TOPHTML$18);
            return var5;
         }
      }

      public boolean isSetTopHTML() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            return this.get_store().find_attribute_user(TOPHTML$18) != null;
         }
      }

      public void setTopHTML(String var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(TOPHTML$18);
            if (var6 == null) {
               var6 = (SimpleValue)this.get_store().add_attribute_user(TOPHTML$18);
            }

            var6.setStringValue(var1);
         }
      }

      public void xsetTopHTML(XmlString var1) {
         synchronized(this.monitor()) {
            this.check_orphaned();
            Object var3 = null;
            XmlString var6 = (XmlString)this.get_store().find_attribute_user(TOPHTML$18);
            if (var6 == null) {
               var6 = (XmlString)this.get_store().add_attribute_user(TOPHTML$18);
            }

            var6.set(var1);
         }
      }

      public void unsetTopHTML() {
         synchronized(this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_attribute(TOPHTML$18);
         }
      }

      public static class HeaderImpl extends XmlComplexContentImpl implements FlowsheetDocument.Flowsheet.Header {
         private static final long serialVersionUID = 1L;
         private static final QName ITEM$0 = new QName("flowsheets.oscarehr.org", "item");
         private static final QName DISPLAYNAME$2 = new QName("", "display_name");

         public HeaderImpl(SchemaType var1) {
            super(var1);
         }

         public FlowsheetDocument.Flowsheet.Header.Item[] getItemArray() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               ArrayList var2 = new ArrayList();
               this.get_store().find_all_element_users(ITEM$0, var2);
               FlowsheetDocument.Flowsheet.Header.Item[] var3 = new FlowsheetDocument.Flowsheet.Header.Item[var2.size()];
               var2.toArray(var3);
               return var3;
            }
         }

         public FlowsheetDocument.Flowsheet.Header.Item getItemArray(int var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               FlowsheetDocument.Flowsheet.Header.Item var3 = null;
               var3 = (FlowsheetDocument.Flowsheet.Header.Item)this.get_store().find_element_user(ITEM$0, var1);
               if (var3 == null) {
                  throw new IndexOutOfBoundsException();
               } else {
                  return var3;
               }
            }
         }

         public int sizeOfItemArray() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               return this.get_store().count_elements(ITEM$0);
            }
         }

         public void setItemArray(FlowsheetDocument.Flowsheet.Header.Item[] var1) {
            this.check_orphaned();
            this.arraySetterHelper(var1, ITEM$0);
         }

         public void setItemArray(int var1, FlowsheetDocument.Flowsheet.Header.Item var2) {
            this.generatedSetterHelperImpl(var2, ITEM$0, var1, (short)2);
         }

         public FlowsheetDocument.Flowsheet.Header.Item insertNewItem(int var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               FlowsheetDocument.Flowsheet.Header.Item var3 = null;
               var3 = (FlowsheetDocument.Flowsheet.Header.Item)this.get_store().insert_element_user(ITEM$0, var1);
               return var3;
            }
         }

         public FlowsheetDocument.Flowsheet.Header.Item addNewItem() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               FlowsheetDocument.Flowsheet.Header.Item var2 = null;
               var2 = (FlowsheetDocument.Flowsheet.Header.Item)this.get_store().add_element_user(ITEM$0);
               return var2;
            }
         }

         public void removeItem(int var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               this.get_store().remove_element(ITEM$0, var1);
            }
         }

         public String getDisplayName() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(DISPLAYNAME$2);
               return var5 == null ? null : var5.getStringValue();
            }
         }

         public XmlString xgetDisplayName() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               XmlString var5 = (XmlString)this.get_store().find_attribute_user(DISPLAYNAME$2);
               return var5;
            }
         }

         public boolean isSetDisplayName() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               return this.get_store().find_attribute_user(DISPLAYNAME$2) != null;
            }
         }

         public void setDisplayName(String var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(DISPLAYNAME$2);
               if (var6 == null) {
                  var6 = (SimpleValue)this.get_store().add_attribute_user(DISPLAYNAME$2);
               }

               var6.setStringValue(var1);
            }
         }

         public void xsetDisplayName(XmlString var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               XmlString var6 = (XmlString)this.get_store().find_attribute_user(DISPLAYNAME$2);
               if (var6 == null) {
                  var6 = (XmlString)this.get_store().add_attribute_user(DISPLAYNAME$2);
               }

               var6.set(var1);
            }
         }

         public void unsetDisplayName() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               this.get_store().remove_attribute(DISPLAYNAME$2);
            }
         }

         public static class ItemImpl extends XmlComplexContentImpl implements FlowsheetDocument.Flowsheet.Header.Item {
            private static final long serialVersionUID = 1L;
            private static final QName RULES$0 = new QName("flowsheets.oscarehr.org", "rules");
            private static final QName RULESET$2 = new QName("flowsheets.oscarehr.org", "ruleset");
            private static final QName MEASUREMENTTYPE$4 = new QName("", "measurement_type");
            private static final QName DISPLAYNAME$6 = new QName("", "display_name");
            private static final QName GUIDELINE$8 = new QName("", "guideline");
            private static final QName GRAPHABLE$10 = new QName("", "graphable");
            private static final QName VALUENAME$12 = new QName("", "value_name");
            private static final QName DSRULES$14 = new QName("", "ds_rules");
            private static final QName PREVENTIONTYPE$16 = new QName("", "prevention_type");

            public ItemImpl(SchemaType var1) {
               super(var1);
            }

            public FlowsheetDocument.Flowsheet.Header.Item.Rules getRules() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  FlowsheetDocument.Flowsheet.Header.Item.Rules var5 = (FlowsheetDocument.Flowsheet.Header.Item.Rules)this.get_store().find_element_user(RULES$0, 0);
                  return var5 == null ? null : var5;
               }
            }

            public boolean isSetRules() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().count_elements(RULES$0) != 0;
               }
            }

            public void setRules(FlowsheetDocument.Flowsheet.Header.Item.Rules var1) {
               this.generatedSetterHelperImpl(var1, RULES$0, 0, (short)1);
            }

            public FlowsheetDocument.Flowsheet.Header.Item.Rules addNewRules() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  FlowsheetDocument.Flowsheet.Header.Item.Rules var2 = null;
                  var2 = (FlowsheetDocument.Flowsheet.Header.Item.Rules)this.get_store().add_element_user(RULES$0);
                  return var2;
               }
            }

            public void unsetRules() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_element(RULES$0, 0);
               }
            }

            public FlowsheetDocument.Flowsheet.Header.Item.Ruleset getRuleset() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  FlowsheetDocument.Flowsheet.Header.Item.Ruleset var5 = (FlowsheetDocument.Flowsheet.Header.Item.Ruleset)this.get_store().find_element_user(RULESET$2, 0);
                  return var5 == null ? null : var5;
               }
            }

            public boolean isSetRuleset() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().count_elements(RULESET$2) != 0;
               }
            }

            public void setRuleset(FlowsheetDocument.Flowsheet.Header.Item.Ruleset var1) {
               this.generatedSetterHelperImpl(var1, RULESET$2, 0, (short)1);
            }

            public FlowsheetDocument.Flowsheet.Header.Item.Ruleset addNewRuleset() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  FlowsheetDocument.Flowsheet.Header.Item.Ruleset var2 = null;
                  var2 = (FlowsheetDocument.Flowsheet.Header.Item.Ruleset)this.get_store().add_element_user(RULESET$2);
                  return var2;
               }
            }

            public void unsetRuleset() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_element(RULESET$2, 0);
               }
            }

            public String getMeasurementType() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(MEASUREMENTTYPE$4);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetMeasurementType() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(MEASUREMENTTYPE$4);
                  return var5;
               }
            }

            public boolean isSetMeasurementType() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(MEASUREMENTTYPE$4) != null;
               }
            }

            public void setMeasurementType(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(MEASUREMENTTYPE$4);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(MEASUREMENTTYPE$4);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetMeasurementType(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(MEASUREMENTTYPE$4);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(MEASUREMENTTYPE$4);
                  }

                  var6.set(var1);
               }
            }

            public void unsetMeasurementType() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(MEASUREMENTTYPE$4);
               }
            }

            public String getDisplayName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(DISPLAYNAME$6);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetDisplayName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(DISPLAYNAME$6);
                  return var5;
               }
            }

            public boolean isSetDisplayName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(DISPLAYNAME$6) != null;
               }
            }

            public void setDisplayName(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(DISPLAYNAME$6);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(DISPLAYNAME$6);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetDisplayName(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(DISPLAYNAME$6);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(DISPLAYNAME$6);
                  }

                  var6.set(var1);
               }
            }

            public void unsetDisplayName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(DISPLAYNAME$6);
               }
            }

            public String getGuideline() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(GUIDELINE$8);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetGuideline() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(GUIDELINE$8);
                  return var5;
               }
            }

            public boolean isSetGuideline() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(GUIDELINE$8) != null;
               }
            }

            public void setGuideline(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(GUIDELINE$8);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(GUIDELINE$8);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetGuideline(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(GUIDELINE$8);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(GUIDELINE$8);
                  }

                  var6.set(var1);
               }
            }

            public void unsetGuideline() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(GUIDELINE$8);
               }
            }

            public String getGraphable() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(GRAPHABLE$10);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetGraphable() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(GRAPHABLE$10);
                  return var5;
               }
            }

            public boolean isSetGraphable() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(GRAPHABLE$10) != null;
               }
            }

            public void setGraphable(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(GRAPHABLE$10);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(GRAPHABLE$10);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetGraphable(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(GRAPHABLE$10);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(GRAPHABLE$10);
                  }

                  var6.set(var1);
               }
            }

            public void unsetGraphable() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(GRAPHABLE$10);
               }
            }

            public String getValueName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(VALUENAME$12);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetValueName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(VALUENAME$12);
                  return var5;
               }
            }

            public boolean isSetValueName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(VALUENAME$12) != null;
               }
            }

            public void setValueName(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(VALUENAME$12);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(VALUENAME$12);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetValueName(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(VALUENAME$12);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(VALUENAME$12);
                  }

                  var6.set(var1);
               }
            }

            public void unsetValueName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(VALUENAME$12);
               }
            }

            public String getDsRules() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(DSRULES$14);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetDsRules() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(DSRULES$14);
                  return var5;
               }
            }

            public boolean isSetDsRules() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(DSRULES$14) != null;
               }
            }

            public void setDsRules(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(DSRULES$14);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(DSRULES$14);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetDsRules(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(DSRULES$14);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(DSRULES$14);
                  }

                  var6.set(var1);
               }
            }

            public void unsetDsRules() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(DSRULES$14);
               }
            }

            public String getPreventionType() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(PREVENTIONTYPE$16);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetPreventionType() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(PREVENTIONTYPE$16);
                  return var5;
               }
            }

            public boolean isSetPreventionType() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(PREVENTIONTYPE$16) != null;
               }
            }

            public void setPreventionType(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(PREVENTIONTYPE$16);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(PREVENTIONTYPE$16);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetPreventionType(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(PREVENTIONTYPE$16);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(PREVENTIONTYPE$16);
                  }

                  var6.set(var1);
               }
            }

            public void unsetPreventionType() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(PREVENTIONTYPE$16);
               }
            }

            public static class RulesImpl extends XmlComplexContentImpl implements FlowsheetDocument.Flowsheet.Header.Item.Rules {
               private static final long serialVersionUID = 1L;
               private static final QName RECOMMENDATION$0 = new QName("flowsheets.oscarehr.org", "recommendation");

               public RulesImpl(SchemaType var1) {
                  super(var1);
               }

               public FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation[] getRecommendationArray() {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     ArrayList var2 = new ArrayList();
                     this.get_store().find_all_element_users(RECOMMENDATION$0, var2);
                     FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation[] var3 = new FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation[var2.size()];
                     var2.toArray(var3);
                     return var3;
                  }
               }

               public FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation getRecommendationArray(int var1) {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation var3 = null;
                     var3 = (FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation)this.get_store().find_element_user(RECOMMENDATION$0, var1);
                     if (var3 == null) {
                        throw new IndexOutOfBoundsException();
                     } else {
                        return var3;
                     }
                  }
               }

               public int sizeOfRecommendationArray() {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     return this.get_store().count_elements(RECOMMENDATION$0);
                  }
               }

               public void setRecommendationArray(FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation[] var1) {
                  this.check_orphaned();
                  this.arraySetterHelper(var1, RECOMMENDATION$0);
               }

               public void setRecommendationArray(int var1, FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation var2) {
                  this.generatedSetterHelperImpl(var2, RECOMMENDATION$0, var1, (short)2);
               }

               public FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation insertNewRecommendation(int var1) {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation var3 = null;
                     var3 = (FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation)this.get_store().insert_element_user(RECOMMENDATION$0, var1);
                     return var3;
                  }
               }

               public FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation addNewRecommendation() {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation var2 = null;
                     var2 = (FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation)this.get_store().add_element_user(RECOMMENDATION$0);
                     return var2;
                  }
               }

               public void removeRecommendation(int var1) {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     this.get_store().remove_element(RECOMMENDATION$0, var1);
                  }
               }

               public static class RecommendationImpl extends XmlComplexContentImpl implements FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation {
                  private static final long serialVersionUID = 1L;
                  private static final QName CONDITION$0 = new QName("flowsheets.oscarehr.org", "condition");
                  private static final QName STRENGTH$2 = new QName("", "strength");

                  public RecommendationImpl(SchemaType var1) {
                     super(var1);
                  }

                  public FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation.Condition getCondition() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        Object var2 = null;
                        FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation.Condition var5 = (FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation.Condition)this.get_store().find_element_user(CONDITION$0, 0);
                        return var5 == null ? null : var5;
                     }
                  }

                  public void setCondition(FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation.Condition var1) {
                     this.generatedSetterHelperImpl(var1, CONDITION$0, 0, (short)1);
                  }

                  public FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation.Condition addNewCondition() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation.Condition var2 = null;
                        var2 = (FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation.Condition)this.get_store().add_element_user(CONDITION$0);
                        return var2;
                     }
                  }

                  public String getStrength() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        Object var2 = null;
                        SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(STRENGTH$2);
                        return var5 == null ? null : var5.getStringValue();
                     }
                  }

                  public XmlString xgetStrength() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        Object var2 = null;
                        XmlString var5 = (XmlString)this.get_store().find_attribute_user(STRENGTH$2);
                        return var5;
                     }
                  }

                  public boolean isSetStrength() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        return this.get_store().find_attribute_user(STRENGTH$2) != null;
                     }
                  }

                  public void setStrength(String var1) {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        Object var3 = null;
                        SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(STRENGTH$2);
                        if (var6 == null) {
                           var6 = (SimpleValue)this.get_store().add_attribute_user(STRENGTH$2);
                        }

                        var6.setStringValue(var1);
                     }
                  }

                  public void xsetStrength(XmlString var1) {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        Object var3 = null;
                        XmlString var6 = (XmlString)this.get_store().find_attribute_user(STRENGTH$2);
                        if (var6 == null) {
                           var6 = (XmlString)this.get_store().add_attribute_user(STRENGTH$2);
                        }

                        var6.set(var1);
                     }
                  }

                  public void unsetStrength() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        this.get_store().remove_attribute(STRENGTH$2);
                     }
                  }

                  public static class ConditionImpl extends JavaStringHolderEx implements FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation.Condition {
                     private static final long serialVersionUID = 1L;
                     private static final QName TYPE$0 = new QName("", "type");
                     private static final QName PARAM$2 = new QName("", "param");
                     private static final QName VALUE$4 = new QName("", "value");

                     public ConditionImpl(SchemaType var1) {
                        super(var1, true);
                     }

                     protected ConditionImpl(SchemaType var1, boolean var2) {
                        super(var1, var2);
                     }

                     public String getType() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(TYPE$0);
                           return var5 == null ? null : var5.getStringValue();
                        }
                     }

                     public XmlString xgetType() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           XmlString var5 = (XmlString)this.get_store().find_attribute_user(TYPE$0);
                           return var5;
                        }
                     }

                     public boolean isSetType() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           return this.get_store().find_attribute_user(TYPE$0) != null;
                        }
                     }

                     public void setType(String var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(TYPE$0);
                           if (var6 == null) {
                              var6 = (SimpleValue)this.get_store().add_attribute_user(TYPE$0);
                           }

                           var6.setStringValue(var1);
                        }
                     }

                     public void xsetType(XmlString var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           XmlString var6 = (XmlString)this.get_store().find_attribute_user(TYPE$0);
                           if (var6 == null) {
                              var6 = (XmlString)this.get_store().add_attribute_user(TYPE$0);
                           }

                           var6.set(var1);
                        }
                     }

                     public void unsetType() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           this.get_store().remove_attribute(TYPE$0);
                        }
                     }

                     public String getParam() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(PARAM$2);
                           return var5 == null ? null : var5.getStringValue();
                        }
                     }

                     public XmlString xgetParam() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           XmlString var5 = (XmlString)this.get_store().find_attribute_user(PARAM$2);
                           return var5;
                        }
                     }

                     public boolean isSetParam() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           return this.get_store().find_attribute_user(PARAM$2) != null;
                        }
                     }

                     public void setParam(String var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(PARAM$2);
                           if (var6 == null) {
                              var6 = (SimpleValue)this.get_store().add_attribute_user(PARAM$2);
                           }

                           var6.setStringValue(var1);
                        }
                     }

                     public void xsetParam(XmlString var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           XmlString var6 = (XmlString)this.get_store().find_attribute_user(PARAM$2);
                           if (var6 == null) {
                              var6 = (XmlString)this.get_store().add_attribute_user(PARAM$2);
                           }

                           var6.set(var1);
                        }
                     }

                     public void unsetParam() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           this.get_store().remove_attribute(PARAM$2);
                        }
                     }

                     public String getValue() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(VALUE$4);
                           return var5 == null ? null : var5.getStringValue();
                        }
                     }

                     public XmlString xgetValue() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           XmlString var5 = (XmlString)this.get_store().find_attribute_user(VALUE$4);
                           return var5;
                        }
                     }

                     public boolean isSetValue() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           return this.get_store().find_attribute_user(VALUE$4) != null;
                        }
                     }

                     public void setValue(String var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(VALUE$4);
                           if (var6 == null) {
                              var6 = (SimpleValue)this.get_store().add_attribute_user(VALUE$4);
                           }

                           var6.setStringValue(var1);
                        }
                     }

                     public void xsetValue(XmlString var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           XmlString var6 = (XmlString)this.get_store().find_attribute_user(VALUE$4);
                           if (var6 == null) {
                              var6 = (XmlString)this.get_store().add_attribute_user(VALUE$4);
                           }

                           var6.set(var1);
                        }
                     }

                     public void unsetValue() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           this.get_store().remove_attribute(VALUE$4);
                        }
                     }
                  }
               }
            }

            public static class RulesetImpl extends XmlComplexContentImpl implements FlowsheetDocument.Flowsheet.Header.Item.Ruleset {
               private static final long serialVersionUID = 1L;
               private static final QName RULE$0 = new QName("flowsheets.oscarehr.org", "rule");

               public RulesetImpl(SchemaType var1) {
                  super(var1);
               }

               public FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule[] getRuleArray() {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     ArrayList var2 = new ArrayList();
                     this.get_store().find_all_element_users(RULE$0, var2);
                     FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule[] var3 = new FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule[var2.size()];
                     var2.toArray(var3);
                     return var3;
                  }
               }

               public FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule getRuleArray(int var1) {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule var3 = null;
                     var3 = (FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule)this.get_store().find_element_user(RULE$0, var1);
                     if (var3 == null) {
                        throw new IndexOutOfBoundsException();
                     } else {
                        return var3;
                     }
                  }
               }

               public int sizeOfRuleArray() {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     return this.get_store().count_elements(RULE$0);
                  }
               }

               public void setRuleArray(FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule[] var1) {
                  this.check_orphaned();
                  this.arraySetterHelper(var1, RULE$0);
               }

               public void setRuleArray(int var1, FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule var2) {
                  this.generatedSetterHelperImpl(var2, RULE$0, var1, (short)2);
               }

               public FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule insertNewRule(int var1) {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule var3 = null;
                     var3 = (FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule)this.get_store().insert_element_user(RULE$0, var1);
                     return var3;
                  }
               }

               public FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule addNewRule() {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule var2 = null;
                     var2 = (FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule)this.get_store().add_element_user(RULE$0);
                     return var2;
                  }
               }

               public void removeRule(int var1) {
                  synchronized(this.monitor()) {
                     this.check_orphaned();
                     this.get_store().remove_element(RULE$0, var1);
                  }
               }

               public static class RuleImpl extends XmlComplexContentImpl implements FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule {
                  private static final long serialVersionUID = 1L;
                  private static final QName CONDITION$0 = new QName("flowsheets.oscarehr.org", "condition");
                  private static final QName INDICATIONCOLOR$2 = new QName("", "indicationColor");

                  public RuleImpl(SchemaType var1) {
                     super(var1);
                  }

                  public FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition[] getConditionArray() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        ArrayList var2 = new ArrayList();
                        this.get_store().find_all_element_users(CONDITION$0, var2);
                        FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition[] var3 = new FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition[var2.size()];
                        var2.toArray(var3);
                        return var3;
                     }
                  }

                  public FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition getConditionArray(int var1) {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition var3 = null;
                        var3 = (FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition)this.get_store().find_element_user(CONDITION$0, var1);
                        if (var3 == null) {
                           throw new IndexOutOfBoundsException();
                        } else {
                           return var3;
                        }
                     }
                  }

                  public int sizeOfConditionArray() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        return this.get_store().count_elements(CONDITION$0);
                     }
                  }

                  public void setConditionArray(FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition[] var1) {
                     this.check_orphaned();
                     this.arraySetterHelper(var1, CONDITION$0);
                  }

                  public void setConditionArray(int var1, FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition var2) {
                     this.generatedSetterHelperImpl(var2, CONDITION$0, var1, (short)2);
                  }

                  public FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition insertNewCondition(int var1) {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition var3 = null;
                        var3 = (FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition)this.get_store().insert_element_user(CONDITION$0, var1);
                        return var3;
                     }
                  }

                  public FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition addNewCondition() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition var2 = null;
                        var2 = (FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition)this.get_store().add_element_user(CONDITION$0);
                        return var2;
                     }
                  }

                  public void removeCondition(int var1) {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        this.get_store().remove_element(CONDITION$0, var1);
                     }
                  }

                  public String getIndicationColor() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        Object var2 = null;
                        SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(INDICATIONCOLOR$2);
                        return var5 == null ? null : var5.getStringValue();
                     }
                  }

                  public XmlString xgetIndicationColor() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        Object var2 = null;
                        XmlString var5 = (XmlString)this.get_store().find_attribute_user(INDICATIONCOLOR$2);
                        return var5;
                     }
                  }

                  public boolean isSetIndicationColor() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        return this.get_store().find_attribute_user(INDICATIONCOLOR$2) != null;
                     }
                  }

                  public void setIndicationColor(String var1) {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        Object var3 = null;
                        SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(INDICATIONCOLOR$2);
                        if (var6 == null) {
                           var6 = (SimpleValue)this.get_store().add_attribute_user(INDICATIONCOLOR$2);
                        }

                        var6.setStringValue(var1);
                     }
                  }

                  public void xsetIndicationColor(XmlString var1) {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        Object var3 = null;
                        XmlString var6 = (XmlString)this.get_store().find_attribute_user(INDICATIONCOLOR$2);
                        if (var6 == null) {
                           var6 = (XmlString)this.get_store().add_attribute_user(INDICATIONCOLOR$2);
                        }

                        var6.set(var1);
                     }
                  }

                  public void unsetIndicationColor() {
                     synchronized(this.monitor()) {
                        this.check_orphaned();
                        this.get_store().remove_attribute(INDICATIONCOLOR$2);
                     }
                  }

                  public static class ConditionImpl extends JavaStringHolderEx implements FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition {
                     private static final long serialVersionUID = 1L;
                     private static final QName TYPE$0 = new QName("", "type");
                     private static final QName PARAM$2 = new QName("", "param");
                     private static final QName VALUE$4 = new QName("", "value");

                     public ConditionImpl(SchemaType var1) {
                        super(var1, true);
                     }

                     protected ConditionImpl(SchemaType var1, boolean var2) {
                        super(var1, var2);
                     }

                     public String getType() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(TYPE$0);
                           return var5 == null ? null : var5.getStringValue();
                        }
                     }

                     public XmlString xgetType() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           XmlString var5 = (XmlString)this.get_store().find_attribute_user(TYPE$0);
                           return var5;
                        }
                     }

                     public boolean isSetType() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           return this.get_store().find_attribute_user(TYPE$0) != null;
                        }
                     }

                     public void setType(String var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(TYPE$0);
                           if (var6 == null) {
                              var6 = (SimpleValue)this.get_store().add_attribute_user(TYPE$0);
                           }

                           var6.setStringValue(var1);
                        }
                     }

                     public void xsetType(XmlString var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           XmlString var6 = (XmlString)this.get_store().find_attribute_user(TYPE$0);
                           if (var6 == null) {
                              var6 = (XmlString)this.get_store().add_attribute_user(TYPE$0);
                           }

                           var6.set(var1);
                        }
                     }

                     public void unsetType() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           this.get_store().remove_attribute(TYPE$0);
                        }
                     }

                     public String getParam() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(PARAM$2);
                           return var5 == null ? null : var5.getStringValue();
                        }
                     }

                     public XmlString xgetParam() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           XmlString var5 = (XmlString)this.get_store().find_attribute_user(PARAM$2);
                           return var5;
                        }
                     }

                     public boolean isSetParam() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           return this.get_store().find_attribute_user(PARAM$2) != null;
                        }
                     }

                     public void setParam(String var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(PARAM$2);
                           if (var6 == null) {
                              var6 = (SimpleValue)this.get_store().add_attribute_user(PARAM$2);
                           }

                           var6.setStringValue(var1);
                        }
                     }

                     public void xsetParam(XmlString var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           XmlString var6 = (XmlString)this.get_store().find_attribute_user(PARAM$2);
                           if (var6 == null) {
                              var6 = (XmlString)this.get_store().add_attribute_user(PARAM$2);
                           }

                           var6.set(var1);
                        }
                     }

                     public void unsetParam() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           this.get_store().remove_attribute(PARAM$2);
                        }
                     }

                     public String getValue() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(VALUE$4);
                           return var5 == null ? null : var5.getStringValue();
                        }
                     }

                     public XmlString xgetValue() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var2 = null;
                           XmlString var5 = (XmlString)this.get_store().find_attribute_user(VALUE$4);
                           return var5;
                        }
                     }

                     public boolean isSetValue() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           return this.get_store().find_attribute_user(VALUE$4) != null;
                        }
                     }

                     public void setValue(String var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(VALUE$4);
                           if (var6 == null) {
                              var6 = (SimpleValue)this.get_store().add_attribute_user(VALUE$4);
                           }

                           var6.setStringValue(var1);
                        }
                     }

                     public void xsetValue(XmlString var1) {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           Object var3 = null;
                           XmlString var6 = (XmlString)this.get_store().find_attribute_user(VALUE$4);
                           if (var6 == null) {
                              var6 = (XmlString)this.get_store().add_attribute_user(VALUE$4);
                           }

                           var6.set(var1);
                        }
                     }

                     public void unsetValue() {
                        synchronized(this.monitor()) {
                           this.check_orphaned();
                           this.get_store().remove_attribute(VALUE$4);
                        }
                     }
                  }
               }
            }
         }
      }

      public static class IndicatorImpl extends JavaStringHolderEx implements FlowsheetDocument.Flowsheet.Indicator {
         private static final long serialVersionUID = 1L;
         private static final QName KEY$0 = new QName("", "key");
         private static final QName COLOUR$2 = new QName("", "colour");

         public IndicatorImpl(SchemaType var1) {
            super(var1, true);
         }

         protected IndicatorImpl(SchemaType var1, boolean var2) {
            super(var1, var2);
         }

         public String getKey() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(KEY$0);
               return var5 == null ? null : var5.getStringValue();
            }
         }

         public XmlString xgetKey() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               XmlString var5 = (XmlString)this.get_store().find_attribute_user(KEY$0);
               return var5;
            }
         }

         public boolean isSetKey() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               return this.get_store().find_attribute_user(KEY$0) != null;
            }
         }

         public void setKey(String var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(KEY$0);
               if (var6 == null) {
                  var6 = (SimpleValue)this.get_store().add_attribute_user(KEY$0);
               }

               var6.setStringValue(var1);
            }
         }

         public void xsetKey(XmlString var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               XmlString var6 = (XmlString)this.get_store().find_attribute_user(KEY$0);
               if (var6 == null) {
                  var6 = (XmlString)this.get_store().add_attribute_user(KEY$0);
               }

               var6.set(var1);
            }
         }

         public void unsetKey() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               this.get_store().remove_attribute(KEY$0);
            }
         }

         public String getColour() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(COLOUR$2);
               return var5 == null ? null : var5.getStringValue();
            }
         }

         public XmlString xgetColour() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               XmlString var5 = (XmlString)this.get_store().find_attribute_user(COLOUR$2);
               return var5;
            }
         }

         public boolean isSetColour() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               return this.get_store().find_attribute_user(COLOUR$2) != null;
            }
         }

         public void setColour(String var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(COLOUR$2);
               if (var6 == null) {
                  var6 = (SimpleValue)this.get_store().add_attribute_user(COLOUR$2);
               }

               var6.setStringValue(var1);
            }
         }

         public void xsetColour(XmlString var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               XmlString var6 = (XmlString)this.get_store().find_attribute_user(COLOUR$2);
               if (var6 == null) {
                  var6 = (XmlString)this.get_store().add_attribute_user(COLOUR$2);
               }

               var6.set(var1);
            }
         }

         public void unsetColour() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               this.get_store().remove_attribute(COLOUR$2);
            }
         }
      }

      public static class MeasurementImpl extends XmlComplexContentImpl implements FlowsheetDocument.Flowsheet.Measurement {
         private static final long serialVersionUID = 1L;
         private static final QName VALIDATIONRULE$0 = new QName("flowsheets.oscarehr.org", "validationRule");
         private static final QName TYPE$2 = new QName("", "type");
         private static final QName TYPEDESC$4 = new QName("", "typeDesc");
         private static final QName TYPEDISPLAYNAME$6 = new QName("", "typeDisplayName");
         private static final QName MEASURINGINSTRC$8 = new QName("", "measuringInstrc");

         public MeasurementImpl(SchemaType var1) {
            super(var1);
         }

         public FlowsheetDocument.Flowsheet.Measurement.ValidationRule getValidationRule() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               FlowsheetDocument.Flowsheet.Measurement.ValidationRule var5 = (FlowsheetDocument.Flowsheet.Measurement.ValidationRule)this.get_store().find_element_user(VALIDATIONRULE$0, 0);
               return var5 == null ? null : var5;
            }
         }

         public void setValidationRule(FlowsheetDocument.Flowsheet.Measurement.ValidationRule var1) {
            this.generatedSetterHelperImpl(var1, VALIDATIONRULE$0, 0, (short)1);
         }

         public FlowsheetDocument.Flowsheet.Measurement.ValidationRule addNewValidationRule() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               FlowsheetDocument.Flowsheet.Measurement.ValidationRule var2 = null;
               var2 = (FlowsheetDocument.Flowsheet.Measurement.ValidationRule)this.get_store().add_element_user(VALIDATIONRULE$0);
               return var2;
            }
         }

         public String getType() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(TYPE$2);
               return var5 == null ? null : var5.getStringValue();
            }
         }

         public XmlString xgetType() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               XmlString var5 = (XmlString)this.get_store().find_attribute_user(TYPE$2);
               return var5;
            }
         }

         public boolean isSetType() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               return this.get_store().find_attribute_user(TYPE$2) != null;
            }
         }

         public void setType(String var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(TYPE$2);
               if (var6 == null) {
                  var6 = (SimpleValue)this.get_store().add_attribute_user(TYPE$2);
               }

               var6.setStringValue(var1);
            }
         }

         public void xsetType(XmlString var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               XmlString var6 = (XmlString)this.get_store().find_attribute_user(TYPE$2);
               if (var6 == null) {
                  var6 = (XmlString)this.get_store().add_attribute_user(TYPE$2);
               }

               var6.set(var1);
            }
         }

         public void unsetType() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               this.get_store().remove_attribute(TYPE$2);
            }
         }

         public String getTypeDesc() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(TYPEDESC$4);
               return var5 == null ? null : var5.getStringValue();
            }
         }

         public XmlString xgetTypeDesc() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               XmlString var5 = (XmlString)this.get_store().find_attribute_user(TYPEDESC$4);
               return var5;
            }
         }

         public boolean isSetTypeDesc() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               return this.get_store().find_attribute_user(TYPEDESC$4) != null;
            }
         }

         public void setTypeDesc(String var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(TYPEDESC$4);
               if (var6 == null) {
                  var6 = (SimpleValue)this.get_store().add_attribute_user(TYPEDESC$4);
               }

               var6.setStringValue(var1);
            }
         }

         public void xsetTypeDesc(XmlString var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               XmlString var6 = (XmlString)this.get_store().find_attribute_user(TYPEDESC$4);
               if (var6 == null) {
                  var6 = (XmlString)this.get_store().add_attribute_user(TYPEDESC$4);
               }

               var6.set(var1);
            }
         }

         public void unsetTypeDesc() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               this.get_store().remove_attribute(TYPEDESC$4);
            }
         }

         public String getTypeDisplayName() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(TYPEDISPLAYNAME$6);
               return var5 == null ? null : var5.getStringValue();
            }
         }

         public XmlString xgetTypeDisplayName() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               XmlString var5 = (XmlString)this.get_store().find_attribute_user(TYPEDISPLAYNAME$6);
               return var5;
            }
         }

         public boolean isSetTypeDisplayName() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               return this.get_store().find_attribute_user(TYPEDISPLAYNAME$6) != null;
            }
         }

         public void setTypeDisplayName(String var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(TYPEDISPLAYNAME$6);
               if (var6 == null) {
                  var6 = (SimpleValue)this.get_store().add_attribute_user(TYPEDISPLAYNAME$6);
               }

               var6.setStringValue(var1);
            }
         }

         public void xsetTypeDisplayName(XmlString var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               XmlString var6 = (XmlString)this.get_store().find_attribute_user(TYPEDISPLAYNAME$6);
               if (var6 == null) {
                  var6 = (XmlString)this.get_store().add_attribute_user(TYPEDISPLAYNAME$6);
               }

               var6.set(var1);
            }
         }

         public void unsetTypeDisplayName() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               this.get_store().remove_attribute(TYPEDISPLAYNAME$6);
            }
         }

         public String getMeasuringInstrc() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(MEASURINGINSTRC$8);
               return var5 == null ? null : var5.getStringValue();
            }
         }

         public XmlString xgetMeasuringInstrc() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var2 = null;
               XmlString var5 = (XmlString)this.get_store().find_attribute_user(MEASURINGINSTRC$8);
               return var5;
            }
         }

         public boolean isSetMeasuringInstrc() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               return this.get_store().find_attribute_user(MEASURINGINSTRC$8) != null;
            }
         }

         public void setMeasuringInstrc(String var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(MEASURINGINSTRC$8);
               if (var6 == null) {
                  var6 = (SimpleValue)this.get_store().add_attribute_user(MEASURINGINSTRC$8);
               }

               var6.setStringValue(var1);
            }
         }

         public void xsetMeasuringInstrc(XmlString var1) {
            synchronized(this.monitor()) {
               this.check_orphaned();
               Object var3 = null;
               XmlString var6 = (XmlString)this.get_store().find_attribute_user(MEASURINGINSTRC$8);
               if (var6 == null) {
                  var6 = (XmlString)this.get_store().add_attribute_user(MEASURINGINSTRC$8);
               }

               var6.set(var1);
            }
         }

         public void unsetMeasuringInstrc() {
            synchronized(this.monitor()) {
               this.check_orphaned();
               this.get_store().remove_attribute(MEASURINGINSTRC$8);
            }
         }

         public static class ValidationRuleImpl extends JavaStringHolderEx implements FlowsheetDocument.Flowsheet.Measurement.ValidationRule {
            private static final long serialVersionUID = 1L;
            private static final QName NAME$0 = new QName("", "name");
            private static final QName REGULAREXP$2 = new QName("", "regularExp");
            private static final QName MAXVALUE$4 = new QName("", "maxValue");
            private static final QName MINVALUE$6 = new QName("", "minValue");
            private static final QName ISDATE$8 = new QName("", "isDate");
            private static final QName ISNUMERIC$10 = new QName("", "isNumeric");
            private static final QName MAXLENGTH$12 = new QName("", "maxLength");
            private static final QName MINLENGTH$14 = new QName("", "minLength");

            public ValidationRuleImpl(SchemaType var1) {
               super(var1, true);
            }

            protected ValidationRuleImpl(SchemaType var1, boolean var2) {
               super(var1, var2);
            }

            public String getName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(NAME$0);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(NAME$0);
                  return var5;
               }
            }

            public boolean isSetName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(NAME$0) != null;
               }
            }

            public void setName(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(NAME$0);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(NAME$0);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetName(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(NAME$0);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(NAME$0);
                  }

                  var6.set(var1);
               }
            }

            public void unsetName() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(NAME$0);
               }
            }

            public String getRegularExp() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(REGULAREXP$2);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetRegularExp() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(REGULAREXP$2);
                  return var5;
               }
            }

            public boolean isSetRegularExp() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(REGULAREXP$2) != null;
               }
            }

            public void setRegularExp(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(REGULAREXP$2);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(REGULAREXP$2);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetRegularExp(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(REGULAREXP$2);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(REGULAREXP$2);
                  }

                  var6.set(var1);
               }
            }

            public void unsetRegularExp() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(REGULAREXP$2);
               }
            }

            public String getMaxValue() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(MAXVALUE$4);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetMaxValue() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(MAXVALUE$4);
                  return var5;
               }
            }

            public boolean isSetMaxValue() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(MAXVALUE$4) != null;
               }
            }

            public void setMaxValue(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(MAXVALUE$4);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(MAXVALUE$4);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetMaxValue(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(MAXVALUE$4);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(MAXVALUE$4);
                  }

                  var6.set(var1);
               }
            }

            public void unsetMaxValue() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(MAXVALUE$4);
               }
            }

            public String getMinValue() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(MINVALUE$6);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetMinValue() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(MINVALUE$6);
                  return var5;
               }
            }

            public boolean isSetMinValue() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(MINVALUE$6) != null;
               }
            }

            public void setMinValue(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(MINVALUE$6);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(MINVALUE$6);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetMinValue(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(MINVALUE$6);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(MINVALUE$6);
                  }

                  var6.set(var1);
               }
            }

            public void unsetMinValue() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(MINVALUE$6);
               }
            }

            public String getIsDate() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(ISDATE$8);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetIsDate() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(ISDATE$8);
                  return var5;
               }
            }

            public boolean isSetIsDate() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(ISDATE$8) != null;
               }
            }

            public void setIsDate(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(ISDATE$8);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(ISDATE$8);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetIsDate(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(ISDATE$8);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(ISDATE$8);
                  }

                  var6.set(var1);
               }
            }

            public void unsetIsDate() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(ISDATE$8);
               }
            }

            public String getIsNumeric() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(ISNUMERIC$10);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetIsNumeric() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(ISNUMERIC$10);
                  return var5;
               }
            }

            public boolean isSetIsNumeric() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(ISNUMERIC$10) != null;
               }
            }

            public void setIsNumeric(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(ISNUMERIC$10);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(ISNUMERIC$10);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetIsNumeric(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(ISNUMERIC$10);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(ISNUMERIC$10);
                  }

                  var6.set(var1);
               }
            }

            public void unsetIsNumeric() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(ISNUMERIC$10);
               }
            }

            public String getMaxLength() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(MAXLENGTH$12);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetMaxLength() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(MAXLENGTH$12);
                  return var5;
               }
            }

            public boolean isSetMaxLength() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(MAXLENGTH$12) != null;
               }
            }

            public void setMaxLength(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(MAXLENGTH$12);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(MAXLENGTH$12);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetMaxLength(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(MAXLENGTH$12);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(MAXLENGTH$12);
                  }

                  var6.set(var1);
               }
            }

            public void unsetMaxLength() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(MAXLENGTH$12);
               }
            }

            public String getMinLength() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  SimpleValue var5 = (SimpleValue)this.get_store().find_attribute_user(MINLENGTH$14);
                  return var5 == null ? null : var5.getStringValue();
               }
            }

            public XmlString xgetMinLength() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var2 = null;
                  XmlString var5 = (XmlString)this.get_store().find_attribute_user(MINLENGTH$14);
                  return var5;
               }
            }

            public boolean isSetMinLength() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  return this.get_store().find_attribute_user(MINLENGTH$14) != null;
               }
            }

            public void setMinLength(String var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  SimpleValue var6 = (SimpleValue)this.get_store().find_attribute_user(MINLENGTH$14);
                  if (var6 == null) {
                     var6 = (SimpleValue)this.get_store().add_attribute_user(MINLENGTH$14);
                  }

                  var6.setStringValue(var1);
               }
            }

            public void xsetMinLength(XmlString var1) {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  Object var3 = null;
                  XmlString var6 = (XmlString)this.get_store().find_attribute_user(MINLENGTH$14);
                  if (var6 == null) {
                     var6 = (XmlString)this.get_store().add_attribute_user(MINLENGTH$14);
                  }

                  var6.set(var1);
               }
            }

            public void unsetMinLength() {
               synchronized(this.monitor()) {
                  this.check_orphaned();
                  this.get_store().remove_attribute(MINLENGTH$14);
               }
            }
         }
      }
   }
}
