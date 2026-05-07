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

import ca.openosp.openo.commn.model.Demographic;
import ca.openosp.openo.integration.vigilance.client.VigilanceClient;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryRequest;
import ca.openosp.openo.integration.vigilance.model.VigilanceQueryResponse;
import ca.openosp.openo.managers.DemographicManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.openosp.openo.utility.LoggedInInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrator service for performing allergy checks via the Vigilance API.
 * This service bridges OpenO's business logic with the low-level VigilanceClient.
 */
@Service
public class VigilanceAllergyCheckService {

    private final DemographicManager demographicManager;
    private final VigilanceService vigilanceService;

    @Autowired
    public VigilanceAllergyCheckService(DemographicManager demographicManager,
                                      VigilanceService vigilanceService) {
        this.demographicManager = demographicManager;
        this.vigilanceService = vigilanceService;
    }

    /**
     * Performs an allergy check for a patient and a specific drug (ATC code).
     *
     * @param loggedInInfo the currently logged in user info
     * @param demographicNo the internal identifier of the patient
     * @param drugAtcCode the ATC code of the target drug
     * @return the response from Vigilance API containing analysis results
     */
    public VigilanceQueryResponse checkAllergies(LoggedInInfo loggedInInfo, Integer demographicNo, String drugAtcCode) {
        // 1. Resolve Patient Profile
        Demographic demographic = demographicManager.getDemographic(loggedInInfo, demographicNo);
        if (demographic == null) {
            throw new IllegalArgumentException("Patient not found for demographicNo: " + demographicNo);
        }

        VigilanceQueryRequest request = assembleRequest(demographic, drugAtcCode);

        // 2. Call Vigilance API via Service layer
        return vigilanceService.queryAnalysis(request);
    }

    private VigilanceQueryRequest assembleRequest(Demographic demographic, String drugAtcCode) {
        VigilanceQueryRequest request = new VigilanceQueryRequest();

        // Assemble Query settings
        VigilanceQueryRequest.Query query = new VigilanceQueryRequest.Query();
        VigilanceQueryRequest.ServiceInfo serviceInfo = new VigilanceQueryRequest.ServiceInfo();
        serviceInfo.setId("analysis");
        serviceInfo.setUserType(2);
        serviceInfo.setAnalysisMode(0);
        query.setService(serviceInfo);

        VigilanceQueryRequest.Config config = new VigilanceQueryRequest.Config();
        List<String> zones = new ArrayList<>();
        zones.add("ON"); // Default to Ontario zone as per Postman collection
        config.setZone(zones);
        query.setConfig(config);

        request.setQuery(query);

        // Assemble Patient Profile
        VigilanceQueryRequest.Profile profile = new VigilanceQueryRequest.Profile();
        VigilanceQueryRequest.Patient patient = new VigilanceQueryRequest.Patient();
        patient.setFirstName(demographic.getFirstName());
        patient.setLastName(demographic.getLastName());
        patient.setGender(demographic.getGender());
        
        VigilanceQueryRequest.Age age = new VigilanceQueryRequest.Age();
        age.setYears(demographic.getAgeInYears());
        patient.setAge(age);

        // TODO: Implement actual weight retrieval from vitals/measurements system
        // Using a default value of 70kg for now as per the technical implementation plan's mandatory requirements
        patient.setWeightKg(70.0); 
        
        profile.setPatient(patient);

        // Assemble Medication list
        List<VigilanceQueryRequest.Medication> medications = new ArrayList<>();
        VigilanceQueryRequest.Medication medication = new VigilanceQueryRequest.Medication();
        List<VigilanceQueryRequest.Product> products = new ArrayList<>();
        VigilanceQueryRequest.Product product = new VigilanceQueryRequest.Product();
        product.setCode(drugAtcCode);
        product.setFmt("atc");
        products.add(product);
        medication.setProduct(products);
        medications.add(medication);
        profile.setMedications(medications);

        request.setProfile(profile);

        return request;
    }
}
