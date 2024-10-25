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

import org.oscarehr.common.model.LookupListItem;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;

@Repository
public class LookupListItemDaoImpl extends AbstractDaoImpl<LookupListItem> implements LookupListItemDao {

    public LookupListItemDaoImpl() {
        super(LookupListItem.class);
    }
    
    @Override
    public List<LookupListItem> findActiveByLookupListId(int lookupListId) {
        return findByLookupListId(lookupListId, Boolean.TRUE);
    }

    @Override
    public List<LookupListItem> findByLookupListId(int lookupListId, boolean active) {
        Query q = entityManager.createQuery(
                "select l from LookupListItem l where l.lookupListId=? and l.active=? order by l.displayOrder");

        q.setParameter(0, lookupListId);
        q.setParameter(1, active);

        @SuppressWarnings("unchecked")
        List<LookupListItem> result = q.getResultList();

        return result;
    }

    @Override
    public LookupListItem findByLookupListIdAndValue(int lookupListId, String value) {
        Query q = entityManager.createQuery("select l from LookupListItem l where l.lookupListId=? and l.value=?");

        q.setParameter(0, lookupListId);
        q.setParameter(1, value);

        LookupListItem item = this.getSingleResultOrNull(q);

        return item;
    }

}
