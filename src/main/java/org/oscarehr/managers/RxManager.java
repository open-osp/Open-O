/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.managers;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.FavoriteDao;
import org.oscarehr.common.exception.AccessDeniedException;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Favorite;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.RxStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oscar.log.LogAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public interface RxManager {

    //public static class PrescriptionDrugs{};
    public List<Drug> getDrugs( LoggedInInfo info,  int demographicNo,  String status)
            throws UnsupportedOperationException;

    public List<Drug> getDrugs( LoggedInInfo info,  int demographicNo,  RxStatus status);
	public List<String> getCurrentSingleLineMedications(LoggedInInfo loggedInInfo, int demographicNo);
    public Drug getDrug( LoggedInInfo info,  int drugId) throws UnsupportedOperationException;
    public Drug addDrug( LoggedInInfo info,  Drug d);
    public Drug updateDrug( LoggedInInfo info,  Drug d);
    public boolean discontinue(LoggedInInfo info, int drugId, int demographicId, String reason);
    public RxManagerImpl.PrescriptionDrugs prescribe(LoggedInInfo info, List<Drug> drugs, Integer demoNo);
    public List<Drug> getHistory(Integer id, LoggedInInfo info, Integer demographicNo);
    public List<Favorite> getFavorites(String pid);
    public Boolean addFavorite(Favorite f);
       
    public List<Drug> getLongTermDrugs( LoggedInInfo info,  int demographicNo);


    /**
     * Utility class for returning a prescription and associated drugs.
     */
    public static class PrescriptionDrugs{
        public Prescription prescription;
        public List<Drug> drugs;

        public PrescriptionDrugs(Prescription p, List<Drug> d){
            this.prescription = p;
            this.drugs = d;
        }

    }
    // statuses for drugs
    public static final String ALL = "all";
    public static final String CURRENT = "current";
    public static final String ARCHIVED = "archived";

}
