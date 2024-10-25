package org.oscarehr.contactRegistry;

import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.managers.ProfessionalSpecialistsManager;
import org.oscarehr.util.JsonUtil;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.form.JSONAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ProfessionalSpecialistAction extends JSONAction {

	private final ProfessionalSpecialistsManager professionalSpecialistsManager = SpringUtils.getBean(ProfessionalSpecialistsManager.class);

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
	                                 HttpServletRequest request, HttpServletResponse response) {

		/*
		 * Designed for backwards compatibility.
		 * Otherwise use the dispatch action methods.
		 */
		String path = mapping.getPath();

		if("/getProfessionalSpecialist".equals(path)) {
			this.get(mapping, form, request, response);
		}

		if("/searchProfessionalSpecialist".equals(path)) {
			this.search(mapping, form, request, response);
		}

		return null;
	}

	public void get(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String specialistId = request.getParameter("id");
		ProfessionalSpecialist professionalSpecialist = null;

		if(specialistId != null && !specialistId.isEmpty()) {
			professionalSpecialist = professionalSpecialistsManager.getProfessionalSpecialist(loggedInInfo, Integer.parseInt(specialistId));
		}

		if(professionalSpecialist != null) {
			JSONObject professionalSpecialistJSON = JsonUtil.pojoToJson(professionalSpecialist);
			super.jsonResponse(response, professionalSpecialistJSON.toString());
		}
	}

	public void search(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String search_keyword = request.getParameter("keyword");
		List<ProfessionalSpecialist> professionalSpecialist = null;

		if(search_keyword != null && !search_keyword.isEmpty()) {
			professionalSpecialist = professionalSpecialistsManager.searchProfessionalSpecialist(loggedInInfo, search_keyword);
		}

		if(professionalSpecialist != null) {
			String professionalSpecialistJSON = JsonUtil.pojoCollectionToJson(professionalSpecialist);
			super.jsonResponse(response, professionalSpecialistJSON);
		}
	}

}
