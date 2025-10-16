package ca.openosp.openo.hospitalReportManager.xsd;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(value=XmlAccessType.FIELD)
@XmlType(name="smokingPacks", propOrder={"perDay", "date"})
public class SmokingPacks {
    @XmlElement(name="PerDay", required=true)
    protected BigDecimal perDay;
    @XmlElement(name="Date", required=true)
    protected XMLGregorianCalendar date;

    public BigDecimal getPerDay() {
        return this.perDay;
    }

    public void setPerDay(BigDecimal value) {
        this.perDay = value;
    }

    public XMLGregorianCalendar getDate() {
        return this.date;
    }

    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }
}
