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
import java.util.Map;
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
        if (response == null || patientAllergies == null) {
            return new ArrayList<>();
        }

        List<VigilanceQueryResponse.ProfileSideEffect> sideEffects = response.profileSideEffects();
        if (sideEffects == null || sideEffects.isEmpty()) {
            return new ArrayList<>();
        }

        List<VigilanceQueryResponse.Product> medications = new ArrayList<>();
        if (response.profile() != null && response.profile().medications() != null) {
            for (VigilanceQueryResponse.MedicationEntry entry : response.profile().medications()) {
                if (entry.product() != null) {
                    medications.addAll(entry.product());
                }
            }
        }

        Map<String, Integer> intensityMap = extractIntensityMap(response.profileIntensity());

        List<String> flaggedCodes = medications.stream()
                .filter(med -> med.detail() != null && med.code() != null)
                .filter(med -> {
                    Integer intensity = intensityMap.get(med.code());
                    return intensity != null && intensity > 0;
                })
                .map(VigilanceQueryResponse.Product::code)
                .collect(Collectors.toList());

        if (flaggedCodes.isEmpty()) {
            return new ArrayList<>();
        }

        return patientAllergies.stream()
                .filter(allergy -> {
                    String allergyCode = allergy.getAtc();
                    return allergyCode != null && !allergyCode.isEmpty() && flaggedCodes.contains(allergyCode);
                })
                .collect(Collectors.toList());
    }

    private static Map<String, Integer> extractIntensityMap(VigilanceQueryResponse.ProfileIntensity profileIntensity) {
        if (profileIntensity == null || profileIntensity.detail() == null) {
            return Map.of();
        }

        return profileIntensity.detail().stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
