//CHECKSTYLE:OFF
package org.oscarehr.documentManager.actions;

import net.sf.json.JSONObject;
import oscar.eform.EFormUtil;
import oscar.oscarEncounter.data.EctFormData;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.enumerator.DocumentType;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.documentManager.EDoc;
import org.oscarehr.documentManager.EDocUtil;
import org.oscarehr.documentManager.data.AttachmentLabResultData;
import org.oscarehr.hospitalReportManager.HRMUtil;
import org.oscarehr.managers.FormsManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.oscarehr.util.SpringUtils;

import oscar.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class DocumentPreview2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private static final Logger logger = MiscUtils.getLogger();
    private final DocumentAttachmentManager documentAttachmentManager = SpringUtils.getBean(DocumentAttachmentManager.class);
    private final FormsManager formsManager = SpringUtils.getBean(FormsManager.class);

    private List<EDoc> allDocuments = new ArrayList<>();
    private List<EFormData> allEForms = new ArrayList<>();
    private ArrayList<HashMap<String, ? extends Object>> allHRMDocuments = new ArrayList<>();
    private List<AttachmentLabResultData> allLabsSortedByVersions = new ArrayList<>();
    private List<EctFormData.PatientForm> allForms = new ArrayList<>();

    public String execute() {
        if ("fetchEFormDocuments".equals(request.getParameter("method"))) {
            return fetchEFormDocuments();
        }
        return fetchConsultDocuments();
    }

    public void renderEDocPDF() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String eDocId = request.getParameter("eDocId");
        try {
            Path docPDFPath = documentAttachmentManager.renderDocument(loggedInInfo, DocumentType.DOC, Integer.parseInt(eDocId));
            generateResponse(response, docPDFPath);
        } catch (PDFGenerationException e) {
            logger.error("Error occured while rendering eDoc. " + e.getMessage(), e);
            generateResponse(response, e.getMessage());
        }
    }

    public void renderEFormPDF() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String eFormId = request.getParameter("eFormId");
        try {
            Path eFormPDFPath = documentAttachmentManager.renderDocument(loggedInInfo, DocumentType.EFORM, Integer.parseInt(eFormId));
            generateResponse(response, eFormPDFPath);
        } catch (PDFGenerationException e) {
            logger.error("Error occured while rendering eForm. " + e.getMessage(), e);
            generateResponse(response, e.getMessage());
        }
    }

    public void renderHrmPDF() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String hrmId = request.getParameter("hrmId");
        try {
            Path hrmPDFPath = documentAttachmentManager.renderDocument(loggedInInfo, DocumentType.HRM, Integer.parseInt(hrmId));
            generateResponse(response, hrmPDFPath);
        } catch (PDFGenerationException e) {
            logger.error("Error occured while rendering HRM. " + e.getMessage(), e);
            generateResponse(response, e.getMessage());
        }
    }

    public void renderLabPDF() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String segmentID = request.getParameter("segmentId");
        try {
            Path labPDFPath = documentAttachmentManager.renderDocument(loggedInInfo, DocumentType.LAB, Integer.parseInt(segmentID));
            generateResponse(response, labPDFPath);
        } catch (PDFGenerationException e) {
            logger.error("Error occured while rendering Lab. " + e.getMessage(), e);
            generateResponse(response, e.getMessage());
        }
    }

    public void renderFormPDF() {
        try {
            Path formPDFPath = documentAttachmentManager.renderDocument(request, response, DocumentType.FORM);
            generateResponse(response, formPDFPath);
        } catch (PDFGenerationException e) {
            logger.error("Error occured while rendering Form. " + e.getMessage(), e);
            generateResponse(response, e.getMessage());
        }
    }

    public void renderPDF() {
        String pdfPathString = StringUtils.isNullOrEmpty(request.getParameter("pdfPath")) ? "" : request.getParameter("pdfPath");
        Path pdfPath = Paths.get(pdfPathString);
        response.setContentType("application/pdf");
        try (InputStream inputStream = Files.newInputStream(pdfPath);
             BufferedInputStream bfis = new BufferedInputStream(inputStream);
             ServletOutputStream outs = response.getOutputStream()) {

            int data;
            while ((data = bfis.read()) != -1) {
                outs.write(data);
            }

            outs.flush();
        } catch (IOException e) {
            logger.error("Error", e);
        }
    }

    public String fetchConsultDocuments() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String demographicNo = StringUtils.isNullOrEmpty(request.getParameter("demographicNo")) ? "0" : request.getParameter("demographicNo");

        allDocuments = EDocUtil.listDocs(loggedInInfo, "demographic", demographicNo, null, EDocUtil.PRIVATE, EDocUtil.EDocSort.OBSERVATIONDATE);
        allEForms = EFormUtil.listPatientEformsCurrent(new Integer(demographicNo), true);
        allHRMDocuments = HRMUtil.listHRMDocuments(loggedInInfo, "report_date", false, demographicNo, false);
        allLabsSortedByVersions = documentAttachmentManager.getAllLabsSortedByVersions(loggedInInfo, demographicNo);
        allForms = formsManager.getEncounterFormsbyDemographicNumber(loggedInInfo, Integer.parseInt(demographicNo), false, true);

        return forwardDocuments();
    }

    public String fetchEFormDocuments() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String demographicNo = StringUtils.isNullOrEmpty(request.getParameter("demographicNo")) ? "0" : request.getParameter("demographicNo");
        String fdid = StringUtils.isNullOrEmpty(request.getParameter("fdid")) ? "0" : request.getParameter("fdid");

        allDocuments = EDocUtil.listDocs(loggedInInfo, "demographic", demographicNo, null, EDocUtil.PRIVATE, EDocUtil.EDocSort.OBSERVATIONDATE);
        allEForms = documentAttachmentManager.getAllEFormsExpectFdid(loggedInInfo, Integer.parseInt(demographicNo), Integer.parseInt(fdid));
        allHRMDocuments = HRMUtil.listHRMDocuments(loggedInInfo, "report_date", false, demographicNo, false);
        allLabsSortedByVersions = documentAttachmentManager.getAllLabsSortedByVersions(loggedInInfo, demographicNo);
        allForms = formsManager.getEncounterFormsbyDemographicNumber(loggedInInfo, Integer.parseInt(demographicNo), false, true);

        return forwardDocuments();
    }

    private void generateResponse(HttpServletResponse response, Path pdfPath) throws PDFGenerationException {
        JSONObject json = new JSONObject();
        String base64Data = documentAttachmentManager.convertPDFToBase64(pdfPath);
        json.put("base64Data", base64Data);
        response.setContentType("text/javascript");
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            throw new PDFGenerationException("An error occurred while writing JSON response to the output stream", e);
        }
    }

    private void generateResponse(HttpServletResponse response, String errorMessage) {
        JSONObject json = new JSONObject();
        json.put("errorMessage", errorMessage);
        response.setContentType("text/javascript");
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            logger.error("An error occurred while writing JSON response to the output stream", e);
        }
    }

    private String forwardDocuments() {
        request.setAttribute("allDocuments", allDocuments);
        request.setAttribute("allLabsSortedByVersions", allLabsSortedByVersions);
        request.setAttribute("allForms", allForms);
        request.setAttribute("allEForms", allEForms);
        request.setAttribute("allHRMDocuments", allHRMDocuments);
        return "fetchDocuments";
    }
}
