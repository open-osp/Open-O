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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>"
                   objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%@ page import="java.util.*" %>
<%@ page import="ca.openosp.openo.report.reportByTemplate.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<html>
    <head>
        <title>Clinic</title>

        <script src="${pageContext.request.contextPath}/js/global.js"></script>
        <script src="${pageContext.request.contextPath}/share/javascript/Oscar.js"></script>
        <link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css">
        <!-- Bootstrap 2.3.1 -->

    </head>
    <body class="BodyStyle">
    <h4><fmt:setBundle basename="oscarResources"/><fmt:message key="admin.admin.clinicAdmin"/></h4></h4>
    <div class="well">

        <form action="<%=request.getContextPath() %>/admin/ManageClinic.do" class="form-horizontal">
            <input type="hidden" name="id" id="id"/>
            <input type="hidden" name="status" id="status" value="A"/>
            <input type="hidden" name="method" id="method" value="update"/>

            <!-- K2A clinic name field removed -->
            <div class="control-group">
                <label class="control-label" for="clinic.clinicAddress"><fmt:setBundle basename="oscarResources"/><fmt:message key="admin.provider.formAddress"/></label>
                <div class="controls">
                    <input type="text" name="clinic.clinicAddress" id="clinic.clinicAddress" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="clinic.clinicCity"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarReport.oscarReportCatchment.msgCity"/></label>
                <div class="controls">
                    <input type="text" name="clinic.clinicCity" id="clinic.clinicCity" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="clinic.clinicPostal"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarReport.oscarReportCatchment.msgPostal"/></label>
                <div class="controls">
                    <input type="text" name="clinic.clinicPostal" id="clinic.clinicPostal" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="clinic.clinicPhone"><fmt:setBundle basename="oscarResources"/><fmt:message key="appointment.addappointment.msgPhone"/></label>
                <div class="controls">
                    <input type="text" name="clinic.clinicPhone" id="clinic.clinicPhone" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="clinic.clinicFax"><fmt:setBundle basename="oscarResources"/><fmt:message key="admin.provider.formFax"/></label>
                <div class="controls">
                    <input type="text" name="clinic.clinicFax" id="clinic.clinicFax" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="clinic.clinicLocationCode"><fmt:setBundle basename="oscarResources"/><fmt:message key="location"/>&nbsp;
                    <fmt:setBundle basename="oscarResources"/><fmt:message key="billing.billingDigSearch.formCode"/></label>
                <div class="controls">
                    <input type="text" name="clinic.clinicLocationCode" id="clinic.clinicLocationCode" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="clinic.clinicProvince"><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographicaddrecordhtm.formprovince"/></label>
                <div class="controls">
                    <input type="text" name="clinic.clinicProvince" id="clinic.clinicProvince" />
                </div>
            </div>
            <div class="control-group" title="Multi phone delimited by |">
                <label class="control-label" for="clinic.clinicDelimPhone"><fmt:setBundle basename="oscarResources"/><fmt:message key="appointment.addappointment.msgPhone"/>|<fmt:setBundle basename="oscarResources"/><fmt:message key="appointment.addappointment.msgPhone"/></label>
                <div class="controls">
                    <input type="text" name="clinic.clinicDelimPhone" id="clinic.clinicDelimPhone" />
                </div>
            </div>
            <div class="control-group" title="Multi fax delimited by |">
                <label class="control-label" for="clinic.clinicDelimFax"><fmt:setBundle basename="oscarResources"/><fmt:message key="admin.provider.formFax"/>|<fmt:setBundle basename="oscarResources"/><fmt:message key="admin.provider.formFax"/></label>
                <div class="controls">
                    <input type="text" name="clinic.clinicDelimFax" id="clinic.clinicDelimFax" />
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <input type="submit" value="<fmt:setBundle basename="oscarResources"/><fmt:message key="global.btnSubmit"/>" class="btn btn-primary">
                </div>
            </div>

        </form>

    </div>
</html>