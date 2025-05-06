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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    int pageType = 0;
    String boxType = request.getParameter("boxType");
    if (boxType == null || boxType.equals("")) {
        pageType = 0;
    } else if (boxType.equals("1")) {
        pageType = 1;
    } else if (boxType.equals("2")) {
        pageType = 2;
    } else if (boxType.equals("3")) {
        pageType = 3;
    } else {
        pageType = 0;
    }   //messageid

    String demographic_no = request.getParameter("demographic_no");
    String demographic_name = "";
    if (demographic_no != null) {
        DemographicData demographic_data = new DemographicData();
        org.oscarehr.common.model.Demographic demographic = demographic_data.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographic_no);
        if (demographic != null) {
            demographic_name = demographic.getLastName() + ", " + demographic.getFirstName();
        }
    }


    pageContext.setAttribute("pageType", "" + pageType);

    if (request.getParameter("orderby") != null) {
        String orderby = request.getParameter("orderby");
        String sessionOrderby = (String) session.getAttribute("orderby");
        if (sessionOrderby != null && sessionOrderby.equals(orderby)) {
            orderby = "!" + orderby;
        }
        session.setAttribute("orderby", orderby);
    }
    String orderby = (String) session.getAttribute("orderby");

    int pageNum = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
%>

<c:if test="${empty msgSessionBean}">
    <c:redirect url="index.jsp"/>
</c:if>
<c:if test="${not empty msgSessionBean}">
    <c:if test="${msgSessionBean.valid == 'false'}">
        <c:redirect url="index.jsp"/>
    </c:if>
</c:if>
<%
    oscar.oscarMessenger.pageUtil.MsgSessionBean bean = (oscar.oscarMessenger.pageUtil.MsgSessionBean) session.getAttribute("msgSessionBean");
%>
<jsp:useBean id="DisplayMessagesBeanId" scope="session" class="oscar.oscarMessenger.pageUtil.MsgDisplayMessagesBean"/>
<% DisplayMessagesBeanId.setProviderNo(bean.getProviderNo());
    bean.nullAttachment();
%>
<jsp:setProperty name="DisplayMessagesBeanId" property="*"/>

