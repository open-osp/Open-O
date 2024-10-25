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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Query;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.common.model.Admission;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

public interface AdmissionDao extends AbstractDao<Admission> {

    public List<Admission> getAdmissions_archiveView(Integer programId, Integer demographicNo);

    public Admission getAdmission(Integer programId, Integer demographicNo);

    public Admission getCurrentAdmission(Integer programId, Integer demographicNo);

    public List<Admission> getAdmissions();

    public List<Admission> getAdmissions(Integer demographicNo);

    public List<Admission> getAdmissionsASC(Integer demographicNo);

    public List<Admission> getAdmissionsByFacility(Integer demographicNo, Integer facilityId);

    public List<Admission> getServiceAndBedProgramAdmissions(Integer demographicNo, Integer facilityId);

    public List<Admission> getAdmissionsByProgramAndClient(Integer demographicNo, Integer programId);

    public List<Admission> getAdmissionsByProgramId(Integer programId, Boolean automaticDischarge, Integer days);

    public List<Integer> getAdmittedDemographicIdByProgramAndProvider(Integer programId, String providerNo);

    public List<Admission> getCurrentAdmissions(Integer demographicNo);

    public List<Admission> getDischargedAdmissions(Integer demographicNo);

    public List<Admission> getCurrentAdmissionsByFacility(Integer demographicNo, Integer facilityId);

    public Admission getCurrentExternalProgramAdmission(ProgramDao programDAO, Integer demographicNo);

    public Admission getCurrentBedProgramAdmission(ProgramDao programDAO, Integer demographicNo);

    public List<Admission> getCurrentServiceProgramAdmission(ProgramDao programDAO, Integer demographicNo);

    public Admission getCurrentCommunityProgramAdmission(ProgramDao programDAO, Integer demographicNo);

    public List<Admission> getCurrentAdmissionsByProgramId(Integer programId);

    public Admission getAdmission(int id);

    public Admission getAdmission(Long id);

    public void saveAdmission(Admission admission);

    public List<Admission> getAdmissionsInTeam(Integer programId, Integer teamId);

    public Admission getTemporaryAdmission(Integer demographicNo);

    public List search(AdmissionSearchBean searchBean);

    public List<Admission> getClientIdByProgramDate(int programId, Date dt);

    public Integer getLastClientStatusFromAdmissionByProgramIdAndClientId(Integer programId, Integer demographicId);

    public List<Admission> getAdmissionsByProgramAndAdmittedDate(int programId, Date startDate, Date endDate);

    public List<Admission> getAdmissionsByProgramAndDate(int programId, Date startDate, Date endDate);

    public boolean wasInProgram(Integer programId, Integer clientId);

    public List getActiveAnonymousAdmissions();

    public List<Admission> getAdmissionsByFacilitySince(Integer demographicNo, Integer facilityId, Date lastUpdateDate);

    public List<Integer> getAdmissionsByFacilitySince(Integer facilityId, Date lastUpdateDate);

    public List<Admission> findAdmissionsByProgramAndDate(Integer programNo, Date day, int startIndex, int numToReturn);

    public Integer findAdmissionsByProgramAndDateAsCount(Integer programNo, Date day);
}
