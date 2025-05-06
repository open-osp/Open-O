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


package org.oscarehr.learning.web;

import com.Ostermiller.util.ExcelCSVParser;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.oscarehr.learning.StudentImporter;
import org.oscarehr.learning.StudentInfo;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class StudentImport2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static Logger logger = MiscUtils.getLogger();


    public String execute() throws IOException {
        return uploadFile();
    }

    /*
     * Import
     *
     * Create student provider
     * Create login
     * Assign Role
     * Student number in practitioner_no field
     *
     *
     */
    public String doImport() {
        return null;
    }

    public String uploadFile()
            throws IOException {
        logger.info("upload student data");

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String[][] data = ExcelCSVParser.parse(Files.newBufferedReader(file.toPath()));

        List<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();

        for (int x = 0; x < data.length; x++) {
            if (data[x].length != 6) {
                logger.warn("skipping line..invalid number of fields");
                continue;
            }
            String lastName = data[x][0];
            String firstName = data[x][1];
            String login = data[x][2];
            String password = data[x][3];
            String pin = data[x][4];
            String studentNumber = data[x][5];

            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setLastName(lastName);
            studentInfo.setFirstName(firstName);
            studentInfo.setUsername(login);
            studentInfo.setPassword(password);
            studentInfo.setPin(pin);
            studentInfo.setStudentNumber(studentNumber);

            studentInfoList.add(studentInfo);
            logger.info("importing: " + lastName + "," + firstName + "," + login + "," + password + "," + pin + "," + studentNumber);
        }

        int recordsImported = StudentImporter.importStudentInfo(loggedInInfo.getCurrentFacility().getId(), studentInfoList);


        request.setAttribute("total_imported", recordsImported);

        response.sendRedirect("/oscarLearning/StudentImport.jsp?r=" + recordsImported);
        return NONE;
    }

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
