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
<!DOCTYPE html>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page import="org.oscarehr.managers.DemographicManager" %>
<%@ page import="oscar.oscarEncounter.data.*"%>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarProvider.data.ProviderData" %>
<%@ page import="org.oscarehr.common.dao.ConsultationRequestExtDao" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.owasp.encoder.Encode" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="org.owasp.encoder.Encode" %>
<%@page
	import="oscar.oscarEncounter.pageUtil.*,oscar.oscarEncounter.data.*"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<%
String demo = request.getParameter("de");
String proNo = (String) session.getAttribute("user");
DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
Demographic demographic=null;

ProviderData pdata = new ProviderData(proNo);
String team = pdata.getTeam();

if (demo != null ){
    demographic = demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demo);
}
else
    response.sendRedirect("../error.jsp");

oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil consultUtil;
consultUtil = new  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil();
consultUtil.estPatient(LoggedInInfo.getLoggedInInfoFromSession(request), demo);

oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewConsultationRequestsUtil theRequests;
theRequests = new  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewConsultationRequestsUtil();
theRequests.estConsultationVecByDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demo);
%>

<html:html lang="en">
<head>
<title><bean:message
	key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.title" />
</title>
<html:base />

<!--META HTTP-EQUIV="Refresh" CONTENT="20;"-->

<!-- jquery -->
	<script src="${pageContext.request.contextPath}/library/jquery/jquery-3.6.4.min.js"></script>
	<script src="${pageContext.request.contextPath}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/library/DataTables/datatables.min.js"></script><!-- 1.13.4 -->

<!-- css -->
	<link href="${pageContext.request.contextPath}/library/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet"><!-- Bootstrap 2.3.1 -->
	<link href="${pageContext.request.contextPath}/library/bootstrap/3.0.0/assets/css/DT_bootstrap.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" rel="stylesheet">

	<style>
		.MainTable .dataTables_length label {
			display: flex;
			align-items: center;
			gap: 5px;
		}
		.MainTable .dataTables_length select {
			display: inline-block;
			width: auto;
		}

		.MainTable .dataTables_filter label {
			display: flex;
			align-items: center;
			gap: 5px;
		}
		.MainTable .dataTables_filter input {
			display: inline-block;
			width: auto;
		}

		.MainTable tbody > tr > td {
			border-top: none;
		}

		tr.MainTableTopRow  td {
			padding: 0 8px 0 8px !important;
		}
	</style>

<script>
	jQuery(document).ready( function () {
	    jQuery('#consultTable').DataTable({
			"lengthMenu": [ [25, 50, 100, -1], [25, 50, 100, "<bean:message key="oscarEncounter.LeftNavBar.AllLabs"/>"] ],
			"order": [[7,'desc']],
			"language": {
				"url": "<%=request.getContextPath() %>/library/DataTables/i18n/<bean:message key="global.i18nLanguagecode"/>.json"
			},
			"initComplete": function () {
				// Add Bootstrap classes to dropdown and search input
				jQuery('.dataTables_length select').addClass('form-control input');
				jQuery('.dataTables_filter input').addClass('form-control input');
			}
		});
	});

	function BackToOscar(){
		window.close();
	}

	function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
		var page = varpage;
		windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
		var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgConsReq"/>", windowprops);
	}

	function popupOscarConS(vheight,vwidth,varpage) { //open a new popup window
		var page = varpage;
		windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
		var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultChoice.oscarConS"/>", windowprops);
		window.close();
	}
</script>

</head>
<body onload="window.focus()">

