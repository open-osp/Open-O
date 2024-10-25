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

package org.oscarehr.casemgmt.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.caisi_integrator.ws.CodeType;
import org.oscarehr.caisi_integrator.ws.FacilityIdDemographicIssueCompositePk;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

public interface CaseManagementIssueDAO {

    public List<CaseManagementIssue> getIssuesByDemographic(String demographic_no);

    public List<CaseManagementIssue> getIssuesByDemographicOrderActive(Integer demographic_no, Boolean resolved);

    public List<CaseManagementIssue> getIssuesByNote(Integer noteId, Boolean resolved);

    public Issue getIssueByCmnId(Integer cmnIssueId);

    public CaseManagementIssue getIssuebyId(String demo, String id);

    public CaseManagementIssue getIssuebyIssueCode(String demo, String issueCode);

    public void deleteIssueById(CaseManagementIssue issue);

    public void saveAndUpdateCaseIssues(List<CaseManagementIssue> issuelist);

    public void saveIssue(CaseManagementIssue issue);

    public List<CaseManagementIssue> getAllCertainIssues();

    public List<Integer> getIssuesByProgramsSince(Date date, List<Program> programs);

    public List<CaseManagementIssue> getIssuesByDemographicSince(String demographic_no, Date date);

    public List<FacilityIdDemographicIssueCompositePk> getIssueIdsForIntegrator(Integer facilityId,
            Integer demographicNo);
}
