package org.oscarehr.common.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.EmailConfig;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EmailConfigDaoImpl extends AbstractDaoImpl<EmailConfig> implements EmailConfigDao {
    public EmailConfigDaoImpl() {
        super(EmailConfig.class);
    }

    @Transactional
    public EmailConfig findActiveEmailConfig(EmailConfig emailConfig) {
		Query query = entityManager.createQuery("SELECT e FROM EmailConfig e WHERE e.senderEmail = :senderEmail AND e.emailType = :emailType AND e.emailProvider = :emailProvider AND e.active = true");
		
		query.setParameter("senderEmail", emailConfig.getSenderEmail());
        query.setParameter("emailType", emailConfig.getEmailType());
        query.setParameter("emailProvider", emailConfig.getEmailProvider());
		
		return getSingleResultOrNull(query);
	}

    public EmailConfig findActiveEmailConfig(String senderEmail) {
		Query query = entityManager.createQuery("SELECT e FROM EmailConfig e WHERE e.senderEmail = :senderEmail AND e.active = true");
		query.setParameter("senderEmail", senderEmail);		
		return getSingleResultOrNull(query);
	}

    @SuppressWarnings("unchecked")
    public List<EmailConfig> fillAllActiveEmailConfigs() {
        Query query = entityManager.createQuery("SELECT e FROM EmailConfig e WHERE e.active = true");

        List<EmailConfig> emailConfigs = query.getResultList();
        if (emailConfigs == null) { emailConfigs = Collections.emptyList(); }
		return emailConfigs;
    }
    
}
