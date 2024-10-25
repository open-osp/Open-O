package oscar.eform;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
		if(formId != null && !formId.isEmpty()) {
			EFormUtil.logError(Integer.parseInt(formId), error);
		}
		return null;
	}
}
