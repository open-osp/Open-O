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

import ca.openosp.openo.caisi_integrator.ws.CachedDemographicPrevention;
import ca.openosp.openo.commn.dao.CVCMappingDao;
import ca.openosp.openo.commn.model.CVCMapping;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.openo.prevention.Prevention;
import ca.openosp.openo.prevention.PreventionDS;
import ca.openosp.openo.prevention.PreventionData;
import ca.openosp.openo.prevention.PreventionDisplayConfig;
import ca.openosp.openo.util.StringUtils;
import org.owasp.encoder.Encode;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class EctDisplayPrevention2Action extends EctDisplayAction {
    private static final String cmd = "preventions";
    private CVCMappingDao cvcMappingDao = SpringUtils.getBean(CVCMappingDao.class);

    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_prevention", "r", null)) {
            return true; //Prevention link won't show up on new CME screen.
        } else {

            //set lefthand module heading and link
            String winName = "prevention" + bean.demographicNo;
            int demographicNumber = Integer.valueOf(bean.demographicNo);
            String url = "popupPage(700,960,'" + winName + "', '" + request.getContextPath() + "/oscarPrevention/index.jsp?demographic_no=" + bean.demographicNo + "')";
            Dao.setLeftHeading(getText("oscarEncounter.LeftNavBar.Prevent"));
            Dao.setLeftURL(url);

            //set righthand link to same as left so we have visual consistency with other modules
            url += ";return false;";
            Dao.setRightURL(url);
            Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action

            //list warnings first as module items
            Prevention p = PreventionData.getPrevention(loggedInInfo, Integer.valueOf(bean.demographicNo));
            PreventionDS pf = SpringUtils.getBean(PreventionDS.class);//PreventionDS.getInstance();

            try {
                pf.getMessages(p);
            } catch (Exception dsException) {
                return false;
            }

            //now we list prevention modules as items
            PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();
            ArrayList<HashMap<String, String>> prevList = pdc.getPreventions();
            Map warningTable = p.getWarningMsgs();

            String highliteColour = "#FF0000";
            String inelligibleColour = "#FF6600";
            String pendingColour = "#FF00FF";
            Date date = null;

            ArrayList<NavBarDisplayDAO.Item> warnings = new ArrayList<NavBarDisplayDAO.Item>();
            ArrayList<NavBarDisplayDAO.Item> items = new ArrayList<NavBarDisplayDAO.Item>();
            String result;
            Date demographicDateOfBirth = PreventionData.getDemographicDateOfBirth(loggedInInfo, demographicNumber);

            // fetch and cache any remote integrated preventions.
            List<CachedDemographicPrevention> integratedPreventions = null;
            List<CachedDemographicPrevention> remotePreventions = PreventionData.getRemotePreventions(loggedInInfo, demographicNumber);

            if (remotePreventions.size() > 0) {
                integratedPreventions = new ArrayList<CachedDemographicPrevention>();
                integratedPreventions.addAll(remotePreventions);
            }

            for (int i = 0; i < prevList.size(); i++) {
                NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
                HashMap<String, String> h = prevList.get(i);
                String prevName = h.get("name");
                ArrayList<Map<String, Object>> alist = PreventionData.getPreventionData(loggedInInfo, prevName, demographicNumber);

                if (integratedPreventions != null) {
                    PreventionData.addRemotePreventions(loggedInInfo, integratedPreventions, alist, prevName, demographicDateOfBirth);
                }

                boolean show = pdc.display(loggedInInfo, h, bean.demographicNo, alist.size());
                if (show) {
                    String newestRecordId = null;
                    Map<String, Object> newestRecord = null;
                    if (alist.size() > 0) {
                        Map<String, Object> hdata = alist.get(alist.size() - 1);
                        newestRecord = hdata;
                        newestRecordId = (String) hdata.get("id");
                        Map<String, String> hExt = PreventionData.getPreventionKeyValues((String) hdata.get("id"));
                        result = hExt.get("result");

                        Object dateObj = hdata.get("prevention_date_asDate");
                        if (dateObj instanceof Date) {
                            date = (Date) dateObj;
                        } else if (dateObj instanceof java.util.GregorianCalendar) {
                            Calendar cal = (Calendar) dateObj;
                            date = cal.getTime();
                        }

                        item.setDate(date);

                        if (hdata.get("refused") != null && hdata.get("refused").equals("2")) {
                            item.setColour(inelligibleColour);
                        } else if (result != null && result.equalsIgnoreCase("pending")) {
                            item.setColour(pendingColour);
                        }

                        if (hdata.containsKey("integratorDemographicId")) {
                            item.setBgColour("#FFCCCC");

                        }
                    } else {
                        item.setDate(null);
                    }

                    String title = StringUtils.maxLenString(h.get("name"), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
                    item.setTitle(title);
                    item.setLinkTitle(h.get("desc"));

                    //clicking this prevention in the echart opens it directly: the patient's newest record of it,
                    //or a blank form to add one
                    String formPage = "AddPreventionData.jsp";
                    String formQuery = "demographic_no=" + Encode.forUriComponent(bean.demographicNo);
                    int formHeight = 600;
                    int formWidth = 900;
                    if (newestRecordId != null) {
                        formQuery += "&id=" + Encode.forUriComponent(newestRecordId);
                    } else if (newestRecord != null && newestRecord.get("integratorFacilityId") != null) {
                        //a record held only at another clinic has no local id; show it read-only, the same as from the list
                        formPage = "display_remote_prevention.jsp";
                        formHeight = 300;
                        formWidth = 500;
                        formQuery += "&remoteFacilityId=" + Encode.forUriComponent(String.valueOf(newestRecord.get("integratorFacilityId")))
                                + "&remotePreventionId=" + Encode.forUriComponent(String.valueOf(newestRecord.get("integratorPreventionId")));
                    } else {
                        formQuery += "&prevention=" + Encode.forUriComponent(prevName)
                                + "&prevResultDesc=" + Encode.forUriComponent(String.valueOf(h.get("resultDesc")));
                        //the SNOMED code switches the blank form into immunization mode, the same as from the list
                        if (h.get("snomedConceptCode") != null) {
                            formQuery += "&snomedId=" + Encode.forUriComponent(h.get("snomedConceptCode"));
                        }
                        //a prevention mapped to more than one CVC vaccine first asks which one, the same as from the list
                        List<CVCMapping> mappings = cvcMappingDao.findMultipleByOscarName(prevName);
                        if (mappings != null && mappings.size() > 1) {
                            formPage = "AddPreventionDataDisambiguate.jsp";
                        }
                    }
                    String formUrl = request.getContextPath() + "/oscarPrevention/" + formPage + "?" + formQuery;
                    //one prevention form window per patient
                    String formWindow = "addPreventionData" + bean.demographicNo;
                    //the JS encoding keeps the handler safe inside the onclick attribute the navbar writes it into
                    item.setURL("popupPage(" + formHeight + "," + formWidth + ",'" + Encode.forJavaScript(formWindow) + "', '"
                            + Encode.forJavaScript(formUrl) + "'); return false;");

                    //if there's a warning associated with this prevention set item apart
                    if (warningTable.containsKey(prevName)) {
                        item.setColour(highliteColour);
                        warnings.add(item);
                    } else {
                        items.add(item);
                    }
                }
            }

            //sort items without warnings chronologically
            Dao.sortItems(items, NavBarDisplayDAO.DATESORT_ASC);

            //add warnings to Dao array first so they will be at top of list
            for (int idx = 0; idx < warnings.size(); ++idx) {
                Dao.addItem(warnings.get(idx));
            }

            //now copy remaining sorted items
            for (int idx = 0; idx < items.size(); ++idx) {
                Dao.addItem(items.get(idx));
            }

            return true;
        }
    }

    public String getCmd() {
        return cmd;
    }

}
