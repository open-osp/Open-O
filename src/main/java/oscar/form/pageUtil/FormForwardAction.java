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

package oscar.form.pageUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.form.data.FrmData;

public class FormForwardAction extends Action {
	
	private final Logger logger = MiscUtils.getLogger();
	private final SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	/**
	 * forward to the current specified form, e.g. ../form/formar.jsp?demographic_no=
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LoggedInInfo loggedInInfo 		= LoggedInInfo.getLoggedInInfoFromSession(request);
		String demographicNo 			= request.getParameter("demographic_no");
		
		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_form", SecurityInfoManager.READ, demographicNo)) 
		{
			throw new SecurityException("missing required security object (_form)");
		}
   
		String formName 				= request.getParameter("formname");
	    String remoteFacilityIdString 	= request.getParameter("remoteFacilityId");
	    String appointmentNo 			= request.getParameter("appointmentNo");
	    String formId 					= request.getParameter("formId");	
	    String provNo 					= request.getParameter("provNo");
        String strFrm 					= URLDecoder.decode(formName, CharEncoding.UTF_8);
        int requestedForm 				= 0;
        int latestForm 					= 0;
        String[] formPath 				= null;       
        
        /*
         * Fetch all the meta data for the requested form
         */       
		try 
		{
			FrmData frmData = new FrmData();
			formPath = frmData.getShortcutFormValue(demographicNo, strFrm);
		    formPath[0] = formPath[0].trim();
		    
		    /*
		     * edit some soon-to-be deprecated methods of storing path values.
		     */
		    if(formPath[0].startsWith("../"))
		    {
		    	formPath[0] = formPath[0].replace("../", "/");
		    }
		    
		    if(formPath[0].endsWith("?demographic_no="))
		    {
		    	formPath[0] = formPath[0].replace("?demographic_no=", "");
		    }

			if(formPath[0].endsWith("?demographicNo="))
			{
				formPath[0] = formPath[0].replace("?demographicNo=", "");
			}

			if(formPath[0].endsWith("&demographic_no="))
			{
				formPath[0] = formPath[0].replace("&demographic_no=", "");
			}

			if(formPath[0].endsWith("&demographicNo="))
			{
				formPath[0] = formPath[0].replace("&demographicNo=", "");
			}
		} 
		catch (SQLException e) 
		{
			logger.error("failed to fetch formPath for " + strFrm, e);
		}

		/*
		 * Build a custom forward path to the requested form. 
		 */
		ActionRedirect redirect = new ActionRedirect(formPath[0]);
		redirect.addParameter("demographic_no", demographicNo);
       
		/*
         * If the formId is requesting the latest form then change its 
         * value to null.  The null value will indicate that the most recent 
         * form should be fetched.
         * Done this way to ensure that incoming null values are also respected.
         */
        if("latest".equalsIgnoreCase(formId))
        {
        	formId = null;
        }
        
        /*
         * get the latest form id from the formPath return by the frmData object.
         */
        if(formPath.length > 1 && formPath[1] != null) 
        {
        	latestForm = Integer.parseInt(formPath[1]);
        }
	
		/*
		 * When the form id is null the most updated form id from the formPath
		 * array is used. 
		 * This can be handy to force results of the most recent form only 
		 */
		if(formId != null) 
    	{
			requestedForm = Integer.parseInt(formId);
			redirect.addParameter("formId", formId);
    	}
    	else if(latestForm > 0)
    	{   		
    		redirect.addParameter("formId", latestForm);
    	}
		
		/*
		 * Send a warning back to the user that this is an older version 
		 * of the form. 
		 */
		if(requestedForm > 0 && requestedForm < latestForm)
		{
			redirect.addParameter("warning", "history");
		}

		if(remoteFacilityIdString != null && ! remoteFacilityIdString.isEmpty()) {
			redirect.addParameter("remoteFacilityId", remoteFacilityIdString);
		}

		if(appointmentNo != null && ! appointmentNo.isEmpty()) {
			redirect.addParameter("appointmentNo", appointmentNo);
		}
		if(provNo != null && ! provNo.isEmpty()) {
			redirect.addParameter("provNo", provNo);
		}

		return redirect;
	}

}
