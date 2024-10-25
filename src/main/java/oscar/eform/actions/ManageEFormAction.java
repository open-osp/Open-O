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


package oscar.eform.actions;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.eform.EFormExportZip;
import oscar.eform.data.EForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageEFormAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward exportEForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "r", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
    	
        String fid = request.getParameter("fid");
        MiscUtils.getLogger().debug("fid: " + fid);
        response.setContentType("application/zip");  //octet-stream
        EForm eForm = new EForm(fid, "1");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + eForm.getFormName().replaceAll("\\s", fid) + ".zip\"");
        EFormExportZip eFormExportZip = new EFormExportZip();
        List<EForm> eForms = new ArrayList<EForm>();
        eForms.add(eForm);
        eFormExportZip.exportForms(eForms, response.getOutputStream());
        return null;
    }

    public ActionForward importEForm(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}

        FormFile zippedForm = (FormFile) form.getMultipartRequestHandler().getFileElements().get("zippedForm");
        List<String> errors = Collections.emptyList();
        try (InputStream zippedFormStream = zippedForm.getInputStream()) {
            request.setAttribute("input", "import");
            EFormExportZip eFormExportZip = new EFormExportZip();
            errors = eFormExportZip.importForm(zippedFormStream);
        }
        if(!errors.isEmpty()){
            request.setAttribute("importErrors", errors);
        	return mapping.findForward("fail");
        }else{
	        request.setAttribute("status", "success");
	        return mapping.findForward("success");
        }
    }


}
