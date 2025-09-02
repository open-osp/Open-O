//CHECKSTYLE:OFF
/**
 * Copyright (c) 2008-2012 Indivica Inc.
 * <p>
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package ca.openosp.openo.hospitalReportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;

import ca.openosp.openo.lab.ca.all.util.Utilities;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class HRMUploadLab2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    public enum FileStatus {
        COMPLETED,
        FAILED,
        INVALID
    }

    @Override
    public String execute() {
        String success = SUCCESS;
        if (!ServletFileUpload.isMultipartContent(request)) {
            return success;
        }

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        List<FileItem> fileItems = getFiles(request);
        if (fileItems == null) {
            return success;
        }

        Map<String, FileStatus> filesStatusMap = processFiles(loggedInInfo, fileItems);
        request.setAttribute("filesStatusMap", filesStatusMap);
        return success;
    }

    private Map<String, FileStatus> processFiles(LoggedInInfo loggedInInfo, List<FileItem> fileItems) {
        Map<String, FileStatus> filesStatusMap = new HashMap<>();

        for (FileItem fileItem : fileItems) {
            String fileName = fileItem.getName();

            try (InputStream inputStream = fileItem.getInputStream()) {
                String filePath = Utilities.saveFile(inputStream, fileName);
                HRMReport report = HRMReportParser.parseReport(loggedInInfo, filePath);
                FileStatus fileStatus = handleHRMReport(loggedInInfo, report);
                filesStatusMap.put(fileName, fileStatus);
            } catch (IOException e) {
                MiscUtils.getLogger().error("Error occurred while processing file", e);
                filesStatusMap.put(fileName, FileStatus.INVALID);
            }
        }

        return filesStatusMap;
    }

    private List<FileItem> getFiles(HttpServletRequest request) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

        try {
            return servletFileUpload.parseRequest(request);
        } catch (FileUploadException e) {
            MiscUtils.getLogger().error("Error occurred while uploading files", e);
            return null;
        }
    }

    private FileStatus handleHRMReport(LoggedInInfo loggedInInfo, HRMReport report) {
        if (report == null) {
            return FileStatus.INVALID;
        }

        try {
            HRMReportParser.addReportToInbox(loggedInInfo, report);
            return FileStatus.COMPLETED;
        } catch (Exception e) {
            MiscUtils.getLogger().error("Couldn't handle uploaded HRM report", e);
            return FileStatus.FAILED;
        }
    }
}
