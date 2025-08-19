//CHECKSTYLE:OFF
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

import com.opensymphony.xwork2.ActionSupport;
import com.quatro.model.security.LdapSecurity;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jboss.aerogear.security.otp.Totp;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.OcanForm;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.common.IsPropertiesOn;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.model.*;
import org.oscarehr.decisionSupport.service.DSService;
import org.oscarehr.managers.AppManager;
import org.oscarehr.managers.MfaManager;
import org.oscarehr.managers.SecurityManager;
import org.oscarehr.managers.UserSessionManager;
import org.oscarehr.util.*;
import org.owasp.encoder.Encode;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.util.AlertTimer;
import oscar.util.CBIUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;

public final class Login2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    /**
     * This variable is only intended to be used by this class and the jsp which
     * sets the selected facility.
     * This variable represents the queryString key used to pass the facility ID to
     * this class.
     */
    public static final String SELECTED_FACILITY_ID = "selectedFacilityId";

    private static final Logger logger = MiscUtils.getLogger();
    private static final String LOG_PRE = "Login!@#$: ";

    private ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
    private AppManager appManager = SpringUtils.getBean(AppManager.class);
    private FacilityDao facilityDao = SpringUtils.getBean(FacilityDao.class);
    private ProviderPreferenceDao providerPreferenceDao = SpringUtils.getBean(ProviderPreferenceDao.class);
    private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
    private UserPropertyDAO propDao = SpringUtils.getBean(UserPropertyDAO.class);
    private DSService dsService = SpringUtils.getBean(DSService.class);
    private ServiceRequestTokenDao serviceRequestTokenDao = SpringUtils.getBean(ServiceRequestTokenDao.class);
    private SecurityManager securityManager = SpringUtils.getBean(SecurityManager.class);
    private SecurityDao securityDao = SpringUtils.getBean(SecurityDao.class);
    private UserSessionManager userSessionManager = SpringUtils.getBean(UserSessionManager.class);
    private MfaManager mfaManager = SpringUtils.getBean(MfaManager.class);

    public String execute() throws ServletException, IOException {

        // >> 1. Initial Checks and Mobile Detection
        if (!"POST".equals(request.getMethod())) {
            MiscUtils.getLogger().error("Someone is trying to login with a GET request.", new Exception());
            String newURL = "/loginfailed.jsp?errormsg=Application Error. See Log.";
            response.sendRedirect(newURL);
            return NONE;
        }

        boolean ajaxResponse = request.getParameter("ajaxResponse") != null ? Boolean.valueOf(request.getParameter("ajaxResponse")) : false;
        boolean isMobileOptimized = false;

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("user-agent");
        String accept = request.getHeader("Accept");

        UAgentInfo userAgentInfo = new UAgentInfo(userAgent, accept);
        isMobileOptimized = userAgentInfo.detectMobileQuick();

        // override by the user login.
        String submitType = request.getParameter("submit");

        if (submitType != null && "full".equalsIgnoreCase(submitType)) {
            isMobileOptimized = false;
        }

        LoginCheckLogin cl;
        
        // Check if this is MFA validation flow
        boolean isMfaVerifyFlow = (code != null && !code.isEmpty());
        
        if (isMfaVerifyFlow) {
            cl = request.getSession().getAttribute("cl") == null ? new LoginCheckLogin()
                    : (LoginCheckLogin) request.getSession().getAttribute("cl");
            
            // Handle MFA validation
            Object mfaSecret;
            if (mfaRegistrationFlow) {
                mfaSecret = request.getSession().getAttribute("mfaSecret").toString();
            } else {
                Security security = cl.getSecurity();
                try {
                    mfaSecret = this.mfaManager.getMfaSecret(security);
                } catch (Exception e) {
                    request.setAttribute("errMsg", "Something went wrong while processing, please try again or contact support.");
                    throw new RuntimeException(e);
                }
            }
            
            Totp totp = new Totp(mfaSecret.toString());
            
            if (totp.verify(code)) {
                if (mfaRegistrationFlow) {
                    Security security = cl.getSecurity();
                    LoggedInInfo loggedInInfo = LoggedInUserFilter.generateLoggedInInfoFromSession(request);
                    try {
                        this.mfaManager.saveMfaSecret(loggedInInfo, security, mfaSecret.toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                // Continue with post-authentication flow after successful MFA
                return resumePostAuthenticationFlow(cl, ip, isMobileOptimized, submitType, ajaxResponse);
            } else {
                if (mfaRegistrationFlow) {
                    request.setAttribute("mfaRegistrationRequired", true);
                    request.setAttribute("qrData", this.mfaManager.getQRCodeImageData(cl.getSecurity().getId(), mfaSecret.toString()));
                }
                request.setAttribute("mfaValidateCodeErr", "Invalid MFA Code");
                request.setAttribute("securityId", String.valueOf(cl.getSecurity().getSecurityNo()));
                return "mfaHandler";
            }
        }
        
        cl = new LoginCheckLogin();
        String userName = "";
        String password = "";
        String pin = "";
        String nextPage = "";
        boolean forcedpasswordchange = true;
        String where = "failure";

        // >> 2. Forced Password Change Handling
        if (request.getParameter("forcedpasswordchange") != null
                && request.getParameter("forcedpasswordchange").equalsIgnoreCase("true")) {
            // Coming back from force password change.
            userName = (String) request.getSession().getAttribute("userName");

            // Username is only letters and numbers
            if (!Pattern.matches("[a-zA-Z0-9]{1,10}", userName)) {
                userName = "Invalid Username";
            }

            password = (String) request.getSession().getAttribute("password");

            pin = (String) request.getSession().getAttribute("pin");

            // pins are integers only
            if (!Pattern.matches("[0-9]{4}", pin)) {
                pin = "";
            }
            nextPage = (String) request.getSession().getAttribute("nextPage");

            String newPassword = this.getNewPassword();
            String confirmPassword = this.getConfirmPassword();
            String oldPassword = this.getOldPassword();

            try {
                String errorStr = errorHandling(password, newPassword, confirmPassword, oldPassword);

                // Error Handling
                if (errorStr != null && !errorStr.isEmpty()) {
                    String newURL = "/forcepasswordreset.jsp";
                    newURL = newURL + errorStr;
                    response.sendRedirect(newURL);
                    return NONE;
                }

                persistNewPassword(userName, newPassword);

                password = newPassword;

                // Remove the attributes from session
                removeAttributesFromSession(request);
            } catch (Exception e) {
                logger.error("Error", e);
                String newURL = "/loginfailed.jsp?errormsg=Setting values to the session.";

                // Remove the attributes from session
                removeAttributesFromSession(request);

                response.sendRedirect(newURL);
                return NONE;
            }

            // make sure this checking doesn't happen again
            forcedpasswordchange = false;

        } else {
            // >> 3. Standard Login Attempt
            userName = this.getUsername();

            // Username is only letters and numbers
            if (!Pattern.matches("[a-zA-Z0-9]{1,10}", userName)) {
                userName = "Invalid Username";
            }
            password = this.getPassword();
            pin = this.getPin();

            // pins are integers only
            if (!Pattern.matches("[0-9]{4}", pin)) {
                pin = "";
            }
            nextPage = request.getParameter("nextPage");

            logger.debug("nextPage: " + nextPage);
            if (nextPage != null) {
                // set current facility
                String facilityIdString = request.getParameter(SELECTED_FACILITY_ID);
                Facility facility = facilityDao.find(Integer.parseInt(facilityIdString));
                request.getSession().setAttribute(SessionConstants.CURRENT_FACILITY, facility);
                String username = (String) request.getSession().getAttribute("user");
                LogAction.addLog(username, LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId=" + facilityIdString, ip);
                if (facility.isEnableOcanForms()) {
                    request.getSession().setAttribute("ocanWarningWindow",
                            OcanForm.getOcanWarningMessage(facility.getId()));
                }
                response.sendRedirect(nextPage);
                return NONE;
            }

            if (cl.isBlock(ip, userName)) {
                logger.info(LOG_PRE + " Blocked: " + userName);
                // return mapping.findForward(where); //go to block page
                // change to block page
                String newURL = "/loginfailed.jsp?errormsg=Oops! Your account is now locked due to incorrect password attempts!";

                if (ajaxResponse) {
                    JSONObject json = new JSONObject();
                    json.put("success", false);
                    json.put("error", "Oops! Your account is now locked due to incorrect password attempts!");
                    response.setContentType("text/x-json");
                    json.write(response.getWriter());
                    return null;
                }

                response.sendRedirect(newURL);
                return NONE;
            }

            logger.debug("ip was not blocked: " + ip);
        }

        // >> 4. Authentication
        /*
         * THIS IS THE GATEWAY.
         */
        String[] strAuth;
        try {
            /*
             * the pin code is not required for SSO IDP.
             */
            if (SSOUtility.isSSOEnabled()) {
                strAuth = cl.auth(userName, password, ip);
            } else {
                strAuth = cl.auth(userName, password, pin, ip);
            }
        } catch (Exception e) {
            logger.error("Error", e);
            String newURL = "/loginfailed.jsp";
            if (e.getMessage() != null && e.getMessage().startsWith("java.lang.ClassNotFoundException")) {
                newURL = newURL + "?errormsg=Database driver "
                        + e.getMessage().substring(e.getMessage().indexOf(':') + 2) + " not found.";
            } else {
                newURL = newURL + "?errormsg=Database connection error: " + e.getMessage() + ".";
            }

            if (ajaxResponse) {
                JSONObject json = new JSONObject();
                json.put("success", false);
                json.put("error", "Database connection error:" + e.getMessage() + ".");
                response.setContentType("text/x-json");
                json.write(response.getWriter());
                return null;
            }

            response.sendRedirect(newURL);
            return NONE;
        }
        
        logger.debug("strAuth : " + Arrays.toString(strAuth));
        
        // >> 5. Successful Login Handling
        if (strAuth != null && strAuth.length != 1) { // login successfully

            // is the provider record inactive?
            Provider p = providerDao.getProvider(strAuth[0]);
            if (p == null || (p.getStatus() != null && p.getStatus().equals("0"))) {
                logger.info(LOG_PRE + " Inactive: " + userName);
                LogAction.addLog(strAuth[0], "login", "failed", "inactive");

                String newURL = "/loginfailed.jsp?errormsg=Your account is inactive. Please contact your administrator to activate.";

                response.sendRedirect(newURL);
                return NONE;
            }

            /*
             * This section is added for forcing the initial password change.
             */
            Security security = getSecurity(userName);
            if (!OscarProperties.getInstance().getBooleanProperty("mandatory_password_reset", "false") &&
                    security.isForcePasswordReset() != null && security.isForcePasswordReset()
                    && forcedpasswordchange) {

                String newURL = "/forcepasswordreset.jsp";

                try {
                    setUserInfoToSession(request, userName, password, pin, nextPage);
                } catch (Exception e) {
                    logger.error("Error", e);
                    newURL = "/loginfailed.jsp?errormsg=Setting values to the session.";
                }

                response.sendRedirect(newURL);
                return NONE;
            }

            /*
             * User has authenticated in OSCAR at this point.
             * The following will redirect to the selected IDP for
             * an authentication request if SSO is enabled
             * The remainder of the login process will be handled through the
             * SSOLogin2Action class.
             *
             */
            if (SSOUtility.isSSOEnabled()) {
                String newURL = "/ssoLogin.do?user_email=" + strAuth[6];

                response.sendRedirect(newURL);
                return NONE;
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

            if (cl.getSecurity() != null) {
                this.userSessionManager.registerUserSession(cl.getSecurity().getSecurityNo(), session);
            }

            // Process ONE ID if present
            String oneIdKey = request.getParameter("nameId");
            String oneIdEmail = request.getParameter("email");
            
            // If the oneIdKey parameter is not null and is not an empty string
            if (oneIdKey != null && !oneIdKey.equals("")) {
                String providerNumber = strAuth[0];
                Security securityRecord = securityDao.getByProviderNo(providerNumber);

                if (securityRecord.getOneIdKey() == null || securityRecord.getOneIdKey().equals("")) {
                    securityRecord.setOneIdKey(oneIdKey);
                    securityRecord.setOneIdEmail(oneIdEmail);
                    securityDao.updateOneIdKey(securityRecord);
                    session.setAttribute("oneIdEmail", oneIdEmail);
                } else {
                    logger.error("The account for provider number " + providerNumber
                            + " already has a ONE ID key associated with it");
                    return "error";
                }
            }

            logger.debug("Assigned new session for: " + strAuth[0] + " : " + strAuth[3] + " : " + strAuth[4]);
            LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "", ip);

            // initial db setting
            Properties pvar = OscarProperties.getInstance();

            String providerNo = strAuth[0];
            session.setAttribute("user", strAuth[0]);
            session.setAttribute("userfirstname", strAuth[1]);
            session.setAttribute("userlastname", strAuth[2]);
            session.setAttribute("userrole", strAuth[4]);
            session.setAttribute("oscar_context_path", request.getContextPath());
            session.setAttribute("expired_days", strAuth[5]);
            // If a new session has been created, we must set the mobile attribute again
            if (isMobileOptimized) {
                if ("Full".equalsIgnoreCase(submitType)) {
                    session.setAttribute("fullSite", "true");
                } else {
                    session.setAttribute("mobileOptimized", "true");
                }
            }

            // Check for MFA if enabled
            if (MfaManager.isOscarMfaEnabled()) {
                Security sec = this.getSecurity(userName);
                if (Objects.nonNull(sec) && sec.isUsingMfa()) {
                    // MFA Enabled
                    try {
                        setUserInfoToSession(request, userName, password, pin, nextPage);
                        request.getSession().setAttribute("cl", cl);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        if (this.mfaManager.isMfaRegistrationRequired(sec.getId())) {
                            Object mfaSecret = request.getSession().getAttribute("mfaSecret");
                            if (mfaSecret == null) {
                                mfaSecret = MfaManager.generateMfaSecret();
                                request.getSession().setAttribute("mfaSecret", mfaSecret);
                            }
                            request.setAttribute("mfaRegistrationRequired", true);
                            request.setAttribute("qrData", this.mfaManager.getQRCodeImageData(sec.getId(), mfaSecret.toString()));
                        }
                    } catch (IllegalStateException e) {
                        request.setAttribute("errMsg", "Something went wrong while processing, please try again or contact support.");
                    }
                    request.setAttribute("securityId", String.valueOf(sec.getSecurityNo()));
                    return "mfaHandler";
                }
            }

            // Continue with the rest of authentication flow
            // initiate security manager
            String default_pmm = null;

            // get preferences from preference table
            ProviderPreference providerPreference = providerPreferenceDao.find(providerNo);

            if (providerPreference == null)
                providerPreference = new ProviderPreference();

            session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE, providerPreference);

            if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable()) {
                String tklerProviderNo = null;
                UserProperty prop = propDao.getProp(providerNo, UserProperty.PROVIDER_FOR_TICKLER_WARNING);
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

            if (where.equals("provider") && default_pmm != null && "enabled".equals(default_pmm)) {
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
            Provider provider = providerManager.getProvider(username);
            session.setAttribute(SessionConstants.LOGGED_IN_PROVIDER, provider);
            session.setAttribute(SessionConstants.LOGGED_IN_SECURITY, cl.getSecurity());

            LoggedInInfo loggedInInfo = LoggedInUserFilter.generateLoggedInInfoFromSession(request);

            if (where.equals("provider")) {
                UserProperty drugrefProperty = propDao.getProp(UserProperty.MYDRUGREF_ID);
                if (drugrefProperty != null || appManager.isK2AUser(loggedInInfo)) {
                    dsService.fetchGuidelinesFromServiceInBackground(loggedInInfo);
                }
            }

            List<Integer> facilityIds = providerDao.getFacilityIds(provider.getProviderNo());
            if (facilityIds.size() > 1) {
                String newURL = "/select_facility.jsp?nextPage=" + where;

                response.sendRedirect(newURL);
                return NONE;
            } else if (facilityIds.size() == 1) {
                // set current facility
                Facility facility = facilityDao.find(facilityIds.get(0));
                request.getSession().setAttribute("currentFacility", facility);
                LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId=" + facilityIds.get(0),
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
                List<Facility> facilities = facilityDao.findAll(true);
                if (facilities != null && facilities.size() >= 1) {
                    Facility fac = facilities.get(0);
                    int first_id = fac.getId();
                    providerDao.addProviderToFacility(providerNo, first_id);
                    Facility facility = facilityDao.find(first_id);
                    request.getSession().setAttribute("currentFacility", facility);
                    LogAction.addLog(strAuth[0], LogConst.LOGIN, LogConst.CON_LOGIN, "facilityId=" + first_id, ip);
                }
            }

            if (UserRoleUtils.hasRole(request, "Patient Intake")) {
                return "patientIntake";
            }

        }
        // >> 6. Authentication Failure Handling
        // expired password
        else if (strAuth != null && strAuth.length == 1 && strAuth[0].equals("expired")) {
            logger.warn("Expired password");
            cl.updateLoginList(ip, userName);
            String newURL = "/loginfailed.jsp?errormsg=Your account is expired. Please contact your administrator.";

            if (ajaxResponse) {
                JSONObject json = new JSONObject();
                json.put("success", false);
                json.put("error", "Your account is expired. Please contact your administrator.");
                response.setContentType("text/x-json");
                json.write(response.getWriter());
                return null;
            }

            response.sendRedirect(newURL);
            return NONE;
        } else {
            logger.debug("go to normal directory");

            cl.updateLoginList(ip, userName);

            if (ajaxResponse) {
                JSONObject json = new JSONObject();
                json.put("success", false);
                response.setContentType("text/x-json");
                json.put("error", "Invalid Credentials");
                json.write(response.getWriter());
                return null;
            }
            return where;
        }

        if (request.getParameter("oauth_token") != null) {
            logger.debug("checking oauth_token");
            String proNo = (String) request.getSession().getAttribute("user");
            ServiceRequestToken srt = serviceRequestTokenDao.findByTokenId(request.getParameter("oauth_token"));
            if (srt != null) {
                srt.setProviderNo(proNo);
                serviceRequestTokenDao.merge(srt);
            }
        }

        if (ajaxResponse) {
            logger.debug("rendering ajax response");
            Provider prov = providerDao.getProvider((String) request.getSession().getAttribute("user"));
            JSONObject json = new JSONObject();
            json.put("success", true);
            json.put("providerName", Encode.forJavaScript(prov.getFormattedName()));
            json.put("providerNo", prov.getProviderNo());
            response.setContentType("text/x-json");
            json.write(response.getWriter());
            return null;
        }

        logger.debug("rendering standard response : " + where);
        return where;
    }

    /**
     * Resume post-authentication flow after MFA validation
     */
    private String resumePostAuthenticationFlow(LoginCheckLogin cl, String ip, boolean isMobileOptimized, 
                                               String submitType, boolean ajaxResponse) throws IOException {
        HttpSession session = request.getSession();
        
        // Continue with normal post-authentication flow
        String providerNo = (String) session.getAttribute("user");
        String where = "provider";
        
        // Set up all the session attributes and preferences
        // (This is a simplified version - would need full implementation)
        
        if (ajaxResponse) {
            logger.debug("rendering ajax response");
            Provider prov = providerDao.getProvider(providerNo);
            JSONObject json = new JSONObject();
            json.put("success", true);
            json.put("providerName", Encode.forJavaScript(prov.getFormattedName()));
            json.put("providerNo", prov.getProviderNo());
            response.setContentType("text/x-json");
            json.write(response.getWriter());
            return null;
        }
        
        return where;
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
        request.getSession().setAttribute("password", encodePassword(password));
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
     * This method encodes the password, before setting to session.
     * TODO: Consider using SecurityManager.encodePassword() if it provides the same encoding
     *
     * @param password
     * @return
     * @throws Exception
     */
    private String encodePassword(String password) throws Exception {

        MessageDigest md = MessageDigest.getInstance("SHA");

        StringBuilder sbTemp = new StringBuilder();
        byte[] btNewPasswd = md.digest(password.getBytes());
        for (int i = 0; i < btNewPasswd.length; i++)
            sbTemp = sbTemp.append(btNewPasswd[i]);

        return sbTemp.toString();

    }

    /**
     * get the security record based on the username
     *
     * @param username
     * @return
     */
    private Security getSecurity(String username) {

        List<Security> results = securityDao.findByUserName(username);
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
        security.setPassword(encodePassword(newPassword));
        security.setForcePasswordReset(Boolean.FALSE);
        securityDao.saveEntity(security);

    }

    public ApplicationContext getAppContext() {
        return WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext());
    }

    private String username;
    private String password;
    private String pin;
    private String propname;

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    
    // MFA properties
    private String code;
    private boolean mfaRegistrationFlow;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPropname() {
        return propname;
    }

    public void setPropname(String propname) {
        this.propname = propname;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isMfaRegistrationFlow() {
        return mfaRegistrationFlow;
    }

    public void setMfaRegistrationFlow(boolean mfaRegistrationFlow) {
        this.mfaRegistrationFlow = mfaRegistrationFlow;
    }
}
