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
<!DOCTYPE HTML>
<%@ page import="org.oscarehr.common.dao.StudyDao" %>
<%@ page import="org.oscarehr.common.model.Study" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.owasp.encoder.Encode" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Edit Study</title>

<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet"><!-- Bootstrap 2.3.1 -->
<script src="${pageContext.request.contextPath}/library/jquery/jquery-3.6.4.min.js"></script>

<script type="text/javascript">
function validateForm() {
	var ret = true;
	var msg = "";
	var name = document.getElementById("studyName");
	var desc = document.getElementById("studyDescription");

	if( name.value == null || name.value == "" ) {
		msg = "Please enter a name for the study\n";
		ret = false;
	}

	if( desc.value == null || desc.value == "" ) {
		msg += "Please enter a description for the study";
		ret = false;
	}

	if( !ret ) {
		alert(msg);
	}
	else {
		window.opener.reload();
		// window.close();
	}


	return ret;
}
</script>

</head>
<body class="BODY" onload="document.forms[0].studyName.focus()">
<%
String studyId = request.getParameter("studyId");
Study study = null;

if( studyId == null ) {
    studyId = "";
}
else {
    StudyDao studyDao = (StudyDao)SpringUtils.getBean(StudyDao.class);
    study = studyDao.find(Integer.parseInt(studyId));
}

%>

<form method="post" action="../study/ManageStudy.do">
<input type="hidden" name="studyId" value="<%=studyId%>"/>
<input type="hidden" name="method" value="saveUpdateStudy"/>

    <h3>&nbsp;&nbsp;<bean:message key="admin.admin.btnStudy" /></h3>

    <div class="well">
        <div class="row">
            <div class="span4">
              <fieldset>
                <legend><bean:message key="admin.admin.btnStudy" /></legend>
                <label><bean:message key="admin.providersearch.formName" /></label>
                <input type="text" class="input-block-level" id="studyName" name="studyName" value="<%=study == null ? "" : Encode.forHtml(study.getStudyName())%>"/>
                <label><bean:message key="issueAdmin.description" /></label>
                <input type="text" class="input-block-level" id="studyDescription" name="studyDescription" value="<%=study == null || study.getDescription() == null ? "" : Encode.forHtml(study.getDescription())%>"/>
                <label><bean:message key="oscarEncounter.formlist.formName" /></label>
                <input type="text" class="input-block-level" name="studyForm" value="<%=study == null || study.getFormName() == null ? "" : Encode.forHtml(study.getFormName())%>"/>
                <label><bean:message key="provider.eRx.labelURL" /></label>
                <input type="text" class="input-block-level" name="studyRemoteURL" value="<%=study == null || study.getRemoteServerUrl() == null ? "" : Encode.forHtml(study.getRemoteServerUrl())%>"/>
                <label>Study Link</label>
                <input type="text" class="input-block-level" name="studyLink" value="<%=study == null || study.getStudyLink() == null ? "" : Encode.forHtml(study.getStudyLink())%>"/><br>
                <input type="submit" class="btn btn-primary" value="<bean:message key="global.btnSave" />" onclick="return validateForm();">
              </fieldset>
            </div> <!-- class="span4" -->
        </div> <!-- class="row" -->
    </div> <!-- class="well" -->


</form>
<%if( !studyId.equals("") ) {%>
	<jsp:include page="listDemographics.jsp"></jsp:include>
<%} %>
</body>
</html>