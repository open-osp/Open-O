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
 import java.io.File;
 import java.io.IOException;
 import java.nio.file.*;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.List;
 
 import com.itextpdf.text.DocumentException;
 import org.apache.commons.io.FileUtils;
 import org.apache.commons.lang.StringUtils;
 import org.apache.logging.log4j.Logger;
 import org.apache.pdfbox.pdmodel.PDDocument;
 import org.oscarehr.common.dao.*;
 import org.oscarehr.common.model.*;
 
 import org.oscarehr.util.LoggedInInfo;
 import org.oscarehr.util.MiscUtils;
 import org.oscarehr.util.PDFGenerationException;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 import oscar.OscarProperties;
 import org.oscarehr.documentManager.EDoc;
 
 import org.oscarehr.documentManager.EDocUtil;
 import oscar.log.LogAction;
 import oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ImagePDFCreator;
 
 public interface DocumentManager{

     public Document getDocument(LoggedInInfo loggedInInfo, Integer id);
 
     public List<Document> getDocumentsByDemographicNo(LoggedInInfo loggedInInfo, Integer demographicNo);
 
     public CtlDocument getCtlDocumentByDocumentId(LoggedInInfo loggedInInfo, Integer documentId);
 
     /**
      * Creates a document and saves it to the provided demographic
      *
      * @param loggedInInfo  The logged in info of the current user
      * @param document      Document to create
      * @param demographicNo The demographic number to save the document to
      * @param providerNo    The optional provider number to route the document to
      * @param documentData  The document byte data
      * @return Document record from the database once it has been created
      * @throws IOException If actions related to getting document data fail
      */
     public Document createDocument(LoggedInInfo loggedInInfo, Document document, Integer demographicNo, String providerNo, byte[] documentData) throws IOException;
     public List<Document> getDocumentsUpdateAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn);
 
     public List<Document> getDocumentsByDemographicIdUpdateAfterDate(LoggedInInfo loggedInInfo, Integer demographicId, Date updatedAfterThisDateExclusive);
 
     public List<Document> getDocumentsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn);
 
     public Integer saveDocument(LoggedInInfo loggedInInfo, EDoc edoc);
 
 
     public Integer saveDocument(LoggedInInfo loggedInInfo, Document document, CtlDocument ctlDocument);
 
     public void moveDocumentToOscarDocuments(LoggedInInfo loggedInInfo, Document document, String fromPath);
 
     public void moveDocument(LoggedInInfo loggedInInfo, Document document, String fromPath, String toPath);

 
     public String getPathToDocument(LoggedInInfo loggedInInfo, int documentId);
 
     public String getFullPathToDocument(String filename);
     /**
      * Fetch by demographic number and given document type
      * ie: get only LAB documents for the given demographic number.
      */
     public List<Document> getDemographicDocumentsByDocumentType(LoggedInInfo loggedInInfo, int demographicNo, DocumentDao.DocumentType documentType);
     public Document getDocumentByDemographicAndFilename(LoggedInInfo loggedInInfo, int demographicNo, String fileName);
 
     /**
      * Add a document to Oscar's document library.
      * <p>
      * This method actually saves the Document contents to the file system. The document resource
      * MUST contain valid Base64 encoded document binary data.
      *
      * @param loggedInInfo
      * @param document
      * @return
      * @throws Exception
      */
     public Document addDocument(LoggedInInfo loggedInInfo, Document document, CtlDocument ctlDocument) throws Exception;
 
     public List<String> getProvidersThatHaveAcknowledgedDocument(LoggedInInfo loggedInInfo, Integer documentId);
 
     public Path renderDocument(LoggedInInfo loggedInInfo, EDoc eDoc) throws PDFGenerationException;
 
     public Path renderDocument(LoggedInInfo loggedInInfo, String documentId) throws PDFGenerationException;

     public Integer addDocumentToQueue(LoggedInInfo loggedInInfo, Integer documentId, Integer queueId);
 }