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

package org.caisi.tickler.web;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.caisi.service.DemographicManagerTickler;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.model.CustomFilter;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class CustomFilter2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static Logger log = MiscUtils.getLogger();
    private ProviderManager providerMgr = SpringUtils.getBean(ProviderManager.class);
    private DemographicManagerTickler demographicMgr = SpringUtils.getBean(DemographicManagerTickler.class);
    private ProgramManager programMgr = SpringUtils.getBean(ProgramManager.class);
    private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);


    public void setProgramManager(ProgramManager programMgr) {
        this.programMgr = programMgr;
    }

    public void setDemographicManager(DemographicManagerTickler demographicManager) {
        this.demographicMgr = demographicManager;
    }

    public void setProviderManager(ProviderManager providerMgr) {
        this.providerMgr = providerMgr;
    }

    String getProviderNo(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("user");
    }

    /* default to 'list' */
    public String execute() throws Exception {
        String method = request.getParameter("method");
        if ("run".equals(method)) {
            return run();
        } else if ("changeShortCutStatus".equals(method)) {
            return changeShortCutStatus();
        } else if ("edit".equals(method)) {
            return edit();
        } else if ("save".equals(method)) {
            return save();
        } else if ("delete".equals(method)) {
            return delete();
        }
        return list();
    }

    //TODO: need to forward to TicklerAction
    public String run() {
        log.debug("run");

        return list();
    }

    public String list() {
        log.debug("list");

        request.setAttribute("custom_filters", ticklerManager.getCustomFilters(this.getProviderNo(request)));
        return "customFilterList";
    }


    public String changeShortCutStatus() throws Exception {
        log.debug("changeShortCutStatus");
        String id = request.getParameter("id");
        if (id != null && !id.equals("")) {
            CustomFilter filter = ticklerManager.getCustomFilterById(Integer.valueOf(id));
            filter.setShortcut(!filter.isShortcut());
            ticklerManager.saveCustomFilter(filter);
            log.debug("Filter :" + filter.getName() + " shortcut now set to " + filter.isShortcut());
        }
        request.setAttribute("custom_filters", ticklerManager.getCustomFilters(this.getProviderNo(request)));
        return "customFilterList";
    }

    public String edit() throws Exception {
        log.debug("edit");

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String id = request.getParameter("id");
        if (id != null && !id.equals("")) {
            CustomFilter filter = ticklerManager.getCustomFilterById(Integer.valueOf(id));
            /* get the demographic */
            String demo_no = filter.getDemographicNo();

            if (!("".equals(demo_no)) && demo_no != null) {
                Demographic demographic = demographicMgr.getDemographic(demo_no);
                if (demographic != null) {
                    filter.setDemographic_webName(demographic.getFormattedName());
                }
            } else
                filter.setDemographic_webName("");

            String filterName = filter.getName();
            if (filterName != null && filterName.equals("*Myticklers*")) {
                filter.setAssignee(filter.getProviderNo());
            }

            this.setFilter(filter);
            request.setAttribute("customFilterForm", filter);
            request.setAttribute("custom_filter", filter);
            request.setAttribute("me_no", request.getSession().getAttribute("user"));
            request.setAttribute("me", providerMgr.getProvider((String) request.getSession().getAttribute("user")).getFormattedName());
        }

        request.setAttribute("providers", providerMgr.getProviders());
        request.setAttribute("priorityList", CustomFilter.priorityList);
        request.setAttribute("statusList", CustomFilter.statusList);

        request.setAttribute("programs", programMgr.getProgramDomainInCurrentFacilityForCurrentProvider(loggedInInfo, false));
        return "customFilterForm";
    }


    /* save a custom filter */
    public String save() {
        log.debug("save");


        CustomFilter filter = this.getFilter();

        if ("".equals(filter.getDemographic_webName())) {
            filter.setDemographicNo("");
        }
        String[] providers = request.getParameterValues("provider");
        if (providers != null) {
            Set sProviders = new HashSet();
            for (int x = 0; x < providers.length; x++) {
                sProviders.add(new Provider(providers[x]));
            }
            filter.setProviders(sProviders);
        }

        String[] assignees = request.getParameterValues("assignee");
        if (assignees != null) {
            Set sAssignees = new HashSet();
            for (int x = 0; x < assignees.length; x++) {
                sAssignees.add(new Provider(assignees[x]));
            }
            filter.setAssignees(sAssignees);
        }
        filter.setProviderNo(this.getProviderNo(request));

        if ("All Programs".equals(filter.getProgramId())) {
            filter.setProgramId("");
        }
        ticklerManager.saveCustomFilter(filter);

        addActionMessage(getText("filter.saved"));

        return list();
    }

    /* delete a filter */
    public String delete() throws Exception {
        log.debug("delete");
        String[] checks = request.getParameterValues("checkbox");

        for (int x = 0; x < checks.length; x++) {
            //ticklerMgr.deleteCustomFilter(checks[x]);
            ticklerManager.deleteCustomFilterById(Integer.valueOf(checks[x]));

        }
        return list();
    }

    private CustomFilter filter;

    public CustomFilter getFilter() {
        return filter;
    }

    public void setFilter(CustomFilter filter) {
        this.filter = filter;
    }
}
