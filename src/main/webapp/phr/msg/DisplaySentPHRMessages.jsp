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

<%@page import="org.oscarehr.phr.util.MyOscarUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.net.URLEncoder" %>



<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="oscar.oscarDemographic.data.DemographicData" %>
<%@ page import="oscar.oscarProvider.data.ProviderData" %>
<%@ page import="java.util.*" %>
<%
    String providerName = request.getSession().getAttribute("userfirstname") + " " +
            request.getSession().getAttribute("userlastname");
    request.setAttribute("forwardto", request.getRequestURI());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <base href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" %>">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/oscarMessenger/encounterStyles.css">
    <title>
        myOSCAR
    </title>
    <style type="text/css">
        td.messengerButtonsA {
            /*background-color: #6666ff;*/
            /*background-color: #6699cc;*/
            background-color: #003399;
        }

        td.messengerButtonsD {
            /*background-color: #84c0f4;*/
            background-color: #555599;
        }

        a.messengerButtons {
            color: #ffffff;
            font-size: 9pt;
            text-decoration: none;
        }


        table.messButtonsA {
            border-top: 2px solid #cfcfcf;
            border-left: 2px solid #cfcfcf;
            border-bottom: 2px solid #333333;
            border-right: 2px solid #333333;
        }

        table.messButtonsD {
            border-top: 2px solid #333333;
            border-left: 2px solid #333333;
            border-bottom: 2px solid #cfcfcf;
            border-right: 2px solid #cfcfcf;
        }

        .myoscarLoginElementNoAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #f3e9e9;
        }

        .myoscarLoginElementAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #d9ecd8;
        }

        .moreInfoBoxoverBody {
            border: 1px solid #9fbbe8;
            padding: 1px;
            padding-left: 3px;
            padding-right: 3px;
            border-top: 0px;
            font-size: 10px;
            background-color: white;
        }

        .moreInfoBoxoverHeader {
            border: 1px solid #9fbbe8;
            background-color: #e8ecf3;
            padding: 2px;
            padding-left: 3px;
            padding-right: 3px;
            border-bottom: 0px;
            font-size: 10px;
            color: red;
        }
    </style>

    <script type="text/javascript">
        function BackToOscar() {
            window.close();
        }

        function setFocus() {
            if (document.getElementById('phrPassword'))
                document.getElementById('phrPassword').focus();
        }
    </script>
</head>

<body class="BodyStyle" vlink="#0000FF" onload="window.focus(); setFocus();">
<!--  -->

