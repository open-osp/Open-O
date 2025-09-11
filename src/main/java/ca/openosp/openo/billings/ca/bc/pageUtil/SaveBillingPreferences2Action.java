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

package ca.openosp.openo.billings.ca.bc.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import ca.openosp.openo.commn.dao.PropertyDao;
import ca.openosp.openo.commn.model.Property;
import ca.openosp.openo.utility.SpringUtils;

import ca.openosp.openo.billings.ca.bc.data.BillingPreference;
import ca.openosp.openo.billings.ca.bc.data.BillingPreferencesDAO;

import java.util.List;

/**
 * Saves the values in the ActionForm into the BillingPreferences record
 *
 * @version 1.0
 */
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class SaveBillingPreferences2Action
        extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    public String execute() {
        BillingPreferencesDAO dao = SpringUtils.getBean(BillingPreferencesDAO.class);
        PropertyDao propertyDao = SpringUtils.getBean(PropertyDao.class);

        List<Property> defaultBillingFormPropertyList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.default_billing_form, this.getProviderNo());
        Property defaultBillingFormProperty = defaultBillingFormPropertyList.isEmpty() ? null : defaultBillingFormPropertyList.get(0);
        String selectedDefaultBillingForm = this.getDefaultBillingForm();
        if (defaultBillingFormProperty == null) {
            defaultBillingFormProperty = new Property();
            defaultBillingFormProperty.setValue(selectedDefaultBillingForm);
            defaultBillingFormProperty.setProviderNo(this.getProviderNo());
            defaultBillingFormProperty.setName(Property.PROPERTY_KEY.default_billing_form.name());
            propertyDao.persist(defaultBillingFormProperty);
        } else {
            defaultBillingFormProperty.setValue(selectedDefaultBillingForm);
            propertyDao.merge(defaultBillingFormProperty);
        }

        // Default Billing Provider
        String selectedDefaultBillingProvider = this.getDefaultBillingProvider();
        if (StringUtils.isNotEmpty(selectedDefaultBillingProvider)) {
            List<Property> defaultBillingProviderPropertyList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.default_billing_provider, this.getProviderNo());
            Property defaultBillingProviderProperty = defaultBillingProviderPropertyList.isEmpty() ? null : defaultBillingProviderPropertyList.get(0);
            if (defaultBillingProviderProperty == null) {
                defaultBillingProviderProperty = new Property();
                defaultBillingProviderProperty.setProviderNo(this.getProviderNo());
                defaultBillingProviderProperty.setValue(selectedDefaultBillingProvider);
                defaultBillingProviderProperty.setName(Property.PROPERTY_KEY.default_billing_provider.name());
                propertyDao.persist(defaultBillingProviderProperty);
            } else {
                defaultBillingProviderProperty.setValue(selectedDefaultBillingProvider);
                propertyDao.merge(defaultBillingProviderProperty);
            }
        }

        // Default Service Location
        String selectedDefaultServiceLocation = this.getDefaultServiceLocation();
        if (StringUtils.isNotEmpty(selectedDefaultServiceLocation)) {
            List<Property> defaultServiceLocationPropertyList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.bc_default_service_location, this.getProviderNo());
            Property defaultServiceLocationProperty = defaultServiceLocationPropertyList.isEmpty() ? null : defaultServiceLocationPropertyList.get(0);
            if (defaultServiceLocationProperty == null) {
                defaultServiceLocationProperty = new Property();
                defaultServiceLocationProperty.setProviderNo(this.getProviderNo());
                defaultServiceLocationProperty.setValue(selectedDefaultServiceLocation);
                defaultServiceLocationProperty.setName(Property.PROPERTY_KEY.bc_default_service_location.name());
                propertyDao.persist(defaultServiceLocationProperty);
            } else {
                defaultServiceLocationProperty.setValue(selectedDefaultServiceLocation);
                propertyDao.merge(defaultServiceLocationProperty);
            }
        }

        List<Property> propList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.invoice_payee_info, this.getProviderNo());
        Property invoicePayeeInfo = propList.isEmpty() ? null : propList.get(0);
        propList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.invoice_payee_display_clinic, this.getProviderNo());
        Property invoiceDisplayClinicInfo = propList.isEmpty() ? null : propList.get(0);


        propList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.auto_populate_refer, this.getProviderNo());
        Property autoPopulateRefer = propList.isEmpty() ? null : propList.get(0);
        if (autoPopulateRefer == null) {
            autoPopulateRefer = new Property();
            autoPopulateRefer.setValue(Boolean.toString(this.isAutoPopulateRefer()));
            autoPopulateRefer.setProviderNo(this.getProviderNo());
            autoPopulateRefer.setName(Property.PROPERTY_KEY.auto_populate_refer.name());
            propertyDao.persist(autoPopulateRefer);
        } else {
            autoPopulateRefer.setValue(Boolean.toString(this.isAutoPopulateRefer()));
            propertyDao.merge(autoPopulateRefer);
        }

        if (invoicePayeeInfo == null) {
            invoicePayeeInfo = new Property();
            invoicePayeeInfo.setName(Property.PROPERTY_KEY.invoice_payee_info.name());
            invoicePayeeInfo.setProviderNo(this.getProviderNo());
            invoicePayeeInfo.setValue(this.getInvoicePayeeInfo());
            propertyDao.persist(invoicePayeeInfo);
        } else {
            invoicePayeeInfo.setValue(this.getInvoicePayeeInfo());
            propertyDao.merge(invoicePayeeInfo);
        }

        if (invoiceDisplayClinicInfo == null) {
            invoiceDisplayClinicInfo = new Property();
            invoiceDisplayClinicInfo.setName(Property.PROPERTY_KEY.invoice_payee_display_clinic.name());
            invoiceDisplayClinicInfo.setProviderNo(this.getProviderNo());
            invoiceDisplayClinicInfo.setValue("" + this.isInvoicePayeeDisplayClinicInfo());
            propertyDao.persist(invoiceDisplayClinicInfo);
        } else {
            invoiceDisplayClinicInfo.setValue("" + this.isInvoicePayeeDisplayClinicInfo());
            propertyDao.merge(invoiceDisplayClinicInfo);
        }

        BillingPreference pref = dao.getUserBillingPreference(this.getProviderNo());
        if (pref == null) {
            pref = new BillingPreference();
            pref.setProviderNo(this.getProviderNo());
            pref.setReferral(Integer.parseInt(this.getReferral()));
            pref.setDefaultPayeeNo(this.getPayeeProviderNo());
            dao.persist(pref);
        } else {
            pref.setReferral(Integer.parseInt(this.getReferral()));
            pref.setDefaultPayeeNo(this.getPayeeProviderNo());
            dao.merge(pref);
        }
        request.setAttribute("providerNo", this.getProviderNo());
        return SUCCESS;
    }

    private String providerNo;
    private String referral;
    private String payeeProviderNo;
    private String gstNo;
    private boolean useClinicGstNo;
    private boolean autoPopulateRefer;

    //What to display for payee info when this providers gets referred to as a payee on an invoice
    private String invoicePayeeInfo;
    private boolean invoicePayeeDisplayClinicInfo;

    //Default billing form preference
    private String defaultBillingForm;
    private String formCode;
    private String description;

    //Default billing provider preference
    private String defaultBillingProvider;

    //Default Teleplan service location (visittype)
    private String defaultServiceLocation;

    public String getProviderNo() {

        return providerNo;
    }

    public void setProviderNo(String providerNo) {

        this.providerNo = providerNo;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public void setPayeeProviderNo(String payeeProviderNo) {
        this.payeeProviderNo = payeeProviderNo;
    }

    public String getReferral() {
        return referral;
    }

    public String getPayeeProviderNo() {
        return payeeProviderNo;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public boolean isUseClinicGstNo() {
        return useClinicGstNo;
    }

    public void setUseClinicGstNo(boolean useClinicGstNo) {
        this.useClinicGstNo = useClinicGstNo;
    }

    public String getInvoicePayeeInfo() {
        return invoicePayeeInfo;
    }

    public void setInvoicePayeeInfo(String invoicePayeeInfo) {
        this.invoicePayeeInfo = invoicePayeeInfo;
    }

    public boolean isInvoicePayeeDisplayClinicInfo() {
        return invoicePayeeDisplayClinicInfo;
    }

    public void setInvoicePayeeDisplayClinicInfo(boolean invoicePayeeDisplayClinicInfo) {
        this.invoicePayeeDisplayClinicInfo = invoicePayeeDisplayClinicInfo;
    }

    public String getDefaultBillingForm() {
        return defaultBillingForm;
    }

    public void setDefaultBillingForm(String defaultBillingForm) {
        this.defaultBillingForm = defaultBillingForm;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultBillingProvider() {
        return defaultBillingProvider;
    }

    public void setDefaultBillingProvider(String defaultBillingProvider) {
        this.defaultBillingProvider = defaultBillingProvider;
    }

    public String getDefaultServiceLocation() {
        return defaultServiceLocation;
    }

    public void setDefaultServiceLocation(String defaultServiceLocation) {
        this.defaultServiceLocation = defaultServiceLocation;
    }

    public boolean isAutoPopulateRefer() {
        return autoPopulateRefer;
    }

    public void setAutoPopulateRefer(boolean autoPopulateRefer) {
        this.autoPopulateRefer = autoPopulateRefer;
    }
}
