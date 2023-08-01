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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@ page language="java" import="oscar.OscarProperties"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%@page import="java.util.List" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@page import="org.oscarehr.casemgmt.model.CaseManagementNoteLink" %>
<%@page import="org.oscarehr.common.dao.PartialDateDao" %>
<%@page import="org.oscarehr.common.model.PartialDate" %>

<%
	String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_allergy" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_allergy");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
OscarProperties props = OscarProperties.getInstance();

	PartialDateDao partialDateDao = (PartialDateDao) SpringUtils.getBean(PartialDateDao.class);
%>

<logic:notPresent name="RxSessionBean" scope="session">
	<logic:redirect href="error.html" />
</logic:notPresent>
<logic:present name="RxSessionBean" scope="session">
	<bean:define id="bean" type="oscar.oscarRx.pageUtil.RxSessionBean" name="RxSessionBean" scope="session" />
	<logic:equal name="bean" property="valid" value="false">
		<logic:redirect href="error.html" />
	</logic:equal>
</logic:present>
<%
oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean)pageContext.findAttribute("bean");
String annotation_display = org.oscarehr.casemgmt.model.CaseManagementNoteLink.DISP_ALLERGY;

com.quatro.service.security.SecurityManager securityManager = new com.quatro.service.security.SecurityManager();
%>

<html:html lang="en">
<head>
<title><bean:message key="EditAllergies.title" /></title>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/allergies.css">
<style type="text/css">
	.ajax-loader {
	background: url(../images/ui-anim_basic_16x16.gif) center right no-repeat;
}
</style>
<script type="text/javascript">

function submitSearchForm() {
	$("#searchStringButton").click();
	return false;
}

