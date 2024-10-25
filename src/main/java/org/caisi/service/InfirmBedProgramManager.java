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
package org.caisi.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;
import org.caisi.dao.BedProgramDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProviderDefaultProgramDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProviderDefaultProgram;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public interface InfirmBedProgramManager {
    void setProviderDefaultProgramDao(ProviderDefaultProgramDao providerDefaultProgramDao);
    void setBedProgramDao(BedProgramDao dao);
    void setProgramDao(ProgramDao dao);
    void setDemographicDao(DemographicDao dao);
    List getPrgramNameID();
    List getPrgramName();
    List<LabelValueBean> getProgramBeans();
    List<LabelValueBean> getProgramBeans(String providerNo, Integer facilityId);
    List<LabelValueBean> getProgramBeansByFacilityId(Integer facilityId);
    List getProgramForApptViewBeans(String providerNo, Integer facilityId);
    List getDemographicByBedProgramIdBeans(int programId, Date dt, String archiveView);
    int getDefaultProgramId();
    int getDefaultProgramId(String providerNo);
    void setDefaultProgramId(String providerNo, int programId);
    Boolean getProviderSig(String providerNo);
    void toggleSig(String providerNo);
    ProgramProviderDAO getProgramProviderDAOT();
    void setProgramProviderDAOT(ProgramProviderDAO programProviderDAOT);
    String[] getProgramInformation(int programId);
}
