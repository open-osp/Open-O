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

 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.List;
 
 import org.oscarehr.PMmodule.model.ProgramClientRestriction;
 import org.oscarehr.common.dao.DemographicDao;
 import org.springframework.beans.factory.annotation.Required;
 import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
 
 /**
  */
 public interface ProgramClientRestrictionDAO {

     public Collection<ProgramClientRestriction> find(int programId, int demographicNo);
 
     public void save(ProgramClientRestriction restriction);
 
     public ProgramClientRestriction find(int restrictionId);
     public Collection<ProgramClientRestriction> findForProgram(int programId);
 
     public Collection<ProgramClientRestriction> findDisabledForProgram(int programId);
     public Collection<ProgramClientRestriction> findForClient(int demographicNo);
 
     public Collection<ProgramClientRestriction> findForClient(int demographicNo, int facilityId);
 
     public Collection<ProgramClientRestriction> findDisabledForClient(int demographicNo);
     public void setDemographicDao(DemographicDao demographicDao);
     public void setProgramDao(ProgramDao programDao);
     public void setProviderDao(ProviderDao providerDao);
 
 }
 