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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  String user_no = (String) session.getAttribute("user");
  int  nItems=0;
      String strLimit1="0";
    String strLimit2="5";
     if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
%>
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>
<%@ page import="java.math.*,java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="/errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ReportProvider" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%@ page import="org.oscarehr.common.model.Billing" %>
<%@ page import="org.oscarehr.common.dao.BillingDao" %>
<%@ page import="oscar.util.ConversionUtils" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao" %>
<%@ page import="org.oscarehr.common.model.Appointment" %>
<%@ page import="org.oscarehr.common.dao.OscarAppointmentDao" %>


<%
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
	OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
%>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
%>
<%
  	int flag = 0, rowCount=0;
  String reportAction=request.getParameter("reportAction")==null?"":request.getParameter("reportAction");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>Billing Report</title>

<script language="JavaScript">
<!--

function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function refresh() {
      history.go(0);

}
//-->
</script>


</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="10">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#FFFFFF">
		<div align="right"><a href=#
			onClick="popupPage(700,720,'../oscarReport/manageProvider.jsp?action=billingreport')"><font
			face="Arial, Helvetica, sans-serif" size="1">Manage Provider
		List </font></a></div>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Billing</font></font></b></font></p>
		</td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<thead>
        <tr style="font-family:Verdana, Arial, Helvetica, sans-serif;
                    font-size: 11px; font-weight: bold; color: #6f6f6f">
            <td style="width: 5%;"></td>
            <td style="width: 45%; padding-left: 8px;">
                <span>Status Filters</span>
            </td>
            <td style="width: 25%;">
                <span>Service Date-Range</span>
            </td>
            <td style="width: 25%;">
                <span>Select Provider</span>
            </td>
        </tr>
    </thead>
    <form name="serviceform" method="get" action="billingReportControl.jsp">
	<tr>
		<td style="width: 5%;"></td>
            <%--<td width="25%" align="center"><font size="2" color="#333333"
			face="Verdana, Arial, Helvetica, sans-serif">--%>
            <td width="45%" style="font-family:Verdana, Arial, Helvetica, sans-serif; font-size: 13px;">
                <input
			type="radio" name="reportAction" value="unbilled"
			<%=reportAction.equals("unbilled")?"checked":""%>> Unbilled <input
			type="radio" name="reportAction" value="billed"
			<%=reportAction.equals("billed")?"checked":""%>> Billed <!--<input type="radio" name="reportAction" value="unsettled"  <%=reportAction.equals("unsettled")?"checked":""%>>
        Unsettled
        <input type="radio" name="reportAction" value="billob"  <%=reportAction.equals("billob")?"checked":""%>>
        OB
          <input type="radio" name="reportAction" value="flu" <%=reportAction.equals("flu")?"checked":""%>>
        FLU</font></td>-->
		<label style="display: inline-flex; align-items: center; margin-left: 10px;">
                    <input type="checkbox" name="filter_noshow" value="true"
                        <%= request.getParameter("filter_noshow") != null ? "checked" : "" %> > No-Show
                </label>

                <label style="padding-left: 10px; display: inline-flex; align-items: center;">
                    <input type="checkbox" name="filter_cancelled" value="true"
                        <%= request.getParameter("filter_cancelled") != null ? "checked" : "" %> > Cancelled
                </label>
            </td>

            <td width="25%">
		<div ><input type="text" name="xml_vdate"
		value="<%=xml_vdate%>"> <font size="1"
			face="Arial, Helvetica, sans-serif"><a href="#"
		onClick="openBrWindow('billingCalendarPopup.jsp?type=&returnItem=xml_vdate&returnForm=serviceform&year=<%=curYear%>&month=<%=curMonth%>','','width=300,height=300')">Begin:</a></font>
                </div>
            </td>
            <td width="25%">
                <div> <select name="providerview">
			<% String proFirst="";
           String proLast="";
           String proOHIP="";
           String specialty_code;
String billinggroup_no;
           int Count = 0;
           for(Object[] result:reportProviderDao.search_reportprovider("billingreport")) {
				ReportProvider rp = (ReportProvider)result[0];
				Provider p = (Provider)result[1];

				 proFirst = p.getFirstName();
				 proLast = p.getLastName();
				 proOHIP = p.getProviderNo();

%>
			<option value="<%=proOHIP%>"
				<%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<%
      }

  %>
		</select></div>
		</td>
		</tr>
	<tr>
		<td width="5%">
		</td>
			<td width="45%">

		</td>
		<td width="25%"><input type="text" name="xml_appointment_date"
			value="<%=xml_appointment_date%>"> <font size="1"
			face="Arial, Helvetica, sans-serif"><a href="#"
			onClick="openBrWindow('billingCalendarPopup.jsp?type=&returnItem=xml_appointment_date&returnForm=serviceform&year=<%=curYear%>&month=<%=curMonth%>','','width=300,height=300')">End:</a></font>
		</td>

		<td width="25%">
			 <font color="#333333" size="2"
			face="Verdana, Arial, Helvetica, sans-serif">
                    <input
			type="hidden" name="verCode" value="V03"> <input
                        type="submit" name="Submit" value="Create Report"> </font>

                    <input type='button' name='print' value='Print' onClick='window.print()' style="margin-left: 10px">
		</td>
	</tr>
	</form>
</table>
<% if (reportAction.compareTo("") == 0 || reportAction == null){%>

<p>&nbsp;</p>
<% } else {
if (reportAction.compareTo("unbilled") == 0) {
%>
<%@ include file="billingReport_unbilled.jspf"%>
<%
} else {
%>
<%
if (reportAction.compareTo("billed") == 0) {
%>
<%@ include file="billingReport_billed.jspf"%>
<%
}else{
if (reportAction.compareTo("unsettled") == 0) {
%>
<%@ include file="billingReport_unsettled.jspf"%>
<%}else{
if (reportAction.compareTo("billob") == 0) {
%>
<%@ include file="billingReport_billob.jspf"%>
<%	}else{
		if (reportAction.compareTo("flu") == 0) {
%>
<%@ include file="billingReport_flu.jspf"%>
<%

		}
  	     }
       }
       }}
%>


<%
  %>

</body>
</html>
