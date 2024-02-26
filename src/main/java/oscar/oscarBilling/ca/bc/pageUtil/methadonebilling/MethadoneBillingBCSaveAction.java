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


package oscar.oscarBilling.ca.bc.pageUtil.methadonebilling;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


/**
 * @author OSCARprn by Treatment - support@oscarprn.com
 * @Company OSCARprn by Treatment
 * @Date Nov 30, 2016
 * @Filename MethadoneBillingBCSaveAction.java
 * @Comment Copy Right OSCARprn by Treatment
 */
public class MethadoneBillingBCSaveAction extends Action{

	public MethadoneBillingBCSaveAction(){}


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        if (request.getSession().getAttribute("user") == null) {
            return (mapping.findForward("Logout"));
        }

        char billAccountStatus = request.getParameter("status").charAt(0);

        MethadoneBillingBCFormBean methadoneBillingBCFormBean = (MethadoneBillingBCFormBean) form;
        MethadoneBillingBCHandler methadoneBillingHandler = new MethadoneBillingBCHandler(methadoneBillingBCFormBean);

    	ActionMessages messages = new ActionMessages();


    	if (methadoneBillingBCFormBean.getServiceDate() == null || methadoneBillingBCFormBean.getServiceDate().length() == 0) {
    		messages.add("serviceDate", new ActionMessage("quickBillingBC.blankServiceDate"));
    		this.addErrors(request, messages);

    		return mapping.findForward("error");
    	}

        if(methadoneBillingHandler.saveBills(billAccountStatus)) {

        	methadoneBillingHandler.reset();
        	request.setAttribute("saved", methadoneBillingHandler.getNumberSaved());
			return mapping.findForward("saved");

        } else {
        	messages.add("bills", new ActionMessage("Can't save bills"));
        	this.addErrors(request, messages);

        	request.setAttribute("saved", new Boolean(false));
        	return mapping.findForward("error");

        }

    }
}



