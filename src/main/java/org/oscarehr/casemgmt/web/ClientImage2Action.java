//CHECKSTYLE:OFF
/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.casemgmt.web;

import com.opensymphony.xwork2.ActionSupport;

import oscar.OscarProperties;

import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.service.ClientImageManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;

public class ClientImage2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private File clientImage;
    private String clientImageFileName;

    private static Logger log = MiscUtils.getLogger();

    private ClientImageManager clientImageManager = SpringUtils.getBean(ClientImageManager.class);

    // Execute on struts action call
    public String execute() {
        return saveImage();
    }

    public String saveImage() {
        HttpSession session = request.getSession(true);
        String id = (String) session.getAttribute("clientId");

        log.info("client image upload: id=" + id);

        // Get file extension from original filename
        String type = null;
        if (clientImageFileName != null && clientImageFileName.contains(".")) {
            type = clientImageFileName.substring(clientImageFileName.lastIndexOf('.') + 1).toLowerCase();
        }

        log.info("extension = " + type);

        // Ensure that the upload directory is correcnt and create a new image object that will be saved to the client
        try {
            // Get context of the temp directory, get the file path to the the temp directory
            ServletContext servletContext = ServletActionContext.getServletContext();
            File tmpdirAttribute = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            String tmpdir = tmpdirAttribute.toString();

            // Set safe directory and client image canonical files
            File safeDirectory = new File(tmpdir).getCanonicalFile();
            File resolvedFile = clientImage.getCanonicalFile();

            // Get safe path and resolved canonical paths
            String safePath = safeDirectory.getCanonicalPath();
            String resolvedPath = resolvedFile.getCanonicalPath();

            // Ensure the file is within the safe directory
            if (!resolvedPath.startsWith(safePath + File.separator)) {
                throw new IllegalArgumentException("Invalid file path: " + resolvedPath);
            }

            byte[] imageData = Files.readAllBytes(resolvedFile.toPath());

            ClientImage clientImageObj = new ClientImage();
            clientImageObj.setDemographic_no(Integer.parseInt(id));
            clientImageObj.setImage_data(imageData);
            clientImageObj.setImage_type(type);

            clientImageManager.saveClientImage(clientImageObj);

        } catch (Exception e) {
            log.error("Error saving image", e);
            addActionError("Error saving image.");
            return ERROR;
        }

        request.setAttribute("success", true);
        return SUCCESS;
    }

    public void setClientImage(File clientImage) { 
        this.clientImage = clientImage; 
    }

    public void setClientImageFileName(String name) { 
        this.clientImageFileName = name; 
    }
}
