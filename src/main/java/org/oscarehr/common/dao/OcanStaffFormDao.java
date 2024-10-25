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
package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;
import org.oscarehr.common.model.OcanStaffForm;

public interface OcanStaffFormDao extends AbstractDao<OcanStaffForm> {

	OcanStaffForm findLatestCompletedInitialOcan(Integer facilityId, Integer clientId);
	Object[] findLatestCompletedInitialOcan_startDates(Integer facilityId, Integer clientId);
	OcanStaffForm findLatestCompletedReassessment(Integer facilityId, Integer clientId);
	Object[] findLatestCompletedReassessment_startDates(Integer facilityId, Integer clientId);
	OcanStaffForm findLatestCompletedDischargedAssessment(Integer facilityId, Integer clientId);
	OcanStaffForm findLatestByFacilityClient(Integer facilityId, Integer clientId, String ocanType);
	OcanStaffForm findByProviderAndSubmissionId(String providerNo, Integer submissionId, String type );
	OcanStaffForm findLatestCbiFormsByFacilityAdmissionId(Integer facilityId, Integer admissionId, Boolean signed);
	List<OcanStaffForm> getLatestCbiFormsByGroupOfAdmissionId();
	List<Integer> findClientsWithOcan(Integer facilityId);
	OcanStaffForm getLastCompletedOcanForm(Integer facilityId, Integer clientId);
	OcanStaffForm getLastCompletedOcanFormByOcanType(Integer facilityId, Integer clientId, String ocanType);
	List<OcanStaffForm> findByFacilityClient(Integer facilityId, Integer clientId, String ocanType);
	OcanStaffForm findOcanStaffFormById(Integer ocanStaffFormId);
	List<OcanStaffForm> findLatestSignedOcanForms(Integer facilityId, String formVersion, Date startDate, Date endDate,String ocanType);
	List<OcanStaffForm> findLatestSignedOcanForms(Integer facilityId, Integer clientId);
	List<OcanStaffForm> findUnsubmittedOcanFormsByOcanType(Integer facilityId, String ocanType,String assessmentid);
	List<OcanStaffForm> findUnsubmittedOcanForms(Integer facilityId);
	List<OcanStaffForm> findSubmittedOcanFormsByAssessmentId(Integer assessmentId);
	List<OcanStaffForm> findAllByFacility(Integer facilityId);
	List<OcanStaffForm> findBySubmissionId(Integer facilityId,Integer submissionId);
	OcanStaffForm findLatestByAssessmentId(Integer facilityId,Integer assessmentId);
	List<Integer> getAllOcanClients(Integer facilityId);
	List<OcanStaffForm> findLatestOcanFormsByStaff(Integer facilityId, String providerNo);
	List<OcanStaffForm> findLatestByConsumer(Integer facilityId, Integer consumerId);
}
