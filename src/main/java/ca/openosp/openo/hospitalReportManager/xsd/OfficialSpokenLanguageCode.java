package ca.openosp.openo.hospitalReportManager.xsd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@XmlType(name="officialSpokenLanguageCode")
@XmlEnum
public enum OfficialSpokenLanguageCode {
    ENG,
    FRE;


    public String value() {
        return this.name();
    }

    public static OfficialSpokenLanguageCode fromValue(String v) {
        return OfficialSpokenLanguageCode.valueOf(v);
    }
}
