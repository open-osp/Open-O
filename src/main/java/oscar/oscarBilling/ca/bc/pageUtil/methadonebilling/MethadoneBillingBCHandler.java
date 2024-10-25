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

package oscar.oscarBilling.ca.bc.pageUtil.methadonebilling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;


import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DiagnosticCodeDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.common.model.Provider;
import oscar.util.UtilDateUtilities;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONObject;
import oscar.OscarProperties;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingNote;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager;
import oscar.oscarBilling.ca.bc.pageUtil.BillingBillingManager.BillingItem;
import oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean;;

/**
 * @author OSCARprn by Treatment - support@oscarprn.com
 * @Company OSCARprn by Treatment
 * @Date Nov 30, 2012
 * @Filename MethadoneBillingBCHandler.java
 * @Comment Copy Right OSCARprn by Treatment
 */
public class MethadoneBillingBCHandler {

	// full logging to be added later. too pressed for time.
	// private static Log log = LogFactory.getLog(BillingSaveBillingAction.class);
	private static final Logger log = MiscUtils.getLogger();

	// default attributes for MSP billing.
	// create new attributes for dynamic form input.
	public static final String PAYMENT_TYPE_NAME = "ELECTRONIC";
	public static final String PAYMENT_TYPE = "6";
	public static final Integer APPOINTMENT_NO = 0;
	public static final String PAYMENT_MODE = "0";
	public static final String CLAIM_CODE = "C02";
	public static final String ANATOMICAL_AREA = "00";
	public static final String NEW_PROGRAM = "00";
	public static final String BILL_REGION = "BC";
	public static final String SUBMISSION_CODE = "0";
	public static final String CORRESPONDENCE_CODE = "0";
	public static final String BILLING_PROV = "BC";
	public static final String MVA_CLAIM_CODE = "N";
	public static final String DEPENDENT_CODE = "00";
	public static final String INTERNAL_COMMENT = "MSP Billing done by methadone billing method.";
	public static final String AFTERHOUR_CODE = "0";
	public static final String ADMISSION_DATE = "0000-00-00";
	public static final String BILLINGVISIT = "A";

	public static final String BILLING_UNIT = "1";
	public static final String HALF_BILLING = "0.5";

	private Date today;
	private MethadoneBillingBCFormBean methadoneBillingBCFormBean;
	private DemographicDao demographicDao;
	private ProviderDao providerDao;
	private Properties oscarProperties;
	private BillingBillingManager bmanager;
	private Billing billing;
	private BillingmasterDAO billingmasterDAO;
    private BillingHistoryDAO billingHistoryDAO;
    private DiagnosticCodeDao diagnosticCodeDao;
	private Billingmaster billingmaster;
	private int numberSaved;