$(document).ready( function() {

	$.fn.bindActionEvents = function() {

		//--> unbind first to avoid multiple binds.
		$(".deleteAllergyLink").unbind("click");
		$(".modifyAllergyLink").unbind("click");
		$("#searchResultsContainer a").unbind("click");
		$(".DivContentSectionHead a img").unbind("click");

		//--> action for selecting from search results.
		$("#searchResultsContainer div[id $= '_content'] a").bind("click", function(event){
			event.preventDefault();
			// override the old addReaction.do with the new addReaction2.do
			var path = "${ pageContext.servletContext.contextPath }/oscarRx/addReaction2.do"
			var param = this.href.split("?")[1];
			
			sendSearchRequest(path, param, "#addAllergyDialogue");
			$("#searchResultsContainer").html("");
		});
		
		//--> delete allergy.
		$(".deleteAllergyLink").bind("click", function(event){
			var ids = this.id.split("_");
			var action = ids[0].split(":")[1];
			var param = ids[1].trim();
			var allergyId = param.split("&")[0];
			allergyId = allergyId.split("=")[1].trim()
			$("#allergy_" + allergyId).addClass("highLightRow");

			var path = "${ pageContext.servletContext.contextPath }/oscarRx/deleteAllergy2.do";

			if( confirm(action+" this Allergy?") ) {
				sendSearchRequest(path, param, ".Step1Text");
			}
		});
		
		//--> modify allergy.
		$(".modifyAllergyLink").bind("click", function(event){
			var ids = this.id.split("_");
			var param = ids[1].trim();
			sendSearchRequest("${ pageContext.servletContext.contextPath }/oscarRx/addReaction2.do",
					param, "#addAllergyDialogue");
		});

		//--> Toggle search results listing.
	    $.fn.toggleSection = function(typecode) {
	    	let imgsrc = document.getElementById(typecode+"_img").src;
	    	if(imgsrc.indexOf('expander') !== -1) {
	    		document.getElementById(typecode+"_img").src='../images/collapser.png';
	    		Effect.BlindDown(document.getElementById(typecode+"_content"), {duration: 0.1 });
	    	} else {
	    		document.getElementById(typecode+"_img").src='../images/expander.png';
	    		Effect.BlindUp(document.getElementById(typecode+"_content"), {duration: 0.1 });
	    	}

	    }

		//--> Toggle search results listing.
		$(".DivContentSectionHead a img").bind("click", function(event){
			event.preventDefault();
			var typecode = this.id.split("_")[0];
			var imgsrc = document.getElementById(typecode+"_img").src;

	    	if(imgsrc.indexOf('expander') !== -1) {
	    		document.getElementById(typecode+"_img").src='../images/collapser.png';
	    		$("#"+typecode+"_content").show();
	    	} else {
	    		document.getElementById(typecode+"_img").src='../images/expander.png';
	    		$("#"+typecode+"_content").hide();
	    	}
		})
	
	} //--> end bind events function.

	//--> set default checkboxes on load
	// $.fn.setDefaults = function() {
	// 	// default set Drug Classes checked.
	// 	document.forms.searchAllergy2.type4.checked = true;
	// }
	
	//--> Send allergy search to server
	$("#searchStringButton").click( function(){

		if( isEmpty() ) {
			$(".highLightButton").removeClass("highLightButton");
			var form = $("#searchAllergy2");
			var url = "${ pageContext.servletContext.contextPath }" + form.attr('action');
			var params = form.serializeArray();
			var json = {};		
			$.each(params, function() {
				json[this.name] = this.value || '';
			});
			json.submit = 'Search';
			// servlet looks for "jsonData" request parameter		
			param = "jsonData=" + JSON.stringify(json);

			// thinking action
			$('#searchString').addClass('ajax-loader');
			
			sendSearchRequest(url, param, "#searchResultsContainer"); 
		}
	});

	//--> focus Enter key on search button
	$("#searchString").keypress(function(e) {
		if(e.which === 13) {
			$("#searchStringButton").trigger('click');
			return false;
		}
	});


	//--> Toggle checkboxes all or none
	$("#typeSelectAll").change( function(){
		if(this.checked) {
			typeSelect();
			//$("label[for='" + this.id + "']").text("None");
		} else {
			typeClear();
			//$("label[for='" + this.id + "']").text("All");
		}
	});

	//--> Cancel add allergy dialogue 
	$("#cancelAddReactionButton").click(function(event){
		event.preventDefault();
		document.forms.RxAddAllergyForm.reactionDescription.value='';
		document.forms.RxAddAllergyForm.startDate.value='';
		document.forms.RxAddAllergyForm.ageOfOnset.value='';
		location.reload();
	})

	//--> Actions after allergy has been added.
	$("input[value='Add Allergy'], .ControlPushButton").click(function() {
		$(".ControlPushButton").removeClass("highLightButton");
	})

	$().bindActionEvents();
	// $().setDefaults();

}); //--> end document ready

//--> AJAX the data to the server.
function sendSearchRequest(path, param, target) {
	var iNKDA = document.forms.searchAllergy2.iNKDA.value;
	if (param.indexOf(paramNKDA)>=0) {
		var hasDrugAllergy = document.forms.searchAllergy2.hasDrugAllergy.value;
		if (hasDrugAllergy=="true") {
			alert("Active drug allergy exists!");
			return;
		}
		if (iNKDA>0) {
			var iAtoA = param.indexOf("allergyToArchive=");
			if (iAtoA>0) iAtoA = param.substring(iAtoA+"allergyToArchive=".length);
			if (iAtoA<0 || iNKDA>iAtoA) {
				alert("\"No Known Drug Allergies\" already exists!");
				return;
			}
		}
	} else if (param.indexOf("ID=0")<0 && iNKDA>0) {
		param += "&nkdaId="+iNKDA;
	}
	$.ajax({
		url: path,
		type: 'POST',
		data: param,
		dataType: 'html',
		success: function(data) {
			renderSearchResults(data, target);
			if (path.indexOf("deleteAllergy")>=0) location.reload();
		}
	});
}

//--> Render response html 
function renderSearchResults(html, id) {
	
	if( id instanceof Array ) {
		$.each(id, function(i, val){
			$(val).replaceWith( jQuery(val, html) );
		});			
	} else {			
		$(id).replaceWith( jQuery(id, html) );
	}
	
	$('.ajax-loader').removeClass('ajax-loader');		
	$().bindActionEvents();
}

