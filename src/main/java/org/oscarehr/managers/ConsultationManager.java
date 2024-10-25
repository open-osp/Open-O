/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
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
 *
 * Modifications made by Magenta Health in 2024.
 */
package org.oscarehr.managers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.DocumentException;
import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.dao.ConsultRequestDao;
import org.oscarehr.common.dao.ConsultResponseDao;
import org.oscarehr.common.dao.ConsultResponseDocDao;
import org.oscarehr.common.dao.ConsultationRequestArchiveDao;
import org.oscarehr.common.dao.ConsultationRequestExtArchiveDao;
import org.oscarehr.common.dao.ConsultationRequestExtDao;
import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.dao.DocumentDao.DocumentType;
import org.oscarehr.common.dao.DocumentDao.Module;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01.ObservationData;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.RefI12;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.SendingUtils;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.ConsultResponseDoc;
import org.oscarehr.common.model.ConsultationRequest;
import org.oscarehr.common.model.ConsultationRequestArchive;
import org.oscarehr.common.model.ConsultationRequestExt;
import org.oscarehr.common.model.ConsultationRequestExtArchive;
import org.oscarehr.common.model.ConsultationResponse;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.CtlDocumentPK;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Property;
import org.oscarehr.common.model.Provider;
import org.oscarehr.consultations.ConsultationRequestSearchFilter;
import org.oscarehr.consultations.ConsultationRequestSearchFilter.SORTDIR;
import org.oscarehr.consultations.ConsultationResponseSearchFilter;
import org.oscarehr.hospitalReportManager.HRMUtil;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.oscarehr.ws.rest.conversion.OtnEconsultConverter;
import org.oscarehr.ws.rest.to.model.ConsultationAttachment;
import org.oscarehr.ws.rest.to.model.ConsultationRequestSearchResult;
import org.oscarehr.ws.rest.to.model.ConsultationResponseSearchResult;
import org.oscarehr.ws.rest.to.model.OtnEconsult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.message.REF_I12;
import org.oscarehr.documentManager.EDoc;
import org.oscarehr.documentManager.EDocUtil;
import oscar.eform.EFormUtil;
import oscar.log.LogAction;
import oscar.oscarEncounter.data.EctFormData;
import oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ConsultationPDFCreator;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;

public interface ConsultationManager {	

	public final String CON_REQUEST_ENABLED = "consultRequestEnabled";
	public final String CON_RESPONSE_ENABLED = "consultResponseEnabled";
	public final String ENABLED_YES = "Y";
	
	public List<ConsultationRequestSearchResult> search(LoggedInInfo loggedInInfo, ConsultationRequestSearchFilter filter);
	
	public List<ConsultationResponseSearchResult> search(LoggedInInfo loggedInInfo, ConsultationResponseSearchFilter filter);
	
	public int getConsultationCount(ConsultationRequestSearchFilter filter);
	
	public int getConsultationCount(ConsultationResponseSearchFilter filter);
	public boolean hasOutstandingConsultations(LoggedInInfo loggedInInfo, Integer demographicNo);
	
	public ConsultationRequest getRequest(LoggedInInfo loggedInInfo, Integer id);
	
	public ConsultationResponse getResponse(LoggedInInfo loggedInInfo, Integer id);
	public List<ConsultationServices> getConsultationServices();
	public List<ProfessionalSpecialist> getReferringDoctorList();
	public ProfessionalSpecialist getProfessionalSpecialist(Integer id);
	
	public void saveConsultationRequest(LoggedInInfo loggedInInfo, ConsultationRequest request);
	
	public void saveConsultationResponse(LoggedInInfo loggedInInfo, ConsultationResponse response);
	
	public List<ConsultDocs> getConsultRequestDocs(LoggedInInfo loggedInInfo, Integer requestId);
	public List<ConsultResponseDoc> getConsultResponseDocs(LoggedInInfo loggedInInfo, Integer responseId);
	public void saveConsultRequestDoc(LoggedInInfo loggedInInfo, ConsultDocs doc);
	
	public void saveConsultResponseDoc(LoggedInInfo loggedInInfo, ConsultResponseDoc doc);
	public void enableConsultRequestResponse(boolean conRequest, boolean conResponse);
	
	public boolean isConsultRequestEnabled();
	public boolean isConsultResponseEnabled();
	public void doHl7Send(LoggedInInfo loggedInInfo, Integer consultationRequestId) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, HL7Exception, ServletException, com.lowagie.text.DocumentException;
	public void importEconsult(LoggedInInfo loggedInInfo, OtnEconsult otnEconsult) throws Exception;
	public List<Document> getEconsultDocuments(LoggedInInfo loggedInInfo, int demographicNo);
	public List<ConsultationAttachment> getEReferAttachments(LoggedInInfo loggedInInfo, HttpServletRequest request, HttpServletResponse response, Integer demographicNo) throws PDFGenerationException;
	
	public List<ProfessionalSpecialist> findByService(LoggedInInfo loggedInInfo, String serviceName);
	public List<ProfessionalSpecialist> findByServiceId(LoggedInInfo loggedInInfo, Integer serviceId);
	public List<ConsultDocs> getAttachedDocumentsByType(LoggedInInfo loggedInInfo, Integer consultRequestId, String docType);
	public Path renderConsultationForm(HttpServletRequest request) throws PDFGenerationException;

	public List<EctFormData.PatientForm> getAttachedForms(LoggedInInfo loggedInInfo, int consultRequestId, int demographicNo);

	public List<EFormData> getAttachedEForms(String requestId);

	public ArrayList<HashMap<String, ? extends Object>> getAttachedHRMDocuments(LoggedInInfo loggedInInfo, String demographicNo, String requestId);

	public void archiveConsultationRequest(Integer requestId);
	public void saveOrUpdateExts(int requestId, List<ConsultationRequestExt> extras);
	public Map<String, ConsultationRequestExt> getExtsAsMap(List<ConsultationRequestExt> extras);
	public Map<String, String> getExtValuesAsMap(List<ConsultationRequestExt> extras);
}
