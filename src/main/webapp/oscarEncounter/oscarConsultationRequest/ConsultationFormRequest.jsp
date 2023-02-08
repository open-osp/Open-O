<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.WebUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<!-- end -->
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>


<%@page import="java.util.ArrayList, java.util.Collections, java.util.List, java.util.*, oscar.util.StringUtils, oscar.dms.*, oscar.oscarEncounter.pageUtil.*,oscar.oscarEncounter.data.*, oscar.OscarProperties, oscar.oscarLab.ca.on.*"%>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager,org.oscarehr.casemgmt.model.CaseManagementNote,org.oscarehr.casemgmt.model.Issue,org.oscarehr.common.model.UserProperty,org.oscarehr.common.dao.UserPropertyDAO,org.springframework.web.context.support.*,org.springframework.web.context.*"%>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.util.WebUtils, oscar.SxmlMisc"%>
<%@page import="oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestForm"%>
<%@page import="oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil"%>
<%@page import="oscar.oscarDemographic.data.DemographicData"%>
<%@page import="oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewRequestAction"%>
<%@page import="org.oscarehr.util.MiscUtils,oscar.oscarClinic.ClinicData"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@ page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.MiscUtils, org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager, org.oscarehr.caisi_integrator.ws.CachedDemographicNote"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao, org.oscarehr.PMmodule.model.Program" %>
<%@page import="oscar.oscarDemographic.data.DemographicData, oscar.oscarRx.data.RxProviderData, oscar.oscarRx.data.RxProviderData.Provider, oscar.oscarClinic.ClinicData"%>
<%@ page import="org.oscarehr.common.dao.FaxConfigDao, org.oscarehr.common.model.FaxConfig" %>
<%@page import="org.oscarehr.common.dao.ConsultationServiceDao" %>
<%@page import="org.oscarehr.common.model.ConsultationServices" %>
<%@ page import="org.oscarehr.managers.DemographicManager" %>
<%@ page import="org.oscarehr.managers.PharmacyManager" %>
<%@page import="org.oscarehr.common.model.DemographicContact" %>
<%@page import="org.oscarehr.common.model.Contact" %>
<%@page import="org.oscarehr.common.model.ProfessionalContact" %>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist" %>
<%@page import="org.oscarehr.common.dao.ContactSpecialtyDao" %>
<%@page import="org.oscarehr.common.dao.DemographicContactDao" %>
<%@page import="org.oscarehr.common.model.ContactSpecialty" %>

<jsp:useBean id="displayServiceUtil" scope="request" class="oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConDisplayServiceUtil" />

<!DOCTYPE html>
<html:html locale="true">

<%! boolean bMultisites=org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	DemographicManager demographicManager = SpringUtils.getBean( DemographicManager.class );
	displayServiceUtil.estSpecialist();	

	//multi-site support
	String appNo = request.getParameter("appNo");
	appNo = (appNo==null ? "" : appNo);

	String defaultSiteName = "";
	Integer defaultSiteId = 0;
	Vector<String> vecAddressName = new Vector<String>() ;
	Vector<String> bgColor = new Vector<String>() ;
	Vector<Integer> siteIds = new Vector<Integer>();
	if (bMultisites) {
		SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");

		List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));
		if (sites != null) {
			for (Site s:sites) {
				   siteIds.add(s.getSiteId());
		           vecAddressName.add(s.getName());
		           bgColor.add(s.getBgColor());
		 	}
		}

		if (appNo != "") {
			defaultSiteName = siteDao.getSiteNameByAppointmentNo(appNo);
		}
	}
	String demo_mrp = null;
	String demo = request.getParameter("de");
		String requestId = request.getParameter("requestId");
		// segmentId is != null when viewing a remote consultation request from an hl7 source
		String segmentId = request.getParameter("segmentId");
		String team = request.getParameter("teamVar");
		String providerNo = (String)session.getAttribute("user");
		String providerNoFromChart = null;
		DemographicData demoData = null;
		org.oscarehr.common.model.Demographic demographic = null;

		RxProviderData rx = new RxProviderData();
		List<Provider> prList = rx.getAllProviders();
		Provider thisProvider = rx.getProvider(providerNo);
		ClinicData clinic = new ClinicData();
		
		EctConsultationFormRequestUtil consultUtil = new EctConsultationFormRequestUtil();
		
		if (requestId != null) { 
			consultUtil.estRequestFromId(loggedInInfo, requestId);		
		}
		
		if (demo == null) { 
			demo = consultUtil.demoNo;
		}

		ArrayList<String> users = (ArrayList<String>)session.getServletContext().getAttribute("CaseMgmtUsers");
		boolean useNewCmgmt = false;
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		CaseManagementManager cmgmtMgr = null;
		
		if (users != null && users.size() > 0 && (users.get(0).equalsIgnoreCase("all") || Collections.binarySearch(users, providerNo) >= 0))
		{
			useNewCmgmt = true;
			cmgmtMgr = (CaseManagementManager)ctx.getBean("caseManagementManager");
		}

		UserPropertyDAO userPropertyDAO = (UserPropertyDAO)ctx.getBean("UserPropertyDAO");
		UserProperty fmtProperty = userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_REQ_PASTE_FMT);
		String pasteFmt = fmtProperty != null?fmtProperty.getValue():null;

		if (demo != null) {
			demoData = new oscar.oscarDemographic.data.DemographicData();
			demographic = demoData.getDemographic(loggedInInfo, demo);			
			providerNoFromChart = demographic.getProviderNo();
			demo_mrp = demographic.getProviderNo();
		
			if( demo_mrp == null || demo_mrp.isEmpty() ) {
				DemographicContact demographicContact = demographicManager.getMostResponsibleProviderFromHealthCareTeam(loggedInInfo, Integer.parseInt(demo));
				
				if( demographicContact != null ) {
					demo_mrp = demographicContact.getContactId();
				}
			}
			
			consultUtil.estPatient(loggedInInfo, demo);
			consultUtil.estActiveTeams();
		} else if (requestId == null && segmentId == null) {
			MiscUtils.getLogger().debug("Missing both requestId and segmentId.");
		}

		if (request.getParameter("error") != null) {
			%>
			<SCRIPT LANGUAGE="JavaScript">
			        alert("The form could not be printed due to an error. Please refer to the server logs for more details.");
			    </SCRIPT>
			<%
		}

		java.util.Calendar calender = java.util.Calendar.getInstance();
		String day = Integer.toString(calender.get(java.util.Calendar.DAY_OF_MONTH));
		String mon = Integer.toString(calender.get(java.util.Calendar.MONTH) + 1);
		String year = Integer.toString(calender.get(java.util.Calendar.YEAR));
		String formattedDate = year + "/" + mon + "/" + day;

		OscarProperties props = OscarProperties.getInstance();
		ConsultationServiceDao consultationServiceDao = SpringUtils.getBean(ConsultationServiceDao.class);
%>
		<%--
			// Get attached documents and labs
		 --%>			
<%
	if(requestId != null && Integer.parseInt(requestId) > 0) {
		List<EDoc> attachedDocuments = EDocUtil.listDocs(loggedInInfo, demo, requestId, EDocUtil.ATTACHED);
        CommonLabResultData commonLabResultData = new CommonLabResultData();
        List<LabResultData> attachedLabs = commonLabResultData.populateLabResultsData(loggedInInfo, demo, requestId, CommonLabResultData.ATTACHED);
		
        pageContext.setAttribute("attachedDocuments", attachedDocuments);
        pageContext.setAttribute("attachedLabs", attachedLabs);
	}
%>		
		<%--
			// Look up list for appointment instructions.
		 --%>
<%

		org.oscarehr.managers.LookupListManager lookupListManager = SpringUtils.getBean(org.oscarehr.managers.LookupListManager.class);
		pageContext.setAttribute("appointmentInstructionList", lookupListManager.findLookupListByName( loggedInInfo, "consultApptInst") );

%>
<%--
	// enable option to populate the patients Health Care Team into the Specialist/Service fields.
	// The Health Care Team module will be available to add additional contacts to the patient demographic
 --%>
<% 

// A null demo varialbe means that this iteration is a postback. This script need not be run on postback.
if( demo != null && "true".equals( props.getProperty("ENABLE_HEALTH_CARE_TEAM_IN_CONSULTATION_REQUESTS") ) ) {
	
	ContactSpecialtyDao contactSpecialtyDao = SpringUtils.getBean(ContactSpecialtyDao.class);
	List<DemographicContact> demographicContacts = demographicManager.getHealthCareTeam(loggedInInfo, Integer.parseInt(demo) );
	HashSet<ConsultationServices> consultationServices = new HashSet<ConsultationServices>();
	List<DemographicContact> healthCareTeam = new ArrayList<DemographicContact>();
	DemographicContactDao demographicContactDao = SpringUtils.getBean(DemographicContactDao.class);
	
	// incoming professionalSpecialists can be added to the patient health care team. 
	// The id for these stray professionalSpecialists are identified as less than 0 
	// - only if the Health Care Team module is enabled. 
	String currentSpecialistId = consultUtil.getSpecialist();
	Integer currentSpecialistIdInt = 0;
	String currentDemographicContact = null;
	
	if( currentSpecialistId != null ) {
		currentSpecialistIdInt = Integer.parseInt( currentSpecialistId );
	}
	
	if( currentSpecialistIdInt < 0 ) {

		// Set the DemographicContact list into an array for further processing.
		// This list contains a guranteed id from the ProfessionalSpecialist model
		for( DemographicContact demographicContact : demographicContacts ) {	
			// separate method to check the current list for existing professionalSpecialist
			// This will determine if a new DemographicContact should be created. 
			if( currentDemographicContact == null && ( (currentSpecialistIdInt * -1) + "" ).equals( demographicContact.getContactId() ) ) {
				currentDemographicContact = demographicContact.getId() + "";
			}
		}
	}
	
	if( currentDemographicContact != null ) {
		
		// this ProfessionalSpecialist is already in the DemographicContacts.
		consultUtil.setSpecialist( currentDemographicContact );
		
	} else if( currentSpecialistIdInt < 0 ) {

		// this ProfessionalSpecialist needs to have a DemographicContact created. 
		String service = consultUtil.getService();

		ContactSpecialty contactSpecialty = contactSpecialtyDao.findBySpecialty( service );
		
		if( contactSpecialty == null ) {
			contactSpecialty = contactSpecialtyDao.findBySpecialty( "other" );
		}
		
		service = contactSpecialty.getId() + "";
		
		if( service == null ){
			service = "";
		}

		DemographicContact demographicContact = addDemographicContact( loggedInInfo, demo, (currentSpecialistIdInt * -1), service );
		
		demographicContactDao.persist( demographicContact );		
		demographicContacts = demographicManager.getHealthCareTeam(loggedInInfo, Integer.parseInt(demo) );		
		consultUtil.setSpecialist( demographicContact.getId() + "" );
	}
	
	setHealthCareTeam( demographicContacts, healthCareTeam, consultationServices, consultationServiceDao );
		
	pageContext.setAttribute("consultUtil", consultUtil);
	pageContext.setAttribute( "consultationServices", consultationServices );
	pageContext.setAttribute( "healthCareTeam", healthCareTeam );
}
%>
<%!
private static DemographicContact addDemographicContact(LoggedInInfo loggedInInfo, 
		String demographicNo, int contactId, String role ) {
	
	if( role == null ) {
		role = "0";
	}
	
	DemographicContact demographicContact = new DemographicContact();
	demographicContact.setFacilityId( loggedInInfo.getCurrentFacility().getId() );
	demographicContact.setCreator( loggedInInfo.getLoggedInProviderNo() );
	demographicContact.setCreated( new Date( System.currentTimeMillis() ) );
	demographicContact.setUpdateDate( new Date( System.currentTimeMillis() ) );
	demographicContact.setDeleted( Boolean.FALSE );
	demographicContact.setDemographicNo( Integer.parseInt(demographicNo) );
	demographicContact.setContactId( contactId + "" );
	demographicContact.setRole( role );
	demographicContact.setType( 3 );
	demographicContact.setCategory( "professional" );
	
	return demographicContact;
}
%>