//--> Check if search field is empty. 
function isEmpty(){
	if (document.forms.searchAllergy2.searchString.value.length == 0){
		alert("Search Field is Empty");
		document.forms.searchAllergy2.searchString.focus();
		return false;
	}
	return true;
}

function show_Search_Criteria(){
	var tbl_as = document.getElementById("advancedSearch");

	if (tbl_as.style.display == '') {
		tbl_as.style.display = 'none';
	}else{
		tbl_as.style.display = '';
	}
}

//--> Checkboxes for search allergy criteria
function typeSelect(){
	var frm = document.forms.searchAllergy2;

	frm.type1.checked = true;
	frm.type2.checked = true;
	frm.type3.checked = true;
	frm.type4.checked = true;
}

function typeClear(){
	var frm = document.forms.searchAllergy2;

	frm.type1.checked = false;
	frm.type2.checked = false;
	frm.type3.checked = false;
	frm.type4.checked = false;
}


function addCustomAllergy(){
	$(".highLightButton").removeClass("highLightButton");
	var name = document.getElementById('searchString').value;
	if(isEmpty() == true){
		name = name.toUpperCase();
		confirm("Adding custom allergy: " + name);
		sendSearchRequest("${ pageContext.servletContext.contextPath }/oscarRx/addReaction2.do",
				"ID=0&type=0&name="+name,"#addAllergyDialogue");
		$("input[value='Custom Allergy']").addClass("highLightButton");
	}
}

function addPenicillinAllergy(){
	$(".highLightButton").removeClass("highLightButton");
	var param = "ID=44452&name=PENICILLINS&type=10";
	
	sendSearchRequest("${ pageContext.servletContext.contextPath }/oscarRx/addReaction2.do",
			param, "#addAllergyDialogue");
	$("input[value='Penicillin']").addClass("highLightButton");
}

function addSulfonamideAllergy(){
	$(".highLightButton").removeClass("highLightButton");
	var param = "ID=44159&name=SULFONAMIDES&type=10";
	
	sendSearchRequest("${ pageContext.servletContext.contextPath }/oscarRx/addReaction2.do",
			param, "#addAllergyDialogue");
	$("input[value='Sulfa']").addClass("highLightButton");
}

var paramNKDA = "name=No Known Drug Allergies";

function addCustomNKDA(){
	$(".highLightButton").removeClass("highLightButton");
	sendSearchRequest("${ pageContext.servletContext.contextPath }/oscarRx/addReaction2.do",
			"ID=0&type=0&"+paramNKDA, "#addAllergyDialogue");
	$("input[value='NKDA']").addClass("highLightButton");
}

</script>

</head>
<bean:define id="patient" type="oscar.oscarRx.data.RxPatientData.Patient" name="Patient" />

<body>
<%=WebUtils.popErrorAndInfoMessagesAsHtml(session)%>

<table id="AutoNumber1">
<tr id="allergiesRowOne" >
	<td colspan="2">
		<jsp:include page="TopLinks.jsp">
			<jsp:param value="Allergies" name="title" />
			<jsp:param value="${ patient.surname }, ${ patient.firstName }" name="patientName" />
			<jsp:param value="${ patient.sex }" name="sex" />
			<jsp:param value="${ patient.age }" name="age" />
			<jsp:param value="${ patient.demographicNo }" name="demographicNo" />
			<jsp:param value="<%= roleName2$ %>" name="security" /> 
			<jsp:param value='<%= (String)session.getAttribute("demographicNo") %>' name="demographicNo" />
		</jsp:include>
	</td>
