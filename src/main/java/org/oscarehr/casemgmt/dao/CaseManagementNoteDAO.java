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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.persistence.PersistenceException;

import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementSearchBean;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.EncounterUtil;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.SessionFactory;

import oscar.OscarProperties;
import oscar.util.SqlUtils;

public interface CaseManagementNoteDAO {

	public List<CaseManagementNote> findAll();

	public List<Provider> getEditors(CaseManagementNote note);

	public List<Provider> getAllEditors(String demographicNo);

	public List<CaseManagementNote> getHistory(CaseManagementNote note);

	public List<CaseManagementNote> getIssueHistory(String issueIds, String demoNo);

	public CaseManagementNote getNote(Long id);

	public List<CaseManagementNote> getNotes(List<Long> ids);

	public CaseManagementNote getMostRecentNote(String uuid);

	public List<CaseManagementNote> getNotesByUUID(String uuid);

	public List<CaseManagementNote> getCPPNotes(String demoNo, long issueId, String staleDate);

	public List<CaseManagementNote> getNotesByDemographic(String demographic_no, String[] issues, String staleDate);

	public List<CaseManagementNote> getNotesByDemographic(String demographic_no, String staleDate);

	public List<CaseManagementNote> getNotesByDemographic(String demographic_no);

	public List<CaseManagementNote> getNotesByDemographicSince(String demographic_no, Date date);

	public long getNotesCountByDemographicId(String demographic_no);

	public List<Object[]> getRawNoteInfoByDemographic(String demographic_no);

	public List<Map<String, Object>> getRawNoteInfoMapByDemographic(String demographic_no);

	public List<Map<String, Object>> getUnsignedRawNoteInfoMapByDemographic(String demographic_no);

	public List<CaseManagementNote> getNotesByDemographic(String demographic_no, Integer maxNotes);

	public List<CaseManagementNote> getActiveNotesByDemographic(String demographic_no, String[] issues);

	public List<CaseManagementNote> getNotesByDemographic(String demographic_no, String[] issueIds, Integer maxNotes);

	public List<CaseManagementNote> getNotesByDemographic(String demographic_no, String[] issueIds);

	public Collection<CaseManagementNote> findNotesByDemographicAndIssueCode(Integer demographic_no,
			String[] issueCodes);

	public Collection<CaseManagementNote> findNotesByDemographicAndIssueCodeInEyeform(Integer demographic_no,
			String[] issueCodes);

	public List<CaseManagementNote> getNotesByDemographicDateRange(String demographic_no, Date startDate, Date endDate);

	public List<CaseManagementNote> getNotesByDemographicLimit(String demographic_no, Integer offset,
			Integer numToReturn);

	public void updateNote(CaseManagementNote note);

	public void saveNote(CaseManagementNote note);

	public Object saveAndReturn(CaseManagementNote note);

	public List<CaseManagementNote> search(CaseManagementSearchBean searchBean);

	public List<Long> getAllNoteIds();

	public boolean haveIssue(Long issid, String demoNo);

	public boolean haveIssue(String issueCode, Integer demographicId);

	public int getNoteCountForProviderForDateRange(String providerNo, Date startDate, Date endDate);

	public int getNoteCountForProviderForDateRangeWithIssueId(String providerNo, Date startDate, Date endDate,
			String issueCode);

	public List<CaseManagementNote> searchDemographicNotes(String demographic_no, String searchString);

	public List<CaseManagementNote> getCaseManagementNoteByProgramIdAndObservationDate(Integer programId,
			Date minObservationDate, Date maxObservationDate);

	public List<CaseManagementNote> getMostRecentNotesByAppointmentNo(int appointmentNo);

	public List<CaseManagementNote> getMostRecentNotes(Integer demographicNo);

	public Long findMaxNoteId();

	public List<Integer> getNotesByFacilitySince(Date date, List<Program> programs);

	public static class EncounterCounts {
		public HashMap<EncounterUtil.EncounterType, Integer> uniqueCounts = new HashMap<EncounterUtil.EncounterType, Integer>();
		public HashMap<EncounterUtil.EncounterType, Integer> nonUniqueCounts = new HashMap<EncounterUtil.EncounterType, Integer>();
		public int totalUniqueCount = 0;

		public EncounterCounts() {
			// initialise with 0 values as 0 values won't show up in a select
			for (EncounterUtil.EncounterType tempType : EncounterUtil.EncounterType.values()) {
				uniqueCounts.put(tempType, 0);
				nonUniqueCounts.put(tempType, 0);
			}
		}
	}

	/**
	 * Get the count of demographic Id's based on the providerId and encounterType,
	 * 2 numbers will be provided, the unique count and the non unique count (which
	 * just represents the
	 * number of encounters in general) All encounter types are represented in the
	 * resulting hashMap, even ones with 0 counts.
	 *
	 * @param programId can be null at which point it's across the entire agency
	 */
	public static EncounterCounts getDemographicEncounterCountsByProgramAndRoleId(Integer programId, int roleId,
																				  Date startDate, Date endDate) {
		Connection c = null;
		try {
			EncounterCounts results = new EncounterCounts();
			c = DbConnectionFilter.getThreadLocalDbConnection();

			// get the numbers broken down by encounter types
			{
				String sqlCommand = "select encounter_type,count(demographic_no), count(distinct demographic_no) from casemgmt_note where reporter_caisi_role=? and observation_date>=? and observation_date<?"
						+ (programId == null ? "" : " and program_no=?") + " group by encounter_type";
				PreparedStatement ps = c.prepareStatement(sqlCommand);
				ps.setInt(1, roleId);
				ps.setTimestamp(2, new Timestamp(startDate.getTime()));
				ps.setTimestamp(3, new Timestamp(endDate.getTime()));
				if (programId != null)
					ps.setInt(4, programId);

				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					EncounterUtil.EncounterType encounterType = EncounterUtil
							.getEncounterTypeFromOldDbValue(rs.getString(1));
					results.nonUniqueCounts.put(encounterType, rs.getInt(2));
					results.uniqueCounts.put(encounterType, rs.getInt(3));
				}
			}

			// get the numbers in total, not broken down.
			{
				String sqlCommand = "select count(distinct demographic_no) from casemgmt_note where reporter_caisi_role=? and observation_date>=? and observation_date<?"
						+ (programId == null ? "" : " and program_no=?");
				PreparedStatement ps = c.prepareStatement(sqlCommand);
				ps.setInt(1, roleId);
				ps.setTimestamp(2, new Timestamp(startDate.getTime()));
				ps.setTimestamp(3, new Timestamp(endDate.getTime()));
				if (programId != null)
					ps.setInt(4, programId);

				ResultSet rs = ps.executeQuery();
				rs.next();

				results.totalUniqueCount = rs.getInt(1);
			}

			return (results);
		} catch (SQLException e) {
			throw (new PersistenceException(e));
		} finally {
			SqlUtils.closeResources(c, null, null);
		}
	}
}
