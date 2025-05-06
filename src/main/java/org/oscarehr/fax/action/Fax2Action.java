//CHECKSTYLE:OFF
/**
 * Copyright (c) 2015-2019. The Pharmacists Clinic, Faculty of Pharmaceutical Sciences, University of British Columbia. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for the
 * The Pharmacists Clinic
 * Faculty of Pharmaceutical Sciences
 * University of British Columbia
 * Vancouver, British Columbia, Canada
 */

package org.oscarehr.fax.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.FaxJob;
import org.oscarehr.common.model.FaxJob.STATUS;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.managers.FaxManager;
import org.oscarehr.managers.FaxManager.TransactionType;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.oscarehr.util.SpringUtils;
import oscar.form.JSONUtil;

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

public class Fax2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private static final Logger logger = MiscUtils.getLogger();
    private final FaxManager faxManager = SpringUtils.getBean(FaxManager.class);
    private final DocumentAttachmentManager documentAttachmentManager = SpringUtils.getBean(DocumentAttachmentManager.class);

    public String execute() {
        String method = request.getParameter("method");
        if ("queue".equals(method)) {
            return queue();
        } else if ("prepareFax".equals(method)) {
            return prepareFax();
        }
        return cancel();
    }

    @SuppressWarnings("unused")
    public String cancel() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String faxForward = transactionType;

        faxManager.flush(loggedInInfo, faxFilePath);




        if (TransactionType.CONSULTATION.name().equalsIgnoreCase(transactionType)) {
            try {
                response.sendRedirect("/oscarEncounter/ViewRequest.do?de=" + demographicNo + "&requestId=" + transactionId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return NONE;
        } else if (TransactionType.EFORM.name().equalsIgnoreCase(transactionType)) {
            try {
                response.sendRedirect("/eform/efmshowform_data.jsp?fdid=" + transactionId + "&parentAjaxId=eforms");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return NONE;
        }

        return faxForward;
    }

    /**
     * Set up fax parameters for this fax to be sent with the next timed
     * batch process.
     * This action assumes that the fax has already been produced and reviewed
     * by the user.
     */
    @SuppressWarnings("unused")
    public String queue() {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        TransactionType transactionType = TransactionType.valueOf(getTransactionType().toUpperCase());
        List<FaxJob> faxJobList = faxManager.createAndSaveFaxJob(loggedInInfo, new HashMap<>());

        boolean success = true;
        for (FaxJob faxJob : faxJobList) {
            faxManager.logFaxJob(loggedInInfo, faxJob, transactionType, transactionId);

            /*
             * only one error will derail the entire fax job.
             */
            if (STATUS.ERROR.equals(faxJob.getStatus())) {
                success = false;
            }
        }

        request.setAttribute("faxSuccessful", success);
        request.setAttribute("faxJobList", faxJobList);

        return "preview";
    }


    /**
     * Get a preview image of the entire fax document.
     */
    @SuppressWarnings("unused")
    public void getPreview() {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String faxFilePath = request.getParameter("faxFilePath");
        String pageNumber = request.getParameter("pageNumber");
        String showAs = request.getParameter("showAs");
        Path outfile = null;
        int page = 1;
        String jobId = request.getParameter("jobId");
        FaxJob faxJob = null;

        if (jobId != null && !jobId.isEmpty()) {
            faxJob = faxManager.getFaxJob(loggedInInfo, Integer.parseInt(jobId));
        }

        if (faxJob != null) {
            faxFilePath = faxJob.getFile_name();
        }

        if (pageNumber != null && !pageNumber.isEmpty()) {
            page = Integer.parseInt(pageNumber);
        }

        /*
         * Displaying the entire PDF using the default browser's view before faxing an EForm (in CoverPage.jsp),
         * and when viewing it in the fax records (Manage Faxes), it is shown as images.
         */
        if (faxFilePath != null && !faxFilePath.isEmpty()) {
            if (showAs != null && showAs.equals("image")) {
                outfile = faxManager.getFaxPreviewImage(loggedInInfo, faxFilePath, page);
                response.setContentType("image/pnsg");
                response.setHeader("Content-Disposition", "attachment;filename=" + outfile.getFileName().toString());
            } else {
                outfile = Paths.get(faxFilePath);
                response.setContentType("application/pdf");
            }
        }

        if (outfile != null) {
            try (InputStream inputStream = Files.newInputStream(outfile);
                 BufferedInputStream bfis = new BufferedInputStream(inputStream);
                 ServletOutputStream outs = response.getOutputStream()) {

                int data;
                while ((data = bfis.read()) != -1) {
                    outs.write(data);
                }
                outs.flush();
            } catch (IOException e) {
                //log.error("Error", e);
            }
        }
    }

    /**
     * Prepare a PDF of the given parameters an then return a path to
     * the for the user to review and add a cover page before sending final.
     */
    @SuppressWarnings("unused")
    public String prepareFax() {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        /*
         * Fax recipient info carried forward.
         */
        TransactionType transactionType = TransactionType.valueOf(getTransactionType().toUpperCase());
        String actionForward = ERROR;
        Path pdfPath = null;
        List<FaxConfig> accounts = faxManager.getFaxGatewayAccounts(loggedInInfo);

        /*
         * No fax accounts - No Fax.
         * This document is saved in a temporary directory as a PDF.
         */
        if (!accounts.isEmpty()) {
            if (transactionType.equals(TransactionType.EFORM)) {
                request.setAttribute("fdid", String.valueOf(transactionId));
                request.setAttribute("demographicId", String.valueOf(demographicNo));

                try {
                    pdfPath = documentAttachmentManager.renderEFormWithAttachments(request, response);
                } catch (PDFGenerationException e) {
                    logger.error(e.getMessage(), e);
                    String errorMessage = "This eForm (and attachments, if applicable) cannot be faxed. \\n\\n" + e.getMessage();
                    request.setAttribute("errorMessage", errorMessage);
                    return "eFormError";
                }
            }
        } else {
            request.setAttribute("message", "No active fax accounts found.");
        }

        if (pdfPath != null) {
            List<Path> documents = new ArrayList<>();
            documents.add(pdfPath);
            request.setAttribute("accounts", accounts);
            request.setAttribute("demographicNo", demographicNo);
            request.setAttribute("documents", documents);
            request.setAttribute("transactionType", transactionType.name());
            request.setAttribute("transactionId", transactionId);
            request.setAttribute("faxFilePath", pdfPath);
            request.setAttribute("letterheadFax", letterheadFax);
            request.setAttribute("professionalSpecialistName", recipient);
            request.setAttribute("fax", recipientFaxNumber);
            actionForward = "preview";
        }

        return actionForward;
    }

    /**
     * Get the actual number of pages in this PDF document.
     */
    @SuppressWarnings("unused")
    public void getPageCount() {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String jobId = request.getParameter("jobId");
        int pageCount = 0;

        if (jobId != null && !jobId.isEmpty()) {
            pageCount = faxManager.getPageCount(loggedInInfo, Integer.parseInt(jobId));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("jobId", jobId);
        jsonObject.put("pageCount", pageCount);

        JSONUtil.jsonResponse(response, jsonObject);
    }

    private String faxFilePath;
    private Integer transactionId;
    private Integer demographicNo;
    private String transactionType;
    private String recipient;
    private String recipientFaxNumber;
    private String letterheadFax;

    public String getFaxFilePath() {
        return faxFilePath;
    }

    public void setFaxFilePath(String faxFilePath) {
        this.faxFilePath = faxFilePath;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(Integer demographicNo) {
        this.demographicNo = demographicNo;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientFaxNumber() {
        return recipientFaxNumber;
    }

    public void setRecipientFaxNumber(String recipientFaxNumber) {
        this.recipientFaxNumber = recipientFaxNumber;
    }

    public String getLetterheadFax() {
        return letterheadFax;
    }

    public void setLetterheadFax(String letterheadFax) {
        this.letterheadFax = letterheadFax;
    }
}
