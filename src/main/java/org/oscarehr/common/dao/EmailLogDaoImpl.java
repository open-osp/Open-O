package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import org.oscarehr.common.model.EmailLog;

@Repository
public class EmailLogDaoImpl extends AbstractDaoImpl<EmailLog> implements EmailLogDao{

    public EmailLogDaoImpl() {
    	super(EmailLog.class);
    }

    /**
     * This method is used for retrieving email logs based on various filters such as date range, demographic number,
     * sender email address, and email status. It is called from the 'Admin > Emails > Manage Emails' page.
     *
     * @param dateBegin The start date for filtering email logs.
     * @param dateEnd The end date for filtering email logs.
     * @param demographicNo The demographic number for demographic lastname and firstname.
     * @param senderEmailAddress The sender email address for filtering email logs.
     * @param emailStatus The email status for filtering email logs.
     * @return A list of email logs that match the specified filters.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<EmailLog> getEmailStatusByDateDemographicSenderStatus(Date dateBegin, Date dateEnd, String demographicNo, String senderEmailAddress, String emailStatus) {
        String hql = "SELECT el FROM EmailLog el JOIN el.emailConfig ec JOIN el.demographic d JOIN el.provider p " +
                        "WHERE 1=1 " +
                        "AND el.demographic.DemographicNo = IFNULL(:demo, el.demographic.DemographicNo) " +
                        "AND el.status = IFNULL(:emailStatus, el.status) " +
                        "AND el.fromEmail = IFNULL(:senderEmailAddress, el.fromEmail) " +
                        "AND DATE(el.timestamp) BETWEEN DATE(:beginDate) AND DATE(:endDate) " +
                        "ORDER BY el.timestamp DESC";
    
        Query query = entityManager.createQuery(hql);
        query.setParameter("beginDate", dateBegin);
        query.setParameter("endDate", dateEnd);
        query.setParameter("demo", demographicNo);
        query.setParameter("emailStatus", emailStatus);
        query.setParameter("senderEmailAddress", senderEmailAddress);

        return query.getResultList();
    }
}
