/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.olis.model;

import javax.servlet.http.HttpServletRequest;

public class OlisQueryParameters {
    
    private String blockedInformationIndividual;
    private String overrideFirstName;
    private String overrideLastName;
    private String overrideRelationship;
    private String startTimePeriod;
    private String endTimePeriod;
    private String observationStartTimePeriod;
    private String observationEndTimePeriod;
    private String quantityLimitedQuery;
    private String quantityLimit;
    private String blockedInformationConsent;
    private String consentBlockAllIndicator;
    private String specimenCollector;
    private String performingLaboratory;
    private String excludePerformingLaboratory;
    private String reportingLaboratory;
    private String placerGroupNumber;
    private String excludeReportingLaboratory;
    private String demographic;
    private String orderingPractitionerCpso;
    private String copiedToPractitionerCpso;
    private String attendingPractitionerCpso;
    private String admittingPractitionerCpso;
    private String resultCodes;
    private String requestCodes;
    private String testRequestPlacer;
    private String retrieveAllResults;
    private String continuationPointer;
    private String destinationLaboratory;
    private String orderingFacility;
    private String z50firstName;
    private String z50lastName;
    private String z50sex;
    private String z50dateOfBirth;
    private String[] requestingHics;
    private String[] testRequestStatus;
    private String requestIp;
    
    public OlisQueryParameters(HttpServletRequest request) {
        this.blockedInformationIndividual = request.getParameter("blockedInformationIndividual");
        this.overrideFirstName = request.getParameter("overrideFirstName");
        this.overrideLastName = request.getParameter("overrideLastName");
        this.overrideRelationship = request.getParameter("overrideRelationship");
        this.startTimePeriod = request.getParameter("startTimePeriod");
        this.endTimePeriod = request.getParameter("endTimePeriod");
        this.observationStartTimePeriod = request.getParameter("observationStartTimePeriod");
        this.observationEndTimePeriod = request.getParameter("observationEndTimePeriod");
        this.quantityLimitedQuery = request.getParameter("quantityLimitedQuery");
        this.quantityLimit = request.getParameter("quantityLimit");
        this.blockedInformationConsent = request.getParameter("blockedInformationConsent");
        this.consentBlockAllIndicator = request.getParameter("consentBlockAllIndicator");
        this.specimenCollector = request.getParameter("specimenCollector");
        this.performingLaboratory = request.getParameter("performingLaboratory");
        this.excludePerformingLaboratory = request.getParameter("excludePerformingLaboratory");
        this.reportingLaboratory = request.getParameter("reportingLaboratory");
        this.placerGroupNumber = request.getParameter("placerGroupNumber");
        this.excludeReportingLaboratory = request.getParameter("excludeReportingLaboratory");
        this.demographic = request.getParameter("demographic");
        this.orderingPractitionerCpso = request.getParameter("orderingPractitionerCpso");
        this.copiedToPractitionerCpso = request.getParameter("copiedToPractitionerCpso");
        this.attendingPractitionerCpso = request.getParameter("attendingPractitionerCpso");
        this.admittingPractitionerCpso = request.getParameter("admittingPractitionerCpso");
        this.resultCodes = request.getParameter("resultCodes");
        this.requestCodes = request.getParameter("requestCodes");
        this.testRequestPlacer = request.getParameter("testRequestPlacer");
        this.retrieveAllResults = request.getParameter("retrieveAllResults");
        this.continuationPointer = request.getParameter("continuationPointer");
        this.destinationLaboratory = request.getParameter("destinationLaboratory");
        this.orderingFacility = request.getParameter("orderingFacility");
        this.z50firstName = request.getParameter("z50firstName");
        this.z50lastName = request.getParameter("z50lastName");
        this.z50sex = request.getParameter("z50sex");
        this.z50dateOfBirth = request.getParameter("z50dateOfBirth");
        this.requestingHics = request.getParameterValues("requestingHic");
        this.testRequestStatus = request.getParameterValues("testRequestStatus");
        this.requestIp = request.getRemoteAddr();
    }

    public String getBlockedInformationIndividual() {
        return blockedInformationIndividual;
    }

    public void setBlockedInformationIndividual(String blockedInformationIndividual) {
        this.blockedInformationIndividual = blockedInformationIndividual;
    }

    public String getOverrideFirstName() {
        return overrideFirstName;
    }

    public void setOverrideFirstName(String overrideFirstName) {
        this.overrideFirstName = overrideFirstName;
    }

    public String getOverrideLastName() {
        return overrideLastName;
    }

    public void setOverrideLastName(String overrideLastName) {
        this.overrideLastName = overrideLastName;
    }

    public String getOverrideRelationship() {
        return overrideRelationship;
    }

    public void setOverrideRelationship(String overrideRelationship) {
        this.overrideRelationship = overrideRelationship;
    }

    public String getStartTimePeriod() {
        return startTimePeriod;
    }

    public void setStartTimePeriod(String startTimePeriod) {
        this.startTimePeriod = startTimePeriod;
    }

    public String getEndTimePeriod() {
        return endTimePeriod;
    }

    public void setEndTimePeriod(String endTimePeriod) {
        this.endTimePeriod = endTimePeriod;
    }

    public String getObservationStartTimePeriod() {
        return observationStartTimePeriod;
    }

    public void setObservationStartTimePeriod(String observationStartTimePeriod) {
        this.observationStartTimePeriod = observationStartTimePeriod;
    }

    public String getObservationEndTimePeriod() {
        return observationEndTimePeriod;
    }

    public void setObservationEndTimePeriod(String observationEndTimePeriod) {
        this.observationEndTimePeriod = observationEndTimePeriod;
    }

