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

import ca.openosp.openo.commn.dao.MeasurementDao;
import ca.openosp.openo.commn.model.Demographic;
import ca.openosp.openo.commn.model.Measurement;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryRequest;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryViewerResponse;
import ca.openosp.openo.managers.DemographicManager;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Orchestrator service for performing allergy checks via the Vigilance API.
 * This service bridges OpenO's business logic with the low-level VigilanceClient.
 */
@Service
public class VigilanceAllergyCheckService {

    private final DemographicManager demographicManager;
    private final MeasurementDao measurementDao;
    private final VigilanceService vigilanceService;

    @Autowired
    public VigilanceAllergyCheckService(DemographicManager demographicManager,
                                        MeasurementDao measurementDao,
                                      VigilanceService vigilanceService) {
        this.demographicManager = demographicManager;
        this.measurementDao = measurementDao;
        this.vigilanceService = vigilanceService;
    }

    /**
     * Performs an allergy check for a patient and a specific drug (ATC code).
     *
     * @param loggedInInfo the currently logged in user info
     * @param demographicNo the internal identifier of the patient
     * @param drugDinCode the ATC code of the target drug
     * @return the response from Vigilance API containing analysis results
     */
    public String checkAllergies(LoggedInInfo loggedInInfo, Integer demographicNo, String drugDinCode) {
        // 1. Resolve Patient Profile
        Demographic demographic = this.demographicManager.getDemographic(loggedInInfo, demographicNo);
        if (demographic == null) {
            throw new IllegalArgumentException("Patient not found for demographicNo: " + demographicNo);
        }

        VigilanceQueryRequest request = assembleRequest(demographic, drugDinCode);

        // 2. Call Vigilance API via Service layer
        return vigilanceService.queryAnalysis(request);
    }

    /**
     * Performs an allergy check with HTML viewer content for a patient and a specific drug (ATC code).
     *
     * @param loggedInInfo the currently logged in user info
     * @param demographicNo the internal identifier of the patient
     * @param drugDinCode the ATC code of the target drug
     * @return combined response with analysis results and HTML viewer content
     */
    public VigilanceQueryViewerResponse checkAllergiesWithViewer(LoggedInInfo loggedInInfo, Integer demographicNo, String drugDinCode) {
        // 1. Resolve Patient Profile
        Demographic demographic = this.demographicManager.getDemographic(loggedInInfo, demographicNo);
        if (demographic == null) {
            throw new IllegalArgumentException("Patient not found for demographicNo: " + demographicNo);
        }

        VigilanceQueryRequest request = assembleRequest(demographic, drugDinCode);

        // 2. Call Vigilance API via Service layer for both analysis and viewer
        return vigilanceService.queryViewerWithAllergies(request);
    }

    private VigilanceQueryRequest assembleRequest(Demographic demographic, String drugDinCode) {
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

        VigilanceQueryRequest.Product product = new VigilanceQueryRequest.Product(drugDinCode, "din");
        VigilanceQueryRequest.Medication medication = new VigilanceQueryRequest.Medication(List.of(product));


        VigilanceQueryRequest.Profile profile = new VigilanceQueryRequest.Profile(
                patient,
                List.of(medication),
                List.of(),
                List.of()
        );

        VigilanceQueryRequest.Institution institution = new VigilanceQueryRequest.Institution(0);

        return new VigilanceQueryRequest(query, profile, institution);
    }
}
