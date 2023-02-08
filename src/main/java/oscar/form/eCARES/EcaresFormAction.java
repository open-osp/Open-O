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

package oscar.form.eCARES;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ostermiller.util.ExcelCSVPrinter;
import net.sf.json.JSONObject;
import org.apache.struts.action.*;
import org.oscarehr.managers.FormeCARESManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.form.JSONAction;
import org.oscarehr.managers.constants.Constants;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2020
 * For: Fraser Health Authority BC.
 */
public class EcaresFormAction extends JSONAction {

	private final FormeCARESManager formeCARESManager = SpringUtils.getBean(FormeCARESManager.class);
	private final SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class); ;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		return fetch(mapping,form,request,response);
	}

	public ActionForward fetch(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		Integer demographicNo = demographicNumberToInteger(request);

		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_form", SecurityInfoManager.READ, demographicNo)) {
			throw new SecurityException("missing required security object (_form)");
		}

		if(demographicNo != null) {
			return mapping.findForward("success");
		}

		return null;
	}

	public void get(ActionMapping mapping, ActionForm form,
					HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		Integer demographicNo = demographicNumberToInteger(request);
		Integer formId = Integer.parseInt( request.getParameter(Constants.Cares.FormField.formId.name()) );
		JSONObject formData = formeCARESManager.getData(loggedInInfo, demographicNo, formId);
		jsonResponse(response, formData);

	}

	public void save(ActionMapping mapping, ActionForm form,
							   HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String formData = request.getParameter(Constants.Cares.FormField.formData.name());
		JSONObject responseMessage = formeCARESManager.save(loggedInInfo, formData);
		jsonResponse(response, responseMessage);

	}

	public void createTickler(ActionMapping mapping, ActionForm form,
						HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String tickler = request.getParameter(Constants.Cares.FormField.tickler.name());
		JSONObject responseMessage = formeCARESManager.createTickler(loggedInInfo, tickler);
		jsonResponse(response, responseMessage);
	}

	/**
	 * Return a blank Tickler widget if no action is specified.
	 */
	public ActionForward getTickler(ActionMapping mapping, ActionForm form,
										  HttpServletRequest request,HttpServletResponse response){

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		Integer demographicNo = demographicNumberToInteger(request);
		Map<String, Object> attributes = formeCARESManager.getTickler(loggedInInfo, null);
		for(Map.Entry<String, Object> entry: attributes.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());

		}
		request.setAttribute("demographicNo", demographicNo);
		return mapping.findForward("tickler");
	}

	public void export(ActionMapping mapping, ActionForm form,
					   HttpServletRequest request,HttpServletResponse response){

		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

		Integer demographicNo = demographicNumberToInteger(request);
		Integer formId = Integer.parseInt( request.getParameter(Constants.Cares.FormField.formId.name()) );
		JSONObject formData = formeCARESManager.getData(loggedInInfo, demographicNo, formId);
		ExcelCSVPrinter printer = null;

		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\"ecga_form_data.csv\"");
			printer = new ExcelCSVPrinter(response.getWriter());
			printer.writeln(new String[] { "Element", "Value"});
			Set keys = formData.keySet();
			for (Object key : keys) {
				printer.writeln(new String[] { (String)key, formData.getString((String)key)});
			}
		} catch (Exception e) {
			MiscUtils.getLogger().warn("Export failed for ecga form id " + formId, e);
			JSONObject responseMessage = new JSONObject();
			responseMessage.put("success", "false");
			jsonResponse(response, responseMessage);
		} finally {
			if(printer != null) {
				try {
					printer.flush();
				} catch (IOException e) {
					MiscUtils.getLogger().warn("Failed to flush stream for ecga form id " + formId, e);
				}
				try {
					printer.close();
				} catch (IOException e) {
					MiscUtils.getLogger().warn("Failed to close stream for ecga form id " + formId, e);
				}
			}
		}
	}

	/**
	 * Crazy OSCAR.  It may be possible that the demographic number
	 * variable name is slightly different.
	 *
	 * This also kinda masks the potential for a parse int exception.
	 */
	private Integer demographicNumberToInteger(HttpServletRequest request) {
		String demographicNo = request.getParameter(Constants.Cares.FormField.demographicNo.name());
		if(demographicNo == null || demographicNo.isEmpty()) {
			demographicNo = request.getParameter(Constants.Cares.FormField.demographic_no.name());
		}
		if(demographicNo != null && ! demographicNo.isEmpty()) {
			return Integer.parseInt( demographicNo );
		}
		return null;
	}

}
