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

<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ page import="oscar.oscarDemographic.data.DemographicData" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_msg");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<%

    //pageContext.setAttribute("pageType",""+pageType);


    if (request.getParameter("orderby") != null) {
        String orderby = request.getParameter("orderby");
        String sessionOrderby = (String) session.getAttribute("orderby");
        if (sessionOrderby != null && sessionOrderby.equals(orderby)) {
            orderby = "!" + orderby;
        }
        session.setAttribute("orderby", orderby);
    }
    String orderby = (String) session.getAttribute("orderby");

    String moreMessages = "false";
    if (request.getParameter("moreMessages") != null) {
        moreMessages = request.getParameter("moreMessages");
    }
    final int INITIAL_DISPLAY = 20;
%>

<c:if test="${empty sessionScope.msgSessionBean}">
    <c:redirect url="index.jsp"/>
</c:if>
<c:if test="${not empty sessionScope.msgSessionBean}">
    <c:set var="bean" value="${sessionScope.msgSessionBean}" scope="page"/>
    <c:if test="${bean.valid == false}">
        <c:redirect url="index.jsp"/>
    </c:if>
</c:if>
<%
    oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean) pageContext.findAttribute("bean");
    String demographic_no = "";
    if (request.getParameter("demographic_no") != null) {
        demographic_no = request.getParameter("demographic_no");
    } else {
        demographic_no = bean.getDemographic_no();
    }

    String demographic_name = "";
    if (demographic_no != null) {
        DemographicData demographic_data = new DemographicData();
        org.oscarehr.common.model.Demographic demographic = demographic_data.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographic_no);
        demographic_name = demographic.getLastName() + ", " + demographic.getFirstName();
    }

%>
<jsp:useBean id="DisplayMessagesBeanId" scope="session"
             class="oscar.oscarMessenger.pageUtil.MsgDisplayMessagesBean"/>
<% 
    if (bean == null) {
        bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean) request.getSession().getAttribute("msgSessionBean");
        if (bean == null) {
            response.sendRedirect("errorpage.jsp?message=Session expired");
            return;
        }
    }

    DisplayMessagesBeanId.setProviderNo(bean.getProviderNo());
    bean.nullAttachment();
%>
<jsp:setProperty name="DisplayMessagesBeanId" property="*"/>

