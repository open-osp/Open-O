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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.AppointmentSearchDao;
import org.oscarehr.common.dao.AppointmentStatusDao;
import org.oscarehr.common.dao.LookupListDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.AppointmentSearch;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.appointment.search.SearchConfig;
import org.oscarehr.appointment.search.TimeSlot;
import org.oscarehr.appointment.search.AppointmentType;
import org.oscarehr.appointment.search.FilterDefinition;
import org.oscarehr.appointment.search.Provider;
import org.oscarehr.appointment.search.filters.AvailableTimeSlotFilter;
import org.oscarehr.util.LoggedInInfo;
//import org.oscarehr.oscar_clinic_component.manager.BookingLearningManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.XmlUtils;
import org.oscarehr.ws.transfer_objects.CalendarScheduleCodePairTransfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

public interface AppointmentSearchManager {
	//Right now these two methods return the same but in the future this could be customized based on the demographic 
	public List<AppointmentType> getAppointmentTypes(SearchConfig config,Integer demographicNo);
	
	public List<AppointmentType> getAppointmentTypes(SearchConfig config,String providerNo);
	
	public SearchConfig getProviderSearchConfig(String providerNo);
	
	public List<TimeSlot> findAppointment(LoggedInInfo loggedInInfo,SearchConfig config, Integer demographicNo,Long appointmentTypeId,Calendar startDate) throws java.lang.ClassNotFoundException,java.lang.InstantiationException,java.lang.IllegalAccessException;

	public static List<TimeSlot> getAllowedTimesByType(DayWorkSchedule dayWorkSchedule, Character[] codes, String providerNo){
		ArrayList<TimeSlot> allowedTimesFilteredByType = new ArrayList<TimeSlot>();
		for (CalendarScheduleCodePairTransfer entry : CalendarScheduleCodePairTransfer.toTransfer(dayWorkSchedule.getTimeSlots())  ){
			char c = entry.getScheduleCode();
			if (Arrays.binarySearch(codes, c) >= 0){
				allowedTimesFilteredByType.add(new TimeSlot(providerNo, null, entry.getDate(), c));
			}
		}
		return allowedTimesFilteredByType;
	}
}
