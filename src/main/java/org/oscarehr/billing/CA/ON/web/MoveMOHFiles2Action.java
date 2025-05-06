package org.oscarehr.billing.CA.ON.web;

import com.opensymphony.xwork2.ActionSupport;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.oscarehr.billing.CA.ON.util.EDTFolder;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;


public class MoveMOHFiles2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest(); 
    HttpServletResponse response = ServletActionContext.getResponse();

    private static Logger logger = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public String execute() throws Exception {
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
            throw new SecurityException("missing required security object (_admin)");
        }

        StringBuilder messages = new StringBuilder();
        StringBuilder errors = new StringBuilder();

        boolean isValid = true;
        String folderParam = request.getParameter("folder");
        if (folderParam == null || folderParam.isEmpty()) {
            errors.append("A folder must be selected.<br/>");
            isValid = false;
            // return "Unable to get folderParam";
        }

        String[] fileNames = request.getParameterValues("mohFile");
        if (fileNames == null) {
            errors.append("Please select file(s) to archive.<br/>");
            isValid = false;
            // return "Unable to get file names";
        }

        if (isValid) {
            String folderPath = getFolderPath(folderParam);
            for (String fileName : fileNames) {
                File file = getFile(folderPath, fileName);
                boolean isValidFileLocation = validateFileLocation(file);
                if (!isValidFileLocation) {
                    logger.warn("Invalid file location " + fileName);
                    continue;
                }

                if (file == null) {
                    logger.warn("Unable to get file " + folderPath + File.pathSeparator + fileName);

                    errors.append("Unable to find file " + fileName + ".<br/>");
                    continue;
                }

                if (file.exists()) {
                    boolean isMoved = moveFile(file);
                    if (isMoved) {
                        messages.append("Archived file " + file.getName() + " successfully.<br/>");
                    } else {
                        errors.append("Unable to archive " + file.getName());
                    }
                }
            }
        }
        
        WebUtils.addErrorMessage(request.getSession(), errors.toString());
        WebUtils.addInfoMessage(request.getSession(), messages.toString());

        return "Success";
    }

    private boolean validateFileLocation(File file) {
    boolean result = false;
    try {
        String filePath = file.getCanonicalPath();
        for(EDTFolder folder : EDTFolder.values()) {
            String edtFolderPath = new File(folder.getPath()).getCanonicalPath();
            if (filePath.startsWith(edtFolderPath)) {
                result = true;
            }
        }
    } catch (Exception e) {
        logger.error("Unable to validate file location", e);
    }
    return result;
    }

    private File getFile(String folderPath, String fileName) {
    try {
        fileName = URLDecoder.decode(fileName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
        logger.error("Unable to decode " + fileName, e);
        return null;
    }

    return new File(folderPath, fileName);
    }

    private boolean moveFile(File file) {
    File archiveDir = new File(EDTFolder.ARCHIVE.getPath());
    try {
        FileUtils.moveToDirectory(file, archiveDir, true);
    } catch (IOException e) {
        logger.error("Unable to move", e);
        return false;
    }
    return true;
    }

    private String getFolderPath(String folderName) {
    EDTFolder folder = EDTFolder.getFolder(folderName);
    return folder.getPath();
    }
}
