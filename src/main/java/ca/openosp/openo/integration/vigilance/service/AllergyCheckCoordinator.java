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
import ca.openosp.openo.utility.LoggedInInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Coordinator for allergy checks that handles provider strategy and failover resilience.
 */
@Service
public class AllergyCheckCoordinator {

    private static final Logger logger = LoggerFactory.getLogger(AllergyCheckCoordinator.class);

    private final VigilanceProvider vigilanceProvider;
    private final DrugRefProvider drugRefProvider;

    @Value("${vigilance.enabled}")
    private boolean isVigilanceEnabled;

    @Autowired
    public AllergyCheckCoordinator(VigilanceProvider vigilanceProvider, DrugRefProvider drugRefProvider) {
        this.vigilanceProvider = vigilanceProvider;
        this.drugRefProvider = drugRefProvider;
    }

    /**
     * Performs an allergy check using the configured provider with automatic failover to DrugRef.
     * 
     * @param loggedInInfo the currently logged in user info
     * @param demographicNo the internal identifier of the patient
     * @param drugAtcCode the ATC code of the target drug
     * @param currentAllergies the patient's existing allergy list
     * @return a subset of original allergies triggering warnings
     * @throws AllergyCheckUnavailableException if both primary and fallback providers fail
     */
    public List<Allergy> performAllergyCheck(LoggedInInfo loggedInInfo, Integer demographicNo, String drugAtcCode, List<Allergy> currentAllergies) {
        if (isVigilanceEnabled) {
            try {
                return executeWithRetry(loggedInInfo, demographicNo, drugAtcCode, currentAllergies, 2);
            } catch (Exception e) {
                logger.warn("Vigilance provider failed. Falling back to DrugRef. Error: {}", e.getMessage());
            }
        }

        // Default or Fallback path
        try {
            return drugRefProvider.checkAllergies(loggedInInfo, demographicNo, drugAtcCode, currentAllergies);
        } catch (Exception e) {
            logger.error("Terminal failure: Both Vigilance and DrugRef providers are unavailable.");
            throw new AllergyCheckUnavailableException("Allergy check service is currently unavailable. Please verify manually.");
        }
    }

    private List<Allergy> executeWithRetry(LoggedInInfo loggedInInfo, Integer demographicNo, String drugAtcCode, List<Allergy> currentAllergies, int maxRetries) throws Exception {
        int attempts = 0;
        while (true) {
            try {
                return vigilanceProvider.checkAllergies(loggedInInfo, demographicNo, drugAtcCode, currentAllergies);
            } catch (Exception e) {
                attempts++;
                if (attempts > maxRetries) {
                    throw e;
                }
                logger.info("Vigilance attempt {} failed, retrying...", attempts);
            }
        }
    }

    public static class AllergyCheckUnavailableException extends RuntimeException {
        public AllergyCheckUnavailableException(String message) {
            super(message);
        }
    }
}