<table class="MainTable" id="scrollNumber1" name="encounterTable">
    <tr class="MainTableTopRow">
        <td class="MainTableTopRowLeftColumn">
            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgMessenger"/>
        </td>
        <td class="MainTableTopRowRightColumn">
            <table class="TopStatusBar">
                <tr>
                    <td>
                        <div class="DivContentTitle"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgInbox"/></div>
                    </td>
                    <td>
                    </td>
                    <td style="text-align:right">
                        <a
                            href="javascript:popupStart(300,400,'About.jsp')"><fmt:setBundle basename="oscarResources"/><fmt:message key="global.about"/></a> |
                        <a href="javascript:popupStart(300,400,'License.jsp')"><fmt:setBundle basename="oscarResources"/><fmt:message key="global.license"/></a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td class="MainTableLeftColumn">
            &nbsp;
        </td>
        <td class="MainTableRightColumn">
            <table width="100%">
                <tr>
                    <td>
                        <table cellspacing=3>
                            <tr>
                                <td>
                                    <table class=messButtonsA cellspacing=0 cellpadding=3>
                                        <tr>
                                            <td class="messengerButtonsA">
                                                <a href="${pageContext.request.contextPath}//phr/PhrMessage.do?method=viewMessages"
                                                           styleClass="messengerButtons">
                                                    <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.btnRefresh"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td>
                                    <table class=messButtonsA cellspacing=0 cellpadding=3>
                                        <tr>
                                            <td class="messengerButtonsA">
                                                <a href="javascript:window.close()" class="messengerButtons">Exit
                                                    MyOscar Messenges</a>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <%
                                    if (MyOscarUtils.isMyOscarEnabled((String) session.getAttribute("user"))) {
                                %>
                                <td class="myoscarLoginElementAuth">
                                    <div>
                                        Status: <b>Logged in as <%=providerName%>
                                    </b>
                                        <form action="../../phr/Logout.do" name="phrLogout" method="POST"
                                              style="margin: 0px; padding: 0px;">
                                            <input type="hidden" name="forwardto"
                                                   value="<%=request.getServletPath()%>?method=<%=request.getParameter("method")%>">
                                            <center><a
                                                    href="javascript: document.forms['phrLogout'].submit()">Logout</a>
                                            </center>
                                        </form>
                                    </div>
                                </td>
                                <%
                                } else {
                                %>
                                <td class="myoscarLoginElementNoAuth">
                                    <div>
                                        <form action="../../phr/Login.do" name="phrLogin" method="POST"
                                              style="margin-bottom: 0px;">
                                            <%--=request.getParameter("phrUserLoginErrorMsg")%>
                                            <%=request.getAttribute("phrUserLoginErrorMsg")--%>
                                            <%
                                                request.setAttribute("phrUserLoginErrorMsg", request.getParameter("phrUserLoginErrorMsg"));
                                                request.setAttribute("phrTechLoginErrorMsg", request.getParameter("phrTechLoginErrorMsg"));
                                            %>
                                            <c:if test="${not empty phrUserLoginErrorMsg}">
                                                <div class="phrLoginErrorMsg"><font color="red">${phrUserLoginErrorMsg}.</font>
                                                <c:if test="${not empty phrTechLoginErrorMsg}">
                                                    <a href="javascript:;"
                                                       title="fade=[on] requireclick=[off] cssheader=[moreInfoBoxoverHeader] cssbody=[moreInfoBoxoverBody] singleclickstop=[on] header=[MyOSCAR Server Response:] body=[${phrTechLoginErrorMsg} </br>]">More
                                                        Info</a></div>
                                                </c:if>
                                            </c:if>
                                            Status: <b>Not logged in</b><br/>
                                            <%=providerName%> password: <input type="password" id="phrPassword"
                                                                               name="phrPassword"
                                                                               style="font-size: 8px; width: 40px;"> <a
                                                href="javascript: document.forms['phrLogin'].submit()">Login</a>
                                            <input type="hidden" name="forwardto"
                                                   value="<%=request.getServletPath()%>?method=<%=request.getParameter("method")%>">
                                        </form>
                                    </div>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                        </table><!--cell spacing=3-->
                    </td>
                </tr>
            </table><!--table width="100%">-->
        </td>
    </tr>
    <tr>
        <td class="MainTableLeftColumn">
            &nbsp;
        </td>
        <td class="MainTableRightColumn">
            <table border="0" width="80%" cellspacing="1">
                <tr>
                    <th bgcolor="#DDDDFF" width="75">
                        &nbsp;
                    </th>
                    <th align="left" bgcolor="#DDDDFF">
                        <a href="<%= request.getContextPath() %>/phr/PhrMessage?orderby=0">

                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgStatus"/>
                        </a>
                    </th>
                    <th align="left" bgcolor="#DDDDFF">
                        <a href="<%= request.getContextPath() %>/phr/PhrMessage?orderby=1">
                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgFrom"/>
                        </a>
                    </th>
                    <th align="left" bgcolor="#DDDDFF">
                        <a action="<%= request.getContextPath() %>/phr/PhrMessage?orderby=2">
                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgSubject"/>
                        </a>
                    </th>
                    <th align="left" bgcolor="#DDDDFF">
                        <a action="<%= request.getContextPath() %>/phr/PhrMessage?orderby=3">
                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgDate"/>
                        </a>
                    </th>
                </tr>
                <c:forEach var="iMessage" items="${indivoMessages}">
                    <tr>
                        <td bgcolor="#EEEEFF" width="75">

                            <c:if test="${iMessage.replied}"> --> </c:if>

                        </td>
                        <td bgcolor="#EEEEFF" width="75">
                            <c:choose>
                                <c:when test="${iMessage.read}">read</c:when>
                                <c:otherwise>new</c:otherwise>
                            </c:choose>
                        </td>
                        <td bgcolor="#EEEEFF">
                            <c:out value="${iMessage.senderPhr}"/></td>
                        <td bgcolor="#EEEEFF">

                            <a href="<%= request.getContextPath() %>/phr/PhrMessage?&method=read&id=${iMessage.id}">
                                <c:out value="${iMessage.docSubject}"/>
                            </a>

                        </td>
                        <td bgcolor="#EEEEFF">
                            <fmt:formatDate value="${iMessage.dateCreated}" type="DATE" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
    <tr>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/boxover.js"></script>
        <td class="MainTableBottomRowLeftColumn">
        </td>
        <td class="MainTableBottomRowRightColumn">
        </td>
    </tr>

</table>
</body>
</html>
