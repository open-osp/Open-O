/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 *
 * Modifications made by Magenta Health in 2024.
 */
package org.oscarehr.common.dao;

import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.OcanConnexOption;
import org.springframework.stereotype.Repository;

@Repository
public class OcanConnexOptionDaoImpl extends AbstractDaoImpl<OcanConnexOption> implements OcanConnexOptionDao {

    public OcanConnexOptionDaoImpl() {
        super(OcanConnexOption.class);
    }

    @Override
    public List<OcanConnexOption> findByLHINCode(String LHIN_code) {
        String sqlCommand = "select x from OcanConnexOption x where x.LHINCode=?1 order by x.orgName";
        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, LHIN_code);
        @SuppressWarnings("unchecked")
        List<OcanConnexOption> results = query.getResultList();
        return (results);
    }

    @Override
    public List<OcanConnexOption> findByLHINCodeOrgName(String LHIN_code, String orgName) {
        String sqlCommand = "select x from OcanConnexOption x where x.LHINCode=?1 and x.orgName=?2 order by x.programName";
        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, LHIN_code);
        query.setParameter(2, orgName);
        @SuppressWarnings("unchecked")
        List<OcanConnexOption> results = query.getResultList();
        return (results);
    }

    @Override
    public List<OcanConnexOption> findByLHINCodeOrgNameProgramName(String LHIN_code, String orgName, String programName) {
        String sqlCommand = "select x from OcanConnexOption x where x.LHINCode=?1 and x.orgName=?2 and x.programName=?3 order by x.programName";
        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, LHIN_code);
        query.setParameter(2, orgName);
        query.setParameter(3, programName);
        @SuppressWarnings("unchecked")
        List<OcanConnexOption> results = query.getResultList();
        return (results);
    }

    @Override
    public OcanConnexOption findByID(Integer connexOptionId) {
        String sqlCommand = "select x from OcanConnexOption x where x.id=?1";
        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, connexOptionId);
        return this.getSingleResultOrNull(query);
    }
}
