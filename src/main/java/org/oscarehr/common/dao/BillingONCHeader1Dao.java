/**
 * Copyright (c) 2024. Magenta Health. All Rights Reserved.
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 *
 * Modifications made by Magenta Health in 2024.
 */

package org.oscarehr.common.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.utility.DateUtils;
import org.oscarehr.billing.CA.ON.model.BillingPercLimit;
import org.oscarehr.billing.CA.dao.GstControlDao;
import org.oscarehr.billing.CA.model.GstControl;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.DateRange;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oscar.OscarProperties;
import oscar.oscarBilling.ca.on.data.BillingDataHlp;
import oscar.oscarBilling.ca.on.pageUtil.BillingStatusPrep;
import oscar.util.ParamAppender;

/**
 *
 * @author Eugene Katyukhin
 */

public interface BillingONCHeader1Dao extends AbstractDao<BillingONCHeader1> {

    public List<BillingONCHeader1> getBillCheader1ByDemographicNo(int demographic_no);

    public int getNumberOfDemographicsWithInvoicesForProvider(String providerNo, Date startDate, Date endDate,
            boolean distinct);

    public void createBills(List<BillingONCHeader1> lBills);

    public String createBill(String provider, Integer demographic, String code, String clinicRefCode, Date serviceDate,
            String curUser);

    public String createBill(String provider, Integer demographic, String code, String dxCode, String clinicRefCode,
            Date serviceDate, String curUser);

    public String createBills(String provider, List<String> demographic_nos, List<String> codes, List<String> dxcodes,
            String clinicRefCode, Date serviceDate, String curUser);

    public boolean billedBetweenTheseDays(String serviceCode, Integer demographicNo, Date startDate, Date endDate);

    public int getDaysSinceBilled(String serviceCode, Integer demographicNo);

    public int getDaysSincePaid(String serviceCode, Integer demographic_no);

    public List<BillingONCHeader1> getInvoices(Integer demographicNo, Integer limit);

    public List<BillingONCHeader1> getInvoices(Integer demographicNo);

    public List<BillingONCHeader1> getInvoicesByIds(List<Integer> ids);

    public List<Map<String, Object>> getInvoicesMeta(Integer demographicNo);

   // public GstControlDao getGstControlDao();

   // public void setGstControlDao(GstControlDao gstControlDao);

    public BillingONItem findBillingONItemByServiceCode(BillingONCHeader1 ch1, String serviceCode);

    public List<BillingONCHeader1> get3rdPartyInvoiceByProvider(Provider p, Date start, Date end, Locale locale);

    public List<BillingONCHeader1> get3rdPartyInvoiceByDate(Date start, Date end, Locale locale);

    public BillingONCHeader1 getLastOHIPBillingDateForServiceCode(Integer demographicNo, String serviceCode);

    public List<BillingONCHeader1> findByAppointmentNo(Integer appointmentNo);

    public List<Object[]> countBillingVisitsByProvider(String providerNo, Date dateBegin, Date dateEnd);

    public List<Object[]> countBillingVisitsByCreator(String providerNo, Date dateBegin, Date dateEnd);

    public List<Long> count_larrykain_clinic(String facilityNum, Date startDate, Date endDate);

    public List<Long> count_larrykain_hospital(String facilityNum1, String facilityNum2, String facilityNum3,
            String facilityNum4, Date startDate, Date endDate);

    public List<Long> count_larrykain_other(String facilityNum1, String facilityNum2, String facilityNum3,
            String facilityNum4, String facilityNum5, Date startDate, Date endDate);

    public List<BillingONCHeader1> findBillingsByManyThings(String status, String providerNo, Date startDate,
            Date endDate, Integer demoNo);

    public List<BillingONCHeader1> findByProviderStatusAndDateRange(String providerNo, List<String> statuses,
            DateRange dateRange);

    public List<Object[]> findBillingsAndDemographicsById(Integer id);

    public List<BillingONCHeader1> findByMagic(List<String> payPrograms, String statusType, String providerNo,
            Date startDate, Date endDate, Integer demoNo, String visitLocation, Date paymentStartDate,
            Date paymentEndDate);

    public List<BillingONCHeader1> getBillingItemByDxCode(Integer demographicNo, String dxCode);

    public List<Object[]> findByMagic2(List<String> payPrograms, String statusType, String providerNo, Date startDate,
            Date endDate, Integer demoNo, List<String> serviceCodes, String dx, String visitType, String visitLocation,
            Date paymentStartDate, Date paymentEndDate);

    public List<Object[]> findByMagic2(List<String> payPrograms, String statusType, String providerNo, Date startDate,
            Date endDate, Integer demoNo, List<String> serviceCodes, String dx, String visitType, String visitLocation,
            Date paymentStartDate, Date paymentEndDate, String claimNo);

    public List<BillingONCHeader1> findByDemoNo(Integer demoNo, int iOffSet, int pageSize);

    public List<BillingONCHeader1> findByDemoNoAndDates(Integer demoNo, DateRange dateRange, int iOffSet, int pageSize);

    public List<Object[]> findBillingsAndDemographicsByDemoIdAndDates(Integer demoNo, String payProgram, Date fromDate,
            Date toDate);

    public List<Object[]> findDemographicsAndBillingsByDxAndServiceDates(List<String> dxCodes, Date from, Date to);

    public List<BillingONCHeader1> findBillingsByDemoNoCh1HeaderServiceCodeAndDate(Integer demoNo,
            List<String> serviceCodes, Date from, Date to);

    public List<String[]> findBillingData(String conditions);

    public List<BillingONCHeader1> findAllByPayProgram(String payProgram, int startIndex, int limit);

}
