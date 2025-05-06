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


package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.common.dao.MeasurementCSSLocationDao;
import org.oscarehr.common.model.MeasurementCSSLocation;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;


import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class EctAddMeasurementStyleSheet2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static MeasurementCSSLocationDao dao = SpringUtils.getBean(MeasurementCSSLocationDao.class);
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public String execute()
            throws ServletException, IOException {

        if (securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null) || securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.measurements", "w", null)) {


            ArrayList<String> messages = new ArrayList<String>();
            String contextPath = request.getContextPath();

            if (!saveFile(file, fileName)) {
                addActionError(getText("errors.fileNotAdded"));
                response.sendRedirect(contextPath + "/oscarEncounter/oscarMeasurements/AddMeasurementStyleSheet.jsp");
                return NONE;
            } else {
                write2Database(fileName);
                String msg = getText("oscarEncounter.oscarMeasurement.msgAddedStyleSheet", fileName);
                messages.add(msg);
                request.setAttribute("messages", messages);
                return SUCCESS;
            }

        } else {
            throw new SecurityException("Access Denied!"); //missing required security object (_admin)
        }
    }

    /**
     * Save a Jakarta FormFile to a preconfigured place.
     *
     * @param file
     */
    public boolean saveFile(File file, String fileName) {
        boolean isAdded = true;

        try {
            // Check if the file already exists in the database
            List<MeasurementCSSLocation> locs = dao.findByLocation(fileName);
            if (!locs.isEmpty()) {
                return false;
            }

            // Retrieve the target directory from properties
            String uploadPath = OscarProperties.getInstance().getProperty("oscarMeasurement_css_upload_path");

            // Ensure the path ends with a slash
            if (!uploadPath.endsWith("/")) {
                uploadPath += "/";
            }

            // Build the full path for the file
            String destinationPath = uploadPath + fileName;

            // Write the file to the destination
            Files.copy(new FileInputStream(file), Paths.get(destinationPath));

        } catch (IOException e) {
            MiscUtils.getLogger().error("Error saving file", e);
            isAdded = false;
        }

        return isAdded;
    }

    /**
     * Write to database
     *
     * @param fileName - the filename to store
     */
    private void write2Database(String fileName) {
        MeasurementCSSLocation m = new MeasurementCSSLocation();
        m.setLocation(fileName);
        dao.persist(m);
    }

    private File file;
    private String fileName; // Name of the uploaded file

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
