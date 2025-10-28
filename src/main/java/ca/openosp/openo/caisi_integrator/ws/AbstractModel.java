package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractModel")
@XmlSeeAlso({ Referral.class })
public abstract class AbstractModel implements Serializable
{
    private static final long serialVersionUID = 1L;
}
