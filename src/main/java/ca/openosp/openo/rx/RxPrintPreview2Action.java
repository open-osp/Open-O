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

package ca.openosp.openo.rx;

import ca.openosp.OscarProperties;
import ca.openosp.openo.PMmodule.service.ProviderManager;
import ca.openosp.openo.commn.dao.OscarAppointmentDao;
import ca.openosp.openo.commn.dao.SiteDao;
import ca.openosp.openo.commn.dao.UserPropertyDAO;
import ca.openosp.openo.commn.model.Appointment;
import ca.openosp.openo.commn.model.DemographicExt;
import ca.openosp.openo.commn.model.FaxConfig;
import ca.openosp.openo.commn.model.PharmacyInfo;
import ca.openosp.openo.commn.model.Provider;
import ca.openosp.openo.commn.model.Site;
import ca.openosp.openo.commn.model.UserProperty;
import ca.openosp.openo.managers.DemographicManager;
import ca.openosp.openo.managers.FaxManager;
import ca.openosp.openo.prescript.data.RxPatientData;
import ca.openosp.openo.prescript.data.RxPharmacyData;
import ca.openosp.openo.prescript.data.RxPrescriptionData;
import ca.openosp.openo.prescript.pageUtil.RxSessionBean;
import ca.openosp.openo.prescript.util.RxUtil;
import ca.openosp.openo.providers.data.ProSignatureData;
import ca.openosp.openo.ui.servlet.ImageRenderingServlet;
import ca.openosp.openo.utility.DigitalSignatureUtils;
import ca.openosp.openo.utility.LoggedInInfo;
import ca.openosp.openo.utility.SpringUtils;
import ca.openosp.openo.web.PrescriptionQrCodeUIBean;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.apache.struts2.ServletActionContext;
import org.owasp.encoder.Encode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.StringJoiner;

/**
 * Struts2 action for handling prescription print preview operations.
 * Migrated from Struts1 DispatchAction pattern.
 *
 * @since 2025-01-23
 */
