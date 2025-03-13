package org.oscarehr.ui.servlet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.common.model.enumerator.UserPropertyKey;
import org.oscarehr.managers.UserPropertyManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.form.JSONAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class EncounterNotePasswordServlet extends JSONAction implements Serializable {

	private static final CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);
	UserPropertyManager userPropertyManager = SpringUtils.getBean(UserPropertyManager.class);
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
	public ActionForward get(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		UserProperty userProperty = userPropertyManager.getUserProperty(loggedInInfo, UserPropertyKey.CASEMGMT_NOTE_PASSWORD_ENABLED);
		if (userProperty != null && userProperty.isChecked()) {
			userProperty = userPropertyManager.getUserProperty(loggedInInfo, UserPropertyKey.CASEMGMT_NOTE_PASSWORD);
			if (userProperty != null && userProperty.getValue() != null && !userProperty.getValue().trim().isEmpty()) {
				jsonResponse(response, "password", userProperty.getValue());
			}
		}
		return null;
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
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		UserProperty userProperty = userPropertyManager.getUserProperty(loggedInInfo, UserPropertyKey.CASEMGMT_NOTE_PASSWORD);
		if(userProperty != null && userProperty.getValue() != null && !userProperty.getValue().trim().isEmpty()) {
			caseManagementManager.updatePasswordLockedNotes(loggedInInfo, userProperty.getValue());
		}
		return null;
	}
}
