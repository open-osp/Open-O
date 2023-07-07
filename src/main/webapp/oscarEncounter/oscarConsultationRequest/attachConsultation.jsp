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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html >
<html>
<head>
<title><bean:message key="oscarEncounter.oscarConsultationRequest.AttachDocPopup.title" /></title>

<script type="text/javascript">
//<!--   
function toggleSelectAll(element, startClassName) {
	jQuery("[class^='"+ startClassName +"']:not(input[disabled='disabled'])").prop('checked', jQuery(element).prop("checked"));
}
//-->
</script>

</head>
<body>
<form id="attachDocumentsForm">
	<table id="attachDocumentsPanel" >
		<tr>
			<td><h2>Documents</h2></td>
		</tr>
		<tr>
			<td>
			<ul id="documentList" style="list-style-type: none;padding:0px;">
            <li class="selectAllHeading">
                 <input id="selectAllDocuments" type="checkbox" onclick="toggleSelectAll(this, 'document_');" value="document_check" title="Select/un-select all documents."/>
                 <label for="selectAllDocuments">Select all</label>  
            </li>
             <c:forEach items="${ allDocuments }" var="document">
             	<li class="doc">
             		<input class="document_check" type="checkbox" name="docNo" id="docNo${document.docId}" value="${document.docId}" title="${ document.description }" />
             		<label for="docNo${document.docId}"><c:out value="${ document.description } ${ document.observationDate }" /></label>
             	</li>
             </c:forEach>
            </ul>
            
		     </td>
		        </tr>
		        
		        <c:if test="${not empty allLabs }" >
		            <tr>
						<td><h2>Labs</h2></td>
					</tr>
	            <tr>
				<td>
					<ul id="labList" style="list-style-type: none;padding:0px;">					
					    <li class="selectAllHeading" >
			                 <input id="selectAllLabs" type="checkbox" onclick="toggleSelectAll(this, 'lab_');" value="lab_check" title="Select/un-select all documents."/>
			                 <label for="selectAllLabs">Select all</label>  
			            </li>
						 <c:forEach items="${ allLabs }" var="lab">
		                	<li class="lab">
		                		<input class="lab_check" type="checkbox" name="labNo" id="labNo${ lab.segmentID }" value="${lab.segmentID}" title="${ lab.label }" />
								<label for="labNo${lab.segmentID}" title="${ lab.label }" ><c:out value="${fn:substring(lab.label, 0, 30)} ${ lab.dateObj }" /></label>
		                	</li>
		                </c:forEach>
	            	</ul> 
	            </td>
       		</tr>
       		</c:if>
	</table>
</form>	
</body>
</html>
