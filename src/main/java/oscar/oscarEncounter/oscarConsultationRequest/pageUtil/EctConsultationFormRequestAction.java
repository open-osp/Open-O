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

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.message.REF_I12;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01.ObservationData;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.RefI12;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.SendingUtils;
import org.oscarehr.common.model.*;
import org.oscarehr.common.model.enumerator.DocumentType;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.documentManager.EDoc;
import org.oscarehr.documentManager.EDocUtil;
import org.oscarehr.fax.core.FaxRecipient;
import org.oscarehr.managers.*;
import org.oscarehr.managers.FaxManager.TransactionType;
import org.oscarehr.util.*;
import oscar.OscarProperties;
import oscar.oscarEncounter.data.EctFormData;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ParameterActionForward;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EctConsultationFormRequestAction extends Action {

	private static final Logger logger=MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private ConsultationManager consultationManager = SpringUtils.getBean(ConsultationManager.class);
	private final DocumentAttachmentManager documentAttachmentManager = SpringUtils.getBean(DocumentAttachmentManager.class);
	private FaxManager faxManager = SpringUtils.getBean(FaxManager.class);

	private final DigitalSignatureManager digitalSignatureManager = SpringUtils.getBean(DigitalSignatureManager.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "w", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		EctConsultationFormRequestForm frm = (EctConsultationFormRequestForm) form;		

		String appointmentHour = frm.getAppointmentHour();
		String appointmentPm = frm.getAppointmentPm();
        String[] attachedDocuments = frm.getDocNo();
        String[] attachedLabs = frm.getLabNo();
		String[] attachedForms = frm.getFormNo();
		String[] attachedEForms = frm.geteFormNo();
		String[] attachedHRMDocuments = frm.getHrmNo();
        List<String> documents = new ArrayList<String>();

		if (appointmentPm.equals("PM") && Integer.parseInt(appointmentHour) < 12 ) {
			appointmentHour = Integer.toString(Integer.parseInt(appointmentHour) + 12);
		}
                else if( appointmentHour.equals("12") && appointmentPm.equals("AM") ) {
                    appointmentHour = "0";
                }		

		String sendTo = frm.getSendTo();
		String submission = frm.getSubmission();
		String providerNo = frm.getProviderNo();
		String demographicNo = frm.getDemographicNo();

		String requestId = "";

		boolean newSignature = request.getParameter("newSignature") != null && request.getParameter("newSignature").equalsIgnoreCase("true");
		String signatureId = null;
		String signatureImg = frm.getSignatureImg();
		if(StringUtils.isBlank(signatureImg)) {
			signatureImg = request.getParameter("newSignatureImg");
			if(signatureImg ==null) {
				signatureImg = "";
			}
		}
	
		
        ConsultationRequestDao consultationRequestDao=(ConsultationRequestDao)SpringUtils.getBean("consultationRequestDao");
        ConsultationRequestExtDao consultationRequestExtDao=(ConsultationRequestExtDao)SpringUtils.getBean("consultationRequestExtDao");
        ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
        DemographicManager demographicManager = SpringUtils.getBean( DemographicManager.class );
        
        String[] format = new String[] {"yyyy-MM-dd","yyyy/MM/dd"};

		if (submission.startsWith("Submit")) {

			try {				
								if (newSignature) {
//									DigitalSignature signature = DigitalSignatureUtils.storeDigitalSignatureFromTempFileToDB(loggedInInfo, signatureImg, Integer.parseInt(demographicNo));
									DigitalSignature signature = digitalSignatureManager.processAndSaveDigitalSignature(loggedInInfo, signatureImg, Integer.parseInt(demographicNo));
									if (signature != null) { signatureId = "" + signature.getId(); }
								}
				
								
                                ConsultationRequest consult = new ConsultationRequest();
                                String dateString = frm.getReferalDate();
                                Date date = null;
                                if(dateString != null && ! dateString.isEmpty()) {
                                	date = DateUtils.parseDate(dateString, format);
                                }
                                consult.setReferralDate(date);
                                consult.setServiceId(new Integer(frm.getService()));

                                consult.setSignatureImg(signatureId);
                                
                        		consult.setLetterheadName(frm.getLetterheadName());
                        		consult.setLetterheadAddress(frm.getLetterheadAddress());
                        		consult.setLetterheadPhone(frm.getLetterheadPhone());
                        		consult.setLetterheadFax(frm.getLetterheadFax());
                        		
								if (frm.getAppointmentDate() != null && !frm.getAppointmentDate().equals("")) {
									date = DateUtils.parseDate(frm.getAppointmentDate(), format);
									consult.setAppointmentDate(date);
									
									if(!StringUtils.isEmpty(appointmentHour) && !StringUtils.isEmpty(frm.getAppointmentMinute())) {
										try {
											date = DateUtils.setHours(date, new Integer(appointmentHour));
											date = DateUtils.setMinutes(date, new Integer(frm.getAppointmentMinute()));
											consult.setAppointmentTime(date);
										}
										catch(NumberFormatException nfEx) {
							                MiscUtils.getLogger().error("Invalid Time", nfEx);
										}
									}
								} else {
									consult.setAppointmentDate(null);
									consult.setAppointmentTime(null);
								}
                                consult.setReasonForReferral(frm.getReasonForConsultation());
                                consult.setClinicalInfo(frm.getClinicalInformation());
                                consult.setCurrentMeds(frm.getCurrentMedications());
                                consult.setAllergies(frm.getAllergies());
                                consult.setProviderNo(frm.getProviderNo());
                                consult.setDemographicId(new Integer(frm.getDemographicNo()));
                                consult.setStatus(frm.getStatus());
                                consult.setStatusText(frm.getAppointmentNotes());
                                consult.setSendTo(frm.getSendTo());
                                consult.setConcurrentProblems(frm.getConcurrentProblems());
                                consult.setUrgency(frm.getUrgency());
                                consult.setAppointmentInstructions( frm.getAppointmentInstructions() );
                                consult.setSiteName(frm.getSiteName());
                                Boolean pWillBook = false;
                                if( frm.getPatientWillBook() != null ) {
                                    pWillBook = frm.getPatientWillBook().equals("1");
                                }
                                consult.setPatientWillBook(pWillBook);

                                if( frm.getFollowUpDate() != null && !frm.getFollowUpDate().equals("") ) {
                                    date = DateUtils.parseDate(frm.getFollowUpDate(), format);
                                    consult.setFollowUpDate(date);
                                }

								Integer specId = null;

								if(! frm.getSpecialist().isEmpty()) {
									specId = Integer.parseInt(frm.getSpecialist());
								}

                                // converting the newer Contacts Table and Health Care Team back and forth
                                // from the older professionalSpecialist module.
                                // This should persist and retrieve values to be backwards compatible.
                                if( OscarProperties.getInstance().getBooleanProperty("ENABLE_HEALTH_CARE_TEAM_IN_CONSULTATION_REQUESTS", "true") ) { 
                                	
                                	// when this is enabled the demographicContactId is being posted as a specId variable.
                                	Integer demographicContactId = new Integer( specId );
                                	
                                	// specId is reset to unknown.
                                	specId = 0;
                                	
                                	DemographicContact demographicContact = demographicManager.getHealthCareMemberbyId( loggedInInfo, demographicContactId );	                            	
                                	
                                	if( demographicContact != null ) {
                                		
                                		consult.setDemographicContact( demographicContact );

                                		// If the demographicContact is holding the specId, then retrieve it for backwards 
                                		// compatibility. For the most part only contacts in the professionalSpecialist table should get through the 
                                		// filters.
                                		if( DemographicContact.TYPE_PROFESSIONALSPECIALIST == demographicContact.getType() ) {
                                			specId = Integer.parseInt( demographicContact.getContactId() );
                                		}                      		
                                	}	                                	 
                                } 
                                 
                                // only add the professionalSpecialist if it checks out. 0 will obviously return a null.
                                ProfessionalSpecialist professionalSpecialist = professionalSpecialistDao.find( specId );
                                
                                if( professionalSpecialist != null ) {
                                	request.setAttribute("professionalSpecialistName", professionalSpecialist.getFormattedTitle() );
                                    consult.setProfessionalSpecialist(professionalSpecialist); 
                                }
                           
                                consultationRequestDao.persist( consult ); 
                                
                                requestId = String.valueOf(consult.getId());                                    
                                MiscUtils.getLogger().debug("saved new consult id "+ requestId );
     
                                Enumeration e = request.getParameterNames();
                                while(e.hasMoreElements()) {
                                	String name = (String)e.nextElement();
                                	if(name.startsWith("ext_")) {
                                		String value = request.getParameter(name);
                                		consultationRequestExtDao.persist(createExtEntry(requestId,name.substring(name.indexOf("_")+1),value));
                                	}
                                }

                                // now that we have consultation id we can save any attached docs as well
								documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.DOC, attachedDocuments, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
								documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.LAB, attachedLabs, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
								documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.FORM, attachedForms, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
								documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.EFORM, attachedEForms, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
								documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.HRM, attachedHRMDocuments, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
			}
	        catch (ParseException e) {
	                MiscUtils.getLogger().error("Invalid Date", e);
	        }

	        request.setAttribute("reqId", requestId);
			request.setAttribute("transType", "2");

		} else

		if (submission.startsWith("Update")) {

		
			requestId = frm.getRequestId();

			consultationManager.archiveConsultationRequest(Integer.parseInt(requestId));
			
			try {				     
				
				if (newSignature) {
					DigitalSignature signature = DigitalSignatureUtils.storeDigitalSignatureFromTempFileToDB(loggedInInfo, signatureImg, Integer.parseInt(demographicNo));
					if (signature != null) {
						signatureId = "" + signature.getId();
					} else {
						signatureId = null;
					}
				} else {
					signatureId = signatureImg;
				}
				
                ConsultationRequest consult = consultationRequestDao.find(new Integer(requestId));
                Date date = DateUtils.parseDate(frm.getReferalDate(), format);
                consult.setReferralDate(date);
                consult.setServiceId(new Integer(frm.getService()));
                consult.setSignatureImg(signatureId);
                consult.setProviderNo(frm.getProviderNo());
        		consult.setLetterheadName(frm.getLetterheadName());
        		consult.setLetterheadAddress(frm.getLetterheadAddress());
        		consult.setLetterheadPhone(frm.getLetterheadPhone());
        		consult.setLetterheadFax(frm.getLetterheadFax());

				Integer specId = null;
				if(!frm.getSpecialist().isEmpty()) {
					specId = new Integer(frm.getSpecialist());
				}

                // converting the newer Contacts Table and Health Care Team back and forth
                // from the older professionalSpecialist module.
                // This should persist and retrieve values to be backwards compatible.
                if( OscarProperties.getInstance().getBooleanProperty("ENABLE_HEALTH_CARE_TEAM_IN_CONSULTATION_REQUESTS", "true") ) {                                	
                	DemographicContact demographicContact = demographicManager.getHealthCareMemberbyId( loggedInInfo, specId );	                            	
                	if( demographicContact != null ) {
                		consult.setDemographicContact(demographicContact);
                		
                		// add in the professional specialist to enable backwards compatibility.
                		if( DemographicContact.TYPE_PROFESSIONALSPECIALIST == demographicContact.getType() ) {
                			specId = Integer.parseInt( demographicContact.getContactId() );
                		}                      		
                	}	                                	 
                } 
                 
                // only add the professionalSpecialist if it checks out.
                ProfessionalSpecialist professionalSpecialist = new ProfessionalSpecialist();
				if(specId != null) {
					professionalSpecialist = professionalSpecialistDao.find(specId);
				}

                if( professionalSpecialist != null ) {
                		request.setAttribute("professionalSpecialistName", professionalSpecialist.getFormattedTitle());
                    consult.setProfessionalSpecialist(professionalSpecialist);                                   
                }
                
                
                if( frm.getAppointmentDate() != null && !frm.getAppointmentDate().equals("") ) {
                	date = DateUtils.parseDate(frm.getAppointmentDate(), format);
                	consult.setAppointmentDate(date);
					try {
						date = DateUtils.setHours(date, new Integer(appointmentHour));
						date = DateUtils.setMinutes(date, new Integer(frm.getAppointmentMinute()));
						consult.setAppointmentTime(date);
					}catch(NumberFormatException nfEx) {
						MiscUtils.getLogger().error("Invalid Time", nfEx);
					}
                } else {
					consult.setAppointmentDate(null);
					consult.setAppointmentTime(null);
				}
                consult.setReasonForReferral(frm.getReasonForConsultation());
                consult.setClinicalInfo(frm.getClinicalInformation());
                consult.setCurrentMeds(frm.getCurrentMedications());
                consult.setAllergies(frm.getAllergies());
                consult.setDemographicId(new Integer(frm.getDemographicNo()));
                consult.setStatus(frm.getStatus());
                consult.setStatusText(frm.getAppointmentNotes());
                consult.setSendTo(frm.getSendTo());
                consult.setConcurrentProblems(frm.getConcurrentProblems());
                consult.setUrgency(frm.getUrgency());
                consult.setAppointmentInstructions( frm.getAppointmentInstructions() );
                consult.setSiteName(frm.getSiteName());
                 Boolean pWillBook = false;
                if( frm.getPatientWillBook() != null ) {
                    pWillBook = frm.getPatientWillBook().equals("1");
                }
                consult.setPatientWillBook(pWillBook);

                if( frm.getFollowUpDate() != null && !frm.getFollowUpDate().equals("") ) {
                    date = DateUtils.parseDate(frm.getFollowUpDate(), format);
                    consult.setFollowUpDate(date);
                }
                consultationRequestDao.merge(consult);
                
                consultationRequestExtDao.clear(Integer.parseInt(requestId));
                Enumeration e = request.getParameterNames();
                while(e.hasMoreElements()) {
                	String name = (String)e.nextElement();
                	if(name.startsWith("ext_")) {
                		String value = request.getParameter(name);
                		consultationRequestExtDao.persist(createExtEntry(requestId,name.substring(name.indexOf("_")+1),value));
                	}
                }
                
                // save any additional attachments added on the update


				documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.DOC, attachedDocuments, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
				documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.LAB, attachedLabs, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
				documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.FORM, attachedForms, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
				documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.EFORM, attachedEForms, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
				documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.HRM, attachedHRMDocuments, providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));
			}

			catch (ParseException e) {
				MiscUtils.getLogger().error("Error", e);
			}

			request.setAttribute("transType", "1");

		}
		else if( submission.equalsIgnoreCase("And Print Preview")) {
			renderConsultationFormWithAttachments(request, response, frm.getRequestId(), demographicNo);
			generatePDFResponse(request, response);
			return null;
		}
				

		frm.setRequestId("");

		request.setAttribute("teamVar", sendTo);

		if (submission.endsWith("And Print Preview")) {
			boolean SUCCESS = renderConsultationFormWithAttachments(request, response, requestId, demographicNo);
			if (SUCCESS) { return mapping.findForward("success"); }
			return mapping.findForward("error");
		} else if (submission.endsWith("And Fax")) {
			
			String[] faxRecipients = request.getParameterValues("faxRecipients");
			HashSet<FaxRecipient> copytoRecipients = new HashSet<FaxRecipient>();
			
			if(faxRecipients != null) {
				for(String recipient : faxRecipients) {
					JSONObject jsonObject = JSONObject.fromObject(recipient);
					String fax = jsonObject.getString("fax");
					String name = jsonObject.getString("name");
					copytoRecipients.add(new FaxRecipient(name, fax));
				}
			}
			
			
			// call-back document descriptions into documents parameter.
			List<EDoc> attachedDocumentList = EDocUtil.listDocs(loggedInInfo, demographicNo, requestId, EDocUtil.ATTACHED);
	        CommonLabResultData commonLabResultData = new CommonLabResultData();
			List<LabResultData> attachedLabList = commonLabResultData.populateLabResultsData(loggedInInfo, demographicNo, requestId, CommonLabResultData.ATTACHED);

			List<EctFormData.PatientForm> attachedFormsList = consultationManager.getAttachedForms(loggedInInfo, Integer.parseInt(requestId), Integer.parseInt(demographicNo));

			List<EFormData> attachedEFormsList = consultationManager.getAttachedEForms(requestId);

			ArrayList<HashMap<String, ? extends Object>> attachedHRMDocumentsList = consultationManager.getAttachedHRMDocuments(loggedInInfo, demographicNo, requestId);

	        if(attachedDocumentList != null) {
	        	for(EDoc documentItem : attachedDocumentList) {
	        		String description = documentItem.getDescription();
	        		if( description == null || description == "") {
	        			description = documentItem.getFileName();
	        		}
	        		documents.add(description);
	        	}
	        }
	        
	        if(attachedLabList != null) {
	           	for(LabResultData labResultData : attachedLabList) {
	           		documents.add(labResultData.getDisciplineDisplayString());
	        	}
	        }

			if(attachedFormsList != null && ! attachedFormsList.isEmpty()) {
				for(EctFormData.PatientForm attachedForm : attachedFormsList) {
					documents.add(attachedForm.formName);
				}
			}

			if(attachedEFormsList != null && ! attachedEFormsList.isEmpty()) {
				for(EFormData attachedEForm : attachedEFormsList) {
					documents.add(attachedEForm.getFormName());
				}
			}

			if (attachedHRMDocumentsList != null && !attachedHRMDocumentsList.isEmpty()) {
				for (HashMap<String, ? extends Object> attachedHRMDocument : attachedHRMDocumentsList) {
					documents.add(attachedHRMDocument.get("name")+"");
				}
			}

			List<FaxConfig>	accounts = faxManager.getFaxGatewayAccounts(loggedInInfo);

			// fax number that will display on the letterhead
	        request.setAttribute("letterheadFax", frm.getLetterheadFax());
			// fax account that will be used to send the fax
			request.setAttribute("faxAccount", frm.getFaxAccount());
		  	request.setAttribute("documents", documents);			
			request.setAttribute("copyToRecipients", copytoRecipients);
			request.setAttribute("reqId", requestId);
			request.setAttribute("accounts", accounts);
			request.setAttribute("transactionType", TransactionType.CONSULTATION.name());
			request.setAttribute("transType", "consultRequest");
			
			return mapping.findForward("fax");
			
		} 
		else if (submission.endsWith("esend"))
		{
			// upon success continue as normal with success message
			// upon failure, go to consultation update page with message
			try {
	            doHl7Send(loggedInInfo, Integer.parseInt(requestId));
	            WebUtils.addLocalisedInfoMessage(request, "oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgCreatedUpdateESent");
            } catch (Exception e) {
            	logger.error("Error contacting remote server.", e);
            	
            	WebUtils.addLocalisedErrorMessage(request, "oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgCreatedUpdateESendError");
	    		ParameterActionForward forward = new ParameterActionForward(mapping.findForward("failESend"));
	    		forward.addParameter("de", demographicNo);
	    		forward.addParameter("requestId", requestId);
	    		return forward;
            }
		}
			

		ParameterActionForward forward = new ParameterActionForward(mapping.findForward("success"));
		forward.addParameter("de", demographicNo);
		return forward;
	}
	
	private ConsultationRequestExt createExtEntry(String requestId, String name,String value) {
		ConsultationRequestExt obj = new ConsultationRequestExt();
		obj.setDateCreated(new Date());
		obj.setKey(name);
		obj.setValue(value);
		obj.setRequestId(Integer.parseInt(requestId));
		return obj;
	}
	
	private void doHl7Send(LoggedInInfo loggedInInfo, Integer consultationRequestId) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, HL7Exception, ServletException {
		
	    ConsultationRequestDao consultationRequestDao=(ConsultationRequestDao)SpringUtils.getBean("consultationRequestDao");
	    ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
	    Hl7TextInfoDao hl7TextInfoDao=(Hl7TextInfoDao)SpringUtils.getBean("hl7TextInfoDao");
	    ClinicDAO clinicDAO=(ClinicDAO)SpringUtils.getBean("clinicDAO");

	    ConsultationRequest consultationRequest=consultationRequestDao.find(consultationRequestId);
	    ProfessionalSpecialist professionalSpecialist=professionalSpecialistDao.find(consultationRequest.getSpecialistId());
	    Clinic clinic=clinicDAO.getClinic();
	    
	    // set status now so the remote version shows this status
	    consultationRequest.setStatus("2");

	    REF_I12 refI12=RefI12.makeRefI12(clinic, consultationRequest);
	    SendingUtils.send(loggedInInfo, refI12, professionalSpecialist);
	    
	    // save after the sending just in case the sending fails.
	    consultationRequestDao.merge(consultationRequest);
	    
	    //--- send attachments ---
    	Provider sendingProvider=loggedInInfo.getLoggedInProvider();
    	DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
    	Demographic demographic=demographicManager.getDemographic(loggedInInfo, consultationRequest.getDemographicId());

    	//--- process all documents ---
	    ArrayList<EDoc> attachments=EDocUtil.listDocs(loggedInInfo, demographic.getDemographicNo().toString(), consultationRequest.getId().toString(), EDocUtil.ATTACHED);
	    for (EDoc attachment : attachments)
	    {
	        ObservationData observationData=new ObservationData();
	        observationData.subject=attachment.getDescription();
	        observationData.textMessage="Attachment for consultation : "+consultationRequestId;
	        observationData.binaryDataFileName=attachment.getFileName();
	        observationData.binaryData=attachment.getFileBytes();

	        ORU_R01 hl7Message=OruR01.makeOruR01(clinic, demographic, observationData, sendingProvider, professionalSpecialist);        
	        SendingUtils.send(loggedInInfo, hl7Message, professionalSpecialist);	    	
	    }
	    
	    //--- process all labs ---
        CommonLabResultData labData = new CommonLabResultData();
        ArrayList<LabResultData> labs = labData.populateLabResultsData(loggedInInfo, demographic.getDemographicNo().toString(), consultationRequest.getId().toString(), CommonLabResultData.ATTACHED);
	    for (LabResultData attachment : labs)
	    {
	    	try {
	            byte[] dataBytes=LabPDFCreator.getPdfBytes(attachment.getSegmentID(), sendingProvider.getProviderNo());
	            Hl7TextInfo hl7TextInfo=hl7TextInfoDao.findLabId(Integer.parseInt(attachment.getSegmentID()));
	            
	            ObservationData observationData=new ObservationData();
	            observationData.subject=hl7TextInfo.getDiscipline();
	            observationData.textMessage="Attachment for consultation : "+consultationRequestId;
	            observationData.binaryDataFileName=hl7TextInfo.getDiscipline()+".pdf";
	            observationData.binaryData=dataBytes;

	            
	            ORU_R01 hl7Message=OruR01.makeOruR01(clinic, demographic, observationData, sendingProvider, professionalSpecialist);        
	            int statusCode=SendingUtils.send(loggedInInfo, hl7Message, professionalSpecialist);
	            if (HttpServletResponse.SC_OK!=statusCode) throw(new ServletException("Error, received status code:"+statusCode));
            } catch (com.lowagie.text.DocumentException e) {
	            logger.error("Unexpected error.", e);
            }	    	
	    }
    }

	private boolean renderConsultationFormWithAttachments(HttpServletRequest request, HttpServletResponse response, String requestId, String demographicNo) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

		request.setAttribute("reqId", requestId);
		request.setAttribute("demographicId", demographicNo);
		String fileName = generateFileName(loggedInInfo, Integer.parseInt(demographicNo));
		String base64PDF = "";
		try {
			Path pdfPath = documentAttachmentManager.renderConsultationFormWithAttachments(request, response);
			base64PDF = documentAttachmentManager.convertPDFToBase64(pdfPath);
		} catch(PDFGenerationException e) {
			logger.error(e.getMessage(), e);
			String errorMessage = "A print preview of this consultation could not be generated. \\n\\n" + e.getMessage();
			request.setAttribute("errorMessage", errorMessage);
			return false;
		}

		request.setAttribute("consultPDFName", fileName);
		request.setAttribute("consultPDF", base64PDF);
		request.setAttribute("isPreviewReady", "true");
		return true;
	}

	private void generatePDFResponse(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		json.put("consultPDF", (String) request.getAttribute("consultPDF"));
		json.put("consultPDFName", (String) request.getAttribute("consultPDFName"));
		json.put("errorMessage", (String) request.getAttribute("errorMessage"));
		response.setContentType("text/javascript");
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private String generateFileName(LoggedInInfo loggedInInfo, int demographicNo) {
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		String demographicLastName = demographicManager.getDemographicFormattedName(loggedInInfo, demographicNo).split(", ")[0];

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
		String formattedDate = dateFormat.format(currentDate);

		return formattedDate + "_" + demographicLastName + ".pdf";
	}

}
