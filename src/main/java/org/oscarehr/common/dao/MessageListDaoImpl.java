/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.common.dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;
import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.OscarMsgType;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class MessageListDaoImpl extends AbstractDaoImpl<MessageList> implements MessageListDao {

    public MessageListDaoImpl() {
        super(MessageList.class);
    }

    @Override
    public List<MessageList> findByProviderNoAndMessageNo(String providerNo, Long messageNo) {
        Query query = createQuery("msg", "msg.providerNo = :pno AND msg.message = :msgNo");
        query.setParameter("pno", providerNo);
        query.setParameter("msgNo", messageNo);
        return query.getResultList();
    }

    /**
     * Caution: excludes message recipients with a deleted status.
     */
    @Override
    public List<MessageList> findByProviderNoAndLocationNo(String providerNo, Integer locationNo) {
        Query query = createQuery("ml",
                "ml.providerNo = :providerNo and ml.status not like 'del' and ml.remoteLocation = :remoteLocation order by ml.message");
        query.setParameter("providerNo", providerNo);
        query.setParameter("remoteLocation", locationNo);
        return query.getResultList();
    }

    /**
     * Gets entire list of message recipients regardless of message status.
     * 
     * @param messageNo
     * @param locationNo
     * @return
     */
    @Override
    public List<MessageList> findAllByMessageNoAndLocationNo(Long messageNo, Integer locationNo) {
        Query query = createQuery("ml",
                "ml.message = :messageNo and ml.remoteLocation = :remoteLocation order by ml.message");
        query.setParameter("messageNo", messageNo);
        query.setParameter("remoteLocation", locationNo);
        return query.getResultList();
    }

    /**
     * Caution: excludes message recipients with a deleted status.
     * 
     * @param messageNo
     * @param locationNo
     * @return
     */
    @Override
    public List<MessageList> findByMessageNoAndLocationNo(Long messageNo, Integer locationNo) {
        Query query = createQuery("ml",
                "ml.message = :messageNo and ml.status not like 'del' and ml.remoteLocation = :remoteLocation order by ml.message");
        query.setParameter("messageNo", messageNo);
        query.setParameter("remoteLocation", locationNo);
        return query.getResultList();
    }

    @Override
    public List<MessageList> findByMessage(Long messageNo) {
        Query query = createQuery("ml", "ml.message = :messageNo");
        query.setParameter("messageNo", messageNo);
        return query.getResultList();
    }

    @Override
    public List<MessageList> findByProviderAndStatus(String providerNo, String status) {
        Query query = createQuery("ml", "ml.providerNo = :providerNo and ml.status = :status");
        query.setParameter("providerNo", providerNo);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<MessageList> findUnreadByProvider(String providerNo) {
        Query query = createQuery("ml", "ml.providerNo = :providerNo and ml.status ='new'");
        query.setParameter("providerNo", providerNo);
        return query.getResultList();
    }

    @Override
    public int findUnreadByProviderAndAttachedCount(String providerNo) {
        Query query = entityManager.createQuery(
                "select count(l) from MessageList l, MsgDemoMap m where l.providerNo= :providerNo and l.status='new' and l.message=m.messageID");

        query.setParameter("providerNo", providerNo);
        return getCountResult(query).intValue();

    }

    @Override
    public int countUnreadByProviderAndFromIntegratedFacility(String providerNo) {
        Query query = entityManager.createQuery(
                "SELECT count(l) FROM MessageList l, MessageTbl mt WHERE l.message = mt.id AND l.providerNo= :providerNo AND l.status='new' AND mt.type = :type");

        query.setParameter("providerNo", providerNo);
        query.setParameter("type", OscarMsgType.INTEGRATOR_TYPE);
        return getCountResult(query).intValue();
    }

    @Override
    public int countUnreadByProvider(String providerNo) {
        Query query = entityManager.createQuery(
                "select count(l) from MessageList l where l.providerNo= :providerNo and l.status='new' and l.sourceFacilityId > 0");

        query.setParameter("providerNo", providerNo);
        return getCountResult(query).intValue();
    }

    @Override
    public List<MessageList> search(String providerNo, String status, int start, int max) {

        StringBuilder sql = new StringBuilder();
        sql.append("select ml from MessageList ml, MessageTbl mt where ml.message = mt.id");

        if (providerNo != null && !providerNo.isEmpty()) {
            sql.append(" AND ml.providerNo= :providerNo ");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND ml.status = :status ");
        }

        sql.append(" ORDER BY mt.date DESC, mt.time DESC");

        Query query = entityManager.createQuery(sql.toString());

        if (providerNo != null && !providerNo.isEmpty()) {
            query.setParameter("providerNo", providerNo);
        }
        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }
        query.setFirstResult(start);
        setLimit(query, max);
        List<MessageList> result = query.getResultList();

        return result;
    }

    @Override
    public Integer searchAndReturnTotal(String providerNo, String status) {

        StringBuilder sql = new StringBuilder();
        sql.append("select count(ml) from MessageList ml, MessageTbl mt where ml.message = mt.id");

        if (providerNo != null && !providerNo.isEmpty()) {
            sql.append(" AND ml.providerNo= :providerNo ");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND ml.status = :status ");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (providerNo != null && !providerNo.isEmpty()) {
            query.setParameter("providerNo", providerNo);
        }
        if (status != null && !status.isEmpty()) {
            query.setParameter("status", status);
        }

        Integer result = ((Long) query.getSingleResult()).intValue();

        return result;
    }

    @Override
    public Integer messagesTotal(int type, String providerNo, Integer remoteLocation, String searchFilter) {

        searchFilter = "%" + searchFilter + "%";

        StringBuilder sql = new StringBuilder();
        sql.append("select count(mt) from ");

        switch (type) {
            case 1:
                // sent
                sql.append("MessageTbl mt where mt.sentByNo= :providerNo AND mt.sentByLocation = :remoteLocation ");
                break;
            case 2:
                // deleted
                sql.append(
                        "MessageList ml, MessageTbl mt where ml.status LIKE 'del' AND ml.message = mt.id AND ml.providerNo= :providerNo AND ml.remoteLocation = :remoteLocation ");
                break;
            default:
                // inbox
                sql.append(
                        "MessageList ml, MessageTbl mt where ml.status !='del' AND ml.message = mt.id AND ml.providerNo= :providerNo AND ml.remoteLocation = :remoteLocation ");
                break;
        }

        if (searchFilter != null && !searchFilter.isEmpty()) {
            sql.append(
                    " AND (mt.subject Like :filter1 OR mt.message Like :filter2 OR mt.sentBy Like :filter3 OR mt.sentTo Like :filter4)");
        }

        Query query = entityManager.createQuery(sql.toString());

        if (providerNo != null && !providerNo.isEmpty()) {
            query.setParameter("providerNo", providerNo);
        }

        if (remoteLocation != null) {
            query.setParameter("remoteLocation", remoteLocation);
        }

        if (searchFilter != null && !searchFilter.isEmpty()) {
            query.setParameter("filter1", searchFilter);
            query.setParameter("filter2", searchFilter);
            query.setParameter("filter3", searchFilter);
            query.setParameter("filter4", searchFilter);
        }

        Integer result = ((Long) query.getSingleResult()).intValue();

        return result;
    }

    @Override
    public List<MessageList> findByIntegratedFacility(int facilityId, String status) {
        Query query = createQuery("ml",
                "ml.status like :status and ml.destinationFacilityId = :destinationFacilityId order by ml.id");
        query.setParameter("destinationFacilityId", facilityId);
        query.setParameter("status", status);
        List<MessageList> results = query.getResultList();
        if (results == null) {
            results = Collections.emptyList();
        }
        return results;
    }

    @Override
    public List<MessageList> findByMessageAndIntegratedFacility(Long messageNo, int facilityId) {
        Query query = createQuery("ml",
                "ml.message = :messageNo and ml.destinationFacilityId = :destinationFacilityId order by ml.id");
        query.setParameter("messageNo", messageNo);
        query.setParameter("destinationFacilityId", facilityId);
        List<MessageList> results = query.getResultList();
        if (results == null) {
            results = Collections.emptyList();
        }
        return results;
    }

}
