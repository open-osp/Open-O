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

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.BedDao;
import org.oscarehr.common.dao.BedDemographicDao;
import org.oscarehr.common.dao.BedDemographicHistoricalDao;
import org.oscarehr.common.dao.BedDemographicStatusDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.RoomDao;
import org.oscarehr.common.model.Bed;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.BedDemographicHistorical;
import org.oscarehr.common.model.BedDemographicHistoricalPK;
import org.oscarehr.common.model.BedDemographicStatus;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Room;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface BedDemographicManager {
	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#demographicExists(java.lang.Integer)
	 */
	public boolean demographicExists(Integer bedId);

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getBedDemographicByBed(java.lang.Integer)
	 */
	public BedDemographic getBedDemographicByBed(Integer bedId);

	public BedDemographic getBedDemographicByDemographic(Integer demographicNo, Integer facilityId);

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getDefaultBedDemographicStatus()
	 */
	public BedDemographicStatus getDefaultBedDemographicStatus();

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getBedDemographicStatuses()
	 */
	public BedDemographicStatus[] getBedDemographicStatuses();

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getExpiredReservations()
	 */
	public BedDemographicHistorical[] getExpiredReservations();

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#saveBedDemographic(org.oscarehr.PMmodule.model.BedDemographic)
	 */
	public void saveBedDemographic(BedDemographic bedDemographic);

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#deleteBedDemographic(BedDemographic)
	 */
	public void deleteBedDemographic(BedDemographic bedDemographic);
	public void setAttributes(BedDemographic bedDemographic);

	public void validate(BedDemographic bedDemographic);
	public void validateBedDemographic(BedDemographic bedDemographic);

	public void validateBedDemographicStatus(Integer bedDemographicStatusId);
	public void validateProvider(String providerId);

	public void validateBed(Integer bedId);

	public void validateDemographic(Integer demographicNo);

}
