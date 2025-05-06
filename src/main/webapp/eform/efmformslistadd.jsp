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
<%
//Lists forms available to add to patient
if (session.getValue("user") == null) response.sendRedirect("../logout.jsp");
String demographic_no = request.getParameter("demographic_no");
String deepColor = "#CCCCFF", weakColor = "#EEEEFF";
String country = request.getLocale().getCountry();
String parentAjaxId = request.getParameter("parentAjaxId");
String appointment = request.getParameter("appointment");

LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<%@ page import="java.util.*, java.sql.*, oscar.eform.*" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.managers.DemographicManager" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO, org.oscarehr.common.model.UserProperty" %>
<%
    String user = (String) session.getAttribute("user");
    if (session.getAttribute("userrole") == null) response.sendRedirect("../logout.jsp");
    String roleName$ = (String) session.getAttribute("userrole") + "," + user;

    String orderByRequest = request.getParameter("orderby");
    String orderBy = "";
    if (orderByRequest == null) {
        orderBy = EFormUtil.NAME;
    } else if (orderByRequest.equals("form_subject")) {
        orderBy = EFormUtil.SUBJECT;
    } else if (orderByRequest.equals("form_date")) {
        orderBy = EFormUtil.DATE;
    }

    String groupView = request.getParameter("group_view");
    if (groupView == null) {
        UserPropertyDAO userPropDAO = (UserPropertyDAO) SpringUtils.getBean(UserPropertyDAO.class);
        UserProperty usrProp = userPropDAO.getProp(user, UserProperty.EFORM_FAVOURITE_GROUP);
        if (usrProp != null) {
            groupView = usrProp.getValue();
        } else {
            groupView = "";
        }
    }
%>
<%!
    DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
%>
<%
    pageContext.setAttribute("demographic", demographicManager.getDemographic(loggedInInfo, demographic_no));
%>

