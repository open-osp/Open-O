//CHECKSTYLE:OFF
/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web.admin;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceException;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.dao.ClientReferralDAO;
import org.oscarehr.PMmodule.dao.VacancyDao;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.BedReservedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.*;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ClientRestrictionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.VacancyTemplateManager;
import org.oscarehr.PMmodule.service.VacancyTemplateManagerImpl;
import org.oscarehr.PMmodule.web.formbean.ProgramManagerViewFormBean;
import org.oscarehr.caisi_integrator.ws.*;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Bed;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.JointAdmission;
import org.oscarehr.common.model.RoomDemographic;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.BedDemographicManager;
import org.oscarehr.managers.BedManager;
import org.oscarehr.managers.RoomDemographicManager;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Required;

import oscar.log.LogAction;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class ProgramManagerView2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static Logger logger = MiscUtils.getLogger();
    private ClientRestrictionManager clientRestrictionManager = SpringUtils.getBean(ClientRestrictionManager.class);
    private FacilityDao facilityDao = SpringUtils.getBean(FacilityDao.class);
    private CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);
    private AdmissionManager admissionManager = SpringUtils.getBean(AdmissionManager.class);
    private RoomDemographicManager roomDemographicManager = SpringUtils.getBean(RoomDemographicManager.class);
    private BedDemographicManager bedDemographicManager = SpringUtils.getBean(BedDemographicManager.class);
    private BedManager bedManager = SpringUtils.getBean(BedManager.class);
    private ClientManager clientManager = SpringUtils.getBean(ClientManager.class);
    private ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);
    private ProgramManagerAction programManagerAction = SpringUtils.getBean(ProgramManagerAction.class);
    private ProgramQueueManager programQueueManager = SpringUtils.getBean(ProgramQueueManager.class);
    private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
    private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);

    public void setFacilityDao(FacilityDao facilityDao) {
        this.facilityDao = facilityDao;
    }

    public void setProgramManagerAction(ProgramManagerAction programManagerAction) {
        this.programManagerAction = programManagerAction;
    }

    public String execute() {
        String method = request.getParameter("method");
        if ("remove_remote_queue".equals(method)) {
            return remove_remote_queue();
        } else if ("viewBedReservationChangeReport".equals(method)) {
            return viewBedReservationChangeReport();
        } else if ("viewBedCheckReport".equals(method)) {
            return viewBedCheckReport();
        } else if ("admit".equals(method)) {
            return admit();
        } else if ("override_restriction".equals(method)) {
            return override_restriction();
        } else if ("assign_team_client".equals(method)) {
            return assign_team_client();
        } else if ("assign_status_client".equals(method)) {
            return assign_status_client();
        } else if ("batch_discharge".equals(method)) {
            return batch_discharge();
        } else if ("reject_from_queue".equals(method)) {
            return reject_from_queue();
        } else if ("select_client_for_admit".equals(method)) {
            return select_client_for_admit();
        } else if ("select_client_for_reject".equals(method)) {
            return select_client_for_reject();
        } else if ("saveReservedBeds".equals(method)) {
            return saveReservedBeds();
        } else if ("switch_beds".equals(method)) {
            return switch_beds();
        }
        return view();
    }

    @SuppressWarnings("unchecked")
    public String view() {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

                // find the program id
        String programId = request.getParameter("id");

        request.getSession().setAttribute("case_program_id", programId);

        if (request.getParameter("newVacancy") != null && "true".equals(request.getParameter("newVacancy")))
            request.setAttribute("vacancyOrTemplateId", "");
        else
            request.setAttribute("vacancyOrTemplateId", this.getVacancyOrTemplateId());

        if (programId == null) {
            programId = (String) request.getAttribute("id");
        }

        String demographicNo = request.getParameter("clientId");

        if (demographicNo != null) {
            request.setAttribute("clientId", demographicNo);
        }

        request.setAttribute("temporaryAdmission", programManager.getEnabled());

        // check role permission
        HttpSession se = request.getSession();
        String providerNo = (String) se.getAttribute("user");
        se.setAttribute("performAdmissions", new Boolean(caseManagementManager.hasAccessRight("perform admissions", "access", providerNo, "", programId)));

        // check if the user is from program's staff
        request.setAttribute("userIsProgramProvider", new Boolean(programManager.getProgramProvider(providerNo, programId) != null));

        // need the queue to determine which tab to go to first
        List<ProgramQueue> queue = programQueueManager.getActiveProgramQueuesByProgramId(Long.valueOf(programId));
        request.setAttribute("queue", queue);

        if (CaisiIntegratorManager.isEnableIntegratedReferrals(loggedInInfo.getCurrentFacility())) {
            request.setAttribute("remoteQueue", getRemoteQueue(loggedInInfo, Integer.parseInt(programId)));
        }

        HashSet<Long> genderConflict = new HashSet<Long>();
        HashSet<Long> ageConflict = new HashSet<Long>();
        for (ProgramQueue programQueue : queue) {
            Demographic demographic = clientManager.getClientByDemographicNo(String.valueOf(programQueue.getClientId()));
            Program program = programManager.getProgram(programQueue.getProgramId());

            if (program.getManOrWoman() != null && demographic.getSex() != null) {
                if ("Man".equals(program.getManOrWoman()) && !"M".equals(demographic.getSex())) {
                    genderConflict.add(programQueue.getClientId());
                }
                if ("Woman".equals(program.getManOrWoman()) && !"F".equals(demographic.getSex())) {
                    genderConflict.add(programQueue.getClientId());
                }
                if ("Transgendered".equals(program.getManOrWoman()) && !"T".equals(demographic.getSex())) {
                    genderConflict.add(programQueue.getClientId());
                }
            }

            if (demographic.getAge() != null) {
                int age = Integer.parseInt(demographic.getAge());
                if (age < program.getAgeMin() || age > program.getAgeMax()) ageConflict.add(programQueue.getClientId());
            }
        }
        request.setAttribute("genderConflict", genderConflict);
        request.setAttribute("ageConflict", ageConflict);

        if (this.getTab() == null || this.getTab().equals("")) {
            if (queue != null && queue.size() > 0) {
                this.setTab("Queue");
            } else {
                this.setTab("General");
            }
        }

        Program program = programManager.getProgram(programId);
        request.setAttribute("program", program);
        Facility facility = facilityDao.find(program.getFacilityId());
        if (facility != null) request.setAttribute("facilityName", facility.getName());

        if (this.getTab().equals("Service Restrictions")) {
            request.setAttribute("service_restrictions", clientRestrictionManager.getActiveRestrictionsForProgram(Integer.valueOf(programId), new Date()));
        }
        if (this.getTab().equals("Staff")) {
            request.setAttribute("providers", programManager.getProgramProviders(programId));
        }

        if (this.getTab().equals("Function User")) {
            request.setAttribute("functional_users", programManager.getFunctionalUsers(programId));
        }

        if (this.getTab().equals("Teams")) {
            List<ProgramTeam> teams = programManager.getProgramTeams(programId);

            for (ProgramTeam team : teams) {
                team.setProviders(programManager.getAllProvidersInTeam(Integer.valueOf(programId), team.getId()));
                team.setAdmissions(programManager.getAllClientsInTeam(Integer.valueOf(programId), team.getId()));
            }

            request.setAttribute("teams", teams);
        }

        if (this.getTab().equals("Clients")) {
            request.setAttribute("client_statuses", programManager.getProgramClientStatuses(new Integer(programId)));

            // request.setAttribute("admissions", admissionManager.getCurrentAdmissionsByProgramId(programId));
            // clients should be active
            List<Admission> admissions = new ArrayList<Admission>();
            List<Admission> ads = admissionManager.getCurrentAdmissionsByProgramId(programId);
            for (Admission admission : ads) {
                Integer clientId = admission.getClientId();
                if (clientId > 0) {
                    Demographic client = clientManager.getClientByDemographicNo(Integer.toString(clientId));
                    if (client != null) {
                        String clientStatus = client.getPatientStatus();
                        if (clientStatus != null && clientStatus.equals("AC")) admissions.add(admission);
                    }
                }
            }
            request.setAttribute("admissions", admissions);

            request.setAttribute("program_name", program.getName());

            List<ProgramTeam> teams = programManager.getProgramTeams(programId);

            for (ProgramTeam team : teams) {
                team.setProviders(programManager.getAllProvidersInTeam(Integer.valueOf(programId), team.getId()));
                team.setAdmissions(programManager.getAllClientsInTeam(Integer.valueOf(programId), team.getId()));
            }

            request.setAttribute("teams", teams);

            List<Program> batchAdmissionPrograms = new ArrayList<Program>();

            for (Program bedProgram : programManager.getBedPrograms()) {
                if (bedProgram.isAllowBatchAdmission() && bedProgram.isActive()) {
                    batchAdmissionPrograms.add(bedProgram);
                }
            }

            List<Program> batchAdmissionServicePrograms = new ArrayList<Program>();
            List<Program> servicePrograms;
            servicePrograms = programManager.getServicePrograms();
            for (Program sp : servicePrograms) {
                if (sp.isAllowBatchAdmission() && sp.isActive()) {
                    batchAdmissionServicePrograms.add(sp);
                }
            }

            // request.setAttribute("programs", batchAdmissionPrograms);
            request.setAttribute("bedPrograms", batchAdmissionPrograms);
            request.setAttribute("communityPrograms", programManager.getCommunityPrograms());
            request.setAttribute("allowBatchDischarge", program.isAllowBatchDischarge());
            request.setAttribute("servicePrograms", batchAdmissionServicePrograms);
        }

        if (this.getTab().equals("Access")) {
            request.setAttribute("accesses", programManager.getProgramAccesses(programId));
        }

        if (this.getTab().equals("Bed Check")) {

            Integer[] bedClientIds = null;
            Boolean[] isFamilyDependents = null;
            JointAdmission clientsJadm = null;
            Bed[] beds = bedManager.getBedsByProgram(Integer.valueOf(programId), true);
            beds = bedManager.addFamilyIdsToBeds(clientManager, beds);
            if (beds != null && beds.length > 0) {
                bedClientIds = bedManager.getBedClientIds(beds);

                if (clientManager != null && bedClientIds != null && bedClientIds.length > 0) {
                    isFamilyDependents = new Boolean[beds.length];
                    for (int i = 0; i < bedClientIds.length; i++) {
                        clientsJadm = clientManager.getJointAdmission(Integer.valueOf(bedClientIds[i].toString()));

                        if (clientsJadm != null && clientsJadm.getHeadClientId() != null) {
                            isFamilyDependents[i] = new Boolean(true);
                        } else {
                            isFamilyDependents[i] = new Boolean(false);
                        }
                    }
                }
            }
            request.setAttribute("bedDemographicStatuses", bedDemographicManager.getBedDemographicStatuses());
            this.setReservedBeds(beds);
            request.setAttribute("isFamilyDependents", isFamilyDependents);
            request.setAttribute("communityPrograms", programManager.getCommunityPrograms());
            request.setAttribute("expiredReservations", bedDemographicManager.getExpiredReservations());
        }

        if (this.getTab().equals("Client Status")) {
            request.setAttribute("client_statuses", programManager.getProgramClientStatuses(new Integer(programId)));
        }

        LogAction.log("view", "program", programId, request);

        request.setAttribute("id", programId);

        return "view";
    }

    protected List<ProgramManagerAction.RemoteQueueEntry> getRemoteQueue(LoggedInInfo loggedInInfo, int programId) {
        try {
            DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
            ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());
            List<Referral> remoteReferrals = referralWs.getReferralsToProgram(programId);

            ArrayList<ProgramManagerAction.RemoteQueueEntry> results = new ArrayList<>();
            for (Referral remoteReferral : remoteReferrals) {
                ProgramManagerAction.RemoteQueueEntry remoteQueueEntry = new ProgramManagerAction.RemoteQueueEntry();
                //remoteQueueEntry.setReferral(remoteReferral);

                DemographicTransfer demographicTransfer = demographicWs.getDemographicByFacilityIdAndDemographicId(remoteReferral.getSourceIntegratorFacilityId(), remoteReferral.getSourceCaisiDemographicId());
                if (demographicTransfer != null) {
                    remoteQueueEntry.setClientName(demographicTransfer.getLastName() + ", " + demographicTransfer.getFirstName());
                } else {
                    remoteQueueEntry.setClientName("N/A");
                }

                FacilityIdStringCompositePk pk = new FacilityIdStringCompositePk();
                pk.setIntegratorFacilityId(remoteReferral.getSourceIntegratorFacilityId());
                pk.setCaisiItemId(remoteReferral.getSourceCaisiProviderId());
                CachedProvider cachedProvider = CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(), pk);
                if (cachedProvider != null) {
                    remoteQueueEntry.setProviderName(cachedProvider.getLastName() + ", " + cachedProvider.getFirstName());
                } else {
                    remoteQueueEntry.setProviderName("N/A");
                }

                results.add(remoteQueueEntry);
            }
            return (results);
        } catch (MalformedURLException e) {
            logger.error("Unexpected Error.", e);
        } catch (WebServiceException e) {
            logger.error("Unexpected Error.", e);
        }

        return (null);
    }

    public String remove_remote_queue() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        Integer remoteReferralId = Integer.valueOf(request.getParameter("remoteReferralId"));

        try {
            ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());
            referralWs.removeReferral(remoteReferralId);
        } catch (MalformedURLException e) {
            logger.error("Unexpected error", e);
        } catch (WebServiceException e) {
            logger.error("Unexpected error", e);
        }

        return view();
    }

    public String viewBedReservationChangeReport() {
        Integer reservedBedId = Integer.valueOf(request.getParameter("reservedBedId"));
        logger.debug(reservedBedId);

        // BedDemographicChange[] bedDemographicChanges = bedDemographicManager.getBedDemographicChanges(reservedBedId)
        request.setAttribute("bedReservationChanges", null);

        return "viewBedReservationChangeReport";
    }

    public String viewBedCheckReport() {
        Integer programId = Integer.valueOf(request.getParameter("programId"));

        Bed[] beds = bedManager.getBedsByProgram(programId, true);
        beds = bedManager.addFamilyIdsToBeds(clientManager, beds);
        request.setAttribute("reservedBeds", beds);
        return "viewBedCheckReport";
    }

    public String admit() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String programId = request.getParameter("id");
        String clientId = request.getParameter("clientId");
        String queueId = request.getParameter("queueId");

        ProgramQueue queue = programQueueManager.getProgramQueue(queueId);
        Program fullProgram = programManager.getProgram(String.valueOf(programId));
        String dischargeNotes = request.getParameter("admission.dischargeNotes");
        String admissionNotes = request.getParameter("admission.admissionNotes");
        String formattedAdmissionDate = request.getParameter("admissionDate");
        Date admissionDate = oscar.util.DateUtils.toDate(formattedAdmissionDate);
        List<Integer> dependents = clientManager.getDependentsList(new Integer(clientId));

        try {
            admissionManager.processAdmission(Integer.valueOf(clientId), loggedInInfo.getLoggedInProviderNo(), fullProgram, dischargeNotes, admissionNotes, queue.isTemporaryAdmission(), dependents, admissionDate);

            //change vacancy status to filled after one patient is admitted to associated program in that vacancy.
            Vacancy vacancy = VacancyTemplateManager.getVacancyByName(queue.getVacancyName());
            if (vacancy != null) {
                vacancy.setStatus("Filled");
                VacancyTemplateManager.saveVacancy(vacancy);
            }

            addActionMessage(getText("admit.success"));
        } catch (ProgramFullException e) {
            addActionMessage(getText("admit.full"));
        } catch (AdmissionException e) {
            addActionMessage(getText("admit.error", e.getMessage()));
            logger.error("Error", e);
        } catch (ServiceRestrictionException e) {
            addActionMessage(getText("admit.service_restricted", e.getRestriction().getComments(), e.getRestriction().getProvider().getFormattedName()));
            // store this for display
            this.setServiceRestriction(e.getRestriction());

            request.getSession().setAttribute("programId", programId);
            request.getSession().setAttribute("admission.dischargeNotes", dischargeNotes);
            request.getSession().setAttribute("admission.admissionNotes", admissionNotes);

            request.setAttribute("id", programId);

            request.setAttribute("hasOverridePermission", caseManagementManager.hasAccessRight("Service restriction override on admission", "access", loggedInInfo.getLoggedInProviderNo(), clientId, programId));

            return "service_restriction_error";
        }

        LogAction.log("view", "admit to program", clientId, request);

        return view();
    }

    public String override_restriction() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String programId = (String) request.getSession().getAttribute("programId");
        String clientId = request.getParameter("clientId");
        String queueId = request.getParameter("queueId");

        String dischargeNotes = (String) request.getSession().getAttribute("admission.dischargeNotes");
        String admissionNotes = (String) request.getSession().getAttribute("admission.admissionNotes");

        request.setAttribute("id", programId);

        if (!caseManagementManager.hasAccessRight("Service restriction override on referral", "access", loggedInInfo.getLoggedInProviderNo(), clientId, programId)) {
            return view();
        }

        ProgramQueue queue = programQueueManager.getProgramQueue(queueId);
        Program fullProgram = programManager.getProgram(String.valueOf(programId));

        try {
            admissionManager.processAdmission(Integer.valueOf(clientId), loggedInInfo.getLoggedInProviderNo(), fullProgram, dischargeNotes, admissionNotes, queue.isTemporaryAdmission(), true);
            addActionMessage(getText("admit.success"));
        } catch (ProgramFullException e) {
            addActionMessage(getText("admit.full"));
            logger.error("Error", e);
        } catch (AdmissionException e) {
            addActionMessage(getText("admit.error", e.getMessage()));
            logger.error("Error", e);
        } catch (ServiceRestrictionException e) {
            throw new RuntimeException(e);
        }

        LogAction.log("view", "override service restriction", clientId, request);

        LogAction.log("view", "admit to program", clientId, request);

        return view();
    }

    public String assign_team_client() {
        String admissionId = request.getParameter("admissionId");
        String teamId = request.getParameter("teamId");
        String programName = request.getParameter("program_name");
        Admission ad = admissionManager.getAdmission(Long.valueOf(admissionId));

        ad.setTeamId(Integer.valueOf(teamId));

        admissionManager.saveAdmission(ad);
        addActionMessage(getText("program.saved", programName));

        LogAction.log("write", "edit program - assign client to team", "", request);
        return view();
    }

    public String assign_status_client() {
        String admissionId = request.getParameter("admissionId");
        String statusId = request.getParameter("clientStatusId");
        String programName = request.getParameter("program_name");
        Admission ad = admissionManager.getAdmission(Long.valueOf(admissionId));

        ad.setClientStatusId(Integer.valueOf(statusId));

        admissionManager.saveAdmission(ad);
        addActionMessage(getText("program.saved", programName));

        LogAction.log("write", "edit program - assign client to status", "", request);
        return view();
    }

    public String batch_discharge() {
        logger.info("do batch discharge");
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String type = request.getParameter("type");
        String admitToProgramId;
        if (type != null && type.equalsIgnoreCase("community")) {
            admitToProgramId = request.getParameter("batch_discharge_community_program");
        } else if (type != null && type.equalsIgnoreCase("bed")) {
            admitToProgramId = request.getParameter("batch_discharge_program");
        } else {
            logger.warn("Invalid program type for batch discharge");
            admitToProgramId = "";
        }

        String message = "";

        // get clients
        Enumeration e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            if (name.startsWith("checked_") && request.getParameter(name).equals("on")) {
                String admissionId = name.substring(8);
                Admission admission = admissionManager.getAdmission(Long.valueOf(admissionId));
                if (admission == null) {
                    logger.warn("admission #" + admissionId + " not found.");
                    continue;
                }

                // temporary admission will not allow batach discharge from bed program.
                if (admission.isTemporaryAdmission() && "bed".equals(type)) {
                    message += admission.getClient().getFormattedName() + " is in this bed program temporarily. You cannot do batch discharge for this client!   \n";
                    continue;
                }

                // in case some clients maybe is already in the community program
                if (type != null) {
                    if (type.equals("community")) {
                        Integer clientId = admission.getClientId();

                        // if discharged program is service program,
                        // then should check if the client is in one bed program
                        /*
                         * if(program_type.equals("Service")) { Admission admission_bed_program = admissionManager.getCurrentBedProgramAdmission(clientId); if(admission_bed_program!=null){ if(!admission_bed_program.isTemporaryAdmission()){ message +=
                         * admission.getClient().getFormattedName() + " is also in the bed program. You cannot do batch discharge for this client! \n"; continue; } } }
                         */
                        // if the client is already in the community program, then cannot do batch discharge to the community program.
                        Admission admission_community_program = admissionManager.getCurrentCommunityProgramAdmission(clientId);
                        if (admission_community_program != null) {
                            message += admission.getClient().getFormattedName() + " is already in one community program. You cannot do batch discharge for this client! \n";
                            continue;
                        }
                    }
                }
                // lets see if there's room first
                if (!"service".equals(type)) {
                    Program programToAdmit = programManager.getProgram(admitToProgramId);
                    if (programToAdmit == null) {
                        message += "Admitting program not found!";
                        continue;
                    }
                    if (programToAdmit.getNumOfMembers() >= programToAdmit.getMaxAllowed()) {
                        message += "Program Full. Cannot admit " + admission.getClient().getFormattedName() + "\n";
                        continue;
                    }
                }
                admission.setDischargeDate(new Date());
                admission.setDischargeNotes("Batch discharge");
                admission.setAdmissionStatus(Admission.STATUS_DISCHARGED);
                admissionManager.saveAdmission(admission);

                // The service program can only be batch discharged, can not be admitted to another program.
                if (!"service".equals(type)) {
                    Admission newAdmission = new Admission();
                    newAdmission.setAdmissionDate(new Date());
                    newAdmission.setAdmissionNotes("Batch Admit");
                    newAdmission.setAdmissionStatus(Admission.STATUS_CURRENT);
                    newAdmission.setClientId(admission.getClientId());
                    newAdmission.setProgramId(Integer.valueOf(admitToProgramId));
                    newAdmission.setProviderNo(loggedInInfo.getLoggedInProviderNo());
//					newAdmission.setTeamId(0);

                    admissionManager.saveAdmission(newAdmission);
                }
            }
        }
        addActionMessage(getText("errors.detail", message));

        return view();
    }

    private void createWaitlistRejectionNotificationTickler(LoggedInInfo loggedInInfo, Facility facility, String clientId, Integer vacancyId, String creatorProviderNo) {
        if (vacancyId == null)
            return;
        VacancyDao vacancyDao = SpringUtils.getBean(VacancyDao.class);
        Vacancy vacancy = vacancyDao.find(vacancyId);
        if (vacancy == null)
            return;

        Demographic d = demographicDao.getDemographic(clientId);
        Tickler t = new Tickler();
        t.setCreator(creatorProviderNo);
        t.setDemographicNo(Integer.parseInt(clientId));
        t.setMessage("Client=[" + d.getFormattedName() + "] rejected from vacancy=[" + vacancy.getName() + "]");
        t.setProgramId(vacancy.getWlProgramId());
        t.setServiceDate(new Date());
        t.setTaskAssignedTo(facility.getAssignRejectedVacancyApplicant());
        t.setUpdateDate(new Date());

        ticklerManager.addTickler(loggedInInfo, t);
    }

    public String reject_from_queue() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String notes = request.getParameter("admission.admissionNotes");
        String programId = request.getParameter("id");
        String clientId = request.getParameter("clientId");
        String rejectionReason = request.getParameter("radioRejectionReason");

        List<Integer> dependents = clientManager.getDependentsList(new Integer(clientId));

        logger.debug("rejecting from queue: program_id=" + programId + ",clientId=" + clientId);

        ProgramQueue queue = this.programQueueManager.getActiveProgramQueue(programId, clientId);

        programQueueManager.rejectQueue(programId, clientId, notes, rejectionReason);

        //TODO: WL notification
        ClientReferralDAO clientReferralDao = SpringUtils.getBean(ClientReferralDAO.class);
        Facility facility = loggedInInfo.getCurrentFacility();
        if (facility.getAssignRejectedVacancyApplicant() != null && facility.getAssignRejectedVacancyApplicant().length() > 0) {
            Integer vacancyId = null;
            if (queue != null) {
                ClientReferral referral = clientReferralDao.getClientReferral(queue.getReferralId());
                if (referral != null) {
                    vacancyId = referral.getVacancyId();
                }
            }
            createWaitlistRejectionNotificationTickler(loggedInInfo, facility, clientId, vacancyId, loggedInInfo.getLoggedInProviderNo());
        }
        if (dependents != null) {
            for (Integer l : dependents) {
                logger.debug("rejecting from queue: program_id=" + programId + ",clientId=" + l.intValue());
                programQueueManager.rejectQueue(programId, l.toString(), notes, rejectionReason);
            }
        }

        return view();
    }

    public String select_client_for_admit() {
        String programId = request.getParameter("id");
        String clientId = request.getParameter("clientId");
        String queueId = request.getParameter("queueId");

        Program program = programManager.getProgram(String.valueOf(programId));
        ProgramQueue queue = programQueueManager.getProgramQueue(queueId);

        //int numMembers = program.getNumOfMembers().intValue();
        //int maxMem = program.getMaxAllowed().intValue();
        //int familySize = clientManager.getDependents(new Long(clientId)).size();
        // TODO: add warning if this admission ( w/ dependents) will exceed the maxMem

        /*
         * If the user is currently enrolled in a bed program, we must warn the provider that this will also be a discharge
         */
        if (program.getType().equalsIgnoreCase("bed") && queue != null && !queue.isTemporaryAdmission()) {
            Admission currentAdmission = admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(clientId));
            if (currentAdmission != null) {
                logger.warn("client already in a bed program..doing a discharge/admit if proceeding");
                request.setAttribute("current_admission", currentAdmission);
                Program currentProgram = programManager.getProgram(String.valueOf(currentAdmission.getProgramId()));
                request.setAttribute("current_program", currentProgram);

                request.setAttribute("sameFacility", program.getFacilityId() == currentProgram.getFacilityId());
            }
        }
        request.setAttribute("do_admit", Boolean.TRUE);

        return view();
    }

    public String select_client_for_reject() {
        request.setAttribute("do_reject", Boolean.TRUE);

        return view();
    }

    public String saveReservedBeds() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        List<Integer> familyList = new ArrayList<Integer>();
        boolean isClientDependent = false;
        boolean isClientFamilyHead = false;

        for (int i = 0; reservedBeds != null && i < reservedBeds.length; i++) {
            Bed reservedBed = reservedBeds[i];

            // detect check box false
            if (request.getParameter("reservedBeds[" + i + "].latePass") == null) {
                reservedBed.setLatePass(false);
            }
            // save bed
            try {
                BedDemographic bedDemographic = reservedBed.getBedDemographic();
                // Since bed_check.jsp blocked dependents to have Community programs displayed in dropdown,
                // so reservedBed for dependents have communityProgramId == 0
                // When changed to Community program --> how about room_demographic update???
                if (bedDemographic != null) {
                    Integer clientId = bedDemographic.getId().getDemographicNo();

                    if (clientId != null) {
                        isClientDependent = clientManager.isClientDependentOfFamily(clientId);
                        isClientFamilyHead = clientManager.isClientFamilyHead(clientId);
                    }

                    if (clientId == null || isClientDependent) {// Forbid saving of this particular bedDemographic record when client is dependent of family
                        // bedManager.saveBed(reservedBed);//should not save bed if dependent
                    } else {// client can be family head or independent

                        if (isClientFamilyHead) {
                            familyList.clear();
                            List<JointAdmission> dependentList = clientManager.getDependents(Integer.valueOf(clientId.toString()));
                            familyList.add(clientId);
                            for (int j = 0; dependentList != null && j < dependentList.size(); j++) {
                                familyList.add(Integer.valueOf(dependentList.get(j).getClientId().toString()));
                            }

                            for (int k = 0; familyList != null && k < familyList.size(); k++) {
                                bedDemographic.getId().setDemographicNo(familyList.get(k));

                                BedDemographic dependentBD = bedDemographicManager.getBedDemographicByDemographic(familyList.get(k), loggedInInfo.getCurrentFacility().getId());

                                if (dependentBD != null) {
                                    bedDemographic.getId().setBedId(dependentBD.getId().getBedId());
                                }
                                bedManager.saveBed(reservedBed);

                                // save bed demographic
                                bedDemographicManager.saveBedDemographic(bedDemographic);

                                Integer communityProgramId = reservedBed.getCommunityProgramId();

                                if (communityProgramId > 0) {
                                    try {
                                        // discharge to community program
                                        admissionManager.processDischargeToCommunity(communityProgramId, bedDemographic.getId().getDemographicNo(), loggedInInfo.getLoggedInProviderNo(), "bed reservation ended - manually discharged", "0", null);
                                    } catch (AdmissionException e) {
                                        addActionMessage(getText("discharge.failure", e.getMessage()));
                                    }
                                }
                            }

                        } else {// client is indpendent
                            bedManager.saveBed(reservedBed);

                            // save bed demographic
                            bedDemographicManager.saveBedDemographic(bedDemographic);

                            Integer communityProgramId = reservedBed.getCommunityProgramId();

                            if (communityProgramId > 0) {
                                try {
                                    // discharge to community program
                                    admissionManager.processDischargeToCommunity(communityProgramId, bedDemographic.getId().getDemographicNo(), loggedInInfo.getLoggedInProviderNo(), "bed reservation ended - manually discharged", "0", null);
                                } catch (AdmissionException e) {
                                    addActionMessage(getText("discharge.failure", e.getMessage()));
                                }
                            }
                        }
                    }
                }
            } catch (BedReservedException e) {
                addActionMessage(getText("bed.reserved.error", e.getMessage()));
            }
        }// end of for (int i=0; i < reservedBeds.length; i++)

        return view();
    }

    public String switch_beds() {
        /*
         * (1)Check whether both clients are from same program //??? probably not necessary ??? (1.1)If not, disallow bed switching
         *
         * (2)Check whether both beds are from same room: (2.1)If beds are from same room: you can switch beds in any circumstances
         *
         * (2.2)If beds are from different rooms: (2.2.1)Only 2 indpendent clients can switch beds between different rooms. (2.2.2)If either client is a dependent, disallow switching beds of different rooms ???(2.2.3)If 2 clients are family heads, allow
         * switching beds with different rooms with conditions: (2.2.3.1)all dependents have to switch together ???
         *
         * (3)Save changes to the 'bed' table ??? <- not implemented yet
         */
        //Bed[] reservedBeds = this.getReservedBeds();
        Bed bed1 = null;
        Bed bed2 = null;
        Integer client1 = null;
        Integer client2 = null;
        boolean isSameRoom = false;
        boolean isFamilyHead1 = false;
        boolean isFamilyHead2 = false;
        boolean isFamilyDependent1 = false;
        boolean isFamilyDependent2 = false;
        boolean isIndependent1 = false;
        boolean isIndependent2 = false;
        Integer bedDemographicStatusId1 = null;
        boolean latePass1 = false;
        Date reservationEnd1 = null;
        Date assignEnd1 = null;
        Date today = new Date();
        // List<Integer> familyList = new ArrayList<Integer>();

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String switchBed1 = this.getSwitchBed1();
        String switchBed2 = this.getSwitchBed2();

        if (bedManager == null || bedDemographicManager == null || roomDemographicManager == null) {
            addActionMessage(getText("bed.check.error"));
            return view();
        }
        if (switchBed1 == null || switchBed1.length() <= 0) {
            addActionMessage(getText("bed.check.error"));
            return view();
        }

        bed1 = bedManager.getBed(Integer.valueOf(switchBed1));
        bed2 = bedManager.getBed(Integer.valueOf(switchBed2));

        if (bed1 == null || bed2 == null) {
            addActionMessage(getText("bed.check.error"));
            return view();
        }
        BedDemographic bedDemographic1 = bedDemographicManager.getBedDemographicByBed(bed1.getId());
        BedDemographic bedDemographic2 = bedDemographicManager.getBedDemographicByBed(bed2.getId());

        if (bedDemographic1 == null || bedDemographic2 == null) {
            addActionMessage(getText("bed.check.error"));
            return view();
        }
        client1 = bedDemographic1.getId().getDemographicNo();
        client2 = bedDemographic2.getId().getDemographicNo();

        bedDemographicStatusId1 = bedDemographic1.getBedDemographicStatusId();
        latePass1 = bedDemographic1.isLatePass();
        reservationEnd1 = bedDemographic1.getReservationEnd();


        // Check whether both beds are from same room:
        if (bed1.getRoomId().intValue() == bed2.getRoomId().intValue()) {
            isSameRoom = true;
        }

        if (isSameRoom) {// you can switch beds in same room for any client combination
            bedDemographicManager.deleteBedDemographic(bedDemographic1);
            bedDemographicManager.deleteBedDemographic(bedDemographic2);

            bedDemographic1.getId().setDemographicNo(client2);
            bedDemographic1.setBedDemographicStatusId(bedDemographic2.getBedDemographicStatusId());
            bedDemographic1.setLatePass(bedDemographic2.isLatePass());
            bedDemographic1.setReservationStart(today);
            bedDemographic1.setReservationEnd(bedDemographic2.getReservationEnd());
            bedDemographic2.getId().setDemographicNo(client1);
            bedDemographic2.setBedDemographicStatusId(bedDemographicStatusId1);
            bedDemographic2.setLatePass(latePass1);
            bedDemographic2.setReservationStart(today);
            bedDemographic2.setReservationEnd(reservationEnd1);

            bedDemographicManager.saveBedDemographic(bedDemographic1);
            bedDemographicManager.saveBedDemographic(bedDemographic2);
        } else {// beds are from different rooms
            isFamilyHead1 = clientManager.isClientFamilyHead(client1);
            isFamilyHead2 = clientManager.isClientFamilyHead(client2);
            isFamilyDependent1 = clientManager.isClientDependentOfFamily(client1);
            isFamilyDependent2 = clientManager.isClientDependentOfFamily(client2);


            RoomDemographic roomDemographic1 = roomDemographicManager.getRoomDemographicByDemographic(client1, loggedInInfo.getCurrentFacility().getId());
            RoomDemographic roomDemographic2 = roomDemographicManager.getRoomDemographicByDemographic(client2, loggedInInfo.getCurrentFacility().getId());

            if (roomDemographic1 == null || roomDemographic2 == null) {
                addActionMessage(getText("bed.check.error"));
                return view();
            }

            if (!isFamilyHead1 && !isFamilyDependent1) {
                isIndependent1 = true;
            }
            if (!isFamilyHead2 && !isFamilyDependent2) {
                isIndependent2 = true;
            }


            // Check whether both clients are indpendents
            if (isIndependent1 && isIndependent2) {
                // Can switch beds and rooms
                bedDemographicManager.deleteBedDemographic(bedDemographic1);
                bedDemographicManager.deleteBedDemographic(bedDemographic2);

                bedDemographic1.getId().setDemographicNo(client2);
                bedDemographic1.setBedDemographicStatusId(bedDemographic2.getBedDemographicStatusId());
                bedDemographic1.setLatePass(bedDemographic2.isLatePass());
                bedDemographic1.setReservationStart(today);
                bedDemographic1.setReservationEnd(bedDemographic2.getReservationEnd());
                bedDemographic2.getId().setDemographicNo(client1);
                bedDemographic2.setBedDemographicStatusId(bedDemographicStatusId1);
                bedDemographic2.setLatePass(latePass1);
                bedDemographic2.setReservationStart(today);
                bedDemographic2.setReservationEnd(reservationEnd1);

                bedDemographicManager.saveBedDemographic(bedDemographic1);
                bedDemographicManager.saveBedDemographic(bedDemographic2);

                roomDemographicManager.deleteRoomDemographic(roomDemographic1);
                roomDemographicManager.deleteRoomDemographic(roomDemographic2);

                assignEnd1 = roomDemographic1.getAssignEnd();
                roomDemographic1.getId().setDemographicNo(client2);
                roomDemographic1.setAssignStart(today);
                roomDemographic1.setAssignEnd(roomDemographic2.getAssignEnd());
                roomDemographic2.getId().setDemographicNo(client1);
                roomDemographic2.setAssignStart(today);
                roomDemographic2.setAssignEnd(assignEnd1);

                roomDemographicManager.saveRoomDemographic(roomDemographic1);
                roomDemographicManager.saveRoomDemographic(roomDemographic2);
            } else {
                if (isFamilyDependent1 || isFamilyDependent2) {// if either client is dependent or both are
                    // do not allow bed switching
                    addActionMessage(getText("bed.check.dependent_disallowed"));
                    return view();
                }
                if (isFamilyHead1 || isFamilyHead2) {// if either clients are family head
                    // very complicated!!!
                    addActionMessage(getText("bed.check.familyHead_switch"));
                    return view();
                }
            }
        }
        return view();
    }

    @Required
    public void setClientRestrictionManager(ClientRestrictionManager clientRestrictionManager) {
        this.clientRestrictionManager = clientRestrictionManager;
    }

    public void setCaseManagementManager(CaseManagementManager caseManagementManager) {
        this.caseManagementManager = caseManagementManager;
    }

    public void setAdmissionManager(AdmissionManager mgr) {
        this.admissionManager = mgr;
    }

    public void setBedDemographicManager(BedDemographicManager demographicBedManager) {
        this.bedDemographicManager = demographicBedManager;
    }

    public void setRoomDemographicManager(RoomDemographicManager roomDemographicManager) {
        this.roomDemographicManager = roomDemographicManager;
    }

    public void setBedManager(BedManager bedManager) {
        this.bedManager = bedManager;
    }

    public void setClientManager(ClientManager mgr) {
        this.clientManager = mgr;
    }

    public void setProgramManager(ProgramManager mgr) {
        this.programManager = mgr;
    }

    public void setProgramQueueManager(ProgramQueueManager mgr) {
        this.programQueueManager = mgr;
    }


    private String tab;
    private String subtab;
    private String clientId;
    private String queueId;
    private Bed[] reservedBeds;
    private String switchBed1;
    private String switchBed2;
    private String vacancyOrTemplateId;

    private String radioRejectionReason;
    private ProgramClientRestriction serviceRestriction;

    public String getRadioRejectionReason() {
        return radioRejectionReason;
    }

    public void setRadioRejectionReason(String radioRejectionReason) {
        this.radioRejectionReason = radioRejectionReason;
    }

    /**
     * @return Returns the tab.
     */
    public String getTab() {
        return tab;
    }

    /**
     * @param tab The tab to set.
     */
    public void setTab(String tab) {
        this.tab = tab;
    }

    /**
     * @return the subtab
     */
    public String getSubtab() {
        return subtab;
    }

    /**
     * @param subtab the subtab to set
     */
    public void setSubtab(String subtab) {
        this.subtab = subtab;
    }

    /**
     * @return Returns the clientId.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId The clientId to set.
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public Bed[] getReservedBeds() {
        return reservedBeds;
    }

    public void setReservedBeds(Bed[] reservedBeds) {
        this.reservedBeds = reservedBeds;
    }

    public ProgramClientRestriction getServiceRestriction() {
        return serviceRestriction;
    }

    public void setServiceRestriction(ProgramClientRestriction serviceRestriction) {
        this.serviceRestriction = serviceRestriction;
    }

    public String getSwitchBed1() {
        return switchBed1;
    }

    public void setSwitchBed1(String switchBed1) {
        this.switchBed1 = switchBed1;
    }

    public String getSwitchBed2() {
        return switchBed2;
    }

    public void setSwitchBed2(String switchBed2) {
        this.switchBed2 = switchBed2;
    }

    public String getVacancyOrTemplateId() {
        return vacancyOrTemplateId;
    }

    public void setVacancyOrTemplateId(String vacancyOrTemplateId) {
        this.vacancyOrTemplateId = vacancyOrTemplateId;
    }

}
