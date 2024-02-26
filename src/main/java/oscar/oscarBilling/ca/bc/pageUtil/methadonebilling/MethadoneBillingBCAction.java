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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;

import oscar.oscarBilling.ca.bc.data.BillingFormData;
import oscar.oscarBilling.ca.bc.data.BillingFormData.BillingVisit;
import oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean;

/**
 * @author OSCARprn by Treatment - support@oscarprn.com
 * @Company OSCARprn by Treatment
 * @Date Nov 30, 2016
 * @Filename MethadoneBillingBCAction.java
 * @Comment Copy Right OSCARprn by Treatment
 */
public class MethadoneBillingBCAction extends Action{

	private MethadoneBillingBCFormBean methadoneBillingBCFormBean;
	private MethadoneBillingBCHandler methadoneBillingHandler;

	public MethadoneBillingBCAction(){

	}

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

    	String creator = (String) request.getSession().getAttribute("user");

        if (creator == null) {
            return (mapping.findForward("Logout"));
        }

        String billingType = request.getParameter("type");

		methadoneBillingBCFormBean = (MethadoneBillingBCFormBean) form;
		methadoneBillingHandler = new MethadoneBillingBCHandler(methadoneBillingBCFormBean);

		response.setContentType("text/text;charset=utf-8");
		response.setHeader("cache-control", "no-cache");

    	if(request.getParameter("remove") != null) {

	    	if(methadoneBillingHandler.removeBill(request.getParameter("remove"))) {

	    		return mapping.findForward("success");

    		}
	    } else if(request.getParameter("rosterStatus") != null && request.getParameter("billingProviderNo") != null) {

	    	methadoneBillingBCFormBean.setDemographics(methadoneBillingHandler.getDemographicDao().getDemographicByRosterStatus(methadoneBillingBCFormBean.getRosterStatus(), methadoneBillingBCFormBean.getPatientStatus()));

	    	methadoneBillingBCFormBean.setBillingData(new ArrayList<BillingSessionBean>());
	    	for (Demographic demographic : methadoneBillingBCFormBean.getDemographics()) {
	    		if (demographic.getProviderNo().equals(methadoneBillingBCFormBean.getBillingProviderNo())) {
	    			methadoneBillingHandler.addBill(demographic, billingType);
	    		}
	    	}

	    } else {  // if not adding or removing data then create a fresh form.
	    	// add some needed form elements to the bean
	    	BillingFormData billingFormData = new BillingFormData();
	    	List<BillingVisit> billingVisit = billingFormData.getVisitType(MethadoneBillingBCHandler.BILLING_PROV);

	    	List<Provider> activeProviders = methadoneBillingHandler.getProviderDao().getActiveProviders();
	    	methadoneBillingBCFormBean.setProviderList(activeProviders);

	    	methadoneBillingHandler.reset();
	    	methadoneBillingBCFormBean.setBillingVisitTypes(billingVisit);


	    }

    	return mapping.findForward("success");
    }

}
