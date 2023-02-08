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


// form_class - a part of class name
// c_lastVisited, formId - if the form has multiple pages
package oscar.form;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

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
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;

public final class FrmAction extends Action {
    
    Logger log = org.oscarehr.util.MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    @SuppressWarnings("rawtypes")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_form", "w", null)) {
			throw new SecurityException("missing required security object (_form)");
		}
    	    	
    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        int newID = -1;
        FrmRecord rec = null;
        
        /* submission of this form is a failure by default */
        String where = "failure";
        ActionForward actionForward = mapping.findForward(where);
        boolean saveSuccess = Boolean.FALSE;
        String formClassName = request.getParameter("form_class");
        String submitType = request.getParameter("submit");
        String id = request.getParameter("formId");
        Integer formId = null;
        
        if(id == null || id.isEmpty())
        {
        	id = request.getParameter("ID");
        }
        
        if(id != null && ! id.isEmpty())
        {
        	id = id.trim();
        	formId = Integer.parseInt(id);
        }

        try {
            
            Integer demographicNo = Integer.parseInt(request.getParameter("demographic_no").trim());
          	
            FrmRecordFactory recorder = new FrmRecordFactory();
            rec = recorder.factory(formClassName);
            Properties props = new Properties();
                     
            log.info("SUBMIT " + submitType);
            
            //if we are graphing, we need to grab info from db and add it to request object
            if( "graph".equals(submitType) )
            {
            	//Rourke needs to know what type of graph is being plotted
            	String graphType = request.getParameter("__graphType");
            	if( graphType != null ) {
            		rec.setGraphType(graphType);
            	}
            	
               props = rec.getGraph(loggedInInfo, demographicNo, formId);
               
               for( Enumeration e = props.propertyNames(); e.hasMoreElements(); ) {
                   String name = (String)e.nextElement();                   
                   request.setAttribute(name,props.getProperty(name));                   
               }
               newID = 0;
            }
            //if we are printing all pages of form, grab info from db and merge with current page info
            else if( "printAll".equals(submitType) ) {
                props = rec.getFormRecord(loggedInInfo, demographicNo, formId);
                
                String name;
                for( Enumeration e = props.propertyNames(); e.hasMoreElements();) {
                    name = (String)e.nextElement();
                    if( request.getParameter(name) == null )
                    {
                    	request.setAttribute(name,props.getProperty(name));
                    }                        
                }
                newID = 0;
            }
            else {
                boolean bMulPage = request.getParameter("c_lastVisited") != null ? true : false;
                String name;

                if (bMulPage) {
                    String curPageNum = request.getParameter("c_lastVisited");
                    String commonField = request.getParameter("commonField") != null ? request.getParameter("commonField") : "&'";
                    curPageNum = curPageNum.length() > 3 ? ("" + curPageNum.charAt(0)) : curPageNum;

                    //copy an old record
                    props = rec.getFormRecord(loggedInInfo, demographicNo, formId);

                    //empty the current page
                    Properties currentParam = new Properties();
                    for (Enumeration varEnum = request.getParameterNames(); varEnum.hasMoreElements();) {
                        name = (String) varEnum.nextElement();
                        currentParam.setProperty(name, "");
                    }
                    for (Enumeration varEnum = props.propertyNames(); varEnum.hasMoreElements();) {
                        name = (String) varEnum.nextElement();
                        // kick off the current page elements, commonField on the current page
                        if (name.startsWith(curPageNum + "_")
                                || (name.startsWith(commonField) && currentParam.containsKey(name))) {
                            props.remove(name);
                        }
                    }
                }

                //update the current record
                for (Enumeration varEnum = request.getParameterNames(); varEnum.hasMoreElements();) {
                    name = (String) varEnum.nextElement();                    
                    props.setProperty(name, request.getParameter(name));                    
                }

                props.setProperty("provider_no", (String) request.getSession().getAttribute("user"));
                newID = rec.saveFormRecord(props);
                
                if(newID > 0)
                {
                	log.info(formClassName + " new form ID " + newID + " successfully saved.");
                	saveSuccess = Boolean.TRUE;
                }
                else
                {
                	log.info(formClassName + " form ID " + formId + " failed to save.");
                }
                
                String ip = request.getRemoteAddr();
                LogAction.addLog((String) request.getSession().getAttribute("user"), 
                		LogConst.ADD, 
                		formClassName, 
                		"" + newID, 
                		ip,
                		demographicNo+"");

            }
            
            /*
             * Forward to the proper link based on the submitType if this form validates 
             * and is successfully saved.
             */
            if(newID > -1)
            {
	            String strAction = rec.findActionValue(submitType);            
	            actionForward = mapping.findForward(strAction);
	            where = actionForward.getPath();
	            where = rec.createActionURL(where, strAction, demographicNo+"", "" + newID);
	            actionForward = new ActionForward(where);
            }

        } catch (Exception ex) {
            // throw new ServletException(ex);
        	log.error("Exception for form " + formClassName + " Save failed.", ex );
        }

        log.info("Forwarding form " + formClassName + " to " + actionForward.getPath());
        
        request.setAttribute("saveSuccess", saveSuccess);
        
        return actionForward; 
    }

}
