/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (C) 2007  Heart & Stroke Foundation
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
package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;
import org.oscarehr.common.model.Hsfo2Visit;

public interface Hsfo2VisitDao extends AbstractDao<Hsfo2Visit> {
    Hsfo2Visit getHsfoVisitById(int id);
    List<Hsfo2Visit> getHsfoVisitByDemographicNo(Integer demographic_no);
    List<Hsfo2Visit> getLockedVisitByDemographicNo(String demographic_no);
    List<Hsfo2Visit> getVisitRecordByPatientId(String patientId);
    Hsfo2Visit getPatientLatestVisitRecordByVisitDate(Date visitdate, String demographic_no);
    List<Hsfo2Visit> getVisitRecordInDateRangeByDemographicNo(String patientId, String startDate, String endDate);
    Hsfo2Visit getFirstVisitRecordForThePatient(String patientId);
    Hsfo2Visit getPatientBaseLineVisitData(String patientId);
    int getMaxVisitId();
}
