package org.oscarehr.documentManager;

import org.oscarehr.common.dao.ConsultDocsDao;
import org.oscarehr.common.dao.EFormDocsDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.common.model.EFormDocs;
import org.oscarehr.common.model.enumerator.DocumentType;
import org.oscarehr.util.SpringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocumentAttach {
	private final ConsultDocsDao consultDocsDao = SpringUtils.getBean(ConsultDocsDao.class);
	private final EFormDocsDao eFormDocsDao = SpringUtils.getBean(EFormDocsDao.class);

	public DocumentAttach() {
	}

	public void attachToConsult(String[] attachments, DocumentType documentType, String providerNo, Integer requestId) {
		List<String> currentList = new ArrayList<>(Arrays.asList(attachments));
		List<ConsultDocs> consultDocsList = consultDocsDao.findByRequestIdDocType(requestId, documentType.getType());
		List<String> oldList = new ArrayList<>();
		for (ConsultDocs consultDoc : consultDocsList) {
			oldList.add(Integer.toString(consultDoc.getDocumentNo()));
		}
		detachFromConsult(currentList, oldList, documentType, requestId);
		attachToConsult(currentList, oldList, documentType, providerNo, requestId);
	}

	private void attachToConsult(List<String> currentList, List<String> oldList, DocumentType documentType, String providerNo, Integer requestId) {
		for (String docId : currentList) {
			if (oldList.contains(docId)) { continue; }
			ConsultDocs consultDoc = new ConsultDocs(requestId, Integer.parseInt(docId), documentType.getType(), providerNo);
			consultDocsDao.persist(consultDoc);
		}
	}

	private void detachFromConsult(List<String> currentList, List<String> oldList, DocumentType documentType, Integer requestId) {
		for (String docId : oldList) {
			if (currentList.contains(docId)) { continue; }
			List<ConsultDocs> detachList = consultDocsDao.findByRequestIdDocNoDocType(requestId, Integer.valueOf(docId), documentType.getType());
			for (ConsultDocs consultDoc : detachList) {
				consultDoc.setDeleted("Y");
				consultDocsDao.merge(consultDoc);
			}
		}
	}

	public void attachToEForm(String[] attachments, DocumentType documentType, String providerNo, Integer fdid) {
		List<String> currentList = new ArrayList<>(Arrays.asList(attachments));
		List<EFormDocs> eFormDocsList = eFormDocsDao.findByFdidIdDocType(fdid, documentType.getType());
		List<String> oldList = new ArrayList<>();
		for (EFormDocs eFormDoc : eFormDocsList) {
			oldList.add(Integer.toString(eFormDoc.getDocumentNo()));
		}
		detachFromEForm(currentList, oldList, documentType, fdid);
		attachToEForm(currentList, oldList, documentType, providerNo, fdid);
	}

	private void attachToEForm(List<String> currentList, List<String> oldList, DocumentType documentType, String providerNo, Integer fdid) {
		for (String docId : currentList) {
			if (oldList.contains(docId)) { continue; }
			EFormDocs eFormDocs = new EFormDocs(fdid, Integer.parseInt(docId), documentType.getType(), providerNo);
			eFormDocsDao.persist(eFormDocs);
		}
	}

	private void detachFromEForm(List<String> currentList, List<String> oldList, DocumentType documentType, Integer fdid) {
		for (String docId : oldList) {
			if (currentList.contains(docId)) { continue; }
			List<EFormDocs> detachList = eFormDocsDao.findByFdidIdDocNoDocType(fdid, Integer.valueOf(docId), documentType.getType());
			for (EFormDocs eFormDoc : detachList) {
				eFormDoc.setDeleted("Y");
				eFormDocsDao.merge(eFormDoc);
			}
		}
	}
}
