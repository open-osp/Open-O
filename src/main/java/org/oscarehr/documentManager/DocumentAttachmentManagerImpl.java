package org.oscarehr.documentManager;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.dao.EFormDocsDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormDocs;
import org.oscarehr.hospitalReportManager.HRMUtil;
import org.oscarehr.managers.*;
import org.oscarehr.common.model.enumerator.DocumentType;
import org.oscarehr.documentManager.data.AttachmentLabResultData;
import org.oscarehr.util.DateUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.PDFGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.eform.EFormUtil;
import oscar.oscarEncounter.data.EctFormData;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import oscar.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class DocumentAttachmentManagerImpl implements DocumentAttachmentManager {
	@Autowired
	private ConsultDocsDao consultDocsDao;
	@Autowired
	private EFormDocsDao eFormDocsDao;

	@Autowired
	private ConsultationManager consultationManager;
	@Autowired
	private DocumentManager documentManager;
	@Autowired
	private EformDataManager eformDataManager;
	@Autowired
	private FormsManager formsManager;
	@Autowired
	private LabManager labManager;
	@Autowired
	private NioFileManager nioFileManager;
	@Autowired
	private SecurityInfoManager securityInfoManager;

	// @Autowired
    // public void setEformDataManager(EformDataManager eformDataManager) {
    //     this.eformDataManager = eformDataManager;
    // }

	public List<String> getConsultAttachments(LoggedInInfo loggedInInfo, Integer requestId, DocumentType documentType, Integer demographicNo) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_con", SecurityInfoManager.READ, demographicNo)) {
			throw new RuntimeException("missing required security object (_con)");
		}

		List<String> consultAttachments = new ArrayList<>();
		List<ConsultDocs> consultDocs = consultDocsDao.findByRequestIdDocType(requestId, documentType.getType());
		for (ConsultDocs consultDocs1 : consultDocs) {
			consultAttachments.add(String.valueOf(consultDocs1.getDocumentNo()));
		}
		return consultAttachments;
	}

	public List<String> getEFormAttachments(LoggedInInfo loggedInInfo, Integer fdid, DocumentType documentType, Integer demographicNo) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_eform", SecurityInfoManager.READ, demographicNo)) {
			throw new RuntimeException("missing required security object (_eform)");
		}

		List<String> eFormAttachments = new ArrayList<>();
		List<EFormDocs> eFormDocs = eFormDocsDao.findByFdidIdDocType(fdid, documentType.getType());
		for (EFormDocs eFormDocs1 : eFormDocs) {
			eFormAttachments.add(String.valueOf(eFormDocs1.getDocumentNo()));
		}
		return eFormAttachments;
	}

	public List<EctFormData.PatientForm> getFormsAttachedToEForms(LoggedInInfo loggedInInfo, Integer fdid, DocumentType documentType, Integer demographicNo) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_eform", SecurityInfoManager.READ, demographicNo)) {
			throw new RuntimeException("missing required security object (_eform)");
		}

		List<EctFormData.PatientForm> attachedForms = new ArrayList<>();
		List<String> attachedFormIds = getEFormAttachments(loggedInInfo, fdid, documentType, demographicNo);
		List<EctFormData.PatientForm> allForms = formsManager.getEncounterFormsbyDemographicNumber(loggedInInfo, demographicNo, true, true);
		for (String formId : attachedFormIds) {
			for (EctFormData.PatientForm form : allForms) {
				if (!form.getFormId().equals(formId)) { continue; }
				attachedForms.add(form);
			}
		}

		return attachedForms;
	}

	/**
	 * This method is responsible for lab version sorting and is intended for use in the attachment window (attachDocument.jsp).
	 * In other parts of the application, developers should utilize CommonLabResultData.populateLabResultsData() to access all available lab data.
	 */
	public List<AttachmentLabResultData> getAllLabsSortedByVersions(LoggedInInfo loggedInInfo, String demographicNo) {
		CommonLabResultData commonLabResultData = new CommonLabResultData();
		List<LabResultData> allLabs = commonLabResultData.populateLabResultsData(loggedInInfo, "", demographicNo, "", "", "", "U");
		Collections.sort(allLabs);

		List<String> allLabVersionIds = new ArrayList<>();
		List<AttachmentLabResultData> allLabsSortedByVersions = new ArrayList<>();

		Map<String, LabResultData> labMap = new HashMap<>();
		for (LabResultData lab : allLabs) {
			labMap.put(lab.getSegmentID(), lab);
		}

		/*
		 * Explaining this code with an example:
		 * Let's assume the 'allLabs' variable contains these lab IDs [1, 2, 3, 4, 5, 6, 7, 8, 9, 10].
		 * Among these IDs, ID 2 is the latest version, and 3, 4, and 5 are older versions of that.
		 * Similarly, 6 is the latest version, and 1, 7, 8, and 9 are older versions of that.
		 * Lab ID 10 doesn't have any version.
		 *
		 * First, I iterate through the 'allLabs' using a for loop.
		 */
		for (LabResultData lab : allLabs) {
			if (allLabVersionIds.contains(lab.getSegmentID())) { continue; }

			AttachmentLabResultData attachmentLabResultData = new AttachmentLabResultData(lab.getSegmentID(), getDisplayLabName(lab), lab.getDateObj());

			/*
			 * Then, if, for example, I pass lab ID 1, it will give all its related labs in the correct version order.
			 * By 'correct order,' I mean it will return this array [7, 9, 8, 1, 6].
			 * This array will be in version order, where the first is the oldest and the last is the latest.
			 */
			String[] matchingLabIds = Hl7textResultsData.getMatchingLabs(lab.getSegmentID()).split(",");


			/*
			 * Here, I add the latest lab (6) to 'allLabsSortedByVersions' after attaching its versions (7, 9, 8, and 1) to the latest lab.
			 */
			for (int i = matchingLabIds.length - 2; i >= 0; i--) {
				LabResultData versionLab = labMap.get(matchingLabIds[i]);
				if (versionLab != null) { attachmentLabResultData.getLabVersionIds().put(versionLab.getSegmentID(), DateUtils.formatDate(versionLab.getDateObj(), null)); }

				/*
				 * Then, I add those version labs (7, 9, and 8) into the 'allLabVersionIds' array so that they can be skipped.
				 * At the start of the for loop, I use `if (allLabVersionIds.contains(lab.getSegmentID())) { continue; }` to ensure that labs already included in 'allLabVersionIds' are skipped during the iteration.
				 */
				allLabVersionIds.add(matchingLabIds[i]);
			}
			allLabsSortedByVersions.add(attachmentLabResultData);
		}
		return allLabsSortedByVersions;
	}

	/**
	 * This method is intended for use in the attachment window (attachDocument.jsp) and is designed to retrieve a list of eForms except one.
	 * In other parts of the application, developers are encouraged to use EFormUtil.listPatientEformsCurrent() to access all available eForms.
	 * The reason for this function is to ensure a user cannot attach an eForm to itself.
	 */
	public List<EFormData> getAllEFormsExpectFdid(LoggedInInfo loggedInInfo, Integer demographicNo, Integer fdid) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_eform", SecurityInfoManager.READ, demographicNo)) {
			throw new RuntimeException("missing required security object (_eform)");
		}

		List<EFormData> allEForms = EFormUtil.listPatientEformsCurrent(demographicNo, true);
		Iterator<EFormData> iterator = allEForms.iterator();
		while (iterator.hasNext()) {
			EFormData eForm = iterator.next();
			if (fdid.equals(eForm.getId())) {
				iterator.remove();
			}
		}

		return allEForms;
	}

	public void attachToConsult(LoggedInInfo loggedInInfo, DocumentType documentType, String[] attachments, String providerNo, Integer requestId, Integer demographicNo) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_con", SecurityInfoManager.WRITE, demographicNo)) {
			throw new RuntimeException("missing required security object (_con)");
		}

		DocumentAttach documentAttach = new DocumentAttach();
		documentAttach.attachToConsult(attachments, documentType, providerNo, requestId);
	}

	/*
	 * @param editOnOcean When editOnOcean is set to false, it signifies a normal consult request, performing just attach or detach operations on the consult request form.
	 * When editOnOcean is set to true, it signifies that the attach or detach operation is being performed on a consult request created by OceanMD.
	 * In this case, it will do two things:
	 * 1. Attach or detach attachments from the consult request.
	 * 2. Add those new attachments to the 'EreferAttachment' table, so Oscar can sent those attachment to OceanMD.
	 * By doing this, the user will not have to manually upload new attachments to e-refer. They will be automatically fetched.
	 */
	public void attachToConsult(LoggedInInfo loggedInInfo, DocumentType documentType, String[] attachments, String providerNo, Integer requestId, Integer demographicNo, Boolean editOnOcean) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_con", SecurityInfoManager.WRITE, demographicNo)) {
			throw new RuntimeException("missing required security object (_con)");
		}

		DocumentAttach documentAttach = new DocumentAttach(demographicNo, editOnOcean);
		documentAttach.attachToConsult(attachments, documentType, providerNo, requestId);
	}

	public void attachToEForm(LoggedInInfo loggedInInfo, DocumentType documentType, String[] attachments, String providerNo, Integer fdid, Integer demographicNo) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_eform", SecurityInfoManager.WRITE, demographicNo)) {
			throw new RuntimeException("missing required security object (_eform)");
		}

		DocumentAttach documentAttach = new DocumentAttach();
		documentAttach.attachToEForm(attachments, documentType, providerNo, fdid);
	}

	public Path concatPDF(ArrayList<Object> pdfDocumentList) throws PDFGenerationException {
		Path path = null;
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			ConcatPDF.concat(pdfDocumentList, outputStream);
			path = nioFileManager.saveTempFile("combinedPDF_" + new Date().getTime(), outputStream);
			flattenPDFFormFields(path);
		} catch (IOException e) {
			throw new PDFGenerationException("An error occurred while concatenating PDF.", e);
		}
		return path;
	}

	public Path concatPDF(List<Path> pdfDocuments) throws PDFGenerationException {
		ArrayList<Object> pdfDocumentList = new ArrayList<>();
		for (Path pdfDocument : pdfDocuments) { pdfDocumentList.add(pdfDocument.toString()); }
		return concatPDF(pdfDocumentList);
	}

	public Path renderDocument(HttpServletRequest request, HttpServletResponse response, DocumentType documentType) throws PDFGenerationException {
		return renderDocument(null, request, response, documentType, 0);
	}

	/**
	 * This renderDocument method is written to render EForms, Docs, HRMs and Labs.
	 *
	 * @param loggedInInfo  The LoggedInInfo object.
	 * @param documentType  The type of the document to be rendered.
	 * @param documentId    The documentId integer.
	 * @return The Path to the rendered document.
	 */
	public Path renderDocument(LoggedInInfo loggedInInfo, DocumentType documentType, Integer documentId) throws PDFGenerationException {
		return renderDocument(loggedInInfo, null, null, documentType, documentId);
	}

	public Path renderConsultationFormWithAttachments(HttpServletRequest request, HttpServletResponse response) throws PDFGenerationException {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String requestId = (String) request.getAttribute("reqId");
		String demographicId = (String) request.getAttribute("demographicId");
		Path consultationFormPDFPath = consultationManager.renderConsultationForm(request);

		List<EFormData> attachedEForms = consultationManager.getAttachedEForms(requestId);
		List<EDoc> attachedEDocs = EDocUtil.listDocs(loggedInInfo, demographicId, requestId, EDocUtil.ATTACHED);
		CommonLabResultData labResultData = new CommonLabResultData();
		List<LabResultData> attachedLabs = labResultData.populateLabResultsData(loggedInInfo, demographicId, requestId, CommonLabResultData.ATTACHED);
		ArrayList<HashMap<String, ? extends Object>> attachedHRMs = consultationManager.getAttachedHRMDocuments(loggedInInfo, demographicId, requestId);
		List<EctFormData.PatientForm> attachedForms = consultationManager.getAttachedForms(loggedInInfo, Integer.parseInt(requestId), Integer.parseInt(demographicId));

		ArrayList<Object> pdfDocumentList = new ArrayList<>();
		pdfDocumentList.add(consultationFormPDFPath.toString());
		attachEFormPDFs(loggedInInfo, attachedEForms, pdfDocumentList);
		attachEDocPDFs(loggedInInfo, attachedEDocs, pdfDocumentList);
		attachLabPDFs(loggedInInfo, attachedLabs, pdfDocumentList);
		attachHRMPDFs(loggedInInfo, attachedHRMs, pdfDocumentList);
		attachFormPDFs(request, response, attachedForms, pdfDocumentList);

		return concatPDF(pdfDocumentList);
	}

	public Path renderEFormWithAttachments(HttpServletRequest request, HttpServletResponse response) throws PDFGenerationException {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String fdid = (String) request.getAttribute("fdid");
		String demographicId = (String) request.getAttribute("demographicId");
		Path eFormPath = eformDataManager.createEformPDF(loggedInInfo, Integer.parseInt(fdid));

		List<EFormData> attachedEForms = EFormUtil.listPatientEformsCurrentAttachedToEForm(fdid);
		List<EDoc> attachedEDocs = EDocUtil.listDocsAttachedToEForm(loggedInInfo, demographicId, fdid, EDocUtil.ATTACHED);
		CommonLabResultData labResultData = new CommonLabResultData();
		List<LabResultData> attachedLabs = labResultData.populateLabResultsDataEForm(loggedInInfo, demographicId, fdid, CommonLabResultData.ATTACHED);
		ArrayList<HashMap<String, ? extends Object>> attachedHRMs = eformDataManager.getHRMDocumentsAttachedToEForm(loggedInInfo, fdid, demographicId);
		List<EctFormData.PatientForm> attachedForms = eformDataManager.getFormsAttachedToEForm(loggedInInfo, fdid, demographicId);

		ArrayList<Object> pdfDocumentList = new ArrayList<>();
		pdfDocumentList.add(eFormPath.toString());
		attachEFormPDFs(loggedInInfo, attachedEForms, pdfDocumentList);
		attachEDocPDFs(loggedInInfo, attachedEDocs, pdfDocumentList);
		attachLabPDFs(loggedInInfo, attachedLabs, pdfDocumentList);
		attachHRMPDFs(loggedInInfo, attachedHRMs, pdfDocumentList);
		attachFormPDFs(request, response, attachedForms, pdfDocumentList);

		return concatPDF(pdfDocumentList);
	}

	public Integer saveEFormAsEDoc(HttpServletRequest request, HttpServletResponse response) throws PDFGenerationException {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String fdid = (String) request.getAttribute("fdid");
		String demographicId = (String) request.getAttribute("demographicId");
		Path eFormPath = renderEFormWithAttachments(request, response);
		return eformDataManager.saveEFormWithAttachmentsAsEDoc(loggedInInfo, fdid, demographicId, eFormPath);
	}

	public String convertPDFToBase64(Path renderedDocument) throws PDFGenerationException {
		String base64 = "";
		if(renderedDocument == null) { return base64; }
		try {
			byte[] bytes = Files.readAllBytes(renderedDocument);
			base64 = Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			throw new PDFGenerationException("An error occurred while processing the PDF file", e);
		}
		return base64 != null ? base64 : "";
	}

	private Path renderDocument(LoggedInInfo loggedInInfo, HttpServletRequest request, HttpServletResponse response, DocumentType documentType, Integer documentId) throws PDFGenerationException {
		Path path = null;
		switch (documentType) {
			case DOC:
				path = documentManager.renderDocument(loggedInInfo, String.valueOf(documentId));
				break;
			case LAB:
				path = labManager.renderLab(loggedInInfo, documentId);
				break;
			case EFORM:
				path = eformDataManager.createEformPDF(loggedInInfo, documentId);
				break;
			case HRM:
				path = HRMUtil.renderHRM(loggedInInfo, documentId);
				break;
			case FORM:
				EctFormData.PatientForm patientForm = null;
				path = formsManager.renderForm(request, response, patientForm);
				break;
		}
		return path;
	}

	private void attachEFormPDFs(LoggedInInfo loggedInInfo, List<EFormData> attachedEForms, ArrayList<Object> pdfDocumentList) throws PDFGenerationException {
		for (EFormData eForm : attachedEForms) {
			Path path = renderDocument(loggedInInfo, DocumentType.EFORM, eForm.getId());
			pdfDocumentList.add(path.toString());
		}
	}

	private void attachEDocPDFs(LoggedInInfo loggedInInfo, List<EDoc> attachedEDocs, ArrayList<Object> pdfDocumentList) throws PDFGenerationException {
		for (EDoc eDoc : attachedEDocs) {
			Path path = documentManager.renderDocument(loggedInInfo, eDoc);
			pdfDocumentList.add(path.toString());
		}
	}

	private void attachLabPDFs(LoggedInInfo loggedInInfo, List<LabResultData> attachedLabs, ArrayList<Object> pdfDocumentList) throws PDFGenerationException {
		for (LabResultData lab : attachedLabs) {
			Path path = renderDocument(loggedInInfo, DocumentType.LAB, Integer.parseInt(lab.getSegmentID()));
			pdfDocumentList.add(path.toString());
		}
	}

	private void attachHRMPDFs(LoggedInInfo loggedInInfo, ArrayList<HashMap<String, ? extends Object>> attachedHRMs, ArrayList<Object> pdfDocumentList) throws PDFGenerationException {
		for (HashMap<String, ?> hrm : attachedHRMs) {
			Path path = renderDocument(loggedInInfo, DocumentType.HRM, (Integer) hrm.get("id"));
			pdfDocumentList.add(path.toString());
		}
	}

	private void attachFormPDFs(HttpServletRequest request, HttpServletResponse response, List<EctFormData.PatientForm> attachedForms, ArrayList<Object> pdfDocumentList) throws PDFGenerationException {
		for (EctFormData.PatientForm form : attachedForms) {
			Path path = formsManager.renderForm(request, response, form);
			pdfDocumentList.add(path.toString());
		}
	}

	private String getDisplayLabName(LabResultData labResultData) {
		String label = labResultData.getLabel() != null ? labResultData.getLabel() : "";
		String discipline = labResultData.getDiscipline() != null ? labResultData.getDiscipline() : "";
		String labTitle = !"".equals(label) ? label.substring(0, Math.min(label.length(), 40)) : discipline.substring(0, Math.min(discipline.length(), 40));
		return StringUtils.isNullOrEmpty(labTitle) ? "UNLABELLED" : labTitle;
	}

	public void flattenPDFFormFields(Path pdfPath) throws PDFGenerationException {
		try (PDDocument document = PDDocument.load(pdfPath.toFile())) {
			PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
			if (acroForm != null) { acroForm.flatten(); }
			document.save(pdfPath.toString());
		} catch (IOException e) {
			throw new PDFGenerationException("Error while flattening the " + pdfPath.getFileName() + " file. " + e.getMessage(), e);
		}
	}
}
