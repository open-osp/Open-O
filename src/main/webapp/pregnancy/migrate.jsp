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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.model.Episode" %>
<%@page import="org.oscarehr.common.dao.EpisodeDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>


<html:html locale="true">
<head>
<title>Migration Tool</title>


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/share/css/OscarStandardLayout.css">
<script src="${pageContext.request.contextPath}/share/javascript/Oscar.js"></script>
<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"> <!-- Bootstrap 2.3.1 -->
<style>
div#demo
{
	margin-left: auto;
	margin-right: auto;
	width: 90%;
	text-align: left;
}
</style>
<script>
function complete() {
	window.close();
	window.opener.reloadNav('forms');
	window.opener.reloadNav('pregnancy');
	return false;
}
</script>
</head>
<body>

	<br/>
	<h2 style="text-align:center">Pregnancy Migration Tool</h2>
	<br/>
<%
	if(request.getAttribute("error") != null) {
%>
	<span style="alert alert-warning"><%=request.getAttribute("error") %></span>
<%
	return;
	}
%>

	<form action="Pregnancy.do">
		<input type="hidden" name="method" value="doPreMigrate"/>
		<input type="hidden" name="demographicNo" value="<%=request.getParameter("demographicNo")%>"/>
		<fieldset>
			<%
			if(request.getAttribute("message") == null) {
			%>
			<h3>Migrate data from ONAR2005 form to the <b>Enhanced</b> form.</h3>
			<p>
				You only run this once per patient for existing charts where an AR2005 form is in progress.<br/>

				This tool will take existing data from the latest AR2005 form, and<br/>
				copy the values over to the new enhanced form.</p>
			<p>
				Once you choose 'Perform Migration', you will get a set of mappings<br/>
				which need to be done manually. This only applies to fields that have been<br/>
				mapped to drop downs
			</p>
			<%
			if(request.getAttribute("warning")!=null) {
				%><h5 style="alert alert-warning"><%=request.getAttribute("warning") %></h5><%
			}
			%>
			<button class="btn">Perform Migration</button>

			<% } else {	%>
				<h4 style="alert alert-warning"><%=request.getAttribute("message")%></h4>
				<br/>
				<br/>
				<button onclick="return complete();" class="btn">Close Window</button>
			<% } %>
		</fieldset>
	</form>

</html:html>