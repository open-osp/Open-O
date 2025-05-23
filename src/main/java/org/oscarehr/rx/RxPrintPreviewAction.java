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

package org.oscarehr.rx;

import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.SiteDao;
import org.oscarehr.common.model.*;
import org.oscarehr.managers.FaxManager;
import org.oscarehr.ui.servlet.ImageRenderingServlet;
import org.oscarehr.util.DigitalSignatureUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.owasp.encoder.Encode;
import oscar.OscarProperties;
import oscar.oscarProvider.data.ProSignatureData;
import oscar.oscarRx.data.RxPharmacyData;
import oscar.oscarRx.pageUtil.RxSessionBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

public class RxPrintPreviewAction extends DispatchAction {

    private final ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
    private final OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
    private final SiteDao siteDao = SpringUtils.getBean(SiteDao.class);
    private final FaxManager faxManager = SpringUtils.getBean(FaxManager.class);
    private static final boolean isMultiSitesEnabled = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();


    /**
     * Main method for handling print preview action
     * Prepares all necessary data for the prescription print preview
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @return An ActionForward to the JSP page that will display the preview
     */
    public ActionForward printPreview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        request.setAttribute("loggedInInfo", loggedInInfo);
        HttpSession session = request.getSession();

        oscar.oscarRx.pageUtil.RxSessionBean sessionBean = setupProviderInfo(request, session);

        setupPageSizes(request);

        List<String> addressName = new ArrayList<>();
        List<String> address = new ArrayList<>();
        setupClinicAddresses(request, session, sessionBean, addressName, address);

        setupComment(request);

        PharmacyInfo pharmacy = setupPharmacyInfo(request);

        setupDigitalSignature(request, loggedInInfo);

        setupFaxConfigs(request, loggedInInfo);

        setupPatientDiagnosis(request, sessionBean);

        setupDisplayFlags(request, sessionBean);

        setupAdditionalAttributes(request, loggedInInfo, pharmacy);

