//CHECKSTYLE:OFF
/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 * <p>
 * Modifications made by Magenta Health in 2024.
 */
package ca.openosp.openo.commn.dao;

import java.util.List;

import ca.openosp.openo.commn.model.FlowSheetCustomization;

public interface FlowSheetCustomizationDao extends AbstractDao<FlowSheetCustomization> {
    FlowSheetCustomization getFlowSheetCustomization(Integer id);

    List<FlowSheetCustomization> getFlowSheetCustomizations(String flowsheet, String provider, Integer demographic);

    List<FlowSheetCustomization> getFlowSheetCustomizations(String flowsheet, String provider);

    List<FlowSheetCustomization> getFlowSheetCustomizations(String flowsheet);

    List<FlowSheetCustomization> getFlowSheetCustomizationsForPatient(String flowsheet, String demographicNo);

    /**
     * Finds a customization at a higher scope level for a specific measurement and action.
     * Used to enforce cascading rules where higher levels (clinic/provider) block lower levels.
     *
     * <p>Scope hierarchy: clinic > provider > patient</p>
     * <ul>
     *   <li>For patient scope: checks clinic and provider levels</li>
     *   <li>For provider scope: checks clinic level only</li>
     *   <li>For clinic scope: returns null (nothing is higher)</li>
     * </ul>
     *
     * @param flowsheet the flowsheet name
     * @param measurement the measurement name
     * @param action the action type (ADD, UPDATE, DELETE)
     * @param providerNo current provider number (empty string for clinic scope)
     * @param demographicNo current demographic number ("0" for clinic/provider scope)
     * @return the blocking customization from a higher level, or null if none exists
     * @since 2025-12-23
     */
    FlowSheetCustomization findHigherLevelCustomization(
        String flowsheet, String measurement, String action,
        String providerNo, String demographicNo);
}
