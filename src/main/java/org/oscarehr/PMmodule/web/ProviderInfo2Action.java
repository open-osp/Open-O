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

package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class ProviderInfo2Action extends ActionSupport {
    private HttpServletRequest request = ServletActionContext.getRequest();
    private FacilityDao facilityDao = SpringUtils.getBean(FacilityDao.class);
    private ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);
    private ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
    private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);

    public String execute() {
        return view();
    }

    public String cancel() {
        return view();
    }

    public String view() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String providerNo = null;
        providerNo = (String) request.getSession().getAttribute("user");
        if (providerNo == null || "".equals(providerNo)) {
            providerNo = loggedInInfo.getLoggedInProviderNo();
        }

        request.setAttribute("provider", providerManager.getProvider(providerNo));
        request.setAttribute("agencyDomain", providerManager.getAgencyDomain(providerNo));

        List<ProgramProvider> programDomain = new ArrayList<ProgramProvider>();

        int facilityId1 = 0;
        Facility facility = (Facility) request.getSession().getAttribute("currentFacility");
        if (facility != null) facilityId1 = facility.getId();

        for (ProgramProvider programProvider : providerManager.getProgramDomainByFacility(providerNo, new Integer(facilityId1))) {
            Program program = programManager.getProgram(programProvider.getProgramId());

            if (program.isActive()) {
                programProvider.setProgram(program);
                programDomain.add(programProvider);
            }
        }

        List<Integer> facilityIds = providerDao.getFacilityIds(providerNo);
        ArrayList<Facility> facilities = new ArrayList<Facility>();
        for (Integer facilityId : facilityIds) {
            facilities.add(facilityDao.find(facilityId));
        }


        request.setAttribute("programDomain", programDomain);
        request.setAttribute("facilityDomain", facilities);

        return "view";
    }
}
