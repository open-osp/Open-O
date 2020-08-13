/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis.dao;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.olis.model.OlisRemovedLabRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class OlisRemovedLabRequestDao extends AbstractDao<OlisRemovedLabRequest>{
	
	public OlisRemovedLabRequestDao() {
	    super(OlisRemovedLabRequest.class);
    }
    
    public List<String> getAccessionNumbersByProviderNo(String providerNo) {
        Query query = entityManager.createQuery("SELECT r.accessionNumber FROM " + this.modelClass.getName() + " r WHERE r.removingProvider = :providerNo GROUP BY r.accessionNumber");
        query.setParameter("providerNo", providerNo);
        return query.getResultList();
    }
    
    public List<OlisRemovedLabRequest> findByAccessionNumberAndProviderNo(String accessionNumber, String providerNo) {
        Query query = entityManager.createQuery("SELECT r FROM " + this.modelClass.getName() + " r WHERE r.accessionNumber = :accessionNumber and r.removingProvider = :providerNo");
        query.setParameter("accessionNumber", accessionNumber);
        query.setParameter("providerNo", providerNo);
        return query.getResultList();
    }
}