        return new ActionForward(mapping.findForward("success"));
    }

    /**
     * Sets up provider information and retrieves the session bean
     *
     * @param request The HTTP request
     * @param session The HTTP session
     * @return The RxSessionBean for the current session
     */
    private oscar.oscarRx.pageUtil.RxSessionBean setupProviderInfo(HttpServletRequest request, HttpSession session) {
        oscar.oscarRx.pageUtil.RxSessionBean sessionBean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        Provider provider = this.providerManager.getProvider(sessionBean.getProviderNo());
        String providerFax = provider.getWorkPhone();
        if (providerFax == null) {
            providerFax = "";
        }
        providerFax = providerFax.replaceAll("[^0-9]", "");
        request.setAttribute("providerFax", providerFax);

        String reprint = request.getSession().getAttribute("rePrint") != null ? (String) request.getSession().getAttribute("rePrint") : "false";
        if (reprint.equalsIgnoreCase("true")) {
            sessionBean = (oscar.oscarRx.pageUtil.RxSessionBean) session.getAttribute("tmpBeanRX");
        }
        request.setAttribute("sessionBean", sessionBean);
        request.setAttribute("reprint", reprint);

        return sessionBean;
    }

    /**
     * Sets up page size options for the prescription
     *
     * @param request The HTTP request
     */
    private void setupPageSizes(HttpServletRequest request) {
        List<KeyValuePair> pageSizes = new ArrayList<>();
        pageSizes.add(new KeyValuePair("A4 page", "PageSize.A4"));
        pageSizes.add(new KeyValuePair("A6 page", "PageSize.A6"));
        pageSizes.add(new KeyValuePair("Letter page", "PageSize.Letter"));
        request.setAttribute("pageSizes", pageSizes);
    }

    /**
     * Sets up clinic addresses based on multi-site configuration
     *
     * @param request     The HTTP request
     * @param session     The HTTP session
     * @param sessionBean The RxSessionBean
     * @param addressName List to store address names
     * @param address     List to store formatted addresses
     */
    private void setupClinicAddresses(HttpServletRequest request, HttpSession session,
                                     oscar.oscarRx.pageUtil.RxSessionBean sessionBean,
                                     List<String> addressName, List<String> address) {
        OscarProperties props = OscarProperties.getInstance();

        if (isMultiSitesEnabled) {
            setupMultiSiteAddresses(session, sessionBean, addressName, address);
        } else if (props.getProperty("clinicSatelliteName") != null) {
            setupSatelliteAddresses(sessionBean, props, addressName, address, request);
        }

        request.setAttribute("addressName", addressName);
        request.setAttribute("address", address);

        // Handle selected address
        String selectedAddressIndex = request.getParameter("addressSel");
        if (selectedAddressIndex == null && session.getAttribute("RX_ADDR") != null) {
            selectedAddressIndex = (String) session.getAttribute("RX_ADDR");
        }

        if (address != null && !address.isEmpty()) {
            String selectedAddress = getSelectedAddress(selectedAddressIndex, address);
            request.setAttribute("selectedAddress", selectedAddress);
            request.setAttribute("useSC", !selectedAddress.isEmpty());
        }
    }

    /**
     * Sets up addresses for multi-site configuration
     *
     * @param session     The HTTP session
     * @param sessionBean The RxSessionBean
     * @param addressName List to store address names
     * @param address     List to store formatted addresses
     */
    private void setupMultiSiteAddresses(HttpSession session, oscar.oscarRx.pageUtil.RxSessionBean sessionBean,
                                        List<String> addressName, List<String> address) {
        String appt_no = (String) session.getAttribute("cur_appointment_no");
        String location = null;
        if (appt_no != null) {
            Appointment result = appointmentDao.find(Integer.parseInt(appt_no));
            if (result != null) location = result.getLocation();
        }

        String doctorName = getDoctorName(sessionBean);

        java.util.ResourceBundle rb = java.util.ResourceBundle.getBundle("oscarResources", java.util.Locale.getDefault());
        List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));

        for (int i = 0; i < sites.size(); i++) {
            Site s = sites.get(i);
            addressName.add(s.getName());
            address.add("<b>" + doctorName + "</b><br>" + s.getName() + "<br>" + s.getAddress() + "<br>" +
                       s.getCity() + ", " + s.getProvince() + " " + s.getPostal() + "<br>" +
                       rb.getString("RxPreview.msgTel") + ": " + s.getPhone() + "<br>" +
                       rb.getString("RxPreview.msgFax") + ": " + s.getFax());
            if (s.getName().equals(location)) session.setAttribute("RX_ADDR", String.valueOf(i));
        }
    }

    /**
     * Sets up addresses for satellite clinics
     *
     * @param sessionBean The RxSessionBean
     * @param props       OscarProperties instance
     * @param addressName List to store address names
     * @param address     List to store formatted addresses
     * @param request     The HTTP request
     */
    private void setupSatelliteAddresses(oscar.oscarRx.pageUtil.RxSessionBean sessionBean, OscarProperties props,
                                        List<String> addressName, List<String> address, HttpServletRequest request) {
        String doctorName = getDoctorName(sessionBean);

        String[] temp0 = props.getProperty("clinicSatelliteName", "").split("\\|");
        String[] temp1 = props.getProperty("clinicSatelliteAddress", "").split("\\|");
        String[] temp2 = props.getProperty("clinicSatelliteCity", "").split("\\|");
        String[] temp3 = props.getProperty("clinicSatelliteProvince", "").split("\\|");
        String[] temp4 = props.getProperty("clinicSatellitePostal", "").split("\\|");
        String[] temp5 = props.getProperty("clinicSatellitePhone", "").split("\\|");
        String[] temp6 = props.getProperty("clinicSatelliteFax", "").split("\\|");
        java.util.ResourceBundle rb = java.util.ResourceBundle.getBundle("oscarResources", request.getLocale());

        for (int i = 0; i < temp0.length; i++) {
            addressName.add(temp0[i]);
            address.add("<b>" + Encode.forHtml(doctorName) + "</b><br>" + Encode.forHtml(temp0[i]) + "<br>" +
                       Encode.forHtml(temp1[i]) + "<br>" + temp2[i] + ", " + temp3[i] + " " + temp4[i] + "<br>" +
                       rb.getString("RxPreview.msgTel") + ": " + temp5[i] + "<br>" +
                       rb.getString("RxPreview.msgFax") + ": " + temp6[i]);
        }
    }

    /**
     * Gets the doctor's name with proper formatting
     *
     * @param sessionBean The RxSessionBean
     * @return Formatted doctor name
     */
    private String getDoctorName(oscar.oscarRx.pageUtil.RxSessionBean sessionBean) {
        oscar.oscarRx.data.RxProviderData.Provider rxprovider = new oscar.oscarRx.data.RxProviderData().getProvider(sessionBean.getProviderNo());
        ProSignatureData sig = new ProSignatureData();
        boolean hasSig = sig.hasSignature(sessionBean.getProviderNo());
        String doctorName = "";
        if (hasSig) {
            doctorName = sig.getSignature(sessionBean.getProviderNo());
        } else {
            doctorName = (rxprovider.getFirstName() + ' ' + rxprovider.getSurname());
        }
        doctorName = doctorName.replaceAll("\\d{6}", "");
        doctorName = doctorName.replaceAll("\\-", "");
        return doctorName;
    }

    /**
     * Sets up comment from session
     *
     * @param request The HTTP request
     */
    private void setupComment(HttpServletRequest request) {
        String comment = request.getSession().getAttribute("comment") != null ?
                        request.getSession().getAttribute("comment").toString() : "";
        request.getSession().removeAttribute("comment");
        request.setAttribute("comment", comment);
    }

    /**
     * Sets up pharmacy information
     *
     * @param request The HTTP request
     * @return PharmacyInfo object or null if not found
     */
    private PharmacyInfo setupPharmacyInfo(HttpServletRequest request) {
        String pharmacyId = request.getParameter("pharmacyId");
        RxPharmacyData pharmacyData = new RxPharmacyData();
        PharmacyInfo pharmacy = null;

        String prefPharmacy = "";
        String prefPharmacyId = "";
        if (pharmacyId != null && !"null".equalsIgnoreCase(pharmacyId)) {
            pharmacy = pharmacyData.getPharmacy(pharmacyId);
            if (pharmacy != null) {
                prefPharmacy = pharmacy.getName().replace("'", "\\'");
                prefPharmacyId = String.valueOf(pharmacy.getId());
                prefPharmacy = prefPharmacy.trim();
                prefPharmacyId = prefPharmacyId.trim();

                request.setAttribute("prefPharmacy", prefPharmacy);
                request.setAttribute("prefPharmacyId", prefPharmacyId);

                setupPharmacyAddress(request, pharmacy);
            }
            request.setAttribute("pharmacy", pharmacy);
        }

        return pharmacy;
    }

    /**
     * Sets up pharmacy address
     *
     * @param request  The HTTP request
     * @param pharmacy The PharmacyInfo object
     */
    private void setupPharmacyAddress(HttpServletRequest request, PharmacyInfo pharmacy) {
        StringJoiner addressJoiner = new StringJoiner("<br>");
        addressJoiner.add(pharmacy.getName());
        addressJoiner.add(pharmacy.getAddress());
        addressJoiner.add(pharmacy.getCity() + ", " + pharmacy.getProvince() + " " + pharmacy.getPostalCode());
        addressJoiner.add("Tel: " + pharmacy.getPhone1() + " " + pharmacy.getPhone2());
        addressJoiner.add("Fax: " + pharmacy.getFax());
        if (pharmacy.getEmail() != null && !pharmacy.getEmail().isEmpty()) {
            addressJoiner.add("Email: " + pharmacy.getEmail());
        }
        if (pharmacy.getNotes() != null && !pharmacy.getNotes().isEmpty()) {
            addressJoiner.add("Note: " + pharmacy.getNotes());
        }

        request.setAttribute("pharmacyAddress", Encode.forHtml(addressJoiner.toString()));
    }

    /**
     * Sets up digital signature
     *
     * @param request     The HTTP request
     * @param loggedInInfo The LoggedInInfo object
     */
    private void setupDigitalSignature(HttpServletRequest request, LoggedInInfo loggedInInfo) {
        String signatureRequestId = DigitalSignatureUtils.generateSignatureRequestId(loggedInInfo.getLoggedInProviderNo());
        String imageUrl = request.getContextPath() + "/imageRenderingServlet?source=" +
                         ImageRenderingServlet.Source.signature_preview.name() + "&" +
                         DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY + "=" + signatureRequestId;

        request.setAttribute("signatureRequestId", signatureRequestId);
        request.setAttribute("imageUrl", imageUrl);
        request.setAttribute("tempPath", System.getProperty("java.io.tmpdir")
                .replaceAll("\\\\", "/")
                + "/signature_"
                + signatureRequestId
                + ".jpg"
        );
    }

    /**
     * Sets up fax configurations
     *
     * @param request     The HTTP request
     * @param loggedInInfo The LoggedInInfo object
     */
    private void setupFaxConfigs(HttpServletRequest request, LoggedInInfo loggedInInfo) {
        List<FaxConfig> faxConfigs = faxManager.getFaxGatewayAccounts(loggedInInfo);
        request.setAttribute("faxConfigs", faxConfigs);
    }

    /**
     * Sets up patient diagnosis
     *
     * @param request     The HTTP request
     * @param sessionBean The RxSessionBean
     */
    private void setupPatientDiagnosis(HttpServletRequest request, RxSessionBean sessionBean) {
        int hsfo_patient_id = sessionBean.getDemographicNo();
        oscar.form.study.HSFO.HSFODAO hsfoDAO = new oscar.form.study.HSFO.HSFODAO();
        int dx = hsfoDAO.retrievePatientDx(String.valueOf(hsfo_patient_id));
        request.setAttribute("dx", dx);
    }

    /**
     * Sets up various UI display flags
     *
     * @param request The HTTP request
     */
    private void setupDisplayFlags(HttpServletRequest request, RxSessionBean sessionBean) {
        boolean isRxFaxEnabled = OscarProperties.getInstance().isRxFaxEnabled();
        request.setAttribute("showRxFaxBlock", isRxFaxEnabled);

        boolean isFaxButtonsDisabled  = ((sessionBean.getStashSize() == 0
                || Objects.isNull(sessionBean.getStashItem(0).getDigitalSignatureId())) ? "disabled" : "").isEmpty();
        request.setAttribute("isFaxDisabled", isFaxButtonsDisabled);

        boolean rxEnabled = OscarProperties.getInstance().isRxSignatureEnabled();
        boolean disableTablet = !OscarProperties.getInstance().getBooleanProperty("signature_tablet", "yes");
        request.setAttribute("showSignatureBlock", rxEnabled && disableTablet);

        request.setAttribute("rxPasteAsterisk", OscarProperties.getInstance().isPropertyActive("rx_paste_asterisk"));
    }

    /**
     * Sets up additional attributes
     *
     * @param request     The HTTP request
     * @param loggedInInfo The LoggedInInfo object
     * @param pharmacy    The PharmacyInfo object
     */
    private void setupAdditionalAttributes(HttpServletRequest request, LoggedInInfo loggedInInfo, PharmacyInfo pharmacy) {
        request.setAttribute("prescribedBy", Encode.forJavaScript(loggedInInfo.getLoggedInProvider().getFormattedName()));

        String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(Calendar.getInstance().getTime());
        request.setAttribute("timeStamp", timeStamp);

        request.setAttribute("pharmacyName", Encode.forJavaScript(pharmacy != null ? pharmacy.getName() : ""));
        request.setAttribute("pharmacyFax", pharmacy != null ? pharmacy.getFax() : "");
    }

    /**
     * Gets the selected address based on the address index
     *
     * @param addressIndex The index of the selected address
     * @param addresses    The list of addresses
     * @return The selected address or empty string if not found
     */
    private String getSelectedAddress(String addressIndex, List<String> addresses) {
        if (addressIndex != null && addresses != null && !addresses.isEmpty()) {
            try {
                int index = Integer.parseInt(addressIndex);
                if (index >= 0 && index < addresses.size()) {
                    return addresses.get(index);
                }
            } catch (NumberFormatException e) {
                // Invalid index, return empty string
            }
        }
        return "";
    }

}
