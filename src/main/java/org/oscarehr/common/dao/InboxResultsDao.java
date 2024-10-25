/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 *
 * Modifications made by Magenta Health in 2024.
 */

 package org.oscarehr.common.dao;

 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.Date;
 import java.util.List;
 
 import javax.persistence.EntityManager;
 import javax.persistence.PersistenceContext;
 import javax.persistence.Query;
 
 import org.apache.http.impl.cookie.DateUtils;
 import org.apache.logging.log4j.Logger;
 import org.oscarehr.common.model.SystemPreferences;
 import org.oscarehr.util.SpringUtils;
 
 import oscar.oscarLab.ca.on.LabResultData;
 import oscar.util.StringUtils;
 
 public interface InboxResultsDao {

    public ArrayList populateHL7ResultsData(String demographicNo, String consultationId, boolean attached);
 
    public boolean isSentToProvider(String docNo, String providerNo) ;
     
    public ArrayList populateDocumentResultsData(String providerNo, String demographicNo, String patientFirstName,
             String patientLastName, String patientHealthNumber, String status);
 
    public ArrayList<LabResultData> populateDocumentResultsData(String providerNo, String demographicNo, String patientFirstName,
             String patientLastName, String patientHealthNumber, String status, boolean isPaged, Integer page,
             Integer pageSize, boolean mixLabsAndDocs, Boolean isAbnormal);
     
    public ArrayList<LabResultData> populateDocumentResultsData(String providerNo, String demographicNo, String patientFirstName,
             String patientLastName, String patientHealthNumber, String status, boolean isPaged, Integer page,
             Integer pageSize, boolean mixLabsAndDocs, Boolean isAbnormal, Date startDate, Date endDate);
 
    //String getStringValue(Object value);
 }
 