<%@page import="org.oscarehr.util.LoggedInInfo" %>
<html>
    <head>
        <title>
            <fmt:setBundle basename="oscarResources"/><fmt:message key="eform.myform.title"/>
        </title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/css/OscarStandardLayout.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/css/eformStyle.css">
        <script type="text/javascript" language="JavaScript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>
        <script type="text/javascript" language="JavaScript">
            function popupPage(varpage, windowname) {
                var page = "" + varpage;
                windowprops = "height=800,width=960,location=no,"
                    + "scrollbars=yes,menubars=no,status=yes,toolbars=no,resizable=yes,top=10,left=200";
                var popup = window.open(page, windowname, windowprops);
                if (popup != null) {
                    if (popup.opener == null) {
                        popup.opener = self;
                    }
                    popup.focus();
                }
            }

            function updateAjax() {
                var parentAjaxId = "<%=parentAjaxId%>";
                if (parentAjaxId != "null") {
                    window.opener.document.forms['encForm'].elements['reloadDiv'].value = parentAjaxId;
                    window.opener.updateNeeded = true;
                }

            }
        </script>
        <style>
            :root *:not(h2, .h2) {
                font-family: Arial, "Helvetica Neue", Helvetica, sans-serif !important;
                font-size: 12px;
                overscroll-behavior: none;
                -webkit-font-smoothing: antialiased;
                -moz-osx-font-smoothing: grayscale;
            }
            :root a {
                color:blue;
            }

            #heading h2 {
                display: inline-block;
            }

            #heading span {
                margin-left:50px;
            }

            div.menu-columns {
                display: flex;
                gap: 10px;
            }

            div.left-column {
                display: flex;
                flex-direction: column;
                gap: 5px;
                flex-basis: max-content;
                text-wrap: nowrap;
            }
        </style>
    </head>

    <body onunload="updateAjax()" class="BodyStyle" vlink="#0000FF">
    <!--  -->
    <table class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn" width="175">
                <fmt:setBundle basename="oscarResources"/><fmt:message key="eform.myform.msgEForm"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td>
                            <fmt:setBundle basename="oscarResources"/><fmt:message key="eform.myform.msgFormLib"/>
                        </td>
                        <td>&nbsp;

                        </td>
                        <td style="text-align:right">
                            <a
                                href="javascript:popupStart(300,400,'About.jsp')"><fmt:setBundle basename="oscarResources"/><fmt:message key="global.about"/></a>
                            | <a href="javascript:popupStart(300,400,'License.jsp')"><fmt:setBundle basename="oscarResources"/><fmt:message key="global.license"/></a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="MainTableLeftColumn" valign="top">

                <a href="<%= request.getContextPath() %>/demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&displaymode=edit&dboperation=search_detail"><fmt:setBundle basename="oscarResources"/><fmt:message key="demographic.demographiceditdemographic.btnMasterFile"/></a>

                <br>
                <a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"
                   class="current"> <fmt:setBundle basename="oscarResources"/><fmt:message key="eform.showmyform.btnAddEForm"/></a><br/>
                <a href="efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><fmt:setBundle basename="oscarResources"/><fmt:message key="eform.calldeletedformdata.btnGoToForm"/></a><br/>
                <a href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><fmt:setBundle basename="oscarResources"/><fmt:message key="eform.showmyform.btnDeleted"/></a>

                <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="w"
                                   reverse="<%=false%>">
                    <br/>
                    <a href="#"
                       onclick="javascript: return popup(600, 1200, '../administration/?show=Forms', 'manageeforms');"
                       style="color: #835921;"><fmt:setBundle basename="oscarResources"/><fmt:message key="eform.showmyform.msgManageEFrm"/></a>
                </security:oscarSec>
                <jsp:include page="efmviewgroups.jsp">
                    <jsp:param name="url" value="../eform/efmformslistadd.jsp"/>
                    <jsp:param name="groupView" value="<%=groupView%>"/>
                </jsp:include>

            </td>
            <td class="MainTableRightColumn" style="vertical-align: top">

                <table class="elements" style="margin-left: 0px; margin-right: 0px;" width="100%">
                    <tr bgcolor=<%=deepColor%>>
                        <th>
                            <a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&group_view=<%=groupView%>&parentAjaxId=<%=parentAjaxId%>"><fmt:setBundle basename="oscarResources"/><fmt:message key="eform.showmyform.btnFormName"/></a></th>
                        <th>
                            <a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&group_view=<%=groupView%>&orderby=form_subject&parentAjaxId=<%=parentAjaxId%>"><fmt:setBundle basename="oscarResources"/><fmt:message key="eform.showmyform.btnSubject"/></a></th>
                        <!--<th><a href="myform.jsp?demographic_no=<%=demographic_no%>&group_view=<%=groupView%>&orderby=file_name"><fmt:setBundle basename="oscarResources"/><fmt:message key="eform.myform.btnFile"/></a></th>-->
                        <th>
                            <a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&group_view=<%=groupView%>&orderby=form_date&parentAjaxId=<%=parentAjaxId%>"><fmt:setBundle basename="oscarResources"/><fmt:message key="eform.showmyform.formDate"/></a></th>
                        <!--<th><a href="myform.jsp?demographic_no=<%=demographic_no%>&group_view=<%=groupView%>"><fmt:setBundle basename="oscarResources"/><fmt:message key="eform.showmyform.formTime"/></a></th> -->
                    </tr>

                    <%
                        ArrayList<HashMap<String, ? extends Object>> eForms;
                        if (groupView.equals("") || groupView.equals("default")) {
                            eForms = EFormUtil.listEForms(orderBy, EFormUtil.CURRENT, roleName$);
                        } else {
                            eForms = EFormUtil.listEForms(orderBy, EFormUtil.CURRENT, groupView, roleName$);
                        }
                        if (eForms.size() > 0) {
                            for (int i = 0; i < eForms.size(); i++) {
                                HashMap<String, ? extends Object> curForm = eForms.get(i);
                    %>
                    <tr bgcolor="<%= ((i%2) == 1)?"#F2F2F2":"white"%>">
                        <td width="30%" style="padding-left: 7px">
                            <a HREF="#"
                               ONCLICK="javascript: popupPage('efmformadd_data.jsp?fid=<%=curForm.get("fid")%>&demographic_no=<%=demographic_no%>&appointment=<%=appointment%>','<%=curForm.get("fid") + "_" + demographic_no %>'); return true;"
                               TITLE='Add This eForm' OnMouseOver="window.status='Add This eForm' ; return true">
                                <%=curForm.get("formName")%>
                            </a></td>
                        <td style="padding-left: 7px"><%=curForm.get("formSubject")%>
                        </td>
                        <td nowrap align='center'><%=curForm.get("formDate")%>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="3" align="center"><fmt:setBundle basename="oscarResources"/><fmt:message key="eform.showmyform.msgNoData"/></td>
                    </tr>
                    <%}%>

                </table>
            </div>
        </div>
    </div>
    </body>
</html>
