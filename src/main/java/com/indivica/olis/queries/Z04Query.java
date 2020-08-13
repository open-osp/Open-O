/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package com.indivica.olis.queries;

import java.util.List;

import com.indivica.olis.parameters.OBR22;
import com.indivica.olis.parameters.OBR4;
import com.indivica.olis.parameters.OBX3;
import com.indivica.olis.parameters.QRD7;
import com.indivica.olis.parameters.ZPD1;
import com.indivica.olis.parameters.ZRP1;
import com.indivica.olis.parameters.ZSD;

/**
 * Z04 - Retrieve Laboratory Information Updates for Practitioner
 * @author jen
 *
 */
public class Z04Query extends Query implements ContinuationPointerQuery, RequestingHicQuery {

	private OBR22 startEndTimestamp = new OBR22(); // mandatory
	private QRD7 quantityLimitedRequest = null;
	private ZRP1 requestingHic = new ZRP1(); // mandatory
	private OBR4 testRequestCodes = new OBR4("HL79901");
	private OBX3 testResultCodes = new OBX3("HL79902");
	private String continuationPointer = null;
	
	@Override
	public String getQueryHL7String() {
		String query = "";
		
		if (startEndTimestamp != null)
			query += startEndTimestamp.toOlisString() + "~";
		
		if (quantityLimitedRequest != null)
			query += quantityLimitedRequest.toOlisString() + "~";
		
		if (requestingHic != null)
			query += requestingHic.toOlisString() + "~";
		
		if (testRequestCodes.hasCodes()) {
			query += testRequestCodes.toOlisString() + "~";
		}
	
		if (testResultCodes.hasCodes()) {
			query += testResultCodes.toOlisString() + "~";
		}
		
		if(query.endsWith("~")) {
			query = query.substring(0,query.length()-1);
		}
		return query;
	}

	public void setStartEndTimestamp(OBR22 startEndTimestamp) {
    	this.startEndTimestamp = startEndTimestamp;
    }

	public void setQuantityLimitedRequest(QRD7 quantityLimitedRequest) {
    	this.quantityLimitedRequest = quantityLimitedRequest;
    }

	public void setRequestingHic(ZRP1 requestingHic) {
    	this.requestingHic = requestingHic;
    }

	public void addToTestRequestCodeList(String testRequestCode) {
		this.testRequestCodes.addValue(testRequestCode);
	}
	
	public void addAllToTestRequestCodeList(List<String> testRequestCodes) {
		this.testRequestCodes.addAllValues(testRequestCodes);
	}
	
	public void addToTestResultCodeList(String testResultCode) {
		this.testResultCodes.addValue(testResultCode);
	}
	
	public void addAllToTestResultCodeList(List<String> testResultCodeList) {
		this.testResultCodes.addAllValues(testResultCodeList);
	}

	public String getContinuationPointer() {
		return continuationPointer;
	}

	public void setContinuationPointer(String continuationPointer) {
		this.continuationPointer = continuationPointer;
	}
	
	public String getRequestingHicIdNumber() {
		return requestingHic.getIdNumber();
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.Z04;
	}
	@Override
    public void setConsentToViewBlockedInformation(ZPD1 consentToViewBlockedInformation) {
		throw new RuntimeException("Not valid for this type of query.");
    }

	@Override
	public void setSubstituteDecisionMaker(ZSD substituteDecisionMaker) {
		throw new RuntimeException("Not valid for this type of query.");
    }

	public boolean hasConsentOverride() {
		return false;
	}
    
    public String getRequestingHicId() {
		return requestingHic.getIdNumber();
	}
}
