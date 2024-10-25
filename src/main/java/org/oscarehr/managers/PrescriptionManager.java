/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 *
 * Modifications made by Magenta Health in 2024.
 */
package org.oscarehr.managers;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDrug;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.exception.AccessDeniedException;
import org.oscarehr.common.model.ConsentType;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oscar.log.LogAction;
import oscar.oscarProvider.data.ProSignatureData;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxProviderData;
import oscar.util.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;

public interface PrescriptionManager {

    public Prescription getPrescription(LoggedInInfo loggedInInfo, Integer prescriptionId);

    public List<Prescription> getPrescriptionUpdatedAfterDate(LoggedInInfo loggedInInfo,
            Date updatedAfterThisDateExclusive, int itemsToReturn);

    public List<Prescription> getPrescriptionByDemographicIdUpdatedAfterDate(LoggedInInfo loggedInInfo,
            Integer demographicId, Date updatedAfterThisDateExclusive);

    public List<Drug> getDrugsByScriptNo(LoggedInInfo loggedInInfo, Integer scriptNo, Boolean archived);

    public List<Drug> getUniqueDrugsByPatient(LoggedInInfo loggedInInfo, Integer demographicNo);

    public List<Prescription> getPrescriptionsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo,
            Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive,
            int itemsToReturn);

    public Prescription createNewPrescription(LoggedInInfo info, List<Drug> drugs, Integer demographicNo);

    public List<Drug> getMedicationsByDemographicNo(LoggedInInfo loggedInInfo, Integer demographicNo, Boolean archived);

    public List<Drug> getActiveMedications(LoggedInInfo loggedInInfo, String demographicNo);

    public List<Drug> getActiveMedications(LoggedInInfo loggedInInfo, Integer demographicNo);

    public Drug findDrugById(LoggedInInfo loggedInInfo, Integer drugId);

    public List<Drug> getLongTermDrugs(LoggedInInfo loggedInInfo, Integer demographicId);

    public List<Prescription> getPrescriptions(LoggedInInfo loggedInInfo, Integer demographicId);

    public boolean print(LoggedInInfo loggedInInfo, int scriptNo);

}
