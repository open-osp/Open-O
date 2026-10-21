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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.Logger;
import ca.openosp.openo.commn.dao.EncounterFormDao;
import ca.openosp.openo.commn.model.EncounterForm;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.MiscUtils;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.OscarProperties;
import ca.openosp.openo.encounter.data.EctFormData;
import ca.openosp.openo.lab.LabRequestReportLink;
import ca.openosp.openo.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class EctDisplayForm2Action extends EctDisplayAction {

    private static Logger logger = MiscUtils.getLogger();
    private static final ObjectMapper JSON = new ObjectMapper();

    /**
     * Forms that get the "form already exists" warning, listed by their database table name.
     *
     * <p>These are longitudinal forms. The patient has one record of the form, and the provider
     * opens that same record again at each visit to add to it. Starting a blank copy by mistake
     * puts an empty form on top of the one that holds all the data, so the Add Form menu warns
     * first.</p>
     *
     * <p>Forms not listed here are snapshot forms. Each one records a single visit or assessment,
     * such as the Annual, a questionnaire, a lab requisition or a Mental Health Act form. Making a
     * new record of those is normal, so they open blank without a warning.</p>
     *
     * <ul>
     * <li>Rourke, Rourke2006, Rourke2009, Rourke2017, Rourke2020: one well-baby record from the
     * first week to age five</li>
     * <li>Growth Charts, Growth 0-36m: measurements added over time</li>
     * <li>AR, AR2005, ON AR Enhanced: one antenatal record per pregnancy, with a visit row for
     * each appointment</li>
     * <li>Health Passport: an ongoing patient summary</li>
     * <li>Chart Checklist: chart items checked off over time</li>
     * <li>CHF, T2Diabetes: chronic disease records with results recorded at multiple dates</li>
     * <li>Pall. Care: a patient care flowsheet with four dated visit columns</li>
     * <li>PeriMenopausal, Mental Health: multiple visit dates recorded on one record</li>
     * <li>Ovulation: a cycle chart filled in day by day</li>
     * <li>ImmunAllergies: an immunization and allergy record updated over time</li>
     * </ul>
     */
    private static final Set<String> LONGITUDINAL_FORM_TABLES = Set.of(
            "formrourke", "formrourke2006", "formrourke2009", "formrourke2017", "formrourke2020",
            "formgrowthchart", "formgrowth0_36",
            "formar", "formonar", "formonarenhanced", "formonarenhancedrecord",
            "formbchp", "formbcclientchartchecklist",
            "formchf", "formtype2diabetes", "formpalliativecare", "formperimenopausal", "formmentalhealth",
            "formovulation", "formimmunallergy");

    private String cmd = "forms";
    private String menuId = "1";

    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        String appointmentNo = bean.appointmentNo;
        if (appointmentNo == null && request.getSession().getAttribute("cur_appointment_no") != null) {
            appointmentNo = (String) request.getSession().getAttribute("cur_appointment_no");

        }


        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_form", "r", null)) {
            return true; // The link of form won't show up on new CME screen.
        } else {
            try {

                String winName = "Forms" + bean.demographicNo;
                StringBuilder url = new StringBuilder("popupPage(600, 700, '" + winName + "', '" + request.getContextPath() + "/oscarEncounter/formlist.jsp?demographic_no=" + bean.demographicNo + "')");

                // set text for lefthand module title
                Dao.setLeftHeading(getText("oscarEncounter.Index.msgForms"));
                // set link for lefthand module title
                Dao.setLeftURL(url.toString());

                // we're going to display a pop up menu of forms so we set the menu title and id num of menu
                Dao.setRightHeadingID(menuId);
                Dao.setMenuHeader(getText("oscarEncounter.LeftNavBar.AddFrm"));
                StringBuilder javascript = new StringBuilder("<script type=\"text/javascript\">");
                String js = "";
                String serviceDateStr;
                StringBuilder strTitle;
                String fullTitle;
                Date date = null;
                String key;
                int hash;
                // grab all of the forms
                EncounterFormDao encounterFormDao = (EncounterFormDao) SpringUtils.getBean(EncounterFormDao.class);
                List<EncounterForm> encounterForms = encounterFormDao.findAll();
                Collections.sort(encounterForms, EncounterForm.BC_FIRST_COMPARATOR);

                String BGCOLOUR = request.getParameter("hC");
                for (EncounterForm encounterForm : encounterForms) {
                    if (encounterForm.getFormName().equalsIgnoreCase("Discharge Summary")) {
                        String caisiProperty = OscarProperties.getInstance().getProperty("caisi");
                        if (caisiProperty != null && (caisiProperty.equalsIgnoreCase("yes")
                                || caisiProperty.equalsIgnoreCase("true")
                                || caisiProperty.equalsIgnoreCase("on"))) {

                        } else {
                            continue; //form out
                        }
                    }
                    winName = encounterForm.getFormName() + bean.demographicNo;

                    String table = encounterForm.getFormTable();
                    if (!table.equalsIgnoreCase("")) {

                        EctFormData.PatientForm[] pforms = EctFormData.getPatientFormsFromLocalAndRemote(loggedInInfo, bean.demographicNo, table);
                        // if a form has been started for the patient, create a module item for it
                        if (pforms.length > 0) {

                            NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
                            EctFormData.PatientForm pfrm = pforms[0];

                            // convert date to that specified in base class
                            DateFormat formatter = new SimpleDateFormat(EctFormData.DATETIME_FORMAT);
                            serviceDateStr = pfrm.getEdited();

                            try {
                                date = formatter.parse(serviceDateStr);
                            } catch (ParseException ex) {
                                logger.debug("EctDisplayForm2Action: Error creating date " + ex.getMessage());
                            }

                            item.setDate(date);

                            fullTitle = encounterForm.getFormName();
                            strTitle = new StringBuilder(StringUtils.maxLenString(fullTitle, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES));

                            if (table.equals("formLabReq07")) {
                                Long reportId = null;

                                HashMap<String, Object> res = LabRequestReportLink.getLinkByRequestId("formLabReq07", Long.valueOf(pfrm.getFormId()));
                                reportId = (Long) res.get("report_id");

                                if (reportId == null) {
                                    strTitle.insert(0, "*");
                                    strTitle.append("*");
                                }
                            }
                            winName = winName + serviceDateStr;
                            hash = Math.abs(winName.hashCode());
                            url = new StringBuilder(
                                    "popupPage(700,960,'" + hash + "started', '" +
                                            request.getContextPath() +
                                            "/form/forwardshortcutname.do?formname="
                                            + encounterForm.getFormName() +
                                            "&demographic_no=" + bean.demographicNo +
                                            (pfrm.getRemoteFacilityId() != null ? "&remoteFacilityId=" + pfrm.getRemoteFacilityId() : "") +
                                            (appointmentNo != null ? "&appointmentNo=" + appointmentNo : "")
                                            + "&formId=latest" + "');");

                            key = StringUtils.maxLenString(fullTitle, MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + serviceDateStr + ")";
                            key = StringEscapeUtils.escapeEcmaScript(key);

                            // auto completion arrays and colour code are set
                            js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompList.push('" + key + "'); autoCompleted['" + key + "'] = \"" + url + "\";";
                            javascript.append(js);

                            // set item href text
                            item.setTitle(strTitle.toString());
                            // set item link
                            url.append("return false;");
                            item.setURL(url.toString());
                            // set item link title text
                            item.setLinkTitle(fullTitle + " " + serviceDateStr);


                            //sorry I have to do this, since the "hidden" field, doesn't mean hidden.
                            //this is a fix so that when they've migrated to the enhanced form, the
                            //regular one is hidden. It's still accessible from the list mode off
                            //the tab header though, if they really need to get to it.
                            boolean dontAdd = false;
                            if (table.equals("formONAR")) {
                                //check to see if we have an enhanced one
                                EctFormData.PatientForm[] pf = EctFormData.getPatientFormsFromLocalAndRemote(loggedInInfo, bean.demographicNo, "formONAREnhancedRecord");
                                if (pf.length > 0) {
                                    dontAdd = true;
                                }
                            }
                            if (!dontAdd)
                                Dao.addItem(item);
                        }
                    }

                    // we add all unhidden forms to the pop up menu
                    if (!encounterForm.isHidden()) {
                        hash = Math.abs(winName.hashCode());
                        url = new StringBuilder("popupPage(700,960,'" + hash + "new', '" + encounterForm.getFormValue() + bean.demographicNo + "&formId=0&provNo=" + bean.providerNo + "&parentAjaxId=" + cmd + ((appointmentNo != null) ? "&appointmentNo=" + appointmentNo : "") + "')");
                        Dao.addPopUpUrl(url.toString());
                        key = StringUtils.maxLenString(encounterForm.getFormName(), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + " (new)";
                        Dao.addPopUpText(encounterForm.getFormName());
                        key = StringEscapeUtils.escapeEcmaScript(key);

                        // auto completion arrays and colour code are set
                        js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompList.push('" + key + "'); autoCompleted['" + key + "'] = \"" + url + ";\";";
                        javascript.append(js);
                    }
                }
                url = new StringBuilder("return !showMenu('" + menuId + "', event);");
                Dao.setRightURL(url.toString());

                javascript.append("</script>");
                Dao.setJavaScript(javascript.toString());

                // sort module items, i.e. forms, from most recently started to more distant
                Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
            } catch (Exception e) {
                logger.error("EctDisplayForm2Action SQL ERROR:", e);
                return false;
            }

            return true;
        }
    }

    /**
     * Answers whether a patient already has a record of one form.
     *
     * <p>The Add Form menu asks before it opens a blank form, so a misclick can be turned into
     * opening the record the patient already has. Only forms kept up to date across visits are
     * looked up; a snapshot form, such as the Annual, always answers false because a new record of
     * it is expected.</p>
     *
     * <p>The patient is the demographicNo parameter, sent by the chart the click happened in. The
     * session's encounter bean is not used: it holds whichever chart loaded last, so with two
     * charts open in two tabs it can name the other patient. The privilege check is made against
     * the same parameter, so a caller cannot ask about a patient they may not read.</p>
     *
     * <p>Writes JSON holding exists, and when true the lastEdited stamp of the most recent record,
     * the url that opens it, and the windowName the Forms box opens that record in, so the warning
     * reuses the box's window instead of opening the record a second time.</p>
     *
     * @return String null, the answer is written straight to the response
     * @throws IOException if the response cannot be written
     */
    public String checkExisting() throws IOException {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String demographicNo = request.getParameter("demographicNo");

        // up to nine digits fits the int the form lookup parses it into
        if (demographicNo == null || !demographicNo.matches("\\d{1,9}")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "demographicNo required");
            return null;
        }
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_form", "r", demographicNo)) {
            throw new SecurityException("missing required sec object (_form)");
        }

        String appointmentNo = request.getParameter("appointmentNo");
        if (appointmentNo != null && !appointmentNo.matches("\\d+")) {
            appointmentNo = null;
        }

        ObjectNode answer = JSON.createObjectNode();
        answer.put("exists", false);

        EncounterFormDao encounterFormDao = SpringUtils.getBean(EncounterFormDao.class);
        for (EncounterForm encounterForm : encounterFormDao.findByFormName(request.getParameter("formName"))) {
            String table = encounterForm.getFormTable();
            if (!isLongitudinal(table)) {
                continue;
            }

            // same lookup, record, date and link as the Forms section in getInfo, so the
            // warning describes the entry shown there and the button opens what clicking it opens
            EctFormData.PatientForm[] pforms =
                    EctFormData.getPatientFormsFromLocalAndRemote(loggedInInfo, demographicNo, table);
            if (pforms.length == 0) {
                continue;
            }

            EctFormData.PatientForm latest = pforms[0];
            // getEdited() formats the date without a null check
            String lastEdited = latest.edited != null ? latest.getEdited() : latest.getCreated();
            answer.put("exists", true);
            answer.put("lastEdited", lastEdited);
            // same name getInfo gives the box's link: form name, patient and edited stamp
            answer.put("windowName",
                    Math.abs((encounterForm.getFormName() + demographicNo + lastEdited).hashCode()) + "started");
            answer.put("url", request.getContextPath()
                    + "/form/forwardshortcutname.do?formname="
                    + URLEncoder.encode(encounterForm.getFormName(), StandardCharsets.UTF_8)
                    + "&demographic_no=" + demographicNo
                    + (latest.getRemoteFacilityId() != null ? "&remoteFacilityId=" + latest.getRemoteFacilityId() : "")
                    + (appointmentNo != null ? "&appointmentNo=" + appointmentNo : "")
                    + "&formId=latest");
            break;
        }

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(answer.toString());
        return null;
    }

    /**
     * Tells whether a form is one the patient keeps up to date across visits, so that opening a
     * blank copy of it deserves a warning.
     *
     * @param formTable String the form's table name, as recorded on its Add Form menu entry, may be null
     * @return boolean true for a longitudinal form, false for a one-visit snapshot or no table
     */
    private static boolean isLongitudinal(String formTable) {
        return formTable != null && LONGITUDINAL_FORM_TABLES.contains(formTable.trim().toLowerCase(Locale.ROOT));
    }

    @Override
    public String getCmd() {
        return cmd;
    }
}
