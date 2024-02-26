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
package org.oscarehr.managers;



import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.dao.EFormDao.EFormSortOrder;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormGroupDao;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.oscarehr.documentManager.ConvertToEdoc;
import org.oscarehr.documentManager.EDoc;
import oscar.form.util.FormTransportContainer;
import oscar.log.LogAction;
import oscar.oscarEncounter.data.EctFormData;
import oscar.oscarEncounter.data.EctFormData.PatientForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * This class will change soon to incorporate dealing with forms
 *
 */
@Service
public class FormsManager {
	private final Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private EFormDao eformDao;
	
	@Autowired
	private EFormGroupDao eFormGroupDao;

	@Autowired
	private EFormDataDao eFormDataDao; 
	
	@Autowired
	private EncounterFormDao encounterFormDao;
	
	@Autowired
	DocumentManager documentManager;
	
	@Autowired
	private SecurityInfoManager securityInfoManager;

	public static final String EFORM = "eform"; 
	public static final String FORM = "form";
	
	
	
	/**
	 * Finds all eforms based on the status. 
	 * 
	 * @param status	
	 * 		Status to be used when looking up forms. 
	 * @param sortOrder
	 * 		Order how records should be sorted. Providing no sort order delegates to the default sorting order of the persistence provider 
	 * @return
	 * 		Returns the list of all forms with the specified status.
	 */

	public List<EForm> findByStatus(LoggedInInfo loggedInInfo, boolean status, EFormSortOrder sortOrder) {
		List<EForm> results = eformDao.findByStatus(status, sortOrder);
		
		if (results.size() > 0) {
			String resultIds = EForm.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "FormsManager.findByStatus", "ids returned=" + resultIds);
		}

