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

<%@page contentType="text/html" %>
<%@ include file="/casemgmt/taglibs.jsp" %>
<%@page import="java.util.*" %>
<%@ page import="java.util.ResourceBundle"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
    ResourceBundle bundle = ResourceBundle.getBundle("oscarResources", request.getLocale());

    String providertitle = (String) request.getAttribute("providertitle");
    String providermsgPrefs = (String) request.getAttribute("providermsgPrefs");
    String providerbtnCancel = (String) request.getAttribute("providerbtnCancel");
    String providerbtnClose = (String) request.getAttribute("providerbtnClose");
    String providerbtnSubmit = (String) request.getAttribute("providerbtnSubmit");
    String providermsgSuccess = (String) request.getAttribute("providermsgSuccess");
%>
<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <base href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" %>">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=bundle.getString(providertitle)%></title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/oscarEncounter/encounterStyles.css">
    </head>

    <body class="BodyStyle" vlink="#0000FF">
    <table class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <%=bundle.getString(providermsgPrefs)%>
            </td>
            <td style="color: white" class="MainTableTopRowRightColumn"></td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn"></td>
            <td class="MainTableRightColumn">
                <%if (request.getAttribute("status") == null) {%>
                <form action="${pageContext.request.contextPath}/setProviderStaleDate.do" method="post">
                    <input type="hidden" name="method" value="<c:out value="${method}"/>">


                    <input type="checkbox" name="clinicalConnectDisableCloseWindow.checked" <c:if test="${clinicalConnectDisableCloseWindow.checked}">checked</c:if> />Disable Close Window (when eChart closed)

                    <br/>

                    <input type="checkbox" name="clinicalConnectDisableLogoutWarning.checked" <c:if test="${clinicalConnectDisableLogoutWarning.checked}">checked</c:if> />Disable Logout Warning

                    <br/><br/>

                    <input type="submit" value="<%=bundle.getString(providerbtnSubmit)%>"/>
                    <input type="button" value="<%=bundle.getString(providerbtnCancel)%>"
                           onclick="window.close();"/>
                </form>
                <%} else {%>
                <%=bundle.getString(providermsgSuccess)%>
                <br/><br/>
                <input type="button" value="<%=bundle.getString(providerbtnClose)%>" onclick="window.close();"/>
                <%}%>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn"></td>
            <td class="MainTableBottomRowRightColumn"></td>
        </tr>
    </table>
    </body>
</html>
