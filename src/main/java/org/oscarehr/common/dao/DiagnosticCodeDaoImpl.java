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
import org.oscarehr.common.model.AbstractCodeSystemModel;
import org.oscarehr.common.model.DiagnosticCode;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class DiagnosticCodeDaoImpl extends AbstractDaoImpl<DiagnosticCode> implements DiagnosticCodeDao {

    public DiagnosticCodeDaoImpl() {
        super(DiagnosticCode.class);
    }

    
	public List<DiagnosticCode> findByDiagnosticCode(String diagnosticCode) {
		String sql = "select x from DiagnosticCode x where x.diagnosticCode=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, diagnosticCode);
		
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

	public List<DiagnosticCode> findByDiagnosticCodeAndRegion(String diagnosticCode, String region) {
		String sql = "select x from DiagnosticCode x where x.diagnosticCode=? and x.region=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, diagnosticCode);
		query.setParameter(1, region);
		
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

	public List<DiagnosticCode> search(String searchString) {
		String sql = "select x from DiagnosticCode x where x.status like 'A' and x.diagnosticCode like ? or x.description like ? order by x.diagnosticCode";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, "%"+searchString+"%");
		query.setParameter(1, "%"+searchString+"%");
		
		List<DiagnosticCode> results = query.getResultList();
		if(results == null) 
		{
			results = Collections.emptyList();
		}
		
		return results;
	}

	public List<DiagnosticCode> newSearch(String a, String b, String c, String d, String e, String f) {
		String sql = "select x from DiagnosticCode x where x.diagnosticCode like ? or x.diagnosticCode like ? or x.diagnosticCode like ? or x.description like ? or x.description like ? or x.description like ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, a);
		query.setParameter(1, b);
		query.setParameter(2, c);
		query.setParameter(3, d);
		query.setParameter(4, e);
		query.setParameter(5, f);

		
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

	public List<DiagnosticCode> searchCode(String code) {
		String sql = "select x from DiagnosticCode x where x.diagnosticCode like ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, code);

		
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

	public List<DiagnosticCode> searchText(String description) {
		String sql = "select x from DiagnosticCode x where x.description like ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, "%"+description+"%");
		
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

    public List<DiagnosticCode> getByDxCode(String dxCode){
        Query query = entityManager.createQuery("select bdx from DiagnosticCode bdx where bdx.diagnosticCode = ?");
        query.setParameter(0,dxCode);
        
        List<DiagnosticCode> results = query.getResultList();
        return results;
    }

	
    public List<DiagnosticCode> findByRegionAndType(String billRegion, String serviceType) {
		Query query = entityManager.createQuery("FROM DiagnosticCode d, CtlDiagCode c " +
				"WHERE d.id = c.diagnosticCode " +
				"AND d.region = :billRegion " +
		 		"AND c.serviceType = :serviceType");
		query.setParameter("billRegion", billRegion);
		query.setParameter("serviceType", serviceType);
		return query.getResultList();
    }
	
	public List<Object[]> findDiagnosictsAndCtlDiagCodesByServiceType(String serviceType) {
		String sql = "FROM DiagnosticCode d, CtlDiagCode c " +
				"WHERE c.diagnosticCode = d.diagnosticCode " +
				"AND c.serviceType = :serviceType " +
	            "ORDER BY d.description";
		
		Query query = entityManager.createQuery(sql);
		query.setParameter("serviceType", serviceType);
		return query.getResultList();
	}

	@Override
	public DiagnosticCode findByCode(String code) {
		List<DiagnosticCode> diagnosticCodeList = getByDxCode(code);
		if(diagnosticCodeList != null && diagnosticCodeList.size() > 0)
		{
			return diagnosticCodeList.get(0);
		}
		return null;
	}

	@Override
	public AbstractCodeSystemModel<?> findByCodingSystem(String codingSystem) {
		Query query = entityManager.createQuery("FROM DiagnosticCode d WHERE d.diagnosticCode like :cs");
		
		query.setParameter("cs", codingSystem);
		query.setMaxResults(1);
		
		return getSingleResultOrNull(query);
	}
}
