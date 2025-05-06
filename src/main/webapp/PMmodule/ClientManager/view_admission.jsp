<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ page import="org.oscarehr.common.model.Admission" %>
<%@ page import="org.oscarehr.PMmodule.model.DischargeReason" %>
<%@ page import="org.oscarehr.common.model.OscarLog" %>
<%@ page import="java.util.List" %>
<%@ include file="/taglibs.jsp" %>
<%
    Admission admission = (Admission) request.getAttribute("admission");
%>
<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <title>Admission Details</title>
        <script>
            function popupPage(vheight, vwidth, varpage) {
                var page = "" + varpage;
                windowprops = "height=" + vheight + ",width=" + vwidth + ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
                var popup = window.open(page, "apptDateHistory", windowprops);
                if (popup != null) {
                    if (popup.opener == null) {
                        popup.opener = self;
                    }
                    popup.focus();
                }
            }

        </script>
        <link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/share/css/extractedFromPages.css"/>
    <body>
    <form action="${pageContext.request.contextPath}/PMmodule/ClientManager.do" method="post">`
        <%
            String id = (String) request.getAttribute("id");
        %>
        <input type="hidden" name="id" id="id"/>

        <table width="100%" border="1" cellspacing="2" cellpadding="3">
            <tr class="b">
                <td width="20%">Client name:</td>
                <td><c:out value="${clientManagerForm.client.formattedName}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Provider name:</td>
                <td><c:out value="${clientManagerForm.provider.formattedName}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Program name:</td>
                <td><c:out value="${clientManagerForm.admission.programName}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Team name:</td>
                <td><c:out value="${clientManagerForm.admission.teamName}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Program type:</td>
                <td><c:out value="${clientManagerForm.admission.programType}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Client status:</td>
                <td><c:out value="${clientManagerForm.admission.clientStatus}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Admission status:</td>
                <td><c:out value="${clientManagerForm.admission.admissionStatus}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Admission notes:</td>
                <td><c:out value="${clientManagerForm.admission.admissionNotes}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Admission date:</td>
                <td>
                    <c:out value="${clientManagerForm.admission.admissionDate}"/>
                    <%if (request.getAttribute("admission_date_updates") != null) { %>
                    <sup><a href="javascript:void(0)"
                            onclick="popupPage(600,400,'<%=request.getContextPath()%>/PMmodule/ClientManager/showHistory.jsp?type=update_admission_date&title=Admission Date Updates&id=<%=id%>');return false;"><%=((List<OscarLog>) request.getAttribute("admission_date_updates")).size() %>
                    </a></sup>
                    <%} %>
                </td>
            </tr>
            <tr class="b">
                <td width="20%">Temporary admission?</td>
                <td><c:out value="${clientManagerForm.admission.temporaryAdmission}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Discharge date:</td>
                <td>
                    <c:out value="${clientManagerForm.admission.dischargeDate}"/>
                    <%if (request.getAttribute("discharge_date_updates") != null) { %>
                    <sup><a href="javascript:void(0)"
                            onclick="popupPage(600,400,'<%=request.getContextPath()%>/PMmodule/ClientManager/showHistory.jsp?type=update_discharge_date&title=Discharge Date Updates&id=<%=id%>');return false;"><%=((List<OscarLog>) request.getAttribute("discharge_date_updates")).size() %>
                    </a></sup>
                    <%} %>

                </td>
            </tr>
            <tr class="b">
                <td width="20%">Discharge reason:</td>
                <td>
                    <%

                        String dischargeReason = admission.getRadioDischargeReason();
                        if (dischargeReason == null || dischargeReason == "" || "".equals(dischargeReason) || "NULL".equals(dischargeReason))
                            dischargeReason = "0";
                        DischargeReason reason = DischargeReason.values()[Integer.valueOf(dischargeReason)];
                    %> <fmt:message bundle="${pmm}" key='<%="discharge.reason." + reason.toString()%>'/>
                </td>
            </tr>
            <tr class="b">
                <td width="20%">Discharge notes:</td>
                <td><c:out value="${clientManagerForm.admission.dischargeNotes}"/></td>
            </tr>
        </table>

    </form>
    </body>
</html>