<table class="MainTable table" id="scrollNumber1">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" style="text-align: left;"><h4><bean:message key="global.con"/></h4></td>
		<td class="MainTableTopRowRightColumn">
		<table class="table">
			<tr>
				<td class="Header" style="white-space:nowrap"><h4><bean:message
					key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgConsReqFor" />
					<%= Encode.forHtml(demographic.getLastName()) %>, 
					<%= Encode.forHtml(demographic.getFirstName()) %> 
					<%= Encode.forHtml(demographic.getSex()) %> 
					<%= Encode.forHtml(demographic.getAge()) %>
				</td>
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr style="vertical-align: top">
		<td class="MainTableLeftColumn">
		<table class="table">
			<tr>
				<td style="white-space:nowrap">
					<a href="javascript:popupOscarRx(700,960,'ConsultationFormRequest.jsp?de=<%= Encode.forUriComponent(demo) %>&teamVar=<%= Encode.forUriComponent(team) %>')">
						<bean:message
							key="oscarEncounter.oscarConsultationRequest.ConsultChoice.btnNewCon" />
					</a>
				</td>
			</tr>
		</table>
		</td>
		<td class="MainTableRightColumn">
		<table class="table">
			<tr>
				<td><bean:message
					key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgClickLink" />
				</td>
			</tr>
			<tr>
				<td>

				<table id="consultTable" class="table">
					<thead>
					<tr>
						<th ><bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgStatus" />
						</th>
						<th ><bean:message
							key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formUrgency" />
						</th>
						<th ><bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgPat" />
						</th>
						<th ><bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgMRP" />
						</th>
						<th ><bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgProvider" />
						</th>
						<th ><bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgService" />
						</th>
						<th ><bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgSpecialist" />
						</th>
						<th ><bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgRefDate" />
						</th>
					</tr>
					</thead>
					<tbody>
					<%
					for (int i = 0; i < theRequests.ids.size(); i++){
						String id       = (String) theRequests.ids.get(i);
						String status   = (String) theRequests.status.get(i);
						String patient  = (String) theRequests.patient.get(i);
						String provider = (String) theRequests.provider.get(i);
						String service  = (String) theRequests.service.get(i);
						String specialist = (String) theRequests.vSpecialist.get(i);
						String date     = (String) theRequests.date.get(i);
						String urgency  = (String) theRequests.urgency.get(i);
						Provider cProv  = (Provider) theRequests.consultProvider.get(i);
					%>
					<tr>
						<td class="stat<%= Encode.forHtmlAttribute(status) %>" >
						<% if (status.equals("1")){ %> <bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgNothingDone" />
						<% }else if(status.equals("2")) { %> <bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgSpecialistCall" />
						<% }else if(status.equals("3")) { %> <bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgPatCall" />
						<% }else if(status.equals("4")) { %> <bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgAppMade" />
						<%-- For ocean referrals --%>
						<% }else if(status.equals("5")) { %> <bean:message
							key="oscarEncounter.oscarConsultationRequest.DisplayDemographicConsultationRequests.msgBookCon" />
						<% } %>
						</td>
						<td class="stat<%=status%>" >
						<% if (urgency.equals("1")){ %> <bean:message
							key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgUrgent" />
						<% }else if(urgency.equals("2")) { %> <bean:message
							key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNUrgent" />
						<% }else if(urgency.equals("3")) { %> <bean:message
							key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgReturn" />
						<% } %>
						</td>
						<td class="stat<%= Encode.forHtmlAttribute(status) %>"><a
							href="javascript:popupOscarRx(700,960,'../../oscarEncounter/ViewRequest.do?de=<%= Encode.forUriComponent(demo) %>&requestId=<%= Encode.forUriComponent(id) %>')">
						<%= Encode.forHtml(patient) %> </a></td>
						<td class="stat<%= Encode.forHtmlAttribute(status) %>"><%= Encode.forHtml(provider) %></td>
						<td class="stat<%= Encode.forHtmlAttribute(status) %>"><%= (cProv != null) ? Encode.forHtml(cProv.getFormattedName()) : "" %></td>
						<td class="stat<%= Encode.forHtmlAttribute(status) %>">
							<a href="javascript:popupOscarRx(700,960,'../../oscarEncounter/ViewRequest.do?de=<%= Encode.forUriComponent(demo) %>&requestId=<%= Encode.forUriComponent(id) %>')">
								<%= Encode.forHtml(StringUtils.trimToEmpty(service)) %>
							</a>
						</td>
						<td class="stat<%= Encode.forHtmlAttribute(status) %>">
							<%= Encode.forHtml(StringUtils.trimToEmpty(specialist)) %>
						</td>
						<td class="stat<%= Encode.forHtmlAttribute(status) %>"><%= Encode.forHtml(date) %></td>
					</tr>
					<%}%>
					</tbody>
				</table>
				</td>
			</tr>

		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
