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
 import java.util.ArrayList;
 import java.util.HashSet;
 import java.util.List;
 import java.util.Set;
 import java.util.TreeSet;
 
 import org.apache.logging.log4j.Logger;
 import org.hibernate.ObjectNotFoundException;
 import org.oscarehr.PMmodule.model.Agency;
 import org.oscarehr.PMmodule.model.IntakeAnswerElement;
 import org.oscarehr.PMmodule.model.IntakeNode;
 import org.oscarehr.PMmodule.model.IntakeNodeJavascript;
 import org.oscarehr.PMmodule.model.IntakeNodeLabel;
 import org.oscarehr.PMmodule.model.IntakeNodeTemplate;
 import org.oscarehr.util.DbConnectionFilter;
 import org.oscarehr.util.MiscUtils;
 import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
 
 /**
  * Hibernate implementation of GenericIntakeNodeDAO interface
  */
 public interface GenericIntakeNodeDAO{

     public IntakeNode getIntakeNode(Integer intakeNodeId);
 
     /**
      *Returns a list of Intake Nodes of type "intake".
      */
     public List<IntakeNode> getIntakeNodes();
     /**
      *Returns a list of Intake Nodes of type "intake".
      */
     public List<IntakeNode> getPublishedIntakeNodesByName(String name);
 
     public List<IntakeNode> getIntakeNodeByEqToId(Integer iNodeEqToId) throws SQLException;;
 
     public Set<Integer> getIntakeNodeIdByEqToId(Integer iNodeEqToId) throws SQLException;;
 
     public Set<Integer> getEqToIdByIntakeNodeId(Integer intakeNodeId) throws SQLException;;
 
     public List<IntakeNode> getIntakeNodeByEqToId(Set<IntakeNode> iNodes);
 
     public void saveNodeLabel(IntakeNodeLabel intakeNodeLabel);
 
     public void updateIntakeNode(IntakeNode intakeNode);
 
     public void updateNodeLabel(IntakeNodeLabel intakeNodeLabel);
 
     public void updateAgencyIntakeQuick(Agency agency);
 
     public IntakeNodeLabel getIntakeNodeLabel(Integer intakeNodeLabelId);
     public IntakeNodeTemplate getIntakeNodeTemplate(Integer intakeNodeTemplateId);
 
     public void saveIntakeNode(IntakeNode intakeNode);
 
     public void saveIntakeNodeTemplate(IntakeNodeTemplate intakeNodeTemplate);
     public void saveIntakeAnswerElement(IntakeAnswerElement intakeAnswerElement);
 
     public void deleteIntakeNode(IntakeNode intakeNode);
 
     public List<IntakeNodeJavascript> getIntakeNodeJavascriptLocation(String questionId);
 }
 