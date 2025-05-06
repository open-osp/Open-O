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

--%><%@ page import="org.oscarehr.PMmodule.model.ClientReferral" %>
<%@ page import="org.oscarehr.common.model.OscarLog" %>
<%@ page import="java.util.List" %>
<%@ include file="/taglibs.jsp" %>
<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <title>Referral Details</title>
        <script>
            function popupPage(vheight, vwidth, varpage) {
                var page = "" + varpage;
                windowprops = "height=" + vheight + ",width=" + vwidth + ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
                var popup = window.open(page, "referralDateHistory", windowprops);
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
                <td><c:out value="${clientManagerForm.referral.programName}"/></td>
            </tr>

            <tr class="b">
                <td width="20%">Provider:</td>
                <td><c:out value="${clientManagerForm.referral.providerFormattedName}"/></td>
            </tr>

            <tr class="b">
                <td width="20%">Completion date:</td>
                <td>
                    <c:out value="${clientManagerForm.referral.completionDate}"/>
                    <%if (request.getAttribute("completion_date_updates") != null) { %>
                    <sup><a href="javascript:void(0)"
                            onclick="popupPage(600,400,'<%=request.getContextPath()%>/PMmodule/ClientManager/showHistory.jsp?type=update_completion_date&title=Completion Date Updates&id=<%=id%>');return false;"><%=((List<OscarLog>) request.getAttribute("completion_date_updates")).size() %>
                    </a></sup>
                    <%} %>
                </td>
            </tr>
            <tr class="b">
                <td width="20%">Completion status:</td>
                <td><c:out value="${clientManagerForm.referral.status}"/></td>
            </tr>
            <tr class="b">
                <td width="20%">Completion notes:</td>
                <td><c:out value="${clientManagerForm.referral.completionNotes}"/></td>
            </tr>

            <tr class="b">
                <td width="20%">Referral date:</td>
                <td>
                    <c:out value="${clientManagerForm.referral.referralDate}"/>
                    <%if (request.getAttribute("referral_date_updates") != null) { %>
                    <sup><a href="javascript:void(0)"
                            onclick="popupPage(600,400,'<%=request.getContextPath()%>/PMmodule/ClientManager/showHistory.jsp?type=update_referral_date&title=Referral Date Updates&id=<%=id%>');return false;"><%=((List<OscarLog>) request.getAttribute("referral_date_updates")).size() %>
                    </a></sup>
                    <%} %>
                </td>
            </tr>

            <tr class="b">
                <td width="20%">Temporary admission:</td>
                <td><c:out value="${clientManagerForm.referral.temporaryAdmission}"/></td>
            </tr>

            <tr class="b">
                <td width="20%">Present problems:</td>
                <td><c:out value="${clientManagerForm.referral.presentProblems}"/></td>
            </tr>

            <tr class="b">
                <td width="20%">Rejection reason:</td>
                <td><c:out value="${clientManagerForm.referral.radioRejectionReason}"/></td>
            </tr>

        </table>

    </form>
    </body>
</html>
