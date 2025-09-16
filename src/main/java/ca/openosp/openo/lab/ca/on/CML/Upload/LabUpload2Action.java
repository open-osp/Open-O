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


package ca.openosp.openo.lab.ca.on.CML.Upload;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.utility.DbConnectionFilter;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.OscarProperties;
import ca.openosp.openo.lab.FileUploadCheck;
import ca.openosp.openo.lab.ca.on.CML.ABCDParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class LabUpload2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    Logger _logger = MiscUtils.getLogger();

    public String execute() {
        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "w", null)) {
            throw new SecurityException("missing required sec object (_lab)");
        }

        String filename = "";
        String proNo = (String) request.getSession().getAttribute("user");
        if (proNo == null) {
            proNo = "";
        }
        String key = request.getParameter("key");
        String keyToMatch = OscarProperties.getInstance().getProperty("CML_UPLOAD_KEY");
        MiscUtils.getLogger().debug("key=" + key);
        String outcome = "";

        //Checks to verify key is matched and file should be saved locally.
        if (key != null && keyToMatch != null && keyToMatch.equals(key)) {

            try {
                // Validate the uploaded file to prevent path traversal attacks
                if (importFile == null) {
                    outcome = "accessDenied";
                    request.setAttribute("outcome", outcome);
                    return SUCCESS;
                }
                
                // Get canonical path to resolve any relative path components and detect path traversal
                String canonicalPath = importFile.getCanonicalPath();
                
                // Verify the file is within the temp directory where Struts2 stores uploaded files
                String tempDir = System.getProperty("java.io.tmpdir");
                if (tempDir != null) {
                    File tempDirFile = new File(tempDir);
                    String tempDirCanonical = tempDirFile.getCanonicalPath();
                    
                    // Check if the file is within the temp directory
                    if (!canonicalPath.startsWith(tempDirCanonical + File.separator)) {
                        _logger.error("Attempted path traversal attack detected for file: " + canonicalPath);
                        outcome = "accessDenied";
                        request.setAttribute("outcome", outcome);
                        return SUCCESS;
                    }
                }

                MiscUtils.getLogger().debug("Lab Upload content type = " + importFile.getName());
                InputStream is = Files.newInputStream(importFile.toPath());
                
                // Sanitize the filename to prevent any path injection in the filename itself
                filename = sanitizeFileName(importFile.getName());

                String localFileName = saveFile(is, filename);
                is.close();


                boolean fileUploadedSuccessfully = false;
                if (localFileName != null) {
                    // Validate the localFileName path before using it
                    File localFile = new File(localFileName);
                    String localCanonicalPath = localFile.getCanonicalPath();
                    
                    // Verify the file exists and is within the document directory
                    OscarProperties props = OscarProperties.getInstance();
                    String documentDir = props.getProperty("DOCUMENT_DIR");
                    if (documentDir != null) {
                        File docDirFile = new File(documentDir);
                        String docDirCanonical = docDirFile.getCanonicalPath();
                        
                        if (!localCanonicalPath.startsWith(docDirCanonical + File.separator)) {
                            _logger.error("Attempted path traversal in localFileName: " + localCanonicalPath);
                            outcome = "accessDenied";
                            request.setAttribute("outcome", outcome);
                            return SUCCESS;
                        }
                    }
                    
                    InputStream fis = new FileInputStream(localFile);
                    int check = FileUploadCheck.UNSUCCESSFUL_SAVE;
                    try {
                        check = FileUploadCheck.addFile(filename, fis, proNo);
                        if (check != FileUploadCheck.UNSUCCESSFUL_SAVE) {
                            outcome = "uploadedPreviously";
                        }
                    } catch (Exception addFileEx) {
                        MiscUtils.getLogger().error("Error", addFileEx);
                        outcome = "databaseNotStarted";
                    }
                    MiscUtils.getLogger().debug("Was file uploaded successfully ?" + fileUploadedSuccessfully);
                    fis.close();
                    if (check != FileUploadCheck.UNSUCCESSFUL_SAVE) {
                        BufferedReader in = new BufferedReader(new FileReader(localFile));
                        ABCDParser abc = new ABCDParser();
                        abc.parse(in);

                        abc.save(DbConnectionFilter.getThreadLocalDbConnection());
                        outcome = "uploaded";
                    }
                } else {
                    outcome = "accessDenied";  //file could not save
                    MiscUtils.getLogger().debug("Could not save file :" + filename + " to disk");
                }

            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
                outcome = "exception";
            }

        } else {
            outcome = "accessDenied";
        }
        request.setAttribute("outcome", outcome);
        MiscUtils.getLogger().debug("forwarding outcome " + outcome);
        return SUCCESS;
    }


    public LabUpload2Action() {
    }


    /**
     * Save a Jakarta FormFile to a preconfigured place.
     *
     * @param stream
     * @param filename
     * @return String
     */
    private static String saveFile(InputStream stream, String filename) {
        String retVal = null;

        try {
            OscarProperties props = OscarProperties.getInstance();
            //properties must exist
            String place = props.getProperty("DOCUMENT_DIR");

            if (!place.endsWith("/"))
                place = new StringBuilder(place).insert(place.length(), "/").toString();
            
            // Validate the document directory path
            File docDir = new File(place);
            String docDirCanonical = docDir.getCanonicalPath();
            
            // The filename is already sanitized, but double-check it doesn't contain path separators
            if (filename.contains("/") || filename.contains("\\") || filename.contains("..")) {
                MiscUtils.getLogger().error("Invalid filename after sanitization: " + filename);
                return null;
            }
            
            // Construct the target file path safely
            String targetFileName = "LabUpload." + filename + "." + (new Date()).getTime();
            File targetFile = new File(docDir, targetFileName);
            String targetCanonicalPath = targetFile.getCanonicalPath();
            
            // Ensure the target file is within the document directory
            if (!targetCanonicalPath.startsWith(docDirCanonical + File.separator)) {
                MiscUtils.getLogger().error("Path traversal attempt in saveFile: " + targetCanonicalPath);
                return null;
            }
            
            retVal = targetCanonicalPath;
            MiscUtils.getLogger().debug(retVal);

            //write the  file to the file specified
            OutputStream bos = new FileOutputStream(targetFile);
            int bytesRead = 0;
            while ((bytesRead = stream.read()) != -1) {
                bos.write(bytesRead);
            }
            bos.close();

            //close the stream
            stream.close();
        } catch (FileNotFoundException fnfe) {

            MiscUtils.getLogger().debug("File not found");
            MiscUtils.getLogger().error("Error", fnfe);
            return null;

        } catch (IOException ioe) {
            MiscUtils.getLogger().error("Error", ioe);
            return null;
        }
        return retVal;
    }

    private File importFile;

    public File getImportFile() {
        return importFile;
    }

    public void setImportFile(File importFile) {
        this.importFile = importFile;
    }
    
    /**
     * Sanitize a filename to prevent path traversal attacks.
     * Removes any path components and dangerous characters.
     * 
     * @param fileName the filename to sanitize
     * @return sanitized filename safe for use
     */
    private String sanitizeFileName(String fileName) {
        if (fileName == null) {
            return "";
        }
        
        // First, get just the filename component (removes any path)
        Path path = Paths.get(fileName);
        String baseName = path.getFileName() != null ? path.getFileName().toString() : "";
        
        // Remove any remaining path traversal sequences or special characters
        // that could be used maliciously
        baseName = baseName.replaceAll("\\.\\.", "")  // Remove ..
                          .replaceAll("[\\\\/]", "")   // Remove any slashes
                          .replaceAll("\\$", "")        // Remove $
                          .replaceAll("~", "");         // Remove ~
        
        // Additional validation - ensure the filename is not empty after sanitization
        if (baseName.trim().isEmpty()) {
            _logger.warn("Filename became empty after sanitization: " + fileName);
            return "invalid_filename";
        }
        
        return baseName;
    }
}
