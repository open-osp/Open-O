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

import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.CaseManagementTmpSave;
import org.springframework.stereotype.Repository;

@Repository
public class CaseManagementTmpSaveDaoImpl extends AbstractDaoImpl<CaseManagementTmpSave> implements CaseManagementTmpSaveDao {

    private static final String NOTE_TAG_REGEXP = "^\\[[[:digit:]]{2}-[[:alpha:]]{3}-[[:digit:]]{4} \\.\\: [^]]*\\][[:space:]]+$";

    public CaseManagementTmpSaveDaoImpl() {
        super(CaseManagementTmpSave.class);
    }

    @Override
    public void remove(String providerNo, Integer demographicNo, Integer programId) {
        Query query = entityManager.createQuery("SELECT x FROM CaseManagementTmpSave x WHERE x.providerNo = ? and x.demographicNo=? and x.programId = ?");
        query.setParameter(0,providerNo);
        query.setParameter(1,demographicNo);
        query.setParameter(2,programId);

        @SuppressWarnings("unchecked")
        List<CaseManagementTmpSave> results = query.getResultList();

        for(CaseManagementTmpSave result : results) {
            remove(result);
        }
    }

    @Override
    public CaseManagementTmpSave find(String providerNo, Integer demographicNo, Integer programId) {
        Query query = entityManager.createQuery("SELECT x FROM CaseManagementTmpSave x WHERE x.providerNo = ? and x.demographicNo=? and x.programId = ? order by x.updateDate DESC");
        query.setParameter(0,providerNo);
        query.setParameter(1,demographicNo);
        query.setParameter(2,programId);

        return this.getSingleResultOrNull(query);
    }

    @Override
    public CaseManagementTmpSave find(String providerNo, Integer demographicNo, Integer programId, Date date) {
        Query query = entityManager.createQuery("SELECT x FROM CaseManagementTmpSave x WHERE x.providerNo = ? and x.demographicNo=? and x.programId = ? and x.updateDate >= ? order by x.updateDate DESC");
        query.setParameter(0,providerNo);
        query.setParameter(1,demographicNo);
        query.setParameter(2,programId);
        query.setParameter(3, date);

        return this.getSingleResultOrNull(query);
    }

    @Override
    public boolean noteHasContent(Integer id) {
        Query query = entityManager.createNativeQuery("SELECT * FROM casemgmt_tmpsave x WHERE x.id = ? and x.note  NOT REGEXP ? order by x.update_date DESC", CaseManagementTmpSave.class);

        query.setParameter(1, id);
        query.setParameter(2, NOTE_TAG_REGEXP);

        return (this.getSingleResultOrNull(query) != null);
    }
}
