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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.util.*,oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet" %>
<%@ page import="org.oscarehr.common.model.Flowsheet" %>
<%@ page import="org.oscarehr.common.dao.FlowsheetDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<%
	String method = request.getParameter("method");
	if(method != null ) {
		if(method.equals("disable")) {
			String name = request.getParameter("name");
			MeasurementTemplateFlowSheetConfig.getInstance().disableFlowsheet(name);
		}
		if(method.equals("enable")) {
			String name = request.getParameter("name");
			MeasurementTemplateFlowSheetConfig.getInstance().enableFlowsheet(name);
		}
		response.sendRedirect("manageFlowsheets.jsp");
	}

%>
<html:html lang="en">
<head>
<script src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Manage Flowsheets</title>

<script src="<%=request.getContextPath()%>/share/javascript/prototype.js"></script>

<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/bootstrap-responsive.css"	rel="stylesheet" type="text/css">

<script src="<%=request.getContextPath() %>/library/jquery/jquery-3.6.4.min.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath() %>/library/jquery/jquery-ui.structure-1.12.1.min.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/library/jquery/jquery-ui.theme-1.12.1.min.css">
<script src="<%=request.getContextPath() %>/library/jquery/jquery-ui-1.12.1.min.js"></script>

<script src="<%=request.getContextPath()%>/js/bootstrap.js"></script>
<script	src="<%=request.getContextPath()%>/share/javascript/Oscar.js"></script>



<script>
jQuery.noConflict();
jQuery(function() {
    jQuery( document ).tooltip();
  });
</script>


</head>

<body>


			<h3>Flowsheets</h3>
			<br>
			<table class="table table-striped table-hover">
                <thead>
				<tr>
					<td><b>Name</b></td>
					<td><b>Universal</B></td>
					<td><b>Dx Triggers</B></td>
					<td><b>Program Triggers</B></td>
					<td><b>Type</b></td>
					<td><b>Enabled</b></td>
					<td><b>Actions</b></td>
				</tr>
                </thead>
                <tbody>
			<%
			Hashtable<String, String> systemFlowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetDisplayNames();
				for(String name:systemFlowsheets.keySet()) {
					String displayName = systemFlowsheets.get(name);
					MeasurementFlowSheet flowSheet = MeasurementTemplateFlowSheetConfig.getInstance().getFlowSheet(name);

					//load from db to know if it's enabled or not.
					Flowsheet fs = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetSettings().get(flowSheet.getName());
					boolean enabled=true;
					if(fs != null) {
						enabled = fs.isEnabled();
					}
					String type="System";
					if(fs!=null) {
						type = (fs.isExternal())?"System":"Custom";
					}


					if(!flowSheet.getDisplayName().equals("Health Tracker")){
					%>

						<tr>
							<td><%=flowSheet.getDisplayName()%></td>
							<td><%=flowSheet.isUniversal() %></td>
							<td><%=flowSheet.getDxTriggersString() %></td>
							<td><%=flowSheet.getProgramTriggersString() %></td>
							<td><%=type %></td>
							<td><%=enabled%></td>
							<td>
								<a href="<%=request.getContextPath()%>/oscarEncounter/oscarMeasurements/adminFlowsheet/EditFlowsheet.jsp?flowsheet=<%=flowSheet.getName()%>">Edit</a>&nbsp;
								<%if(enabled) { %>
									<a href="manageFlowsheets.jsp?method=disable&name=<%=flowSheet.getName()%>">Disable</a>
								<% } else { %>
									<a href="manageFlowsheets.jsp?method=enable&name=<%=flowSheet.getName()%>">Enable</a>
								<% } %>
							</td>
						</tr>
					<%
					}
				}
			%>
            </tbody>
			</table>

			<br><br><br>

			<form enctype="multipart/form-data" method="POST" action="<%=request.getContextPath()%>/admin/manageFlowsheetsUpload.jsp">
				<input type="file" name="flowsheet_file">
				<span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img alt="alert" src="<%=request.getContextPath()%>/images/icon_alertsml.gif"/></span>

				&nbsp;
				<input type="submit" value="Upload" class="btn btn-primary">
			</form>

</html:html>