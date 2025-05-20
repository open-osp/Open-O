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
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.PharmacyInfo;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Site;
import org.oscarehr.util.SpringUtils;
import org.owasp.encoder.Encode;
import org.springframework.web.context.support.WebApplicationContextUtils;
import oscar.OscarProperties;
import oscar.oscarProvider.data.ProSignatureData;
import oscar.oscarRx.data.RxPharmacyData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class RxPrintPreviewAction extends DispatchAction {

    private final ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
    private final OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
    private final SiteDao siteDao = SpringUtils.getBean(SiteDao.class);
    private static final boolean isMultiSitesEnabled = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();

    public ActionForward printPreview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        oscar.oscarRx.pageUtil.RxSessionBean sessionBean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        Provider provider = this.providerManager.getProvider(sessionBean.getProviderNo());
        String providerFax = provider.getWorkPhone();
        if (providerFax == null) {
            providerFax = "";
        }
        providerFax = providerFax.replaceAll("[^0-9]", "");

        request.setAttribute("providerFax", providerFax);

        List<KeyValuePair> pageSizes = new ArrayList<>();
        pageSizes.add(new KeyValuePair("A4 page", "PageSize.A4"));
        pageSizes.add(new KeyValuePair("A6 page", "PageSize.A6"));
        pageSizes.add(new KeyValuePair("Letter page", "PageSize.Letter"));
        request.setAttribute("pageSizes", pageSizes);

        String reprint = request.getSession().getAttribute("rePrint") != null ? (String) request.getSession().getAttribute("rePrint") : "false";

        if (reprint.equalsIgnoreCase("true")) {
            sessionBean = (oscar.oscarRx.pageUtil.RxSessionBean) session.getAttribute("tmpBeanRX");
        }

        // for satellite clinics
        List<String> addressName = null;
        List<String> address = null;
        OscarProperties props = OscarProperties.getInstance();
        if (isMultiSitesEnabled) {
            String appt_no = (String) session.getAttribute("cur_appointment_no");
            String location = null;
            if (appt_no != null) {
                Appointment result = appointmentDao.find(Integer.parseInt(appt_no));
                if (result != null) location = result.getLocation();
            }

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

            addressName = new ArrayList<>();
            address = new ArrayList<>();

            java.util.ResourceBundle rb = java.util.ResourceBundle.getBundle("oscarResources", request.getLocale());

            List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));

            for (int i = 0; i < sites.size(); i++) {
                Site s = sites.get(i);
                addressName.add(s.getName());
                address.add("<b>" + doctorName + "</b><br>" + s.getName() + "<br>" + s.getAddress() + "<br>" + s.getCity() + ", " + s.getProvince() + " " + s.getPostal() + "<br>" + rb.getString("RxPreview.msgTel") + ": " + s.getPhone() + "<br>" + rb.getString("RxPreview.msgFax") + ": " + s.getFax());
                if (s.getName().equals(location))
                    session.setAttribute("RX_ADDR", String.valueOf(i));
            }


        } else if (props.getProperty("clinicSatelliteName") != null) {
            oscar.oscarRx.data.RxProviderData.Provider rxprovider = new oscar.oscarRx.data.RxProviderData().getProvider(sessionBean.getProviderNo());
            ProSignatureData sig = new ProSignatureData();
            boolean hasSig = sig.hasSignature(sessionBean.getProviderNo());
            String doctorName = "";
            if (hasSig) {
                doctorName = sig.getSignature(sessionBean.getProviderNo());
            } else {
                doctorName = (rxprovider.getFirstName() + ' ' + rxprovider.getSurname());
            }

            addressName = new ArrayList<>();
            address = new ArrayList<>();
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
                address.add("<b>" + Encode.forHtml(doctorName) + "</b><br>" + Encode.forHtml(temp0[i]) + "<br>" + Encode.forHtml(temp1[i]) + "<br>" + temp2[i] + ", " + temp3[i] + " " + temp4[i] + "<br>" + rb.getString("RxPreview.msgTel") + ": " + temp5[i] + "<br>" + rb.getString("RxPreview.msgFax") + ": " + temp6[i]);
            }
        }

        request.setAttribute("reprint", reprint);
        request.setAttribute("addressName", addressName);
        request.setAttribute("address", address);


        String comment = request.getSession().getAttribute("comment") != null ? request.getSession().getAttribute("comment").toString() : "";
        request.getSession().removeAttribute("comment");
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
            }
        }

        request.setAttribute("pharmacy", pharmacy);
        request.setAttribute("prefPharmacy", prefPharmacy);
        request.setAttribute("prefPharmacyId", prefPharmacyId);
        request.setAttribute("comment", comment);

        return new ActionForward(mapping.findForward("success"));
    }

}
