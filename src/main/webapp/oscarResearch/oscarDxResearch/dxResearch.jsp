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

<%@ page import="oscar.oscarResearch.oscarDxResearch.util.dxResearchCodingSystem"%>
<%@ page import="com.quatro.service.security.SecurityManager" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_dxresearch" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_dxresearch");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
  
	boolean disable;
	SecurityManager sm = new SecurityManager();
	
	if(sm.hasWriteAccess("_dx.code",roleName$)) {
		disable=false;
	}else{
		disable=true;
	}
	boolean showQuicklist=false;
	
	if(sm.hasWriteAccess("_dx.quicklist",roleName$)) {
		showQuicklist=true;
	}
	
    String user_no = (String) session.getAttribute("user");
    String color ="";
    int Count=0; 
    
    pageContext.setAttribute("showQuicklist", showQuicklist);
    pageContext.setAttribute("disable", disable);
%>

<!DOCTYPE html>
<html:html lang="en">
<head>
<title><bean:message key="oscarResearch.oscarDxResearch.dxResearch.title" /></title>
	
	<script type="text/javascript" src="${pageContext.servletContext.contextPath}/js/global.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath}/share/javascript/prototype.js"></script>

 	<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/jquery/jquery-1.12.0.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/jquery/jquery-ui-1.12.1.min.js"></script> 
	<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${pageContext.servletContext.contextPath}/library/oscar-modal-dialog.js"></script> 
	
	<link rel="stylesheet" type="text/css" media="all" href="${pageContext.servletContext.contextPath}/library/jquery/jquery-ui-1.12.1.min.css" />
	<link rel="stylesheet" type="text/css" media="all" href="${pageContext.servletContext.contextPath}/library/bootstrap/3.0.0/css/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" media="all" href="${pageContext.servletContext.contextPath}/library/bootstrap/3.0.0/css/bootstrap-theme.min.css" />
	<link rel="stylesheet" type="text/css" href="dxResearch.css">
	
<script type="text/javascript">
//<!--

jQuery.noConflict();

function setfocus() {
	document.forms[0].xml_research1.focus();
	document.forms[0].xml_research1.select();
}

var remote=null;

function rs(n,u,w,h,x) {
	args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
	remote=window.open(u,n,args);
	if (remote != null) {
		if (remote.opener == null)
			remote.opener = self;
	}
	if (x == 1) { return remote; }
}

function popPage(url) {
	awnd=rs('',url ,400,200,1);
	awnd.focus();
}

var awnd=null;

function ResearchScriptAttach() {
	var t0 = escape(document.forms[0].xml_research1.value);
	var t1 = escape(document.forms[0].xml_research2.value);
	var t2 = escape(document.forms[0].xml_research3.value);
	var t3 = escape(document.forms[0].xml_research4.value);
	var t4 = escape(document.forms[0].xml_research5.value);
        var codeType = document.forms[0].selectedCodingSystem.value;
        var demographicNo = escape(document.forms[0].demographicNo.value);
        
	awnd=rs('att','dxResearchCodeSearch.do?codeType=' + codeType + '&xml_research1='+t0 + '&xml_research2=' + t1 + '&xml_research3=' + t2 + '&xml_research4=' + t3 + '&xml_research5=' + t4 +'&demographicNo=' + demographicNo,600,600,1);
	awnd.focus();
}

function submitform(target,sysCode){	
        document.forms[0].forward.value = target;
        
        if( sysCode != '' )
            document.forms[0].selectedCodingSystem.value = sysCode;
            
	document.forms[0].submit()
}

function set(target) {
     document.forms[0].forward.value=target;
}


function openNewPage(vheight,vwidth,varpage) { 
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "<bean:message key="global.oscarComm"/>", windowprops);
  popup.focus();
}

document.onkeypress = processKey;

function processKey(e) {
	if (e==null) {
		e = window.event;
	} else if (e.keyCode==13) {
		ResearchScriptAttach();
	}
}

function showdatebox(x) {
    document.getElementById("startdatenew"+x).show();
    document.getElementById("startdate1st"+x).hide();
}

