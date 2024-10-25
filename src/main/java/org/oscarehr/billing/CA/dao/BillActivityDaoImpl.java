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

package org.oscarehr.billing.CA.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.model.BillActivity;
import org.oscarehr.common.dao.AbstractDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class BillActivityDaoImpl extends AbstractDaoImpl<BillActivity> implements BillActivityDao {

    public BillActivityDaoImpl() {
        super(BillActivity.class);
    }

    @Override
    public List<BillActivity> findCurrentByMonthCodeAndGroupNo(String monthCode, String groupNo, Date updateDateTime) {
        Query q = entityManager.createQuery(
                "SELECT b FROM BillActivity b WHERE b.monthCode=? AND b.groupNo=? AND b.updateDateTime > ? AND b.status != 'D' ORDER BY b.batchCount");
        q.setParameter(0, monthCode);
        q.setParameter(1, groupNo);
        q.setParameter(2, updateDateTime);

        @SuppressWarnings("unchecked")
        List<BillActivity> results = q.getResultList();

        return results;
    }

    @Override
    public List<BillActivity> findCurrentByDateRange(Date startDate, Date endDate) {
        Query q = entityManager.createQuery(
                "SELECT b FROM BillActivity b WHERE b.updateDateTime >= ? AND  b.updateDateTime <= ? AND b.status != 'D' ORDER BY b.id DESC");
        q.setParameter(0, startDate);
        q.setParameter(1, endDate);

        @SuppressWarnings("unchecked")
        List<BillActivity> results = q.getResultList();

        return results;
    }
}
