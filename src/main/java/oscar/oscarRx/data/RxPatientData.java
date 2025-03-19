/**
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
 */

package oscar.oscarRx.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.oscarRx.data.model.Patient;

public class RxPatientData {
	public static Logger logger = MiscUtils.getLogger();
	private static final DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);

	private RxPatientData() {
		// prevent instantiation
	}

	/* Patient Search */

	public static Patient[] PatientSearch(LoggedInInfo loggedInInfo, String surname, String firstName) {

		Patient[] arr = {};
		List<Patient> patients = new ArrayList<Patient>();
		List<Demographic> demographics = demographicManager.searchDemographic(loggedInInfo, surname + "," + firstName);
		for (Demographic demographic : demographics) {
			Patient p = new Patient(demographic);
			patients.add(p);
		}
		return patients.toArray(arr);
	}

	/* Patient Information */

	public static Patient getPatient(LoggedInInfo loggedInInfo, int demographicNo) {
		Demographic demographic = demographicManager.getDemographic(loggedInInfo, demographicNo);
		return new Patient(demographic);
	}

	public static Patient getPatient(LoggedInInfo loggedInInfo, String demographicNo) {
		Demographic demographic = demographicManager.getDemographic(loggedInInfo,demographicNo);
		return new Patient(demographic);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.toString();
	}
}
