/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDaoImpl;
import org.oscarehr.common.dao.SystemPreferencesDao;
import org.oscarehr.common.model.SystemPreferences;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToProvider;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class HRMDocumentToProviderDao extends AbstractDaoImpl<HRMDocumentToProvider> {

	public HRMDocumentToProviderDao() {
		super(HRMDocumentToProvider.class);
	}

	public List<HRMDocumentToProvider> findAllUnsigned(Integer page, Integer pageSize) {
		String sql = "select x from " + this.modelClass.getName() + " x where (x.signedOff IS NULL or x.signedOff = 0)";
		Query query = entityManager.createQuery(sql);
		query.setMaxResults(pageSize);
		query.setFirstResult(page*pageSize);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public List<HRMDocumentToProvider> findByProviderNo(String providerNo, Integer page, Integer pageSize) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.providerNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, providerNo);
		query.setMaxResults(pageSize);
		query.setFirstResult(page*pageSize);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public List<HRMDocumentToProvider> findByProviderNoLimit(String providerNo, List<Integer> demographicNumbers, Date newestDate, Date oldestDate,
				Integer viewed, Integer signedOff, boolean isPaged, Integer page, Integer pageSize) {
		
		String hrmToDemographicTableName = "";
		
		StringBuilder hrmToDemographicJoinAndSearchSql = new StringBuilder();
		if (demographicNumbers != null && !demographicNumbers.isEmpty()) {
			hrmToDemographicTableName = ", HRMDocumentToDemographic d";
			hrmToDemographicJoinAndSearchSql.append(" AND x.hrmDocumentId = d.hrmDocumentId AND (");
			for (int i = 0; i < demographicNumbers.size(); i++) { //String demographicNumber : demographicNumbers) {
                hrmToDemographicJoinAndSearchSql.append("d.demographicNo = :demographicNumber").append(i);
                if (i < demographicNumbers.size() - 1) {
                    hrmToDemographicJoinAndSearchSql.append(" OR ");
                } else {
                    hrmToDemographicJoinAndSearchSql.append(") ");
                }
            }
		}

		SystemPreferencesDao systemPreferencesDao = SpringUtils.getBean(SystemPreferencesDao.class);
		String dateSearchType = "serviceObservation";

		SystemPreferences systemPreferences = systemPreferencesDao.findPreferenceByName(SystemPreferences.LAB_DISPLAY_PREFERENCE_KEYS.inboxDateSearchType);
		if (systemPreferences != null)
		{
			if (systemPreferences.getValue() != null && !systemPreferences.getValue().isEmpty())
			{
				dateSearchType = systemPreferences.getValue();
			}
		}
		
		String sql = "select x from " + this.modelClass.getName() + " x, HRMDocument h" + hrmToDemographicTableName + " where x.hrmDocumentId=h.id AND x.providerNo like ?" + hrmToDemographicJoinAndSearchSql.toString();
		if (newestDate != null)
			sql += dateSearchType.equals("receivedCreated")?" and h.timeReceived <= :newest":" and h.reportDate <= :newest";
		if (oldestDate != null)
			sql += dateSearchType.equals("receivedCreated")?" and h.timeReceived >= :oldest":" and h.reportDate >= :oldest";
		if (viewed != 2)
			sql += " and x.viewed = :viewed";
		if (signedOff != 2)
			sql += " and x.signedOff = :signedOff";

		Query query = entityManager.createQuery(sql);
		query.setParameter(0, providerNo);
		
        if (demographicNumbers != null && !demographicNumbers.isEmpty()) {
            for (int i = 0; i < demographicNumbers.size(); i++) { //String demographicNumber : demographicNumbers) {
                query.setParameter("demographicNumber" + i, demographicNumbers.get(i));
            }
        }
		if (newestDate != null)
			query.setParameter("newest", newestDate);

		if (oldestDate != null)
			query.setParameter("oldest", oldestDate);

		if (viewed != 2)
			query.setParameter("viewed", viewed);

		if (signedOff != 2)
			query.setParameter("signedOff", signedOff);
		if (isPaged)
		{
			query.setFirstResult(page * pageSize);
			query.setMaxResults((page + 1) * pageSize);
		}
		
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}


	public List<HRMDocumentToProvider> findByHrmDocumentId(Integer hrmDocumentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, hrmDocumentId);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public List<HRMDocumentToProvider> findByHrmDocumentIdNoSystemUser(Integer hrmDocumentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.providerNo != '-1'";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, hrmDocumentId);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public HRMDocumentToProvider findByHrmDocumentIdAndProviderNo(Integer hrmDocumentId, String providerNo) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.providerNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, hrmDocumentId);
		query.setParameter(1, providerNo);
		try {
			List<HRMDocumentToProvider> results = query.getResultList();
			return results.get(results.size() - 1);
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<HRMDocumentToProvider> findByHrmDocumentIdAndProviderNoList(Integer hrmDocumentId, String providerNo) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.providerNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, hrmDocumentId);
		query.setParameter(1, providerNo);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public List<HRMDocumentToProvider> findSignedByHrmDocumentId(Integer hrmDocumentId) {
		String sql = "select x from " + this.modelClass.getName() + " x where x.hrmDocumentId=? and x.signedOff=1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, hrmDocumentId);
		@SuppressWarnings("unchecked")
		List<HRMDocumentToProvider> documentToProviders = query.getResultList();
		return documentToProviders;
	}

	public Integer getCountByProviderNo(String providerNo){
		String sql = "select count(*) from " + this.modelClass.getName() + " x where x.providerNo=? and x.signedOff=0";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, providerNo);
		@SuppressWarnings("unchecked")
        Long result = (Long)query.getSingleResult();
		return result.intValue();
	}
}