public class RxPrintPreview2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private final ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
    private final OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
    private final SiteDao siteDao = SpringUtils.getBean(SiteDao.class);
    private final FaxManager faxManager = SpringUtils.getBean(FaxManager.class);
    private static final boolean isMultiSitesEnabled = ca.openosp.openo.commn.IsPropertiesOn.isMultisitesEnable();

    @Override
    public String execute() throws Exception {
        return printPreview();
    }

    /**
     * Main method for handling print preview action.
     * Prepares all necessary data for the prescription print preview.
     *
     * @return "success" to forward to the JSP page that will display the preview
     */
    public String printPreview() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        request.setAttribute("loggedInInfo", loggedInInfo);
        HttpSession session = request.getSession();

        RxSessionBean sessionBean = setupProviderInfo(request, session);

        setupPageSizes(request);

        List<String> addressName = new ArrayList<>();
        List<String> address = new ArrayList<>();
        setupClinicAddresses(request, session, sessionBean, addressName, address);

        setupComment(request);

        PharmacyInfo pharmacy = setupPharmacyInfo(request);

        setupDigitalSignature(request, loggedInInfo);

        setupFaxConfigs(request, loggedInInfo);

        setupDisplayFlags(request, sessionBean);

        setupAdditionalAttributes(request, loggedInInfo, pharmacy);

        // Setup additional data from PreviewContent.jsp
        Date rxDate = setupRxDate(sessionBean, request);
        request.setAttribute("rxDate", rxDate);

        setupPatientInfo(loggedInInfo, sessionBean, request);

        setupDoctorInfo(sessionBean, request);

        setupFirstNationsInfo(loggedInInfo, sessionBean, request);

        setupPatientDOBDisplay(sessionBean, request);

        setupPrescriptionData(sessionBean, request);

        setupClinicTitle(request);

        setupEnhancedRxInfo(request, sessionBean);

        setupQrCodeEnabled(request);

        setupFormsPromoText(request);

        return SUCCESS;
    }

    /**
     * Sets up the prescription date.
     *
     * @param sessionBean The RxSessionBean
     * @param request     The HTTP request
     * @return The prescription date
     */
    private Date setupRxDate(RxSessionBean sessionBean, HttpServletRequest request) {
        Date rxDate = RxUtil.Today();
        String rePrint = (String) request.getSession().getAttribute("rePrint");

        if (rePrint != null && rePrint.equalsIgnoreCase("true")) {
            rxDate = sessionBean.getStashItem(0).getRxDate();
        } else {
            // Set Date to latest in stash
            Date tmp;
            for (int idx = 0; idx < sessionBean.getStashSize(); ++idx) {
                tmp = sessionBean.getStashItem(idx).getRxDate();
                if (tmp.after(rxDate)) {
                    rxDate = tmp;
                }
            }
        }

        request.setAttribute("rxDateFormatted", RxUtil.DateToString(rxDate, "MMMM d, yyyy", request.getLocale()));
        return rxDate;
    }

    /**
     * Sets up patient information.
     *
     * @param loggedInInfo The LoggedInInfo object
     * @param sessionBean  The RxSessionBean
     * @param request      The HTTP request
     */
    private void setupPatientInfo(LoggedInInfo loggedInInfo, RxSessionBean sessionBean, HttpServletRequest request) {
        RxPatientData.Patient patient = RxPatientData.getPatient(loggedInInfo, sessionBean.getDemographicNo());
        String patientAddress = patient.getAddress() == null ? "" : patient.getAddress();
        String patientCity = patient.getCity() == null ? "" : patient.getCity();
        String patientProvince = patient.getProvince() == null ? "" : patient.getProvince();
        String patientPostal = patient.getPostal() == null ? "" : patient.getPostal();
        String patientPhone = patient.getPhone() == null ? "" : patient.getPhone();
        String patientHin = patient.getHin() == null ? "" : patient.getHin();

        // Format patient city and postal code
        int check = (!patientCity.trim().isEmpty() ? 1 : 0) | (!patientProvince.trim().isEmpty() ? 2 : 0);
        String patientCityPostal = String.format("%s%s%s %s", patientCity, check == 3 ? ", " : check == 2 ? "" : " ", patientProvince, patientPostal);

        // Get patient chart number if enabled
        String ptChartNo = "";
        if (OscarProperties.getInstance().getProperty("showRxChartNo", "").equalsIgnoreCase("true")) {
            ptChartNo = patient.getChartNo() == null ? "" : patient.getChartNo();
        }

        request.setAttribute("patient", patient);
        request.setAttribute("patientAddress", patientAddress);
        request.setAttribute("patientCity", patientCity);
        request.setAttribute("patientProvince", patientProvince);
        request.setAttribute("patientPostal", patientPostal);
        request.setAttribute("patientCityPostal", patientCityPostal);
        request.setAttribute("patientPhone", patientPhone);
        request.setAttribute("patientHin", patientHin);
        request.setAttribute("patientChartNo", ptChartNo);
        request.setAttribute("demographicNo", sessionBean.getDemographicNo());
    }

    /**
     * Sets up doctor information.
     *
     * @param sessionBean The RxSessionBean
     * @param request     The HTTP request
     */
    private void setupDoctorInfo(RxSessionBean sessionBean, HttpServletRequest request) {
        String signingProvider = sessionBean.getProviderNo();
        String rePrint = (String) request.getSession().getAttribute("rePrint");

        if (rePrint != null && rePrint.equalsIgnoreCase("true")) {
            signingProvider = sessionBean.getStashItem(0).getProviderNo();
        }

        ca.openosp.openo.prescript.data.RxProviderData.Provider provider = new ca.openosp.openo.prescript.data.RxProviderData().getProvider(signingProvider);

        ProSignatureData sig = new ProSignatureData();
        boolean hasSig = sig.hasSignature(signingProvider);
        String doctorName = "";
        if (hasSig) {
            doctorName = sig.getSignature(signingProvider);
        } else {
            doctorName = (provider.getFirstName() + ' ' + provider.getSurname());
        }

        String pracNo = provider.getPractitionerNo();

        request.setAttribute("signingProvider", signingProvider);
        request.setAttribute("provider", provider);
        request.setAttribute("doctorName", doctorName);
        request.setAttribute("pracNo", pracNo);
    }

    /**
     * Sets up First Nations module information.
     *
     * @param loggedInInfo The LoggedInInfo object
     * @param sessionBean  The RxSessionBean
     * @param request      The HTTP request
     */
    private void setupFirstNationsInfo(LoggedInInfo loggedInInfo, RxSessionBean sessionBean, HttpServletRequest request) {
        if ("true".equalsIgnoreCase(OscarProperties.getInstance().getProperty("FIRST_NATIONS_MODULE"))) {
            DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);

            DemographicExt demographicExtStatusNum = demographicManager.getDemographicExt(loggedInInfo, sessionBean.getDemographicNo(), "statusNum");
            String bandNumber = "";

            if (demographicExtStatusNum != null) {
                bandNumber = demographicExtStatusNum.getValue();
            }

            if (bandNumber == null) {
                bandNumber = "";
            }

            // if band number is empty try the alternate composite.
            if (bandNumber.isEmpty()) {
                DemographicExt demographicExtBandName = demographicManager.getDemographicExt(loggedInInfo, sessionBean.getDemographicNo(), "fNationCom");
                DemographicExt demographicExtBandFamily = demographicManager.getDemographicExt(loggedInInfo, sessionBean.getDemographicNo(), "fNationFamilyNumber");
                DemographicExt demographicExtBandFamilyPosition = demographicManager.getDemographicExt(loggedInInfo, sessionBean.getDemographicNo(), "fNationFamilyPosition");

                String bandName = demographicExtBandName != null ? demographicExtBandName.getValue() : "";
                String bandFamily = demographicExtBandFamily != null ? demographicExtBandFamily.getValue() : "";
                String bandFamilyPosition = demographicExtBandFamilyPosition != null ? demographicExtBandFamilyPosition.getValue() : "";

                StringBuilder bandNumberString = new StringBuilder();

                if (!bandName.isEmpty()) {
                    bandNumberString.append(bandName);
                }

                if (!bandFamily.isEmpty()) {
                    bandNumberString.append("-").append(bandFamily);
                }

                if (!bandFamilyPosition.isEmpty()) {
                    bandNumberString.append("-").append(bandFamilyPosition);
                }

                bandNumber = bandNumberString.toString();
            }

            request.setAttribute("bandNumber", bandNumber);
        }
    }

    /**
     * Sets up patient DOB display preferences.
     *
     * @param sessionBean The RxSessionBean
     * @param request     The HTTP request
     */
    private void setupPatientDOBDisplay(RxSessionBean sessionBean, HttpServletRequest request) {
        RxPatientData.Patient patient = (RxPatientData.Patient) request.getAttribute("patient");
        String patientDOBStr = RxUtil.DateToString(patient.getDOB(), "MMM d, yyyy");
        boolean showPatientDOB = false;

        // Check if user prefers to show DOB in print
        UserPropertyDAO userPropertyDAO = SpringUtils.getBean(UserPropertyDAO.class);
        UserProperty prop = userPropertyDAO.getProp(sessionBean.getProviderNo(), UserProperty.RX_SHOW_PATIENT_DOB);
        if (prop != null && prop.getValue().equalsIgnoreCase("yes")) {
            showPatientDOB = true;
        }

        request.setAttribute("patientDOBStr", patientDOBStr);
        request.setAttribute("showPatientDOB", showPatientDOB);
    }

    /**
     * Sets up prescription data.
     *
     * @param sessionBean The RxSessionBean
     * @param request     The HTTP request
     */
    private void setupPrescriptionData(RxSessionBean sessionBean, HttpServletRequest request) {
        StringJoiner strRx = new StringJoiner(";;");
        StringJoiner strRxNoNewLines = new StringJoiner("\n");

        String[] rxFullOutLines = new String[sessionBean.getStashSize()];

        for (int i = 0; i < sessionBean.getStashSize(); i++) {
            RxPrescriptionData.Prescription rx = sessionBean.getStashItem(i);
            String fullOutLine = rx.getFullOutLine();
            strRx.add(fullOutLine);
            strRxNoNewLines.add(fullOutLine.replaceAll(";", " ")
                    .replaceAll("\\\\n", "\n"));
            rxFullOutLines[i] = fullOutLine.replaceAll(";", "<br>")
                    .replaceAll("\\\\n", "<br>");
        }

        request.setAttribute("strRx", strRx.toString().replaceAll("\\\\n", "<br>"));
        request.setAttribute("strRxNoNewLines", strRxNoNewLines.toString());
        request.setAttribute("rxFullOutLines", rxFullOutLines);
    }

    /**
     * Sets up provider information and retrieves the session bean.
     *
     * @param request The HTTP request
     * @param session The HTTP session
     * @return The RxSessionBean for the current session
     */
    private RxSessionBean setupProviderInfo(HttpServletRequest request, HttpSession session) {
        RxSessionBean sessionBean = (RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        Provider provider = this.providerManager.getProvider(sessionBean.getProviderNo());
        String providerFax = provider.getWorkPhone();
        if (providerFax == null) {
            providerFax = "";
        }
        providerFax = providerFax.replaceAll("[^0-9]", "");
        request.setAttribute("providerFax", providerFax);

        String reprint = request.getSession().getAttribute("rePrint") != null ? (String) request.getSession().getAttribute("rePrint") : "false";
        if (reprint.equalsIgnoreCase("true")) {
            sessionBean = (RxSessionBean) session.getAttribute("tmpBeanRX");
        } else {
            session.setAttribute("tmpBeanRX", sessionBean);
        }
        request.setAttribute("sessionBean", sessionBean);
        request.setAttribute("reprint", reprint);

        request.setAttribute("providerName", ca.openosp.openo.providers.data.ProviderData.getProviderName(sessionBean.getProviderNo()));
        request.setAttribute("providerNo", sessionBean.getProviderNo());

        return sessionBean;
    }

    /**
     * Sets up page size options for the prescription.
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
     * Sets up clinic addresses based on multi-site configuration.
     *
     * @param request     The HTTP request
     * @param session     The HTTP session
     * @param sessionBean The RxSessionBean
     * @param addressName List to store address names
     * @param address     List to store formatted addresses
     */
    private void setupClinicAddresses(HttpServletRequest request, HttpSession session, RxSessionBean sessionBean, List<String> addressName, List<String> address) {
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
     * Sets up addresses for multi-site configuration.
     *
     * @param session     The HTTP session
     * @param sessionBean The RxSessionBean
     * @param addressName List to store address names
     * @param address     List to store formatted addresses
     */
    private void setupMultiSiteAddresses(HttpSession session, RxSessionBean sessionBean, List<String> addressName, List<String> address) {
        String appt_no = (String) session.getAttribute("cur_appointment_no");
        String location = null;
        if (appt_no != null) {
            Appointment result = appointmentDao.find(Integer.parseInt(appt_no));
            if (result != null) location = result.getLocation();
        }

        String doctorName = getDoctorName(sessionBean);

        ResourceBundle rb = ResourceBundle.getBundle("oscarResources", Locale.getDefault());
        List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));

        for (int i = 0; i < sites.size(); i++) {
            Site s = sites.get(i);
            addressName.add(s.getName());
            address.add("<b>" + doctorName + "</b><br>" + s.getName() + "<br>" + s.getAddress() + "<br>" + s.getCity() + ", " + s.getProvince() + " " + s.getPostal() + "<br>" + rb.getString("RxPreview.msgTel") + ": " + s.getPhone() + "<br>" + rb.getString("RxPreview.msgFax") + ": " + s.getFax());
            if (s.getName().equals(location)) session.setAttribute("RX_ADDR", String.valueOf(i));
        }
    }

    /**
     * Sets up addresses for satellite clinics.
     *
     * @param sessionBean The RxSessionBean
     * @param props       OscarProperties instance
     * @param addressName List to store address names
     * @param address     List to store formatted addresses
     * @param request     The HTTP request
     */
    private void setupSatelliteAddresses(RxSessionBean sessionBean, OscarProperties props, List<String> addressName, List<String> address, HttpServletRequest request) {
        String doctorName = getDoctorName(sessionBean);

        String[] temp0 = props.getProperty("clinicSatelliteName", "").split("\\|");
        String[] temp1 = props.getProperty("clinicSatelliteAddress", "").split("\\|");
        String[] temp2 = props.getProperty("clinicSatelliteCity", "").split("\\|");
        String[] temp3 = props.getProperty("clinicSatelliteProvince", "").split("\\|");
        String[] temp4 = props.getProperty("clinicSatellitePostal", "").split("\\|");
        String[] temp5 = props.getProperty("clinicSatellitePhone", "").split("\\|");
        String[] temp6 = props.getProperty("clinicSatelliteFax", "").split("\\|");
        ResourceBundle rb = ResourceBundle.getBundle("oscarResources", request.getLocale());

        for (int i = 0; i < temp0.length; i++) {
            addressName.add(temp0[i]);
            address.add("<b>" + Encode.forHtml(doctorName) + "</b><br>" + Encode.forHtml(temp0[i]) + "<br>" + Encode.forHtml(temp1[i]) + "<br>" + temp2[i] + ", " + temp3[i] + " " + temp4[i] + "<br>" + rb.getString("RxPreview.msgTel") + ": " + temp5[i] + "<br>" + rb.getString("RxPreview.msgFax") + ": " + temp6[i]);
        }
    }

    /**
     * Gets the doctor's name with proper formatting.
     *
     * @param sessionBean The RxSessionBean
     * @return Formatted doctor name
     */
    private String getDoctorName(RxSessionBean sessionBean) {
        ca.openosp.openo.prescript.data.RxProviderData.Provider rxprovider = new ca.openosp.openo.prescript.data.RxProviderData().getProvider(sessionBean.getProviderNo());
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
     * Sets up comment from session.
     *
     * @param request The HTTP request
     */
    private void setupComment(HttpServletRequest request) {
        String comment = request.getSession().getAttribute("comment") != null ? request.getSession().getAttribute("comment").toString() : "";
        request.getSession().removeAttribute("comment");
        request.setAttribute("comment", Encode.forJavaScript(comment.replaceAll("\n", "<br>")));
    }

    /**
     * Sets up pharmacy information.
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
     * Sets up pharmacy address.
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

        request.setAttribute("pharmacyAddress", addressJoiner.toString());
    }

    /**
     * Sets up digital signature.
     *
     * @param request      The HTTP request
     * @param loggedInInfo The LoggedInInfo object
     */
    private void setupDigitalSignature(HttpServletRequest request, LoggedInInfo loggedInInfo) {
        String signatureRequestId = DigitalSignatureUtils.generateSignatureRequestId(loggedInInfo.getLoggedInProviderNo());
        String imageUrl = request.getContextPath() + "/imageRenderingServlet?source=" + ImageRenderingServlet.Source.signature_preview.name() + "&" + DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY + "=" + signatureRequestId;

        request.setAttribute("signatureRequestId", signatureRequestId);
        request.setAttribute("imageUrl", imageUrl);
        request.setAttribute("tempPath", System.getProperty("java.io.tmpdir").replaceAll("\\\\", "/") + "/signature_" + signatureRequestId + ".jpg");

        // Set start image URL for digital signature
        String startimageUrl = request.getContextPath() + "/images/1x1.gif";

        // Check if there's a digital signature ID in the stash
        HttpSession session = request.getSession();
        RxSessionBean xbean = (RxSessionBean) session.getAttribute("tmpBeanRX");
        if (xbean != null && xbean.getStashSize() > 0 && Objects.nonNull(xbean.getStashItem(0).getDigitalSignatureId())) {
            startimageUrl = request.getContextPath() + "/imageRenderingServlet?source=" + ImageRenderingServlet.Source.signature_stored.name() + "&digitalSignatureId=" + xbean.getStashItem(0).getDigitalSignatureId();
        }

        request.setAttribute("startimageUrl", startimageUrl);
        request.setAttribute("tmpBeanRX", xbean);
    }

    /**
     * Sets up fax configurations.
     *
     * @param request      The HTTP request
     * @param loggedInInfo The LoggedInInfo object
     */
    private void setupFaxConfigs(HttpServletRequest request, LoggedInInfo loggedInInfo) {
        List<FaxConfig> faxConfigs = faxManager.getFaxGatewayAccounts(loggedInInfo);
        request.setAttribute("faxConfigs", faxConfigs);
    }

    /**
     * Sets up various UI display flags.
     *
     * @param request     The HTTP request
     * @param sessionBean The RxSessionBean
     */
    private void setupDisplayFlags(HttpServletRequest request, RxSessionBean sessionBean) {
        boolean isRxFaxEnabled = OscarProperties.getInstance().isRxFaxEnabled();
        request.setAttribute("showRxFaxBlock", isRxFaxEnabled);

        if (isRxFaxEnabled) {
            boolean isFaxButtonsDisabled = sessionBean.getStashSize() == 0 || Objects.isNull(sessionBean.getStashItem(0).getDigitalSignatureId());
            request.setAttribute("isFaxDisabled", isFaxButtonsDisabled);
        }

        boolean rxEnabled = OscarProperties.getInstance().isRxSignatureEnabled();
        boolean disableTablet = !OscarProperties.getInstance().getBooleanProperty("signature_tablet", "yes");
        request.setAttribute("showSignatureBlock", rxEnabled && disableTablet);

        request.setAttribute("rxPasteAsterisk", OscarProperties.getInstance().isPropertyActive("rx_paste_asterisk"));
    }

    /**
     * Sets up additional attributes.
     *
     * @param request      The HTTP request
     * @param loggedInInfo The LoggedInInfo object
     * @param pharmacy     The PharmacyInfo object
     */
    private void setupAdditionalAttributes(HttpServletRequest request, LoggedInInfo loggedInInfo, PharmacyInfo pharmacy) {
        request.setAttribute("prescribedBy", Encode.forJavaScript(loggedInInfo.getLoggedInProvider().getFormattedName()));

        String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(Calendar.getInstance().getTime());
        request.setAttribute("timeStamp", timeStamp);

        request.setAttribute("pharmacyName", Encode.forJavaScript(pharmacy != null ? pharmacy.getName() : ""));
        request.setAttribute("pharmacyFax", pharmacy != null ? pharmacy.getFax() : "");
    }

    /**
     * Gets the selected address based on the address index.
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

    /**
     * Sets up clinic title for display.
     *
     * @param request The HTTP request
     */
    private void setupClinicTitle(HttpServletRequest request) {
        ca.openosp.openo.prescript.data.RxProviderData.Provider provider = (ca.openosp.openo.prescript.data.RxProviderData.Provider) request.getAttribute("provider");
        String clinicTitle = provider.getClinicName().replaceAll("\\(\\d{6}\\)", "") + "<br>";
        clinicTitle += provider.getClinicAddress() + "<br>";
        clinicTitle += provider.getClinicCity() + "   " + provider.getClinicPostal();

        request.setAttribute("clinicTitle", clinicTitle);

        // Set clinic phone for use in JSP
        String finalPhone = provider.getClinicPhone();
        request.setAttribute("phone", finalPhone);

        // Handle infirmary view program phone
        HttpSession session = request.getSession();
        if (session.getAttribute("infirmaryView_programTel") != null) {
            String infirmaryPhone = (String) session.getAttribute("infirmaryView_programTel");
            request.setAttribute("infirmaryPhone", infirmaryPhone);
        }
    }

    /**
     * Sets up enhanced RX information if enabled.
     *
     * @param request     The HTTP request
     * @param sessionBean The RxSessionBean
     */
    private void setupEnhancedRxInfo(HttpServletRequest request, RxSessionBean sessionBean) {
        String rx_enhance = OscarProperties.getInstance().getProperty("rx_enhance");

        if (rx_enhance != null && rx_enhance.equals("true")) {
            RxPatientData.Patient patient = (RxPatientData.Patient) request.getAttribute("patient");
            ca.openosp.openo.prescript.data.RxProviderData.Provider provider = (ca.openosp.openo.prescript.data.RxProviderData.Provider) request.getAttribute("provider");

            // Format patient DOB
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String patientDOB = patient.getDOB() == null ? "" : formatter.format(patient.getDOB());

            // Format doctor info
            String docInfo = request.getAttribute("doctorName") + "\n" + provider.getClinicName().replaceAll("\\(\\d{6}\\)", "") + "RxPreview.PractNo" + request.getAttribute("pracNo") + "\n" + provider.getClinicAddress() + "\n" + provider.getClinicCity() + "   " + provider.getClinicPostal() + "\n" + "RxPreview.msgTel" + ": " + provider.getClinicPhone() + "\n" + "RxPreview.msgFax" + ": " + provider.getClinicFax();

            // Format patient info
            String patientInfo = patient.getFirstName() + " " + patient.getSurname() + "\n" + request.getAttribute("patientAddress") + "\n" + request.getAttribute("patientCity") + "   " + request.getAttribute("patientPostal") + "\n" + "RxPreview.msgTel" + ": " + request.getAttribute("patientPhone") + (patientDOB != null && !patientDOB.trim().isEmpty() ? "\n" + "RxPreview.msgDOB" + ": " + patientDOB : "") + (!((String) request.getAttribute("patientHin")).trim().isEmpty() ? "\n" + "oscar.oscarRx.hin" + ": " + request.getAttribute("patientHin") : "");

            request.setAttribute("enhancedDocInfo", docInfo);
            request.setAttribute("enhancedPatientInfo", patientInfo);
            request.setAttribute("patientDOBFormatted", patientDOB);
        }

        request.setAttribute("rx_enhance", rx_enhance);
    }

    /**
     * Sets up QR code enabled flag.
     *
     * @param request The HTTP request
     */
    private void setupQrCodeEnabled(HttpServletRequest request) {
        String signingProvider = (String) request.getAttribute("signingProvider");
        boolean qrCodeEnabled = PrescriptionQrCodeUIBean.isPrescriptionQrCodeEnabledForProvider(signingProvider);
        request.setAttribute("qrCodeEnabled", qrCodeEnabled);
    }

    /**
     * Sets up forms promo text if available.
     *
     * @param request The HTTP request
     */
    private void setupFormsPromoText(HttpServletRequest request) {
        String formsPromoText = OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT");
        if (formsPromoText != null && !formsPromoText.isEmpty()) {
            request.setAttribute("formsPromoText", formsPromoText);
        }

        // Set RX_FOOTER if available
        String rxFooter = OscarProperties.getInstance().getProperty("RX_FOOTER");
        if (rxFooter != null) {
            request.setAttribute("rxFooter", rxFooter);
        }
    }
}