<html>
    <head>
        <base href="<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/" %>">
        <link rel="stylesheet" type="text/css" href="css/encounterStyles.css">
        <title>
            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.title"/>
        </title>

        <script type="text/javascript" src="<%=request.getContextPath()%>/library/jquery/jquery-1.12.0.min.js"></script>

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

            tr.newMessage {

            }

            tr.newMessage td {
                font-weight: bold;
            }

            .TopStatusBar {
                width: 100% !important;
            }

            .integratedMessage {
                background-color: #FFCCCC;
                color: black;
            }

            .normalMessage {
                background-color: #EEEEFF;
                color: black;
            }

            span.recipientList {
                text-overflow: ellipsis;
                white-space: nowrap;
                overflow: hidden;
            }

            span.recipientList:hover {
                position: relative;
                text-overflow: clip;
                width: auto;
                white-space: normal;
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

            function uload() {
                if (opener.callRefreshTabAlerts) {
                    opener.callRefreshTabAlerts("oscar_new_msg");
                    setTimeout("window.close()", 100);
                    return false;
                }
                return true;
            }

            function checkAll(formId) {
                var f = document.getElementById(formId);
                var val = f.checkA.checked;
                for (i = 0; i < f.messageNo.length; i++) {
                    f.messageNo[i].checked = val;
                }
            }

            $(document).ready(function () {

                var lengthText = 30;
                const recipientLists = $('.recipientList');

                $.each(recipientLists, function (key, value) {
                    var text = $(value).text();
                    var shortText = $.trim(text).substring(0, lengthText).split(" ").slice(0, -1).join(" ") + "...";
                    $(value).text(shortText);
                    $(value).attr("title", $.trim(text));
                })
            })

        </script>
    </head>

    <body class="BodyStyle" vlink="#0000FF" onload="window.focus()" onunload="return uload()">
    <div id="pop-up"><p></p></div>
    <table class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <h2><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgMessenger"/></h2>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td>
                            <h2>
                                <% String inbxStyle = "messengerButtonsA";
                                    String sentStyle = "messengerButtonsA";
                                    String delStyle = "messengerButtonsA";
                                    switch (pageType) {
                                        case 0: %>
                                <div class="DivContentTitle"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgInbox"/></div>
                                <% inbxStyle = "messengerButtonsD";
                                    break;
                                    case 1: %>
                                <div class="DivContentTitle"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgSentTitle"/></div>
                                <% sentStyle = "messengerButtonsD";
                                    break;
                                    case 2: %>
                                <div class="DivContentTitle"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgArchived"/></div>
                                <% delStyle = "messengerButtonsD";
                                    break;
                                    case 3: %>
                                <div class="DivContentTitle">Messages related to <%=demographic_name%>
                                </div>
                                <% delStyle = "messengerButtonsD";
                                    break;
                                }%>
                            </h2>
                        </td>
                        <td>
                            <!-- edit 2006-0811-01 by wreby -->
                            <form action="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.do" method="post">
                                <input name="boxType" type="hidden" value="<%=pageType%>">
                                <input name="searchString" type="text" size="20"
                                       value="<jsp:getProperty name="DisplayMessagesBeanId" property="filter"/>">
                                <input name="btnSearch" type="submit"
                                       value="<fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.btnSearch"/>">
                                <input name="btnClearSearch" type="submit"
                                       value="<fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.btnClearSearch"/>">
                            </form>
                            <!-- end edit 2006-0811-01 by wreby -->
                        </td>
                        <td style="text-align:right">

                            <a href="<%=request.getContextPath()%>/oscarEncounter/About.jsp" target="_new"><fmt:setBundle basename="oscarResources"/><fmt:message key="global.about"/></a>
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
                                                    <a href="${pageContext.request.contextPath}/oscarMessenger/CreateMessage.jsp"
                                                               styleClass="messengerButtons">
                                                        <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.btnCompose"/>
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3>
                                            <tr>
                                                <td class="messengerButtonsA">
                                                    <a href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp"
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
                                                    <a href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp?boxType=1"
                                                               styleClass="messengerButtons">
                                                        <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.btnSent"/><!--sentMessage--link-->
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3>
                                            <tr>
                                                <td class="messengerButtonsA">
                                                    <a href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp?boxType=2"
                                                               styleClass="messengerButtons">
                                                        <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.btnDeletedMessage"/><!--deletedMessage--link-->
                                                    </a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table class=messButtonsA cellspacing=0 cellpadding=3>
                                            <tr>
                                                <td class="messengerButtonsA">
                                                    <a href="javascript:BackToOscar()"
                                                       class="messengerButtons"><fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.btnExit"/></a>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>

                        </td>
                    </tr>
                    <%
                        String contextPath = request.getContextPath();
                        String strutsAction = contextPath + "/oscarMessenger/DisplayMessages.do";
                        if (pageType == 2) {
                            strutsAction = contextPath + "/oscarMessenger/ReDisplayMessages.do";
                        }
                    %>

                    <form action="<%=strutsAction%>" method="post" styleId="msgList">
                    <%
                        java.util.Vector theMessages2 = new java.util.Vector();
                        switch (pageType) {
                            case 0:
                                theMessages2 = DisplayMessagesBeanId.estInbox(orderby, pageNum);
                                break;
                            case 1:
                                theMessages2 = DisplayMessagesBeanId.estSentItemsInbox(orderby, pageNum);
                                break;
                            case 2:
                                theMessages2 = DisplayMessagesBeanId.estDeletedInbox(orderby, pageNum);
                                break;
                            case 3:
                                theMessages2 = DisplayMessagesBeanId.estDemographicInbox(orderby, demographic_no);
                                break;
                        }   //messageid
                    %>

                    <tr>
                        <td>
                            <table border="0" width="90%" cellspacing="1">

                                <tr>
                                    <td colspan="6">
                                        <table style="width:100%;">
                                            <tr>
                                                <td>
                                                    <%if (pageType == 0) {%>
                                                    <input name="btnDelete" type="submit"
                                                           value="<fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.formArchive"/>">
                                                    <%} else if (pageType == 2) {%>
                                                    <input type="submit"
                                                           value="<fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.formUnarchive"/>">
                                                    <%}%>
                                                    &nbsp;
                                                </td>
                                                <td align="right">
                                                    <%
                                                        int recordsToDisplay = 25;

                                                        String previous = "";
                                                        String next = "";
                                                        String path = request.getContextPath() + "/oscarMessenger/DisplayMessages.jsp?boxType=" + pageType + "&page=";
                                                        Boolean search = false;
                                                        if (request.getParameter("searchString") != null) {
                                                            search = true;
                                                        }

                                                        if (pageType != 3) {

                                                            int totalMsgs = DisplayMessagesBeanId.getTotalMessages(pageType);

                                                            int totalPages = totalMsgs / recordsToDisplay + (totalMsgs % recordsToDisplay == 0 ? 0 : 1);

                                                            if (pageNum > 1) {
                                                                previous = "<a href='" + path + (pageNum - 1) + "' title='previous page'><< Previous</a> ";
                                                                out.print(previous);
                                                            }

                                                            if (pageNum < totalPages) {
                                                                next = "<a href='" + path + (pageNum + 1) + "' title='next page'>Next >></a>";
                                                                out.print(next);
                                                            }
                                                        }%>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <tr>
                                    <th align="left" bgcolor="#DDDDFF" width="75">
                                        <%if (pageType != 1) {%>
                                        <input type="checkbox" name="checkAll2" onclick="checkAll('msgList')"
                                               id="checkA"/>
                                        <%} %>
                                    </th>
                                    <th align="left" bgcolor="#DDDDFF">
                                        <a href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp?orderby=status"
                                                   paramId="boxType" paramName="pageType">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgStatus"/>
                                        </a>
                                    </th>
                                    <th align="left" bgcolor="#DDDDFF">
                                        <%if (pageType == 1) {%>
                                        <a href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp?orderby=sentto"
                                                   paramId="boxType" paramName="pageType">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgTo"/>
                                        </a>
                                        <%} else {%>
                                        <a href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp?orderby=from"
                                                   paramId="boxType" paramName="pageType">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgFrom"/>
                                        </a>
                                        <% } %>
                                    </th>
                                    <th align="left" bgcolor="#DDDDFF">
                                        <a href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp?orderby=subject"
                                                   paramId="boxType" paramName="pageType">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgSubject"/>
                                        </a>
                                    </th>
                                    <th align="left" bgcolor="#DDDDFF">
                                        <a href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp?orderby=date"
                                                   paramId="boxType" paramName="pageType">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgDate"/>
                                        </a>
                                    </th>
                                    <th align="left" bgcolor="#DDDDFF">
                                        <a href="${pageContext.request.contextPath}/oscarMessenger/DisplayMessages.jsp?orderby=linked"
                                                   paramId="boxType" paramName="pageType">
                                            <fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.msgLinked"/>
                                        </a>
                                    </th>
                                </tr>


                                <!--   for loop Control Initiliation variabe changed to nextMessage   -->
                                <%
                                    for (int i = 0; i < theMessages2.size(); i++) {
                                        oscar.oscarMessenger.data.MsgDisplayMessage dm;
                                        dm = (oscar.oscarMessenger.data.MsgDisplayMessage) theMessages2.get(i);
                                        String key = "oscarMessenger.DisplayMessages.msgStatus" + dm.getStatus().substring(0, 1).toUpperCase() + dm.getStatus().substring(1);
                                %>

                                <% if ("oscarMessenger.DisplayMessages.msgStatusNew".equals(key)) {%>
                                <tr class="newMessage">
                                            <%}else{%>
                                <tr>
                                    <%}%>
                                    <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'
                                        width="75">
                                        <%if (pageType != 1) {%>
                                        <input type="checkbox" name="messageNo" value="<%=dm.getMessageId() %>"/>
                                        <% } %>
                                        &nbsp;
                                        <%
                                            String atta = dm.getAttach();
                                            String pdfAtta = dm.getPdfAttach();
                                            if (atta.equals("1") || pdfAtta.equals("1")) { %>
                                        <img src="oscarMessenger/img/clip4.jpg">
                                        <% } %>


                                    </td>
                                    <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'>
                                        <fmt:setBundle basename="oscarResources"/><fmt:message key="<%= key %>"/>
                                    </td>
                                    <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'>
                                        <span class="recipientList">
                                        <%
                                            if (pageType == 1) {
                                                out.print(dm.getSentto());
                                            } else {
                                                out.print(dm.getSentby());
                                            }
                                        %>
                                    	</span>
                                    </td>
                                    <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'>
                                        <a href="<%=request.getContextPath()%>/oscarMessenger/ViewMessage.do?messageID=<%=dm.getMessageId()%>&boxType=<%=pageType%>">
                                            <%=dm.getThesubject()%>
                                        </a>

                                    </td>
                                    <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'>
                                        <%= dm.getThedate() %>
                                        &nbsp;&nbsp;
                                        <%= dm.getThetime() %>
                                    </td>
                                    <td class='<%= dm.getType() == 3 ? "integratedMessage" : "normalMessage" %>'>

                                        <%if (dm.getDemographic_no() != null && !dm.getDemographic_no().equalsIgnoreCase("null")) {%>
                                        <oscar:nameage demographicNo="<%=dm.getDemographic_no()%>"></oscar:nameage>
                                        <%} %>

                                    </td>
                                </tr>
                                <%}%>

                                <tr>
                                    <td colspan="6">
                                        <table width="100%">
                                            <tr>
                                                <td>
                                                    <%if (pageType == 0) {%>
                                                    <input name="btnDelete" type="submit"
                                                           value="<fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.formArchive"/>">
                                                    <%} else if (pageType == 2) {%>
                                                    <input type="submit"
                                                           value="<fmt:setBundle basename="oscarResources"/><fmt:message key="oscarMessenger.DisplayMessages.formUnarchive"/>">
                                                    <%}%>
                                                </td>

                                                <td align="right">
                                                    <%
                                                        if (pageType != 3) {
                                                            out.print(previous + next);
                                                        }
                                                    %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>


                            </form>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableBottomRowLeftColumn">

            </td>
            <td class="MainTableBottomRowRightColumn">

            </td>
        </tr>
    </table>
    </body>
</html>
