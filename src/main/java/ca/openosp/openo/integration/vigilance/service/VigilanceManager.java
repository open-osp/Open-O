/**
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package ca.openosp.openo.integration.vigilance.service;

import ca.openosp.openo.integration.vigilance.model.VigilanceAnalysisResult;
import ca.openosp.openo.integration.vigilance.model.VigilanceStatusResult;
import ca.openosp.openo.prescript.data.RxPrescriptionData;
import ca.openosp.openo.utility.LoggedInInfo;

import java.util.List;

/**
 * Manager for Vigilance drug analysis operations.
 * Orchestrates status checks and drug interaction analysis, applying OpenO-specific
 * business rules such as provider warning-level preferences.
 */
public interface VigilanceManager {

    /**
     * Checks the operational status of the Vigilance service.
     * Returns null if allergy/interaction warnings are disabled via {@code rx3.disable_allergy_warnings}.
     *
     * @return status result, or null if disabled
     */
    VigilanceStatusResult getStatus();

    /**
     * Performs interaction analysis for a patient's prescription stash.
     * Applies provider warning-level preferences to determine alert visibility.
     * Returns null if allergy/interaction warnings are disabled via {@code rx3.disable_allergy_warnings}.
     *
     * @param loggedInInfo  current user session info
     * @param demographicNo patient identifier
     * @param stash         list of staged prescriptions from RxSessionBean
     * @return analysis result, or null if disabled
     */
    VigilanceAnalysisResult analyzeDrugsInteraction(LoggedInInfo loggedInInfo, int demographicNo, List<RxPrescriptionData.Prescription> stash);
}
