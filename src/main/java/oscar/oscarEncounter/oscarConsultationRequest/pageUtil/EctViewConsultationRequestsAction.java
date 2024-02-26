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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EctViewConsultationRequestsAction extends Action {
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    private static final Logger logger= MiscUtils.getLogger();

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    	
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "r", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
    	
        EctViewConsultationRequestsForm frm = (EctViewConsultationRequestsForm) form;
        
        String defaultPattern = "yyyy-MM-dd";   
        String sendTo = null;
        String includeCompleted = null;                
        String startDate = frm.getStartDate();
        String endDate = frm.getEndDate();
        boolean includedComp = false;        
                
        sendTo = frm.getSendTo();
        includeCompleted = frm.getIncludeCompleted();
                                
        if(includeCompleted != null && includeCompleted.equals("include") ){
           includedComp = true; 
        }

        SimpleDateFormat simpleDateFormat = null;

        try{
            if (startDate != null && !startDate.isEmpty()) {
                simpleDateFormat = new SimpleDateFormat(defaultPattern);
                request.setAttribute("startDate", simpleDateFormat.parse(startDate));
            }

            if (endDate != null && !endDate.isEmpty()) {
                if (simpleDateFormat == null) {
                    simpleDateFormat = new SimpleDateFormat(defaultPattern);
                }
                request.setAttribute("endDate", simpleDateFormat.parse(endDate));
            }
        } catch (Exception e) {
            logger.error("Cannot parse start date " + startDate + " and/or end date " + endDate + " for consultation request report. ", e);
        }

        request.setAttribute("includeCompleted",new Boolean(includedComp));               
        request.setAttribute("teamVar", sendTo);
        request.setAttribute("orderby",frm.getOrderby());
        request.setAttribute("desc",frm.getDesc());
        request.setAttribute("searchDate",frm.getSearchDate());                
        return mapping.findForward("success");
    }
    
    
}
