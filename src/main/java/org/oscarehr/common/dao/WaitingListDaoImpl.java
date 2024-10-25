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

import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.WaitingList;
import org.springframework.stereotype.Repository;

@Repository
public class WaitingListDaoImpl extends AbstractDaoImpl<WaitingList> implements WaitingListDao {

    public WaitingListDaoImpl() {
        super(WaitingList.class);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findByDemographic(Integer demographicNo) {
        Query query = entityManager.createQuery("FROM WaitingListName wn, WaitingList w WHERE wn.id = w.listId AND w.demographicNo = :demoNo AND w.isHistory != 'Y'");
        query.setParameter("demoNo", demographicNo);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> findWaitingListsAndDemographics(Integer listId) {
        Query query = entityManager.createQuery("FROM WaitingList w, Demographic d WHERE w.demographicNo = d.DemographicNo AND  w.listId = :listId AND w.isHistory = 'N' ORDER BY w.position");
        query.setParameter("listId", listId);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<WaitingList> findByWaitingListId(Integer listId) {
        Query query = entityManager.createQuery("FROM WaitingList w WHERE w.listId = :listId AND w.isHistory = 'N' ORDER BY w.onListSince");
        query.setParameter("listId", listId);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Appointment> findAppointmentFor(WaitingList waitingList) {
        Query query = entityManager.createQuery("From Appointment a where a.appointmentDate >= :onListSince AND a.demographicNo = :demographicNo");
        query.setParameter("onListSince", waitingList.getOnListSince());
        query.setParameter("demographicNo", waitingList.getDemographicNo());
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<WaitingList> findByWaitingListIdAndDemographicId(Integer waitingListId, Integer demographicId) {
        Query query = createQuery("wl", "wl.demographicNo = :demoNo AND wl.listId = :listId");
        query.setParameter("demoNo", demographicId);
        query.setParameter("listId", waitingListId);
        return query.getResultList();
    }

    public Integer getMaxPosition(Integer listId) {
        Query query = entityManager.createQuery("select max(w.position) from WaitingList w where w.listId = :listId AND w.isHistory = 'N'");
        query.setParameter("listId", listId);
        Long result = (Long) query.getSingleResult();
        if (result == null) {
            return 0;
        } 
        return result.intValue();
    }

    @SuppressWarnings("unchecked")
    public List<WaitingList> search_wlstatus(Integer demographicId) {
        Query query = entityManager.createQuery("select wl from WaitingList wl where wl.demographicNo = :demoNo AND wl.isHistory = 'N' order BY wl.onListSince desc");
        query.setParameter("demoNo", demographicId);
        return query.getResultList();
    }
}
