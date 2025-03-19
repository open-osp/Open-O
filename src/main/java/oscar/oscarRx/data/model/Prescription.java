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

package oscar.oscarRx.data.model;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.IndivoDocsDao;
import org.oscarehr.common.dao.PrescriptionDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.IndivoDocs;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;
import oscar.util.ConversionUtils;
import oscar.util.DateUtils;

import java.util.*;

// erased an orfin }
public class Prescription {

    int drugId;
    String providerNo;
    int demographicNo;
    long randomId = 0;
    java.util.Date rxCreatedDate = null;
    java.util.Date rxDate = null;
    java.util.Date endDate = null;
    java.util.Date pickupDate = null;
    java.util.Date pickupTime = null;
    java.util.Date writtenDate = null;
    String writtenDateFormat = null;
    String rxDateFormat = null;
    java.util.Date printDate = null;
    int numPrints = 0;
    String BN = null; // regular
    int GCN_SEQNO = 0; // regular
    String customName = null; // custom
    float takeMin = 0;
    float takeMax = 0;
    String frequencyCode = null;
    String duration = null;
    String durationUnit = null;
    String quantity = null;
    int repeat = 0;
    java.util.Date lastRefillDate = null;
    boolean nosubs = false;
    boolean prn = false;
    Boolean longTerm = null;
    boolean shortTerm = false;
    Boolean pastMed = null;
    boolean startDateUnknown = false;
    Boolean patientCompliance = null;
    String special = null;
    String genericName = null;
    boolean archived = false; // ADDED BY JAY DEC 3 2002
    String atcCode = null;
    String script_no = null;
    String regionalIdentifier = null;
    String method = null;
    String unit = null;
    String unitName = null;
    String route = null;
    String drugForm = null;
    String dosage = null;
    String outsideProviderName = null;
    String outsideProviderOhip = null;
    boolean custom = false;
    private String indivoIdx = null; // indivo document index for this prescription
    private boolean registerIndivo = false;
    private final String docType = "Rx";
    private boolean discontinued = false;//indicate if the rx has isDisontinued before.
    private String lastArchDate = null;
    private String lastArchReason = null;
    private Date archivedDate;
    private boolean discontinuedLatest = false;
    String special_instruction = null;
    private boolean durationSpecifiedByUser = false;
    private boolean customNote = false;
    boolean nonAuthoritative = false;
    String eTreatmentType = null;
    private boolean hideCpp = false;
    String rxStatus = null;
    private Integer refillDuration = 0;
    private Integer refillQuantity = 0;
    private String dispenseInterval = "";
    private int position = 0;
    private String comment = null;

    private String drugFormList = "";
    private String datesReprinted = "";
    private boolean dispenseInternal = false;

    private List<String> policyViolations = new ArrayList<String>();

    private int drugReferenceId;

    private String drugReasonCode;
    private String drugReasonCodeSystem;

    private String protocol;
    private String priorRxProtocol;
    private Integer pharmacyId;

    private Integer digitalSignatureId;

    public Integer getDigitalSignatureId() {
        return digitalSignatureId;
    }

    public void setDigitalSignatureId(Integer digitalSignatureId) {
        this.digitalSignatureId = digitalSignatureId;
    }

    public String getDrugReasonCode() {
        return drugReasonCode;
    }

    public void setDrugReasonCode(String drugReasonCode) {
        this.drugReasonCode = drugReasonCode;
    }

    public String getDrugReasonCodeSystem() {
        return drugReasonCodeSystem;
    }

    public void setDrugReasonCodeSystem(String drugReasonCodeSystem) {
        this.drugReasonCodeSystem = drugReasonCodeSystem;
    }

    public List<String> getPolicyViolations() {
        return policyViolations;
    }

    public void setPolicyViolations(List<String> policyViolations) {
        this.policyViolations = policyViolations;
    }

    public void setDrugReferenceId(int drugId2) {
        this.drugReferenceId = drugId2;

    }

    public int getDrugReferenceId() {
        return drugReferenceId;
    }

    public boolean getStartDateUnknown() {
        return startDateUnknown;
    }

