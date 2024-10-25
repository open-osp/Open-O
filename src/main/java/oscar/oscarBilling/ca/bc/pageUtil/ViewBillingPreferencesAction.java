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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import oscar.OscarProperties;
import oscar.oscarBilling.ca.bc.data.BillingFormData;
import oscar.oscarBilling.ca.bc.data.BillingPreference;
import oscar.oscarBilling.ca.bc.data.BillingPreferencesDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Forwards flow of control to Billing Preferences Screen
 * @version 1.0
 */
public class ViewBillingPreferencesAction
    extends Action {

  private final BillingPreferencesDAO dao = SpringUtils.getBean(BillingPreferencesDAO.class);
  private final PropertyDao propertyDao = SpringUtils.getBean(PropertyDao.class);
  private final ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);

  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
    BillingPreferencesActionForm frm = (
        BillingPreferencesActionForm) actionForm;

    LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(servletRequest);
    List<Property> propList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.invoice_payee_info, frm.getProviderNo());
    Property invoicePayeeInfo = propList.isEmpty() ? null : propList.get(0);

    if(invoicePayeeInfo == null || invoicePayeeInfo.getValue() == null) {
      Provider provider = providerDao.getProvider(frm.getProviderNo());
      frm.setInvoicePayeeInfo( provider.getFirstName() + " " + provider.getLastName());
    } else {
      frm.setInvoicePayeeInfo(invoicePayeeInfo.getValue());
    }
    //Default to true when nothing is set
    frm.setInvoicePayeeDisplayClinicInfo(propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.invoice_payee_display_clinic, frm.getProviderNo()).isEmpty() || propertyDao.isActiveBooleanProperty(Property.PROPERTY_KEY.invoice_payee_display_clinic, frm.getProviderNo()));

    BillingPreference pref = dao.getUserBillingPreference(frm.getProviderNo());
    //If the user doesn't have a BillingPreference record create one
    if (pref == null) {
      pref = new BillingPreference();
      pref.setProviderNo(frm.getProviderNo());
      dao.saveUserPreferences(pref);
    }
    BillingFormData billform = new BillingFormData();
    ArrayList billingFormList = new ArrayList<>();
    oscar.oscarBilling.ca.bc.data.BillingFormData.BillingForm defaultBillingForm = billform.new BillingForm("Clinic Default", Property.PROPERTY_VALUE.clinicdefault.name());
    billingFormList.add(defaultBillingForm);
    billingFormList.addAll(Arrays.asList(billform.getFormList()));
    servletRequest.setAttribute("billingFormList", billingFormList);

    List<Property> defaultBillingFormPropertyList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.default_billing_form, frm.getProviderNo());
    frm.setDefaultBillingForm(defaultBillingFormPropertyList.isEmpty() ? null : defaultBillingFormPropertyList.get(0).getValue());

    List<Property> defaultBillingProviderPropertyList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.default_billing_provider, frm.getProviderNo());
    frm.setDefaultBillingProvider(defaultBillingProviderPropertyList.isEmpty() ? null : defaultBillingProviderPropertyList.get(0).getValue());

    frm.setReferral(String.valueOf(pref.getReferral()));
    frm.setPayeeProviderNo(String.valueOf(pref.getDefaultPayeeNo()));

    List<Provider> providerList = providerDao.getBillableProvidersInBC(loggedInInfo);
    // add a selection to trigger a "custom" option
    Provider customProvider = new Provider("CUSTOM", "", null, null, null, "Custom");
    Provider blankProvider = new Provider("NONE", "", null, null, null, "");
    if(providerList == null) {
      providerList = Arrays.asList(customProvider, blankProvider);
    } else {
      providerList.add(customProvider);
      providerList.add(blankProvider);
    }
    servletRequest.setAttribute("providerList",providerList);

    providerList = providerDao.getProvidersWithNonEmptyOhip(loggedInInfo);
    // add the clinic default selection to the select list
    Provider defaultProvider = new Provider(Property.PROPERTY_VALUE.clinicdefault.name(), "", null, null, null, "Clinic Default");
    if(providerList == null) {
      providerList = Collections.singletonList(defaultProvider);
    } else {
      providerList.add(defaultProvider);
    }
    servletRequest.setAttribute("billingProviderList",providerList);

    // Check for a per-provider property and if none set it to CLINICDEFAULT
    List<Property> defaultServiceLocationPropertyList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.bc_default_service_location, frm.getProviderNo());
    if (!defaultServiceLocationPropertyList.isEmpty()) {
        frm.setDefaultServiceLocation(defaultServiceLocationPropertyList.get(0).getValue());
    } else {
        frm.setDefaultServiceLocation(Property.PROPERTY_VALUE.clinicdefault.name());
    }

    // Prepare a formatted list of service locations
    String billRegion = OscarProperties.getInstance().getProperty("billregion", "");
    BillingFormData billingFormData = new BillingFormData();
    ArrayList<BillingFormData.BillingVisit> billingVisits = new ArrayList<>();
    billingVisits.add(new BillingFormData.BillingVisit(Property.PROPERTY_VALUE.clinicdefault.name(), "Clinic Default"));
    billingVisits.addAll(billingFormData.getVisitType(billRegion));
    servletRequest.setAttribute("serviceLocationList",billingVisits);

    // Prepare a list of default yes/no/clinic values for use on the preferences page (eg. display dx2/3)
    Map<String, String> defaultYesNoList = new HashMap<>();
    defaultYesNoList.put(Property.PROPERTY_VALUE.clinicdefault.name(), "Clinic Default");
    defaultYesNoList.put("true", "Yes");
    defaultYesNoList.put("false", "No");
    servletRequest.setAttribute("defaultYesNoList", defaultYesNoList);

    return actionMapping.findForward("success");
  }

}
