package org.oscarehr.ui.servlet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.common.model.enumerator.UserPropertyKey;
import org.oscarehr.managers.UserPropertyManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.form.JSONAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EncounterNotePasswordServlet extends JSONAction {

	UserPropertyManager userPropertyManager = SpringUtils.getBean(UserPropertyManager.class);

	@Override
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.unspecified(mapping, form, request, response);
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
}
