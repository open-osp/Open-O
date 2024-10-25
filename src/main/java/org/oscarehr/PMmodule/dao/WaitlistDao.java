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
package org.oscarehr.PMmodule.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.oscarehr.PMmodule.model.Vacancy;
import org.oscarehr.PMmodule.service.VacancyTemplateManager;
import org.oscarehr.PMmodule.wlmatch.CriteriaBO;
import org.oscarehr.PMmodule.wlmatch.CriteriasBO;
import org.oscarehr.PMmodule.wlmatch.MatchBO;
import org.oscarehr.PMmodule.wlmatch.VacancyDisplayBO;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.match.client.ClientData;
import org.oscarehr.match.vacancy.VacancyData;
import org.oscarehr.match.vacancy.VacancyTemplateData;
import org.springframework.stereotype.Repository;

public interface WaitlistDao {

	public List<MatchBO> getClientMatches(int vacancyId);

	public List<MatchBO> getClientMatchesWithMinPercentage(int vacancyId, double percentage);

	public Collection<EFormData> searchForMatchingEforms(CriteriasBO crits);

	public List<VacancyDisplayBO> listDisplayVacanciesForWaitListProgram(int programID);

	public List<VacancyDisplayBO> listDisplayVacanciesForAllWaitListPrograms();

	public List<VacancyDisplayBO> getDisplayVacanciesForAgencyProgram(int programID);

	public VacancyDisplayBO getDisplayVacancy(int vacancyID);

	public void loadStats(VacancyDisplayBO bo);

	public Integer getProgramIdByVacancyId(int vacancyId);

	public List<VacancyDisplayBO> listNoOfVacanciesForWaitListProgram();

	public List<VacancyDisplayBO> listVacanciesForWaitListProgram();

	public List<ClientData> getAllClientsData();

	public List<ClientData> getAllClientsDataByProgramId(int wlProgramId);

	public ClientData getClientData(int clientId);

	// private static final String field_type_one = "select_one";
	// private static final String field_type_number = "number";

	public VacancyData loadVacancyData(final int vacancyId);

	public VacancyData loadVacancyData(final int vacancyId, final int wlProgramId);

}