    public void setStartDateUnknown(boolean startDateUnknown) {
        this.startDateUnknown = startDateUnknown;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDatesReprinted() {
        return datesReprinted;
    }

    public void setDatesReprinted(String datesReprinted) {
        this.datesReprinted = datesReprinted;
    }

    public String getDrugFormList() {
        return drugFormList;
    }

    public void setDrugFormList(String drugFormList) {
        this.drugFormList = drugFormList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isHideCpp() {
        return hideCpp;
    }

    public void setHideCpp(boolean hideCpp) {
        this.hideCpp = hideCpp;
    }

    public String getETreatmentType() {
        return eTreatmentType;
    }

    public void setETreatmentType(String treatmentType) {
        eTreatmentType = treatmentType;
    }

    public String getRxStatus() {
        return rxStatus;
    }

    public void setRxStatus(String status) {
        rxStatus = status;
    }

    public boolean isCustomNote() {
        return customNote;
    }

    public void setCustomNote(boolean b) {
        customNote = b;
    }

    public boolean isMitte() {
        if (unitName != null && (unitName.equalsIgnoreCase("D") || unitName.equalsIgnoreCase("W") || unitName.equalsIgnoreCase("M") || unitName.equalsIgnoreCase("day") || unitName.equalsIgnoreCase("week") || unitName.equalsIgnoreCase("month") || unitName.equalsIgnoreCase("days") || unitName.equalsIgnoreCase("weeks") || unitName.equalsIgnoreCase("months") || unitName.equalsIgnoreCase("mo")))
            return true;
        else return false;
    }

    public boolean isDurationSpecifiedByUser() {
        return durationSpecifiedByUser;
    }

    public void setDurationSpecifiedByUser(boolean b) {
        this.durationSpecifiedByUser = b;
    }

    public String getSpecialInstruction() {
        return special_instruction;
    }

    public void setSpecialInstruction(String s) {
        special_instruction = s;
    }

    public boolean isLongTerm() {
        boolean trueFalse = Boolean.FALSE;
        if (longTerm != null) {
            trueFalse = longTerm;
        }
        return trueFalse;
    }

    public boolean isDiscontinuedLatest() {
        return this.discontinuedLatest;
    }

    public void setDiscontinuedLatest(boolean dl) {
        this.discontinuedLatest = dl;
    }

    public String getLastArchDate() {
        return this.lastArchDate;
    }

    public void setLastArchDate(String lastArchDate) {
        this.lastArchDate = lastArchDate;
    }

    public String getLastArchReason() {
        return this.lastArchReason;
    }

    public void setLastArchReason(String lastArchReason) {
        this.lastArchReason = lastArchReason;
    }

    public boolean isDiscontinued() {
        return this.discontinued;
    }

    public void setDiscontinued(boolean discon) {
        this.discontinued = discon;
    }

    public void setArchivedDate(Date ad) {
        this.archivedDate = ad;
    }

    public Date getArchivedDate() {
        return this.archivedDate;
    }

    // RxDrugData.GCN gcn = null;
    public Prescription(int drugId, String providerNo, int demographicNo) {
        this.drugId = drugId;
        this.providerNo = providerNo;
        this.demographicNo = demographicNo;
    }

    public long getRandomId() {
        return this.randomId;
    }

    public void setRandomId(long randomId) {
        this.randomId = randomId;
    }

    public int getNumPrints() {
        return this.numPrints;
    }

    public void setNumPrints(int numPrints) {
        this.numPrints = numPrints;
    }

    public Date getPrintDate() {
        return this.printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public void setScript_no(String script_no) {
        this.script_no = script_no;
    }

    public String getScript_no() {
        return this.script_no;
    }

    public void setIndivoIdx(String idx) {
        indivoIdx = idx;
    }

    public String getIndivoIdx() {
        return indivoIdx;
    }

    public void setRegisterIndivo() {
        registerIndivo = true;
    }

    public boolean isRegisteredIndivo() {
        return registerIndivo;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    // ADDED BY JAY DEC 03 2002
    public boolean isArchived() {
        return this.archived;
    }

    public void setArchived(String tf) {
        this.archived = Boolean.parseBoolean(tf);
    }

    /// ///////////////////////////
    public int getDrugId() {
        return this.drugId;
    }

    public String getProviderNo() {
        return this.providerNo;
    }

    public int getDemographicNo() {
        return this.demographicNo;
    }

    public Date getRxDate() {
        return this.rxDate;
    }

    public void setRxDate(Date RHS) {
        this.rxDate = RHS;
    }

    public Date getPickupDate() {
        return this.pickupDate;
    }

    public void setPickupDate(Date RHS) {
        this.pickupDate = RHS;
    }

    public Date getPickupTime() {
        return this.pickupTime;
    }

    public void setPickupTime(Date RHS) {
        this.pickupTime = RHS;
    }

    public Date getEndDate() {
        if (this.isDiscontinued()) return this.archivedDate;
        else return this.endDate;
    }

    public void setEndDate(Date RHS) {
        this.endDate = RHS;
    }

    public Date getWrittenDate() {
        return this.writtenDate;
    }

    public void setWrittenDate(Date RHS) {
        this.writtenDate = RHS;
    }

    public String getWrittenDateFormat() {
        return this.writtenDateFormat;
    }

    public void setWrittenDateFormat(String RHS) {
        this.writtenDateFormat = RHS;
    }

    public String getRxDateFormat() {
        return this.rxDateFormat;
    }

    public void setRxDateFormat(String RHS) {
        this.rxDateFormat = RHS;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public String getPriorRxProtocol() {
        return priorRxProtocol;
    }

    public void setPriorRxProtocol(String priorRxProtocol) {
        this.priorRxProtocol = priorRxProtocol;
    }

    public Integer getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(Integer pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    /*
     * Current should contain non-expired drugs, as well as long terms drugs that are not deleted/discontinued
     */
    public boolean isCurrent() {
        if (isLongTerm() && !isDiscontinued() && !isArchived()) {
            return true;
        }
        boolean b = false;

        try {
            GregorianCalendar cal = new GregorianCalendar(Locale.CANADA);
            cal.add(GregorianCalendar.DATE, -1);

            if (this.getEndDate().after(cal.getTime())) {
                b = true;
            }
        } catch (Exception e) {
            b = false;
        }

        return b;
    }

    public void calcEndDate() {
        try {
            GregorianCalendar cal = new GregorianCalendar(Locale.CANADA);
            int days = 0;

            //          p("this.getRxDate()",this.getRxDate().toString());
            cal.setTime(this.getRxDate());

            if (this.getDuration() != null && this.getDuration().length() > 0) {
                if (Integer.parseInt(this.getDuration()) > 0) {
                    int i = Integer.parseInt(this.getDuration());
                    //      p("i",Integer.toString(i));
                    //      p("this.getDurationUnit()",this.getDurationUnit());
                    if (this.getDurationUnit() != null && this.getDurationUnit().equalsIgnoreCase("D")) {
                        days = i;
                    }
                    if (this.getDurationUnit() != null && this.getDurationUnit().equalsIgnoreCase("W")) {
                        days = i * 7;
                    }
                    if (this.getDurationUnit() != null && this.getDurationUnit().equalsIgnoreCase("M")) {
                        days = i * 30;
                    }

                    if (this.getRepeat() > 0) {
                        int r = this.getRepeat();

                        r++; // if we have a repeat of 1, multiply days by 2

                        days = days * r;
                    }
                    //    p("days",Integer.toString(days));
                    if (days > 0) {
                        cal.add(GregorianCalendar.DATE, days);
                    }
                }
            }

            this.endDate = cal.getTime();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        //     p("endDate",RxUtil.DateToString(this.endDate));
    }

    public String getBrandName() {
        return this.BN;
    }

    public void setBrandName(String RHS) {
        this.BN = RHS;
        // this.gcn=null;
    }

    public int getGCN_SEQNO() {
        return this.GCN_SEQNO;
    }

    public void setGCN_SEQNO(int RHS) {
        this.GCN_SEQNO = RHS;
        // this.gcn=null;
    }

    /*
     * public RxDrugData.GCN getGCN() { if (this.gcn==null) { this.gcn = new RxDrugData().getGCN(this.BN, this.GCN_SEQNO); }
     *
     * return gcn; }
     */
    public boolean isCustom() {
        boolean b = false;

        if (this.customName != null) {
            b = true;
        } else if (this.GCN_SEQNO == 0) {
            b = true;
        }
        return b;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String RHS) {
        this.customName = RHS;
        if (this.customName != null) {
            if (this.customName.equalsIgnoreCase("null") || this.customName.equalsIgnoreCase("")) {
                this.customName = null;
            }
        }
    }

    public float getTakeMin() {
        return this.takeMin;
    }

    public void setTakeMin(float RHS) {
        this.takeMin = RHS;
    }

    public String getTakeMinString() {
        return RxUtil.FloatToString(this.takeMin);
    }

    public float getTakeMax() {
        return this.takeMax;
    }

    public void setTakeMax(float RHS) {
        this.takeMax = RHS;
    }

    public String getTakeMaxString() {
        return RxUtil.FloatToString(this.takeMax);
    }

    public String getFrequencyCode() {
        return this.frequencyCode;
    }

    public void setFrequencyCode(String RHS) {
        this.frequencyCode = RHS;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String RHS) {
        this.duration = RHS;
    }

    public String getDurationUnit() {
        return this.durationUnit;
    }

    public void setDurationUnit(String RHS) {
        this.durationUnit = RHS;
    }

    public String getQuantity() {
        if (this.quantity == null) {
            this.quantity = "";
        }
        return this.quantity;
    }

    public void setQuantity(String RHS) {
        if (RHS == null || RHS.equals("null") || RHS.length() < 1) {
            this.quantity = "0";
        } else {
            this.quantity = RHS;
        }
    }

    public int getRepeat() {
        return this.repeat;
    }

    public void setRepeat(int RHS) {
        this.repeat = RHS;
    }

    public Date getLastRefillDate() {
        return this.lastRefillDate;
    }

    public void setLastRefillDate(Date RHS) {
        this.lastRefillDate = RHS;
    }

    public boolean getNosubs() {
        return this.nosubs;
    }

    public int getNosubsInt() {
        if (this.getNosubs() == true) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setNosubs(boolean RHS) {
        this.nosubs = RHS;
    }

    public void setNosubs(int RHS) {
        if (RHS == 0) {
            this.setNosubs(false);
        } else {
            this.setNosubs(true);
        }
    }

    public boolean isPrn() {//conventional name for getter of boolean variable
        return this.prn;
    }

    public boolean getPrn() {
        return this.prn;
    }

    public int getPrnInt() {
        if (this.getPrn() == true) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setPrn(boolean RHS) {
        this.prn = RHS;
    }

    public void setPrn(int RHS) {
        if (RHS == 0) {
            this.setPrn(false);
        } else {
            this.setPrn(true);
        }
    }

    public Boolean getLongTerm() {
        return this.longTerm;
    }

    public void setLongTerm(Boolean trueFalseNull) {
        this.longTerm = trueFalseNull;
    }

    public boolean getShortTerm() {
        return this.shortTerm;
    }

    public void setShortTerm(boolean st) {
        this.shortTerm = st;
    }

    public void setNonAuthoritative(boolean nonAuthoritative) {
        this.nonAuthoritative = nonAuthoritative;
    }

    public boolean isNonAuthoritative() {
        return this.nonAuthoritative;
    }

    public boolean isPastMed() {
        boolean trueFalse = Boolean.FALSE;
        if (this.pastMed != null) {
            trueFalse = this.pastMed;
        }
        return trueFalse;
    }

    public Boolean getPastMed() {
        return this.pastMed;
    }

    public void setPastMed(Boolean trueFalseNull) {
        this.pastMed = trueFalseNull;
    }

    public boolean getDispenseInternal() {
        return isDispenseInternal();
    }

    public boolean isDispenseInternal() {
        return dispenseInternal;
    }

    public void setDispenseInternal(boolean dispenseInternal) {
        this.dispenseInternal = dispenseInternal;
    }

    public boolean isPatientCompliance() {
        boolean trueFalse = Boolean.FALSE;
        if (this.patientCompliance != null) {
            trueFalse = this.patientCompliance;
        }
        return trueFalse;
    }

    public Boolean getPatientCompliance() {
        return this.patientCompliance;
    }

    public void setPatientCompliance(Boolean trueFalseNull) {
        this.patientCompliance = trueFalseNull;
    }

    public String getSpecial() {

        //if (special == null || special.length() < 6) {
        if (special == null || special.length() < 4) {
            // the reason this is here is because Tomislav/Caisi was having massive problems tracking down
            // drugs that randomly go missing in prescriptions, like a list of 20 drugs and 3 would be missing on the prescription.
            // it was tracked down to some code which required a special, but we couldn't figure out why a special was required or missing.
            // so now we have code to log an error when a drug is missing a special, we still don't know why it's required or missing
            // but at least we know which drug does it.
            RxPrescriptionData.logger.warn("Some one is retrieving the drug special but it appears to be blank : " + special);
        }

        return special;
    }

    public String getOutsideProviderName() {
        return this.outsideProviderName;
    }

    public void setOutsideProviderName(String outsideProviderName) {
        this.outsideProviderName = outsideProviderName;
    }

    public String getOutsideProviderOhip() {
        return this.outsideProviderOhip;
    }

    public void setOutsideProviderOhip(String outsideProviderOhip) {
        this.outsideProviderOhip = outsideProviderOhip;
    }

    public void setSpecial(String RHS) {

        //if (RHS == null || RHS.length() < 6) {
        if (RHS == null || RHS.length() < 4) {
            RxPrescriptionData.logger.warn("Some one is setting the drug special but it appears to be blank : " + special);
        }

        if (RHS != null) {
            if (!RHS.equals("null")) {
                special = RHS;
            } else {
                special = null;
            }
        } else {
            special = null;
        }

        //if (special == null || special.length() < 6) {
        if (special == null || special.length() < 4) {
            RxPrescriptionData.logger.warn("after processing the drug special but it appears to be blank : " + special);
        }
    }

    public String getSpecialDisplay() {
        String ret = "";

        String s = this.getSpecial();

        if (s != null) {
            if (s.length() > 0) {
                ret = "<br>";

                int i;
                String[] arr = s.split("\n");

                for (i = 0; i < arr.length; i++) {
                    ret += arr[i].trim();
                    if (i < arr.length - 1) {
                        ret += "; ";
                    }
                }
            }
        }

        return ret;
    }

    //function used for passing data to LogAction; maps to data column in log table
    public String getAuditString() {
        return getFullOutLine();
    }

    public String getFullOutLine() {
        String extra = "";

        if (getNosubs()) {
            extra += "SUBSTITUTION NOT ALLOWED";
        }
        if ((getRefillQuantity() != null && getRefillQuantity() > 0) || (getRefillDuration() != null && getRefillDuration() > 0)) {
            extra = "Refill: Qty:" + (getRefillQuantity() != null ? getRefillQuantity() : "0") + " Duration:" + (getRefillDuration() != null ? getRefillDuration() : "0") + " Days";
        }
        return (RxPrescriptionData.getFullOutLine(getSpecial() + "\n" + extra));
    }

    public String getDosageDisplay() {
        String ret = "";
        if (this.getTakeMin() != this.getTakeMax()) {
            ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
        } else {
            ret += this.getTakeMinString();
        }
        return ret;
    }

    public String getFreqDisplay() {
        String ret = this.getFrequencyCode();
        if (this.getPrn()) {
            ret += " PRN ";
        }
        return ret;
    }

    public String getRxDisplay() {
        try {
            String ret;

            if (this.isCustom()) {
                if (this.customName != null) {
                    ret = this.customName + " ";
                } else {
                    ret = "Unknown ";
                }
            } else {
                // RxDrugData.GCN gcn = this.getGCN();

                ret = this.getBrandName() + " "; // gcn.getBrandName() + " ";
                // + gcn.getStrength() + " "
                // + gcn.getDoseForm() + " "
                // + gcn.getRoute() + " ";
            }

            if (this.getTakeMin() != this.getTakeMax()) {
                ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
            } else {
                ret += this.getTakeMinString();
            }

            ret += " " + this.getFrequencyCode();

            if (this.getPrn()) {
                ret += " PRN ";
            }
            ret += " " + this.getDuration() + " ";

            if (getDurationUnit() != null && this.getDurationUnit().equals("D")) {
                ret += "Day";
            }
            if (getDurationUnit() != null && this.getDurationUnit().equals("W")) {
                ret += "Week";
            }
            if (getDurationUnit() != null && this.getDurationUnit().equals("M")) {
                ret += "Month";
            }

            try {
                if (this.getDuration() != null && this.getDuration().trim().length() == 0) {
                    this.setDuration("0");
                }
                if (this.getDuration() != null && !this.getDuration().equalsIgnoreCase("null") && Integer.parseInt(this.getDuration()) > 1) {
                    ret += "s";
                }
            } catch (Exception durationCalcException) {
                RxPrescriptionData.logger.error("Error with duration:", durationCalcException);
            }
            ret += "  ";
            ret += this.getQuantity();
            ret += " Qty  Repeats: ";
            ret += String.valueOf(this.getRepeat());

            if (this.getNosubs()) {
                ret += " No subs ";
            }

            return ret;
        } catch (Exception e) {
            RxPrescriptionData.logger.error("unexpected error", e);
            return null;
        }
    }

    public String getDrugName() {
        String ret;
        if (this.isCustom()) {
            if (this.customName != null) {
                ret = this.customName + " ";
            } else {
                ret = "Unknown ";
            }
        } else {
            ret = this.getBrandName() + " ";
        }
        return ret;
    }

    public String getFullFrequency() {
        String ret = "";
        if (this.getTakeMin() != this.getTakeMax()) {
            ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
        } else {
            ret += this.getTakeMinString();
        }

        ret += " " + this.getFrequencyCode();
        return ret;
    }

    public String getFullDuration() {
        String ret = this.getDuration() + " ";
        if (this.getDurationUnit().equals("D")) {
            ret += "Day";
        }
        if (this.getDurationUnit().equals("W")) {
            ret += "Week";
        }
        if (this.getDurationUnit().equals("M")) {
            ret += "Month";
        }

        if (Integer.parseInt(this.getDuration()) > 1) {
            ret += "s";
        }
        return ret;
    }

    public void Delete() {
        try {
            DrugDao drugDao = (DrugDao) SpringUtils.getBean(DrugDao.class);
            Drug drug = drugDao.find(getDrugId());
            if (drug != null) {
                drug.setArchived(true);
                drugDao.merge(drug);
            }
        } catch (Exception e) {
            RxPrescriptionData.logger.error("unexpected error", e);
        }
    }

    public boolean Save() {
        return Save(null);
    }

    public boolean registerIndivo() {
        IndivoDocs doc = new IndivoDocs();
        doc.setOscarDocNo(getDrugId());
        doc.setIndivoDocIdx(getIndivoIdx());
        doc.setDocType(docType);
        doc.setDateSent(new Date());
        doc.setUpdate(isRegisteredIndivo() ? "U" : "I");

        IndivoDocsDao dao = SpringUtils.getBean(IndivoDocsDao.class);
        dao.persist(doc);
        return true;
    }

    public boolean Print(LoggedInInfo loggedInInfo) {
        PrescriptionDao dao = SpringUtils.getBean(PrescriptionDao.class);
        org.oscarehr.common.model.Prescription p = dao.find(ConversionUtils.fromIntString(getScript_no()));
        String providerNo = loggedInInfo.getLoggedInProviderNo();

        if (p == null) return false;

        String dates_reprinted = p.getDatesReprinted();
        String now = DateUtils.format("yyyy-MM-dd HH:mm:ss", new Date());
        if (dates_reprinted != null && dates_reprinted.length() > 0) {
            dates_reprinted += "," + now + ";" + providerNo;
        } else {
            dates_reprinted = now + ";" + providerNo;
        }
        p.setDatesReprinted(dates_reprinted);
        dao.merge(p);
        this.setNumPrints(this.getNumPrints() + 1);

        return true;

    }

    public int getNextPosition() {
        DrugDao dao = SpringUtils.getBean(DrugDao.class);
        int position = dao.getMaxPosition(this.getDemographicNo());
        return (position + 1);
    }

    public boolean Save(String scriptId) {

        this.calcEndDate();

        // clean up fields
        if (this.takeMin > this.takeMax) this.takeMax = this.takeMin;

        if (getSpecial() == null || getSpecial().length() < 6)
            RxPrescriptionData.logger.warn("drug special appears to be null or empty : " + getSpecial());

        String escapedSpecial = StringEscapeUtils.escapeSql(this.getSpecial());

        if (escapedSpecial == null || escapedSpecial.length() < 6)
            RxPrescriptionData.logger.warn("drug special after escaping appears to be null or empty : " + escapedSpecial);

        DrugDao dao = SpringUtils.getBean(DrugDao.class);
        Drug drug = new Drug();

        this.position = this.getNextPosition();
        syncDrug(drug, ConversionUtils.fromIntString(scriptId));
        dao.persist(drug);
        drugId = drug.getId();

        return true;
    }

    private void syncDrug(Drug drug, Integer scriptId) {
        // the fields set are based on previous code, I don't know the details of why which are and are not set and can not audit it at this point in time.
        drug.setProviderNo(getProviderNo());
        drug.setDemographicId(getDemographicNo());
        drug.setRxDate(getRxDate());
        drug.setEndDate(getEndDate());
        drug.setWrittenDate(getWrittenDate());
        drug.setBrandName(getBrandName());
        drug.setGcnSeqNo(getGCN_SEQNO());
        drug.setCustomName(getCustomName());
        drug.setTakeMin(getTakeMin());
        drug.setTakeMax(getTakeMax());
        drug.setFreqCode(getFrequencyCode());
        drug.setDuration(getDuration());
        drug.setDurUnit(getDurationUnit());
        drug.setQuantity(getQuantity());
        drug.setRepeat(getRepeat());
        drug.setLastRefillDate(getLastRefillDate());
        drug.setNoSubs(getNosubs());
        drug.setPrn(getPrn());
        drug.setSpecial(getSpecial());
        drug.setGenericName(getGenericName());
        drug.setScriptNo(scriptId);
        drug.setAtc(atcCode);
        drug.setRegionalIdentifier(regionalIdentifier);
        drug.setUnit(getUnit());
        drug.setMethod(getMethod());
        drug.setRoute(getRoute());
        drug.setDrugForm(getDrugForm());
        drug.setOutsideProviderName(getOutsideProviderName());
        drug.setOutsideProviderOhip(getOutsideProviderOhip());
        drug.setCustomInstructions(getCustomInstr());
        drug.setDosage(getDosage());
        drug.setUnitName(getUnitName());
        drug.setLongTerm(getLongTerm());
        drug.setShortTerm(getShortTerm());
        drug.setCustomNote(isCustomNote());
        drug.setPastMed(getPastMed());
        drug.setDispenseInternal(getDispenseInternal());
        drug.setSpecialInstruction(getSpecialInstruction());
        drug.setPatientCompliance(getPatientCompliance());
        drug.setNonAuthoritative(isNonAuthoritative());
        drug.setPickUpDateTime(getPickupDate());
        drug.setETreatmentType(getETreatmentType());
        drug.setRxStatus(getRxStatus());
        drug.setDispenseInterval(getDispenseInterval());
        drug.setRefillQuantity(getRefillQuantity());
        drug.setRefillDuration(getRefillDuration());
        drug.setHideFromCpp(false);
        drug.setPosition(position);
        drug.setComment(getComment());
        drug.setStartDateUnknown(getStartDateUnknown());
        drug.setDispenseInternal(getDispenseInternal());
        drug.setProtocol(protocol);
        drug.setPriorRxProtocol(priorRxProtocol);
        drug.setPharmacyId(getPharmacyId());
    }

    public boolean AddToFavorites(String providerNo, String favoriteName) {
        Favorite fav = new Favorite(0, providerNo, favoriteName, this.getBrandName(), this.getGCN_SEQNO(), this.getCustomName(), this.getTakeMin(), this.getTakeMax(), this.getFrequencyCode(), this.getDuration(), this.getDurationUnit(), this.getQuantity(), this.getRepeat(), this.getNosubsInt(), this.getPrnInt(), this.getSpecial(), this.getGenericName(), this.getAtcCode(), this.getRegionalIdentifier(), this.getUnit(), this.getUnitName(), this.getMethod(), this.getRoute(), this.getDrugForm(),
                this.getCustomInstr(), this.getDosage());
        fav.setDispenseInternal(this.getDispenseInternal());

        return fav.Save();
    }

    /**
     * Getter for property atcCode.
     *
     * @return Value of property atcCode.
     */
    public String getAtcCode() {
        return atcCode;
    }

    /**
     * Checks to see if atcCode is not null or an emtpy string
     */
    public boolean isValidAtcCode() {
        if (atcCode != null && !atcCode.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * Setter for property atcCode.
     *
     * @param atcCode New value of property atcCode.
     */
    public void setAtcCode(String atcCode) {
        this.atcCode = atcCode;
    }

    /**
     * Getter for property regionalIdentifier.
     *
     * @return Value of property regionalIdentifier.
     */
    public String getRegionalIdentifier() {
        return regionalIdentifier;
    }

    /**
     * Setter for property regionalIdentifier.
     *
     * @param regionalIdentifier New value of property regionalIdentifier.
     */
    public void setRegionalIdentifier(String regionalIdentifier) {
        this.regionalIdentifier = regionalIdentifier;
    }

    /**
     * Getter for property method.
     *
     * @return Value of property method.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Setter for property method.
     *
     * @param method New value of property method.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Getter for property unit.
     *
     * @return Value of property unit.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Setter for property unit.
     *
     * @param unit New value of property unit.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Getter for property unitName
     *
     * @return Value of property unitName.
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Setter for property unitName.
     *
     * @param unitName New value of property unitName.
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * Getter for property route.
     *
     * @return Value of property route.
     */
    public String getRoute() {
        return route;
    }

    /**
     * Setter for property route.
     *
     * @param route New value of property route.
     */
    public void setRoute(String route) {
        this.route = route;
    }

    public String getDrugForm() {
        return drugForm;
    }

    public void setDrugForm(String drugForm) {
        this.drugForm = drugForm;
    }

    /**
     * Setter for property custom (does it have customized directions)
     *
     * @param custom value for custom
     */
    public void setCustomInstr(boolean custom) {
        this.custom = custom;
    }

    public boolean getCustomInstr() {
        return this.custom;
    }

    /**
     * Getter for property rxCreatedDate.
     *
     * @return Value of property rxCreatedDate.
     */
    public Date getRxCreatedDate() {
        return rxCreatedDate;
    }

    /**
     * Setter for property rxCreatedDate.
     *
     * @param rxCreatedDate New value of property rxCreatedDate.
     */
    public void setRxCreatedDate(Date rxCreatedDate) {
        this.rxCreatedDate = rxCreatedDate;
    }

    /**
     * Getter for property dosage.
     *
     * @return Value of property dosage.
     */
    public String getDosage() {
        return dosage;
    }

    /**
     * Setter for property dosage.
     *
     * @param dosage New value of property dosage.
     */
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Integer getRefillDuration() {
        return refillDuration;
    }

    public void setRefillDuration(int refillDuration) {
        this.refillDuration = refillDuration;
    }

    public Integer getRefillQuantity() {
        return refillQuantity;
    }

    public void setRefillQuantity(int refillQuantity) {
        this.refillQuantity = refillQuantity;
    }

    public String getDispenseInterval() {
        return dispenseInterval;
    }

    public void setDispenseInterval(String dispenseInterval) {
        this.dispenseInterval = dispenseInterval;
    }

}
