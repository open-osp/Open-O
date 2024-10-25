/**
 * Copyright (c) 2021 WELL EMR Group Inc. This software is made available under the terms of the GNU
 * General Public License, Version 2, 1991 (GPLv2). License details are available via
 * "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.common.dao;

import java.util.Date;

import org.hibernate.Hibernate;
import org.oscarehr.common.model.EReferAttachmentData;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.List;

@Repository
public class EReferAttachmentDataDaoImpl extends AbstractDaoImpl<EReferAttachmentData> implements EReferAttachmentDataDao {
    public EReferAttachmentDataDaoImpl() {
        super(EReferAttachmentData.class);
    }

    public EReferAttachmentData getRecentByDocumentId(Integer docId, String type, Date expiry) {
        EReferAttachmentData eReferAttachmentData = null;

        String sql = "SELECT d FROM EReferAttachmentData d WHERE d.labId = :docId AND d.labType = :labType AND d.eReferAttachment.created > :expiry AND d.eReferAttachment.archived = FALSE";

        Query query = entityManager.createQuery(sql);
        query.setParameter("docId", docId);
        query.setParameter("expiry", expiry);
        query.setParameter("labType", type);

        List<EReferAttachmentData> eReferAttachmentDataList = query.getResultList();

        if (!eReferAttachmentDataList.isEmpty()) {
            eReferAttachmentData = eReferAttachmentDataList.get(0);
            Hibernate.initialize(eReferAttachmentData.geteReferAttachment().getAttachments());
        }

        return eReferAttachmentData;
    }
}