</tr>
<tr>
	<td id="allergiesColumnOneRowTwo" >
		<%@ include file="SideLinksEditFavorites2.jsp" %><!-- <td></td>Side Bar File --->
	</td>	
	<td id="allergiesColumnTwoRowTwo" ><!--Column Two Row Two-->
		<table>
		<tr class="DivCCBreadCrumbs" >
			<td>
				<a href="SearchDrug3.jsp" ><bean:message key="SearchDrug.title" /></a>
				&nbsp;&gt;&nbsp; 
				<b><bean:message key="EditAllergies.title" /></b>
			</td>
		</tr>
		<!----Start new rows here-->

		<tr class="DivContentSectionHead" >
			<td>
				<bean:message key="EditAllergies.section1Title" />
			</td>
		</tr>
		<tr id="patientDataRow">
			<td>
				<table>
				<tr>
					<td>
						<b><bean:message key="SearchDrug.nameText" /></b> 
						<jsp:getProperty name="patient" property="surname" />, 
						<jsp:getProperty name="patient" property="firstName" /> 
					</td>
					<td>&nbsp;</td>
					<td><b>Age:</b> <jsp:getProperty name="patient" property="age" /></td>
				</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="DivContentSectionHead" >
				<bean:message key="EditAllergies.section2Title" />
				<span class="view_menu">View:
<%

String demoNo=request.getParameter("demographicNo");

if(demoNo==null) {
	demoNo = (String)session.getAttribute("demographicNo");
}else{
	session.setAttribute("demographicNo", demoNo);
}

if(demoNo == null || demoNo.equals("null")) {
	demoNo = String.valueOf(bean.getDemographicNo());
}

String strView=request.getParameter("view");
if (strView==null) strView = "Active";

String[] navArray={"Active","All","Inactive"};

int i=0;
for(i=0;i<navArray.length;i++)
{
	if( strView.equals(navArray[i]) ){
		out.print(" <span class='view_selected'>"+navArray[i]+"</span>");
	}else{
		out.print("<span class='view_menu'><a href='ShowAllergies2.jsp?demographicNo="+demoNo+"&view="+navArray[i]+"'>");
		out.print(navArray[i]);
		out.print("</a></span>");
	}
}
//1 mild 2 moderate 3 severe 4 unknown

