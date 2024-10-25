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

 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.Hashtable;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Set;
 import java.util.SortedMap;
 import java.util.SortedSet;
 import java.util.TreeSet;
 
 import org.apache.logging.log4j.Logger;
 import org.oscarehr.PMmodule.dao.GenericIntakeDAO;
 import org.oscarehr.PMmodule.dao.GenericIntakeDAOImpl;
 import org.oscarehr.PMmodule.dao.GenericIntakeNodeDAO;
 import org.oscarehr.PMmodule.dao.ProgramDao;
 import org.oscarehr.PMmodule.model.Agency;
 import org.oscarehr.PMmodule.model.Intake;
 import org.oscarehr.PMmodule.model.IntakeAnswer;
 import org.oscarehr.PMmodule.model.IntakeAnswerElement;
 import org.oscarehr.PMmodule.model.IntakeNode;
 import org.oscarehr.PMmodule.model.IntakeNodeJavascript;
 import org.oscarehr.PMmodule.model.IntakeNodeLabel;
 import org.oscarehr.PMmodule.model.IntakeNodeTemplate;
 import org.oscarehr.PMmodule.model.Program;
 import org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter;
 import org.oscarehr.PMmodule.web.adapter.ocan.OcanXmlAdapterFactory;
 import org.oscarehr.common.dao.AdmissionDao;
 import org.oscarehr.common.model.Admission;
 import org.oscarehr.common.model.ReportStatistic;
 import org.oscarehr.util.MiscUtils;
 import org.springframework.transaction.annotation.Transactional;
 
 public interface GenericIntakeManager {
     public void setGenericIntakeNodeDAO(
             GenericIntakeNodeDAO genericIntakeNodeDAO);
 
     public void setGenericIntakeDAO(GenericIntakeDAO genericIntakeDAO);
 
     public void setProgramDao(ProgramDao programDao);
 
     public void setAdmissionDao(AdmissionDao admissionDao);
 
     // Copy
 
     public Intake copyQuickIntake(Integer clientId, String staffId,
             Integer facilityId);
 
     public Intake copyRegIntake(Integer clientId, String staffId,
             Integer facilityId);
 
     public Intake copyIndepthIntake(Integer clientId, String staffId,
             Integer facilityId);
 
     public Intake copyProgramIntake(Integer clientId, Integer programId,
             String staffId, Integer facilityId);
 
     // Create
 
     public Intake createQuickIntake(String providerNo);
 
     public Intake createIndepthIntake(String providerNo);
 
     public Intake createIntake(IntakeNode node, String providerNo);
 
     public Intake createProgramIntake(Integer programId, String providerNo);
 
     // Get
 
     public Intake getMostRecentQuickIntakeByFacility(Integer clientId,
             Integer facilityId);
 
     public Intake getMostRecentQuickIntake(Integer clientId, Integer facilityId);
 
     public Intake getRegIntakeById(Integer intakeId, Integer facilityId);
     public Intake getMostRecentIndepthIntake(Integer clientId,
             Integer facilityId);
     public Intake getMostRecentProgramIntake(Integer clientId,
             Integer programId, Integer facilityId);
     public List<Intake> getQuickIntakes(Integer clientId, Integer facilityId);
 
     public List<Intake> getRegIntakes(Integer clientId, Integer facilityId);
 
     public List<Integer> getIntakeClientsByFacilityId(Integer facilityId);
     public List<Integer> getIntakeFacilityIds();
 
     public List<Intake> getIndepthIntakes(Integer clientId, Integer facilityId);
 
     public List<Intake> getProgramIntakes(Integer clientId, Integer facilityId);
 
     public List<Intake> getIntakesByType(Integer clientId, Integer facilityId, Integer formType);
 
     public List<Program> getProgramsWithIntake(Integer clientId);
 
     // Save
 
     public Integer saveIntake(Intake intake);
 
     // Save Or Update
 
     public void saveUpdateIntake(Intake intake);
 
     // Report
 
     public GenericIntakeDAOImpl.GenericIntakeReportStatistics getQuestionStatistics2(
             String intakeType, Integer programId, Date startDate, Date endDate)
             throws SQLException;
 
     public Map<String, SortedSet<ReportStatistic>> getQuestionStatistics(
             String nodeId, String intakeType, Integer programId, Date startDate, Date endDate,
             boolean includePast) throws SQLException;
 
     public Intake copyIntakeWithId(IntakeNode node, Integer clientId,
             Integer programId, String staffId, Integer facilityId);
 
     public List<IntakeNode> getIntakeNodes();
 
     public void saveNodeLabel(IntakeNodeLabel intakeNodeLabel);
 
     public void saveIntakeNode(IntakeNode intakeNode);
     public void deleteIntakeForm(IntakeNode intakeNode);
     public void updateIntakeNode(IntakeNode intakeNode);
     public void saveIntakeNodeTemplate(IntakeNodeTemplate intakeNodeTemplate);
 
     public void saveIntakeAnswerElement(IntakeAnswerElement intakeAnswerElement);
     public void updateNodeLabel(IntakeNodeLabel intakeNodeLabel);
 
     public void updateAgencyIntakeQuick(Integer intakeNodeId);
     public IntakeNodeLabel getIntakeNodeLabel(Integer intakeNodeLabelId);
 
     public IntakeNodeTemplate getIntakeNodeTemplate(Integer intakeNodeTemplateId);
     public IntakeNode getIntakeNode(Integer nodeId);
 
     public List<IntakeNode> getIntakeNodesByType(Integer formType);
 
     public List<Map<String, String>> getIntakeListforOcan(Calendar after);
     /*
      * street health report, not finished yet public List getCohort(Date
      * beginDate, Date endDate) { return genericIntakeDAO.getCohort(beginDate,
      * endDate, demographicDao.getClients()); }
      */
 
 
     public List<IntakeNodeJavascript> getIntakeNodeJavascriptLocation(String questionId);
 }
 