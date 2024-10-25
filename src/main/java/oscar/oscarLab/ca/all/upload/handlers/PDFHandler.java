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

package oscar.oscarLab.ca.all.upload.handlers;

import com.itextpdf.text.pdf.PdfReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import org.oscarehr.documentManager.EDoc;
import org.oscarehr.documentManager.EDocUtil;
import oscar.log.LogAction;
import oscar.log.LogConst;

/**
 *
 * @author mweston4
 */
public class PDFHandler  implements MessageHandler{
    protected static Logger logger = org.oscarehr.util.MiscUtils.getLogger();
    
    @Override
    public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {

        String providerNo = "-1";
        String filePath = fileName;              
        if (!(fileName.endsWith(".pdf") || fileName.endsWith(".PDF"))) {
            logger.error("Document " + fileName + "does not have pdf extension");
            return null;
        }
        else {
            int fileNameIdx = fileName.lastIndexOf("/");
            fileName = fileName.substring(fileNameIdx+1);
        }
                
        EDoc newDoc = new EDoc("", "", fileName, "", providerNo, providerNo, "", 'A',
				oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", "-1", false);
                
        newDoc.setDocPublic("0");                       
        
        InputStream fis = null; 
                
        try {            
            fis = new FileInputStream(filePath);                                                   
            newDoc.setContentType("application/pdf");

            //Find the number of pages
            PdfReader reader = new PdfReader(filePath);          
            int numPages = reader.getNumberOfPages();
            reader.close();
            newDoc.setNumberOfPages(numPages);
            
            String doc_no = EDocUtil.addDocumentSQL(newDoc);

            LogAction.addLog(providerNo, LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, ipAddr,"","DocUpload");
            
            //Get provider to route document to
            String batchPDFProviderNo = OscarProperties.getInstance().getProperty("batch_pdf_provider_no");            
            if ((batchPDFProviderNo != null) && !batchPDFProviderNo.isEmpty()) {
                
                ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) SpringUtils.getBean(ProviderInboxRoutingDao.class);
                providerInboxRoutingDao.addToProviderInbox(batchPDFProviderNo, Integer.parseInt(doc_no), "DOC");
                
                //Add to default queue for now, not sure how or if any other queues can be used anyway (MAB)                 
                QueueDocumentLinkDao queueDocumentLinkDAO = (QueueDocumentLinkDao) SpringUtils.getBean(QueueDocumentLinkDao.class);          
                Integer did=Integer.parseInt(doc_no.trim());
                queueDocumentLinkDAO.addToQueueDocumentLink(1,did);  
            }else if(serviceName != null && serviceName.startsWith("providerNo")) {
            		String providerStr = serviceName.substring("providerNo".length());
            		String[] providers = providerStr.trim().split(" ");
            		ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) SpringUtils.getBean(ProviderInboxRoutingDao.class);
            		for(String provider: providers) {
                     providerInboxRoutingDao.addToProviderInbox(provider, Integer.parseInt(doc_no), "DOC");   
            		}
            }
        }
        catch (FileNotFoundException e) {
            logger.info("An unexpected error has occurred:" + e.toString(),e);
            return null;
        }
        catch (Exception e) {
                logger.info("An unexpected error has occurred:" + e.toString(),e);
                return null;
        } finally {
            try {
                if (fis != null) {
                        fis.close();
                }                
            } catch (IOException e1) {
                logger.info("An unexpected error has occurred:" + e1.toString(),e1);
                return null;
            }
        }			      	              
		
        return "success";
    }
}