<%!
private static void setHealthCareTeam( List<DemographicContact> demographicContacts, 
		List<DemographicContact> healthCareTeam, HashSet<ConsultationServices> consultationServices,
		ConsultationServiceDao consultationServiceDao ) {

	for( DemographicContact demographicContact : demographicContacts ) {
		// ensure consent has been given to contact this specialist.
		// ensure that this specialist has a cpso (specialist, msp, or college id)
		if( demographicContact.isConsentToContact() && 
				( ( (ProfessionalContact) demographicContact.getDetails() ).getCpso() != null ) &&
				( ! ( (ProfessionalContact) demographicContact.getDetails() ).getCpso().isEmpty() )
			) {
			healthCareTeam.add( demographicContact );
			
			// Get the specialty list for this group of specialist.
			// This is a hack. Do not expand on it. There are several specialty look up tables in Oscar 
			// The health care team uses the ContactSpecialty table and this Consultation feature uses the consultatationServices
			ConsultationServices consultService = consultationServiceDao.findByDescription( demographicContact.getRole() );
			if( consultService != null ) {
				consultationServices.add( consultService );
			}
		}
	}

}

%>
<%--
	// Read the Health Care Team from the pageScope into a Javascript globalScope;
 --%>
<c:if test="${ not empty consultationServices }" >
	<script type="text/javascript" >
		var consultationServices = [];
	</script>	
	<c:forEach items="${ consultationServices }" var="consultationService" varStatus="loop">
		<script type="text/javascript"> 
		//<!--
			var service = {};
			service.id = `${ consultationService.serviceId }`;
			service.description = `${ consultationService.serviceDesc }`;
			if( service ) {
				consultationServices.push( service );
			}
		//--> 
		</script>
	</c:forEach>
</c:if>
<%--
	// Read the associated services and specialties from the pageScope into a Javascript globalScope;
 --%>
 <c:if test="${ not empty healthCareTeam }" >
	<script type="text/javascript" >
		var healthCareTeam = [];
	</script>	
	<c:forEach items="${ healthCareTeam }" var="demographicContact" varStatus="loop">
		<script type="text/javascript"> 
		//<!--
			var contact = {};
			contact.contactId = `${ demographicContact.details.id }`;
			contact.specNbr = `${ demographicContact.details.cpso }`;
			contact.phoneNum = `${ demographicContact.details.workPhone }`;
			contact.specName = `${ demographicContact.details.formattedName }`;
			contact.service = `${ demographicContact.role }`;
			contact.specFax = `${ demographicContact.details.fax }`;		
			contact.specAddress = `${ demographicContact.details.address }`;		
			contact.specAddress2 = `${ demographicContact.details.address2 }`;
			contact.city = `${ demographicContact.details.city }`;
			contact.province = `${ demographicContact.details.province }`;
			contact.postal = `${ demographicContact.details.postal }`; 
			contact.note = `${ demographicContact.details.note }`;
			healthCareTeam[ `${ demographicContact.id }` ] = contact;
		//--> 
		</script>
	</c:forEach>
</c:if>
 
<%-- Add function for specialist selection events. --%>
<script type="text/javascript"> 
	//<!--
	function getSpecialist( selected ) {
		var specialistIndex = selected.value;
		var form = document.EctConsultationFormRequestForm;

		if( specialistIndex < 0 ) {		
			form.phone.value = ("");
			form.fax.value = ("");
			form.address.value = ("");
			
			specialistFaxNumber = ""; // global variable		
		}
		
		if( specialistIndex > -1 ) {
			form.annotation.value = healthCareTeam[ specialistIndex ].note; 
			form.phone.value = healthCareTeam[ specialistIndex ].phoneNum;
			form.fax.value = healthCareTeam[ specialistIndex ].specFax;					
			form.address.value = healthCareTeam[ specialistIndex ].specAddress;
			
			specialistFaxNumber = healthCareTeam[ specialistIndex ].specFax; // global variable
			updateFaxButton();
			
			var service = healthCareTeam[ specialistIndex ].service;

			if( ! service ) {
				
				form.service.value = '57';
				
			} else {
				form.service.value = "";
				for( var i = 0; consultationServices.length; i++ ) {
					var specialistService = consultationServices[i];
					if( specialistService.description === service ) {
						form.service.value = specialistService.id;
					}
				}
			}
		}
	}
//--> 
</script>

<head>
<title>
<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.title" />
</title>
<html:base />
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<script>
	var ctx = '<%=request.getContextPath()%>';
	var requestId = '<%=request.getParameter("requestId")%>';
	var demographicNo = '<%=demo%>';
	var demoNo = '<%=demo%>';
	var appointmentNo = '<%=appNo%>';
</script>


<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/js/jquery_css/smoothness/jquery-ui-1.7.3.custom.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.7.1.min.js" ></script>                
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js" ></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery_oscar_defaults.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/prototype.js"></script>
<link href="<%=request.getContextPath() %>/css/jquery-ui.min.css" rel="stylesheet" media="screen" />
<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>

   <script>
     jQuery.noConflict();
   </script>

<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/css/healthCareTeam.css" />
<oscar:customInterface section="conreq"/>
<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/oscarEncounter/encounterStyles.css">

<style type="text/css">

#attachedDocumentTable {
    border: blue thin solid;
    border-collapse: collapse;
    width:100%;
background-color: #ddddff;
}
#attachedDocumentTable tr td {
    padding:5px;
}

#attachedDocumentsTable {
    border-collapse: collapse;
    width:100%;
}

#attachedDocumentsTable h3, #attachedLabsTable h3 {
    margin: 0px !important;
    padding: 0px !important;
    border-bottom: grey thin solid;
}

#attachedLabsTable {
    border-collapse: collapse;
    width:100%;
}

.ui-dialog {
    width:400px !important;
    height: auto !important;
    font-size: small !important;
}
.ui-autocomplete{
     font-size: small !important;
}

th, td.tite1 {
background-color: #BFBFFF;
color : black;
font-size: small;
padding: 0px;
}
td.tite3 {
background-color: #BFBFFF;
color : black;
padding: 0px;
}
td.tite1 {
    padding:5px 10px;
}

td.tite4 {
 padding:0px 10px;
background-color: #ddddff;
color : black;
font-size: small;
}

td.stat{
font-size: 10pt;
}

.consultDemographicData input {
    width: 98% !important;
} 

.consultDemographicData select{
    width: 100% !important;
}

input#referalDate, input#appointmentDate, input#followUpDate {
   background-image: url( ../../images/cal.gif);
   background-position-x: right;
   background-position-y: center;
   background-repeat: no-repeat;
}

#referalDate_cal, #appointmentDate_cal, #followUpDate_cal  {
    display: none !important;
}

* table tr td {
    vertical-align:top !important;
}


textarea {
    width: 98% !important;
    padding:1%;
}

.MainTableLeftColumn td {
    background-color: #ddddff;
}

.controlPanel {
    padding: 5px 10px !important;
    border:blue thin solid;
}

.heading{
    font-weight: bold;
    padding:5px !important;
}


</style>
</head>


<script type="text/javascript">

var servicesName = new Object();   		// used as a cross reference table for name and number
var services = new Array();				// the following are used as a 2D table for makes and models
var specialists = new Array();
var specialistFaxNumber = "";
<%oscar.oscarEncounter.oscarConsultationRequest.config.data.EctConConfigurationJavascriptData configScript;
				configScript = new oscar.oscarEncounter.oscarConsultationRequest.config.data.EctConConfigurationJavascriptData();
				out.println(configScript.getJavascript());%>

