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

<%@page contentType="text/html"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@page import="java.util.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
	"http://www.w3.org/TR/html4/loose.dtd">

<html:html>
	<head>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
		<html:base />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><bean-el:message key="${providertitle}" /></title>
		<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">

<style>
input[type="radio"]{
margin-left:20px
}
</style>
	</head>

	<body class="BodyStyle" vlink="#0000FF">
		<table class="MainTable" id="scrollNumber1" name="encounterTable">
			<tr class="MainTableTopRow">
				<td class="MainTableTopRowLeftColumn" style="width:135px">
					<bean-el:message key="${providermsgPrefs}" />
			   	</td>
				<td style="color: white" class="MainTableTopRowRightColumn"></td>
			</tr>
			<tr>
				<td class="MainTableLeftColumn"></td>
				<td class="MainTableRightColumn">
			<%if( request.getAttribute("status") == null ){%>
			<html:form action="/setTicklerPreferences.do">
				<input type="hidden" name="method" value="<c:out value="${method}"/>">
				
				<html:hidden property="taskAssigneeSelection.value" styleId="taskAssignee"/> 
			           		
				<h2>Default Tickler Task Assignee:</h2>

				<h3><c:out value="${providerMsg}"/></h3>

				<html:radio property="taskAssigneeMRP.value" styleId="taskAssigneeDefault" value="default" onchange="checkAssignee()">Default</html:radio>							
				<html:radio property="taskAssigneeMRP.value" styleId="taskAssigneeMRP" value="mrp"  onchange="checkAssignee()">MRP</html:radio>
				<html:radio property="taskAssigneeMRP.value" styleId="taskAssigneeProvider" value="provider"  onchange="checkAssignee()">Set a provider</html:radio>


				<div style="margin-top:20px;margin-bottom:20px;padding-left:20px;height:50px">
				  <div style="display:none;" id="taskAssigneeDefaultContainer">
				    <h3>No preference set.</h3>
				  </div>

				  <div style="display:none;" id="taskAssigneeMRPContainer">
				    <h3>Most Responsible Physician (MRP) as specified on the patients master record (demographics).</h3>
				  </div>

				  <div style="display:none;" id="taskAssigneeProviderContainer">
				    <h3>Select a provider from the list to set as your default assignee:</h3>
				    <br>
				    <html:select property="taskAssigneeProvider.value" onchange="updateTaskAssignee(this.value)">
					<html:options collection="providerSelect" property="value" labelProperty="label"/>
				    </html:select>
				  </div>
				</div>

				<input type="submit" value="<bean-el:message key="${providerbtnSubmit}"/>"/>
				<input type="button" value="<bean-el:message key="${providerbtnCancel}"/>" onclick="window.close();"/>
			</html:form>
			<%}else {%>
					<h1><bean-el:message key="${providerMsg}"/></h1>
					<br/><br/>
					<input type="button" value="<bean-el:message key="${providerbtnClose}"/>" onclick="window.close();"/>
			<%}%>
				</td>
			</tr>
			<tr>
				<td class="MainTableBottomRowLeftColumn"></td>
				<td class="MainTableBottomRowRightColumn"></td>
			</tr>
		</table>

<script>


function checkAssignee(){

one = document.getElementById("taskAssigneeDefault");
divDefault = document.getElementById("taskAssigneeDefaultContainer");

if(one.checked){
 divDefault.style.display="block";
 updateTaskAssignee('');//clear
}else{
 divDefault.style.display="none";
}


mrp = document.getElementById("taskAssigneeMRP");
divMRP = document.getElementById("taskAssigneeMRPContainer");

if(mrp.checked){
 divMRP.style.display="block";
 updateTaskAssignee('mrp');
}else{
 divMRP.style.display="none";
}


provider = document.getElementById("taskAssigneeProvider");
divProvider = document.getElementById("taskAssigneeProviderContainer");

if(provider.checked){
 divProvider.style.display="block";
}else{
 divProvider.style.display="none";
}

}


function updateTaskAssignee(v){
el = document.getElementById("taskAssignee");
el.value=v;
}


function updateProviderSelect(){
var savedAssignee = document.forms[0]['taskAssigneeSelection.value'].value;
if(savedAssignee.length>0 && savedAssignee!='mrp'){
document.forms[0]['taskAssigneeProvider.value'].value=savedAssignee;
}else{
 
}
}

updateProviderSelect();


checkAssignee();
</script>

	</body>
</html:html>
