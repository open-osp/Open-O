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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.TicklerLink;
import org.springframework.stereotype.Repository;

@Repository
public class TicklerLinkDaoImpl extends AbstractDaoImpl<TicklerLink> implements TicklerLinkDao {

    public TicklerLinkDaoImpl() {
        super(TicklerLink.class);
    }

    @Override
    public TicklerLink getTicklerLink(Integer id) {
        return find(id);
    }

    @Override
    public List<TicklerLink> getLinkByTableId(String tableName, Long tableId) {
        Query query = entityManager.createQuery(
                "SELECT tLink from TicklerLink tLink WHERE tLink.tableName = ? and tLink.tableId = ? order by tLink.id");
        query.setParameter(0, tableName);
        query.setParameter(1, tableId);

        @SuppressWarnings("unchecked")
        List<TicklerLink> results = query.getResultList();

        return results;
    }

    @Override
    public List<TicklerLink> getLinkByTickler(Integer ticklerNo) {
        Query query = entityManager
                .createQuery("SELECT tLink from TicklerLink tLink WHERE tLink.ticklerNo = ? order by tLink.id");
        query.setParameter(0, ticklerNo);

        @SuppressWarnings("unchecked")
        List<TicklerLink> results = query.getResultList();
        return results;
    }

    @Override
    public void save(TicklerLink cLink) {
        persist(cLink);
    }

    @Override
    public void update(TicklerLink cLink) {
        merge(cLink);
    }
}
