<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib prefix="oscar" uri="/oscarPropertiestag" %>
<%@page import="java.util.*,oscar.util.*"%>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="org.oscarehr.common.model.SystemPreferences" %>
<%@ page import="org.oscarehr.common.dao.PropertyDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Property" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.SystemPreferencesDao" %>
<%@ page import="oscar.oscarClinic.ClinicData" %>
<%@ page import="oscar.oscarBilling.ca.bc.data.BillingPreference" %>
<%@ page import="oscar.oscarBilling.ca.bc.data.BillingPreferencesDAO" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%
	SystemPreferencesDao systemPreferencesDao = SpringUtils.getBean(SystemPreferencesDao.class);
	PropertyDao propertyDao = SpringUtils.getBean(PropertyDao.class);
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	Provider provider = loggedInInfo.getLoggedInProvider();
	BillingPreferencesDAO billingPreferencesDAO = SpringUtils.getBean(BillingPreferencesDAO.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillingPreference billingPreference = billingPreferencesDAO.getUserBillingPreference(provider.getProviderNo());
	boolean autoPopulateRefer = propertyDao.isActiveBooleanProperty(Property.PROPERTY_KEY.auto_populate_refer, provider.getProviderNo());

%>
<!DOCTYPE HTML>
<html:html>
<head>
	<title></title>

<html:base />
	<script src="${pageContext.request.contextPath}/library/jquery/jquery-3.6.4.min.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/library/bootstrap/3.0.0/js/bootstrap.min.js" type="text/javascript"></script>
	<link href="${pageContext.request.contextPath}/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet" type="text/css"/>
	<script src="<%=request.getContextPath()%>/csrfguard" type="text/javascript"></script>


	<style>
		table {
			width:100%;
		}

	</style>
</head>
<body class="BodyStyle">
<div class="container">
<html:form action="/billing/CA/BC/saveBillingPreferencesAction.do" method="POST">
<html:hidden property="providerNo" />
	<h2>Billing Preferences</h2>
<table class="table-condensed" id="scrollNumber1">

	<tr>
		<td class="MainTableRightColumn">
			<div class="form-group">
				<label for="defaultBillingProvider">Select Default Billing Provider:</label>
				<html:select property="defaultBillingProvider" styleClass="form-control" styleId="defaultBillingProvider">
					<html:options collection="billingProviderList" property="providerNo" labelProperty="fullName" />
				</html:select>
				<div id="default-provider-alert" class="alert alert-warning" style="display:none;">Warning: all remaining billing preferences are now overridden by the preferences of this selected Default Billing Provider. Select "Clinic Default" to use your own Billing Preference Settings</div>
			</div>
		</td>
	</tr>
		<tr>
			<td class="MainTableRightColumn">
			<label for="referral"> Select Default Referral Type:</label> <html:select
				styleId="referral" styleClass="form-control" property="referral">
				<html:option value="1">Refer To</html:option>
				<html:option value="2">Refer By</html:option>
				<html:option value="3">Neither</html:option>
			</html:select>
		</tr>
		<tr>

			<%
				// Check for a global setting that overrides any per-provider setting
				boolean globalAutoPopulateRefer = false;
				List<Property> propertyList = propertyDao.findGlobalByName("auto_populate_refer");
				if (!propertyList.isEmpty()) {
					for (Property property : propertyList)
					{
						if (property.getValue().equalsIgnoreCase("true")) {
							globalAutoPopulateRefer = true;
							break;
						}
					}
				}
			%>
			<td class="MainTableRightColumn">
				<label for="autoPopulateRefer" class="checkbox-inline">
				<html:checkbox styleId="autoPopulateRefer" property="autoPopulateRefer" disabled="<%=globalAutoPopulateRefer%>"/>
				Auto-populate Referring Physician on Billing Form</label>
				<% if (globalAutoPopulateRefer) { %>
				<p class="text-warning">Note: The Auto-Populate option above cannot be changed per provider, since it is already <b>enabled</b> for all providers.
					Change this setting at <b>Administration - Billing - Settings.</b></p>
				<% } %>
			</td>
		</tr>
		<tr>

			<td class="MainTableRightColumn">
				<label for="defaultBillingForm">Select Default Billing Form:</label>
				<html:select property="defaultBillingForm" styleClass="form-control" styleId="defaultBillingForm">
					<html:options collection="billingFormList" property="formCode"
								  labelProperty="description" />
				</html:select>
			</td>
		</tr>
		<tr>

			<td class="MainTableRightColumn"><label for="defaultServiceLocation">Select Default Service Location:</label>
				<html:select property="defaultServiceLocation" styleClass="form-control" styleId="defaultServiceLocation">
					<html:options collection="serviceLocationList" property="visitType"
								  labelProperty="description" />
				</html:select> </td>
		</tr>
		<tr>
			<td class="MainTableRightColumn">
				<label for="payeeProviderNo">Select Default Payee (select "Custom" to set a custom payee):</label>
				<html:select property="payeeProviderNo" styleClass="form-control" styleId="payeeProviderNo" onchange="defaultPayeeSelect()">
				<html:options collection="providerList" property="providerNo" labelProperty="fullName" />
			</html:select> </td>
		</tr>
		<tr>
			<td class="MainTableRightColumn" >
				<label for="invoicePayeeInfo">Set Your Payee Information:</label>
				<html:textarea rows="4" cols="35" styleClass="form-control" styleId="invoicePayeeInfo" property="invoicePayeeInfo">&amp;</html:textarea>
			</td>
		</tr>
		<tr>
			<td class="MainTableRightColumn">
				<label for="invoicePayeeDisplayClinicInfo" class="checkbox-inline">
				<html:checkbox styleId="invoicePayeeDisplayClinicInfo" property="invoicePayeeDisplayClinicInfo"/>
					Display Clinic Information Under Payee</label>
			</td>
		</tr>
		<tr>
			<td class="MainTableRightColumn">
				<label>Example Payee Information:</label>
				<div class="tableHeader rowSpacing">(This is the payee info displayed on your private invoices)</div>
				<table class="table-condensed" style="border:thin solid grey;">
					<%
						Provider payeeProvider = providerDao.getProvider(billingPreference != null ? "" + billingPreference.getDefaultPayeeNo() : null);
						String payeeInfo;
						if(billingPreference == null || "NONE".equals(billingPreference.getDefaultPayeeNo())){
							payeeInfo = "";
						} else {
							if("CUSTOM".equals(billingPreference.getDefaultPayeeNo())){
								List<Property> propList = propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.invoice_payee_info, provider.getProviderNo());
								payeeInfo = !propList.isEmpty() ? propList.get(0).getValue() : "";
							} else {
								payeeInfo = (payeeProvider != null ? (payeeProvider.getFirstName() + " " + payeeProvider.getLastName()) : "");
							}
						}

						ClinicData clinic = new ClinicData();

						String strPhones = clinic.getClinicDelimPhone();
						if (strPhones == null) {
							strPhones = "";
						}
						String strFaxes = clinic.getClinicDelimFax();
						if (strFaxes == null) {
							strFaxes = "";
						}
						Vector vecPhones = new Vector();
						Vector vecFaxes = new Vector();
						StringTokenizer st = new StringTokenizer(strPhones, "|");
						while (st.hasMoreTokens()) {
							vecPhones.add(st.nextToken());
						}
						st = new StringTokenizer(strFaxes, "|");
						while (st.hasMoreTokens()) {
							vecFaxes.add(st.nextToken());
						}

					%>

					<tr class="secHead">
						<td height="14">Please Make Cheque Payable To: </td>
					</tr>
					<% if(!StringUtils.isNullOrEmpty(payeeInfo)) { %>
					<tr>
						<td class="title4 payeeInfo"><%=Encode.forHtml(payeeInfo)%></td>
					</tr>
					<% }
						//Default to true when not found
						if(propertyDao.findByNameAndProvider(Property.PROPERTY_KEY.invoice_payee_display_clinic, provider.getProviderNo()).isEmpty() || propertyDao.isActiveBooleanProperty(Property.PROPERTY_KEY.invoice_payee_display_clinic, provider.getProviderNo())) {
					%>

					<tr>
						<%  SystemPreferences useCustomInvoiceClinicInfo = systemPreferencesDao.findPreferenceByName(SystemPreferences.GENERAL_SETTINGS_KEYS.invoice_use_custom_clinic_info);
							if(useCustomInvoiceClinicInfo == null || StringUtils.isNullOrEmpty(useCustomInvoiceClinicInfo.getValue())) { %>
						<td class="title4">
							<%=Encode.forHtml(clinic.getClinicName())%>
						</td>
					</tr>
					<tr>
						<td class="address"><%=Encode.forHtml(clinic.getClinicAddress()+", "+clinic.getClinicCity()+", "+clinic.getClinicProvince()+" "+clinic.getClinicPostal())%> </td>
					</tr>
					<tr>
						<td class="address" id="clinicPhone"> Telephone: <%=vecPhones.size() >= 1 ? vecPhones.elementAt(0) : Encode.forHtml(clinic.getClinicPhone())%> </td>
					</tr>
					<tr>
						<td class="address" id="clinicFax"> Fax: <%=vecFaxes.size() >= 1 ? vecFaxes.elementAt(0) : Encode.forHtml(clinic.getClinicFax())%> </td>
						<% } else {
							SystemPreferences customInvoiceClinicInfo = systemPreferencesDao.findPreferenceByName(SystemPreferences.GENERAL_SETTINGS_KEYS.invoice_custom_clinic_info);
						%>
						<td class="payeeInfo"><%= Encode.forHtml(customInvoiceClinicInfo.getValue())%></td>

						<% } %>
					</tr>
					<% } %>
				</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableBottomRowRightColumn">
				<html:submit property="submit" styleClass="btn btn-primary pull-right" value="Save" /></td>
		</tr>
