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
	if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

	//int demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
	String demographic_no = request.getParameter("demographic_no");
	String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>

<%@ page import="java.util.*, oscar.eform.*"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<%
	String country = request.getLocale().getCountry();
	String orderByRequest = request.getParameter("orderby");
	String orderBy = "";
	if (orderByRequest == null) orderBy = EFormUtil.DATE;
	else if (orderByRequest.equals("form_subject")) orderBy = EFormUtil.SUBJECT;
	else if (orderByRequest.equals("form_name")) orderBy = EFormUtil.NAME;

	String appointment = request.getParameter("appointment");
	String parentAjaxId = request.getParameter("parentAjaxId");
%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">
	<head>
		<title><bean:message key="eform.showmyform.title" /></title>

		<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet">
		<link href="${pageContext.request.contextPath}/css/DT_bootstrap.css" rel="stylesheet">
		<link href="${pageContext.request.contextPath}/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" rel="stylesheet" >

		<script src="${pageContext.request.contextPath}/js/global.js"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
		<script src="${pageContext.request.contextPath}/library/jquery/jquery-3.6.4.min.js"></script>
		<script src="${pageContext.request.contextPath}/library/DataTables/datatables.min.js"></script><!-- 1.13.4 -->

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
				if( parentAjaxId != "null" ) {
					window.opener.document.forms['encForm'].elements['reloadDiv'].value = parentAjaxId;
					window.opener.updateNeeded = true;
				}

			}

			$(document).ready(function() {
				var shareDocumentsTarget = "../sharingcenter/documents/shareDocumentsAction.jsp";
				var table = jQuery('#efmTable').DataTable({
					"pageLength": 15,
					"lengthMenu": [ [15, 30, 60, 120, -1], [15, 30, 60, 120, '<bean:message key="demographic.search.All"/>'] ],
					"order": [2],
					"language": {
						"url": "<%=request.getContextPath() %>/library/DataTables/i18n/<bean:message key="global.i18nLanguagecode"/>.json"
					}
				});

			});
		</script>
	</head>
	<body onunload="updateAjax()" >

	<table class="MainTable" id="scrollNumber1">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn" style="width:175px;"><h4><bean:message
					key="eform.showmyform.msgMyForm" /></h4></td>
			<td class="MainTableTopRowRightColumn">
				<table class="TopStatusBar" style="width:100%">
					<tr>
						<td><bean:message key="eform.independent.btnDeleted" /></td>
						<td>&nbsp;</td>
						<td style="text-align: right"><oscar:help keywords="eform" key="app.top1"/> | <a
								href="javascript:popupStart(600,800,'About.jsp')"><bean:message
								key="global.about" /></a> | <a
								href="javascript:popupStart(600,800,'License.jsp')"><bean:message
								key="global.license" /></a></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableLeftColumn" style="vertical-align:top;">

				<a href="${ pageContext.request.contextPath }/demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&displaymode=edit&dboperation=search_detail"><bean:message key="demographic.demographiceditdemographic.btnMasterFile" /></a>

				<br>
				<a href="efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>" class="current"> <bean:message key="eform.showmyform.btnAddEForm"/></a><br>
				<a href="efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.calldeletedformdata.btnGoToForm"/></a><br>
				<a href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>&parentAjaxId=<%=parentAjaxId%>"><bean:message key="eform.showmyform.btnDeleted"/></a>
				<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.eform" rights="r" reverse="<%=false%>" >
					<br>
					<a href="#" onclick="javascript: return popup(600, 1200, '../administration/?show=Forms', 'manageeforms');" style="color: #835921;"><bean:message key="eform.showmyform.msgManageEFrm"/></a>
				</security:oscarSec>
			</td>

			<td class="MainTableRightColumn" style="width:90%; vertical-align:top">
				<table id="efmTable" style="width:100%" class="display compact nowrap">
					<thead>
					<tr>
						<th><a
								href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&orderby=form_name&parentAjaxId=<%=parentAjaxId%>"><bean:message
								key="eform.showmyform.btnFormName" /></a></th>
						<th><a
								href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&orderby=form_subject&parentAjaxId=<%=parentAjaxId%>"><bean:message
								key="eform.showmyform.btnSubject" /></a></th>
						<th><a
								href="efmpatientformlistdeleted.jsp?demographic_no=<%=demographic_no%>&parentAjaxId=<%=parentAjaxId%>"><bean:message
								key="eform.showmyform.formDate" /></a></th>
						<th><bean:message key="eform.showmyform.msgAction" /></th>
					</tr>
					</thead>
					<tbody>
					<%
						ArrayList<HashMap<String, ? extends Object>> forms = EFormUtil.listPatientEForms(orderBy, EFormUtil.DELETED, demographic_no, null);
						for (int i=0; i< forms.size(); i++) {
							HashMap<String, ? extends Object> curform = forms.get(i);
					%>
					<tr>
						<td><a href="#"
						       ONCLICK="popupPage('efmshowform_data.jsp?fdid=<%= curform.get("fdid")%>', '<%="FormPD" + i%>'); return false;"
						       TITLE="View Form"
						       onmouseover="window.status='View This Form'; return true"><%=curform.get("formName")%></a></td>
						<td><%=curform.get("formSubject")%></td>
						<td style='text-align:center'><%=curform.get("formDate")%></td>
						<td style='text-align:center'><a
								href="../eform/unRemoveEForm.do?fdid=<%=curform.get("fdid")%>&demographic_no=<%=demographic_no%>&parentAjaxId=<%=parentAjaxId%>" onClick="javascript: return confirm('Are you sure you want to restore this eform?');"><bean:message
								key="global.btnRestore" /></a></td>
					</tr>
					<%
						}
					%>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn"></td>
			<td class="MainTableBottomRowRightColumn"></td>
		</tr>
	</table>
	</body>
</html:html>