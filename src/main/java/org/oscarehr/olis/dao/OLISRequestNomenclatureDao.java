/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDaoImpl;
import org.oscarehr.olis.model.OLISRequestNomenclature;
import org.springframework.stereotype.Repository;

@Repository
public class OLISRequestNomenclatureDao extends AbstractDaoImpl<OLISRequestNomenclature>{

	
	public OLISRequestNomenclatureDao() {
	    super(OLISRequestNomenclature.class);
    }

	public OLISRequestNomenclature findByNameId(String id) {
		String sql = "select x from "+ this.modelClass.getName() + " x where x.nameId=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(0, id);		
		return this.getSingleResultOrNull(query);
	}
	
	@SuppressWarnings("unchecked")
    public List<OLISRequestNomenclature> findAll() {
		String sql = "select x from " + this.modelClass.getName() + " x";
		Query query = entityManager.createQuery(sql);
		return query.getResultList();
	}
	
}
