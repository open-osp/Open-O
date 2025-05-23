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
import org.oscarehr.common.model.ScratchPad;
import org.springframework.stereotype.Repository;

@Repository
public class ScratchPadDaoImpl extends AbstractDaoImpl<ScratchPad> implements ScratchPadDao {

    public ScratchPadDaoImpl() {
        super(ScratchPad.class);
    }

    @Override
    public boolean isScratchFilled(String providerNo) {
        String sSQL = "SELECT s FROM ScratchPad s WHERE s.providerNo = ? AND status=1 order by s.id";
        Query query = entityManager.createQuery(sSQL);
        query.setParameter(0, providerNo);

        @SuppressWarnings("unchecked")
        List<ScratchPad> results = query.getResultList();
        if (results.size() > 0 && results.get(0).getText().trim().length() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public ScratchPad findByProviderNo(String providerNo) {
        Query query = createQuery("sp", "sp.providerNo = :providerNo AND sp.status=1 order by sp.id DESC");
        query.setMaxResults(1);
        query.setParameter("providerNo", providerNo);
        return getSingleResultOrNull(query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> findAllDatesByProviderNo(String providerNo) {
        String sql = "Select sp.dateTime, sp.id from ScratchPad sp where sp.providerNo = :providerNo AND sp.status=1 order by sp.dateTime DESC";
        Query query = entityManager.createQuery(sql);
        query.setParameter("providerNo", providerNo);
        return query.getResultList();
    }
}
