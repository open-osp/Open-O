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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class BillingPreferencesActionForm
    extends ActionForm {
  private String providerNo;
  private String referral;
  private String payeeProviderNo;
  private String gstNo;
  private boolean useClinicGstNo;
  private boolean autoPopulateRefer;

  //What to display for payee info when this provider gets referred to as a payee on an invoice
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

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
      /** @todo: finish this method, this is just the skeleton.*/
    return null;
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
