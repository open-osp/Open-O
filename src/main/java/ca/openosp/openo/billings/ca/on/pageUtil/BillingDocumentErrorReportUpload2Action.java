//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package ca.openosp.openo.billings.ca.on.pageUtil;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import ca.openosp.openo.commn.dao.BatchEligibilityDao;
import ca.openosp.openo.commn.dao.DemographicCustDao;
import ca.openosp.openo.commn.model.BatchEligibility;
import ca.openosp.openo.commn.model.Demographic;
import ca.openosp.openo.commn.model.DemographicCust;
import ca.openosp.openo.managers.DemographicManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.OscarProperties;
import ca.openosp.openo.billings.ca.on.bean.BillingClaimBatchAcknowledgementReportBeanHandler;
import ca.openosp.openo.billings.ca.on.bean.BillingClaimsErrorReportBeanHandler;
import ca.openosp.openo.billings.ca.on.bean.BillingEDTOBECOutputSpecificationBean;
import ca.openosp.openo.billings.ca.on.bean.BillingEDTOBECOutputSpecificationBeanHandler;
import ca.openosp.openo.billings.ca.on.data.BillingClaimsErrorReportBeanHandlerSave;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BillingDocumentErrorReportUpload2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private BatchEligibilityDao batchEligibilityDao = (BatchEligibilityDao) SpringUtils.getBean(BatchEligibilityDao.class);
    private DemographicCustDao demographicCustDao = (DemographicCustDao) SpringUtils.getBean(DemographicCustDao.class);
    private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);

    public String execute() throws ServletException, IOException {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String filename = request.getParameter("filename") == null ? "null" : request.getParameter("filename");

        if (filename == "null") {
            if (!saveFile(file1, file1FileName)) {
                addActionError(getText("errors.fileNotAdded"));

                response.sendRedirect(request.getContextPath() + "/billing/CA/ON/billingOBECEA.jsp");
                return NONE;
            } else {
                if (getData(loggedInInfo, file1FileName, "DOCUMENT_DIR", request)) {
                    String sanitized = sanitizeFileName(file1FileName);
                    return (sanitized.startsWith("L")) ? "outside" : SUCCESS;
                }
                else {
                    addActionError(getText("errors.incorrectFileFormat"));

                    response.sendRedirect(request.getContextPath() + "/billing/CA/ON/billingOBECEA.jsp");
                    return NONE;                
                }
            }
        } else {
            if (getData(loggedInInfo, filename, "ONEDT_INBOX", request)) {
                String sanitized = sanitizeFileName(filename);
                return (sanitized.startsWith("L")) ? "outside" : SUCCESS;
            } else if (getData(loggedInInfo, filename, "ONEDT_ARCHIVE", request)) {
                String sanitized = sanitizeFileName(filename);
                return (sanitized.startsWith("L")) ? "outside" : SUCCESS;
            } else {
                addActionError(getText("errors.incorrectFileFormat"));

                response.sendRedirect(request.getContextPath() + "/billing/CA/ON/billingOBECEA.jsp");
                return NONE;
            }
        }
    }

    /**
     * Save a Jakarta FormFile to a preconfigured place.
     *
     * @param file
     * @return boolean
     */
    public static boolean saveFile(File file, String fileName) {
        String retVal = null;
        boolean isAdded = true;

        try {
            // Sanitize the filename to prevent path traversal
            String sanitizedFileName = sanitizeFileName(fileName);
            if (sanitizedFileName == null || sanitizedFileName.isEmpty()) {
                MiscUtils.getLogger().error("Invalid filename provided: " + fileName);
                return false;
            }

            OscarProperties props = OscarProperties.getInstance();

            // Get target directory
            String place = props.getProperty("DOCUMENT_DIR");
            if (place == null || place.trim().isEmpty()) {
                MiscUtils.getLogger().error("DOCUMENT_DIR property is not configured");
                return false;
            }
            
            // Validate and canonicalize the base directory path first
            File placeDir;
            try {
                placeDir = new File(place).getCanonicalFile();
                // Ensure the directory exists and is actually a directory
                if (!placeDir.exists() || !placeDir.isDirectory()) {
                    MiscUtils.getLogger().error("DOCUMENT_DIR does not exist or is not a directory: " + place);
                    return false;
                }
            } catch (IOException e) {
                MiscUtils.getLogger().error("Invalid DOCUMENT_DIR path: " + place, e);
                return false;
            }
            
            // Now safely construct the destination file path
            File destFile = new File(placeDir, sanitizedFileName).getCanonicalFile();
            
            // Validate that the destination is within the allowed directory
            if (!destFile.getCanonicalPath().startsWith(placeDir.getCanonicalPath() + File.separator)) {
                MiscUtils.getLogger().error("Path does not start with document path: " + sanitizedFileName);
                return false;
            }

            retVal = destFile.getPath();
            MiscUtils.getLogger().debug(retVal);

            try (InputStream stream = new FileInputStream(file);
                 OutputStream bos = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[2048];
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
            }

            String inboxDir = props.getProperty("ONEDT_INBOX");
            if (inboxDir == null || inboxDir.trim().isEmpty()) {
                MiscUtils.getLogger().error("ONEDT_INBOX property is not configured");
                return false;
            }
            
            // Validate and canonicalize the inbox directory path
            File inboxDirFile;
            try {
                inboxDirFile = new File(inboxDir).getCanonicalFile();
                // Ensure the directory exists and is actually a directory
                if (!inboxDirFile.exists() || !inboxDirFile.isDirectory()) {
                    MiscUtils.getLogger().error("ONEDT_INBOX does not exist or is not a directory: " + inboxDir);
                    return false;
                }
            } catch (IOException e) {
                MiscUtils.getLogger().error("Invalid ONEDT_INBOX path: " + inboxDir, e);
                return false;
            }
            
            // Now safely construct the inbox file path
            File inboxFile = new File(inboxDirFile, sanitizedFileName).getCanonicalFile();
            
            // Validate that the inbox file is within the allowed directory
            if (!inboxFile.getCanonicalPath().startsWith(inboxDirFile.getCanonicalPath() + File.separator)) {
                MiscUtils.getLogger().error("Path does not start with inbox: " + sanitizedFileName);
                return false;
            }
            
            org.apache.commons.io.FileUtils.copyFile(destFile, inboxFile);
        } catch (FileNotFoundException e) {
            MiscUtils.getLogger().error("File not found", e);
            return isAdded = false;

        } catch (IOException ioe) {
            MiscUtils.getLogger().error("Error", ioe);
            return isAdded = false;
        }

        return isAdded;
    }


    /**
     * Get Data from the file.
     *
     * @param file
     * @return
     */
    private boolean getData(LoggedInInfo loggedInInfo, String fileName, String pathDir, HttpServletRequest request) 
            throws ServletException, IOException {
        boolean isGot = false;

        try {
            // Sanitize the filename to prevent path traversal
            String sanitizedFileName = sanitizeFileName(fileName);
            if (sanitizedFileName == null || sanitizedFileName.isEmpty()) {
                MiscUtils.getLogger().error("Invalid filename provided: " + fileName);
                return false;
            }

            OscarProperties props = OscarProperties.getInstance();
            String filepath = props.getProperty(pathDir);
            boolean bNewBilling = "true".equals(props.getProperty("isNewONbilling", ""));

            if (filepath == null || filepath.isBlank()) {
                throw new IllegalStateException("Missing or empty path: " + pathDir);
            }

            File safeDir = new File(filepath).getCanonicalFile();
            File inputFile = new File(safeDir, sanitizedFileName).getCanonicalFile();

            // Validate that the file is within the allowed directory
            if (!inputFile.getCanonicalPath().startsWith(safeDir.getCanonicalPath() + File.separator)) {
                MiscUtils.getLogger().error("File path is not in the correct directory: " + sanitizedFileName);
                throw new IllegalArgumentException("File path is outside allowed directory");
            }

            FileInputStream file = new FileInputStream(inputFile);
            MiscUtils.getLogger().debug("File path: " + inputFile.getAbsolutePath());
            // Assign associated report Name
            ArrayList<String> messages = new ArrayList<String>();
            String ReportName = "";

            if (sanitizedFileName.substring(0, 1).compareTo("E") == 0 || sanitizedFileName.substring(0, 1).compareTo("F") == 0) {
                ReportName = "Claims Error Report";
                BillingClaimsErrorReportBeanHandler hd = generateReportE(file, bNewBilling, sanitizedFileName);
                request.setAttribute("claimsErrors", hd);
                isGot = hd.verdict;
            } else if (sanitizedFileName.substring(0, 1).compareTo("B") == 0) {
                ReportName = "Claim Batch Acknowledgement Report";
                BillingClaimBatchAcknowledgementReportBeanHandler hd = generateReportB(file);
                request.setAttribute("batchAcks", hd);
                isGot = hd.verdict;
            } else if (sanitizedFileName.substring(0, 1).compareTo("X") == 0) {
                ReportName = "Claim File Rejection Report";
                messages = generateReportX(file);
                request.setAttribute("messages", messages);
                isGot = reportXIsGenerated;
            } else if (sanitizedFileName.substring(0, 1).compareTo("R") == 0) {
                ReportName = "EDT OBEC Output Specification";
                BillingEDTOBECOutputSpecificationBeanHandler hd = generateReportR(loggedInInfo, file);
                request.setAttribute("outputSpecs", hd);
                isGot = hd.verdict;
            } else if (sanitizedFileName.substring(0, 1).compareTo("L") == 0) {
                ReportName = "OUTSIDE USE REPORT";
                request.setAttribute("backupfilepath", filepath);
                request.setAttribute("filename", sanitizedFileName);
                isGot = true;
            }

            request.setAttribute("ReportName", ReportName);
        } catch (FileNotFoundException fnfe) {

            MiscUtils.getLogger().debug("File not found");
            MiscUtils.getLogger().error("Error", fnfe);
            return isGot = false;

        }

        return isGot;
    }

    /**
     * Generate Claims Error Report (E).
     *
     * @param file
     * @return BillingClaimsErrorReportBeanHandler
     */
    private BillingClaimsErrorReportBeanHandler generateReportE(FileInputStream file, boolean bB, String filename) {
        BillingClaimsErrorReportBeanHandler hd = null;
        if (bB) {
            hd = (new BillingClaimsErrorReportBeanHandlerSave(file, filename)).getErrorReportBeanObj(file);
        } else {
            hd = new BillingClaimsErrorReportBeanHandler(file);
        }

        return hd;
    }

    /**
     * Generate Claim Batch Acknowledgement Report (B).
     *
     * @param file
     * @return BillingClaimBatchAcknowlegementReportBeanHandler
     */
    private BillingClaimBatchAcknowledgementReportBeanHandler generateReportB(FileInputStream file) {
        BillingClaimBatchAcknowledgementReportBeanHandler hd = new BillingClaimBatchAcknowledgementReportBeanHandler(
                file);

        return hd;
    }

    /**
     * Generate Claim File Rejection Report (X).
     *
     * @param file
     * @return
     */
    private boolean reportXIsGenerated = true;

    private ArrayList<String> generateReportX(FileInputStream file) {
        ArrayList<String> messages = new ArrayList<String>();
        messages.add("M01 | Message Reason         Length     Msg Type   Filler  Record Image");
        messages.add("M02 | File:    File Name    Date:   Mail Date   Time: Mail Time     Process Date");
        InputStreamReader reader = new InputStreamReader(file);
        BufferedReader input = new BufferedReader(reader);
        String nextline;
        try {
            while ((nextline = input.readLine()) != null) {
                String headerCount = nextline.substring(2, 3);

                if (headerCount.compareTo("1") == 0) {
                    String recordLength = nextline.substring(23, 28);
                    String msgType = nextline.substring(28, 31);
                    String filler = nextline.substring(32, 39);
                    String error = nextline.substring(39, 76);
                    String explain = nextline.substring(3, 23);
                    String msg = "M01 | " + explain + "   " + recordLength + "   " + msgType + "   " + filler + "   "
                            + URLEncoder.encode(error, "UTF-8");
                    messages.add(msg);

                }
                if (headerCount.compareTo("2") == 0) {
                    String mailFile = nextline.substring(8, 20);
                    String mailDate = nextline.substring(25, 33);
                    String mailTime = nextline.substring(38, 44);
                    String batchProcessDate = nextline.substring(50, 58);
                    String msg = "M02 | File:   " + mailFile + "    " + "Date:   " + mailDate + "   " + "Time: "
                            + mailTime + "     PDate: " + batchProcessDate;
                    messages.add(msg);
                }
            }

        } catch (IOException ioe) {
            MiscUtils.getLogger().error("Error", ioe);
        } catch (StringIndexOutOfBoundsException ioe) {
            reportXIsGenerated = false;
        }
        return messages;
    }

    /**
     * Generate EDT OBEC Output Specification (R).
     *
     * @param file
     * @return BillingEDTOBECOutputSpecificationBeanHandler
     */
    @SuppressWarnings("unchecked")
    private BillingEDTOBECOutputSpecificationBeanHandler generateReportR(LoggedInInfo loggedInInfo, FileInputStream file) {
        BillingEDTOBECOutputSpecificationBeanHandler hd = new BillingEDTOBECOutputSpecificationBeanHandler(loggedInInfo, file);
        Vector<BillingEDTOBECOutputSpecificationBean> outputSpecVector = hd.getEDTOBECOutputSecifiationBeanVector();

        for (int i = 0; i < outputSpecVector.size(); i++) {
            BillingEDTOBECOutputSpecificationBean bean = outputSpecVector.elementAt(i);
            String hin = bean.getHealthNo();
            String responseCode = bean.getResponseCode();
            int responseCodeNum = -1;
            try {
                responseCodeNum = Integer.parseInt(responseCode);
            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            }

            if (responseCodeNum < 50 || responseCodeNum > 59) {

                BatchEligibility batchEligibility = batchEligibilityDao.find(Integer.parseInt(responseCode));

                List<Demographic> ds = demographicManager.searchByHealthCard(loggedInInfo, hin);

                if (!ds.isEmpty()) {
                    Demographic d = ds.get(0);
                    if (d.getVer().trim().compareTo(bean.getVersion().trim()) == 0) {
                        for (Demographic demographic : ds) {
                            demographic.setVer("##");
                            demographicManager.updateDemographic(loggedInInfo, demographic);
                        }
                        DemographicCust demographicCust = demographicCustDao.find(d.getDemographicNo());
                        if (demographicCust != null && batchEligibility != null) {
                            String newAlert = demographicCust.getAlert() + "\n" + "Invalid old version code: "
                                    + bean.getVersion() + "\nReason: " + batchEligibility.getMOHResponse() + "- "
                                    + batchEligibility.getReason() + "\nResponse Code: " + responseCode;
                            demographicCust.setAlert(newAlert);
                            demographicCustDao.merge(demographicCust);
                        }
                    }
                }
            }
        }

        return hd;
    }
    private File file1; // Uploaded file
    private String file1FileName; // Name of the uploaded file
    private String file1ContentType; // Content type of the uploaded file

    private String filename; // Filename parameter from request

    public File getFile1() {
        return file1;
    }

    public void setFile1(File file1) {
        this.file1 = file1;
    }

    public String getFile1FileName() {
        return file1FileName;
    }

    public void setFile1FileName(String file1FileName) {
        this.file1FileName = file1FileName;
    }

    public String getFile1ContentType() {
        return file1ContentType;
    }

    public void setFile1ContentType(String file1ContentType) {
        this.file1ContentType = file1ContentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    /**
     * Sanitizes a filename to prevent path traversal attacks.
     * Removes any path components and dangerous characters from the filename.
     * 
     * @param fileName the filename to sanitize
     * @return sanitized filename safe for filesystem operations, or null if invalid
     */
    private static String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }

        // First normalize Unicode to prevent homoglyph attacks
        // This converts characters to their canonical form
        String normalized = java.text.Normalizer.normalize(fileName, java.text.Normalizer.Form.NFKC);
    
        // URL decode to catch encoded traversal attempts like %2e%2e
        String decoded = normalized;
        try {
            // Decode multiple times to catch double-encoding
            for (int i = 0; i < 3; i++) {
                String temp = java.net.URLDecoder.decode(decoded, "UTF-8");
                if (temp.equals(decoded)) {
                    break; // No more encoding layers
                }
                decoded = temp;
            }
        } catch (Exception e) {
            // If decoding fails, reject the filename
            return null;
        }
    
        // First, extract just the filename (remove any path components)
        String baseName = new File(decoded).getName();
        
        // Remove dangerous characters and sequences in a single pass
        // Using character class for efficiency
            String sanitized = baseName
                // Remove path traversal sequences (including Unicode variations)
                .replaceAll("\\.\\.+[/\\\\]?", "")
                // Remove control characters (U+0000 to U+001F and U+007F to U+009F)
                .replaceAll("[\\p{Cntrl}]", "")
                // Remove format characters that could be used for tricks
                .replaceAll("[\\p{Cf}]", "")
                // Remove traditional dangerous file system characters
                .replaceAll("[\\\\/:*?\"<>|~$]", "")
                // Remove zero-width characters that could hide malicious content
                .replaceAll("[\\u200B-\\u200F\\u202A-\\u202E\\uFEFF]", "");

        // Trim the result
        sanitized = sanitized.trim();
        
        // Ensure we still have a valid filename after sanitization
        if (sanitized.trim().isEmpty()) {
            return null;
        }
        
        return sanitized;
    }
}
