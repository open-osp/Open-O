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
package ca.openosp;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import ca.openosp.openo.utility.MiscUtils;

public class DocumentUploadServlet extends HttpServlet {

    final static int BUFFER = 4096;

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String foldername = "", fileheader = "", forwardTo = "";
        forwardTo = OscarProperties.getInstance().getProperty("RA_FORWORD");
        foldername = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");

        String inboxFolder = OscarProperties.getInstance().getProperty("ONEDT_INBOX");
        String archiveFolder = OscarProperties.getInstance().getProperty("ONEDT_ARCHIVE");

        if (forwardTo == null || forwardTo.length() < 1)
            return;

        String providedFilename = request.getParameter("filename");
        if (providedFilename != null) {
            providedFilename = URLDecoder.decode(providedFilename, "UTF-8");
            
            // Validate and sanitize the filename to prevent path traversal
            String sanitizedFilename = FilenameUtils.getName(providedFilename);
            if (sanitizedFilename == null || sanitizedFilename.isEmpty()) {
                MiscUtils.getLogger().error("Invalid filename provided: " + providedFilename);
                return;
            }
            
            File documentDirectory = new File(foldername);
            File inboxDir = new File(inboxFolder);
            File archiveDir = new File(archiveFolder);
            
            // Use sanitized filename to construct safe file paths
            File providedFile = new File(inboxDir, sanitizedFilename);
            
            try {
                // Validate that the file is within the inbox directory using canonical paths
                String canonicalInboxPath = inboxDir.getCanonicalPath();
                String canonicalFilePath = providedFile.getCanonicalPath();
                
                if (!canonicalFilePath.startsWith(canonicalInboxPath + File.separator)) {
                    MiscUtils.getLogger().error("File does not reside in the inbox path: " + providedFilename);
                    return;
                }
                
                // If file doesn't exist in inbox, check archive
                if (!providedFile.exists()) {
                    providedFile = new File(archiveDir, sanitizedFilename);

                    String canonicalArchivePath = archiveDir.getCanonicalPath();
                    canonicalFilePath = providedFile.getCanonicalPath();
                    
                    if (!canonicalFilePath.startsWith(canonicalArchivePath + File.separator)) {
                        MiscUtils.getLogger().error("File does not reside in the archive path: " + providedFilename);
                        return;
                    }
                }
                
                // Verify the file exists before copying
                if (!providedFile.exists()) {
                    MiscUtils.getLogger().error("File not found: " + sanitizedFilename);
                    return;
                }
                
                FileUtils.copyFileToDirectory(providedFile, documentDirectory);
                fileheader = sanitizedFilename;
                
            } catch (IOException e) {
                MiscUtils.getLogger().error("Error processing file: " + sanitizedFilename, e);
                return;
            }
        } else {

            DiskFileUpload upload = new DiskFileUpload();

            try {
                // Parse the request
                @SuppressWarnings("unchecked")
                List<FileItem> /* FileItem */items = upload.parseRequest(request);
                // Process the uploaded items
                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();

                    if (item.isFormField()) { //
                    } else {
                        String pathName = item.getName();
                        String[] fullFile = pathName.split("[/|\\\\]");
                        String uploadedFilename = fullFile[fullFile.length - 1];
                        
                        // Sanitize the uploaded filename to prevent path traversal
                        String sanitizedUploadedFilename = FilenameUtils.getName(uploadedFilename);
                        if (sanitizedUploadedFilename == null || sanitizedUploadedFilename.isEmpty()) {
                            MiscUtils.getLogger().error("Invalid uploaded filename: " + uploadedFilename);
                            continue; // Skip this file
                        }
                        
                        File documentDir = new File(foldername);
                        File savedFile = new File(documentDir, sanitizedUploadedFilename);
                        
                        try {
                            // Validate that the saved file will be within the document directory
                            String canonicalDocPath = documentDir.getCanonicalPath();
                            String canonicalSavedPath = savedFile.getCanonicalPath();
                            
                            if (!canonicalSavedPath.startsWith(canonicalDocPath + File.separator)) {
                                MiscUtils.getLogger().error("File does not start with document path: " + uploadedFilename);
                                continue; // Skip this file
                            }
                            
                            fileheader = sanitizedUploadedFilename;
                            item.write(savedFile);
                            
                            if (OscarProperties.getInstance().isPropertyActive("moh_file_management_enabled")) {
                                File inboxDir = new File(inboxFolder);
                                FileUtils.copyFileToDirectory(savedFile, inboxDir);
                            }
                        } catch (IOException e) {
                            MiscUtils.getLogger().error("Error validating file path for: " + sanitizedUploadedFilename, e);
                            continue; // Skip this file
                        }
                    }
                }
            } catch (FileUploadException e) {
                MiscUtils.getLogger().error("Error", e);
            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            }
        }

        DocumentBean documentBean = new DocumentBean();
        request.setAttribute("documentBean", documentBean);
        documentBean.setFilename(fileheader);
        RequestDispatcher dispatch = getServletContext().getRequestDispatcher(forwardTo);
        dispatch.forward(request, response);
    }
}
