/*
 *Copyright (c) 2023. Magenta Health Inc. All Rights Reserved.
 *
 *This software is published under the GPL GNU General Public License.
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA
 */
package org.oscarehr.inboxhub.display;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import org.oscarehr.inboxhub.inboxdata.LabDataController;
import org.oscarehr.inboxhub.query.InboxhubQuery;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.oscarMDS.data.CategoryData;

public class ManageInboxhubAction extends DispatchAction {
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public ActionForward undefined(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        MiscUtils.getLogger().error("Undefined action attempted for ManageInboxhubAction");
        return mapping.findForward("error");
    }

    public ActionForward displayInboxForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", SecurityInfoManager.READ, null)) {
            return mapping.findForward("unauthorized");
        }

        LabDataController labDataController = new LabDataController();
        String unclaimed = (String) request.getParameter("unclaimed");
        InboxhubQuery query = (InboxhubQuery) form;
        labDataController.setInboxFormQueryUnclaimed(query, unclaimed);
        request.setAttribute("query", query);

        labDataController.sanitizeInboxFormQuery(loggedInInfo, query, null, null);
        CategoryData categoryData = labDataController.getCategoryData(query);
        int[] totalCounts = labDataController.getTotalResultsCountBasedOnQuery(query, categoryData);

        request.setAttribute("totalDocsCount", totalCounts[0]);
        request.setAttribute("totalLabsCount", totalCounts[1]);
        request.setAttribute("totalHRMCount", totalCounts[2]);
        request.setAttribute("totalResultsCount", totalCounts[3]);
        request.setAttribute("viewMode", query.getViewMode());
        request.setAttribute("categoryData", categoryData);
        return mapping.findForward("success");
    }

    public ActionForward displayInboxList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", SecurityInfoManager.READ, null)) {
            return mapping.findForward("unauthorized");
        }

        fetchLabData(form, request);
        return mapping.findForward("displayList");
    }

    public ActionForward displayInboxView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", SecurityInfoManager.READ, null)) {
            return mapping.findForward("unauthorized");
        }

        fetchLabData(form, request);
        return mapping.findForward("displayView");
    }

    private void fetchLabData(ActionForm form, HttpServletRequest request) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String page = request.getParameter("page");
        String pageSize = request.getParameter("pageSize");
        String demographicFilter = request.getParameter("demographicFilter");
        String typeFilter = request.getParameter("typeFilter");

        InboxhubQuery query = (InboxhubQuery) form;
        query.setPage(Integer.parseInt(page));
        query.setPageSize(Integer.parseInt(pageSize));

        LabDataController labDataController = new LabDataController();
        labDataController.sanitizeInboxFormQuery(loggedInInfo, query, demographicFilter, typeFilter);
        ArrayList<LabResultData> labDocs = labDataController.getLabData(loggedInInfo, query);
        if (labDocs.size() > 0) {
            String providerNo = request.getSession().getAttribute("user").toString();
            ArrayList<String> labLinks = labDataController.getLabLink(labDocs, query, request.getContextPath(), providerNo);
            request.setAttribute("labLinks", labLinks);
        }

        request.setAttribute("page", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("labDocs", labDocs);
        request.setAttribute("hasMoreData", labDocs.size() > 0);
        request.setAttribute("searchProviderNo", query.getSearchProviderNo());
    }
}
 