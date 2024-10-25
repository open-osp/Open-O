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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.AbstractDaoImpl;
import org.oscarehr.common.dao.OscarJobDao;
import org.oscarehr.common.dao.OscarJobTypeDao;
import org.oscarehr.common.jobs.OscarJobUtils;
import org.oscarehr.common.model.OscarJob;
import org.oscarehr.common.model.OscarJobType;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

public interface OscarJobManager{

	
	public void saveJob(LoggedInInfo loggedInInfo, OscarJob oscarJob);
	
	public void updateJob(LoggedInInfo loggedInInfo, OscarJob oscarJob);
	
	/*
	 * Make sure it's a valid class, and that it implements OscarRunnable
	 */
	public List<OscarJobType> getCurrentlyAvaliableJobTypes();
	
	public List<OscarJobType> getAllJobTypes();
	
	public List<OscarJob> getAllJobs(LoggedInInfo loggedInInfo);
	public List<OscarJob> getJobByName(LoggedInInfo loggedInInfo,String name);
	
	public OscarJob getJob(LoggedInInfo loggedInInfo, Integer id);
	public OscarJobType addIfNotLoaded(LoggedInInfo loggedInInfo,OscarJobType jobType);
	
	public OscarJobType getJobType(LoggedInInfo loggedInInfo, Integer id);
	
	public void saveJobType(LoggedInInfo loggedInInfo, OscarJobType oscarJob);
	public void updateJobType(LoggedInInfo loggedInInfo, OscarJobType oscarJob);

	public static OscarJobType getFTPSJob() {
		OscarJobType oscarJobType = new OscarJobType();
		oscarJobType.setDescription("Common FTPS Job type");
		oscarJobType.setClassName("org.oscarehr.integration.surveillance.FTPSJob");
		oscarJobType.setEnabled(true);
		oscarJobType.setName("FTPS");
		oscarJobType.setUpdated(new Date());
		return oscarJobType;
	}
}