	/**
	 * Default constructor.
	 */
	public MethadoneBillingBCHandler(){

		this.today = new Date();
    	demographicDao = (DemographicDao) SpringUtils.getBean(DemographicDao.class);
    	providerDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);
    	oscarProperties = OscarProperties.getInstance();
    	bmanager = new BillingBillingManager();
    	billing = new Billing();
		billingmasterDAO = (BillingmasterDAO) SpringUtils.getBean(BillingmasterDAO.class);
		billingHistoryDAO = new BillingHistoryDAO();
		diagnosticCodeDao = (DiagnosticCodeDao) SpringUtils.getBean(DiagnosticCodeDao.class);

	}

	/**
	 * Instantiate a new handler with fresh form bean data.
	 */
	public MethadoneBillingBCHandler(MethadoneBillingBCFormBean methadoneBillingBCFormBean) {

		this.methadoneBillingBCFormBean = methadoneBillingBCFormBean;

		this.today = new Date();
    	demographicDao = (DemographicDao) SpringUtils.getBean(DemographicDao.class);
    	providerDao = (ProviderDao) SpringUtils.getBean(ProviderDao.class);
    	oscarProperties = OscarProperties.getInstance();
    	bmanager = new BillingBillingManager();
    	billing = new Billing();
		billingmasterDAO = (BillingmasterDAO) SpringUtils.getBean(BillingmasterDAO.class);
		billingHistoryDAO = new BillingHistoryDAO();

		diagnosticCodeDao = (DiagnosticCodeDao) SpringUtils.getBean(DiagnosticCodeDao.class);

	}

	/**
	 *
	 * @return Demographic Data Access Object
	 */
	public DemographicDao getDemographicDao() {
		return demographicDao;
	}

	/**
	 *
	 * @return Provider Data Access Object
	 */
	public ProviderDao getProviderDao() {
		return providerDao;
	}

	/**
	 *
	 * @return Oscar Properties Object
	 */
	public Properties getOscarProperties() {
		return oscarProperties;
	}


	/**
	 * The number of invoices saved in the last
	 * session.
	 * @return int
	 */
	public int getNumberSaved() {
		return numberSaved;
	}

	/**
	 * set the number of invoices saved in this session.
	 * @param numberSaved
	 */
	private void setNumberSaved(int numberSaved) {
		this.numberSaved = numberSaved;
	}

	/**
	 * Reset the form and session objects for
	 * fresh entries.
	 */
	public void reset() {
    	methadoneBillingBCFormBean.setIsHeaderSet(false);
    	methadoneBillingBCFormBean.setBillingProvider("");
    	methadoneBillingBCFormBean.setBillingProviderNo("");
    	methadoneBillingBCFormBean.setServiceDate("");
    	methadoneBillingBCFormBean.setRosterStatus("");
    	methadoneBillingBCFormBean.setPatientStatus("");
    	methadoneBillingBCFormBean.setBillingData(new ArrayList<BillingSessionBean>());
    	methadoneBillingBCFormBean.setDemographics(new ArrayList<Demographic>());

	}

	/**
	 * Set the header data for this group of billings.
	 * Header consists of a provider, service location, and service date and
	 * is the header for a group of individual patients with the header data
	 * in common.
	 *
	 * @param billingEntry
	 */
	public void setHeader(JSONObject billingEntry) {

		String visitLocation = billingEntry.getString("visitLocation");
		String visitDate = billingEntry.getString("visitDate");
		String providerNo = billingEntry.getString("provider");
		String creator = billingEntry.getString("creator");

    	// if any of the variables are empty
		if( (!providerNo.equals("empty"))||
				(!visitLocation.equals("empty")) ||
				(!visitDate.equals(""))
		) {


    		// set the header data in the quickBillingbean. Only happens on the first add.
			// Then they are set and used during the entire session.
			this.methadoneBillingBCFormBean.setBillingProviderNo(providerNo);
    		this.methadoneBillingBCFormBean.setBillingProvider(
    				providerDao.getProviderName(providerNo)
    		);

    		this.methadoneBillingBCFormBean.setServiceDate(visitDate);
    		this.methadoneBillingBCFormBean.setCreator(creator);

		}
	}

	/**
     * Create a new billing object to be stored into an array
     * for later processing.
     * Lots of data here is hard coded.
     * @param data
     */
	public boolean addBill(Demographic demographic, String billingType) {

		String providerNo = this.methadoneBillingBCFormBean.getBillingProviderNo();
		BillingSessionBean bean = new BillingSessionBean();
		String unit = BILLING_UNIT;
		Provider provider = null;
		String billingCode = "";
		String dxCode = "";

		if (this.methadoneBillingBCFormBean.getRosterStatus().equals(oscarProperties.getProperty("methadoneRosterStatus"))) {
			billingCode = oscarProperties.getProperty("methadoneBillingCode"+billingType);
			dxCode = oscarProperties.getProperty("methadoneDxCode"+billingType);
		} else if (this.methadoneBillingBCFormBean.getRosterStatus().equals(oscarProperties.getProperty("suboxoneRosterStatus"))) {
			billingCode = oscarProperties.getProperty("suboxoneBillingCode"+billingType);
			dxCode = oscarProperties.getProperty("suboxoneDxCode"+billingType);
		} else if (this.methadoneBillingBCFormBean.getRosterStatus().equals(oscarProperties.getProperty("sromRosterStatus"))) {
			billingCode = oscarProperties.getProperty("sromBillingCode"+billingType);
			dxCode = oscarProperties.getProperty("sromDxCode"+billingType);
		}

		if(!providerNo.isEmpty()) {
			provider = this.providerDao.getProvider(providerNo);
		}

		// first take care of the service codes.
		String[] service = new String[0]; // not sure what this does, but pressed for time soooo...

		ArrayList<BillingItem> billItem = bmanager.getDups2(
				service,
				billingCode,
				"",
				"",
				unit,
				"",
				""
		);

		// diagnostic codes.
		List<DiagnosticCode> diagnosticCodes = diagnosticCodeDao.getByDxCode(dxCode);
		if (diagnosticCodes.size() > 0) {
			bean.setDx1(dxCode);
		} else {
			bean.setDx1("");
			log.warn("No diagnostic code for: " + dxCode);
		}
		bean.setDx2(""); //(!billingEntry.getString("dxCode2").isEmpty()) ? billingEntry.getString("dxCode2") : "");
		bean.setDx3(""); //(!billingEntry.getString("dxCode3").isEmpty()) ? billingEntry.getString("dxCode3") : "");

		// billing codes.
		bean.setGrandtotal(bmanager.getGrandTotal(billItem));
		bean.setBillItem(billItem);
		bean.setSubmissionCode(SUBMISSION_CODE);
		//bean.setFacilityNum(null);
		//bean.setFacilitySubNum(null);
		bean.setPaymentTypeName(PAYMENT_TYPE_NAME);
		bean.setApptNo(APPOINTMENT_NO.toString());
		bean.setBillRegion(BILL_REGION);

		// demographic data
		bean.setPatientNo(demographic.getDemographicNo().toString());
		bean.setPatientLastName(demographic.getLastName());
		bean.setPatientFirstName(demographic.getFirstName());
		bean.setPatientName(demographic.getLastName()+", "+demographic.getFirstName());
		bean.setPatientDoB(convertDate8Char(demographic.getFormattedDob()));
		bean.setPatientAddress1(demographic.getAddress());
		bean.setPatientAddress2(demographic.getCity());
		bean.setPatientPostal(demographic.getPostal());
		bean.setPatientSex(demographic.getSex());
		bean.setPatientPHN(demographic.getHin());
		bean.setPatientHCType(demographic.getHcType());
		bean.setPatientAge(demographic.getAge());

		// billing settings
		bean.setBillingType(billingType); // billing account status = O
		bean.setPaymentType(PAYMENT_TYPE);
		bean.setEncounter(PAYMENT_MODE);
		//bean.setWcbId(null);

		// visit information
		bean.setVisitType(BILLING_PROV);
		bean.setVisitLocation(oscarProperties.getProperty("visitlocation")); //global location also sets the clarification code.
		bean.setServiceDate(convertDate8Char(methadoneBillingBCFormBean.getServiceDate()));
		//bean.setStartTimeHr(null);
		//bean.setStartTimeMin(null);
		//bean.setEndTimeHr(null);
		//bean.setEndTimeMin(null);
		bean.setAdmissionDate(ADMISSION_DATE);

		// provider data for billing
		// aka: ohip number, billing number, practitioner number, payee...
		bean.setBillingProvider(provider.getBillingNo());
		bean.setBillingPracNo(provider.getOhipNo());
		bean.setBillingGroupNo(null);

		bean.setCreator(methadoneBillingBCFormBean.getCreator());

		bean.setApptProviderNo(provider.getProviderNo());

		bean.setReferral1("");
		bean.setReferral2("");
		bean.setReferType1("");
		bean.setReferType2("");

		// codes are 0=no notes n=external c=internal b=both
		bean.setCorrespondenceCode(CORRESPONDENCE_CODE);

		// notes which are sent to msp and seen by recipient.
		//bean.setNotes(null);
		bean.setDependent(DEPENDENT_CODE);
		//bean.setAfterHours(null);
		//bean.setTimeCall(null);
		//bean.setShortClaimNote(null);
		//bean.setService_to_date(null);
		//bean.setIcbc_claim_no(null);
		bean.setMva_claim_code(MVA_CLAIM_CODE);

		// notes which are internal - NOT seen by recipient.
		bean.setMessageNotes(INTERNAL_COMMENT);

		return (this.methadoneBillingBCFormBean.getBillingData()).add(bean);
	}

	/**
	 * Triggers exsisting class: BillingSaveBillingAction to recursivley save the bills array list.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean saveBills(char billAccountStatus) {

		String dataCenterId = oscarProperties.getProperty("dataCenterId");
		ArrayList<BillingSessionBean> billingSessionBeans = methadoneBillingBCFormBean.getBillingData();
		ListIterator<BillingSessionBean> it = billingSessionBeans.listIterator();
		int invoiceCount = billingSessionBeans.size();

		while(it.hasNext()) {
			BillingSessionBean billingSessionBean = it.next();

			billing = getBillingObj(billingSessionBean, billAccountStatus);

			ArrayList<BillingItem> billItems = billingSessionBean.getBillItem();
			for(BillingItem item : billItems) {

				// save indivdual bills.
	            //billing.setBillingNo();
	            billingmasterDAO.save(billing);
	            String billingid = ""+billing.getId();
	            billingmaster = saveBill(
	            		billingid,
	            		billing.getStatus(),
	            		dataCenterId,
	            		item.getDispLineTotal(),
	            		""+PAYMENT_MODE,
	            		billingSessionBean,
	            		"" + item.getUnit()  ,
	            		"" + item.getServiceCode()
	            );

	            billingmasterDAO.save(billingmaster);

	            // billing history entry
	            String billingMasterNo = "" + billingmaster.getBillingmasterNo();

	            billingHistoryDAO.createBillingHistoryArchive(billingMasterNo);
	            BillingNote billingNote = new BillingNote();
                billingNote.addNote(billingMasterNo, billingSessionBean.getCreator(), billingSessionBean.getMessageNotes());
			}

		}
		setNumberSaved(invoiceCount);
		return true;

	}

	/**
	 * remove selected bill from the bill arraylist stored in the quickBillingBCFormBean
	 * @param bill
	 */
	public boolean removeBill(String bill) {

		ArrayList<BillingSessionBean> billingSessionBeans = methadoneBillingBCFormBean.getBillingData();
		billingSessionBeans.remove(Integer.parseInt(bill));
		return true;
	}

	/**
	 * Class borrowed from BillingSaveBillingAction
	 * @param bean
	 * @param curDate
	 * @param billingAccountStatus
	 * @return
	 */
    private Billing getBillingObj(BillingSessionBean bean, char billingAccountStatus){

        Billing bill = new Billing();

        bill.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
        bill.setProviderNo(bean.getApptProviderNo());
        bill.setAppointmentNo(APPOINTMENT_NO);
        bill.setDemographicName(bean.getPatientName());
        bill.setHin(bean.getPatientPHN());
        bill.setUpdateDate(today);
        bill.setBillingDate(UtilDateUtilities.StringToDate(bean.getServiceDate(), "yyyyMMdd"));
        bill.setTotal(bean.getGrandtotal());
        bill.setStatus(""+billingAccountStatus);
        bill.setDob(bean.getPatientDoB());
        bill.setVisitDate(UtilDateUtilities.StringToDate(bean.getServiceDate(), "yyyyMMdd"));
        bill.setVisitType(bean.getVisitType());
        bill.setProviderOhipNo(bean.getBillingPracNo());
        bill.setApptProviderNo(bean.getApptProviderNo());
        bill.setCreator(bean.getCreator());
        bill.setBillingtype(bean.getBillingType());

        return bill;
    }

    /**
     * Again method borrowed from BillingSaveBillingAction.
     *
     * @param billingid
     * @param billingAccountStatus
     * @param dataCenterId
     * @param billedAmount
     * @param paymentMode
     * @param bean
     * @param billingUnit
     * @param serviceCode
     * @return
     */
     private Billingmaster saveBill(
    		String billingid,
    		String billingAccountStatus,
    		String dataCenterId,
    		String billedAmount,
    		String paymentMode,
    		BillingSessionBean bean,
    		String billingUnit,
    		String serviceCode
    ) {
        Billingmaster bill = new Billingmaster();

        bill.setBillingNo(Integer.parseInt(billingid));
        bill.setCreatedate(today);
        bill.setBillingstatus(billingAccountStatus);
        bill.setDemographicNo(Integer.parseInt(bean.getPatientNo()));
        bill.setAppointmentNo(Integer.parseInt(bean.getApptNo()));
        bill.setClaimcode(CLAIM_CODE);
        bill.setDatacenter(dataCenterId);
        bill.setPayeeNo(bean.getBillingProvider());
        bill.setPractitionerNo(bean.getBillingPracNo());
        bill.setPhn(bean.getPatientPHN());

        bill.setNameVerify(bean.getPatientFirstName(),bean.getPatientLastName());
        bill.setDependentNum(bean.getDependent());
        bill.setBillingUnit(billingUnit); //"" + billItem.getUnit());
        bill.setClarificationCode(bean.getVisitLocation().substring(0, 2));

        bill.setAnatomicalArea(ANATOMICAL_AREA);
        bill.setAfterHour(AFTERHOUR_CODE);

        bill.setNewProgram(NEW_PROGRAM);
        bill.setBillingCode(serviceCode);//billItem.getServiceCode());
        bill.setBillAmount(billedAmount);

        bill.setPaymentMode(paymentMode);

        bill.setServiceDate(convertDate8Char(bean.getServiceDate()));
        bill.setServiceToDay(bean.getService_to_date());

        bill.setSubmissionCode(bean.getSubmissionCode());

        bill.setExtendedSubmissionCode(" ");
        bill.setDxCode1(bean.getDx1());
        bill.setDxCode2(bean.getDx2());
        bill.setDxCode3(bean.getDx3());
        bill.setDxExpansion(" ");

        bill.setServiceLocation(BILLINGVISIT);
        bill.setReferralFlag1(bean.getReferType1());
        bill.setReferralNo1(bean.getReferral1());
        bill.setReferralFlag2(bean.getReferType2());
        bill.setReferralNo2(bean.getReferral2());
        bill.setTimeCall(bean.getTimeCall());
        bill.setServiceStartTime(bean.getStartTime());
        bill.setServiceEndTime(bean.getEndTime());
        bill.setBirthDate(convertDate8Char(bean.getPatientDoB()));
        bill.setOfficeNumber("");
        bill.setCorrespondenceCode(bean.getCorrespondenceCode());
        bill.setClaimComment(bean.getShortClaimNote());
        bill.setMvaClaimCode(bean.getMva_claim_code());
        bill.setIcbcClaimNo(bean.getIcbc_claim_no());
        bill.setFacilityNo(bean.getFacilityNum());
        bill.setFacilitySubNo(bean.getFacilitySubNum());

        bill.setPaymentMethod(Integer.parseInt(bean.getPaymentType()));

        if (!bean.getPatientHCType().trim().equals(bean.getBillRegion().trim())) {

            bill.setOinInsurerCode(bean.getPatientHCType());
            bill.setOinRegistrationNo(bean.getPatientPHN());
            bill.setOinBirthdate(convertDate8Char(bean.getPatientDoB()));
            bill.setOinFirstName(bean.getPatientFirstName());
            bill.setOinSecondName(" ");
            bill.setOinSurname(bean.getPatientLastName());
            bill.setOinSexCode(bean.getPatientSex());
            bill.setOinAddress(bean.getPatientAddress1());
            bill.setOinAddress2(bean.getPatientAddress2());
            bill.setOinAddress3("");
            bill.setOinAddress4("");
            bill.setOinPostalcode(bean.getPatientPostal());

            bill.setPhn("0000000000");
            bill.setNameVerify("0000");
            bill.setDependentNum("00");
            bill.setBirthDate("00000000");

        }

        return bill;
    }


    /**
     * UTILITY METHOD -REALLY SHOULDN'T BE IN HERE...
     * But I made it static - unlike others.
     * @param s
     * @return
     */
    public static String convertDate8Char(String s) {
        String  sdate = "00000000", syear = "", smonth = "", sday = "";
       log.debug("s=" + s);
       if (s != null) {

           if (s.indexOf("-") != -1) {

               syear = s.substring(0, s.indexOf("-"));
               s = s.substring(s.indexOf("-") + 1);
               smonth = s.substring(0, s.indexOf("-"));
               if (smonth.length() == 1) {
                   smonth = "0" + smonth;
               }
               s = s.substring(s.indexOf("-") + 1);
               sday = s;
               if (sday.length() == 1) {
                   sday = "0" + sday;
               }

               log.debug("Year" + syear + " Month" + smonth + " Day" + sday);
               sdate = syear + smonth + sday;

           } else {
               sdate = s;
           }
           log.debug("sdate:" + sdate);
       } else {
           sdate = "00000000";

       }
       return sdate;
   }




}
