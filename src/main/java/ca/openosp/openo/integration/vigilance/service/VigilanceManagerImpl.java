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

import ca.openosp.OscarProperties;
import ca.openosp.openo.commn.model.UserProperty;
import ca.openosp.openo.commn.dao.UserPropertyDAO;
import ca.openosp.openo.integration.vigilance.model.VigilanceAnalysisResult;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryViewerResponse;
import ca.openosp.openo.integration.vigilance.model.VigilanceStatusResult;
import ca.openosp.openo.prescript.data.RxPrescriptionData;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of VigilanceManager for drug analysis operations.
 * <p>
 * Orchestrates drug interaction checks via the Vigilance API,
 * applying provider-specific warning level preferences.
 */
@Service
public class VigilanceManagerImpl implements VigilanceManager {

    private static final String VIGILANCE_DOWN_MESSAGE = "Drug interaction analysis service is currently unavailable. Prescriptions will be saved but you won't receive interaction warnings from Vigilance.";

    private final VigilanceService vigilanceService;
    private final VigilanceDrugsInteractionCheckService vigilanceDrugsInteractionCheckService;
    private final UserPropertyDAO userPropertyDAO;

    /**
     * Creates a new manager with the required services.
     */
    @Autowired
    public VigilanceManagerImpl(@Nullable VigilanceService vigilanceService,
                                @Nullable VigilanceDrugsInteractionCheckService vigilanceDrugsInteractionCheckService,
                                UserPropertyDAO userPropertyDAO) {
        this.vigilanceService = vigilanceService;
        this.vigilanceDrugsInteractionCheckService = vigilanceDrugsInteractionCheckService;
        this.userPropertyDAO = userPropertyDAO;
    }

    /**
     * Returns the current status of the Vigilance drug analysis service.
     * Returns null if allergy/interaction warnings are disabled for this provider.
     *
     * @return status result indicating service health, or null if warnings are disabled
     */
    @Override
    public VigilanceStatusResult getStatus() {
        if (isAllergyInteractionWarningsDisabled() || vigilanceService == null) {
            return null;
        }

        if (!vigilanceService.hasValidEntry()) {
            try {
                vigilanceService.statusCheck();
            } catch (Exception e) {
                MiscUtils.getLogger().error("Initial Vigilance status check failed", e);
            }
        }

        try {
            var status = vigilanceService.getStatusIfUp();
            if (status != null) {
                return new VigilanceStatusResult(true, null);
            } else {
                return new VigilanceStatusResult(false, VIGILANCE_DOWN_MESSAGE);
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error checking Vigilance status", e);
            return new VigilanceStatusResult(false, VIGILANCE_DOWN_MESSAGE);
        }
    }

    /**
     * Performs an drug interaction analysis for a patient's prescriptions.
     * Applies provider-specific warning level preferences to filter results.
     *
     * @param loggedInInfo the currently logged in user info
     * @param demographicNo the internal identifier of the patient
     * @param stash the list of prescriptions to analyze
     * @return analysis result with alert status and display icon, or null if warnings are disabled
     */
    @Override
    public VigilanceAnalysisResult analyzeDrugsInteraction(LoggedInInfo loggedInInfo, int demographicNo, List<RxPrescriptionData.Prescription> stash) {
        if (isAllergyInteractionWarningsDisabled() || vigilanceDrugsInteractionCheckService == null) {
            return null;
        }

        int providerPreferredWarningLevel = getProviderWarningLevel(loggedInInfo.getLoggedInProviderNo());

        try {
            VigilanceQueryViewerResponse queryViewerResponse = vigilanceDrugsInteractionCheckService.checkDrugsInteraction(
                    loggedInInfo, demographicNo, stash);

            String rawVigilanceResponse = queryViewerResponse.rawResponse();
            boolean hasRawResponse = !rawVigilanceResponse.isEmpty();

            boolean showAlert = false;
            String displayIconValue = null;

            try {
                if (Objects.nonNull(queryViewerResponse.vigilanceQueryResponse()) &&
                    Objects.nonNull(queryViewerResponse.vigilanceQueryResponse().summary()) &&
                    Objects.nonNull(queryViewerResponse.vigilanceQueryResponse().summary().displayIcon())) {
                    displayIconValue = queryViewerResponse.vigilanceQueryResponse().summary().displayIcon();
                    if (!displayIconValue.startsWith("alert0")) {
                        showAlert = true;

                        if (providerPreferredWarningLevel >= 4) {
                            showAlert = false;
                            displayIconValue = null;
                        } else if ("alert3".equals(displayIconValue)) {
                            if (providerPreferredWarningLevel >= 2) {
                                showAlert = false;
                            }
                        } else if ("alert2".equals(displayIconValue)) {
                            if (providerPreferredWarningLevel == 3) {
                                showAlert = false;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                MiscUtils.getLogger().warn("Failed to extract displayIcon", e);
            }

            String rawResponse = null;
            String token = null;
            if (showAlert && hasRawResponse) {
                rawResponse = rawVigilanceResponse;
                token = queryViewerResponse.token();
            }

            return new VigilanceAnalysisResult(
                    providerPreferredWarningLevel,
                    showAlert,
                    displayIconValue,
                    rawResponse,
                    token
            );

        } catch (Exception e) {
            MiscUtils.getLogger().error("Error in analyzeDrugsInteraction", e);
            return new VigilanceAnalysisResult(
                    providerPreferredWarningLevel,
                    false,
                    null,
                    null,
                    null
            );
        }
    }

    private boolean isAllergyInteractionWarningsDisabled() {
        boolean vigilanceEnabled = OscarProperties.getInstance().getBooleanProperty("vigilance.enabled", "true");
        if (!vigilanceEnabled) {
            return true;
        }
        String disabled = OscarProperties.getInstance().getProperty("rx3.disable_allergy_warnings", "false");
        return "true".equals(disabled);
    }

    private int getProviderWarningLevel(String providerNo) {
        UserProperty warningLevelProp = userPropertyDAO.getProp(providerNo, "rxInteractionWarningLevel");
        if (warningLevelProp != null && warningLevelProp.getValue() != null && !warningLevelProp.getValue().isEmpty()) {
            try {
                return Integer.parseInt(warningLevelProp.getValue());
            } catch (NumberFormatException e) {
                MiscUtils.getLogger().warn("Invalid rxInteractionWarningLevel value: " + warningLevelProp.getValue());
            }
        }
        return 0;
    }
}
