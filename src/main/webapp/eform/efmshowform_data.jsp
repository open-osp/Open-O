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
<%@ page import="java.sql.*, oscar.eform.data.*, org.oscarehr.managers.FaxManager, org.oscarehr.util.LoggedInInfo"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<%
  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
  pageContext.setAttribute("faxActive", FaxManager.isEnabled(loggedInInfo));
%>

<%--
	Addition of a floating global toolbar specifically for activation of the 
	Fax and eDocument functions.
--%>
<c:if test="${ faxActive }">
	<script type="text/javascript" src="${ pageContext.servletContext.contextPath }/eform/eformFloatingToolbar/eform_floating_toolbar.js" ></script>
</c:if>

<%
	String id = request.getParameter("fid");
	String messageOnFailure = "No eform or appointment is available";
  if (id == null) {  // form exists in patient
      id = request.getParameter("fdid");
      String appointmentNo = request.getParameter("appointment");
      String eformLink = request.getParameter("eform_link");

      EForm eForm = new EForm(id);
      eForm.setContextPath(request.getContextPath());
      eForm.setOscarOPEN(request.getRequestURI());
      eForm.setFdid(id);
      
      if ( appointmentNo != null ) eForm.setAppointmentNo(appointmentNo);
      if ( eformLink != null ) eForm.setEformLink(eformLink);

      String parentAjaxId = request.getParameter("parentAjaxId");
      if( parentAjaxId != null ) eForm.setAction(parentAjaxId);
      out.print(eForm.getFormHtml());
  } else {  //if form is viewed from admin screen
      EForm eForm = new EForm(id, "-1"); //form cannot be submitted, demographic_no "-1" indicate this specialty
      eForm.setContextPath(request.getContextPath());
      eForm.setupInputFields();
      eForm.setOscarOPEN(request.getRequestURI());
      eForm.setImagePath();
      eForm.setFdid("");
      out.print(eForm.getFormHtml());
  }
%>

<c:if test="${ sessionScope.useIframeResizing }" >
	<script type="text/javascript" src="${ pageContext.servletContext.contextPath }/library/pym.js"></script>
	<script type="text/javascript">
	    var pymChild = new pym.Child({ polling: 500 });
	</script>
</c:if>
