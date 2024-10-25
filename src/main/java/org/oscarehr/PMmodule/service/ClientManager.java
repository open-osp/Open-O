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
package org.oscarehr.PMmodule.service;

import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.ClientReferralDAO;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.AlreadyQueuedException;
import org.oscarehr.PMmodule.exception.ServiceRestrictionException;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.JointAdmissionDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.JointAdmission;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public interface ClientManager {

    boolean isOutsideOfDomainEnabled();

    Demographic getClientByDemographicNo(String demographicNo);

    List<Demographic> getClients();

    List<Demographic> search(ClientSearchFormBean criteria, boolean returnOptinsOnly,boolean excludeMerged);

    List<Demographic> search(ClientSearchFormBean criteria);

    List<ClientReferral> getReferrals();

    List<ClientReferral> getReferrals(String clientId);

    List<ClientReferral> getReferralsByFacility(Integer clientId, Integer facilityId);

    List<ClientReferral> getActiveReferrals(String clientId, String sourceFacilityId);

    ClientReferral getClientReferral(String id);

    void saveClientReferral(ClientReferral referral);

    void addClientReferralToProgramQueue(ClientReferral referral);

    List<ClientReferral> searchReferrals(ClientReferral referral);

    void saveJointAdmission(JointAdmission admission);

    List<JointAdmission> getDependents(Integer clientId);

    List<Integer> getDependentsList(Integer clientId);

    JointAdmission getJointAdmission(Integer clientId);

    boolean isClientDependentOfFamily(Integer clientId);

    boolean isClientFamilyHead(Integer clientId);

    void removeJointAdmission(Integer clientId, String providerNo);

    void removeJointAdmission(JointAdmission admission);

    void processReferral(ClientReferral referral) throws AlreadyAdmittedException, AlreadyQueuedException, ServiceRestrictionException;

    void processReferral(ClientReferral referral, boolean override) throws AlreadyAdmittedException, AlreadyQueuedException, ServiceRestrictionException;

    void saveClient(Demographic client);

    DemographicExt getDemographicExt(String id);

    List<DemographicExt> getDemographicExtByDemographicNo(int demographicNo);

    DemographicExt getDemographicExt(int demographicNo, String key);

    void updateDemographicExt(DemographicExt de);

    void saveDemographicExt(int demographicNo, String key, String value);

    void removeDemographicExt(String id);

    void removeDemographicExt(int demographicNo, String key);

    boolean checkHealthCardExists(String hin, String hcType);
}
