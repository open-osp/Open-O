//CHECKSTYLE:OFF
package ca.openosp.openo.commn.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import ca.openosp.openo.commn.model.EmailConfig;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EmailConfigDaoImpl extends AbstractDaoImpl<EmailConfig> implements EmailConfigDao {
    public EmailConfigDaoImpl() {
        super(EmailConfig.class);
    }

    @Transactional
    public EmailConfig findActiveEmailConfig(EmailConfig emailConfig) {
        Query query = entityManager.createQuery("SELECT e FROM EmailConfig e WHERE e.senderEmail = ?1 AND e.emailType = ?2 AND e.emailType = ?3 AND e.active = true");

        query.setParameter(1, emailConfig.getSenderEmail());
        query.setParameter(2, emailConfig.getEmailType());
        query.setParameter(3, emailConfig.getEmailProvider());

        return getSingleResultOrNull(query);
    }

    public EmailConfig findActiveEmailConfig(String senderEmail) {
        Query query = entityManager.createQuery("SELECT e FROM EmailConfig e WHERE e.senderEmail = ?1 AND e.active = true");
        query.setParameter(1, senderEmail);
        return getSingleResultOrNull(query);
    }

    @SuppressWarnings("unchecked")
    public List<EmailConfig> fillAllActiveEmailConfigs() {
        Query query = entityManager.createQuery("SELECT e FROM EmailConfig e WHERE e.active = true");

        List<EmailConfig> emailConfigs = query.getResultList();
        if (emailConfigs == null) {
            emailConfigs = Collections.emptyList();
        }
        return emailConfigs;
    }

}
