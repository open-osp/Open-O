package oscar.oscarEncounter.oceanEReferal.pageUtil;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.EReferAttachmentDao;
import org.oscarehr.common.model.EReferAttachment;
import org.oscarehr.common.model.EReferAttachmentData;
import org.oscarehr.common.model.enumerator.DocumentType;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EReferAction extends DispatchAction {
	private static final Logger logger = MiscUtils.getLogger();
	private final DocumentAttachmentManager documentAttachmentManager = SpringUtils.getBean(DocumentAttachmentManager.class);

	// Documents (attachments) originate from the consult request window.
	// Users can attach these documents using the attachment GUI on the consult request form.
	// All documents are internal to Oscar.
	public void attachOceanEReferralConsult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = StringUtils.isNullOrEmpty(request.getParameter("demographicNo")) ? "" : request.getParameter("demographicNo");
		String documents = StringUtils.isNullOrEmpty(request.getParameter("documents")) ? "" : request.getParameter("documents");
		if (documents.isEmpty() || demographicNo.isEmpty()) { return; }

		EReferAttachment eReferAttachment = new EReferAttachment(Integer.parseInt(demographicNo));
		List<EReferAttachmentData> attachments = new ArrayList<>();
		
		for (String document : documents.split("\\|")) {
			String type = document.replaceAll("\\d", "");
			Integer id = Integer.parseInt(document.substring(type.length()));
			EReferAttachmentData attachmentData = new EReferAttachmentData(eReferAttachment, id, type);
			attachments.add(attachmentData);
		}
		eReferAttachment.setAttachments(attachments);
		EReferAttachmentDao eReferAttachmentDao = SpringUtils.getBean(EReferAttachmentDao.class);
		eReferAttachmentDao.persist(eReferAttachment);

		try (PrintWriter writer = response.getWriter()) {
			writer.write(eReferAttachment.getId().toString());
		} catch (IOException e) {
			logger.error("Failed to write the eReferAttachment ID to the response", e);
		}
	}

	// Documents (attachments) originate from the consult request window.
	// Users can attach these documents using the attachment GUI on the consult request form.
	// All documents are internal to Oscar.
	public void editOceanEReferralConsult(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo = loggedInInfo.getLoggedInProviderNo();
		String demographicNo = StringUtils.isNullOrEmpty(request.getParameter("demographicNo")) ? "" : request.getParameter("demographicNo");
		String requestId = StringUtils.isNullOrEmpty(request.getParameter("requestId")) ? "" : request.getParameter("requestId");
		String documents = StringUtils.isNullOrEmpty(request.getParameter("documents")) ? "" : request.getParameter("documents");
		if (documents.isEmpty() || demographicNo.isEmpty() || requestId.isEmpty()) { return; }

		List<String> docs = new ArrayList<>();
		List<String> labs = new ArrayList<>();
		List<String> eforms = new ArrayList<>();
		List<String> hrms = new ArrayList<>();
		
		for (String document : documents.split("\\|")) {
			String type = document.replaceAll("\\d", "");
			switch(type) {
				case "D": docs.add(document.substring(type.length())); break;
				case "L": labs.add(document.substring(type.length())); break;
				case "E": eforms.add(document.substring(type.length())); break;
				case "H": hrms.add(document.substring(type.length())); break;
			}
		}

		documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.DOC, docs.toArray(new String[0]), providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo), Boolean.TRUE);
		documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.LAB, labs.toArray(new String[0]), providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo), Boolean.TRUE);
		documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.EFORM, eforms.toArray(new String[0]), providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo), Boolean.TRUE);
		documentAttachmentManager.attachToConsult(loggedInInfo, DocumentType.HRM, hrms.toArray(new String[0]), providerNo, Integer.parseInt(requestId), Integer.parseInt(demographicNo), Boolean.TRUE);
	}
}
