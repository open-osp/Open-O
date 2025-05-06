//CHECKSTYLE:OFF
/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 * <p>
 * Modifications made by Magenta Health in 2024.
 */


package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.QuickList;
import org.springframework.stereotype.Repository;

@Repository
public class QuickListDaoImpl extends AbstractDaoImpl<QuickList> implements QuickListDao {

    public QuickListDaoImpl() {
        super(QuickList.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QuickList> findAll() {
        Query query = createQuery("x", null);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> findDistinct() {
        Query query = entityManager.createQuery("select distinct ql.quickListName from QuickList ql");
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QuickList> findByNameResearchCodeAndCodingSystem(String quickListName, String researchCode, String codingSystem) {
        Query query = entityManager.createQuery("from QuickList q where q.quickListName = ?1 AND q.dxResearchCode = ?2 AND q.codingSystem = ?3");
        query.setParameter(1, quickListName);
        query.setParameter(2, researchCode);
        query.setParameter(3, codingSystem);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QuickList> findByCodingSystem(String codingSystem) {
        String csQuery = "";
        if (codingSystem != null) {
            csQuery = " WHERE ql.codingSystem = ?1";
        }
        Query query = entityManager.createQuery("select ql from QuickList ql " + csQuery + " GROUP BY ql.quickListName");
        if (codingSystem != null) {
            query.setParameter(1, codingSystem);
        }
        return query.getResultList();
    }

    @NativeSql
    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findResearchCodeAndCodingSystemDescriptionByCodingSystem(String codingSystem, String quickListName) {
        try {
            // Cannot set parameter to table column name ("c."+ codingSystem).
            String sql = "Select q.dxResearchCode, c.description FROM quickList q, " + codingSystem 
                       + " c where q.codingSystem = '" + codingSystem 
                       + "' and q.quickListName= '" + quickListName + "' AND c." + codingSystem
                       + " = q.dxResearchCode order by c.description";
            Query query = entityManager.createNativeQuery(sql);
            //query.setParameter(1, codingSystem);
            //query.setParameter(2, codingSystem);
            //query.setParameter(3, quickListName);
            return query.getResultList();
        } catch (Exception e) {
            // TODO replace when test ignores are merged
            return new ArrayList<Object[]>();
        }

    }
}
