//CHECKSTYLE:OFF
package ca.openosp.openo.commn.dao;

import ca.openosp.openo.commn.model.EReferAttachment;

import java.util.Date;


public interface EReferAttachmentDao extends AbstractDao<EReferAttachment> {
    public EReferAttachment getRecentByDemographic(Integer demographicNo, Date expiry);
}
