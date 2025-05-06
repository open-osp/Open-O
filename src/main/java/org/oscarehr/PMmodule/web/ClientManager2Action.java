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

package org.oscarehr.PMmodule.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.VacancyDao;
import org.oscarehr.PMmodule.dao.VacancyTemplateDao;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.AlreadyQueuedException;
import org.oscarehr.PMmodule.exception.ClientAlreadyRestrictedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.HealthSafety;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ClientRestrictionManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.service.HealthSafetyManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.web.formbean.ClientManagerFormBean;
import org.oscarehr.PMmodule.web.formbean.ErConsentFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.PMmodule.wlmatch.MatchBO;
import org.oscarehr.PMmodule.wlmatch.MatchingManager;
import org.oscarehr.PMmodule.wlmatch.VacancyDisplayBO;
import org.oscarehr.PMmodule.wlservice.WaitListService;
import org.oscarehr.caisi_integrator.ws.CachedAdmission;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.Gender;
import org.oscarehr.caisi_integrator.ws.Referral;
import org.oscarehr.caisi_integrator.ws.ReferralWs;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.CdsClientFormDao;
import org.oscarehr.common.dao.IntegratorConsentDao;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.common.dao.RemoteReferralDao;
import org.oscarehr.common.model.*;
import org.oscarehr.managers.BedDemographicManager;
import org.oscarehr.managers.BedManager;
import org.oscarehr.managers.RoomDemographicManager;
import org.oscarehr.managers.RoomManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;
import org.springframework.beans.factory.annotation.Required;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.oscarDemographic.data.DemographicRelationship;

