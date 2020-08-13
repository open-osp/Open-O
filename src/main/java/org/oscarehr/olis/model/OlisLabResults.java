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

import com.indivica.olis.queries.Query;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OlisLabResults {
    private String searchType = "";
    private Query queryUsed = null;
    private List<String> queryErrorList = new ArrayList<String>();
    private List<OlisLabResultDisplay> resultList = new ArrayList<OlisLabResultDisplay>();
    private List<OLISHL7Handler.OLISError> resultErrors =  new ArrayList<OLISHL7Handler.OLISError>();
    private boolean hasBlockedContent = false;
    private boolean hasRequestingProvider = false;
    private boolean hasPatientLevelBlock = false;
    private boolean display320Error = true;
    private boolean hasPatientConsent = true;
    private String continuationPointer = null;
    private String emrTransactionId = null;
    
    private String demographicName = "";
    private String demographicHin = "";
    private String demographicMrn = "";
    private String demographicSex = "";
    private String demographicDob = "";

    public OlisLabResults() { }

    public String getSearchType() {
        return searchType;
    }
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Query getQueryUsed() {
        return queryUsed;
    }
    public void setQueryUsed(Query queryUsed) {
        this.queryUsed = queryUsed;
    }

    public List<String> getQueryErrorList() {
        return queryErrorList;
    }
    public void setQueryErrorList(List<String> queryErrorList) {
        this.queryErrorList = queryErrorList;
    }

    public List<OlisLabResultDisplay> getResultList() {
        return resultList;
    }
    public List<OlisLabResultDisplay> getResultListSorted() {
        Collections.sort(resultList, OlisLabResultDisplay.OLIS_LAB_RESULT_DISPLAY_COMPARATOR);
        return resultList;
    }
    public void setResultList(List<OlisLabResultDisplay> resultList) {
        this.resultList = resultList;
    }

    public List<OlisMeasurementsResultDisplay> getAllMeasurements() {
        List<OlisMeasurementsResultDisplay> results = new ArrayList<OlisMeasurementsResultDisplay>();
        for (OlisLabResultDisplay olisLabResultDisplay : getResultList()) {
            results.addAll(olisLabResultDisplay.getMeasurements());
        }
        Collections.sort(results, OlisMeasurementsResultDisplay.DEFAULT_OLIS_SORT_COMPARATOR);
        return results;
    }

    public List<OLISHL7Handler.OLISError> getResultErrors() {
        return resultErrors;
    }
    public void setResultErrors(List<OLISHL7Handler.OLISError> resultErrors) {
        this.resultErrors = resultErrors;
    }

    /**
     * Tests if the resultErrors (if any) in the results have the provided identifier 
     * @param identifierToMatch The OLIS error identifier number to check against
     * @return true if found, false otherwise
     */
    public boolean hasErrorWithIdentifier(String identifierToMatch) {
        for (OLISHL7Handler.OLISError error : resultErrors) {
            if (identifierToMatch.equals(error.getIndentifer())) {
                return true;
            }
        }
        return false;
    }

    public boolean isHasBlockedContent() {
        return hasBlockedContent;
    }
    public void setHasBlockedContent(boolean hasBlockedContent) {
        this.hasBlockedContent = hasBlockedContent;
    }

    public boolean isHasPatientLevelBlock() {
        return hasPatientLevelBlock;
    }
    public void setHasPatientLevelBlock(boolean hasPatientLevelBlock) {
        this.hasPatientLevelBlock = hasPatientLevelBlock;
    }

    public boolean isHasRequestingProvider() {
        return hasRequestingProvider;
    }
    public void setHasRequestingProvider(boolean hasRequestingProvider) {
        this.hasRequestingProvider = hasRequestingProvider;
    }

    public boolean isDisplay320Error() {
        return display320Error;
    }
    public void setDisplay320Error(boolean display320Error) {
        this.display320Error = display320Error;
    }

    public boolean isHasPatientConsent() {
        return hasPatientConsent;
    }
    public void setHasPatientConsent(boolean hasPatientConsent) {
        this.hasPatientConsent = hasPatientConsent;
    }

    public String getContinuationPointer() {
        return continuationPointer;
    }

    public void setContinuationPointer(String continuationPointer) {
        this.continuationPointer = continuationPointer;
    }

    public String getEmrTransactionId() {
        return emrTransactionId;
    }
    public void setEmrTransactionId(String emrTransactionId) {
        this.emrTransactionId = emrTransactionId;
    }

    public String getDemographicName() {
        return demographicName;
    }
    public void setDemographicName(String demographicName) {
        this.demographicName = demographicName;
    }

    public String getDemographicHin() {
        return demographicHin;
    }
    public void setDemographicHin(String demographicHin) {
        this.demographicHin = demographicHin;
    }

    public String getDemographicMrn() {
        return demographicMrn;
    }
    public void setDemographicMrn(String demographicMrn) {
        this.demographicMrn = demographicMrn;
    }

    public String getDemographicSex() {
        return demographicSex;
    }
    public void setDemographicSex(String demographicSex) {
        this.demographicSex = demographicSex;
    }

    public String getDemographicDob() {
        return demographicDob;
    }
    public void setDemographicDob(String demographicDob) {
        this.demographicDob = demographicDob;
    }
    
    public void setDemographicInfo(OLISHL7Handler reportHandler) {
        this.setDemographicName(reportHandler.getPatientName());
        if (reportHandler.getPatientIdentifier("JHN") != null && reportHandler.getPatientIdentifier("JHN").length > 0) {
            this.setDemographicHin(reportHandler.getPatientIdentifier("JHN")[0]);
        }
        if (reportHandler.getPatientIdentifier("MR") != null && reportHandler.getPatientIdentifier("MR").length > 1) {
            String[] identifiers = reportHandler.getPatientIdentifier("MR");
            String hospital = reportHandler.getSourceOrganization(identifiers[1]);
            this.setDemographicMrn(identifiers[0] + " " + hospital + " (Lab " + identifiers[1] + ")");
        }
        this.setDemographicSex(reportHandler.getSex());
        this.setDemographicDob(reportHandler.getDOB());
    }
}
