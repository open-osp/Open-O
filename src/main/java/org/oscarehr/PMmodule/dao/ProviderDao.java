/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.PMmodule.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.common.NativeSql;
import org.oscarehr.common.dao.ProviderFacilityDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderFacility;
import org.oscarehr.common.model.ProviderFacilityPK;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.hibernate.type.StandardBasicTypes;
import oscar.OscarProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.SessionFactory;

import com.quatro.model.security.SecProvider;

@SuppressWarnings("unchecked")
public interface ProviderDao {

	public static final String PR_TYPE_DOCTOR = "doctor";
	public static final String PR_TYPE_RESIDENT = "resident";

	public boolean providerExists(String providerNo);

	public Provider getProvider(String providerNo);

	public String getProviderName(String providerNo);

	public String getProviderNameLastFirst(String providerNo);

	public List<Provider> getProviders();

	public List<Provider> getProviders(String[] providers);

	public List<Provider> getProviderFromFirstLastName(String firstname, String lastname);

	public List<Provider> getProviderLikeFirstLastName(String firstname, String lastname);

	public List<Provider> getActiveProviderLikeFirstLastName(String firstname, String lastname);

	public List<SecProvider> getActiveProviders(Integer programId);

	public List<Provider> getActiveProviders(String facilityId, String programId);

	public List<Provider> getActiveProviders();

	public List<Provider> getActiveProviders(boolean filterOutSystemAndImportedProviders);

	public List<Provider> getActiveProvidersByRole(String role);

	public List<Provider> getDoctorsWithOhip();

	public List<Provider> getBillableProviders();

	public List<Provider> getBillableProvidersInBC(LoggedInInfo loggedInInfo);

	public List<Provider> getBillableProvidersInBC();

	public List<Provider> getProviders(boolean active);

	public List<Provider> getActiveProviders(String providerNo, Integer shelterId);

	public List<Provider> getActiveProvider(String providerNo);

	public List<Provider> search(String name);

	public List<Provider> getProvidersByTypeWithNonEmptyOhipNo(String type);

	public List<Provider> getProvidersByType(String type);

	public List<Provider> getProvidersByTypePattern(String typePattern);

	public List getShelterIds(String provider_no);

	public void addProviderToFacility(String provider_no, int facilityId);

	public void removeProviderFromFacility(String provider_no,
			int facilityId);

	public List<Integer> getFacilityIds(String provider_no);

	public List<String> getProviderIds(int facilityId);

	public void updateProvider(Provider provider);

	public void saveProvider(Provider provider);

	public Provider getProviderByPractitionerNo(String practitionerNo);

	public Provider getProviderByPractitionerNo(String practitionerNoType, String practitionerNo);

	public Provider getProviderByPractitionerNo(String[] practitionerNoTypes, String practitionerNo);

	public List<String> getUniqueTeams();

	public List<Provider> getBillableProvidersOnTeam(Provider p);

	public List<Provider> getBillableProvidersByOHIPNo(String ohipNo);

	public List<Provider> getProvidersWithNonEmptyOhip(LoggedInInfo loggedInInfo);

	public List<Provider> getProvidersWithNonEmptyOhip();

	public List<Provider> getCurrentTeamProviders(String providerNo);

	public List<String> getActiveTeams();

	public List<String> getActiveTeamsViaSites(String providerNo);

	public List<Provider> getProviderByPatientId(Integer patientId);

	public List<Provider> getDoctorsWithNonEmptyCredentials();

	public List<Provider> getProvidersWithNonEmptyCredentials();

	public List<String> getProvidersInTeam(String teamName);

	public List<Object[]> getDistinctProviders();

	public List<String> getRecordsAddedAndUpdatedSinceTime(Date date);

	public List<Provider> searchProviderByNamesString(String searchString, int startIndex, int itemsToReturn);

	public List<Provider> search(String term, boolean active, int startIndex, int itemsToReturn);

	public List<String> getProviderNosWithAppointmentsOnDate(Date appointmentDate);

	public List<Provider> getOlisHicProviders();

	public Provider getProviderByPractitionerNoAndOlisType(String practitionerNo, String olisIdentifierType);

	public List<Provider> getOlisProvidersByPractitionerNo(List<String> practitionerNumbers);

	public List<Provider> getProvidersByIds(List<String> providerNumbers);

	public Map<String, String> getProviderNamesByIdsAsMap(List<String> providerNumbers);
}
