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

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BedDemographicDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.RoomDao;
import org.oscarehr.common.dao.RoomDemographicDao;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.Room;
import org.oscarehr.common.model.RoomDemographic;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface RoomDemographicManager {

	
	public boolean roomDemographicExists(Integer roomId);

	public int getRoomOccupanyByRoom(Integer roomId);
	

	public List<RoomDemographic> getRoomDemographicByRoom(Integer roomId);

	public RoomDemographic getRoomDemographicByDemographic(Integer demographicNo, Integer facilityId);


	public void saveRoomDemographic(RoomDemographic roomDemographic);

	public void cleanUpBedTables(RoomDemographic roomDemographic);
	
	
	public void deleteRoomDemographic(RoomDemographic roomDemographic);

	public void setAttributes(RoomDemographic roomDemographic);

	public void validate(RoomDemographic roomDemographic);

	public void validateRoomDemographic(RoomDemographic roomDemographic);

	public void validateProvider(String providerId);

	public void validateRoom(Integer roomId);

	public void validateDemographic(Integer demographicNo);
}
