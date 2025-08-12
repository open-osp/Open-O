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

package oscar.oscarResearch.oscarDxResearch.pageUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DxDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.DxAssociation;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.Ostermiller.util.ExcelCSVParser;
import com.Ostermiller.util.ExcelCSVPrinter;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class dxResearchLoadAssociations2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private DxDao dxDao = (DxDao) SpringUtils.getBean(DxDao.class);
    private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    private static final String PRIVILEGE_READ = "r";
    private static final String PRIVILEGE_UPDATE = "u";
    private static final String PRIVILEGE_WRITE = "w";

    public String execute() throws Exception {
        getAllAssociations();

        String method = request.getParameter("method");
        if ("uploadFile".equals(method)) {
            uploadFile();
        }
        if ("clearAssociations".equals(method)) {
            clearAssociations();
        }
        if ("export".equals(method)) {
            export();
        }
        if ("autoPopulateAssociations".equals(method)) {
            autoPopulateAssociations();
        }

        return SUCCESS;
     }

    public String getAllAssociations() throws IOException {
        checkPrivilege(request, PRIVILEGE_READ);

        //load associations
        List<DxAssociation> associations = dxDao.findAllAssociations();

        for (DxAssociation assoc : associations) {
            assoc.setDxDescription(getDescription(assoc.getDxCodeType(), assoc.getDxCode()));
            assoc.setDescription(getDescription(assoc.getCodeType(), assoc.getCode()));
        }

        //serialize and return
        JSONArray jsonArray = JSONArray.fromObject(associations);
        response.getWriter().print(jsonArray);
        return null;
    }

    private String getDescription(String dxCodeType, String dxCode) {
        for (Object[] o : dxDao.findCodingSystemDescription(dxCodeType, dxCode)) {
            return String.valueOf(o[1]);
        }
        return null;
    }

    public String clearAssociations() throws IOException {
        checkPrivilege(request, PRIVILEGE_UPDATE);

        int recordsUpdated = dxDao.removeAssociations();

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("recordsUpdated", recordsUpdated);
        response.getWriter().print(JSONObject.fromObject(map));
        return null;
    }

    public String addAssociation() throws IOException {
        checkPrivilege(request, PRIVILEGE_WRITE);

        DxAssociation dxa = new DxAssociation();
        dxa.setCodeType(request.getParameter("codeType"));
        dxa.setCode(request.getParameter("code"));
        dxa.setDxCodeType(request.getParameter("dxCodeType"));
        dxa.setDxCode(request.getParameter("dxCode"));

        dxDao.persist(dxa);

        Map<String, String> map = new HashMap<String, String>();
        map.put("result", "success");
        response.getWriter().print(JSONObject.fromObject(map));
        return null;
    }

    public String export() throws IOException {
        checkPrivilege(request, PRIVILEGE_READ);

        List<DxAssociation> associations = dxDao.findAllAssociations();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"dx_associations.csv\"");

        ExcelCSVPrinter printer = new ExcelCSVPrinter(response.getWriter());

        printer.writeln(new String[]{"Issue List Code Type", "Issue List Code", "Disease Registry Code Type", "Disease Registry Code"});
        for (DxAssociation dxa : associations) {
            printer.writeln(new String[]{dxa.getCodeType(), dxa.getCode(), dxa.getDxCodeType(), dxa.getDxCode()});
        }

        printer.flush();
        printer.close();

        return null;
    }

    public String uploadFile() throws IOException {
        checkPrivilege(request, PRIVILEGE_WRITE);

        if (file == null) {
            addActionError("File not uploaded.");
            return ERROR;
        }

        String[][] data = ExcelCSVParser.parse(new FileReader(file));

        int rowsInserted = 0;

        if (this.isReplace()) {
            dxDao.removeAssociations();
        }

        for (int x = 1; x < data.length; x++) {
            if (data[x].length != 4) {
                continue;
            }
            DxAssociation assoc = new DxAssociation();
            assoc.setCodeType(data[x][0]);
            assoc.setCode(data[x][1]);
            assoc.setDxCodeType(data[x][2]);
            assoc.setDxCode(data[x][3]);

            dxDao.persist(assoc);
            rowsInserted++;
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("recordsAdded", rowsInserted);
        response.getWriter().print(JSONObject.fromObject(map));

        return SUCCESS;
    }

    public String autoPopulateAssociations() throws IOException {
        checkPrivilege(request, PRIVILEGE_WRITE);

        int recordsAdded = 0;
        CaseManagementIssueDAO cmiDao = (CaseManagementIssueDAO) SpringUtils.getBean(CaseManagementIssueDAO.class);
        CaseManagementManager cmMgr = (CaseManagementManager) SpringUtils.getBean(CaseManagementManager.class);
        IssueDAO issueDao = (IssueDAO) SpringUtils.getBean(IssueDAO.class);
        DxresearchDAO dxrDao = (DxresearchDAO) SpringUtils.getBean(DxresearchDAO.class);

        //clear existing entries
        dxrDao.removeAllAssociationEntries();

        //get all certain issues
        List<CaseManagementIssue> certainIssues = cmiDao.getAllCertainIssues();
        MiscUtils.getLogger().debug("certain issues found=" + certainIssues.size());
        for (CaseManagementIssue issue : certainIssues) {
            Issue iss = issueDao.getIssue(issue.getIssue().getId());
            MiscUtils.getLogger().debug("checking " + iss.getType() + "," + iss.getCode());
            DxAssociation assoc = dxDao.findAssociation(iss.getType(), iss.getCode());
            if (assoc != null) {
                MiscUtils.getLogger().debug("match");
                //we now have a certain issue which matches an association.
                cmMgr.saveToDx(LoggedInInfo.getLoggedInInfoFromSession(request), issue.getDemographic_no().toString(), assoc.getDxCode(), assoc.getDxCodeType(), true);
                recordsAdded++;
            }
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("recordsAdded", recordsAdded);
        response.getWriter().print(JSONObject.fromObject(map));

        return null;
    }


    private void checkPrivilege(HttpServletRequest request, String privilege) {
        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_dxresearch", privilege, null)) {
            throw new RuntimeException("missing required security object (_dxresearch)");
        }
    }

    private File file; // Uploaded file
    private boolean replace = true; // Flag for replacement

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }
}