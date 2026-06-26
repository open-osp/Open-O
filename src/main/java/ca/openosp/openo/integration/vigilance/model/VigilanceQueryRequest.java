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

package ca.openosp.openo.integration.vigilance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Request object for performing a query analysis via the Vigilance API.
 * Contains information about the service to use and the patient's profile.
 */
public record VigilanceQueryRequest(Query query, Profile profile, Institution institution) implements VigilanceRequest {

    /**
     * Configuration for the query, including service and specific settings.
     */
    public record Query(ServiceInfo service, Config config) {}

    /**
     * Information about the specific Vigilance service to be invoked.
     */
    public record ServiceInfo(String id, int userType, int analysisMode) {}

    /**
     * Additional configuration parameters for the query, such as geographical zones.
     */
    public record Config(List<String> zone) {}

    /**
     * Patient profile information including demographics and current medications.
     */
    public record Profile(Patient patient, List<Medication> medications, List<Diagnose> diagnoses, List<RxProblem> rxProblems) {}

    /**
     * Type of institution making the request.
     */
    public record Institution(@JsonProperty("type") int type) {}

    /**
     * Demographic information for the patient.
     */
    public record Patient(String firstName, String lastName, String gender, Age age, double weightKg) {}

    /**
     * Patient's age information.
     */
    public record Age(int years) {}

    /**
     * Medication information for the patient.
     */
    public record Medication(List<Product> product) {}

    /**
     * Specific medication product details.
     */
    public record Product(String code, String fmt) {}

    /**
     * Represents a clinical diagnosis associated with the patient profile.
     */
    public record Diagnose(String code) {}

    /**
     * Represents a medication-related problem or issue reported by Vigilance for a
     * prescription or drug exposure.
     */
    public record RxProblem(String code, String fmt, int prob) {}

}
