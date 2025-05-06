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


package oscar.oscarLab.pageUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.util.MiscUtils;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;

import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oscar.oscarLab.ca.on.CommonLabResultData;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class FileLabs2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public FileLabs2Action() {
    }

    public String execute() {
        if ("fileLabAjax".equals(request.getParameter("method"))) {
            return fileLabAjax();
        }

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", "w", null)) {
            throw new SecurityException("missing required security object (_lab)");
        }

        String flaggedLabs = request.getParameter("flaggedLabs");

        JSONArray jsonArray = null;
        ArrayList<String[]> listFlaggedLabs = new ArrayList<>();

        if (flaggedLabs != null && !flaggedLabs.isEmpty()) {
            JSONObject jsonObject = JSONObject.fromObject(flaggedLabs);
            jsonArray = (JSONArray) jsonObject.get("files");
        }

        if (jsonArray != null) {
            String[] labid;
            for (int i = 0; i < jsonArray.size(); i++) {
                labid = jsonArray.getString(i).split(":");
                listFlaggedLabs.add(labid);
            }
        }

        boolean success = CommonLabResultData.fileLabs(listFlaggedLabs, loggedInInfo);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.accumulate("success", success);
        jsonResponse.accumulate("files", jsonArray);

        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(jsonResponse);
            out.flush();
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error with JSON response ", e);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public String fileLabAjax() {

        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_lab", "w", null)) {
            throw new SecurityException("missing required security object (_lab)");
        }

        String providerNo = (String) request.getSession().getAttribute("user");
        String flaggedLab = request.getParameter("flaggedLabId").trim();
        String labType = request.getParameter("labType").trim();

        ArrayList<String[]> listFlaggedLabs = new ArrayList<String[]>();
        String[] la = new String[]{flaggedLab, labType};
        listFlaggedLabs.add(la);
        CommonLabResultData.fileLabs(listFlaggedLabs, providerNo);

        return null;
    }
}
