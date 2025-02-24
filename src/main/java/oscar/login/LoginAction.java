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
import org.oscarehr.common.IsPropertiesOn;
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

    /**
     * Handles an error by returning either an AJAX response or a standard error forward,
     * depending on the `isAjaxResponse` flag.
     * @param mapping The ActionMapping used to find the error forward.
     * @param response The HttpServletResponse to write the AJAX error message to.
     * @param isAjaxResponse A boolean indicating whether the response should be in AJAX format.
     * @param errMsg The error message to return.
     * @return An ActionForward for standard requests or null for AJAX requests.
     *         If a database connection error occurs or the specified database driver is not found,
     *         an ActionForward will also be returned.
     * @throws IOException If an I/O error occurs while writing the AJAX response.
     */
    private static ActionForward handleAjaxErrOrForwardErr(ActionMapping mapping, HttpServletResponse response, boolean isAjaxResponse, String errMsg) throws IOException {
        if (isAjaxResponse) {
            return handleAjaxError(response, errMsg);
        } else {
            return getErrorForward(mapping, errMsg);
        }
    }

    /**
     * Handles an AJAX error by writing a JSON error message to the HttpServletResponse.
     *
     * @param response The HttpServletResponse to write the JSON error message to.
     * @param errMsg The error message to include in the JSON response.
     * @return null, as the response is handled directly within this method.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    private static ActionForward handleAjaxError(HttpServletResponse response, String errMsg) throws IOException {
        JSONObject json = new JSONObject();
        json.put("success", false);
        json.put("error", errMsg);
        response.setContentType("text/x-json");
        json.write(response.getWriter());
        return null;
    }

    /**
     * Retrieves the ActionForward for the "error" path, appending the provided error message
     * as a query parameter.
     *
     * @param mapping  The ActionMapping used to locate the "error" forward.
     * @param errMsg The error message to append to the forward URL.
     * @return An ActionForward representing the "error" view with the error message attached.
     */
    private static ActionForward getErrorForward(ActionMapping mapping, String errMsg) {
        String url = mapping.findForward("error").getPath();
        return new ActionForward(url + "?errormsg=" + errMsg);
    }

    /**
     * Initializes the AlertTimer, which periodically manage CDM alerts.
     * This method is called only once during login and the timer runs as a daemon thread.
     * This is currently active for BC users based on the "billregion" property.
     * The polling frequency and the alert codes are configured in properties using
     * the "ALERT_POLL_FREQUENCY" and "CDM_ALERTS" keys respectively.
     */
    private static void initializeAlertTimer() {
        Properties pvar = OscarProperties.getInstance();
        if (pvar.getProperty("billregion").equals("BC")) {
            String alertFreq = pvar.getProperty("ALERT_POLL_FREQUENCY");
            if (alertFreq != null) {
                Long longFreq = new Long(alertFreq);
                String[] alertCodes = OscarProperties.getInstance().getProperty("CDM_ALERTS").split(",");
                AlertTimer.getInstance(alertCodes, longFreq.longValue());
            }
        }
    }

    /**
     * Validates the provided PIN against a 4-digit numeric pattern.
     *
     * @param pin The PIN string to validate.
     * @return The validated PIN string if it matches the pattern, or an empty string otherwise.
     */
    private static String validatePinPattern(String pin) {
        return validatePattern("[0-9]{4}", pin, "");
    }

    /**
     * Validates the provided username against a pattern allowing only alphanumeric characters
     * with a length between 1 and 10 characters.
     *
     * @param userName The username string to validate.
     * @return The validated username string if it matches the pattern,
     *         or "Invalid Username" otherwise.
     */
    private static String validateUsernamePattern(String userName) {
        return validatePattern("[a-zA-Z0-9]{1,10}", userName, "Invalid Username");
    }

    /**
     * Validates the provided `value` against the given regular expression `patternVal`.
     * If the value matches the pattern, it is returned. Otherwise, the `defaultVal` is returned.
     *
     * @param patternVal The regular expression pattern to validate against.
     * @param value      The string value to validate.
     * @param defaultVal The default value to return if the validation fails.
     * @return The validated string if it matches the pattern, or the `defaultVal` otherwise.
     */
    private static String validatePattern(String patternVal, String value, String defaultVal) {
        if (!Pattern.matches(patternVal, value)) {
            value = defaultVal;
        }
        return value;
    }

    /**
     * TODO: for the love of god - please help me clean-up this nightmare
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // >> 1. Initial Checks and Mobile Detection
        InitialChecksResult initialChecksResult = this.performInitialChecks(mapping, request);
        if (initialChecksResult.errForward != null) {
            return initialChecksResult.errForward;
        }

        LoginCheckLogin cl = new LoginCheckLogin();

        UserLoginInfo userLoginInfo;

        // >> 2. Forced Password Change Handling
        if (initialChecksResult.isForcePasswordChangeNeeded) {

            PasswordChangeResult passwordChangeResult = this.processForcedPasswordChange(mapping, form, request);

            if (passwordChangeResult.actionForward != null) {
                return passwordChangeResult.actionForward;
            }

            userLoginInfo = passwordChangeResult;

            // make sure this checking doesn't happen again
            initialChecksResult.isForcePasswordChangeNeeded = false;
        }
        // >> 3. Standard Login Attempt
        else {
            LoginAttemptResult loginAttemptResult = this.handleLoginAttempt(mapping, form, request, response, cl, initialChecksResult);

            // consider actionForward as Error-ActionForward
            if (loginAttemptResult.actionForward != null) {
                return loginAttemptResult.actionForward;
            }

            if (loginAttemptResult.selectFacilityForward != null) {
                return loginAttemptResult.selectFacilityForward;
            }

            userLoginInfo = loginAttemptResult;
        }

        logger.debug("ip was not blocked: " + initialChecksResult.ip);

        String oneIdKey = request.getParameter("nameId");
        String oneIdEmail = request.getParameter("email");

        String where = "failure";

        // >> 4. Authentication
        /*
         * THIS IS THE GATEWAY.
         */

        AuthResultWrapper authResultWrapper = this.authenticateUser(mapping, response, cl, userLoginInfo, initialChecksResult);
        if (authResultWrapper.actionForward != null) {
            return authResultWrapper.actionForward;
        }
        AuthResult authResult = authResultWrapper.authResult;

        // >> 5. Successful Login Handling
        if (authResult != null && authResult.hasNoError()) { // login successfully

            // is the provider record inactive?
            GenericResult providerStatusResult = this.isProviderActive(mapping, authResult, userLoginInfo.username);
            if (providerStatusResult != null && providerStatusResult.actionForward != null)
                return providerStatusResult.actionForward;

            /*
             * This section is added for forcing the initial password change.
             */
            GenericResult forcePasswordChangeRequiredResult = this.isForcePasswordChangeNeeded(mapping, request,
                    userLoginInfo, initialChecksResult.isForcePasswordChangeNeeded);
            if (forcePasswordChangeRequiredResult != null)
                return forcePasswordChangeRequiredResult.actionForward;

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
                boolean isOneIdProcessError = this.processOneIdFlow(authResult, oneIdKey, oneIdEmail, session);
                if (isOneIdProcessError) return mapping.findForward("error");
            }

            logger.debug("Assigned new session for: " + authResult.getProviderNo() + " : " + authResult.getLastname() + " : " + authResult.getRoleName());
            LogAction.addLog(authResult.getProviderNo(), LogConst.LOGIN, LogConst.CON_LOGIN, "", initialChecksResult.ip);

            // initial db setting

            this.setAuthResultToSession(session, authResult);
            session.setAttribute("oscar_context_path", request.getContextPath());
            // If a new session has been created, we must set the mobile attribute again
            if (initialChecksResult.isMobileOptimized) {
                if ("Full".equalsIgnoreCase(initialChecksResult.submitType)) {
                    session.setAttribute("fullSite", "true");
                } else {
                    session.setAttribute("mobileOptimized", "true");
                }
            }

            // initiate security manager

            String providerNo = authResult.getProviderNo();

            String default_pmm = this.processProviderUserConfiguration(session, providerNo);

            where = this.getWhere(default_pmm);

            /*
             * if (OscarProperties.getInstance().isTorontoRFQ()) { where = "caisiPMM"; }
             */
            // Lazy Loads AlertTimer instance only once, will run as daemon for duration of
            // server runtime
            initializeAlertTimer();

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

            ActionForward selectFacilityForward = this.processFacilitySelectionFlow(provider, where, session, authResult, initialChecksResult, providerNo);
            if (selectFacilityForward != null)
                return selectFacilityForward;

            if (UserRoleUtils.hasRole(request, "Patient Intake")) {
                return mapping.findForward("patientIntake");
            }

        }

        // >> 6. Authentication Failure Handling
        // expired password
        else if (authResult != null && authResult.isAccountExpired()) {
            return this.getExpiredPasswordForward(mapping, response, cl, initialChecksResult, userLoginInfo);
        } else {
            return this.getLoginErrorActionForward(mapping, response, cl, initialChecksResult, userLoginInfo, where, oneIdKey);
        }

        // >> 7. OAuth Token Handling
        if (request.getParameter("oauth_token") != null) {
            this.handleOAuthToken(request);
        }

        // >> 8. AJAX Response Handling
        if (initialChecksResult.isAjaxResponse) {
            return this.handleSuccessAjaxResponse(request, response);
        }

        // >> 9. Standard Response Handling
        logger.debug("rendering standard response : " + where);
        return mapping.findForward(where);
    }

    /**
     * Sets user information retrieved from authentication to the HTTP session.
     *
     * @param session The HTTP session to store the user information.
     * @param authResult The AuthResult object containing the authenticated user's information.
     */
    private void setAuthResultToSession(HttpSession session, AuthResult authResult) {
        session.setAttribute("user", authResult.getProviderNo());
        session.setAttribute("userfirstname", authResult.getFirstname());
        session.setAttribute("userlastname", authResult.getLastname());
        session.setAttribute("userrole", authResult.getRoleName());
        session.setAttribute("expired_days", authResult.getExpiredDays());
    }

    /**
     * Processes and sets up session attributes related to provider user configuration.
     * This includes retrieving provider preferences, setting start/end hours, group number, and
     * handling Caisi specific properties if enabled.
     *
     * @param session The HttpSession to store the configuration attributes.
     * @param providerNo The provider number for whom to retrieve preferences.
     * @return The default PMM setting if Caisi is enabled, otherwise null.
     */
    private String processProviderUserConfiguration(HttpSession session, String providerNo) {
        String default_pmm = null;
        // get preferences from preference table
        ProviderPreference providerPreference = this.providerPreferenceDao.find(providerNo);

        if (providerPreference == null)
            providerPreference = new ProviderPreference();

        session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE, providerPreference);

        if (IsPropertiesOn.isCaisiEnable()) {
            default_pmm = this.processCaisiProperties(providerNo, session, providerPreference);
        }
        session.setAttribute("starthour", providerPreference.getStartHour().toString());
        session.setAttribute("endhour", providerPreference.getEndHour().toString());
        session.setAttribute("everymin", providerPreference.getEveryMin().toString());
        session.setAttribute("groupno", providerPreference.getMyGroupNo());
        return default_pmm;
    }

    /**
     * Processes Caisi specific properties and sets them in the session.  This includes
     * determining the provider number for tickler warnings, setting the new tickler
     * warning window preference, default PMM setting, billing preference for deletion,
     * and handling the Case Management user list.
     *
     * @param providerNo The provider number for whom to process Caisi properties.
     * @param session The HttpSession to store the Caisi properties.
     * @param providerPreference The provider's preferences.
     * @return The default PMM setting retrieved from provider preferences.
     */
    private String processCaisiProperties(String providerNo, HttpSession session, ProviderPreference providerPreference) {
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

        @SuppressWarnings("unchecked")
        ArrayList<String> newDocArr = (ArrayList<String>) session.getServletContext()
                .getAttribute("CaseMgmtUsers");
        if ("enabled".equals(providerPreference.getDefaultNewOscarCme())) {
            newDocArr.add(providerNo);
            session.setAttribute("CaseMgmtUsers", newDocArr);
        }
        return providerPreference.getDefaultCaisiPmm();
    }

    /**
     * Determines the appropriate view to forward to after login, based on the default PMM setting
     * and other configured properties.
     *
     * @param default_pmm The default PMM setting for the user.
     * @return The name of the view to forward to (e.g., "provider", "caisiPMM", "programLocation", "shelterSelection").
     */
    private String getWhere(String default_pmm) {
        String where = "provider";

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
        return where;
    }

    /**
     * Processes the facility selection flow for a user. If the provider is associated with multiple facilities,
     * it redirects to the facility selection page. If associated with a single facility, it sets the current
     * facility in the session. If not associated with any facility, it adds the provider to the first available
     * facility and sets it as the current facility. It also logs the facility selection and handles OCAN/CBI
     * form enablement based on the selected facility.
     *
     * @param provider            The Provider object representing the logged-in provider.
     * @param where              The target view to redirect to after facility selection.
     * @param session            The HttpSession to store the selected facility.
     * @param authResult          The authentication result containing provider details.
     * @param initialChecksResult The initial checks result containing client IP information for logging.
     * @param providerNo         The provider number.
     * @return An ActionForward to the facility selection page if multiple facilities are available, or null
     * if the facility is automatically selected and set in the session.
     */
    private ActionForward processFacilitySelectionFlow(Provider provider, String where, HttpSession session, AuthResult authResult, InitialChecksResult initialChecksResult, String providerNo) {
        List<Integer> facilityIds = this.providerDao.getFacilityIds(provider.getProviderNo());
        if (facilityIds.size() > 1) {
            return (new ActionForward("/select_facility.jsp?nextPage=" + where));
        } else if (facilityIds.size() == 1) {
            // set current facility
            Facility facility = this.facilityDao.find(facilityIds.get(0));
            session.setAttribute("currentFacility", facility);
            LogAction.addLog(authResult.getProviderNo(), LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId=" + facilityIds.get(0),
                    initialChecksResult.ip);
            if (facility.isEnableOcanForms()) {
                session.setAttribute("ocanWarningWindow",
                        OcanForm.getOcanWarningMessage(facility.getId()));
            }
            if (facility.isEnableCbiForm()) {
                session.setAttribute("cbiReminderWindow",
                        CBIUtil.getCbiSubmissionFailureWarningMessage(facility.getId(), provider.getProviderNo()));
            }
        } else {
            Facility facility = this.getFacilityByProviderNumber(providerNo, authResult, initialChecksResult);
            if (facility != null)
                session.setAttribute("currentFacility", facility);

        }
        return null;
    }

    /**
     * Retrieves the first available active facility and adds the provider to it
     * if the provider is not already associated with any facility.
     *
     * @param providerNo The provider number.
     * @param authResult The authentication result containing provider details. Used for logging purposes.
     * @param initialChecksResult The initial checks result containing client IP information for logging.
     * @return The Facility object that the provider was added to, or null if no active facilities exist.
     */
    private Facility getFacilityByProviderNumber(String providerNo, AuthResult authResult, InitialChecksResult initialChecksResult) {
        List<Facility> facilities = this.facilityDao.findAll(true);
        if (facilities != null && facilities.size() >= 1) {
            Facility fac = facilities.get(0);
            int first_id = fac.getId();
            this.providerDao.addProviderToFacility(providerNo, first_id);
            Facility facility = this.facilityDao.find(first_id);
            LogAction.addLog(authResult.getProviderNo(), LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId=" + first_id, initialChecksResult.ip);
            return facility;
        }
        return null;
    }

    /**
     * Processes the ONE ID authentication flow. This method updates the user's security record with the provided
     * ONE ID key and email, if the user doesn't already have a ONE ID key associated with their account.
     * It also sets the ONE ID email in the session.
     *
     * @param authResult The authentication result containing the provider number.
     * @param oneIdKey The ONE ID key to associate with the user.
     * @param oneIdEmail The ONE ID email to associate with the user.
     * @param session The HTTP session.
     * @return `true` if an error occurs during processing (e.g., the user already has a ONE ID key), `false` otherwise.
     */
    private boolean processOneIdFlow(AuthResult authResult, String oneIdKey, String oneIdEmail, HttpSession session) {
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
            return true;
        }
        return false;
    }

    /**
     * Handles login errors by updating the login attempt list, logging the failure,
     * and returning an appropriate ActionForward or AJAX response.
     *
     * @param mapping The ActionMapping for this request.
     * @param response The HttpServletResponse for this request.
     * @param cl The LoginCheckLogin object for managing login attempts.
     * @param initialChecksResult The result of initial login checks.
     * @param userLoginInfo The user's login information.
     * @param where The target view to forward to after login.
     * @param oneIdKey The ONE ID key provided, if any.
     * @return An ActionForward to the appropriate error page or a null for an AJAX response.
     * @throws IOException If an I/O error occurs during AJAX response writing.
     */
    private ActionForward getLoginErrorActionForward(ActionMapping mapping, HttpServletResponse response, LoginCheckLogin cl, InitialChecksResult initialChecksResult, UserLoginInfo userLoginInfo, String where, String oneIdKey) throws IOException {
        logger.debug("go to normal directory");

        cl.updateLoginList(initialChecksResult.ip, userLoginInfo.username);

        String errMsg = "Invalid Credentials.";

        if (initialChecksResult.isAjaxResponse) {
            return handleAjaxError(response, errMsg);
        }

        ParameterActionForward forward = new ParameterActionForward(mapping.findForward(where));
        forward.addParameter("login", "failed");
        if (oneIdKey != null && !oneIdKey.equals("")) {
            forward.addParameter("nameId", oneIdKey);
        }

        return forward;
    }

    /**
     * Handles the case where a user's password has expired.  This logs the attempt, updates the login
     * attempt list, and returns an appropriate ActionForward or AJAX response.
     *
     * @param mapping The ActionMapping for this request.
     * @param response The HttpServletResponse for this request.
     * @param cl The LoginCheckLogin object for managing login attempts.
     * @param initialChecksResult The result of initial checks performed before login.  Contains information about whether the request is an AJAX request.
     * @param userLoginInfo The user's login information (username).
     * @return An ActionForward to the error page or a null for an AJAX response.
     * @throws IOException If an I/O error occurs during AJAX response writing.
     */
    private ActionForward getExpiredPasswordForward(ActionMapping mapping, HttpServletResponse response, LoginCheckLogin cl, InitialChecksResult initialChecksResult, UserLoginInfo userLoginInfo) throws IOException {
        logger.warn("Expired password");
        cl.updateLoginList(initialChecksResult.ip, userLoginInfo.username);

        String errMsg = "Your account is expired. Please contact your administrator.";

        return handleAjaxErrOrForwardErr(mapping, response, initialChecksResult.isAjaxResponse, errMsg);
    }

    /**
     * Authenticates the user using the provided credentials and handles any exceptions that may occur during the process.
     * This method acts as a wrapper around the `authenticateUser` method that performs the actual authentication,
     * catching any exceptions and returning an appropriate error response or forwarding.
     *
     * @param mapping The ActionMapping for this request, used for error forwarding.
     * @param response The HttpServletResponse for this request, used for writing error messages.
     * @param cl The LoginCheckLogin object used to perform the authentication.
     * @param userLoginInfo A UserLoginInfo object containing the user's login credentials.
     * @param initialChecksResult The results of the initial checks, including whether this is an AJAX request and the client's IP.
     * @return An AuthResultWrapper containing the AuthResult if authentication is successful, or an ActionForward to an error page if authentication fails or an exception occurs.
     * @throws IOException If an I/O error occurs while writing the error response.
     */
    private AuthResultWrapper authenticateUser(ActionMapping mapping, HttpServletResponse response, LoginCheckLogin cl, UserLoginInfo userLoginInfo, InitialChecksResult initialChecksResult) throws IOException {
        AuthResultWrapper authResultWrapper = new AuthResultWrapper();
        try {
            authResultWrapper.authResult = this.authenticateUser(cl, userLoginInfo, initialChecksResult.ip);
        } catch (Exception e) {
            logger.error("Error", e);
            String errMsg = "Database connection error:" + e.getMessage() + ".";

            if (e.getMessage() != null && e.getMessage().startsWith("java.lang.ClassNotFoundException")) {
                errMsg = "Database driver " + e.getMessage().substring(e.getMessage().indexOf(':') + 2) + " not found.";
                authResultWrapper.actionForward = getErrorForward(mapping, errMsg);
            } else {
                authResultWrapper.actionForward = handleAjaxErrOrForwardErr(mapping, response, initialChecksResult.isAjaxResponse, errMsg);
            }
        }

        return authResultWrapper;
    }

    /**
     * Checks if a forced password change is required for the given user.  This occurs when the
     * "mandatory_password_reset" property is false, the user's security record indicates a forced
     * password reset is needed, and the `forcedpasswordchange` flag is true.  If a forced password
     * change is required, user information is stored in the session and a GenericResult containing
     * a forward to the password reset page is returned.
     *
     * @param mapping The ActionMapping for the request, used to locate the "forcepasswordreset" forward.
     * @param request The HttpServletRequest, used for setting session attributes.
     * @param userLoginInfo The user's login information.
     * @param forcedpasswordchange A flag indicating if a forced password change is needed based on initial checks.
     * @return A GenericResult containing an ActionForward to the password reset page if a forced change
     *         is required, or null otherwise.
     */
    private GenericResult isForcePasswordChangeNeeded(ActionMapping mapping, HttpServletRequest request, UserLoginInfo userLoginInfo, boolean forcedpasswordchange) {
        if (this.isForcePasswordChangeRequired(userLoginInfo.username, forcedpasswordchange)) {
            try {
                this.setUserInfoToSession(request, userLoginInfo);
                return new GenericResult(mapping.findForward("forcepasswordreset").getPath());
            } catch (Exception e) {
                logger.error("Error", e);
                return new GenericResult(getErrorForward(mapping, "Setting values to the session."));
            }
        }
        return null;
    }

    /**
     * Checks if the provider associated with the given AuthResult is active.
     * If the provider is inactive, logs the failed login attempt and returns a GenericResult
     * containing an ActionForward to the error page.  If the provider is active or not found,
     * it implies success and returns null.
     *
     * @param mapping The ActionMapping for the request, used to locate the "error" forward.
     * @param authResult The AuthResult containing the authenticated user's information, including provider number.
     * @param username The username of the user attempting to login.  Used for logging.
     * @return A GenericResult object with an ActionForward to the error page if the provider is inactive,
     *         or null if the provider is active or not found.
     */
    private GenericResult isProviderActive(ActionMapping mapping, AuthResult authResult, String username) {
        Provider p = this.providerDao.getProvider(authResult.getProviderNo());
        if (p == null || (p.getStatus() != null && p.getStatus().equals("0"))) {
            logger.info(LOG_PRE + " Inactive: " + username);
            LogAction.addLog(authResult.getProviderNo(), "login", "failed", "inactive");

            return new GenericResult(getErrorForward(mapping, "Your account is inactive. Please contact your administrator to activate."));
        }
        return null;
    }

    /**
     * Handles the OAuth token received in the request. This method retrieves the provider number
     * from the current session and associates it with the provided OAuth token in the
     * ServiceRequestToken table. This links the OAuth token to the user.
     *
     * @param request The HttpServletRequest containing the OAuth token.
     */
    private void handleOAuthToken(HttpServletRequest request) {
        logger.debug("checking oauth_token");
        String proNo = (String) request.getSession().getAttribute("user");
        ServiceRequestToken srt = this.serviceRequestTokenDao.findByTokenId(request.getParameter("oauth_token"));
        if (srt != null) {
            srt.setProviderNo(proNo);
            this.serviceRequestTokenDao.merge(srt);
        }
    }

    /**
     * Handles a successful AJAX login by returning a JSON response containing the provider's name and number.
     *
     * @param request The HttpServletRequest containing the session information.
     * @param response The HttpServletResponse used to write the JSON response.
     * @return null, as the response is handled directly within this method.
     * @throws IOException If an I/O error occurs while writing the response.
     */
    private ActionForward handleSuccessAjaxResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    /**
     * Checks if a forced password change is required for the given user. This happens when:
     * <ul>
     *     <li>The "mandatory_password_reset" property is false.</li>
     *     <li>The user's security record indicates a forced password reset is needed.</li>
     *     <li>The forcedpasswordchange flag is true.</li>
     * </ul>
     *
     * @param username The username of the user to check.
     * @param forcedpasswordchange A flag indicating whether a forced password change check is needed.
     *                             This flag is typically set based on whether the user explicitly
     *                             requested a password change or if it's a regular login attempt.
     * @return `true` if a forced password change is required, `false` otherwise.
     */
    private boolean isForcePasswordChangeRequired(String username, boolean forcedpasswordchange) {
        Security security = this.getSecurity(username);
        return !OscarProperties.getInstance().getBooleanProperty("mandatory_password_reset", "false") &&
                security.isForcePasswordReset() != null && security.isForcePasswordReset()
                && forcedpasswordchange;
    }

    /**
     * Performs initial checks before attempting to log in a user. This includes:
     * <ul>
     *     <li>Verifying that the request method is POST.</li>
     *     <li>Checking for an AJAX request indicator.</li>
     *     <li>Detecting whether the request is from a mobile device.</li>
     *     <li>Retrieving the client's IP address.</li>
     *     <li>Determining if a forced password change is required.</li>
     * </ul>
     *
     * @param mapping The ActionMapping for the current request.  Used for forwarding in case of errors.
     * @param request The HttpServletRequest for the current request.  Used to access request parameters and headers.
     * @return An InitialChecksResult object containing the results of the checks, including:
     *         <ul>
     *              <li>`errForward`: An ActionForward to an error page if initial checks fail (e.g., wrong request method), NULL otherwise.</li>
     *              <li>`isAjaxResponse`: True if the request is an AJAX request, False otherwise.</li>
     *              <li>`isMobileOptimized`: True if the request is from a mobile device, False otherwise.</li>
     *              <li>`ip`: The client's IP address.</li>
     *              <li>`submitType`: full/null.</li>
     *              <li>`isForcePasswordChangeNeeded`: True if a forced password change is needed, False otherwise.</li>
     *         </ul>
     */
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

        boolean isForcePasswordChangeNeeded = request.getParameter("forcedpasswordchange") != null
                && request.getParameter("forcedpasswordchange").equalsIgnoreCase("true");

        return new InitialChecksResult(ajaxResponse, isMobileOptimized, ip, submitType, isForcePasswordChangeNeeded);
    }

    /**
     * Processes a forced password change request. Retrieves user credentials from the session,
     * validates the new password and confirmation, and updates the user's security record.
     *
     * @param mapping The ActionMapping used to locate the password reset forward and error forward.
     * @param form The ActionForm containing the new and confirm passwords.
     * @param request The HttpServletRequest, used to access session attributes.
     * @return A PasswordChangeResult containing the updated user credentials and next page
     *         information, or an ActionForward to the password reset page if there are errors.
     * @throws IOException If an I/O error occurs during processing.
     */
    private PasswordChangeResult processForcedPasswordChange(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws IOException {
        // Coming back from force password change.
        String userName = (String) request.getSession().getAttribute("userName");
        String password = (String) request.getSession().getAttribute("password");
        String pin = (String) request.getSession().getAttribute("pin");

        // Username is only letters and numbers
        userName = validateUsernamePattern(userName);

        // pins are integers only
        pin = validatePinPattern(pin);

        String nextPage = (String) request.getSession().getAttribute("nextPage");

        String newPassword = ((LoginForm) form).getNewPassword();
        String confirmPassword = ((LoginForm) form).getConfirmPassword();
        String oldPassword = ((LoginForm) form).getOldPassword();

        try {
            String errorStr = this.errorHandling(password, newPassword, confirmPassword, oldPassword);

            // Error Handling
            if (errorStr != null && !errorStr.isEmpty()) {
                String newURL = mapping.findForward("forcepasswordreset").getPath();
                newURL = newURL + errorStr;
                return new PasswordChangeResult(new ActionForward(newURL));
            }

            this.persistNewPassword(userName, newPassword);

            password = newPassword;

            // Remove the attributes from session
            this.removeAttributesFromSession(request);
        } catch (Exception e) {
            logger.error("Error", e);

            // Remove the attributes from session
            this.removeAttributesFromSession(request);

            return new PasswordChangeResult(getErrorForward(mapping, "Setting values to the session."));
        }

        return new PasswordChangeResult(userName, password, pin, nextPage);
    }

    /**
     * Handles a user's login attempt. This method retrieves user credentials from the form,
     * performs validation, checks for blocked IPs/users, and sets up session attributes.
     *
     * @param mapping The ActionMapping for this request.
     * @param form The ActionForm containing the user's login credentials.
     * @param request The HttpServletRequest for this request.
     * @param response The HttpServletResponse for this request.
     * @param cl The LoginCheckLogin object for checking login attempts.
     * @param initialChecksResult The result of initial checks performed before login.
     * @return A LoginAttemptResult containing the validated username, password, pin, and next page
     *         information, or an ActionForward to an error page or facility selection page if necessary.
     * @throws IOException If an I/O error occurs.
     */
    private LoginAttemptResult handleLoginAttempt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, LoginCheckLogin cl, InitialChecksResult initialChecksResult) throws IOException {
        LoginForm loginForm = (LoginForm) form;
        String nextPage = request.getParameter("nextPage");

        logger.debug("nextPage: " + nextPage);
        if (nextPage != null) {
            // set current facility
            ActionForward selectFacilityForward = this.getSelectFacilityForward(mapping, request, initialChecksResult.ip, nextPage);
            return new LoginAttemptResult(selectFacilityForward, false);
        }

        String userName = loginForm.getUsername();
        String password = loginForm.getPassword();
        String pin = loginForm.getPin();

        // Username is only letters and numbers
        userName = validateUsernamePattern(userName);

        // pins are integers only
        pin = validatePinPattern(pin);

        if (cl.isBlock(initialChecksResult.ip, userName)) {
            logger.info(LOG_PRE + " Blocked: " + userName);
            // return mapping.findForward(where); //go to block page
            // change to block page
            String errMsg = "Oops! Your account is now locked due to incorrect password attempts!";

            return new LoginAttemptResult(handleAjaxErrOrForwardErr(mapping, response, initialChecksResult.isAjaxResponse, errMsg), true);
        }

        return new LoginAttemptResult(userName, password, pin, nextPage);
    }

    /**
     * Retrieves the ActionForward for the given `nextPage`, after setting the selected facility
     * in the session.  This method is used when a user has multiple facilities and needs to
     * select one before proceeding.
     *
     * @param mapping The ActionMapping for the request.
     * @param request The HttpServletRequest containing the selected facility ID.
     * @param ip The IP address of the client.
     * @param nextPage The name of the forward to return after setting the facility.
     * @return An ActionForward to the specified `nextPage`.
     */
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

    /**
     * Authenticates a user using the provided credentials and IP address.
     *
     * @param cl The LoginCheckLogin object used to perform the authentication.
     * @param userLoginInfo A UserLoginInfo object containing the user's username, password, and PIN.
     * @param ip The IP address of the client attempting to log in.
     * @return An AuthResult object containing the authentication results, or null if authentication fails.
     * @throws Exception If an error occurs during authentication, such as a database connection error.
     */
    private AuthResult authenticateUser(LoginCheckLogin cl, UserLoginInfo userLoginInfo, String ip) throws Exception {
        String[] strAuth;

        /*
         * the pin code is not required for SSO IDP.
         */
        if (SSOUtility.isSSOEnabled()) {
            strAuth = cl.auth(userLoginInfo.username, userLoginInfo.password, ip);
        } else {
            strAuth = cl.auth(userLoginInfo.username, userLoginInfo.password, userLoginInfo.pin, ip);
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
     * @param userLoginInfo
     */
    private void setUserInfoToSession(HttpServletRequest request, UserLoginInfo userLoginInfo) throws Exception {
        request.getSession().setAttribute("userName", userLoginInfo.username);
        request.getSession().setAttribute("password", this.securityManager.encodePassword(userLoginInfo.password));
        request.getSession().setAttribute("pin", userLoginInfo.pin);
        request.getSession().setAttribute("nextPage", userLoginInfo.nextPage);

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

        Security security = this.getSecurity(userName);
        security.setPassword(this.securityManager.encodePassword(newPassword));
        security.setForcePasswordReset(Boolean.FALSE);
        this.securityDao.saveEntity(security);

    }

    public ApplicationContext getAppContext() {
        return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
    }

    /**
     * DTO to hold the results of initial checks performed
     * during the login process. This avoids multiple return values from the
     * performInitialChecks method.
     */
    private static class InitialChecksResult {
        public ActionForward errForward;
        public boolean isAjaxResponse;
        public boolean isMobileOptimized;
        public String ip;
        public String submitType;
        public boolean isForcePasswordChangeNeeded;

        public InitialChecksResult(boolean isAjaxResponse, boolean isMobileOptimized, String ip, String submitType, boolean isForcePasswordChangeNeeded) {
            this.isAjaxResponse = isAjaxResponse;
            this.isMobileOptimized = isMobileOptimized;
            this.ip = ip;
            this.submitType = submitType;
            this.isForcePasswordChangeNeeded = isForcePasswordChangeNeeded;
        }

        public InitialChecksResult(ActionForward errorForward) {
            this.errForward = errorForward;
        }
    }

    private static class PasswordChangeResult extends UserLoginInfo {

        public PasswordChangeResult(String username, String password, String pin, String nextPage) {
            super(username, password, pin, nextPage);
        }

        public PasswordChangeResult(ActionForward errForward) {
            super(errForward);
        }
    }

    private static class LoginAttemptResult extends UserLoginInfo {

        ActionForward selectFacilityForward;

        public LoginAttemptResult(String username, String password, String pin, String nextPage) {
            super(username, password, pin, nextPage);
        }

        public LoginAttemptResult(ActionForward forward, boolean isErr) {
            if (isErr)
                actionForward = forward;
            else
                this.selectFacilityForward = forward;
        }

    }

    private static class UserLoginInfo extends GenericResult {
        String username;
        String password;
        String pin;
        String nextPage;

        public UserLoginInfo(String username, String password, String pin, String nextPage) {
            this.username = username;
            this.password = password;
            this.pin = pin;
            this.nextPage = nextPage;
        }

        public UserLoginInfo(ActionForward errForward) {
            super(errForward);
        }

        public UserLoginInfo() {
        }
    }

    private static class AuthResultWrapper extends GenericResult {
        AuthResult authResult;

        public AuthResultWrapper() {

        }
    }

    private static class GenericResult {
        ActionForward actionForward;

        public GenericResult(ActionForward actionForward) {
            this.actionForward = actionForward;
        }

        public GenericResult(String pathToBuildForward) {
            actionForward = new ActionForward(pathToBuildForward);
        }

        public GenericResult() {
        }
    }
}
