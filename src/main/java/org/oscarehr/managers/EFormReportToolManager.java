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
package org.oscarehr.managers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormReportToolDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormReportTool;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 
 * @author Marc Dumontier
 *
 */
public interface EFormReportToolManager{

	
	//@PersistenceContext
	//protected EntityManager entityManager = null;

	
	public List<EFormReportTool> findAll(LoggedInInfo loggedInInfo, Integer offset, Integer limit);
	/*
	 * Updates the eft_latest column to 1 for the latest form from each demographic. This is calculated by latest form date/form time, and in the 
	 * case that there's 2 results, the highest fdid will be marked. 
	 */
	public void markLatest(LoggedInInfo loggedInInfo, Integer eformReportToolId);
	public void addNew(LoggedInInfo loggedInInfo, EFormReportTool eformReportTool);

	public void populateReportTable(LoggedInInfo loggedInInfo, Integer eformReportToolId);

	public void deleteAllData(LoggedInInfo loggedInInfo, Integer eformReportToolId);
	public void remove(LoggedInInfo loggedInInfo, Integer eformReportToolId);
	
	public int getNumRecords(LoggedInInfo loggedInInfo,Integer eformReportToolId);
	public Integer getNumRecords(LoggedInInfo loggedInInfo, EFormReportTool eformReportTool);
}
