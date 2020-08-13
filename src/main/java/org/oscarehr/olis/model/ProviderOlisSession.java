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

import ca.ssha._2005.hial.ArrayOfString;
import ca.ssha._2005.hial.Error;
import ca.ssha._2005.hial.Response;
import ca.ssha.www._2005.hial.OLISStub;
import com.indivica.olis.Driver;
import com.indivica.olis.parameters.ZPD1;
import com.indivica.olis.parameters.ZSD;
import com.indivica.olis.queries.ContinuationPointerQuery;
import com.indivica.olis.queries.Query;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.common.model.OscarLog;
import org.oscarehr.olis.OLISProtocolSocketFactory;
import org.oscarehr.olis.OLISUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.OscarProperties;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;
import oscar.oscarLab.ca.all.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProviderOlisSession {
    
    // The LoggedInInfo of the owner of this OLIS session
    private LoggedInInfo sessionOwner;
    // A map of the results for this provider's session, mapped by the provider no of the HIC that the result set is for
    private Map<String, OlisLabResults> requestingHicResultMap = new HashMap<String, OlisLabResults>();

    public ProviderOlisSession(LoggedInInfo sessionOwner) {
        this.sessionOwner = sessionOwner;
    }

    public LoggedInInfo getSessionOwner() {
        return sessionOwner;
    }
    public void setSessionOwner(LoggedInInfo sessionOwner) {
        this.sessionOwner = sessionOwner;
    }

    public Map<String, OlisLabResults> getRequestingHicResultMap() {
        return requestingHicResultMap;
    }
    public void setRequestingHicResultMap(Map<String, OlisLabResults> requestingHicResultMap) {
        this.requestingHicResultMap = requestingHicResultMap;
    }

    public boolean isResultsEmpty() {
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            if (!labResults.getResultList().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public List<OlisLabResultDisplay> getAllResultDisplaysSorted() {
        List<OlisLabResultDisplay> allResults = new ArrayList<OlisLabResultDisplay>();
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            allResults.addAll(labResults.getResultList());
        }
        Collections.sort(allResults, OlisLabResultDisplay.OLIS_LAB_RESULT_DISPLAY_COMPARATOR);
        return allResults;
    }
    public List<OlisLabResultDisplay> getLabResultDisplayByPlacerGroupNo(String placerGroupNo) {
        List<OlisLabResultDisplay> results = new ArrayList<OlisLabResultDisplay>();;
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            for (OlisLabResultDisplay resultDisplay : labResults.getResultList()) {
                if (resultDisplay.getPlacerGroupNo().equals(placerGroupNo)) {
                    results.add(resultDisplay);
                }
            }
        }
        return results;
    }
    
    public boolean hasErrors() {
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            if (!labResults.getResultErrors().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Tests if the resultErrors (if any) in the results have the provided identifier 
     * @param identifiersToMatch The OLIS error identifiers number to check against
     * @return true if found, false otherwise
     */
    public boolean hasErrorWithIdentifiers(List<String> identifiersToMatch) {
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            for (OLISHL7Handler.OLISError error : labResults.getResultErrors()) {
                if (identifiersToMatch.contains(error.getIndentifer())) {
                    return true;
                }
            }
        }
        return false;
    }
    public List<OLISHL7Handler.OLISError> getAllResultErrors() {
        List<OLISHL7Handler.OLISError> allErrors = new ArrayList<OLISHL7Handler.OLISError>();
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            allErrors.addAll(labResults.getResultErrors());
        }
        return allErrors;
    }
    public List<OlisMeasurementsResultDisplay> getAllMeasurementDisplays() {
        List<OlisMeasurementsResultDisplay> allMeasurements = new ArrayList<OlisMeasurementsResultDisplay>();
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            allMeasurements.addAll(labResults.getAllMeasurements());
        }
        return allMeasurements;
    }
    public boolean isHasPatientLevelBlock() {
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            if (labResults.isHasPatientLevelBlock()) {
                return true;
            }
        }
        return false;
    }
    public boolean hasBlockedContent() {
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            if (labResults.isHasBlockedContent()) {
                return true;
            }
        }
        return false;
    }
    public boolean hasPatientConsent() {
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            if (labResults.isHasPatientConsent()) {
                return true;
            }
        }
        return false;
    }
    public boolean hasContinuationPointer() {
        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            if (labResults.getContinuationPointer() != null) {
                return true;
            }
        }
        return false;
    }
    public String getSearchType() {
        // all results are part of the same search so the type is the same across all of them
        return new ArrayList<OlisLabResults>(requestingHicResultMap.values()).get(0).getSearchType();
    }

    /**
     * Create and run the OLIS queries based on the provided queryType and OlisQueryParameters
     * @param queryType The OLIS query type
     * @param queryParameters The query 
     */
    public void doQueries(String queryType, OlisQueryParameters queryParameters) {

        for (String requestingHic : queryParameters.getRequestingHics()) {
            Query query = OLISUtils.createOlisQuery(queryType, sessionOwner, requestingHic, queryParameters);
            try {
                OLISStub.OLISRequest olisRequest = Driver.createOlisRequest(query, sessionOwner.getLoggedInProvider());
                Response olisResponse = performOlisRequest(olisRequest);
                OlisLabResults labResults = new OlisLabResults();
                labResults.setSearchType(queryType);
                labResults.setQueryUsed(query);
			    // Gets the EMR transaction id from the request attributes and sets it to the lab results
                labResults.setEmrTransactionId(olisRequest.getHIALRequest().getClientTransactionID());
                if (olisResponse.getErrors() != null) {
                    labResults.setQueryErrorList(getQueryErrorList(olisResponse));
                } else if (olisResponse.getContent() != null) {
                    String olisResultString = olisResponse.getContent();

                    UUID olisResultFileUuid = UUID.randomUUID();
                    File olisResultFile = new File(System.getProperty("java.io.tmpdir") + "/olis_" + olisResultFileUuid.toString() + ".response");
                    FileUtils.writeStringToFile(olisResultFile, olisResultString);

                    OLISUtils.logTransaction(sessionOwner, query, olisResultString, olisResultFile.getName());
                    
                    processResponseHeaderData(labResults, olisResultString, query);
                    processResponseMessageData(labResults, olisResultString);
                }
                requestingHicResultMap.put(requestingHic, labResults);
            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            }
        }
    }

    /**
     * Reruns the existing OLIS queries
     * @param force Determines if the rerun queries will attempt to rerun with consent information
     * @param queryParameters The parameters for the consent info
     * @param runContinuationQueries if true the queries will be rerun to fetch more info using any existing continuation pointers
     */
    public void redoQueries(boolean force, OlisQueryParameters queryParameters, boolean runContinuationQueries) {
        OscarLogDao logDao = SpringUtils.getBean(OscarLogDao.class);

        for (OlisLabResults labResults : requestingHicResultMap.values()) {
            boolean runQuery = true;
            Query query = labResults.getQueryUsed();
            if (force) {
                // If consent is authorized by a substitute decision maker, adds the necessary information
                // If not then it sets tge ZPD1 to Z for patient consent  
                String authorizedBy = queryParameters.getBlockedInformationIndividual();
                if (StringUtils.trimToEmpty(authorizedBy).equals("substitute")) {
                    // Sets the ZPD1 to X for Substitute Decision Maker
                    query.setConsentToViewBlockedInformation(new ZPD1("X"));
                    // Gets the decision maker's first and last name and their relationship to the patient
                    String firstName = StringUtils.trimToEmpty(queryParameters.getOverrideFirstName());
                    String lastName = StringUtils.trimToEmpty(queryParameters.getOverrideLastName());
                    String relationship = StringUtils.trimToEmpty(queryParameters.getOverrideRelationship());
                    // Sets the substitute decision maker as the ZSD attributes
                    query.setSubstituteDecisionMaker(new ZSD(firstName, lastName, relationship));
                } else {
                    // Sets ZPD1 to Z for Patient Consent
                    query.setConsentToViewBlockedInformation(new ZPD1("Z"));
                }

                String blockedInfoIndividual = queryParameters.getBlockedInformationIndividual();
                // Log the consent override
                OscarLog logItem = new OscarLog();
                logItem.setAction("OLIS search");
                logItem.setContent("consent override");
                logItem.setContentId("demographicNo=" + query.getDemographicNo() + ",givenby=" + blockedInfoIndividual);
                if (sessionOwner != null) {
                    logItem.setProviderNo(sessionOwner.getLoggedInProviderNo());
                }
                else {
                    logItem.setProviderNo("-1");
                }
                logItem.setIp(sessionOwner.getIp());

                logDao.persist(logItem);

            }

            if (!runContinuationQueries && query instanceof ContinuationPointerQuery) {
                // Remove continuation pointer if it exists to re-pull all results
                ((ContinuationPointerQuery) query).setContinuationPointer(null);
            }
            
            
            if (runContinuationQueries && query instanceof ContinuationPointerQuery &&
                    ((ContinuationPointerQuery)query).getContinuationPointer() == null) {
                // Do not run the continuation query if there is no pointer
                runQuery = false;
            }
            
            if (runQuery) {
                try {
                    OLISStub.OLISRequest olisRequest = Driver.createOlisRequest(query, sessionOwner.getLoggedInProvider());
                    Response olisResponse = performOlisRequest(olisRequest);
                    labResults.setQueryUsed(query);
                    // Gets the EMR transaction id from the request attributes and sets it to the lab results
                    labResults.setEmrTransactionId(olisRequest.getHIALRequest().getClientTransactionID());
                    if (olisResponse.getErrors() != null) {
                        labResults.setQueryErrorList(getQueryErrorList(olisResponse));
                    } else if (olisResponse.getContent() != null) {
                        String olisResultString = olisResponse.getContent();

                        UUID olisResultFileUuid = UUID.randomUUID();
                        File olisResultFile = new File(System.getProperty("java.io.tmpdir") + "/olis_" + olisResultFileUuid.toString() + ".response");
                        FileUtils.writeStringToFile(olisResultFile, olisResultString);

                        OLISUtils.logTransaction(sessionOwner, query, olisResultString, olisResultFile.getName());
                        
                        processResponseHeaderData(labResults, olisResultString, query);
                        processResponseMessageData(labResults, olisResultString);
                    }
                } catch (Exception e) {
                	MiscUtils.getLogger().error("Error", e);
                }
            }
        }
    }

    /**
     * Performs an OLIS request using the provided OLISStub.OLISRequest
     * @param olisRequest The request to run
     * @return The Response to the query
     * @throws Exception If there is a connection issue or a provided request parameter is invalid
     */
    private Response performOlisRequest(OLISStub.OLISRequest olisRequest) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", OscarProperties.getInstance().getProperty("olis_truststore").trim());
        System.setProperty("javax.net.ssl.trustStorePassword", OscarProperties.getInstance().getProperty("olis_truststore_password").trim());
        
        String olisRequestURL = OscarProperties.getInstance().getProperty("olis_request_url", "https://olis.ssha.ca/ssha.olis.webservices.ER7/OLIS.asmx");
        OLISStub olis = new OLISStub(olisRequestURL);
        olis._getServiceClient().getOptions().setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, new Protocol("https",(ProtocolSocketFactory)  new OLISProtocolSocketFactory(),443));

        OLISStub.OLISRequestResponse olisResponse = olis.oLISRequest(olisRequest);
        String signedOlisResponse = olisResponse.getHIALResponse().getSignedResponse().getSignedData();
        String unsignedOlisResponse = Driver.unsignData(signedOlisResponse);

        return Driver.unmarshalResponseXml(unsignedOlisResponse);
    }
    
    private List<String> getQueryErrorList(Response response) {
        List<String> errorStringList = new LinkedList<String>();
        // Read all the errors
        List<Error> errorList = response.getErrors().getError();
        for (ca.ssha._2005.hial.Error error : errorList) {
            StringBuilder errorString = new StringBuilder();
            errorString.append("ERROR ").append(error.getNumber()).append(" (").append(error.getSeverity()).append(") : ").append(error.getMessage());
            ArrayOfString details = error.getDetails();
            if (details != null) {
                List<String> detailList = details.getString();
                for (String detail : detailList) {
                    errorString.append("\n").append(detail);
                }
            }
            errorStringList.add(errorString.toString());
        }
        return errorStringList;
    }
    
    private void processResponseHeaderData(OlisLabResults olisLabResults, String olisResultString, Query queryUsed) {
        OLISHL7Handler reportHandler = (OLISHL7Handler) Factory.getHandler("OLIS_HL7", olisResultString);
        if (reportHandler != null) {
            if (reportHandler.hasPatient()) {
                olisLabResults.setDemographicInfo(reportHandler);
            }

            List<OLISHL7Handler.OLISError> errors = reportHandler.getReportErrors();
            List<OLISHL7Handler.OLISError> errorsToRemove = new ArrayList<>();

            boolean hasBlockedContent = false;
            if (errors.size() > 0) {
                olisLabResults.getResultErrors().addAll(errors);
                boolean hasPatientConsent = reportHandler.hasPatientConsent();
                olisLabResults.setHasPatientConsent(hasPatientConsent);
                // Loops through each error
                for (OLISHL7Handler.OLISError error : errors) {
                    // If the error is either 320 or 920, then some of the results have blocked content and are hidden
                    if (error.getIndentifer().equals("320") || error.getIndentifer().equals("920")) {
                        hasBlockedContent = true;
                        // If the error is a 920
                        if (error.getIndentifer().equals("920")) {
                            // If the error is a 920, then sets the patient level block flag so we know which
                            // consent messages to display
                            olisLabResults.setHasPatientLevelBlock(true);
                            // If the error is a 920 error, then we don't need to display the 320 error
                            olisLabResults.setDisplay320Error(false);
                        }

                        // If the patient has consent, then both 320 and 920 errors can be removed from the errors, if there is no patient consent, then checks if we need to display the 320 error
                        if (hasPatientConsent) {
                            errorsToRemove.add(error);
                        }
                    }
                }
            }

            errors.removeAll(errorsToRemove);
            olisLabResults.setHasBlockedContent(hasBlockedContent);
            olisLabResults.setContinuationPointer(reportHandler.getContinuationPointer());
            if (queryUsed instanceof ContinuationPointerQuery) {
                ((ContinuationPointerQuery) queryUsed).setContinuationPointer(reportHandler.getContinuationPointer());
            }
        }
    }

    private void processResponseMessageData(OlisLabResults olisLabResults, String olisResultString) throws IOException {
        ArrayList<String> messages = Utilities.separateMessagesFromResponse(olisResultString);
        List<String> resultList = new LinkedList<String>();

        if (messages != null) {
            for (String message : messages) {

                String resultUuid = UUID.randomUUID().toString();

                File olisResultFile = new File(System.getProperty("java.io.tmpdir") + "/olis_" + resultUuid + ".response");
                FileUtils.writeStringToFile(olisResultFile, message);

                // Parse the HL7 string...
                message = message.replaceAll("\\\\H\\\\", "\\\\.H\\\\");
                message = message.replaceAll("\\\\N\\\\", "\\\\.N\\\\");
                OLISHL7Handler olisResultHandler = (OLISHL7Handler) Factory.getHandler("OLIS_HL7", message);
                if (olisResultHandler.getOBRCount() == 0) {
                    continue;
                }

                resultList.add(resultUuid);
                // Gets the ordering provider number from the report
                String licenceNumber = olisResultHandler.getOrderingProviderNumber();
                // Compares if the report is ordered by the requesting provider
                if (StringUtils.trimToEmpty(sessionOwner.getLoggedInProvider().getPractitionerNo()).equals(licenceNumber)) {
                    // If so, sets the flag to true
                    olisLabResults.setHasRequestingProvider(true);
                }

                olisLabResults.getResultList().addAll(OlisLabResultDisplay.getListFromHandler(olisResultHandler, resultUuid, olisLabResults.getEmrTransactionId()));
            }
        }
    }
}
