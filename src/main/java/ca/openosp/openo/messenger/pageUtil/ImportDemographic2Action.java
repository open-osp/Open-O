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

package ca.openosp.openo.messenger.pageUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.openosp.openo.commn.model.Demographic;
import ca.openosp.openo.managers.MessengerDemographicManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class ImportDemographic2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static MessengerDemographicManager messengerDemographicManager = SpringUtils.getBean(MessengerDemographicManager.class);

    public String execute() {

        String remoteFacilityId = request.getParameter("remoteFacilityId");
        String remoteDemographicNo = request.getParameter("remoteDemographicNo");
        String messageID = request.getParameter("messageID");
        String selectedDemographicNo = request.getParameter("selectedDemographicNo");

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        /*
         * During the first round(selectedDemographicNo == null): the given demographic is imported or a selection list
         * is offered back to the user
         *
         * During the second round (selectedDemographicNo != null): the user has made a selection and that selection
         * will be linked with the remote demographic.
         */
        if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {

            /*
             * Import the demographic automatically if there is no match in the local facility. Otherwise
             * return a list of possible matches for the user to select from.
             */
            List<Demographic> demographicList = null;

            if (selectedDemographicNo == null) {
                demographicList = messengerDemographicManager.importDemographic(loggedInInfo,
                        Integer.parseInt(remoteFacilityId), Integer.parseInt(remoteDemographicNo), Integer.parseInt(messageID));
            }

            if (demographicList != null) {
                request.setAttribute("demographicUserSelect", demographicList);
                request.setAttribute("remoteDemographicNo", remoteDemographicNo);
            }

            /*
             * If the user has chosen a local demographic to link with the demographic offered from the remote facility.
             */
            if (selectedDemographicNo != null) {
                messengerDemographicManager.linkDemographicWithRemote(loggedInInfo,
                        Integer.parseInt(selectedDemographicNo), Integer.parseInt(remoteFacilityId), Integer.parseInt(remoteDemographicNo), Integer.parseInt(messageID));
            }
        }

        request.setAttribute("boxType", "0");
        request.setAttribute("messageID", messageID);

        return SUCCESS;
    }
}
