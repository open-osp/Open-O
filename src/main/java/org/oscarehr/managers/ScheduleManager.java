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

 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.GregorianCalendar;
 import java.util.List;
 import java.util.TreeMap;
 
 import javax.ws.rs.WebApplicationException;
 import javax.ws.rs.core.Response;
 import javax.ws.rs.core.Response.Status;
 
 import org.apache.commons.lang.time.DateUtils;
 import org.apache.logging.log4j.Logger;
 import org.oscarehr.common.dao.AppointmentArchiveDao;
 import org.oscarehr.common.dao.AppointmentStatusDao;
 import org.oscarehr.common.dao.AppointmentTypeDao;
 import org.oscarehr.common.dao.OscarAppointmentDao;
 import org.oscarehr.common.dao.ScheduleDateDao;
 import org.oscarehr.common.dao.ScheduleHolidayDao;
 import org.oscarehr.common.dao.ScheduleTemplateCodeDao;
 import org.oscarehr.common.dao.ScheduleTemplateDao;
 import org.oscarehr.common.model.Appointment;
 import org.oscarehr.common.model.AppointmentArchive;
 import org.oscarehr.common.model.AppointmentStatus;
 import org.oscarehr.common.model.AppointmentType;
 import org.oscarehr.common.model.ConsentType;
 import org.oscarehr.common.model.ScheduleDate;
 import org.oscarehr.common.model.ScheduleHoliday;
 import org.oscarehr.common.model.ScheduleTemplate;
 import org.oscarehr.common.model.ScheduleTemplateCode;
 import org.oscarehr.common.model.ScheduleTemplatePrimaryKey;
 import org.oscarehr.common.model.Security;
 import org.oscarehr.util.LoggedInInfo;
 import org.oscarehr.util.MiscUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 
 import oscar.log.LogAction;
 
 public interface ScheduleManager {

     /*Right now the date object passed is converted to a local time.  
     *
     * As in, if the server's timezone is set to EST and the method is called with two data objects set to
     *
     * 2011-11-11 2:01 TZ america/new york
     * 2011-11-10 23:01 TZ america/los angeles
     * 
     * They will both return the DayWorkSchedule for November 11 2011;
     * 
     * The DayWorkSchedule returned will be in the server's local timezone.
     *
     */
     public DayWorkSchedule getDayWorkSchedule(String providerNo, Calendar date);
 
     public List<Appointment> getDayAppointments(LoggedInInfo loggedInInfo, String providerNo, Date date);
 
     public List<Appointment> getDayAppointments(LoggedInInfo loggedInInfo, String providerNo, Calendar date);
     public List<ScheduleTemplateCode> getScheduleTemplateCodes();
 
     public List<AppointmentType> getAppointmentTypes();
 
     public void addAppointment(LoggedInInfo loggedInInfo, Security security, Appointment appointment);
     public List<Appointment> getAppointmentsForPatient(LoggedInInfo loggedInInfo, Integer demographicId, int startIndex, int itemsToReturn);
 
     public List<Appointment> getAppointmentsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn);
     public Appointment getAppointment(LoggedInInfo loggedInInfo, Integer appointmentId);
 
     public void updateAppointment(LoggedInInfo loggedInInfo, Appointment appointment);
 
     public List<Appointment> getAppointmentsForDateRangeAndProvider(LoggedInInfo loggedInInfo, Date startTime, Date endTime, String providerNo);
 
     public List<Appointment> getAppointmentUpdatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn);
 
     public List<Appointment> getAppointmentByDemographicIdUpdatedAfterDate(LoggedInInfo loggedInInfo, Integer demographicId, Date updatedAfterThisDateExclusive);
     public List<AppointmentArchive> getAppointmentArchiveUpdatedAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn);
 
     public List<AppointmentStatus> getAppointmentStatuses(LoggedInInfo loggedInInfo);
     
     public List<Integer> getAllDemographicIdByProgramProvider(LoggedInInfo loggedInInfo, Integer programId, String providerNo);
     public List<Object[]> listAppointmentsByPeriodProvider(LoggedInInfo loggedInInfo, Date sDate, Date eDate, String providers);
 
     public List<Object[]> listProviderAppointmentCounts(LoggedInInfo loggedInInfo, String sDateStr, String eDateStr);
     
     public  boolean removeIfDoubleBooked(LoggedInInfo loggedInInfo,Calendar startTime, Calendar endTime, String providerNo,  Appointment appointment);
 
 }
 