<html>
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <base href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" %>">
        <link rel="stylesheet" type="text/css" href="css/encounterStyles.css">
        <title><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.title"/>
        </title>
        <link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/share/css/extractedFromPages.css"/>

        <style>
            .TopStatusBar {
                width: 100% !important;
                height: 100% !important;
            }
        </style>

        <script type="text/javascript">
            function BackToOscar() {
                if (opener.callRefreshTabAlerts) {
                    opener.callRefreshTabAlerts("oscar_new_msg");
                    setTimeout("window.close()", 100);
                } else {
                    window.close();
                }
            }

            function unlink() {
                document.forms[0].submit();
            }
        </script>
    </head>

    <body class="BodyStyle" vlink="#0000FF" onload="window.focus()">
    <!--  -->
    <table class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgMessenger"/></td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td>
                            <div class="DivContentTitle"><h2>Messages related to <%=demographic_name%>
                            </h2></div>
                        </td>
                        <td></td>
                        <td style="text-align: right">

                            <a href="javascript:void(0)"
                               onclick="javascript:popupPage(600,700,'../oscarEncounter/About.jsp')"><fmt:setBundle basename="oscarResources"/><fmt:message key="global.about"/></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn">&nbsp;</td>
            <td class="MainTableRightColumn">
                <table width="100%">
                    <tr>
                        <td>
                            <table cellspacing=3>
                                <tr>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3>
                                            <tr>
                                                <td class="messengerButtonsA"><a
                                                        href="javascript:BackToOscar()"
                                                        class="messengerButtons"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.btnExit"/></a></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>


                    <tr>
                        <td>
                            <%
                                String contextPath = request.getContextPath();
                                String strutsAction = contextPath + "/oscarMessenger/DisplayDemographicMessages.do?demographic_no=" + demographic_no; 
                            %>

                            <form action="<%=strutsAction%>" method="post">

                                <table border="0" width="80%" cellspacing="1">
                                    <tr>
                                        <th bgcolor="#DDDDFF" width="75">&nbsp;</th>
                                        <th align="left" bgcolor="#DDDDFF">
                                            <% if (moreMessages.equals("true")) {%> <a
                                                href="${pageContext.request.contextPath}/oscarMessenger/DisplayDemographicMessages.jsp?orderby=from&moreMessages=true">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgFrom"/>
                                        </a> <%} else {%> <a
                                                href="${pageContext.request.contextPath}/oscarMessenger/DisplayDemographicMessages.jsp?orderby=from&moreMessages=false">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgFrom"/>
                                        </a> <%}%>
                                        </th>
                                        <th align="left" bgcolor="#DDDDFF">
                                            <% if (moreMessages.equals("true")) {%> <a
                                                href="${pageContext.request.contextPath}/oscarMessenger/DisplayDemographicMessages.jsp?orderby=subject&moreMessages=true">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgSubject"/>
                                        </a> <%} else {%> <a
                                                href="${pageContext.request.contextPath}/oscarMessenger/DisplayDemographicMessages.jsp?orderby=subject&moreMessages=false">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgSubject"/>
                                        </a> <%}%>
                                        </th>
                                        <th align="left" bgcolor="#DDDDFF">
                                            <% if (moreMessages.equals("true")) {%> <a
                                                href="${pageContext.request.contextPath}/oscarMessenger/DisplayDemographicMessages.jsp?orderby=date&moreMessages=true">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgDate"/>
                                        </a> <%} else {%> <a
                                                href="${pageContext.request.contextPath}/oscarMessenger/DisplayDemographicMessages.jsp?orderby=date&moreMessages=false">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgDate"/>
                                        </a> <%}%>
                                        </th>
                                        <th align="left" bgcolor="#DDDDFF">
                                            <% if (moreMessages.equals("true")) {%>
                                            <a
                                                    href="${pageContext.request.contextPath}/oscarMessenger/DisplayDemographicMessages.jsp?orderby=linked&moreMessages=true">
                                                <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgLinked"/>
                                            </a>
                                            <%} else {%>
                                            <a
                                                    href="${pageContext.request.contextPath}/oscarMessenger/DisplayDemographicMessages.jsp?orderby=linked&moreMessages=false">
                                                <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgLinked"/>
                                            </a>
                                            <%}%>
                                        </th>
                                    </tr>
                                    <% //java.util.Vector theMessages = new java.util.Vector() ;
                                        java.util.Vector theMessages2 = new java.util.Vector();
                                        theMessages2 = DisplayMessagesBeanId.estDemographicInbox(orderby, demographic_no);
                                        String msgCount = Integer.toString(theMessages2.size());
                                    %>
                                    <!--   for loop Control Initiliation variabe changed to nextMessage   -->
                                    <%
                                        for (int i = 0; i < theMessages2.size(); i++) {
                                            oscar.oscarMessenger.data.MsgDisplayMessage dm;
                                            dm = (oscar.oscarMessenger.data.MsgDisplayMessage) theMessages2.get(i);
                                            String isLastMsg = "false";
                                    %>
                                    <tr>
                                        <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'
                                            width="75"><input type="checkbox" name="messageNo" value="<%=dm.getMessageId() %>"/> <%
                                            String atta = dm.getAttach();
                                            if (atta.equals("1")) {
                                        %><img src="img/clip4.jpg">
                                            <%
                                                }
                                            %> &nbsp;
                                        </td>

                                        <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'><%= dm.getSentby()  %>
                                        </td>
                                        <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'><a
                                                href="<%=request.getContextPath()%>/oscarMessenger/ViewMessage.do?from=encounter&demographic_no=<%=demographic_no%>&msgCount=<%=msgCount%>&orderBy=<%=orderby%>&messageID=<%=dm.getMessageId()%>&messagePosition=<%=dm.getMessagePosition()%>">
                                            <%=dm.getThesubject()%>
                                        </a></td>
                                        <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'><%= dm.getThedate()  %>
                                        </td>
                                        <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'>
                                            <oscar:nameage demographicNo="<%=dm.getDemographic_no()%>"></oscar:nameage>
                                        </td>
                                    </tr>
                                    <%}%>
                                </table>
                                <table width="80%">
                                    <tr>
                                        <td><input type="button" value="Unlink Messages"
                                                   onclick="javascript:unlink();"></td>
                                        <%
                                            if (moreMessages.equals("false") && theMessages2.size() >= INITIAL_DISPLAY) {
                                        %>
                                        <td width="60%"></td>
                                        <td align="left"><a
                                                href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp?moreMessages=true">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgAllMessage"/>
                                        </a></td>
                                        <%}%>
                                    </tr>
                                </table>

                            </form></td>
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
</html>
