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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.AppointmentStatusDao;
import org.oscarehr.common.dao.LookupListDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.AppointmentArchive;
import org.oscarehr.common.model.AppointmentStatus;
import org.oscarehr.common.model.LookupList;
import org.oscarehr.common.model.LookupListItem;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

public interface AppointmentManager {

	public List<Appointment> getAppointmentHistoryWithoutDeleted(LoggedInInfo loggedInInfo, Integer demographicNo, Integer offset, Integer limit);

	public List<Object> getAppointmentHistoryWithDeleted(LoggedInInfo loggedInInfo, Integer demographicNo, Integer offset, Integer limit);

	/**
	 * Returns appointment for display.
	 * 
	
	 * @param appointment				appointment data
	 * @param loggedInInfo
	 * @return							appointment data
	 * 
	 */
	public void addAppointment(LoggedInInfo loggedInInfo, Appointment appointment);

	public void updateAppointment(LoggedInInfo loggedInInfo, Appointment appointment);

	public void deleteAppointment(LoggedInInfo loggedInInfo, int apptNo);

	public Appointment getAppointment(LoggedInInfo loggedInInfo, int apptNo);

	public Appointment updateAppointmentStatus(LoggedInInfo loggedInInfo, int apptNo, String status);

	
	public Appointment updateAppointmentType(LoggedInInfo loggedInInfo, int apptNo, String type);

	public Appointment updateAppointmentUrgency(LoggedInInfo loggedInInfo, int apptNo, String urgency);

    public List<AppointmentStatus> getAppointmentStatuses();

	public List<LookupListItem> getReasons();

	public List<Appointment> findMonthlyAppointments(LoggedInInfo loggedInInfo, String providerNo, int year, int month);

	public String getNextAppointmentDate(Integer demographicNo);
}
