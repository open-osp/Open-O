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
<!DOCTYPE html>
<%
    //Lists forms available to add to patient
    if (session.getValue("user") == null) {
        response.sendRedirect("../logout.jsp");
    }
    String demographic_no = request.getParameter("demographic_no");
    String country = request.getLocale().getCountry();
    String parentAjaxId = request.getParameter("parentAjaxId");
    String appointment = request.getParameter("appointment");

    LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<%@ page import="java.util.*, oscar.eform.*" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO, org.oscarehr.common.model.UserProperty" %>
<%
    String user = (String) session.getAttribute("user");
    if (session.getAttribute("userrole") == null) {
        response.sendRedirect("../logout.jsp");
    }
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
        UserPropertyDAO userPropDAO = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
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
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="org.oscarehr.managers.DemographicManager" %>
<html:html>
    <head>
        <title>
            <bean:message key="eform.myform.title"/>
        </title>

        <link href="${pageContext.request.contextPath}/library/bootstrap/3.0.0/css/bootstrap.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/library/DataTables/datatables.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/library/jquery/jquery-ui-1.12.1.min.css" rel="stylesheet">


        <script src="${pageContext.request.contextPath}/js/global.js"></script>
        <script src="${pageContext.request.contextPath}/library/jquery/jquery-3.6.4.min.js"></script>
        <script src="${pageContext.request.contextPath}/library/bootstrap/3.0.0/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/library/DataTables/datatables.min.js"></script>

        <script src="${pageContext.request.contextPath}/share/javascript/Oscar.js"></script>

        <script>
			function popupPage(varpage, windowname) {
				var page = "" + varpage;
				windowprops = "height=700,width=800,location=no,"
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

			$(document).ready(function () {

				var table = jQuery('#efmTable').DataTable({
					"pageLength": 15,
					"lengthMenu": [[15, 30, 60, 120, -1], [15, 30, 60, 120, '<bean:message key="demographic.search.All"/>']],
					"order": [[0,'asc']],
					"language": {
						"url": "<%=request.getContextPath() %>/library/DataTables/i18n/<bean:message key="global.i18nLanguagecode"/>.json"
					}
				});

			});
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

    <body onunload="updateAjax()">

    <div class="container">
        <div id="heading">
        <h2>
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-earmark-plus" viewBox="0 0 16 16">
                <path d="M8 6.5a.5.5 0 0 1 .5.5v1.5H10a.5.5 0 0 1 0 1H8.5V11a.5.5 0 0 1-1 0V9.5H6a.5.5 0 0 1 0-1h1.5V7a.5.5 0 0 1 .5-.5"></path>
                <path d="M14 4.5V14a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2h5.5zm-3 0A1.5 1.5 0 0 1 9.5 3V1H4a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V4.5z"></path>
            </svg>
            Add eForm
        </h2> <span><c:out value="${demographic.displayName}" /></span>
        </div>
        <div class="menu-columns">

            <div class="left-column">

                <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&displaymode=edit&dboperation=search_detail"><bean:message
                        key="demographic.demographiceditdemographic.btnMasterFile"/></a>
                <a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"
                   class="current"> <bean:message key="eform.showmyform.btnAddEForm"/></a>
                <jsp:include page="efmviewgroups.jsp">
                    <jsp:param name="url" value="../eform/efmformslistadd.jsp"/>
                    <jsp:param name="groupView" value="<%=groupView%>"/>
                </jsp:include>

                <a href="efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message
                        key="eform.calldeletedformdata.btnGoToForm"/></a>
                <a href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message
                        key="eform.showmyform.btnDeleted"/></a>

                <security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="w"
                                   reverse="<%=false%>">
                    <a href="#"
                       onclick="return popup(600, 1200, '../administration/?show=Forms', 'manageeforms');"
                       style="color: #835921;"><bean:message key="eform.showmyform.msgManageEFrm"/></a>
                </security:oscarSec>

            </div>
            <div class="right-column">

                <table id="efmTable" class="table table-striped table-compact dataTable no-footer">
                    <thead>
                    <tr>
                        <th><bean:message key="eform.showmyform.btnFormName"/></th>
                        <th><bean:message key="eform.showmyform.btnSubject"/></th>
                        <th><bean:message key="eform.showmyform.formDate"/></th>
                    </tr>
                    </thead>
                    <tbody>
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
                    <tr>
                        <td>
                            <a HREF="#"
                               ONCLICK="popupPage('efmformadd_data.jsp?fid=<%=curForm.get("fid")%>&demographic_no=<%=demographic_no%>&appointment=<%=appointment%>','<%=curForm.get("fid") + "_" + demographic_no %>'); return true;"
                               TITLE='Add This eForm' OnMouseOver="window.status='Add This eForm' ; return true">
                                <%= Encode.forHtmlContent((String) curForm.get("formName")) %>
                            </a></td>
                        <td><%=Encode.forHtmlContent((String) curForm.get("formSubject"))%>
                        </td>
                        <td><%=curForm.get("formDate")%>
                        </td>
                    </tr>
                    <%
                            }
                        } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    </body>
</html:html>