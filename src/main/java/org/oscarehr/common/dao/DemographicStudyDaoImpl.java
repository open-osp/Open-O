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

import org.oscarehr.common.model.DemographicStudy;
import org.oscarehr.common.model.DemographicStudyPK;

import javax.persistence.Query;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicStudyDaoImpl extends AbstractDaoImpl<DemographicStudy> implements DemographicStudyDao {

    public DemographicStudyDaoImpl() {
        super(DemographicStudy.class);
    }

    @Override
    public List<DemographicStudy> findAll() {
        Query query = createQuery("x", null);
        return query.getResultList();
    }

    @Override
    public int removeByDemographicNo(Integer demographicNo) {
        Query query = entityManager.createQuery("delete from DemographicStudy x where x.id.demographicNo=?");
        query.setParameter(0, demographicNo);
        return query.executeUpdate();
    }

    @Override
    public DemographicStudy findByDemographicNoAndStudyNo(int demographicNo, int studyNo) {
        DemographicStudyPK pk = new DemographicStudyPK();
        pk.setDemographicNo(demographicNo);
        pk.setStudyNo(studyNo);

        return find(pk);
    }

    @Override
    public List<DemographicStudy> findByStudyNo(int studyNo) {
        Query query = entityManager.createQuery("select x from DemographicStudy x where x.id.studyNo=?");
        query.setParameter(0, studyNo);
        return query.getResultList();
    }

    @Override
    public List<DemographicStudy> findByDemographicNo(int demographicNo) {
        Query query = entityManager.createQuery("select x from DemographicStudy x where x.id.demographicNo=?");
        query.setParameter(0, demographicNo);
        return query.getResultList();
    }
}
