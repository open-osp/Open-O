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

import ca.openosp.openo.commn.model.Allergy;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for translating Vigilance API analysis results into OpenO Allergy objects.
 */
public class VigilanceAllergyMapper {

    /**
     * Filters the patient's existing allergies based on warnings returned by Vigilance.
     * 
     * @param patientAllergies the original list of patient allergies
     * @param response the analysis result from the Vigilance API
     * @return a subset of the original allergies that match the Vigilance findings
     */
    public static List<Allergy> mapToMatchingAllergies(List<Allergy> patientAllergies, VigilanceQueryResponse response) {
        if (response == null || response.getAlerts() == null || patientAllergies == null) {
            return new ArrayList<>();
        }

        // 1. Filter for alerts where type == "allergyCross"
        List<VigilanceQueryResponse.Alert> crossAllergyAlerts = response.getAlerts().stream()
                .filter(alert -> "allergyCross".equalsIgnoreCase(alert.getType()))
                .collect(Collectors.toList());

        if (crossAllergyAlerts.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Collect all ATC codes identified as risks by Vigilance
        List<String> vigilanceAtcCodes = new ArrayList<>();
        for (VigilanceQueryResponse.Alert alert : crossAllergyAlerts) {
            if (alert.getBasis() != null && alert.getBasis().getRxProblems() != null) {
                for (VigilanceQueryResponse.RxProblem problem : alert.getBasis().getRxProblems()) {
                    if (problem.getAtc() != null && !problem.getAtc().isEmpty()) {
                        vigilanceAtcCodes.add(problem.getAtc());
                    }
                }
            }
        }

        // 3. Match against patient's allergies based on ATC code (Forbidden to use description strings)
        return patientAllergies.stream()
                .filter(allergy -> {
                    String patientAtc = allergy.getAtc();
                    return patientAtc != null && !patientAtc.isEmpty() && vigilanceAtcCodes.contains(patientAtc);
                })
                .collect(Collectors.toList());
    }
}
