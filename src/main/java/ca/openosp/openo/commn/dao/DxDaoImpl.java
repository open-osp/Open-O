//CHECKSTYLE:OFF
/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * <p>
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 * <p>
 * Modifications made by Magenta Health in 2024.
 */
package ca.openosp.openo.commn.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ca.openosp.openo.commn.NativeSql;
import ca.openosp.openo.commn.model.DxAssociation;
import ca.openosp.openo.utility.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class DxDaoImpl extends AbstractDaoImpl<DxAssociation> implements DxDao {

    public DxDaoImpl() {
        super(DxAssociation.class);
    }

    @Override
    public List<DxAssociation> findAllAssociations() {
        Query query = entityManager.createQuery("select x from DxAssociation x order by x.dxCodeType,x.dxCode");

        @SuppressWarnings("unchecked")
        List<DxAssociation> results = query.getResultList();

        return results;
    }

    @Override
    public int removeAssociations() {
        Query query = entityManager.createQuery("DELETE from DxAssociation");
        return query.executeUpdate();
    }

    @Override
    public DxAssociation findAssociation(String codeType, String code) {
        Query query = entityManager.createQuery("SELECT x from DxAssociation x where x.codeType = ?1 and x.code = ?2");
        query.setParameter(1, codeType);
        query.setParameter(2, code);

        @SuppressWarnings("unchecked")
        List<DxAssociation> results = query.getResultList();
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    @NativeSql
    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findCodingSystemDescription(String codingSystem, String code) {
        // Validate codingSystem to prevent SQL injection - only allow alphanumeric and underscore
        if (codingSystem == null || !codingSystem.matches("^[a-zA-Z0-9_]+$")) {
            MiscUtils.getLogger().warn("Invalid coding system name: " + codingSystem);
            return new ArrayList<Object[]>();
        }

        try {
            // Use parameterized query for the code value, table/column names must be validated
            String sql = "SELECT " + codingSystem + ", description FROM " + codingSystem + " WHERE " + codingSystem
                    + " = ?1";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, code);
            return query.getResultList();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error querying coding system: " + codingSystem, e);
            return new ArrayList<Object[]>();
        }
    }

    @NativeSql
    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> findCodingSystemDescription(String codingSystem, String[] keywords) {
        try {
            // Validate codingSystem to prevent SQL injection - only allow alphanumeric and underscore
            if (codingSystem == null || !codingSystem.matches("^[a-zA-Z0-9_]+$")) {
                MiscUtils.getLogger().warn("Invalid coding system name: " + codingSystem);
                return new ArrayList<Object[]>();
            }
            
            // Filter out empty keywords
            List<String> validKeywords = new ArrayList<>();
            for (String keyword : keywords) {
                if (keyword != null && !keyword.trim().isEmpty()) {
                    validKeywords.add(keyword.trim());
                }
            }
            
            if (validKeywords.isEmpty()) {
                Query query = entityManager.createNativeQuery("select " + codingSystem + ", description from " + codingSystem);
                return query.getResultList();
            }
            
            // Build parameterized query
            StringBuilder buf = new StringBuilder("select " + codingSystem + ", description from " + codingSystem + " where ");
            List<String> conditions = new ArrayList<>();
            
            for (int i = 0; i < validKeywords.size(); i++) {
                int paramIndex = i * 2 + 1;
                conditions.add("(" + codingSystem + " like ?" + paramIndex + " or description like ?" + (paramIndex + 1) + ")");
            }
            
            buf.append(String.join(" or ", conditions));
            
            Query query = entityManager.createNativeQuery(buf.toString());
            
            // Set parameters
            int paramIndex = 1;
            for (String keyword : validKeywords) {
                String likePattern = "%" + keyword + "%";
                query.setParameter(paramIndex++, likePattern);
                query.setParameter(paramIndex++, likePattern);
            }
            
            return query.getResultList();
        } catch (Exception e) {
            MiscUtils.getLogger().error("error", e);
            return new ArrayList<Object[]>();
        }

    }

    @NativeSql
    @Override
    public String getCodeDescription(String codingSystem, String code) {
        String desc = "";
        
        // Validate codingSystem to prevent SQL injection - only allow alphanumeric and underscore
        if (codingSystem == null || !codingSystem.matches("^[a-zA-Z0-9_]+$")) {
            MiscUtils.getLogger().warn("Invalid coding system name: " + codingSystem);
            return desc;
        }
        
        // Use parameterized query for the code value
        String sql = "select description from " + codingSystem + " where " + codingSystem + "=?1";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, code);
            desc = (String) query.getSingleResult();
        } catch (Exception e) {
            MiscUtils.getLogger().error("error executing query for codingSystem: " + codingSystem, e);
        }
        return desc;
    }

}