String[] ColourCodesArray=new String[6];
ColourCodesArray[1]="#F5F5F5"; // Mild Was set to yellow (#FFFF33) SJHH requested not to flag mild
ColourCodesArray[2]="#FF6600"; // Moderate
ColourCodesArray[3]="#CC0000"; // Severe
ColourCodesArray[4]="#E0E0E0"; // unknown
ColourCodesArray[5]="#FFFFFF"; // no reaction

							out.print(" <span class='view_selected'>"+navArray[i]+"</span>");


						}else{
							out.print("<span class='view_menu'><a href='" + request.getContextPath() + "/oscarRx/showAllergy.do?demographicNo="+demoNo+"&view="+navArray[i]+"'>");
								out.print(navArray[i]);
							out.print("</a></span>");
						}
					 }
					 //1 mild 2 moderate 3 severe 4 unknown

					 String[] ColourCodesArray=new String[5];
					 ColourCodesArray[1]="#F5F5F5"; // Mild Was set to yellow (#FFFF33) SJHH requested not to flag mild
					 ColourCodesArray[2]="#FF6600"; // Moderate
					 ColourCodesArray[3]="#CC0000"; // Severe
					 ColourCodesArray[4]="#E0E0E0"; // unknown

					 String allergy_colour_codes = "<table class='allergy_legend' cellspacing='0'><tr><td><b>Legend:</b></td> <td > <table class='colour_codes' bgcolor='"+ColourCodesArray[1]+"'><td> </td></table></td> <td >Mild</td> <td > <table class='colour_codes' bgcolor='"+ColourCodesArray[2]+"'><td> </td></table></td> <td >Moderate</td><td > <table class='colour_codes' bgcolor='"+ColourCodesArray[3]+"'><td> </td></table></td> <td >Severe</td> </tr></table>";
				%>

				</span>

			</td>
		</tr>
		</table>
		<tr>
			<td>
				<table border="0">
				<tr>
					<td class="Step1Text">
						<%=allergy_colour_codes%>
					</td>
					</tr>
				</table>
				</td>
			</tr>
			
			<tr id="addAllergyInterface" >
			
				<td>
					<form action="/oscarRx/searchAllergy2.do" id="searchAllergy2"  >
					<table>
					<tr><th>Add an Allergy</th></tr>
						<tr id="allergyQuickButtonRow" >
                            <td>                           	
                        		<input type=button class="ControlPushButton" onclick="javascript:addCustomNKDA();" value="NKDA" />                              
                               <input type=button class="ControlPushButton" onclick="javascript:addPenicillinAllergy();" value="Penicillin" />
                               <input type=button class="ControlPushButton" onclick="javascript:addSulfonamideAllergy();" value="Sulfa" />
	                            <input type="text" name="searchString" value="${ searchString }" size="16" id="searchString" maxlength="16" autofocus="autofocus" />
	                            <input type="button" value="Search Drugs" id="searchStringButton" class="ControlPushButton" />
	                            OR
	                            <input type=button class="ControlPushButton" onclick="addCustomAllergy();" value="Custom Allergy" />

                            </td>
						</tr>

						<tr>
							<td><b>Status</b></td>
							<td><b>Entry Date</b></td>
							<td><b>Description</b></td>
							<td><b>Allergy Type</b></td>
							
							<td><b>Non-Drug</b></td>
							<td><b>Severity</b></td>
							<td><b>Onset of Reaction</b></td>
							<td><b>Reaction</b></td>
							<td><b>Start Date</b></td>
							<td><b>Life Stage</b></td>
							<td><b>Age Of Onset</b></td>
							<td><b><img src="../images/notes.gif" border="0" width="10px" height="12px" alt="Annotation"></b></td>
							<td><b>Action</b></td>
						</tr>

					</table>
					</form>
				</td>
			</tr>
			
			<tr>
				<td id="searchResultsContainer" ></td>
			</tr>

					<table>
					<tr>
						<th>Add an Allergy</th>
					</tr>
					<tr id="allergyQuickButtonRow" >
						<td>	
							<input type=button class="ControlPushButton" onclick="javascript:addCustomNKDA();" value="NKDA" /> 
							<input type=button class="ControlPushButton" onclick="javascript:addPenicillinAllergy();" value="Penicillin" />
							<input type=button class="ControlPushButton" onclick="javascript:addSulfonamideAllergy();" value="Sulfa" /> 
						</td>
					</tr>
					<tr>
						<td id="addAllergyDialogue"></td>
					</tr>
					<tr id="allergySearchCriteraRow">
						<td>
							<div id="allergySearchSelectors">
								<input type="checkbox" name="type4" id="type4" ${ type4 ? 'checked' : '' } /> 
								<label for="type4" >Drug Classes</label>
								<input type="checkbox" name="type3" id="type3" ${ type3 ? 'checked' : '' } /> 
								<label for="type3" >Ingredients</label>
								<input type="checkbox" name="type2" id="type2" ${ type2 ? 'checked' : '' } /> 
								<label for="type2" >Generic Names</label>
								<input type="checkbox" name="type1" id="type1" ${ type1 ? 'checked' : '' } /> 
								<label for="type1" >Brand Names</label>
								<input type="checkbox" name="typeSelectAll" id="typeSelectAll" /> 
								<label for="typeSelectAll" >All</label>
							</div>
							<input type="text" name="searchString" value="${ searchString }" size="16" id="searchString" maxlength="16" />	
							<input type="button" value="Search" id="searchStringButton" class="ControlPushButton" />
							OR
							<input type=button class="ControlPushButton" onclick="javascript:addCustomAllergy();" value="Custom Allergy" />
						</td>
					</tr>
					</table>
				</form>
			</td>				
		</tr>
		<tr>
			<td id="searchResultsContainer" ></td>
		</tr>
		</table>
	</td>
</tr>

<tr class="lastRow">
	<td colspan="2"></td>
</tr>
</table>

</body>
</html:html>
