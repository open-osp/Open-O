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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.bc.data.BillingPreference;
import oscar.oscarBilling.ca.bc.data.BillingPreferencesDAO;

import java.util.List;

/**
 * Saves the values in the ActionForm into the BillingPreferences record
 * @version 1.0
 */
public class SaveBillingPreferencesAction
    extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
    BillingPreferencesActionForm frm = (
        BillingPreferencesActionForm) actionForm;
    BillingPreferencesDAO dao = SpringUtils.getBean(BillingPreferencesDAO.class);
    PropertyDao propertyDao = SpringUtils.getBean(PropertyDao.class);

    List<Property> defaultBillingFormPropertyList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.default_billing_form, frm.getProviderNo());
    Property defaultBillingFormProperty = defaultBillingFormPropertyList.isEmpty() ? null : defaultBillingFormPropertyList.get(0);
    String selectedDefaultBillingForm = frm.getDefaultBillingForm();
    if (defaultBillingFormProperty == null) {
      defaultBillingFormProperty = new Property();
      defaultBillingFormProperty.setValue(selectedDefaultBillingForm);
      defaultBillingFormProperty.setProviderNo(frm.getProviderNo());
      defaultBillingFormProperty.setName(Property.PROPERTY_KEY.default_billing_form.name());
      propertyDao.persist(defaultBillingFormProperty);
    } else {
      defaultBillingFormProperty.setValue(selectedDefaultBillingForm);
      propertyDao.merge(defaultBillingFormProperty);
    }

    // Default Billing Provider
    String selectedDefaultBillingProvider = frm.getDefaultBillingProvider();
    if (StringUtils.isNotEmpty(selectedDefaultBillingProvider)) {
      List<Property> defaultBillingProviderPropertyList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.default_billing_provider, frm.getProviderNo());
      Property defaultBillingProviderProperty = defaultBillingProviderPropertyList.isEmpty() ? null : defaultBillingProviderPropertyList.get(0);
      if (defaultBillingProviderProperty == null) {
        defaultBillingProviderProperty = new Property();
        defaultBillingProviderProperty.setProviderNo(frm.getProviderNo());
        defaultBillingProviderProperty.setValue(selectedDefaultBillingProvider);
        defaultBillingProviderProperty.setName(Property.PROPERTY_KEY.default_billing_provider.name());
        propertyDao.persist(defaultBillingProviderProperty);
      } else {
        defaultBillingProviderProperty.setValue(selectedDefaultBillingProvider);
        propertyDao.merge(defaultBillingProviderProperty);
      }
    }

    // Default Service Location
    String selectedDefaultServiceLocation = frm.getDefaultServiceLocation();
    if (StringUtils.isNotEmpty(selectedDefaultServiceLocation)) {
      List<Property> defaultServiceLocationPropertyList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.bc_default_service_location, frm.getProviderNo());
      Property defaultServiceLocationProperty = defaultServiceLocationPropertyList.isEmpty() ? null : defaultServiceLocationPropertyList.get(0);
      if (defaultServiceLocationProperty == null) {
        defaultServiceLocationProperty = new Property();
        defaultServiceLocationProperty.setProviderNo(frm.getProviderNo());
        defaultServiceLocationProperty.setValue(selectedDefaultServiceLocation);
        defaultServiceLocationProperty.setName(Property.PROPERTY_KEY.bc_default_service_location.name());
        propertyDao.persist(defaultServiceLocationProperty);
      } else {
        defaultServiceLocationProperty.setValue(selectedDefaultServiceLocation);
        propertyDao.merge(defaultServiceLocationProperty);
      }
    }

    List<Property> propList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.invoice_payee_info, frm.getProviderNo());
    Property invoicePayeeInfo = propList.isEmpty() ? null : propList.get(0);
    propList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.invoice_payee_display_clinic, frm.getProviderNo());
    Property invoiceDisplayClinicInfo = propList.isEmpty() ? null : propList.get(0);


    propList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.auto_populate_refer, frm.getProviderNo());
    Property autoPopulateRefer = propList.isEmpty() ? null : propList.get(0);
    if (autoPopulateRefer == null) {
      autoPopulateRefer = new Property();
      autoPopulateRefer.setValue(Boolean.toString(frm.isAutoPopulateRefer()));
      autoPopulateRefer.setProviderNo(frm.getProviderNo());
      autoPopulateRefer.setName(Property.PROPERTY_KEY.auto_populate_refer.name());
      propertyDao.persist(autoPopulateRefer);
    } else {
      autoPopulateRefer.setValue(Boolean.toString(frm.isAutoPopulateRefer()));
      propertyDao.merge(autoPopulateRefer);
    }

    if(invoicePayeeInfo == null){
      invoicePayeeInfo = new Property();
      invoicePayeeInfo.setName(Property.PROPERTY_KEY.invoice_payee_info.name());
      invoicePayeeInfo.setProviderNo(frm.getProviderNo());
      invoicePayeeInfo.setValue(frm.getInvoicePayeeInfo());
      propertyDao.persist(invoicePayeeInfo);
    } else {
      invoicePayeeInfo.setValue(frm.getInvoicePayeeInfo());
      propertyDao.merge(invoicePayeeInfo);
    }

    if(invoiceDisplayClinicInfo == null){
      invoiceDisplayClinicInfo = new Property();
      invoiceDisplayClinicInfo.setName(Property.PROPERTY_KEY.invoice_payee_display_clinic.name());
      invoiceDisplayClinicInfo.setProviderNo(frm.getProviderNo());
      invoiceDisplayClinicInfo.setValue("" + frm.isInvoicePayeeDisplayClinicInfo());
      propertyDao.persist(invoiceDisplayClinicInfo);
    } else {
      invoiceDisplayClinicInfo.setValue("" + frm.isInvoicePayeeDisplayClinicInfo());
      propertyDao.merge(invoiceDisplayClinicInfo);
    }

    BillingPreference pref = dao.getUserBillingPreference(frm.getProviderNo());
    if(pref == null){
      pref = new BillingPreference();
      pref.setProviderNo(frm.getProviderNo());
      pref.setReferral(Integer.parseInt(frm.getReferral()));
      pref.setDefaultPayeeNo(frm.getPayeeProviderNo());
      dao.persist(pref);
    } else {
      pref.setReferral(Integer.parseInt(frm.getReferral()));
      pref.setDefaultPayeeNo(frm.getPayeeProviderNo());
      dao.merge(pref);
    }
    servletRequest.setAttribute("providerNo",frm.getProviderNo());
    return actionMapping.findForward("success");

  }
}