function update_date(did, demoNo, provNo) {
    var startdate = document.getElementById("startdatenew"+did).value;
    window.location.href = "dxResearchUpdate.do?startdate="+startdate+"&did="+did+"&demographicNo="+demoNo+"&providerNo="+provNo;
}

//-->
</script>

</head>

<body onLoad="setfocus();">
<div class="wrapper">

	<div id="page-header">	
		<table id="oscarDxHeader">
			<tr>
				<td id="oscarDxHeaderLeftColumn"><h1><bean:message key="global.disease" /></h1></td>

				<td id="oscarDxHeaderCenterColumn">				
					<oscar:nameage demographicNo="${ demographicNo }" />					
				</td>
				<td id="oscarDxHeaderRightColumn" align=right>
					<span class="HelpAboutLogout"> 
						<a style="font-size: 10px; font-style: normal;" href="${ ctx }oscarEncounter/About.jsp" target="_new">About</a>
						<a style="font-size: 10px; font-style: normal;" target="_blank"
									href="http://www.oscarmanual.org/search?SearchableText=&Title=Chart+Interface&portal_type%3Alist=Document">Help</a>		
					</span>
				</td>
			</tr>
		</table>
	</div>

<table>
	<tr>
		<td><html:form action="/oscarResearch/oscarDxResearch/dxResearch.do" >
			<table>
				<html:errors />
				<tr>
					<td id="codeSelectorTable">

					<table>
						<tr>
							<td>
							<div class="input-group">
								<span class="input-group-addon" id="basic-addon3">
									<bean:message key="oscarResearch.oscarDxResearch.codingSystem" />
								</span> 
								
								<html:select styleClass="form-control" property="selectedCodingSystem"  disabled="<%=disable%>">
									<logic:iterate id="codingSys" name="codingSystem" property="codingSystems">
										<option value="<bean:write name="codingSys"/>"><bean:write name="codingSys" /></option>
									</logic:iterate>
								</html:select>
							</div>						
							</td>
						</tr>
						<tr>
							<td><html:text styleClass="form-control" property="xml_research1" disabled="<%=disable%>" /> 
							<input type="hidden" name="demographicNo" value="<bean:write name="demographicNo"/>"> 
							<input type="hidden" name="providerNo" value="<bean:write name="providerNo"/>"></td>
						</tr>
						<tr>
							<td><html:text styleClass="form-control" property="xml_research2" disabled="<%=disable%>"/></td>
						</tr>
						<tr>
							<td><html:text styleClass="form-control" property="xml_research3" disabled="<%=disable%>"/></td>
						</tr>
						<tr>
							<td><html:text styleClass="form-control" property="xml_research4" disabled="<%=disable%>"/></td>
						</tr>
						<tr>
							<td><html:text styleClass="form-control" property="xml_research5" disabled="<%=disable%>"/></td>
						</tr>
						<tr>
							<td>
							<input type="hidden" name="forward" value="none" /> 
                               <%if(!disable) { %>                             
                               <input type="button" name="codeSearch" class="btn btn-primary"
								value="<bean:message key="oscarResearch.oscarDxResearch.btnCodeSearch"/>"
								onClick="javascript: ResearchScriptAttach();") > 
                                                            
                                <input type="button" name="codeAdd" class="btn btn-primary"
								value="<bean:message key="ADD"/>"
								onClick="javascript: submitform('','');">
								
								<% } else { %>
								
								 <input type="button" name="button" class="btn btn-primary"
								value="<bean:message key="oscarResearch.oscarDxResearch.btnCodeSearch"/>"
								onClick="javascript: ResearchScriptAttach();")  disabled="<%=disable%>"> 
                                                            
                                <input type="button" name="button" class="btn btn-primary"
								value="<bean:message key="ADD"/>"
								onClick="javascript: submitform('','');" disabled="<%=disable%>">
								<% } %>
								</td>
						</tr>
						
					<%-- DX QUICK LIST - returns a table --%>
						<logic:equal name="showQuicklist" value="true" scope="page">
						<tr>
							<td>
							<jsp:include page="dxQuickList.jsp" >
								<jsp:param value="false" name="disable"/>
								<jsp:param value="${ param.quickList }" name="quickList" />
								<jsp:param value="${ demographicNo }" name="demographicNo"/>
								<jsp:param value="${ providerNo }" name="providerNo"/>
							</jsp:include>
							</td>
						</tr>
						</logic:equal>
					<%-- DX QUICK LIST --%>
						
					</table>
	
					</td>
					<td id="displayDxCodeTable">

					<table>
						<tr>
							<th>System</th>
							<th class="heading" ><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgCode" /></th>
							<th class="heading" ><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgDiagnosis" /></th>
							<th class="heading" ><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgFirstVisit" /></th>
							<th class="heading" ><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgLastVisit" /></th>
							<% if(!disable){ %>
							<th class="heading" ><bean:message key="oscarResearch.oscarDxResearch.dxResearch.msgAction" /></th>
							<%} %>					
						</tr>
						<logic:iterate id="diagnotics" name="allDiagnostics" property="dxResearchBeanVector" indexId="ctr">

							<logic:equal name="diagnotics" property="status" value="A">
								<tr>
								<td>
									<bean:write name="diagnotics" property="type" />
								</td>
								<td class="notResolved"> <bean:write name="diagnotics" property="dxSearchCode" /></td>
									<td class="notResolved"><bean:write name="diagnotics" property="description" /></td>
									<td class="notResolved">
                                                                            <a href="#" onclick="showdatebox(<bean:write name="diagnotics" property="dxResearchNo" />);">
                                                                                <div id="startdate1st<bean:write name="diagnotics" property="dxResearchNo" />"><bean:write name="diagnotics" property="start_date" /></div>
                                                                                <input class="form-control" id="startdatenew<bean:write name="diagnotics" property="dxResearchNo" />" type="text" name="start_date" size="8" value="<bean:write name="diagnotics" property="start_date" />" style="display:none" />
                                                                            </a>
                                                                        </td>
									<td class="notResolved">
                                                                                <bean:write name="diagnotics" property="end_date" />
                                                                        </td>
									<% if(!disable){ %>
									<td class="notResolved">
									<a href='dxResearchUpdate.do?status=C&did=<bean:write name="diagnotics" property="dxResearchNo" />&demographicNo=<bean:write name="demographicNo" />&providerNo=<bean:write name="providerNo" />'><bean:message
										key="oscarResearch.oscarDxResearch.dxResearch.btnResolve" /></a> 
									<a href='dxResearchUpdate.do?status=D&did=<bean:write name="diagnotics" property="dxResearchNo" />&demographicNo=<bean:write name="demographicNo" />&providerNo=<bean:write name="providerNo" />' onClick="javascript: return confirm('Are you sure you would like to delete: <bean:write name="diagnotics" property="description" /> ?')"><bean:message
										key="oscarResearch.oscarDxResearch.dxResearch.btnDelete" /></a> 
									<a href='#' onclick="update_date(<bean:write name="diagnotics" property="dxResearchNo" />,<bean:write name="demographicNo" />,<bean:write name="providerNo" />);"><bean:message
										key="oscarResearch.oscarDxResearch.dxResearch.btnUpdate" /></a>
                                    </td>
                                     <%} %>                                   
								</tr>
							</logic:equal>
							<logic:equal name="diagnotics" property="status" value="C">
								<tr>
									<td> <bean:write name="diagnotics" property="dxSearchCode" /></td>
                                    <td><bean:write name="diagnotics" property="description" /></td>
									<td><bean:write name="diagnotics" property="start_date" /></td>
									<td><bean:write name="diagnotics" property="end_date" /></td>
									<% if(!disable){ %>
									<td><bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnResolve" /> |
									<a href='dxResearchUpdate.do?status=D&did=<bean:write name="diagnotics" property="dxResearchNo" />&demographicNo=<bean:write name="demographicNo" />&providerNo=<bean:write name="providerNo" />'  onClick="javascript: return confirm('Are you sure you would like to delete this?')"><bean:message key="oscarResearch.oscarDxResearch.dxResearch.btnDelete" /></a></td>
									<%} %>
								</tr>
							</logic:equal>
						</logic:iterate>
					</table>

					</td>
				</tr>
			</table>			
			</html:form>
		</td>
	</tr>
</table>
</div>
</body>
</html:html>