</table>
</html:form>
</div>

	<script type="text/javascript">
		document.getElementsByName('autoPopulateRefer')[0].checked = <%= autoPopulateRefer %>;
        function defaultPayeeSelect() {
            let invoicePayeeInfoTA = document.getElementById("invoicePayeeInfo");
            let payeeProviderNoSelect = document.getElementById("payeeProviderNo");
            if(payeeProviderNoSelect.value === 'CUSTOM'){
                invoicePayeeInfoTA.disabled = false;
            } else if(payeeProviderNoSelect.value === 'NONE'){
                invoicePayeeInfoTA.disabled = true;
                invoicePayeeInfoTA.value = '';
            } else {
                invoicePayeeInfoTA.disabled = true;
                Array.prototype.forEach.call(payeeProviderNoSelect.options, function (option) {
                    if(option.value === payeeProviderNoSelect.value)
                        invoicePayeeInfoTA.value = option.text;
                });
            }
        }

        defaultPayeeSelect();

		$( document ).ready(function() {

			const defaultValue = "<%= Property.PROPERTY_VALUE.clinicdefault.name() %>";

			$("#defaultBillingProvider").on("change", function () {
				let selected = $("#defaultBillingProvider option:selected").val();
				disableFields(selected);
			})

			function disableFields(selected) {
				// disable other settings whenever a default provider is selected to override.
				if (selected && selected !== defaultValue) {

					// $("#referral").prop('disabled', true);
					// $("#autoPopulateRefer").prop('disabled', true);
					// $("#defaultBillingForm").prop('disabled', true);
					// $("#defaultServiceLocation").prop('disabled', true);
					// $("#payeeProviderNo").prop('disabled', true);
					// $("#invoicePayeeInfo").prop('disabled', true);
					// $("#invoicePayeeDisplayClinicInfo").prop('disabled', true);

					$("#default-provider-alert").show();

				} else {

					// $("#referral").prop('disabled', false);
					// $("#autoPopulateRefer").prop('disabled', false);
					// $("#defaultBillingForm").prop('disabled', false);
					// $("#defaultServiceLocation").prop('disabled', false);
					// $("#payeeProviderNo").prop('disabled', false);
					// $("#invoicePayeeInfo").prop('disabled', false);
					// $("#invoicePayeeDisplayClinicInfo").prop('disabled', false);

					$("#default-provider-alert").hide();
				}
			}

			disableFields($("#defaultBillingProvider option:selected").val());
		})
    </script>
</body>
</html:html>
