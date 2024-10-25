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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ReportStatistic;
import org.oscarehr.util.AccumulatorMap;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import oscar.util.SqlUtils;
public interface GenericIntakeDAO {

    List<Object[]> getOcanIntakesAfterDate(Calendar after);

    Intake getLatestIntakeByFacility(IntakeNode node, Integer clientId, Integer programId, Integer facilityId);

    Intake getLatestIntake(IntakeNode node, Integer clientId, Integer programId, Integer facilityId);

    List<Integer> getIntakeClientsByFacilityId(Integer facilityId);

    List<Integer> getIntakeFacilityIds();

    List<Intake> getIntakes(IntakeNode node, Integer clientId, Integer programId, Integer facilityId);

    List<Intake> getIntakesByType(Integer formType, Integer clientId, Integer programId, Integer facilityId);

    List<Intake> getIntakesByFacility(IntakeNode node, Integer clientId, Integer programId, Integer facilityId);

    List<Intake> getRegIntakes(List<IntakeNode> nodes, Integer clientId, Integer programId, Integer facilityId);

    Intake getIntakeById(IntakeNode node, Integer intakeId, Integer programId, Integer facilityId);

    Integer getIntakeNodeIdByIntakeId(Integer intakeId);

    List<Integer> getIntakeNodesIdByClientId(Integer clientId);

    List<Integer> getIntakeNodesIdByClientId(Integer clientId, Integer formType);

    Integer saveIntake(Intake intake);

    void saveUpdateIntake(Intake intake);

    List<Integer> getIntakeIds2(Integer nodeId, Date startDate, Date endDate) throws SQLException;

    SortedSet<Integer> getLatestIntakeIds(Integer nodeId, Date startDate, Date endDate);

    SortedMap<Integer, SortedMap<String, ReportStatistic>> getReportStatistics(Hashtable<Integer, Integer> answerIds, Set<Integer> intakeIds);

    //GenericIntakeReportStatistics getReportStatistics2(List<Integer> intakeIds, Set<Integer> answerIds) throws SQLException;

    List<IntakeNode> getIntakeNodesByType(Integer formType);

    public static class GenericIntakeReportStatistics {
        public int totalIntakeCount = 0;
        /**
         * This is a map of <intake_node_id, <intake_answer.val, count>>
         */
        public HashMap<Integer, AccumulatorMap<String>> intakeNodeResults = new HashMap<Integer, AccumulatorMap<String>>();

        public void addResult(int intakeNodeId, String answer) {
            AccumulatorMap<String> accumulatorMap = intakeNodeResults.get(intakeNodeId);

            if (accumulatorMap == null) {
                accumulatorMap = new AccumulatorMap<String>();
                intakeNodeResults.put(intakeNodeId, accumulatorMap);
            }

            accumulatorMap.increment(answer);
        }
    }
}
