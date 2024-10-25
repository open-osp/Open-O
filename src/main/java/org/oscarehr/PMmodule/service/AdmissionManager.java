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
package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.ClientReferralDAO;
import org.oscarehr.PMmodule.dao.ProgramClientStatusDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramQueueDao;
import org.oscarehr.PMmodule.dao.VacancyDao;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.JointAdmission;
import org.oscarehr.common.model.RoomDemographic;
import org.oscarehr.managers.BedManager;
import org.oscarehr.managers.BedDemographicManager;
import org.oscarehr.managers.RoomManager;
import org.oscarehr.managers.RoomDemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public interface AdmissionManager {

    List<Admission> getAdmissions_archiveView(String programId, Integer demographicNo);
    Admission getAdmission(String programId, Integer demographicNo);
    Admission getCurrentAdmission(String programId, Integer demographicNo);
    List<Admission> getAdmissionsByFacility(Integer demographicNo, Integer facilityId);
    List<Admission> getCurrentAdmissionsByFacility(Integer demographicNo, Integer facilityId);
    List<Admission> getAdmissions();
    List<Admission> getAdmissions(Integer demographicNo);
    List<Admission> getCurrentAdmissions(Integer demographicNo);
    Admission getCurrentBedProgramAdmission(Integer demographicNo);
    List<Admission> getCurrentServiceProgramAdmission(Integer demographicNo);
    Admission getCurrentExternalProgramAdmission(Integer demographicNo);
    Admission getCurrentCommunityProgramAdmission(Integer demographicNo);
    List<Admission> getCurrentAdmissionsByProgramId(String programId);
    Admission getAdmission(Long id);
    Admission getAdmission(Integer id);
    void saveAdmission(Admission admission);
    void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes) throws ProgramFullException, AdmissionException, ServiceRestrictionException;
    void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission) throws ProgramFullException, AdmissionException, ServiceRestrictionException;
    void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission,List<Integer> dependents) throws ProgramFullException, AdmissionException, ServiceRestrictionException;
    void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission, boolean overrideRestriction) throws ProgramFullException, AdmissionException, ServiceRestrictionException;
    void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, Date admissionDate) throws ProgramFullException, AdmissionException, ServiceRestrictionException;
    void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission,List<Integer> dependents, Date admissionDate) throws ProgramFullException, AdmissionException, ServiceRestrictionException;    
    void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission, Date admissionDate, boolean overrideRestriction, List<Integer> dependents) throws ProgramFullException, AdmissionException, ServiceRestrictionException;
    void processInitialAdmission(Integer demographicNo, String providerNo, Program program, String admissionNotes, Date admissionDate) throws ProgramFullException, AlreadyAdmittedException, ServiceRestrictionException;
    Admission getTemporaryAdmission(Integer demographicNo);
    List<Admission> getCurrentTemporaryProgramAdmission(Integer demographicNo);
    boolean isDependentInDifferentProgramFromHead(Integer demographicNo, List<JointAdmission> dependentList);
    List search(AdmissionSearchBean searchBean);
    void processDischarge(Integer programId, Integer demographicNo, String dischargeNotes, String radioDischargeReason) throws AdmissionException;
    void processDischarge(Integer programId, Integer demographicNo, String dischargeNotes, String radioDischargeReason, Date dischargeDate) throws AdmissionException;
    void processDischarge(Integer programId, Integer demographicNo, String dischargeNotes, String radioDischargeReason,Date dischargeDate, List<Integer> dependents, boolean fromTransfer, boolean automaticDischarge) throws AdmissionException;
    void processDischargeToCommunity(Integer communityProgramId, Integer demographicNo, String providerNo, String notes, String radioDischargeReason,Date dischargeDate) throws AdmissionException;
    void processDischargeToCommunity(Integer communityProgramId, Integer demographicNo, String providerNo, String notes, String radioDischargeReason,List<Integer> dependents,Date dischargeDate) throws AdmissionException;
    boolean isActiveInCurrentFacility(LoggedInInfo loggedInInfo, int demographicId);
    List getActiveAnonymousAdmissions();
    boolean wasInProgram(Integer programId, Integer clientId);
    List<Admission> findAdmissionsByProgramAndDate(LoggedInInfo loggedInInfo, Integer programNo, Date day, int startIndex, int numToReturn);
    Integer findAdmissionsByProgramAndDateAsCount(LoggedInInfo loggedInInfo, Integer programNo, Date day);
}
