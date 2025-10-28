//CHECKSTYLE:OFF
package ca.openosp.openo.commn.dao;

import org.hibernate.Hibernate;
import ca.openosp.openo.commn.model.EReferAttachment;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Repository
public class EReferAttachmentDaoImpl extends AbstractDaoImpl<EReferAttachment> implements EReferAttachmentDao {
    public EReferAttachmentDaoImpl() {
        super(EReferAttachment.class);
    }

    @Override
    public EReferAttachment getRecentByDemographic(Integer demographicNo, Date expiry) {
        EReferAttachment eReferAttachment = null;

        String sql = "SELECT e FROM " + modelClass.getSimpleName() + " e WHERE e.archived = FALSE AND e.demographicNo = ?1 AND e.created > ?2";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1, demographicNo);
        query.setParameter(2, expiry);

        List<EReferAttachment> eReferAttachments = query.getResultList();

        if (!eReferAttachments.isEmpty()) {
            eReferAttachment = eReferAttachments.get(0);
            Hibernate.initialize(eReferAttachment.getAttachments());
        }

        return eReferAttachment;
    }
}
