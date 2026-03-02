//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package ca.openosp.openo.encounter.pageUtil;

import ca.openosp.openo.prescript.data.RxPrescriptionData;
import org.apache.logging.log4j.Logger;
import ca.openosp.openo.PMmodule.caisi_integrator.CaisiIntegratorManager;
import ca.openosp.openo.PMmodule.caisi_integrator.IntegratorFallBackManager;
import ca.openosp.openo.caisi_integrator.ws.CachedDemographicDrug;
import ca.openosp.openo.provider.web.CppPreferencesUIBean;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.prescript.data.RxPrescriptionData.Prescription;
import ca.openosp.openo.util.DateUtils;
import ca.openosp.openo.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EctDisplayRx2Action extends EctDisplayAction {
    private String cmd = "Rx";
    private static final Logger logger = MiscUtils.getLogger();

    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao) {

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_rx", "r", null)) {
            return true; //Prescription link won't show up on new CME screen.
        } else {

            //set lefthand module heading and link
            String winName = "Rx" + bean.demographicNo;
            String leftUrl = "popupPage(580,1027,'" + winName + "','" + request.getContextPath() + "/oscarRx/choosePatient.do?providerNo=" + bean.providerNo + "&demographicNo=" + bean.demographicNo + "')";
            String url = leftUrl;
            Dao.setLeftHeading(getText("oscarEncounter.NavBar.Medications"));
            Dao.setLeftURL(leftUrl);

            //set righthand link to same as left so we have visual consistency with other modules

            url += "; return false;";
            Dao.setRightURL(url);
            Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action

            //grab all of the diseases associated with patient and add a list item for each
            String dbFormat = "yyyy-MM-dd";
            String serviceDateStr;
            Date date;
            RxPrescriptionData prescriptData = new RxPrescriptionData();
            Prescription[] arr = prescriptData.getUniquePrescriptionsByPatient(Integer.parseInt(bean.demographicNo));

            ArrayList<Prescription> uniqueDrugs = new ArrayList<Prescription>();
            for (Prescription p : arr) uniqueDrugs.add(p);

            int demographicId = Integer.parseInt(bean.demographicNo);

            CppPreferencesUIBean prefsBean = new CppPreferencesUIBean(loggedInInfo.getLoggedInProviderNo());
            prefsBean.loadValues();

            // --- get integrator drugs ---
            if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
                try {


                    List<CachedDemographicDrug> remoteDrugs = null;
                    try {
                        if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
                            remoteDrugs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicDrugsByDemographicId(demographicId);
                        }
                    } catch (Exception e) {
                        MiscUtils.getLogger().error("Unexpected error.", e);
                        CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(), e);
                    }

                    if (CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())) {
                        remoteDrugs = IntegratorFallBackManager.getRemoteDrugs(loggedInInfo, demographicId);
                    }

                    logger.debug("remote Drugs : " + remoteDrugs.size());

                    for (CachedDemographicDrug remoteDrug : remoteDrugs) {
                        Prescription p = new Prescription(remoteDrug.getFacilityIdIntegerCompositePk().getIntegratorFacilityId(), remoteDrug.getCaisiProviderId(), demographicId);
                        p.setArchived(remoteDrug.isArchived() ? "1" : "0");
                        if (remoteDrug.getEndDate() != null) p.setEndDate(remoteDrug.getEndDate().getTime());
                        if (remoteDrug.getRxDate() != null) p.setRxDate(remoteDrug.getRxDate().getTime());
                        p.setSpecial(remoteDrug.getSpecial());
                        p.setOutsideProviderName(" "); //little hack so that the style gets set to "external"

                        // okay so I'm not exactly making it unique... that's the price of last minute conformance test changes.
                        uniqueDrugs.add(p);
                    }
                } catch (Exception e) {
                    logger.error("error getting remote drugs", e);
                }
            }

            // Single-pass stable partition: active prescriptions first, preserving
            // relative order within each group. Calls isActiveDrug() exactly once per
            // drug (O(n)) instead of O(n log n) times via a sort comparator, avoiding
            // repeated GregorianCalendar allocations inside Prescription.isCurrent().
            stablePartitionActiveFirst(uniqueDrugs);

            long now = System.currentTimeMillis();
            long month = 1000L * 60L * 60L * 24L * 30L;
            for (Prescription drug : uniqueDrugs) {
                if (drug.isArchived())
                    continue;
                if (drug.isHideCpp()) {
                    continue;
                }

                NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
                date = drug.getRxDate();
                serviceDateStr = DateUtils.formatDate(date, request.getLocale());

                if (prefsBean != null && "on".equals(prefsBean.getEnable())) {
                    Locale locale = request.getLocale();

                    String descr = "";
                    String title = "";

                    if (!StringUtils.isNullOrEmpty(drug.getCustomName())) {
                        descr = drug.getCustomName();
                    } else {
                        descr = drug.getBrandName();
                    }

                    if (prefsBean != null && "on".equals(prefsBean.getMedicationStartDate())) {
                        descr += " Start Date:" + DateUtils.formatDate(drug.getRxDate(), locale);
                    }
                    if (prefsBean != null && "on".equals(prefsBean.getMedicationEndDate()) && !drug.isLongTerm()) {
                        descr += " End Date:" + DateUtils.formatDate(drug.getEndDate(), locale);
                    }
                    if (prefsBean != null && "on".equals(prefsBean.getMedicationQty())) {
                        descr += " Qty:" + drug.getQuantity();
                    }
                    if (prefsBean != null && "on".equals(prefsBean.getMedicationRepeats())) {
                        descr += " Repeats:" + drug.getRepeat();
                    }

                    String tmp = "";
                    if (drug.getFullOutLine() != null)
                        tmp = drug.getFullOutLine().replaceAll(";", " ");

                    descr = "<span " + getClassColour(drug, now, month) + ">" + descr + "</span>";

                    item.setTitle(descr);
                    item.setLinkTitle(tmp + " " + serviceDateStr + " - " + drug.getEndDate());

                } else {
                    String tmp = "";
                    if (drug.getFullOutLine() != null)
                        tmp = drug.getFullOutLine().replaceAll(";", " ");

                    String strTitle = StringUtils.maxLenString(tmp, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
                    // strTitle = "<span " + styleColor + ">" + strTitle + "</span>";
                    strTitle = "<span " + getClassColour(drug, now, month) + ">" + strTitle + "</span>";
                    item.setTitle(strTitle);
                    item.setLinkTitle(tmp + " " + serviceDateStr + " - " + drug.getEndDate());
                }

                item.setURL("return false;");
                Dao.addItem(item);
            }

            return true;
        }
    }

    String getClassColour(Prescription drug, long referenceTime, long durationToSoon) {
        StringBuilder sb = new StringBuilder("class=\"");

        if (!drug.isLongTerm() && (drug.isCurrent() && drug.getEndDate() != null && (drug.getEndDate().getTime() - referenceTime <= durationToSoon))) {
            sb.append("expireInReference ");
        }

        if (isActiveDrug(drug)) {
            sb.append("currentDrug ");
        }

        if (drug.isArchived()) {
            sb.append("archivedDrug ");
        }

        if (!drug.isLongTerm() && !drug.isCurrent()) {
            sb.append("expiredDrug ");
        }

        if (drug.isLongTerm()) {
            sb.append("longTermMed ");
        }

        if (drug.isDiscontinued()) {
            sb.append("discontinued ");
        }

        if (drug.getOutsideProviderName() != null && !drug.getOutsideProviderName().equals("")) {
            sb = new StringBuilder("class=\"");
            sb.append("external ");
        }

        String retval = sb.toString();

        if (retval.equals("class=\"")) {
            return "";
        }

        return retval.substring(0, retval.length()) + "\"";

    }


    /**
     * Determines whether a prescription is considered active for display purposes.
     *
     * <p>A drug is active if it is current and not archived, or if it is long-term.
     * This definition is shared between {@link #stablePartitionActiveFirst(List)}
     * and the CSS class assignment in {@link #getClassColour}.</p>
     *
     * <p><b>Note:</b> This method does not filter archived long-term drugs — it will
     * return {@code true} for a drug that is long-term even if archived. In
     * {@link #getInfo}, archived drugs are pre-filtered before sort and display.
     * If moving this to a utility class, additional checks (e.g. {@code !drug.isArchived()})
     * would be needed for standalone use.</p>
     *
     * @param drug the prescription to evaluate
     * @return boolean {@code true} if the drug is considered active
     */
    public static boolean isActiveDrug(Prescription drug) {
        return (drug.isCurrent() && !drug.isArchived()) || drug.isLongTerm();
    }

    /**
     * Reorders {@code drugs} in-place so that active prescriptions come first,
     * preserving the original relative order within each group (stable partition).
     *
     * <p>Calls {@link #isActiveDrug(Prescription)} exactly once per element
     * (O(n)), avoiding the repeated {@link java.util.GregorianCalendar} allocations
     * that would occur if the same check were used inside a sort comparator.</p>
     *
     * @param drugs the mutable list of prescriptions to reorder
     * @since 2026-03-02
     */
    public static void stablePartitionActiveFirst(List<Prescription> drugs) {
        if (drugs == null || drugs.isEmpty()) {
            return;
        }
        List<Prescription> active = new ArrayList<>(drugs.size());
        List<Prescription> inactive = new ArrayList<>();
        for (Prescription drug : drugs) {
            if (isActiveDrug(drug)) {
                active.add(drug);
            } else {
                inactive.add(drug);
            }
        }
        drugs.clear();
        drugs.addAll(active);
        drugs.addAll(inactive);
    }

    public String getCmd() {
        return cmd;
    }
}
