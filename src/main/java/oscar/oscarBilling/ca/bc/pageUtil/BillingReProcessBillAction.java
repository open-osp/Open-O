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
package oscar.oscarBilling.ca.bc.pageUtil;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.Misc;
import oscar.MyDateFormat;
import oscar.OscarProperties;
import oscar.entities.Billingmaster;
import oscar.oscarBilling.ca.bc.MSP.MSPBillingNote;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.bc.data.BillingFormData;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingNote;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarDemographic.data.DemographicData;
import oscar.util.SqlUtils;
import oscar.util.StringUtils;

public class BillingReProcessBillAction extends Action {
    private static final Logger logger = MiscUtils.getLogger();
    private final BillingDao billingDao = SpringUtils.getBean(BillingDao.class);

    private final BillingmasterDAO billingmasterDAO = SpringUtils.getBean(BillingmasterDAO.class);

  //Misc misc = new Misc();
  MSPReconcile msp = new MSPReconcile();
  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,ServletException {
    if (request.getSession().getAttribute("user") == null) {
      return (mapping.findForward("Logout"));
    }

    boolean massEdit = request.getParameter("billCheck") != null;

    List<BillingReProcessBillForm> billingReProcessBillFormList = new ArrayList<BillingReProcessBillForm>();
    if (massEdit) {
        String[] billList = request.getParameterValues("billCheck");
        for (String billId : billList) {
            String billingMasterNo = billId.split("_")[1];
            BillingReProcessBillForm billingReProcessBillForm = createBillingReProcessBillForm(billingMasterNo, billingmasterDAO, request);
            billingReProcessBillFormList.add(billingReProcessBillForm);
        }
    } else {
        BillingReProcessBillForm billingReProcessBillForm = (BillingReProcessBillForm) form;
        billingReProcessBillFormList.add(billingReProcessBillForm);
    }

    for (BillingReProcessBillForm frm : billingReProcessBillFormList) {
        String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");
        String billingmasterNo = frm.getBillingmasterNo();
        String demographicNo = frm.getDemoNo();
        DemographicData demoD = new DemographicData();
        org.oscarehr.common.model.Demographic demo = demoD.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicNo);

        logger.debug("RETRIEVING Using " + billingmasterNo);
        Billingmaster billingmaster = billingmasterDAO.getBillingMasterByBillingMasterNo(billingmasterNo);
        Billing bill = billingmasterDAO.getBilling(billingmaster.getBillingNo());


        String billingType = bill.getBillingtype();
        logger.debug("type " + billingType);


    BillingFormData billform = new BillingFormData();

    ///
    String providerNo = frm.getProviderNo(); //f
    String demographicFirstName = demo.getFirstName(); //d
    String demographicLastName = demo.getLastName(); //d
   
    if (demographicLastName.length() < 2){
    	demographicLastName += "   ";
     }
     if (demographicFirstName.length() < 1){
    	 demographicFirstName += "   ";
     }
     
    String name_verify = demographicFirstName.substring(0, 1) + " " + demographicLastName.substring(0, 2); //d
    String billingGroupNo = billform.getGroupNo(providerNo);
    String practitionerNo = billform.getPracNo(providerNo); //p

    //d
    String hcNo = Objects.nonNull(demo.getHin()) ?
            demo.getHin().trim() + (Objects.nonNull(demo.getVer()) ? demo.getVer().trim() : "")
            : "";
    String dependentNo = frm.getDependentNo(); //f

    String visitLocation = frm.getLocationVisit(); //f
    String clarificationCode = visitLocation.substring(0, 2); //f
    String anatomicalArea = frm.getAnatomicalArea(); //f
    String afterHour = frm.getAfterHours(); //f
    String newProgram = frm.getNewProgram(); //f
    String billingUnit = frm.getBillingUnit(); ///f

    String billingServiceCode = frm.getService_code(); //f
    String billingServicePrice = frm.getBillingAmount(); //f
    String payment_mode = frm.getPaymentMode(); //f
    String serviceDate = frm.getServiceDate(); //f
    String serviceToDate = frm.getServiceToDay(); //f
    String submissionCode = frm.getSubmissionCode(); //f
    String exSubmissionCode = ""; //f
    String dxCode1 = frm.getDx1(); //f
    String dxCode2 = frm.getDx2(); //f
    String dxCode3 = frm.getDx3(); //f
    String dxExpansion = ""; //f
    String serviceLocation = frm.getServiceLocation();
    String referralFlag1 = frm.getReferalPracCD1(); //f
    String referralNo1 = frm.getReferalPrac1(); //f
    String referralFlag2 = frm.getReferalPracCD2(); //f
    String referralNo2 = frm.getReferalPrac2(); //f
    String timeCall = frm.getTimeCallRec(); //f
    String serviceStartTime = frm.getStartTime(); //f
    String serviceEndTime = frm.getFinishTime(); //f
    String birthDate = DemographicData.getDob(demo); //d
    String correspondenceCode = frm.getCorrespondenceCode(); //f
    String claimComment = frm.getShortComment(); //f
    String icbcClaimNo = frm.getIcbcClaim();

    String billingStatus = frm.getStatus(); //f

    String facilityNum = frm.getFacilityNum();
    String facilitySubNum = frm.getFacilitySubNum();

    String originalMSPNumber = Misc.forwardZero("", 20);

    String oinInsurerCode = frm.getInsurerCode(); //f
    String oinRegistrationNo = demo.getHin()+demo.getVer(); //d
    String oinBirthdate = DemographicData.getDob(demo); //d
    String oinFirstName = demo.getFirstName(); //d
    String oinSecondName = ""; //d
    String oinSurname = demo.getLastName(); //d
    String oinSexCode = demo.getSex(); //d
    String oinAddress = demo.getAddress(); //d
    String oinAddress2 = demo.getCity(); //d
    String oinAddress3 = ""; //d
    String oinAddress4 = ""; //d
    String oinPostalcode = demo.getPostal(); //d

    String hcType = demo.getHcType(); //d

    String messageNotes = frm.getMessageNotes();
    String billRegion = OscarProperties.getInstance().getProperty("billregion");
    String submit = frm.getSubmit();
    String secondSQL = null;

        // If its a ICBC Bill and the status does not need to change, mark the billingStatus as ICBC NO SUB
        billingStatus = StringUtils.isNullOrEmpty(billingStatus) && billingType.equals(MSPReconcile.BILLTYPE_ICBC) ? "I" : billingStatus;
        if ((submit.equals("Resubmit Bill") || submit.equals("Reprocess and Resubmit Bill")) || billingStatus.equals("O")) {
            if (!"W".equals(billingStatus) && !"I".equals(billingStatus)) {
                billingStatus = "O";
            }

            secondSQL = "update billing set status = '" + billingStatus + "' where billing_no ='" + frm.getBillNumber() + "'";
        } else if (submit.equals("Settle Bill")) {
            billingStatus = "S";
        } else if (submit.equals("Revert to PWE")) {
            // Set to PWE
            billingStatus = "E";
        }

    if (hcType.equals(billRegion)) { //if its bc go on
      //oinInsurerCode = "";
      oinRegistrationNo = "";
      oinBirthdate = "";
      oinFirstName = "";
      oinSecondName = "";
      oinSurname = "";
      oinSexCode = "";
      oinAddress = "";
      oinAddress2 = "";
      oinAddress3 = "";
      oinAddress4 = "";
      oinPostalcode = "";

    } else { //other provinces
      oinInsurerCode = hcType;
      hcNo = "000000000";
      name_verify = "0000";
    }

    if (submissionCode.equals("E")) {
      String seqNum = frm.getDebitRequestSeqNum();
      String dateRecieved = frm.getDebitRequestDate();
      try {
        dateRecieved = dateRecieved.trim();
        Integer.parseInt(dateRecieved);
      }catch (Exception e) {
        MiscUtils.getLogger().error("Error", e);
        dateRecieved = "";
      }

      originalMSPNumber = constructOriginalMSPNumber(dataCenterId, seqNum,dateRecieved);
    }
    /**
     * Check the bill type, if it has been changed by the user
     * we need to ensure that the correct fee code is associated
     * e.g. If bill is changed from MSP to Private, the correct private fee must be retrieved
     *
     */

    String persistedBillType = this.getPersistedBillType(billingmasterNo);
    if (persistedBillType != null) {
      if (!persistedBillType.equals(billingStatus)) {
        //if the bill status was changed to "Bill Patient
        //And the persisted bill status is anything but private
        if (MSPReconcile.BILLPATIENT.equals(billingStatus) &&
            !MSPReconcile.PAIDPRIVATE.equals(persistedBillType)) {
          //get the correct the Private code representation
          //and correct code amount if applicable
          //yes, this is lame. Private codes are simply the standard msp
          //code with the letter 'A' prepended. The current db design should really
          //have a 'fees' associative table
          //get the private fee data if it exists
          String[] privateCodeRecord = getServiceCodePrice(billingServiceCode,true);
          if (privateCodeRecord != null && privateCodeRecord.length == 1) {
            billingServiceCode = "A" + billingServiceCode;
            billingServicePrice = privateCodeRecord[0];

          }
        }
      }
    } else {
      throw new RuntimeException("BILLING BC - " + new java.util.Date().toString() + " - billingmaster_no " + billingmasterNo + " doesnt't seem to have a type");
    }

    //Multiply the bill amount by the units - Fixes bug where wrong amount being sent to MSP

    try{

            //BillingCodeData bcd = new BillingCodeData();
            //BillingService billingService = bcd.getBillingCodeByCode(billingServiceCode, new Date());
            String codePrice = StringUtils.isNullOrEmpty(request.getParameter("billingAmount")) ? (StringUtils.isNullOrEmpty(frm.getBillingAmount()) ? "0.00" : frm.getBillingAmount()) : request.getParameter("billingAmount"); //billingService.getValue();
            logger.debug("codePrice=" + codePrice + " amount on form " + request.getParameter("billingAmount"));

      if("E".equals(payment_mode)){
          codePrice = "0.00";
      }

      double dblBillAmount = Double.parseDouble(codePrice);
      BigDecimal bdFee = new BigDecimal(""+dblBillAmount).setScale(2,RoundingMode.HALF_UP);



      billingServicePrice = bdFee.toString();
    } catch(NumberFormatException e){
      MiscUtils.getLogger().error("Error", e);
      throw new RuntimeException("BC BILLING - Exception when attempting to multiply Bill Amount by Unit ");
    }
    bill.setProviderOhipNo(practitionerNo);
    bill.setBillingDate(MyDateFormat.getSysDate(serviceDate));
    billingmaster.setDatacenter(dataCenterId);
        //TODO
        billingmaster.setPayeeNo(billingGroupNo);
        billingmaster.setPractitionerNo(practitionerNo);
        billingmaster.setPhn(hcNo);
        billingmaster.setNameVerify(name_verify);
        billingmaster.setDependentNum(dependentNo);
        billingmaster.setBillingUnit(billingUnit);
        billingmaster.setClarificationCode(clarificationCode);
        billingmaster.setAnatomicalArea(anatomicalArea);
        billingmaster.setAfterHour(afterHour);
        billingmaster.setNewProgram(newProgram);
        billingmaster.setBillingCode(billingServiceCode);
        billingmaster.setBillAmount(billingServicePrice);
        billingmaster.setPaymentMode(payment_mode);
        billingmaster.setServiceDate(convertDate8Char(serviceDate));
        billingmaster.setServiceToDay(serviceToDate);
        billingmaster.setSubmissionCode(submissionCode);
        billingmaster.setExtendedSubmissionCode(exSubmissionCode);
        billingmaster.setDxCode1(dxCode1);
        billingmaster.setDxCode2(dxCode2);
        billingmaster.setDxCode3(dxCode3);
        billingmaster.setDxExpansion(dxExpansion);
        billingmaster.setServiceLocation(serviceLocation);
        billingmaster.setReferralFlag1(referralFlag1);
        billingmaster.setReferralNo1(referralNo1);
        billingmaster.setReferralFlag2(referralFlag2);
        billingmaster.setReferralNo2(referralNo2);
        billingmaster.setTimeCall(timeCall);
        billingmaster.setServiceStartTime(serviceStartTime);
        billingmaster.setServiceEndTime(serviceEndTime);
        billingmaster.setBirthDate(birthDate);
        billingmaster.setCorrespondenceCode(correspondenceCode);
        billingmaster.setClaimComment(claimComment);
        billingmaster.setOriginalClaim(originalMSPNumber);
        billingmaster.setFacilityNo(facilityNum);
        billingmaster.setFacilitySubNo(facilitySubNum);
        billingmaster.setIcbcClaimNo(icbcClaimNo);

        billingmaster.setOinInsurerCode(oinInsurerCode);
        billingmaster.setOinRegistrationNo(oinRegistrationNo);
        billingmaster.setOinBirthdate(oinBirthdate);
        billingmaster.setOinFirstName(oinFirstName);
        billingmaster.setOinSecondName(oinSecondName);
        billingmaster.setOinSurname(oinSurname);
        billingmaster.setOinSexCode(oinSexCode);
        billingmaster.setOinAddress(oinAddress);
        billingmaster.setOinAddress2(oinAddress2);
        billingmaster.setOinAddress3(oinAddress3);
        billingmaster.setOinAddress4(oinAddress4);
        billingmaster.setOinPostalcode(oinPostalcode);
        try{
	        String wcbId = request.getParameter("WCBid");
	        if(wcbId != null && wcbId.length()>0) {
	        	billingmaster.setWcbId(Integer.parseInt(wcbId));
	        }
        }catch(Exception e){
        	MiscUtils.getLogger().warn("warning",e);
        }
        bill.setProviderNo(providerNo);
        logger.debug("WHAT IS BILL <ASTER "+billingmaster.getBillingmasterNo());
        billingmasterDAO.update(billingmaster);
        billingmasterDAO.update(bill);

        logger.debug("type 2"+bill.getBillingtype());
        logger.debug("WHAT IS BILL <ASTER2 "+billingmaster.getBillingmasterNo());


        if (!StringUtils.isNullOrEmpty(billingStatus)) {  //What if billing status is null?? the status just doesn't get updated but everything else does??'
            //Why does this get called??  update billing type based on the billing status.  I guess this is effective when you switch this to bill on
            msp.updateBillingStatus(frm.getBillNumber(), billingStatus, billingmasterNo);
        }
        BillingHistoryDAO dao = new BillingHistoryDAO();
        //If the adjustment amount field isn't empty, create an archive of the adjustment
        if (frm.getAdjAmount() != null && !"".equals(frm.getAdjAmount())) {
            double dblAdj = Math.abs(new Double(frm.getAdjAmount()).doubleValue());
            //if 1 this adjustment is a debit
            if ("1".equals(frm.getAdjType())) {
                dblAdj = dblAdj * -1.0;
            }
            dao.createBillingHistoryArchive(frm.getBillingmasterNo(), dblAdj, MSPReconcile.PAYTYPE_IA);
            msp.settleIfBalanced(frm.getBillingmasterNo());
        } else {
            dao.createBillingHistoryArchive(billingmasterNo);
        }
        if (secondSQL != null) {
            // If its an No Sub ICBC billing, it needs to be set to set back to status "O"
            billingStatus = billingStatus.equals("I") ? "O" : billingStatus;
            Billing b = billingDao.find(Integer.parseInt(frm.getBillNumber()));
            if (b != null) {
                b.setStatus(billingStatus);
                billingDao.merge(b);
            }

        }

      if (correspondenceCode.equals("N") || correspondenceCode.equals("B")) {
        MSPBillingNote n = new MSPBillingNote();
        n.addNote(billingmasterNo,(String) request.getSession().getAttribute("user"),frm.getNotes());
      }

      if (messageNotes != null) {
        BillingNote n = new BillingNote();
        if (n.hasNote(billingmasterNo) || !messageNotes.trim().equals("")) {
          n.addNote(billingmasterNo,(String) request.getSession().getAttribute("user"),messageNotes);
        }
      }
   

        request.setAttribute("billingmaster_no", billingmasterNo);
        if (submit.equals("Reprocess and Resubmit Bill")) {
            request.setAttribute("close", "true");
        }
    }
    return massEdit ? mapping.findForward("save") : mapping.findForward("success");
  }


  private String[] getServiceCodePrice(String billingServiceCode,boolean usePrefix) {
    String prepend = usePrefix?"A":"";
    String[] privateCodeRecord = SqlUtils.getRow(
        "select value from billingservice where service_code = '" + prepend + billingServiceCode + "'");
    return privateCodeRecord;
  }

  /**
   * getPersistedBillType
   *
   * @param billingmasterNo String
   * @return String
   */
  private String getPersistedBillType(String billingmasterNo) {
    String qry = "select billingstatus from billingmaster where billingmaster.billingmaster_no = " +
        billingmasterNo;
    String row[] = SqlUtils.getRow(qry);
    String ret = null;
    if (row != null) {
      ret = row[0];
    }
    return ret;
  }

  /**
   * @todo THis belongs in a utility class
   * @param s String
   * @return String
   */
  public String convertDate8Char(String s) {
    String sdate = "00000000", syear = "", smonth = "", sday = "";
    logger.debug("s=" + s);
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

        logger.debug("Year" + syear + " Month" + smonth + " Day" + sday);
        sdate = syear + smonth + sday;

      }else {
        sdate = s;
      }
      logger.debug("sdate:" + sdate);
    }else {
      sdate = "00000000";

    }
    return sdate;
  }

  public String constructOriginalMSPNumber(String dataCenterNum, String seqNum,
                                           String dateRecieved) {
    String retval = "";

    retval = Misc.forwardZero(dataCenterNum, 5) + Misc.forwardZero(seqNum, 7) +
        Misc.forwardZero(dateRecieved, 8);
    return retval;
  }

    public String getDebitRequestSeqNum(String str){
        return (str == null || str.length() < 12) ? "" : str.substring(5,12);
    }

    public String getDebitRequestDate (String str){
        return (str == null || str.length() <12) ? "" : str.substring(13);
    }

    public BillingReProcessBillForm createBillingReProcessBillForm (String billingMasterNo, BillingmasterDAO billingmasterDAO, HttpServletRequest request) {
        MSPReconcile msp = new MSPReconcile();
        Properties allFields = msp.getBillingMasterRecord(billingMasterNo);

        Billingmaster billingMaster = billingmasterDAO.getBillingMasterByBillingMasterNo(billingMasterNo);
        Billing bill = billingmasterDAO.getBilling(billingMaster.getBillingNo());

//        boolean reProcessBCP = request.getParameter("hiddenFilterType") != null && request.getParameter("hiddenFilterType").equals("BCP");

        BillingReProcessBillForm billingReProcessBillForm = new BillingReProcessBillForm();
        billingReProcessBillForm.setBillingmasterNo(String.valueOf(billingMasterNo));
        billingReProcessBillForm.setInsurerCode(allFields.getProperty("oinInsurerCode"));
        billingReProcessBillForm.setProviderNo(bill.getProviderNo());
        billingReProcessBillForm.setDemoNo(String.valueOf(bill.getDemographicNo()));
        billingReProcessBillForm.setDependentNo(allFields.getProperty("dependentNum"));
        billingReProcessBillForm.setAfterHours(allFields.getProperty("afterHour"));
        billingReProcessBillForm.setService_code(allFields.getProperty("billingCode"));

        billingReProcessBillForm.setStatus("");

        billingReProcessBillForm.setBillNumber(allFields.getProperty("billingNo"));
        billingReProcessBillForm.setAnatomicalArea(allFields.getProperty("anatomicalArea"));
        billingReProcessBillForm.setNewProgram(allFields.getProperty("newProgram"));
        billingReProcessBillForm.setBilling_unit(allFields.getProperty("billingUnit"));
        billingReProcessBillForm.setBillingUnit(allFields.getProperty("billingUnit"));
        billingReProcessBillForm.setBillingAmount(allFields.getProperty("billAmount"));


        billingReProcessBillForm.setDx1(allFields.getProperty("dxCode1"));
        billingReProcessBillForm.setDx2(allFields.getProperty("dxCode2"));
        billingReProcessBillForm.setDx3(allFields.getProperty("dxCode3"));
        billingReProcessBillForm.setPaymentMode(allFields.getProperty("paymentMode"));
        billingReProcessBillForm.setSubmissionCode(allFields.getProperty("submissionCode"));
        billingReProcessBillForm.setServiceDate(allFields.getProperty("serviceDate"));
        billingReProcessBillForm.setServiceToDay(allFields.getProperty("serviceToDay"));
        billingReProcessBillForm.setServiceLocation(allFields.getProperty("serviceLocation"));
        billingReProcessBillForm.setReferalPracCD1(allFields.getProperty("referralFlag1"));
        billingReProcessBillForm.setReferalPrac1(allFields.getProperty("referralNo1"));
        billingReProcessBillForm.setReferalPracCD2(allFields.getProperty("referralFlag2"));
        billingReProcessBillForm.setReferalPrac2(allFields.getProperty("referralNo2"));
        billingReProcessBillForm.setTimeCallRec(allFields.getProperty("timeCall"));
        billingReProcessBillForm.setCorrespondenceCode(allFields.getProperty("correspondenceCode"));
        billingReProcessBillForm.setMvaClaim(allFields.getProperty("mvaClaimCode"));
        billingReProcessBillForm.setIcbcClaim(allFields.getProperty("icbcClaimNo"));

        billingReProcessBillForm.setFacilityNum(billingMaster.getFacilityNo());
        billingReProcessBillForm.setFacilitySubNum(billingMaster.getFacilitySubNo());

        MSPBillingNote billingNote = new MSPBillingNote();
        String corrNote = billingNote.getNote(billingMasterNo);

        billingReProcessBillForm.setNotes(corrNote);

        BillingNote  bNote = new BillingNote();
        String messageNotes = bNote.getNote(billingMasterNo);

        billingReProcessBillForm.setMessageNotes(messageNotes);

        billingReProcessBillForm.setStartTime(allFields.getProperty("serviceStartTime"));
        billingReProcessBillForm.setFinishTime(allFields.getProperty("serviceEndTime"));
        billingReProcessBillForm.setSubmit(request.getParameter("submitOperation") != null ? request.getParameter("submitOperation") : "");
        billingReProcessBillForm.setLocationVisit(allFields.getProperty("clarificationCode"));
        billingReProcessBillForm.setService_code(allFields.getProperty("billingCode"));
        billingReProcessBillForm.setBilling_amount(allFields.getProperty("bilAmount"));
        billingReProcessBillForm.setShortComment(allFields.getProperty("claimComment"));

        billingReProcessBillForm.setDependent(null);

        billingReProcessBillForm.setDebitRequestSeqNum(getDebitRequestSeqNum(allFields.getProperty("originalClaim")));
        billingReProcessBillForm.setDebitRequestDate(getDebitRequestDate(allFields.getProperty("originalClaim")));

        billingReProcessBillForm.setAdjAmount("");
        billingReProcessBillForm.setAdjType(null);

        billingReProcessBillForm.setServlet(null);
        billingReProcessBillForm.setMultipartRequestHandler(null);

        return billingReProcessBillForm;
    }
}
