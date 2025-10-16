package ca.openosp.openo.hospitalReportManager.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@XmlType(name="propertyOfOffendingAgent")
@XmlEnum
public enum PropertyOfOffendingAgent {
    DR,
    ND,
    UK;


    public String value() {
        return this.name();
    }

    public static PropertyOfOffendingAgent fromValue(String v) {
        return PropertyOfOffendingAgent.valueOf(v);
    }
}
