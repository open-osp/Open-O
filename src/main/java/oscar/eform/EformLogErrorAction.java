package oscar.eform;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jsoup.internal.StringUtil;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EformLogErrorAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
	                             HttpServletRequest request, HttpServletResponse response) throws Exception {
		String formId = request.getParameter("formId");
		String error = request.getParameter("error");

		/*
		 * silent update to the eform error log.
		 */
		if(formId != null && !formId.isEmpty() && StringUtil.isNumeric(formId)) {
			String sanitizedError = StringEscapeUtils.escapeHtml4(error);
			EFormUtil.logError(Integer.parseInt(formId), sanitizedError);
		}
		return null;
	}
}
