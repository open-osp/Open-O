package org.oscarehr.common.dao;
import org.hibernate.Hibernate;
import org.oscarehr.common.model.EReferAttachment;
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
		
		String sql = "SELECT e FROM " + modelClass.getSimpleName() + " e WHERE e.archived = FALSE AND e.demographicNo = :demographicNo AND e.created > :expiry";
		Query query = entityManager.createQuery(sql);
		query.setParameter("demographicNo", demographicNo);
		query.setParameter("expiry", expiry);
		
		List<EReferAttachment> eReferAttachments = query.getResultList();
		
		if (!eReferAttachments.isEmpty()) {
			eReferAttachment = eReferAttachments.get(0);
			Hibernate.initialize(eReferAttachment.getAttachments());
		}
		
		return eReferAttachment;
	}
}