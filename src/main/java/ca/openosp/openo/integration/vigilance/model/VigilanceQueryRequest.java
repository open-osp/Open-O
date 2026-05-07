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
public class VigilanceQueryRequest implements VigilanceRequest {
    private Query query;
    private Profile profile;

    public Query getQuery() { return query; }
    public void setQuery(Query query) { this.query = query; }
    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    /**
     * Configuration for the query, including service and specific settings.
     */
    public static class Query {
        private ServiceInfo service;
        private Config config;

        public ServiceInfo getService() { return service; }
        public void setService(ServiceInfo service) { this.service = service; }
        public Config getConfig() { return config; }
        public void setConfig(Config config) { this.config = config; }
    }

    /**
     * Information about the specific Vigilance service to be invoked.
     */
    public static class ServiceInfo {
        private String id;
        private int userType;
        private int analysisMode;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public int getUserType() { return userType; }
        public void setUserType(int userType) { this.userType = userType; }
        public int getAnalysisMode() { return analysisMode; }
        public void setAnalysisMode(int analysisMode) { this.analysisMode = analysisMode; }
    }

    /**
     * Additional configuration parameters for the query, such as geographical zones.
     */
    public static class Config {
        private List<String> zone;

        public List<String> getZone() { return zone; }
        public void setZone(List<String> zone) { this.zone = zone; }
    }

    /**
     * Patient profile information including demographics and current medications.
     */
    public static class Profile {
        private Patient patient;
        private List<Medication> medications;

        public Patient getPatient() { return patient; }
        public void setPatient(Patient patient) { this.patient = patient; }
        public List<Medication> getMedications() { return medications; }
        public void setMedications(List<Medication> medications) { this.medications = medications; }
    }

    /**
     * Demographic information for the patient.
     */
    public static class Patient {
        private String firstName;
        private String lastName;
        private String gender;
        private Age age;
        private double weightKg;

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public Age getAge() { return age; }
        public void setAge(Age age) { this.age = age; }
        public double getWeightKg() { return weightKg; }
        public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
    }

    /**
     * Patient's age information.
     */
    public static class Age {
        private int years;

        public int getYears() { return years; }
        public void setYears(int years) { this.years = years; }
    }

    /**
     * Medication information for the patient.
     */
    public static class Medication {
        private List<Product> product;

        public List<Product> getProduct() { return product; }
        public void setProduct(List<Product> product) { this.product = product; }
    }

    /**
     * Specific medication product details.
     */
    public static class Product {
        private String code;
        private String fmt;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getFmt() { return fmt; }
        public void setFmt(String fmt) { this.fmt = fmt; }
    }
}
