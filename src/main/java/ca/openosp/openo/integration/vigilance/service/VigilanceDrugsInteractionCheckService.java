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

import ca.openosp.openo.casemgmt.service.CaseManagementManager;
import ca.openosp.openo.commn.dao.MeasurementDao;
import ca.openosp.openo.commn.model.Demographic;
import ca.openosp.openo.commn.model.Drug;
import ca.openosp.openo.commn.model.Measurement;
import ca.openosp.openo.integration.vigilance.exception.VigilanceIntegrationException;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryRequest;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryViewerResponse;
import ca.openosp.openo.integration.vigilance.model.VigilanceStatusResponse;
import ca.openosp.openo.managers.DemographicManager;
import ca.openosp.openo.prescript.data.RxPrescriptionData;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.openosp.openo.integration.vigilance.config.ConditionalOnVigilanceEnabled;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.net.ConnectException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Orchestrator service for performing drug interaction checks via the Vigilance API.
 * This service bridges OpenO's business logic with the low-level VigilanceClient.
 */
@Service
@ConditionalOnVigilanceEnabled
public class VigilanceDrugsInteractionCheckService {

    private static final Logger log = LoggerFactory.getLogger(VigilanceDrugsInteractionCheckService.class);

    private final DemographicManager demographicManager;
    private final MeasurementDao measurementDao;
    private final CaseManagementManager caseManagementManager;
    private final VigilanceService vigilanceService;

    /**
     * Creates a new drug interaction check service with required dependencies.
     */
    @Autowired
    public VigilanceDrugsInteractionCheckService(DemographicManager demographicManager,
                                                 MeasurementDao measurementDao,
                                                 CaseManagementManager caseManagementManager,
                                                 VigilanceService vigilanceService) {
        this.demographicManager = demographicManager;
        this.measurementDao = measurementDao;
        this.caseManagementManager = caseManagementManager;
        this.vigilanceService = vigilanceService;
    }

    /**
     * Performs an interaction check for a patient and prescribed-prescribing drugs.
     * Checks cached status first, then calls the Vigilance API.
     * On failure, attempts to determine if the service is down and updates cache accordingly.
     *
     * @param loggedInInfo the currently logged in user info
     * @param demographicNo the internal identifier of the patient
     * @param stashDrugs the list of prescriptions to analyze
     * @return the response from Vigilance API containing analysis results
     */
    public VigilanceQueryViewerResponse checkDrugsInteraction(LoggedInInfo loggedInInfo, Integer demographicNo, List<RxPrescriptionData.Prescription> stashDrugs) {
        // 1. Resolve Patient Profile
        Demographic demographic = this.demographicManager.getDemographic(loggedInInfo, demographicNo);
        if (demographic == null) {
            throw new IllegalArgumentException("Patient not found for demographicNo: " + demographicNo);
        }

        List<Drug> prescriptionDrugs = this.caseManagementManager.getCurrentPrescriptions(demographicNo);

        VigilanceQueryRequest request = assembleRequest(demographic, stashDrugs, prescriptionDrugs);

        // 2. Pre-flight status check - skip if cache is healthy
        VigilanceStatusResponse cachedStatus = vigilanceService.getStatusIfUp();
        if (cachedStatus != null) {
            log.debug("Vigilance status confirmed from cache, proceeding with queryAnalysis");
        } else {
            log.warn("Vigilance status not available in cache or unhealthy - proceeding with queryAnalysis anyway");
        }

        // 3. Call Vigilance API via Service layer with error handling
        try {
            return vigilanceService.queryAnalysis(request);
        } catch (VigilanceIntegrationException e) {
            handleQueryFailure(e);
            throw e;
        }
    }

    private void handleQueryFailure(VigilanceIntegrationException exception) {
        String message = exception.getMessage();
        boolean isServiceDown = false;

        if (exception.getCause() instanceof ConnectException || 
            exception.getCause() instanceof WebClientRequestException) {
            isServiceDown = true;
            log.error("Vigilance appears to be unreachable: {}", exception.getMessage());
        } else if (message != null && message.contains("5")) {
            isServiceDown = true;
            log.error("Vigilance returned server error: {}", message);
        }

        if (isServiceDown) {
            try {
                vigilanceService.statusCheck();
            } catch (Exception statusEx) {
                log.debug("Status check also failed, cache already updated with down state");
            }
        }
    }

    private VigilanceQueryRequest assembleRequest(Demographic demographic, List<RxPrescriptionData.Prescription> stagedDrugs, List<Drug> prescriptionDrugs) {
        VigilanceQueryRequest.ServiceInfo serviceInfo = new VigilanceQueryRequest.ServiceInfo("analysis", 2, 0);
        VigilanceQueryRequest.Config config = new VigilanceQueryRequest.Config(List.of("ON"));
        VigilanceQueryRequest.Query query = new VigilanceQueryRequest.Query(serviceInfo, config);

        VigilanceQueryRequest.Age age = new VigilanceQueryRequest.Age(demographic.getAgeInYears());
        double wt = 0;
        Measurement measurement = this.measurementDao.findLastEntered(demographic.getDemographicNo(), "WT");
        if (Objects.nonNull(measurement)) {
            try {
                wt = Double.parseDouble(measurement.getDataField());
            } catch (NumberFormatException e) {
                MiscUtils.getLogger().warn("Unable to parse weight measurement: " + measurement.getDataField());
            }
        }
        VigilanceQueryRequest.Patient patient = new VigilanceQueryRequest.Patient(
            demographic.getFirstName(),
            demographic.getLastName(),
            demographic.getGender(),
            age,
            wt
        );

        List<VigilanceQueryRequest.Medication> medications = Stream.concat(
                stagedDrugs.stream()
                        .map(stagedDrug -> getProductObject(stagedDrug.getRegionalIdentifier())),
                prescriptionDrugs.stream()
                        .filter(d -> !d.isArchived())
                        .filter(Drug::isCurrent)
                        .filter(d -> StringUtils.isNotBlank(d.getRegionalIdentifier()) && !"0".equals(d.getRegionalIdentifier()))
                        .filter(d -> !"0".equals(d.getGcnSeqNo()))
                        .map(d -> getProductObject(d.getRegionalIdentifier()))
        ).toList();

        VigilanceQueryRequest.Profile profile = new VigilanceQueryRequest.Profile(
                patient,
                medications,
                List.of(),
                List.of()
        );

        VigilanceQueryRequest.Institution institution = new VigilanceQueryRequest.Institution(0);

        return new VigilanceQueryRequest(query, profile, institution);
    }

    private static VigilanceQueryRequest.Medication getProductObject(String drugDinCode) {
        return new VigilanceQueryRequest.Medication(List.of(
                new VigilanceQueryRequest.Product(drugDinCode, drugDinCode.contains("##") ? "generx" : "din")
        ));
    }
}