/////////////////////////////////////////////////////////////////////
function initMaster() {
	makeSpecialistslist(2);
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
// create car make objects and fill arrays
//==========
function K( serviceNumber, service ){

	//servicesName[service] = new ServicesName(serviceNumber);
        servicesName[service] = serviceNumber;
	services[serviceNumber] = new Service( );
}
//-------------------------------------------------------------------
	
	//-----------------disableDateFields() disables date fields if "Patient Will Book" selected
	var disableFields=false;
</script>

<oscar:oscarPropertiesCheck defaultVal="false" value="true" property="CONSULTATION_PATIENT_WILL_BOOK">
	<script type="text/javascript" >

	
	function disableDateFields(){
		if(document.forms[0].patientWillBook.checked){
			setDisabledDateFields(document.forms[0], true);
		}
		else{
			setDisabledDateFields(document.forms[0], false);
		}
	}
	</script>
</oscar:oscarPropertiesCheck>

<oscar:oscarPropertiesCheck defaultVal="false" value="false" property="CONSULTATION_PATIENT_WILL_BOOK">
	<script type="text/javascript" >
	
	function disableDateFields(){

		setDisabledDateFields(document.forms[0], false);

	}
	</script>
</oscar:oscarPropertiesCheck>


<script type="text/javascript" >

function getClinicalData( data, target ) {
	jQuery.ajax({
		method : "POST",
		url : "${ pageContext.request.contextPath }/oscarConsultationRequest/consultationClinicalData.do",
		data : data,
		dataType : 'JSON',
		success: function(data) {
			jQuery(target).val( jQuery(target).val() + "\n" + data.note );			
		}
	});
}

jQuery(document).ready(function(){
	jQuery(".medicationData").click(function(){
		var data = new Object();
		var target = "#" + this.id.split("_")[1];		
		data.method = this.id.split("_")[0];
		data.demographicNo = <%= demo %>;
		getClinicalData( data, target )
	});
	
	jQuery(".clinicalData").click(function(){
		var data = new Object();
		var target = "#" + this.id.split("_")[1];		
		data.method = "fetchIssueNote";
		data.issueType = this.id.split("_")[0];
		data.demographicNo = <%= demo %>;
		getClinicalData( data, target )
	});
})

function setDisabledDateFields(form, disabled)
{
	//form.appointmentYear.disabled = disabled;
	//form.appointmentMonth.disabled = disabled;
	//form.appointmentDay.disabled = disabled;
	form.appointmentHour.disabled = disabled;
	form.appointmentMinute.disabled = disabled;
	form.appointmentPm.disabled = disabled;
}

function disableEditing()
{
	if (disableFields)
	{
		form=document.forms[0];

		setDisabledDateFields(form, disableFields);

		form.status[0].disabled = disableFields;
		form.status[1].disabled = disableFields;
		form.status[2].disabled = disableFields;
		form.status[3].disabled = disableFields;

		form.referalDate.disabled = disableFields;
		form.service.disabled = disableFields;
		form.urgency.disabled = disableFields;
		form.phone.disabled = disableFields;
		form.fax.disabled = disableFields;
		form.address.disabled = disableFields;
		form.patientWillBook.disabled = disableFields;
		form.sendTo.disabled = disableFields;

		form.appointmentNotes.disabled = disableFields;
		form.reasonForConsultation.disabled = disableFields;
		form.clinicalInformation.disabled = disableFields;
		form.concurrentProblems.disabled = disableFields;
		form.currentMedications.disabled = disableFields;
		form.allergies.disabled = disableFields;
                form.annotation.disabled = disableFields;

		disableIfExists(form.update, disableFields);
		disableIfExists(form.updateAndPrint, disableFields);
		disableIfExists(form.updateAndSendElectronically, disableFields);
		disableIfExists(form.updateAndFax, disableFields);

		disableIfExists(form.submitSaveOnly, disableFields);
		disableIfExists(form.submitAndPrint, disableFields);
		disableIfExists(form.submitAndSendElectronically, disableFields);
		disableIfExists(form.submitAndFax, disableFields);
	}
}

function disableIfExists(item, disabled)
{
	if (item!=null) item.disabled=disabled;
}

//------------------------------------------------------------------------------------------
/////////////////////////////////////////////////////////////////////
// create car model objects and fill arrays
//=======
function D( servNumber, specNum, phoneNum ,SpecName,SpecFax,SpecAddress){
    var specialistObj = new Specialist(servNumber,specNum,phoneNum, SpecName, SpecFax, SpecAddress);
    services[servNumber].specialists.push(specialistObj);
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function Specialist(makeNumber,specNum,phoneNum,SpecName, SpecFax, SpecAddress){

	this.specId = makeNumber;
	this.specNbr = specNum;
	this.phoneNum = phoneNum;
	this.specName = SpecName;
	this.specFax = SpecFax;
	this.specAddress = SpecAddress;
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
// make name constructor
function ServicesName( makeNumber ){

	this.serviceNumber = makeNumber;
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
// make constructor
function Service(  ){

	this.specialists = new Array();
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
// construct model selection on page
function fillSpecialistSelect( aSelectedService ){


	var selectedIdx = aSelectedService.selectedIndex;
	var makeNbr = (aSelectedService.options[ selectedIdx ]).value;

	document.EctConsultationFormRequestForm.specialist.options.selectedIndex = 0;
	document.EctConsultationFormRequestForm.specialist.options.length = 1;

	document.EctConsultationFormRequestForm.phone.value = ("");
	document.EctConsultationFormRequestForm.fax.value = ("");
	document.EctConsultationFormRequestForm.address.value = ("");

	if ( selectedIdx == 0)
	{
		return;
	}

        var i = 1;
	var specs = (services[makeNbr].specialists);
	for ( var specIndex = 0; specIndex < specs.length; ++specIndex ){
		   aPit = specs[ specIndex ];	   	
           document.EctConsultationFormRequestForm.specialist.options[ i++ ] = new Option( aPit.specName , aPit.specNbr );
	}

}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function fillSpecialistSelect1( makeNbr )
{
	//document.EctConsultationFormRequestForm.specialist.options.length = 1;

	var specs = (services[makeNbr].specialists);
	var i=1;
    var match = false;
        
	for ( var specIndex = 0; specIndex < specs.length; ++specIndex )
	{
		aPit = specs[specIndex];

		if(aPit.specNbr=="<%=consultUtil.specialist%>"){
			//look for matching specialist on spec list and make option selected
			match=true;
			document.EctConsultationFormRequestForm.specialist.options[i] = new Option(aPit.specName, aPit.specNbr,false ,true );
		}else{
			//add specialist on list as normal
			document.EctConsultationFormRequestForm.specialist.options[i] = new Option(aPit.specName, aPit.specNbr );
		}

		i++;
	}

	<%if(requestId!=null){ %>
		if(!match){ 
			//if no match then most likely doctor has been removed from specialty list so just add specialist
			document.EctConsultationFormRequestForm.specialist.options[0] = new Option("<%=consultUtil.getSpecailistsName(consultUtil.specialist)%>", "<%=consultUtil.specialist%>",false ,true );

			//don't display if no consultant was saved
			<%if( ! "null".equals(consultUtil.specialist) ){%>
			document.getElementById("consult-disclaimer").style.display='inline';
			<%}else{%>
			//display so user knows why field is empty
			document.EctConsultationFormRequestForm.specialist.options[0] = new Option("No Consultant Saved", "-1");
			<%}%>
		}
	<%}%>

}
//-------------------------------------------------------------------f

/////////////////////////////////////////////////////////////////////
function setSpec(servNbr,specialNbr){
//    //window.alert("get Called");
    specs = (services[servNbr].specialists);
//    //window.alert("got specs");
    var i=1;
    var NotSelected = true;
 
    for ( var specIndex = 0; specIndex < specs.length; ++specIndex ){
//      //  window.alert("loop");
        aPit = specs[specIndex];
        if (aPit.specNbr == specialNbr){
//        //    window.alert("if");
            document.EctConsultationFormRequestForm.specialist.options[i].selected = true;
            NotSelected = false;
        }

        i++;
    }

    if( NotSelected )
        document.EctConsultationFormRequestForm.specialist.options[0].selected = true;
//    window.alert("exiting");

}
//=------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
//insert first option title into specialist drop down list select box
function initSpec() {
	<%if(requestId==null){ %>
	var aSpecialist = services["-1"].specialists[0];
    document.EctConsultationFormRequestForm.specialist.options[0] = new Option(aSpecialist.specNbr, aSpecialist.specId);
    <%}%>
}

/////////////////////////////////////////////////////////////////////
function initService(ser,name,spec,specname,phone,fax,address){
	var i = 0;
	var isSel = 0;
	var strSer = new String(ser);
	var strNa = new String(name);
	var strSpec = new String(spec);
	var strSpecNa = new String(specname);
	var strPhone = new String(phone);
	var strFax = new String(fax);
	var strAddress = new String(address);

	var isSerDel=1;//flagging service if deleted: 1=deleted 0=active

	$H(servicesName).each(function(pair){
	if( pair.value == strSer ) {
	isSerDel = 0;
	}
	});

	if (isSerDel==1 && strSer != "null") {
	K(strSer,strNa);
	D(strSer,strSpec,strPhone,strSpecNa,strFax,strAddress);
    }

        $H(servicesName).each(function(pair){
              var opt = new Option( pair.key, pair.value );
              if( pair.value == strSer ) {
                opt.selected = true;
                fillSpecialistSelect1( pair.value );
              }
              $("service").options.add(opt);

        });

	}
//-------------------------------------------------------------------
/////////////////////////////////////////////////////////////////////
function onSelectSpecialist(SelectedSpec)	{
	var selectedIdx = SelectedSpec.selectedIndex;
	var form=document.EctConsultationFormRequestForm;

	if (selectedIdx==null || selectedIdx==-1 || (SelectedSpec.options[ selectedIdx ]).value == "-1") {   		//if its the first item set everything to blank
		form.phone.value = ("");
		form.fax.value = ("");
		form.address.value = ("");

		enableDisableRemoteReferralButton(form, true);

		<%
		if (props.isConsultationFaxEnabled()) {//
		%>
		specialistFaxNumber = "";
		updateFaxButton();
		<% } %>
		
		return;
	}
	var selectedService = document.EctConsultationFormRequestForm.service.value;  				// get the service that is selected now
	var specs = (services[selectedService].specialists); 			// get all the specs the offer this service
    
	// load the text fields with phone fax and address for past consult review even if spec has been removed from service list
	<%if(requestId!=null && ! "null".equals( consultUtil.specialist ) ){ %>
	form.phone.value = '<%=StringEscapeUtils.escapeJavaScript(consultUtil.specPhone)%>';
	form.fax.value = '<%=StringEscapeUtils.escapeJavaScript(consultUtil.specFax)%>';					
	form.address.value = '<%=StringEscapeUtils.escapeJavaScript(consultUtil.specAddr) %>';

	//make sure this dislaimer is displayed
	document.getElementById("consult-disclaimer").style.display='inline';
	<%}%>
	
								
        for( var idx = 0; idx < specs.length; ++idx ) {
            aSpeci = specs[idx];									// get the specialist Object for the currently selected spec
            if( aSpeci.specNbr == SelectedSpec.value ) {
            	form.phone.value = (aSpeci.phoneNum.replace(null,""));
            	form.fax.value = (aSpeci.specFax.replace(null,""));					// load the text fields with phone fax and address
            	form.address.value = (aSpeci.specAddress.replace(null,""));
            	
       			//since there is a match make sure the dislaimer is hidden
       			document.getElementById("consult-disclaimer").style.display='none';
        	
            	<%
        		if (props.isConsultationFaxEnabled()) {//
				%>
				specialistFaxNumber = aSpeci.specFax.trim();
				updateFaxButton();
        		<% } %>
            	
            	jQuery.getJSON("getProfessionalSpecialist.json", {id: aSpeci.specNbr},
                    function(xml)
                    {
                		var hasUrl=xml.eDataUrl!=null&&xml.eDataUrl!="";
                		enableDisableRemoteReferralButton(form, !hasUrl);

                                var annotation = document.getElementById("annotation");
                                annotation.value = xml.annotation;
                	}
            	);

            	break;
            }
        }//spec loop
	 
	}

//-----------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function FillThreeBoxes(serNbr)	{

	var selectedService = document.EctConsultationFormRequestForm.service.value;  				// get the service that is selected now
	var specs = (services[selectedService].specialists);					// get all the specs the offer this service

        for( var idx = 0; idx < specs.length; ++idx ) {
            aSpeci = specs[idx];									// get the specialist Object for the currently selected spec
            if( aSpeci.specNbr == serNbr ) {
                document.EctConsultationFormRequestForm.phone.value = (aSpeci.phoneNum);
                document.EctConsultationFormRequestForm.fax.value = (aSpeci.specFax);					// load the text fields with phone fax and address
                document.EctConsultationFormRequestForm.address.value = (aSpeci.specAddress);
                <%
        		if (props.isConsultationFaxEnabled()) {//
				%>
				specialistFaxNumber = aSpeci.specFax.trim();
				updateFaxButton();
				<% } %>
                break;
           }
        }
}
//-----------------------------------------------------------------

function enableDisableRemoteReferralButton(form, disabled)
{
	var button=form.updateAndSendElectronically;
	if (button!=null) button.disabled=disabled;
	button=form.submitAndSendElectronically;
	if (button!=null) button.disabled=disabled;

        var button=form.updateAndSendElectronicallyTop;
	if (button!=null) button.disabled=disabled;
	button=form.submitAndSendElectronicallyTop;
	if (button!=null) button.disabled=disabled;
}

//-->

function BackToOscar() {
       window.close();
}
function rs(n,u,w,h,x){
	args="width="+w+",height="+h+",resizalbe=yes,scrollbars=yes,status=0,top=60,left=30";
        remote=window.open(u,n,args);
        if(remote != null){
	   if (remote.opener == null)
		remote.opener = self;
	}
	if ( x == 1 ) { return remote; }
}

var DocPopup = null;
function popup(location) {
    DocPopup = window.open(location,"_blank","height=380,width=580");

    if (DocPopup != null) {
        if (DocPopup.opener == null) {
            DocPopup.opener = self;
        }
    }
}

function popupAttach( height, width, url, windowName){
  var page = url;
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(url, windowName, windowprops);
  if (popup != null){
    if (popup.opener == null){
      popup.opener = self;
    }
  }
  popup.focus();
  return false;
}

function popupOscarCal(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no,screenX=0,screenY=0,top=20,left=20";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCal"/>", windowprops);

  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}

</script>

<oscar:oscarPropertiesCheck value="true" property="ENABLE_HEALTH_CARE_TEAM_IN_CONSULTATION_REQUESTS" defaultVal="false">
<script type="text/javascript">
//<!--
	function checkFormHCT(){
	
		var msg = "Please select a Consultant. Or add a Consultant using edit Health Care Team.";    
	   	var specialistElement = document.EctConsultationFormRequestForm.specialist.options;
		if( ! specialistElement || specialistElement.selectedIndex < 0 ) {	   
		   	document.EctConsultationFormRequestForm.specialist.focus();
		   	alert(msg);
		   	return false;
	   	}
		
		msg = "The selected consultant contains an invalid specialty type. Please add or correct the current specialty using edit Health Care Team";
	   	var serviceElement = document.EctConsultationFormRequestForm.service;
	   	if( ! serviceElement || serviceElement.value == "" ) {	   
		   	document.EctConsultationFormRequestForm.service.focus();
		   	alert(msg);
		   	return false;
	   	}
	   	
		return true;
	}
//-->
</script>
</oscar:oscarPropertiesCheck>

<script type="text/javascript">

function checkForm(submissionVal,formName){
	
	var success = true;

   if (typeof checkFormHCT === "function") { 
   		if( ! checkFormHCT() ) {
   			return false;
   		}
   }
   
   var msg = "<bean:message key="Errors.service.noServiceSelected"/>";
   msg  = msg.replace('<li>','');
   msg  = msg.replace('</li>','');
   var serviceOptionsElement = document.EctConsultationFormRequestForm.service.options;
  if ( serviceOptionsElement && serviceOptionsElement.selectedIndex == 0 ){
     alert(msg);
     document.EctConsultationFormRequestForm.service.focus();
     return false;
  }
  var faxNumber = document.EctConsultationFormRequestForm.fax.value;
  faxNumber = faxNumber.trim();
  var apptDate = document.EctConsultationFormRequestForm.appointmentDate.value;
  var hasApptTime = document.EctConsultationFormRequestForm.appointmentHour.options.selectedIndex != 0 && 
  	document.EctConsultationFormRequestForm.appointmentMinute.options.selectedIndex != 0;
  
  if(apptDate.length > 0 && !hasApptTime) {
	  alert('Please enter appointment time. You cannot choose appointment date only.');
	  return false;
  }
  
  if('Submit And Fax' === submissionVal && !faxNumber) {
	  alert('Please enter a valid 10 digit consultant fax number');
	  return false;
  }
  
  $("saved").value = "true";
  document.forms[formName].submission.value=submissionVal;
  document.forms[formName].submit();
  return true;
}
</script>


<%
String lhndType = "provider"; //set default as provider
String providerDefault = providerNo;

if(consultUtil.letterheadName == null ){
//nothing saved so find default	
UserProperty lhndProperty = userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_LETTERHEADNAME_DEFAULT);
String lhnd = lhndProperty != null?lhndProperty.getValue():null;
//1 or null = provider, 2 = MRP and 3 = clinic

	if(lhnd!=null){	
		if(lhnd.equals("2")){
			//mrp
			providerDefault = providerNoFromChart;
		}else if(lhnd.equals("3")){
			//clinic
			lhndType="clinic";
		}
	}	

}
%>

<script>

var providerData = new Object(); //{};


providerData['<%=StringEscapeUtils.escapeHtml(clinic.getClinicName())%>'] = new Object();

var addr;
var ph;
var fx;

<% 
if (consultUtil.letterheadAddress != null) { 
	%>addr = '<%=consultUtil.letterheadAddress%>';<%
} else {
	%> addr = '<%=clinic.getClinicAddress() %>  <%=clinic.getClinicCity() %>  <%=clinic.getClinicProvince() %>  <%=clinic.getClinicPostal() %>';<%
}

if(consultUtil.letterheadPhone != null) {
	%>ph = '<%=consultUtil.letterheadAddress%>';<%
} else {
	%>ph = '<%=clinic.getClinicPhone()%>';<%
}


if(consultUtil.letterheadFax != null) {
	%>fx = '<%=consultUtil.letterheadFax%>';<%
} else {
	%>fx = '<%=clinic.getClinicFax()%>';<%
}
%>
providerData['<%=StringEscapeUtils.escapeHtml(clinic.getClinicName())%>'].address = addr;
providerData['<%=StringEscapeUtils.escapeHtml(clinic.getClinicName())%>'].phone = ph;
providerData['<%=StringEscapeUtils.escapeHtml(clinic.getClinicName())%>'].fax = fx;


<%
for (Provider p : prList) {
	if (!p.getProviderNo().equalsIgnoreCase("-1")) {
		String prov_no = "prov_"+p.getProviderNo();

		%>
	 providerData['<%=prov_no%>'] = new Object(); //{};

	providerData['<%=prov_no%>'].address = "<%=p.getFullAddress() %>";
	providerData['<%=prov_no%>'].phone = "<%=p.getClinicPhone().trim() %>";
	providerData['<%=prov_no%>'].fax = "<%=p.getClinicFax().trim() %>";

<%	}
}

ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
List<Program> programList = programDao.getAllActivePrograms();

if (OscarProperties.getInstance().getBooleanProperty("consultation_program_letterhead_enabled", "true")) {
	if (programList != null) {
		for (Program p : programList) {
			String progNo = "prog_" + p.getId();
%>
		providerData['<%=progNo %>'] = new Object();
		providerData['<%=progNo %>'].address = "<%=(p.getAddress() != null && p.getAddress().trim().length() > 0) ? p.getAddress().trim() : ((clinic.getClinicAddress() + "  " + clinic.getClinicCity() + "   " + clinic.getClinicProvince() + "  " + clinic.getClinicPostal()).trim()) %>";
		providerData['<%=progNo %>'].phone = "<%=(p.getPhone() != null && p.getPhone().trim().length() > 0) ? p.getPhone().trim() : clinic.getClinicPhone().trim() %>";
		providerData['<%=progNo %>'].fax = "<%=(p.getFax() != null && p.getFax().trim().length() > 0) ? p.getFax().trim() : clinic.getClinicFax().trim() %>";
<%
		}
	}
} %>


function switchProvider(value) {
	if (value==-1) {
		document.getElementById("letterheadName").value = value;
		document.getElementById("letterheadAddress").value = "<%=(clinic.getClinicAddress() + "  " + clinic.getClinicCity() + "   " + clinic.getClinicProvince() + "  " + clinic.getClinicPostal()).trim() %>";
		document.getElementById("letterheadAddressSpan").innerHTML = "<%=(clinic.getClinicAddress() + "  " + clinic.getClinicCity() + "   " + clinic.getClinicProvince() + "  " + clinic.getClinicPostal()).trim() %>";
		document.getElementById("letterheadPhone").value = "<%=clinic.getClinicPhone().trim() %>";
		document.getElementById("letterheadPhoneSpan").innerHTML = "<%=clinic.getClinicPhone().trim() %>";
		document.getElementById("letterheadFax").value = "<%=clinic.getClinicFax().trim() %>";
		// document.getElementById("letterheadFaxSpan").innerHTML = "<%=clinic.getClinicFax().trim() %>";
	} else {
		var origValue = value;
		if (typeof providerData["prov_" + value.toString()] != "undefined")
			value = "prov_" + value;
		
		
		document.getElementById("letterheadName").value = origValue;
		document.getElementById("letterheadAddress").value = providerData[value]['address'];
		document.getElementById("letterheadAddressSpan").innerHTML = providerData[value]['address'].replace(" ", "");
		document.getElementById("letterheadPhone").value = providerData[value]['phone'];
		document.getElementById("letterheadPhoneSpan").innerHTML = providerData[value]['phone'];
		document.getElementById("letterheadFax").value = providerData[value]['fax'];
		//document.getElementById("letterheadFaxSpan").innerHTML = providerData[value]['fax'];
	}
}

<%
String signatureRequestId=DigitalSignatureUtils.generateSignatureRequestId(loggedInInfo.getLoggedInProviderNo());
String imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_preview.name()+"&"+DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;
String storedImgUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_stored.name()+"&digitalSignatureId=";
%>

var POLL_TIME=1500;
var counter=0;
function refreshImage()
{
	counter=counter+1;
	document.getElementById('signatureImgTag').src='<%=imageUrl%>&rand='+counter;
	document.getElementById('signatureImg').value='<%=signatureRequestId%>';
}

function showSignatureImage()
{
	if (document.getElementById('signatureImg') != null && document.getElementById('signatureImg').value.length > 0) {
		document.getElementById('signatureImgTag').src = "<%=storedImgUrl %>" + document.getElementById('signatureImg').value;

		<% if (OscarProperties.getInstance().getBooleanProperty("topaz_enabled", "true")) { 
		  //this is empty
		%>

		document.getElementById('clickToSign').style.display = "none";

		<% } else { 
		  //this is empty
		%>

		document.getElementById("signatureFrame").style.display = "none";

		<% } %>


		document.getElementById('signatureShow').style.display = "block";
	}

	return true;
}

<%
String userAgent = request.getHeader("User-Agent");
String browserType = "";
if (userAgent != null) {
	if (userAgent.toLowerCase().indexOf("ipad") > -1) {
		browserType = "IPAD";
	} else {
		browserType = "ALL";
	}
}
%>

function requestSignature()
{


	<% if (OscarProperties.getInstance().getBooleanProperty("topaz_enabled", "true")) { %>
	document.getElementById('newSignature').value = "true";
	document.getElementById('signatureShow').style.display = "block";
	document.getElementById('clickToSign').style.display = "none";
	document.getElementById('signatureShow').style.display = "block";
	setInterval('refreshImage()', POLL_TIME);
	document.location='<%=request.getContextPath()%>/signature_pad/topaz_signature_pad.jnlp.jsp?<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>';

	<% } %>
}

var isSignatureDirty = false;
var isSignatureSaved = <%= consultUtil.signatureImg != null && !"".equals(consultUtil.signatureImg) ? "true" : "false" %>;

function signatureHandler(e) {
	isSignatureDirty = e.isDirty;
	isSignatureSaved = e.isSave;
	<%
	if (props.isConsultationFaxEnabled()) { //
	%>
	updateFaxButton();
	<% } %>
	if (e.isSave) {
		refreshImage();
		document.getElementById('newSignature').value = "true";
	}
	else {
		document.getElementById('newSignature').value = "false";
	}
}

var requestIdKey = "<%=signatureRequestId %>";

function AddOtherFaxProvider() {
	var name = jQuery("#searchHealthCareTeamInput").val();
	var fax = jQuery("#copytoSpecialistFax").val();
	if ( checkPhone(fax) ) {
		_AddOtherFax(name,fax);
		jQuery("#searchHealthCareTeamInput").val("");
		jQuery("#copytoSpecialistFax").val("");
	}
	else {
		alert("The fax number you entered is invalid.");
	}
}
function AddOtherFax() {
	var number = jQuery("#otherFaxInput").val();
	if (checkPhone(number)) {
		_AddOtherFax(number,number);
	}
	else {
		alert("The fax number you entered is invalid.");
	}
}

function _AddOtherFax(name, number) {
	var remove = "<a href='javascript:void(0);' onclick='removeRecipient(this)'>remove</a>";
	var rvalue = {};
	rvalue.name=name;
	rvalue.fax=number;
	var html = "<tr><td class='tite1'>" + name + "</td><td class='tite1'>" + number + "</td><td class='tite1'>" + remove
		+ "<input type='hidden' id='faxRecipients' name='faxRecipients' value='" + JSON.stringify(rvalue) + "' /> </td></tr>";
	jQuery("#addFaxRecipient").append(jQuery(html));
	updateFaxButton();
}

function checkPhone(str)
{
	str = str.trim().replace(/\D/g,'');
	var phone =  /^((\+\d{1,3}(-| )?\(?\d\)?(-| )?\d{1,5})|(\(?\d{2,6}\)?))(-| )?(\d{3,4})(-| )?(\d{4})(( x| ext)\d{1,5}){0,1}$/
	if (str.match(phone)) {
   		return true;
 	} else {
 		return false;
 	}
}

function removeRecipient(el) {
	var el = jQuery(el);
	if (el) { el.parent().parent().remove(); }
	else { alert("Unable to remove recipient."); }
}

function hasFaxNumber() {
	return specialistFaxNumber.length > 0 || ( jQuery("#faxRecipients").val() != null && jQuery("#faxRecipients").val() != "undefined");
}
function updateFaxButton() {
	var disabled = !hasFaxNumber();
	document.getElementById("fax_button").disabled = disabled;
	document.getElementById("fax_button2").disabled = disabled;
}
</script>

<%=WebUtils.popErrorMessagesAsAlert(session)%>

<body topmargin="0" leftmargin="0" vlink="#0000FF" 
	onload="window.focus();disableDateFields();disableEditing();showSignatureImage();">
<html:errors />
<html:form styleId="consultationRequestForm" action="/oscarEncounter/RequestConsultation" onsubmit="alert('HTHT'); return false;" >
	<%
		EctConsultationFormRequestForm thisForm = (EctConsultationFormRequestForm)request.getAttribute("EctConsultationFormRequestForm");

		if (requestId != null && ! "null".equals( requestId ) && ! requestId.isEmpty() )
		{
			EctViewRequestAction.fillFormValues(LoggedInInfo.getLoggedInInfoFromSession(request), thisForm, new Integer(requestId));
                thisForm.setSiteName(consultUtil.siteName);
                defaultSiteName = consultUtil.siteName ;

		}
		else if (segmentId != null)
		{
			EctViewRequestAction.fillFormValues(thisForm, segmentId);
                thisForm.setSiteName(consultUtil.siteName);
                defaultSiteName = consultUtil.siteName ;
		}
		else if (request.getAttribute("validateError") == null)
		{
			//  new request
			if (demo != null)
			{
				oscar.oscarDemographic.data.RxInformation RxInfo = new oscar.oscarDemographic.data.RxInformation();
                EctViewRequestAction.fillFormValues(thisForm,consultUtil);
				
                if( "true".equalsIgnoreCase( props.getProperty("CONSULTATION_AUTO_INCLUDE_ALLERGIES", "true") ) ) { 
                	String allergies = RxInfo.getAllergies( loggedInInfo, demo );
					thisForm.setAllergies( allergies );
                }
                
                if( "true".equalsIgnoreCase( props.getProperty( "CONSULTATION_AUTO_INCLUDE_MEDICATIONS", "true" ) ) ) {
					if (props.getProperty("currentMedications", "").equalsIgnoreCase("otherMedications"))
					{
						oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation( loggedInInfo, demo );
						thisForm.setCurrentMedications(EctInfo.getFamilyHistory());
					}
					else
					{
						thisForm.setCurrentMedications(RxInfo.getCurrentMedication(demo));
					}
                }
                
				team = consultUtil.getProviderTeam(consultUtil.mrp);
			}

			thisForm.setStatus("1");

			thisForm.setSendTo(team);

       		if (bMultisites) {
        		thisForm.setSiteName(defaultSiteName);
       		}
		}

		if (thisForm.iseReferral())
		{
			%>
				<SCRIPT LANGUAGE="JavaScript">
					disableFields=true;
				</SCRIPT>
			<%
		}


	%>

	<% if (!props.isConsultationFaxEnabled() || !OscarProperties.getInstance().isPropertyActive("consultation_dynamic_labelling_enabled")) { %>
	<input type="hidden" name="providerNo" value="<%=providerNo%>">
	<% } %>
	<input type="hidden" name="demographicNo" value="<%=demo%>">
	<input type="hidden" name="requestId" value="<%=requestId%>">
	<input type="hidden" name="ext_appNo" value="<%=request.getParameter("appNo") %>">
	<input type="hidden" name="source" value="<%=(requestId!=null)?thisForm.getSource():request.getParameter("source") %>">
	<input type="hidden" id="saved" value="false">

	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn">Consultation</td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td class="Header"
						style="padding-left: 2px; padding-right: 2px; border-right: 2px solid #003399; text-align: left; font-size: 80%; font-weight: bold; width: 100%;"
						NOWRAP>
						<h2>
						<%=thisForm.getPatientName()%> <%=thisForm.getPatientSex()%>	<%=thisForm.getPatientAge()%>
						</h2>
						</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr style="vertical-align: top">
			<td class="MainTableLeftColumn">
			<table>
				<tr>
					<td colspan="2">
					<table>
						<tr>
							<td class="stat" colspan="2"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCreated" /></td>
						</tr>
						<tr>
							<td class="stat" colspan="2"  nowrap><%=thisForm.getProviderName()%>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgStatus" />
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="status" value="1" />
							</td>
							<td class="stat"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNoth" />:
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="status" value="2" />
							</td>
							<td class="stat"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSpecCall" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="status" value="3" />
							</td>
							<td class="stat"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPatCall" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="status" value="4" />
							</td>
							<td class="stat"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCompleted" /></td>
						</tr>
					</table>
					</td>
				</tr>

				<tr>
					<td colspan="2">
					<table id="attachedDocumentTable">
						<tr>
							<td class="tite4" >
								Attachments
							</td>
						</tr>	
							<tr><td><table id="attachedDocumentsTable">
									<tr>
										<td><h3>Documents</h3></td>
									</tr>
								<c:forEach items="${ attachedDocuments }" var="attachedDocument">
									<tr id="entry_docNo${ attachedDocument.docId }">
										<td> 
											<a target="_blank" href="${ ctx }/dms/ManageDocument.do?method=display&amp;doc_no=${ attachedDocument.docId }" alt="${ attachedDocument.description }" title="${ attachedDocument.description }">
												<c:out value="${ attachedDocument.description }" />
											</a>
											<input name="docNo" value="${ attachedDocument.docId }" id="delegate_docNo${ attachedDocument.docId }" class="delegateAttachment" type="hidden">
										</td>
									</tr>
								</c:forEach>
							</table></td></tr>
		
							<tr><td><table id="attachedLabsTable">
								<tr>
									<td><h3>Labs</h3></td>
								</tr>
								<c:forEach items="${ attachedLabs }" var="attachedLab">
									<tr id="entry_labNo${ attachedLab.segmentID }">
										<td> 
											<a target="_blank" href="${ ctx }/lab/CA/ALL/labDisplay.jsp?segmentID=${ attachedLab.segmentID }" alt="${ attachedLab.discipline }" title="${ attachedLab.discipline }">
												<c:out value="${ attachedLab.discipline }" /> 
												<c:out value="${ attachedLab.dateTime }" />
											</a>	
											<input name="labNo" value="${ attachedLab.segmentID }" id="delegate_labNo${ attachedLab.segmentID }" class="delegateAttachment" type="hidden">
										</td>
									</tr>
								</c:forEach>
							</table></td></tr>

									<c:if test="${ not EctConsultationFormRequestForm.eReferral }">
										<tr>
											<td style="text-align: right;"><a
												href="javascript:void(0);" id="attachDocumentPanelBtn"
												title="Add/Remove Attachment"
												data-poload="${ ctx }/attachDocs.do?method=fetchAll&amp;demographicNo=<%=demo%>&amp;requestId=<%=requestId%>">
													Add Attachment </a></td>
										</tr>
									</c:if>
								</table>
					</td>
				</tr>
			</table>
			</td>
			<td class="MainTableRightColumn">
			<table width="100%" height="100%">

				<!----Start new rows here-->
				<tr>
					<td class="tite4 controlPanel" colspan=2>
	
					<% if (request.getAttribute("id") != null) { %>
						<input name="update" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdate"/>" onclick="return checkForm('Update Consultation Request','EctConsultationFormRequestForm');" />
						<input name="updateAndPrint" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndPrint"/>" onclick="return checkForm('Update Consultation Request And Print Preview','EctConsultationFormRequestForm');" />
						<input name="printPreview" type="button" value="Print Preview" onclick="return checkForm('And Print Preview','EctConsultationFormRequestForm');" />
												
						<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
							<input name="updateAndSendElectronicallyTop" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndSendElectronicReferral"/>" 
								onclick="return checkForm('Update_esend','EctConsultationFormRequestForm');" />
						</logic:equal>
			
						<oscar:oscarPropertiesCheck value="yes" property="consultation_fax_enabled">
							<input id="fax_button" name="updateAndFax" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndFax"/>" onclick="return checkForm('Update And Fax','EctConsultationFormRequestForm');" />
						</oscar:oscarPropertiesCheck>

					<% } else { %>
						<input name="submitSaveOnly" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmit"/>" onclick="return checkForm('Submit Consultation Request','EctConsultationFormRequestForm'); " />
						<input name="submitAndPrint" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndPrint"/>" onclick="return checkForm('Submit Consultation Request And Print Preview','EctConsultationFormRequestForm'); " />

						<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
							<input name="submitAndSendElectronicallyTop" type="button" 
							value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndSendElectronicReferral"/>" 
							onclick="return checkForm('Submit_esend','EctConsultationFormRequestForm');" />
						</logic:equal>
				
						<oscar:oscarPropertiesCheck value="yes" property="consultation_fax_enabled">
							<input id="fax_button" name="submitAndFax" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndFax"/>" onclick="return checkForm('Submit And Fax','EctConsultationFormRequestForm');" />
						</oscar:oscarPropertiesCheck>
						<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
							<input type="button" value="Send eResponse" onclick="$('saved').value='true';document.location='<%=thisForm.getOruR01UrlString(request)%>'" />
						</logic:equal>
					<% } %>
					</td>
                    </tr>
                    <tr class="consultDemographicData" >
					<td>

					<table height="100%" width="100%">
						<% if (props.isConsultationFaxEnabled() && OscarProperties.getInstance().isPropertyActive("consultation_dynamic_labelling_enabled")) { %>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAssociated2" /></td>
							<td  class="tite3">
								<html:select property="providerNo" onchange="switchProvider(this.value)">
									<%
										for (Provider p : prList) {
											if (p.getProviderNo().compareTo("-1") != 0) {
									%>
									<option value="<%=p.getProviderNo() %>" <%=((consultUtil.providerNo != null && consultUtil.providerNo.equalsIgnoreCase(p.getProviderNo())) || (consultUtil.providerNo == null &&  providerNo.equalsIgnoreCase(p.getProviderNo())) ? "selected='selected'" : "") %>>
										<%=p.getFirstName() %> <%=p.getSurname() %>
									</option>
									<% }

								}
								%>
								</html:select>
							</td>
						</tr>
						<% } %>
						<tr>						
						<td class="tite4">
							<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formRefDate" />
						</td>
								
						<oscar:oscarPropertiesCheck value="false" property="CONSULTATION_LOCK_REFERRAL_DATE">	
	                            <td class="tite3">
	                            <img alt="calendar" id="referalDate_cal" src="../../images/cal.gif" /> 
								<%
								if (request.getAttribute("id") != null)	{
								%> 
								<html:text styleId="referalDate" property="referalDate" ondblclick="this.value='';"/>
								 <%
	 							} else {
	 							%> 
	 							<html:text styleId="referalDate" property="referalDate" ondblclick="this.value='';" value="<%=formattedDate%>"/>
	 							<%
	 							}
		 						%>
								</td>							
						</oscar:oscarPropertiesCheck>
						
						<oscar:oscarPropertiesCheck value="true" property="CONSULTATION_LOCK_REFERRAL_DATE">
						
								<td  class="tite3" >
									<html:text styleId="referalDate" property="referalDate" readonly="true" value="<%=formattedDate%>" />
								</td>
													
						</oscar:oscarPropertiesCheck>

						</tr>
						<oscar:oscarPropertiesCheck value="false" property="ENABLE_HEALTH_CARE_TEAM_IN_CONSULTATION_REQUESTS" defaultVal="false">
							<tr>
								<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formService" />
								</td>
								<td  class="tite3">
									<html:select styleId="service" property="service" onchange="fillSpecialistSelect(this);">
								</html:select>							
								</td>
							</tr>
						</oscar:oscarPropertiesCheck>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formCons" />
							</td>
							<td  class="tite3">
							<% if (thisForm.iseReferral()) { %>
							
									<%=thisForm.getProfessionalSpecialistName()%>	
																
							<% } else if( OscarProperties.getInstance().getBooleanProperty("ENABLE_HEALTH_CARE_TEAM_IN_CONSULTATION_REQUESTS", "true") ) { %>
								
									<select name="specialist" id="specialist" onchange="getSpecialist(this)" >	
										<c:forEach items="${ healthCareTeam }" var="contact" varStatus="loop">											
											<option value="${ contact.id }" ${ specialist eq contact.id ? 'selected' : ''} >
												${ contact.details.formattedName } ( ${ contact.role } )
											</option>
										</c:forEach>
									</select>
								
							<% } else { %>
									
									<span id="consult-disclaimer" title="When consult was saved this was the saved consultant but is no longer on this specialist list." style="display:none;font-size:24px;" >*</span> 
									<html:select styleId="specialist" property="specialist" size="1" onchange="onSelectSpecialist(this)" ></html:select>

							<%} // end specialist list condition block %>
							</td>
						</tr>
						<oscar:oscarPropertiesCheck value="true" property="ENABLE_HEALTH_CARE_TEAM_IN_CONSULTATION_REQUESTS" defaultVal="false">
						<tr>
							<td class="tite4">
								<input type="hidden" id="hctService" name="service" value="0" />
							</td>
							<td class="tite4" style="font-size:11px;" >
								<a href="javascript:void(0);" 
								onclick="popupPage(500,700,'${ctx}/demographic/Contact.do?method=manageContactList&contactList=HCT&view=detached&demographic_no=<%=demo%>' ); return false;" >
									edit Health Care Team
								</a>
							</td>
						</tr>
						</oscar:oscarPropertiesCheck>
                                                
                        <tr>
                            <td class="tite4">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formInstructions" />
                            </td>
                            <td  class="tite3">
                                <textarea id="annotation" style="color: blue;" rows="4" readonly></textarea>
                            </td>
                        </tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formUrgency" /></td>
							<td  class="tite3">
								<html:select property="urgency">
									<html:option value="2">
										<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNUrgent" />
									</html:option>
									<html:option value="1">
										<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgUrgent" />
									</html:option>
									<html:option value="3">
										<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgReturn" />
									</html:option>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="tite4">
								<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formPhone" />
							</td>
							<td  class="tite3"><input type="text" name="phone" class="righty" value="<%=thisForm.getProfessionalSpecialistPhone()%>" /></td>
						</tr>
						<tr>
							<td class="tite4">
								<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formFax" />
								<c:if test="${ not empty consultUtil.specialistFaxLog.status }">
									<span style="font-size:80%;color:red;" >Status: <c:out value="${ consultUtil.specialistFaxLog.status }" /></span>
								</c:if>
							</td>
							<td  class="tite3">							
								<input type="text" name="fax" class="righty" />
							</td>
						</tr>

						<tr>
							<td class="tite4">
								<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAddr" />
							</td>
							<td  class="tite3">
								<textarea name="address" rows="5" ><%=thisForm.getProfessionalSpecialistAddress()%></textarea>
							</td>
						</tr>
	
						<oscar:oscarPropertiesCheck defaultVal="false" value="true" property="CONSULTATION_APPOINTMENT_INSTRUCTIONS_LOOKUP">
							<tr>
								<td class="tite4">
									<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.appointmentInstr" />
								</td>
								<td  class="tite3">														
									<html:select property="appointmentInstructions" styleId="appointmentInstructions" >
										<html:option value="" ></html:option>
										<c:forEach items="${ appointmentInstructionList.items }" var="appointmentInstruction">
											<%-- Ensure that only active items are shown --%>
											<c:if test="${ appointmentInstruction.active }" >
												<html:option value="${ appointmentInstruction.value }" >
													<c:out value="${ appointmentInstruction.label }" />
												</html:option>
											</c:if>
										</c:forEach>								
									</html:select>
								</td>
							</tr>
						</oscar:oscarPropertiesCheck>	
						<oscar:oscarPropertiesCheck defaultVal="false" value="true" property="CONSULTATION_PATIENT_WILL_BOOK">
							<tr>
								<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formPatientBook" /></td>
								<td  class="tite3"><html:checkbox property="patientWillBook" value="1" onclick="disableDateFields()">
								</html:checkbox></td>
							</tr>
						</oscar:oscarPropertiesCheck>
	
	
						<tr>						
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnAppointmentDate" />
							</td>
                            <td  class="tite3"><img alt="calendar" id="appointmentDate_cal" src="../../images/cal.gif"> 
 							<html:text styleId="appointmentDate" property="appointmentDate" readonly="true" ondblclick="this.value='';" />
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message	key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAppointmentTime" />
							</td>
							<td  class="tite3">
							<table>
								<tr>
									<td><html:select property="appointmentHour">
										<html:option value="-1"></html:option>
										<%
											for (int i = 1; i < 13; i = i + 1)
														{
															String hourOfday = Integer.toString(i);
										%>
										<html:option value="<%=hourOfday%>"><%=hourOfday%></html:option>
										<%
											}
										%>
									</html:select></td>
									<td><html:select property="appointmentMinute">
										<html:option value="-1"></html:option>
										<%
											for (int i = 0; i < 60; i = i + 1)
														{
															String minuteOfhour = Integer.toString(i);
															if (i < 10)
															{
																minuteOfhour = "0" + minuteOfhour;
															}
										%>
										<html:option value="<%=String.valueOf(i)%>"><%=minuteOfhour%></html:option>
										<%
											}
										%>
									</html:select></td>
									<td><html:select property="appointmentPm">
										<html:option value="AM">AM</html:option>
										<html:option value="PM">PM</html:option>
									</html:select></td>					</tr>
							</table>
			
							</td>
						</tr>
						<%if (bMultisites) { %>
						<tr>
							<td  class="tite4">
								<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.siteName" />
							</td>
							<td>
								<html:select property="siteName" onchange='this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor'>
						            <%  for (int i =0; i < vecAddressName.size();i++){
						                 String te = vecAddressName.get(i);
						                 String bg = bgColor.get(i);
						                 if (te.equals(defaultSiteName))
						                	 defaultSiteId = siteIds.get(i);
						            %>
						                    <html:option value="<%=te%>" style='<%="background-color: "+bg%>'> <%=te%> </html:option>
						            <%  }%>
							</html:select>
							</td>
						</tr>
						<%} %>
					</table>
					</td>
					<td valign="top">
					<table height="100%" width="100%" bgcolor="white">
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPatient" />
							</td>
                                                        <td class="tite1"><a href="javascript:void();" onClick="popupAttach(600,900,'<%=request.getContextPath()%>/demographic/demographiccontrol.jsp?demographic_no=<%=demo%>&displaymode=edit&dboperation=search_detail')"><%=thisForm.getPatientName()%></a></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgAddress" />
							</td>
							<td class="tite1"><%=thisForm.getPatientAddress().replace("null", "")%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPhone" />
							</td>
							<td class="tite1"><%=thisForm.getPatientPhone()%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgWPhone" />
							</td>
							<td class="tite1"><%=thisForm.getPatientWPhone()%></td>
						</tr>
												<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCellPhone" />
							</td>
							<td class="tite1"><%=thisForm.getPatientCellPhone()%></td>
						</tr>
                                                <tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgEmail" />
							</td>
							<td class="tite1"><%=thisForm.getPatientEmail()%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgBirthDate" />
							</td>
							<td class="tite1"><%=thisForm.getPatientDOB()%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSex" />
							</td>
							<td class="tite1"><%=thisForm.getPatientSex()%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgHealthCard" />
							</td>
							<td class="tite1"><%=thisForm.getPatientHealthNum()%><%=thisForm.getPatientHealthCardVersionCode()%><%=thisForm.getPatientHealthCardType()%>
							</td>
						</tr>
						<tr id="conReqSendTo">
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSendTo" />
							</td>
							<td class="tite3"><html:select property="sendTo">
								<html:option value="-1">---- <bean:message
										key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgTeams" /> ----</html:option>
								<%
									for (int i = 0; i < consultUtil.teamVec.size(); i++)
												{
													String te = (String)consultUtil.teamVec.elementAt(i);
								%>
								<html:option value="<%=te%>"><%=te%></html:option>
								<%
									}
								%>
							</html:select></td>
						</tr>

						<tr>
							<td colspan="2" class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAppointmentNotes" />
							</td>
						</tr>
						<tr>
							<td colspan="2" class="tite3"><html:textarea property="appointmentNotes"></html:textarea></td>
						</tr>
                       
						
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formLastFollowup" />
							</td>
							<td class="tite3">
	                             <img alt="calendar" id="followUpDate_cal" src="../../images/cal.gif" />
	                             <html:text styleId="followUpDate" property="followUpDate" ondblclick="this.value='';" />
                             </td>
						
						</tr>
						
						<%
							if(thisForm.getFdid() != null) {
						%>
						<tr>
							<td class="tite4">EForm
							</td>
							<td class="tite1">
								<a href="<%=request.getContextPath()%>/eform/efmshowform_data.jsp?fdid=<%=thisForm.getFdid() %>">Click to view</a>
							</td>
						</tr>
						<%
							}
						%>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="tite4 heading">Letterhead</td>
				<tr>
					<td colspan="2">
					<table  width="100%">
						<tr>
						
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.letterheadName" />
							</td>							
							<td  class="tite1">				
								<select name="letterheadName" id="letterheadName" onchange="switchProvider(this.value)">
									<option value="<%=StringEscapeUtils.escapeHtml(clinic.getClinicName())%>" <%=(consultUtil.letterheadName != null && consultUtil.letterheadName.equalsIgnoreCase(clinic.getClinicName()) ? "selected='selected'" : lhndType.equals("clinic") ? "selected='selected'" : "" )%>><%=clinic.getClinicName() %></option>
								<%
									for (Provider p : prList) {
										if (p.getProviderNo().compareTo("-1") != 0 && (p.getFirstName() != null || p.getSurname() != null)) {
								%>
								<option value="<%=p.getProviderNo() %>" 
								<%=(consultUtil.letterheadName != null && consultUtil.letterheadName.equalsIgnoreCase(p.getProviderNo()) ? "selected='selected'"  : consultUtil.letterheadName == null && p.getProviderNo().equalsIgnoreCase(providerDefault) && lhndType.equals("provider") ? "selected='selected'"  : "") %>>
									<%=p.getSurname() %>, <%=p.getFirstName().replace("Dr.", "") %>
								</option>
								<% }
								}

								if (OscarProperties.getInstance().getBooleanProperty("consultation_program_letterhead_enabled", "true")) {
								for (Program p : programList) {
								%>
									<option value="prog_<%=p.getId() %>" <%=(consultUtil.letterheadName != null && consultUtil.letterheadName.equalsIgnoreCase("prog_" + p.getId()) ? "selected='selected'"  : "") %>>
									<%=p.getName() %>
									</option>
								<% }
								}%>
								</select>
								<%if ( props.isConsultationFaxEnabled() ) {%>
									<div style="font-size:12px"><input type="checkbox" name="ext_letterheadTitle" value="Dr" <%=(consultUtil.letterheadTitle != null && consultUtil.letterheadTitle.equals("Dr") ? "checked"  : "") %>>Include Dr. with name</div>
								<%}%>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.letterheadAddress" />
							</td>
							<td  class="tite1">
								<% if (consultUtil.letterheadAddress != null) { %>
									<input type="hidden" name="letterheadAddress" id="letterheadAddress" value="<%=StringEscapeUtils.escapeHtml(consultUtil.letterheadAddress) %>" />
									<span id="letterheadAddressSpan">
										<%=consultUtil.letterheadAddress %>
									</span>
								<% } else { %>
									<input type="hidden" name="letterheadAddress" id="letterheadAddress" value="<%=StringEscapeUtils.escapeHtml(clinic.getClinicAddress()) %>  <%=StringEscapeUtils.escapeHtml(clinic.getClinicCity()) %>  <%=StringEscapeUtils.escapeHtml(clinic.getClinicProvince()) %>  <%=StringEscapeUtils.escapeHtml(clinic.getClinicPostal()) %>" />
									<span id="letterheadAddressSpan">
										<%=clinic.getClinicAddress() %><%=clinic.getClinicCity() %><%=clinic.getClinicProvince() %><%=clinic.getClinicPostal() %>
									</span>
								<% } %>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.letterheadPhone" />
							</td>
							<td  class="tite1">
								<% if (consultUtil.letterheadPhone != null) {
								%>
									<input type="hidden" name="letterheadPhone" id="letterheadPhone" value="<%=StringEscapeUtils.escapeHtml(consultUtil.letterheadPhone) %>" />
								 	<span id="letterheadPhoneSpan">
										<%=consultUtil.letterheadPhone%>
									</span>
								<% } else { %>
									<input type="hidden" name="letterheadPhone" id="letterheadPhone" value="<%=StringEscapeUtils.escapeHtml(clinic.getClinicPhone()) %>" />
									<span id="letterheadPhoneSpan">
										<%=clinic.getClinicPhone()%>
									</span>
								<% } %>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.letterheadFax" />
							</td>
							<td  class="tite1">
							   <%								
									FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
									List<FaxConfig> faxConfigs = faxConfigDao.findAll(null, null);
								%>
									<span id="letterheadFaxSpan">
										<select name="letterheadFax" id="letterheadFax">
								<%
									for( FaxConfig faxConfig : faxConfigs ) {
								%>
										<option value="<%=faxConfig.getFaxNumber()%>" <%=faxConfig.getFaxNumber().equalsIgnoreCase(consultUtil.letterheadFax) ? "selected" : ""%>><%=faxConfig.getFaxUser()%></option>								
								<%	    
									}								
								%>
									</select>
								</span>
							
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<% if (props.isConsultationFaxEnabled()) { %>
	
					<tr>
						<td colspan=2 class="tite4 heading">									
							Additional Fax Recipients
						</td>
					</tr>
					<tr>
						<td colspan=2 >
							<table style="border-collapse:collapse;" id="addFaxRecipient" width="100%">

								<tr>
									<td class="tite4">
										Name <input type="text" id="searchHealthCareTeamInput" value="" placeholder="last, first"  />
									</td>
									
									<td class="tite4">
										Fax <input type="text" id="copytoSpecialistFax" placeholder="xxx-xxx-xxxx" value="" />
									</td>
									<td class="tite4">
										<button onclick="AddOtherFaxProvider(); return false;" > Add Recipient </button>
									</td>
								</tr>									
								<c:if test="${ not empty consultUtil.copyToFaxLog }">
									<c:forEach items="${ consultUtil.copyToFaxLog }" var="faxLog">
										<tr>
											<td class="tite4"><c:out value="${ faxLog.name }" /></td>
											<td class="tite4"><c:out value="${ faxLog.fax }" /></td>
											<td class="tite4">
												<c:out value="${ faxLog.status }" />
												<c:out value="${ faxLog.sent }" />
											</td>
										</tr>
									</c:forEach>
								</c:if>
							</table>
						</td>	
					</tr>
				<% } %>
				
	
				<tr>
					<td colspan="2" class="tite4 heading"><bean:message
						key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formReason" />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<html:textarea rows="10" property="reasonForConsultation" ></html:textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table style="border-collapse: collapse;" width="100%">
						<tr>
							<td width="30%" class="tite4 heading">
								<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formClinInf" />						
							</td>
							<td id="clinicalInfoButtonBar" class="tite4 buttonBar" >
								<input id="SocHistory_clinicalInformation" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportSocHistory"/>" />
								<input id="FamHistory_clinicalInformation" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportFamHistory"/>"  />
								<input id="MedHistory_clinicalInformation" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportMedHistory"/>"  />
								<input id="Concerns_clinicalInformation" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportConcerns"/>"  />
								<input id="OMeds_clinicalInformation" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportOtherMeds"/>"  />
								<input id="Reminders_clinicalInformation" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportReminders"/>"  />								
								<input id="RiskFactors_clinicalInformation" type="button" class="btn clinicalData" value="Risk Factors" />
								<input id="fetchMedications_clinicalInformation" type="button" class="btn medicationData" value="Active Medications" />
								<input id="fetchLongTermMedications_clinicalInformation" type="button" class="btn medicationData" value="Long Term Medications" />
							</td>
						</tr>
					</table>
				</tr>
				<tr>
				<td colspan="2">
					<html:textarea rows="10" styleId="clinicalInformation" property="clinicalInformation"></html:textarea></td>
				</tr>
				<tr>
					<td colspan="2" >
					<table style="border-collapse: collapse;" width="100%">
						<tr>
							<td width="30%" class="tite4 heading">
							<%
								if (props.getProperty("significantConcurrentProblemsTitle", "").length() > 1)
										{
											out.print(props.getProperty("significantConcurrentProblemsTitle", ""));
										}
										else
										{
							%> <bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formSignificantProblems" />
							<%
 	}
 %>
							</td>
							<td id="concurrentProblemsButtonBar" class="tite4 buttonBar">
								<input id="SocHistory_concurrentProblems" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportSocHistory"/>" />
								<input id="FamHistory_concurrentProblems" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportFamHistory"/>"  />
								<input id="MedHistory_concurrentProblems" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportMedHistory"/>"  />
								<input id="Concerns_concurrentProblems" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportConcerns"/>"  />
								<input id="OMeds_concurrentProblems" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportOtherMeds"/>"  />
								<input id="Reminders_concurrentProblems" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportReminders"/>"  />	
								<input id="RiskFactors_concurrentProblems" type="button" class="btn clinicalData" value="Risk Factors" />
								<input id="fetchMedications_concurrentProblems" type="button" class="btn medicationData" value="Active Medications" />
								<input id="fetchLongTermMedications_concurrentProblems" type="button" class="btn medicationData" value="Long Term Medications" />
								
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr id="trConcurrentProblems">
					<td colspan=2>
					
					<html:textarea rows="10" styleId="concurrentProblems" property="concurrentProblems"></html:textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table style="border-collapse: collapse;" width="100%">
						<tr>
							<td width="30%" class="tite4 heading">
								<% if (props.getProperty("currentMedicationsTitle", "").length() > 1) {
									out.print( props.getProperty("currentMedicationsTitle", "") );
								}else { %> 										
									<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formCurrMedications" />
								<% }  %>
							</td>
							<td id="medsButtonBar" class="tite4 buttonBar">
								<input id="OMeds_currentMedications" type="button" class="btn clinicalData" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportOtherMeds"/>"  />								
								<input id="fetchMedications_currentMedications" type="button" class="btn medicationData" value="Active Medications" />
								<input id="fetchLongTermMedications_currentMedications" type="button" class="btn medicationData" value="Long Term Medications" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan=2>
						<html:textarea rows="10" styleId="currentMedications" property="currentMedications"></html:textarea>
					</td>
				</tr>
				<tr>
					<td colspan=2 >
						<table style="border-collapse: collapse;" width="100%">
						<tr>
							<td width="30%" class="tite4 heading">
							<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAllergies" />
							</td>
							<td class="tite4 buttonBar">
								<input id="fetchAllergies_allergies" type="button" class="btn medicationData" value="Allergies" />
							</td>
						</tr>						
						</table>
					</td>
					</tr>
				<tr>
					<td colspan=2>
						<html:textarea rows="10" styleId="allergies" property="allergies"></html:textarea></td>
				</tr>

<%
				if (props.isConsultationSignatureEnabled()) {
				%>
				<tr>
					<td colspan=2 class="tite4 heading"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formSignature" />
					</td>
				</tr>
				<tr>
					<td colspan=2>

						<input type="hidden" name="newSignature" id="newSignature" value="true" />
						<input type="hidden" name="signatureImg" id="signatureImg" value="<%=(consultUtil.signatureImg != null ? consultUtil.signatureImg : "") %>" />
						<input type="hidden" name="newSignatureImg" id="newSignatureImg" value="<%=signatureRequestId %>" />

						<div id="signatureShow" style="display: none;">
							<img id="signatureImgTag" src="" />
						</div>

						<% if (OscarProperties.getInstance().getBooleanProperty("topaz_enabled", "true")) { %>
						<input type="button" id="clickToSign" onclick="requestSignature()" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formClickToSign" />" />
						<% } else { %>
						<iframe style="width:500px; height:132px;"id="signatureFrame" src="<%= request.getContextPath() %>/signature_pad/tabletSignature.jsp?inWindow=true&<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>" ></iframe>
						<% } %>

					</td>
				</tr>
				<tr><td colspan=2 class="spacer"></td></tr>
				<% }%>

				<tr>

				<td colspan=2 class="tite4 controlPanel">
						<input type="hidden" name="submission" value="" />
						
						<%if (request.getAttribute("id") != null) {%>
						
							<input name="update" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdate"/>" 
								onclick="return checkForm('Update Consultation Request','EctConsultationFormRequestForm');" />
							<input name="updateAndPrint" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndPrint"/>" 
								onclick="return checkForm('Update Consultation Request And Print Preview','EctConsultationFormRequestForm');" />
							
							<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
								<input name="updateAndSendElectronically" type="button" 
									value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndSendElectronicReferral"/>" 
									onclick="return checkForm('Update_esend','EctConsultationFormRequestForm');" />
							</logic:equal>
							
							<oscar:oscarPropertiesCheck value="yes" property="consultation_fax_enabled">
								<input id="fax_button2" name="updateAndFax" type="button" 
									value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndFax"/>" 
									onclick="return checkForm('Update And Fax','EctConsultationFormRequestForm');" />
							</oscar:oscarPropertiesCheck>
							
						<%} else {%>
						
							<input name="submitSaveOnly" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmit"/>" 
								onclick="return checkForm('Submit Consultation Request','EctConsultationFormRequestForm'); " />
							<input name="submitAndPrint" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndPrint"/>" 
								onclick="return checkForm('Submit Consultation Request And Print Preview','EctConsultationFormRequestForm'); " />
								
							<logic:equal value="true" property="eReferral" name="EctConsultationFormRequestForm" >
								<input name="submitAndSendElectronically" type="button" 
									value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndSendElectronicReferral"/>" 
									onclick="return checkForm('Submit_esend','EctConsultationFormRequestForm');" />
							</logic:equal>
							<oscar:oscarPropertiesCheck value="yes" property="consultation_fax_enabled">
								<input id="fax_button2" name="submitAndFax" type="button" 
									value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndFax"/>" 
									onclick="return checkForm('Submit And Fax','EctConsultationFormRequestForm');" />
							</oscar:oscarPropertiesCheck>
							<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
									<input type="button" value="Send eResponse" onclick="$('saved').value='true';document.location='<%=thisForm.getOruR01UrlString(request)%>'" />
							</logic:equal>
							
						<% }%>
						
					</td>
				</tr>
				
			<script type="text/javascript">
			//<!--
				function initConsultationServices() {
					initMaster();
			        initService('<%=consultUtil.service%>','<%=((consultUtil.service==null)?"":StringEscapeUtils.escapeJavaScript(consultUtil.getServiceName(consultUtil.service.toString())))%>','<%=consultUtil.specialist%>','<%=((consultUtil.specialist==null)?"":StringEscapeUtils.escapeJavaScript(consultUtil.getSpecailistsName(consultUtil.specialist.toString())))%>','<%=StringEscapeUtils.escapeJavaScript(consultUtil.specPhone)%>','<%=StringEscapeUtils.escapeJavaScript(consultUtil.specFax)%>','<%=StringEscapeUtils.escapeJavaScript(consultUtil.specAddr)%>');
		            initSpec();
		            
		            document.EctConsultationFormRequestForm.phone.value = ("");
		        	document.EctConsultationFormRequestForm.fax.value = ("");
		        	document.EctConsultationFormRequestForm.address.value = ("");
		            <%if (request.getAttribute("id") != null)
							{%>
		                setSpec('<%=consultUtil.service%>','<%=consultUtil.specialist%>');
		                FillThreeBoxes('<%=consultUtil.specialist%>');
		            <%}
							else
							{%>
		                document.EctConsultationFormRequestForm.service.options.selectedIndex = 0;
		                document.EctConsultationFormRequestForm.specialist.options.selectedIndex = 0;
		            <%}%>
		
		            onSelectSpecialist(document.EctConsultationFormRequestForm.specialist);
		            
		            <%
		            	//new with BORN referrals. Allow form to be loaded with service and 
		            	//specialist pre-selected
		            	String reqService = request.getParameter("service");
		            	
		            	String reqSpecialist = request.getParameter("specialist");
		            	
		            	if(reqService != null && reqSpecialist != null) {
		            		ConsultationServices consultService = consultationServiceDao.findByDescription(reqService);
		            		if(consultService != null) {
		            		%>
		            		jQuery("#service").val('<%=consultService.getId()%>');
		            		fillSpecialistSelect(document.getElementById('service'));
		            		jQuery("#specialist").val('<%=reqSpecialist%>');
		            		onSelectSpecialist(document.getElementById('specialist'));
		            		<%
		            	} }
		            	
		            	String serviceId = request.getParameter("serviceId");
		            	if(serviceId != null) {
		            		%>
		            		jQuery("#service").val('<%=serviceId%>');
		            		fillSpecialistSelect(document.getElementById('service'));
		            		<%
		            	}
		            %>
				}
        //-->
        </script>
        
       	<oscar:oscarPropertiesCheck value="false" property="ENABLE_HEALTH_CARE_TEAM_IN_CONSULTATION_REQUESTS" defaultVal="false" >
			<script type="text/javascript">
			//<!--
				initConsultationServices();
			//-->
			</script>
		</oscar:oscarPropertiesCheck>
		
		<oscar:oscarPropertiesCheck value="true" property="ENABLE_HEALTH_CARE_TEAM_IN_CONSULTATION_REQUESTS" defaultVal="false" >
			<script type="text/javascript">
			//<!--
				var specialist = "${ consultUtil.specialist }";
				var service = "${ consultUtil.service }";

				document.EctConsultationFormRequestForm.specialist.value = specialist;
				if(  typeof healthCareTeam !== 'undefined' && healthCareTeam !== null ) {
					document.EctConsultationFormRequestForm.annotation.value = healthCareTeam[ specialist ].note; 
					document.EctConsultationFormRequestForm.phone.value = healthCareTeam[ specialist ].phoneNum;
					document.EctConsultationFormRequestForm.fax.value = healthCareTeam[ specialist ].specFax;					
					document.EctConsultationFormRequestForm.address.value = healthCareTeam[ specialist ].specAddress;
				}
				document.EctConsultationFormRequestForm.service.value = service
				
			//-->
			</script>
		</oscar:oscarPropertiesCheck>

				<!----End new rows here-->

				<tr height="100%">
					<td></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn"></td>
			<td class="MainTableBottomRowRightColumn"></td>
		</tr>
	</table>
<div id="attachDocumentDisplay" style="display:none;"></div>
</html:form>
</body>

<script type="text/javascript" >
jQuery(document).ready( function() {
	var ctx = "${pageContext.request.contextPath}";
	//--> Autocomplete searches
	jQuery( "#searchHealthCareTeamInput" ).autocomplete({				
		source: function( request, response ) { 
			var url = ctx + "/demographic/Contact.do?method=searchAllContacts&searchMode=search_name&orderBy=c.lastName,c.firstName";
			jQuery.ajax({
				url: url,
				type: "GET",
				dataType: "json",
				data: {
				 	term: request.term   			  
				},
				contentType: "application/json",
				success: function( data ) {
					response(jQuery.map(data, function( item ) {					
						return {
						  label: item.lastName + ", " 
						  + item.firstName + " :: "
						  + item.residencePhone
						  + " :: " + item.address 
						  + " " + item.city,
						  value: item.id,
						  contact: item
						 }						 
					}));
				}
			});
	    },
	    minLength: 2,  
	    focus: function( event, ui ) {
	    		event.preventDefault();
	        return false;
	    },
	    select: function(event, ui) { 
	    		event.preventDefault(); 	    		
	    		jQuery("#copytoSpecialistFax").val(ui.item.contact.fax);
	    		jQuery("#searchHealthCareTeamInput").val( ui.item.contact.lastName + ", " + ui.item.contact.firstName );
	    }      
	});
})
</script>


<script type="text/javascript">

Calendar.setup( { inputField : "followUpDate", ifFormat : "%Y/%m/%d", showsTime :false, trigger : "followUpDate", singleClick : true, step : 1 } );
Calendar.setup( { inputField : "appointmentDate", ifFormat : "%Y/%m/%d", showsTime :false, trigger : "appointmentDate", singleClick : true, step : 1 } );
<%if("false".equals(OscarProperties.getInstance().getProperty("CONSULTATION_LOCK_REFERRAL_DATE", "true"))) {%>
	Calendar.setup( { inputField : "referalDate", ifFormat : "%Y/%m/%d", showsTime :false, trigger : "referalDate", singleClick : true, step : 1 } );
<%}%>
jQuery(document).ready(function(){

	/**
		DOCUMENT ATTACHMENT MANAGER JAVASCRIPT		
	**/
	jQuery(document).on( 'click', '*[data-poload]', function() {
		
		var trigger = jQuery(this);
		trigger.off('click');
		var triggerId = "#" + trigger.attr('id');
		var title = trigger.attr("title");
		
		jQuery("#attachDocumentDisplay").load( trigger.data('poload') ).dialog({    		
			title: title,
			modal:false,
			closeText: "Close",
			height: 250,
			width: 'auto',
			resizable: true,
			position: { my: "left", at:"right", of: triggerId },
 			beforeClose: function(event, ui) {
 				// before the dialog is closed:

 			    // pass the checked elements to the consultation request form
 				jQuery('#attachDocumentsForm').find(".document_check:checked:not(input[disabled='disabled']), .lab_check:checked:not(input[disabled='disabled'])").each(function(index,data){
 					var element = jQuery(this);
 					var input = jQuery("<input />", {type: 'hidden', name: element.attr('name'), value: element.val(), id: "delegate_" + element.attr('id'), class: 'delegateAttachment'});
 					var row = jQuery("<tr>", {id: "entry_" + element.attr("name") + element.val()});
 					var column = jQuery("<td>");
 	 				var target = "#attachedDocumentsTable";
 	 				
 					if("lab_check".indexOf(element.attr("class")) != -1) 
 					{
 						target = "#attachedLabsTable";
 					} 

 					column.text(element.attr("title"));
 					column.append(input);
 					row.append(column);

 					jQuery('#consultationRequestForm').find(target).append(row);
 				});
			
				// remove unchecked elements from the request form.
				jQuery('#attachDocumentsForm').find(".document_pre_check:not(input[disabled='disabled']), .lab_pre_check:not(input[disabled='disabled'])").each(function(index,data){
					var checkedElement = jQuery(this);
				
					if( !checkedElement.is(':checked') ) {
						var checkedElementClass = checkedElement.attr("class");
						jQuery('#consultationRequestForm').find("#entry_" + checkedElement.attr("id")).remove();
						checkedElement.attr("class", checkedElementClass.split("_")[0] + "_check");
					}		
				});
			},
			
			// pre check all selected elements after the dialog panel fully loads.
			show: { 
				effect: "slide",
				duration: 600,
				complete: function(){
					jQuery('#consultationRequestForm').find(".delegateAttachment").each(function(index,data) {
						var delegate = "#" + this.id.split("_")[1];
						var element = jQuery('#attachDocumentsForm').find(delegate);
						var elementClassType = element.attr("class").split("_")[0];						
						element.attr("checked", true).attr("class", elementClassType + "_pre_check");				
					});
				} 
			}
		});
	
	})

})

</script>

</html:html>

<%!protected String listNotes(CaseManagementManager cmgmtMgr, String code, String providerNo, String demoNo)
	{
		// filter the notes by the checked issues
		List<Issue> issues = cmgmtMgr.getIssueInfoByCode(providerNo, code);

		String[] issueIds = new String[issues.size()];
		int idx = 0;
		for (Issue issue : issues)
		{
			issueIds[idx] = String.valueOf(issue.getId());
		}

		// need to apply issue filter
		List<CaseManagementNote> notes = cmgmtMgr.getNotes(demoNo, issueIds);
		StringBuffer noteStr = new StringBuffer();
		for (CaseManagementNote n : notes)
		{
			if (!n.isLocked() && !n.isArchived()) noteStr.append(n.getNote() + "\n");
		}

		return noteStr.toString();
}%>


