/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReadLab;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
@SuppressWarnings("unchecked")
public class ReadLabDao extends AbstractDao<ReadLab> {

	public ReadLabDao() {
		super(ReadLab.class);
	}

    /**
     * Searches for an existing ReadLab entry and if it does not exist, adds one
     * @param providerNo The providerNo for the readLab to search/create
     * @param labType The labType for the readLab to search/create
     * @param labId The labId for the readLab to search/create
     */
	public void markAsRead(String providerNo, String labType, Integer labId) {
        ReadLab readLab = getByProviderNoAndLabTypeAndLabId(providerNo, labType, labId);
        if (readLab == null) {
            readLab = new ReadLab(providerNo, labType, labId);
            persist(readLab);
        }
    }

    /**
     * Checks for the existance of a ReadLab record
     * @param providerNo The providerNo for the readLab to search for
     * @param labType The labType for the readLab to search for
     * @param labId The labId for the readLab to search for
     * @return true if ReadLab record exists, false otherwise
     */
    public boolean isRead(String providerNo, String labType, Integer labId) {
        return getByProviderNoAndLabTypeAndLabId(providerNo, labType, labId) != null;
    }

    /**
     * Gets a ReadLab entry based on the provided parameters
     * @param providerNo The providerNo for the readLab to search for
     * @param labType The labType for the readLab to search for
     * @param labId The labId for the readLab to search for
     * @return A ReadLab entry if found, null otherwise
     */
    public ReadLab getByProviderNoAndLabTypeAndLabId(String providerNo, String labType, Integer labId) {
        String sql = "SELECT x FROM ReadLab x WHERE x.id.providerNo = :providerNo " +
                "AND x.id.labType = :labType AND x.id.labId = :labId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("providerNo", providerNo);
        query.setParameter("labType", labType);
        query.setParameter("labId", labId);

        @SuppressWarnings("unchecked")
        List<ReadLab> results = query.getResultList();
        return (results.size() > 0) ? results.get(0) : null;
    }
}