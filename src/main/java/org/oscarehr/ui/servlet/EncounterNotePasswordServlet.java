package org.oscarehr.ui.servlet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.managers.UserPropertyManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.form.JSONAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class EncounterNotePasswordServlet extends JSONAction implements Serializable {

	private static final CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);
	private static final UserPropertyManager userPropertyManager = SpringUtils.getBean(UserPropertyManager.class);

	private static final long serialVersionUID = 1L;

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.unspecified(mapping, form, request, response);
	}

	/**
	 * Retrieves the password for password-protected notes associated with the logged-in user
	 * and sends it in the JSON response if applicable.
	 *
	 * @param mapping The ActionMapping associated with this request.
	 * @param form The ActionForm containing the request data.
	 * @param request The HttpServletRequest object representing the client request.
	 * @param response The HttpServletResponse object for sending the response.
	 * @return An ActionForward object for navigation, or null if no navigation is required.
	 * @throws Exception If an error occurs during the process.
	 */
	public void getPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String password = userPropertyManager.getEncounterNotePassword(loggedInInfo);
		if (password != null && !password.trim().isEmpty()) {
			jsonResponse(response, "password", password);
		}

	}

	/**
	 * Updates the password in password-locked notes associated with the logged-in user, if a valid
	 * password is available in user properties.
	 *
	 * @param mapping The ActionMapping associated with this request.
	 * @param form The ActionForm containing the request data.
	 * @param request The HttpServletRequest object representing the client request.
	 * @param response The HttpServletResponse object for sending the response.
	 * @return An ActionForward object for navigation, or null if no navigation is required.
	 */
	public void update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		caseManagementManager.updatePasswordLockedNotes(loggedInInfo);
	}

	/**
	 * Toggles the enable/disable state for password-locked notes associated with the logged-in user.
	 *
	 * @param mapping The ActionMapping associated with this request.
	 * @param form The ActionForm containing the request data.
	 * @param request The HttpServletRequest object representing the client request.
	 * @param response The HttpServletResponse object for sending the response.
	 * @return An ActionForward object for navigation, or null if no navigation is required.
	 */
	public void enableDisable(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		caseManagementManager.enableDisablePasswordLockedNotes(loggedInInfo);
	}
}
