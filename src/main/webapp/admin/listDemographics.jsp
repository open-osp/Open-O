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
<!DOCTYPE HTML>
<%@ page import="org.oscarehr.common.dao.StudyDetailsDao, org.oscarehr.common.model.StudyDetails" %>
<%@ page import="org.oscarehr.common.model.Demographic, org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.owasp.encoder.Encode" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.reporting");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<html>
<head>
<title>Demographic Study Listing</title>


<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"> <!-- Bootstrap 2.3.1 -->
<script type="text/javascript">

var studyMembersChecked = false;
var providerMembersChecked = false;

function selectAll(type) {
	var elements = new Array();
	var check;

	if( type == "Demographic" ) {
		elements = document.getElementsByName("demographicNo");
		check = studyMembersChecked;
		studyMembersChecked = !studyMembersChecked;
	}
	else if( type == "Provider") {
		elements = document.getElementsByName("providerNo");
		check = providerMembersChecked;
		providerMembersChecked = !providerMembersChecked;
	}

	for( var idx = 0; idx < elements.length; ++idx ) {
		if( check ) {
			elements[idx].checked = false;
		}
		else {
			elements[idx].checked = true;
		}
	}

}

function checkForm(type) {

	var elements = new Array();
	var ret = false;
	if( type == "Demographic" ) {
		elements = document.getElementsByName("demographicNo");
	}
	else if( type == "Provider") {
		elements = document.getElementsByName("providerNo");
	}

	for( var idx = 0; idx < elements.length; ++idx ) {
		if( elements[idx].checked ) {
			ret = true;
			break;
		}
	}

	if( !ret ) {
		alert("Please select at least one " + type + " to remove.");
	}

	return ret;

}

</script>

</head>
<body>

<%
	String error = (String)request.getAttribute("error");
	if( error != null ) {
%>
<span class="alert alert-warning"><h3><%=error%></h3></span>
<%
	}
	else {
	    String studyId = request.getParameter("studyId");
	    if( studyId == null ) {
			studyId = (String)request.getAttribute("studyId");
	    }

	    StudyDetailsDao studyDetailsDao = (StudyDetailsDao)SpringUtils.getBean(StudyDetailsDao.class);
	    StudyDetails studyDetails = studyDetailsDao.find(Integer.parseInt(studyId));
	    Set<Demographic> setDemographics = studyDetails.getDemographics();
	    Set<Provider> setProviders = studyDetails.getProviders();
%>
<form method="post" action="../study/ManageStudy.do">
<input type="hidden" name="method" value="RemoveFromStudy">
<input type="hidden" name="studyId" value="<%=studyId%>">
<div class="well">
<h4><%=Encode.forHtml(studyDetails.getStudyName())%>&nbsp; <bean:message key="demographic.demographicexport.providers" /></h4>
<!-- do not alter the value of this submit as it informs the action of the form -->
<input type="submit" class="btn" name="submit" value="Remove Provider" title="<bean:message key="eform.groups.removeFromGroup" />" onclick="return checkForm('Provider');" >
<table id="providerTable" class="table table-striped table-condensed table-hover" style="width:500px;">
<thead>
<tr>
	<th title="<bean:message key="eform.groups.removeFromGroup" />"><bean:message key="REMOVE" />&nbsp; <input type="checkbox" onclick="selectAll('Provider')"></th>
	<th><bean:message key="report.demographicstudyreport.msgProvider" /></th>
	<th><bean:message key="admin.providersearch.formName" /></th>
</tr>
</thead>
<tbody>
<%
		for( Provider provider : setProviders ) {
%>
			<tr>
				<td><input type="checkbox" id="p_<%=provider.getProviderNo()%>" name="providerNo" value="<%=provider.getProviderNo()%>" ></td>
				<td><%=provider.getProviderNo()%></td>
				<td><%=Encode.forHtml(provider.getFormattedName())%></td>
			</tr>
<%
		}
%>
</tbody>
</table>

</div>
<div class="well">
<h4><%=studyDetails.getStudyName()%> &nbsp; <bean:message key="oscarEncounter.formIntakeHx.academicEnrollment" /></h4>
<!-- do not alter the value of this submit as it informs the action of the form -->
<input type="submit" class="btn" name="submit" value="Remove Demographic" title="<bean:message key="eform.groups.removeFromGroup" />" onclick="return checkForm('Demographic');">
<table id="demoTable"  class="table table-striped table-condensed table-hover">
<thead>
<tr>
	<th><bean:message key="REMOVE" /> &nbsp; <input type="checkbox" onclick="selectAll('Demographic')"></th>
	<th><bean:message key="oscarEncounter.search.demographicSearch.msgDemoN" /></th>
	<th><bean:message key="admin.providersearch.formName" /></th>
	<th><bean:message key="oscarEncounter.search.demographicSearch.msgDOB" /></th>
	<th><bean:message key="oscarEncounter.search.demographicSearch.msgAddr" /></th>
	<th><bean:message key="dms.incomingDocs.MRP" /></th>
</tr>
</thead>
<tbody>
<%
		for( Demographic demo : setDemographics ) {
%>
			<tr>
				<td><input type="checkbox" id="d_<%=demo.getDemographicNo()%>" name="demographicNo" value="<%=demo.getDemographicNo()%>"></td>
				<td><%=demo.getDemographicNo()%></td>
				<td><%=Encode.forHtml(demo.getDisplayName())%></td>
				<td><%=Encode.forHtml(demo.getBirthDayAsString())%></td>
				<td><%=demo.getAddress() != null ? Encode.forHtml(demo.getAddress()) : "N/A"%></td>
				<td><%=demo.getProvider() != null ? Encode.forHtml(demo.getProvider().getFormattedName()) : "N/A"%></td>
			</tr>
<%
		}
%>
</tbody>
</table>
</div>

</form>
<%}%>
</body>
</html>