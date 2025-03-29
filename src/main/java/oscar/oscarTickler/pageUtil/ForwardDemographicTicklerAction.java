package oscar.oscarTickler.pageUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.owasp.encoder.Encode;
import oscar.oscarDemographic.data.DemographicNameAgeString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * This class is used to forward to the add tickler screen with the demographic preselected
 * @author jay
 */
public class ForwardDemographicTicklerAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    /** Creates a new instance of ForwardDemographicTicklerAction */
    public ForwardDemographicTicklerAction() {
    }
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_tickler", "u", null)) {
			throw new RuntimeException("missing required security object (_tickler)");
		}
  	
       String demoNo = request.getParameter("demographic_no");
       if ( demoNo != null ){
	       demoNo = Encode.forHtmlContent(demoNo);
          Map<String, String> h = DemographicNameAgeString.getInstance().getNameAgeSexHashtable(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo);
          request.setAttribute("demographic_no", demoNo);
          request.setAttribute("demoName", h.get("lastName")+", "+h.get("firstName"));
          
          String docType = request.getParameter("docType");
          String docId = request.getParameter("docId");

		  if(docType != null) {
			  docType = Encode.forHtmlContent(docType);
		  }
		  if(docId != null) {
			  docId = Encode.forHtmlContent(docId);
		  }
          
          request.setAttribute("docType", docType);
          request.setAttribute("docId", docId);
          
       }
       return mapping.findForward("success");   
    }     
}