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

package ca.openosp.openo.fax.action;

import com.opensymphony.xwork2.ActionSupport;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import ca.openosp.openo.commn.model.FaxConfig;
import ca.openosp.openo.commn.model.FaxJob;
import ca.openosp.openo.commn.model.FaxJob.STATUS;
import ca.openosp.openo.documentManager.DocumentAttachmentManager;
import ca.openosp.openo.managers.FaxManager;
import ca.openosp.openo.managers.FaxManager.TransactionType;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.PDFGenerationException;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.openo.form.JSONUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import ca.openosp.OscarProperties;

public class Fax2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = MiscUtils.getLogger();
    private final FaxManager faxManager = SpringUtils.getBean(FaxManager.class);
    private final DocumentAttachmentManager documentAttachmentManager = SpringUtils.getBean(DocumentAttachmentManager.class);

    public String execute() {
        String method = request.getParameter("method");
        if ("queue".equals(method)) {
            return queue();
        } else if ("prepareFax".equals(method)) {
            return prepareFax();
        } else if ("getPreview".equals(method)) {
            getPreview();
            return null;
        } else if ("getPageCount".equals(method)) {
            getPageCount();
            return null;
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
                response.sendRedirect(request.getContextPath() + "/oscarEncounter/ViewRequest.do?de=" + demographicNo + "&requestId=" + transactionId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return NONE;
        } else if (TransactionType.EFORM.name().equalsIgnoreCase(transactionType)) {
            try {
                response.sendRedirect(request.getContextPath() + "/eform/efmshowform_data.jsp?fdid=" + transactionId + "&parentAjaxId=eforms");
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

        // Build the fax job parameters map
        HashMap<String, Object> faxJobMap = new HashMap<>();
        faxJobMap.put("faxFilePath", faxFilePath);
        faxJobMap.put("recipient", recipient);
        faxJobMap.put("recipientFaxNumber", recipientFaxNumber);
        faxJobMap.put("senderFaxNumber", senderFaxNumber);
        faxJobMap.put("demographicNo", demographicNo);
        faxJobMap.put("comments", comments);
        faxJobMap.put("coverpage", coverpage);
        faxJobMap.put("copyToRecipients", copyToRecipients);

        List<FaxJob> faxJobList = faxManager.createAndSaveFaxJob(loggedInInfo, faxJobMap);

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
                // The faxManager.getFaxPreviewImage method already handles path validation
                outfile = faxManager.getFaxPreviewImage(loggedInInfo, faxFilePath, page);
                if (outfile != null && outfile.getFileName() != null) {
                    response.setContentType("image/png");
                    String sanitizedFilename = FilenameUtils.getName(outfile.getFileName().toString());
                    // Encode filename to prevent HTTP response splitting by removing any control characters
                    String encodedFilename = URLEncoder.encode(sanitizedFilename, StandardCharsets.UTF_8)
                            .replaceAll("\\+", "%20"); // Replace + with %20 for spaces in filenames
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFilename + "\"");
                }
            } else {
                // Validate the PDF path to prevent path traversal attacks
                Path pdfPath = Path.of(faxFilePath);

                try {
                    // Get the canonical path to resolve any path traversal attempts
                    Path canonicalPdfPath = pdfPath.toRealPath();

                    // Define allowed base directories
                    String[] allowedBasePaths = {
                        OscarProperties.getInstance().getProperty("DOCUMENT_DIR", "/var/lib/OscarDocument/"),
                        OscarProperties.getInstance().getProperty("TMP_DIR", "/tmp/"),
                        System.getProperty("java.io.tmpdir")
                    };

                    boolean isValidPath = false;
                    for (String basePath : allowedBasePaths) {
                        if (basePath != null && !basePath.isEmpty()) {
                            Path basePathObj = Path.of(basePath);
                            if (Files.exists(basePathObj)) {
                                Path baseCanonicalPath = basePathObj.toRealPath();
                                if (canonicalPdfPath.startsWith(baseCanonicalPath)) {
                                    isValidPath = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (!isValidPath) {
                        logger.error("Access denied: Path traversal attempt detected for path: " + faxFilePath);
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                        return;
                    }

                    // Ensure the file exists and is a regular file
                    if (!Files.exists(canonicalPdfPath) || !Files.isRegularFile(canonicalPdfPath)) {
                        logger.error("PDF file not found or is not a regular file: " + faxFilePath);
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
                        return;
                    }

                    outfile = canonicalPdfPath;
                    response.setContentType("application/pdf");
                } catch (IOException e) {
                    logger.error("Error processing file path: " + faxFilePath, e);
                    try {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing file");
                    } catch (IOException ex) {
                        logger.error("Error sending error response", ex);
                    }
                    return;
                }
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
                logger.error("Error reading or writing file", e);
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

        ObjectNode jsonObject = objectMapper.createObjectNode();
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
    private String senderFaxNumber;
    private String comments;
    private String coverpage;
    private String[] copyToRecipients;

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

    public String getSenderFaxNumber() {
        return senderFaxNumber;
    }

    public void setSenderFaxNumber(String senderFaxNumber) {
        this.senderFaxNumber = senderFaxNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCoverpage() {
        return coverpage;
    }

    public void setCoverpage(String coverpage) {
        this.coverpage = coverpage;
    }

    public String[] getCopyToRecipients() {
        return copyToRecipients;
    }

    public void setCopyToRecipients(String[] copyToRecipients) {
        this.copyToRecipients = copyToRecipients;
    }
}
