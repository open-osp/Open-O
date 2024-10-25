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
package org.oscarehr.PMmodule.service;

import java.util.Date;
import java.util.List;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.PMmodule.exception.ClientAlreadyRestrictedException;

public interface ClientRestrictionManager {

    List<ProgramClientRestriction> getActiveRestrictionsForProgram(int programId, Date asOfDate);

    List<ProgramClientRestriction> getDisabledRestrictionsForProgram(Integer programId, Date asOfDate);

    List<ProgramClientRestriction> getActiveRestrictionsForClient(int demographicNo, Date asOfDate);

    List<ProgramClientRestriction> getActiveRestrictionsForClient(int demographicNo, int facilityId, Date asOfDate);

    List<ProgramClientRestriction> getDisabledRestrictionsForClient(int demographicNo, Date asOfDate);

    ProgramClientRestriction checkClientRestriction(int programId, int demographicNo, Date asOfDate);

    void saveClientRestriction(ProgramClientRestriction restriction) throws ClientAlreadyRestrictedException;

    void terminateEarly(int programClientRestrictionId, String providerNo);

    void disableClientRestriction(int restrictionId);

    void enableClientRestriction(Integer restrictionId);
}
