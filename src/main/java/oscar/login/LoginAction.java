/**
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
import oscar.util.ParameterActionForward;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public final class LoginAction extends DispatchAction {

	public static Map<Integer, HttpSession> userSessionMap = new HashMap<>();

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

	private final SecurityManager securityManager = SpringUtils.getBean(SecurityManager.class);
	private final UserSessionManager userSessionManager = SpringUtils.getBean(UserSessionManager.class);

	// remove after testing is done
	// private SsoAuthenticationManager ssoAuthenticationManager =
	// SpringUtils.getBean(SsoAuthenticationManager.class);
	/**
	 * TODO: for the love of god - please help me clean-up this nightmare
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(!"POST".equals(request.getMethod())) {
			MiscUtils.getLogger().error("Someone is trying to login with a GET request.",new Exception());
			String newURL = mapping.findForward("error").getPath();
			newURL = newURL + "?errormsg=Application Error. See Log.";
			return new ActionForward(newURL);
		}

		boolean ajaxResponse = request.getParameter("ajaxResponse") != null?Boolean.valueOf(request.getParameter("ajaxResponse")):false;
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

		LoginCheckLogin cl = new LoginCheckLogin();
		String oneIdKey = request.getParameter("nameId");
		String oneIdEmail = request.getParameter("email");
		String userName = "";
		String password = "";
		String pin = "";
		String nextPage = "";
		boolean forcedpasswordchange = true;
		String where = "failure";

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

			String newPassword = ((LoginForm) form).getNewPassword();
			String confirmPassword = ((LoginForm) form).getConfirmPassword();
			String oldPassword = ((LoginForm) form).getOldPassword();

			try {
				String errorStr = errorHandling(password, newPassword, confirmPassword, oldPassword);

				// Error Handling
				if (errorStr != null && !errorStr.isEmpty()) {
					String newURL = mapping.findForward("forcepasswordreset").getPath();
					newURL = newURL + errorStr;
					return (new ActionForward(newURL));
				}

				persistNewPassword(userName, newPassword);

				password = newPassword;

				// Remove the attributes from session
				removeAttributesFromSession(request);
			} catch (Exception e) {
				logger.error("Error", e);
				String newURL = mapping.findForward("error").getPath();
				newURL = newURL + "?errormsg=Setting values to the session.";

				// Remove the attributes from session
				removeAttributesFromSession(request);

				return (new ActionForward(newURL));
			}

			// make sure this checking doesn't happen again
			forcedpasswordchange = false;

		} else {
			userName = ((LoginForm) form).getUsername();

			// Username is only letters and numbers
			// Username is only letters and numbers
			if (!Pattern.matches("[a-zA-Z0-9]{1,10}", userName)) {
				userName = "Invalid Username";
			}
			password = ((LoginForm) form).getPassword();
			pin = ((LoginForm) form).getPin();

			// pins are integers only
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
				return mapping.findForward(nextPage);
			}

			if (cl.isBlock(ip, userName)) {
				logger.info(LOG_PRE + " Blocked: " + userName);
				// return mapping.findForward(where); //go to block page
				// change to block page
				String newURL = mapping.findForward("error").getPath();
				newURL = newURL + "?errormsg=Oops! Your account is now locked due to incorrect password attempts!";

				if (ajaxResponse) {
					JSONObject json = new JSONObject();
					json.put("success", false);
					json.put("error", "Oops! Your account is now locked due to incorrect password attempts!");
					response.setContentType("text/x-json");
					json.write(response.getWriter());
					return null;
				}

				return (new ActionForward(newURL));
			}

			logger.debug("ip was not blocked: " + ip);

		}

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
			String newURL = mapping.findForward("error").getPath();
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

			return (new ActionForward(newURL));
		}
		logger.debug("strAuth : " + Arrays.toString(strAuth));
		if (strAuth != null && strAuth.length != 1) { // login successfully

			// is the provider record inactive?
			ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
			Provider p = providerDao.getProvider(strAuth[0]);
			if (p == null || (p.getStatus() != null && p.getStatus().equals("0"))) {
				logger.info(LOG_PRE + " Inactive: " + userName);
				LogAction.addLog(strAuth[0], "login", "failed", "inactive");

				String newURL = mapping.findForward("error").getPath();
				newURL = newURL + "?errormsg=Your account is inactive. Please contact your administrator to activate.";
				return (new ActionForward(newURL));
			}

			/*
			 * This section is added for forcing the initial password change.
			 */
			Security security = getSecurity(userName);
			if (!OscarProperties.getInstance().getBooleanProperty("mandatory_password_reset", "false") &&
					security.isForcePasswordReset() != null && security.isForcePasswordReset()
					&& forcedpasswordchange) {

				String newURL = mapping.findForward("forcepasswordreset").getPath();

				try {
					setUserInfoToSession(request, userName, password, pin, nextPage);
				} catch (Exception e) {
					logger.error("Error", e);
					newURL = mapping.findForward("error").getPath();
					newURL = newURL + "?errormsg=Setting values to the session.";
				}

				return new ActionForward(newURL);
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
				ActionRedirect redirect = new ActionRedirect(mapping.findForward("ssoLogin"));
				redirect.addParameter("user_email", strAuth[6]);
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

			if (security != null) {
				this.userSessionManager.registerUserSession(security.getSecurityNo(), session);
			}

			// If the ondIdKey parameter is not null and is not an empty string
			if (oneIdKey != null && !oneIdKey.equals("")) {
				String providerNumber = strAuth[0];
				SecurityDao securityDao = (SecurityDao) SpringUtils.getBean(SecurityDao.class);
				Security securityRecord = securityDao.getByProviderNo(providerNumber);

				if (securityRecord.getOneIdKey() == null || securityRecord.getOneIdKey().equals("")) {
					securityRecord.setOneIdKey(oneIdKey);
					securityRecord.setOneIdEmail(oneIdEmail);
					securityDao.updateOneIdKey(securityRecord);
					session.setAttribute("oneIdEmail", oneIdEmail);
				} else {
					logger.error("The account for provider number " + providerNumber
							+ " already has a ONE ID key associated with it");
					return mapping.findForward("error");
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
					DSService service = SpringUtils.getBean(DSService.class);
					service.fetchGuidelinesFromServiceInBackground(loggedInInfo);
				}
			}

			List<Integer> facilityIds = providerDao.getFacilityIds(provider.getProviderNo());
			if (facilityIds.size() > 1) {
				return (new ActionForward("/select_facility.jsp?nextPage=" + where));
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
				return mapping.findForward("patientIntake");
			}

		}
		// expired password
		else if (strAuth != null && strAuth.length == 1 && strAuth[0].equals("expired")) {
			logger.warn("Expired password");
			cl.updateLoginList(ip, userName);
			String newURL = mapping.findForward("error").getPath();
			newURL = newURL + "?errormsg=Your account is expired. Please contact your administrator.";

			if (ajaxResponse) {
				JSONObject json = new JSONObject();
				json.put("success", false);
				json.put("error", "Your account is expired. Please contact your administrator.");
				response.setContentType("text/x-json");
				json.write(response.getWriter());
				return null;
			}

			return (new ActionForward(newURL));
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

			ParameterActionForward forward = new ParameterActionForward(mapping.findForward(where));
			forward.addParameter("login", "failed");
			if (oneIdKey != null && !oneIdKey.equals("")) {
				forward.addParameter("nameId", oneIdKey);
			}

			return forward;
		}

		if (request.getParameter("oauth_token") != null) {
			logger.debug("checking oauth_token");
			String proNo = (String) request.getSession().getAttribute("user");
			ServiceRequestTokenDao serviceRequestTokenDao = SpringUtils.getBean(ServiceRequestTokenDao.class);
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
		return mapping.findForward(where);
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

		SecurityDao securityDao = (SecurityDao) SpringUtils.getBean(SecurityDao.class);
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
		security.setPassword(this.securityManager.encodePassword(newPassword));
		security.setForcePasswordReset(Boolean.FALSE);
		SecurityDao securityDao = (SecurityDao) SpringUtils.getBean(SecurityDao.class);
		securityDao.saveEntity(security);

	}

	public ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
	}
}
