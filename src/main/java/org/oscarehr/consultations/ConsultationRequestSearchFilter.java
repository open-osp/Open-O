/**
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
 */
package org.oscarehr.consultations;

import java.util.Date;

public class ConsultationRequestSearchFilter {

	public enum SORTMODE {
        Demographic, Service, Consultant, Team, Status, MRP, AppointmentDate, FollowUpDate ,ReferralDate, Urgency
	}
	
	public enum SORTDIR {
        asc,desc
	}

	/**
	 * Represents the various statuses that a consultation request can have.
	 * This enum is used to track and manage the state of a consultation request throughout its lifecycle.
	 *
	 * Each status is assigned an ordinal value which can be used to represent the status
	 * radio button selection in a consultation request form.
	 *
	 * The incomplete status is used as an indicator to fetch all statuses except complete.
	 * There is no actual incomplete status in the form.
	 */
	public enum STATUS {
		Nothing(1), PendingSpecialist(2), PendingPatient(3), Completed(4), Incomplete(5);

		private final int value;

		STATUS(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static STATUS fromValue(int value) {
			for (STATUS s : values()) {
				if (s.value == value) {
					return s;
				}
			}
			return null;
		}
	}

	private Integer id;
	private Date referralStartDate;
	private Date referralEndDate;
	private STATUS status;
	private String team;
	private Date appointmentStartDate;
	private Date appointmentEndDate;
	private Integer demographicNo;
	private Integer mrpNo;
	private String urgency;
	private SORTMODE sortMode = SORTMODE.ReferralDate;
	private SORTDIR sortDir = SORTDIR.desc;
	private int startIndex;
	private int numToReturn;

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getReferralStartDate() {
		return referralStartDate;
	}
	public void setReferralStartDate(Date referralStartDate) {
		this.referralStartDate = referralStartDate;
	}
	public Date getReferralEndDate() {
		return referralEndDate;
	}
	public void setReferralEndDate(Date referralEndDate) {
		this.referralEndDate = referralEndDate;
	}
	public STATUS getStatus() {
		return status;
	}
	/*
	 * Default status is all status'
	 */
	public void setStatus(STATUS status) {
		this.status = status;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public Date getAppointmentStartDate() {
		return appointmentStartDate;
	}
	public void setAppointmentStartDate(Date appointmentStartDate) {
		this.appointmentStartDate = appointmentStartDate;
	}
	public Date getAppointmentEndDate() {
		return appointmentEndDate;
	}
	public void setAppointmentEndDate(Date appointmentEndDate) {
		this.appointmentEndDate = appointmentEndDate;
	}
	public Integer getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	public Integer getMrpNo() {
		return mrpNo;
	}
	public void setMrpNo(Integer mrpNo) {
		this.mrpNo = mrpNo;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public SORTMODE getSortMode() {
		return sortMode;
	}
	public void setSortMode(SORTMODE sortMode) {
		this.sortMode = sortMode;
	}
	public SORTDIR getSortDir() {
		return sortDir;
	}
	public void setSortDir(SORTDIR sortDir) {
		this.sortDir = sortDir;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getNumToReturn() {
		return numToReturn;
	}
	public void setNumToReturn(int numToReturn) {
		this.numToReturn = numToReturn;
	}


	
}
