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
package ca.openosp.openo.dashboard.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opensymphony.xwork2.ActionSupport;

import ca.openosp.openo.managers.ProviderManager2;
import ca.openosp.openo.managers.SecurityInfoManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.SpringUtils;

/**
 * Struts2 action for managing auto-link to MRP (Most Responsible Provider) property.
 * Provides endpoints for viewing and updating the auto-link status.
 *
 * @since 2025-01-20
 */
public class ProviderLinkingRulesAction extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private static final Logger logger = MiscUtils.getLogger();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ProviderManager2 providerManager2 = SpringUtils.getBean(ProviderManager2.class);
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    /**
     * Main execution method that routes to specific methods based on the "method" parameter.
     *
     * @return String result code (typically null for JSON responses)
     */
    public String execute() {
        String method = request.getParameter("method");

        if ("updateProviderLinkingRulesProperty".equals(method)) {
            return updateProviderLinkingRulesProperty();
        } else if ("viewProviderLinkingRulesPropertyStatus".equals(method)) {
            return viewProviderLinkingRulesPropertyStatus();
        }

        return null;
    }

    /**
     * Updates the auto-link to MRP property status.
     * Requires WRITE permission on either _admin.lab or _admin.hrm security objects.
     *
     * @return String result code (null for JSON response)
     */
    public String updateProviderLinkingRulesProperty() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_admin", SecurityInfoManager.WRITE, null) &&
            !securityInfoManager.hasPrivilege(loggedInInfo, "_lab", SecurityInfoManager.WRITE, null)) {
            throw new RuntimeException("missing required security object (_admin or _lab)");
        }

        String value = request.getParameter("status");
        boolean status = providerManager2.updateProviderLinkingRulesProperty(loggedInInfo, value);

        ObjectNode json = objectMapper.createObjectNode();
        json.put("status", status);
        writeJsonResponse(response, json.toString());

        return null;
    }

    /**
     * Retrieves the current auto-link to MRP property status.
     * Requires READ permission on either _admin.lab or _admin.hrm security objects.
     *
     * @return String result code (null for JSON response)
     */
    public String viewProviderLinkingRulesPropertyStatus() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_admin", SecurityInfoManager.READ, null) &&
            !securityInfoManager.hasPrivilege(loggedInInfo, "_lab", SecurityInfoManager.READ, null)) {
            throw new RuntimeException("missing required security object (_admin or _lab)");
        }

        boolean status = providerManager2.viewProviderLinkingRulesPropertyStatus(loggedInInfo);

        ObjectNode json = objectMapper.createObjectNode();
        json.put("status", status);
        writeJsonResponse(response, json.toString());

        return null;
    }

    /**
     * Writes a JSON response to the HTTP response output stream.
     *
     * @param response HttpServletResponse the response object
     * @param json String JSON content to write
     */
    private void writeJsonResponse(HttpServletResponse response, String json) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(json);
        } catch (Exception e) {
            logger.error("An error occurred while writing JSON response to the output stream", e);
        }
    }
}
