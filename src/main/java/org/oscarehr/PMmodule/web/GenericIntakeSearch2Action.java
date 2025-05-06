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

import com.opensymphony.xwork2.ActionSupport;
import com.quatro.model.LookupCodeValue;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeConstants;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.caisi_integrator.ws.*;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;
import oscar.OscarProperties;
import oscar.util.DateUtils;
import oscar.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.util.*;

public class GenericIntakeSearch2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static Logger LOG = MiscUtils.getLogger();

    private static final List<LookupCodeValue> genders = new ArrayList<LookupCodeValue>();
    protected ClientManager clientManager = SpringUtils.getBean(ClientManager.class);

    // Parameters
    protected static final String METHOD = "method";
    protected static final String TYPE = "type";
    protected static final String CLIENT_ID = "clientId";
    protected static final String INTAKE_ID = "intakeId";
    protected static final String CLIENT_EDIT_ID = "id";
    protected static final String PROGRAM_ID = "programId";
    protected static final String START_DATE = "startDate";
    protected static final String END_DATE = "endDate";
    protected static final String INCLUDE_PAST = "includePast";

    // Method Names
    protected static final String EDIT_CREATE = "create";

    protected static final String EDIT_UPDATE = "update";

    // Session Attributes
    protected static final String CLIENT = "client";

    static {
        LookupCodeValue lv1 = new LookupCodeValue();
        lv1.setCode("M");
        lv1.setDescription("Male");
        genders.add(lv1);
        LookupCodeValue lv2 = new LookupCodeValue();
        lv2.setCode("F");
        lv2.setDescription("Female");
        genders.add(lv2);
        LookupCodeValue lv3 = new LookupCodeValue();
        lv3.setCode("T");
        lv3.setDescription("Transgender");
        genders.add(lv3);
        LookupCodeValue lv4 = new LookupCodeValue();
        lv4.setCode("O");
        lv4.setDescription("Other");
        genders.add(lv4);
    }

    // Forwards
    private static final String FORWARD_SEARCH_FORM = "searchForm";
    private static final String FORWARD_INTAKE_EDIT = "intakeEdit";

    private ClientImageDAO clientImageDAO = SpringUtils.getBean(ClientImageDAO.class);
    private SurveyManager surveyManager = SpringUtils.getBean(SurveyManager.class);


    @Override
    public String execute() throws Exception {
        String method = request.getParameter("method");
        if ("searchFromRemoteAdmit".equals(method)) {
            return searchFromRemoteAdmit();
        } else if ("search".equals(method)) {
            return search();
        } else if ("createLocal".equals(method)) {
            return createLocal();
        } else if ("updateLocal".equals(method)) {
            return updateLocal();
        } else if ("copyRemote".equals(method)) {
            return copyRemote();
        } 
        request.setAttribute("genders", getGenders());
        return FORWARD_SEARCH_FORM;
    }

    public String searchFromRemoteAdmit() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        try {
            Integer remoteReferralId = Integer.parseInt(request.getParameter("remoteReferralId"));

            ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());
            Referral remoteReferral = referralWs.getReferral(remoteReferralId);

            DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
            DemographicTransfer demographicTransfer = demographicWs.getDemographicByFacilityIdAndDemographicId(remoteReferral.getSourceIntegratorFacilityId(), remoteReferral.getSourceCaisiDemographicId());

            this.setFirstName(demographicTransfer.getFirstName());
            this.setGender(demographicTransfer.getGender().name());
            this.setHealthCardNumber(demographicTransfer.getHin());
            this.setHealthCardVersion(demographicTransfer.getHinVersion());
            this.setLastName(demographicTransfer.getLastName());

            if (demographicTransfer.getBirthDate() != null) {
                this.setYearOfBirth(String.valueOf(demographicTransfer.getBirthDate().get(Calendar.YEAR)));
                this.setMonthOfBirth(String.valueOf(demographicTransfer.getBirthDate().get(Calendar.MONTH)));
                this.setDayOfBirth(String.valueOf(demographicTransfer.getBirthDate().get(Calendar.DAY_OF_MONTH)));
            }
        } catch (MalformedURLException e) {
            LOG.error("Unexpected Error.", e);
        } catch (WebServiceException e) {
            LOG.error("Unexpected Error.", e);
        }

        return (search());
    }

    public String search() {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        // UCF
        request.getSession().setAttribute("survey_list", surveyManager.getAllFormsForCurrentProviderAndCurrentFacility(loggedInInfo));

        List<Demographic> localMatches = localSearch(loggedInInfo.getLoggedInProviderNo());
        this.setLocalMatches(localMatches);

        this.setSearchPerformed(true);
        request.setAttribute("genders", getGenders());

        if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
            createRemoteList();
        }

        // if matches found display results, otherwise create local intake
        @SuppressWarnings("unchecked")
        List<MatchingDemographicTransferScore> remoteMatches = (List<MatchingDemographicTransferScore>) request.getAttribute("remoteMatches");

        String roleName$ = (String) request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
        if (roleName$.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
            return FORWARD_SEARCH_FORM;
        }

        if (!localMatches.isEmpty() || (remoteMatches != null && remoteMatches.size() > 0)) {
            return FORWARD_SEARCH_FORM;
        } else {
            return createLocal();
        }
    }

    private void createRemoteList() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        try {
            DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());

            MatchingDemographicParameters parameters = new MatchingDemographicParameters();
            parameters.setMaxEntriesToReturn(10);
            parameters.setMinScore(7);

            String temp = StringUtils.trimToNull(this.getFirstName());
            parameters.setFirstName(temp);

            temp = StringUtils.trimToNull(this.getLastName());
            parameters.setLastName(temp);

            temp = StringUtils.trimToNull(this.getHealthCardNumber());
            parameters.setHin(temp);

            GregorianCalendar cal = new GregorianCalendar();
            {
                DateUtils.setToBeginningOfDay(cal);

                temp = StringUtils.trimToNull(this.getYearOfBirth());
                if (temp != null) cal.set(Calendar.YEAR, Integer.parseInt(temp));

                temp = StringUtils.trimToNull(this.getMonthOfBirth());
                if (temp != null) cal.set(Calendar.MONTH, Integer.parseInt(temp));

                temp = StringUtils.trimToNull(this.getDayOfBirth());
                if (temp != null) cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp));

                parameters.setBirthDate(cal);
            }

            List<MatchingDemographicTransferScore> integratedMatches = demographicWs.getMatchingDemographics(parameters);
            request.setAttribute("remoteMatches", integratedMatches);

            List<CachedFacility> allFacilities = CaisiIntegratorManager.getRemoteFacilities(loggedInInfo, loggedInInfo.getCurrentFacility());
            HashMap<Integer, String> facilitiesNameMap = new HashMap<Integer, String>();
            for (CachedFacility cachedFacility : allFacilities)
                facilitiesNameMap.put(cachedFacility.getIntegratorFacilityId(), cachedFacility.getName());

            request.setAttribute("facilitiesNameMap", facilitiesNameMap);
        } catch (WebServiceException e) {
            LOG.warn("Error connecting to integrator. " + e.getMessage());
            LOG.debug("Error connecting to integrator.", e);
        } catch (Exception e) {
            LOG.error("Unexpected error.", e);
        }
    }

    public String createLocal() {
        return forwardIntakeEditCreate(Demographic.create(this.getFirstName(), this.getLastName(), this.getGender(), this.getMonthOfBirth(), this.getDayOfBirth(), this.getYearOfBirth(),
                this.getHealthCardNumber(), this.getHealthCardVersion()));
    }

    public String updateLocal() {
        String roleName$ = (String) request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
        if (roleName$.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
            request.setAttribute("demographicNo", new Long(this.getDemographicId()));
            return "clientEdit";
        }


        return forwardIntakeEditUpdate(this.getDemographicId());
    }

    /**
     * This method is run from at least 2 locations, 1 is from "new client" and a remote client is found. 2 is from admitting remote referrals.
     */
    public String copyRemote() throws Exception {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        try {
            int remoteFacilityId = Integer.parseInt(request.getParameter("remoteFacilityId"));
            int remoteDemographicId = Integer.parseInt(request.getParameter("remoteDemographicId"));

            Demographic demographic = CaisiIntegratorManager.makeUnpersistedDemographicObjectFromRemoteEntry(loggedInInfo, loggedInInfo.getCurrentFacility(), remoteFacilityId, remoteDemographicId);


            String roleName$ = (String) request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
            if (roleName$.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
                clientManager.saveClient(demographic);
                request.setAttribute("demographicNo", new Long(demographic.getDemographicNo()));
                String providerNo = ((Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER)).getProviderNo();
                this.erClerklinkRemoteDemographic(loggedInInfo, remoteFacilityId, remoteDemographicId, providerNo, demographic);
                return "clientEdit";
            }

            return forwardIntakeEditCreate(demographic);
        } catch (Exception e) {
            //log.error("Unexpected error.", e);
            return execute();
        }
    }

    private List<Demographic> localSearch(String providerNo) {
        String strictSearch = OscarProperties.getInstance().getProperty("caisi.new_client.strict_search", "false");

        ClientSearchFormBean clientSearchBean = new ClientSearchFormBean();
        clientSearchBean.setFirstName(this.getFirstName());
        clientSearchBean.setLastName(this.getLastName());
        clientSearchBean.setGender(this.getGender());
        if (strictSearch.equalsIgnoreCase("true")) {
            ProgramProviderDAO ppDao = (ProgramProviderDAO) SpringUtils.getBean(ProgramProviderDAO.class);
            clientSearchBean.setSearchOutsideDomain(false);
            clientSearchBean.setProgramDomain(ppDao.getProgramDomain(providerNo));
        } else {
            clientSearchBean.setSearchOutsideDomain(true);
        }
        clientSearchBean.setSearchUsingSoundex(true);

        return clientManager.search(clientSearchBean);
    }

    protected String forwardIntakeEditCreate(Demographic client) {
        request.getSession().setAttribute(CLIENT, client);

        StringBuilder parameters = new StringBuilder("?");
        parameters.append(METHOD).append("=").append(EDIT_CREATE).append("&");
        parameters.append(TYPE).append("=").append(Intake.QUICK);

        copyParameter(request, "remoteReferralId", parameters);
        copyParameter(request, "remoteFacilityId", parameters);
        copyParameter(request, "remoteDemographicId", parameters);

        addDestinationProgramId(request, parameters);

        request.setAttribute("genders", getGenders());

        return createRedirectForward(FORWARD_INTAKE_EDIT, parameters);
    }

    private void addDestinationProgramId(HttpServletRequest request, StringBuilder parameters) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String remoteReferralId = StringUtils.trimToNull(request.getParameter("remoteReferralId"));
        if (remoteReferralId != null) {
            try {
                ReferralWs referralWs = CaisiIntegratorManager.getReferralWs(loggedInInfo, loggedInInfo.getCurrentFacility());
                Referral remoteReferral = referralWs.getReferral(Integer.parseInt(remoteReferralId));
                parameters.append("&destinationProgramId=");
                parameters.append(remoteReferral.getDestinationCaisiProgramId());
            } catch (MalformedURLException e) {
                LOG.error("Unexpected error.", e);
            } catch (WebServiceException e) {
                LOG.error("Unexpected error.", e);
            }
        }
    }

    private void copyParameter(HttpServletRequest request, String parameterName, StringBuilder url) {
        String temp = StringUtils.trimToNull(request.getParameter(parameterName));
        if (temp != null) {
            if (url.indexOf("?") < 0) url.append("?");
            else url.append("&");
            url.append(parameterName);
            url.append("=");
            url.append(temp);
        }
    }

    protected String forwardIntakeEditUpdate(Integer clientId) {
        StringBuilder parameters = new StringBuilder("?");
        parameters.append(METHOD).append("=").append(EDIT_UPDATE).append("&");
        parameters.append(TYPE).append("=").append(Intake.QUICK).append("&");
        parameters.append(CLIENT_ID).append("=").append(clientId);

        copyParameter(request, "remoteReferralId", parameters);
        addDestinationProgramId(request, parameters);

        return createRedirectForward(FORWARD_INTAKE_EDIT, parameters);
    }

    public static List<LookupCodeValue> getGenders() {
        return genders;
    }

    private void erClerklinkRemoteDemographic(LoggedInInfo loggedInInfo, int remoteFacilityId, int remoteDemographicId, String providerNo, Demographic client) {

        try {
            DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());

            // link the clients
            demographicWs.linkDemographics(providerNo, client.getDemographicNo(), remoteFacilityId, remoteDemographicId);

            // copy image if exists
            {
                DemographicTransfer demographicTransfer = demographicWs.getDemographicByFacilityIdAndDemographicId(remoteFacilityId, remoteDemographicId);

                if (demographicTransfer.getPhoto() != null) {
                    ClientImage clientImage = new ClientImage();
                    clientImage.setDemographic_no(client.getDemographicNo());
                    clientImage.setImage_data(demographicTransfer.getPhoto());
                    clientImage.setImage_type("jpg");
                    clientImageDAO.saveClientImage(clientImage);
                }
            }
        } catch (MalformedURLException e) {
            LOG.error("Error", e);
        } catch (WebServiceException e) {
            LOG.error("Error", e);
        }
    }

    protected String createRedirectForward(String forwardName, StringBuffer parameters) {
        return forwardName;
    }

    protected String createRedirectForward(String forwardName, StringBuilder parameters) {
        return forwardName;
    }

    public void setClientManager(ClientManager mgr) {
        this.clientManager = mgr;
    }

    private LabelValueBean[] months;
    private LabelValueBean[] days;

    private String method;

    private String firstName;
    private String lastName;
    private String monthOfBirth;
    private String dayOfBirth;
    private String yearOfBirth;
    private String healthCardNumber;
    private String healthCardVersion;
    private String gender;

    private boolean searchPerformed = false;

    private Collection<Demographic> localMatches;

    private String localAgencyUsername;

    private Integer demographicId;

    public GenericIntakeSearch2Action() {
        setMonths(GenericIntakeConstants.MONTHS);
        setDays(GenericIntakeConstants.DAYS);
    }

    public boolean isSearchPerformed() {
        return searchPerformed;
    }

    public void setSearchPerformed(boolean searchPerformed) {
        this.searchPerformed = searchPerformed;
    }

    public LabelValueBean[] getMonths() {
        return months;
    }

    public void setMonths(LabelValueBean[] months) {
        this.months = months;
    }

    public LabelValueBean[] getDays() {
        return days;
    }

    public void setDays(LabelValueBean[] days) {
        this.days = days;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMonthOfBirth() {
        return monthOfBirth;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public String getHealthCardVersion() {
        return healthCardVersion;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMonthOfBirth(String monthOfBirth) {
        this.monthOfBirth = monthOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }

    public void setHealthCardVersion(String healthCardVersion) {
        this.healthCardVersion = healthCardVersion;
    }


    public Collection<Demographic> getLocalMatches() {
        return localMatches;
    }

    public void setLocalMatches(Collection<Demographic> localMatches) {
        this.localMatches = localMatches;
    }

    public Integer getDemographicId() {
        return demographicId;
    }

    public void setDemographicId(Integer demographicId) {
        this.demographicId = demographicId;
    }

    public String getLocalAgencyUsername() {
        return localAgencyUsername;
    }

    public void setLocalAgencyUsername(String localAgencyUsername) {
        this.localAgencyUsername = localAgencyUsername;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
