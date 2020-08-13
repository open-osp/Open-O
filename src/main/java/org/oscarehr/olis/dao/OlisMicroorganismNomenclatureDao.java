/**
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
 */
package org.oscarehr.olis.dao;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.olis.model.OlisMicroorganismNomenclature;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OlisMicroorganismNomenclatureDao extends AbstractDao<OlisMicroorganismNomenclature> {
    public OlisMicroorganismNomenclatureDao() {
        super(OlisMicroorganismNomenclature.class);
    }

    public Map<String, OlisMicroorganismNomenclature> findAllByMicroorganismCodes(List<String> requestCodes) {
        String sql = "SELECT x FROM " + this.modelClass.getName() + " x WHERE x.microorganismCode IN (:microorganismCodes)";
        Query q = entityManager.createQuery(sql);
        q.setParameter("microorganismCodes", requestCodes);
        List<OlisMicroorganismNomenclature> resultsList = q.getResultList();

        Map<String, OlisMicroorganismNomenclature> resultsMap = new HashMap<>();
        for (OlisMicroorganismNomenclature nomenclature: resultsList) {
            resultsMap.put(nomenclature.getMicroorganismCode(), nomenclature);
        }
        return resultsMap;
    }
    
}
