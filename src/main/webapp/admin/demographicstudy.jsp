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
<%@ page import="org.oscarehr.common.dao.StudyDao, org.oscarehr.common.model.Study" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="org.owasp.encoder.Encode" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manage Study</title>

<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet"><!-- Bootstrap 2.3.1 -->
<link href="${pageContext.request.contextPath}/css/DT_bootstrap.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/library/DataTables-1.10.12/media/css/jquery.dataTables.min.css" rel="stylesheet" >
<link href="${pageContext.request.contextPath}/css/bootstrap-responsive.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/library/jquery/jquery-3.6.4.min.js"></script>
<script src="${pageContext.request.contextPath}/library/DataTables/datatables.min.js"></script> <!-- DataTables 1.13.4 -->
<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"> <!-- Bootstrap 2.3.1 -->

<script type="text/javascript">
var popup;
function popupStart(vheight,vwidth,varpage,windowname) {
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    popup = window.open(varpage, windowname, windowprops);
}

function changeStatus(id, value) {

	var url = "<%=request.getContextPath()%>/study/ManageStudy.do";
	var data = "studyId=" + id + "&studyStatus=" + value;
	var msg;

	if( value == 1 ) {
		msg = "Turned on Study";
	}
	else {
		msg = "Study is now turned off";
	}

	jQuery.post(url, {method: 'setStudyStatus', studyId: id, studyStatus: value },function(transport){
			alert(msg);
		}
	);

}

function reload() {
	setTimeout(function(){window.location.reload();},2000);
}


    function initiate(){
	    $('#study').DataTable({
             "order": [],
            "language": {
                        "url": "<%=request.getContextPath() %>/library/DataTables/i18n/<bean:message key="global.i18nLanguagecode"/>.json"
                    }
        });
        return;
    }
</script>

</head>
<body onload="initiate()">
<form method="post" action="">
<br>
<div class="well">
<table id="study" class="table table-striped">
    <thead>
        <tr>
	        <th>Name</th>
	        <th>Status</th>
	        <th>Add Demographic</th>
	        <th>Add Provider</th>
        </tr>
    </thead>
    <tbody>
<%
StudyDao studyDao = (StudyDao)SpringUtils.getBean(StudyDao.class);

List<Study> listStudies = studyDao.findAll();
boolean active;
for( Study study : listStudies ) {
    active = study.getCurrent1() == 1;
%>
        <tr>
	        <td><a href="#" onclick="popupStart(800, 1200, '<%= request.getContextPath() %>/admin/addStudy.jsp?studyId=<%=study.getId()%>', 'editStudy')"><%=Encode.forHtml(study.getStudyName())%></a></td>
	        <td><input type="radio" name="status_<%=study.getId()%>" <%=active ? "checked" : ""%> value="active" onclick="changeStatus('<%=study.getId()%>','1');"/>&nbsp;Active<br/>
		        <input type="radio" name="status_<%=study.getId()%>" <%=active ? "" : "checked"%> value="inactive" onclick="changeStatus('<%=study.getId()%>','0');"/>Inactive
	        </td>
	        <td><input type="button" class="btn" value="Add Demographic" onclick="window.open('<%= request.getContextPath() %>/oscarReport/ReportDemographicReport.jsp?studyId=<%=study.getId()%>')"/></td>
	        <td><input type="button" class="btn" value="Add Provider" onclick="popupStart(768, 1024, '<%= request.getContextPath() %>/admin/addProvider.jsp?studyId=<%=study.getId()%>', 'providerselect')"/></td>
        </tr>
<%
}
%>
    </tbody>
</table>
</div>
<input type="button" class="btn btn-primary" value="New Study" onclick="popupStart(450, 650, '<%= request.getContextPath() %>/admin/addStudy.jsp', 'editStudy')"/>

</form>
</body>
</html>