		return (results);
	}


    /**
     * get eform in group by group name
     * @param groupName
     * @return list of EForms
     */
    public List<EForm> getEfromInGroupByGroupName(LoggedInInfo loggedInInfo, String groupName){
    	List<EForm> results = eformDao.getEfromInGroupByGroupName(groupName);
    	if (results.size() > 0) {
			String resultIds = EForm.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "FormsManager.getEfromInGroupByGroupName", "ids returned=" + resultIds);
		}

		return (results);
    }
    
    
    public List<String> getGroupNames(){
    	return eFormGroupDao.getGroupNames();
    }
    
    
    public List<EFormData> findByDemographicId(LoggedInInfo loggedInInfo, Integer demographicId){
    	List<EFormData> results = eFormDataDao.findByDemographicId(demographicId);
    	if (results.size() > 0) {
			String resultIds = EForm.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "FormsManager.findByDemographicId", "ids returned=" + resultIds);
		}

		return (results);
    	
    }
      
	public List<EncounterForm> getAllEncounterForms() {
		List<EncounterForm> results = encounterFormDao.findAll();
		Collections.sort(results, EncounterForm.FORM_NAME_COMPARATOR);
		return (results);
	}
	
	public List<EncounterForm> getSelectedEncounterForms() {
		List<EncounterForm> results = encounterFormDao.findAllNotHidden();
		Collections.sort(results, EncounterForm.FORM_NAME_COMPARATOR);
		return (results);
	}
	
	public List<PatientForm> getEncounterFormsbyDemographicNumber(LoggedInInfo loggedInInfo, Integer demographicId, boolean getAllVersions, boolean getOnlyPDFReadyForms) {
  		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_form", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_form)");
		}

		return processEncounterForms(loggedInInfo, demographicId, getAllVersions, getOnlyPDFReadyForms);
	}

	private List<PatientForm> processEncounterForms(LoggedInInfo loggedInInfo, Integer demographicId, boolean getAllVersions, boolean getOnlyPDFReadyForms) {
		List<PatientForm> patientFormList = new ArrayList<PatientForm>();
		List<EncounterForm> encounterFormList = getAllEncounterForms();
		String[] pdfReadyFormNames = {"Annual"};

		for (EncounterForm encounterForm : encounterFormList) {
			String formName = encounterForm.getFormName();
			if (getOnlyPDFReadyForms && !Arrays.asList(pdfReadyFormNames).contains(formName)) { continue; }

			String table = encounterForm.getFormTable();
			PatientForm[] patientFormArray = EctFormData.getPatientForms(demographicId + "", table);
			int maxFormsToProcess = getAllVersions ? patientFormArray.length : Math.min(1, patientFormArray.length);
			for (int i = 0; i < maxFormsToProcess; i++) {
				PatientForm patientForm = patientFormArray[i];
				patientForm.setTable(table);
				patientForm.setFormName(formName);
				patientFormList.add(patientForm);
			}
		}

		return patientFormList;
	}

	/**
	 * Saves a form as PDF EDoc. 
	 * Returns the id of the converted document. 
	 */
	public Integer saveFormDataAsEDoc( LoggedInInfo loggedInInfo, FormTransportContainer formTransportContainer ) {
		
  		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_edoc", SecurityInfoManager.WRITE, null)) {
			throw new RuntimeException("missing required security object (_eform)");
		}
		
		EDoc edoc = ConvertToEdoc.from( formTransportContainer );
		documentManager.moveDocument( loggedInInfo, edoc.getDocument(), edoc.getFilePath(), null );
		edoc.setFilePath(null);
		Integer documentId = documentManager.saveDocument( loggedInInfo, edoc );

		if( documentId != null ) {
			LogAction.addLogSynchronous(loggedInInfo, "FormsManager.saveFormDataAsEDoc", "Document ID saved: " + documentId );
		} else {
			LogAction.addLogSynchronous(loggedInInfo, "FormsManager.saveFormDataAsEDoc", "Document conversion for Form " + edoc.getFileName() + " failed.");
		}
		
		return documentId;
	}

	public Path renderForm(LoggedInInfo loggedInInfo, FormTransportContainer formTransportContainer) {
  		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_form", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_form)");
		}
  		
  		LogAction.addLogSynchronous(loggedInInfo, "FormsManager.saveFormAsTempPdf", "" );
  		
		return ConvertToEdoc.saveAsTempPDF(formTransportContainer);
	}

	/**
	 * This method processes a PatientForm, which can be null, and retrieves data using the 'formId', 'formName',
	 * and 'demographicNo' parameters from the HttpServletRequest request.
	 *
	 * @param form The PatientForm to process (can be null).
	 * @param request The HttpServletRequest containing the parameters.
	 */
	public Path renderForm(HttpServletRequest request, HttpServletResponse response, EctFormData.PatientForm form) throws PDFGenerationException {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_form", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_form)");
		}

		FormTransportContainer formTransportContainer = getFormTransportContainer(request, response, form);
		Path path = null;
		try {
			path = ConvertToEdoc.saveAsTempPDF(formTransportContainer);
		} catch (Exception e) {
			throw new PDFGenerationException("Error Details: Form [" + formTransportContainer.getFormName() + "] could not be converted into a PDF", e);
		}
		return path;
	}

	private FormTransportContainer getFormTransportContainer(HttpServletRequest request, HttpServletResponse response, EctFormData.PatientForm form) throws PDFGenerationException {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String formId = request.getParameter("formId") != null ? request.getParameter("formId") : form.getFormId();
		String formName = request.getParameter("formName") != null ? request.getParameter("formName") : form.getFormName();
		String demographicNo = request.getParameter("demographicNo") != null ? request.getParameter("demographicNo") : form.getDemoNo();
		String formPath = "/form/forwardshortcutname.jsp?method=fetch&formname=" + formName + "&demographic_no=" + demographicNo + "&formId=" + formId;
		FormTransportContainer formTransportContainer = null;
		try {
			formTransportContainer = new FormTransportContainer(response, request, formPath);
			formTransportContainer.setDemographicNo(demographicNo);
			formTransportContainer.setProviderNo(loggedInInfo.getLoggedInProviderNo());
			formTransportContainer.setSubject(formName + " Form ID " + formId);
			formTransportContainer.setFormName(formName);
			formTransportContainer.setRealPath(request.getServletContext().getRealPath(File.separator));
		} catch (ServletException | IOException e) {
			throw new PDFGenerationException("An error occurred while processing the form. " + "Form name: " + formName, e);
		}
		return formTransportContainer;
	}

}

