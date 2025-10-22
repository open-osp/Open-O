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

package ca.openosp.openo.consultations;

import java.util.List;

import ca.openosp.openo.commn.PaginationQuery;
import ca.openosp.openo.commn.dao.ConsultRequestDao;
import ca.openosp.openo.commn.model.ConsultationRequest;
import ca.openosp.openo.utility.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.openosp.openo.log.LogAction;

@Component
@Deprecated
public class ConsultationService {
    @Autowired
    private ConsultRequestDao consultationDao;


    /**
     * Use to get consultation count for pagination display
     *
     * @param paginationQuery
     * @return
     */
    @Deprecated
    public int getConsultationCount(PaginationQuery paginationQuery) {
        return this.consultationDao.getConsultationCount(paginationQuery);
    }

    /**
     * List consultations
     *
     * @param paginationQuery
     * @return
     */
    @Deprecated
    public List<ConsultationRequest> listConsultationRequests(LoggedInInfo loggedInInfo, PaginationQuery paginationQuery) {
        ConsultationQuery query = (ConsultationQuery) paginationQuery;

        List<ConsultationRequest> results = consultationDao.listConsultationRequests(query);
        //--- log action ---
        if (results.size() > 0) {
            String resultIds = ConsultationRequest.getIdsAsStringList(results);
            LogAction.addLogSynchronous(loggedInInfo, "ConsultationService.listConsultationRequests", "ids returned=" + resultIds);
        }


        return results;
    }
}
