/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.login;

import com.quatro.model.security.LdapSecurity;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.OcanForm;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.model.*;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.managers.AppManager;
import org.oscarehr.managers.SecurityManager;
import org.oscarehr.util.*;
import org.owasp.encoder.Encode;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.util.AlertTimer;
import oscar.util.CBIUtil;
import oscar.util.ParameterActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public final class LoginAction extends DispatchAction {

    /**
     * This variable is only intended to be used by this class and the jsp which
     * sets the selected facility.
     * This variable represents the queryString key used to pass the facility ID to
     * this class.
     */
    public static final String SELECTED_FACILITY_ID = "selectedFacilityId";

    private static final Logger logger = MiscUtils.getLogger();
    private static final String LOG_PRE = "Login!@#$: ";

    private final ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
    private final AppManager appManager = SpringUtils.getBean(AppManager.class);
    private final FacilityDao facilityDao = SpringUtils.getBean(FacilityDao.class);
    private final ProviderPreferenceDao providerPreferenceDao = SpringUtils.getBean(ProviderPreferenceDao.class);
    private final ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
    private final UserPropertyDAO propDao = SpringUtils.getBean(UserPropertyDAO.class);
    private final DSService dsService = SpringUtils.getBean(DSService.class);
    private final ServiceRequestTokenDao serviceRequestTokenDao = SpringUtils.getBean(ServiceRequestTokenDao.class);

    private final SecurityManager securityManager = SpringUtils.getBean(SecurityManager.class);
    private final SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);

    // remove after testing is done
    // private SsoAuthenticationManager ssoAuthenticationManager =
    // SpringUtils.getBean(SsoAuthenticationManager.class);

    private static ActionForward handleAjaxErrOrForwardErr(ActionMapping mapping, HttpServletResponse response, boolean isAjaxResponse, String errMsg) throws IOException {
        if (isAjaxResponse) {
            return handleAjaxError(response, errMsg);
        } else {
            return getErrorForward(mapping, errMsg);
        }
    }

    private static ActionForward handleAjaxError(HttpServletResponse response, String errMsg) throws IOException {
        JSONObject json = new JSONObject();
        json.put("success", false);
        json.put("error", errMsg);
        response.setContentType("text/x-json");
        json.write(response.getWriter());
        return null;
    }

    private static ActionForward getErrorForward(ActionMapping mapping, String errormsg) {
        String url = mapping.findForward("error").getPath();
        return new ActionForward(url + "?errormsg=" + errormsg);
    }

    /**
     * TODO: for the love of god - please help me clean-up this nightmare
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // >> 1. Initial Checks and Mobile Detection
        InitialChecksResult initialChecksResult = performInitialChecks(mapping, request);
        if (initialChecksResult.errForward != null) {
            return initialChecksResult.errForward;
        }

        boolean ajaxResponse = initialChecksResult.ajaxResponse;
        boolean isMobileOptimized = initialChecksResult.isMobileOptimized;
        String ip = initialChecksResult.ip;
        String submitType = initialChecksResult.submitType;

        LoginCheckLogin cl = new LoginCheckLogin();
        String oneIdKey = request.getParameter("nameId");
        String oneIdEmail = request.getParameter("email");
        String userName;
        String password;
        String pin;
        String nextPage;
        boolean forcedpasswordchange = initialChecksResult.forcedpasswordchange;
        String where = "failure";

        // >> 2. Forced Password Change Handling
        if (forcedpasswordchange) {

            PasswordChangeResult passwordChangeResult = handleForcedPasswordChange(mapping, form, request);

            if(passwordChangeResult.errForward !=null){
                return passwordChangeResult.errForward;
            }

            userName = passwordChangeResult.username;
            password = passwordChangeResult.password;
            pin = passwordChangeResult.pin;
            nextPage = passwordChangeResult.nextPage;

            // make sure this checking doesn't happen again
            forcedpasswordchange = false;
        }
        // >> 3. Standard Login Attempt
        else {
            LoginInfoResult loginInfoResult = initializePreLoginInfo(request, form);
            userName = loginInfoResult.username;
            password = loginInfoResult.password;
            pin = loginInfoResult.pin;
            nextPage = loginInfoResult.nextPage;

            logger.debug("nextPage: " + nextPage);
            if (nextPage != null) {
                // set current facility
                return getSelectFacilityForward(mapping, request, ip, nextPage);
            }

            if (cl.isBlock(ip, userName)) {
                logger.info(LOG_PRE + " Blocked: " + userName);
                // return mapping.findForward(where); //go to block page
                // change to block page
                String errMsg = "Oops! Your account is now locked due to incorrect password attempts!";

                return handleAjaxErrOrForwardErr(mapping, response, ajaxResponse, errMsg);
            }
        }

        logger.debug("ip was not blocked: " + ip);

        // >> 4. Authentication
        /*
         * THIS IS THE GATEWAY.
         */
        AuthResult authResult;
        try {
            authResult = this.authenticate(cl, userName, password, pin, ip);
        } catch (Exception e) {
            logger.error("Error", e);
            String errMsg = "Database connection error:" + e.getMessage() + ".";

            if (e.getMessage() != null && e.getMessage().startsWith("java.lang.ClassNotFoundException")) {
                errMsg = "Database driver " + e.getMessage().substring(e.getMessage().indexOf(':') + 2) + " not found.";
                return getErrorForward(mapping, errMsg);
            } else {
                return handleAjaxErrOrForwardErr(mapping, response, ajaxResponse, errMsg);
            }
        }

        // >> 5. Successful Login Handling
        if (authResult != null && authResult.hasNoError()) { // login successfully

            // is the provider record inactive?
            Provider p = this.providerDao.getProvider(authResult.getProviderNo());
            if (p == null || (p.getStatus() != null && p.getStatus().equals("0"))) {
                logger.info(LOG_PRE + " Inactive: " + userName);
                LogAction.addLog(authResult.getProviderNo(), "login", "failed", "inactive");

                return getErrorForward(mapping, "Your account is inactive. Please contact your administrator to activate.");
            }

            /*
             * This section is added for forcing the initial password change.
             */
            Security security = getSecurity(userName);
            if (isForcePasswordChangeRequired(security, forcedpasswordchange)) {
                try {
                    setUserInfoToSession(request, userName, password, pin, nextPage);
                    return new ActionForward(mapping.findForward("forcepasswordreset").getPath());
                } catch (Exception e) {
                    logger.error("Error", e);
                    return getErrorForward(mapping, "Setting values to the session.");
                }

            }

            // ################----------------------->
            // REMOVE AFTER TESTING IS DONE.
            // if(true) {
            // String[] authenticationParameters = new String[]{userName, password, pin,
            // ip};
            // Map<String, Object> sessionData = ssoAuthenticationManager.checkLogin(new
            // HashMap<>(), authenticationParameters);
            // HttpSession newSession = request.getSession(true);
            //
            // logger.debug("New session created: " + newSession.getId());
            //
            // newSession.setMaxInactiveInterval(7200);
            // newSession.setAttribute("oscar_context_path", request.getContextPath());
            //
            // // full site or mobile
            // newSession.setAttribute("fullSite", "true");
            //
            // for (String key : sessionData.keySet()) {
            // newSession.setAttribute(key, sessionData.get(key));
            // }
            //
            // return mapping.findForward("provider");
            // }
            // <-----------------------################

            /*
             * User has authenticated in OSCAR at this point.
             * The following will redirect to the selected IDP for
             * an authentication request if SSO is enabled
             * The remainder of the login process will be handled through the
             * SSOLoginAction class.
             *
             */
            if (SSOUtility.isSSOEnabled()) {
                ActionRedirect redirect = new ActionRedirect(mapping.findForward("ssoLogin").getPath());
                redirect.addParameter("user_email", authResult.getEmail());
                return redirect;
            }

            // invalidate the existing session
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (request.getParameter("invalidate_session") != null
                        && request.getParameter("invalidate_session").equals("false")) {
                    // don't invalidate in this case it messes up authenticity of OAUTH
                } else {
                    session.invalidate();
                }
            }
            session = request.getSession(); // Create a new session for this user
            session.setMaxInactiveInterval(7200); // 2 hours

            // If the ondIdKey parameter is not null and is not an empty string
            if (oneIdKey != null && !oneIdKey.equals("")) {
                String providerNumber = authResult.getProviderNo();
                Security securityRecord = this.securityDao.getByProviderNo(providerNumber);

                if (securityRecord.getOneIdKey() == null || securityRecord.getOneIdKey().equals("")) {
                    securityRecord.setOneIdKey(oneIdKey);
                    securityRecord.setOneIdEmail(oneIdEmail);
                    this.securityDao.updateOneIdKey(securityRecord);
                    session.setAttribute("oneIdEmail", oneIdEmail);
                } else {
                    logger.error("The account for provider number " + providerNumber
                            + " already has a ONE ID key associated with it");
                    return mapping.findForward("error");
                }
            }

            logger.debug("Assigned new session for: " + authResult.getProviderNo() + " : " + authResult.getLastname() + " : " + authResult.getRoleName());
            LogAction.addLog(authResult.getProviderNo(), LogConst.LOGIN, LogConst.CON_LOGIN, "", ip);

            // initial db setting
            Properties pvar = OscarProperties.getInstance();

            String providerNo = authResult.getProviderNo();
            session.setAttribute("user", authResult.getProviderNo());
            session.setAttribute("userfirstname", authResult.getFirstname());
            session.setAttribute("userlastname", authResult.getLastname());
            session.setAttribute("userrole", authResult.getRoleName());
            session.setAttribute("oscar_context_path", request.getContextPath());
            session.setAttribute("expired_days", authResult.getExpiredDays());
            // If a new session has been created, we must set the mobile attribute again
            if (isMobileOptimized) {
                if ("Full".equalsIgnoreCase(submitType)) {
                    session.setAttribute("fullSite", "true");
                } else {
                    session.setAttribute("mobileOptimized", "true");
                }
            }

            // initiate security manager
            String default_pmm = null;

            // get preferences from preference table
            ProviderPreference providerPreference = this.providerPreferenceDao.find(providerNo);

            if (providerPreference == null)
                providerPreference = new ProviderPreference();

            session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE, providerPreference);

            if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable()) {
                String tklerProviderNo = null;
                UserProperty prop = this.propDao.getProp(providerNo, UserProperty.PROVIDER_FOR_TICKLER_WARNING);
                if (prop == null) {
                    tklerProviderNo = providerNo;
                } else {
                    tklerProviderNo = prop.getValue();
                }
                session.setAttribute("tklerProviderNo", tklerProviderNo);

                session.setAttribute("newticklerwarningwindow", providerPreference.getNewTicklerWarningWindow());
                session.setAttribute("default_pmm", providerPreference.getDefaultCaisiPmm());
                session.setAttribute("caisiBillingPreferenceNotDelete",
                        String.valueOf(providerPreference.getDefaultDoNotDeleteBilling()));

                default_pmm = providerPreference.getDefaultCaisiPmm();
                @SuppressWarnings("unchecked")
                ArrayList<String> newDocArr = (ArrayList<String>) request.getSession().getServletContext()
                        .getAttribute("CaseMgmtUsers");
                if ("enabled".equals(providerPreference.getDefaultNewOscarCme())) {
                    newDocArr.add(providerNo);
                    session.setAttribute("CaseMgmtUsers", newDocArr);
                }
            }
            session.setAttribute("starthour", providerPreference.getStartHour().toString());
            session.setAttribute("endhour", providerPreference.getEndHour().toString());
            session.setAttribute("everymin", providerPreference.getEveryMin().toString());
            session.setAttribute("groupno", providerPreference.getMyGroupNo());

            where = "provider";

            if (where.equals("provider") && "enabled".equals(default_pmm)) {
                where = "caisiPMM";
            }

            if (where.equals("provider")
                    && OscarProperties.getInstance().getProperty("useProgramLocation", "false").equals("true")) {
                where = "programLocation";
            }

            String quatroShelter = OscarProperties.getInstance().getProperty("QUATRO_SHELTER");
            if (quatroShelter != null && quatroShelter.equals("on")) {
                where = "shelterSelection";
            }

            /*
             * if (OscarProperties.getInstance().isTorontoRFQ()) { where = "caisiPMM"; }
             */
            // Lazy Loads AlertTimer instance only once, will run as daemon for duration of
            // server runtime
            if (pvar.getProperty("billregion").equals("BC")) {
                String alertFreq = pvar.getProperty("ALERT_POLL_FREQUENCY");
                if (alertFreq != null) {
                    Long longFreq = new Long(alertFreq);
                    String[] alertCodes = OscarProperties.getInstance().getProperty("CDM_ALERTS").split(",");
                    AlertTimer.getInstance(alertCodes, longFreq.longValue());
                }
            }

            String username = (String) session.getAttribute("user");
            Provider provider = this.providerManager.getProvider(username);
            session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER, provider);
            session.setAttribute(SessionConstants.LOGGED_IN_SECURITY, cl.getSecurity());

            LoggedInInfo loggedInInfo = LoggedInUserFilter.generateLoggedInInfoFromSession(request);

            if (where.equals("provider")) {
                UserProperty drugrefProperty = this.propDao.getProp(UserProperty.MYDRUGREF_ID);
                if (drugrefProperty != null || this.appManager.isK2AUser(loggedInInfo)) {
                    this.dsService.fetchGuidelinesFromServiceInBackground(loggedInInfo);
                }
            }

            List<Integer> facilityIds = this.providerDao.getFacilityIds(provider.getProviderNo());
            if (facilityIds.size() > 1) {
                return (new ActionForward("/select_facility.jsp?nextPage=" + where));
            } else if (facilityIds.size() == 1) {
                // set current facility
                Facility facility = this.facilityDao.find(facilityIds.get(0));
                request.getSession().setAttribute("currentFacility", facility);
                LogAction.addLog(authResult.getProviderNo(), LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId=" + facilityIds.get(0),
                        ip);
                if (facility.isEnableOcanForms()) {
                    request.getSession().setAttribute("ocanWarningWindow",
                            OcanForm.getOcanWarningMessage(facility.getId()));
                }
                if (facility.isEnableCbiForm()) {
                    request.getSession().setAttribute("cbiReminderWindow",
                            CBIUtil.getCbiSubmissionFailureWarningMessage(facility.getId(), provider.getProviderNo()));
                }
            } else {
                List<Facility> facilities = this.facilityDao.findAll(true);
                if (facilities != null && facilities.size() >= 1) {
                    Facility fac = facilities.get(0);
                    int first_id = fac.getId();
                    this.providerDao.addProviderToFacility(providerNo, first_id);
                    Facility facility = this.facilityDao.find(first_id);
                    request.getSession().setAttribute("currentFacility", facility);
                    LogAction.addLog(authResult.getProviderNo(), LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId=" + first_id, ip);
                }
            }

            if (UserRoleUtils.hasRole(request, "Patient Intake")) {
                return mapping.findForward("patientIntake");
            }

        }

        // >> 6. Authentication Failure Handling
        // expired password
        else if (authResult != null && authResult.isAccountExpired()) {
            logger.warn("Expired password");
            cl.updateLoginList(ip, userName);

            String errMsg = "Your account is expired. Please contact your administrator.";

            return handleAjaxErrOrForwardErr(mapping, response, ajaxResponse, errMsg);
        } else {
            logger.debug("go to normal directory");

            cl.updateLoginList(ip, userName);

            String errMsg = "Invalid Credentials.";

            if (ajaxResponse) {
                return handleAjaxError(response, errMsg);
            }

            ParameterActionForward forward = new ParameterActionForward(mapping.findForward(where));
            forward.addParameter("login", "failed");
            if (oneIdKey != null && !oneIdKey.equals("")) {
                forward.addParameter("nameId", oneIdKey);
            }

            return forward;
        }

        // >> 7. OAuth Token Handling
        if (request.getParameter("oauth_token") != null) {
            logger.debug("checking oauth_token");
            String proNo = (String) request.getSession().getAttribute("user");
            ServiceRequestToken srt = this.serviceRequestTokenDao.findByTokenId(request.getParameter("oauth_token"));
            if (srt != null) {
                srt.setProviderNo(proNo);
                this.serviceRequestTokenDao.merge(srt);
            }
        }

        // >> 8. AJAX Response Handling
        if (ajaxResponse) {
            logger.debug("rendering ajax response");
            Provider prov = this.providerDao.getProvider((String) request.getSession().getAttribute("user"));
            JSONObject json = new JSONObject();
            json.put("success", true);
            json.put("providerName", Encode.forJavaScript(prov.getFormattedName()));
            json.put("providerNo", prov.getProviderNo());
            response.setContentType("text/x-json");
            json.write(response.getWriter());
            return null;
        }

        // >> 9. Standard Response Handling
        logger.debug("rendering standard response : " + where);
        return mapping.findForward(where);
    }

    private static boolean isForcePasswordChangeRequired(Security security, boolean forcedpasswordchange) {
        return !OscarProperties.getInstance().getBooleanProperty("mandatory_password_reset", "false") &&
                security.isForcePasswordReset() != null && security.isForcePasswordReset()
                && forcedpasswordchange;
    }

    private InitialChecksResult performInitialChecks(ActionMapping mapping, HttpServletRequest request) {

        if (!"POST".equals(request.getMethod())) {
            MiscUtils.getLogger().error("Someone is trying to login with a GET request.", new Exception());
            return new InitialChecksResult(getErrorForward(mapping, "Application Error. See Log."));
        }

        boolean ajaxResponse = request.getParameter("ajaxResponse") != null ? Boolean.valueOf(request.getParameter("ajaxResponse")) : false;
        boolean isMobileOptimized;

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("user-agent");
        String accept = request.getHeader("Accept");

        UAgentInfo userAgentInfo = new UAgentInfo(userAgent, accept);
        isMobileOptimized = userAgentInfo.detectMobileQuick();

        // override by the user login.
        String submitType = request.getParameter("submit");

        if ("full".equalsIgnoreCase(submitType)) {
            isMobileOptimized = false;
        }

        boolean forcedpasswordchange = request.getParameter("forcedpasswordchange") != null
                && request.getParameter("forcedpasswordchange").equalsIgnoreCase("true");

        return new InitialChecksResult(ajaxResponse, isMobileOptimized, ip, submitType, forcedpasswordchange);
    }

    private PasswordChangeResult handleForcedPasswordChange(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws IOException {
        // Coming back from force password change.
        String userName = (String) request.getSession().getAttribute("userName");

        // Username is only letters and numbers
        if (!Pattern.matches("[a-zA-Z0-9]{1,10}", userName)) {
            userName = "Invalid Username";
        }

        String password = (String) request.getSession().getAttribute("password");

        String pin = (String) request.getSession().getAttribute("pin");

        // pins are integers only
        if (!Pattern.matches("[0-9]{4}", pin)) {
            pin = "";
        }
        String nextPage = (String) request.getSession().getAttribute("nextPage");

        String newPassword = ((LoginForm) form).getNewPassword();
        String confirmPassword = ((LoginForm) form).getConfirmPassword();
        String oldPassword = ((LoginForm) form).getOldPassword();

        try {
            String errorStr = errorHandling(password, newPassword, confirmPassword, oldPassword);

            // Error Handling
            if (errorStr != null && !errorStr.isEmpty()) {
                String newURL = mapping.findForward("forcepasswordreset").getPath();
                newURL = newURL + errorStr;
                return new PasswordChangeResult(new ActionForward(newURL));
            }

            persistNewPassword(userName, newPassword);

            password = newPassword;

            // Remove the attributes from session
            removeAttributesFromSession(request);
        } catch (Exception e) {
            logger.error("Error", e);

            // Remove the attributes from session
            removeAttributesFromSession(request);

            return new PasswordChangeResult(getErrorForward(mapping, "Setting values to the session."));
        }

        return new PasswordChangeResult(userName, password, pin, nextPage);
    }

    private LoginInfoResult initializePreLoginInfo(HttpServletRequest request, ActionForm form) {

        LoginForm loginForm = (LoginForm) form;
        String userName = loginForm.getUsername();
        String password = loginForm.getPassword();
        String pin = loginForm.getPin();

        String nextPage = request.getParameter("nextPage");

        // Username is only letters and numbers
        if (!Pattern.matches("[a-zA-Z0-9]{1,10}", userName)) {
            userName = "Invalid Username";
        }

        // pins are integers only
        if (!Pattern.matches("[0-9]{4}", pin)) {
            pin = "";
        }

        return new LoginInfoResult(userName, password, pin, nextPage);
    }

    private ActionForward getSelectFacilityForward(ActionMapping mapping, HttpServletRequest request, String ip, String nextPage) {
        String facilityIdString = request.getParameter(SELECTED_FACILITY_ID);
        Facility facility = this.facilityDao.find(Integer.parseInt(facilityIdString));
        request.getSession().setAttribute(SessionConstants.CURRENT_FACILITY, facility);
        String username = (String) request.getSession().getAttribute("user");
        LogAction.addLog(username, LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId=" + facilityIdString, ip);
        if (facility.isEnableOcanForms()) {
            request.getSession().setAttribute("ocanWarningWindow",
                    OcanForm.getOcanWarningMessage(facility.getId()));
        }
        return mapping.findForward(nextPage);
    }

    private AuthResult authenticate(LoginCheckLogin cl, String username, String password, String pin, String ip) throws Exception {
        String[] strAuth;

        /*
         * the pin code is not required for SSO IDP.
         */
        if (SSOUtility.isSSOEnabled()) {
            strAuth = cl.auth(username, password, ip);
        } else {
            strAuth = cl.auth(username, password, pin, ip);
        }

        logger.debug("strAuth : " + Arrays.toString(strAuth));

        if (strAuth != null && strAuth.length != 1) {
            return new AuthResult(strAuth);
        }

        return null;
    }

    /**
     * Removes attributes from session
     *
     * @param request
     */
    private void removeAttributesFromSession(HttpServletRequest request) {
        request.getSession().removeAttribute("userName");
        request.getSession().removeAttribute("password");
        request.getSession().removeAttribute("pin");
        request.getSession().removeAttribute("nextPage");
    }

    /**
     * Set user info to session
     *
     * @param request
     * @param userName
     * @param password
     * @param pin
     * @param nextPage
     */
    private void setUserInfoToSession(HttpServletRequest request, String userName, String password, String pin,
                                      String nextPage) throws Exception {
        request.getSession().setAttribute("userName", userName);
        request.getSession().setAttribute("password", this.securityManager.encodePassword(password));
        request.getSession().setAttribute("pin", pin);
        request.getSession().setAttribute("nextPage", nextPage);

    }

    /**
     * Performs the error handling
     *
     * @param oldEncodedPassword
     * @param newPassword
     * @param confirmPassword
     * @param oldPassword
     * @return
     */
    private String errorHandling(String oldEncodedPassword, String newPassword, String confirmPassword,
                                 String oldPassword) {

        String newURL = "";

        if (!this.securityManager.matchesPassword(oldPassword, oldEncodedPassword)) {
            newURL = newURL
                    + "?errormsg=Your old password, does NOT match the password in the system. Please enter your old password.";
        } else if (!newPassword.equals(confirmPassword)) {
            newURL = newURL + "?errormsg=Your new password, does NOT match the confirmed password. Please try again.";
        } else if (!Boolean.parseBoolean(OscarProperties.getInstance().getProperty("IGNORE_PASSWORD_REQUIREMENTS"))
                && newPassword.equals(oldPassword)) {
            newURL = newURL
                    + "?errormsg=Your new password, is the same as your old password. Please choose a new password.";
        }
        return newURL;
    }

    /**
     * get the security record based on the username
     *
     * @param username
     * @return
     */
    private Security getSecurity(String username) {

        List<Security> results = this.securityDao.findByUserName(username);
        Security security = null;
        if (results.size() > 0)
            security = results.get(0);

        if (security == null) {
            return null;
        } else if (OscarProperties.isLdapAuthenticationEnabled()) {
            security = new LdapSecurity(security);
        }

        return security;
    }

    /**
     * Persists the new password
     *
     * @param userName
     * @param newPassword
     * @return
     */
    private void persistNewPassword(String userName, String newPassword) throws Exception {

        Security security = getSecurity(userName);
        security.setPassword(this.securityManager.encodePassword(newPassword));
        security.setForcePasswordReset(Boolean.FALSE);
        this.securityDao.saveEntity(security);

    }

    public ApplicationContext getAppContext() {
        return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
    }

    private static class InitialChecksResult {
        public ActionForward errForward;
        public boolean ajaxResponse;
        public boolean isMobileOptimized;
        public String ip;
        public String submitType;
        public boolean forcedpasswordchange;

        public InitialChecksResult(boolean ajaxResponse, boolean isMobileOptimized, String ip, String submitType, boolean forcedpasswordchange) {
            this.ajaxResponse = ajaxResponse;
            this.isMobileOptimized = isMobileOptimized;
            this.ip = ip;
            this.submitType = submitType;
            this.forcedpasswordchange = forcedpasswordchange;
        }

        public InitialChecksResult(ActionForward errorForward) {
            this.errForward = errorForward;
        }
    }

    private static class PasswordChangeResult extends BaseResult {

        public PasswordChangeResult(String username, String password, String pin, String nextPage) {
            super(username, password, pin, nextPage);
        }

        public PasswordChangeResult(ActionForward errForward) {
            super(errForward);
        }
    }

    private static class LoginInfoResult extends BaseResult {

        public LoginInfoResult(String username, String password, String pin, String nextPage) {
            super(username, password, pin, nextPage);
        }
    }

    private static class BaseResult {
        String username;
        String password;
        String pin;
        String nextPage;
        ActionForward errForward;

        public BaseResult(String username, String password, String pin, String nextPage) {
            this.username = username;
            this.password = password;
            this.pin = pin;
            this.nextPage = nextPage;
        }

        public BaseResult(ActionForward errForward) {
            this.errForward = errForward;
        }
    }
}
