/**
 * Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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
package org.oscarehr.integration.mcedt.mailbox;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.integration.mcedt.DelegateFactory;
import org.oscarehr.integration.mcedt.McedtMessageCreator;
import org.oscarehr.integration.mcedt.ResourceForm;

import ca.ontario.health.edt.Detail;
import ca.ontario.health.edt.DetailData;
import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.ResourceStatus;
import ca.ontario.health.edt.TypeListResult;


public class ResourceAction extends DispatchAction {

	private static Logger logger = org.oscarehr.util.MiscUtils.getLogger();
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		//functions needed for the upload page
		ActionUtils.removeSuccessfulUploads(request);
		ActionUtils.removeUploadResponseResults(request);
		ActionUtils.removeSubmitResponseResults(request);
		Date startDate= ActionUtils.getOutboxTimestamp();
		Date endDate = new Date();
		if (startDate!=null && endDate!=null) {
			ActionUtils.moveOhipToOutBox(startDate,endDate);

			/*
			 * The method ActionUtils.moveObecToOutBox is slow with many files in 'OscarDocument/oscar/document/'.
			 * To optimize, we will move OBEC files during generation rather than during MCEDT mailbox opening.
			 * See ObecData.writeFile() for details on the updated process.
			 */
			// ActionUtils.moveObecToOutBox(startDate,endDate);	

			ActionUtils.setOutboxTimestamp(endDate);
		}
		ActionUtils.setUploadResourceId(request, new BigInteger("-1"));
		
		
		if(request.getSession().getAttribute("resourceList")!=null){
			request.getSession().removeAttribute("resourceList");
		}		
		if(request.getSession().getAttribute("resourceID")!=null){
			request.getSession().removeAttribute("resourceID");
		}
		if(request.getSession().getAttribute("info")!=null){
			request.getSession().removeAttribute("info");
		}		
		return mapping.findForward("success");
	}
	
	//----------------------------------
	public ActionForward loadDownloadList(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
	HttpServletResponse response) throws Exception {		

		List<DetailDataCustom> resourceList;
		try{
			resetPage(form);
			resourceList = loadList(form,request,response,ResourceStatus.DOWNLOADABLE);			
			request.getSession().setAttribute("resourceListDL",resourceList);
		}
		catch(Exception e) {
			logger.error("Unable to load resource list ", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			return mapping.findForward("success");
		}				
		 
		return mapping.findForward("successUserDownload");		
	}	
	
	private void resetPage(ActionForm form){
		ResourceForm resourceForm = (ResourceForm)form;
		resourceForm.setResourceType("");
		resourceForm.setStatus("");
		resourceForm.setPageNo(1);		
	}
	
	public ActionForward loadSentList(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ResourceForm resourceForm = (ResourceForm) form;
		resetPage(form);
		List<DetailDataCustom> resourceList;		
		List<DetailDataCustom> resourceListFiltered = new ArrayList<DetailDataCustom>();
		
		try{
			if(request.getSession().getAttribute("resourceTypeList")==null){
				EDTDelegate delegate = DelegateFactory.getEDTDelegateInstance();
				resourceForm.setTypeListResult(getTypeList(request, delegate));
			}
			resourceForm.setStatus("UPLOADED");			
			resourceList = loadList(form,request,response,ResourceStatus.UPLOADED);
			request.getSession().setAttribute("resourceListSent",resourceList);
			request.getSession().setAttribute("resourceStatus","UPLOADED");
		}
		catch(Exception e){
			return mapping.findForward("successUserSent");
		}
		return mapping.findForward("successUserSent");
	}
			
	public List<DetailDataCustom> loadList( ActionForm form, HttpServletRequest request, 
		HttpServletResponse response, ResourceStatus resourceStatus) throws Exception {
			
			ResourceForm resourceForm = (ResourceForm) form;						
			
			try{
				EDTDelegate delegate = DelegateFactory.getEDTDelegateInstance();
				
				
				if(request.getSession().getAttribute("resourceTypeList")==null){
					resourceForm.setTypeListResult(getTypeList(request, delegate));
					request.getSession().setAttribute("resourceTypeList",resourceForm.getTypeListResult());
				}
				else{
					resourceForm.setTypeListResult((TypeListResult)request.getSession().getAttribute("resourceTypeList"));
				}			

			return getResourceList(request, resourceForm, delegate, resourceStatus);
		}
		catch(Exception e) {
			logger.error("Unable to load resource list ", e);
			saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			return null;
		}					
		
	}		
			
	private List<DetailDataCustom> getResourceList(HttpServletRequest request, ResourceForm form, EDTDelegate delegate, ResourceStatus resourceStatus) {
	    Detail result = ActionUtils.getDetails(request);
	    List<DetailDataCustom> resourceList =new ArrayList<DetailDataCustom>();	    
	    
	    
	    if (result == null) {
		    try {
		    	String resourceType = form.getResourceType();
		    	if (resourceType != null && resourceType.trim().isEmpty()) {
		    		resourceType = null;
		    	}	    	
					
				result = delegate.list(resourceType, resourceStatus, form.getPageNoAsBigInt());
				
				BigInteger resultSize = null;				
				if(result!=null)
					resultSize = result.getResultSize();				
				request.getSession().setAttribute("resultSize",resultSize);
				
				if(result!=null && result.getData()!=null){ 
									
					DetailDataCustom detailDataK;
					for(DetailData detailData : result.getData()){						

						detailDataK = new DetailDataCustom();
						detailDataK = ActionUtils.mapDetailData(form, detailDataK, detailData);
						resourceList.add(detailDataK);						
					}							
					
				}							
					
				
			} catch (Exception e) {
				logger.error("Unable to load resource list ", e);
				saveErrors(request, ActionUtils.addMessage("resourceAction.getResourceList.fault", McedtMessageCreator.exceptionToString(e)));
			
			}
	    }	    
	    return resourceList;
    }	
	
	
	public ActionForward changeDisplay(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		return reset(mapping, form, request, response);
	}

	private ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    ActionUtils.removeDetails(request);
		return unspecified(mapping, form, request, response);
    }


	private TypeListResult getTypeList(HttpServletRequest request, EDTDelegate delegate) {
		TypeListResult result = ActionUtils.getTypeList(request); 
		if (result == null) {
			try {
				result = delegate.getTypeList();
				ActionUtils.setTypeList(request, result);
			} catch (Exception e) {
				logger.error("Unable to load type list", e);
				
				saveErrors(request, ActionUtils.addMessage("resourceAction.getTypeList.fault", McedtMessageCreator.exceptionToString(e)));
			}
		}
		return result;
    }
}