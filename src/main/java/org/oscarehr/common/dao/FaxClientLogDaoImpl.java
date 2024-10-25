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

import org.oscarehr.common.model.FaxClientLog;
import org.springframework.stereotype.Repository;

@Repository
public class FaxClientLogDaoImpl extends AbstractDaoImpl<FaxClientLog> implements FaxClientLogDao {

    public FaxClientLogDaoImpl() {
        super(FaxClientLog.class);
    }

    @Override
    public FaxClientLog findClientLogbyFaxId(int faxId) {
        Query query = entityManager.createQuery("select log from FaxClientLog log where log.faxId = :id");

        // faxId is the id for an entry in the Faxes table.
        query.setParameter("id", faxId);

        return super.getSingleResultOrNull(query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FaxClientLog> findClientLogbyFaxIds(List<Integer> faxIds) {
        if (faxIds == null || faxIds.size() == 0) {
            return Collections.emptyList();
        }

        Query query = entityManager.createNativeQuery("SELECT * FROM FaxClientLog WHERE faxId IN (:faxIds)",
                FaxClientLog.class);
        query.setParameter("faxIds", faxIds);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FaxClientLog> findClientLogbyRequestId(int requestId) {

        // only the most recent entries
        Query query = entityManager.createQuery(
                "select log from FaxClientLog log where log.requestId = :requestId order by log.startTime desc");

        // faxId is the id for an entry in the Faxes table.
        query.setParameter("requestId", requestId);
        List<FaxClientLog> results = query.getResultList();
        if (results == null) {
            results = Collections.emptyList();
        }
        return results;
    }
}
