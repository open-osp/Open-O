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

package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Query;

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementType;
import org.springframework.stereotype.Repository;
import org.oscarehr.common.dao.MeasurementDaoImpl.SearchCriteria;

public interface MeasurementDao extends AbstractDao<Measurement> {

	public List<Measurement> findByDemographicIdUpdatedAfterDate(Integer demographicId, Date updatedAfterThisDate);

	public List<Measurement> findByCreateDate(Date updatedAfterThisDateExclusive, int itemsToReturn);

	public List<Integer> findDemographicIdsUpdatedAfterDate(Date updatedAfterThisDate);

	public List<Measurement> findMatching(Measurement measurement);

	public List<Measurement> findByDemographicNo(Integer demographicNo);

	public List<Measurement> findByDemographicNoAndType(Integer demographicNo, String type);

	public Measurement findLatestByDemographicNoAndType(int demographicNo, String type);

	public List<Measurement> findByAppointmentNo(Integer appointmentNo);

	public List<Measurement> findByAppointmentNo2(Integer appointmentNo);

	public List<Measurement> findByAppointmentNoAndType(Integer appointmentNo, String type);

	public Measurement findLatestByAppointmentNoAndType(int appointmentNo, String type);

	public List<Measurement> findByDemographicIdObservedDate(Integer demographicId, Date startDate, Date endDate);

	public List<Measurement> findByDemographicId(Integer demographicId);

	public List<Measurement> find(SearchCriteria criteria);

	public List<Measurement> findByIdTypeAndInstruction(Integer demographicId, String type, String instructions);

	public HashMap<String, Measurement> getMeasurements(Integer demographicNo, String[] types);

	public Set<Integer> getAppointmentNosByDemographicNoAndType(int demographicNo, String type, Date startDate,
			Date endDate);

	public HashMap<String, Measurement> getMeasurementsPriorToDate(Integer demographicNo, Date d);

	public List<Date> getDatesForMeasurements(Integer demographicNo, String[] types);

	public List<Object[]> findMeasurementsByDemographicIdAndLocationCode(Integer demoNo, String loincCode);

	public List<Object[]> findMeasurementsWithIdentifiersByDemographicIdAndLocationCode(Integer demoNo,
			String loincCode);

	public List<Object> findLabNumbers(Integer demoNo, String identCode);

	public List<Object> findLabNumbersOrderByObserved(Integer demoNo, String identCode);

	public Measurement findLastEntered(Integer demo, String type);

	public List<MeasurementType> findMeasurementsTypes(Integer demoNo);

	public List<Object[]> findMeasurementsAndProviders(Integer measurementId);

	public List<Object[]> findMeasurementsAndProvidersByType(String type, Integer demographicNo);

	public Object[] findMeasurementsAndProvidersByDemoAndType(Integer demographicNo, String type);

	public List<Measurement> findByValue(String key, String value);

	public List<Object> findObservationDatesByDemographicNoTypeAndMeasuringInstruction(Integer demo, String type,
			String mInstrc);

	public List<Date> findByDemographicNoTypeAndMeasuringInstruction(Integer demo, String type, String mInstrc);

	public Measurement findByDemographicNoTypeAndDate(Integer demo, String type, java.util.Date date);

	public List<Measurement> findByDemoNoTypeDateAndMeasuringInstruction(Integer demoNo, Date from, Date to,
			String type, String instruction);

	public List<Object[]> findLastEntered(Date from, Date to, String measurementType, String mInstrc);

	public List<Measurement> findByDemographicNoTypeAndDate(Integer demographicNo, Date createDate,
			String measurementType, String mInstrc);

	public List<Object[]> findByDemoNoDateTypeMeasuringInstrAndDataField(Integer demographicNo, Date dateEntered,
			String measurementType, String mInstrc, String upper, String lower);

	public List<Object[]> findLastEntered(Date from, Date to, String measurementType);

	public List<Measurement> findByDemoNoDateAndType(Integer demoNo, Date createDate, String type);

	public List<Object[]> findByDemoNoDateTypeAndDataField(Integer demographicNo, Date dateEntered, String type,
			String upper, String lower);

	public List<Object[]> findTypesAndMeasuringInstructionByDemographicId(Integer demoNo);

	public List<Object[]> findByCreateDate(Date from, Date to);

	public List<Measurement> findByType(Integer demographicId, String type);

	public List<Measurement> findByType(Integer demographicId, String type, Date after);

	public List<Measurement> findByType(Integer demographicId, List<String> types);

	public List<Measurement> findByType(Integer demographicId, List<String> types, Date after);

	public List<Measurement> findByType(String type);

	public List<Measurement> findByType(List<String> types);

	public List<Integer> findDemographicIdsByType(List<String> types);

	public List<Measurement> findByTypeBefore(Integer demographicId, String type, Date date);

	public List<Measurement> findByProviderDemographicLastUpdateDate(String providerNo, Integer demographicId,
			Date updatedAfterThisDateExclusive, int itemsToReturn);

	public List<Measurement> findByDemographicLastUpdateAfterDate(Integer demographicId,
			Date updatedAfterThisDateExclusive);

	public List<Measurement> findLatestByDemographicObservedAfterDate(Integer demographicId,
			Date observedAfterDateExclusive);

	public List<Integer> findNewMeasurementsSinceDemoKey(String keyName);

	public List<Measurement> findMeasurementByTypeAndDate(Integer demoNo, String type, Date start, Date end);
}
