/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *
 * Modifications made by Magenta Health in 2024.
 */

package com.quatro.dao.security;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.oscarehr.PMmodule.web.formbean.StaffForm;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.quatro.model.security.Secuserrole;

/**
 * A data access object (DAO) providing persistence and search support for
 * Secuserrole entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 *
 * @see com.quatro.model.security.Secuserrole
 * @author MyEclipse Persistence Tools
 */

public interface SecuserroleDao {
	public static final String PROVIDER_NO = "providerNo";
	public static final String ROLE_NAME = "roleName";
	public static final String ORGCD = "orgcd";
	public static final String ACTIVEYN = "activeyn";

	public void saveAll(List list);

	public void save(Secuserrole transientInstance);

	public void updateRoleName(Integer id, String roleName);

	public void delete(Secuserrole persistentInstance);

	public int deleteByOrgcd(String orgcd);

	public int deleteByProviderNo(String providerNo);

	public int deleteById(Integer id);

	public int update(Secuserrole instance);

	public Secuserrole findById(java.lang.Integer id);

	public List findByExample(Secuserrole instance);

	public List findByProperty(String propertyName, Object value);

	public List findByProviderNo(Object providerNo);

	public List findByRoleName(Object roleName);

	public List findByOrgcd(Object orgcd, boolean activeOnly);

	public List searchByCriteria(StaffForm staffForm);

	public List findByActiveyn(Object activeyn);

	public List findAll();

	public Secuserrole merge(Secuserrole detachedInstance);

	public void attachDirty(Secuserrole instance);

	public void attachClean(Secuserrole instance);
}
