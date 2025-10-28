//CHECKSTYLE:OFF
package ca.openosp.openo.commn.dao;

import ca.openosp.openo.commn.model.EmailConfig;

import java.util.Collections;
import java.util.List;

public interface EmailConfigDao extends AbstractDao<EmailConfig> {
    public EmailConfig findActiveEmailConfig(EmailConfig emailConfig);

    public EmailConfig findActiveEmailConfig(String senderEmail);

    @SuppressWarnings("unchecked")
    public List<EmailConfig> fillAllActiveEmailConfigs();
}