    public String getQuantityLimitedQuery() {
        return quantityLimitedQuery;
    }

    public void setQuantityLimitedQuery(String quantityLimitedQuery) {
        this.quantityLimitedQuery = quantityLimitedQuery;
    }

    public String getQuantityLimit() {
        return quantityLimit;
    }

    public void setQuantityLimit(String quantityLimit) {
        this.quantityLimit = quantityLimit;
    }

    public String getBlockedInformationConsent() {
        return blockedInformationConsent;
    }

    public void setBlockedInformationConsent(String blockedInformationConsent) {
        this.blockedInformationConsent = blockedInformationConsent;
    }

    public String getConsentBlockAllIndicator() {
        return consentBlockAllIndicator;
    }

    public void setConsentBlockAllIndicator(String consentBlockAllIndicator) {
        this.consentBlockAllIndicator = consentBlockAllIndicator;
    }

    public String getSpecimenCollector() {
        return specimenCollector;
    }

    public void setSpecimenCollector(String specimenCollector) {
        this.specimenCollector = specimenCollector;
    }

    public String getPerformingLaboratory() {
        return performingLaboratory;
    }

    public void setPerformingLaboratory(String performingLaboratory) {
        this.performingLaboratory = performingLaboratory;
    }

    public String getExcludePerformingLaboratory() {
        return excludePerformingLaboratory;
    }

    public void setExcludePerformingLaboratory(String excludePerformingLaboratory) {
        this.excludePerformingLaboratory = excludePerformingLaboratory;
    }

    public String getReportingLaboratory() {
        return reportingLaboratory;
    }

    public void setReportingLaboratory(String reportingLaboratory) {
        this.reportingLaboratory = reportingLaboratory;
    }

    public String getPlacerGroupNumber() {
        return placerGroupNumber;
    }

    public void setPlacerGroupNumber(String placerGroupNumber) {
        this.placerGroupNumber = placerGroupNumber;
    }

    public String getExcludeReportingLaboratory() {
        return excludeReportingLaboratory;
    }

    public void setExcludeReportingLaboratory(String excludeReportingLaboratory) {
        this.excludeReportingLaboratory = excludeReportingLaboratory;
    }

    public String getDemographic() {
        return demographic;
    }

    public void setDemographic(String demographic) {
        this.demographic = demographic;
    }

    public String getOrderingPractitionerCpso() {
        return orderingPractitionerCpso;
    }

    public void setOrderingPractitionerCpso(String orderingPractitionerCpso) {
        this.orderingPractitionerCpso = orderingPractitionerCpso;
    }

    public String getCopiedToPractitionerCpso() {
        return copiedToPractitionerCpso;
    }

    public void setCopiedToPractitionerCpso(String copiedToPractitionerCpso) {
        this.copiedToPractitionerCpso = copiedToPractitionerCpso;
    }

    public String getAttendingPractitionerCpso() {
        return attendingPractitionerCpso;
    }

    public void setAttendingPractitionerCpso(String attendingPractitionerCpso) {
        this.attendingPractitionerCpso = attendingPractitionerCpso;
    }

    public String getAdmittingPractitionerCpso() {
        return admittingPractitionerCpso;
    }

    public void setAdmittingPractitionerCpso(String admittingPractitionerCpso) {
        this.admittingPractitionerCpso = admittingPractitionerCpso;
    }

    public String getResultCodes() {
        return resultCodes;
    }

    public void setResultCodes(String resultCodes) {
        this.resultCodes = resultCodes;
    }

    public String getRequestCodes() {
        return requestCodes;
    }

    public void setRequestCodes(String requestCodes) {
        this.requestCodes = requestCodes;
    }

    public String getTestRequestPlacer() {
        return testRequestPlacer;
    }

    public void setTestRequestPlacer(String testRequestPlacer) {
        this.testRequestPlacer = testRequestPlacer;
    }

    public String getRetrieveAllResults() {
        return retrieveAllResults;
    }

    public void setRetrieveAllResults(String retrieveAllResults) {
        this.retrieveAllResults = retrieveAllResults;
    }

    public String getContinuationPointer() {
        return continuationPointer;
    }

    public void setContinuationPointer(String continuationPointer) {
        this.continuationPointer = continuationPointer;
    }

    public String getDestinationLaboratory() {
        return destinationLaboratory;
    }

    public void setDestinationLaboratory(String destinationLaboratory) {
        this.destinationLaboratory = destinationLaboratory;
    }

    public String getOrderingFacility() {
        return orderingFacility;
    }

    public void setOrderingFacility(String orderingFacility) {
        this.orderingFacility = orderingFacility;
    }

    public String getZ50firstName() {
        return z50firstName;
    }

    public void setZ50firstName(String z50firstName) {
        this.z50firstName = z50firstName;
    }

    public String getZ50lastName() {
        return z50lastName;
    }

    public void setZ50lastName(String z50lastName) {
        this.z50lastName = z50lastName;
    }

    public String getZ50sex() {
        return z50sex;
    }

    public void setZ50sex(String z50sex) {
        this.z50sex = z50sex;
    }

    public String getZ50dateOfBirth() {
        return z50dateOfBirth;
    }

    public void setZ50dateOfBirth(String z50dateOfBirth) {
        this.z50dateOfBirth = z50dateOfBirth;
    }

    public String[] getRequestingHics() {
        return requestingHics;
    }

    public void setRequestingHics(String[] requestingHics) {
        this.requestingHics = requestingHics;
    }

    public String[] getTestRequestStatus() {
        return testRequestStatus;
    }

    public void setTestRequestStatus(String[] testRequestStatus) {
        this.testRequestStatus = testRequestStatus;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }
}
