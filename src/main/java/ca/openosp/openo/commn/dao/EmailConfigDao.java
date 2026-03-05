//CHECKSTYLE:OFF
package ca.openosp.openo.commn.dao;

import ca.openosp.openo.commn.model.EmailConfig;

import java.util.Collections;
import java.util.List;

public interface EmailConfigDao extends AbstractDao<EmailConfig> {
    public EmailConfig findActiveEmailConfig(EmailConfig emailConfig);

    public EmailConfig findActiveEmailConfig(String senderEmail);

    /**
     * Finds an active email configuration by its database ID.
     *
     * @param id int the primary key of the email configuration
     * @return EmailConfig the matching active email configuration, or null if no active config exists with this ID
     */
    public EmailConfig findActiveEmailConfigById(int id);

    @SuppressWarnings("unchecked")
    public List<EmailConfig> fillAllActiveEmailConfigs();
}
