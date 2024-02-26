/**
 * Copyright (c) 2015-2019. The Pharmacists Clinic, Faculty of Pharmaceutical Sciences, University of British Columbia. All Rights Reserved.
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
 * The Pharmacists Clinic
 * Faculty of Pharmaceutical Sciences
 * University of British Columbia
 * Vancouver, British Columbia, Canada
 */

package org.oscarehr.fax.action;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.FaxJob;
import org.oscarehr.common.model.FaxJob.STATUS;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.managers.FaxManager;
import org.oscarehr.managers.FaxManager.TransactionType;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.oscarehr.util.SpringUtils;
import oscar.form.JSONAction;


public class FaxAction extends JSONAction {

	private static final Logger logger=MiscUtils.getLogger();
	private final FaxManager faxManager = SpringUtils.getBean(FaxManager.class);
	private final DocumentAttachmentManager documentAttachmentManager = SpringUtils.getBean(DocumentAttachmentManager.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
		
	@SuppressWarnings("unused")
	public ActionForward cancel(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo 		= LoggedInInfo.getLoggedInInfoFromSession(request);
		DynaActionForm faxActionForm 	= (DynaActionForm) form;
		String faxFilePath 				= (String) faxActionForm.get("faxFilePath");
		Integer transactionId 			= (Integer) faxActionForm.get("transactionId");
		Integer demographicNo			= (Integer) faxActionForm.get("demographicNo");
		String transactionType			= faxActionForm.getString("transactionType").toUpperCase();
		ActionRedirect faxForward 		= new ActionRedirect(mapping.findForward(transactionType));
		
		faxManager.flush(loggedInInfo, faxFilePath);
		
		if(TransactionType.CONSULTATION.name().equalsIgnoreCase(transactionType))
		{
			faxForward.addParameter("de", demographicNo);
			faxForward.addParameter("requestId", transactionId);
		}
		else if(TransactionType.EFORM.name().equalsIgnoreCase(transactionType))
		{
			faxForward.addParameter("fdid", transactionId);
			faxForward.addParameter("parentAjaxId", "eforms");
		}

		return faxForward;
	}

	/**
	 * Set up fax parameters for this fax to be sent with the next timed
	 * batch process.
	 * This action assumes that the fax has already been produced and reviewed 
	 * by the user.
	 */
	@SuppressWarnings("unused")
	public ActionForward queue(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo 		= LoggedInInfo.getLoggedInInfoFromSession(request);
		DynaActionForm faxActionForm 	= (DynaActionForm) form;
		Integer transactionId 			= (Integer) faxActionForm.get("transactionId");
		TransactionType transactionType	= TransactionType.valueOf(faxActionForm.getString("transactionType").toUpperCase());		
		List<FaxJob> faxJobList 		= faxManager.createAndSaveFaxJob(loggedInInfo, faxActionForm);
		
		boolean success = true;
		for(FaxJob faxJob : faxJobList) 
		{
			faxManager.logFaxJob(loggedInInfo, faxJob, transactionType, transactionId);
			
			/*
			 * only one error will derail the entire fax job.
			 */
			if(STATUS.ERROR.equals(faxJob.getStatus()))
			{
				success = false;
			}
		}
					
		request.setAttribute("faxSuccessful", success);
		request.setAttribute("faxJobList", faxJobList);
		
		return mapping.findForward("preview");
	}
	

	/**
	 * Get a preview image of the entire fax document.
	 */
	@SuppressWarnings("unused")
	public void getPreview(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo 	= LoggedInInfo.getLoggedInInfoFromSession(request);
		String faxFilePath 			= request.getParameter("faxFilePath");
		String pageNumber			= request.getParameter("pageNumber");
		String showAs = request.getParameter("showAs");
		Path outfile 				= null;
		int page 					= 1;
		String jobId 				= request.getParameter("jobId");
		FaxJob faxJob 				= null;

		if(jobId != null && ! jobId.isEmpty()) {
			faxJob = faxManager.getFaxJob(loggedInInfo, Integer.parseInt(jobId));
		}

		if(faxJob != null) {
			faxFilePath = faxJob.getFile_name();
		}

		if(pageNumber != null && ! pageNumber.isEmpty()) {
			page = Integer.parseInt(pageNumber);
		}
		
		/*
		* Displaying the entire PDF using the default browser's view before faxing an EForm (in CoverPage.jsp), 
		* and when viewing it in the fax records (Manage Faxes), it is shown as images.
		*/
		if(faxFilePath != null && ! faxFilePath.isEmpty())
		{
			if (showAs != null && showAs.equals("image")) {
				outfile = faxManager.getFaxPreviewImage(loggedInInfo, faxFilePath, page);
				response.setContentType("image/pnsg");
				response.setHeader("Content-Disposition", "attachment;filename=" + outfile.getFileName().toString());
			} else {
				outfile = Paths.get(faxFilePath);
				response.setContentType("application/pdf");
			}
		}

		if(outfile != null)
		{
			try(InputStream inputStream = Files.newInputStream(outfile);
					BufferedInputStream bfis = new BufferedInputStream(inputStream);
					ServletOutputStream outs = response.getOutputStream()){
				
				int data;
				while ((data = bfis.read()) != -1) {
					outs.write(data);
				}
				outs.flush();
			} catch (IOException e) {
				log.error("Error", e);	
			}
		}
	}
	
	/**
	 * Prepare a PDF of the given parameters an then return a path to 
	 * the for the user to review and add a cover page before sending final.  
	 * 
	 */
	@SuppressWarnings("unused")
	public ActionForward prepareFax(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		LoggedInInfo loggedInInfo 		= LoggedInInfo.getLoggedInInfoFromSession(request);
		DynaActionForm faxActionForm 	= (DynaActionForm) form;
		/*
		 * Fax recipient info carried forward. 
		 */
		String recipient 				= (String) faxActionForm.get("recipient");
		String recipientFaxNumber 		= (String) faxActionForm.get("recipientFaxNumber");
		String letterheadFax			= (String) faxActionForm.get("letterheadFax");
		Integer demographicNo			= (Integer)faxActionForm.get("demographicNo");
		Integer transactionId 			= (Integer)faxActionForm.get("transactionId");
		TransactionType transactionType	= TransactionType.valueOf(faxActionForm.getString("transactionType").toUpperCase());		
		ActionForward actionForward 	= mapping.findForward("error");
		Path pdfPath 					= null;
		List<FaxConfig>	accounts		= faxManager.getFaxGatewayAccounts(loggedInInfo);

		/*
		 * No fax accounts - No Fax.
		 * This document is saved in a temporary directory as a PDF.
		 */
		if(! accounts.isEmpty())
		{
			if (transactionType.equals(TransactionType.EFORM)) {
				request.setAttribute("fdid", String.valueOf(transactionId));
				request.setAttribute("demographicId", String.valueOf(demographicNo));

				try {
					pdfPath = documentAttachmentManager.renderEFormWithAttachments(request, response);
				} catch (PDFGenerationException e) {
					logger.error(e.getMessage(), e);
					String errorMessage = "This eForm (and attachments, if applicable) cannot be faxed. \\n\\n" + e.getMessage();
					request.setAttribute("errorMessage", errorMessage);
					return mapping.findForward("eFormError");
				}
			}
		} else {
			request.setAttribute("message", "No active fax accounts found.");
		}
		
		if(pdfPath != null)
		{
			List<Path> documents = new ArrayList<>();
			documents.add(pdfPath);
			request.setAttribute("accounts", accounts);
			request.setAttribute("demographicNo", demographicNo);
			request.setAttribute("documents", documents);
			request.setAttribute("transactionType", transactionType.name());
			request.setAttribute("transactionId", transactionId);
			request.setAttribute("faxFilePath", pdfPath);
			request.setAttribute("letterheadFax", letterheadFax);
			request.setAttribute("professionalSpecialistName", recipient);
			request.setAttribute("fax", recipientFaxNumber);
			actionForward = mapping.findForward("preview");
		}
		
		return actionForward;
	}

	/**
	 * Get the actual number of pages in this PDF document.
	 */
	@SuppressWarnings("unused")
	public void getPageCount(ActionMapping mapping, ActionForm form,
							  HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String jobId 			  = request.getParameter("jobId");
		int pageCount = 0;

		if(jobId != null && ! jobId.isEmpty()) {
			pageCount = faxManager.getPageCount(loggedInInfo, Integer.parseInt(jobId));
		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jobId", jobId);
		jsonObject.put("pageCount", pageCount);

		jsonResponse(response, jsonObject);
	}

}
