package ca.openosp.openo.hospitalReportManager.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@XmlType(name="personNamePartQualifierCode")
@XmlEnum
public enum PersonNamePartQualifierCode {
    BR,
    SP,
    CL,
    IN;


    public String value() {
        return this.name();
    }

    public static PersonNamePartQualifierCode fromValue(String v) {
        return PersonNamePartQualifierCode.valueOf(v);
    }
}
