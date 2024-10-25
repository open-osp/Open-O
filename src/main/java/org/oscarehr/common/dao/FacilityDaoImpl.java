/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 *
 * Modifications made by Magenta Health in 2024.
 */
package org.oscarehr.common.dao;

import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.Facility;
import org.springframework.stereotype.Repository;

@Repository
public class FacilityDaoImpl extends AbstractDaoImpl<Facility> implements FacilityDao {

	public FacilityDaoImpl() {
		super(Facility.class);
	}

	/**
     * Find all ordered by name.
     * @param active null is find all, true is find only active, false is find only inactive.
     */
    public List<Facility> findAll(Boolean active)
	{
		StringBuilder sb=new StringBuilder();
		sb.append("select x from Facility x");
		if (active!=null) sb.append(" where x.disabled=?");
		sb.append(" order by x.name");
		
		Query query = entityManager.createQuery(sb.toString());
		if (active!=null) query.setParameter(0, !active);
		
		@SuppressWarnings("unchecked")
		List<Facility> results = query.getResultList();

		return(results);
	}

}
