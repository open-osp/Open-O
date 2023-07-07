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


/*
 * InsideLabUploadAction.java
 *
 * Created on June 28, 2007, 1:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.pageUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;
import oscar.oscarLab.ca.all.util.Utilities;

public class InsideLabUploadAction extends Action {
    private enum FileStatus {
        COMPLETED,
        FAILED,
        INVALID,
        EXISTS
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ActionForward success = mapping.findForward("success");
        if (!ServletFileUpload.isMultipartContent(request)) { return success; }

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        checkUserPrivilege(loggedInInfo);

        List<FileItem> fileItems = getFiles(request);
        if (fileItems == null) { return success; }
        Map<String, FileStatus> filesStatusMap = processFiles(loggedInInfo, request, fileItems);
        request.setAttribute("filesStatusMap", filesStatusMap);

        return success;
    }

    private void checkUserPrivilege(LoggedInInfo loggedInInfo) {
        SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", "w", null)) {
            throw new SecurityException("missing required security object (_lab)");
        }
    }

    private List<FileItem> getFiles(HttpServletRequest request) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

        try {
            List<FileItem> fileItems = servletFileUpload.parseRequest(request);
            List<FileItem> filteredFileItems = new ArrayList<>();

            for (FileItem fileItem : fileItems) {
                if (fileItem.getFieldName().equals("importFiles")) {
                    filteredFileItems.add(fileItem);
                }
            }
            return filteredFileItems;
        } catch (FileUploadException e) {
            MiscUtils.getLogger().error("Error occurred while uploading HL7 labs", e);
            return null;
        }
    }

    private Map<String, FileStatus> processFiles(LoggedInInfo loggedInInfo, HttpServletRequest request, List<FileItem> fileItems) {
        Map<String, FileStatus> filesStatusMap = new HashMap<>();
        String fileType = getFileType(request);

        for (FileItem fileItem : fileItems) {
            String fileName = fileItem.getName();
            String filePath = saveFile(fileItem);
            if (filePath == null) {
                filesStatusMap.put(fileName, FileStatus.FAILED);
                break;
            }
            FileStatus fileStatus = processFile(loggedInInfo, request, filePath, fileType);
            filesStatusMap.put(fileName, fileStatus);
        }

        return filesStatusMap;
    }

    private String getFileType(HttpServletRequest request) {
        String fileType = request.getParameter("type");
        String otherFileType = request.getParameter("otherType");
        if (fileType != null && !fileType.equals("OTHER")) { return fileType; }
        if (otherFileType != null) { return otherFileType; }

        return null;
    }

    private String saveFile(FileItem fileItem) {
        try (InputStream formFileInputStream = fileItem.getInputStream()) {
            return Utilities.saveFile(formFileInputStream, fileItem.getName());
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error occurred while saving " + fileItem.getName() + " file", e);
            return null;
        }
    }

    private FileStatus processFile(LoggedInInfo loggedInInfo, HttpServletRequest request, String filePath, String fileType) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        int checkFileUploadedSuccessfully;

        try (InputStream localFileInputStream = Files.newInputStream(path)) {
            String providerNumber = (String) request.getSession().getAttribute("user");
            checkFileUploadedSuccessfully = FileUploadCheck.addFile(fileName, localFileInputStream, providerNumber);
            if (checkFileUploadedSuccessfully == FileUploadCheck.UNSUCCESSFUL_SAVE) { return FileStatus.EXISTS; }
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error occurred while processing " + fileName + " file", e);
            return FileStatus.FAILED;
        }

        MessageHandler msgHandler = HandlerClassFactory.getHandler(fileType);
        if ((msgHandler.parse(loggedInInfo, getClass().getSimpleName(), filePath, checkFileUploadedSuccessfully, request.getRemoteAddr())) != null) {
            return FileStatus.COMPLETED;
        }
        return FileStatus.INVALID;
    }
}