import com.quatro.service.LookupManager;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class ClientManager2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static final Logger logger = MiscUtils.getLogger();

    private HealthSafetyManager healthSafetyManager = SpringUtils.getBean(HealthSafetyManager.class);
    private ClientRestrictionManager clientRestrictionManager = SpringUtils.getBean(ClientRestrictionManager.class);
    private SurveyManager surveyManager = SpringUtils.getBean(SurveyManager.class);
    private LookupManager lookupManager = SpringUtils.getBean(LookupManager.class);
    private CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);
    private AdmissionManager admissionManager = SpringUtils.getBean(AdmissionManager.class);
    private GenericIntakeManager genericIntakeManager = SpringUtils.getBean(GenericIntakeManager.class);
    private BedDemographicManager bedDemographicManager = SpringUtils.getBean(BedDemographicManager.class);
    private BedManager bedManager = SpringUtils.getBean(BedManager.class);
    private ClientManager clientManager = SpringUtils.getBean(ClientManager.class);
    private ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);
    private ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
    private ProgramQueueManager programQueueManager = SpringUtils.getBean(ProgramQueueManager.class);
    private IntegratorConsentDao integratorConsentDao = SpringUtils.getBean(IntegratorConsentDao.class);
    private CdsClientFormDao cdsClientFormDao = SpringUtils.getBean(CdsClientFormDao.class);
    private static AdmissionDao admissionDao = SpringUtils.getBean(AdmissionDao.class);
    private static ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
    private static ProgramDao programDao = SpringUtils.getBean(ProgramDao.class);
    private OcanStaffFormDao ocanStaffFormDao = SpringUtils.getBean(OcanStaffFormDao.class);
    private RemoteReferralDao remoteReferralDao = SpringUtils.getBean(RemoteReferralDao.class);
    private VacancyDao vacancyDao = SpringUtils.getBean(VacancyDao.class);
    private VacancyTemplateDao vacancyTemplateDao = SpringUtils.getBean(VacancyTemplateDao.class);
    private MatchingManager matchingManager = new MatchingManager();

    private RoomDemographicManager roomDemographicManager = SpringUtils.getBean(RoomDemographicManager.class);
    private RoomManager roomManager = SpringUtils.getBean(RoomManager.class);


    public void setIntegratorConsentDao(IntegratorConsentDao integratorConsentDao) {
        this.integratorConsentDao = integratorConsentDao;
    }


    public void setCdsClientFormDao(CdsClientFormDao cdsClientFormDao) {
        this.cdsClientFormDao = cdsClientFormDao;
    }

    public String execute() {
        String method = request.getParameter("method");
        if ("admit".equals(method)) {
            return admit();
        } else if ("admit_select_program".equals(method)) {
            return admit_select_program();
        } else if ("cancel".equals(method)) {
            return cancel();
        } else if ("discharge".equals(method)) {
            return discharge();
        } else if ("discharge_community".equals(method)) {
            return discharge_community();
        } else if ("discharge_community_select_program".equals(method)) {
            return discharge_community_select_program();
        } else if ("nested_discharge_community_select_program".equals(method)) {
            return nested_discharge_community_select_program();
        } else if ("discharge_select_program".equals(method)) {
            return discharge_select_program();
        } else if ("nested_discharge_select_program".equals(method)) {
            return nested_discharge_select_program();
        } else if ("getGeneralFormsReport".equals(method)) {
            return getGeneralFormsReport();
        } else if ("edit".equals(method)) {
            return edit();
        } else if ("getLinks".equals(method)) {
            return getLinks();
        } else if ("refer".equals(method)) {
            return refer();
        } else if ("refer_select_program".equals(method)) {
            return refer_select_program();
        } else if ("vacancy_refer_select_program".equals(method)) {
            return vacancy_refer_select_program();
        } else if ("service_restrict".equals(method)) {
            return service_restrict();
        } else if ("restrict_select_program".equals(method)) {
            return restrict_select_program();
        } else if ("terminate_early".equals(method)) {
            return terminate_early();
        } else if ("override_restriction".equals(method)) {
            return override_restriction();
        } else if ("refreshBedDropDownForReservation".equals(method)) {
            return refreshBedDropDownForReservation();
        } else if ("save".equals(method)) {
            return save();
        } else if ("saveBedReservation".equals(method)) {
            return saveBedReservation();
        }  else if ("save_survey".equals(method)) {
            return save_survey();
        } else if ("save_joint_admission".equals(method)) {
            return save_joint_admission();
        } else if ("remove_joint_admission".equals(method)) {
            return remove_joint_admission();
        } else if ("search_programs".equals(method)) {
            return search_programs();
        } else if ("submit_erconsent".equals(method)) {
            return submit_erconsent();
        } else if ("survey".equals(method)) {
            return survey();
        } else if ("update".equals(method)) {
            return update();
        }  else if ("view_referral".equals(method)) {
            return view_referral();
        } else if ("view_admission".equals(method)) {
            return view_admission();
        }
        this.setView(new ClientManagerFormBean());

        return edit();
    }

    public String admit() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        Admission admission = this.getAdmission();
        Program program = this.getProgram();
        String demographicNo = request.getParameter("id");

        Program fullProgram = programManager.getProgram(String.valueOf(program.getId()));

        try {
            admissionManager.processAdmission(Integer.valueOf(demographicNo), loggedInInfo.getLoggedInProviderNo(), fullProgram, admission.getDischargeNotes(), admission.getAdmissionNotes(), admission.isTemporaryAdmission());
        } catch (ProgramFullException e) {
            addActionMessage(getText("admit.error", "Program is full."));
        } catch (AdmissionException e) {
            addActionMessage(getText("admit.error", e.getMessage()));
        } catch (ServiceRestrictionException e) {
            addActionMessage(getText("admit.service_restricted", new String[]{e.getRestriction().getComments(), e.getRestriction().getProvider().getFormattedName()}));
        }

        LogAction.log("write", "admit", demographicNo, request);

        setEditAttributes(request, demographicNo);
        return "edit";
    }

    public String admit_select_program() {

        Program program = this.getProgram();
        String demographicNo = request.getParameter("id");
        setEditAttributes(request, demographicNo);

        program = programManager.getProgram(program.getId());

        /*
         * If the user is currently enrolled in a bed program, we must warn the provider that this will also be a discharge
         */
        if (program.getType().equalsIgnoreCase("bed")) {
            Admission currentAdmission = admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(demographicNo));
            if (currentAdmission != null) {
                request.setAttribute("current_admission", currentAdmission);
                request.setAttribute("current_program", programManager.getProgram(currentAdmission.getProgramId()));
            }
        }
        request.setAttribute("do_admit", new Boolean(true));

        return "edit";
    }

    public String cancel() {

        Admission admission = this.getAdmission();
        admission.setDischargeNotes("");
        admission.setRadioDischargeReason("");

        this.setView(new ClientManagerFormBean());
        return edit();
    }

    public String discharge() {
        Admission admission = this.getAdmission();
        Program p = this.getProgram();
        String id = request.getParameter("id");
        List<Integer> dependents = clientManager.getDependentsList(new Integer(id));
        String formattedDischargeDate = request.getParameter("dischargeDate");
        Date dischargeDate = oscar.util.DateUtils.toDate(formattedDischargeDate);
        boolean success = true;

        try {
            admissionManager.processDischarge(p.getId(), new Integer(id), admission.getDischargeNotes(), admission.getRadioDischargeReason(), dischargeDate, dependents, false, false);
        } catch (AdmissionException e) {
            addActionMessage(getText("discharge.failure", e.getMessage()));
            success = false;
        }

        if (success) {
            addActionMessage(getText("discharge.success"));
            LogAction.log("write", "discharge", id, request);
        }

        setEditAttributes(request, id);
        admission.setDischargeNotes("");
        admission.setRadioDischargeReason("");
        admission.setDischargeDate(new Date());
        return "edit";
    }

    public String discharge_community() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        Admission admission = this.getAdmission();
        Program program = this.getProgram();
        String clientId = request.getParameter("id");
        List<Integer> dependents = clientManager.getDependentsList(new Integer(clientId));

        try {
            admissionManager.processDischargeToCommunity(program.getId(), new Integer(clientId), loggedInInfo.getLoggedInProviderNo(), admission.getDischargeNotes(), admission.getRadioDischargeReason(), dependents, null);
            LogAction.log("write", "discharge", clientId, request);
            addActionMessage(getText("discharge.success"));
        } catch (AdmissionException e) {
            addActionMessage(getText("discharge.failure", e.getMessage()));
        }

        setEditAttributes(request, clientId);
        admission.setDischargeNotes("");
        admission.setRadioDischargeReason("");

        return "edit";
    }

    public String discharge_community_select_program() {
        String id = request.getParameter("id");

        setEditAttributes(request, id);

        Admission admission = admissionDao.getCurrentBedProgramAdmission(programDao, Integer.parseInt(id));
        if (admission != null) {
            request.setAttribute("admissionDate", admission.getAdmissionDate("yyyy-MM-dd"));
        }


        request.setAttribute("do_discharge", new Boolean(true));
        request.setAttribute("community_discharge", new Boolean(true));
        return "edit";
    }

    public String nested_discharge_community_select_program() {
        request.setAttribute("nestedReason", "true");
        return discharge_community_select_program();
    }

    public String discharge_select_program() {
        String id = request.getParameter("id");
        String admissionId = request.getParameter("admission.id");
        
        Program program = this.getProgram();
        request.setAttribute("programId", String.valueOf(program.getId()));
        setEditAttributes(request, id);

        request.setAttribute("do_discharge", new Boolean(true));

        if (admissionId != null) {
            Admission admission = admissionDao.find(Integer.parseInt(admissionId));
            if (admission != null) {
                request.setAttribute("admissionDate", admission.getAdmissionDate("yyyy-MM-dd"));
            }
        }

        return "edit";
    }

    public String nested_discharge_select_program() {
        request.setAttribute("nestedReason", "true");
        setEditAttributes(request, request.getParameter("id"));
        request.setAttribute("do_discharge", new Boolean(true));
        return "edit";
    }

    public String getGeneralFormsReport() {
        request.setAttribute("generalIntakeNodes", genericIntakeManager.getIntakeNodesByType(3));

        return "generalFormsReport";
    }

    public String edit() {
        String id = request.getParameter("id");

        if (id == null || id.equals("")) {
            Object o = request.getAttribute("demographicNo");

            if (o instanceof String) {
                id = (String) o;
            }

            if (o instanceof Long) {
                id = String.valueOf(o);
            }
        }

        setEditAttributes(request, id);

        LogAction.log("read", "pmm client record", id, request);

        String roles = (String) request.getSession().getAttribute("userrole");

        // for Vaccine Provider
        if (roles.indexOf("Vaccine Provider") != -1) {
            try {
                response.sendRedirect("/VaccineProviderReport.do?id=" + id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        // for ERModule
        if (roles.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
            Map<?, ?> consentMap = (Map<?, ?>) request.getSession().getAttribute("er_consent_map");

            if (consentMap == null) {
                return "consent";
            }

            if (consentMap.get(id) == null) {
                return "consent";
            }

            request.getSession().setAttribute("er_consent_map", consentMap);
            return "er-redirect";
        }

        Demographic demographic = clientManager.getClientByDemographicNo(id);
        request.getSession().setAttribute("clientGender", demographic.getSex());
        request.getSession().setAttribute("clientAge", demographic.getAge());
        request.getSession().setAttribute("demographicId", demographic.getDemographicNo());

        return "edit";
    }

    public String getLinks() {
        return "links";
    }

    public String refer() {
        ClientReferral referral = this.getReferral();

        int clientId = Integer.parseInt(request.getParameter("id"));
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        Program p1 = this.getProgram();

        Integer selectVacancyId = p1.getVacancyId();
        int programId = p1.getId();
        // if it's local
        if (programId != 0) {
            Program p = programManager.getProgram(programId);
            referral.setClientId((long) clientId);
            referral.setProgramId((long) programId);
            referral.setProviderNo(loggedInInfo.getLoggedInProviderNo());

            referral.setFacilityId(loggedInInfo.getCurrentFacility().getId());

            referral.setReferralDate(new Date());
            referral.setProgramType(p.getType());
            ClientManagerFormBean tabBean = this.getView();
            if (tabBean.getTab().equals("Refer to vacancy")) {
                //p = getMatchVacancy(p); //???????
                if (selectVacancyId != null) {
                    referral.setVacancyId(selectVacancyId);
                    referral.setSelectVacancy(vacancyDao.getVacancyById(selectVacancyId).getName());
                }

            } else {
                String vacancyId = request.getParameter("vacancyId");
                if (vacancyId == null || vacancyId.trim().length() == 0) {
                    referral.setSelectVacancy("none");
                } else {
                    Vacancy v = null;
                    try {
                        v = vacancyDao.getVacancyById(Integer.parseInt(vacancyId.trim()));
                    } catch (Exception e) {
                        MiscUtils.getLogger().error("error", e);
                    }
                    if (v != null) {
                        referral.setVacancyId(Integer.parseInt(vacancyId.trim()));
                        referral.setSelectVacancy(v.getName());
                    }
                }
            }

            referToLocalAgencyProgram(request, referral, p);
        }
        // remote referral
        else if (referral.getRemoteFacilityId() != null && referral.getRemoteProgramId() != null) {
            try {
                int remoteFacilityId = Integer.parseInt(referral.getRemoteFacilityId());
                int remoteProgramId = Integer.parseInt(referral.getRemoteProgramId());

                Referral integratorReferral = new Referral();
                integratorReferral.setDestinationIntegratorFacilityId(remoteFacilityId);
                integratorReferral.setDestinationCaisiProgramId(remoteProgramId);
                integratorReferral.setPresentingProblem(referral.getPresentProblems());
                integratorReferral.setReasonForReferral(referral.getNotes());
                integratorReferral.setSourceCaisiDemographicId(clientId);
                integratorReferral.setSourceCaisiProviderId(loggedInInfo.getLoggedInProviderNo());

                ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());
                referralWs.makeReferral(integratorReferral);

                // save local copy
                RemoteReferral remoteReferral = new RemoteReferral();
                remoteReferral.setFacilityId(loggedInInfo.getCurrentFacility().getId());
                remoteReferral.setDemographicId(clientId);
                remoteReferral.setPresentingProblem(referral.getPresentProblems());
                remoteReferral.setReasonForReferral(referral.getNotes());
                remoteReferral.setReferalDate(new GregorianCalendar());

                CachedFacility cachedFacility = CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(), remoteFacilityId);
                remoteReferral.setReferredToFacilityName(cachedFacility.getName());

                FacilityIdIntegerCompositePk remoteProgramCompositeKey = new FacilityIdIntegerCompositePk();
                remoteProgramCompositeKey.setIntegratorFacilityId(remoteFacilityId);
                remoteProgramCompositeKey.setCaisiItemId(remoteProgramId);
                CachedProgram cachedProgram = CaisiIntegratorManager.getRemoteProgram(loggedInInfo, loggedInInfo.getCurrentFacility(), remoteProgramCompositeKey);
                remoteReferral.setReferredToProgramName(cachedProgram.getName());

                remoteReferral.setReferringProviderNo(loggedInInfo.getLoggedInProviderNo());
                remoteReferralDao.persist(remoteReferral);
            } catch (Exception e) {
                WebUtils.addErrorMessage(request.getSession(), "Error processing referral : " + e.getMessage());
                logger.error("Unexpected Error.", e);
            }
        }

        setEditAttributes(request, String.valueOf(clientId));
        this.setProgram(new Program());
        this.setReferral(new ClientReferral());

        return "edit";
    }

    private void referToLocalAgencyProgram(HttpServletRequest request, ClientReferral referral, Program p) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        Program program = programManager.getProgram(p.getId());

        referral.setStatus(ClientReferral.STATUS_ACTIVE);

        boolean success = true;
        try {
            clientManager.processReferral(referral);
        } catch (AlreadyAdmittedException e) {
            addActionMessage(getText("refer.already_admitted"));
            success = false;
        } catch (AlreadyQueuedException e) {
            addActionMessage(getText("refer.already_referred"));
            success = false;
        } catch (ServiceRestrictionException e) {
            addActionMessage(getText("refer.service_restricted", new String[]{e.getRestriction().getComments(), e.getRestriction().getProvider().getFormattedName()}));

            // store this for display
            this.setServiceRestriction(e.getRestriction());

            // going to need this in case of override
            this.setReferral(referral);

            // store permission
            request.setAttribute("hasOverridePermission", caseManagementManager.hasAccessRight("Service restriction override on referral", "access", loggedInInfo.getLoggedInProviderNo(), String.valueOf(referral.getClientId()), "" + program.getId()));

            // jump to service restriction error page to allow overrides, etc.
            // return "service_restriction_error";
        }

        if (success) {
            addActionMessage(getText("refer.success"));
        }

        LogAction.log("write", "referral", String.valueOf(referral.getClientId()), request);
    }

    public String refer_select_program() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        Program p = this.getProgram();
        ClientReferral r = getReferral();
        String id = request.getParameter("id");
        setEditAttributes(request, id);

        // if it's a local referral
        long programId = p.getId();
        if (programId != 0) {
            Program program = programManager.getProgram(programId);
            p.setName(program.getName());
            request.setAttribute("program", program);
        }
        // if it's a remote referal
        else if (r.getRemoteFacilityId() != null && r.getRemoteProgramId() != null) {
            try {
                FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
                pk.setIntegratorFacilityId(Integer.parseInt(r.getRemoteFacilityId()));
                pk.setCaisiItemId(Integer.parseInt(r.getRemoteProgramId()));
                CachedProgram cachedProgram = CaisiIntegratorManager.getRemoteProgram(loggedInInfo, loggedInInfo.getCurrentFacility(), pk);

                p.setName(cachedProgram.getName());

                Program program = new Program();
                BeanUtils.copyProperties(program, cachedProgram);

                request.setAttribute("program", program);
            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            }
        }

        request.setAttribute("do_refer", true);
        request.setAttribute("temporaryAdmission", programManager.getEnabled());

        return "edit";
    }

    public String vacancy_refer_select_program() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        Program p = this.getProgram();
        ClientReferral r = this.getReferral();
        r.setSelectVacancy(request.getParameter("vacancyName"));
        this.setReferral(r);
        String id = request.getParameter("id");
        setEditAttributes(request, id);

        // if it's a local referral
        long programId = p.getId();
        if (programId != 0) {
            Program program = programManager.getProgram(programId);
            p.setName(program.getName());
            p.setVacancyName(request.getParameter("vacancyName"));
            p.setVacancyId(Integer.valueOf(request.getParameter("vacancyId")));
            request.setAttribute("program", program);
        }
        // if it's a remote referal
        else if (r.getRemoteFacilityId() != null && r.getRemoteProgramId() != null) {
            try {
                FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
                pk.setIntegratorFacilityId(Integer.parseInt(r.getRemoteFacilityId()));
                pk.setCaisiItemId(Integer.parseInt(r.getRemoteProgramId()));
                CachedProgram cachedProgram = CaisiIntegratorManager.getRemoteProgram(loggedInInfo, loggedInInfo.getCurrentFacility(), pk);

                p.setName(cachedProgram.getName());

                Program program = new Program();
                BeanUtils.copyProperties(program, cachedProgram);

                request.setAttribute("program", program);
            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            }
        }

        request.setAttribute("do_refer", true);
        request.setAttribute("temporaryAdmission", programManager.getEnabled());

        //return "edit";
        return "refer_vacancy";
    }

    public String service_restrict() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        ProgramClientRestriction restriction = this.getServiceRestriction();
        Integer days = this.getServiceRestrictionLength();

        Program p = this.getProgram();
        String id = request.getParameter("id");

        restriction.setProgramId(p.getId());
        restriction.setDemographicNo(Integer.valueOf(id));
        restriction.setStartDate(new Date());
        restriction.setProviderNo(loggedInInfo.getLoggedInProviderNo());
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + days);
        restriction.setEndDate(cal.getTime());
        restriction.setEnabled(true);

        boolean success;
        try {
            clientRestrictionManager.saveClientRestriction(restriction);
            success = true;
        } catch (ClientAlreadyRestrictedException e) {
            addActionMessage(getText("restrict.already_restricted"));
            success = false;
        }

        if (success) {
            addActionMessage(getText("restrict.success"));
        }
        this.setProgram(new Program());
        this.setServiceRestriction(new ProgramClientRestriction());
        this.setServiceRestrictionLength(null);

        Facility facility = (Facility) request.getSession().getAttribute("currentFacility");
        if (facility != null) {
            request.setAttribute("serviceRestrictions", clientRestrictionManager.getActiveRestrictionsForClient(Integer.valueOf(id), facility.getId(), new Date()));
        } else {
            request.setAttribute("serviceRestrictions", clientRestrictionManager.getActiveRestrictionsForClient(Integer.valueOf(id), 0, new Date()));
        }

        setEditAttributes(request, id);
        LogAction.log("write", "service_restriction", id, request);

        return "edit";
    }

    public String restrict_select_program() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        Program p = this.getProgram();
        String id = request.getParameter("id");
        setEditAttributes(request, id);

        Program program = programManager.getProgram(p.getId());
        p.setName(program.getName());

        request.setAttribute("do_restrict", true);
        request.setAttribute("can_restrict", caseManagementManager.hasAccessRight("Create service restriction", "access", loggedInInfo.getLoggedInProviderNo(), id, "" + p.getId()));
        request.setAttribute("program", program);

        return "edit";
    }

    public String terminate_early() {

        int programClientRestrictionId = Integer.parseInt(request.getParameter("restrictionId"));
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        clientRestrictionManager.terminateEarly(programClientRestrictionId, loggedInInfo.getLoggedInProviderNo());

        return edit();
    }

    public String override_restriction() {
        ProgramClientRestriction restriction = this.getServiceRestriction();
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        ClientReferral referral = this.getReferral();

        if (!caseManagementManager.hasAccessRight("Service restriction override on referral", "access", loggedInInfo.getLoggedInProviderNo(), "" + restriction.getDemographicNo(), "" + restriction.getProgramId())) {
            this.setReferral(new ClientReferral());
            setEditAttributes(request, "" + referral.getClientId());

            return "edit";
        }

        boolean success = true;
        try {
            clientManager.processReferral(referral, true);
        } catch (AlreadyAdmittedException e) {
            addActionMessage(getText("refer.already_admitted"));
            success = false;
        } catch (AlreadyQueuedException e) {
            addActionMessage(getText("refer.already_referred"));
            success = false;
        } catch (ServiceRestrictionException e) {
            throw new RuntimeException("service restriction encountered during override");
        }

        if (success) {
            addActionMessage(getText("refer.success"));
        }
        this.setProgram(new Program());
        this.setReferral(new ClientReferral());
        setEditAttributes(request, "" + referral.getClientId());
        LogAction.log("write", "referral", "" + referral.getClientId(), request);

        return "edit";
    }

    public String refreshBedDropDownForReservation() {
        BedDemographic bedDemographic = this.getBedDemographic();
        String roomId = request.getParameter("roomId");
        request.setAttribute("roomId", roomId);

        request.setAttribute("isRefreshRoomDropDown", "Y");

        // retrieve an array of beds associated with this roomId
        Bed[] unreservedBeds = bedManager.getCurrentPlusUnreservedBedsByRoom(Integer.valueOf(roomId), bedDemographic.getId().getBedId(), false);

        request.setAttribute("unreservedBeds", unreservedBeds);

        return edit();
    }

    public String save() {
        return edit();
    }

    public String saveBedReservation() {
        // When room has beds assigned to it --> should not let client select room only.
        // When room has no beds assigned to it --> allow clients to select room only.

        BedDemographic bedDemographic = this.getBedDemographic();
        Date today = new Date();
        String roomId = request.getParameter("roomId");
        bedDemographic.setReservationStart(today);
        bedDemographic.setRoomId(Integer.valueOf(roomId));

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        Integer bedId = bedDemographic.getBedId();
        Integer demographicNo = bedDemographic.getId().getDemographicNo();
        boolean isBedSelected = (bedDemographic.getBedId() != null && bedDemographic.getBedId().intValue() != 0);

        boolean isFamilyHead = false;
        boolean isFamilyDependent = false;
        int familySize = 0;
        RoomDemographic roomDemographic = null;

        // if room has bed --> must be assigned as room/bed combo or
        // if room has no bed --> must be assigned as room only.
        boolean isRoomAssignedWithBeds = roomManager.isRoomAssignedWithBeds(bedDemographic.getRoomId());

        if (isRoomAssignedWithBeds && (bedId == null || bedId.intValue() == 0)) {// if assignedBed==1 && no bed assigned --> display error
            addActionMessage(getText("bed.reservation.room_bed"));
            return edit();
        }

        // get dependents to be saved, removed from 'room_demographic' & 'bed_demographic' tables.
        List<JointAdmission> dependentList = clientManager.getDependents(new Integer(demographicNo));
        JointAdmission clientsJadm = clientManager.getJointAdmission(new Integer(demographicNo));

        if (dependentList != null && dependentList.size() > 0) {
            // condition met then demographicNo must be familyHead
            familySize = dependentList.size() + 1;
            isFamilyHead = true;
        }

        if (clientsJadm != null && clientsJadm.getHeadClientId() != null) {
            isFamilyDependent = true;
        }

        if (!isFamilyHead && isFamilyDependent) {// when client is dependent of a family -> do not attempt to assign.
            // Display message notifying that the client cannot be saved (assign or unassign) a room or a bed
            addActionMessage(getText("bed.reservation.dependent_disallowed"));
            return edit();
        } else {// check whether client is familyHead or independent client
            // create roomDemographic from bedDemographic
            roomDemographic = roomDemographicManager.getRoomDemographicByDemographic(demographicNo, loggedInInfo.getCurrentFacility().getId());
            if (roomDemographic == null) {// demographicNo (familyHead or independent) has no record in 'room_demographic'
                roomDemographic = RoomDemographic.create(demographicNo, bedDemographic.getProviderNo());
            }
            roomDemographic.setRoomDemographicFromBedDemographic(bedDemographic);
            // detect check box false
            if (request.getParameter("bedDemographic.latePass") == null) {
                bedDemographic.setLatePass(false);
            }
            // when client is familyHead, all family + dependents must be assigned or unassigned together
            if (isFamilyHead && !isFamilyDependent) {
                // Conditions:
                // (1)Check whether the familySize is less than or equal to the (roomCapacity - roomOccupancy) currently.
                // (i.e.) [roomCapacity] - [roomOccupancy] - [familySize] >= 0
                // (1.1)roomCapacity is either max number of clients a room can accomodate or number of beds assigned to this room
                // with the beds taking precedence!
                // >> if familyHead choose room with bed, then roomCapacity = [total number of unreserved beds]
                // >> if familyHead choose room only, then roomCapacity = Capacity set for that particular room
                // (1.2)roomOccupancy = [number of all reserved beds] - [number of family members if occupying beds] or
                // roomOccupancy = [number of all assigned clients] - [number of family members if amongst assigned]
                // (2)If less than --> display message to notify that there is not enough space in room.
                // (3)If greater or equal :
                // (3.1)for room/bed --> Delete all records in 'bed_demographic' & 'room_demographic' if any -->
                // then save room/bed for all family members one at a time.
                // (3.2)for room only --> Delete all records in 'room_demographic' if any --> then save room for all family
                // members one at a time.

                int roomCapacity = 0;
                int roomOccupancy = 0;
                Room room = roomManager.getRoom(bedDemographic.getRoomId());
                Integer[] dependentIds = new Integer[dependentList.size()];
                List<Integer> unreservedBedIdList = new ArrayList<Integer>();
                List<Integer> dependentsBedIdList = new ArrayList<Integer>();
                List<Integer> availableBedIdList = new ArrayList<Integer>();
                List<Integer> correctedAvailableBedIdList = new ArrayList<Integer>();
                int numberOfFamilyMembersAssignedRoomBed = 0;
                Bed[] bedReservedInRoom = null;
                Bed[] bedUnReservedInRoom = null;
                List rdsByRoom = null;

                if (familySize > 1) {
                    for (int i = 0; i < dependentList.size(); i++) {
                        dependentIds[i] = new Integer((dependentList.get(i)).getClientId().intValue());
                    }
                }

                // Check whether all family members are under same bed program -> if not, display error message.
                boolean isProgramDifferent = admissionManager.isDependentInDifferentProgramFromHead(demographicNo, dependentList);

                if (isProgramDifferent) {
                    addActionMessage(getText("bed.reservation.programId_different"));
                    // Display message notifying that the dependent is under different bed program than family head -> cannot assign room/bed
                }

                if (bedDemographic.getRoomId().intValue() == 0) {// unassigning whole family
                    // unassign family head first
                    roomDemographicManager.saveRoomDemographic(roomDemographic);
                    if (isBedSelected) {
                        bedDemographicManager.saveBedDemographic(bedDemographic);
                    } else {
                        // if only select room without bed, delete previous selected bedId in 'bed_demographic' table
                        roomDemographicManager.cleanUpBedTables(roomDemographic);
                    }
                    // unassigning all dependents
                    for (int i = 0; dependentIds != null && i < dependentIds.length; i++) {
                        roomDemographic.getId().setDemographicNo(dependentIds[i]);
                        bedDemographic.getId().setDemographicNo(dependentIds[i]);
                        roomDemographicManager.saveRoomDemographic(roomDemographic);
                        if (isBedSelected) {
                            bedDemographicManager.saveBedDemographic(bedDemographic);
                        } else {
                            // if only select room without bed, delete previous selected bedId in 'bed_demographic' table
                            roomDemographicManager.cleanUpBedTables(roomDemographic);
                        }
                    }
                } else {
                    if (bedId == null || bedId.intValue() == 0) {// assign room only
                        if (room != null) {
                            roomCapacity = room.getOccupancy().intValue();
                        }
                    } else {// roomCapacity = total number of beds assigned to room
                        Bed[] bedAssignedToRoom = bedManager.getBedsByRoom(bedDemographic.getRoomId());
                        if (bedAssignedToRoom != null && bedAssignedToRoom.length > 0) {
                            roomCapacity = bedAssignedToRoom.length;
                        }
                    }

                    // roomOccupancy = [number of all assigned clients] - [number of family members if amongst room assigned] or
                    // roomOccupancy = [number of all reserved beds] - [number of family members if occupying beds]
                    // bedIdList = [id of all unreserved beds] + [id beds previously occupied by family members]
                    if (bedId == null || bedId.intValue() == 0) {// assign room only
                        int numberOfFamilyMembersAssignedRoom = 0;
                        rdsByRoom = roomDemographicManager.getRoomDemographicByRoom(bedDemographic.getRoomId());

                        if (rdsByRoom != null && !rdsByRoom.isEmpty()) {
                            for (int i = 0; i < rdsByRoom.size(); i++) {
                                int rdsClientId = ((RoomDemographic) (rdsByRoom.get(i))).getId().getDemographicNo().intValue();
                                if (demographicNo.intValue() == rdsClientId) {
                                    numberOfFamilyMembersAssignedRoom++;
                                }
                                for (int j = 0; j < dependentIds.length; j++) {

                                    if (dependentIds[j].intValue() == rdsClientId) {
                                        numberOfFamilyMembersAssignedRoom++;
                                    }
                                }
                            }
                            roomOccupancy = rdsByRoom.size() - numberOfFamilyMembersAssignedRoom;
                        }
                    } else {// assign room/bed combination

                        BedDemographic bd = null;

                        // unreservedBedIdList = [id of all unreserved beds] + [id beds previously occupied by family members]
                        bedUnReservedInRoom = bedManager.getReservedBedsByRoom(bedDemographic.getRoomId(), false);
                        if (bedUnReservedInRoom != null && bedUnReservedInRoom.length > 0) {
                            for (int i = 0; i < bedUnReservedInRoom.length; i++) {
                                unreservedBedIdList.add(bedUnReservedInRoom[i].getId());
                            }
                        }

                        bedReservedInRoom = bedManager.getReservedBedsByRoom(bedDemographic.getRoomId(), true);
                        if (bedReservedInRoom != null && bedReservedInRoom.length > 0) {

                            for (int i = 0; i < bedReservedInRoom.length; i++) {

                                int bedReservedInRoomId = (bedReservedInRoom[i]).getId().intValue();
                                bd = bedDemographicManager.getBedDemographicByBed(bedReservedInRoomId);
                                int bdClientId = bd.getId().getDemographicNo().intValue();

                                if (demographicNo.intValue() == bdClientId) {
                                    dependentsBedIdList.add(bd.getId().getBedId());
                                    numberOfFamilyMembersAssignedRoomBed++;
                                } else {
                                    for (int j = 0; j < dependentIds.length; j++) {
                                        if (dependentIds[j].intValue() == bdClientId) {
                                            dependentsBedIdList.add(bd.getId().getBedId());
                                            numberOfFamilyMembersAssignedRoomBed++;
                                        }
                                    }
                                }
                            }// end for loop
                            roomOccupancy = bedReservedInRoom.length - numberOfFamilyMembersAssignedRoomBed;
                        }

                        if (!unreservedBedIdList.isEmpty()) {
                            availableBedIdList.addAll(unreservedBedIdList);
                        }
                        if (!dependentsBedIdList.isEmpty()) {
                            availableBedIdList.addAll(dependentsBedIdList);
                        }
                    }// end of assign room/bed combination

                    // Check whether the familySize is less than or equal to the (roomCapacity - roomOccupancy) currently

                    if (roomCapacity > 0 && roomOccupancy >= 0 && familySize > 0 && (roomCapacity - roomOccupancy - familySize >= 0)) {
                        Integer clientId = null;

                        // assigning for familyHead only
                        roomDemographicManager.saveRoomDemographic(roomDemographic);

                        if (isBedSelected) {
                            BedDemographic bdHeadDelete = bedDemographicManager.getBedDemographicByDemographic(bedDemographic.getId().getDemographicNo(), loggedInInfo.getCurrentFacility().getId());
                            if (bdHeadDelete != null) {
                                bedDemographicManager.deleteBedDemographic(bdHeadDelete);
                            }
                            for (int i = 0; i < availableBedIdList.size(); i++) {
                                if (bedDemographic.getId().getBedId().intValue() != availableBedIdList.get(i).intValue()) {
                                    correctedAvailableBedIdList.add(availableBedIdList.get(i));
                                }
                            }
                            bedDemographicManager.saveBedDemographic(bedDemographic);
                        } else {
                            // if only select room without bed, delete previous selected bedId in 'bed_demographic' table
                            roomDemographicManager.cleanUpBedTables(roomDemographic);
                        }
                        // Assign for each dependent member of family
                        for (int i = 0; i < dependentList.size(); i++) {
                            // clienId is each dependent
                            clientId = new Integer(dependentList.get(i).getClientId().intValue());

                            if (clientId != null) {
                                roomDemographic = roomDemographicManager.getRoomDemographicByDemographic(clientId, loggedInInfo.getCurrentFacility().getId());
                                bedDemographic.getId().setDemographicNo(clientId); // change to dependent member

                                // assigning both room & bed (different ones) for all dependents
                                if (isBedSelected && correctedAvailableBedIdList.size() >= dependentList.size()) {

                                    BedDemographic bdDependent = bedDemographicManager.getBedDemographicByDemographic(bedDemographic.getId().getDemographicNo(), loggedInInfo.getCurrentFacility().getId());
                                    bedDemographic.getId().setBedId(correctedAvailableBedIdList.get(i));

                                    if (roomDemographic == null) {
                                        roomDemographic = RoomDemographic.create(clientId, bedDemographic.getProviderNo());
                                    }
                                    roomDemographic.setRoomDemographicFromBedDemographic(bedDemographic);
                                    // detect check box false
                                    if (request.getParameter("bedDemographic.latePass") == null) {
                                        bedDemographic.setLatePass(false);
                                    }

                                    roomDemographicManager.saveRoomDemographic(roomDemographic);
                                    if (bdDependent != null) {
                                        bedDemographicManager.deleteBedDemographic(bdDependent);
                                    }
                                    bedDemographicManager.saveBedDemographic(bedDemographic);

                                } else if (!isBedSelected) {// assigning room only for all dependents

                                    if (roomDemographic != null) {
                                        roomDemographic.setRoomDemographicFromBedDemographic(bedDemographic);
                                    } else {
                                        roomDemographic = RoomDemographic.create(clientId, bedDemographic.getProviderNo());
                                        roomDemographic.setRoomDemographicFromBedDemographic(bedDemographic);
                                    }
                                    // detect check box false
                                    if (request.getParameter("bedDemographic.latePass") == null) {
                                        bedDemographic.setLatePass(false);
                                    }
                                    roomDemographicManager.saveRoomDemographic(roomDemographic);

                                    // if only select room without bed, delete previous selected bedId in 'bed_demographic' table
                                    roomDemographicManager.cleanUpBedTables(roomDemographic);
                                }

                            }// end of if( clientId != null )

                        }// end for loop

                    } else {// if(roomCapacity - roomOccupancy - familySize < 0 )
                        String occupancy = "0";
                        String available = "0";
                        // Display message notifying that the roomCapacity is deficient ...
                        if (isBedSelected) {
                            if (bedReservedInRoom != null) {
                                occupancy = String.valueOf(bedReservedInRoom.length);
                            }
                            if (availableBedIdList != null) {
                                available = String.valueOf(availableBedIdList.size());
                            }
                            addActionMessage(getText("bed.reservation.bedsCapacity_exceeded", occupancy, available));
                        } else {
                            if (rdsByRoom != null) {
                                occupancy = String.valueOf(rdsByRoom.size());
                            }
                            if (roomCapacity > 0) {
                                available = "" + (roomCapacity - rdsByRoom.size());
                            }
                            addActionMessage(getText("bed.reservation.roomCapacity_exceeded", occupancy, available));
                        }
                        return edit();
                    }// end of if(roomCapacity - roomOccupancy - familySize < 0 )

                }// end of if(roomId != 0) -> (i.e.) assigning instead of unassigning

            } else { // when client is independent -> just assign/unassign either room/bed or room only.

                roomDemographicManager.saveRoomDemographic(roomDemographic);

                if (isBedSelected) {
                    bedDemographicManager.saveBedDemographic(bedDemographic);
                } else {
                    // if only select room without bed, delete previous selected bedId in 'bed_demographic' table
                    roomDemographicManager.cleanUpBedTables(roomDemographic);
                }
            }// end of isIndependentClient

        }// end of isFamilyHead || isIndependentClient

        if (bedDemographic.getRoomId().intValue() == 0) {
            addActionMessage(getText("bed.reservation.unreserved"));
        } else {
            addActionMessage(getText("bed.reservation.success"));
        }
        return edit();
    }

    public String save_survey() {
        CaisiFormInstance formInstance = this.getForm();
        ClientManagerFormBean formBean = this.getView();
        formInstance.setFormId(0);

        String clientId = (String) request.getAttribute("clientId");
        if (clientId == null) {
            clientId = request.getParameter("id");
        }

        formBean.setTab("Forms");

        setEditAttributes(request, clientId);
        return "edit";
    }

    public String save_joint_admission() {
        JointAdmission jadmission = new JointAdmission();

        String headClientId = request.getParameter("headClientId");
        String clientId = request.getParameter("dependentClientId");
        String type = request.getParameter("type");
        Integer headInteger = new Integer(headClientId);
        Integer clientInteger = new Integer(clientId);

        jadmission.setAdmissionDate(new Date());
        jadmission.setHeadClientId(headInteger);
        jadmission.setArchived(false);
        jadmission.setClientId(clientInteger);
        jadmission.setProviderNo((String) request.getSession().getAttribute("user"));
        jadmission.setTypeId(new Integer(type));
        clientManager.saveJointAdmission(jadmission);
        setEditAttributes(request, request.getParameter("clientId"));

        return "edit";
    }

    public String remove_joint_admission() {
        String clientId = request.getParameter("dependentClientId");
        clientManager.removeJointAdmission(new Integer(clientId), (String) request.getSession().getAttribute("user"));
        setEditAttributes(request, request.getParameter("clientId"));
        return "edit";
    }

    public String search_programs() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);


        Program criteria = this.getProgram();
        List<Program> programs = programManager.search(criteria);
        request.setAttribute("programs", programs);

        if (CaisiIntegratorManager.isEnableIntegratedReferrals(loggedInInfo.getCurrentFacility())) {
            try {
                List<CachedProgram> results = CaisiIntegratorManager.getRemoteProgramsAcceptingReferrals(loggedInInfo, loggedInInfo.getCurrentFacility());

                filterResultsByCriteria(results, criteria);

                removeCommunityPrograms(results);

                request.setAttribute("remotePrograms", results);
            } catch (MalformedURLException e) {
                logger.error("unexpected error", e);
            } catch (WebServiceException e) {
                logger.error("unexpected error", e);
            }
        }

        ProgramUtils.addProgramRestrictions(request);

        return "search_programs";
    }

    private void removeCommunityPrograms(List<CachedProgram> results) {
        Iterator<CachedProgram> it = results.iterator();
        while (it.hasNext()) {
            CachedProgram cachedProgram = it.next();
            if ("community".equals(cachedProgram.getType())) it.remove();
        }
    }

    private void filterResultsByCriteria(List<CachedProgram> results, Program criteria) {

        Iterator<CachedProgram> it = results.iterator();
        while (it.hasNext()) {
            CachedProgram cachedProgram = it.next();
            String temp = StringUtils.trimToNull(criteria.getName());
            if (temp != null) {
                if (!cachedProgram.getName().toLowerCase().contains(temp.toLowerCase())) {
                    it.remove();
                    continue;
                }
            }

            temp = StringUtils.trimToNull(criteria.getType());
            if (temp != null) {
                if (!cachedProgram.getType().equals(temp)) {
                    it.remove();
                    continue;
                }
            }

            temp = StringUtils.trimToNull(criteria.getManOrWoman());
            if (temp != null) {
                if (cachedProgram.getGender() != null && !cachedProgram.getGender().name().equals(temp.toUpperCase())) {
                    it.remove();
                    continue;
                }
            }

            if (criteria.isTransgender() && cachedProgram.getGender() != Gender.T) {
                it.remove();
                continue;
            }

            if (criteria.isFirstNation() && !cachedProgram.isFirstNation()) {
                it.remove();
                continue;
            }

            if (criteria.isBedProgramAffiliated() && !cachedProgram.isBedProgramAffiliated()) {
                it.remove();
                continue;
            }

            if (criteria.isAlcohol() && !cachedProgram.isAlcohol()) {
                it.remove();
                continue;
            }

            temp = StringUtils.trimToNull(criteria.getAbstinenceSupport());
            if (temp != null) {
                if (cachedProgram.getAbstinenceSupport() != null && !cachedProgram.getAbstinenceSupport().equals(temp)) {
                    it.remove();
                    continue;
                }
            }

            if (criteria.isPhysicalHealth() && !cachedProgram.isPhysicalHealth()) {
                it.remove();
                continue;
            }

            if (criteria.isMentalHealth() && !cachedProgram.isMentalHealth()) {
                it.remove();
                continue;
            }

            if (criteria.isHousing() && !cachedProgram.isHousing()) {
                it.remove();
                continue;
            }
        }
    }

    public String submit_erconsent() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        ErConsentFormBean consentFormBean = this.getErconsent();
        boolean success = true;

        String demographicNo = request.getParameter("id");

        // save consent to session
        Map<String, ErConsentFormBean> consentMap = (Map) request.getSession().getAttribute("er_consent_map");

        if (consentMap == null) {
            consentMap = new HashMap<String, ErConsentFormBean>();
        }
        consentMap.put(demographicNo, consentFormBean);

        request.getSession().setAttribute("er_consent_map", consentMap);

        List<?> programDomain = providerManager.getProgramDomain(loggedInInfo.getLoggedInProviderNo());
        if (programDomain.size() > 0) {
            boolean doAdmit = true;
            boolean doRefer = true;
            ProgramProvider program = (ProgramProvider) programDomain.get(0);
            // refer/admin client to service program associated with this user

            ClientReferral referral = new ClientReferral();
            referral.setFacilityId(loggedInInfo.getCurrentFacility().getId());
            referral.setClientId(new Long(demographicNo));
            referral.setNotes("ER Automated referral\nConsent Type: " + consentFormBean.getConsentType() + "\nReason: " + consentFormBean.getConsentReason());
            referral.setProgramId(program.getProgramId().longValue());
            referral.setProviderNo(loggedInInfo.getLoggedInProviderNo());
            referral.setReferralDate(new Date());
            referral.setStatus(ClientReferral.STATUS_ACTIVE);

            Admission currentAdmission = admissionManager.getCurrentAdmission(String.valueOf(program.getProgramId()), Integer.valueOf(demographicNo));
            if (currentAdmission != null) {
                referral.setStatus(ClientReferral.STATUS_REJECTED);
                referral.setCompletionNotes("Client currently admitted");
                referral.setCompletionDate(new Date());
                addActionMessage(getText("refer.already_admitted"));
                doAdmit = false;
            }
            ProgramQueue queue = programQueueManager.getActiveProgramQueue(String.valueOf(program.getId()), demographicNo);
            if (queue != null) {
                referral.setStatus(ClientReferral.STATUS_REJECTED);
                referral.setCompletionNotes("Client already in queue");
                referral.setCompletionDate(new Date());
                addActionMessage(getText("refer.already_referred"));
                doRefer = false;
            }
            if (doRefer) {
                if (referral.getFacilityId() == null) {
                    referral.setFacilityId(loggedInInfo.getCurrentFacility().getId());
                }
                clientManager.saveClientReferral(referral);
            }

            if (doAdmit) {
                String admissionNotes = "ER Automated admission\nConsent Type: " + consentFormBean.getConsentType() + "\nReason: " + consentFormBean.getConsentReason();
                try {
                    admissionManager.processAdmission(Integer.valueOf(demographicNo), loggedInInfo.getLoggedInProviderNo(), programManager.getProgram(String.valueOf(program.getProgramId())), null, admissionNotes);
                } catch (Exception e) {
                    MiscUtils.getLogger().error("Error", e);
                    addActionMessage(getText("admit.error", e.getMessage()));
                    success = false;
                }
            }
        }

        this.setErconsent(new ErConsentFormBean());
        request.setAttribute("id", demographicNo);
        if (success) {
            return "er-redirect";
        } else {
            return "intake-search";
        }
    }

    public String survey() {
        CaisiFormInstance formInstance = this.getForm();

        if (request.getAttribute("survey_saved") != null) {
            setEditAttributes(request, (String) request.getAttribute("clientId"));
            return "edit";
        }

        String clientId = request.getParameter("id");
        String formId = String.valueOf(formInstance.getFormId());

        formInstance.setFormId(0);

        try {
            response.sendRedirect("/PMmodule/Forms/SurveyExecute.do?method=survey&formId=" + formId + "&clientId=" + clientId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String update() {
        return edit();
    }

    public String view_referral() {
        String referralId = request.getParameter("referralId");
        ClientReferral referral = clientManager.getClientReferral(referralId);
        Demographic client = clientManager.getClientByDemographicNo("" + referral.getClientId());

        String providerNo = referral.getProviderNo();
        Provider provider = providerManager.getProvider(providerNo);


        this.setReferral(referral);
        this.setClient(client);
        this.setProvider(provider);
        OscarLogDao logDao = SpringUtils.getBean(OscarLogDao.class);
        List<OscarLog> logs = logDao.findByActionAndData("update_referral_date", referralId);
        if (logs.size() > 0)
            request.setAttribute("referral_date_updates", logs);

        logs = logDao.findByActionAndData("update_completion_date", referralId);
        if (logs.size() > 0)
            request.setAttribute("completion_date_updates", logs);


        return "view_referral";
    }

    public String view_admission() {
        String admissionId = request.getParameter("admissionId");
        Admission admission = admissionManager.getAdmission(Long.valueOf(admissionId));
        Demographic client = clientManager.getClientByDemographicNo("" + admission.getClientId());
        String providerNo = admission.getProviderNo();
        Provider provider = providerManager.getProvider(providerNo);


        this.setAdmission(admission);
        this.setClient(client);
        this.setProvider(provider);
        OscarLogDao logDao = SpringUtils.getBean(OscarLogDao.class);
        List<OscarLog> logs = logDao.findByActionAndData("update_admission_date", admissionId);
        if (logs.size() > 0)
            request.setAttribute("admission_date_updates", logs);

        logs = logDao.findByActionAndData("update_discharge_date", admissionId);
        if (logs.size() > 0)
            request.setAttribute("discharge_date_updates", logs);


        return "view_admission";
    }

    private boolean isInDomain(long programId, List<?> programDomain) {
        for (int x = 0; x < programDomain.size(); x++) {
            ProgramProvider p = (ProgramProvider) programDomain.get(x);

            if (p.getProgramId().longValue() == programId) {
                return true;
            }
        }

        return false;
    }

	/*
    private Program getMatchVacancy(Program p){

        List<VacancyDisplayBO> vacancyDisplayBOs = matchingManager.listNoOfVacanciesForWaitListProgram();

        Program program = p;
        for(int j=0;j<vacancyDisplayBOs.size();j++){
            if(vacancyDisplayBOs.get(j).getProgramId().equals(program.getId())){
                if(vacancyDisplayBOs.get(j).getNoOfVacancy() != 0){
                    program.setNoOfVacancy(vacancyDisplayBOs.get(j).getNoOfVacancy());
                    program.setVacancyName(vacancyDisplayBOs.get(j).getVacancyName());
                    program.setDateCreated(vacancyDisplayBOs.get(j).getCreated().toString());
                    int vacancyId = vacancyDisplayBOs.get(j).getVacancyID();
                    List<MatchBO> matchList= matchingManager.getClientMatches(vacancyId);
                    double percentageMatch = 0;
                    for(int k=0;k<matchList.size();k++){
                        percentageMatch = percentageMatch + matchList.get(k).getPercentageMatch();
                    }
                    program.setVacancyId(vacancyId);
                    program.setMatches(percentageMatch);
                }
            }
        }
        return program;
    }
    */

    private void setEditAttributes(HttpServletRequest request, String demographicNo) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String providerNo = loggedInInfo.getLoggedInProviderNo();

        Integer facilityId = loggedInInfo.getCurrentFacility().getId();
        ClientManagerFormBean tabBean = this.getView();
        Integer demographicId = Integer.valueOf(demographicNo);

        request.setAttribute("id", demographicNo);
        request.setAttribute("client", clientManager.getClientByDemographicNo(demographicNo));

        // program domain
        List<Program> programDomain = new ArrayList<Program>();

        for (Iterator<?> i = providerManager.getProgramDomain(providerNo).iterator(); i.hasNext(); ) {
            ProgramProvider programProvider = (ProgramProvider) i.next();
            programDomain.add(programManager.getProgram(programProvider.getProgramId()));
        }

        request.setAttribute("programDomain", programDomain);

        // check role permission
        HttpSession se = request.getSession();
        List admissions = admissionManager.getCurrentAdmissions(Integer.valueOf(demographicNo));
        for (Iterator it = admissions.iterator(); it.hasNext(); ) {
            Admission admission = (Admission) it.next();
            String inProgramId = String.valueOf(admission.getProgramId());
            String inProgramType = admission.getProgramType();
            if ("service".equalsIgnoreCase(inProgramType)) {
                se.setAttribute("performDischargeService", new Boolean(caseManagementManager.hasAccessRight("perform discharges", "access", providerNo, demographicNo, inProgramId)));
                se.setAttribute("performAdmissionService", new Boolean(caseManagementManager.hasAccessRight("perform admissions", "access", providerNo, demographicNo, inProgramId)));

            } else if ("bed".equalsIgnoreCase(inProgramType)) {
                se.setAttribute("performDischargeBed", new Boolean(caseManagementManager.hasAccessRight("perform discharges", "access", providerNo, demographicNo, inProgramId)));
                se.setAttribute("performAdmissionBed", new Boolean(caseManagementManager.hasAccessRight("perform admissions", "access", providerNo, demographicNo, inProgramId)));
                se.setAttribute("performBedAssignments", new Boolean(caseManagementManager.hasAccessRight("perform bed assignments", "access", providerNo, demographicNo, inProgramId)));

            }
        }

        // tab override - from survey module
        String tabOverride = (String) request.getAttribute("tab.override");

        if (tabOverride != null && tabOverride.length() > 0) {
            tabBean.setTab(tabOverride);
        }

        if (tabBean.getTab().equals("Summary")) {
            /* survey module */
            request.setAttribute("survey_list", surveyManager.getAllFormsForCurrentProviderAndCurrentFacility(loggedInInfo));
            request.setAttribute("surveys", surveyManager.getFormsForCurrentProviderAndCurrentFacility(loggedInInfo, demographicNo));

            // request.setAttribute("admissions", admissionManager.getCurrentAdmissions(Integer.valueOf(demographicNo)));
            // only allow bed/service programs show up.(not external program)
            List<Admission> currentAdmissionList = admissionManager.getCurrentAdmissionsByFacility(demographicId, facilityId);
            ArrayList<AdmissionForDisplay> bedServiceList = new ArrayList<AdmissionForDisplay>();
            for (Admission admission1 : currentAdmissionList) {
                if (!"External".equalsIgnoreCase(programManager.getProgram(admission1.getProgramId()).getType())) {
                    bedServiceList.add(new AdmissionForDisplay(admission1));
                }
            }
            addRemoteAdmissions(loggedInInfo, bedServiceList, demographicId);
            request.setAttribute("admissions", bedServiceList);

            Intake mostRecentQuickIntake = genericIntakeManager.getMostRecentQuickIntakeByFacility(Integer.valueOf(demographicNo), facilityId);
            request.setAttribute("mostRecentQuickIntake", mostRecentQuickIntake);

            HealthSafety healthsafety = healthSafetyManager.getHealthSafetyByDemographic(Long.valueOf(demographicNo));
            request.setAttribute("healthsafety", healthsafety);

            request.setAttribute("referrals", getReferralsForSummary(loggedInInfo, Integer.parseInt(demographicNo), facilityId));

            // FULL OCAN Staff/Client Assessment
            OcanStaffForm ocanStaffForm = ocanStaffFormDao.findLatestByFacilityClient(facilityId, Integer.valueOf(demographicNo), "FULL");
            if (ocanStaffForm != null) {
                if (ocanStaffForm.getAssessmentStatus() != null && ocanStaffForm.getAssessmentStatus().equals("In Progress"))
                    request.setAttribute("ocanStaffForm", ocanStaffForm);
            } else {
                request.setAttribute("ocanStaffForm", null);
            }

            // SELF+CORE OCAN Staff/Client Assessment
            OcanStaffForm selfOcanStaffForm = ocanStaffFormDao.findLatestByFacilityClient(facilityId, Integer.valueOf(demographicNo), "SELF");
            if (selfOcanStaffForm != null) {
                if (selfOcanStaffForm.getAssessmentStatus() != null && selfOcanStaffForm.getAssessmentStatus().equals("In Progress")) {
                    request.setAttribute("selfOcanStaffForm", selfOcanStaffForm);
                }
            } else {
                request.setAttribute("selfOcanStaffForm", null);
            }

            // CORE OCAN Staff/Client Assessment
            OcanStaffForm coreOcanStaffForm = ocanStaffFormDao.findLatestByFacilityClient(facilityId, Integer.valueOf(demographicNo), "CORE");
            if (coreOcanStaffForm != null) {
                if (coreOcanStaffForm.getAssessmentStatus() != null && coreOcanStaffForm.getAssessmentStatus().equals("In Progress")) {
                    request.setAttribute("coreOcanStaffForm", coreOcanStaffForm);
                }
            } else {
                request.setAttribute("coreOcanStaffForm", null);
            }

            //CBI form and OCAN forms are stored in same table OcanStaffForm.
            populateCbiData(request, Integer.parseInt(demographicNo), facilityId);

            // CDS
            populateCdsData(request, Integer.parseInt(demographicNo), facilityId);
        }

        /* history */
        if (tabBean.getTab().equals("History")) {
            ArrayList<AdmissionForDisplay> allResults = new ArrayList<AdmissionForDisplay>();

            List<Admission> addLocalAdmissions = admissionManager.getAdmissionsByFacility(demographicId, facilityId);
            for (Admission admission : addLocalAdmissions)
                allResults.add(new AdmissionForDisplay(admission));

            addRemoteAdmissions(loggedInInfo, allResults, demographicId);

            request.setAttribute("admissionHistory", allResults);
            request.setAttribute("referralHistory", getReferralsForHistory(loggedInInfo, demographicId, facilityId));
        }

        List<?> currentAdmissions = admissionManager.getCurrentAdmissions(Integer.valueOf(demographicNo));

        for (int x = 0; x < currentAdmissions.size(); x++) {
            Admission admission = (Admission) currentAdmissions.get(x);

            if (isInDomain(admission.getProgramId().longValue(), providerManager.getProgramDomain(providerNo))) {
                request.setAttribute("isInProgramDomain", Boolean.TRUE);
                break;
            }
        }

        /* bed reservation view */
        BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByDemographic(Integer.valueOf(demographicNo), facilityId);
        request.setAttribute("bedDemographic", bedDemographic);

        RoomDemographic roomDemographic = roomDemographicManager.getRoomDemographicByDemographic(Integer.valueOf(demographicNo), facilityId);

        if (roomDemographic != null) {
            Integer roomIdInt = roomDemographic.getId().getRoomId();
            Room room = null;
            if (roomIdInt != null) {
                room = roomManager.getRoom(roomIdInt);
            }
            if (room != null) {
                roomDemographic.setRoom(room);
            }
        }
        request.setAttribute("roomDemographic", roomDemographic);

        if (tabBean.getTab().equals("Bed/Room Reservation")) {

            boolean isRefreshRoomDropDown = false;
            if (request.getAttribute("isRefreshRoomDropDown") != null && request.getAttribute("isRefreshRoomDropDown").equals("Y")) {
                isRefreshRoomDropDown = true;
            } else {
                isRefreshRoomDropDown = false;
            }

            String roomId = request.getParameter("roomId");
            if (roomDemographic != null && roomId == null) {
                roomId = roomDemographic.getId().getRoomId().toString();
            }

            // set bed program id
            Admission bedProgramAdmission = admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(demographicNo));
            Integer bedProgramId = null;
            if (bedProgramAdmission != null) {
                bedProgramId = (bedProgramAdmission != null) ? bedProgramAdmission.getProgramId() : null;
            }
            request.setAttribute("bedProgramId", bedProgramId);

            Bed reservedBed = null;

            if (bedDemographic == null) {
                bedDemographic = BedDemographic.create(Integer.valueOf(demographicNo), bedDemographicManager.getDefaultBedDemographicStatus(), providerNo);

                if (roomDemographic != null) {
                    bedDemographic.setReservationStart(roomDemographic.getAssignStart());
                    bedDemographic.setReservationEnd(roomDemographic.getAssignEnd());
                }

                reservedBed = null;

            } else {

                reservedBed = bedManager.getBed(bedDemographic.getBedId());
            }

            if (isRefreshRoomDropDown) {
                bedDemographic.setRoomId(Integer.valueOf(roomId));
            }

            this.setBedDemographic(bedDemographic);
            Room[] availableRooms = roomManager.getAvailableRooms(facilityId, bedProgramId, Boolean.TRUE, demographicNo);

            request.setAttribute("availableRooms", availableRooms);

            if ((isRefreshRoomDropDown && roomId != null) || (reservedBed == null && !"0".equals(roomId))) {
                request.setAttribute("roomId", roomId);
            } else if (reservedBed != null) {
                request.setAttribute("roomId", reservedBed.getRoomId().toString());
            } else {
                request.setAttribute("roomId", "0");
            }
            request.setAttribute("isAssignedBed", String.valueOf(roomManager.isAssignedBed((String) request.getAttribute("roomId"), availableRooms)));

            // retrieve an array of beds associated with this roomId
            Bed[] unreservedBeds = null;

            if (isRefreshRoomDropDown && request.getAttribute("unreservedBeds") != null) {
                unreservedBeds = (Bed[]) request.getAttribute("unreservedBeds");

            } else if (reservedBed != null) {

                // unreservedBeds = bedManager.getBedsByRoomProgram(availableRooms, bedProgramId, false);
                unreservedBeds = bedManager.getCurrentPlusUnreservedBedsByRoom(reservedBed.getRoomId(), bedDemographic.getId().getBedId(), false);
            }

            this.setUnreservedBeds(unreservedBeds);

            // set bed demographic statuses
            this.setBedDemographicStatuses(bedDemographicManager.getBedDemographicStatuses());
        }
        /* forms */
        if (tabBean.getTab().equals("Forms")) {
            request.setAttribute("regIntakes", genericIntakeManager.getRegIntakes(Integer.valueOf(demographicNo), facilityId));
            request.setAttribute("quickIntakes", genericIntakeManager.getQuickIntakes(Integer.valueOf(demographicNo), facilityId));
            // request.setAttribute("indepthIntakes", genericIntakeManager.getIndepthIntakes(Integer.valueOf(demographicNo), facilityId));
            request.setAttribute("indepthIntakes", genericIntakeManager.getIntakesByType(Integer.valueOf(demographicNo), facilityId, 2));
            request.setAttribute("generalIntakes", genericIntakeManager.getIntakesByType(Integer.valueOf(demographicNo), facilityId, 3));
            request.setAttribute("programIntakes", genericIntakeManager.getProgramIntakes(Integer.valueOf(demographicNo), facilityId));
            request.setAttribute("programsWithIntake", genericIntakeManager.getProgramsWithIntake(Integer.valueOf(demographicNo)));

            request.setAttribute("indepthIntakeNodes", genericIntakeManager.getIntakeNodesByType(2));
            request.setAttribute("generalIntakeNodes", genericIntakeManager.getIntakeNodesByType(3));

            /* survey module */
            request.setAttribute("survey_list", surveyManager.getAllFormsForCurrentProviderAndCurrentFacility(loggedInInfo));
            request.setAttribute("surveys", surveyManager.getFormsForCurrentProviderAndCurrentFacility(loggedInInfo, demographicNo));

            /* consent forms */
            int clientId = Integer.parseInt(demographicNo);
            List<IntegratorConsent> consentTemp = integratorConsentDao.findByFacilityAndDemographic(facilityId, clientId);
            TreeMap<Date, HashMap<String, Object>> consents = new TreeMap<Date, HashMap<String, Object>>(Collections.reverseOrder());
            for (IntegratorConsent x : consentTemp) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("createdDate", DateFormatUtils.ISO_DATETIME_FORMAT.format(x.getCreatedDate()).replace('T', ' '));
                Provider provider = providerDao.getProvider(x.getProviderNo());
                map.put("provider", provider.getFormattedName());
                map.put("consentId", x.getId());

                consents.put(x.getCreatedDate(), map);
            }

            request.setAttribute("consents", consents.values());

            // CDS forms
            List<CdsClientForm> cdsForms = cdsClientFormDao.findByFacilityClient(facilityId, clientId);
            request.setAttribute("cdsForms", cdsForms);

            // CBI forms
            List<OcanStaffForm> cbiForms = ocanStaffFormDao.findByFacilityClient(facilityId, clientId, "CBI");
            request.setAttribute("cbiForms", cbiForms);

            // FULL OCAN Forms
            List<OcanStaffForm> ocanStaffForms = ocanStaffFormDao.findByFacilityClient(facilityId, clientId, "FULL");
            request.setAttribute("ocanStaffForms", ocanStaffForms);

            // SELF+CORE OCAN Forms
            List<OcanStaffForm> selfOcanStaffForms = ocanStaffFormDao.findByFacilityClient(facilityId, clientId, "SELF");
            request.setAttribute("selfOcanStaffForms", selfOcanStaffForms);

            // CORE OCAN Forms
            List<OcanStaffForm> coreOcanStaffForms = ocanStaffFormDao.findByFacilityClient(facilityId, clientId, "CORE");
            request.setAttribute("coreOcanStaffForms", coreOcanStaffForms);

        }

        /* refer */
        if (tabBean.getTab().equals("Refer") || tabBean.getTab().equals("Refer to vacancy")) {
            List<ClientReferral> clientReferrals = clientManager.getActiveReferrals(demographicNo, String.valueOf(facilityId));
            List<ClientReferral> clientReferralDisplay = new ArrayList<ClientReferral>();
            for (ClientReferral cr : clientReferrals) {
                Vacancy v = vacancyDao.getVacancyById(cr.getVacancyId() == null ? 0 : cr.getVacancyId());
                if (v != null) {
                    cr.setVacancyTemplateName(vacancyTemplateDao.getVacancyTemplate(v.getTemplateId()).getName());
                }
                clientReferralDisplay.add(cr);
            }
            request.setAttribute("referrals", clientReferralDisplay);

            if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
                try {
                    ArrayList<RemoteReferral> results = new ArrayList<RemoteReferral>();

                    // get local data
                    List<RemoteReferral> remoteReferralsFromDB = remoteReferralDao.findByFacilityIdDemogprahicId(facilityId, demographicId);
                    results.addAll(remoteReferralsFromDB);

                    // get remote Data
                    ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());

                    Integer currentRemoteFacilityId = CaisiIntegratorManager.getCurrentRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility()).getIntegratorFacilityId();
                    List<Referral> referrals = referralWs.getLinkedReferrals(Integer.parseInt(demographicNo));

                    if (referrals != null) {
                        for (Referral remoteReferral : referrals) {
                            if (currentRemoteFacilityId.equals(remoteReferral.getSourceIntegratorFacilityId()))
                                continue;

                            RemoteReferral temp = new RemoteReferral();
                            CachedFacility cachedFacility = CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(), remoteReferral.getDestinationIntegratorFacilityId());
                            temp.setReferredToFacilityName(cachedFacility.getName());

                            FacilityIdIntegerCompositePk pk = new FacilityIdIntegerCompositePk();
                            pk.setIntegratorFacilityId(remoteReferral.getDestinationIntegratorFacilityId());
                            pk.setCaisiItemId(remoteReferral.getDestinationCaisiProgramId());
                            CachedProgram cachedProgram = CaisiIntegratorManager.getRemoteProgram(loggedInInfo, loggedInInfo.getCurrentFacility(), pk);
                            temp.setReferredToProgramName(cachedProgram.getName());

                            temp.setReferalDate(remoteReferral.getReferralDate());

                            Provider tempProvider = providerDao.getProvider(remoteReferral.getSourceCaisiProviderId());
                            temp.setReferringProviderNo(tempProvider.getFormattedName());

                            temp.setReasonForReferral(remoteReferral.getReasonForReferral());
                            temp.setPresentingProblem(remoteReferral.getPresentingProblem());

                            results.add(temp);
                        }
                    }

                    Comparator<RemoteReferral> tempComparator = new Comparator<RemoteReferral>() {
                        @Override
                        public int compare(RemoteReferral o1, RemoteReferral o2) {
                            if (o1.getReferalDate() == null && o2.getReferalDate() == null) return (0);
                            if (o1.getReferalDate() == null) return (-1);
                            if (o2.getReferalDate() == null) return (1);
                            return (o1.getReferalDate().compareTo(o2.getReferalDate()));
                        }
                    };

                    Collections.sort(results, tempComparator);

                    request.setAttribute("remoteReferrals", results);
                } catch (Exception e) {
                    logger.error("Unexpected Error.", e);
                }
            }
            //Added for refer to Vacancy
            if (tabBean.getTab().equals("Refer to vacancy")) {
//				Program criteria = this.getProgram();				
//				List<Program> programs = programManager.search(criteria);

                //List<Program> programs = programManager.getPrograms(facilityId);

                //List<VacancyDisplayBO> vacancyDisplayBOs = matchingManager.listVacanciesForWaitListProgram();
                //get all vacancies.
                WaitListService s = new WaitListService();
                List<VacancyDisplayBO> vacancyDisplayBOs = s.listVacanciesForAllWaitListPrograms();

                List<Program> vacancyPrograms = new ArrayList<Program>();

                for (int j = 0; j < vacancyDisplayBOs.size(); j++) {
                    Program program = programManager.getProgram(vacancyDisplayBOs.get(j).getProgramId());

                    //if(vacancyDisplayBOs.get(j).getNoOfVacancy() != 0){
                    //program.setNoOfVacancy(vacancyDisplayBOs.get(j).getNoOfVacancy());
                    program.setVacancyName(vacancyDisplayBOs.get(j).getVacancyName());
                    program.setDateCreated(vacancyDisplayBOs.get(j).getCreated().toString());

                    int vacancyId = vacancyDisplayBOs.get(j).getVacancyID();
                    List<MatchBO> matchList = matchingManager.getClientMatches(vacancyId);
                    double percentageMatch = 0;
                    for (int k = 0; k < matchList.size(); k++) {
                        percentageMatch = percentageMatch + matchList.get(k).getPercentageMatch();
                    }

                    program.setVacancyId(vacancyId);
                    program.setMatches(percentageMatch);
                    program.setVacancyTemplateName(vacancyDisplayBOs.get(j).getVacancyTemplateName());
                    vacancyPrograms.add(program);
                }

                request.setAttribute("programs", vacancyPrograms);
            }

        }

        /* service restrictions */
        if (tabBean.getTab().equals("Service Restrictions")) {
            // request.setAttribute("serviceRestrictions", clientRestrictionManager.getActiveRestrictionsForClient(Integer.valueOf(demographicNo), new Date()));
            request.setAttribute("serviceRestrictions", clientRestrictionManager.getActiveRestrictionsForClient(Integer.valueOf(demographicNo), facilityId, new Date()));

            request.setAttribute("serviceRestrictionList", lookupManager.LoadCodeList("SRT", true, null, null));
        }

        /* discharge */
        if (tabBean.getTab().equals("Discharge")) {
            request.setAttribute("communityPrograms", programManager.getCommunityPrograms());
            request.setAttribute("serviceAdmissions", admissionManager.getCurrentServiceProgramAdmission(Integer.valueOf(demographicNo)));
            request.setAttribute("temporaryAdmissions", admissionManager.getCurrentTemporaryProgramAdmission(Integer.valueOf(demographicNo)));
            request.setAttribute("current_bed_program", admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(demographicNo)));
            request.setAttribute("current_community_program", admissionManager.getCurrentCommunityProgramAdmission(Integer.valueOf(demographicNo)));
            request.setAttribute("dischargeReasons", lookupManager.LoadCodeList("DRN", true, null, null));
            request.setAttribute("dischargeReasons2", ""/*lookupManager.LoadCodeList("DR2", true, null, null)*/);
        }

        /* Relations */
        DemographicRelationship demoRelation = new DemographicRelationship();
        List<Map<String, Object>> relList = demoRelation.getDemographicRelationshipsWithNamePhone(loggedInInfo, demographicNo, facilityId);
        List<JointAdmission> list = clientManager.getDependents(new Integer(demographicNo));
        JointAdmission clientsJadm = clientManager.getJointAdmission(new Integer(demographicNo));
        int familySize = list.size() + 1;
        if (familySize > 1) {
            request.setAttribute("groupHead", "yes");
        }
        if (clientsJadm != null) {
            request.setAttribute("dependentOn", clientsJadm.getHeadClientId());
            List<JointAdmission> depList = clientManager.getDependents(clientsJadm.getHeadClientId());
            familySize = depList.size() + 1;
            Demographic headClientDemo = clientManager.getClientByDemographicNo("" + clientsJadm.getHeadClientId());
            request.setAttribute("groupName", headClientDemo.getFormattedName() + " Group");
        }

        if (relList != null && relList.size() > 0) {
            for (Map<String, Object> h : relList) {
                String demographic = (String) h.get("demographicNo");
                Integer demoLong = new Integer(demographic);
                JointAdmission demoJadm = clientManager.getJointAdmission(demoLong);

                // IS PERSON JOINTLY ADMITTED WITH ME, They will either have the same HeadClient or be my headClient
                if (clientsJadm != null && clientsJadm.getHeadClientId().longValue() == demoLong) { // they're my head client
                    h.put("jointAdmission", "head");
                } else if (demoJadm != null && clientsJadm != null && clientsJadm.getHeadClientId().longValue() == demoJadm.getHeadClientId().longValue()) {
                    // They depend on the same person i do!
                    h.put("jointAdmission", "dependent");
                } else if (demoJadm != null && demoJadm.getHeadClientId().longValue() == new Long(demographicNo).longValue()) {
                    // They depend on me
                    h.put("jointAdmission", "dependent");
                }
                // Can this person be added to my depended List
                if (clientsJadm == null && demoJadm == null && clientManager.getDependents(demoLong).size() == 0) {
                    // yes if - i am not dependent on anyone
                    // - this person is not dependent on someone
                    // - this person is not a head of a family already
                    h.put("dependentable", "yes");
                }
                if (demoJadm != null) { // DEPENDS ON SOMEONE
                    h.put("dependentOn", demoJadm.getHeadClientId());
                    if (demoJadm.getHeadClientId().longValue() == new Long(demographicNo).longValue()) {
                        h.put("dependent", demoJadm.getTypeId());
                    }
                } else if (clientsJadm != null && clientsJadm.getHeadClientId().longValue() == demoLong) { // HEAD PERSON WON'T DEPEND ON ANYONE
                    h.put("dependent", new Long(0));
                }
            }
            request.setAttribute("relations", relList);
            request.setAttribute("relationSize", familySize);

        }
    }

    private void populateCbiData(HttpServletRequest request, Integer demographicNo, Integer facilityId) {
        List<Admission> admissions = admissionDao.getAdmissions(demographicNo);

        ArrayList<OcanStaffForm> allLatestCbiForms = new ArrayList<OcanStaffForm>();

        for (Admission admission : admissions) {
            OcanStaffForm cbiForm = ocanStaffFormDao.findLatestCbiFormsByFacilityAdmissionId(facilityId, admission.getId().intValue(), null);
            if (cbiForm != null) allLatestCbiForms.add(cbiForm);
        }

        request.setAttribute("allLatestCbiForms", allLatestCbiForms);
    }

    private void addRemoteAdmissions(LoggedInInfo loggedInInfo, ArrayList<AdmissionForDisplay> admissionsForDisplay, Integer demographicId) {
        if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {

            try {
                List<CachedAdmission> cachedAdmissions = null;
                try {
                    if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
                        cachedAdmissions = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedAdmissionsByDemographicId(demographicId);
                    }
                } catch (Exception e) {
                    MiscUtils.getLogger().error("Unexpected error.", e);
                    CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(), e);
                }

                if (CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
                    cachedAdmissions = IntegratorFallBackManager.getRemoteAdmissions(loggedInInfo, demographicId);
                }

                for (CachedAdmission cachedAdmission : cachedAdmissions)
                    admissionsForDisplay.add(new AdmissionForDisplay(loggedInInfo, cachedAdmission));

                Collections.sort(admissionsForDisplay, AdmissionForDisplay.ADMISSION_DATE_COMPARATOR);
            } catch (Exception e) {
                logger.error("Error retrieveing integrated admissions.", e);
            }
        }
    }

    private List<ReferralSummaryDisplay> getReferralsForSummary(LoggedInInfo loggedInInfo, Integer demographicNo, Integer facilityId) {
        ArrayList<ReferralSummaryDisplay> allResults = new ArrayList<ReferralSummaryDisplay>();

        List<ClientReferral> tempResults = clientManager.getActiveReferrals(String.valueOf(demographicNo), String.valueOf(facilityId));
        for (ClientReferral clientReferral : tempResults) {
            String vacancyName = clientReferral.getSelectVacancy();
            if (vacancyName != null) {
                List<Vacancy> vlist = vacancyDao.getVacanciesByName(vacancyName); //assume vacancyName is unique.
                if (vlist.size() > 0) {
                    Integer vacancyTemplateId = vlist.get(0).getTemplateId();
                    clientReferral.setVacancyTemplateName(vacancyTemplateDao.getVacancyTemplate(vacancyTemplateId).getName());
                }
            }
            allResults.add(new ReferralSummaryDisplay(clientReferral));

        }

        if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
            try {
                ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());

                List<Referral> tempRemoteReferrals = referralWs.getLinkedReferrals(demographicNo);
                for (Referral referral : tempRemoteReferrals)
                    allResults.add(new ReferralSummaryDisplay(loggedInInfo, referral));

                Collections.sort(allResults, ReferralSummaryDisplay.REFERRAL_DATE_COMPARATOR);
            } catch (Exception e) {
                logger.error("Unexpected error.", e);
            }
        }

        return (allResults);
    }

    private List<ReferralHistoryDisplay> getReferralsForHistory(LoggedInInfo loggedInInfo, Integer demographicNo, Integer facilityId) {
        ArrayList<ReferralHistoryDisplay> allResults = new ArrayList<ReferralHistoryDisplay>();

        for (ClientReferral clientReferral : clientManager.getReferralsByFacility(demographicNo, facilityId))
            allResults.add(new ReferralHistoryDisplay(clientReferral));

        if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
            try {
                ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());

                List<Referral> tempRemoteReferrals = referralWs.getLinkedReferrals(demographicNo);
                for (Referral referral : tempRemoteReferrals)
                    allResults.add(new ReferralHistoryDisplay(loggedInInfo, loggedInInfo.getCurrentFacility(), referral));

                Collections.sort(allResults, ReferralHistoryDisplay.REFERRAL_DATE_COMPARATOR);
            } catch (Exception e) {
                logger.error("Unexpected error.", e);
            }
        }

        return (allResults);
    }

    public static String getEscapedAdmissionSelectionDisplay(int admissionId) {
        Admission admission = admissionDao.getAdmission((long) admissionId);

        StringBuilder sb = new StringBuilder();
        if (admission != null) {
            sb.append(admission.getProgramName());
            sb.append(" ( ");
            sb.append(DateFormatUtils.ISO_DATE_FORMAT.format(admission.getAdmissionDate()));
            sb.append(" - ");
            if (admission.getDischargeDate() == null) sb.append("current");
            else sb.append(DateFormatUtils.ISO_DATE_FORMAT.format(admission.getDischargeDate()));
            sb.append(" )");
        }
        return (StringEscapeUtils.escapeHtml(sb.toString()));
    }

    public static String getEscapedProviderDisplay(String providerNo) {
        Provider provider = providerDao.getProvider(providerNo);

        return (StringEscapeUtils.escapeHtml(provider.getFormattedName()));
    }

    public static String getEscapedDateDisplay(Date d) {
        String display = DateFormatUtils.ISO_DATE_FORMAT.format(d);

        return (StringEscapeUtils.escapeHtml(display));
    }

    @Required
    public void setClientRestrictionManager(ClientRestrictionManager clientRestrictionManager) {
        this.clientRestrictionManager = clientRestrictionManager;
    }

    public void setHealthSafetyManager(HealthSafetyManager healthSafetyManager) {
        this.healthSafetyManager = healthSafetyManager;
    }

    public void setLookupManager(LookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public void setCaseManagementManager(CaseManagementManager caseManagementManager) {
        this.caseManagementManager = caseManagementManager;
    }

    public void setAdmissionManager(AdmissionManager mgr) {
        this.admissionManager = mgr;
    }

    public void setGenericIntakeManager(GenericIntakeManager genericIntakeManager) {
        this.genericIntakeManager = genericIntakeManager;
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

    public void setProviderManager(ProviderManager mgr) {
        this.providerManager = mgr;
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    private void populateCdsData(HttpServletRequest request, Integer demographicNo, Integer facilityId) {
        List<Admission> admissions = admissionDao.getAdmissions(demographicNo);
        List<Program> domain = null;


        ArrayList<CdsClientForm> allLatestCdsForms = new ArrayList<CdsClientForm>();

        boolean restrict = "true".equals(OscarProperties.getInstance().getProperty("caisi.cds.restrict_by_program_domain", "false"));
        if (restrict) {
            domain = programManager.getProgramDomain(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
        }

        for (Admission admission : admissions) {
            CdsClientForm cdsClientForm = cdsClientFormDao.findLatestByFacilityAdmissionId(facilityId, admission.getId().intValue(), null);
            if (cdsClientForm != null) {
                if (restrict) {
                    if (isAdmissionInDomain(admission, domain)) {
                        allLatestCdsForms.add(cdsClientForm);
                    }
                } else {
                    allLatestCdsForms.add(cdsClientForm);
                }
            }
        }

        request.setAttribute("allLatestCdsForms", allLatestCdsForms);
    }

    private boolean isAdmissionInDomain(Admission admission, List<Program> domain) {
        for (Program p : domain) {
            if (p.getId().intValue() == admission.getProgramId().intValue()) {
                return true;
            }
        }
        return false;
    }

    public static String getCdsProgramDisplayString(CdsClientForm cdsClientForm) {
        Admission admission = admissionDao.getAdmission(cdsClientForm.getAdmissionId());
        Program program = programDao.getProgram(admission.getProgramId());

        String displayString = program.getName() + " : " + DateFormatUtils.ISO_DATE_FORMAT.format(admission.getAdmissionDate());
        return (StringEscapeUtils.escapeHtml(displayString));
    }

    public static String getCbiProgramDisplayString(OcanStaffForm ocanStaffForm) {
        Admission admission = admissionDao.getAdmission(ocanStaffForm.getAdmissionId());
        Program program = programDao.getProgram(admission.getProgramId());

        String displayString = program.getName() + " : " + DateFormatUtils.ISO_DATE_FORMAT.format(admission.getAdmissionDate());
        return (StringEscapeUtils.escapeHtml(displayString));
    }

    private ClientManagerFormBean view;
    private Admission admission;
    private Program program;
    private BedDemographic bedDemographic;
    private ClientReferral referral;
    private ProgramClientRestriction serviceRestriction;
    private Integer serviceRestrictionLength;
    private CaisiFormInstance form;
    private ErConsentFormBean erconsent;
    private Demographic client;
    private Provider provider;
    private Bed[] unreservedBeds;
    private BedDemographicStatus[] bedDemographicStatuses;

    public ClientManagerFormBean getView() {
        return view;
    }

    public void setView(ClientManagerFormBean view) {
        this.view = view;
    }

    public Admission getAdmission() {
        return admission;
    }

    public void setAdmission(Admission admission) {
        this.admission = admission;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public BedDemographic getBedDemographic() {
        return bedDemographic;
    }

    public void setBedDemographic(BedDemographic bedDemographic) {
        this.bedDemographic = bedDemographic;
    }

    public ClientReferral getReferral() {
        return referral;
    }

    public void setReferral(ClientReferral referral) {
        this.referral = referral;
    }

    public ProgramClientRestriction getServiceRestriction() {
        return serviceRestriction;
    }

    public void setServiceRestriction(ProgramClientRestriction serviceRestriction) {
        this.serviceRestriction = serviceRestriction;
    }

    public Integer getServiceRestrictionLength() {
        return serviceRestrictionLength;
    }

    public void setServiceRestrictionLength(Integer serviceRestrictionLength) {
        this.serviceRestrictionLength = serviceRestrictionLength;
    }

    public CaisiFormInstance getForm() {
        return form;
    }

    public void setForm(CaisiFormInstance form) {
        this.form = form;
    }

    public ErConsentFormBean getErconsent() {
        return erconsent;
    }

    public void setErconsent(ErConsentFormBean erconsent) {
        this.erconsent = erconsent;
    }

    public Demographic getClient() {
        return client;
    }

    public void setClient(Demographic client) {
        this.client = client;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Bed[] getUnreservedBeds() {
        return unreservedBeds;
    }

    public void setUnreservedBeds(Bed[] unreservedBeds) {
        this.unreservedBeds = unreservedBeds;
    }

    public BedDemographicStatus[] getBedDemographicStatuses() {
        return bedDemographicStatuses;
    }

    public void setBedDemographicStatuses(BedDemographicStatus[] bedDemographicStatuses) {
        this.bedDemographicStatuses = bedDemographicStatuses;
    }
}
