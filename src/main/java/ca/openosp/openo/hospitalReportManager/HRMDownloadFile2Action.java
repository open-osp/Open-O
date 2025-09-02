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
package ca.openosp.openo.hospitalReportManager;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import ca.openosp.openo.hospitalReportManager.dao.HRMDocumentDao;
import ca.openosp.openo.hospitalReportManager.model.HRMDocument;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.openo.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

public class HRMDownloadFile2Action extends ActionSupport {
    private HttpServletRequest request = ServletActionContext.getRequest();
    private HttpServletResponse response = ServletActionContext.getResponse();

    private HRMDocumentDao hrmDocumentDao = SpringUtils.getBean(HRMDocumentDao.class);
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public String execute() throws Exception {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null)) {
            throw new SecurityException("missing required sec object (_hrm)");
        }

        String hash = request.getParameter("hash");
        if (StringUtils.isNullOrEmpty(hash)) {
            throw new Exception("no hash parameter passed");
        }

        List<Integer> ids = hrmDocumentDao.findByHash(hash);

        if (ids == null || ids.size() == 0) {
            throw new Exception("no documents found for hash - " + hash);
        }

        if (ids.size() > 1) {
            throw new Exception("too many documents found for hash - " + hash);
        }

        HRMDocument hd = hrmDocumentDao.find(ids.get(0));

        if (hd == null) {
            throw new Exception("HRMDocument not found - " + ids.get(0));
        }

        HRMReport report = HRMReportParser.parseReport(loggedInInfo, hd.getReportFile());

        if (report == null) {
            throw new Exception("Failed to parse HRMDocument with id " + hd.getId());
        }

        if (!report.isBinary()) {
            throw new Exception("no binary document found");
        }

        byte[] data = report.getBinaryContent();


        String fileName = (report.getLegalLastName() + "-" + report.getLegalFirstName() + "-" + report.getFirstReportClass() + report.getFileExtension()).replaceAll("\\s", "_");
        String contentType = "application/octet-stream";

        if (report.getFileExtension().equals(".pdf")) {
            contentType = "application/pdf";
        }
        if (report.getFileExtension().equals(".tiff")) {
            contentType = "image/tiff";
        }
        if (report.getFileExtension().equals(".rtf")) {
            contentType = "text/enriched";
        }
        if (report.getFileExtension().equals(".jpg")) {
            contentType = "image/jpeg";
        }
        if (report.getFileExtension().equals(".gif")) {
            contentType = "image/gif";
        }
        if (report.getFileExtension().equals(".png")) {
            contentType = "image/png";
        }
        if (report.getFileExtension().equals(".html")) {
            contentType = "text/html";
        }

        File temp = File.createTempFile("HRMDownloadFile", report.getFileExtension());
        temp.deleteOnExit();

        FileUtils.writeByteArrayToFile(temp, data);

        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);

        return NONE;
    }
}
