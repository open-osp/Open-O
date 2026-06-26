//CHECKSTYLE:OFF

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
