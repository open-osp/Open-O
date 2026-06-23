//CHECKSTYLE:OFF

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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class VigilanceManagerImpl implements VigilanceManager {

    private static final String VIGILANCE_DOWN_MESSAGE = "Drug analysis service is currently unavailable. Prescriptions will be saved but you won't receive allergy/interaction warnings from Vigilance.";

    private final VigilanceService vigilanceService;
    private final VigilanceAllergyCheckService vigilanceAllergyCheckService;
    private final UserPropertyDAO userPropertyDAO;

    @Autowired
    public VigilanceManagerImpl(VigilanceService vigilanceService,
                                VigilanceAllergyCheckService vigilanceAllergyCheckService,
                                UserPropertyDAO userPropertyDAO) {
        this.vigilanceService = vigilanceService;
        this.vigilanceAllergyCheckService = vigilanceAllergyCheckService;
        this.userPropertyDAO = userPropertyDAO;
    }

    @Override
    public VigilanceStatusResult getStatus() {
        if (isAllergyWarningsDisabled()) {
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

    @Override
    public VigilanceAnalysisResult analyzeAllergy(LoggedInInfo loggedInInfo, int demographicNo, List<RxPrescriptionData.Prescription> stash) {
        if (isAllergyWarningsDisabled()) {
            return null;
        }

        int providerPreferredWarningLevel = getProviderWarningLevel(loggedInInfo.getLoggedInProviderNo());

        try {
            VigilanceQueryViewerResponse queryViewerResponse = vigilanceAllergyCheckService.checkAllergies(
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
            MiscUtils.getLogger().error("Error in analyzeAllergy", e);
            return new VigilanceAnalysisResult(
                    providerPreferredWarningLevel,
                    false,
                    null,
                    null,
                    null
            );
        }
    }

    private boolean isAllergyWarningsDisabled() {
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
