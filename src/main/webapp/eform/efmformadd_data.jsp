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

<%@ page import="oscar.eform.data.*"%>
<%@ page import="org.oscarehr.managers.FaxManager, org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
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

<c:if test="${ not empty reqestScope.page_errors }">
	<script type='text/javascript'>
		function hideDiv() {
		    if (document.getElementById) { // DOM3 = IE5, NS6
		        document.getElementById('hideshow').style.display = 'none';
		    }
		    else {
		        if (document.layers) { // Netscape 4
		            document.hideshow.display = 'none';
		        }
		        else { // IE 4
		            document.all.hideshow.style.display = 'none';
		        }
		    }
		}
	</script>
	
	<div id="hideshow" style="position: relative; z-index: 999;">
		<a href="javascript:hideDiv()">Hide Errors</a> 
		<span style="font-size: 10px; font-color: darkred;"> <html:errors /> </span>
	</div>
</c:if>

<%
  String provider_no = (String) session.getValue("user");
  String demographic_no = request.getParameter("demographic_no");
  String appointment_no = request.getParameter("appointment");
  String fid = request.getParameter("fid");
  String eform_link = request.getParameter("eform_link");
  String source = request.getParameter("source");
  

  EForm thisEForm = null;
  if (fid == null || demographic_no == null) 
  {
      //if the info is in the request attribute
      thisEForm = (EForm) request.getAttribute("curform");
  } 
  else 
  {
      //if the info is in the request parameter
      thisEForm = new EForm(fid, demographic_no);
      thisEForm.setProviderNo(provider_no);  //needs provider for the action
  }

  if (appointment_no!=null) 
  {
	  thisEForm.setAppointmentNo(appointment_no);
  }
  
  if (eform_link!=null) 
  {
	  thisEForm.setEformLink(eform_link);
  }
  
  thisEForm.setContextPath(request.getContextPath());
  thisEForm.setupInputFields();
  thisEForm.setImagePath();
  thisEForm.setDatabaseAPs();
  thisEForm.setOscarOPEN(request.getRequestURI());
  thisEForm.setAction();
  thisEForm.setSource(source);

  out.print(thisEForm.getFormHtml());
%>


<c:if test="${ sessionScope.useIframeResizing }" >
	<script type="text/javascript" src="${ pageContext.servletContext.contextPath }/library/pym.js"></script>
	<script type="text/javascript">
	    var pymChild = new pym.Child({ polling: 500 });
	</script>
</c:if>
