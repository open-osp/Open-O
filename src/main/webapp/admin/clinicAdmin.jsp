<%--

    Copyright (c) 2007 Peter Hutten-Czapski based on OSCAR general requirements
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

<%@ page import="java.util.*"%>
<%@ page import="oscar.oscarReport.reportByTemplate.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html lang="en">
<head>
<title>Clinic</title>

<script src="${pageContext.request.contextPath}/js/global.js"></script>
<script src="${pageContext.request.contextPath}/share/javascript/Oscar.js"></script>
<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"> <!-- Bootstrap 2.3.1 -->

</head>
<body class="BodyStyle">
<h4><bean:message key="admin.admin.clinicAdmin" /></h4></h4>
<div class="well">

<html:form	action="/admin/ManageClinic" styleClass="form-horizontal">
<html:hidden property="clinic.id" />
<html:hidden property="clinic.status" value="A" />
<html:hidden property="method" value="update" />

  <div class="control-group">
    <label class="control-label" for="clinic.clinicName"><bean:message key="admin.k2a.clinicName" /></label>
    <div class="controls">
      <html:text property="clinic.clinicName" />
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="clinic.clinicAddress"><bean:message key="admin.provider.formAddress" /></label>
    <div class="controls">
      <html:text property="clinic.clinicAddress" />
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="clinic.clinicCity"><bean:message key="oscarReport.oscarReportCatchment.msgCity" /></label>
    <div class="controls">
      <html:text property="clinic.clinicCity" />
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="clinic.clinicPostal"><bean:message key="oscarReport.oscarReportCatchment.msgPostal" /></label>
    <div class="controls">
      <html:text property="clinic.clinicPostal" />
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="clinic.clinicPhone"><bean:message key="appointment.addappointment.msgPhone" /></label>
    <div class="controls">
      <html:text property="clinic.clinicPhone" />
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="clinic.clinicFax"><bean:message key="admin.provider.formFax" /></label>
    <div class="controls">
      <html:text property="clinic.clinicFax" />
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="clinic.clinicLocationCode" ><bean:message key="location" />&nbsp;  <bean:message key="billing.billingDigSearch.formCode" /></label>
    <div class="controls">
      <html:text property="clinic.clinicLocationCode" />
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="clinic.clinicProvince"><bean:message key="demographic.demographicaddrecordhtm.formprovince" /></label>
    <div class="controls">
      <html:text property="clinic.clinicProvince" />
    </div>
  </div>
  <div class="control-group" title="Multi phone delimited by |">
    <label class="control-label" for="clinic.clinicDelimPhone" ><bean:message key="appointment.addappointment.msgPhone" />|<bean:message key="appointment.addappointment.msgPhone" /></label>
    <div class="controls">
      <html:text property="clinic.clinicDelimPhone" />
    </div>
  </div>
  <div class="control-group" title="Multi fax delimited by |">
    <label class="control-label" for="clinic.clinicDelimFax" ><bean:message key="admin.provider.formFax" />|<bean:message key="admin.provider.formFax" /></label>
    <div class="controls">
      <html:text property="clinic.clinicDelimFax" />
    </div>
  </div>
  <div class="control-group">
    <div class="controls">
      <input type="submit" value="<bean:message key="global.btnSubmit" />" class="btn btn-primary">
    </div>
  </div>

</html:form>

</div>
</html:html>