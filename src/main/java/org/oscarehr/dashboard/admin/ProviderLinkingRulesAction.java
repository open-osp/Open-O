package org.oscarehr.dashboard.admin;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.ProviderManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

public class ProviderLinkingRulesAction extends DispatchAction {
    private static final Logger logger = MiscUtils.getLogger();

    private ProviderManager2 providerManager2 = SpringUtils.getBean(ProviderManager2.class);
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public ActionForward updateProviderLinkingRulesProperty(ActionMapping actionmapping,ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_admin", SecurityInfoManager.WRITE, null) &&
            !securityInfoManager.hasPrivilege(loggedInInfo, "_lab", SecurityInfoManager.WRITE, null)) {
			throw new RuntimeException("missing required security object (_admin or _lab)");
		}

        String value = request.getParameter("status");
        boolean status = providerManager2.updateProviderLinkingRulesProperty(loggedInInfo, value);
        
        JSONObject json = new JSONObject();
		json.put("status", status);
        writeJsonResponse(response, json.toString());
        return null;
    }

    public ActionForward viewProviderLinkingRulesPropertyStatus(ActionMapping actionmapping,ActionForm actionform, HttpServletRequest request, HttpServletResponse response) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_admin", SecurityInfoManager.READ, null) &&
            !securityInfoManager.hasPrivilege(loggedInInfo, "_lab", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_admin or _lab)");
		}

        boolean status = providerManager2.viewProviderLinkingRulesPropertyStatus(loggedInInfo);
        
        JSONObject json = new JSONObject();
		json.put("status", status);
        writeJsonResponse(response, json.toString());
        return null;
    }

    private void writeJsonResponse(HttpServletResponse response, String json) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.write(json);
        } catch (Exception e) {
            logger.error("An error occurred while writing JSON response to the output stream", e);
        }
    }
    
}
