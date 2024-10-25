/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2015-2019. The Pharmacists Clinic, Faculty of Pharmaceutical Sciences, University of British Columbia. All Rights Reserved.
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
 * The Pharmacists Clinic
 * Faculty of Pharmaceutical Sciences
 * University of British Columbia
 * Vancouver, British Columbia, Canada
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.managers;

 import java.io.ByteArrayInputStream;
 import java.io.File;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.nio.file.Files;
 import java.nio.file.Path;
 import java.nio.file.Paths;
 import java.nio.file.StandardCopyOption;
 import java.util.*;
 
 import org.apache.logging.log4j.Logger;
 import org.apache.struts.action.DynaActionForm;
 import org.oscarehr.common.dao.ClinicDAO;
 import org.oscarehr.common.dao.FaxClientLogDao;
 import org.oscarehr.common.dao.FaxConfigDao;
 import org.oscarehr.common.dao.FaxJobDao;
 import org.oscarehr.common.model.Clinic;
 import org.oscarehr.common.model.FaxClientLog;
 import org.oscarehr.common.model.FaxConfig;
 import org.oscarehr.common.model.FaxJob;
 import org.oscarehr.common.model.FaxJob.STATUS;
 import org.oscarehr.fax.core.FaxAccount;
 import org.oscarehr.fax.core.FaxRecipient;
 import org.oscarehr.util.LoggedInInfo;
 import org.oscarehr.util.MiscUtils;
 import org.oscarehr.util.SpringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 import net.sf.json.JSONObject;
 import oscar.OscarProperties;
 import org.oscarehr.documentManager.EDocUtil;
 import oscar.form.util.FormTransportContainer;
 import oscar.log.LogAction;
 import oscar.util.ConcatPDF;

 public interface FaxManager{

      enum TransactionType {CONSULTATION, EFORM, FORM, RX, DOCUMENT}

      Path renderFaxDocument(LoggedInInfo loggedInInfo, TransactionType transactionType, FormTransportContainer formTransportContainer);

      Path renderFaxDocument(LoggedInInfo loggedInInfo, TransactionType transactionType, int transactionId, int demographicNo);

     /**
      * @Deprecated
      * Move these rendering methods into a more generic class like the DocumentManager
      *
      * @return
      */
      Path renderFaxDocument(LoggedInInfo loggedInInfo, TransactionType transactionType, int transactionId, int demographicNo, FormTransportContainer formTransportContainer);

      Path renderConsultationRequest(LoggedInInfo loggedInInfo, int requestId, int demographicNo);

      Path renderDocument(LoggedInInfo loggedInInfo, int documentNo, int demographicNo);

      Path renderEform(LoggedInInfo loggedInInfo, int eformId, int demographicNo);
      Path renderPrescription(LoggedInInfo loggedInInfo, int rxId, int demographicNo);

      Path renderForm(LoggedInInfo loggedInInfo, int formId, int demographicNo);

      Path renderForm(LoggedInInfo loggedInInfo, FormTransportContainer formTransportContainer);

     /**
      * Calls the save method after the faxJob(s) are created.
      * The FaxJob list that is returned contains persisted FaxJob Objects
      * This method has a specific purpose for the FaxAction class.  Use the
      * createFaxJob(LoggedInInfo loggedInInfo, Map<String, Object> faxJobMap) signature otherwise.
      *
      */
      List<FaxJob> createAndSaveFaxJob(LoggedInInfo loggedInInfo, DynaActionForm faxActionForm);

     /**
      * 1.) Creates the faxJob
      * 2.) duplicates the faxJob for each recipient
      * 3.) saves all the faxJobs to be sent.
      * Map should contain values for:
      * faxFilePath
      * recipient
      * recipientFaxNumber
      * comments (for cover page)
      * isCoverpage
      * senderFaxNumber
      * demographicNo
      * copytoRecipients (as String[])
      *
      * The FaxJob list that is returned contains persisted FaxJob Objects
      *
      */
      List<FaxJob> createAndSaveFaxJob(LoggedInInfo loggedInInfo, Map<String, Object> faxJobMap);

     /**
      * The beginning of a new Fax job from the parameters in the given Map.
      * Map should contain values for:
      * faxFilePath
      * recipient
      * recipientFaxNumber
      * comments (for cover page)
      * isCoverpage
      * senderFaxNumber
      * demographicNo
      * copytoRecipients (as String[])
      * The FaxJob returned is NEW UN-PERSISTED FaxJob Object with a single recipient
      *
      */
      FaxJob createFaxJob(LoggedInInfo loggedInInfo, Map<String, Object> faxJobMap);

     /**
      * Add recipients from an indexed array of JSON formatted strings
      * name:<recipient>
      * fax:<recipient fax number>
      *
      */
      List<FaxJob> addRecipients(LoggedInInfo loggedInInfo, FaxJob faxJob, String[] faxRecipients);

     /**
      * Create 1 faxJob for each fax recipient. Sets each faxJob to the
      * default status of WAITNG.
      *
      */
      List<FaxJob> addRecipients(LoggedInInfo loggedInInfo, FaxJob faxJob, List<FaxRecipient> faxRecipients);

     /**
      * Persist to the database for transmission later if the fax account is valid.
      *
      * The given faxjob must contain a valid sender fax number and username.
      */
      List<FaxJob> saveFaxJob(LoggedInInfo loggedInInfo, List<FaxJob> faxJobList);

     /**
      * Create new or update fax job.
      */
      FaxJob saveFaxJob(LoggedInInfo loggedInInfo, FaxJob faxJob);

     /**
      * prepend a fax cover page to the given existing PDF document.
      *
      */
      Path addCoverPage(LoggedInInfo loggedInInfo, String note, Path currentDocument) throws IOException;

      Path addCoverPage(LoggedInInfo loggedInInfo, String note, FaxRecipient recipient, FaxAccount sender, Path currentDocument) throws IOException;

     /**
      * Overload
      * Get preview image by specific page number.
      */
      Path getFaxPreviewImage(LoggedInInfo loggedInInfo, String filePath, int pageNumber);

     /**
      * Overload
      * Get a preview image of the documents being faxed.  Default is
      * the first page only
      */
      Path getFaxPreviewImage(LoggedInInfo loggedInInfo, String filePath);

     /**
      * Get a preview image of the documents being faxed.  Default is
      * the first page only
      *
      */
      Path getFaxPreviewImage(LoggedInInfo loggedInInfo, Path filePath);
     /**
      * Get preview image by specific page number.
      */
      Path getFaxPreviewImage(LoggedInInfo loggedInInfo, Path filePath, int pageNumber);

     /**
      * Sets both the global user log and the fax job log.
      *
      */
      void logFaxJob(LoggedInInfo loggedInInfo, FaxJob faxJob, TransactionType transactionType, int transactionId);
     /**
      * Update the transaction logs with a new status.
      */
      void updateFaxLog(LoggedInInfo loggedInInfo, FaxJob faxJob);
     /**
      * Returns all the active sender accounts in this system.
      */
      List<FaxConfig> getFaxGatewayAccounts(LoggedInInfo loggedInInfo);

      List<FaxConfig> getFaxConfigurationAccounts(LoggedInInfo loggedInInfo);
     /**
      * Get all fax jobs with a waiting to be sent status by
      * sender fax number.
      */
      List<FaxJob> getOutGoingFaxes(LoggedInInfo loggedInInfo, String senderFaxNumber);
     /**
     * Clear the preview cache and temp directory.
      *
      */
      boolean flush(LoggedInInfo loggedInInfo, String filePath);
     /**
      * Check if fax services are enabled.
      */

      FaxJob getFaxJob(LoggedInInfo loggedInInfo, int jobId);
     /**
      * Returns the actual page count of this PDF document instead of
      * depending on the value that is placed in the database table.
      * Important for when faxes are merged or when a cover page is added.
      */
      int getPageCount(LoggedInInfo loggedInInfo, int jobId);

     /**
      * Faxes can be resent by the user if the fax contains a status of
      * ERROR or COMPLETE.  The fax status of the original fax will be changed to
      * RESENT and cannot be resent again.
      */
      boolean resendFax(LoggedInInfo loggedInInfo, String jobId, String destination);

     public void restartFaxScheduler(LoggedInInfo loggedInInfo);

     public JSONObject getFaxSchedularStatus(LoggedInInfo loggedInInfo);
     /**
      * Check if fax services are enabled.
      */
     public static boolean isEnabled() {

         FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
         List<FaxConfig> accounts = faxConfigDao.findAll(0,null);
         for(FaxConfig account : accounts)
         {
             if(account.isActive())
             {
                 return Boolean.TRUE;
             }

         }
         return Boolean.FALSE;
     }
 }
