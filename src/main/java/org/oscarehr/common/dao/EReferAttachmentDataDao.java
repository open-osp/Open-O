package org.oscarehr.common.dao;

import org.oscarehr.common.model.EReferAttachmentData;

import java.util.Date;

public interface EReferAttachmentDataDao extends AbstractDao<EReferAttachmentData>{
    public EReferAttachmentData getRecentByDocumentId(Integer docId, String type, Date expiry);
}
