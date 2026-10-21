//CHECKSTYLE:OFF

package ca.openosp.openo.managers;

import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
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
import ca.openosp.openo.casemgmt.service.CaseManagementManager.IssueType;
import ca.openosp.openo.commn.model.ConsultDocs;
import ca.openosp.openo.commn.model.ConsultResponseDoc;
import ca.openosp.openo.commn.model.ConsultationRequest;
import ca.openosp.openo.commn.model.ConsultationRequestExt;
import ca.openosp.openo.commn.model.ConsultationResponse;
import ca.openosp.openo.commn.model.ConsultationServices;
import ca.openosp.openo.commn.model.Document;
import ca.openosp.openo.commn.model.EFormData;
import ca.openosp.openo.commn.model.ProfessionalSpecialist;
import ca.openosp.openo.consultations.ConsultationRequestSearchFilter;
import ca.openosp.openo.consultations.ConsultationResponseSearchFilter;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.PDFGenerationException;
import ca.openosp.openo.webserv.rest.to.model.ConsultationAttachment;
import ca.openosp.openo.webserv.rest.to.model.ConsultationRequestSearchResult;
import ca.openosp.openo.webserv.rest.to.model.ConsultationResponseSearchResult;
import ca.openosp.openo.webserv.rest.to.model.OtnEconsult;

import ca.uhn.hl7v2.HL7Exception;
import ca.openosp.openo.encounter.data.EctFormData;

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

    public void doHl7Send(LoggedInInfo loggedInInfo, Integer consultationRequestId) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, HL7Exception, ServletException, DocumentException;

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

    /**
     * Builds the text a CPP section pastes into the consultation request. With the paste
     * preferences applied, the provider's CPP Paste Format preference decides the layout:
     * Single Line joins the notes on one line separated by commas, Multi Line keeps one note
     * per line, and the heading preference starts the paste with the section title. Without
     * them, the notes are pasted one per line with no heading.
     *
     * @param loggedInInfo LoggedInInfo the current session, also the provider whose preferences apply
     * @param demographicNo String the patient whose notes are pasted
     * @param issueType IssueType the CPP section to paste
     * @param applyPastePreferences boolean true applies the provider's paste format and heading preferences
     * @return String the pasted text, empty when the section has no notes
     */
    public String getCppPasteNote(LoggedInInfo loggedInInfo, String demographicNo, IssueType issueType, boolean applyPastePreferences);

    /**
     * Lays out entries for pasting into the consultation request, the same way
     * {@link #getCppPasteNote} lays out CPP notes: with the paste preferences applied, the
     * provider's CPP Paste Format and heading preferences decide the layout; without them,
     * the entries are pasted one per line with no heading.
     *
     * @param loggedInInfo LoggedInInfo the current session, also the provider whose preferences apply
     * @param entries List<String> the entries to paste, in paste order
     * @param heading String section title, pasted only when the provider turned headings on
     * @param applyPastePreferences boolean true applies the provider's paste format and heading preferences
     * @return String the pasted text, empty when there are no entries
     */
    public String formatPasteNote(LoggedInInfo loggedInInfo, List<String> entries, String heading, boolean applyPastePreferences);
}
